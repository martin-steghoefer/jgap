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
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public SwappingMutationOperatorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(SwappingMutationOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_1() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertNotNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_2() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(null);
    assertEquals(0, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_3() {
    IUniversalRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

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
     * E.g. we could check if something has changed. For that use a
     * RandomGeneratorForTest*/
  }
}
