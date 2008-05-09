/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;

import org.jgap.impl.*;

import junit.framework.*;

/**
 * Tests the Population class.
 *
 * @author Klaus Meffert
 * @author Chris Knowles
 * @since 2.0
 */
public class PopulationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.40 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(PopulationTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    try {
      new Population(new ConfigurationForTesting(), (IChromosome[])null);
      fail();
    } catch (NullPointerException e) {
      ; //this is OK
    }
  }

  public void testConstruct_1()
      throws Exception {
    try {
      new Population(conf, -1);
    } catch (IllegalArgumentException iae) {
      ; //this is ok
    }
  }

  public void testConstruct_2()
      throws Exception {
    try {
      new Population(new ConfigurationForTesting(), (IChromosome)null);
      fail();
    } catch (IllegalArgumentException e) {
      ; //this is OK
    }
  }

  public void testConstruct_3()
      throws Exception {
    int nTot = 100;
    Chromosome[] chromosomes = new Chromosome[nTot];
    Population pop = new Population(conf, chromosomes);
    assertNotNull(pop);
    //check size is correct
    assertEquals(nTot, pop.size());
  }

  public void testAddChromosome_0()
      throws Exception {
    Gene g = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, g, 29);
    c.setFitnessValue(45);
    Population p = new Population(conf);
    p.addChromosome(c);
    assertEquals(1, p.size());
  }

  public void testAddChromosome_1()
      throws Exception {
    Population p = new Population(conf);
    p.addChromosome(null);
    assertEquals(0, p.size());
  }

  public void testAddChromosomes_0()
      throws Exception {
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    c.setFitnessValue(45);
    Population p1 = new Population(conf);
    p1.addChromosome(c);
    Population p2 = new Population(conf);
    p2.addChromosomes(p1);
    assertEquals(p1.size(), p2.size());
  }

  public void testAddChromosomes_1()
      throws Exception {
    Population p = new Population(conf);
    p.addChromosomes(null);
    assertEquals(0, p.size());
  }

  public void testSetChromosome_0()
      throws Exception {
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    Population p = new Population(conf);
    p.setChromosome(0, c);
    p.setChromosome(0, c);
    assertEquals(1, p.size());
    try {
      p.setChromosome(2, c);
      fail();
    } catch (IndexOutOfBoundsException oex) {
      ; //this is OK
    }
  }

  public void testSetChromosome_1()
      throws Exception {
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    Population p = new Population(conf);
    p.addChromosome(c);
    Chromosome c2 = new Chromosome(conf, g, 20);
    p.setChromosome(0, c2);
    assertEquals(1, p.size());
    assertEquals(p.getChromosome(0), c2);
    assertFalse(c.equals(c2));
  }

  public void testSetChromosomes_0()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      chromosomes.add(c);
    }
    Population p = new Population(conf);
    p.setChromosomes(chromosomes);
    assertEquals(nTot, p.size());
  }

  public void testGetChromosomes_0()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      chromosomes.add(c);
    }
    Population p = new Population(conf);
    p.setChromosomes(chromosomes);
    assertEquals(chromosomes, p.getChromosomes());
  }

  public void testGetChromosome_0()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Chromosome thechosenone = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      chromosomes.add(c);
      if (i == 100) {
        thechosenone = c;
      }
    }
    Population p = new Population(conf);
    p.setChromosomes(chromosomes);
    assertEquals(thechosenone, p.getChromosome(100));
  }

  public void testToChromosomes_0()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      c.getGene(0).setAllele(new Double(i));
      chromosomes.add(c);
    }
    Population p = new Population(conf);
    p.setChromosomes(chromosomes);
    IChromosome[] aChrom = p.toChromosomes();
    assertEquals(aChrom.length, chromosomes.size());
    // compare populations with unsorted list of chromosomes
    Population newPop = new Population(conf, aChrom);
    assertEquals(p, newPop);
    assertEquals(newPop, p);
    // compare list of chromosomes
    Collections.sort(chromosomes);
    List toChromosomes = Arrays.asList(aChrom);
    Collections.sort(toChromosomes);
    assertEquals(chromosomes, toChromosomes);
    // compare populations with sorted list of chromosomes
    Population newPop2 = new Population(conf);
    newPop2.setChromosomes(toChromosomes);
    assertEquals(p, newPop2);
    assertEquals(newPop, newPop2);
    assertEquals(newPop2, p);
    assertEquals(newPop2, newPop);
  }

  /**
   * Empty population
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testDetermineFittestChromosome_0()
      throws Exception {
    Population p = new Population(conf);
    assertEquals(null, p.determineFittestChromosome());
  }

  /**
   * Unordered list of fitness values of chroms in population.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testDetermineFittestChromosome_1()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population(conf);
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(5);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(conf, g, 3);
    c.setFitnessValue(19);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(conf, g, 1);
    c.setFitnessValue(11);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(conf, g, 8);
    c.setFitnessValue(18);
    p.addChromosome(c);
    chromosomes.add(c);
    Chromosome fittest = (Chromosome) chromosomes.get(1);
    p.setChromosomes(chromosomes);
    assertEquals(fittest, p.determineFittestChromosome());
  }

  /**
   * Ordered list of fitness values of chroms in population.
   *
   * @throws Exception
   */
  public void testDetermineFittestChromosome_2()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population(conf);
    int nTot = 100;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      c.setFitnessValue(i);
      p.addChromosome(c);
      chromosomes.add(c);
    }
    Chromosome fittest = (Chromosome) chromosomes.get(99);
    p.setChromosomes(chromosomes);
    assertEquals(fittest, p.determineFittestChromosome());
  }

  /**
   * Ordered list of fitness values of chroms in population. Use fitness
   * evaluator different from standard one.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testDetermineFittestChromosome_3()
      throws Exception {
    conf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population(conf);
    int nTot = 100;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene(conf);
      c = new Chromosome(conf, g, 10);
      c.setFitnessValue(i);
      p.addChromosome(c);
      chromosomes.add(c);
    }
    Chromosome fittest = (Chromosome) chromosomes.get(0);
    p.setChromosomes(chromosomes);
    assertEquals(fittest, p.determineFittestChromosome());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosome_4()
      throws Exception {
    Population p = new Population(conf);
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    c.setFitnessValue(22);
    p.addChromosome(c);
    assertEquals(null, p.determineFittestChromosomes(0));
    assertEquals(c, p.determineFittestChromosomes(1).get(0));
  }

  /**
   * Special case exposing a previous bug in method under test.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testDetermineFittestChromosome_5()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population(conf);
    conf.reset();
    conf.setFitnessEvaluator(new MyFitnessEvaluator());
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(5);
    chromosomes.add(c);
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(3);
    chromosomes.add(c);
    p.setChromosomes(chromosomes);
    assertEquals(c, p.determineFittestChromosome());
    // Next is important to come into a dangerous situation.
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(1); //the fittest!
    p.addChromosome(c);
    assertEquals(c, p.determineFittestChromosome());
  }

  /**
   * Test that the last chromosome in the population is preserved if it is the
   * fittest one.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testDetermineFittestChromosome_2_0()
      throws Exception {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population(conf);
    conf.reset();
    // Lower fitness values are better.
    // --------------------------------
    conf.setFitnessEvaluator(new MyFitnessEvaluator());
    // Build the population.
    // ---------------------
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(5);
    chromosomes.add(c);
    g = new DoubleGene(conf);
    IChromosome c2 = new Chromosome(conf, g, 10);
    c2.setFitnessValue(7);
    chromosomes.add(c2);
    p.setChromosomes(chromosomes);
    assertEquals(c, p.determineFittestChromosome(0, p.size()));
    // Next is important to come into a dangerous situation.
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(1); //the fittest!
    p.addChromosome(c);
    assertEquals(c, p.determineFittestChromosome(0, p.size()-1));
  }

  /**
   * @throws Exception
   *
   * @author Dan Clark
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_1()
      throws Exception {
    Population p = getNewPopulation(conf);
    assertEquals(1, p.determineFittestChromosomes(1).size());
    assertEquals(1, p.determineFittestChromosomes(1).size());
    assertEquals(3, p.determineFittestChromosomes(3).size());
    assertEquals(3, p.determineFittestChromosomes(3).size());
    // Expect result list with 5 entries (not 6) as population consists
    // of 5 entries. Even if more are requested, the maximum returned is 5
    // in this case.
    assertEquals(5, p.determineFittestChromosomes(6).size());
  }

  /**
   * Exposes bug 1422962.
   *
   * @throws Exception
   *
   * @author Dan Clark
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_2()
      throws Exception {
    Population population = getNewPopulation(conf);
    IChromosome topC = population.determineFittestChromosome();
    List top = population.determineFittestChromosomes(1);
    assertEquals(topC, population.determineFittestChromosome());
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(2);
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(3);
    assertEquals(top.get(0), population.determineFittestChromosome());
  }

  /**
   * Exposes bug 1422962.
   *
   * @throws Exception
   *
   * @author Dan Clark
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_3()
      throws Exception {
    Population population = getNewPopulation(conf);
    assertTrue(population.isChanged());
    List top = population.determineFittestChromosomes(1);
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(2);
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(3);
    assertEquals(top.get(0), population.determineFittestChromosome());
    population = getNewPopulation(conf);
    assertEquals(24, population.determineFittestChromosome().getFitnessValue(),
                 DELTA);
    top = population.determineFittestChromosomes(5);
    assertFalse(population.isChanged());
    assertEquals(24, ( (IChromosome) top.get(0)).getFitnessValue(), DELTA);
    assertEquals(23, ( (IChromosome) top.get(1)).getFitnessValue(), DELTA);
    assertEquals(23, ( (IChromosome) top.get(2)).getFitnessValue(), DELTA);
    assertEquals(23, ( (IChromosome) top.get(3)).getFitnessValue(), DELTA);
    assertEquals(22, ( (IChromosome) top.get(4)).getFitnessValue(), DELTA);
    double oldFitness = population.getChromosome(0).getFitnessValue();
    for (int i = 1; i < population.size(); i++) {
      double currFitness = population.getChromosome(i).getFitnessValue();
      assertTrue(currFitness <= oldFitness);
      oldFitness = currFitness;
    }
  }

  private static Population getNewPopulation(Configuration a_conf)
      throws InvalidConfigurationException {
    Population population = new Population(a_conf);
    Gene g = new DoubleGene(a_conf);
    Chromosome c = new Chromosome(a_conf, g, 10);
    c.setFitnessValue(22);
    population.addChromosome(c);
    c = new Chromosome(a_conf, g, 10);
    c.setFitnessValue(24);
    population.addChromosome(c);
    c = new Chromosome(a_conf, g, 10);
    c.setFitnessValue(23);
    population.addChromosome(c);
    population.addChromosome(c);
    population.addChromosome(c);
    return population;
  }

  public void testSize_0()
      throws Exception {
    Population p = new Population(conf, 10);
    // size only counts number of "real" chromosomes not placeholders
    assertEquals(0, p.size());
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 5);
    p.addChromosome(c);
    assertEquals(1, p.size());
    c = new Chromosome(conf, g, 3);
    p.addChromosome(c);
    assertEquals(2, p.size());
  }

  public void testIterator_0()
      throws Exception {
    Population p = new Population(conf, 10);
    Iterator it = p.iterator();
    assertFalse(it.hasNext());
    // size only counts number of "real" chromosomes not placeholders
    assertEquals(0, p.size());
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 5);
    p.addChromosome(c);
    assertTrue(it.hasNext());
  }

  public void testContains_0()
      throws Exception {
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    c.setFitnessValue(45);
    Population p1 = new Population(conf);
    assertFalse(p1.contains(c));
    assertFalse(p1.contains(null));
    p1.addChromosome(c);
    assertTrue(p1.contains(c));
    assertFalse(p1.contains(null));
    assertFalse(p1.contains(new Chromosome(conf, g, 5)));
  }

  /**
   * Single chromosome.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_0()
      throws Exception {
    Population pop = new Population(conf);
    Gene g1 = new DoubleGene(conf);
    Gene g2 = new StringGene(conf);
    Chromosome c1 = new Chromosome(conf, new Gene[] {
                                   g1, g2
    });
    pop.addChromosome(c1);
    List genes = pop.getGenome(true);
    assertEquals(2, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
    genes = pop.getGenome(false);
    assertEquals(2, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
  }

  /**
   * Two chromosomes.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_1()
      throws Exception {
    Population pop = new Population(conf);
    assertTrue(pop.isChanged());
    Gene g1 = new DoubleGene(conf);
    Gene g2 = new StringGene(conf);
    Chromosome c1 = new Chromosome(conf, new Gene[] {g1, g2});
    pop.addChromosome(c1);
    Gene g3 = new BooleanGene(conf);
    Gene g4 = new IntegerGene(conf, 0, 10);
    Gene g5 = new FixedBinaryGene(conf, 4);
    Chromosome c2 = new Chromosome(conf, new Gene[] {g3, g4, g5});
    pop.addChromosome(c2);
    List genes = pop.getGenome(true);
    assertEquals(5, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
    assertEquals(g3, genes.get(2));
    assertEquals(g4, genes.get(3));
    assertEquals(g5, genes.get(4));
    genes = pop.getGenome(false);
    assertEquals(5, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
    assertEquals(g3, genes.get(2));
    assertEquals(g4, genes.get(3));
    assertEquals(g5, genes.get(4));
  }

  /**
   * Using CompositeGene.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_2()
      throws Exception {
    Population pop = new Population(conf);
    Gene g1 = new DoubleGene(conf);
    Gene g2 = new StringGene(conf);
    Chromosome c1 = new Chromosome(conf, new Gene[] {g1, g2});
    pop.addChromosome(c1);
    Gene g3 = new BooleanGene(conf);
    CompositeGene g4 = new CompositeGene(conf);
    Gene g5 = new FixedBinaryGene(conf, 4);
    g4.addGene(g5);
    Gene g6 = new DoubleGene(conf, 1.0d, 4.0d);
    g4.addGene(g6);
    Chromosome c2 = new Chromosome(conf, new Gene[] {g3, g4});
    pop.addChromosome(c2);
    assertTrue(pop.isChanged());
    // resolve CompositeGene with the following call
    List genes = pop.getGenome(true);
    assertEquals(5, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
    assertEquals(g3, genes.get(2));
    assertEquals(g5, genes.get(3));
    assertEquals(g6, genes.get(4));
    // don't resolve CompositeGene with the following call
    genes = pop.getGenome(false);
    assertEquals(4, genes.size());
    assertEquals(g1, genes.get(0));
    assertEquals(g2, genes.get(1));
    assertEquals(g3, genes.get(2));
    assertEquals(g4, genes.get(3));
  }

  /**
   * Ensures that the Population class is implementing the Serializable
   * interface.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testIsSerializable_0()
      throws Exception {
    assertTrue(isSerializable(new Population(conf)));
  }

  /**
   * Ensures that Population and all objects contained implement Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct genotype to be serialized
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)
    });
    Population pop = new Population(conf, chroms);
    assertEquals(pop, doSerialize(pop));
    assertTrue(pop.isChanged());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_0()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Population pop = new Population(conf, chroms);
    assertEquals(chroms[0], pop.removeChromosome(0));
    assertEquals(0, pop.size());
    try {
      pop.removeChromosome(0);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * With illegal index.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_1()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Population pop = new Population(conf, chroms);
    try {
      pop.removeChromosome(1);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * With illegal index.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_2()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Population pop = new Population(conf, chroms);
    try {
      pop.removeChromosome( -1);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_3()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Population pop = new Population(conf, chroms);
    IChromosome c = pop.removeChromosome(0);
    assertEquals(chroms[0], c);
    assertTrue(pop.isChanged());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSortByFitness_0()
      throws Exception {
    IChromosome[] chroms = new Chromosome[3];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new DoubleGene(conf, 1, 5)});
    chroms[0].setFitnessValueDirectly(45d);
    chroms[1] = new Chromosome(conf, new Gene[] {
                               new DoubleGene(conf, 1, 5)});
    chroms[1].setFitnessValueDirectly(41d);
    chroms[2] = new Chromosome(conf, new Gene[] {
                               new DoubleGene(conf, 1, 5)});
    chroms[2].setFitnessValueDirectly(47d);
    Population pop = new Population(conf, chroms);
    assertTrue(pop.isChanged());
    assertFalse(pop.isSorted());
    pop.sortByFitness();
    assertFalse(pop.isChanged());
    assertTrue(pop.isSorted());
    double oldFitness = pop.getChromosome(0).getFitnessValue();
    for (int i = 1; i < pop.size(); i++) {
      double currFitness = pop.getChromosome(i).getFitnessValue();
      assertTrue(currFitness <= oldFitness);
      oldFitness = currFitness;
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSortByFitness_1()
      throws Exception {
    Population pop = new Population(conf);
    Gene g = new DoubleGene(conf);
    Chromosome c = new Chromosome(conf, g, 10);
    c.setFitnessValue(4.5d);
    pop.addChromosome(c);
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(4.1d);
    pop.addChromosome(c);
    g = new DoubleGene(conf);
    c = new Chromosome(conf, g, 10);
    c.setFitnessValue(4.7d);
    pop.addChromosome(c);
    assertTrue(pop.isChanged());
    assertFalse(pop.isSorted());
    pop.sortByFitness();
    assertFalse(pop.isChanged());
    assertTrue(pop.isSorted());
    double oldFitness = pop.getChromosome(0).getFitnessValue();
    for (int i = 1; i < pop.size(); i++) {
      double currFitness = pop.getChromosome(i).getFitnessValue();
      assertTrue(currFitness <= oldFitness);
      oldFitness = currFitness;
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_0()
      throws Exception {
    Population pop = new Population(conf);
    assertEquals(1, pop.compareTo(null));
    Population pop2 = new Population(conf);
    assertEquals(0, pop.compareTo(pop2));
    pop2.addChromosome(new Chromosome(conf));
    assertEquals( -1, pop.compareTo(pop2));
    assertEquals(1, pop2.compareTo(pop));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_0()
      throws Exception {
    Gene g = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, g, 29);
    c.setFitnessValue(45);
    Population p = new Population(conf);
    p.addChromosome(c);
    String repr = p.getPersistentRepresentation();
    Population q = new Population(conf);
    q.setValueFromPersistentRepresentation(repr);
    assertEquals(p, q);
    assertEquals(p.getPersistentRepresentation(), q.getPersistentRepresentation());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_1()
      throws Exception {
    Gene[] genes1 = new Gene[2];
    genes1[0] = new StringGene(conf);
    genes1[1] = new DoubleGene(conf);
    Chromosome chrom = new Chromosome(conf, genes1);
    Population pop = new Population(conf, chrom);
    String repr = pop.getPersistentRepresentation();
    Population q = new Population(conf);
    q.setValueFromPersistentRepresentation(repr);
    assertEquals(pop, q);
    assertEquals(pop.getPersistentRepresentation(),
                 q.getPersistentRepresentation());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_2()
      throws Exception {
    Population pop = new Population(conf);
    pop.setValueFromPersistentRepresentation(null);
    assertEquals(0, pop.size());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_3()
      throws Exception {
    Population pop = new Population(conf);
    try {
      pop.setValueFromPersistentRepresentation("1"
          + Population.CHROM_DELIMITER
          + "2");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_4()
      throws Exception {
    Population pop = new Population(conf);
    try {
      pop.setValueFromPersistentRepresentation("1"
          + Population.CHROM_DELIMITER
          + "0"
          + Population.CHROM_DELIMITER);
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Invalid closing tag.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_5()
      throws Exception {
    Population pop = new Population(conf);
    pop.setValueFromPersistentRepresentation(Population.CHROM_DELIMITER_HEADING
        + Chromosome.class.getName()
        + Population.CHROM_DELIMITER
        + "47.11"
        + Chromosome.CHROM_DELIMITER
        + "1"
        + Chromosome.CHROM_DELIMITER
        + "<" + IntegerGene.class.getName()
        + Chromosome.GENE_DELIMITER
        + "2:4:4>"
        + Population.CHROM_DELIMITER_CLOSING);
    assertEquals(1, pop.size());
    IChromosome chrom = pop.getChromosome(0);
    assertEquals(1, chrom.size());
  }

  /**
   * Invalid closing tag.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_6()
      throws Exception {
    Population pop = new Population(conf);
    try {
      pop.setValueFromPersistentRepresentation(Population.
          CHROM_DELIMITER_HEADING
          + Chromosome.class.getName()
          + Population.CHROM_DELIMITER
          + "47.11"
          + Chromosome.CHROM_DELIMITER
          + "1"
          + Chromosome.CHROM_DELIMITER
          + "<" + IntegerGene.class.getName()
          + Chromosome.GENE_DELIMITER
          + "2:4:4>/");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  class MyFitnessEvaluator
      implements FitnessEvaluator {
    public boolean isFitter(final double a_fitness_value1,
                            final double a_fitness_value2) {
      return a_fitness_value1 < a_fitness_value2;
    }

    public boolean isFitter(IChromosome a_chrom1, IChromosome a_chrom2) {
      return isFitter(a_chrom1.getFitnessValue(), a_chrom2.getFitnessValue());
    }
  }
}
