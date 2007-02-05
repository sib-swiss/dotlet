// Identity matrix

public class Identity extends ComparisonMatrix
{
    public static final int SIZE = 32; // let's try with a power of 2...

    protected int match;
    protected int mismatch;
    
    public Identity ()
    {
	matrix = new int [ SIZE ][ SIZE ];

	match = 1;
	mismatch = 0;
	
	for (int i = 0; i < SIZE; i++)
	    matrix[ i ][ i ] = match; // others should already be zero
    }

    public Identity (int ma, int mi)
    {
	match = ma;
	mismatch = mi;
	
	matrix = new int [ SIZE ][ SIZE ];
	
	for (int i = 0; i < SIZE; i++)
	    for (int j = 0; j < SIZE; j++)
		if (i == j)
		    matrix[ i ][ j ] = match;
		else
		    matrix[ i ][ j ] = mismatch;
    }

    
    public int getMax () { return match; }
    public int getMin () { return mismatch; }
    
    public int score (char hc, char vc)
    {
	// no longer used?
	
	if (hc == vc)	
	    return 1;
	else
	    return 0;
    }
}

