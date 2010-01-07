/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.eval;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the PopulationHistory class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class PopulationHistoryTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

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
    } catch (IllegalArgumentException iex) {
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testGetPopulation_1()
      throws Exception {
    Population pop = new Population(conf);
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 4);
    chrom.setFitnessValue(17);
    pop.addChromosome(chrom);
    Population pop2 = new Population(conf);
    gene = new IntegerGene(conf);
    chrom = new Chromosome(conf, gene, 3);
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testRemoveAllPopulations_0()
      throws Exception {
    PopulationHistory ph = new PopulationHistory(0);
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
    ph.addPopulation(pop);
    ph.addPopulation(pop2);
    ph.removeAllPopulations();
    assertEquals(0, ph.size());
  }

  /**
   * Limited capacity for 2 Population objects.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testAddPopulation_0()
      throws Exception {
    PopulationHistory ph = new PopulationHistory(2);
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
    Population pop3 = new Population(conf);
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.addPopulation(pop3);
    assertEquals(2, ph.size());
  }

  /**
   * Unlimited capacity of Population objects.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testAddPopulation_1()
      throws Exception {
    PopulationHistory ph = new PopulationHistory(0);
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
    Population pop3 = new Population(conf);
    ph.addPopulation(pop);
    assertEquals(1, ph.size());
    ph.addPopulation(pop2);
    assertEquals(2, ph.size());
    ph.addPopulation(pop3);
    assertEquals(3, ph.size());
  }

  /**
   * Unlimited capacity of Population objects.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSetPopulations_0()
      throws Exception {
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
    Population pop3 = new Population(conf);
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
   * Limited capacity of Population objects.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSetPopulations_1()
      throws Exception {
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
    Population pop3 = new Population(conf);
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
   * Limited capacity of Population objects.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testSize_0()
      throws Exception {
    Population pop = new Population(conf);
    Population pop2 = new Population(conf);
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
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetPopulations_0()
      throws Exception {
    PopulationHistory ph = new PopulationHistory(3);
    List l = ph.getPopulations();
    assertEquals(0, l.size());
    Population pop = new Population(conf);
    ph.addPopulation(pop);
    assertEquals(1, l.size());
    Population pop2 = new Population(conf);
    ph.addPopulation(pop2);
    assertEquals(2, l.size());
    ph.addPopulation(pop2);
    assertEquals(3, l.size());
    l = ph.getPopulations();
    assertEquals(pop2, l.get(0));
    assertEquals(pop2, l.get(1));
    assertEquals(pop, l.get(2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void testSerializable_0()
      throws Exception {
    PopulationHistory ph = new PopulationHistory(0);
    assertTrue(super.isSerializable(ph));
    PopulationHistory clon = (PopulationHistory)doSerialize(ph);
    assertEquals(ph, clon);
  }
}
