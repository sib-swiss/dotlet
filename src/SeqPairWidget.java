// SeqPairWidget - equivalent of dotter's Alignment Tool

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Rectangle;
//import java.awt.Shape;
import java.awt.FontMetrics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

//import ComparisonMatrix;

public abstract class SeqPairWidget extends Panel
{
    static final int MATCH = 1;
    static final int NEUTRAL = 0;
    static final int MISMATCH = -1;
    static Color MATCH_BG = Dotlet.BRIGHT_BLUE;
    static Color MISMATCH_BG = Dotlet.GREY_BLUE; // Bon, j'ai fini par céder après des heures de cassage de pieds :-) - Note: le referee 3 a demandé de mettre 2 couleurs, je suis vengé.
    static final Color HILITWINCOLOR = Dotlet.GREY_PINK;
    static final int MARGIN = 20;

    String hs;			// horizontal sequence
    String vs;			// vertical sequence
    String hsName = "horizontal";	// name of horizontal sequence
    String vsName = "vertical";		// name of vertical sequence
    ComparisonMatrix mat;	// guess what?
    int matrixMax;
    int matrixMin;
    int matrixRange;
    
    int hpos;			// position in the horizomtal seq
    int vpos;			// ... vertical ...
    int hwl;			// half window's length
    int hl;			// horizontal sequence's length
    int vl;			// guess what
	
    // The following variables are used to paint the widget
    
    // These never change
    Font f;			// nonproportional font for the sequence
    Font pf;			// proportional font for labels
    FontMetrics fm;		// FontMetrics for f
    FontMetrics pfm;		// FontMetrics for pf
    int cHeight;		// sequence character height (max)
    int pcHeight;		// label character height (max)
    int cWidth;			// sequence character width (same for all)
    Image im = null;		
    Graphics osg;		// Offscreen graphics
    
    // These change only when the widget is resized
    int height;			// the widget's total width
    int width;			// the widget's height
    int writeableWidth;		// the widget's writeable width (accounting for margins)
    Rectangle clipRect;		// the clipping area
    Rectangle hlWindow;		// the area corresponding to the window
    int nchars;			// the maximum number of sequence chars that can be displayed,
				//     given the writeable width.
    int middletext;		// the horizontal middle of the text
    
    // Constructors

    SeqPairWidget ()
    {
		addComponentListener (new ResizeHandler ());
		// addKeyListener (new KeyHandler ());
		
		f = new Font ("Monospaced", Font.PLAIN, 12);
		pf = new Font ("Dialog", Font.BOLD, 12);
    }

    // These allow the sequences to be set later

    public void setSequences (String h, String v)
    {
	hs = h;
	vs = v;
    }
    
    public void setSequences (String h, String v, String hsn, String vsn)
    {
	// 4-arg form: allows to set the sequences' names.
	hs = h;
	vs = v;
	hsName = hsn;
	vsName = vsn;
    }
    // These methods allow to follow the cursor
    

    //horizontal movement
    
    public void left (int n)
    {
	if (hpos - n < hwl)
	    hpos = hwl;
	else
	    hpos -= n;

	repaint ();
    }
    public void left () { left (1); }
    
    public void right (int n)
    {
	if (hpos + n + hwl + 1 >= hl)
	    hpos = hl - hwl - 2;
	else
	    hpos += n;

	repaint ();
    }
    public void right () { right (1); }

    // vertical movement
    
    public void up (int n)
    {
	if (vpos - n < hwl)
	    vpos = hwl;
	else
	    vpos -= n;

	repaint ();
    }
    public void up () { up (1); }    

    public void down (int n)
    {
	if (vpos + hwl + n + 1 >= vl)
	    vpos = vl - hwl - 2;
	else
	    vpos += n;

	repaint ();
    }
    public void down () { down (1); }

    // diagonal movement

    public void upstream (int d)
    {
	left (d);
	up (d);
    }
    public void upstream () { upstream (1); }

    
    public void downstream (int d)
    {
	down (d);
	right (d);
    }
    public void downstream () { downstream (1); }
    

    public void upright (int d)
    {
	up (d);
	right (d);
    }
    public void upright () { upright (1); }
    
    public void downleft (int d)
    {
	down (d);
	left (d);
    }
    public void downleft () { downleft (1); }
    
    // arbitrary jump
    
    public void setPos (int h, int v)
    {
	if (h < hwl)
	    hpos = hwl;
	else if (h + hwl >= hl)
	    hpos = hl - hwl - 2;
	else
	    hpos = h;

	if (v < hwl)
	    vpos = hwl;
	else if (v + hwl >= vl)
	    vpos = vl - hwl - 2;
	else
	    vpos = v;

	repaint ();
    }

    // Info methods

    public int getHalfWindowSize () { return hwl; }
    public int getHpos() { return hpos; }
    public int getVpos() { return vpos; }
    
    /*modification by Olivier*/
    /**
     * Method for getting a subsequence from one of the 2 sequences in the alignment
     * 
     */
    public String getSequence(boolean seq, int start, int end)
    {
    	//horizontal seq
    	if(!seq)
    	{
    		//Seb:
    		if (end >= hl)
    		{
    			end=hl;
    		}
    		return this.hs.substring(start, end);
    	}
    	//vertical seq
    	else
    	{
    		//Seb:
    		if (end >= vl)
    		{
    			end=vl;
    		}
    		return this.vs.substring(start, end);
    	}
    }
    
    public String getSequence(boolean seq, int start1, int end1, int start2, int end2)
    {
    	if(seq)
    	{
    		return this.hs.substring(start1, end1) + this.hs.substring(start2, end2); 
    	}
    	else
    	{
    		return this.vs.substring(start1, end1) + this.vs.substring(start2, end2);
    	}
    }
    /*end modification*/

    // Graphic methods

    public void update (Graphics g)
    {	
    	paint (g);		// Down with the flickering !!
    }
    
    public abstract void paint (Graphics g);

    void highlightWindow ()
    {
	osg.setColor (HILITWINCOLOR);
	osg.drawRect (hlWindow.x, hlWindow.y, hlWindow.width, hlWindow.height);
    }
    
    void clearClipRect ()
    {
	// This method USED to clear the clipRect, but now we don't use a clipRect
	// anymore, so it simply clears the whole drawing area. We keep the name to
	// avoid breaking code.
	osg.clearRect (0, 0, getSize().width, getSize().height);
    }

    String displayedSubstring (String s, int pos)
    {
		// returns the substring of `s' to be displayed, given the
		// number of displayable characters, and given that position
		// `pos' is centered in the widget. If the start index is
		// below 0 (i.e., if we shift the window too near the
		// sequence's beginning, the substring is prepadded with the
		// appropriate amount of spaces.
		
		StringBuffer sb = new StringBuffer ();
		String ss;		
		int start = pos - nchars / 2;
		int stop = start + nchars;
		
		if (start < 0)
		    {
			stop = nchars - 1;
			for (; start < 0; start++)
			    sb.append (' ');
		    }
		if (stop >= s.length ())
		    stop = s.length();
				
		// System.out.println ("start: " + start + ", stop: " + stop + ", length: " + s.substring (start, stop).length());
		sb.append (s.substring (start, stop));
		ss = new String (sb);
		if (ss.length () > nchars)
		    ss = ss.substring (0, nchars - 1);
	
		return ss;
    }

    abstract void newSize ();
    
    int matchColor (char c1, char c2)
    {
		if (c1 == ' ' || c2 == ' ')
		    return NEUTRAL;
		else if (c1 == '*' || c2 == '*')
		    return NEUTRAL;
	
		if (c1 == c2)
			return MATCH;
	
		int score = mat.score (c1, c2);
		if (score > 0)
		    return MISMATCH;
		else
		    return NEUTRAL;
    }

    void drawSubSequence3f (String sseq, String comp, int h, int [] match)
    {
		// to run on each frame with the prot as comp
		//*** comp is assumed to be not shorter than sseq *** 
		
		for (int i = 0; i < match.length; i++)
		    {
			switch (matchColor (sseq.charAt (i), comp.charAt (i)))
			    {
			    case MATCH:
				osg.setColor (MATCH_BG);
				osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
				match[ i ] = MATCH;
				break;
			    case MISMATCH:
				osg.setColor (MISMATCH_BG);
				osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
				break;
			    }
			
		    }
		osg.setColor (Color.black);
		osg.drawString (sseq, MARGIN, h);
    }
    
    void drawSubSequence3f (String sseq, int h, int [] match)
    {
		// this one is for the protein
		
		for (int i = 0; i < match.length; i++)
		{
			if (i < match.length && match[ i ] == MATCH)
			{
				osg.setColor (MATCH_BG);
				osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
			}		
		}
		osg.setColor (Color.black);
		osg.drawString (sseq, MARGIN, h);
    }
    
    void drawSubSequence (String sseq, String comp, int h, int [] match)
    {
		// to run on the first prot sequence (avoids redondant lookups
		// in the comparison matrix)
		//*** comp is assumed to be not shorter than sseq *** 
	
		for (int i = 0; i < match.length; i++)
		{
			// System.out.println ("Comparing chars at index " + i);
			switch (match[ i ] = matchColor (sseq.charAt (i), comp.charAt (i)))
			{
			    case MATCH:
					osg.setColor (MATCH_BG);
					osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
					match[ i ] = MATCH;
					break;
			    case MISMATCH:
					osg.setColor (MISMATCH_BG);
					osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
					break;
			}
			
		}
		osg.setColor (Color.black);
		osg.drawString (sseq, MARGIN, h);
    }
    
    void drawSubSequence (String sseq, int h, int [] match)
    {
		// this one is for the second protein
		
		for (int i = 0; i < match.length; i++)
		    {
			switch (match[ i ])
			    {
			    case MATCH:
				osg.setColor (MATCH_BG);
				osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
				match[ i ] = MATCH;
				break;
			    case MISMATCH:
				osg.setColor (MISMATCH_BG);
				osg.fillRect (MARGIN + i * cWidth, h - cHeight + 2, cWidth, cHeight);
				break;
			    }
		    }
		osg.setColor (Color.black);
		osg.drawString (sseq, MARGIN, h);
    }

    void drawLabel (String beforeMiddle, String afterMiddle, int h)
    {
		int lblWidth = pfm.stringWidth (beforeMiddle);
			
		osg.setColor (Color.black);
		osg.setFont (pf);
		
		// draw this just before the middle...
		osg.drawString (beforeMiddle, middletext - lblWidth, h);
		// ... and this just after
		osg.drawString (afterMiddle, middletext + 3, h);
		osg.drawLine (middletext, h - pcHeight, middletext, h);	// vertical line
		//System.out.println(beforeMiddle + "\t| " + afterMiddle);
    }

    /*modified by Olivier*/
    /**
     * Draws a selection rectangle at coordinates
     */
    void drawSelectionRect(int x, int y, int width, int height)
    {
    	osg.setColor(MouseSelectionMisc.seqSelectionColor);
    	osg.drawRect(x, y, width, height);
    }
    
    void drawSelectionResidues(String seq1, String seq2, int h)
    {
    	int start = (MouseSelectionMisc.startX < MARGIN) ? 
    			MARGIN : (MouseSelectionMisc.startX / cWidth) * cWidth;
    	int stop = (MouseSelectionMisc.startX < MARGIN) ? 
    			MouseSelectionMisc.width + start - MARGIN: MouseSelectionMisc.width + start;
    	int limit = MARGIN + (seq1.length() * cWidth);
    	int j = (start - MARGIN) / cWidth;

    	for(int i=start; i<stop; i+=cWidth)
    	{
    		if(i < limit)
    		{
    			try
    			{
	    			if(seq1.charAt(j) != ' ')
		    		{
		    			osg.fillRect(i, h - cHeight, cWidth, 2);
		    		}
		    		if(seq2.charAt(j) != ' ')
		    		{
		    			osg.fillRect(i, h + cHeight + 2, cWidth, 2);
		    		}
		    		j++;
    			}
    			catch(Exception e) {}
    		}
    	}
    }
    /*end modification*/
    
    // Handlers
    
    class ResizeHandler extends ComponentAdapter
    {
	public void componentResized (ComponentEvent e)
	{
	    newSize ();
	}
    }

    class KeyHandler extends KeyAdapter
    {
	final int n = 1;
	
	public void keyTyped (KeyEvent e)
	{
	    switch (e.getKeyChar ())
		{
		case '<':
		    upstream (n);
		    break;
		case '>':
		    downstream (n);
		    break;
		case ']':
		    upright (n);
		    break;
		case '[':
		    downleft (n);
		    break;
		}
	}

	public void keyPressed (KeyEvent e)
	{
	    switch (e.getKeyCode ())
		{
		case KeyEvent.VK_LEFT:
		    left (n);
		    break;
		case KeyEvent.VK_RIGHT:
		    right (n);
		    break;
		case KeyEvent.VK_UP:
		    up (n);
		    break;
		case KeyEvent.VK_DOWN:
		    down (n);
		    break;
		}
	}
    }

    public Dimension getPreferredSize ()
    {
	return new Dimension (256, 150);
    }
    public Dimension getMinimumSize ()
    {
	return getPreferredSize ();
    }
}

