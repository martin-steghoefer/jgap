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
import org.jgap.util.*;
import junit.framework.*;

/**
 * Tests the Genotype class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GenotypeTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.47 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GenotypeTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_0() {
    try {
      new Genotype(null, new Population(0));
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
    catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_1()
      throws Exception {
    try {
      Population pop = null;
      new Genotype(new DefaultConfiguration(), pop);
      fail();
    }
    catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_2()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    try {
      new Genotype(null, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
    catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_3()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    try {
      new Genotype(new DefaultConfiguration(), chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_4()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    try {
      new Genotype(conf, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_5()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    try {
      new Genotype(conf, chroms);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_6()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertTrue(genotype.getConfiguration().getFitnessEvaluator()
               instanceof DefaultFitnessEvaluator);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_7()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    try {
      new Genotype(conf, new Chromosome[] {null});
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_8()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
    new Genotype(conf, chroms);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_9()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(7);
    try {
      new Genotype(conf, chroms);
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
   * @since 2.0
   */
  public void testGetChromosomes_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[1];
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 5)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertEquals(1, genotype.getChromosomes().length);
    assertEquals(chrom, genotype.getChromosomes()[0]);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testGetFittestChromosome_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[1];
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 5)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    privateAccessor.setField(genotype, "m_population", new Population(1));
    assertEquals(null, genotype.getFittestChromosome());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testGetFittestChromosome_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    IChromosome[] chroms = new Chromosome[1];
    IChromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 5)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    IChromosome chrom2 = genotype.getFittestChromosome();
    assertEquals(chrom, chrom2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetFittestChromosomes_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[1];
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 5)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    List l = genotype.getFittestChromosomes(1);
    Chromosome chrom2 = (Chromosome) l.get(0);
    assertEquals(chrom, chrom2);
  }

  /**
   * Assert population size shrinks when using special configuration and by
   * overwriting default setting for keeping population size constant
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_0()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    config.setKeepPopulationSizeConstant(false);
    // Add new NaturalSelector
    config.addNaturalSelector(new WeightedRouletteSelector(), true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize()
        * config.getSampleChromosome().getGenes().length;
    genotype.evolve(1);
    assertTrue(popSize >= genotype.getPopulation().size());
  }

  /**
   * Test evolve with BulkFitnessFunction
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_1()
      throws Exception {
    // override setFF in order to set the BulkFitnessFunction although
    // ConfigurationForTest set an ordinary FF beforehand
    Configuration config = new ConfigurationForTest() {
      public synchronized void setFitnessFunction(FitnessFunction
                                                  a_functionToSet)
          throws InvalidConfigurationException {
        setBulkFitnessFunction(new BulkFitnessOffsetRemover(a_functionToSet));
      }
    };
    Genotype genotype = Genotype.randomInitialGenotype(config);
    // Just test that the following runs without error by trusting exception
    // handling
    genotype.evolve();
  }

  /**
   * Test that population size remains constant when the configuration contains
   * a BCS as postselector
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_2_1()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    config.setKeepPopulationSizeConstant(false);
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    BestChromosomesSelector bcs = new BestChromosomesSelector();
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertEquals(popSize, genotype.getPopulation().size());
  }

  /**
   * Test that multiple NaturalSelector's work without error
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_2_2()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Add another NaturalSelector (others already exist within
    // ConfigurationForTest)
    BestChromosomesSelector bcs = new BestChromosomesSelector();
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertEquals(popSize, genotype.getPopulation().size());
  }

  /**
   * Test that population size remains constant with default settings
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEvolve_3_1()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    config.setKeepPopulationSizeConstant(true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve(2);
    assertEquals(popSize, genotype.getPopulation().size());
  }

  /**
   * Test that population size grows with default settings overwritten
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEvolve_3_2()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertTrue(popSize < genotype.getPopulation().size());
  }

  /**
   * GeneticOperators missing
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_4()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all genetic operators
    config.getGeneticOperators().clear();
    config.addNaturalSelector(new WeightedRouletteSelector(), true);
    try {
      Genotype.randomInitialGenotype(config);
      fail();
    }
    catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will be recomputed. Reveals bug 1368072.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_1()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    Gene[] genes = new Gene[] {
        new BooleanGene()};
    config.setSampleChromosome(new ChromosomeForTest(genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(true);
    config.getGeneticOperators().clear();
    SwappingMutationOperator op = new SwappingMutationOperator(1);
    op.setStartOffset(0); // because size is 1
    config.addGeneticOperator(op);
    assertTrue(doTestEvolve_5(config) > 0);
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will be recomputed. Reveals bug 1368072.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_2()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    Gene[] genes = new Gene[] {
        new BooleanGene()};
    config.setSampleChromosome(new ChromosomeForTest(genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(true);
    config.getGeneticOperators().clear();
    config.addGeneticOperator(new MutationOperator(1));
    assertTrue(doTestEvolve_5(config) > 0);
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will only be recomputed if necessary. Special case in which bug 1368072
   * does not apply.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_3()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    Gene[] genes = new Gene[] {
        new BooleanGene()};
    config.setSampleChromosome(new ChromosomeForTest(genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(true);
    config.getGeneticOperators().clear();
    config.addGeneticOperator(new MutationOperator(0));
    assertEquals(0, doTestEvolve_5(config));
  }

  /**
   * Helper: used in tests for evolve method
   * @param config Configuration
   * @throws Exception
   * @return number of times the fitness value has been computed for the first
   * chromosome
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  private int doTestEvolve_5(Configuration config)
      throws Exception {
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(2);
    // Reset counter. Because of static state holder we only need to do this
    // for one chromosome referencing the same global state holder as well as
    // all other chromosomes (of class ChromosomeForTest) do.
    ChromosomeForTest chrom = (ChromosomeForTest) genotype.getPopulation().
        getChromosome(0);
    chrom.resetComputedTimes();
    // Mark any chromosome as original (that is not cloned)
    for (int i = 0; i < genotype.getPopulation().size(); i++) {
      chrom = (ChromosomeForTest) genotype.getPopulation().getChromosome(i);
      chrom.resetIsCloned();
    }
    // Now do the test evolution --> new fitness values must be recomputed!
    genotype.evolve(2);
    // Check if global state holder indicates that getFitnessValue() has been
    // called at least once for a cloned (e.g. mutated) chromosome and that for
    // this call the to date fitness value is initial (i.e. not set).
    chrom = (ChromosomeForTest) genotype.getPopulation().
        getChromosome(0);
    return chrom.getComputedTimes();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_6()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Add another NaturalSelector (others already exist within
    // ConfigurationForTest)
    BestChromosomesSelector bcs = new BestChromosomesSelector();
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.setConfiguration(null);
    try {
      genotype.evolve();
      fail();
    }
    catch (IllegalStateException iex) {
      ; // this is OK
    }
  }

  /**
   * minimumPopSizePercent > 0.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_7()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.setMinimumPopSizePercent(290);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    config.setPopulationSize(10);
    config.setPreservFittestIndividual(false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(1);
    assertEquals( (int) (10 * 290.0 / 100), genotype.getPopulation().size());
  }

  /**
   * Preserve fittest Chromosome.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_8()
      throws Exception {
    Configuration config = new ConfigurationForTest();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.setMinimumPopSizePercent(290);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    config.setPopulationSize(10);
    config.setPreservFittestIndividual(true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(1);
    IChromosome fittest = genotype.getFittestChromosome();
    genotype.evolve(1);
    assertEquals(fittest, genotype.getFittestChromosome());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testToString_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    Chromosome[] chroms = new Chromosome[1];
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 55)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertTrue(genotype.toString() != null);
    assertTrue(genotype.toString().length() > 0);
    assertEquals(IChromosome.S_SIZE + ":1, "
                 + IChromosome.S_FITNESS_VALUE + ":"
                 + FitnessFunction.NO_FITNESS_VALUE
                 + ", "
                 + IChromosome.S_ALLELES + ":[IntegerGene(1,55)=null], "
                 + IChromosome.S_APPLICATION_DATA + ":null"
                 + " ["
                 + FitnessFunction.NO_FITNESS_VALUE
                 + "]\n", genotype.toString());
  }

  /**
   * Same as testToString_0 except that fitness value is computed
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testToString_1()
      throws Exception {
    final double fitnessvalue = 5.0d;
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(fitnessvalue));
    Chromosome[] chroms = new Chromosome[1];
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 55)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertTrue(genotype.toString() != null);
    assertTrue(genotype.toString().length() > 0);
    // compute fitness of Genotype thus of all contained chromosomes
    genotype.getFittestChromosome();
    assertEquals(IChromosome.S_SIZE + ":1, "
                 + IChromosome.S_FITNESS_VALUE + ":"
                 + fitnessvalue
                 + ", "
                 + IChromosome.S_ALLELES + ":[IntegerGene(1,55)=null], "
                 + IChromosome.S_APPLICATION_DATA + ":null"
                 + " ["
                 + fitnessvalue
                 + "]\n", genotype.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testRandomInitialGenotype_0()
      throws Exception {
    try {
      Genotype.randomInitialGenotype(null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testRandomInitialGenotype_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Chromosome chrom = new Chromosome(new Gene[] {
                                      new IntegerGene(1, 9999)});
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    assertEquals(7777, genotype.getChromosomes().length);
  }

  /**
   * Test for a Chromosome class not equal to org.jgap.Chromosome
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRandomInitialGenotype_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene aGene = new IntegerGene(1, 9999);
    aGene.setEnergy(22.3d);
    Chromosome chrom = new ChromosomeForTest2(new Gene[] {aGene});
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    assertEquals(7777, genotype.getPopulation().size());
    Chromosome c = (Chromosome) genotype.getPopulation().getChromosome(0);
    Gene g = c.getGene(0);
    assertEquals(aGene.getEnergy(), g.getEnergy(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRandomInitialGenotype_3()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    IChromosome chrom = new MyChromosome();
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    try {
      Genotype.randomInitialGenotype(conf);
      fail();
    }
    catch (IllegalStateException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testEquals_0()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(99999);
    Genotype genotype = new Genotype(conf, chroms);
    assertFalse(genotype.equals(null));
    Genotype genotype2 = new Genotype(conf, chroms);
    assertTrue(genotype.equals(genotype2));
    assertEquals(genotype.toString(), genotype2.toString());
    assertEquals(genotype.toString(), genotype2.toString());
    assertFalse(genotype.equals(new Chromosome()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_1()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(99999);
    Genotype genotype = new Genotype(conf, chroms);
    Chromosome[] chroms2 = new Chromosome[2];
    chroms2[0] = new Chromosome(new Gene[] {new IntegerGene(1, 5)});
    chroms2[1] = new Chromosome(new Gene[] {new IntegerGene(2, 4)});
    Genotype genotype2 = new Genotype(conf, chroms2);
    assertFalse(genotype.equals(genotype2));
  }

  /**
   * Test if hashcode working in general
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @author John Serri
   * @since 2.1
   */
  public void testHashcode_0()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(new BooleanGene(), 9));
    conf.setPopulationSize(99999);
    Genotype genotype = new Genotype(conf, chroms);
    genotype.hashCode();
  }

  private final static int MAX_CHROMOSOME_TO_TEST = 1000;

  private final static int MAX_GENES_TO_TEST = 25;

  private final static int MAX_GENES_TYPES = 6;

  public void testHashcode_1()
      throws Exception {
    int Count;
    int NumGenes;
    int GeneCount;
    int GeneType;
    Gene[] genes;
    Chromosome chrom;
    TestHashcode thc = new TestHashcode();
    thc.setVerbose(!true);
    List UniqueChromosome = new ArrayList();
    List EqualChromosome = new ArrayList();
    Genotype geno;
    //Build Random Chromosomes
    for (Count = 0; Count < MAX_CHROMOSOME_TO_TEST; Count++) {
      NumGenes = (int) (Math.random() * MAX_GENES_TO_TEST) + 1;
      genes = new Gene[NumGenes];
      for (GeneCount = 0; GeneCount < NumGenes; GeneCount++) {
        GeneType = (int) (Math.random() * MAX_GENES_TYPES);
        switch (GeneType) {
          case 0:
            genes[GeneCount] = new IntegerGene();
            break;
          case 1:
            genes[GeneCount] = new BooleanGene();
            break;
          case 2:
            genes[GeneCount] = new CompositeGene();
            break;
          case 3:
            genes[GeneCount] = new DoubleGene();
            break;
          case 4:
            genes[GeneCount] = new FixedBinaryGene(5);
            break;
          case 5:
            genes[GeneCount] = new StringGene();
            break;
        }
      }
      chrom = new Chromosome(genes);
      Population pop = new Population();
      pop.addChromosome(chrom);
      Configuration conf = new DefaultConfiguration();
      conf.setFitnessFunction(new StaticFitnessFunction(0.5d));
      conf.setSampleChromosome(chrom);
      conf.setPopulationSize(5);
      geno = new Genotype(conf, pop);
      // We only want to add unique object, since equal object will return the
      // same hashcode
      if (UniqueChromosome.contains(geno) == false) {
        UniqueChromosome.add(geno);
      }
    }
    //Test to see if enough hashcodes are unique
    thc.setFractionUnique(.95);
    if (!thc.testHashCodeUniqueness(UniqueChromosome)) {
      System.out.println(
          "testHashCodeUniqueness failed\n Actual Percent unique = " +
          thc.getActualFractionUnique());
      fail();
    }
    //Test mathematical average and dispersion of hashcode
    //I am not sure of the value of this test since boundary values are
    //pretty much arbitrary
//    thc.setAverageMax(16500000);
//    thc.setAverageMin(14000);
//    thc.setStdDevMax(2100000000);
//    thc.setStdDevMin(90000);
//    if (thc.testDispersion(UniqueChromosome) == false) {
//      fail();
//    }

    //Build identical Chromosomes
    for (Count = 0; Count < 3; Count++) {
      genes = new Gene[1];
      genes[0] = new IntegerGene();
      chrom = new Chromosome(genes);
      Population pop = new Population();
      pop.addChromosome(chrom);
      Configuration conf = new DefaultConfiguration();
      conf.setFitnessFunction(new StaticFitnessFunction(0.5d));
      conf.setSampleChromosome(chrom);
      conf.setPopulationSize(5);
      geno = new Genotype(conf, pop);
      EqualChromosome.add(geno);
    }
    //If an object is equal it must have the same hashcode
    if (!thc.testHashCodeEquality(EqualChromosome)) {
      fail();
    }
  }

  public void testSetActiveConfiguration_0()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new ConfigurationForTest();
    Genotype genotype = new Genotype(conf, chroms);
    genotype.setActiveConfiguration(conf);
    genotype.setActiveConfiguration(null);
    assertNull(genotype.getConfiguration());
  }

  public void testSetActiveConfiguration_1()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new ConfigurationForTest();
    Genotype genotype = new Genotype(conf, chroms);
    Genotype.setConfiguration(null);
    try {
      genotype.setActiveConfiguration(null);
      fail();
    }
    catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetActiveConfiguration_2()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new ConfigurationForTest();
    Genotype genotype = new Genotype(conf, chroms);
    genotype.setConfiguration(null);
    Configuration conf2 = new ConfigurationForTest();
    genotype.setActiveConfiguration(conf2);
    assertTrue(genotype.getConfiguration().isLocked());
  }

  /**
   * Ensures Genotype is implementing Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testIsSerializable_0()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new ConfigurationForTest();
    assertTrue(isSerializable(new Genotype(conf, chroms)));
  }

  /**
   * Ensures that Genotype and all objects contained implement Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testdoSerialize_0()
      throws Exception {
    // construct genotype to be serialized
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(new Gene[] {
                               new IntegerGene(1, 5)});
    Configuration conf = new ConfigurationForTest();
    Genotype genotype = new Genotype(conf, chroms);
    // serialize genotype to a file
    assertEquals(genotype, super.doSerialize(genotype));
  }

  public class ChromosomeForTest2
      extends ChromosomeForTest {
    public ChromosomeForTest2(final Gene[] a_initialGenes) {
      super(a_initialGenes);
    }

    public boolean isHandlerFor(Class a_class) {
      if (a_class == ChromosomeForTest2.class) {
        return true;
      }
      else {
        return false;
      }
    }

    public Object perform(Object a_obj, Class a_class, Object a_params)
        throws Exception {
      return randomInitialChromosome2();
    }
  }
  public class MyChromosome
      implements IChromosome {
    public Gene getGene(int a_desiredLocus) {
      return new IntegerGene();
    }
    public Gene[] getGenes() {
      return new Gene[] {};
    }

    public int size() {
      return 1;
    }

    public void setFitnessValue(double a_newFitnessValue) {
    }

    public double getFitnessValue() {
      return 0;
    }
    public void setFitnessValueDirectly(double a_newFitnessValue) {

    }
    public double getFitnessValueDirectly() {
      return getFitnessValue();
    }
    public int compareTo(Object other) {
      return 0;
    }

    public void setGenes(Gene[] a_genes)
        throws InvalidConfigurationException {
    }
    public void setIsSelectedForNextGeneration(boolean a_isSelected) {
    }
    public boolean isSelectedForNextGeneration() {
      return true;
    }
    public Object clone() {
      return null;
    }

    public void setConstraintChecker(IGeneConstraintChecker a_constraintChecker)
        throws InvalidConfigurationException {
    }

    public void setApplicationData(Object a_newData) {
    }

    public Object getApplicationData() {
      return null;
    }

    public void cleanup() {
    }
  }
}
