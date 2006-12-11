// PAM120.java  

public class PAM120 extends ComparisonMatrix
{
  PAM120 ()
    {
      int tmp [][] =
	{
		{  3,  0, -3,  0,  0, -4,  1, -3, -1, -8, -2, -3, -2, -1, -8,  1, -1, -3,  1,  1, -8,  0, -7, -1, -4, -1, -8},
		{  0,  4, -6,  4,  3, -5,  0,  1, -3, -8,  0, -4, -4,  3, -8, -2,  0, -2,  0,  0, -8, -3, -6, -1, -3,  2, -8},
		{ -3, -6,  9, -7, -7, -6, -4, -4, -3, -8, -7, -7, -6, -5, -8, -4, -7, -4,  0, -3, -8, -3, -8, -4, -1, -7, -8},
		{  0,  4, -7,  5,  3, -7,  0,  0, -3, -8, -1, -5, -4,  2, -8, -3,  1, -3,  0, -1, -8, -3, -8, -2, -5,  3, -8},
		{  0,  3, -7,  3,  5, -7, -1, -1, -3, -8, -1, -4, -3,  1, -8, -2,  2, -3, -1, -2, -8, -3, -8, -1, -5,  4, -8},
		{ -4, -5, -6, -7, -7,  8, -5, -3,  0, -8, -7,  0, -1, -4, -8, -5, -6, -5, -3, -4, -8, -3, -1, -3,  4, -6, -8},
		{  1,  0, -4,  0, -1, -5,  5, -4, -4, -8, -3, -5, -4,  0, -8, -2, -3, -4,  1, -1, -8, -2, -8, -2, -6, -2, -8},
		{ -3,  1, -4,  0, -1, -3, -4,  7, -4, -8, -2, -3, -4,  2, -8, -1,  3,  1, -2, -3, -8, -3, -3, -2, -1,  1, -8},
		{ -1, -3, -3, -3, -3,  0, -4, -4,  6, -8, -3,  1,  1, -2, -8, -3, -3, -2, -2,  0, -8,  3, -6, -1, -2, -3, -8},
		{ -8, -8, -8, -8, -8, -8, -8, -8, -8,  1, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8},
		{ -2,  0, -7, -1, -1, -7, -3, -2, -3, -8,  5, -4,  0,  1, -8, -2,  0,  2, -1, -1, -8, -4, -5, -2, -5, -1, -8},
		{ -3, -4, -7, -5, -4,  0, -5, -3,  1, -8, -4,  5,  3, -4, -8, -3, -2, -4, -4, -3, -8,  1, -3, -2, -2, -3, -8},
		{ -2, -4, -6, -4, -3, -1, -4, -4,  1, -8,  0,  3,  8, -3, -8, -3, -1, -1, -2, -1, -8,  1, -6, -2, -4, -2, -8},
		{ -1,  3, -5,  2,  1, -4,  0,  2, -2, -8,  1, -4, -3,  4, -8, -2,  0, -1,  1,  0, -8, -3, -4, -1, -2,  0, -8},
		{ -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8,  1, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8},
		{  1, -2, -4, -3, -2, -5, -2, -1, -3, -8, -2, -3, -3, -2, -8,  6,  0, -1,  1, -1, -8, -2, -7, -2, -6, -1, -8},
		{ -1,  0, -7,  1,  2, -6, -3,  3, -3, -8,  0, -2, -1,  0, -8,  0,  6,  1, -2, -2, -8, -3, -6, -1, -5,  4, -8},
		{ -3, -2, -4, -3, -3, -5, -4,  1, -2, -8,  2, -4, -1, -1, -8, -1,  1,  6, -1, -2, -8, -3,  1, -2, -5, -1, -8},
		{  1,  0,  0,  0, -1, -3,  1, -2, -2, -8, -1, -4, -2,  1, -8,  1, -2, -1,  3,  2, -8, -2, -2, -1, -3, -1, -8},
		{  1,  0, -3, -1, -2, -4, -1, -3,  0, -8, -1, -3, -1,  0, -8, -1, -2, -2,  2,  4, -8,  0, -6, -1, -3, -2, -8},
		{ -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8,  1, -8, -8, -8, -8, -8, -8},
		{  0, -3, -3, -3, -3, -3, -2, -3,  3, -8, -4,  1,  1, -3, -8, -2, -3, -3, -2,  0, -8,  5, -8, -1, -3, -3, -8},
		{ -7, -6, -8, -8, -8, -1, -8, -3, -6, -8, -5, -3, -6, -4, -8, -7, -6,  1, -2, -6, -8, -8, 12, -5, -2, -7, -8},
		{ -1, -1, -4, -2, -1, -3, -2, -2, -1, -8, -2, -2, -2, -1, -8, -2, -1, -2, -1, -1, -8, -1, -5, -2, -3, -1, -8},
		{ -4, -3, -1, -5, -5,  4, -6, -1, -2, -8, -5, -2, -4, -2, -8, -6, -5, -5, -3, -3, -8, -3, -2, -3,  8, -5, -8},
		{ -1,  2, -7,  3,  4, -6, -2,  1, -3, -8, -1, -3, -2,  0, -8, -1,  4, -1, -1, -2, -8, -3, -7, -1, -5,  4, -8},
		{ -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8, -8,  1},
	};
	    
	matrix = tmp;
    }
    
    public int score (char hc, char vc)
    {
	try
	{
	   return matrix [ hc - 'A' ][ vc - 'A' ];
	}   
	catch (ArrayIndexOutOfBoundsException e)
        {
	   String s = e.getMessage ();
		System.out.println ("Unknown symbol: " + (Integer.parseInt (s) + 65));

	   return matrix [ 'J' - 'A' ][ 'J' - 'A' ]; // by default, return unknown codon
	}
    }
}
	    
