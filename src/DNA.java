// Some molbio functions

import java.util.Hashtable;
//import UnknownCodonException;

public class DNA
{
    private static Hashtable geneticCode = null;

    private static void buildCode ()
    {
		geneticCode = new Hashtable ();
	
		// System.out.print ("Filling genetic code hash table...");
		
		geneticCode.put ("TTT", new Character ('F'));
		geneticCode.put ("TTC", new Character ('F'));
		geneticCode.put ("TTA", new Character ('L'));
		geneticCode.put ("TTG", new Character ('L'));
		
		geneticCode.put ("TCT", new Character ('S'));
		geneticCode.put ("TCC", new Character ('S'));
		geneticCode.put ("TCA", new Character ('S'));
		geneticCode.put ("TCG", new Character ('S'));
	
		// I use '[' for the STOP codon, because it's just after 'Z'
		// so I can simply use a char [ 27 ][ 27 ] for the matrix, and
		// address it directly by letter.
		
		geneticCode.put ("TAT", new Character ('Y'));
		geneticCode.put ("TAC", new Character ('Y'));
		geneticCode.put ("TAA", new Character ('['));
		geneticCode.put ("TAG", new Character ('['));
	
		geneticCode.put ("TGT", new Character ('C'));
		geneticCode.put ("TGC", new Character ('C'));
		geneticCode.put ("TGA", new Character ('['));
		geneticCode.put ("TGG", new Character ('W'));
	
		geneticCode.put ("CTT", new Character ('L'));
		geneticCode.put ("CTC", new Character ('L'));
		geneticCode.put ("CTA", new Character ('L'));
		geneticCode.put ("CTG", new Character ('L'));
	
		geneticCode.put ("CCT", new Character ('P'));
		geneticCode.put ("CCC", new Character ('P'));
		geneticCode.put ("CCA", new Character ('P'));
		geneticCode.put ("CCG", new Character ('P'));
	
		geneticCode.put ("CAT", new Character ('H'));
		geneticCode.put ("CAC", new Character ('H'));
		geneticCode.put ("CAA", new Character ('Q'));
		geneticCode.put ("CAG", new Character ('Q'));
	
		geneticCode.put ("CGT", new Character ('R'));
		geneticCode.put ("CGC", new Character ('R'));
		geneticCode.put ("CGA", new Character ('R'));
		geneticCode.put ("CGG", new Character ('R'));
	
		geneticCode.put ("ATT", new Character ('I'));
		geneticCode.put ("ATC", new Character ('I'));
		geneticCode.put ("ATA", new Character ('I'));
		geneticCode.put ("ATG", new Character ('M'));
	
		geneticCode.put ("ACT", new Character ('T'));
		geneticCode.put ("ACC", new Character ('T'));
		geneticCode.put ("ACA", new Character ('T'));
		geneticCode.put ("ACG", new Character ('T'));
	
		geneticCode.put ("AAT", new Character ('N'));
		geneticCode.put ("AAC", new Character ('N'));
		geneticCode.put ("AAA", new Character ('K'));
		geneticCode.put ("AAG", new Character ('K'));
	
		geneticCode.put ("AGT", new Character ('S'));
		geneticCode.put ("AGC", new Character ('S'));
		geneticCode.put ("AGA", new Character ('R'));
		geneticCode.put ("AGG", new Character ('R'));
	
		geneticCode.put ("GTT", new Character ('V'));
		geneticCode.put ("GTC", new Character ('V'));
		geneticCode.put ("GTA", new Character ('V'));
		geneticCode.put ("GTG", new Character ('V'));
		geneticCode.put ("GTN", new Character ('V'));
		
		geneticCode.put ("GCT", new Character ('A'));
		geneticCode.put ("GCC", new Character ('A'));
		geneticCode.put ("GCA", new Character ('A'));
		geneticCode.put ("GCG", new Character ('A'));
		geneticCode.put ("GCN", new Character ('A'));
	
		geneticCode.put ("GAT", new Character ('D'));
		geneticCode.put ("GAC", new Character ('D'));
		geneticCode.put ("GAA", new Character ('E'));
		geneticCode.put ("GAG", new Character ('E'));
	
		geneticCode.put ("GGT", new Character ('G'));
		geneticCode.put ("GGC", new Character ('G'));
		geneticCode.put ("GGA", new Character ('G'));
		geneticCode.put ("GGG", new Character ('G'));
		geneticCode.put ("GGN", new Character ('G'));
	
		/*
		System.out.println (" Done.");
		System.out.println ("Now a few tests:");
		System.out.println ("What does 'GGG' stand for? ... " + geneticCode.get ("GGG"));
		*/
    }
	

    // Tests if a string is nucleotide
    public static  boolean isDNA (String seq)
    {
		int l = seq.length ();
		double ATGC = 0.0;
		
		for (int i = 0; i < l; i++)
		{
		    if (seq.charAt (i) == 'A' ||
			seq.charAt (i) == 'T' ||
			seq.charAt (i) == 'G' ||
			seq.charAt (i) == 'C' ||
			seq.charAt (i) == 'U') //Seb: Add Uracil management

		    ++ATGC;
		}
		//System.out.println("Done");
	
		// We say it's DNA if there is more than 80% ATGC
		if (ATGC / (double) l > 0.8)
		    return true;
		else
		    return false;
    }

    public static String translate (String seq) throws UnknownCodonException
    {
		int l = seq.length ();
		StringBuffer sb = new StringBuffer (l / 3);
	
		// Note the integer division here: unless there are 3 * n nucleotides, l > 3 * (l / 3) !
		for (int i = 0; i < 3 * (l / 3); i += 3)
		    {
			String codon = seq.substring (i, i + 3);
			sb.append (getAA (codon));
		    }
	
		return new String (sb);
    }
		    
    public static String complement (String seq)
    {
    	StringBuffer sb = new StringBuffer ();
	
		for (int i = 0; i < seq.length (); i++)
		    switch (seq.charAt (i))
			{
			case 'T':
			    sb.append ('A');
			    break;
			case 'C':
			    sb.append ('G');
			    break;
			case 'A':
			    sb.append ('T');
			    break;
			case 'G':
			    sb.append ('C');
			    break;
			default:
			    sb.append ('N');
			}
	
		return sb.toString ();
    }
    
    public static String revcomp (String seq)
    {
	StringBuffer sb = new StringBuffer ();
	
	for (int i = seq.length () - 1; i >= 0; i--)
	    switch (seq.charAt (i))
		{
		case 'T':
		    sb.append ('A');
		    break;
		case 'C':
		    sb.append ('G');
		    break;
		case 'A':
		    sb.append ('T');
		    break;
		case 'G':
		    sb.append ('C');
		    break;
		default:
		    sb.append ('N');
		}

	return sb.toString ();
    }
    
    public static Character getAA (String codon) throws UnknownCodonException
    {
	Object aa;

	if (geneticCode == null)
	    buildCode ();
	
	// if codon contains anything else than A, T, G or C, return X.
	for (int i = 0; i < 3; i++)
	    if (codon.charAt (i) != 'A' &&
		codon.charAt (i) != 'C' &&
		codon.charAt (i) != 'G' &&
		codon.charAt (i) != 'T')
		return new Character('X');

	aa = geneticCode.get (codon);

	if (aa == null)
	    throw new UnknownCodonException ("Codon " + codon + " is unknown.");

	System.out.println ("Codon '" + codon + "' translates into " + aa); 
	
	return (Character) aa;
    }
	
}
