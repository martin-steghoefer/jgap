/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests for ChainOfSelectors class
 *
 * @since 1.1
 * @author Klaus Meffert
 */
public class ChainOfSelectorsTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ChainOfSelectorsTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0() {
    new ChainOfSelectors();
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_0() {
    ChainOfSelectors c = new ChainOfSelectors();
    assertTrue(c.isEmpty());
    c.clear();
    assertTrue(c.isEmpty());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_1()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors();
    assertEquals(0, c.size());
    c.addNaturalSelector(new BestChromosomesSelector());
    assertEquals(1, c.size());
    assertFalse(c.isEmpty());
    c.clear();
    assertTrue(c.isEmpty());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_2()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors();
    Collection l = new Vector();
    l.add(new BestChromosomesSelector());
    l.add(new WeightedRouletteSelector());
    c.addAll(l);
    assertEquals(2, c.size());
    c.clear();
    assertTrue(c.isEmpty());
    assertEquals(0, c.size());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testIterator_0()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors();
    Collection l = new Vector();
    l.add(new BestChromosomesSelector());
    l.add(new WeightedRouletteSelector());
    c.addAll(l);
    Iterator it = c.iterator();
    assertTrue(it.hasNext());
    assertNotNull(it.next());
    assertTrue(it.hasNext());
    assertNotNull(it.next());
    assertFalse(it.hasNext());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddNaturalSelector_0()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors();
    try {
      c.addNaturalSelector(null);
      fail();
    }
    catch (InvalidConfigurationException inex) {
      ; //this is OK
    }
  }

  public void testEquals_0()
      throws Exception {
    ChainOfSelectors c1 = new ChainOfSelectors();
    ChainOfSelectors c2 = new ChainOfSelectors();
    assertFalse(c1.equals(null));
    assertTrue(c1.equals(c2));
    TournamentSelector t1 = new TournamentSelector();
    c1.addNaturalSelector(t1);
    assertFalse(c1.equals(c2));
    c2.addNaturalSelector(new TournamentSelector());
    /**@todo improve: 2 unsame selectors of same class with same params should
     * make the chain equal!
     */
    assertFalse(c1.equals(c2));
    c2.clear();
    c2.addNaturalSelector(t1);
    assertTrue(c1.equals(c2));
  }
}
