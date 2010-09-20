/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import java.io.*;
import java.util.*;
import org.apache.commons.lang.builder.*;

/**
 * Culture is a memory not being bound to a generation, but possibly persistent
 * during the whole history of a genotype (over all generations).
 * See GPConfiguration for current support of culture with Genetic Programming.<p>
 * Also see http://cs.gmu.edu/~sean/papers/culture-gp96.pdf
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Culture
    implements Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.16 $";

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
   * Width of a matrixed memory (optional, but necessary to set if you want
   * to treat the memory as a matrix).
   */
  private int m_width;


  /**
   * Constructor.
   *
   * @param a_size the size of the memory in cells (CultureMemoryCell instances)
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
    m_width = 1;
  }

  /**
   * Sets a memory cell with a given value. The memory cell will be newly
   * created for that.
   *
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
   *
   * @param a_index index of the memory cell
   * @param a_value value to set in the memory
   * @param a_historySize size of history to use, or less than 1 for turning
   * history off
   * @param a_infotext informative name of the memory cell
   * @return newly created memory cell set with the given value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CultureMemoryCell set(final int a_index, final Object a_value,
                               final int a_historySize, final String a_infotext) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Illegal memory index!");
    }
    CultureMemoryCell cell = new CultureMemoryCell(a_infotext, a_historySize);
    cell.setValue(a_value);
    m_memory[a_index] = cell;
    return cell;
  }

  /**
   * Sets a memory cell with a given value. The memory cell will be newly
   * created for that.
   *
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
      index = m_memoryNames.size() - 1;
    }
    CultureMemoryCell cell = new CultureMemoryCell(a_name, a_historySize);
    cell.setValue(a_value);
    m_memory[index] = cell;
    return cell;
  }

  /**
   * Retrieves the memory cell at the given index.
   *
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
   *
   * @param a_name name of the memory cell to read out
   * @return stored memory cell for given name, or null if name unknown
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
      throw new IllegalArgumentException("Memory name unknown: " + a_name);
    }
    return m_memory[index];
  }

  /**
   * Checks if a memory cell with the given name exists.
   *
   * @param a_name the name of the cell to check
   * @return true: cell excists
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean contains(final String a_name) {
    if (a_name == null || a_name.length() < 1) {
      throw new IllegalArgumentException("Illegal memory name!");
    }
    int index = m_memoryNames.indexOf(a_name);
    return index >=0;
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

  /**
   * The equals-method.
   * @param a_other the other object to compare
   * @return true if the objects are regarded as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      cex.printStackTrace();
      return false;
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
    Culture other = (Culture) a_other;
    if (other == null) {
      return 1;
    }
    // Problem: Vector does not implement Comparable. Thus we call toArray().
    // ----------------------------------------------------------------------
    return new CompareToBuilder()
        .append(m_size, other.m_size)
        .append(m_memory, other.m_memory)
        .append(m_memoryNames.toArray(), other.m_memoryNames.toArray())
        .toComparison();
  }

  /**
   * Sets the width of the matrix memory. Important to call before using
   * setMatrix/getMatrix!
   *
   * @param a_width the width the matrix should have
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setMatrixWidth(int a_width) {
    int size = size();
    if (a_width > size) {
      throw new IllegalArgumentException("Width must not be greater than the"
          + " size of the memory ("
          + size
          + ") !");
    }
    m_width = a_width;
  }

  /**
   * Stores a value in the matrix memory. Use setMatrixWidth(int) beforehand!
   *
   * @param a_x the first coordinate of the matrix (width)
   * @param a_y the second coordinate of the matrix (height)
   * @param a_value the value to store
   * @return created or used memory cell
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CultureMemoryCell setMatrix(int a_x, int a_y, Object  a_value) {
    int index = a_x * m_width + a_y;
    CultureMemoryCell cell = m_memory[index];
    if (cell == null) {
      cell = new CultureMemoryCell(a_x+"_"+a_y, -1);
    }
    cell.setValue(a_value);
    m_memory[index] = cell;
    return cell;
  }

  /**
   * Reads a value from the matrix memory that was previously stored with
   * setMatrix(...).
   *
   * @param a_x the first coordinate of the matrix (width)
   * @param a_y the second coordinate of the matrix (height)
   * @return read value
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CultureMemoryCell getMatrix(int a_x, int a_y) {
    int index = a_x * m_width + a_y;
    return get(index);
  }
}
