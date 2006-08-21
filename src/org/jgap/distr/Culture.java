/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import java.util.*;

/**
 * Culture is a memory not being bound to a generation, but possibly persistent
 * during the whole history of a genotype (over all generations).<p>
 * Also see http://cs.gmu.edu/~sean/papers/culture-gp96.pdf
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Culture implements java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * The storage to use.
   */
  private CultureMemoryCell[] m_memory;

  /**
   * Storage for named indices.
   */
  private List m_memoryNames = new Vector();

  /**
   * Number of memory cells available.
   */
  private int m_size;

  /**
   * Constructor.
   * @param a_size size of the memory
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Culture(final int a_size) {
    if (a_size < 1) {
      throw new IllegalArgumentException("Size must be greater than zero!");
    }
    m_size = a_size;
    m_memory = new CultureMemoryCell[m_size];
  }

  /**
   * Sets a memory cell with a given value. The memory cell will be newly
   * created for that.
   * @param a_index index of the memory cell
   * @param a_value value to set in the memory
   * @param a_historySize size of history to use, or less than 1 for turning
   * history off
   * @param a_name informative name of the memory cell
   * @return newly created memory cell set with the given value
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public CultureMemoryCell set(final int a_index, final double a_value,
                  final int a_historySize, final String a_name) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Illegal memory index!");
    }
    CultureMemoryCell cell = new CultureMemoryCell(a_name, a_historySize);
    cell.setDouble(a_value);
    m_memory[a_index] = cell;
    return cell;
  }

  /**
   * Sets a memory cell with a given value. The memory cell will be newly
   * created for that.
   * @param a_index index of the memory cell
   * @param a_value value to set in the memory
   * @param a_historySize size of history to use, or less than 1 for turning
   * history off
   * @param a_name informative name of the memory cell
   * @return newly created memory cell set with the given value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CultureMemoryCell set(final int a_index, final Object a_value,
                  final int a_historySize, final String a_name) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Illegal memory index!");
    }
    CultureMemoryCell cell = new CultureMemoryCell(a_name, a_historySize);
    cell.setValue(a_value);
    m_memory[a_index] = cell;
    return cell;
  }

  /**
   * Sets a memory cell with a given value. The memory cell will be newly
   * created for that.
   * @param a_name named index of the memory cell
   * @param a_value value to set in the memory
   * @param a_historySize size of history to use, or less than 1 for turning
   * history off
   * @return newly created memory cell set with the given value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CultureMemoryCell set(final String a_name, final Object a_value,
                  final int a_historySize) {
    if (a_name == null || a_name.length() < 1) {
      throw new IllegalArgumentException("Illegal memory name!");
    }
    int index = m_memoryNames.indexOf(a_name);
    if (index < 0) {
      // Create new named index.
      // -----------------------
      m_memoryNames.add(a_name);
      index = m_memoryNames.size() -1;
    }
    CultureMemoryCell cell = new CultureMemoryCell(a_name, a_historySize);
    cell.setValue(a_value);
    m_memory[index] = cell;
    return cell;
  }

  /**
   * Retrieves the memory cell at the given index.
   * @param a_index index of the memory cell to read out
   * @return stored memory cell at given index
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public CultureMemoryCell get(final int a_index) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Illegal memory index!");
    }
    return m_memory[a_index];
  }

  /**
   * Retrieves the memory cell at the given index.
   * @param a_name name of the memory cell to read out
   * @return stored memory cell for given name, or null of name unknown
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CultureMemoryCell get(final String a_name) {
    if (a_name == null || a_name.length() < 1) {
      throw new IllegalArgumentException("Illegal memory name!");
    }
    int index = m_memoryNames.indexOf(a_name);
    if (index < 0) {
      throw new IllegalArgumentException("Memory name unknown!");
    }
    return m_memory[index];
  }

  /**
   * @return size of the memory
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int size() {
    return m_memory.length;
  }

  /**
   * @return String representation of the cultural memory
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public String toString() {
    StringBuffer result = new StringBuffer("[");
    int len = m_memory.length;
    if (m_memory[0] == null) {
      result.append(m_memory[0]);
    }
    else {
      result.append(m_memory[0].toString());
    }
    for (int i = 1; i < len; i++) {
      if (m_memory[i] == null) {
        result.append(";" + m_memory[i]);
      }
      else {
        result.append(";" + m_memory[i].toString());
      }
    }
    result.append("]");
    return result.toString();
  }

  /**
   * Clears the memory.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void clear() {
    m_memory = new CultureMemoryCell[m_size];
    m_memoryNames.clear();
  }

  /**
   * @return cloned list of symbolic memory names
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public List getMemoryNames() {
    return new Vector(m_memoryNames);
  }
}
