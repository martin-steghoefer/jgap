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
 * Tests for ReproductionOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ReproductionOperatorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.6 $";

  public ReproductionOperatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(ReproductionOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    //test only if class can be constructed without error
    ReproductionOperator repOp = new ReproductionOperator();
  }

  public void testOperate_0() {
    ReproductionOperator repOp = new ReproductionOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{};
    repOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    repOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  public void testOperate_1() {
  /**@todo implement.
   * E.g. we could check if something has changed. For that use a
   * RandomGeneratorForTest*/
  }
}
