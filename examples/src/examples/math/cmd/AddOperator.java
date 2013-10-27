/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.cmd;

import org.jgap.util.*;
import examples.math.*;

/**
 * The add operation.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class AddOperator
    extends MathOperator implements ICloneable {
  public AddOperator() {
    super();
  }

  public AddOperator(Operand theLeft, Operand theRight) {
    super(theLeft, theRight);
  }

  public double calcuate() {
    return getLeftOperand().value() + getRightOperand().value();
  }

  public void accept(MathVisitor theVisitor) {
    theVisitor.visit(this);
  }

  public Object clone() {
    AddOperator op = new AddOperator( (Operand)super.getLeftOperand().clone(),
                                     (Operand)super.getRightOperand().clone());
    return op;
  }
}
