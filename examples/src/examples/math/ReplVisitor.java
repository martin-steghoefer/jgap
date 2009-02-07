/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math;

import examples.math.cmd.*;

/**
 * Only used in MathGene.toString().
 *
 * @author Michael Grove
 * @since 3.4.2
 */
@Deprecated
public class ReplVisitor
    implements MathVisitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private StringBuffer mBuffer = new StringBuffer();

  public void reset() {
    mBuffer = new StringBuffer();
  }

  public String render(Operand theOp) {
    if (theOp != null) {
      theOp.accept(this);
    }
    return mBuffer.toString();
  }

  public void visit(ValueOperand theOp) {
    mBuffer.append(" ").append(theOp.value()).append(" ");
  }

  public void visit(AddOperator theOp) {
    mBuffer.append("(");
    theOp.getLeftOperand().accept(this);
    mBuffer.append(" + ");
    theOp.getRightOperand().accept(this);
    mBuffer.append(")");
  }

  public void visit(MinusOperator theOp) {
    mBuffer.append("(");
    theOp.getLeftOperand().accept(this);
    mBuffer.append(" - ");
    theOp.getRightOperand().accept(this);
    mBuffer.append(")");
  }

  public void visit(DivideOperator theOp) {
    mBuffer.append("(");
    theOp.getLeftOperand().accept(this);
    mBuffer.append(" / ");
    theOp.getRightOperand().accept(this);
    mBuffer.append(")");
  }

  public void visit(MultiplyOperator theOp) {
    mBuffer.append("(");
    theOp.getLeftOperand().accept(this);
    mBuffer.append(" * ");
    theOp.getRightOperand().accept(this);
    mBuffer.append(")");
  }

  public static void main(String[] args) {
    Operand aExpr1 = new AddOperator(new ValueOperand(4), new ValueOperand(20));
    Operand aExpr2 = new DivideOperator(new MinusOperator(new ValueOperand(34.3),
        new MultiplyOperator(new ValueOperand(2), new ValueOperand(37))),
                                        aExpr1);
    System.err.println(new ReplVisitor().render(aExpr1) + " = " + aExpr1.value());
    System.err.println(new ReplVisitor().render(aExpr2) + " = " + aExpr2.value());
  }
}
