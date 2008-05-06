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
import org.jgap.event.*;
import junit.framework.*;

/**
 * Tests the DefaultConfiguration class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultConfigurationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.20 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultConfigurationTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testConstruct_0() {
    assertEquals(EventManager.class, conf.getEventManager().getClass());
    assertEquals(DefaultFitnessEvaluator.class,
                 conf.getFitnessEvaluator().getClass());
    assertEquals(BestChromosomesSelector.class,
                 conf.getNaturalSelectors(false).get(0).getClass());
    assertEquals(StockRandomGenerator.class,
                 conf.getRandomGenerator().getClass());
    assertEquals(ChromosomePool.class, conf.getChromosomePool().getClass());
    assertEquals(2, conf.getGeneticOperators().size());
    // Test if all 2 slots are occupied by the 2 default GeneticOperator's
    int code = 0;
    GeneticOperator op;
    for (int i = 0; i < 2; i++) {
      op = (GeneticOperator) conf.getGeneticOperators().get(i);
      if (op instanceof MutationOperator) {
        code = code ^ 1;
      }
//      else if (op instanceof ReproductionOperator) {
//        code = code ^ 2;
//      }
      else if (op instanceof CrossoverOperator) {
        code = code ^ 4;
      }
    }
    assertEquals(5, code);
  }

  /**
   * Provoke error during initialization.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testConstruct_1() {
    try {
      Configuration conf = new DefaultConfigForTesting();
      fail();
    }
    catch (RuntimeException rex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_2()
      throws Exception {
    DefaultConfiguration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    assertEquals("", conf.getId());
    assertEquals("", conf.getName());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_3()
      throws Exception {
    DefaultConfiguration.reset();
    DefaultConfiguration conf = new DefaultConfiguration("xxX1","3a");
    assertEquals("xxX1", conf.getId());
    assertEquals("3a", conf.getName());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testClone_0()
      throws Exception {
    DefaultConfiguration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    Gene gene = new BooleanGene(conf);
    conf.setSampleChromosome(new Chromosome(conf, gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
    conf.setPopulationSize(1);
    Configuration theClone = (Configuration)conf.clone();
    assertEquals(conf, theClone);
    List genOps = theClone.getGeneticOperators();
    assertEquals(2, genOps.size());
    assertEquals(CrossoverOperator.class,
                 theClone.getGeneticOperators().get(0).getClass());
    assertEquals(MutationOperator.class,
                 theClone.getGeneticOperators().get(1).getClass());
  }

  class DefaultConfigForTesting
      extends DefaultConfiguration {
    public void setRandomGenerator(RandomGenerator a_generatorToSet)
        throws InvalidConfigurationException {
      throw new InvalidConfigurationException("For Test only");
    }
  }
}
