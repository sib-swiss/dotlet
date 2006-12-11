//import java.awt.Image;
import java.awt.Color;
//import java.awt.FontMetrics;
import java.awt.Graphics;
//import java.awt.Dimension;
//import java.awt.Font;
import java.awt.Rectangle;
//import java.awt.Shape;

//import SeqPairWidget;

public class DNAProteinWidget extends SeqPairWidget
{
    // This widget is for displaying a DNA vs. protein comparison, the
    // three translated frames as horizontal sequences.

    static final int half_gutter = 2; // separates the 3 frames from the protein sequence
    
    String frame1;		// first frame
    String frame2;		// guess what ?
    String frame3;
    
    
    DNAProteinWidget (String hn, String f1, String f2, String f3, String vn, String v, ComparisonMatrix m, int w)
    {
	super ();

	hsName = hn;
	vsName = vn;
	vs = v;
	mat = m;
	hwl = w;
	
    	StringBuffer sb1 = new StringBuffer (f1);
    	StringBuffer sb2 = new StringBuffer (f2);
    	StringBuffer sb3 = new StringBuffer (f3);

	// we take frame 1 from the third aa, frame 2 from the second,
	// and frame 3 from the first.

	System.out.println (sb1);
	
	// replace '[' ('Z' + 1) by '*' (STOP codon)	
	for (int i = 0; i < sb1.length(); i++)
	    if (sb1.charAt (i)  == '[')
	    	sb1.setCharAt (i, '*');
	for (int i = 0; i < sb2.length(); i++)
	    if (sb2.charAt (i)  == '[')
	    	sb2.setCharAt (i, '*');
	for (int i = 0; i < sb3.length(); i++)
	    if (sb3.charAt (i)  == '[')
	    	sb3.setCharAt (i, '*');
		

	frame1 = sb1.toString().substring (2, f1.length ());
	frame2 = sb2.toString().substring (1, f2.length ());
	frame3 = sb3.toString().substring (0, f3.length ());
	
	System.out.println (frame1);

	System.out.println ("  " + frame1);
	System.out.println ("  " + frame2);
	System.out.println ("  " + frame3);

	// now, the frames still have different lengths, and we clip
	// them to the length of frame 1:

	frame2 = frame2.substring (0, frame1.length ());
	frame3 = frame3.substring (0, frame1.length ());

	System.out.println (frame1);
	System.out.println (frame2);
	System.out.println (frame3);

	hl = frame1.length ();
	vl = vs.length ();
	
	hpos = hl / 2;
	vpos = vl / 2;

	matrixMax = mat.getMax ();
	matrixMin = mat.getMin ();
    }
    
    public void paint (Graphics g)
    {
	// Offscreen graphics
	if (im == null)
	    newSize ();
	
	// Extract the substrings to be displayed
	String f1ss = displayedSubstring (frame1, hpos);
	String f2ss = displayedSubstring (frame2, hpos);
	String f3ss = displayedSubstring (frame3, hpos);
	String vss = displayedSubstring (vs, vpos);
	
	clearClipRect (); // reset the drawing area

	// draw the sequences
	osg.setFont (f);
	osg.setColor (Color.black);

	// the frames are drawn char by char, according to the match
	// with the vertical seq.
	int matches [] = new int [ Math.min (f1ss.length (), vss.length ()) ];
	drawSubSequence3f (f1ss, vss, height / 2 - cHeight - half_gutter, matches);
	drawSubSequence3f (f2ss, vss, height / 2 - half_gutter, matches);
	drawSubSequence3f (f3ss, vss, height / 2 + cHeight - half_gutter, matches);
	drawSubSequence3f (vss, height / 2 + 2 * cHeight + half_gutter, matches);
	    
	highlightWindow (); // draw the comparison window

	drawLabel (hsName + " (translated) ", " " + hpos,
		    height / 2 - 2 * cHeight - half_gutter - 4);
	drawLabel (vsName + " ", " " + vpos, height / 2 + 3 * cHeight + half_gutter
	+ 7);
	
	// And finally:
	g.drawImage (im, 0, 0, this);
    }


    // These are auxiliaries for paint ()
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

	// Up to here, it's the same as for the Prot-Prot widget. Now,
	// things begin to change, the main difference being that
	// there are 4 seqs to display instead of 2.
	
	/*
	// Compute clipping rectangle
	//   *** the computed height seems to be wrong, but I don't
	//   see why, hence I adjust it with '+4'...
	clipRect = new Rectangle (MARGIN,
				  height / 2 - 2 * cHeight - pcHeight - half_gutter,
				  writeableWidth,
				  4 * cHeight + 2 * pcHeight + 2 * half_gutter + 4);
	*/
	// debug
	// osg.drawRect (clipRect.x, clipRect.y, clipRect.width, clipRect.height);
	
	// Do this before the clip...
	osg.draw3DRect (0, 0, width - 2, height - 2, true);
	
	// Clip!
	// osg.setClip ((Shape) clipRect);

    	int wWidth = (2 * hwl + 1) * cWidth;
	// length of window in pixels (+1 for center residue)
	hlWindow = new Rectangle (MARGIN + (nchars / 2 - hwl) * cWidth,
				  height / 2 - 2 * cHeight - half_gutter,
				  wWidth - 1,
				  4 * cHeight + 2 * half_gutter + 4);
    }
}
	
