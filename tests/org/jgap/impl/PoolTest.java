/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.*;

import junit.framework.*;

/**
 * Tests for Pool class
 *
 * @since 1.1
 * @author Klaus Meffert
 */
public class PoolTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public PoolTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(PoolTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Pool pool = new Pool();
  }

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

  public void testReleaseAllObjects_0() {
    Pool pool = new Pool();
    pool.releaseAllObjects(null);
    assertEquals(0, pool.size());
  }

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

  public void testSize_0() {
    Pool pool = new Pool();
    assertEquals(0, pool.size());
  }
}
