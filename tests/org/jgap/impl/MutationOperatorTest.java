/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests for MutationOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class MutationOperatorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  public MutationOperatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(MutationOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    MutationOperator mutOp = new MutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_1() {
    MutationOperator mutOp = new MutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertNotNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_2() {
    MutationOperator mutOp = new MutationOperator(null);
    assertEquals(0, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_3() {
    MutationRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  public void testOperate_0() throws Exception {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{};
    mutOp.operate(new Configuration(), population, candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    RandomGeneratorForTest gen = new RandomGeneratorForTest();
    gen.setNextInt(9);
    conf.setRandomGenerator(gen);
    Chromosome c1 = new Chromosome(new BooleanGene(),9);
    conf.setSampleChromosome(c1);
    conf.addNaturalSelector(new BestChromosomesSelector(),true);
    conf.setPopulationSize(5);
    for (int i=0;i<c1.getGenes().length;i++) {
      c1.getGene(i).setAllele(Boolean.TRUE);
    }
    c1.setActiveConfiguration(conf);
    Chromosome c2 = new Chromosome(new IntegerGene(),4);
    c2.setActiveConfiguration(conf);
    for (int i=0;i<c2.getGenes().length;i++) {
      c2.getGene(i).setAllele(new Integer(27));
    }
    population = new Chromosome[]{c1,c2};
    mutOp.operate(conf, population, candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  public void testOperate_1() throws Exception {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    Configuration conf = new Configuration();
    conf.setRandomGenerator(new StockRandomGenerator());
    try {
      mutOp.operate(conf, population, candChroms);
      fail();
    }
    catch (IllegalStateException iex) {
      ;//this is OK
    }
  }

  public void testOperate_2() {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    try {
      mutOp.operate(new Configuration(), population, candChroms);
      fail();
    }
    catch (NullPointerException nex) {
        ;//this is OK
    }
  }

  public void testOperate_3() {
  /**@todo implement.
   * E.g. we could check if something has changed. For that use a
   * RandomGeneratorForTest*/
  }
}

class TestFitnessFunction
    extends FitnessFunction {
  protected int evaluate(Chromosome a_subject) {
    //result does not matter here
    return 1;
  }
}
