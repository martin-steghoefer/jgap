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

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Abstract base class for GP-commands related to mathematical calculation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class MathCommand
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  public MathCommand(final Configuration a_conf, int a_arity,
                     Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_returnType);
  }

  public void setAllele(Object a_newValue) {
  }

  public Object getAllele() {
    return null;
  }

  public int compareTo(Object o) {
    return 0;/**@todo fix*/
  }

  public boolean equals(Object o1) {
    return compareTo(o1) == 0;
  }

  public Class getChildType(GPProgram a_ind, int i) {
    return getReturnType();
  }
}
