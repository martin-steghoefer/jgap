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
import org.apache.commons.lang.builder.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Exchanges the values of two memory cells.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class ExchangeMemory
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_sourceStorageName;

  private String m_targetStorageName;

  public ExchangeMemory(final GPConfiguration a_conf,
                        String a_firstStorageName,
                        String a_secondStorageName)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
    if (a_firstStorageName == null || a_firstStorageName.length() < 1) {
      throw new IllegalArgumentException(
          "First memory name must not be empty!");
    }
    if (a_secondStorageName == null || a_secondStorageName.length() < 1) {
      throw new IllegalArgumentException(
          "Second memory name must not be empty!");
    }
    if (a_firstStorageName.equals(a_secondStorageName)) {
      throw new IllegalArgumentException(
          "First and second memory name must be different!");
    }
    m_sourceStorageName = a_firstStorageName;
    m_targetStorageName = a_secondStorageName;
  }

  public String toString() {
    return "exchange("
        + m_sourceStorageName
        + ", "
        + m_targetStorageName
        + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Exchange Memory";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    // Read from memory.
    // -----------------
    try {
      Object value1 = getGPConfiguration().readFromMemory(
          m_sourceStorageName);
      Object value2 = getGPConfiguration().readFromMemory(
          m_targetStorageName);
      // Store in memory.
      // ----------------
      getGPConfiguration().storeInMemory(m_sourceStorageName, value2);
      getGPConfiguration().storeInMemory(m_targetStorageName, value1);
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "ExchangeMemory without preceeding StoreTerminal");
    }
  }

  public boolean isAffectGlobalState() {
    /**@todo subclass from new abstract class MemoryCommand?*/
    return true;
  }

  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return null;
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
      ExchangeMemory other = (ExchangeMemory) a_other;
      return new CompareToBuilder()
          .append(m_sourceStorageName, other.m_sourceStorageName)
          .append(m_targetStorageName, other.m_targetStorageName)
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
        ExchangeMemory other = (ExchangeMemory) a_other;
        return new EqualsBuilder()
            .append(m_sourceStorageName, other.m_sourceStorageName)
            .append(m_targetStorageName, other.m_targetStorageName)
            .isEquals();
      } catch (ClassCastException cex) {
        return false;
      }
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
      ExchangeMemory result = new ExchangeMemory(getGPConfiguration(),
          m_sourceStorageName, m_targetStorageName);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
