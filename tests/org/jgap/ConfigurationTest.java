package org.jgap;

import org.jgap.event.*;
import org.jgap.impl.*;
import junit.framework.*;


/**
 * <p>Title: Tests for Configuration class</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class ConfigurationTest extends TestCase {

  public ConfigurationTest() {
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

  public void testAddGeneticOperator_2() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(1, conf.getGeneticOperators().size());
    conf.addGeneticOperator(new MutationOperator());
    assertEquals(2, conf.getGeneticOperators().size());
  }

  public void testVerifyStateIsValid_0() throws InvalidConfigurationException {
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

  public void testVerifyStateIsValid_1() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    try {
      conf.verifyStateIsValid();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testVerifyStateIsValid_2() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    try {
      conf.verifyStateIsValid();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testVerifyStateIsValid_3() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    conf.setRandomGenerator(new StockRandomGenerator());
    try {
      conf.verifyStateIsValid();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testVerifyStateIsValid_4() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    try {
      conf.verifyStateIsValid();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testVerifyStateIsValid_5() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    try {
      conf.verifyStateIsValid();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  public void testVerifyStateIsValid_6() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    conf.verifyStateIsValid();
  }


  public void testIsLocked_0() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    conf.setFitnessFunction(new StaticFitnessFunction(2));
    Gene gene = new BooleanGene();
    conf.setSampleChromosome(new Chromosome(gene, 5));
    conf.setNaturalSelector(new WeightedRouletteSelector());
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new MutationOperator());
    conf.setPopulationSize(1);
    assertEquals(false,conf.isLocked());
    conf.lockSettings();
    assertEquals(true,conf.isLocked());
  }

  public void testGetter_0() throws InvalidConfigurationException {
    Configuration conf = new Configuration();
    assertEquals(false, conf.isLocked());
    FitnessFunction fitFunc = new StaticFitnessFunction(2);
    conf.setFitnessFunction(fitFunc);
    Gene gene = new BooleanGene();
    Chromosome sample = new Chromosome(gene, 55);
    conf.setSampleChromosome(sample);
    NaturalSelector natSel = new WeightedRouletteSelector();
    conf.setNaturalSelector(natSel);
    RandomGenerator randGen = new StockRandomGenerator();
    conf.setRandomGenerator(randGen);
    EventManager evMan = new EventManager();
    conf.setEventManager(evMan);
    GeneticOperator mutOp = new MutationOperator();
    conf.addGeneticOperator(mutOp);
    GeneticOperator repOp = new ReproductionOperator();
    conf.addGeneticOperator(repOp);
    GeneticOperator croOp = new CrossoverOperator();
    conf.addGeneticOperator(croOp);
    conf.setPopulationSize(7);
    assertEquals(fitFunc, conf.getFitnessFunction());
    assertEquals(natSel, conf.getNaturalSelector());
    assertEquals(randGen, conf.getRandomGenerator());
    assertEquals(sample, conf.getSampleChromosome());
    assertEquals(evMan, conf.getEventManager());
    assertEquals(7,conf.getPopulationSize());
    assertEquals(3, conf.getGeneticOperators().size());
    assertEquals(mutOp, conf.getGeneticOperators().get(0));
    assertEquals(repOp, conf.getGeneticOperators().get(1));
    assertEquals(croOp, conf.getGeneticOperators().get(2));
  }

}