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
 * Interface for mathematical operands.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public interface Operand
    extends ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  double value();

  void accept(MathVisitor theVisitor);
}
