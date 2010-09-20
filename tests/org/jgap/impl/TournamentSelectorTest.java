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
 * Tests the TournamentSelector class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class TournamentSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.16 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(TournamentSelectorTest.class);
    return suite;
  }

  /**
   * Valid construction
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_0()
      throws Exception {
    Configuration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    TournamentSelector sel = new TournamentSelector(conf, 1, 1.0d);
    assertNotNull(privateAccessor.getField(sel, "m_chromosomes"));
    assertNotNull(privateAccessor.getField(sel, "m_fitnessValueComparator"));
    new TournamentSelector(conf, 1, 0.5d);
    new TournamentSelector(conf, 10, 0.00001d);
    new TournamentSelector(conf, 50, 0.4d);
  }

  /**
   * Invalid construction.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_1()
      throws Exception {
    try {
      new TournamentSelector(null, 0, 0.5d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Invalid construction,
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_2()
      throws Exception {
    try {
      new TournamentSelector(null, -1, 0.5d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Invalid construction.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_3()
      throws Exception {
    try {
      new TournamentSelector(null, 4, 0.0d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Invalid construction.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_4()
      throws Exception {
    try {
      new TournamentSelector(null, 4, 1.0001d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testAdd_0()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 5, 0.5d);
    Gene gene = new BooleanGene(conf);
    IChromosome chrom = new Chromosome(conf, gene, 5);
    selector.add(chrom);
    List chromosomes = ( (Vector) privateAccessor.getField(selector,
        "m_chromosomes"));
    assertEquals(1, chromosomes.size());
    assertEquals(chrom, chromosomes.get(0));
    selector.add(chrom);
    assertEquals(chrom, chromosomes.get(0));
    assertEquals(2, chromosomes.size());
    selector.add(chrom);
    assertEquals(3, chromosomes.size());
  }

  public void testEmpty_0()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 4, 0.1d);
    Gene gene = new BooleanGene(conf);
    IChromosome chrom = new Chromosome(conf, gene, 5);
    selector.add(chrom);
    selector.empty();
    List chromosomes = ( (Vector) privateAccessor.getField(selector,
        "m_chromosomes"));
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if clear()-method does not affect original Population.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testEmpty_1()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 4, 0.1d);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    Population pop = new Population(conf, 1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population(conf);
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
    assertNotNull(popNew.getChromosome(0));
  }

  /**
   * Test if clear()-method does not affect return value.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testEmpty_2()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 10, 1.0d);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    Population pop = new Population(conf, 1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population(conf);
    selector.select(1, null, popNew);
    pop = popNew;
    selector.empty();
    assertEquals(1, pop.size());
    assertNotNull(pop.getChromosome(0));
  }

  /**
   * Test if below functionality working without error.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_0()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 4, 0.3d);
    Gene gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    selector.select(1, null, new Population(conf));
  }

  /**
   * Always select best chromosome for granted if prob is 1.0 and index for
   * selected chromosomes in tournament is equal to index of best chromosome.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_1()
      throws Exception {
    // random generator always returning 1 (index of best chromosome below)
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting(1);
    conf.setRandomGenerator(rn);
    TournamentSelector selector = new TournamentSelector(conf, 4, 1.0d);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population(conf);
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    // receive top 3 chromosomes
    // -------------------------
    pop.getChromosomes().clear();
    selector.select(3, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(3, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(bestChrom, bestChroms[1]);
    assertEquals(bestChrom, bestChroms[2]);
  }

  /**
   * Always select best chromosome for granted if prob is 1.0 and index for
   * selected chromosomes in tournament is equal to index of best chromosome.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_2()
      throws Exception {
    // random generator always returning 1 (index of best chromosome below)
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting(1);
    conf.setRandomGenerator(rn);
    TournamentSelector selector = new TournamentSelector(conf, 4, 1.0d);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population(conf);
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    // receive top 30 chromosomes.
    // ---------------------------
    pop.getChromosomes().clear();
    selector.select(30, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(30, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(bestChrom, bestChroms[1]);
    assertTrue(bestChrom == bestChroms[0]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_3()
      throws Exception {
    //Set index of chromosome to be selected by ThresholdSelector to 1.
    //1 because the best chromosome will be index 0 and the other one has
    // index 1.
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting(0);
    rn.setNextDouble(0.0d);
    conf.setRandomGenerator(rn);
    TournamentSelector selector = new TournamentSelector(conf, 2, 1.0d);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population(conf);
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    assertFalse(bestChroms[0].equals(bestChrom));
  }

  /**
   * Ensure that selected Chromosome's are not equal to added Chromosome's.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_4()
      throws Exception {
    TournamentSelector selector = new TournamentSelector(conf, 1, 0.2d);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 30 chromosomes.
    // ---------------------------
    Population pop = new Population(conf);
    selector.select(30, null, pop);
    Population bestChroms = pop;
    List chromosomes = (Vector) privateAccessor.getField(selector,
        "m_chromosomes");
    assertFalse(bestChroms.equals(chromosomes));
  }

  /**
   * Never select best chromosome if prob is 0.0d. it is not allowed to select
   * probability to 0.0d therefor we set it via reflection.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_5()
      throws Exception {
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting(0);
    conf.setRandomGenerator(rn);
    TournamentSelector selector = new TournamentSelector(conf, 4, 0.00001d);
    setNestedField(selector, "m_config", "m_probability", new Double(0.0d));
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    IChromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 5 chromosomes.
    // ---------------------------
    Population pop = new Population(conf);
    IChromosome[] bestChroms;
    selector.select(5, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(thirdBestChrom, bestChroms[0]);
    assertEquals(thirdBestChrom, bestChroms[1]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSelect_6()
      throws Exception {
    // random generator always returning 1 (index of best chromosome below)
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting(1);
    conf.setRandomGenerator(rn);
    TournamentSelector selector = new TournamentSelector(conf, 4, 1.0d);
    Population toAddFrom = new Population(conf);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(conf, gene, 7);
    thirdBestChrom.setFitnessValue(10);
    toAddFrom.addChromosome(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(conf, gene, 3);
    bestChrom.setFitnessValue(12);
    toAddFrom.addChromosome(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene(conf);
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(conf, gene, 3);
    secondBestChrom.setFitnessValue(11);
    toAddFrom.addChromosome(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population(conf);
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    // nothing selected (from nothing!)
    assertEquals(0, bestChroms.length);
    selector.select(1, toAddFrom, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    // receive top 3 chromosomes
    // -------------------------
    pop.getChromosomes().clear();
    selector.select(3, toAddFrom, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(3, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(bestChrom, bestChroms[1]);
    assertEquals(bestChrom, bestChroms[2]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testReturnsUniqueChromosomes_0() throws Exception {
    Configuration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    TournamentSelector selector = new TournamentSelector(conf, 2, 0.5d);
    assertFalse(selector.returnsUniqueChromosomes());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetTournametSize_0()
      throws Exception {
    Configuration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    TournamentSelector sel = new TournamentSelector(conf, 1, 1.0d);
    sel.setTournamentSize(5);
    assertEquals(5, sel.getTournamentSize());
    try {
      sel.setTournamentSize(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetProbability_0()
      throws Exception {
    Configuration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    TournamentSelector sel = new TournamentSelector(conf, 1, 1.0d);
    sel.setProbability(0.6d);
    assertEquals(0.6d, sel.getProbability(), DELTA);
    try {
      sel.setProbability(1.6d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; // this is OK
    }
  }
}
