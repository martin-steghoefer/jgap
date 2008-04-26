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
import org.jgap.gp.function.*;
import org.jgap.gp.*;

/**
 * Renders the branches' colors of a tree to display.
 * Created by Brian Risk of Geneffects on March 18, 2004.
 * Last Modified on March 19, 2004.
 * www.geneffects.com
 * Modified by Klaus Meffert
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class JGAPTreeBranchRenderer
    implements TreeBranchRenderer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public Color getBranchColor(Object a_node, int a_level) {
    String name = ( (JGAPTreeNode) a_node).getName();
    Color out = Color.white;
    if (name.equals(IfElse.class.getName())) {
      out = new Color(255, 30, 30);
    }
    else if (name.equals(Add.class.getName())) {
      out = new Color(86, 140, 0);
    }
    else if (name.equals(AddAndStore.class.getName())) {
      out = new Color(44, 200, 70);
    }
    else if (name.equals(Add3.class.getName())) {
      out = new Color(0, 86, 22);
    }
    else if (name.equals(Subtract.class.getName())) {
      out = new Color(171, 0, 0);
    }
    else if (name.equals(Multiply.class.getName())) {
      out = new Color(85, 0, 85);
    }
    else if (name.equals(Multiply3.class.getName())) {
      out = new Color(0, 190, 171);
    }
    else if (name.equals(Equals.class.getName())) {
      out = new Color(0, 0, 255);
    }
    else if (name.equals(Or.class.getName())) {
      out = new Color(20, 200, 40);
    }
    else if (name.equals(Xor.class.getName())) {
      out = new Color(10, 150, 80);
    }
    else if (name.equals(And.class.getName())) {
      out = new Color(90, 100, 90);
    }
    else if (name.equals(If.class.getName())) {
      out = new Color(200, 250, 100);
    }
    else if (name.equals(Not.class.getName())) {
      out = new Color(240, 50, 0);
    }
    else if (name.equals(Sine.class.getName())) {
      out = new Color(50, 10, 0);
    }
    else if (name.equals(Cosine.class.getName())) {
      out = new Color(50, 200, 0);
    }
    else if (name.equals(Exp.class.getName())) {
      out = new Color(200, 0, 50);
    }
    else if (name.equals(Pow.class.getName())) {
      out = new Color(100, 50, 150);
    }
    else if (name.equals(SubProgram.class.getName())) {
      out = new Color(33, 66, 99);
    }
    else if (name.equals(StoreTerminal.class.getName())) {
      out = new Color(100, 40, 200);
    }
    else if (name.equals(ReadTerminal.class.getName())) {
      out = new Color(200, 80, 100);
    }
    else if (name.equals(TransferMemory.class.getName())) {
      out = new Color(100, 200, 40);
    }
    else if (name.equals(ForLoop.class.getName())) {
      out = new Color(77, 240, 110);
    }
    else if (name.equals(Increment.class.getName())) {
      out = new Color(150, 150, 40);
    }
    return out;
  }
}
