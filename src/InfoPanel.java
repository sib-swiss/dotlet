import java.awt.*;

public class InfoPanel extends Panel {
    
    private String horizontalSequence=null;
    private String verticalSequence=null;
    private String matrix="";
    private String window="";
    private String zoom="";
    private int lowThreshold=0;
    private int highThreshold=100;
    private String lowScore="";
    private String highScore="";
    
    public void setInfo(String h,String v,String m,String w,String z){
	horizontalSequence=h;
	verticalSequence=v;
	matrix=m;
	window=w;
	zoom=z;
	repaint();
    }
    public void setThreshold(int low,int high,int span){ 
	lowThreshold=100*low/span;
	highThreshold=100*high/span;
	repaint();
    }
    public void setScale(int low,int high){
	lowScore=String.valueOf(low);
	highScore=String.valueOf(high);
    }
    public void paint(Graphics g){
	Font pf = new Font ("Dialog", Font.PLAIN, 12);
	g.setFont(pf);
	if(horizontalSequence==null){return;}
	g.drawString("horizontal: "+horizontalSequence,10,14);
	g.drawString("vertical: "+verticalSequence,10,28);
	g.drawString("matrix: "+matrix,10,42);
	g.drawString("sliding window: "+window,10,54);
	g.drawString("zoom: "+zoom,10,66);
	g.drawString("score range: "+lowScore+" to "+highScore,10,78);
	g.drawString("gray scale: "+String.valueOf(lowThreshold)+"% - "+String.valueOf(highThreshold)+"%",10,90);
	
    }
    
    /*modified by Olivier*/
    public String[] getInfo()
    {
    	String[] data = new String[6];
    	
    	data[0] = this.matrix;
    	data[1] = this.window;
    	data[2] = this.lowScore+" to "+this.highScore;
    	data[3] = String.valueOf(this.lowThreshold)+"% - "+String.valueOf(this.highThreshold)+"%";
    	data[4] = this.horizontalSequence;
    	data[5] = this.verticalSequence;
    	
    	return data;
    }
    /*end modification*/
}
