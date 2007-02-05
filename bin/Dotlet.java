import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;


/**
 * Dotlet
 * @author omartin
 * @version 1.5 
 */
public class Dotlet extends Applet
{
  private VisualByteMap visualByteMap;
  private DotterPanel dotterPanel;
  private InputPanel inputPanel;
  private ScalePanel scalePanel;
  private SeqPairPanel seqPairPanel;
  //private Panel widgetPanel;
  private InfoPanel infoPanel;
  private KeyHandler keyHandler;
  
  /*modification by Olivier*/
  private boolean isApplet = true;
  /*end modification*/
  
  public final static Color BRIGHT_BLUE=new Color(50,150,255);
  public final static Color GREY_PINK=new Color(255,0,255);
  public final static Color GREY_BLUE=BRIGHT_BLUE.brighter();

  public void init(){
    visualByteMap=new VisualByteMap();
    dotterPanel=new DotterPanel(this);
    infoPanel=new InfoPanel();
    scalePanel=new ScalePanel(visualByteMap,dotterPanel,infoPanel,this);
    seqPairPanel=new SeqPairPanel(dotterPanel);
    dotterPanel.setSeqPairPanel(seqPairPanel);
    inputPanel=new InputPanel(visualByteMap,dotterPanel,scalePanel,seqPairPanel,infoPanel);
    setLayout(new BorderLayout(5,5));
    add("Center",inputPanel);
    Panel p=new Panel();
    add("South",p);
    p.setLayout(new BorderLayout(5,5));
    p.add("West",dotterPanel);
    Panel p2=new Panel();
    p2.setLayout(new BorderLayout(5,5));
    p2.add("Center",infoPanel);
    p2.add("South",scalePanel);
    p.add("East",p2);
    p.add("South",seqPairPanel);
    dotterPanel.displayMessage("Input one or more sequence(s)");
    
    keyHandler=new KeyHandler(dotterPanel);
    inputPanel.addKeyListener(keyHandler);
    dotterPanel.addKeyListener(keyHandler);
    seqPairPanel.addKeyListener(keyHandler);
    scalePanel.addKeyListener(keyHandler);
 
    int count=1;
    String name=""; // to enter the loop
    String seq;
    try{ // case we are not in a browser!
      while(name!=null){
	name=getParameter("name"+String.valueOf(count));
	seq=getParameter("seq"+String.valueOf(count));
	if(name!=null && seq!=null){
	  inputPanel.addSequence(name,seq);
	}
	count++;
      }
    }
    catch(Exception e){
    }
  }
  public static void main(String[] args){
    Frame frame=new AppletFrame("Dotlet");
    frame.setSize(630,580);
    Dotlet dotlet=new Dotlet();
    dotlet.init();
    
    /*modification by Olivier*/
    dotlet.setIsApplet(false);
    /*end modification*/
    
    frame.add(dotlet);
    frame.setVisible(true);
  }
  
  /*modification by Olivier*/
  public boolean isApplet()
  {
	  return this.isApplet;
  }
  
  public void setIsApplet(boolean status)
  {
	  this.isApplet = status;
  }
  /*end modification*/
}

class AppletFrame extends Frame implements WindowListener{
  
  AppletFrame(String title){
    super(title);
    addWindowListener(this);
  }
  public void windowActivated(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowClosing(WindowEvent e){
   dispose();
   System.exit(0);
  }
  public void windowDeactivated(WindowEvent e) {} 
  public void windowDeiconified(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {} 
}
