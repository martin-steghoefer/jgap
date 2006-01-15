/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the WeightedRouletteSelector class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class WeightedRouletteSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.22 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(WeightedRouletteSelectorTest.class);
    return suite;
  }

  /**
   * Test if construction possible without failure
   */
  public void testConstruct_0() {
    new WeightedRouletteSelector();
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAdd_0()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    Genotype.setConfiguration(conf);
    selector.add(chrom);
    Map chromosomes = (Map) privateAccessor.getField(selector,
        "m_wheel");
    assertEquals(1, chromosomes.size());
    Iterator it = chromosomes.keySet().iterator();
    assertEquals(chrom, it.next());
    selector.add(chrom);
    assertEquals(1, chromosomes.size());
    it = chromosomes.keySet().iterator();
    assertEquals(chrom, it.next());
  }

  /**
   * Provoke NullPointerException
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_0()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    try {
      selector.select(1, null, new Population());
      fail();
    }
    catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_1()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    selector.setDoubletteChromosomesAllowed(false);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 4);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene();
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 2);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest randgen = new RandomGeneratorForTest();
    randgen.setNextDouble(0.9999d);
    conf.setRandomGenerator(randgen);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(thirdBestChrom, bestChroms[0]);
    // now select top 4 chromosomes (should only select 3!)
    // ----------------------------------------------------
    popNew.getChromosomes().clear();
    selector.select(4, null, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(3, bestChroms.length);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_2()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    selector.setDoubletteChromosomesAllowed(false);
    Population toAddFrom = new Population();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 4);
    thirdBestChrom.setFitnessValue(10);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene();
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    toAddFrom.addChromosome(bestChrom);
    // add third chromosome
    // --------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 2);
    secondBestChrom.setFitnessValue(11);
    toAddFrom.addChromosome(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest randgen = new RandomGeneratorForTest();
    randgen.setNextDouble(0.9999d);
    conf.setRandomGenerator(randgen);
    Population popNew = new Population();
    selector.select(1, toAddFrom, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(thirdBestChrom, bestChroms[0]);
    // now select top 4 chromosomes (should only select 3!)
    // ----------------------------------------------------
    popNew.getChromosomes().clear();
    selector.select(4, toAddFrom, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(3, bestChroms.length);
  }

  /**
   * Ensure the scaling of fitness value works without error (like division
   * by zero)
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_3()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    selector.setDoubletteChromosomesAllowed(false);
    Population toAddFrom = new Population();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 4);
    thirdBestChrom.setFitnessValue(0);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene();
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(1.0d);
    toAddFrom.addChromosome(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 2);
    secondBestChrom.setFitnessValue( -1.0d);
    toAddFrom.addChromosome(secondBestChrom);
    // receive top 3 chromosomes, no error should occur
    // ------------------------------------------------
    DefaultConfiguration conf = new DefaultConfiguration();
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest randgen = new RandomGeneratorForTest();
    randgen.setNextDouble(0.0d);
    conf.setRandomGenerator(randgen);
    Population popNew = new Population();
    selector.select(3, toAddFrom, popNew);
  }

  /**
   * Use DeltaFitnessEvaluator
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSelect_4()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    selector.setDoubletteChromosomesAllowed(false);
    Population toAddFrom = new Population();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 4);
    thirdBestChrom.setFitnessValue(1);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene();
    gene.setAllele(new Double(2.3d));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(2);
    toAddFrom.addChromosome(secondBestChrom);
    // add third chromosome
    // --------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome bestChrom = new Chromosome(gene, 2);
    bestChrom.setFitnessValue(3);
    toAddFrom.addChromosome(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    DefaultConfiguration conf = new DefaultConfiguration();
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest randgen = new RandomGeneratorForTest();
    randgen.setNextDouble(0.9999d);
    conf.setRandomGenerator(randgen);
    Population popNew = new Population();
    selector.select(1, toAddFrom, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    // now select top 4 chromosomes (should only select 3!)
    // ----------------------------------------------------
    popNew.getChromosomes().clear();
    selector.select(4, toAddFrom, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(3, bestChroms.length);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testEmpty_0()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    conf.setPopulationSize(7);
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    conf.setSampleChromosome(chrom);
    selector.add(chrom);
    selector.empty();
    Map chromosomes = (Map) privateAccessor.getField(selector, "m_wheel");
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if clear()-method does not affect original Population
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEmpty_1()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    chrom.setFitnessValue(3);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
  }

  /**
   * Test if clear()-method does not affect return value
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEmpty_2()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    chrom.setFitnessValue(7);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testReturnsUniqueChromosomes_0() {
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    assertFalse(selector.returnsUniqueChromosomes());
  }
}
