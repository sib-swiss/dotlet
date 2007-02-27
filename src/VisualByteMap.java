import java.awt.Image;
import java.awt.Graphics;
//import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.image.MemoryImageSource;
import java.awt.image.IndexColorModel;
import java.awt.image.ImageObserver;

public class VisualByteMap{
  
  private int fullWidth,fullHeight;              // lengths of both sequences
  private int totalWidth,totalHeight;            // map size in pixels
  private int minValue,maxValue,rangeValue;      // values extrema and range
  private int maxX,maxY;                         // map size in submap!
  
  private static int SIZE=64;                    // edge size of square submap
  private byte[][][] data=null;                  // map data
  
  private IndexColorModel indexColorModel;       // for data rendering
  
  private static int SCALE_SIZE=256;
  private static int SCALE_HEIGHT=200;
  private int zoom=1;
  
  private byte[] greyScale=new byte[SCALE_SIZE];
  private int[] histogramX=new int[SCALE_SIZE];
  private int[] histogramY=new int[SCALE_SIZE];
  private int[] histogramLogY=new int[SCALE_SIZE];
  
  private Image mapImage; // scaleImage;
  private MemoryImageSource memoryImageSource;// scaleImageSource;
  private OptimisticObserver optimisticObserver=new OptimisticObserver();
  private boolean histogramRescaled=false;
  
  /*modified by Olivier*/
  //variables existing elsewhere but repeated here to avoid over-coupling objects
  int scaleMin;
  int scaleMax;
  /*end modification*/

  public void setZoom(int value){
    zoom=value;
  }
  public int getZoom(){
    return zoom;
  }
  public void setMapSize(int totalWidth,int totalHeight,int minValue,int maxValue){
    fullWidth=totalWidth;
    fullHeight=totalHeight;
    this.totalWidth=1+totalWidth/zoom;
    this.totalHeight=1+totalHeight/zoom;
    this.minValue=minValue;
    this.maxValue=maxValue;
    rangeValue=maxValue-minValue;
    maxX=1+(this.totalWidth-1)/SIZE;          
    maxY=1+(this.totalHeight-1)/SIZE;

    //System.out.println(zoom+" - "+maxX+" -  "+maxY);
    
    // memory allocation... may crash if structure too large
    // or more surely when the java virtual machine is not correctly implemented!
    if(data!=null){
      data=null;
      System.gc();
    }
    data=new byte[maxX][maxY][SIZE*SIZE];
    // prepare the grey scale
    for(int i=0;i<greyScale.length;i++){greyScale[i]=(byte)i;}
    indexColorModel=new IndexColorModel(8,256,greyScale,greyScale,greyScale);
    //prepare the animation
    memoryImageSource=new MemoryImageSource(SIZE,SIZE,indexColorModel,data[0][0],0,SIZE);
    memoryImageSource.setAnimated(true);
    memoryImageSource.setFullBufferUpdates(true);
    mapImage=Toolkit.getDefaultToolkit().createImage(memoryImageSource);
    // prepare the histogram
    histogramRescaled=false;
    for(int i=0;i<256;i++){
      histogramX[i]=i;
      histogramY[i]=0;
      histogramLogY[i]=-1; // "-1" means missing value !
    }
  }
  public static int getScaleSize(){
    return SCALE_SIZE;
  }
  public static int getScaleHeight(){
    return SCALE_HEIGHT;
  }
  public int getWidth(){
    return totalWidth;
  }
  public int getHeight(){
    return totalHeight;
  }
  public int getFullWidth(){
    return fullWidth;
  }
  public int getFullHeight(){
    return fullHeight;
  }
    public int getLowScore(){
	return minValue;
    }
    public int getHighScore(){
	return maxValue;
    }
   /*
    public void setData(int x,int y,int V){
       int value=255*(V-minValue)/rangeValue;
       histogramY[value]++;
       int mapX=x/SIZE;
       int mapY=y/SIZE;
       int offset=(y-mapY*SIZE)*SIZE+(x-mapX*SIZE);
       //data[mapX][mapY][offset]=(byte)value; 
       }
  */
    /*modified by Olivier*/
    public int getPixel(int x, int y)
    {
    	int result = 0;
    	x/=zoom;
        y/=zoom;
        
        int mapX=x/SIZE;
        int mapY=y/SIZE;
        int offset=(y-mapY*SIZE)*SIZE+(x-mapX*SIZE);        
  
        /*int temp = this.data.length;
        System.out.println(temp);
        System.out.println(this.data[0].length);
        System.out.println(this.data[0][0].length);*/
        
        //Sebastien: to avoid out of range selections
        if (mapX >= this.data.length)      		
        {
        	mapX=this.data.length-1;
        	//System.out.println("-");
        }
        if (mapY >= this.data[0].length)      		
        {
        	//System.out.println("+");
        	mapY=this.data[0].length-1;
        }    
        
        try
        {
        	result = this.data[mapX][mapY][offset];
        	result&=0xff;
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
        	System.err.println("ArrayIndexOutOfBoundsException " + e.getMessage());
        	System.exit(1);
        }

        return result;
    }
    /*end modification*/
    
  public void orData(int x,int y,int V){
    // System.out.println(x+","+y+","+V);
    
    int newValue=255*(V-minValue)/rangeValue;
    histogramY[newValue]++;
    x/=zoom;
    y/=zoom;
    int mapX=x/SIZE;
    int mapY=y/SIZE;
    int offset=(y-mapY*SIZE)*SIZE+(x-mapX*SIZE);
    int oldValue=data[mapX][mapY][offset];
    // this is tricky:
    // in principle bytes are signed in java
    // but they are taken unsigned by MemoryImageSource,
    // so one need to be carefull when adding unsigned bytes
    // which behave like signed bytes!
    // hence
    oldValue&=0xff;
    // is needed to make things work
    if(newValue>oldValue){
      //if(histogramY[oldValue]>0){
      //    histogramY[oldValue]--;
      // ((}
      //histogramY[newValue]++;
      data[mapX][mapY][offset]=(byte)newValue; 
    }
  }  
   
  /*
    public void addData(int x,int y,int V){
    //if(x<0 || x>=totalWidth || y<0 || y>=totalHeight){
    //    System.out.print(x+" "+y+" "+V+" "+minValue+" "+maxValue+" "+totalWidth+" "+totalHeight);
    //}
    int mapX=x/SIZE;
    int mapY=y/SIZE;
    int offset=(y-mapY*SIZE)*SIZE+(x-mapX*SIZE);
    int oldValue=data[mapX][mapY][offset];
    oldValue&=0xff;
    if(oldValue>0){
    histogramY[oldValue]--;
    }
    else{
    histogramY[0]=Math.max(0,histogramY[0]-1);
    }
    int newValue=oldValue+255*(V-minValue)/rangeValue;
    histogramY[newValue]++;
    data[mapX][mapY][offset]=(byte)(newValue);
    }
  */
  public void imageUpdate(Image image,int atX,int atY,int width,int height,ImageObserver imageObserver){
  	Graphics graphics=image.getGraphics();
  	/*modified by Olivier*/
	int stopX = Math.min(1+(atX+width)/SIZE,maxX);
	int stopY = Math.min(1+(atY+height)/SIZE,maxY);
	for(int x=atX/SIZE;x<stopX;x++)
	{
	  for(int y=atY/SIZE;y<stopY;y++)
	  {
		  memoryImageSource.newPixels(data[x][y],indexColorModel,0,SIZE);
		  graphics.drawImage(mapImage,x*SIZE-atX,y*SIZE-atY,optimisticObserver);
	  }
	}
	/*for(int x=atX/SIZE;x<Math.min(1+(atX+width)/SIZE,maxX);x++){
	  for(int y=atY/SIZE;y<Math.min(1+(atY+height)/SIZE,maxY);y++){
		  memoryImageSource.newPixels(data[x][y],indexColorModel,0,SIZE);
		  graphics.drawImage(mapImage,x*SIZE-atX,y*SIZE-atY,optimisticObserver);
		  //graphics.drawImage(mapImage,x*SIZE-atX,y*SIZE-atY,imageObserver);
	  }
	}*/
	/*end modification*/
  }
  public void scaleUpdate(Image image){
    Graphics g=image.getGraphics();
    for(int x=0;x<SCALE_SIZE;x++){
      g.setColor(new Color(indexColorModel.getRGB(x)));
      g.drawLine(x,0,x,SCALE_HEIGHT-1);
    }
    if(histogramRescaled){
      g.setColor(Dotlet.BRIGHT_BLUE);
      for(int x=0;x<SCALE_SIZE;x++){
	g.drawLine(x,histogramY[x],x,SCALE_HEIGHT);
      }
      g.setColor(Dotlet.GREY_PINK);
      for(int x=0;x<SCALE_SIZE;x++){	    
	g.drawLine(x,histogramLogY[x]-1,x,histogramLogY[x]+1);
      }
    }	     
  }
  public void prepareScale(){
    int max=0;
    int total=0;
    for(int i=0;i<SCALE_SIZE;i++){
      if(max<histogramY[i]){
	max=histogramY[i];
      }
      total+=histogramY[i];
    }
    float count;
    float ratio,maxLog,logRatio;
    for(int i=0;i<SCALE_SIZE;i++){
      count=histogramY[i];
      ratio=count/max;
      histogramY[i]=Math.round(SCALE_HEIGHT*(1-ratio));
      if(ratio>0){
	maxLog=(float)-Math.log(1/(float)max);
	logRatio=(float)-Math.log(ratio);
	histogramLogY[i]=(int)Math.round(SCALE_HEIGHT*logRatio/maxLog);	
      }
      else{
	histogramLogY[i]=-10; // means not a "log" number
      }
    }
    histogramRescaled=true;
  }
  public void setGreyScale(int low,int high,int total){
    float lowThreshold=255*low/total;  
    float highThreshold=255*high/total;
    
    /*modification by Olivier*/
    this.scaleMin = (int)lowThreshold;
    this.scaleMax = (int)highThreshold;
    /*end modification*/
    
    float value;
    for(int i=0;i<greyScale.length;i++){
      value=(i-lowThreshold)/(highThreshold-lowThreshold);
      greyScale[i]=(byte)(Math.max(0,Math.min(255,(255*value))));
    }
    indexColorModel=new IndexColorModel(8,256,greyScale,greyScale,greyScale);
  }
}

class OptimisticObserver implements ImageObserver{
  
  public boolean imageUpdate(Image image,int flags,int x,int y,int w,int h){
    return true;// really optimistic!
  }
}
