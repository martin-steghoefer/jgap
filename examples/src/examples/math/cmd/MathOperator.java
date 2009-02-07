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

/**
 * Base class for mathematical operators.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public abstract class MathOperator
    implements Operator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Operand mLeftOperand;

  private Operand mRightOperand;

  public MathOperator() {
  }

  public MathOperator(Operand theLeft, Operand theRight) {
    mLeftOperand = theLeft;
    mRightOperand = theRight;
  }

  public void setLeftOperand(Operand theOp) {
    mLeftOperand = theOp;
  }

  public void setRightOperand(Operand theOp) {
    mRightOperand = theOp;
  }

  public Operand getLeftOperand() {
    return mLeftOperand;
  }

  public Operand getRightOperand() {
    return mRightOperand;
  }

  public double value() {
    return calcuate();
  }

  public abstract Object clone();
}
