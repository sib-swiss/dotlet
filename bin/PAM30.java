// PAM30.java  

public class PAM30 extends ComparisonMatrix
{
  PAM30 ()
    {
      int tmp [][] =
	{
		{  6, -3, -6, -3, -2, -8, -2, -7, -5,-17, -7, -6, -5, -4,-17, -2, -4, -7,  0, -1,-17, -2,-13, -3, -8, -3,-17},
		{ -3,  6,-12,  6,  1,-10, -3, -1, -6,-17, -2, -9,-10,  6,-17, -7, -3, -7, -1, -3,-17, -8,-10, -5, -6,  0,-17},
		{ -6,-12, 10,-14,-14,-13, -9, -7, -6,-17,-14,-15,-13,-11,-17, -8,-14, -8, -3, -8,-17, -6,-15, -9, -4,-14,-17},
		{ -3,  6,-14,  8,  2,-15, -3, -4, -7,-17, -4,-12,-11,  2,-17, -8, -2,-10, -4, -5,-17, -8,-15, -5,-11,  1,-17},
		{ -2,  1,-14,  2,  8,-14, -4, -5, -5,-17, -4, -9, -7, -2,-17, -5,  1, -9, -4, -6,-17, -6,-17, -5, -8,  6,-17},
		{ -8,-10,-13,-15,-14,  9, -9, -6, -2,-17,-14, -3, -4, -9,-17,-10,-13, -9, -6, -9,-17, -8, -4, -8,  2,-13,-17},
		{ -2, -3, -9, -3, -4, -9,  6, -9,-11,-17, -7,-10, -8, -3,-17, -6, -7, -9, -2, -6,-17, -5,-15, -5,-14, -5,-17},
		{ -7, -1, -7, -4, -5, -6, -9,  9, -9,-17, -6, -6,-10,  0,-17, -4,  1, -2, -6, -7,-17, -6, -7, -5, -3, -1,-17},
		{ -5, -6, -6, -7, -5, -2,-11, -9,  8,-17, -6, -1, -1, -5,-17, -8, -8, -5, -7, -2,-17,  2,-14, -5, -6, -6,-17},
		{-17,-17,-17,-17,-17,-17,-17,-17,-17,  1,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17},
		{ -7, -2,-14, -4, -4,-14, -7, -6, -6,-17,  7, -8, -2, -1,-17, -6, -3,  0, -4, -3,-17, -9,-12, -5, -9, -4,-17},
		{ -6, -9,-15,-12, -9, -3,-10, -6, -1,-17, -8,  7,  1, -7,-17, -7, -5, -8, -8, -7,-17, -2, -6, -6, -7, -7,-17},
		{ -5,-10,-13,-11, -7, -4, -8,-10, -1,-17, -2,  1, 11, -9,-17, -8, -4, -4, -5, -4,-17, -1,-13, -5,-11, -5,-17},
		{ -4,  6,-11,  2, -2, -9, -3,  0, -5,-17, -1, -7, -9,  8,-17, -6, -3, -6,  0, -2,-17, -8, -8, -3, -4, -3,-17},
		{-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,  1,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17},
		{ -2, -7, -8, -8, -5,-10, -6, -4, -8,-17, -6, -7, -8, -6,-17,  8, -3, -4, -2, -4,-17, -6,-14, -5,-13, -4,-17},
		{ -4, -3,-14, -2,  1,-13, -7,  1, -8,-17, -3, -5, -4, -3,-17, -3,  8, -2, -5, -5,-17, -7,-13, -5,-12,  6,-17},
		{ -7, -7, -8,-10, -9, -9, -9, -2, -5,-17,  0, -8, -4, -6,-17, -4, -2,  8, -3, -6,-17, -8, -2, -6,-10, -4,-17},
		{  0, -1, -3, -4, -4, -6, -2, -6, -7,-17, -4, -8, -5,  0,-17, -2, -5, -3,  6,  0,-17, -6, -5, -3, -7, -5,-17},
		{ -1, -3, -8, -5, -6, -9, -6, -7, -2,-17, -3, -7, -4, -2,-17, -4, -5, -6,  0,  7,-17, -3,-13, -4, -6, -6,-17},
		{-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,  1,-17,-17,-17,-17,-17,-17},
		{ -2, -8, -6, -8, -6, -8, -5, -6,  2,-17, -9, -2, -1, -8,-17, -6, -7, -8, -6, -3,-17,  7,-15, -5, -7, -6,-17},
		{-13,-10,-15,-15,-17, -4,-15, -7,-14,-17,-12, -6,-13, -8,-17,-14,-13, -2, -5,-13,-17,-15, 13,-11, -5,-14,-17},
		{ -3, -5, -9, -5, -5, -8, -5, -5, -5,-17, -5, -6, -5, -3,-17, -5, -5, -6, -3, -4,-17, -5,-11, -5, -7, -5,-17},
		{ -8, -6, -4,-11, -8,  2,-14, -3, -6,-17, -9, -7,-11, -4,-17,-13,-12,-10, -7, -6,-17, -7, -5, -7, 10, -9,-17},
		{ -3,  0,-14,  1,  6,-13, -5, -1, -6,-17, -4, -7, -5, -3,-17, -4,  6, -4, -5, -6,-17, -6,-14, -5, -9,  6,-17},
		{-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,-17,  1},
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
	    