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
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
      this.assertNotNull(pop);
  }

  public void testConstruct_3() {
      int nTot = 100;
      Chromosome[] chromosomes = new Chromosome[nTot];
      Population pop = new Population(chromosomes);
      assertNotNull(pop);

      //check size is correct
      assertEquals(pop.size(), nTot);
  }

  public void testAddChromosome_0(){
      Gene g = new IntegerGene();
      Chromosome c = new Chromosome(g, 29);
      c.setFitnessValue(45);
      Population p = new Population();
      p.addChromosome(c);
      assertEquals(p.size(), 1);
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
      this.assertEquals(nTot, p.size());
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
      this.assertEquals(chromosomes, p.getChromosomes());
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
      this.assertEquals(thechosenone, p.getChromosome(100));
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

}

