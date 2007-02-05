import java.awt.Color;

/**
 * Class for misc. things about mouse selection (start, end, width, ...)
 * @author omartin
 *
 */
public final class MouseSelectionMisc 
{
	static Color seqSelectionColor = Color.RED;
	
	static int xOn;
	static int xOff;
	static int yOn;
	static int yOff;
	static int width;
	static int height;
	static int startX;
	static int startY;
	static int endX;
	static int endY;
	static int windowSize;
	
	public static void setMouseCoordinates()
	{
		if(xOn < 0) { xOn = 0;}
		if(xOff < 0) { xOff = 0;}
		if(yOn < 0) { yOn = 0;}
		if(yOff < 0) { yOff = 0;}
		
		//normal case -> to the right, downwards
		if(xOff > xOn)
		{
			startX = xOn;
			endX = xOff;
		}
		//selection going leftwards
		else
		{			
			startX = xOff;
			endX = xOn;
		}
		
		//normal case
		if(yOff > yOn)
		{
			startY = yOn;
			endY = yOff;
		}
		//selection going upwards
		else
		{
			startY = yOff;
			endY = yOn;
		}
		
		width = endX - startX;
		height = endY - startY;
	}
	
	public static void displayVars()
	{
		System.out.print("xOn " + xOn);
		System.out.println(" yOn " + yOn);
		System.out.print("xOff " + xOff);
		System.out.println(" yOff " + yOff);
		System.out.print("startX " + startX);
		System.out.println(" startY " + startY);
		System.out.print("endX " + endX);
		System.out.println(" endY " + endY);
		System.out.print("width " + width);
		System.out.println(" height " + height);
		System.out.println("windowSize " + windowSize);
	}
}
