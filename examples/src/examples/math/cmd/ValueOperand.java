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

import examples.math.*;

/**
 * A mathematical operand.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class ValueOperand
    implements Operand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private double mValue;

  public ValueOperand(double theValue) {
    mValue = theValue;
  }

  public double value() {
    return mValue;
  }

  public void setValue(double theVal) {
    mValue = theVal;
  }

  public void accept(MathVisitor theVisitor) {
    theVisitor.visit(this);
  }

  public Object clone() {
    Operand op = new ValueOperand(mValue);
    return op;
  }
}
