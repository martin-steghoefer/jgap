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
 * Represents a memory cell used within {@link org.jgap.distr.Culture}, a
 * special form of memory.
 * <P>
 * CultureMemoryCell also stores metadata along with the value-to-store, like
 * date/time of setting a value, change history.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class CultureMemoryCell
    implements Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  /**
   * Informative name of the memory cell (optional)
   */
  private String m_name;

  /**
   * Version of the memory value, i.e. how many times has setValue(..) been
   * called)? First version (no value assigned) is zero, second version (first
   * time a value is assigned to the memory) is 1 etc.
   */
  private int m_version;

  /**
   * Value of the memory cell
   */
  private Object m_value;

  /**
   * How many times has the memory cell been read out?
   */
  private int m_readAccessed;

  /**
   * How many historical values should be kept for evaluation purposes?
   * Values less than one turn history feature off
   */
  private int m_historySize;

  private int m_internalHistorySize;

  /**
   * If history logging turned off, we need to keep the prior version for
   * evaluation purpose, e.g. see getReadAccessedCurrentVersion
   */
  private CultureMemoryCell m_previousVersion;

  /**
   * History of memory values
   */
  private List m_history;

  /**
   * Time in milliseconds when the version number is incremented (e.g. when
   * setting the value of the cell)
   */
  private long m_dateTimeVersion;

  /**
   * Default constructor.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public CultureMemoryCell() {
    this(null);
  }

  /**
   * Sets history size to 3.
   * @param a_name informative name of the memory cell
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public CultureMemoryCell(final String a_name) {
    this(a_name, 3);
  }

  /**
   * Allows to freely specify informative name of memory cell as well as size
   * of history to keep.
   * @param a_name informative name of the memory cell
   * @param a_historySize size of history to keep. Use values less than 1 for
   * turning history logging off
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public CultureMemoryCell(final String a_name, final int a_historySize) {
    setHistorySize(a_historySize);
    m_history = new Vector(getHistorySize());
    setName(a_name);
  }

  /**
   * Sets the informative name of the memory cell.
   * @param a_name informative name
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setName(final String a_name) {
    m_name = a_name;
  }

  /**
   * @return informative name of the memory cell
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public String getName() {
    return m_name;
  }

  /**
   * Sets a new memory value.
   * @param a_value the memory value to set
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setValue(final Object a_value) {
    if (m_historySize > 0) {
      keepHistory(a_value, getVersion(), getName());
    }
    else {
      m_previousVersion = getNewInstance(m_value, getVersion(),
          getName());
    }
    m_value = a_value;
    incrementVersion();
  }

  /**
   * Convenience method to store a primitive double easily.
   *
   * @param a_value double value to store
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setDouble(final double a_value) {
    setValue(new Double(a_value));
  }

  /**
   * Convenience method to retrieve a primitive double value from memory
   * easily. Here a ClassCastException could occur!
   * @return double value representing current memory value
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double getCurrentValueAsDouble() {
    return ( (Double) getCurrentValue()).doubleValue();
  }

  /**
   * @return current memory value
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Object getCurrentValue() {
    m_readAccessed++;
    return m_value;
  }

  /**
   * @return list of most recent entries (except current entry). The item at
   * index 0 is the oldest, the item at highest index is the youngest one.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getHistory() {
    return m_history;
  }

  /**
   * @return version of memory value, read only
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getVersion() {
    return m_version;
  }

  /**
   * Increment version number and keep track of current time.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  protected void incrementVersion() {
    m_version++;
    // Memorize current time.
    m_dateTimeVersion = System.currentTimeMillis();
  }

  /**
   * Puts an entry into history. Stores all information as a new
   * CultureMemoryCell instance.
   *
   * @param a_value memory value to store
   * @param a_version version of the value
   * @param a_name name to store
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  protected void keepHistory(final Object a_value, final int a_version,
                             final String a_name) {
    trimHistory(m_historySize - 1);
    // simply add a new instance of CultureMemoryCell for keeping history track
    CultureMemoryCell cell = getNewInstance(a_value, a_version, a_name);
    cell.m_internalHistorySize = m_historySize;
    m_history.add(cell);
  }

  /**
   * Creates a new instance of CultureMemoryCell preset with the given
   * parameters. Used for creating history entries.
   *
   * @param a_value memory value to store
   * @param a_version version of the value
   * @param a_name name to store
   * @return new instance of CultureMemoryCell
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  protected CultureMemoryCell getNewInstance(final Object a_value,
      final int a_version,
      final String a_name) {
    // DON'T USE SETTERS IN HERE BECAUSE OF ENDLESS LOOPS!
    CultureMemoryCell cell = new CultureMemoryCell(a_name, 0);
    cell.m_value = a_value;
    cell.m_version = a_version;
    return cell;
  }

  /**
   * @return number of times the memory cell has been read accessed
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getReadAccessed() {
    return m_readAccessed;
  }

  /**
   * @return number of read accesses since current value has been set.
   * Calculated by subtracting number of read accesses for prior version from
   * total number of read accesses
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getReadAccessedCurrentVersion() {
    if (m_historySize < 1) {
      // Use internal (simple/atomic) history.
      // -------------------------------------
      return getReadAccessed() - m_previousVersion.getReadAccessed();
    }
    else {
      // Use sophisticated history (list).
      // ---------------------------------
      CultureMemoryCell cell = (CultureMemoryCell) m_history.get(
          m_history.size() - 1);
      return getReadAccessed() - cell.getReadAccessed();
    }
  }

  /**
   * Sets the size of the history and scales down the history log it is larger
   * than the given size.
   * @param a_size new size of the history log = how many entries to store
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setHistorySize(final int a_size) {
    if (getHistory() != null && a_size > getHistory().size()) {
      trimHistory(a_size);
      m_historySize = a_size;
    }
    else if (a_size < 0) {
      m_historySize = 0;
    }
    else {
      m_historySize = a_size;
    }
  }

  /**
   * Trims the history to the given size.
   * @param a_size new size of history
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  protected void trimHistory(final int a_size) {
    // trim length of history
    while (m_history.size() > a_size) {
      // remove one entry (always the first one = oldest one)
      m_history.remove(0);
    }
  }

  /**
   * @return size of the history
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getHistorySize() {
    return m_historySize;
  }

  /**
   * @return String representation of the cultural memory cell including all
   * important information (also history log).
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    toStringRecursive(result, getHistorySize());
    return result.toString();
  }

  /**
   * Recursive part of toString().
   * @param a_result gathered result so far and modified here
   * @param a_historySize history size just for information purpose
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  protected void toStringRecursive(StringBuffer a_result,
                                   final int a_historySize) {
    List history = getHistory();
    a_result.append("[Name:" + getName() + ";");
    a_result.append("Value:" + m_value + ";"); //not getCurrentValue()!
    a_result.append("Version:" + getVersion() + ";");
    a_result.append("Read accessed:" + getReadAccessed() + ";");
    a_result.append("History Size:" + a_historySize + ";");
    a_result.append("History:[");
    for (int i = 0; i < history.size(); i++) {
      if (i > 0) {
        a_result.append(";");
      }
      CultureMemoryCell cell = (CultureMemoryCell) history.get(i);
      // do recursive call
      cell.toStringRecursive(a_result, cell.m_internalHistorySize);
      a_result.append("]");
    }
    a_result.append("]");
  }

  /**
   * @return time in milliseconds when the current version has been created
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public long getVersionTimeMilliseconds() {
    return m_dateTimeVersion;
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
      return false;
    }
  }

  /**
   * The compareTo-method.
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    CultureMemoryCell other = (CultureMemoryCell) a_other;
    if (other == null) {
      return 1;
    }
    return new CompareToBuilder()
        .append(m_value, other.m_value)
        .append(m_version, other.m_version)
        .append(m_historySize, other.m_historySize)
        .toComparison();
  }
}
