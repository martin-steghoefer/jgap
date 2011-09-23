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
import org.jgap.util.*;

import junit.framework.*;

import org.jgap.event.EventManager;

/**
 * Tests the Genotype class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GenotypeTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.75 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GenotypeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_0()
      throws Exception {
    try {
      new Genotype(null, new Population(conf, 0));
      fail();
    } catch (IllegalArgumentException invex) {
      ; //this is OK
    }
  }

  /**
   * Unproper Configuration (e.g. fitness function not set)
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_02()
      throws Exception {
    conf = new DefaultConfiguration();
    try {
      new Genotype(conf, new Population(conf, 0));
      fail();
    } catch (InvalidConfigurationException invex) {
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
    } catch (IllegalArgumentException invex) {
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
    chroms[0] = new Chromosome(new ConfigurationForTesting(), new Gene[] {
      new IntegerGene(new ConfigurationForTesting(), 1, 5)
    });
    try {
      new Genotype(null, chroms);
      fail();
    } catch (InvalidConfigurationException invex) {
      ; //this is OK
    } catch (IllegalArgumentException invex) {
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
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)
    });
    try {
      new Genotype(new DefaultConfiguration(), chroms);
      fail();
    } catch (InvalidConfigurationException invex) {
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
    Configuration conf = new DefaultConfiguration();
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    try {
      new Genotype(conf, chroms);
      fail();
    } catch (InvalidConfigurationException invex) {
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
    Configuration conf = new DefaultConfiguration();
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    try {
      new Genotype(conf, chroms);
      fail();
    } catch (InvalidConfigurationException invex) {
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
    Configuration conf = new DefaultConfiguration();
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    assertTrue(genotype.getConfiguration().getFitnessEvaluator()
               instanceof DefaultFitnessEvaluator);
    assertSame(conf, genotype.getConfiguration());
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
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(7);
    try {
      new Genotype(conf, new Chromosome[] {null});
      fail();
    } catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Tests that construction is possible without exception. Here, the sample
   * chromosome is of a different type than the chromosome(s) making up the
   * population. This is possible because the sample chromosome comes into play
   * just with Genotype.randomInitialGenotype.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_8()
      throws Exception {
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(7);
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
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(7);
    try {
      new Genotype(conf, chroms);
      fail();
    } catch (IllegalArgumentException iex) {
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 5)});
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 5)});
    chroms[0] = chrom;
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(7);
    Genotype genotype = new Genotype(conf, chroms);
    privateAccessor.setField(genotype, "m_population", new Population(conf, 1));
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
    IChromosome chrom = new Chromosome(conf, new Gene[] {
                                       new IntegerGene(conf, 1, 5)});
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 5)});
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
   * overwriting default setting for keeping population size constant.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_0()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    config.setKeepPopulationSizeConstant(false);
    // Add new NaturalSelector
    config.addNaturalSelector(new WeightedRouletteSelector(config), true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize()
        * config.getSampleChromosome().getGenes().length;
    genotype.evolve(1);
    assertTrue(popSize >= genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test evolve with BulkFitnessFunction.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_1()
      throws Exception {
    // override setFF in order to set the BulkFitnessFunction although
    // ConfigurationForTest set an ordinary FF beforehand
    Configuration config = new ConfigurationForTesting() {
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
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that population size remains constant when the configuration contains
   * a BCS as postselector.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testEvolve_2_1()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setKeepPopulationSizeConstant(false);
    // Remove all natural selectors.
    // -----------------------------
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    BestChromosomesSelector bcs = new BestChromosomesSelector(config);
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertEquals(popSize, genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that size of selected sub-population size is the fraction configured
   * when using a BCS as preselector.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testEvolve_2_3()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setKeepPopulationSizeConstant(false);
    // Select only 3/4 of previous generation and and thus try to mutate only
    // on 3/4 of the chromosomes in the population.
    // ----------------------------------------------------------------------
    config.setSelectFromPrevGen(0.75d);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // A zero in this sequence represents a gene to be mutated.
    // --------------------------------------------------------
    rand.setNextIntSequence(new int[] {
                            // First Chromosome
                            1, 0, 1,
                            // Second Chromosome
                            1, 1, 1,
                            // Third Chromosome
                            1, 1, 1,
                            // Fourth Chromosome
                            1, 1, 1
    });
    rand.setNextDouble(0.7d);
    config.setRandomGenerator(rand);
    // Remove all natural selectors.
    // -----------------------------
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    BestChromosomesSelector bcs = new BestChromosomesSelector(config);
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    IChromosome c = genotype.getPopulation().getChromosome(4);
    assertEquals(2.3/*FitnessFunction.NO_FITNESS_VALUE*/, c.getFitnessValueDirectly(),
                 DELTA);
    assertEquals( (int) Math.round(popSize * config.getSelectFromPrevGen() + 1),
                       genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
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
    Configuration config = new ConfigurationForTesting();
    // Add another NaturalSelector (some already exist within
    // ConfigurationForTest).
    // ------------------------------------------------------
    BestChromosomesSelector bcs = new BestChromosomesSelector(config);
    bcs.setOriginalRate(1);
    bcs.setDoubletteChromosomesAllowed(true);
    config.addNaturalSelector(bcs, false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertEquals(popSize, genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that population size remains constant with default settings.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEvolve_3_1()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setKeepPopulationSizeConstant(true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve(2);
    assertEquals(popSize, genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that population size grows with default settings overwritten.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEvolve_3_2()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setSelectFromPrevGen(1.0d);
    // Overwrite default setting.
    // --------------------------
    config.setKeepPopulationSizeConstant(false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    int popSize = config.getPopulationSize();
    genotype.evolve();
    assertTrue(popSize < genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that population size grows with default settings overwritten.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testEvolve_3_25()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextInt(0);
    config.setRandomGenerator(rand);
    config.setPopulationSize(200);
    config.setSelectFromPrevGen(1.0d);
    // Overwrite default setting.
    // --------------------------
    config.setKeepPopulationSizeConstant(false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve();
    assertEquals(200*2,genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Test that population size grows with default settings overwritten.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testEvolve_3_26()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // A Chromosome has 3 genes here. We let every 6th gene mutate, thus every
    // second Chromosome.
    // -----------------------------------------------------------------------
    rand.setNextIntSequence(new int[]{0,1,2,1,1,2});
    config.setRandomGenerator(rand);
    config.setPopulationSize(200);
    config.setSelectFromPrevGen(1.0d);
    // Overwrite default setting.
    // --------------------------
    config.setKeepPopulationSizeConstant(false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(4);
    // As only every second chromosome is mutated, the population is raised by
    // 50 %, or 200/2 with 200 = population size.
    // -----------------------------------------------------------------------
    assertEquals(200+200/2,genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Expect exception when no natural selectors are provided.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testEvolve_3_3()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    // Remove all natural selectors
    config.removeNaturalSelectors(false);
    config.removeNaturalSelectors(true);
    try {
      Genotype.randomInitialGenotype(config);
      fail();
    } catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will be recomputed. Reveals bug 1368072.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_1()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    Gene[] genes = new Gene[] {
        new BooleanGene(conf)};
    Configuration.resetProperty(Configuration.PROPERTY_SAMPLE_CHROM_INST);
    config.setSampleChromosome(new ChromosomeForTesting(config, genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(true);
    config.getGeneticOperators().clear();
    SwappingMutationOperator op = new SwappingMutationOperator(config, 1);
    op.setStartOffset(0); // because size is 1
    config.addGeneticOperator(op);
    assertTrue(doTestEvolve_5(config) > 0);
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will be recomputed. Reveals bug 1368072.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_2()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    Gene[] genes = new Gene[] {
        new BooleanGene(conf)};
    Configuration.resetProperty(Configuration.PROPERTY_SAMPLE_CHROM_INST);
    config.setSampleChromosome(new ChromosomeForTesting(config, genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(true);
    config.getGeneticOperators().clear();
    config.addGeneticOperator(new MutationOperator(config, 1));
    assertTrue(doTestEvolve_5(config) > 0);
  }

  /**
   * Test that for new chromosomes (e.g. mutated ones) their fitness value
   * will only be recomputed if necessary. Special case in which bug 1368072
   * does not apply.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testEvolve_5_3()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    Gene[] genes = new Gene[] {
        new BooleanGene(conf)};
    Configuration.resetProperty(Configuration.PROPERTY_SAMPLE_CHROM_INST);
    config.setSampleChromosome(new ChromosomeForTesting(config, genes));
    config.setPreservFittestIndividual(true);
    config.setKeepPopulationSizeConstant(true);
    BestChromosomesSelector sel = (BestChromosomesSelector) config.
        getNaturalSelector(true, 0);
    sel.setDoubletteChromosomesAllowed(!true);
    config.getGeneticOperators().clear();
    config.addGeneticOperator(new MutationOperator(config, 0));
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
    ChromosomeForTesting chrom = (ChromosomeForTesting) genotype.getPopulation().
        getChromosome(0);
    chrom.resetComputedTimes();
    // Mark any chromosome as original (that is not cloned)
    for (int i = 0; i < genotype.getPopulation().size(); i++) {
      chrom = (ChromosomeForTesting) genotype.getPopulation().getChromosome(i);
      chrom.resetIsCloned();
    }
    // Now do the test evolution --> new fitness values must be recomputed!
    genotype.evolve(2);
    // Check if global state holder indicates that getFitnessValue() has been
    // called at least once for a cloned (e.g. mutated) chromosome and that for
    // this call the to date fitness value is initial (i.e. not set).
    chrom = (ChromosomeForTesting) genotype.getPopulation().
        getChromosome(0);
    return chrom.getComputedTimes();
  }

  /**
   * Population is null.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_6()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.setPopulation(null);
    try {
      genotype.evolve();
      fail();
    } catch (NullPointerException iex) {
      ; // this is OK
    }
  }

  /**
   * minimumPopSizePercent > 0.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_7()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setMinimumPopSizePercent(290);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    config.setPopulationSize(10);
    config.setPreservFittestIndividual(false);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(1);
    assertEquals( (int) (10 * 290.0 / 100), genotype.getPopulation().size());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Preserve fittest Chromosome.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEvolve_8()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setMinimumPopSizePercent(290);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    config.setPopulationSize(10);
    config.setPreservFittestIndividual(true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(1);
    IChromosome fittest = genotype.getFittestChromosome();
    genotype.evolve(1);
    assertTrue(genotype.getFittestChromosome().getFitnessValue() >=
               fittest.getFitnessValue());
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Preserve fittest Chromosome.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEvolve_9()
      throws Exception {
    Configuration config = new ConfigurationForTesting();
    config.setMinimumPopSizePercent(290);
    // Overwrite default setting
    config.setKeepPopulationSizeConstant(!true);
    config.setPopulationSize(10);
    config.setPreservFittestIndividual(true);
    Genotype genotype = Genotype.randomInitialGenotype(config);
    genotype.evolve(1);
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
  }

  /**
   * Asserts that evolutions works without error (see bug 1748528).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  public void testEvolve_10()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setPreservFittestIndividual(true);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, new Gene[]{gene});
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    // Fitness Evaluator (lower is better).
    // ------------------------------------
    conf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    // Selector.
    // ---------
    conf.removeNaturalSelectors(false);
    conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);

    Genotype genotype = Genotype.randomInitialGenotype(conf);
    genotype.evolve(1);
    // Ensure all chromosomes are unique in the population.
    // ----------------------------------------------------
    assertTrue(uniqueChromosomes(genotype.getPopulation()));
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 55)});
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 55)});
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
    } catch (IllegalArgumentException illex) {
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
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 9999)});
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    assertEquals(7777, genotype.getChromosomes().length);
  }

  /**
   * Test for a Chromosome class not equal to org.jgap.Chromosome.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRandomInitialGenotype_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene aGene = new IntegerGene(conf, 1, 9999);
    aGene.setEnergy(22.3d);
    Chromosome chrom = new ChromosomeForTest2(conf, new Gene[] {aGene});
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
   * Use a chromosome implementation for which the DefaultInitializer is not
   * suited.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRandomInitialGenotype_3()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    IChromosome chrom = new MyChromosome(conf);
    conf.setPopulationSize(7777);
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(chrom);
    try {
      Genotype.randomInitialGenotype(conf);
      fail();
    } catch (IllegalStateException iex) {
      ; //this is OK
    }
  }

  /**
   * GeneticOperators missing.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testRandomInitialGenotype_4()
      throws Exception {
    Configuration config = new Configuration();
    Genotype.setStaticConfiguration(config);
    // Remove all genetic operators
    config.getGeneticOperators().clear();
    config.addNaturalSelector(new WeightedRouletteSelector(), true);
    try {
      Genotype.randomInitialGenotype(config);
      fail();
    } catch (InvalidConfigurationException iex) {
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
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(99999);
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {new IntegerGene(conf, 1, 5)});
    Genotype genotype = new Genotype(conf, chroms);
    assertNotNull(genotype);
    Genotype genotype2 = new Genotype(conf, chroms);
    assertTrue(genotype.equals(genotype2));
    assertEquals(genotype.toString(), genotype2.toString());
    assertEquals(genotype.toString(), genotype2.toString());
    assertFalse(genotype.equals(new Chromosome(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(99999);
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Genotype genotype = new Genotype(conf, chroms);
    Chromosome[] chroms2 = new Chromosome[2];
    chroms2[0] = new Chromosome(conf, new Gene[] {new IntegerGene(conf, 1, 5)});
    chroms2[1] = new Chromosome(conf, new Gene[] {new IntegerGene(conf, 2, 4)});
    Genotype genotype2 = new Genotype(conf, chroms2);
    assertFalse(genotype.equals(genotype2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(99999);
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Genotype genotype = new Genotype(conf, chroms);
    assertFalse(genotype.equals(null));
  }

  /**
   * Test if hashcode working in general.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @author John Serri
   * @since 2.1
   */
  public void testHashcode_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 9));
    conf.setPopulationSize(99999);
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
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
    //Build random Chromosomes
    for (Count = 0; Count < MAX_CHROMOSOME_TO_TEST; Count++) {
      NumGenes = (int) (Math.random() * MAX_GENES_TO_TEST) + 1;
      genes = new Gene[NumGenes];
      for (GeneCount = 0; GeneCount < NumGenes; GeneCount++) {
        GeneType = (int) (Math.random() * MAX_GENES_TYPES);
        switch (GeneType) {
          case 0:
            genes[GeneCount] = new IntegerGene(conf);
            break;
          case 1:
            genes[GeneCount] = new BooleanGene(conf);
            break;
          case 2:
            genes[GeneCount] = new CompositeGene(conf);
            break;
          case 3:
            genes[GeneCount] = new DoubleGene(conf);
            break;
          case 4:
            genes[GeneCount] = new FixedBinaryGene(conf, 5);
            break;
          case 5:
            genes[GeneCount] = new StringGene(conf);
            break;
        }
      }
      Configuration.reset();
      Configuration conf = new DefaultConfiguration();
      chrom = new Chromosome(conf, genes);
      conf.setFitnessFunction(new StaticFitnessFunction(0.5d));
      conf.setSampleChromosome(chrom);
      conf.setPopulationSize(5);
      Population pop = new Population(conf);
      pop.addChromosome(chrom);
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
      genes[0] = new IntegerGene(conf);
      Configuration.reset();
      Configuration conf = new DefaultConfiguration();
      chrom = new Chromosome(conf, genes);
      conf.setFitnessFunction(new StaticFitnessFunction(0.5d));
      conf.setSampleChromosome(chrom);
      conf.setPopulationSize(5);
      Population pop = new Population(conf);
      pop.addChromosome(chrom);
      geno = new Genotype(conf, pop);
      EqualChromosome.add(geno);
    }
    //If an object is equal it must have the same hashcode
    if (!thc.testHashCodeEquality(EqualChromosome)) {
      fail();
    }
  }

  /**
   * This test fails and shows the need for revamping configuration object
   * handling.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
//  public void testSetActiveConfiguration_0()
//      throws Exception {
//    Configuration conf = new ConfigurationForTest();
//    Chromosome[] chroms = new Chromosome[1];
//    chroms[0] = new Chromosome(conf, new Gene[] {
//                               new IntegerGene(conf, 1, 5)});
//    Genotype genotype = new Genotype(conf, chroms);
//    genotype.setActiveConfiguration(conf);
//    genotype.setActiveConfiguration(null);
//    // If working properly, the next call should return null!
//    assertNotNull(genotype.getConfiguration());
//  }
//
//  public void testSetActiveConfiguration_1()
//      throws Exception {
//    Configuration conf = new ConfigurationForTest();
//    Chromosome[] chroms = new Chromosome[1];
//    chroms[0] = new Chromosome(conf, new Gene[] {
//                               new IntegerGene(conf, 1, 5)});
//    Genotype genotype = new Genotype(conf, chroms);
//    Genotype.setConfiguration(null);
//    try {
//      genotype.setActiveConfiguration(null);
//      fail();
//    }
//    catch (InvalidConfigurationException iex) {
//      ; //this is OK
//    }
//  }
//
//  /**
//   * @throws Exception
//   * @author Klaus Meffert
//   * @since 2.6
//   */
//  public void testSetActiveConfiguration_2()
//      throws Exception {
//    Configuration conf = new ConfigurationForTest();
//    Chromosome[] chroms = new Chromosome[1];
//    chroms[0] = new Chromosome(conf, new Gene[] {
//                               new IntegerGene(conf, 1, 5)});
//    Genotype genotype = new Genotype(conf, chroms);
//    /**@todo following will be obsolete*/
//    genotype.setConfiguration(null);
//    Configuration conf2 = new ConfigurationForTest();
//    genotype.setActiveConfiguration(conf2);
//    assertTrue(genotype.getConfiguration().isLocked());
//  }

  /**
   * Ensures Genotype is implementing Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testIsSerializable_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    assertTrue(isSerializable(new Genotype(conf, chroms)));
  }

  /**
   * Ensures that Genotype and all objects contained implement Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testdoSerialize_0()
      throws Exception {
    // construct genotype to be serialized
    Configuration conf = new ConfigurationForTesting();
    Chromosome[] chroms = new Chromosome[1];
    chroms[0] = new Chromosome(conf, new Gene[] {
                               new IntegerGene(conf, 1, 5)});
    Genotype genotype = new Genotype(conf, chroms);
    // Serialize genotype to a file.
    // -----------------------------
    assertEquals(genotype, super.doSerialize(genotype));
  }

  public class ChromosomeForTest2
      extends ChromosomeForTesting {
    public ChromosomeForTest2(Configuration a_config,
                              final Gene[] a_initialGenes)
        throws InvalidConfigurationException {
      super(a_config, a_initialGenes);
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
    private transient Configuration m_conf;

    public MyChromosome(Configuration a_conf)
        throws InvalidConfigurationException {
      m_conf = a_conf;
    }

    public Gene getGene(int a_desiredLocus) {
      try {
        return new IntegerGene(m_conf);
      } catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }

    public String getUniqueID() {
      return "";
    }

    public void setUniqueIDTemplate(String a_templateID, int a_index) {
    }

    public String getUniqueIDTemplate(int a_index) {
      return "";
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

    public Configuration getConfiguration() {
      return m_conf;
    }

    public void increaseAge() {
    }

    public void resetAge() {
    }

    public void setAge(int a_age) {
    }

    public int getAge() {
      return 1;
    }

    public void increaseOperatedOn() {
    }

    public void resetOperatedOn() {
    }
    public int operatedOn() {
      return 0;
    }
  }

  /**
   * With specific configurations there was a bug in GABreeder: Some chromosomes
   * where not updates properly.
   *
   * @throws Exception
   * @since 3.6
   */
  public void testBreeder_0() throws Exception {
    Configuration conf = new TestConfiguration();
    conf.setPreservFittestIndividual(false);
    FitnessFunction myFunc =
        new Test2Function(conf);
    conf.setFitnessFunction(myFunc);
    Gene[] sampleGenes = new Gene[2];
    sampleGenes[0] = new DoubleGene(conf, 0, 2); // X
    sampleGenes[1] = new DoubleGene(conf, 0, 2); // Y
    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    conf.setPopulationSize(20);
    Genotype population = Genotype.randomInitialGenotype(conf);
    population.evolve(200);
    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    double fit = bestSolutionSoFar.getFitnessValue();
    double sol1 = (Double) bestSolutionSoFar.getGene(0).getAllele();
    double sol2 = (Double) bestSolutionSoFar.getGene(1).getAllele();
    if (Math.abs(sol1 + sol2 - fit) > 0.001) {
      assertFalse("Fitness value incorrect", true);
    }
  }

  class TestConfiguration
      extends Configuration implements ICloneable {
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.75 $";

    public TestConfiguration() {
      this("","");
    }

    /**
     * Constructs a new DefaultConfiguration instance with a number of
     * configuration settings set to default values. It is still necessary
     * to set the sample Chromosome, population size, and desired fitness
     * function. Other settings may optionally be altered as desired.
     *
     * @param a_id unique id for the configuration within the current thread
     * @param a_name informative name of the configuration, may be null
     *
     * @author Neil Rotstan
     * @author Klaus Meffert
     * @since 1.0
     */
    public TestConfiguration(String a_id, String a_name) {
      super(a_id, a_name);
      try {
        setBreeder(new GABreeder());
        setRandomGenerator(new StockRandomGenerator());
        setEventManager(new EventManager());
        BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
            this, 0.90d);
        //bestChromsSelector.setDoubletteChromosomesAllowed(true);
        bestChromsSelector.setDoubletteChromosomesAllowed(false);
        addNaturalSelector(bestChromsSelector, false);
        setMinimumPopSizePercent(0);
        //
        setSelectFromPrevGen(1.0d);
        setKeepPopulationSizeConstant(true);
        setFitnessEvaluator(new DefaultFitnessEvaluator());
        setChromosomePool(new ChromosomePool());
        addGeneticOperator(new CrossoverOperator(this, 0.5d));
        addGeneticOperator(new MutationOperator(this, 8));
      }
      catch (InvalidConfigurationException e) {
        throw new RuntimeException(
            "Fatal error: DefaultConfiguration class could not use its "
            + "own stock configuration values. This should never happen. "
            + "Please report this as a bug to the JGAP team.");
      }
    }

    /**
     * @return deep clone of this instance
     *
     * @author Klaus Meffert
     * @since 3.2
     */
    public Object clone() {
      return super.clone();
    }
}

  public class Test2Function extends FitnessFunction {
  double MAX = -1;
      Configuration config1;
       Test2Function(Configuration config){
           config1= config;
       }




      public double evaluate(IChromosome ic) {


          Double X = getX(ic);
          Double Y = getY(ic);

          double function = Y + X;
          if(function > -1) {
            MAX = function;
          }
          return function;

      }


      public Double getX(IChromosome a_potentialSolution) {
      Double x = (Double) a_potentialSolution.getGene(0).getAllele();
      return x;
      }


      public Double getY(IChromosome a_potentialSolution) {
      Double y = (Double) a_potentialSolution.getGene(1).getAllele();
      return y;
      }
  }

}
