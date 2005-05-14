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
import junitx.util.*;

/**
 * Tests for Configuration class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ConfigurationTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(ConfigurationTest.class);
    return suite;
  }

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

  public void testAddGeneticOperator_2()
      throws
      InvalidConfigurationException {
    Configuration conf = new Configuration();
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(1, conf.getGeneticOperators().size());
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(2, conf.getGeneticOperators().size());
  }

  public void testVerifyStateIsValid_0()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_1()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_2()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_3()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_4()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_5()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_6()
      throws
      InvalidConfigurationException {
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

  public void testVerifyStateIsValid_7()
      throws
      InvalidConfigurationException {
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
    EventManager evMan = new EventManager();
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

  public void testAddNaturalSelector_0()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, true);
    assertEquals(selector, conf.getNaturalSelectors(true).get(0));
  }

  public void testAddNaturalSelector_1()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, false);
    assertEquals(selector, conf.getNaturalSelectors(false).get(0));
  }

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

  public void testAddNaturalSelector_3()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, false);
    conf.getNaturalSelectors(false).clear();
    conf.addNaturalSelector(selector, false);
    Integer i = (Integer) PrivateAccessor.getField(conf,
        "m_sizeNaturalSelectorsPost");
    assertEquals(1, i.intValue());
  }

  public void testAddNaturalSelector_4()
      throws Exception {
    Configuration conf = new Configuration();
    NaturalSelector selector = new WeightedRouletteSelector();
    conf.addNaturalSelector(selector, true);
    conf.getNaturalSelectors(true).clear();
    conf.addNaturalSelector(selector, true);
    Integer i = (Integer) PrivateAccessor.getField(conf,
        "m_sizeNaturalSelectorsPre");
    assertEquals(1, i.intValue());
  }

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

  public void testSetPreserveFittestIndividual_0() {
    Configuration conf = new Configuration();
    assertFalse(conf.isPreserveFittestIndividual());
    conf.setPreservFittestIndividual(true);
    assertTrue(conf.isPreserveFittestIndividual());
    conf.setPreservFittestIndividual(false);
    assertFalse(conf.isPreserveFittestIndividual());
  }

  public void testGenerationNr_0() {
    Configuration conf = new Configuration();
    assertEquals(0, conf.getGenerationNr());
    conf.incrementGenerationNr();
    assertEquals(1, conf.getGenerationNr());
    conf.incrementGenerationNr();
    conf.incrementGenerationNr();
    assertEquals(3, conf.getGenerationNr());
  }

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

  public void testSetBulkFitnessFunction_3()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setBulkFitnessFunction(new TestBulkFitnessFunction());
  }

  public void testGetPopulationSize_0()
      throws Exception {
    Configuration conf = new Configuration();
    assertEquals(0, conf.getPopulationSize());
    final int SIZE = 22;
    conf.setPopulationSize(SIZE);
    assertEquals(SIZE, conf.getPopulationSize());
  }

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
   * @since 2.3
   */
  public void testToString_0()
      throws Exception {
    /**@todo implement*/
  }
}

class TestFitnessFunction
    extends FitnessFunction {
  /**
   * @param a_subject Chromosome
   * @return double
   * @since 2.0 (until 1.1: return type int)
   */
  protected double evaluate(Chromosome a_subject) {
    //result does not matter here
    return 1.0000000d;
  }
}

class TestBulkFitnessFunction
    extends BulkFitnessFunction {
  public void evaluate(Population a_subjects) {
  }
}