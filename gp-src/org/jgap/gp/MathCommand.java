package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.*;

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

  /**@todo currently not used*/
  protected MathLanguage getMathLanguage() {
    return ( (MathLanguage) getLanguage());
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
