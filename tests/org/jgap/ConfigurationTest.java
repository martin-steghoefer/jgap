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

import org.jgap.event.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the Configuration class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ConfigurationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.27 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ConfigurationTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testAddGeneticOperator_0() {
    Configuration conf = new Configuration();
    try {
      conf.addGeneticOperator(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testAddGeneticOperator_1() {
    Configuration conf = new Configuration();
    try {
      conf.lockSettings();
      conf.addGeneticOperator(new MutationOperator());
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testAddGeneticOperator_2()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(1, conf.getGeneticOperators().size());
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(2, conf.getGeneticOperators().size());
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_0()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_1()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_2()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_3()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), true);
    conf.setRandomGenerator(new StockRandomGenerator());
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_4()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_5()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), true);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_6()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), true);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    conf.verifyStateIsValid();
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testVerifyStateIsValid_7()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), true);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Configuration with sample chromosome that has no genes
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testVerifyStateIsValid_8()
      throws InvalidConfigurationException {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Chromosome sample = new Chromosome();
    conf.setSampleChromosome(sample);
    conf.setPopulationSize(5);
    try {
      conf.verifyStateIsValid();
      fail();
    }
    catch (InvalidConfigurationException illex) {
      ; //this is OK
    }
  }

  /**
   * Configuration with sample chromosome that has a gene with allele value null
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testVerifyStateIsValid_9()
      throws InvalidConfigurationException {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    BooleanGene gene = new BooleanGene();
    gene.setAllele(null);
    Chromosome sample = new Chromosome(gene, 55);
    conf.setSampleChromosome(sample);
    conf.setPopulationSize(5);
    conf.verifyStateIsValid();
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testIsLocked_0()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    assertEquals(false, conf.isLocked());
    conf.lockSettings();
    assertEquals(true, conf.isLocked());
  }

  /**
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   */
  public void testGetter_0()
      throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    FitnessFunction fitFunc = new StaticFitnessFunction(2);
    conf.setFitnessFunction(fitFunc);
    Gene gene = new BooleanGene();
    Chromosome sample = new Chromosome(gene, 55);
    conf.setSampleChromosome(sample);
    NaturalSelector natSel = new WeightedRouletteSelector();
    conf.addNaturalSelector(natSel, false);
    RandomGenerator randGen = new StockRandomGenerator();
    conf.setRandomGenerator(randGen);
    IEventManager evMan = new EventManager();
    conf.setEventManager(evMan);
    GeneticOperator mutOp = new MutationOperator();
    conf.addGeneticOperator(mutOp);
    GeneticOperator croOp = new CrossoverOperator();
    conf.addGeneticOperator(croOp);
    conf.setPopulationSize(7);
    assertEquals(fitFunc, conf.getFitnessFunction());
    assertEquals(natSel, conf.getNaturalSelectors(false).get(0));
    assertEquals(randGen, conf.getRandomGenerator());
    assertEquals(sample, conf.getSampleChromosome());
    assertEquals(evMan, conf.getEventManager());
    assertEquals(7, conf.getPopulationSize());
    assertEquals(2, conf.getGeneticOperators().size());
    assertEquals(mutOp, conf.getGeneticOperators().get(0));
    assertEquals(croOp, conf.getGeneticOperators().get(1));
  }

  /**
   * Tests a deprecated function!
   * @throws Exception
   */
  public void testSetNaturalSelector_0()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.setNaturalSelector(selector);
    assertEquals(selector, conf.getNaturalSelectors(false).get(0));
  }

  /**
   * Tests a deprecated function!
   */
  public void testGetNaturalSelector_0() {
    Configuration conf = new Configuration();
    NaturalSelector selector = conf.getNaturalSelector();
    assertEquals(null, selector);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testGetNaturalSelector_1()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new BestChromosomesSelector();
    conf.addNaturalSelector(selector, false);
    assertEquals(selector, conf.getNaturalSelector());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testGetNaturalSelector_2()
      throws Exception {
    Configuration conf = new Configuration();
    try {
      conf.getNaturalSelector(true, 0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testGetNaturalSelector_3()
      throws Exception {
    Configuration conf = new Configuration();
    try {
      conf.getNaturalSelector(false, 0);
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
   * @since 2.2
   */
  public void testGetNaturalSelector_4()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new BestChromosomesSelector();
    conf.addNaturalSelector(selector, false);
    assertEquals(selector, conf.getNaturalSelector(false, 0));
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testAddNaturalSelector_0()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, true);
    assertEquals(selector, conf.getNaturalSelectors(true).get(0));
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testAddNaturalSelector_1()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, false);
    assertEquals(selector, conf.getNaturalSelectors(false).get(0));
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testAddNaturalSelector_2()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector1 = new WeightedRouletteSelector();
    NaturalSelector selector2 = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector1, false);
    assertEquals(selector1, conf.getNaturalSelectors(false).get(0));
    conf.addNaturalSelector(selector2, true);
    assertEquals(selector2, conf.getNaturalSelectors(true).get(0));
    assertEquals(selector1, conf.getNaturalSelectors(false).get(0));
    try {
      assertEquals(null, conf.getNaturalSelectors(false).get(1));
      fail();
    }
    catch (Exception ex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testAddNaturalSelector_3()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, false);
    conf.getNaturalSelectors(false).clear();
    conf.addNaturalSelector(selector, false);
    Integer i = (Integer) privateAccessor.getField(conf,
        "m_sizeNaturalSelectorsPost");
    assertEquals(1, i.intValue());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testAddNaturalSelector_4()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, true);
    conf.getNaturalSelectors(true).clear();
    conf.addNaturalSelector(selector, true);
    Integer i = (Integer) privateAccessor.getField(conf,
        "m_sizeNaturalSelectorsPre");
    assertEquals(1, i.intValue());
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetFitnessFunction_0() {
    Configuration conf = new Configuration();
    try {
      conf.setFitnessFunction(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetFitnessEvaluator_0() {
    Configuration conf = new Configuration();
    try {
      conf.setFitnessEvaluator(null);
      fail();
    }
    catch (IllegalStateException istex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetPreserveFittestIndividual_0() {
    Configuration conf = new Configuration();
    assertFalse(conf.isPreserveFittestIndividual());
    conf.setPreservFittestIndividual(true);
    assertTrue(conf.isPreserveFittestIndividual());
    conf.setPreservFittestIndividual(false);
    assertFalse(conf.isPreserveFittestIndividual());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGenerationNr_0() {
    Configuration conf = new Configuration();
    assertEquals(0, conf.getGenerationNr());
    conf.incrementGenerationNr();
    assertEquals(1, conf.getGenerationNr());
    conf.incrementGenerationNr();
    conf.incrementGenerationNr();
    assertEquals(3, conf.getGenerationNr());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetBulkFitnessFunction_0() {
    Configuration conf = new Configuration();
    try {
      conf.setBulkFitnessFunction(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetBulkFitnessFunction_1()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setFitnessFunction(new TestFitnessFunction());
    try {
      conf.setBulkFitnessFunction(new TestBulkFitnessFunction());
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetBulkFitnessFunction_2()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setBulkFitnessFunction(new TestBulkFitnessFunction());
    try {
      conf.setFitnessFunction(new TestFitnessFunction());
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetBulkFitnessFunction_3()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setBulkFitnessFunction(new TestBulkFitnessFunction());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testGetPopulationSize_0()
      throws Exception {
    Configuration conf = new Configuration();
    assertEquals(0, conf.getPopulationSize());
    final int SIZE = 22;
    conf.setPopulationSize(SIZE);
    assertEquals(SIZE, conf.getPopulationSize());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetPopulationSize_1()
      throws Exception {
    Configuration conf = new Configuration();
    try {
      conf.setPopulationSize(0);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetRandomGenerator_0() {
    Configuration conf = new Configuration();
    try {
      conf.setRandomGenerator(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetEventManager_0() {
    Configuration conf = new Configuration();
    try {
      conf.setEventManager(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testLock_0()
      throws Exception {
    Configuration conf = new Configuration();
    try {
      conf.lockSettings();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testLock_1()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene gene = new BooleanGene();
    Chromosome sample = new Chromosome(gene, 55);
    conf.setSampleChromosome(sample);
    conf.addNaturalSelector(new WeightedRouletteSelector(), false);
    conf.setRandomGenerator(new GaussianRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    conf.lockSettings();
    assertEquals(true, conf.isLocked());
    try {
      conf.verifyChangesAllowed();
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetSampleChromosome_0()
      throws Exception {
    Configuration conf = new Configuration();
    Gene gene = new BooleanGene();
    new Chromosome(gene, 55);
    try {
      conf.setSampleChromosome(null);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testGetChromosomeSize_0()
      throws Exception {
    Configuration conf = new Configuration();
    Gene gene = new BooleanGene();
    final int SIZE = 55;
    Chromosome sample = new Chromosome(gene, SIZE);
    conf.setSampleChromosome(sample);
    assertEquals(SIZE, conf.getChromosomeSize());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_0()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    String s = trimString(conf.toString());
    String eventmgr = conf.getEventManager() != null ?
        conf.getEventManager().getClass().getName() : conf.S_NONE;
    String genops = "";
    if (conf.getGeneticOperators().size() < 1) {
      genops = conf.S_NONE;
    }
    else {
      for (int i = 0; i < conf.getGeneticOperators().size(); i++) {
        if (i > 0) {
          genops += ";";
        }
        genops += conf.getGeneticOperators().get(i).getClass().getName(); ;
      }
    }
    // natural selectors (pre)
    String natselsPre = "";
    int natsize = conf.getNaturalSelectors(true).size();
    if (natsize < 1) {
      natselsPre = conf.S_NONE;
    }
    else {
      for (int i = 0; i < natsize; i++) {
        if (i > 0) {
          natselsPre += "; ";
        }
        natselsPre += " " +
            conf.getNaturalSelectors(true).get(i).getClass().getName();
      }
    }
    // natural selectors (post)
    String natselsPost = "";
    natsize = conf.getNaturalSelectors(false).size();
    if (natsize < 1) {
      natselsPost = conf.S_NONE;
    }
    else {
      for (int i = 0; i < natsize; i++) {
        if (i > 0) {
          natselsPost += "; ";
        }
        natselsPost += " " +
            conf.getNaturalSelectors(false).get(i).getClass().getName();
      }
    }
    assertEquals(trimString(conf.S_CONFIGURATION + ":"
                            + conf.S_CONFIGURATION_NAME + ":"
                            + conf.getName() + " "
                            + conf.S_POPULATION_SIZE + ":"
                            + conf.getPopulationSize() + " "
                            + conf.S_MINPOPSIZE + ":"
                            + conf.getMinimumPopSizePercent() + " "
                            + conf.S_CHROMOSOME_SIZE + ":"
                            + conf.getChromosomeSize() + " "
                            + conf.S_SAMPLE_CHROM + ":"
                            + conf.S_SIZE + ":"
                            + conf.getSampleChromosome().size() + " "
                            + conf.S_TOSTRING + ":"
                            + conf.getSampleChromosome().toString() + " "
                            + conf.S_RANDOM_GENERATOR + ":"
                            + conf.getRandomGenerator().getClass().getName() +
                            " "
                            + conf.S_EVENT_MANAGER + ":"
                            + eventmgr + " "
                            + conf.S_CONFIGURATION_HANDLER + ":"
                            + conf.getConfigurationHandler().getName() + " "
                            + conf.S_FITNESS_FUNCTION + ":"
                            + conf.getFitnessFunction().getClass().getName() +
                            " "
                            + conf.S_FITNESS_EVALUATOR + ":"
                            + conf.getFitnessEvaluator().getClass().getName() +
                            " "
                            + conf.S_GENETIC_OPERATORS + ":"
                            + genops + " "
                            + conf.S_NATURAL_SELECTORS + "(" + conf.S_PRE + ")" +
                            ":"
                            + natselsPre + " "
                            + conf.S_NATURAL_SELECTORS + "(" + conf.S_POST +
                            ")" + ":"
                            + natselsPost + " "
                            ), s);
  }

  /**
   *
   * @param s1 String
   * @return String
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  private String trimString(String s1) {
    String result = s1;
    int index = 0;
    while (index < result.length()) {
      if (result.charAt(index) == '\n') {
        result = result.substring(0, index) + result.substring(index + 1);
      }
      else {
        if (result.charAt(index) == ':') {
          if (index < result.length() - 1) {
            if (result.charAt(index + 1) == ' ' ||
                result.charAt(index + 1) == '\n') {
              result = result.substring(0, index + 1) +
                  result.substring(index + 2);
            }
            else {
              index++;
            }
          }
          else {
            index++;
          }
        }
        else if (result.charAt(index) == ' ') {
          if (index == 0) {
            result = result.substring(1);
          }
          else if (index == result.length() - 1) {
            result = result.substring(0, result.length() - 1);
          }
          else
          if (result.charAt(index + 1) == ' ' ||
              result.charAt(index + 1) == '\n') {
            result = result.substring(0, index) + result.substring(index + 1);
          }
          else {
            index++;
          }
        }
        else {
          index++;
        }
      }
    }
    return result;
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetName_0() {
    Configuration conf = new Configuration("tEstName");
    assertEquals("tEstName", conf.getName());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetName_1() {
    Configuration conf = new Configuration("tEstName");
    conf.setName("HallI");
    assertEquals("HallI", conf.getName());
    conf.setName("HallA");
    assertEquals("HallA", conf.getName());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetName_2() {
    Configuration conf = new Configuration();
    assertEquals(null, conf.getName());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testKeepPopSizeConstant_0() {
    Configuration conf = new Configuration();
    assertEquals(true, conf.isKeepPopulationSizeConstant());
    conf.setKeepPopulationSizeConstant(false);
    assertEquals(false, conf.isKeepPopulationSizeConstant());
    conf.setKeepPopulationSizeConstant(true);
    assertEquals(true, conf.isKeepPopulationSizeConstant());
    conf.setKeepPopulationSizeConstant(false);
    assertEquals(false, conf.isKeepPopulationSizeConstant());
  }

}
class TestBulkFitnessFunction
    extends BulkFitnessFunction {
  public void evaluate(Population a_subjects) {
  }
}
