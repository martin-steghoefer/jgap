/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.eval;

import java.util.*;

import org.jgap.*;
import org.jgap.impl.*;

import junit.framework.*;

/**
 * Tests for PopulationHistory class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class PopulationHistoryTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(PopulationHistoryTest.class);
    return suite;
  }

  /**
   * Test if construction possible.
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_0() {
    new PopulationHistory(0);
    new PopulationHistory(1);
    new PopulationHistory(100);
    new PopulationHistory(1000);
  }

  /**
   * Test if construction fails correctly.
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_1() {
    try {
      new PopulationHistory( -1);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testGetPopulation_0() {
    Population pop = new PopulationHistory(0).getPopulation(0);
    assertEquals(null, pop);
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testGetPopulation_1() {
    Population pop = new Population();
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 4);
    chrom.setFitnessValue(17);
    pop.addChromosome(chrom);

    Population pop2 = new Population();
    gene = new IntegerGene();
    chrom = new Chromosome(gene, 3);
    chrom.setFitnessValue(3);
    pop2.addChromosome(chrom);

    //ensure we have not two populations seen as equal.
    assertFalse(pop.equals(pop2));
    PopulationHistory ph = new PopulationHistory(0);
    ph.addPopulation(pop);
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());

    //lastly added population has index 0!
    Population result = ph.getPopulation(1);
    assertEquals(pop, result);
    result = ph.getPopulation(0);
    assertEquals(pop2, result);
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testRemoveAllPopulations_0() {
    PopulationHistory ph = new PopulationHistory(0);
    Population pop = new Population();
    Population pop2 = new Population();
    ph.addPopulation(pop);
    ph.addPopulation(pop2);
    ph.removeAllPopulations();
    assertEquals(0, ph.size());
  }

  /**
   * Limited capacity of 2 Population objects
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testAddPopulation_0() {
    PopulationHistory ph = new PopulationHistory(2);
    Population pop = new Population();
    Population pop2 = new Population();
    Population pop3 = new Population();
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.addPopulation(pop3);
    assertEquals(2, ph.size());
  }

  /**
   * Unlimited capacity of Population objects
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testAddPopulation_1() {
    PopulationHistory ph = new PopulationHistory(0);
    Population pop = new Population();
    Population pop2 = new Population();
    Population pop3 = new Population();
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.addPopulation(pop3);
    assertEquals(3, ph.size());
  }

  /**
   * Unlimited capacity of Population objects
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSetPopulations_0() {
    Population pop = new Population();
    Population pop2 = new Population();
    Population pop3 = new Population();
    List pops = new Vector();
    pops.add(pop);
    pops.add(pop2);
    pops.add(pop3);
    PopulationHistory ph = new PopulationHistory(0);
    ph.setPopulations(pops);
    assertEquals(3, ph.size());
    assertEquals(pop, ph.getPopulation(0));
    assertEquals(pop2, ph.getPopulation(1));
    assertEquals(pop3, ph.getPopulation(2));
  }

  /**
   * Limited capacity of Population objects
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSetPopulations_1() {
    Population pop = new Population();
    Population pop2 = new Population();
    Population pop3 = new Population();
    List pops = new Vector();
    pops.add(pop);
    pops.add(pop2);
    pops.add(pop3);
    PopulationHistory ph = new PopulationHistory(2);
    ph.setPopulations(pops);
    assertEquals(2, ph.size());
    assertEquals(pop, ph.getPopulation(0));
    assertEquals(pop2, ph.getPopulation(1));
  }

  /**
   * Limited capacity of Population objects
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSize_0() {
    Population pop = new Population();
    Population pop2 = new Population();
    PopulationHistory ph = new PopulationHistory(2);
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.removeAllPopulations();
    assertEquals(0, ph.size());
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop);
    assertEquals(2, ph.size());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetPopulations_0() {
    PopulationHistory ph = new PopulationHistory(3);
    List l = ph.getPopulations();
    assertEquals(0, l.size());
    Population pop = new Population();
    ph.addPopulation(pop);
    assertEquals(1, l.size());
    Population pop2 = new Population();
    ph.addPopulation(pop2);
    assertEquals(2, l.size());
    ph.addPopulation(pop2);
    assertEquals(3, l.size());
    l = ph.getPopulations();
    assertEquals(pop2, l.get(0));
    assertEquals(pop2, l.get(1));
    assertEquals(pop, l.get(2));
  }
}
