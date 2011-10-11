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

import org.jgap.impl.salesman.*;
import org.jgap.impl.fitness.*;
import org.jgap.impl.job.*;

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
  private final static String CVS_REVISION = "$Revision: 1.39 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllImplTests");
    suite.addTest(AveragingCrossoverOperatorTest.suite());
    suite.addTest(BestChromosomesSelectorTest.suite());
    suite.addTest(BooleanGeneTest.suite());
    suite.addTest(BulkFitnessOffsetRemoverTest.suite());
    suite.addTest(CauchyRandomGeneratorTest.suite());
    suite.addTest(ChainOfSelectorsTest.suite());
    suite.addTest(ChromosomePoolTest.suite());
    suite.addTest(CompositeGeneTest.suite());
    suite.addTest(CompositeGenePersistentReprTest.suite());
    suite.addTest(CrossoverOperatorTest.suite());
    suite.addTest(DefaultCloneHandlerTest.suite());
    suite.addTest(DefaultCompareToHandlerTest.suite());
    suite.addTest(DefaultConfigurationTest.suite());
    suite.addTest(DefaultCrossoverRateCalculatorTest.suite());
    suite.addTest(DefaultInitializerTest.suite());
    suite.addTest(DefaultMutationRateCalculatorTest.suite());
    suite.addTest(DoubleGeneTest.suite());
    suite.addTest(FixedBinaryGeneTest.suite());
    suite.addTest(FittestPopulationMergerTest.suite());
    suite.addTest(GaussianMutationOperatorTest.suite());
    suite.addTest(GaussianRandomGeneratorTest.suite());
    suite.addTest(GreedyCrossoverTest.suite());
    suite.addTest(IntegerGeneTest.suite());
    suite.addTest(InversionOperatorTest.suite());
    suite.addTest(JGAPFactoryTest.suite());
    suite.addTest(MapGeneTest.suite());
    suite.addTest(MutationOperatorTest.suite());
    suite.addTest(SwappingMutationOperatorTest.suite());
    suite.addTest(NumberGeneTest.suite());
    suite.addTest(PoolTest.suite());
    suite.addTest(SeededRandomGeneratorTest.suite());
    suite.addTest(SetGeneTest.suite());
    suite.addTest(StockRandomGeneratorTest.suite());
    suite.addTest(StringGeneTest.suite());
    suite.addTest(ThresholdSelectorTest.suite());
    suite.addTest(TournamentSelectorTest.suite());
    suite.addTest(TwoWayMutationOperatorTest.suite());
    suite.addTest(WeightedRouletteSelectorTest.suite());

    suite.addTest(AllFitnessTests.suite());

    suite.addTest(AllSalesmanTests.suite());

    suite.addTest(AllJobTests.suite());
    return suite;
  }
}
