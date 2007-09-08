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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the DefaultCrossoverRateCalculator class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DefaultCrossoverRateCalculatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.12 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultCrossoverRateCalculatorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    // reset the configurational parameters set
    Configuration.reset();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testCalculateCurrentRate_0()
      throws Exception {
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    Gene gene = new IntegerGene(conf, 1, 5);
    Chromosome chrom = new Chromosome(conf, gene, 50);
    conf.setSampleChromosome(chrom);
    int rate = calc.calculateCurrentRate();
    assertEquals(conf.getChromosomeSize(), rate);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testCalculateCurrentRate_01()
      throws Exception {
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    Gene gene = new IntegerGene(conf, 1, 5);
    Chromosome chrom = new Chromosome(conf, gene, 30);
    conf.setSampleChromosome(chrom);
    int rate = calc.calculateCurrentRate();
    assertEquals(conf.getChromosomeSize(), rate);
  }

  /**
   * If there are zero chromosomes in the config., the crossover rate
   * nevertheless should be 1, because Random needs positive integers as input
   * (see CrossoverOperator.operate for calling Random class)
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testCalculateCurrentRate_1()
      throws Exception {
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    int rate = calc.calculateCurrentRate();
    assertEquals(1, rate);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testToBePermutated_0()
      throws Exception {
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    assertTrue(calc.toBePermutated(null, 0));
  }
}
