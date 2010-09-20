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
import junitx.util.*;

/**
 * Tests the ThresholdSelector class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ThresholdSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ThresholdSelectorTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    try {
      new ThresholdSelector(null, 1.1d);
      fail();
    }
    catch (IllegalArgumentException ex) {
      ; //this is OK
    }
  }

  public void testConstruct_1()
      throws Exception {
    try {
      new ThresholdSelector(null, -0.5d);
      fail();
    }
    catch (IllegalArgumentException ex) {
      ; //this is OK
    }
  }

  public void testConstruct_2()
      throws Exception {
    Configuration.reset();
    DefaultConfiguration conf = new DefaultConfiguration();
    ThresholdSelector selector = new ThresholdSelector(conf, 0.5d);
    Double m_bestChroms_Percentage = (Double) getNestedField(selector,
        "m_config", "m_bestChroms_Percentage");
    assertEquals(0.5d, m_bestChroms_Percentage.doubleValue(), DELTA);
    assertFalse(selector.returnsUniqueChromosomes());
    Object m_fitnessValueComparator = privateAccessor.getField(selector,
        "m_fitnessValueComparator");
    assertTrue(m_fitnessValueComparator != null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_3()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    ThresholdSelector op = new ThresholdSelector();
    assertSame(conf, op.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testAdd_0()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 0.5d);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    selector.add(chrom);
    List chromosomes = ( (Vector) PrivateAccessor.getField(selector,
        "m_chromosomes"));
    assertEquals(1, chromosomes.size());
    assertEquals(chrom, chromosomes.get(0));
    selector.add(chrom);
    assertEquals(chrom, chromosomes.get(0));
    assertEquals(2, chromosomes.size());
    selector.add(chrom);
    assertEquals(3, chromosomes.size());
  }

  /**
   * Test if below functionality available without error.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_0()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 0.3d);
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_1()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
    assertEquals(secondBestChrom, bestChroms[1]);
    assertEquals(thirdBestChrom, bestChroms[2]);
  }

  /**
   * Always select best chromosome if threshold is 1.0d.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSelect_2()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
    assertEquals(thirdBestChrom, bestChroms[1]);
    assertTrue(bestChrom == bestChroms[0]);
  }

  /**
   * Never select best chromosome for granted if threshold is 0.0d.
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
    conf.setRandomGenerator(new RandomGeneratorForTesting(1));
    ThresholdSelector selector = new ThresholdSelector(conf, 0.0d);
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
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
    List chromosomes = (Vector) PrivateAccessor.getField(selector,
        "m_chromosomes");
    assertFalse(bestChroms.equals(chromosomes));
  }

  /**
   * Always select best chromosome if threshold is 1.0d. Target population not
   * empty.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSelect_5()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
    selector.select(30, pop, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(31, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(thirdBestChrom, bestChroms[3]);
    assertTrue(bestChrom == bestChroms[0]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testEmpty_0()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    selector.add(chrom);
    selector.empty();
    Boolean needsSorting = (Boolean) PrivateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    List chromosomes = ( (Vector) PrivateAccessor.getField(selector,
        "m_chromosomes"));
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if method clear() does not affect original Population.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testEmpty_1()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
   * Test if method clear() does not affect return value.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testEmpty_2()
      throws Exception {
    ThresholdSelector selector = new ThresholdSelector(conf, 1.0d);
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
}
