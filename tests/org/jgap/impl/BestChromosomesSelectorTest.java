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
import junitx.util.*;

/**
 * Tests for BestChromosomesSelector class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BestChromosomesSelectorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  public BestChromosomesSelectorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(BestChromosomesSelectorTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Boolean needsSorting = (Boolean) PrivateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    assertTrue(selector.returnsUniqueChromosomes());
    assertFalse(selector.getDoubletteChromosomesAllowed());
  }


  public void testDoubletteChromosomesAllowed_0() throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(false);
    assertFalse(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
    selector.setDoubletteChromosomesAllowed(true);
    assertTrue(selector.getDoubletteChromosomesAllowed());
  }

  public void testAdd_0()
      throws Exception {
    BestChromosomesSelector selector = new BestChromosomesSelector();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    selector.add(chrom);
    Boolean needsSorting = (Boolean) PrivateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.TRUE, needsSorting);
    List chromosomes = ( (Population) PrivateAccessor.getField(selector,
        "m_chromosomes")).getChromosomes();
    assertEquals(1, chromosomes.size());
    assertEquals(chrom, chromosomes.get(0));
    selector.add(chrom);
    assertEquals(chrom, chromosomes.get(0));
    // if BestChromosomesSelector adds non-unique chroms, then we have a count
    // of two then after the add(..), else a count of 1
    selector.setDoubletteChromosomesAllowed(false);
    assertEquals(1, chromosomes.size());
    selector.setDoubletteChromosomesAllowed(true);
    selector.add(chrom);
    assertEquals(2, chromosomes.size());
  }

  /**
   * Test if below functionality available without error
   * @throws Exception
   */
  public void testSelect_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    BestChromosomesSelectorForTest selector = new BestChromosomesSelectorForTest();
    Gene gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    selector.select(1,null,new Population());
  }

  public void testSelect_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // add third chromosome
    // ---------------------
    gene = new IntegerGene();
    gene.setAllele(new Integer(444));
    Chromosome secondBestChrom = new Chromosome(gene, 3);
    secondBestChrom.setFitnessValue(11);
    selector.add(secondBestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population();
    selector.select(1, null, pop);
    Chromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    selector.setOriginalRate(1.0d);
    // receive top 3 chromosomes
    // -------------------------
    pop.getChromosomes().clear();
    selector.select(3, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(3, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(secondBestChrom, bestChroms[1]);
    assertEquals(thirdBestChrom, bestChroms[2]);
  }

  public void testSelect_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    // receive top 1 (= best) chromosome
    // ---------------------------------
    Population pop = new Population();
    selector.select(1, null,pop);
    Chromosome[] bestChroms = pop.toChromosomes();
    assertEquals(1, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    selector.setOriginalRate(1.0d);
    // receive top 30 chromosomes (select-method should take into account only
    // 2 chroms!)
    // ----------------------------------
    pop.getChromosomes().clear();
    selector.select(30, null, pop);
    bestChroms = pop.toChromosomes();
    assertEquals(2, bestChroms.length);
    assertEquals(bestChrom, bestChroms[0]);
    assertEquals(thirdBestChrom, bestChroms[1]);
    assertTrue(bestChrom == bestChroms[0]);
  }

  /**
   * Ensure that selected Chromosome's are not equal to added Chromosome's.
   * @throws Exception
   */
  public void testSelect_3()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    BestChromosomesSelector selector = new BestChromosomesSelector();
    // add first chromosome
    // --------------------
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    Chromosome thirdBestChrom = new Chromosome(gene, 7);
    thirdBestChrom.setFitnessValue(10);
    selector.add(thirdBestChrom);
    // add second chromosome
    // ---------------------
    gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    Chromosome bestChrom = new Chromosome(gene, 3);
    bestChrom.setFitnessValue(12);
    selector.add(bestChrom);
    selector.setOriginalRate(1.0d);
    // receive top 30 chromosomes (select-method should take into account only
    // 2 chroms!)
    // ----------------------------------
    Population pop = new Population();
    selector.select(30,null,pop);
    Population bestChroms = pop;
    Population chromosomes = (Population) PrivateAccessor.getField(selector, "m_chromosomes");
    assertFalse(bestChroms.equals(chromosomes));
  }

  public void testEmpty_0()
      throws Exception {
    BestChromosomesSelectorForTest selector = new BestChromosomesSelectorForTest();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    selector.add(chrom);
    selector.empty();
    Boolean needsSorting = (Boolean) PrivateAccessor.getField(selector,
        "m_needsSorting");
    assertEquals(Boolean.FALSE, needsSorting);
    List chromosomes = ( (Population) PrivateAccessor.getField(selector,
        "m_chromosomes")).getChromosomes();
    assertEquals(0, chromosomes.size());
  }

  /**
   * Test if clear()-method does not affect original Population
   * @throws Exception
   */
  public void testEmpty_1()
      throws Exception {
    BestChromosomesSelectorForTest selector = new BestChromosomesSelectorForTest();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    selector.empty();
    assertEquals(1, popNew.size());
    assertNotNull(popNew.getChromosome(0));
  }

  /**
   * Test if clear()-method does not affect return value
   * @throws Exception
   */
  public void testEmpty_2()
      throws Exception {
    BestChromosomesSelectorForTest selector = new BestChromosomesSelectorForTest();
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    Gene gene = new BooleanGene();
    Chromosome chrom = new Chromosome(gene, 5);
    Population pop = new Population(1);
    pop.addChromosome(chrom);
    selector.add(chrom);
    Population popNew = new Population();
    selector.select(1, null, popNew);
    pop = popNew;
    selector.empty();
    assertEquals(1, pop.size());
    assertNotNull(pop.getChromosome(0));
  }

}

class BestChromosomesSelectorForTest extends BestChromosomesSelector {
  public void add(Chromosome toAdd) {
    super.add(toAdd);
  }
}
