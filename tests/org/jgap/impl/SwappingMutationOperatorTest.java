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
 * Tests for SwappingMutationOperator class
 *
 * @author Klaus Meffert
 * @since 2.1
 */
public class SwappingMutationOperatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public SwappingMutationOperatorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(SwappingMutationOperatorTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_0() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_1() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertNotNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_2() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(null);
    assertEquals(0, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_3() {
    IUniversalRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    RandomGeneratorForTest gen = new RandomGeneratorForTest();
    gen.setNextInt(9);
    conf.setRandomGenerator(gen);
    Genotype.setConfiguration(conf);
    Chromosome c1 = new Chromosome(new BooleanGene(), 9);
    conf.setSampleChromosome(c1);
    conf.addNaturalSelector(new BestChromosomesSelector(), true);
    conf.setPopulationSize(5);
    for (int i = 0; i < c1.getGenes().length; i++) {
      c1.getGene(i).setAllele(Boolean.TRUE);
    }
    Chromosome c2 = new Chromosome(new IntegerGene(), 4);
    for (int i = 0; i < c2.getGenes().length; i++) {
      c2.getGene(i).setAllele(new Integer(27));
    }
    population = new Chromosome[] {
        c1, c2};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_1()
      throws Exception {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(new BooleanGene(), 9),
        (new Chromosome(new IntegerGene(), 4))};
    Configuration conf = new Configuration();
    conf.setRandomGenerator(new StockRandomGenerator());
    Genotype.setConfiguration(conf);
    mutOp.operate(new Population(population), candChroms);
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_2() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(new BooleanGene(), 9),
        (new Chromosome(new IntegerGene(), 4))};
    try {
      mutOp.operate(new Population(population), candChroms);
      fail();
    }
    catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  public void testOperate_3() {
    /**@todo implement.
     * E.g. we could check if something has changed (and in an expected manner).
     * For that use a RandomGeneratorForTest*/
  }

  /** Check if none of the genes is lost during swapping.
   * The checksum must stay the same. This step tests the swapping part,
   * not the the whole operator.
   * @author Audrius Meskauskas
   */
  public void testOperate_4() throws Exception {
      Configuration conf = new DefaultConfiguration();
      conf.setFitnessFunction(new TestFitnessFunction());
      Genotype.setConfiguration(conf);

      int n_iterations = 20;

      RandomGenerator generator = new StockRandomGenerator();
      SwappingMutationOperator mutOp = new SwappingMutationOperator();
      mutOp.setStartOffset(0);

      for (int n_genes = 0; n_genes < 20; n_genes++) {

      Gene [] genes = new IntegerGene [ n_genes ];
      for (int i = 0; i < genes.length; i++) {
          genes [i] = new IntegerGene(-1000,1000);
          genes [i].setToRandomValue(generator);
      }

      final long checksum = checksum (genes);
      Gene [] prev = new Gene [genes.length];

      for (int i = 0; i < n_iterations; i++)
       for (int gene = 0; gene < genes.length; gene++) {
           System.arraycopy( genes, 0, prev, 0, genes.length);
           genes = mutOp.operate( generator, gene, genes );
           // checksum constant:
           assertEquals( checksum, checksum (genes) );
       }
      }
  }

 private long checksum( Gene [] a_genes )
 {
     long s = 0;
     for (int i = 0; i < a_genes.length; i++) {
         s += ( (IntegerGene) a_genes [i] ).intValue();
     }
     return s;
 }

}
