import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import streamprint.AppletRedirection;
import streamprint.SendToServer;
import streamprint.TextBuffer;


/**
 * Class for opening an output frame with sequences in fasta format
 * @author omartin
 *
 */
public class SeqOutputDialog extends Frame
{
	private Applet applet;					//applet for redirection to browser
	private Panel panelButtons;				//send/cancel buttons
	private TextArea textArea;				//text/sequences output
	private Button buttonSend;				
	private Button buttonCancel;
	private ImagePanel imagePanel;			//imagePanel for direct access
	private SeqPairWidget seqPairWidget;	//for direct access of variables
	private VisualByteMap visualBM;			//for direct access of variables
	private final int lineLength = 60;		//length of a regular line for a fasta file
	private ArrayList sequencesSelected;		//to display sequences
	private StringBuffer fragments1;		//to append multiple selection of fragments, first sequence
	private StringBuffer fragments2;		//to append multiple selection of fragments, second sequence
	
	/**
	 * Constructor
	 * @param imagePanel
	 */
	public SeqOutputDialog(ImagePanel imagePanel, boolean searchSequences, 
			int startX, int startY, int scaleMin)
	{
 		super("Matching sequences in FASTA format");
		
		this.panelButtons = new Panel(new FlowLayout(FlowLayout.CENTER));
		this.textArea = new TextArea("", 20, this.lineLength);
		this.buttonSend = new Button("Send to browser");
		this.buttonCancel = new Button("Cancel");
		this.imagePanel = imagePanel;
		this.applet = this.imagePanel.getApplet();
		this.seqPairWidget = this.imagePanel.getSeqPairPanel().getSeqPairWidget();
		this.visualBM = this.imagePanel.getVisualByteMap();
		
		setLayout(new BorderLayout());
		this.panelButtons.add(this.buttonSend);
		this.panelButtons.add(this.buttonCancel);
		add(this.textArea, BorderLayout.CENTER);
		add(this.panelButtons, BorderLayout.PAGE_END);
		
		if(searchSequences)
		{
			searchSequences();
		}
		else
		{
			stockFragment(startX, startY, scaleMin);
		}
		
		this.buttonSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendToBrowser();
			}
		});
		this.buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				close();
			}
		});
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});
		
		pack();
		setVisible(true);
	}

	/**
	 * Search for the sequences above the grey cutoff (histogram)
	 *
	 */
	void searchSequences()
	{
		//get sequence of reference	
		//warning: do not mix image and sequence coordinate because of the sliding window,
		//coord correction for image/sequence translation is only made at a  
		//seqPairWidget.getSequence() call
		if(MouseSelectionMisc.width > 0 && MouseSelectionMisc.height > 0)
		{
			StringBuffer refSeq = new StringBuffer(">");
			String nameSeq = "unknown";
			ArrayList arraySequences = new ArrayList();
			boolean vertRef = false;
			int startRef = 0;
			int endRef = 0;
			int startOther = 0;
			int endOther = 0;			//I only check full diagonals contained in the rectangular selection, not up to the mouse selection end	
			this.seqPairWidget = this.imagePanel.getSeqPairPanel().getSeqPairWidget();
			this.sequencesSelected = new ArrayList();
			int window = 2 * this.seqPairWidget.hwl;
			int seqLength = 0;
			
			//vertical reference
			if(MouseSelectionMisc.width > MouseSelectionMisc.height)
			{
				refSeq.append(this.seqPairWidget.vsName);
				nameSeq = this.seqPairWidget.hsName;
				startRef = MouseSelectionMisc.startY;
				endRef = MouseSelectionMisc.endY;
				startOther = MouseSelectionMisc.startX;
				seqLength = endRef - startRef;
				endOther = ((((MouseSelectionMisc.endX - startOther) / seqLength) * seqLength) + startOther);
				vertRef = true;
				//displayValues(this.imagePanel, startOther, endOther, startRef, endRef);
			}
			//horizontal reference
			else
			{
				refSeq.append(this.seqPairWidget.hsName);
				nameSeq = this.seqPairWidget.vsName;
				startRef = MouseSelectionMisc.startX;
				endRef = MouseSelectionMisc.endX;
				seqLength = endRef - startRef;
				startOther = MouseSelectionMisc.startY;
				endOther = ((((MouseSelectionMisc.endY - startOther) / seqLength) * seqLength) + startOther);
				//displayValues(this.imagePanel, startRef, endRef, startOther, endOther);
			}
			
			/*System.out.println("refSeq " + refSeq);
			System.out.println("nameSeq " + nameSeq);			
			System.out.println("startRef " + startRef);
			System.out.println("endRef " + endRef);
			System.out.println("seqLength " + seqLength);
			System.out.println("startOther " + startOther);
			System.out.println("endOther " + endOther);
			System.out.println("diffOther " + diffOther);*/
			
			// refSeq.append(" " + String.valueOf(startRef+1) + "-" + String.valueOf(endRef+1+window) + "\n");
			refSeq.append("/" + String.valueOf(startRef+1) + "-" + String.valueOf(endRef+1+window) + "\n");
			refSeq.append(insertNewLine(new StringBuffer(this.seqPairWidget.getSequence(vertRef, startRef, 
					endRef + window)), 0));	

			searchDiagonals(startOther, endOther, startRef, endRef, vertRef, 
					seqLength, this.imagePanel.getScaleMin(), window, 
					arraySequences, nameSeq);
			
			//add the sequence of reference to the output
			this.textArea.setText(refSeq.toString());
			
			//add all the others matching to the output
			for(int i=0; i<arraySequences.size(); i++)
			{
				this.textArea.append((String)arraySequences.get(i));
			}	
		}
	}
	
	/**
	 * Function to find matching sequences with a reference sequence
	 * @param startOther
	 * @param endOther
	 * @param startRef
	 * @param endRef
	 * @param vertRef
	 * @param seqLength
	 * @param scaleMin
	 * @param window
	 * @param arraySequences
	 * @param nameSeq
	 */
	private void searchDiagonals(int startOther, int endOther, int startRef, 
			int endRef, boolean vertRef, int seqLength, int scaleMin, 
			int window, ArrayList arraySequences, String nameSeq)
	{
		int totalScore = 0;
		int seqNumber = 0;
		int coordX = 0;
		int coordY = 0;
		int k=0;
		
		//for every diagonals
		for(int i=startOther; i<endOther; i++)
		{
			k = 0;
			totalScore = 0;
			//for every point of a diagonal
			for(int j=startRef; j <endRef; j++)
			{				
				if(!vertRef)
				{
					coordX = j;
					coordY = i+k;
				}
				else
				{					
					coordX = i+k;
					coordY = j;
				}

				//System.out.println(coordX + " " + coordY + " " + this.imagePanel.getVisualByteMap().getPixel(coordX, coordY));
				if(this.imagePanel.getVisualByteMap().getPixel(coordX, coordY) > scaleMin)
				{
					totalScore++;
				}
			
				k++;
			}
			
			if(isConditionFullfilled(totalScore, seqLength))
			{
				seqNumber++;
				
				addSequence(i, seqLength, window, seqNumber, vertRef, startRef, 
						endRef, arraySequences, nameSeq);
			}
		}
	}
	
	public void stockFragment(int startX, int startY, int scaleMin)
	{
		//this.textArea.append(startX + " " + startY + "\n");
		int value = Integer.MAX_VALUE;
		int x = startX;
		int y = startY;
		int endX1 = 0;
		int endY1 = 0;
		int endX2 = 0;
		int endY2 = 0;
				
		//displayMatrixValues(x, y, 5);
		
		//back up
		while(value > scaleMin && x >= 0 && y >= 0)
		{			
			value = this.imagePanel.getVisualByteMap().getPixel(x, y);	
			x--;
			y--;
		}
		endX1 = x+1;
		endY1 = y+1;
		x = startX;
		y = startY;
		value = Integer.MAX_VALUE;
		
		//forward down
		while(value > scaleMin && x < this.imagePanel.getVisualByteMap().getFullWidth() 
				&& y < this.imagePanel.getVisualByteMap().getFullHeight())
		{
			value = this.imagePanel.getVisualByteMap().getPixel(x, y);
			//System.out.println(value + " " + scaleMin + " " + x + " " + y);
			x++;
			y++;
		}			
		endX2 = x-1;
		endY2 = y-1;
	
		//this.textArea.append(endX1 + " " + endY1 + " / " + endX2 + " " + endY2);
		addSequence(endX1, endY1, endX2, endY2);
	}
	
	/**
	 * Adds matching sequences in a rectangular selection to the ArrayList
	 * @param i
	 * @param seqLength
	 * @param window
	 * @param seqNumber
	 * @param vertRef
	 * @param startRef
	 * @param endRef
	 * @param arraySequences
	 * @param nameSeq
	 */
	private void addSequence(int i, int seqLength, int window, int seqNumber,
			boolean vertRef, int startRef, int endRef, ArrayList arraySequences,
			String nameSeq)
	{
		int startSeq = i;
		int endSeq = i + seqLength + window;
		
		StringBuffer temp = new StringBuffer("\n\n>" + nameSeq + "/");
		/* StringBuffer temp = new StringBuffer("\n\n>" + nameSeq + "_");
		temp.append(seqNumber + " "); */
		temp.append((startSeq+1) + "-" + (endSeq+1));
		temp.append("\n");
		temp.append(insertNewLine(new StringBuffer(this.seqPairWidget.getSequence(!vertRef,
				startSeq, endSeq)), 0));												
		arraySequences.add(temp.toString());
		
		if(vertRef)
		{
			this.sequencesSelected.add(new Sequence(i, i + seqLength, 
					startRef, endRef));
		}
		else
		{
			this.sequencesSelected.add(new Sequence(startRef, endRef, 
					i, i + seqLength));	
		}
	}
	
	/**
	 * Adds fragments and merge them in a single sequence iteratively
	 * @param startX1
	 * @param startY1
	 * @param endX1
	 * @param endY1
	 */
	private void addSequence(int startX, int startY, int endX, int endY)
	{
		int window = (2 * this.seqPairWidget.hwl) + 1;
		int offset = 0;
		String header1 = ">SUB_" + this.seqPairWidget.hsName + "\n";		//horizontal
		String header2 = ">SUB_" + this.seqPairWidget.vsName + "\n";		//vertical
		
		//first round of addition
		if(this.fragments1 == null && this.fragments2 == null)
		{
			this.fragments1 = new StringBuffer();
			this.fragments2 = new StringBuffer();
			this.sequencesSelected = new ArrayList();
		}
		
		//to draw the matching diagonals on imagePanel
		this.sequencesSelected.add(new Sequence(startX, endX, startY, endY));
		
		endX += window;
		endY += window;
		
		offset = prepareFragments(this.fragments1);
		this.fragments1.append(insertNewLine(new StringBuffer(this.seqPairWidget.getSequence(false, startX, endX)), offset) + "\n");
		
		offset = prepareFragments(this.fragments2);
		this.fragments2.append(insertNewLine(new StringBuffer(this.seqPairWidget.getSequence(true, startY, endY)), offset) + "\n");
		
		if(!this.fragments1.toString().startsWith(header1))
		{
			this.fragments1.insert(0, header1);
			this.fragments2.insert(0, header2);
		}
		
		this.textArea.setText(this.fragments1.toString());
		this.textArea.append(this.fragments2.toString());
	}
	
	/**
	 * Computes the offset required for the newline insertion
	 * @param string
	 * @return offset
	 */
	private int prepareFragments(StringBuffer string)
	{
		int offset = 0;
		
		//except for the first insertion
		//remove last character \n and count up to the last remaining \n
		//to compute offset
		if((string.length() > 0))
		{
			string.deleteCharAt(string.length() - 1);
			offset = string.length() - 1 - string.lastIndexOf("\n");
		}
		
		return offset;
	}
	
	/**
	 * Wether or not the diagonal fullfills the condition(s) to be sent back to browser
	 * @return true or false
	 */
	private boolean isConditionFullfilled(int score, int length)
	{
		//here simple condition: at least 50% of score above cutoff
		if(score > length / 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Insert a newline character for fasta output
	 * @param stringBuf
	 * @return
	 */
	private String insertNewLine(StringBuffer stringBuf, int offset)
	{		
		int totalLength = stringBuf.length();					
		int l = this.lineLength - offset;
		
		//put a newline for safety
		while(l < totalLength)
		{												
			stringBuf.insert(l, '\n');
			l += this.lineLength + 1;										
			totalLength = stringBuf.length();						
		}
		
		return stringBuf.toString();
	}
	
	/**
	 * Send result back to browser
	 *
	 */
	private void sendToBrowser()
	{
		if(this.applet != null)
		{
			try
			{
				URL urlCGI = new URL("http://" 
        				+ applet.getParameter("fileServer") + ":"
        				+ applet.getParameter("port")
        				+ applet.getParameter("scriptSendProtHub") + "?"
        				+ applet.getParameter("fileName"));
    			URL urlRedirection = new URL("http://"
    					+ applet.getParameter("fileServer") + ":"
        				+ applet.getParameter("port")
    					+ applet.getParameter("pathProtHub"));
    			
    			//System.out.println(urlCGI.toString());
    			//System.out.println(urlRedirection.toString());
    			
    			SendToServer sendToServer = new SendToServer(urlCGI, new TextBuffer(this.textArea.getText()), 
    					false, null, null, new AppletRedirection(applet, urlRedirection));
    			
    			new Thread(sendToServer).start();
			}
			catch(MalformedURLException e)
			{
				System.err.println("MalformedURLException : " + e.toString());
			}
		}
	}
	
	/**
	 * Display values of the visualbyemap at coordinates
	 * @param imagePanel
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 */
	private void displayValues(ImagePanel imagePanel, int startX, int endX,
			int startY, int endY)
	{
		for(int j=startY; j<endY; j++)
		{
			for(int i=startX; i<endX; i++)
			{
				System.out.print(imagePanel.getVisualByteMap().getPixel(i, j));
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Closes window and sets the list of selected sequences to null
	 *
	 */
	private void close()
	{
		imagePanel.setOutputDialog(null);
		this.sequencesSelected = null;
		dispose();
	}
	
	public void setApplet(Applet applet)
	{
		this.applet = applet;
	}
	
	public ArrayList getSequences()
	{
		return this.sequencesSelected;
	}
	
	public void setSequences(ArrayList seq)
	{
		this.sequencesSelected = seq;
	}
	
	public void setText(String text)
	{
		this.textArea.setText(text);
	}
	
	public void appendtext(String text)
	{
		this.textArea.append(text);
	}
}

/**
 * Class containing sequence start and end coordinates, used to draw matching 
 * sequences on screen
 * @author omartin
 *
 */
class Sequence
{
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	public Sequence(int startX, int endX, int startY, int endY)
	{
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
	}

	public int getEndX() {
		return this.endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return this.endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public int getStartX() {
		return this.startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return this.startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}
}
