import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import streamprint.*;
import sun.java2d.loops.FillRect;


public class InputPanel extends Panel implements ActionListener,ItemListener{
    
  private VisualByteMap visualByteMap;
  private DotterPanel dotterPanel;
  private ScalePanel scalePanel;
  private SeqPairPanel seqPairPanel;
  private InfoPanel infoPanel;
  /*modification by Olivier*/
  private SeqInputDialog dialog;
  private Button buttonPrint = new Button("print");
  /*end modification*/
  
  private Hashtable sequences=new Hashtable();
  private Button inputButton=new Button("input"); 
  private Choice horizChoice=new Choice();
  private Choice vertChoice=new Choice();
  private Choice methodChoice=new Choice();
  private Button computeButton=new Button("compute");
  private Choice widthChoice=new Choice();
  private Choice zoomChoice=new Choice();
  private ComparisonMatrix comparisonMatrix;

  private static final String[] matrixName={"Identity",
					    "Blosum100","Blosum65","Blosum62","Blosum45","Blosum30",
					    "PAM30","PAM80","PAM120","PAM160","PAM200","PAM220","PAM250",
					    "Gonnet"};
  private static final String X_IDENTITY="Identity";
  
  private int seqCounter=1;
  
  public InputPanel(VisualByteMap vbm,DotterPanel dp,ScalePanel sp,SeqPairPanel spp,InfoPanel ip){
    visualByteMap=vbm;
    dotterPanel=dp;
    scalePanel=sp;
    seqPairPanel=spp;
    infoPanel=ip;

    //setLayout(new GridLayout(1,6));
    setLayout(new FlowLayout());
    /*modification by Olivier*/
    this.dotterPanel.getImagePanel().setInfoPanel(this.infoPanel);
    add(this.buttonPrint);
    this.buttonPrint.setEnabled(false);
    this.buttonPrint.addActionListener(this);
    /*end modification*/
    add(inputButton);
    inputButton.addActionListener(this);
    //updateSequenceChoice(horizChoice);
    horizChoice.add("#horizontal#");
    add(horizChoice);
    horizChoice.setEnabled(false);
    //updateSequenceChoice(vertChoice);
    vertChoice.add("# vertical #");
    add(vertChoice);
    horizChoice.addItemListener(this);
    vertChoice.addItemListener(this);
    vertChoice.setEnabled(false);
    add(methodChoice);
    methodChoice.setEnabled(false);
    methodChoice.add("#matrix choice#");
    for(int i=0;i<30;i++){widthChoice.add(String.valueOf(1+2*i));}
    add(widthChoice);
    widthChoice.select("15");
    widthChoice.setEnabled(false);
    for(int i=1;i<11;i++){zoomChoice.add("1:"+String.valueOf(i));}
    add(zoomChoice);
    zoomChoice.setEnabled(false);
    add(computeButton);
    computeButton.addActionListener(this);
    computeButton.setEnabled(false);
  }
  public void actionPerformed(ActionEvent e){
    if(e.getSource()==inputButton){
    	/*modification by Olivier*/    	
    	//to avoid opening multiple dialog windows
    	if(this.dialog == null)
    	{
    		this.dialog=new SeqInputDialog(this);
    		//this.dialog.dispose();    	
    	}    	
    	/*end modification*/
    }
    if(e.getSource()==computeButton){    	
    	compute();
    	/*modification by Olivier*/
    	this.buttonPrint.setEnabled(true);
    	/*end modification*/
    }
    /*modification by Olivier*/
    if(e.getSource()==this.buttonPrint)
    {
    	Applet applet = this.dotterPanel.getImagePanel().getApplet();
    	
    	//if it is a real applet
    	if(((Dotlet)applet).isApplet())
    	{
    		try
        	{
    			TextBuffer buffer = new TextBuffer();
    			URL urlCGI = new URL("http://" 
        				+ applet.getParameter("fileServer") + ":"
        				+ applet.getParameter("port")
        				+ applet.getParameter("scriptPS2PDF") + "?"
        				+ applet.getParameter("fileName"));
    			URL urlRedirection = new URL("http://"
    					+ applet.getParameter("fileServer") 
    					+ applet.getParameter("pathTemp")
    					+ applet.getParameter("fileName") + ".pdf");
    			
    			PrintDialogAWT printDialogAWT = new PrintDialogAWT(null, 
        				this.dotterPanel.getImagePanel(), buffer);  
        		printDialogAWT.setSendToServer(new SendToServer(urlCGI, 
        				buffer, true, 
        				printDialogAWT.getLabelStatus(), printDialogAWT.getWindow(), 
        				new AppletRedirection(applet, urlRedirection)));
        	}
        	catch(MalformedURLException e1)
        	{
        		System.err.println("MalformedURLException " + e1.getMessage());
        	}
    	}
    	//or it is launched as an application, it is useless to try to print?
    	else
    	{
    		/*printDialogAWT = new PrintDialogAWT((Frame)applet.getParent(), 
    				this.dotterPanel.getImagePanel(), buffer);*/
    	}
    }
    /*end modification*/
  }
  // called by the two sequence button!
  public void itemStateChanged(ItemEvent e){
    // System.out.println(sequences.isEmpty());
    if(sequences.isEmpty()){
      return;
    }
    methodChoice.removeAll();
    String seq1=(String)sequences.get(horizChoice.getSelectedItem());
    String seq2=(String)sequences.get(vertChoice.getSelectedItem());
    for(int i=0;i<matrixName.length;i++){methodChoice.add(matrixName[i]);}
    /*Seb: All matrices are read at the startup to get the max size of the matrix selector window.
     * Now, for nt. vs nt., identity matrix is selected by default, and other choices are disable.
     * For prot/prot, or nt/prot, choices are enable, with Blosum62 as default.
     * Thus, matrix name are no more hidden when nt. vs nt. comparison is first selected
     * then a prot/prot or nt/prot is secondly selected. 
    */
    if(DNA.isDNA(seq1) && DNA.isDNA(seq2)){
      methodChoice.select(X_IDENTITY);
      methodChoice.setEnabled(false);
    }
    else{
      methodChoice.select("Blosum62");
      methodChoice.setEnabled(true);
    }
    methodChoice.addNotify();
  }	    
 private void updateSequenceChoice(Choice choice){
   if(sequences.isEmpty()){
     return;
   }
   choice.removeAll();
   for (Enumeration e = sequences.keys() ; e.hasMoreElements() ;) {
     choice.add((String)e.nextElement());
   }
   choice.addNotify();
 }
  /*private boolean inputIsValid(){
    if(horizChoice.getItemCount()==0){return false;}
    if(vertChoice.getItemCount()==0){return false;}
    return true;
  }*/
  public void addSequence(String name,String sequence){
    if(name.length()==0){
      name="seq_"+String.valueOf(seqCounter++);
    }
    if(sequence.length()==0){
      dotterPanel.displayMessage("Sequence is null");
      return;
    }
    dotterPanel.displayMessage("Sequence recorded with name "+name);
    sequences.put(name,sequence);
    updateSequenceChoice(horizChoice);
    updateSequenceChoice(vertChoice);
    //itemStateChanged(null);
    horizChoice.setEnabled(true);
    vertChoice.setEnabled(true);
    methodChoice.setEnabled(true);
    computeButton.setEnabled(true);
    widthChoice.setEnabled(true);
    zoomChoice.setEnabled(true);
    itemStateChanged(null);
    repaint();
  }
    private void compute(){
    	/*Seb: add a refresh action for the dotplot image to avoid
    	 to keep print of former image */
    	dotterPanel.imagePanel.getBackground();
    	dotterPanel.imagePanel.repaint(0, 0, getWidth(), getHeight());
		if(sequences.isEmpty()){
		    dotterPanel.displayMessage("FIRST input sequence");
		    return;
		}	    
		dotterPanel.displayMessage("\ncomputing"); // does not work
		String horizontalName=horizChoice.getSelectedItem();
		String horizontalSequence=(String)sequences.get(horizontalName);
		String verticalName=vertChoice.getSelectedItem();
		String verticalSequence=(String)sequences.get(verticalName);
		int windowSize=Integer.parseInt(widthChoice.getSelectedItem());
		int halfWindowSize=windowSize/2;
		//this.visualByteMap.halfWindowSize = halfWindowSize;
		String methodStr=methodChoice.getSelectedItem();
	 	String zoomStr=zoomChoice.getSelectedItem();
		int zoomValue=Integer.parseInt(zoomStr.substring(2));
	      	if(windowSize>horizontalSequence.length()){
		  dotterPanel.displayMessage(windowSize+" is too large");
		  return;
		}
		if(windowSize>verticalSequence.length()){
		  dotterPanel.displayMessage(windowSize+" is too large");
		  return;
		}
		dotterPanel.displayMessage("computing "+methodStr);
		try{
		  comparisonMatrix=(ComparisonMatrix)Class.forName(methodStr).newInstance();
		}
		catch(Exception e){
		  dotterPanel.displayMessage(e.toString());
		  return;
		}
		visualByteMap.setZoom(zoomValue);
		ScoreTable scoreTable=null;
		try{
		  if(DNA.isDNA(horizontalSequence)){
		    if(DNA.isDNA(verticalSequence)){ //DNA vs DNA
		      scoreTable=new DNADNAScoreTable(horizontalSequence,
						      verticalSequence,
						      halfWindowSize,
						      comparisonMatrix,
						      visualByteMap);
		    }
		    else{ //DNA vs PROT
		      if(windowSize>horizontalSequence.length()/3-2){
			dotterPanel.displayMessage(windowSize+" is too large");
			return;
		      }
		      scoreTable=new DNAProteinScoreTable(horizontalSequence,
							  verticalSequence,
							  halfWindowSize,
							  comparisonMatrix,
							  visualByteMap);		    
		    }
		  }
		  else{
		    if(DNA.isDNA(verticalSequence)){ //PROTvs DNA
		      dotterPanel.displayMessage("try DNA vs PROT");
		    }
		    else{ //PROT vs PROT
		      scoreTable=new SameTypeScoreTable(horizontalSequence,
							verticalSequence,
							halfWindowSize,
							comparisonMatrix,
							visualByteMap);      
		    }
		  }
		}
		catch(UnknownCodonException e){
		  System.out.println("unknown codon");
		  dotterPanel.displayMessage("unknown codon");
		  scoreTable=null;
		}
		catch(IdioticUserChoiceException e){
		  System.out.println("unknown error");
		  dotterPanel.displayMessage("unknown error");
		  scoreTable=null;
		}
		catch(java.lang.OutOfMemoryError e){
		  dotterPanel.displayMessage("OUT OF MEMORY");
		  scoreTable=null;
		}
		if(scoreTable!=null){
		  //dotterPanel.displayMessage("computing");
		  //dotterPanel.repaint();
		  //Thread.sleep(1000);
		  // dotterPanel.repaint(1);
		  // fill the dotplot
		  seqPairPanel.setEnabled(false);
		  scalePanel.setEnabled(false);
		  Thread thread=new Thread(scoreTable);
		  //thread.setPriority(Thread.MIN_PRIORITY);
		  //System.out.println("START");
		  thread.start();
		  try{
		    thread.join();
		  }
		  catch(Exception e){}
		  //while(thread.isAlive()){
		  //System.out.println("Thread is alive");
		  //  dotterPanel.displayMessage("computing "+methodStr);
		  
		  //Thread.sleep(100);
		  //}
		  // System.out.println("STOP");
		  try{
		    seqPairPanel.setWidget(horizontalName,
					 horizontalSequence,
					 verticalName,
					 verticalSequence,
					 comparisonMatrix,
					 halfWindowSize);
		  }
		  catch(Exception e){}
		  dotterPanel.setVisualByteMap(visualByteMap);
		  dotterPanel.displayDotPlot(windowSize,horizontalSequence.length(),verticalSequence.length());
		  dotterPanel.setCursor(0,0);
		  scalePanel.setEnabled(true);
		  scalePanel.updateScale();
		  seqPairPanel.setEnabled(true);
		  infoPanel.setInfo(horizontalName,verticalName,methodStr,String.valueOf(windowSize),zoomStr);
		}
	
    }
    
    /*modified by Olivier*/
    public void setDialog(SeqInputDialog dialog)
    {
    	this.dialog = dialog;
    }
    /*end modification*/
}
