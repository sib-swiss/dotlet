import java.awt.*;
import java.awt.event.*;

/*import DNA;
import UnknownCodonException;*/

public class SeqPairPanel extends Panel implements AdjustmentListener{

    private DotterPanel dotterPanel;
    private SeqPairWidget seqPairWidget=null;
    private Scrollbar horizScrollbar=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,255);
    private Scrollbar vertScrollbar=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,255);
    private int vspace=5;
    private KeyListener keyListener;
    public boolean DNAvsProt = true; //Seb: check status of seq: true if seq have the same nature, else false

    public SeqPairPanel(DotterPanel dp){
		dotterPanel=dp;
		setLayout(new BorderLayout());
		add("North",horizScrollbar);
		horizScrollbar.addAdjustmentListener(this);
		add("South",vertScrollbar);
		vertScrollbar.addAdjustmentListener(this);
		setEnabled(false);
    }
    public void addKeyListener(KeyListener keyListener){
		super.addKeyListener(keyListener);
		this.keyListener=keyListener;
    }
    public void paint(Graphics g){
	 	if(seqPairWidget!=null){
		    seqPairWidget.paint(g);
		}
    }
    public void setEnabled(boolean b){
		horizScrollbar.setEnabled(b);
		vertScrollbar.setEnabled(b);
	}
    public Dimension getMinimumSize(){
    	return getPreferredSize();
    }
    public Dimension getMaximumSize(){
    	return getPreferredSize();
    }
    public Dimension getPreferredSize(){
		Dimension dim=horizScrollbar.getPreferredSize();
		return new Dimension(255,120+2*dim.height+2*vspace);
    }
    public void setWidget(String horizontalName,
			  String horizontalSequence,
			  String verticalName,
			  String verticalSequence,
			  ComparisonMatrix comparisonMatrix,
			  int halfWindowSize)
	throws UnknownCodonException{
		System.out.println ("horiz length: " + horizontalSequence.length());
		System.out.println ("vert  length: " + verticalSequence.length());
		if(seqPairWidget!=null){
		    remove(seqPairWidget);
		}
		int scrollLength;
		if(DNA.isDNA(horizontalSequence)){
		    if(DNA.isDNA(verticalSequence)){
			seqPairWidget=new DNADNAWidget(horizontalName, horizontalSequence,
						       verticalName, verticalSequence,
						       comparisonMatrix,
						       halfWindowSize);
			scrollLength =  horizontalSequence.length ()+ halfWindowSize;
			DNAvsProt = true;
		    }
		    else{
			// DNA vs. prot
			String frame1, frame2, frame3;

			frame1 = DNA.translate (horizontalSequence);
			frame2 = DNA.translate (horizontalSequence.substring (1));
			frame3 = DNA.translate (horizontalSequence.substring (2));
			
			seqPairWidget = new DNAProteinWidget (horizontalName,
							      frame1,
							      frame2,
							      frame3,
							      verticalName, verticalSequence,
							      comparisonMatrix,
							      halfWindowSize);
			scrollLength =  (horizontalSequence.length ()) / 3 - 2 + halfWindowSize;
			DNAvsProt = false;
		    }
		}
		else{
		    //prot vs prot
		    seqPairWidget=new ProteinProteinWidget(horizontalName, horizontalSequence,
							   verticalName, verticalSequence,
							   comparisonMatrix,
							   halfWindowSize);
		    scrollLength =  horizontalSequence.length ()+ halfWindowSize;
		    DNAvsProt = true;
		}

		horizScrollbar.setValues(halfWindowSize+1,
					 halfWindowSize*2+1,
					 halfWindowSize,
					 scrollLength);
		vertScrollbar.setValues(halfWindowSize+1,
					halfWindowSize*2+1,
					halfWindowSize,
					verticalSequence.length ()+  halfWindowSize);
		setEnabled(true);
		add("Center",seqPairWidget);
		validate();
		seqPairWidget.addKeyListener(keyListener);
		repaint();
    }
    private void setCursorPos(int hPos,int vPos){ // called by scrollbars
		int hws=seqPairWidget.getHalfWindowSize();
		dotterPanel.setCursor(hPos-hws,vPos-hws);
		seqPairWidget.setPos(hPos,vPos);
    }

    // The folllowing is called is intended to be called from dotterPanel
    public void setCursor(int hPos,int vPos){ 
		int hws=seqPairWidget.getHalfWindowSize();
		hPos+=hws;   // change references!
		vPos+=hws;
		horizScrollbar.setValue(hPos);
		vertScrollbar.setValue(vPos);	
		seqPairWidget.setPos (hPos,vPos);
    }
    public void adjustmentValueChanged(AdjustmentEvent e){
		int x,y;
		if(e.getSource()==horizScrollbar){
		    x = e.getValue();
		    y = seqPairWidget.getVpos();
		}
		else{ //(e.getSource()==vertScrollbar)
		    x = seqPairWidget.getHpos();
		    y = e.getValue();
		}
		setCursorPos(x,y);
    }
    
    /*modification by Olivier*/
    public SeqPairWidget getSeqPairWidget()
    {
    	return this.seqPairWidget;
    }
    /*end modification*/
}
