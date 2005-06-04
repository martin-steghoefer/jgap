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
 * Teste the GreedyCrossover class
 *
 * @author Klaus Meffert
 * @since 2.1
 */
public class GreedyCrossoverTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.13 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GreedyCrossoverTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
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
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    op.setStartOffset(0);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes1 = new Gene[] {
        cgene1, cgene2};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene[] genes2 = new Gene[] {
        cgene2, cgene1};
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
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Same as testOperate_0 except op.setStartOffset(1) instead of 0
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_1()
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
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    op.setStartOffset(1);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes1 = new Gene[] {
        cgene1, cgene2};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene[] genes2 = new Gene[] {
        cgene2, cgene1};
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
    try {
      op.operate(new Population(population), chroms);
      fail();
    }
    catch (Error e) {
      ; //this is OK
    }
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
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    CompositeGene compGene = new CompositeGene();
    compGene.addGene(cgene1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes1 = new Gene[] {
        cgene1, cgene2};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene[] genes2 = new Gene[] {
        cgene1, cgene2, cgene1};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    try {
      op.operate(new Population(population), chroms);
      fail();
    }
    catch (Error e) {
      ; //this is OK
    }
  }

  /**
   * Test with CompositeGene and two identical Genes in a Chromosome
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    op.setStartOffset(0);
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    CompositeGene compGene = new CompositeGene();
    compGene.addGene(cgene1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes1 = new Gene[] {
        compGene, cgene1, cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene[] genes2 = new Gene[] {
        compGene, cgene1, cgene1};
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
    try {
      op.operate(new Population(population), chroms);
      fail();
    }
    catch (Error e) {
      ; //this is OK
    }
  }

  /**
   * Test the example from the literature.
   * This test tests the crossover main algorithm, not the whole operator.
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.1
   */
  public void testOperate_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    GreedyCrossover cross = new GreedyCrossover() {
      /* Computes the distances how it was described in the
         literature example */
      public double distance(Object a_from, Object a_to) {
        IntegerGene from = (IntegerGene) a_from;
        IntegerGene to = (IntegerGene) a_to;
        int a = from.intValue();
        int b = to.intValue();
        if (a > b) {
          int t = a;
          a = b;
          b = t;
        }
        ;
        // 4,1 is shorter than 4,5
        if (a == 1 && b == 4) {
          return 1;
        }
        if (a == 4 && b == 5) {
          return 2;
        }
        // 1,2 is shorter that 1,3
        if (a == 1 && b == 2) {
          return 10;
        }
        if (a == 1 && b == 3) {
          return 20;
        }
        // 2,0 is shorter than 2,3
        if (a == 0 && b == 2) {
          return 100;
        }
        if (a == 2 && b == 3) {
          return 200;
        }
        throw new Error
            ("These two should not be compared: " + a + " and " + b);
      }
    };
    cross.ASSERTIONS = true;
    cross.setStartOffset(0);
    Chromosome a =
        chromosome(new int[] {1, 2, 3, 4, 5, 0});
    Chromosome b =
        chromosome(new int[] {4, 1, 3, 2, 0, 5});
    // in the literature example it was 1, 2, 0, 5, 4, 3, but the random
    // choice is involved in the last step. In this implementation
    // the choice is not random and the last two genes are always
    // returned as 3, 4.
    // -----------------------------------------------------------------
    Chromosome must_a = chromosome(new int[] {1, 2, 0, 5, 3, 4});
    // this is same as in the literature, the random choice is not involved.
    // ---------------------------------------------------------------------
    Chromosome must_b = chromosome(new int[] {4, 1, 2, 0, 5, 3});
    cross.operate(b, a);
    assertEquals(a, must_a);
    assertEquals(b, must_b);
  }

  /**
   * Tests if population size grows expectedly after two consecutive calls.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_5()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextDouble(0.45d);
    rand.setNextInt(0);
    op.setStartOffset(0);
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene cgene1_2 = new IntegerGene(1, 150);
    cgene1.setAllele(new Integer(99));
    Gene[] genes1 = new Gene[] {
        cgene1, cgene1_2};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(9));
    Gene cgene2_2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(1));
    Gene[] genes2 = new Gene[] {
        cgene2, cgene2_2};
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
    assertEquals(3, chroms.size());
    Population pop = new Population(population);
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2, chroms.size());
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2 + 2, chroms.size());
  }

  /**
   * Tests if error thrown because of wrong length of gene
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testOperate_6()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GreedyCrossover op = new GreedyCrossover();
    op.ASSERTIONS = true;
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextDouble(0.45d);
    rand.setNextInt(0);
    op.setStartOffset(0);
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
    cgene2.setAllele(new Integer(9));
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
    assertEquals(3, chroms.size());
    Population pop = new Population(population);
    try {
      op.operate(pop, chroms);
      fail();
    }catch (Error e) {
      ;//this is OK
    }
  }

  /**
   * Make a chromosome from the array of integer genes.
   * @param genes input genes
   * @return chromosome containing input genes
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.1
   */
  private Chromosome chromosome(int[] genes)
      throws Exception {
    IntegerGene[] ig = new IntegerGene[genes.length];
    for (int i = 0; i < ig.length; i++) {
      ig[i] = new IntegerGene(0, 5);
      ig[i].setAllele(new Integer(genes[i]));
    }
    return new Chromosome(ig);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testStartoffset_0() {
    GreedyCrossover op = new GreedyCrossover();
    assertEquals(1, op.getStartOffset());
    op.setStartOffset(2);
    assertEquals(2, op.getStartOffset());
    op.setStartOffset(1);
    assertEquals(1, op.getStartOffset());
    op.setStartOffset(0);
    assertEquals(0, op.getStartOffset());
  }
}
