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
import org.jgap.JGAPTestCase.*;
import junit.framework.*;

/**
 * Tests the InversionOperator class.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class InversionOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.12 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(InversionOperatorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_0()
      throws Exception {
    new InversionOperator(conf);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_1()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    InversionOperator op = new InversionOperator();
    assertSame(conf, op.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testOperate_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // tupels: (index of chromosome; locus), see InversionOperator.operate
    rand.setNextIntSequence(new int[] {
                            0, 1, 1, 1});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    InversionOperator op = new InversionOperator(conf);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene cgene3 = new IntegerGene(conf, 2, 120);
    cgene3.setAllele(new Integer(99));
    Gene[] genes2 = new Gene[] {
        cgene2, cgene3};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    final int size = chroms.size();
    op.operate(new Population(conf, population), chroms);
    assertEquals(size + 1, chroms.size());
    Chromosome target = (Chromosome) chroms.get(size);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    op.operate(new Population(conf, population), chroms);
    assertEquals(size + 2, chroms.size());
    target = (Chromosome) chroms.get(size + 1);
    assertEquals(99, ( (Integer) target.getGene(0).getAllele()).intValue());
    assertEquals(8, ( (Integer) target.getGene(1).getAllele()).intValue());
  }

  /**
   * Tests if operator produces same results for two operate-runs.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new InversionOperator(conf);
    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    Chromosome[] population2 = (Chromosome[]) population.clone();
    op.operate(new Population(conf, population), chroms);
    op.operate(new Population(conf, population2), chroms);
    assertTrue(isChromosomesEqual(population, population2));
  }

  /**
   * Test with CompositeGene.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // tupels: (index of chromosome; locus), see InversionOperator.operate
    rand.setNextIntSequence(new int[] {
                            0, 1, 1, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    InversionOperator op = new InversionOperator(conf);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    CompositeGene compGene = new CompositeGene(conf);
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    int size = chroms.size();
    op.operate(new Population(conf, population), chroms);
    assertEquals(size + 1, chroms.size());
    Chromosome target = (Chromosome) chroms.get(size);
    CompositeGene cog = (CompositeGene) target.getGene(0);
    assertEquals(6, ( (Integer) ( (Vector) cog.getAllele()).get(0)).intValue());
    op.operate(new Population(conf, population), chroms);
    assertEquals(size + 2, chroms.size());
    target = (Chromosome) chroms.get(size + 1);
    IntegerGene result = (IntegerGene) target.getGene(0);
    assertEquals(8, ( (Integer) result.getAllele()).intValue());
  }

  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0()
      throws Exception {
    GeneticOperator op = new InversionOperator(conf);
    assertFalse(op.equals(new Chromosome(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // tupels: (index of chromosome; locus), see InversionOperator.operate
    rand.setNextIntSequence(new int[] {
                            0, 1, 1, 1, 2});
    conf.setPopulationSize(6);
    conf.setRandomGenerator(rand);
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    Gene cgene1_2 = new IntegerGene(conf, 1, 10);
    cgene1_2.setAllele(new Integer(9));
    CompositeGene compGene = new CompositeGene(conf);
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    chrom1.setConstraintChecker(new TestConstraintChecker());
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2, cgene1_2};
    Chromosome chrom2 = new Chromosome(conf);
    chrom2.setGenes(genes2);
    chrom2.setConstraintChecker(new TestConstraintChecker());
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    InversionOperator op = new InversionOperator(conf);
    try {
      op.operate(new Population(conf, population), chroms);
      fail();
    } catch (Error iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_0()
      throws Exception {
    InversionOperator op = new InversionOperator(conf);
    assertEquals(1, op.compareTo(null));
    InversionOperator op2 = new InversionOperator(conf);
    assertEquals(0, op.compareTo(op2));
    assertEquals(0, op2.compareTo(op));
  }

  public class TestConstraintChecker
      implements IGeneConstraintChecker {
    private int m_callcount;

    public boolean verify(Gene a_gene, Object a_alleleValue,
                          IChromosome a_chrom, int a_index) {
      if (m_callcount++ >= 2) {
        return false;
      }
      else {
        return true;
      }
    }
  }
}
