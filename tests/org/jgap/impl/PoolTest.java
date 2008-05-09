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
 * Tests the Pool class.
 *
 * @since 1.1
 * @author Klaus Meffert
 */
public class PoolTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(PoolTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    new Pool();
  }

  /**
   * @author Klaus Meffert
   */
  public void testClear_0() {
    Pool pool = new Pool();
    assertEquals(0, pool.size());
    pool.clear();
    assertEquals(0, pool.size());
    pool.releaseObject(new Object());
    assertEquals(1, pool.size());
    pool.releaseObject(new Object());
    assertEquals(2, pool.size());
    pool.clear();
    assertEquals(0, pool.size());
  }

  /**
   * @author Klaus Meffert
   */
  public void testAcquirePooledObject_0() {
    Pool pool = new Pool();
    assertEquals(null, pool.acquirePooledObject());
    Vector obj = new Vector();
    pool.releaseObject(obj);
    assertEquals(1, pool.size());
    Object obj2 = pool.acquirePooledObject();
    assertEquals(obj, obj2);
    assertEquals(0, pool.size());
  }

  /**
   * @author Klaus Meffert
   */
  public void testReleaseAllObjects_0() {
    Pool pool = new Pool();
    pool.releaseAllObjects(null);
    assertEquals(0, pool.size());
  }

  /**
   * @author Klaus Meffert
   */
  public void testReleaseAllObjects_1() {
    Pool pool = new Pool();
    Collection coll = new Vector();
    coll.add(new HashMap());
    coll.add(new Vector());
    pool.releaseAllObjects(coll);
    assertEquals(2, pool.size());
    coll.add(new Object());
    pool.releaseAllObjects(coll);
    assertEquals(5, pool.size());
  }

  /**
   * @author Klaus Meffert
   */
  public void testSize_0() {
    Pool pool = new Pool();
    assertEquals(0, pool.size());
  }
}
