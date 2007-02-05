// Blosum30.java  

public class Blosum30 extends ComparisonMatrix
{
  Blosum30 ()
    {
      int tmp [][] =
	{
		{  4,  0, -3,  0,  0, -2,  0, -2,  0, -7,  0, -1,  1,  0, -7, -1,  1, -1,  1,  1, -7,  1, -5,  0, -4,  0, -7},
		{  0,  5, -2,  5,  0, -3,  0, -2, -2, -7,  0, -1, -2,  4, -7, -2, -1, -2,  0,  0, -7, -2, -5, -1, -3,  0, -7},
		{ -3, -2, 17, -3,  1, -3, -4, -5, -2, -7, -3,  0, -2, -1, -7, -3, -2, -2, -2, -2, -7, -2, -2, -2, -6,  0, -7},
		{  0,  5, -3,  9,  1, -5, -1, -2, -4, -7,  0, -1, -3,  1, -7, -1, -1, -1,  0, -1, -7, -2, -4, -1, -1,  0, -7},
		{  0,  0,  1,  1,  6, -4, -2,  0, -3, -7,  2, -1, -1, -1, -7,  1,  2, -1,  0, -2, -7, -3, -1, -1, -2,  5, -7},
		{ -2, -3, -3, -5, -4, 10, -3, -3,  0, -7, -1,  2, -2, -1, -7, -4, -3, -1, -1, -2, -7,  1,  1, -1,  3, -4, -7},
		{  0,  0, -4, -1, -2, -3,  8, -3, -1, -7, -1, -2, -2,  0, -7, -1, -2, -2,  0, -2, -7, -3,  1, -1, -3, -2, -7},
		{ -2, -2, -5, -2,  0, -3, -3, 14, -2, -7, -2, -1,  2, -1, -7,  1,  0, -1, -1, -2, -7, -3, -5, -1,  0,  0, -7},
		{  0, -2, -2, -4, -3,  0, -1, -2,  6, -7, -2,  2,  1,  0, -7, -3, -2, -3, -1,  0, -7,  4, -3,  0, -1, -3, -7},
		{ -7, -7, -7, -7, -7, -7, -7, -7, -7,  1, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7},
		{  0,  0, -3,  0,  2, -1, -1, -2, -2, -7,  4, -2,  2,  0, -7,  1,  0,  1,  0, -1, -7, -2, -2,  0, -1,  1, -7},
		{ -1, -1,  0, -1, -1,  2, -2, -1,  2, -7, -2,  4,  2, -2, -7, -3, -2, -2, -2,  0, -7,  1, -2,  0,  3, -1, -7},
		{  1, -2, -2, -3, -1, -2, -2,  2,  1, -7,  2,  2,  6,  0, -7, -4, -1,  0, -2,  0, -7,  0, -3,  0, -1, -1, -7},
		{  0,  4, -1,  1, -1, -1,  0, -1,  0, -7,  0, -2,  0,  8, -7, -3, -1, -2,  0,  1, -7, -2, -7,  0, -4, -1, -7},
		{ -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7,  1, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7},
		{ -1, -2, -3, -1,  1, -4, -1,  1, -3, -7,  1, -3, -4, -3, -7, 11,  0, -1, -1,  0, -7, -4, -3, -1, -2,  0, -7},
		{  1, -1, -2, -1,  2, -3, -2,  0, -2, -7,  0, -2, -1, -1, -7,  0,  8,  3, -1,  0, -7, -3, -1,  0, -1,  4, -7},
		{ -1, -2, -2, -1, -1, -1, -2, -1, -3, -7,  1, -2,  0, -2, -7, -1,  3,  8, -1, -3, -7, -1,  0, -1,  0,  0, -7},
		{  1,  0, -2,  0,  0, -1,  0, -1, -1, -7,  0, -2, -2,  0, -7, -1, -1, -1,  4,  2, -7, -1, -3,  0, -2, -1, -7},
		{  1,  0, -2, -1, -2, -2, -2, -2,  0, -7, -1,  0,  0,  1, -7,  0,  0, -3,  2,  5, -7,  1, -5,  0, -1, -1, -7},
		{ -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7,  1, -7, -7, -7, -7, -7, -7},
		{  1, -2, -2, -2, -3,  1, -3, -3,  4, -7, -2,  1,  0, -2, -7, -4, -3, -1, -1,  1, -7,  5, -3,  0,  1, -3, -7},
		{ -5, -5, -2, -4, -1,  1,  1, -5, -3, -7, -2, -2, -3, -7, -7, -3, -1,  0, -3, -5, -7, -3, 20, -2,  5, -1, -7},
		{  0, -1, -2, -1, -1, -1, -1, -1,  0, -7,  0,  0,  0,  0, -7, -1,  0, -1,  0,  0, -7,  0, -2, -1, -1,  0, -7},
		{ -4, -3, -6, -1, -2,  3, -3,  0, -1, -7, -1,  3, -1, -4, -7, -2, -1,  0, -2, -1, -7,  1,  5, -1,  9, -2, -7},
		{  0,  0,  0,  0,  5, -4, -2,  0, -3, -7,  1, -1, -1, -1, -7,  0,  4,  0, -1, -1, -7, -3, -1,  0, -2,  4, -7},
		{ -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7, -7,  1},
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
	    