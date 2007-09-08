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

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the CultureMemoryCell class.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class CultureMemoryCellTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.11 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CultureMemoryCellTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_0() {
    CultureMemoryCell cell = new CultureMemoryCell("aName", 77);
    assertEquals("[Name:aName;Value:null;Version:0;Read accessed:0;"
                 + "History Size:77;History:[]",
                 cell.toString());
    cell.setDouble(45.9d);
    String result = cell.toString();
    assertEquals(
        "[Name:aName;Value:45.9;Version:1;Read accessed:0;History Size:77;"
        + "History:[[Name:aName;Value:45.9;Version:0;Read accessed:0;"
        + "History Size:77;History:[]]]", result);
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_1() {
    CultureMemoryCell cell = new CultureMemoryCell("aName", 77);
    assertEquals("[Name:aName;Value:null;Version:0;Read accessed:0;"
                 + "History Size:77;History:[]",
                 cell.toString());
    cell.setDouble(17.3d);
    cell.setDouble( -45.9d);
    String result = cell.toString();
    assertEquals(
        "[Name:aName;Value:-45.9;Version:2;Read accessed:0;History Size:77;"
        + "History:[[Name:aName;Value:17.3;Version:0;Read accessed:0;"
        + "History Size:77;History:[]];[Name:aName;Value:-45.9;Version:1;"
        + "Read accessed:0;History Size:77;History:[]]]", result);
  }

  /**
   * Empty cell.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_2() {
    CultureMemoryCell cell = new CultureMemoryCell();
    assertEquals("[Name:null;Value:null;Version:0;Read accessed:0;"
                 + "History Size:3;History:[]",
                 cell.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testSetDouble_0() {
    CultureMemoryCell cell = new CultureMemoryCell("ANAME", 55);
    cell.setDouble(17.3d);
    assertEquals(17.3d, cell.getCurrentValueAsDouble(), DELTA);
    cell.setDouble(1.8d);
    assertEquals(1.8d, ( (Double) cell.getCurrentValue()).doubleValue(), DELTA);
  }

  /*
   * Tests main functionality of CultureMemoryCell.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testSetValue_0() {
    CultureMemoryCell cell = new CultureMemoryCell("aname", 3);
    assertEquals(0, cell.getVersion());
    cell.setValue(new Integer(15));
    assertEquals(1, cell.getVersion());
    assertEquals(15, ( (Integer) cell.getCurrentValue()).intValue());
    assertEquals(1, cell.getReadAccessed());
    cell.setValue(new Integer(29));
    assertEquals(2, cell.getVersion());
    assertEquals(29, ( (Integer) cell.getCurrentValue()).intValue());
    assertEquals(2, cell.getReadAccessed());
    List history = cell.getHistory();
    assertEquals(2, history.size());
    CultureMemoryCell c1 = (CultureMemoryCell) history.get(0);
    assertEquals(15, ( (Integer) c1.getCurrentValue()).intValue());
    c1 = (CultureMemoryCell) history.get(1);
    assertEquals(29, ( (Integer) c1.getCurrentValue()).intValue());
    cell.setValue(new Integer(42));
    history = cell.getHistory();
    assertEquals(3, history.size());
    c1 = (CultureMemoryCell) history.get(0);
    assertEquals(15, ( (Integer) c1.getCurrentValue()).intValue());
    c1 = (CultureMemoryCell) history.get(1);
    assertEquals(29, ( (Integer) c1.getCurrentValue()).intValue());
    c1 = (CultureMemoryCell) history.get(2);
    assertEquals(42, ( (Integer) c1.getCurrentValue()).intValue());
    cell.setValue(new Integer(33));
    assertEquals(3, history.size());
    c1 = (CultureMemoryCell) history.get(0);
    assertEquals(29, ( (Integer) c1.getCurrentValue()).intValue());
    c1 = (CultureMemoryCell) history.get(1);
    assertEquals(42, ( (Integer) c1.getCurrentValue()).intValue());
    c1 = (CultureMemoryCell) history.get(2);
    assertEquals(33, ( (Integer) c1.getCurrentValue()).intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    CultureMemoryCell cell = new CultureMemoryCell("ANAME", 55);
    cell.setDouble(17.3d);
    CultureMemoryCell cell2 = (CultureMemoryCell) doSerialize(cell);
    assertEquals(cell, cell2);
  }

}
