/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the Population class
 *
 * @author Klaus Meffert
 * @author Chris Knowles
 * @since 2.0
 */
public class PopulationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.30 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(PopulationTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try {
      new Population(null);
      fail();
    }
    catch (NullPointerException e) {
      ; //this is OK
    }
  }

  public void testConstruct_1() {
    try {
      new Population( -1);
    }
    catch (IllegalArgumentException iae) {
      ; //this is ok
    }
  }

  public void testConstruct_3() {
    int nTot = 100;
    Chromosome[] chromosomes = new Chromosome[nTot];
    Population pop = new Population(chromosomes);
    assertNotNull(pop);
    //check size is correct
    assertEquals(nTot, pop.size());
  }

  public void testAddChromosome_0() {
    Gene g = new IntegerGene();
    Chromosome c = new Chromosome(g, 29);
    c.setFitnessValue(45);
    Population p = new Population();
    p.addChromosome(c);
    assertEquals(1, p.size());
  }

  public void testAddChromosome_1() {
    Population p = new Population();
    p.addChromosome(null);
    assertEquals(0, p.size());
  }

  public void testAddChromosomes_0() {
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    c.setFitnessValue(45);
    Population p1 = new Population();
    p1.addChromosome(c);
    Population p2 = new Population();
    p2.addChromosomes(p1);
    assertEquals(p1.size(), p2.size());
  }

  public void testAddChromosomes_1() {
    Population p = new Population();
    p.addChromosomes(null);
    assertEquals(0, p.size());
  }

  public void testSetChromosome_0() {
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    Population p = new Population();
    p.setChromosome(0, c);
    p.setChromosome(0, c);
    assertEquals(1, p.size());
    try {
      p.setChromosome(2, c);
      fail();
    }
    catch (IndexOutOfBoundsException oex) {
      ; //this is OK
    }
  }

  public void testSetChromosome_1() {
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    Population p = new Population();
    p.addChromosome(c);
    Chromosome c2 = new Chromosome(g, 20);
    p.setChromosome(0, c2);
    assertEquals(1, p.size());
    assertEquals(p.getChromosome(0), c2);
    assertFalse(c.equals(c2));
  }

  public void testSetChromosomes_0() {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
      chromosomes.add(c);
    }
    Population p = new Population();
    p.setChromosomes(chromosomes);
    assertEquals(nTot, p.size());
  }

  public void testGetChromosomes_0() {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
      chromosomes.add(c);
    }
    Population p = new Population();
    p.setChromosomes(chromosomes);
    assertEquals(chromosomes, p.getChromosomes());
  }

  public void testGetChromosome_0() {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Chromosome thechosenone = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
      chromosomes.add(c);
      if (i == 100) {
        thechosenone = c;
      }
    }
    Population p = new Population();
    p.setChromosomes(chromosomes);
    assertEquals(thechosenone, p.getChromosome(100));
  }

  public void testToChromosomes_0() {
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    int nTot = 200;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
      chromosomes.add(c);
    }
    Population p = new Population();
    p.setChromosomes(chromosomes);
    IChromosome[] aChrom = p.toChromosomes();
    assertEquals(aChrom.length, chromosomes.size());
    for (int i = 0; i < nTot; i++) {
      assertTrue(chromosomes.contains(aChrom[i]));
    }
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
    Genotype.setConfiguration(new DefaultConfiguration());
    Population p = new Population();
    assertEquals(null, p.determineFittestChromosome());
  }

  /**
   * Unordered list of fitness values of chroms in population
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testDetermineFittestChromosome_1()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population();
    g = new DoubleGene();
    c = new Chromosome(g, 10);
    c.setFitnessValue(5);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(g, 3);
    c.setFitnessValue(19);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(g, 1);
    c.setFitnessValue(11);
    p.addChromosome(c);
    chromosomes.add(c);
    c = new Chromosome(g, 8);
    c.setFitnessValue(18);
    p.addChromosome(c);
    chromosomes.add(c);
    Chromosome fittest = (Chromosome) chromosomes.get(1);
    p.setChromosomes(chromosomes);
    assertEquals(fittest, p.determineFittestChromosome());
  }

  /**
   * Ordered list of fitness values of chroms in population
   * @throws Exception
   */
  public void testDetermineFittestChromosome_2()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population();
    int nTot = 100;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
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
   * evaluator different from standard one
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testDetermineFittestChromosome_3()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    Genotype.getConfiguration().setFitnessEvaluator(
        new DeltaFitnessEvaluator());
    List chromosomes = new ArrayList();
    Gene g = null;
    Chromosome c = null;
    Population p = new Population();
    int nTot = 100;
    for (int i = 0; i < nTot; i++) {
      g = new DoubleGene();
      c = new Chromosome(g, 10);
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
    Genotype.setConfiguration(new DefaultConfiguration());
    Population p = new Population();
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    c.setFitnessValue(22);
    p.addChromosome(c);
    assertEquals(null, p.determineFittestChromosomes(0));
    assertEquals(c, p.determineFittestChromosomes(1).get(0));
  }

  /**
   * @throws Exception
   *
   * @author Dan Clark,Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_1()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    Population p = getNewPopulation();
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
   * Exposes bug 1422962
   * @throws Exception
   *
   * @author Dan Clark, Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_2()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    Population population = getNewPopulation();
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
   * Exposes bug 1422962
   * @throws Exception
   *
   * @author Dan Clark, Klaus Meffert
   * @since 2.6
   */
  public void testDetermineFittestChromosomes_3()
      throws Exception {
    Genotype.setConfiguration(new DefaultConfiguration());
    Population population = getNewPopulation();
    assertTrue( population.isChanged());
    List top = population.determineFittestChromosomes(1);
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(2);
    assertEquals(top.get(0), population.determineFittestChromosome());
    top = population.determineFittestChromosomes(3);
    assertEquals(top.get(0), population.determineFittestChromosome());

    population = getNewPopulation();
    assertEquals(24, population.determineFittestChromosome().getFitnessValue(),
                 DELTA);
    top = population.determineFittestChromosomes(5);
    assertFalse( population.isChanged());
    assertEquals(24, ((IChromosome)top.get(0)).getFitnessValue(), DELTA);
    assertEquals(23, ((IChromosome)top.get(1)).getFitnessValue(), DELTA);
    assertEquals(23, ((IChromosome)top.get(2)).getFitnessValue(), DELTA);
    assertEquals(23, ((IChromosome)top.get(3)).getFitnessValue(), DELTA);
    assertEquals(22, ((IChromosome)top.get(4)).getFitnessValue(), DELTA);
    double oldFitness = population.getChromosome(0).getFitnessValue();
    for (int i = 1; i < population.size(); i++) {
      double currFitness = population.getChromosome(i).getFitnessValue();
      assertTrue(currFitness <= oldFitness);
      oldFitness = currFitness;
    }
  }

  private static Population getNewPopulation()
      throws InvalidConfigurationException {
    Population population = new Population();
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    c.setFitnessValue(22);
    population.addChromosome(c);
    c = new Chromosome(g, 10);
    c.setFitnessValue(24);
    population.addChromosome(c);
    c = new Chromosome(g, 10);
    c.setFitnessValue(23);
    population.addChromosome(c);
    population.addChromosome(c);
    population.addChromosome(c);
    return population;
  }

  public void testSize_0() {
    Population p = new Population(10);
    // size only counts number of "real" chromosomes not placeholders
    assertEquals(0, p.size());
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 5);
    p.addChromosome(c);
    assertEquals(1, p.size());
    c = new Chromosome(g, 3);
    p.addChromosome(c);
    assertEquals(2, p.size());
  }

  public void testIterator_0() {
    Population p = new Population(10);
    Iterator it = p.iterator();
    assertFalse(it.hasNext());
    // size only counts number of "real" chromosomes not placeholders
    assertEquals(0, p.size());
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 5);
    p.addChromosome(c);
    assertTrue(it.hasNext());
  }

  public void testContains_0() {
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    c.setFitnessValue(45);
    Population p1 = new Population();
    assertFalse(p1.contains(c));
    assertFalse(p1.contains(null));
    p1.addChromosome(c);
    assertTrue(p1.contains(c));
    assertFalse(p1.contains(null));
    assertFalse(p1.contains(new Chromosome(g, 5)));
  }

  /**
   * Single chromosome
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_0() {
    Population pop = new Population();
    Gene g1 = new DoubleGene();
    Gene g2 = new StringGene();
    Chromosome c1 = new Chromosome(new Gene[] {g1, g2});
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
   * Two chromosomes
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_1() {
    Population pop = new Population();
    assertTrue( pop.isChanged());
    Gene g1 = new DoubleGene();
    Gene g2 = new StringGene();
    Chromosome c1 = new Chromosome(new Gene[] {g1, g2});
    pop.addChromosome(c1);
    Gene g3 = new BooleanGene();
    Gene g4 = new IntegerGene(0, 10);
    Gene g5 = new FixedBinaryGene(4);
    Chromosome c2 = new Chromosome(new Gene[] {g3, g4, g5});
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
   * CompositeGene
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testGetGenome_2() {
    Population pop = new Population();
    Gene g1 = new DoubleGene();
    Gene g2 = new StringGene();
    Chromosome c1 = new Chromosome(new Gene[] {g1, g2});
    pop.addChromosome(c1);
    Gene g3 = new BooleanGene();
    CompositeGene g4 = new CompositeGene();
    Gene g5 = new FixedBinaryGene(4);
    g4.addGene(g5);
    Gene g6 = new DoubleGene(1.0d, 4.0d);
    g4.addGene(g6);
    Chromosome c2 = new Chromosome(new Gene[] {g3, g4});
    pop.addChromosome(c2);
    assertTrue( pop.isChanged());
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
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testIsSerializable_0() {
    assertTrue(super.isSerializable(new Population()));
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
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Population pop = new Population(chroms);
    assertEquals(pop, super.doSerialize(pop));
    assertTrue( pop.isChanged());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_0() {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Population pop = new Population(chroms);
    assertEquals(chroms[0], pop.removeChromosome(0));
    assertEquals(0, pop.size());
    try {
      pop.removeChromosome(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * With illegal index.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_1() {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Population pop = new Population(chroms);
    try {
      pop.removeChromosome(1);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * With illegal index.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testRemoveChromosome_2() {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Population pop = new Population(chroms);
    try {
      pop.removeChromosome( -1);
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
   * @since 2.4
   */
  public void testRemoveChromosome_3()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Population pop = new Population(chroms);
    IChromosome c = pop.removeChromosome(0);
    assertEquals(chroms[0], c);
    assertTrue( pop.isChanged());
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSortByFitness_0() {
    IChromosome[] chroms = new Chromosome[3];
    chroms[0] = new Chromosome(new Gene[] {
                               new DoubleGene(1, 5)});
    chroms[0].setFitnessValueDirectly(45d);
    chroms[1] = new Chromosome(new Gene[] {
                               new DoubleGene(1, 5)});
    chroms[1].setFitnessValueDirectly(41d);
    chroms[2] = new Chromosome(new Gene[] {
                               new DoubleGene(1, 5)});
    chroms[2].setFitnessValueDirectly(47d);
    Population pop = new Population(chroms);
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
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSortByFitness_1() {
    Population pop = new Population();
    Gene g = new DoubleGene();
    Chromosome c = new Chromosome(g, 10);
    c.setFitnessValue(4.5d);
    pop.addChromosome(c);
    g = new DoubleGene();
    c = new Chromosome(g, 10);
    c.setFitnessValue(4.1d);
    pop.addChromosome(c);
    g = new DoubleGene();
    c = new Chromosome(g, 10);
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
}
