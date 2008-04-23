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
 * Renders the branches' colors of a tree to display.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class AntTreeBranchRenderer
    extends JGAPTreeBranchRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public Color getBranchColor(Object a_node, int a_level) {
    String name = ( (JGAPTreeNode) a_node).getName();
    Color out = Color.white;
    if (name.equals(IfFoodAheadElse.class.getName())) {
      out = new Color(255, 30, 30);
    }
    else {
      return super.getBranchColor(a_node, a_level);
    }
    return out;
  }
}
