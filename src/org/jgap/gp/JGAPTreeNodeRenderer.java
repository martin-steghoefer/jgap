/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

//
//  METreeNodeRenderer.java
//  MetaEvolve
//
//  Version 1
//
//  Created by Brian Risk of Geneffects on March 19, 2004.
//  Last Modified on March 19, 2004.
//  www.geneffects.com
//

import java.awt.*;
import org.jgap.util.tree.*;

public class JGAPTreeNodeRenderer
    implements TreeNodeRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  //This implementation basis the shade of the node on the level
  //but you may employ any property of your node.
  public Color getNodeColor(Object a_node, int a_level) {
    Color out = Color.black;
    if (a_level <= 7) {
      int amt = (8 - a_level) * 32;
      if (amt >= 256) {
        amt = 255;
      }
      out = new Color(amt, amt, amt);
    }
    return out;
  }
}
