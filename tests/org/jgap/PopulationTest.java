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
 * Tests for Population class
 *
 * @author Klaus Meffert
 * @author Chris Knowles
 * @since 2.0
 */
public class PopulationTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public PopulationTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(PopulationTest.class);
    return suite;
  }

  public void testConstruct_0() {
      try
      {
        new Population(null);
        fail();
      }
      catch (NullPointerException e)
      {
        ;//this is OK
      }
  }

  public void testConstruct_1() {
      try
      {
        new Population(-1);
      }
      catch (IllegalArgumentException iae)
      {
        ;//this is ok
      }
  }

  public void testConstruct_2() {
      Population pop = new Population();
      assertNotNull(pop);
  }

  public void testConstruct_3() {
      int nTot = 100;
      Chromosome[] chromosomes = new Chromosome[nTot];
      Population pop = new Population(chromosomes);
      assertNotNull(pop);

      //check size is correct
      assertEquals(nTot, pop.size());
  }

  public void testAddChromosome_0(){
      Gene g = new IntegerGene();
      Chromosome c = new Chromosome(g, 29);
      c.setFitnessValue(45);
      Population p = new Population();
      p.addChromosome(c);
      assertEquals(1,p.size());
  }

  public void testAddChromosome_1(){
      Population p = new Population();
      p.addChromosome(null);
      assertEquals(0,p.size());
  }

  public void testAddChromosomes_0(){
      Gene g = new DoubleGene();
      Chromosome c = new Chromosome(g,10);
      c.setFitnessValue(45);

      Population p1 = new Population();
      p1.addChromosome(c);

      Population p2 = new Population();
      p2.addChromosomes(p1);
      assertEquals(p1.size(), p2.size());
  }

  public void testAddChromosomes_1(){
      Population p = new Population();
      p.addChromosomes(null);
      assertEquals(0,p.size());
  }

  public void testSetChromosome_0(){
      Gene g = new DoubleGene();
      Chromosome c = new Chromosome(g,10);

      Population p = new Population();
      try {
        p.setChromosome(0, c);
        fail();
      }catch (IndexOutOfBoundsException oex) {
        ;//this is OK
      }
  }

  public void testSetChromosome_1(){
      Gene g = new DoubleGene();
      Chromosome c = new Chromosome(g,10);

      Population p = new Population();
      p.addChromosome(c);
      Chromosome c2 = new Chromosome(g,20);
      p.setChromosome(0, c2);
      assertEquals(1, p.size());
      assertEquals(p.getChromosome(0),c2);
      assertFalse(c.equals(c2));
  }

  public void testSetChromosomes_0(){
      List chromosomes = new ArrayList();
      Gene g = null;
      Chromosome c = null;
      int nTot = 200;
      for(int i =0;i<nTot;i++){
        g = new DoubleGene();
        c = new Chromosome(g,10);
        chromosomes.add(c);
      }

      Population p = new Population();
      p.setChromosomes(chromosomes);
      assertEquals(nTot, p.size());
  }

  public void testGetChromosomes_0(){
      List chromosomes = new ArrayList();
      Gene g = null;
      Chromosome c = null;
      int nTot = 200;
      for(int i =0;i<nTot;i++){
        g = new DoubleGene();
        c = new Chromosome(g,10);
        chromosomes.add(c);
      }

      Population p = new Population();
      p.setChromosomes(chromosomes);
      assertEquals(chromosomes, p.getChromosomes());
  }

  public void testGetChromosome_0(){
      List chromosomes = new ArrayList();
      Gene g = null;
      Chromosome c = null;
      Chromosome thechosenone=null;
      int nTot = 200;
      for(int i =0;i<nTot;i++){
        g = new DoubleGene();
        c = new Chromosome(g,10);
        chromosomes.add(c);
        if (i==100){
            thechosenone = c;
        }
      }

      Population p = new Population();
      p.setChromosomes(chromosomes);
      assertEquals(thechosenone, p.getChromosome(100));
  }

  public void testToChromosomes_0(){
      List chromosomes = new ArrayList();
      Gene g = null;
      Chromosome c = null;

      int nTot = 200;
      for(int i =0;i<nTot;i++){
        g = new DoubleGene();
        c = new Chromosome(g,10);
        chromosomes.add(c);
      }

      Population p = new Population();
      p.setChromosomes(chromosomes);

      Chromosome[] aChromosome = p.toChromosomes();
      assertEquals(aChromosome.length, chromosomes.size());
      for(int i=0;i<nTot;i++){
          assertTrue(chromosomes.contains(aChromosome[i]));
      }
  }

  public void testDetermineFittestChromosome_0() throws Exception {
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
    assertEquals(p.determineFittestChromosome(), fittest);
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
    Chromosome c = new Chromosome(g,10);
    c.setFitnessValue(45);

    Population p1 = new Population();
    assertFalse(p1.contains(c));
    assertFalse(p1.contains(null));
    p1.addChromosome(c);
    assertTrue(p1.contains(c));
    assertFalse(p1.contains(null));
    assertFalse(p1.contains(new Chromosome(g,5)));
  }
}
