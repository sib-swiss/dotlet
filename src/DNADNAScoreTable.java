// ScoreTable - constructs a dot plot

/*import ComparisonMatrix;
import IdioticUserChoiceException;
import DNA;
import UnknownCodonException;
import VisualByteMap;*/

public class DNADNAScoreTable extends ScoreTable
{
    char [] ha;			// the characters of the horizontal seq
    char [] va;			// ... vertical
    char [] cha;		// ... horizontal, complemented
    int vbmhl;			// horizontal length of byte map

    // cha is not reverse-complemented, because it needs to be scanned
    // from the end, which amounts to reversing it twice.
    
    // Constructor
    DNADNAScoreTable (String h, String v, int w, ComparisonMatrix m, VisualByteMap vbm)
	throws IdioticUserChoiceException,
	       UnknownCodonException
    {
	super (w, m, vbm);

	ha = h.toCharArray ();
	va = v.toCharArray ();
	cha = DNA.revcomp(h).toCharArray ();

	hl = ha.length;
	vl = va.length;
	
	// subtract 'A' from all chars, so 'A' -> 0, etc.
	for (int i = 0; i < hl; i++)
	    {
		ha[ i ] -= 'A';
		cha[ i ] -= 'A';
	    }

	for (int i = 0; i < vl; i++)
	    va[ i ] -= 'A';

	vbmhl = hl - 2 * w;
	visualByteMap.setMapSize (vbmhl, vl - 2 * w, minScore, maxScore);

	System.out.println ("Building map (" +  (hl - 2 * w) + "," + (vl - 2 * w) + ")...");
	// fillMap (); left out of ctor will be called asynchronously
	System.out.println ("done, let's have a peek:");
	// debugPrint ();
    }

   void fillMap ()
    {
	int wl = 2 * hwl;
	int di, dj;		// diagonal indices
	int tmp;
	int tmp_c;
	int prevValue;
	int prevValue_c;	// previous value for the complementary strand
	
	// progressBar = new ProgressBar (hl * vl);
	
	// diagonals from top row
	for (int i = hwl; i < hl - hwl; i++)
	{
		prevValue = w_score (i, hwl);
		prevValue_c = w_score_c (i, hwl);
		
		visualByteMap.orData (i - hwl, 0, prevValue);
		visualByteMap.orData (vbmhl - (i - hwl) - 1, 0, prevValue_c);
		
		for (di = i + 1 - hwl, dj =  1;
		     di + wl < hl && dj + wl < vl;
		     di++, dj++)
		    {
			tmp = prevValue + r_score (di + wl, dj + wl)
			    - r_score (di - 1, dj - 1);
			tmp_c = prevValue_c + r_score_c (di + wl, dj + wl)
			    - r_score_c (di - 1, dj - 1);


			visualByteMap.orData (di, dj, tmp);
			prevValue = tmp;

			visualByteMap.orData (vbmhl - di - 1, dj, tmp_c);
			prevValue_c = tmp_c;
			// System.out.println ("M (" + (vbmhl - di) + "," + dj + ")");
			// debugMatrix[ vbmhl - di ][ dj ] = tmp_c;

			// progressBar.tick();
		    }
	    }

	System.out.println ("Done upper triangle.");
	
	// diagonals from left column
	// first column

	for (int j = hwl + 1; j < vl - hwl; j++)
	    {
		prevValue = w_score (hwl, j);
		prevValue_c = w_score_c (hwl, j);
		
		visualByteMap.orData (0, j - hwl, prevValue);
		visualByteMap.orData (vbmhl - 1,
				       j - hwl,
				       prevValue_c);
		
		for (di = 1, dj = j + 1 - hwl;
		     di + wl < hl && dj + wl < vl;
		     di++, dj++)
		    {
			tmp = prevValue + r_score (di + wl, dj + wl)
			    - r_score (di - 1, dj - 1);
			tmp_c = prevValue_c + r_score_c (di + wl, dj + wl)
			    - r_score_c (di - 1, dj - 1);

			visualByteMap.orData (di, dj, tmp);
			prevValue = tmp;

			visualByteMap.orData (vbmhl - di - 1, dj, tmp_c);
			prevValue_c = tmp_c;

			// debugMatrix[ vbmhl - di ][ dj ] = tmp_c;
			// progressBar.tick ();
		    }
	    }
	    // progressBar.dispose ();
    }

    // Window score for the complement
    int w_score_c (int i, int j)
    {
	int sc = 0;
	
	for (int p = -hwl; p <= hwl; p++)
	    sc += r_score_c (i + p, j + p);

	return sc;
    }
    
    // Score of a point (i, j) ("residue score")
    int r_score (int i, int j)
    {
	return  matrixArray[ ha[ i ] ][ va[ j ] ];
    }

        // Score of a point (i, j) ("residue score")
    int r_score_c (int i, int j)
    {
	return  matrixArray[ cha[ i ] ][ va[ j ] ];
    }
}



				  

    
	

    
