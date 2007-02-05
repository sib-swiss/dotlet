// Abstraction of a nucleotide or protein scoring matrix (PAM, BLOSUM, etc)

public abstract class ComparisonMatrix
{
    int matrix [][];
    
    ComparisonMatrix () {}

    abstract public int score (char c1, char c2);

    public int score (Character C1, Character C2)
    {
	return score (C1.charValue(), C2.charValue());
    }
    
    public int getMax ()
    {
	int imax = matrix.length;
	int jmax = matrix[ 0 ].length;

	int max = matrix[ 0 ][ 0 ];
	
	for (int i = 0; i < imax; i++)
	    for (int j = 0; j < jmax; j++)
		if (matrix[ i ][ j ] > max)
		    max = matrix[ i ][ j ];

	return max;
    }

    public int getMin ()
    {
	int imax = matrix.length;
	int jmax = matrix[ 0 ].length;

	int min = matrix[ 0 ][ 0 ];
	
	for (int i = 0; i < imax; i++)
	    for (int j = 0; j < jmax; j++)
		if (matrix[ i ][ j ] < min)
		    min = matrix[ i ][ j ];

	return min;
    }

    public int [][] getArray () {return matrix;}
}

