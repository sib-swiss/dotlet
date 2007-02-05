//import java.awt.Image;
//import java.awt.Color;
//import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.Dimension;
//import java.awt.Font;
import java.awt.Rectangle;
//import java.awt.Shape;

//import SeqPairWidget;

public class ProteinProteinWidget extends SeqPairWidget
{
    // The subclasses of SeqPairWidget differ only by their paint()
    // methods

    ProteinProteinWidget (String hn, String h, String vn, String v, ComparisonMatrix m, int w)
    {
	super ();

	hsName = hn;
	hs = h;
	vsName = vn;
	vs = v;
	mat = m;
	hwl = w;

	hl = hs.length ();
	vl = vs.length ();
	
	hpos = hl / 2;
	vpos = vl / 2;

	matrixMax = mat.getMax ();
	matrixMin = mat.getMin ();
	
    }
    
    public void paint (Graphics g)
    { 
		// Check that the sequences have been set
		if (hs == null || vs == null)
		{
			g.setFont (pf);
			g.drawString ("(no sequences)", 20, 20);
			return;
		}
		
		// Offscreen graphics
		if (im == null)
		    newSize ();
		
		// Extract the substrings to be displayed
		String hss = displayedSubstring (hs, hpos);
		String vss = displayedSubstring (vs, vpos);
		
		clearClipRect (); // reset the drawing area
	
		// Display the labels
		drawLabel (hsName + " ", String.valueOf (hpos + 1), height / 2 - cHeight -4);
		drawLabel (vsName + " ", String.valueOf (vpos + 1),
					height / 2 + cHeight + pcHeight + 5);
	
		// Draw the strings, char by char, colouring the characters
		// according to similarity
		osg.setFont (f);
	
		int matches [] = new int[ Math.min (hss.length (), vss.length ()) ];
		drawSubSequence (hss, vss, height / 2, matches);
		drawSubSequence (vss, height / 2 + cHeight, matches);
	
		highlightWindow (); // draw the comparison window
		
		// And finally:
		g.drawImage (im, 0, 0, this);
    }

    void newSize ()
    {
		// Create a new image and get a ref to its Graphics
		im = createImage (getSize().width, getSize().height);
		osg = im.getGraphics ();
	
		// Get the font metrics
		fm = osg.getFontMetrics (f);
		pfm = osg.getFontMetrics (pf);
	
		// Get the character sizes
		cHeight = fm.getAscent ();
		pcHeight = pfm.getAscent ();
		cWidth = fm.charWidth ('M'); // any letter can do, f is fixed-width
		
		// Get the outer dimensions
		width = getSize().width;
		height = getSize().height;
		
		// Compute writeable width
		writeableWidth = width - 2 * MARGIN;
	
		// Compute number of sequence chars to display
		nchars = writeableWidth / cWidth;
	
		// this is the horizontal pixel value of the middle
		// character's center, which is not always the same as the
		// middle of the widget, because the widget's width is not
		// always a multiple of a character's width.
		middletext = MARGIN + (nchars / 2) * cWidth + cWidth / 2;
		
		/*
		// Compute clipping rectangle
		clipRect = new Rectangle (MARGIN, height / 2 - cHeight - pcHeight -2,
					  writeableWidth, 2 * (cHeight + pcHeight) + 10);
		*/
		
		// Do this before the clip...
		osg.draw3DRect (0, 0, width - 2, height - 2, true);
		
		// Clip!
		// osg.setClip ((Shape) clipRect);  // we no longer use a clipRect
	
		// Compute the highlighted rectangle (all those '2 +' and '-
		// 1' are post-hoc adjustments :-))
		// length of window in pixels (+1 for center residue):
		int wWidth = (2 * hwl + 1) * cWidth; 
		hlWindow = new Rectangle (MARGIN + (nchars / 2 - hwl) * cWidth, 
			    height / 2 - cHeight, 
			    wWidth - 1, 
			    2 * cHeight + 3);

    }
}
	
