/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the ChromosomePool class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ChromosomePoolTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ChromosomePoolTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * Test if construction possible without failure.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_0() throws Exception {
    ChromosomePool pool = new ChromosomePool();
    Pool p = (Pool)privateAccessor.getField(pool, "m_chromosomePool");
    assertNotNull(p);
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testAquireChromosome_0() {
    assertEquals(null, new ChromosomePool().acquireChromosome());
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testReleaseChromosome_0() {
    try {
      new ChromosomePool().releaseChromosome(null);
      fail();
    }
    catch (IllegalArgumentException nex) {
      ; //this is OK
    }
  }

  /**
   * Should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testReleaseChromosome_1()
      throws Exception {
    ChromosomePool pool = new ChromosomePool();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setPopulationSize(5);
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    pool.releaseChromosome(chrom);
  }
}
