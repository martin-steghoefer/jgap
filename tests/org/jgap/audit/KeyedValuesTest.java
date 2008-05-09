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
 * Tests for KeyedValues class
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class KeyedValuesTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public void setUp() {
    super.setUp();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(KeyedValuesTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    assertEquals(0, kv.size());
    assertTrue(kv.hashCode() < 0);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    Comparable key2 = new Double(2.4d);
    Number value2 = new Double(4.6d);
    kv.setValue(key2, value2);
    assertSame(key, kv.getKey(0));
    assertSame(value, kv.getValue(0));
    assertSame(key2, kv.getKey(1));
    assertSame(value2, kv.getValue(1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValue_1()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    value = new Double(23.11d);
    kv.setValue(key, value);
    assertSame(value, kv.getValue(key));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetKeys_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    List keys = kv.getKeys();
    assertEquals(1, keys.size());
    assertSame(key, keys.get(0));
    key = new Double(22.3d);
    value = new Double(42.5d);
    kv.setValue(key, value);
    keys = kv.getKeys();
    assertEquals(2, keys.size());
    assertSame(key, keys.get(1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetValue_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    Comparable key2 = new Double(22.3d);
    Number value2 = new Double(42.5d);
    kv.setValue(key2, value2);
    assertSame(value, kv.getValue(key));
    assertSame(value2, kv.getValue(key2));
    Comparable key3 = new Double( -94.3d);
    assertNull(kv.getValue(key3));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetKey_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    try {
      kv.getKey(0);
      fail();
    } catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetKey_1()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Number value = new Double(2.3d);
    kv.setValue(null, value);
    assertNull(kv.getKey(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetKey_2()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    kv.setValue(null, null);
    assertNull(kv.getKey(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetValue_1()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    try {
      kv.getValue(0);
      fail();
    } catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetValue_2()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    kv.setValue(key, null);
    assertNull(kv.getValue(key));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetValue_3()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    assertNull(kv.getValue(key));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testClone_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    Comparable key2 = new Double(2.4d);
    Number value2 = new Double(4.6d);
    kv.setValue(key2, value2);
    KeyedValues clone = (KeyedValues) kv.clone();
    assertEquals(clone, kv);
    assertSame(key, clone.getKey(0));
    assertSame(value, clone.getValue(0));
    assertSame(key2, clone.getKey(1));
    assertSame(value2, clone.getValue(1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    assertFalse(kv.equals(null));
    assertTrue(kv.equals(kv));
    assertFalse(kv.equals(new Vector()));
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_1()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    KeyedValues kv2 = new KeyedValues();
    assertFalse(kv.equals(kv2));
    assertFalse(kv2.equals(kv));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testHashcode_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    Object data = privateAccessor.getField(kv, "m_data");
    assertEquals(data.hashCode(), kv.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetIndex_0()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = null;
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    assertEquals(0, kv.getIndex(null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetIndex_1()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Comparable key = new Double(23.11d);
    Number value = new Double(4.5d);
    kv.setValue(key, value);
    assertEquals( -1, kv.getIndex(null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetIndex_2()
      throws Exception {
    KeyedValues kv = new KeyedValues();
    Number value = new Double(4.5d);
    kv.setValue(null, value);
    assertEquals( -1, kv.getIndex(new Double(23.11d)));
  }
}
