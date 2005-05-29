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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests for Culture class
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class CultureTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  private final static double DELTA = 0.00000001d;

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CultureTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try {
      new Culture(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_1() {
    try {
      new Culture( -3);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() {
    Culture c = new Culture(7);
    assertEquals(7, c.size());
  }

  public void testGet_0() {
    try {
      Culture c = new Culture(35);
      c.set( -1, 2, 5, "");
      fail();
    }
    catch (IllegalArgumentException iex) {
      //this is OK
    }
  }

  public void testGet_1() {
    try {
      Culture c = new Culture(35);
      c.set(35, 2, 5, "");
      fail();
    }
    catch (IllegalArgumentException iex) {
      //this is OK
    }
  }

  public void testGet_2() {
    Culture c = new Culture(9);
    c.set(3, 5.7d, 0, "");
    CultureMemoryCell cell = c.get(3);
    assertEquals(5.7d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(0, cell.getHistorySize());
    assertEquals("", cell.getName());
  }

  public void testGet_3() {
    Culture c = new Culture(9);
    c.set(3, -5.7d, -1, null);
    CultureMemoryCell cell = c.get(3);
    assertEquals( -5.7d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(0, cell.getHistorySize());
    assertEquals(null, cell.getName());
  }

  public void testGet_4() {
    Culture c = new Culture(11);
    c.set(0, 0.0d, 17, "aName");
    CultureMemoryCell cell = c.get(0);
    assertEquals(0.0d, cell.getCurrentValueAsDouble(), DELTA);
    assertEquals(17, cell.getHistorySize());
    assertEquals("aName", cell.getName());
  }

  public void testToString_0() {
    /**@todo implement*/
  }
}
