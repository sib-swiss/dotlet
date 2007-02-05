// ScoreTable - constructs a dot plot

/*import ComparisonMatrix;
import IdioticUserChoiceException;
import DNA;
import UnknownCodonException;
import VisualByteMap;*/

public class SameTypeScoreTable extends ScoreTable
{
    char [] ha;
    char [] va;
    
    // Constructor
    SameTypeScoreTable (String h, String v, int w, ComparisonMatrix m, VisualByteMap vbm)
	throws IdioticUserChoiceException,
	       UnknownCodonException
    {
		super (w, m, vbm);
	
		ha = h.toCharArray ();
		va = v.toCharArray ();
		hl = ha.length;
		vl = va.length;
		
		// subtract 'A' from all chars, so 'A' -> 0, etc.
		for (int i = 0; i < hl; i++)
		    ha[ i ] -= 'A';
	
		for (int i = 0; i < vl; i++)
		    va[ i ] -= 'A';
	
		visualByteMap.setMapSize (hl - 2 * w, vl - 2 * w, minScore, maxScore);
		
		// fillMap ();
    }

    // Score of a point (i, j) ("residue score")
    int r_score (int i, int j)
    {
    	return  matrixArray[ ha[ i ] ][ va[ j ] ];
    }
}



				  

    
	

    
