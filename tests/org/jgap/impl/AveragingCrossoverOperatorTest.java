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
 * Test class for AveragingCrossoverOperator class
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AveragingCrossoverOperatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.10 $";

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(AveragingCrossoverOperatorTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new AveragingCrossoverOperator();
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    op.operate(new Population(population), chroms);
    assertEquals(5, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Tests if crossing over produces same results for two operate-runs
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new AveragingCrossoverOperator();
    Genotype.setConfiguration(conf);
    conf.addGeneticOperator(op);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    Chromosome[] population2 = (Chromosome[]) population.clone();
    op.operate(new Population(population), chroms);
    op.operate(new Population(population2), chroms);
    assertTrue(isChromosomesEqual(population, population2));
  }

  /**
   * Using CompositeGene
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new AveragingCrossoverOperator();
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    CompositeGene compGene = new CompositeGene();
    compGene.addGene(sampleGene);
    Chromosome chrom = new Chromosome(compGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    compGene = new CompositeGene();
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    op.operate(new Population(population), chroms);
    assertEquals(5, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    CompositeGene result = (CompositeGene) target.getGene(0);
    assertEquals(8, ( (Integer) ( (Vector) result.getAllele()).get(0)).intValue());
  }

  public static boolean isChromosomesEqual(Chromosome[] list1,
                                           Chromosome[] list2) {
    if (list1 == null) {
      if (list2 == null) {
        return true;
      }
      else {
        return false;
      }
    }
    else if (list2 == null) {
      return false;
    }
    else {
      if (list1.length != list2.length) {
        return false;
      }
      else {
        for (int i = 0; i < list1.length; i++) {
          Chromosome c1 = (Chromosome) list1[i];
          Chromosome c2 = (Chromosome) list2[i];
          if (!c1.equals(c2)) {
            return false;
          }
        }
        return true;
      }
    }
  }

  /**
   * Following should be possible without exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0() {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    new AveragingCrossoverOperator( (RandomGenerator)null);
  }

  /**
   * Following should be possible without exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_1() {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    new AveragingCrossoverOperator( (IUniversalRateCalculator)null);
  }
}