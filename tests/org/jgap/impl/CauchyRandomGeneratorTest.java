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

import org.jgap.Configuration;
import org.jgap.RandomGenerator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for CauchyRandomGenerator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGeneratorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001d;

  public CauchyRandomGeneratorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CauchyRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
   */
  public void testGeneral() {
    Configuration conf = new DefaultConfiguration();
    RandomGenerator calc = new CauchyRandomGenerator();
    calc.nextInt();
    calc.nextBoolean();
    calc.nextDouble();
    calc.nextFloat();
    calc.nextInt();
    calc.nextLong();
  }

  public void testNextCauchy_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    final double stdDeriv = 0.04d;
    CauchyRandomGenerator calc = new CauchyRandomGenerator(0.0d, stdDeriv);
    calc.nextCauchy();
  }
}
