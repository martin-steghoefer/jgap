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

import org.jgap.*;

import junit.framework.*;

/**
 * Tests for ChromosomePool class
 *
 * @since 2.0
 * @author Klaus Meffert
 */
public class ChromosomePoolTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ChromosomePoolTest.class);
    return suite;
  }

  /**
   * Test if construction possible without failure
   */
  public void testConstruct_0() {
    new ChromosomePool();
  }

  public void testAquireChromosome_0() {
    assertEquals(null, new ChromosomePool().acquireChromosome());
  }

  public void testReleaseChromosome_0() {
    try {
      new ChromosomePool().releaseChromosome(null);
      fail();
    }
    catch (NullPointerException nex) {
      ;//this is OK
    }
  }

  public void testReleaseChromosome_1() throws Exception {
    ChromosomePool pool = new ChromosomePool();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setPopulationSize(5);
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    pool.releaseChromosome(chrom);
  }
}
