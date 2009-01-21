/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Stores a value in the internal memory.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class StoreTerminal
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_storageName;

  private Class m_type;

  public StoreTerminal(final GPConfiguration a_conf, String a_storageName,
                       Class a_childType)
      throws InvalidConfigurationException {
    this(a_conf, a_storageName, a_childType, 0, 0);
  }

  /**
   * Allows setting a sub type and sub return type.
   *
   * @param a_conf GPConfiguration
   * @param a_storageName String
   * @param a_childType Class
   * @param a_subChildType int
   * @param a_subReturnType int
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public StoreTerminal(final GPConfiguration a_conf, String a_storageName,
                       Class a_childType, int a_subReturnType,
                       int a_subChildType
      )
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass, a_subReturnType,
          new int[] {a_subChildType});
    m_type = a_childType;
    if (a_storageName == null || a_storageName.length() < 1) {
      throw new IllegalArgumentException("Memory name must not be empty!");
    }
    m_storageName = a_storageName;
  }

  public String toString() {
    return "store(" + m_storageName + ", &1)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Store Terminal(" + m_storageName + ")";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = null;
    if (m_type == CommandGene.IntegerClass) {
      value = new Integer(c.execute_int(n, 0, args));
    }
    else if (m_type == CommandGene.LongClass) {
      value = new Long(c.execute_long(n, 0, args));
    }
    else if (m_type == CommandGene.DoubleClass) {
      value = new Double(c.execute_double(n, 0, args));
    }
    else if (m_type == CommandGene.FloatClass) {
      value = new Float(c.execute_float(n, 0, args));
    }
    else {
      value = c.execute(n, 0, args);
    }
    // Store in memory.
    // ----------------
    getGPConfiguration().storeInMemory(m_storageName, value);
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Store in memory.
    // ----------------
    getGPConfiguration().storeInMemory(m_storageName, new Integer(value));
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = c.execute_long(n, 0, args);
    getGPConfiguration().storeInMemory(m_storageName, new Long(value));
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = c.execute_double(n, 0, args);
    getGPConfiguration().storeInMemory(m_storageName, new Double(value));
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = c.execute_float(n, 0, args);
    getGPConfiguration().storeInMemory(m_storageName, new Float(value));
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = c.execute_object(n, 0, args);
    getGPConfiguration().storeInMemory(m_storageName, value);
    return value;
  }

  public boolean isAffectGlobalState() {
    return true;
  }

  public boolean isValid(ProgramChromosome a_program) {
    return a_program.getIndividual().getCommandOfClass(0, ReadTerminal.class) >
        0;
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return m_type;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    StoreTerminal other = (StoreTerminal) a_other;
    return new CompareToBuilder()
        .append(m_storageName, other.m_storageName)
        .append(m_type, other.m_type)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      StoreTerminal other = (StoreTerminal) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_storageName, other.m_storageName)
          .append(m_type, other.m_type)
          .isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      StoreTerminal result = new StoreTerminal(getGPConfiguration(),
          m_storageName, m_type, getSubReturnType(), getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
