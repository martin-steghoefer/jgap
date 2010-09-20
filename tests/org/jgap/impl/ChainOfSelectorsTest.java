/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the ChainOfSelectors class.
 *
 * @since 1.1
 * @author Klaus Meffert
 */
public class ChainOfSelectorsTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ChainOfSelectorsTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0() {
    new ChainOfSelectors(conf);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_0() {
    ChainOfSelectors c = new ChainOfSelectors(conf);
    assertTrue(c.isEmpty());
    c.clear();
    assertTrue(c.isEmpty());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_1()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors(conf);
    assertEquals(0, c.size());
    c.addNaturalSelector(new BestChromosomesSelector(conf));
    assertEquals(1, c.size());
    assertFalse(c.isEmpty());
    c.clear();
    assertTrue(c.isEmpty());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClear_2()
      throws Exception {
    ChainOfSelectors c = new ChainOfSelectors(conf);
    Collection l = new Vector();
    l.add(new BestChromosomesSelector(conf));
    l.add(new WeightedRouletteSelector(conf));
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
    ChainOfSelectors c = new ChainOfSelectors(conf);
    Collection l = new Vector();
    l.add(new BestChromosomesSelector(conf));
    l.add(new WeightedRouletteSelector(conf));
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
    ChainOfSelectors c = new ChainOfSelectors(conf);
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
    ChainOfSelectors c1 = new ChainOfSelectors(conf);
    ChainOfSelectors c2 = new ChainOfSelectors(conf);
    assertFalse(c1.equals(null));
    assertTrue(c1.equals(c2));
    TournamentSelector t1 = new TournamentSelector(conf, 3, 0.2d);
    c1.addNaturalSelector(t1);
    assertFalse(c1.equals(c2));
    c2.addNaturalSelector(new TournamentSelector(conf,4,0.1d));
    /**@todo improve: 2 unsame selectors of same class with same params should
     * make the chain equal!
     */
    assertFalse(c1.equals(c2));
    c2.clear();
    c2.addNaturalSelector(t1);
    assertTrue(c1.equals(c2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_1()
      throws Exception {
    ChainOfSelectors c1 = new ChainOfSelectors(conf);
    assertFalse(c1.equals(new BooleanGene(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testHashCode_0()
      throws Exception {
    ChainOfSelectors c1 = new ChainOfSelectors(conf);
    ChainOfSelectors c2 = new ChainOfSelectors(conf);
    assertEquals(c1.hashCode(), c2.hashCode());
    c1.addNaturalSelector(new BestChromosomesSelector(conf));
    assertFalse(c1.hashCode() == c2.hashCode());
    assertEquals(c1.hashCode(), c1.hashCode());
    c2.addNaturalSelector(new BestChromosomesSelector(conf));
    assertFalse(c1.hashCode() == c2.hashCode());
  }
}
