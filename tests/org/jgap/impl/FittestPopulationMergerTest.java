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
import org.jgap.distr.*;
import junit.framework.*;

/**
 * Tests the FittestPopulationMerger class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class FittestPopulationMergerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(FittestPopulationMergerTest.class);
    return suite;
  }

  /**
   * Test if construction possible
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_0() {
    new FittestPopulationMerger();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testMergePopulations_0()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 4);
    chrom.setFitnessValue(5);
    Chromosome[] chroms1 = new Chromosome[3];
    chroms1[0] = chrom;
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(7);
    chroms1[1] = chrom;
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(2);
    chroms1[2] = chrom;
    Chromosome[] chroms2 = new Chromosome[4];
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(4);
    chroms2[0] = chrom;
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(7);
    chroms2[1] = chrom;
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(1);
    chroms2[2] = chrom;
    chrom = new Chromosome(conf, gene, 1);
    chrom.setFitnessValue(10);
    chroms2[3] = chrom;
    Population pop1 = new Population(conf, chroms1);
    Population pop2 = new Population(conf, chroms2);
    IPopulationMerger merger = new FittestPopulationMerger();
    Population result = merger.mergePopulations(pop1, pop2, 4);
    assertEquals(4, result.size());
    assertEquals(10.0d, result.getChromosome(0).getFitnessValue(), DELTA);
    assertEquals(7.0d, result.getChromosome(1).getFitnessValue(), DELTA);
    assertEquals(7.0d, result.getChromosome(2).getFitnessValue(), DELTA);
    assertEquals(5.0d, result.getChromosome(3).getFitnessValue(), DELTA);
  }
}
