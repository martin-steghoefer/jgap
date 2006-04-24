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

/**
 * Culture is a memory not being bound to a generation, but possibly persistent
 * during the whole history of a genotype (over all generations).<p>
 * Also see http://cs.gmu.edu/~sean/papers/culture-gp96.pdf
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Culture {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * The storage to use.
   */
  private CultureMemoryCell[] m_memory;

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
    m_memory = new CultureMemoryCell[a_size];
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
}
