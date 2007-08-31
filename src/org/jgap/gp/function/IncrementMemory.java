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

import org.jgap.*;
import org.jgap.gp.*;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.jgap.gp.impl.*;

/**
 * Increments the value of a memory cell and returns the incremented value.
 * Allows presetting a value in case* memory cell is not initialized.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class IncrementMemory
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  private int m_increment;

  private String m_memoryName;

  private int m_initialValue;

  /**
   * Constructor for using an increment of 1.
   *
   * @param a_conf the configuration to use
   * @param a_type the type of the terminal to increment (e.g. IntegerClass)
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IncrementMemory(final GPConfiguration a_conf, Class a_type,
                         String a_memoryName, int a_initialValue)
      throws InvalidConfigurationException {
    this(a_conf, a_type, a_memoryName, a_initialValue, 1);
  }

  /**
   * Constructor to freely choose increment.
   *
   * @param a_conf the configuration to use
   * @param a_type the type of the terminal to increment (e.g. IntegerClass)
   * @param a_increment the increment to use, may also be negative
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IncrementMemory(final GPConfiguration a_conf, Class a_type,
                         String a_memoryName, int a_initialValue,
                         int a_increment)
      throws InvalidConfigurationException {
    this(a_conf, a_type, a_memoryName, a_initialValue, a_increment, 0, 0);
  }

  public IncrementMemory(final GPConfiguration a_conf, Class a_type,
                         String a_memoryName, int a_initialValue,int a_increment,
                         int a_subReturnType, int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_type, a_subReturnType, a_subChildType);
    m_increment = a_increment;
    m_memoryName = a_memoryName;
    m_initialValue = a_initialValue;
  }

  public String toString() {
    if (m_increment == 1) {
      return "INCMEM(" + m_memoryName + ")";
    }
    else {
      return "INCMEM(" + m_memoryName + ", " + m_increment + ")";
    }
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "INCMEM(" + m_memoryName + ")";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    Integer value = (Integer) getGPConfiguration().readFromMemoryIfExists(
        m_memoryName);
    int valueI;
    if (value == null) {
      valueI = m_initialValue;
    }
    else {
      valueI = value.intValue() + 1;
    }
    getGPConfiguration().storeInMemory(m_memoryName, new Integer(valueI));
    return valueI;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      IncrementMemory other = (IncrementMemory) a_other;
      return new CompareToBuilder()
          .append(m_increment, other.m_increment)
          .append(m_memoryName, other.m_memoryName)
          .toComparison();
    }
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean equals(Object a_other) {
    if (a_other == null) {
      return false;
    }
    else {
      try {
        IncrementMemory other = (IncrementMemory) a_other;
        return new EqualsBuilder()
            .append(m_increment, other.m_increment)
            .append(m_memoryName, other.m_memoryName)
            .isEquals();
      } catch (ClassCastException cex) {
        return false;
      }
    }
  }
}
