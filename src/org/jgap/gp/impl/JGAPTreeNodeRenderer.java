/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.awt.*;
import org.jgap.util.tree.*;
import org.jgap.gp.terminal.*;

/**
 * Renders the nodes' colors of a tree to display.
 * Created by Brian Risk of Geneffects on March 19, 2004.
 * Last Modified on March 19, 2004.
 * www.geneffects.com
 *
 * Modified by Klaus Meffert
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class JGAPTreeNodeRenderer
    implements TreeNodeRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  //This implementation basis the shade of the node on the level
  //but you may employ any property of your node.
  public Color getNodeColor(Object a_node, int a_level) {
    String name = ( (JGAPTreeNode) a_node).getName();
    Color out;
    if (name.equals(Constant.class.getName())) {
      out = Color.orange;
    }
    else if (name.equals(Variable.class.getName())) {
      out = Color.green;
    }
    else if (name.equals(Terminal.class.getName())) {
      out = Color.yellow;
    }
    else if (name.equals(NOP.class.getName())) {
      out = new Color(255, 255, 255);
    }
    else if (name.equals(True.class.getName())) {
      out = Color.blue;
    }
    else if (name.equals(False.class.getName())) {
      out = Color.gray;
    }
    else {
      switch (a_level) {
        case 0:
          out = Color.orange;
          break;
        case 1:
          out = new Color(240, 200, 100);
          break;
        case 2:
          out = new Color(200, 140, 80);
          break;
        case 3:
          out = new Color(140, 240, 180);
          break;
        case 4:
          out = new Color(140, 180, 220);
          break;
        default:
          if (a_level <= 7) {
            int amt = (8 - a_level) * 32;
            if (amt >= 256) {
              amt = 255;
            }
            out = new Color(amt, amt, amt);
          }
          else {
            out = Color.black;
          }
      }
    }
    return out;
  }
}
