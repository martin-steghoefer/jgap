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

import org.jgap.MutationRateCalculator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for MutationOperator class
 * @author Klaus Meffert
 * @since 1.1
 */
public class MutationOperatorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public MutationOperatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(MutationOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    MutationOperator mutOp = new MutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_1() {
    MutationOperator mutOp = new MutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertNotNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_2() {
    MutationOperator mutOp = new MutationOperator(null);
    assertEquals(0, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_3() {
    MutationRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  public void testOperate_0() {
    /**@todo implement.
     * E.g. we could check if something has changed. For that use a
     * RandomGeneratorForTest*/
  }
}
