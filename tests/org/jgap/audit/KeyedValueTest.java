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
 * Tests for KeyedValue class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class KeyedValueTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public void setUp() {
    super.setUp();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(KeyedValueTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testConstruct_0()
      throws Exception {
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(key, value);
    assertSame(key, kv.getKey());
    assertSame(value, kv.getValue());
    value = new Double(8.5d);
    kv.setValue(value);
    assertSame(value, kv.getValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1()
      throws Exception {
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(null, value);
    assertNull(kv.getKey());
    assertSame(value, kv.getValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_2()
      throws Exception {
    KeyedValue kv = new KeyedValue(null, null);
    assertNull(kv.getKey());
    assertNull(kv.getValue());
    assertTrue(kv.hashCode() < 0);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testClone_0()
      throws Exception {
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(key, value);
    KeyedValue clone = (KeyedValue) kv.clone();
    assertEquals(clone, kv);
    assertSame(key, clone.getKey());
    assertSame(value, clone.getValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_0()
      throws Exception {
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(key, value);
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
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(key, value);
    Comparable key2 = new Double(2.3d);
    Number value2 = new Double(14.5d);
    KeyedValue kv2 = new KeyedValue(key2, value2);
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
    Comparable key = new Double(2.3d);
    Number value = new Double(4.5d);
    KeyedValue kv = new KeyedValue(key, value);
    KeyedValue kv2 = new KeyedValue(null, value);
    assertFalse(kv.equals(kv2));
    assertFalse(kv2.equals(kv));
  }

  /**
    * @throws Exception
    * @author Klaus Meffert
    * @since 3.0
    */
   public void testHashcode_0()
       throws Exception {
     Comparable key = new Double(2.3d);
     Number value = new Double(4.5d);
     KeyedValue kv = new KeyedValue(key, value);
     assertEquals(41 * key.hashCode() + value.hashCode(), kv.hashCode());
   }
}
