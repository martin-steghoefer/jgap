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
 * Test class for Culture class
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class CultureTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CultureTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try {
      Culture c = new Culture(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_1() {
    try {
      Culture c = new Culture( -3);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() {
      Culture c = new Culture( 7);
      assertEquals(7,c.size());
  }

  public void testToString_0() {
    /**@todo implement*/
  }
  /**@todo implement tests*/
}
