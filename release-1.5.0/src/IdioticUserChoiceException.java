// When the user chooses stupid parameters :-)

public class IdioticUserChoiceException extends Exception
{
    IdioticUserChoiceException () {}
    IdioticUserChoiceException (String msg)
    {
	super (msg);
    }
}	
