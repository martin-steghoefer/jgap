/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import java.awt.*;

import org.jgap.gp.impl.*;

/**
 * Renders the nodes' colors of a tree to display.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class AntTreeNodeRenderer
    extends JGAPTreeNodeRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  //This implementation basis the shade of the node on the level
  //but you may employ any property of your node.
  public Color getNodeColor(Object a_node, int a_level) {
    String name = ( (JGAPTreeNode) a_node).getName();
    Color out;
    if (name.equals(Move.class.getName())) {
      out = new Color(0, 140, 86);
    }
    else if (name.equals(Left.class.getName())) {
      out = new Color(44, 200, 70);
    }
    else if (name.equals(Right.class.getName())) {
      out = new Color(0, 86, 22);
    }
    else {
      return super.getNodeColor(a_node, a_level);
    }
    return out;
  }
}
