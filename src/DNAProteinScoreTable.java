// ScoreTable - constructs a dot plot

/*import ComparisonMatrix;
import IdioticUserChoiceException;
import DNA;
import UnknownCodonException;
import VisualByteMap;*/

public class DNAProteinScoreTable extends ScoreTable
{
    // The DNA seq gets translated into the 3 frames
    private char [] frame1;
    private char [] frame2;
    private char [] frame3;
    private char [] va;		// vertical array

    // Constructor
    DNAProteinScoreTable (String h, String v, int w, ComparisonMatrix m,
			  VisualByteMap vbm)
			  throws UnknownCodonException
    {
	super (w, m, vbm);

	//System.out.println ("Ctor called."); //Seb: comment some useless printings
	
	// Scores are tripled since we compute a sum over the 3 frames
	System.out.println ("Scores range from " + minScore + " to " + maxScore);

	System.out.print ("Translating 3 frames...");
	// Translate h string into 3 frames as char [].
	frame1 = DNA.translate(h).toCharArray ();
	frame2 = DNA.translate(h.substring (1)).toCharArray ();
	frame3 = DNA.translate(h.substring (2)).toCharArray ();
	System.out.print (" Done.\n");
	va = v.toCharArray ();
	
	//Seb: comment some useless printings which seem to slow execution time with Opera browser !
	/*System.out.println (frame1);
	System.out.println (" " + new String(frame2));
	System.out.println ("  " + new String(frame3));*/

	// This is the length over which the 3 frames overlap
	// (I did a drawing, so believe me.)
	hl = h.length () / 3 - 2;
	vl = v.length ();

	System.out.println ("Correcting seqs.");
	
	// Substract 'A' from all chars
	for (int i = 0; i < hl; i++)
	    {
		frame1 [ i ] -= 'A';
		frame2 [ i ] -= 'A';
		frame3 [ i ] -= 'A';
	    }

	for (int i = 0; i < vl; i++)
	    va[ i ] -= 'A';

	visualByteMap.setMapSize (hl - 2 * w, vl - 2 * w, minScore, maxScore);
    }

    // Score of a point (i, j) ("residue score")
    int r_score (int i, int j)
    {
	// hs is DNA, vs is protein
	
	return Math.max (Math.max (matrixArray[ frame1[ i ]][ va[ j ] ],
	    matrixArray[ frame2[ i ]][ va[ j ] ]),
	    matrixArray[ frame3[ i ]][ va[ j ] ]);
    }
}
