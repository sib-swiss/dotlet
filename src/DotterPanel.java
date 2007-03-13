import java.awt.*;
//import java.awt.image.*;
import java.awt.event.*;

public class DotterPanel extends Panel implements AdjustmentListener,ActionListener{
  
  protected ImagePanel imagePanel;
  
  private VisualByteMap visualByteMap;
  
  private Scrollbar xScrollbar=new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 100);
  private Scrollbar yScrollbar=new Scrollbar(Scrollbar.VERTICAL, 0, 1, 0, 100);;
  private String toNorthEast="/";
  private Button northEastButton=new Button(toNorthEast);
  private String toNorthWest="\\";
  private Button northWestButton=new Button(toNorthWest);
  private String toSouthEast="\\";
  private Button southEastButton=new Button(toSouthEast);
  private String toSouthWest="/";
  private Button southWestButton=new Button(toSouthWest);
  private Button southButton=new Button("");
  private Button eastButton=new Button("");
  
  public DotterPanel(Component visibleComponent){
    
    setLayout(new SquareLayout());
    imagePanel=new ImagePanel(visibleComponent);
    add("Center",imagePanel);
    add("Northeast",northEastButton);
    northEastButton.addActionListener(this);
    add("Northwest",northWestButton);
    northWestButton.addActionListener(this);
    add("Southeast",southEastButton);
    southEastButton.addActionListener(this);
    add("Southwest",southWestButton);
    southWestButton.addActionListener(this);
    add("North",xScrollbar);
    xScrollbar.addAdjustmentListener(this);
    add("West",yScrollbar);
    yScrollbar.addAdjustmentListener(this);
    imagePanel.setScrollbar(xScrollbar,yScrollbar);
    southButton.setEnabled(false);
    eastButton.setEnabled(false);
    add("South",southButton);
    add("East",eastButton);
    setEnabled(false);
  }
  public void setSeqPairPanel(SeqPairPanel spp){
    imagePanel.setSeqPairPanel(spp);
  }
  public void addKeyListener(KeyListener keyListener){
    super.addKeyListener(keyListener);
    imagePanel.addKeyListener(keyListener);
  }
  public void actionPerformed(ActionEvent e){
    int decX=20;
    int decY=20;
    if(e.getSource()==northEastButton){
      decY=-decY;
    }
    if(e.getSource()==northWestButton){
      decX=-decX;
      decY=-decY;
    }
    if(e.getSource()==southEastButton){
    }
    if(e.getSource()==southWestButton){
      decX=-decX;
    }
    imagePanel.moveImageRel(decX,decY);
    xScrollbar.setValue(imagePanel.getImageX());
    yScrollbar.setValue(imagePanel.getImageY());
  }
  public void adjustmentValueChanged(AdjustmentEvent e){
    imagePanel.moveImageTo(xScrollbar.getValue(),yScrollbar.getValue());
  }	
  public void setVisualByteMap(VisualByteMap visualByteMap){
    this.visualByteMap=visualByteMap;
    imagePanel.setVisualByteMap(visualByteMap);
    xScrollbar.setValues(0,imagePanel.getImageWidth(),0,visualByteMap.getWidth()-1);
    yScrollbar.setValues(0,imagePanel.getImageHeight(),0,visualByteMap.getHeight()-1);
  }
  public void displayDotPlot(int windowSize, int horizLength, int vertLength){
    imagePanel.displayDotPlot(windowSize,horizLength,vertLength);
    setEnabled(true);
  }
  public void displayMessage(String s){
    setEnabled(false);
    imagePanel.displayMessage(s);
  }
  public void setEnabled(boolean b){
    xScrollbar.setEnabled(b);
    yScrollbar.setEnabled(b);
    northEastButton.setEnabled(b);
    northWestButton.setEnabled(b);
    southEastButton.setEnabled(b);
    southWestButton.setEnabled(b);
  }
  public void requestUpdate(){
    imagePanel.requestUpdate();
  }
  public void relMoveCursor(int decX,int decY){
     setCursor(imagePanel.getCursorX()+decX,imagePanel.getCursorY()+decY);
  }
  public void setCursor(int x,int y){
    imagePanel.setCursor(x,y);
  }
  
  /*modification by Olivier*/
  public ImagePanel getImagePanel()
  {
	  return this.imagePanel;
  }
  /*end modification*/
}

