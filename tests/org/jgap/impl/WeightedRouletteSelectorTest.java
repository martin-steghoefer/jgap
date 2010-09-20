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

import junit.framework.*;

/**
 * Tests the WeightedRouletteSelector class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class WeightedRouletteSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.34 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(WeightedRouletteSelectorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * Test if construction possible without failure.
   *
   * @throws Exception
   */
  public void testConstruct_0() throws Exception{
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setStaticConfiguration(conf);
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
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Configuration conf = new DefaultConfiguration();
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_0()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome bestChrom = new Chromosome(conf, gene, 7);
    bestChrom.setFitnessValue(10);
    selector.add(bestChrom);
    Population p = new Population(conf);
    selector.select(1, null, p);
    assertSame(bestChrom, p.getChromosome(0));
    assertEquals(1, p.size());
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
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting randgen = new RandomGeneratorForTesting();
    randgen.setNextDouble(0.9999d);
    conf.setRandomGenerator(randgen);
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 4);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene(conf);
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 2);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population popNew = new Population(conf);
    selector.select(1, null, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(thirdBestChrom, bestChroms[0]);
    assertSame(thirdBestChrom, bestChroms[0]);
    // now select top 4 chromosomes
    // ----------------------------
    popNew.getChromosomes().clear();
    selector.select(4, null, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(4, bestChroms.length);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting randgen = new RandomGeneratorForTesting();
    randgen.setNextDouble(0.9999d);
    conf.setRandomGenerator(randgen);
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Population toAddFrom = new Population(conf);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 4);
    thirdBestChrom.setFitnessValue(10);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene(conf);
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    toAddFrom.addChromosome(bestChrom);
    // add third chromosome
    // --------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 2);
    secondBestChrom.setFitnessValue(11);
    toAddFrom.addChromosome(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population popNew = new Population(conf);
    selector.select(1, toAddFrom, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(thirdBestChrom, bestChroms[0]);
    // now select top 4 chromosomes
    // ----------------------------
    popNew.getChromosomes().clear();
    selector.select(4, toAddFrom, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(4, bestChroms.length);
  }

  /**
   * Ensure the scaling of fitness value works without error (like division
   * by zero).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setStaticConfiguration(conf);
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    RandomGeneratorForTesting randgen = new RandomGeneratorForTesting();
    randgen.setNextDouble(0.0d);
    conf.setRandomGenerator(randgen);
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Population toAddFrom = new Population(conf);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 4);
    thirdBestChrom.setFitnessValue(0);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene(conf);
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(1.0d);
    toAddFrom.addChromosome(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 2);
    secondBestChrom.setFitnessValue( -1.0d);
    toAddFrom.addChromosome(secondBestChrom);
    // receive top 3 chromosomes, no error should occur
    // ------------------------------------------------
    Population popNew = new Population(conf);
    selector.select(3, toAddFrom, popNew);
  }

  /**
   * Use DeltaFitnessEvaluator.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSelect_4()
      throws Exception {
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    RandomGeneratorForTesting randgen = new RandomGeneratorForTesting();
    randgen.setNextDouble(0.3333333333333d);
    conf.setRandomGenerator(randgen);
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Population toAddFrom = new Population(conf);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 4);
    thirdBestChrom.setFitnessValue(1);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene(conf);
    gene.setAllele(new Double(2.3d));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 3);
    secondBestChrom.setFitnessValue(2);
    toAddFrom.addChromosome(secondBestChrom);
    // add third chromosome
    // --------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome bestChrom = new Chromosome(conf, gene, 2);
    bestChrom.setFitnessValue(3);
    toAddFrom.addChromosome(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population popNew = new Population(conf);
    selector.select(1, toAddFrom, popNew);
    IChromosome[] bestChroms = popNew.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    // now select top 4 chromosomes
    // ----------------------------
    popNew.getChromosomes().clear();
    selector.select(4, toAddFrom, popNew);
    bestChroms = popNew.toChromosomes();
    assertEquals(4, bestChroms.length);
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
    Configuration conf = new DefaultConfiguration();
    Genotype.setStaticConfiguration(conf);
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    conf.setPopulationSize(7);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    conf.setSampleChromosome(chrom);
    selector.add(chrom);
    selector.empty();
    Map chromosomes = (Map) privateAccessor.getField(selector, "m_wheel");
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if clear()-method does not affect original Population.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEmpty_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    chrom.setFitnessValue(3);
    Population pop = new Population(conf, 1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population(conf);
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
  }

  /**
   * Test if clear()-method does not affect return value.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEmpty_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    chrom.setFitnessValue(7);
    Population pop = new Population(conf, 1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population(conf);
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testReturnsUniqueChromosomes_0() throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setStaticConfiguration(conf);
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    assertFalse(selector.returnsUniqueChromosomes());
  }

  /**
   * Ensures WRS is implementing Serializable.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testIsSerializable_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setStaticConfiguration(conf);
    WeightedRouletteSelector selector = new WeightedRouletteSelector();
    assertTrue(isSerializable(selector));
  }

  /**
   * Ensures that WRS implements Serializable
   * correctly.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testDoSerialize_0()
      throws Exception {
    WeightedRouletteSelector selector = new WeightedRouletteSelector(conf);
    Object o = doSerialize(selector);
    assertEquals(o, selector);
  }

  /**@todo add test*/
//  public void test_WeightedSelection_0() {
//    WeightedRouletteSelector ws = new WeightedRouletteSelector();
//    // set a population of size 2
//    int numCrossovers = 100;
//    int crossoversSoFar = 0;
//    while (crossoversSoFar < numCrossovers) {
//      ws.select(2, a_population, parents);
//      Chromosome mother = parents.getChromosome(0);
//      Chromosome father = parents.getChromosome(1);
//      parents.removeChromosome(1);
//      parents.removeChromosome(0);
//      if (father.equals(mother)) {
//        continue;
//      }
//      // I had some crossover function here yet you don't need it for the test case.
//      crossoversSoFar++;
//    }
//  }
}
