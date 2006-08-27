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
//  METreeRenderer.java
//  MetaEvolve
//
//  Version 1
//
//  Created by Brian Risk of Geneffects on March 18, 2004.
//  Last Modified on March 19, 2004.
//  www.geneffects.com
//

import java.awt.*;
import org.jgap.util.tree.*;
import org.jgap.gp.function.*;

public class JGAPTreeBranchRenderer
    implements TreeBranchRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Color getBranchColor(Object a_node, int a_level) {
    String name = ( (JGAPTreeNode) a_node).getName();
    Color out = Color.black;
    if (name.equals(IfElse.class.getName())) {
      out = new Color(255, 0, 0);
    }
    if (name.equals(Add.class.getName())) {
      out = new Color(86, 0, 0);
    }
    if (name.equals(Add3.class.getName())) {
      out = new Color(0, 86, 22);
    }
    if (name.equals(Subtract.class.getName())) {
      out = new Color(171, 0, 0);
    }
    if (name.equals(Multiply.class.getName())) {
      out = new Color(0, 0, 85);
    }
    if (name.equals(Multiply3.class.getName())) {
      out = new Color(0, 0, 171);
    }
    if (name.equals(Equals.class.getName())) {
      out = new Color(0, 0, 255);
    }
    if (name.equals(Or.class.getName())) {
      out = new Color(0, 200, 0);
    }
    if (name.equals(Xor.class.getName())) {
      out = new Color(0, 150, 0);
    }
    if (name.equals(And.class.getName())) {
      out = new Color(0, 100, 0);
    }
    if (name.equals(If.class.getName())) {
      out = new Color(0, 250, 0);
    }
    if (name.equals(Not.class.getName())) {
      out = new Color(0, 50, 0);
    }
    if (name.equals(Sine.class.getName())) {
      out = new Color(50, 10, 0);
    }
    if (name.equals(Cosine.class.getName())) {
      out = new Color(50, 200, 0);
    }
    if (name.equals(Exp.class.getName())) {
      out = new Color(200, 0, 50);
    }
    if (name.equals(Pow.class.getName())) {
      out = new Color(100, 50, 150);
    }
    return out;
  }
}
