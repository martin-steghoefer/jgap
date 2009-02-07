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
 *
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public interface MathVisitor {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  void visit(ValueOperand theOp);

  void visit(AddOperator theOp);

  void visit(MinusOperator theOp);

  void visit(DivideOperator theOp);

  void visit(MultiplyOperator theOp);
}
