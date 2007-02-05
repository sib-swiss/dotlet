//import java.awt.Image;
import java.awt.Color;
//import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.Dimension;
//import java.awt.Font;
import java.awt.Rectangle;
//import java.awt.Shape;
//import java.awt.Toolkit;

/*import SeqPairWidget;
import DNA;*/

public class DNADNAWidget extends SeqPairWidget
{
    // The subclasses of SeqPairWidget differ only by their paint()
    // methods
    private static final int halfGutter = 5; // separates the 2 alignments
    static Color MISMATCH_BG = Color.lightGray;
    
    String rcs;			// The reverse-complemented vertical sequence
    Rectangle rhlWindow;	// ... and its highlighted box
    
    DNADNAWidget (String hn, String h, String vn, String v, ComparisonMatrix m, int w)
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

	rcs = DNA.revcomp (hs);
    }
    
    public void paint (Graphics g)
    {
	// Offscreen graphics
	if (im == null)
	    newSize ();
	
	// Extract the substrings to be displayed
	final String hss = displayedSubstring (hs, hpos);
	final String vss = displayedSubstring (vs, vpos);
	final String rcss = displayedSubstring (rcs, hl - hpos - 1);

	clearClipRect (); // reset the drawing area

	// Display the labels
	drawLabel (hsName + " ", " " + hpos,
		   height / 2 - 2 * cHeight - pcHeight - halfGutter - 4);
	drawLabel (vsName + " ", " " + vpos, height / 2 - halfGutter + 5);
	drawLabel (hsName + " (revcomp'd) ", " " + hpos,
		   height / 2 + pcHeight + halfGutter - 4);
	drawLabel (vsName + " ", " " + vpos,
		   height / 2 + 2 * cHeight + 2 * pcHeight + halfGutter + 5);
	
	// Draw the strings, char by char, colouring the characters
	// according to similarity
	osg.setFont (f);

	int matches [] = new int[ Math.min (hss.length (), vss.length ()) ];
	// System.out.println ("h length: " + hss.length() + ", v length: " + vss.length());
	// System.out.println (matches.length + " positions in match array");
	drawSubSequence (hss, vss, height / 2 - cHeight -pcHeight - halfGutter, matches);
	drawSubSequence (vss, height / 2 - pcHeight - halfGutter, matches);

	matches = new int[ Math.min (rcss.length (), vss.length ()) ];
	drawSubSequence (rcss, vss, height / 2 + cHeight + pcHeight + halfGutter, matches);
	drawSubSequence (vss, height / 2 + 2 * cHeight + pcHeight + halfGutter, matches);
	
	highlightWindow (); // draw the comparison window
	
	// And finally:
	g.drawImage (im, 0, 0, this);
    }

    void newSize ()
    {
	// Create a new image and get a ref to its Graphics
	im = createImage (getSize().width, getSize().height);
	osg = im.getGraphics ();

	// get the fontMetrics and character sizes
	fm = osg.getFontMetrics (f);
	pfm = osg.getFontMetrics (pf);
	cHeight = fm.getAscent ();
	pcHeight = pfm.getAscent ();
	cWidth = fm.charWidth ('M'); // any letter can do, f is proportional
	
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
	
	/* // Compute clipping rectangle
	clipRect = new Rectangle (MARGIN,
				  height / 2 - 2 * cHeight - 2 * pcHeight - halfGutter,
				  writeableWidth,
				  4 * cHeight + 4 * pcHeight + 2 * halfGutter);
	*/

	// Do this before the clip...
	osg.draw3DRect (0, 0, width - 2, height - 2, true);
	
	// Clip!
	// osg.setClip ((Shape) clipRect);

	// Compute the highlighted rectangle (all those '2 +' and '-
	// 1' are post-hoc adjustments :-))
	int wWidth = (2 * hwl + 1) * cWidth; // length of window in pixels (+1 for center residue)
	
	hlWindow = new Rectangle (MARGIN + (nchars / 2 - hwl) * cWidth,
				  height / 2 - 2 * cHeight - pcHeight - halfGutter,
				  wWidth - 1,
				  2 * cHeight + 3);

	// this class has a second highlighted box to draw, for the revcomp'ed sequence.
	rhlWindow = new Rectangle (MARGIN + (nchars / 2 - hwl) * cWidth,
				   height / 2 + pcHeight + halfGutter,
				   wWidth - 1,
				   2 * cHeight + 3);

	

    }

    void highlightWindow ()
    {
	super.highlightWindow (); // this draws the first box, for the 2 input sequences
	// this draws the boy for the second alignment (revcomp'd)
	osg.setColor (HILITWINCOLOR);
	osg.drawRect ( rhlWindow.x, rhlWindow.y, rhlWindow.width, rhlWindow.height);
    }
    
    int matchColor (char c1, char c2)
    {
	//int score = mat.score (c1, c2);

	if (c1 == c2 && c1 != ' ')
	    return MATCH;
	else
	    return NEUTRAL;
   } 
}
	
