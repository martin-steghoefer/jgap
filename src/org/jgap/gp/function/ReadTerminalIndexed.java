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
 * Reads a value from the internal indexed memory.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class ReadTerminalIndexed
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Index of the memory cell to read.
   */
  private int m_index;

  public ReadTerminalIndexed(final GPConfiguration a_conf, Class a_returnType,
                             int a_index)
      throws InvalidConfigurationException {
    this(a_conf, a_returnType, a_index, 0);
  }

  public ReadTerminalIndexed(final GPConfiguration a_conf, Class a_returnType,
                             int a_index, int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType, a_subReturnType, null);
    if (a_index < 0 || a_index > getGPConfiguration().getMemorySize()) {
      throw new IllegalArgumentException("Memory index invalid!");
    }
    m_index = a_index;
  }

  public String toString() {
    return "read_from_index(" + m_index + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Read Terminal Indexed";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    // Read from memory.
    // -----------------
    try {
      try {
        Integer value = (Integer) getGPConfiguration().readIndexedMemory(m_index);
        if (value == null) {
          return 0;
        }
        return value.intValue();
      } catch (NullPointerException nex) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminalIndexed without preceeding StoreTerminalIndexed");
    }
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    try {
      try {
        return ( (Long) getGPConfiguration().readIndexedMemory(m_index)).
            longValue();
      } catch (NullPointerException nex) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminalIndexed without preceeding StoreTerminalIndexed");
    }
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    try {
      try {
        return ( (Double) getGPConfiguration().readIndexedMemory(m_index)).
            doubleValue();
      } catch (NullPointerException nex) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminalIndexed without preceeding StoreTerminalIndexed");
    }
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    try {
      try {
        return ( (Float) getGPConfiguration().readIndexedMemory(m_index)).
            floatValue();
      } catch (NullPointerException nex) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminalIndexed without preceeding StoreTerminalIndexed");
    }
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    try {
      try {
        return getGPConfiguration().readIndexedMemory(m_index);
      } catch (NullPointerException nex) {
        throw new IllegalArgumentException();
      }
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ReadTerminalIndexed without preceeding StoreTerminalIndexed");
    }
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
    ReadTerminalIndexed other = (ReadTerminalIndexed) a_other;
    return new CompareToBuilder()
        .append(m_index, other.m_index)
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
      ReadTerminalIndexed other = (ReadTerminalIndexed) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_index, other.m_index)
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
      ReadTerminalIndexed result = new ReadTerminalIndexed(getGPConfiguration(),
          getReturnType(), m_index, getSubReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
