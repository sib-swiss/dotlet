import java.awt.event.*;

public class KeyHandler extends KeyAdapter
{
    private DotterPanel dotterPanel;
    
    public KeyHandler(DotterPanel dp){
	dotterPanel=dp;
    }
    public void keyTyped (KeyEvent e)
    {
	switch (e.getKeyChar ())
	    {
	    case '>':
		dotterPanel.relMoveCursor(1,1);
		break;
	    case '<':
		dotterPanel.relMoveCursor(-1,-1);
		break;
	    case ']':
		dotterPanel.relMoveCursor(1,-1);
		break;
	    case '[':
		dotterPanel.relMoveCursor(-1,1);
		break;
	    }
    }
    public void keyPressed (KeyEvent e)
    {
	switch (e.getKeyCode ())
	    {
	    case KeyEvent.VK_LEFT:
		dotterPanel.relMoveCursor(-1,0);
		break;
	    case KeyEvent.VK_RIGHT:
		dotterPanel.relMoveCursor(1,0);
		break;
	    case KeyEvent.VK_UP:
		dotterPanel.relMoveCursor(0,-1);
		break;
	    case KeyEvent.VK_DOWN:
		dotterPanel.relMoveCursor(0,1);
		break;
	    }
    }
}
