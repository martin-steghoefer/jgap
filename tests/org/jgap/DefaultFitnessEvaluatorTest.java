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

package org.jgap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test cases for class DefaultFitnessEvaluator
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public final class DefaultFitnessEvaluatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";
  public DefaultFitnessEvaluatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultFitnessEvaluatorTest.class);
    return suite;
  }

  public void testIsFitter_0() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(true, evaluator.isFitter(1, 0));
    assertEquals(false, evaluator.isFitter(0, 1));
  }

  public void testIsFitter_1() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(true, evaluator.isFitter(12, 11));
    assertEquals(false, evaluator.isFitter(11, 12));
  }

  public void testIsFitter_2() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -1, 1));
    assertEquals(true, evaluator.isFitter(1, -1));
  }

  public void testIsFitter_3() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(false, evaluator.isFitter(0, 0));
  }

  public void testIsFitter_4() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -4, -4));
  }

  public void testIsFitter_5() {
    FitnessEvaluator evaluator = new DefaultFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -3, -1));
    assertEquals(true, evaluator.isFitter( -1, -3));
  }
}
