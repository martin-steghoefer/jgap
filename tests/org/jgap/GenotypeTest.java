package org.jgap;

import junit.framework.*;
import org.jgap.impl.*;

/**
 * <p>Title: Tests for Genotype class</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class GenotypeTest extends TestCase {
  public GenotypeTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(GenotypeTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try {
      Genotype genotype = new Genotype(null, null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
    catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    try {
      Genotype genotype = new Genotype(null, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
    catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  public void testConstruct_3() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    try {
      Genotype genotype = new Genotype(new DefaultConfiguration(), chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testConstruct_4() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    try {
      Genotype genotype = new Genotype(conf, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testConstruct_5() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    try {
      Genotype genotype = new Genotype(conf, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testConstruct_6() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
  }

  public void testConstruct_7() throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    try {
      Genotype genotype = new Genotype(conf, null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  public void testGetChromosomes_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[ 1 ];
    Chromosome chrom = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    chroms[ 0 ] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertEquals(1, genotype.getChromosomes().length);
    assertEquals(chrom, genotype.getChromosomes()[ 0 ]);
  }

  public void testGetFittestChromosome_0() {
    /**@todo implement*/
  }

  public void testEvolve_0() {
    /**@todo implement*/
  }

  /**
   * This test fails intentionally.
   * Reason: Chromosome.m_activeConfiguration is final but there is an easy way to
   * construct a Chromosome without giving a configuration (see below)!
   * @throws Exception
   */
  public void testToString_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[ 1 ];
    Chromosome chrom = new Chromosome(new Gene[] {new IntegerGene(1, 55)});
    chroms[ 0 ] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    chrom.setActiveConfiguration(conf);
    Genotype genotype = new Genotype(conf, chroms);
    assertTrue(genotype.toString() != null);
    assertTrue(genotype.toString().length() > 0);
  }

  public void testRandomInitialGenotype_0() throws Exception {
    try {
      Genotype genotype = Genotype.randomInitialGenotype(null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  public void testRandomInitialGenotype_1() throws Exception {
    Configuration conf = new DefaultConfiguration();
    Chromosome chrom = new Chromosome(new Gene[] {new IntegerGene(1, 9999)});
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    assertEquals(7777, genotype.getChromosomes().length);
  }

  public void testEquals_0() throws Exception {
    Chromosome[] chroms = new Chromosome[ 1 ];
    chroms[ 0 ] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(99999);
    Genotype genotype = new Genotype(conf, chroms);
    assertEquals(false, genotype.equals(null));
    Genotype genotype2 = new Genotype(conf, chroms);
    assertTrue(genotype.equals(genotype2));
    /**@todo following still fails, also see testToString_0()*/
//    assertEquals(genotype.toString(), genotype2.toString());
  }
}