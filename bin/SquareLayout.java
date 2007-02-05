// This example is from the book _Java AWT Reference_ by John Zukowski.
// Written by John Zukowski.  Copyright (c) 1997 O'Reilly & Associates.
// You may study, use, modify, and distribute this example for any purpose.
// This example is provided WITHOUT WARRANTY either expressed or

import java.awt.*;

/**
 * An 'educational' layout.  CornerLayout will layout a container
 * using members named "Northeast", "Northwest", "Southeast",
 * "Southwest", and "Center".
 *
 * The "Northeast", "Northwest", "Southeast" and "Soutwest" components
 * get sized relative to the adjacent corner's components and
 * the constraints of the container's size.  The "Center" component will
 * get any space left over. 
 */

public class SquareLayout implements LayoutManager {
    int hgap;
    int vgap;
    int mode;

    Component northwest;
    Component southwest;
    Component northeast;
    Component southeast;
    Component center;
    Component north,east,south,west;

    /**
     * Constructs a new CornerLayout.
     */
    public SquareLayout() {
        this (0, 0);
    }

    public SquareLayout(int hgap, int vgap) {
	this.hgap = hgap;
	this.vgap = vgap;
    }

    public void addLayoutComponent(String name, Component comp) {
	if ("Center".equals(name)) {
	    center = comp;
        } else if ("Northwest".equals(name)) {
            northwest = comp;
        } else if ("Southeast".equals(name)) {
            southeast = comp;
        } else if ("Northeast".equals(name)) {
            northeast = comp;
        } else if ("Southwest".equals(name)) {
            southwest = comp;
	} else if ("South".equals(name)) {
            south = comp;
	} else if ("North".equals(name)) {
            north = comp;
	} else if ("East".equals(name)) {
            east = comp;
	} else if ("West".equals(name)) {
            west = comp;
	} 
    }

    public void removeLayoutComponent(Component comp) {
	if (comp == center) {
	    center = null;
        } else if (comp == northwest) {
            northwest = null;
        } else if (comp == southeast) {
            southeast = null;
        } else if (comp == northeast) {
            northeast = null;
        } else if (comp == southwest) {
            southwest = null;
	} else if (comp == south) {
            south = null;
	} else if (comp == north) {
            north = null;
	} else if (comp == east) {
            east = null;
	} else if (comp == west) {
            west = null;
	}
    }

    public Dimension minimumLayoutSize(Container target) {
	Dimension dim = new Dimension(0, 0);

        Dimension northeastDim = new Dimension (0,0);
        Dimension northwestDim = new Dimension (0,0);
        Dimension southeastDim = new Dimension (0,0);
        Dimension southwestDim = new Dimension (0,0);
        Dimension centerDim    = new Dimension (0,0);
	Dimension northDim     = new Dimension (0,0);
	Dimension southDim     = new Dimension (0,0);
	Dimension eastDim      = new Dimension (0,0);
	Dimension westDim      = new Dimension (0,0);

	
        if ((northeast != null) && northeast.isVisible()) {
            northeastDim = northeast.getMinimumSize();
	}
        if ((southwest != null) && southwest.isVisible()) {
            southwestDim = southwest.getMinimumSize();
	}
        if ((center != null) && center.isVisible()) {
            centerDim = center.getMinimumSize();
	}
        if ((northwest != null) && northwest.isVisible()) {
            northwestDim = northwest.getMinimumSize();
	}
        if ((southeast != null) && southeast.isVisible()) {
            southeastDim = southeast.getMinimumSize();
	}
        if ((north != null) && north.isVisible()) {
            northDim = north.getMinimumSize();
	}
        if ((south != null) && south.isVisible()) {
            southDim = south.getMinimumSize();
	}
        if ((east != null) && east.isVisible()) {
            eastDim = east.getMinimumSize();
	}
        if ((west != null) && west.isVisible()) {
            westDim = west.getMinimumSize();
	}
	dim.width = Math.max (westDim.width,
			      Math.max(northwestDim.width, southwestDim.width)) +
	    hgap +
	    Math.max(centerDim.width,
		     Math.max(northDim.width,southDim.width)) +
	    hgap +
            Math.max (eastDim.width,
		      Math.max(northeastDim.width, southeastDim.width));
        dim.height = Math.max(northDim.height,
			      Math.max (northwestDim.height, northeastDim.height)) +
	    + vgap +
	    Math.max(centerDim.height,
		     Math.max(eastDim.width,westDim.width)) +
	    vgap +
	    Math.max(southDim.height,
		     Math.max (southeastDim.height, southwestDim.height));

	Insets insets = target.getInsets();
        dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;

	return dim;
    }
    
    public Dimension preferredLayoutSize(Container target) {
	Dimension dim = new Dimension(0, 0);
        Dimension northeastDim = new Dimension (0,0);
        Dimension northwestDim = new Dimension (0,0);
        Dimension southeastDim = new Dimension (0,0);
        Dimension southwestDim = new Dimension (0,0);
        Dimension centerDim    = new Dimension (0,0);
	Dimension northDim     = new Dimension (0,0);
	Dimension southDim     = new Dimension (0,0);
	Dimension eastDim      = new Dimension (0,0);
	Dimension westDim      = new Dimension (0,0);


        if ((northeast != null) && northeast.isVisible()) {
            northeastDim = northeast.getPreferredSize();
	}
        if ((southwest != null) && southwest.isVisible()) {
            southwestDim = southwest.getPreferredSize();
	}
        if ((center != null) && center.isVisible()) {
            centerDim = center.getPreferredSize();
	}
        if ((northwest != null) && northwest.isVisible()) {
            northwestDim = northwest.getPreferredSize();
	}
        if ((southeast != null) && southeast.isVisible()) {
            southeastDim = southeast.getPreferredSize();
	}
        if ((north != null) && north.isVisible()) {
            northDim = north.getPreferredSize();
	}
        if ((south != null) && south.isVisible()) {
            southDim = south.getPreferredSize();
	}
        if ((east != null) && east.isVisible()) {
            eastDim = east.getPreferredSize();
	}
        if ((west != null) && west.isVisible()) {
            westDim = west.getPreferredSize();
	} 

	dim.width = Math.max (westDim.width,
			      Math.max(northwestDim.width, southwestDim.width)) +
	    hgap +
	    Math.max(centerDim.width,
		     Math.max(northDim.width,southDim.width)) +
	    hgap +
            Math.max (eastDim.width,
		      Math.max(northeastDim.width, southeastDim.width));
        dim.height = Math.max(northDim.height,
			      Math.max (northwestDim.height, northeastDim.height)) +
	    + vgap +
	    Math.max(centerDim.height,
		     Math.max(eastDim.width,westDim.width)) +
	    vgap +
	    Math.max(southDim.height,
		     Math.max (southeastDim.height, southwestDim.height));

	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right;
	dim.height += insets.top + insets.bottom;

	return dim;
    }

    public void layoutContainer(Container target) {
	Insets insets = target.getInsets();
	int top = insets.top;
        int bottom = target.getSize().height - insets.bottom;
	int left = insets.left;
        int right = target.getSize().width - insets.right;

        Dimension northeastDim = new Dimension (0,0);
        Dimension northwestDim = new Dimension (0,0);
        Dimension southeastDim = new Dimension (0,0);
        Dimension southwestDim = new Dimension (0,0);
        Dimension centerDim    = new Dimension (0,0);
	Dimension northDim     = new Dimension (0,0);
	Dimension southDim     = new Dimension (0,0);
	Dimension eastDim      = new Dimension (0,0);
	Dimension westDim      = new Dimension (0,0);

        Point topLeftCorner, topRightCorner, bottomLeftCorner,bottomRightCorner;

        if ((northeast != null) && northeast.isVisible()) {
            northeastDim = northeast.getPreferredSize();
	}
        if ((southwest != null) && southwest.isVisible()) {
            southwestDim = southwest.getPreferredSize();
	}
        if ((center != null) && center.isVisible()) {
            centerDim = center.getPreferredSize();
	}
        if ((northwest != null) && northwest.isVisible()) {
            northwestDim = northwest.getPreferredSize();
	}
        if ((southeast != null) && southeast.isVisible()) {
            southeastDim = southeast.getPreferredSize();
	}
        if ((north != null) && north.isVisible()) {
            northDim = north.getPreferredSize();
	}
        if ((south != null) && south.isVisible()) {
            southDim = south.getPreferredSize();
	}
        if ((east != null) && east.isVisible()) {
            eastDim = east.getPreferredSize();
	}
        if ((west != null) && west.isVisible()) {
            westDim = west.getPreferredSize();
	} 

        topLeftCorner = new Point (left +
				Math.max(westDim.width,Math.max(northwestDim.width, southwestDim.width)),
				top + 
				Math.max(northDim.height,Math.max(northwestDim.height, northeastDim.height)));
        topRightCorner = new Point (right -
				    Math.max(eastDim.width,Math.max(northeastDim.width, southeastDim.width)),
				    top +
				    Math.max(northDim.height,Math.max(northwestDim.height, northeastDim.height)));
        bottomLeftCorner = new Point (left + 
				      Math.max(westDim.width,Math.max(northwestDim.width, southwestDim.width)),
				      bottom - 
				      Math.max(southDim.height,Math.max(southwestDim.height, southeastDim.height)));
        bottomRightCorner = new Point (right  -
				       Math.max(eastDim.width,Math.max(northeastDim.width, southeastDim.width)),
				       bottom - 
				       Math.max(southDim.height,Math.max(southwestDim.height, southeastDim.height)));

        if ((northwest != null) && northwest.isVisible()) {
            northwest.setBounds(left, top,
                                left + topLeftCorner.x,
                                top + topLeftCorner.y);
	}
        if ((southwest != null) && southwest.isVisible()) {
            southwest.setBounds(left, bottomLeftCorner.y,
                                bottomLeftCorner.x - left,
                                bottom - bottomLeftCorner.y);
	}
        if ((west != null) && west.isVisible()) {
            west.setBounds(left, bottomLeftCorner.y,
                                bottomLeftCorner.x - left,
                                bottom - bottomLeftCorner.y);
	}

        if ((southeast != null) && southeast.isVisible()) {
            southeast.setBounds(bottomRightCorner.x,
                        bottomRightCorner.y,
                        right - bottomRightCorner.x,
                        bottom - bottomRightCorner.y);
	}

        if ((northeast != null) && northeast.isVisible()) {
            northeast.setBounds(topRightCorner.x, top,
                                right - topRightCorner.x,
                                topRightCorner.y);
	}
	
        if ((north != null) && north.isVisible()) {
            north.setBounds(topLeftCorner.x + hgap,
			    top,
                            bottomRightCorner.x - topLeftCorner.x - 2*hgap,
			    topRightCorner.y);
	}
        if ((south != null) && south.isVisible()) {
            south.setBounds(bottomLeftCorner.x + hgap,
			    bottomLeftCorner.y,
                            bottomRightCorner.x - topLeftCorner.x - 2*hgap,
			    bottom - bottomRightCorner.y);
	}
        if ((west != null) && west.isVisible()) {
            west.setBounds(top,
			   topLeftCorner.y+hgap,
			   topLeftCorner.x,
			   bottomLeftCorner.y - topLeftCorner.y - 2*hgap);
	}
        if ((east != null) && east.isVisible()) {
            east.setBounds(topRightCorner.x,
			   topRightCorner.y+hgap,
			   right- topRightCorner.x,
			   bottomLeftCorner.y - topLeftCorner.y - 2*hgap);
	}

        if ((center != null) && center.isVisible()) {
            int x = topLeftCorner.x + hgap;
            int y = topLeftCorner.y + vgap;
            int width = bottomRightCorner.x - topLeftCorner.x - hgap * 2;
            int height = bottomRightCorner.y - topLeftCorner.y - vgap * 2;
            center.setBounds(x, y, width, height);

	}
        
    }
   
    public String toString() {
	return getClass().getName() + "[hgap=" + hgap +
                        ",vgap=" + vgap + "]";
    }
}
