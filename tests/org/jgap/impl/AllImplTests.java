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

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.impl
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllImplTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.17 $";

  public AllImplTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(AveragingCrossoverOperatorTest.suite());
    suite.addTest(BestChromosomesSelectorTest.suite());
    suite.addTest(BooleanGeneTest.suite());
    suite.addTest(CauchyRandomGeneratorTest.suite());
    suite.addTest(ChainOfSelectorsTest.suite());
    suite.addTest(ChromosomePoolTest.suite());
    suite.addTest(CompositeGeneTest.suite());
    suite.addTest(CrossoverOperatorTest.suite());
    suite.addTest(DefaultConfigurationTest.suite());
    suite.addTest(DefaultCrossoverRateCalculatorTest.suite());
    suite.addTest(DefaultMutationRateCalculatorTest.suite());
    suite.addTest(DoubleGeneTest.suite());
    suite.addTest(FixedBinaryGeneTest.suite());
    suite.addTest(FittestPopulationMergerTest.suite());
    suite.addTest(GaussianMutationOperatorTest.suite());
    suite.addTest(GaussianRandomGeneratorTest.suite());
    suite.addTest(IntegerGeneTest.suite());
    suite.addTest(MutationOperatorTest.suite());
    suite.addTest(NumberGeneTest.suite());
    suite.addTest(PoolTest.suite());
    suite.addTest(ReproductionOperatorTest.suite());
    suite.addTest(StringGeneTest.suite());
    suite.addTest(TournamentSelectorTest.suite());
    suite.addTest(ThresholdSelectorTest.suite());
    suite.addTest(WeightedRouletteSelectorTest.suite());
    return suite;
  }
}
