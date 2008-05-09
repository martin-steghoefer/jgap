/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util.tree;

//
//  TreePrinter.java
//  MetaEvolve
//
//  Version 1
//
//  Created by Brian Risk of Geneffects on March 15, 2004.
//  Last Modified on March 19, 2004.
//  www.geneffects.com
//

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.tree.*;

public class TreeVisualizer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private int side = 512; //the height and width of the image

  private double circleDiminishFactor = 0.875;

  private double branchStartWidth = 16.0;

  private double branchDiminshFactor = 0.6666;

  private TreeBranchRenderer tbr = null;

  private TreeNodeRenderer tnr = null;

  private Color bkgndColor = Color.white;

  private Color arenaColor = Color.black;

  private Color branchColor = Color.white;

  private Color nodeColor = Color.red;

  private boolean renderNodes = true;

  private int ignorePastLevel = -1; //if set to less than 0, all levels are drawn

  public TreeVisualizer() {}

  //TreeNode is an interface found under java.swing.tree.*
  public BufferedImage renderTree(TreeNode tn) {
    BufferedImage bufferedImage = new BufferedImage(side, side,
        BufferedImage.TYPE_INT_RGB);
    // Create a graphics contents on the buffered image
    Graphics2D g2d = bufferedImage.createGraphics();
    // Making the graphic object draw anti-aliased shapes
    g2d.addRenderingHints(new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON));
    g2d.setColor(bkgndColor);
    g2d.fillRect(0, 0, side, side);
    g2d.setColor(arenaColor);
    g2d.fillOval(0, 0, side, side);
    g2d.setColor(branchColor);
    drawBranches(g2d, tn, 0, 0.0, 2.0 * Math.PI);
    if (renderNodes) {
      g2d.setColor(nodeColor);
      drawNodes(g2d, tn, 0, 0.0, 2.0 * Math.PI);
    }
    g2d.dispose();
    return bufferedImage;
  }

  public void writeImageFile(RenderedImage ri, File f) {
    try {
      ImageIO.write(ri, "png", f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Point drawBranches(Graphics2D g, TreeNode tn, int level, double start,
                             double finish) {
    double span = finish - start;
    double middle = start + (span / 2.0);
    Point middlePoint = radToCart(getR(level), middle, side / 2, side / 2);
    Stroke strokeSize = getStroke(level);
    if (!tn.isLeaf()) {
      if (ignorePastLevel >= 0) {
        if (ignorePastLevel < level + 1) {
          return middlePoint;
        }
      }
      double subSection = span / (double) tn.getChildCount();
      double s1 = start;
      double s2 = start + subSection;
      for (int i = 0; i < tn.getChildCount(); i++) {
        TreeNode tn2 = tn.getChildAt(i);
        Point connectPoint = drawBranches(g, tn2, level + 1, s1, s2);
        g.setStroke(strokeSize);
        if (tbr != null) {
          Color nc = tbr.getBranchColor(tn, level);
          if (nc != null) {
            g.setColor(nc);
          }
        }
        g.drawLine( (int) middlePoint.getX(), (int) middlePoint.getY(),
                   (int) connectPoint.getX(), (int) connectPoint.getY());
        s1 += subSection;
        s2 += subSection;
      }
    }
    return middlePoint;
  }

  private void drawNodes(Graphics2D g, TreeNode tn, int level, double start,
                         double finish) {
    double span = finish - start;
    double middle = start + (span / 2.0);
    Point middlePoint = radToCart(getR(level), middle, side / 2, side / 2);
    double x = middlePoint.getX();
    double y = middlePoint.getY();
    g.setStroke(new BasicStroke(0));
    double r = branchStartWidth * Math.pow(branchDiminshFactor, level);
    if ( (int) (2 * r) > 0) {
      if (tnr != null) {
        Color nc = tnr.getNodeColor(tn, level);
        if (nc != null) {
          g.setColor(nc);
        }
      }
      g.fillOval( (int) (x - r), (int) (y - r), (int) (2 * r), (int) (2 * r));
    }
    if (!tn.isLeaf()) {
      if (ignorePastLevel >= 0) {
        if (ignorePastLevel < level + 1) {
          return;
        }
      }
      double subSection = span / (double) tn.getChildCount();
      double s1 = start;
      double s2 = start + subSection;
      for (int i = 0; i < tn.getChildCount(); i++) {
        TreeNode tn2 = tn.getChildAt(i);
        drawNodes(g, tn2, level + 1, s1, s2);
        s1 += subSection;
        s2 += subSection;
      }
    }
  }

  private Stroke getStroke(int level) {
    return new BasicStroke( (float) (branchStartWidth *
                                     Math.pow(branchDiminshFactor, level)),
                           BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
  }

  //x and y are the origin, the pixel location of the center of the coordinate system
  private Point radToCart(double r, double theta, int x, int y) {
    theta += Math.PI / 2.0;
    int nx = (int) (r * Math.cos(theta)) + x;
    int ny = (int) (r * Math.sin(theta)) + y;
    return new Point(nx, ny);
  }

  private double getR(int level) {
    double r = (double) (side / 2) -
        ( (double) (side / 2) * Math.pow(circleDiminishFactor, level));
    return r;
  }

  //mutating and accessing methods
  public void setSide(int s) {
    side = s;
  }

  public int getSide() {
    return side;
  }

  public void setCircleDiminishFactor(double c) {
    circleDiminishFactor = c;
  }

  public double getCircleDiminishFactor() {
    return circleDiminishFactor;
  }

  public void setBranchStartWidth(double b) {
    branchStartWidth = b;
  }

  public double getBranchStartWidth() {
    return branchStartWidth;
  }

  public void setBranchDiminishFactor(double s) {
    branchDiminshFactor = s;
  }

  public double getBranchDiminshFactor() {
    return branchDiminshFactor;
  }

  public void setBkgndColor(Color c) {
    bkgndColor = c;
  }

  public Color getBkgndColor() {
    return bkgndColor;
  }

  public void setArenaColor(Color c) {
    arenaColor = c;
  }

  public Color getArenaColor() {
    return arenaColor;
  }

  public void setBranchColor(Color c) {
    branchColor = c;
  }

  public Color getBranchColor() {
    return branchColor;
  }

  public void setNodeColor(Color c) {
    nodeColor = c;
  }

  public Color getNodeColor() {
    return nodeColor;
  }

  public void setTreeBranchRenderer(TreeBranchRenderer ntbr) {
    tbr = ntbr;
  }

  public TreeBranchRenderer getTreeBranchRenderer() {
    return tbr;
  }

  public void setTreeNodeRenderer(TreeNodeRenderer ntnr) {
    tnr = ntnr;
  }

  public TreeNodeRenderer getTreeNodeRenderer() {
    return tnr;
  }

  public void setRenderNodes(boolean r) {
    renderNodes = r;
  }

  public boolean getRenderNodes() {
    return renderNodes;
  }

  public void setIgnorePastLevel(int i) {
    ignorePastLevel = i;
  }

  public int getIgnorePastLevel() {
    return ignorePastLevel;
  }
}
