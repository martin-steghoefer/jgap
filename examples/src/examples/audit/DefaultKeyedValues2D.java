package examples.audit;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jfree.data.DefaultKeyedValues;/**@todo do it ourselves*/
import org.jfree.data.KeyedValues2D;/**@todo do it ourselves*/

public class DefaultKeyedValues2D implements Cloneable, Serializable{

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  /** The row keys. */
  private List rowKeys;

  /** The column keys. */
  private List columnKeys;

  /** The row data. */
  private List rows;

  /** If the row keys should be sorted by their comparable order. */
  private boolean sortRowKeys;

  /**
   * Creates a new instance (initially empty).
   */
  public DefaultKeyedValues2D() {
      this(false);
  }

  /**
   * Creates a new instance (initially empty).
   * @param sortRowKeys if the row keys should be sorted.
   */
  public DefaultKeyedValues2D(final boolean a_sortRowKeys) {
      rowKeys = new java.util.ArrayList();
      columnKeys = new java.util.ArrayList();
      rows = new java.util.ArrayList();
      sortRowKeys = a_sortRowKeys;
  }

  /**
   * Returns the row count.
   *
   * @return the row count.
   */
  public int getRowCount() {
      return rowKeys.size();
  }

  /**
   * Returns the column count.
   *
   * @return the column count.
   */
  public int getColumnCount() {
      return columnKeys.size();
  }

  /**
   * Returns the value for a given row and column.
   *
   * @param row  the row index.
   * @param column  the column index.
   *
   * @return the value.
   */
  public Number getValue(final int row, final int column) {

      Number result = null;
      final DefaultKeyedValues rowData = (DefaultKeyedValues) rows.get(row);
      if (rowData != null) {
          final Comparable columnKey = (Comparable) columnKeys.get(column);
          if (columnKey != null) {
              result = rowData.getValue(columnKey);
          }
      }
      return result;

  }

  /**
   * Returns the key for a given row.
   *
   * @param row  the row index (zero based).
   *
   * @return the row index.
   */
  public Comparable getRowKey(final int row) {
      return (Comparable) rowKeys.get(row);
  }

  /**
   * Returns the row index for a given key.
   *
   * @param key  the key.
   *
   * @return the row index.
   */
  public int getRowIndex(final Comparable key) {
      if (sortRowKeys) {
          return Collections.binarySearch(rowKeys, key);
      }
      else {
          return rowKeys.indexOf(key);
      }
  }

  /**
   * Returns the row keys.
   *
   * @return the row keys.
   */
  public List getRowKeys() {
      return Collections.unmodifiableList(rowKeys);
  }

  /**
   * Returns the key for a given column.
   *
   * @param column  the column.
   *
   * @return the key.
   */
  public Comparable getColumnKey(final int column) {
      return (Comparable) columnKeys.get(column);
  }

  /**
   * Returns the column index for a given key.
   *
   * @param key  the key.
   *
   * @return the column index.
   */
  public int getColumnIndex(final Comparable key) {
      return columnKeys.indexOf(key);
  }

  /**
   * Returns the column keys.
   *
   * @return the column keys.
   */
  public List getColumnKeys() {
      return Collections.unmodifiableList(columnKeys);
  }

  /**
   * Returns the value for the given row and column keys.
   *
   * @param rowKey  the row key.
   * @param columnKey  the column key.
   *
   * @return the value.
   */
  public Number getValue(final Comparable rowKey, final Comparable columnKey) {

      Number result = null;
      final int row = getRowIndex(rowKey);

      if (row >= 0) {
          final DefaultKeyedValues rowData = (DefaultKeyedValues) rows.get(row);
          result = rowData.getValue(columnKey);
      }
      return result;

  }

  /**
   * Adds a value to the table.  Performs the same function as setValue(...).
   *
   * @param value  the value.
   * @param rowKey  the row key.
   * @param columnKey  the column key.
   */
  public void addValue(Number value, Comparable rowKey, Comparable columnKey) {
      setValue(value, rowKey, columnKey);
  }

  /**
   * Adds or updates a value.
   *
   * @param value  the value.
   * @param rowKey  the row key.
   * @param columnKey  the column key.
   */
  public void setValue(Number value, Comparable rowKey, Comparable columnKey) {

      final DefaultKeyedValues row;
      int rowIndex = getRowIndex(rowKey);

      if (rowIndex >= 0) {
          row = (DefaultKeyedValues) rows.get(rowIndex);
      }
      else {
          row = new DefaultKeyedValues();
          if (sortRowKeys) {
              rowIndex = -rowIndex - 1;
              rowKeys.add(rowIndex, rowKey);
              rows.add(rowIndex, row);
          }
          else {
              rowKeys.add(rowKey);
              rows.add(row);
          }
      }
      row.setValue(columnKey, value);

      final int columnIndex = columnKeys.indexOf(columnKey);
      if (columnIndex < 0) {
          columnKeys.add(columnKey);
      }
  }

  /**
   * Removes a value.
   *
   * @param rowKey  the row key.
   * @param columnKey  the column key.
   */
  public void removeValue(final Comparable rowKey, final Comparable columnKey) {
      setValue(null, rowKey, columnKey);

      // 1. check wether the row is now empty.
      boolean allNull = true;
      final int rowIndex = getRowIndex(rowKey);
      DefaultKeyedValues row = (DefaultKeyedValues) rows.get(rowIndex);

      for (int item = 0, itemCount = row.getItemCount(); item < itemCount; item++) {
          if (row.getValue(item) != null) {
              allNull = false;
              break;
          }
      }

      if (allNull) {
          rowKeys.remove(rowIndex);
          rows.remove(rowIndex);
      }

      // 2. check wether the column is now empty.
      allNull = true;
      final int columnIndex = getColumnIndex(columnKey);

      for (int item = 0, itemCount = rows.size(); item < itemCount; item++) {
          row = (DefaultKeyedValues) rows.get(item);
          if (row.getValue(columnIndex) != null) {
              allNull = false;
              break;
          }
      }

      if (allNull) {
          for (int item = 0, itemCount = rows.size(); item < itemCount; item++) {
              row = (DefaultKeyedValues) rows.get(item);
              row.removeValue(columnIndex);
          }
          columnKeys.remove(columnIndex);
      }
  }

  /**
   * Removes a row.
   *
   * @param rowIndex  the row index.
   */
  public void removeRow(final int rowIndex) {
      rowKeys.remove(rowIndex);
      rows.remove(rowIndex);
  }

  /**
   * Removes a row.
   *
   * @param rowKey  the row key.
   */
  public void removeRow(final Comparable rowKey) {
      removeRow(getRowIndex(rowKey));
  }

  /**
   * Removes a column.
   *
   * @param columnIndex  the column index.
   */
  public void removeColumn(final int columnIndex) {
      final Comparable columnKey = getColumnKey(columnIndex);
      removeColumn(columnKey);
  }

  /**
   * Removes a column.
   *
   * @param columnKey  the column key.
   */
  public void removeColumn(final Comparable columnKey) {
      final Iterator iterator = rows.iterator();
      while (iterator.hasNext()) {
          final DefaultKeyedValues rowData = (DefaultKeyedValues) iterator.next();
          rowData.removeValue(columnKey);
      }
      columnKeys.remove(columnKey);
  }

  /**
   * Clears all the data and associated keys.
   */
  public void clear() {
      rowKeys.clear();
      columnKeys.clear();
      rows.clear();
  }

  /**
   * Tests if this object is equal to another.
   *
   * @param o  the other object.
   *
   * @return A boolean.
   */
  public boolean equals(final Object o) {

      if (o == null) {
          return false;
      }
      if (o == this) {
          return true;
      }

      if (!(o instanceof KeyedValues2D)) {
          return false;
      }
      final KeyedValues2D kv2D = (KeyedValues2D) o;
      if (!getRowKeys().equals(kv2D.getRowKeys())) {
          return false;
      }
      if (!getColumnKeys().equals(kv2D.getColumnKeys())) {
          return false;
      }
      final int rowCount = getRowCount();
      if (rowCount != kv2D.getRowCount()) {
          return false;
      }

      final int colCount = getColumnCount();
      if (colCount != kv2D.getColumnCount()) {
          return false;
      }

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
   * Returns a hash code.
   *
   * @return a hash code.
   */
  public int hashCode() {
      int result;
      result = rowKeys.hashCode();
      result = 29 * result + columnKeys.hashCode();
      result = 29 * result + rows.hashCode();
      return result;
  }

  /**
   * Returns a clone.
   *
   * @return A clone.
   *
   * @throws CloneNotSupportedException  this class will not throw this exception, but subclasses
   *         (if any) might.
   */
  public Object clone() throws CloneNotSupportedException {
      return super.clone();
  }

}
