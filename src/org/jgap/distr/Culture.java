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
 * during the whole history of a genotype (over all generations)
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Culture {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * The storage to use. I decided to use double values as they are as
   * performant as integers (or even faster) and are usable generically.
   */
  private double m_memory[];

  /**
   * Constructor
   * @param a_size size of the memory
   */
  public Culture(int a_size) {
    if (a_size < 1) {
      throw new IllegalArgumentException("Size must be greater than zero!");
    }
    m_memory = new double[a_size];
  }

  /**
   * Sets a memory cell with a given value
   * @param a_index the index of the memory cell
   * @param a_value the value to set in the memory
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void set(int a_index, double a_value) {
    if (a_index < 0 || a_index>= size()) {
      throw new IllegalArgumentException("Illegal memory index!");
    }
    m_memory[a_index] = a_value;
  }

  /**
   * Retrieves the value of a given memory cell
   * @param a_index the index of the memory cell to read out
   * @return value currently stored in memory cell
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double get(int a_index) {
    if (a_index < 0 || a_index>= size()) {
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
    result.append(m_memory[0]);
    for (int i = 1; i < len; i++) {
      result.append("; "+m_memory[i]);
    }
    result.append("]");
    return result.toString();
  }
}