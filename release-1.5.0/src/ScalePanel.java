import java.awt.*;
import java.awt.event.*;

public class ScalePanel extends Panel implements  AdjustmentListener,MouseListener,MouseMotionListener{

  private int scaleWidth;
  private int scaleHeight;
  private VisualByteMap visualByteMap;
  private DotterPanel dotterPanel;
  private Image image=null;
  private boolean imageVisible=false;
  private int scrollSpan=100;
  private Scrollbar lowScrollbar=new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,scrollSpan+1);
  private Scrollbar highScrollbar=new Scrollbar(Scrollbar.HORIZONTAL,scrollSpan,1,0,scrollSpan+1);
  private BorderLayout borderLayout=new BorderLayout(5,5);
  private InfoPanel infoPanel;
  //  private int mouseX;
 
  public ScalePanel(VisualByteMap vbm,DotterPanel dp,InfoPanel ip,Component anyVisibleComponent){
    visualByteMap=vbm;
    dotterPanel=dp;
    infoPanel=ip;
    /*modified by Olivier*/
    //scaleWidth=visualByteMap.getScaleSize();
    //scaleHeight=visualByteMap.getScaleHeight();
    scaleWidth = VisualByteMap.getScaleSize();
    scaleHeight = VisualByteMap.getScaleHeight();
    /*end modification*/
    highScrollbar.addAdjustmentListener(this);
    lowScrollbar.addAdjustmentListener(this);
    setLayout(borderLayout);
    
    add("North",highScrollbar);
    add("South",lowScrollbar);
    // image=anyVisibleComponent.createImage(scaleWidth,scaleHeight);
    // updateScale();
    setEnabled(false);
    addMouseListener(this);
    addMouseMotionListener(this);
   }
  public void adjustmentValueChanged(AdjustmentEvent e){
    updateScale();
  }
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseMoved(MouseEvent e){}
  public void mousePressed(MouseEvent e){
    int mouseX=scrollSpan*e.getX()/scaleWidth;
    int midX=(lowScrollbar.getValue()+highScrollbar.getValue())/2;
    int decX=mouseX-midX;
    if(decX<0){
      if(lowScrollbar.getValue() >= -decX){
	if(highScrollbar.getValue() >= -decX){
	  lowScrollbar.setValue(lowScrollbar.getValue()+decX);
	  highScrollbar.setValue(highScrollbar.getValue()+decX);
	  updateScale();
	}
      }
    }
    else if(decX>0){
      if(lowScrollbar.getValue() <= scrollSpan-decX){
	if(highScrollbar.getValue() <= scrollSpan-decX){
	  lowScrollbar.setValue(lowScrollbar.getValue()+decX);
	  highScrollbar.setValue(highScrollbar.getValue()+decX);
	  updateScale();
	}
      }
    }
  }
  public void mouseDragged(MouseEvent e){
    mousePressed(e);
  } 
  public void updateScale(){
    if(image==null){
      image=this.createImage(scaleWidth,scaleHeight);
    }
    visualByteMap.setGreyScale(lowScrollbar.getValue(),highScrollbar.getValue(),scrollSpan);
    visualByteMap.scaleUpdate(image);
    setEnabled(true);
    repaint();
    dotterPanel.requestUpdate();
    infoPanel.setThreshold(lowScrollbar.getValue(),highScrollbar.getValue(),scrollSpan);
    infoPanel.setScale(visualByteMap.getLowScore(),visualByteMap.getHighScore());
    
    /*modification by Olivier*/
    //ugly as hell
    /*if(this.dotterPanel != null && this.dotterPanel.getImagePanel() != null &&
    		this.dotterPanel.getImagePanel().getSeqOutputDialog() != null)
    {
    	this.dotterPanel.getImagePanel().getSeqOutputDialog().setScales(lowScrollbar.getValue(), 
    			highScrollbar.getValue(), scrollSpan);
    }*/
    /*end modification*/
  }    
  public void paint(Graphics g){
    if(imageVisible){
      int y=highScrollbar.getSize().height+borderLayout.getVgap();
      g.drawImage(image,0,y,this);
    }
  }
  public Dimension getMinimumSize(){
    return getPreferredSize();
  }
  public Dimension getMaximumSize(){
    return getPreferredSize();
  }
  public Dimension getPreferredSize(){
    Dimension dim=highScrollbar.getPreferredSize();
    return new Dimension(scaleWidth,scaleHeight+2*dim.height+2*borderLayout.getVgap());
  }
  public void setEnabled(boolean b){
    lowScrollbar.setEnabled(b);
    highScrollbar.setEnabled(b);
    imageVisible=b;
  }
}
