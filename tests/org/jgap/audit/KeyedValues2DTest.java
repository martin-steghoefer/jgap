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

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests for KeyedValues2D class
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class KeyedValues2DTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public void setUp() {
    super.setUp();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(KeyedValues2DTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerializable_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = new Double(4.5d);
    Comparable key1 = new Double(0.3d);
    Comparable key2 = new Double(-3.5d);
    kv.setValue(value, key1, key2);
    super.isSerializable(kv);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    assertEquals(0, kv.getRowCount());
    assertEquals(0, kv.getColumnCount());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D(false);
    Comparable row = new Integer(8);
    Comparable col = new Integer(4);
    Number value = new Double(4.5d);
    kv.setValue(value, row, col);
    Comparable row2 = new Integer(11);
    Comparable col2 = new Integer(1);
    Number value2 = new Double(9.0d);
    kv.setValue(value2, row2, col2);
    assertSame(row, kv.getRowKey(0));
    assertSame(value, kv.getValue(row, col));
    assertSame(row2, kv.getRowKey(1));
    assertSame(value2, kv.getValue(row2, col2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_02()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D(true);
    Comparable row = new Integer(8);
    Comparable col = new Integer(4);
    Number value = new Double(4.5d);
    kv.setValue(value, row, col);
    Comparable row2 = new Integer(11);
    Comparable col2 = new Integer(1);
    Number value2 = new Double(9.0d);
    kv.setValue(value2, row2, col2);
    assertSame(row, kv.getRowKey(0));
    assertSame(value, kv.getValue(row, col));
    assertSame(row2, kv.getRowKey(1));
    assertSame(value2, kv.getValue(row2, col2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_1()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Comparable col = new Integer(4);
    Number value = new Double(4.5d);
    kv.setValue(value, null, col);
    assertEquals(value, kv.getValue(0, 0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_2()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Comparable row = new Integer(4);
    Number value = new Double(4.5d);
    kv.setValue(value, row, null);
    assertEquals(null, kv.getValue(0, 0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_3()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Comparable row = new Integer(4);
    Number value = new Double(4.5d);
    kv.setValue(value, row, null);
    value = new Double(11.8d);
    kv.setValue(value, row, null);
    assertEquals(null, kv.getValue(0, 0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetValue_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Comparable col = new Integer(4);
    Number value = new Double(4.5d);
    assertEquals(null, kv.getValue(col, col));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testClone_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = new Double(4.5d);
    Comparable rowkey = new Double(2.3d);
    Comparable colkey = new Double(-7.82d);
    kv.setValue(value, rowkey, colkey);
    KeyedValues2D clone = (KeyedValues2D) kv.clone();
    assertEquals(clone, kv);
    assertSame(kv.getRowKey(0), clone.getRowKey(0));
    assertSame(kv.getColumnKey(0), clone.getColumnKey(0));
    assertSame(kv.getValue(0,0), clone.getValue(0,0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_0()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    assertFalse(kv.equals(null));
    assertTrue(kv.equals(kv));
    assertFalse(kv.equals(new Vector()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_1()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = new Double(4.5d);
    Comparable rowkey = new Double(12.3d);
    Comparable colkey = new Double(92.3d);
    kv.setValue(value, rowkey, colkey);
    KeyedValues2D kv2 = new KeyedValues2D();
    assertFalse(kv.equals(kv2));
    assertFalse(kv2.equals(kv));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_2()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = new Double(4.5d);
    Comparable rowkey = new Double(12.3d);
    Comparable colkey = new Double(92.3d);
    kv.setValue(value, rowkey, colkey);
    KeyedValues2D kv2 = new KeyedValues2D();
    Comparable colkey2 = new Double(17.9d);
    kv2.setValue(value, rowkey, colkey2);
    assertFalse(kv.equals(kv2));
    assertFalse(kv2.equals(kv));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_3()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = new Double(4.5d);
    Comparable rowkey = new Double(12.3d);
    Comparable colkey = new Double(92.3d);
    kv.setValue(value, rowkey, colkey);
    KeyedValues2D kv2 = new KeyedValues2D();
    Number value2 = null;
    kv2.setValue(value2, rowkey, colkey);
    assertFalse(kv.equals(kv2));
    assertFalse(kv2.equals(kv));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_4()
      throws Exception {
    KeyedValues2D kv = new KeyedValues2D();
    Number value = null;
    Comparable rowkey = new Double(12.3d);
    Comparable colkey = new Double(92.3d);
    kv.setValue(value, rowkey, colkey);
    KeyedValues2D kv2 = new KeyedValues2D();
    kv2.setValue(value, rowkey, colkey);
    assertTrue(kv.equals(kv2));
  }

}
