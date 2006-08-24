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
 * Stores a value in the internal memory.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class StoreTerminal
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_storageName;

  private Class m_type;

  public StoreTerminal(final Configuration a_conf, String a_storageName,
                              Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass);
    m_type = a_type;
    if (a_storageName == null || a_storageName.length() < 1) {
      throw new IllegalArgumentException("Memory name must not be empty!");
    }
    m_storageName = a_storageName;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new StoreTerminal(getConfiguration(), m_storageName,
                                           m_type);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    return "store_in(" + m_storageName + ", &1)";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = null;
    if (m_type == CommandGene.IntegerClass) {
      value = new Integer(c.execute_int(n, 0, args));
    }
    /**@todo add additional types*/
    else {
      value = c.execute(n, 0, args);
    }
    // Store in memory.
    // ----------------
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName,
        value);
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Store in memory.
    // ----------------
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName,
        new Integer(value));
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = c.execute_long(n, 0, args);
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName,
        new Long(value));
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = c.execute_double(n, 0, args);
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName,
        new Double(value));
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = c.execute_float(n, 0, args);
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName,
        new Float(value));
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = c.execute_object(n, 0, args);
    ( (GPConfiguration) getConfiguration()).storeInMemory(m_storageName, value);
    return value;
  }

  public boolean isAffectGlobalState() {
    return true;
  }

  public boolean isValid(ProgramChromosome a_program) {
    return getIndividual().getCommandOfClass(0, ReadTerminal.class) > 0;
  }

  public Class getChildType(int i) {
    return m_type;
  }
}
