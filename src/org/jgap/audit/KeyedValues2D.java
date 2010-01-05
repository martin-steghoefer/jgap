/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import java.io.*;
import java.util.*;
import org.jgap.util.*;

/**
 * A collection of (row, column) tupels
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class KeyedValues2D
    implements ICloneable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.7 $";

  /** The row keys */
  private List m_rowKeys;

  /** The column keys */
  private List m_columnKeys;

  /** The row data */
  private List m_rows;

  /** Should row keys be sorted by their comparable order? */
  private boolean m_sortRowKeys;

  /**
   * Constructor setting behavior: non-sorted keys
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public KeyedValues2D() {
    this(false);
  }

  /**
   * Constructor.
   *
   * @param a_sortRowKeys true: row keys should be sorted
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public KeyedValues2D(final boolean a_sortRowKeys) {
    m_rowKeys = Collections.synchronizedList(new ArrayList());
    m_columnKeys = Collections.synchronizedList(new ArrayList());
    m_rows = Collections.synchronizedList(new ArrayList());
    m_sortRowKeys = a_sortRowKeys;
  }

  /**
   * @return the number of rows
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getRowCount() {
    return m_rowKeys.size();
  }

  /**
   * @return the number of columns
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getColumnCount() {
    return m_columnKeys.size();
  }

  /**
   * Returns the value for a given row and column
   *
   * @param a_row the row index
   * @param a_column the column index
   *
   * @return value at given row and column
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Number getValue(final int a_row, final int a_column) {
    Number result = null;
    final KeyedValues rowData = (KeyedValues) m_rows.get(a_row);
    final Comparable columnKey = (Comparable) m_columnKeys.get(a_column);
    if (columnKey != null) {
      result = rowData.getValue(columnKey);
    }
    return result;
  }

  /**
   * @param a_row the row index, starting at 0
   *
   * @return key for the given row
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Comparable getRowKey(final int a_row) {
    return (Comparable) m_rowKeys.get(a_row);
  }

  /**
   * @param a_key the row key
   *
   * @return row index for the given key
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getRowIndex(final Comparable a_key) {
    if (m_sortRowKeys) {
      return Collections.binarySearch(m_rowKeys, a_key);
    }
    else {
      return m_rowKeys.indexOf(a_key);
    }
  }

  /**
   * @return row keys
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getRowKeys() {
    return Collections.unmodifiableList(m_rowKeys);
  }

  /**
   * @param a_column the column
   *
   * @return key for the given column
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Comparable getColumnKey(final int a_column) {
    return (Comparable) m_columnKeys.get(a_column);
  }

  /**
   * @return the column keys
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getColumnKeys() {
    return Collections.unmodifiableList(m_columnKeys);
  }

  /**
   * @param a_rowKey the row key
   * @param a_columnKey the column key
   *
   * @return value for the given row and column keys
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Number getValue(final Comparable a_rowKey,
                         final Comparable a_columnKey) {
    Number result;
    final int row = getRowIndex(a_rowKey);
    if (row >= 0) {
      final KeyedValues rowData = (KeyedValues) m_rows.get(row);
      result = rowData.getValue(a_columnKey);
    }
    else {
      result = null;
    }
    return result;
  }

  /**
   * Sets a value.
   *
   * @param a_value the value
   * @param a_rowKey the row key
   * @param a_columnKey the column key
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setValue(final Number a_value, final Comparable a_rowKey,
                       final Comparable a_columnKey) {
    final KeyedValues row;
    int rowIndex = getRowIndex(a_rowKey);
    if (rowIndex >= 0) {
      row = (KeyedValues) m_rows.get(rowIndex);
    }
    else {
      row = new KeyedValues();
      if (m_sortRowKeys) {
        rowIndex = -rowIndex - 1;
        m_rowKeys.add(rowIndex, a_rowKey);
        m_rows.add(rowIndex, row);
      }
      else {
        m_rowKeys.add(a_rowKey);
        m_rows.add(row);
      }
    }
    row.setValue(a_columnKey, a_value);
    final int columnIndex = m_columnKeys.indexOf(a_columnKey);
    if (columnIndex < 0) {
      m_columnKeys.add(a_columnKey);
    }
  }

  /**
   * Tests if this object is equal to another
   *
   * @param a_object other object.
   *
   * @return true: this object is equal to the other one
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public boolean equals(final Object a_object) {
    if (a_object == null) {
      return false;
    }
    if (a_object == this) {
      return true;
    }
    if (! (a_object instanceof KeyedValues2D)) {
      return false;
    }
    final KeyedValues2D kv2D = (KeyedValues2D) a_object;
    if (!getRowKeys().equals(kv2D.getRowKeys())) {
      return false;
    }
    if (!getColumnKeys().equals(kv2D.getColumnKeys())) {
      return false;
    }
    int rowCount = getRowCount();
    int colCount = getColumnCount();
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c++) {
        final Number v1 = getValue(r, c);
        final Number v2 = kv2D.getValue(r, c);
        if (v1 == null) {
          if (v2 != null) {
            return false;
          }
        }
        else {
          if (!v1.equals(v2)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * @return hash code of the instance
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int hashCode() {
    int result;
    result = m_rowKeys.hashCode();
    result = 29 * result + m_columnKeys.hashCode();
    result = 31 * result + m_rows.hashCode();
    return result;
  }

  /**
   * @return clone of the current instance
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }
}
