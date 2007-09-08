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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the Culture class.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class CultureTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.15 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CultureTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testConstruct_0() {
    try {
      new Culture(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testConstruct_1() {
    try {
      new Culture( -3);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testConstruct_2() {
    Culture c = new Culture(7);
    assertEquals(7, c.size());
  }

  /**
   * Illegal index.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_0() {
    try {
      Culture c = new Culture(35);
      c.set( -1, 2, 5, "");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal index (too high).
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_1() {
    try {
      Culture c = new Culture(35);
      c.set(35, 2, 5, "");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_2() {
    Culture c = new Culture(9);
    CultureMemoryCell cell1 = c.set(3, 5.7d, 0, "");
    CultureMemoryCell cell = c.get(3);
    assertSame(cell1, cell);
    assertEquals(5.7d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(0, cell.getHistorySize());
    assertEquals("", cell.getName());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_3() {
    Culture c = new Culture(9);
    CultureMemoryCell cell1 = c.set(3, -5.7d, -1, null);
    CultureMemoryCell cell = c.get(3);
    assertSame(cell1, cell);
    assertEquals( -5.7d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(0, cell.getHistorySize());
    assertEquals(null, cell.getName());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_4() {
    Culture c = new Culture(11);
    CultureMemoryCell cell1 = c.set(0, 0.0d, 17, "aName");
    CultureMemoryCell cell = c.get(0);
    assertSame(cell1, cell);
    assertEquals(0.0d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(17, cell.getHistorySize());
    assertEquals("aName", cell.getName());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGet_5() {
    Culture c = new Culture(11);
    CultureMemoryCell cell = c.get(7);
    assertEquals(null, cell);
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_0() {
    Culture c = new Culture(11);
    String s = "[";
    for (int i = 0; i < c.size(); i++) {
      if (i > 0) {
        s += ";";
      }
      if (c.get(i) == null) {
        s += "null";
      }
      else {
        s += c.get(i).toString();
      }
    }
    s += "]";
    assertEquals(s, c.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_1() {
    Culture c = new Culture(11);
    c.set(1, 0.0d, 17, "aName");
    c.set(3, 23.5d, 5, "ANAME");
    c.set(4, 19.6d, 0, "aName");
    String s = "[";
    for (int i = 0; i < c.size(); i++) {
      if (i > 0) {
        s += ";";
      }
      if (c.get(i) == null) {
        s += "null";
      }
      else {
        s += c.get(i).toString();
      }
    }
    s += "]";
    assertEquals(s, c.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testSerialize_0()
      throws Exception {
    Culture c = new Culture(11);
    c.set(0, 2.3d, -1, "no name");
    Culture c2 = (Culture) doSerialize(c);
    assertEquals(c, c2);
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testClear_0() {
    Culture c = new Culture(11);
    c.set(1, 2.3d, -1, "no name");
    c.set("hallo", new Double(2.3d), -1);
    c.clear();
    // Check named memory.
    assertEquals(0, c.getMemoryNames().size());
    // Check memory cells themselves.
    for (int i = 0; i < c.size(); i++) {
      assertNull(c.get(i));
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testContains_0() {
    Culture c = new Culture(11);
    c.set("uniquename","value",-1);
    assertTrue(c.contains("uniquename"));
    assertFalse(c.contains("uniqueName"));
    assertFalse(c.contains("UNIQUENAME"));
    assertTrue(c.contains("uniquename"));
    c.clear();
    assertFalse(c.contains("uniquename"));
  }

  /**
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testContains_1() {
    Culture c = new Culture(7);
    assertFalse(c.contains("uniquename"));
    assertFalse(c.contains("*"));
    assertFalse(c.contains("%"));
  }

  /**
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testContains_2() {
    Culture c = new Culture(7);
    try {
      assertFalse(c.contains(""));
      fail();
    }catch (IllegalArgumentException iex) {
      ; // this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testContains_3() {
    Culture c = new Culture(7);
    try {
      assertFalse(c.contains(null));
      fail();
    }catch (IllegalArgumentException iex) {
      ; // this is OK
    }
  }
}
