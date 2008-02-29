import java.applet.Applet;
import java.awt.*;
//import java.awt.image.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.text.DateFormat;
import java.util.Date;


public class ImagePanel extends Panel implements MouseListener,
		MouseMotionListener, Printable {

	private Image image = null;

	private VisualByteMap visualByteMap;

	private static final int maxImageWidth = 300;
	private static final  int maxImageHeight = 300;

	private int imageWidth, imageHeight;

	private int posX = 0; // VisualByteMap x offset in pixels
	private int posY = 0; // VisualByteMap y offset in pixels

	private int cursorX = 0; // in sequence coordinates
	private int cursorY = 0; // "

	private UpdateTrigger updateTrigger;

	private boolean updateRequested = false;
	private boolean updating = false;
	private boolean messageDisplay = false;

	private int margin = 5;

	private SeqPairPanel seqPairPanel;

	private String messageText = "";

	private boolean needRefresh = false;

	private Scrollbar xScrollbar;
	private Scrollbar yScrollbar;
	
	/*modification by Olivier*/
	private SeqOutputDialog seqOutputDialog;
	private boolean drawSelection = false;
	private boolean drawSnapLimit = false;
	private boolean drawSelectedSequences = false;
	private int snapWidth = 20;
	private int snapHeight = 20;
	private int halfSnapWidth;
	private int halfSnapHeight;
	private Applet applet;
	private int maxX;
	private int maxY;
	private InfoPanel infoPanel;
	/*end modification*/
	
	//Seb:
	private int windowSize;
	private int horizLength;
	private int vertLength;
		

	public ImagePanel(Component visibleComponent) {
		image = visibleComponent.createImage(maxImageWidth, maxImageHeight);
		addMouseListener(this);
		addMouseMotionListener(this);
		updateTrigger = new UpdateTrigger(this);
		
		/*modification by Olivier*/
		MouseSelectionMisc.xOn = Integer.MIN_VALUE;
		MouseSelectionMisc.yOn = Integer.MIN_VALUE;
		this.halfSnapWidth = this.snapWidth / 2;
		this.halfSnapHeight = this.snapHeight / 2;
		this.applet = (Applet) visibleComponent;
		/*end modification*/
		// updateTrigger.start();
		// displayMessage("loading");
	}

	public void setSeqPairPanel(SeqPairPanel spp) {
		seqPairPanel = spp;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public int getImageX() {
		return posX;
	}

	public int getImageY() {
		return posY;
	}

	public Dimension getPreferredSize() {
		return new Dimension(maxImageWidth + 2 * margin, maxImageHeight + 2
				* margin);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public void mouseClicked(MouseEvent e) 
	{
		/*modified by Olivier*/
		if(e.isControlDown() && this.visualByteMap.getZoom() < 2 && this.seqPairPanel.DNAvsProt)
		{
			openOutput(false);
			this.drawSelectedSequences = true;
			repaint();
		}				
		/*end modification*/
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		/*modified by Olivier*/
		if(this.drawSelection)
		{
			openOutput(true);
						
			this.drawSelectedSequences = true;
			this.drawSelection = false;
			MouseSelectionMisc.xOn = Integer.MIN_VALUE;
			MouseSelectionMisc.yOn = Integer.MIN_VALUE;
			repaint();
		}
		/*end modification*/
	}

	public void mouseMoved(MouseEvent e) {
		/*modified by Olivier*/
		if(e.isControlDown() && this.visualByteMap.getZoom() < 2 && this.seqPairPanel.DNAvsProt)
		{
			//System.out.println(e.getX() + " " + e.getY());	
			this.drawSnapLimit = true;
			mousePressed(e);
			searchForSnap((e.getX() + this.posX - this.margin) * visualByteMap.getZoom(), 
					(e.getY() + this.posY - this.margin) * visualByteMap.getZoom());
		}
		else
		{
			this.drawSnapLimit = false;
		}
		/*end modification*/
	}

	public void mousePressed(MouseEvent e) {	
		/*modified by Olivier*/
		//System.out.println((e.getX()-margin)+"\t"+(e.getY()-margin)+"\t"+posX+"\t"+posY);
		if(!e.isControlDown())
		{
			/*end modification*/			
			int x = e.getX() - margin;
			int y = e.getY() - margin;
			//Seb: If mouse coordinates are out of image range, coordinates are shifted to range limits
			if ((x+posX) < 0)
			{
				x=0;
			}
			if ((y+posY) < 0)
			{
				y=0;
			}
			if ((x+posX) > (horizLength-windowSize+1))
			{
				//System.out.println("window: " + windowSize + " " + horizLength + " " + vertLength);
				x=(horizLength-windowSize+1)-posX;
			}
			if ((y+posY) > (vertLength-windowSize+1))
			{
				y=(vertLength-windowSize+1)-posY;
			}
			//Seb: What is the problem with mouse moved pointer on Mac ?
			//System.out.println(e.paramString());
			if (x >= imageWidth || y >= imageHeight) {
			//end of try ! See 50 lines below
			//if (x < 0 || x >= imageWidth || y < 0 || y >= imageHeight) {
				int decX = 0;
				int decY = 0;
				/*if (x < 0) {
					decX = -1 + x / 10;
				} else {*/
					//modified by Olivier, there was a bug in the scrolling process
					//decX = 1 + (x - imageWidth) / 10;
					decX = 1 + (x - imageWidth/2) / 10;  //
				/*}			
				if (y < 0) {
					decY = -1 + y / 10;
				} else {*/
					//modified by Olivier
					//decY = 1 + (y - imageHeight) / 10;
					decY = 1 + (y - imageHeight/2) / 10;
				//}
				//System.out.println(decX+" "+decY);
				moveImageRel(decX, decY);
				xScrollbar.setValue(getImageX());
				yScrollbar.setValue(getImageY());
				return;
			}
			/*modified by Olivier*/
			if(e.isShiftDown() && this.visualByteMap.getZoom() < 2 && this.seqPairPanel.DNAvsProt)
			{
				//due to the fact that mousePressed and mouseDragged are thightly coupled,
				//I have to separate a first click and a dragging event here using Integer.MIN_VALUE 
				//first click
				if(MouseSelectionMisc.xOn == Integer.MIN_VALUE)
				{
					MouseSelectionMisc.xOn = x + this.posX;
					MouseSelectionMisc.yOn = y + this.posY;
					MouseSelectionMisc.xOff = MouseSelectionMisc.xOn;
					MouseSelectionMisc.yOff = MouseSelectionMisc.yOn;				
				}
				//dragging
				else
				{
					MouseSelectionMisc.xOff = x + this.posX;
					MouseSelectionMisc.yOff = y + this.posY;	
					this.drawSelection = true;
					repaint();
				}
				MouseSelectionMisc.setMouseCoordinates();	
				//MouseSelectionMisc.displayVars();
			}
			else
			{
				this.drawSelectedSequences = false;
				//Seb: What is the problem with mouse moved pointer on Mac ?
				//System.out.println(e.paramString());
			/*end modification*/
				
				x = (posX + x) * visualByteMap.getZoom();
				y = (posY + y) * visualByteMap.getZoom();
				setCursor(x, y);
				seqPairPanel.setCursor(x, y);
				
			/*modified by Olivier*/
			}
		}
		/*end modification*/
	}

	public void mouseDragged(MouseEvent e) {
		mousePressed(e);
	}
	
	public void setScrollbar(Scrollbar x, Scrollbar y) {
		xScrollbar = x;
		yScrollbar = y;
	}

	public void setCursor(int x, int y) {
		// x and y are in sequence coordinates
		// x/=visualByteMap.getZoom();
		// y/=visualByteMap.getZoom();
		// now in VisualByteMap coordinates
		if (messageDisplay) {
			return;
		}
		if (x < 0) {
			return;
		}
		if (x > (visualByteMap.getFullWidth() - 1)) {
			return;
		}
		if (y < 0) {
			return;
		}
		if (y > (visualByteMap.getFullHeight() - 1)) {
			return;
		}
		seqPairPanel.setCursor(x, y);
		//System.out.println("setCursor "+x+","+y);
		cursorX = x;
		cursorY = y;
		requestUpdate();
	}

	public int getCursorX() {
		return cursorX;// *visualByteMap.getZoom();
	}

	public int getCursorY() {
		return cursorY;// *visualByteMap.getZoom();
	}

	public void moveImageRel(int decX, int decY) {
		// decX and decY are in pixels!
		moveImageTo(posX + decX, posY + decY);
	}

	public void moveImageTo(int newX, int newY) {
		// posX and posY are in pixels!
		if (image == null) {
			return;
		}
		if (newX < 0) {
			newX = 0;
		}
		if (newY < 0) {
			newY = 0;
		}
		if (newX > visualByteMap.getWidth() - imageWidth) {
			newX = visualByteMap.getWidth() - imageWidth;
		}
		if (newY > visualByteMap.getHeight() - imageHeight) {
			newY = visualByteMap.getHeight() - imageHeight;
		}
		posX = newX;
		posY = newY;
		requestUpdate();
	}

	public void setVisualByteMap(VisualByteMap vbm) {
		visualByteMap = vbm;
		imageWidth = Math.min(maxImageWidth, vbm.getWidth());
		imageHeight = Math.min(maxImageHeight, vbm.getHeight());
	}

	public void paint(Graphics g) {
		if (needRefresh) {
			g.setColor(Color.lightGray);
			g.fillRect(margin, margin, maxImageWidth + 1, maxImageHeight + 1);
			//needRefresh = false; //Seb: force refreshing all the time to get a more nice background !
		}
		/*modified by Olivier*/
		//after the background fillRect(), otherwise background is not correctly 
		//refreshed
		g.setClip(this.margin, this.margin, this.imageWidth, this.imageHeight);
		/*end modification*/
		if (messageDisplay) {
			g.setColor(Color.lightGray);
			g.fillRect(margin, margin, maxImageWidth, maxImageHeight);
			g.setColor(Color.black);
			g.drawRect(margin, margin, maxImageWidth, maxImageHeight);
			FontMetrics fm = g.getFontMetrics();
			int x = fm.stringWidth(messageText);
			g.drawString(messageText, (maxImageWidth - x) / 2,
					maxImageHeight / 2);
			// System.out.println("message DONE");
		} else {// display dot plot
			g.drawImage(image, margin, margin, this);			
			
			/*modified by Olivier*/
			if(this.drawSelection)
			{
				g.setColor(MouseSelectionMisc.seqSelectionColor);
				//to get back to subwindow coord, subtract pos[XY]
				g.drawRect(MouseSelectionMisc.startX + this.margin - this.posX, 
						MouseSelectionMisc.startY + this.margin  - this.posY, 
						MouseSelectionMisc.width, 
						MouseSelectionMisc.height);
			}
			else
			{
			/*end modification*/
				g.setColor(Dotlet.BRIGHT_BLUE);
				int x = margin + cursorX / visualByteMap.getZoom() - posX;
				int y = margin + cursorY / visualByteMap.getZoom() - posY;
				if (x > margin - 1 && x < margin + imageWidth) {
					g.drawLine(x, margin, x, margin + imageHeight - 1);
				}
				if (y > margin - 1 && y < margin + imageHeight) {
					g.drawLine(margin, y, margin + imageWidth - 1, y);
				}
			/*modified by Olivier*/
				if(this.drawSnapLimit)
				{
					g.drawRect(x - this.halfSnapWidth, y - this.halfSnapHeight, 
							this.snapWidth, this.snapHeight);
				}
				if(this.drawSelectedSequences)
				{
					drawSelectedSequences(g, false, 0, 0, 0, 0);
					//this.drawSelectedSequences = false;
				}
			}
			/*end modification*/
		}
		updating = false;
	}

	public void update(Graphics g) { // cancel background refresh for non
										// blinking animation
		paint(g);
	}

	public void requestUpdate() {
		updateRequested = true;
	}

	void updateDisplay() {

		updateRequested = false; // set to false before updating allows
		// new update request to be catched!
		updating = true;
		if (messageDisplay) {
			System.out.println(messageText);
		} else {
			visualByteMap.imageUpdate(image, posX, posY, imageWidth,
					imageHeight, this);
		}
		repaint();
	}

	boolean needUpdate() {
		return updateRequested && !updating;
	}

	public void displayDotPlot(int windowSize, int horizLength, int vertLength) {
		needRefresh = true;
		visualByteMap.prepareScale();
		image = createImage(imageWidth, imageHeight);
		messageDisplay = false;
		posX = 0;
		posY = 0;
		cursorX = 0;
		cursorY = 0;
		//Seb:
		this.windowSize=windowSize;
		this.horizLength=horizLength;
		this.vertLength=vertLength;
		if (updateTrigger.isAlive()) {
			/*modified by Olivier*/
			//updateTrigger.stop();
			updateTrigger.ok = false;
			/*end modification*/
		}
		updateTrigger = new UpdateTrigger(this);
		updateTrigger.start();
		requestUpdate();
	}

	public void displayMessage(String text) {
		// System.out.println("message: "+text);
		if (updateTrigger.isAlive()) {
			/*modified by Olivier*/
			//updateTrigger.stop();
			updateTrigger.ok = false;
			/*end modification*/
		}
		messageText = text;
		needRefresh = true;
		messageDisplay = true;
		updateDisplay();
		// updating=false;

		// requestUpdate();
		// repaint();// and it does not repaint!!! I know
		// updateDisplay();
	}
	
	/* modified by Olivier */
	/**
	 * Opens a new output window or use the existing one if it is already opened,
	 * then runs one of the functions available 
	 */
	private void openOutput(boolean searchSequences)
	{		
		//no existing window
		if(this.seqOutputDialog == null)
		{
			this.seqOutputDialog = new SeqOutputDialog(this, searchSequences,
					this.maxX, this.maxY, getScaleMin());
		}
		//an output window is already opened, use it
		else
		{
			if(searchSequences)
			{
				this.seqOutputDialog.searchSequences();
			}
			else
			{
				this.seqOutputDialog.stockFragment(this.maxX, this.maxY, getScaleMin());
			}
		}
	}
	
	/**
	 * Search for a local max around mouse position
	 */
	public void searchForSnap(int x, int y)
	{		
		int value = 0;
		int max = Integer.MIN_VALUE;
		int maxX = max;
		int maxY = max;
		int startX = x - this.halfSnapWidth;
		int startY = y - this.halfSnapHeight;
		int endX = x + this.halfSnapWidth;
		int endY = y + this.halfSnapHeight;
		int scaleMin = getScaleMin();				
		
		startX = (startX < 0)  ? 0 :startX; 
		startY = (startY < 0)  ? 0 :startY;
		endX = (endX > this.visualByteMap.getFullWidth())  ? this.visualByteMap.getFullWidth() :endX;
		endY = (endY > this.visualByteMap.getFullHeight())  ? this.visualByteMap.getFullHeight() :endY;
		
		for(int j=startY; j<=endY; j++)
		{
			for(int i=startX; i<=endX; i++)
			{
				//System.out.println("i " + i + " j" + j);
				value = this.visualByteMap.getPixel(i, j);
				if((max < value) && (value > scaleMin))
				{
					max = value;
					maxX = i;
					maxY = j;
				}
				//System.out.print(this.visualByteMap.getPixel(i, j));
				//System.out.print(" ");
			}
			//System.out.println();
		}
		//System.out.println();
		
		setCursor(maxX, maxY);
		seqPairPanel.setCursor(maxX, maxY);
		this.maxX = maxX;
		this.maxY = maxY;
	}
	 
	/**
	 * Draws the sequences present in the arraylist, either on screen or on printer
	 * @param g
	 * @param boolean
	 * @param startX
	 * @param startY
	 * 
	 */
	private void drawSelectedSequences(Graphics g, boolean forPrinter, 
			int startX, int startY, int width, int height)
	{
		if(this.seqOutputDialog != null && this.seqOutputDialog.getSequences() != null)
		{
			g.setColor(MouseSelectionMisc.seqSelectionColor);
			Sequence seq = null;
			int nbrSeq = this.seqOutputDialog.getSequences().size();
			for(int i=0; i<nbrSeq; i++)
			{
				seq = (Sequence) this.seqOutputDialog.getSequences().get(i);
				//for screen drawing
				if(!forPrinter)
				{
					g.drawLine(seq.getStartX() + this.margin - this.posX, 
							seq.getStartY() + this.margin - this.posY, 
							seq.getEndX() + this.margin - this.posX, 
							seq.getEndY() + this.margin - this.posY);
				}
				//for printing
				else
				{
					g.setClip(startX, startY, width, height);
					g.drawLine(seq.getStartX() - this.posX + startX, 
							seq.getStartY() - this.posY + startY, 
							seq.getEndX() - this.posX + startX, 
							seq.getEndY() - this.posY + startY);
					g.setClip(null);
				}
			}
		}
	}
	
	public SeqPairPanel getSeqPairPanel()
	{
		return this.seqPairPanel;
	}
	
	public VisualByteMap getVisualByteMap() 
	{
		return visualByteMap;
	}
	
	public SeqOutputDialog getSeqOutputDialog()
    {
    	return this.seqOutputDialog;
    }
	
    public void setOutputDialog(SeqOutputDialog dialog)
    {
    	this.seqOutputDialog = dialog;
    }

	public int getPosX() 
	{
		return posX;
	}

	public int getPosY() 
	{
		return posY;
	}
	
	public Applet getApplet()
	{
		return this.applet;
	}
	
	public void setApplet(Applet applet)
	{
		if(this.seqOutputDialog != null)
		{
			this.seqOutputDialog.setApplet(applet);
		}
	}

	public void setInfoPanel(InfoPanel infoPanel)
	{
		this.infoPanel = infoPanel;
	}
	
	public int getScaleMin()
	{
		int scaleMin = 0;
		
		if(this.visualByteMap.scaleMin < this.visualByteMap.scaleMax)
		{
			scaleMin = this.visualByteMap.scaleMin;
		}
		else
		{
			scaleMin = this.visualByteMap.scaleMax;
		}
		
		return scaleMin;
	}
	
	/**
	 * Displays matrix values at x,y coordinates within a radius
	 * @param x
	 * @param y
	 * @param radius
	 */
	private void displayMatrixValues(int x, int y, int radius)
	{
		int startX = x - radius;
		int startY = y - radius;
		int endX = x + radius;
		int endY = y + radius;
		
		for(int j=startY; j<=endY; j++)
		{
			for(int i=startX; i<=endX; i++)
			{
				System.out.print(this.visualByteMap.getPixel(i, j));
				System.out.print(" ");
			}	
			System.out.println();
		}
	}
	
	/**
	 * Callback function to print image dotplot
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException 
	{
		String[] info = this.infoPanel.getInfo();
		//the viewable image is always square	
		int startX = (int) pageFormat.getImageableX();
		int startY = (int) pageFormat.getImageableY();
		int width = (this.image.getWidth(this) < ImagePanel.maxImageWidth) ? this.image.getWidth(this) : ImagePanel.maxImageWidth;
		int height = (this.image.getHeight(this) < ImagePanel.maxImageHeight) ? this.image.getHeight(this) : ImagePanel.maxImageHeight;
		//int width = ImagePanel.maxImageWidth;
		//int height = ImagePanel.maxImageHeight;		
		int commentX = 0;
		int commentY = 0;		
		int scaleSize = 5;	//size of scale line
		int marginImageInfo = 40;	//margin between dotplot and informations
		int fontHeight = 8;
		
		graphics.setFont(new Font(graphics.getFont().getFontName(), Font.PLAIN, fontHeight));
		graphics.setColor(Color.BLACK);
		
		startX++;	//otherwise the left limit is only half-drawn
		startY += fontHeight * 2;
		graphics.drawRect(startX, startY+scaleSize, ImagePanel.maxImageWidth+2, 
				ImagePanel.maxImageHeight+2);
		startX++;
		startY++;
		drawDotPlot(graphics, startX, startY, width, height, info, fontHeight, scaleSize);
		commentX = startX + ImagePanel.maxImageWidth + scaleSize + marginImageInfo;
		commentY = startY + scaleSize + (fontHeight * 2);
		drawDotInfo(graphics, commentX, commentY, info);
		
		if(pageIndex > 0)
		{
			return Printable.NO_SUCH_PAGE;
		}
		else
		{
			return Printable.PAGE_EXISTS;
		}
	}	
	
	/**
	 * Draws image and calls colored sequence drawing and scales drawing
	 * @param graphics
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 * @param info
	 * @param fontHeight
	 * @param scaleSize
	 * @return size of vertical sequence name
	 */
	private int drawDotPlot(Graphics graphics, int startX, int startY, 
			int width, int height, String[] info, int fontHeight, int scaleSize)
	{
		startY += scaleSize;
		
		graphics.drawImage(this.image, startX, startY, this);
		drawSelectedSequences(graphics, true, startX, startY, width, height);
		return drawScales(graphics, scaleSize, fontHeight, startX, startY, width, 
				height, info);
	}
	
	/**
	 * Draws scales
	 * @param graphics
	 * @param scaleSize
	 * @param fontHeight
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 * @param info
	 * @return size of vertical sequence name
	 */
	private int drawScales(Graphics graphics, int scaleSize, int fontHeight, 
			int startX, int startY, int imageWidth, int imageHeight, String[] info)
	{
		graphics.setColor(Color.BLACK);
		String seqHoriz = info[4];
		String seqVert = info[5];
		int window = Integer.parseInt(info[1]);
		int windowOffset = (window - 1) / 2;
		//int spaceNameHorizSeq = 5;
		int seqHorizontalFirstRes = this.posX + windowOffset + 1;
		int seqVerticalFirstRes = this.posY + windowOffset + 1;
		int seqHorizontalLastRes = (this.image.getWidth(this) < ImagePanel.maxImageWidth) ? this.visualByteMap.getFullWidth() + window : seqHorizontalFirstRes + ImagePanel.maxImageWidth + windowOffset;
		int seqVerticalLastRes = (this.image.getHeight(this) < ImagePanel.maxImageHeight) ? this.visualByteMap.getFullHeight() + window : seqVerticalFirstRes + ImagePanel.maxImageHeight + windowOffset;
		
		//to center horizontal sequence name
		FontRenderContext frc = ((Graphics2D)graphics).getFontRenderContext();
		Font f = graphics.getFont();
		Rectangle2D bounds = f.getStringBounds(seqHoriz, frc);
		int seqHorizWidth = (int) bounds.getWidth();
		//to get vertical name length to return it afterwards
		bounds = f.getStringBounds(seqVert, frc);
		int seqVertWidth = (int) bounds.getWidth();
		
		//first horizontal mark
		graphics.drawLine(startX, startY, startX, startY-scaleSize);
		//last horizontal mark
		graphics.drawLine(startX+imageWidth, startY, startX+imageWidth, startY-scaleSize);
		//first vertical mark
		graphics.drawLine(startX+ImagePanel.maxImageWidth, startY, startX+ImagePanel.maxImageWidth+scaleSize, startY);
		//last vertical mark
		graphics.drawLine(startX+ImagePanel.maxImageWidth, startY+imageHeight, startX+ImagePanel.maxImageWidth+scaleSize, startY+imageHeight);
		
		//first position of horizontal sequence
		graphics.drawString(String.valueOf(seqHorizontalFirstRes), startX, startY-scaleSize);
		//last position of horizontal sequence
		graphics.drawString(String.valueOf(seqHorizontalLastRes), startX+imageWidth, startY-scaleSize);
		//first position of vertical sequence
		graphics.drawString(String.valueOf(seqVerticalFirstRes), startX+ImagePanel.maxImageWidth+scaleSize, startY+fontHeight);
		//last position of vertical sequence
		graphics.drawString(String.valueOf(seqVerticalLastRes), startX+ImagePanel.maxImageWidth+scaleSize, startY+imageHeight-2);
		
		//name of horizontal sequence
		graphics.drawString(seqHoriz, startX + (ImagePanel.maxImageWidth/2) - (seqHorizWidth / 2), startY-scaleSize-fontHeight);
		//name of vertical sequence
		graphics.drawString(seqVert, startX+ImagePanel.maxImageWidth+(8*scaleSize), startY+(ImagePanel.maxImageHeight/2));
		
		return seqVertWidth;
	}
	
	/**
	 * Draws dotplot informations
	 * @param graphics
	 * @param x
	 * @param y
	 * @param info
	 */
	private void drawDotInfo(Graphics graphics, int x, int y, String[] info)
	{		
		graphics.setColor(Color.BLACK);
		int fontHeight = graphics.getFontMetrics().getHeight();
		String[] data = new String[7];		
		
		data[0] = "Matrix : " + info[0];
		data[1] = "Sliding window : " + info[1];
		data[2] = "Zoom : " + this.visualByteMap.getZoom();
		data[3] = "Score range : " + info[2];
		data[4] = "Gray scale : " + info[3];
		data[5] = "Date : " + DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
		data[6] = "\u00A9 Dotlet, Swiss Institute of Bioinformatics";
		
		for(int i=0; i<data.length; i++)
		{
			graphics.drawString(data[i], x, y);
			y += fontHeight;
		}	
	}
	/*end modification*/
}

class UpdateTrigger extends Thread {

	private ImagePanel imagePanel;

	private int delay = 50;

	private int count = 0;

	/* modified by Olivier */
	boolean ok = true;
	/*end modification*/

	UpdateTrigger(ImagePanel p) {
		imagePanel = p;
	}

	public void run() {
		try {
			/* modified by Olivier */
			while (ok) {
			/*end modification*/
				sleep(delay);
				// System.out.println(count);
				count++;
				if (imagePanel.needUpdate()) {
					imagePanel.updateDisplay();
				}
			}
		} 
		catch (InterruptedException e) {
		}
	}
}
