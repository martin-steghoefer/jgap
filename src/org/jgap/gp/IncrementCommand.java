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
 * The increment operation.
 *
 * @author Konrad Odell
 * @since 3.0
 */
public class IncrementCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  private double incAmt = 1;
  
  public IncrementCommand(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 2, a_type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new IncrementCommand(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
  }

  public String toString() {
    return "+" + incAmt;
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return new Double(c.execute_int(n, 0, args) + c.execute_int(n, 1, args) +incAmt).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return new Double(c.execute_long(n, 0, args) + c.execute_long(n, 1, args) +incAmt ).longValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return new Double(c.execute_float(n, 0, args) + c.execute_float(n, 1, args) +incAmt).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) + c.execute_double(n, 1, args) +incAmt;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_increment(c.
        execute_object(n, 1, args));
  }

  public static interface Compatible {
    public Object execute_increment(Object o);
  }

  public double getIncAmt()
  {
    return incAmt; 
  }
  
  public void setIncAmt(double incAmt)
  {
    this.incAmt = incAmt;
  }
}
