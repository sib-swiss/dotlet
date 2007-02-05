// When the user chooses stupid parameters :-)

public class UnknownCodonException extends Exception
{
    UnknownCodonException () {}
    UnknownCodonException (String msg)
    {
	super (msg);
    }
}	
