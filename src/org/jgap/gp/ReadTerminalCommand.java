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
 * Reads a value from the internal memory.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ReadTerminalCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private String m_storageName;

  public ReadTerminalCommand(final Configuration a_conf, Class type,
                             String a_storageName)
      throws InvalidConfigurationException {
    super(a_conf, 0, type);
    m_storageName = a_storageName;
  }

  protected Gene newGeneInternal() {
    /**@todo not used for GP*/
    try {
      Gene gene = new ReadTerminalCommand(getConfiguration(), getReturnType(),
                                          m_storageName);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
    System.err.println("appliedMutation");
  }

  public String toString() {
    return "read_from(" + m_storageName + ", &1)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    // Read from memory.
    // -----------------
    try {
      return ( (Integer) ( (GPConfiguration) getConfiguration()).readFromMemory(
          m_storageName)).intValue();
    }
    catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminal without preceeding StoreTerminal");
    }
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    try {
      return ( (Long) ( (GPConfiguration) getConfiguration()).readFromMemory(
          m_storageName)).intValue();
    }
    catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminal without preceeding StoreTerminal");
    }
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    try {
      return ( (Double) ( (GPConfiguration) getConfiguration()).readFromMemory(
          m_storageName)).intValue();
    }
    catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminal without preceeding StoreTerminal");
    }
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    try {
      return ( (Float) ( (GPConfiguration) getConfiguration()).readFromMemory(
          m_storageName)).intValue();
    }
    catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminal without preceeding StoreTerminal");
    }
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    try {
      return ( (GPConfiguration) getConfiguration()).readFromMemory(
          m_storageName);
    }
    catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminal without preceeding StoreTerminal");
    }
  }

  public static interface Compatible {
    public Object execute_read(Object o);
  }
}
