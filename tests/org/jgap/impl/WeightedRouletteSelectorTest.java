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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jgap.*;
import java.util.*;
import junitx.util.PrivateAccessor;

/**
 * Tests for WeightedRouletteSelector class
 *
 * @since 1.1
 * @author Klaus Meffert
 */
public class WeightedRouletteSelectorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";
  public WeightedRouletteSelectorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(WeightedRouletteSelectorTest.class);
    return suite;
  }

  /**
   * Test if construction possible without failure
   */
  public void testConstruct_0() {
    new WeightedRouletteSelector();
  }

  public void testAdd_0() throws Exception {
    NaturalSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    chrom.setActiveConfiguration(conf);
    selector.add(conf, chrom);
    Map chromosomes = (Map) PrivateAccessor.getField(selector,
        "m_wheel");
    assertEquals(1, chromosomes.size());
    Iterator it = chromosomes.keySet().iterator();
    assertEquals(chrom, it.next());
    selector.add(null, chrom);
    assertEquals(1, chromosomes.size());
    it = chromosomes.keySet().iterator();
    assertEquals(chrom, it.next());
  }

  public void testSelect_0() throws Exception {
    NaturalSelector selector = new WeightedRouletteSelector();
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(null, thirdBestChrom);
    try {
      Chromosome[] bestChroms = selector.select(null, 1);
      fail();
    }
    catch (NullPointerException nex) {
      ;//this is OK
    }
  }

  public void testSelect_1() throws Exception {
    NaturalSelector selector = new WeightedRouletteSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 4);
    thirdBestChrom.setFitnessValue(10);
    selector.add(null, thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new DoubleGene();
    gene.setAllele(new Double(2.3d));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(null, bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 2);
    secondBestChrom.setFitnessValue(11);
    selector.add(null, secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTest randgen = new RandomGeneratorForTest();
    randgen.setNextDouble(0.9d);
    conf.setRandomGenerator(randgen);
    Chromosome[] bestChroms = selector.select(conf, 1);
    assertEquals(1, bestChroms.length);
    assertEquals(thirdBestChrom, bestChroms[0]);
    // now select top 4 chromosomes (should only select 3!)
    // ------------------------------
    bestChroms = selector.select(conf, 4);
    assertEquals(3, bestChroms.length);
  }

  public void testEmpty_0() throws Exception {
    NaturalSelector selector = new WeightedRouletteSelector();
    Configuration conf = new DefaultConfiguration();
    conf.setPopulationSize(7);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    conf.setSampleChromosome(chrom);
    chrom.setActiveConfiguration(conf);
    selector.add(conf, chrom);
    selector.empty();
    Map chromosomes = (Map) PrivateAccessor.getField(selector,
        "m_wheel");
    assertEquals(0, chromosomes.size());
  }
}

