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
 * Tests for BestChromosomesSelector class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BestChromosomesSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.25 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BestChromosomesSelectorTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testConstruct_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Boolean needsSorting = (Boolean) privateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    assertTrue(selector.returnsUniqueChromosomes());
    assertFalse(selector.getDoubletteChromosomesAllowed());
    Object fitnessValueComparator = privateAccessor.getField(selector,
        "m_fitnessValueComparator");
    assertTrue(fitnessValueComparator != null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testDoubletteChromosomesAllowed_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(false);
    assertFalse(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testAdd_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    selector.add(chrom);
    Boolean needsSorting = (Boolean) privateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.TRUE, needsSorting);
    List chromosomes = ( (Population) privateAccessor.getField(selector,
        "m_chromosomes")).getChromosomes();
    assertEquals(1, chromosomes.size());
    assertEquals(chrom, chromosomes.get(0));
    selector.add(chrom);
    assertEquals(chrom, chromosomes.get(0));
    // if BestChromosomesSelector adds non-unique chroms, then we have a count
    // of two then after the add(..), else a count of 1
    selector.setDoubletteChromosomesAllowed(false);
    assertEquals(1, chromosomes.size());
    selector.setDoubletteChromosomesAllowed(true);
    selector.add(chrom);
    assertEquals(2, chromosomes.size());
  }

  /**
   * Test if below functionality available without error.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    BestChromosomesSelector selector = new BestChromosomesSelector();
    Gene gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    selector.select(1, null, new Population());
  }

  /**
   * Test selection algorithm
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population();
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    selector.setOriginalRate(1.0d);
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
   * Test selection algorithm
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population();
    selector.select(1, null, pop);
    IChromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    selector.setOriginalRate(1.0d);
    // receive top 30 chromosomes (select-method should take into account only
    // 2 chroms!)
    // -----------------------------------------------------------------------
    pop.getChromosomes().clear();
    selector.select(30, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(2, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(thirdBestChrom, bestChroms[1]);
    assertTrue(bestChrom == bestChroms[0]);
  }

  /**
   * Ensure that selected Chromosome's are not equal to added Chromosome's.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_3()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    selector.setOriginalRate(1.0d);
    // receive top 30 chromosomes (select-method should take into account only
    // 2 chroms!)
    // -----------------------------------------------------------------------
    Population pop = new Population();
    selector.select(30, null, pop);
    Population bestChroms = pop;
    Population chromosomes = (Population) privateAccessor.getField(selector,
        "m_chromosomes");
    assertTrue(bestChroms.equals(chromosomes));
  }

  /**
   * Test selection algorithm with allowed doublettes
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    BestChromosomesSelector selector = new BestChromosomesSelector();
    selector.setDoubletteChromosomesAllowed(true);
    selector.setOriginalRate(0.3d);
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(Boolean.valueOf(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population();
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
    assertEquals(secondBestChrom, bestChroms[2]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEmpty_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    selector.add(chrom);
    selector.empty();
    Boolean needsSorting = (Boolean) privateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    List chromosomes = ( (Population) privateAccessor.getField(selector,
        "m_chromosomes")).getChromosomes();
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if clear()-method does not affect original Population.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEmpty_1()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
    assertNotNull(popNew.getChromosome(0));
  }

  /**
   * Test if clear()-method does not affect return value.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEmpty_2()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    pop = popNew;
    selector.empty();
    assertEquals(1, pop.size());
    assertNotNull(pop.getChromosome(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetOriginalRate_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    try {
      selector.setOriginalRate(1.01d);
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
   * @since 2.2
   */
  public void testSetOriginalRate_0_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    try {
      selector.setOriginalRate( -0.1d);
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
   * @since 2.2
   */
  public void testSetOriginalRate_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    selector.setOriginalRate(0.3d);
    assertEquals(0.3d, selector.getOriginalRate(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testReturnsUnique_0() {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    assertTrue(selector.returnsUniqueChromosomes());

  }
}
