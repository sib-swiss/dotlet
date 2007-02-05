// ScoreTable - constructs a dot plot

//import java.awt.Frame;

/*import ComparisonMatrix;
import IdioticUserChoiceException;
import DNA;
import UnknownCodonException;
import VisualByteMap;*/


/*
  There are two possible coordinate systems here: one for the table
  we're filling, and another for the sequences. They are generally not
  the same, except when the window size is 0. In this version we use
  exclusively _sequence_ coordinates, that means the table has some
  extra padding around it - margins, as it were, of half the window
  length.
*/  

/*
  And, yes I KNOW there are plenty of ways to make this code much
  quicker and harder to understand. I'll rely on the Almighty
  Compiler for this... :-)
*/


public abstract class ScoreTable implements Runnable
{
    protected VisualByteMap visualByteMap;
    protected int hwl;		// half window length
    protected int maxScore;
    protected int minScore;
    protected int [][] matrixArray;
    protected int hl;
    protected int vl;
    protected int ticks;	// tracks the percent completedness
    // protected ProgressBar progressBar;
    
    // Constructor
    ScoreTable (int w, ComparisonMatrix m, VisualByteMap vbm)
    {
	visualByteMap = vbm;
	matrixArray = m.getArray ();
	hwl = w;

	maxScore = m.getMax() * (2 * hwl + 1);
	minScore = m.getMin() * (2 * hwl + 1);

    }
    
    public void run () 
    { 
	System.out.println ("run() - calling fillMap()...");
	fillMap (); 
	System.out.println ("run() - Done.");
    }
    
    void fillMap ()
    {
	int wl = 2 * hwl;
	int di, dj;		// diagonal indices
	int tmp;
	int prevValue;

	System.out.println ("Starting upper triangle.");
	// progressBar = new ProgressBar (wl * hl);	

	// diagonals from top row
	for (int i = hwl; i < hl - hwl; i++)
	    {
		// System.out.println ("Diagonal " + i);

		prevValue = w_score(i, hwl);
		visualByteMap.orData (i - hwl, 0, prevValue);

		// System.out.println ("Initial value: " + prevValue);        
		
		for (di = i + 1 - hwl, dj =  1;
		     di + wl < hl && dj + wl < vl;
		     di++, dj++)
		    {
			tmp = prevValue + r_score (di + wl, dj + wl)
			    - r_score (di - 1, dj - 1);

			// System.out.print ("M(" + di + "," + dj + ") = " +
			// tmp);
			
			visualByteMap.orData (di, dj, tmp);

			// System.out.println ("... data set.");
			
			prevValue = tmp;

			// progressBar.tick();
		    }
	    }

	System.out.println ("Upper triangle done.");
	
	// diagonals from left column
	// first column
	for (int j = hwl; j < vl - hwl; j++)
	    {
		prevValue = w_score (hwl, j);
		visualByteMap.orData (0, j - hwl, prevValue);
		
		for (di = 1, dj = j + 1 - hwl;
		     di + wl < hl && dj + wl < vl;
		     di++, dj++)
		    {
			tmp = prevValue + r_score (di + wl, dj + wl)
			    - r_score (di - 1, dj - 1);
			
			visualByteMap.orData (di, dj, tmp);
			prevValue = tmp;

			// progressBar.tick ();
		    }
	    }
    System.out.println ("Lower triangle done.");
    // progressBar.dispose();
    }


    
    // Internal methods
    // score of a window of centered on (i,j), and of half-size hwl ("window score")
    int w_score (int i, int j)
    {
	int sc = 0;
	
	for (int p = -hwl; p <= hwl; p++)
	    sc += r_score (i + p, j + p);

	return sc;
    }
    
    abstract int r_score (int i, int j);

}    

