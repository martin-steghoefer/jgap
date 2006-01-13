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
 * Tests the CrossoverOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CrossoverOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.21 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CrossoverOperatorTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    new CrossoverOperator(null);
    new CrossoverOperator(new DefaultMutationRateCalculator());
    new CrossoverOperator(2);
    new CrossoverOperator(1);
    new CrossoverOperator(50);
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
    try {
      new CrossoverOperator(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_2() {
    try {
      new CrossoverOperator( -3);
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
   * @since 1.1
   */
  public void testOperate_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
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
    CrossoverOperator op = new CrossoverOperator();
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
    GeneticOperator op = new CrossoverOperator();
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
   * Test with CompositeGene
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
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
    CrossoverOperator op = new CrossoverOperator();
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    CompositeGene compGene = new CompositeGene();
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
    assertEquals(8, ( (Integer) ( (Vector) result.getAllele())
                 .get(0)).intValue());
  }

  /**
   * Considers IGeneticOperatorConstraint. Here, the crossing over of a
   * BooleanGene is forbidden by that constraint.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    IGeneticOperatorConstraint constraint = new
        GeneticOperatorConstraintForTest();
    Genotype.getConfiguration().getJGAPFactory().setGeneticOperatorConstraint(
        constraint);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 2, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);

    CrossoverOperator op = new CrossoverOperator();

    Gene cgene0 = new IntegerGene(1, 10);
    cgene0.setAllele(new Integer(8));
    Gene[] genes0 = new Gene[] {
        cgene0};
    Chromosome chrom0 = new Chromosome(genes0);

    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(5));
    Gene[] genes1 = new Gene[] {
        cgene1};
    ChromosomeForTest chrom1 = new ChromosomeForTest(genes1);

    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(6));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom0, chrom1, chrom2};
    // add some nonsense objects to results list (to see if they are kept)
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    // Do the crossing over.
    // ---------------------
    op.operate(new Population(population), chroms);

    assertEquals(2 + 2, chroms.size());
    assertSame(gene1, chroms.get(0));
    assertSame(gene2, chroms.get(1));
    Chromosome target = (Chromosome) chroms.get(2);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Ensures the operator is implementing Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator();
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that the operator and all objects contained implement Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    CrossoverOperator op = new CrossoverOperator(
        new DefaultCrossoverRateCalculator());
    CrossoverOperator o = (CrossoverOperator) doSerialize(op);
    assertEquals(o, op);
  }

  public class GeneticOperatorConstraintForTest
      implements IGeneticOperatorConstraint {
    public boolean isValid(Population a_pop, List a_chromosomes,
                           GeneticOperator a_caller) {
      Iterator it = a_chromosomes.iterator();
      while (it.hasNext()) {
        Chromosome chrom = (Chromosome) it.next();
        if (ChromosomeForTest.class == chrom.getClass()) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0() throws Exception {
    GeneticOperator op = new CrossoverOperator();
    assertFalse(op.equals(new Chromosome()));
  }
}
