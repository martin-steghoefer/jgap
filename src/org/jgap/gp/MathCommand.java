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
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public MathCommand(final Configuration a_conf, int a_arity,
                     Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_returnType);
  }

  protected double getState(GPConfiguration config) {
    return ( (Double) config.getState()).doubleValue();
  }

  protected void setState(GPConfiguration config, double a_state) {
    config.setState(new Double(a_state));
  }

  public void setAllele(Object a_newValue) {
  }

  public Object getAllele() {
    return null;
  }

  public int compareTo(Object o) {
    return 0;
  }

  public boolean equals(Object o1) {
    return compareTo(o1) == 0;
  }

  public double getFitnessValue() {
    return 0;
  }

  public Class getChildType(int i) {
    return getReturnType();
  }
}
