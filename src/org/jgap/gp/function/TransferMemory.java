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
 * Transfers a memory value to another memory cell.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class TransferMemory
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_sourceStorageName;

  private String m_targetStorageName;

  public TransferMemory(final GPConfiguration a_conf,
                        String a_sourceStorageName,
                        String a_targetStorageName)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
    if (a_sourceStorageName == null || a_sourceStorageName.length() < 1) {
      throw new IllegalArgumentException(
          "Source memory name must not be empty!");
    }
    if (a_targetStorageName == null || a_targetStorageName.length() < 1) {
      throw new IllegalArgumentException(
          "Target memory name must not be empty!");
    }
    if (a_sourceStorageName.equals(a_targetStorageName)) {
      throw new IllegalArgumentException(
          "Source and target memory name must be different!");
    }
    m_sourceStorageName = a_sourceStorageName;
    m_targetStorageName = a_targetStorageName;
  }

  public String toString() {
    return "transfer("
        + m_sourceStorageName
        + " -> "
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
    return "Transfer Memory";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    // Read from memory.
    // -----------------
    try {
      Object value = getGPConfiguration().readFromMemory(m_sourceStorageName);
      // Store in memory.
      // ----------------
      getGPConfiguration().storeInMemory(m_targetStorageName, value);
    } catch (IllegalArgumentException iex) {
      throw new IllegalStateException(
          "TransferMemory without preceeding StoreTerminal");
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
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    TransferMemory other = (TransferMemory) a_other;
    return new CompareToBuilder()
        .append(m_sourceStorageName, other.m_sourceStorageName)
        .append(m_targetStorageName, other.m_targetStorageName)
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
      TransferMemory other = (TransferMemory) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_sourceStorageName, other.m_sourceStorageName)
          .append(m_targetStorageName, other.m_targetStorageName)
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
      TransferMemory result = new TransferMemory(getGPConfiguration(),
          m_sourceStorageName, m_targetStorageName);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
