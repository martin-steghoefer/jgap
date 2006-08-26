/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Pushes a value onto the stack.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Push
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Class m_type;

  public Push(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass);
    m_type = a_type;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new Push(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    return "push &1";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Integer(value));
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Integer(value));
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = c.execute_long(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Long(value));
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = c.execute_double(n, 0, args);
    pushIt(new Double(value));
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = c.execute_float(n, 0, args);
    pushIt(new Float(value));
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = c.execute_object(n, 0, args);
    pushIt(value);
    return value;
  }

  public static interface Compatible {
    public Object execute_push(Object o);
  }

  public boolean isAffectGlobalState() {
    return true;
  }

  public boolean isValid(ProgramChromosome a_program) {
    /**@todo consider n (execute_int...)*/
    return a_program.getCommandOfClass(0, Pop.class) >= 0;
  }

  /**
   * Helper method
   * @param a_value the value to push onto the stack
   */
  protected void pushIt(Object a_value) {
    ((GPConfiguration)getConfiguration()).pushToStack(a_value);
  }

  public Class getChildType(GPProgram a_ind, int a_chromNum) {
    return m_type;
  }
}
