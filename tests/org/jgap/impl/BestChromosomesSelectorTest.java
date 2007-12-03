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
 * Tests the BestChromosomesSelector class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BestChromosomesSelectorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.35 $";

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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    Boolean needsSorting = (Boolean) privateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    assertTrue(selector.returnsUniqueChromosomes());
    assertTrue(selector.getDoubletteChromosomesAllowed());
    Object fitnessValueComparator = privateAccessor.getField(selector,
        "m_fitnessValueComparator");
    assertTrue(fitnessValueComparator != null);
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
    BestChromosomesSelector op = new BestChromosomesSelector();
    assertSame(conf, op.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testDoubletteChromosomesAllowed_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    selector.setDoubletteChromosomesAllowed(false);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
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
    // of two after the add(..), otherwise a count of 1
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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
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
   * Test selection algorithm.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_1()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
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
   * Test selection algorithm.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_2()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    selector.setDoubletteChromosomesAllowed(false);
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
    assertSame(bestChrom, bestChroms[0]);
  }

  /**
   * Ensure that selected Chromosomes are not equal to added Chromosomes.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_3()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    selector.setDoubletteChromosomesAllowed(false);
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
    selector.setOriginalRate(1.0d);
    // receive top 30 chromosomes (select-method should take into account only
    // 2 chroms!)
    // -----------------------------------------------------------------------
    Population pop = new Population(conf);
    selector.select(30, null, pop);
    Population bestChroms = pop;
    Population chromosomes = (Population) privateAccessor.getField(selector,
        "m_chromosomes");
    assertTrue(bestChroms.equals(chromosomes));
  }

  /**
   * Test selection algorithm with allowed doublettes.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSelect_4()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    selector.setDoubletteChromosomesAllowed(true);
    // the following original rate controls that only 30% of the chromosomes
    // will be considered for selection as given with BestChromosomesSelector.
    // The last 70% will be added as doublettes in this case.
    selector.setOriginalRate(0.3d);
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
    // receive top 4 chromosomes with original rate = 0.3
    // --------------------------------------------------
    pop.getChromosomes().clear();
    selector.select(4, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(4, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(bestChrom, bestChroms[1]); //because of originalRate = 0.3
    assertEquals(secondBestChrom, bestChroms[2]);
    assertEquals(thirdBestChrom, bestChroms[3]);
    // Non-unique chromosomes should have been returned, although not the same
    // but a cloned instance!
    assertEquals(bestChroms[0], bestChroms[1]);
    // receive top 4 chromosomes with original rate = 1
    // ------------------------------------------------
    pop.getChromosomes().clear();
    selector.setOriginalRate(1.0d);
    selector.select(4, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(4, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(secondBestChrom, bestChroms[1]);
    assertEquals(thirdBestChrom, bestChroms[2]);
    assertEquals(bestChrom, bestChroms[3]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEmpty_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
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
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEmpty_2()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 5);
    Population pop = new Population(conf, 1);
    pop.addChromosome(chrom);
    Population popNew = new Population(conf);
    selector.select(1, pop, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
    assertNotNull(popNew.getChromosome(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetOriginalRate_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
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
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    selector.setOriginalRate(0.3d);
    assertEquals(0.3d, selector.getOriginalRate(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testReturnsUnique_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    assertTrue(selector.returnsUniqueChromosomes());
  }

  /**
   * Ensures BCS is implementing Serializable.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testIsSerializable_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    assertTrue(isSerializable(selector));
  }

  /**
   * Ensures that BCS implements Serializable correctly.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testDoSerialize_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector(conf);
    Object o = doSerialize(selector);
    assertEquals(o, selector);
  }}
