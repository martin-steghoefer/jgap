/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import org.jgap.*;
import java.util.*;

/**
 * Configuration that allows for permutating several components of it for
 * evaluation/auditing purposes.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class PermutingConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  private List m_randomGeneratorSlots;

  private int m_randomGeneratorIndex;

  private List m_naturalSelectorSlots;

  private int m_naturalSelectorIndex;

  private List m_geneticOperatorSlots;

  private int m_geneticOperatorIndex;

  private List m_fitnessFunctionSlots;

  private int m_fitnessFunctionIndex;

  private int m_componentIndex;

  /**
   * The resulting configuration as determined by permutation.
   */
  private Configuration m_configuration;

  public PermutingConfiguration() {
    super();
    init();
  }

  public void init() {
    m_randomGeneratorSlots = new Vector();
    m_naturalSelectorSlots = new Vector();
    m_geneticOperatorSlots = new Vector();
    m_fitnessFunctionSlots = new Vector();
    m_randomGeneratorIndex = 0;
    m_naturalSelectorIndex = 0;
    m_geneticOperatorIndex = 0;
    m_fitnessFunctionIndex = 0;
    m_componentIndex = 0;
  }

  /**
   * Initializes the configuration by preselecting important parameters from
   * the input configuration object
   * @param a_conf Configuration
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public PermutingConfiguration(Configuration a_conf)
      throws InvalidConfigurationException {
    this();
    setEventManager(a_conf.getEventManager());
    setFitnessEvaluator(a_conf.getFitnessEvaluator());
    if (a_conf.getFitnessFunction() != null) {
      setFitnessFunction(a_conf.getFitnessFunction());
    }
    setMinimumPopSizePercent(a_conf.getMinimumPopSizePercent());
    if (a_conf.getPopulationSize() > 0) {
      setPopulationSize(a_conf.getPopulationSize());
    }
    if (a_conf.getSampleChromosome() != null) {
      setSampleChromosome(a_conf.getSampleChromosome());
    }
    setRandomGenerator(a_conf.getRandomGenerator());
    if (a_conf.getChromosomePool() != null) {
      setChromosomePool(a_conf.getChromosomePool());
    }
  }

  public void addRandomGeneratorSlot(RandomGenerator a_randomGenerator) {
    m_randomGeneratorSlots.add(a_randomGenerator);
  }

  public void addNaturalSelector(NaturalSelector a_naturalSel, boolean a_egal) {
    throw new UnsupportedOperationException(
        "Use addNaturalSelectorSlot instead!");
  }

  public void addNaturalSelectorSlot(NaturalSelector a_naturalSelector) {
    m_naturalSelectorSlots.add(a_naturalSelector);
  }

  public synchronized void addGeneticOperator(GeneticOperator a_geneticOp) {
    throw new UnsupportedOperationException(
        "Use addGeneticOperatorSlot instead!");
  }

  public void addGeneticOperatorSlot(GeneticOperator a_geneticOperator) {
    m_geneticOperatorSlots.add(a_geneticOperator);
  }

  public void addFitnessFunctionSlot(FitnessFunction a_fitnessFunction) {
    m_fitnessFunctionSlots.add(a_fitnessFunction);
  }

  public Configuration next()
      throws InvalidConfigurationException {
    m_configuration = new Configuration();
    m_configuration.setEventManager(getEventManager());
    m_configuration.setFitnessEvaluator(getFitnessEvaluator());
    if (getFitnessFunction() != null) {
      m_configuration.resetProperty(Configuration.PROPERTY_FITFUNC_INST);
      setFitnessFunction(getFitnessFunction());
    }
    m_configuration.setMinimumPopSizePercent(getMinimumPopSizePercent());
    if (getPopulationSize() > 0) {
      m_configuration.setPopulationSize(getPopulationSize());
    }
    if (getSampleChromosome() != null) {
      m_configuration.setSampleChromosome(getSampleChromosome());
    }
    m_configuration.setRandomGenerator(getRandomGenerator());
    if (getChromosomePool() != null) {
      m_configuration.setChromosomePool(getChromosomePool());
    }
    List list;
    Iterator it;
    /**@todo make permutation below computed dynamic and not static*/
    /**@todo introduce new parameters: populationSize,
     * setPreservFittestIndividual, MAX_ALLOWED_EVOLUTIONS
     */

    // Permute GeneticOperator's.
    // --------------------------
    if (m_geneticOperatorIndex >=
        Math.pow(2, m_geneticOperatorSlots.size()) - 1) {
//      m_componentIndex++;
//    }
//    if (bitSet(m_componentIndex, 0)) {
      m_geneticOperatorIndex = 0;
      m_naturalSelectorIndex++;
//      m_randomGeneratorIndex = 0;
//      m_fitnessFunctionIndex = 0;
    }
    list = nextList(m_geneticOperatorIndex++, m_geneticOperatorSlots);
    it = list.iterator();
    GeneticOperator op;
    while (it.hasNext()) {
      op = (GeneticOperator) it.next();
      m_configuration.addGeneticOperator(op);
    }
    // Permute NaturalSelector's.
    // --------------------------
    if (m_naturalSelectorIndex >=
        Math.pow(2, m_naturalSelectorSlots.size()) - 1) {
//      m_componentIndex++;
//    }
//    if (bitSet(m_componentIndex, 1)) {
      m_naturalSelectorIndex = 0;
      m_randomGeneratorIndex++;
//      m_fitnessFunctionIndex = 0;
    }
    list = nextList(m_naturalSelectorIndex, m_naturalSelectorSlots);
    it = list.iterator();
    NaturalSelector ns;
    while (it.hasNext()) {
      ns = (NaturalSelector) it.next();
      m_configuration.addNaturalSelector(ns, true);
      /**@todo allow for "false"*/
    }
    // Permute RandomGenerator's.
    // --------------------------
//    if (true || bitSet(m_componentIndex, 2)) {
    m_randomGeneratorIndex++;
    if (m_randomGeneratorIndex >= m_randomGeneratorSlots.size()) {
      m_randomGeneratorIndex = 0;
      m_fitnessFunctionIndex++;
    }
//    }
    RandomGenerator rg = (RandomGenerator) m_randomGeneratorSlots.get(
        m_randomGeneratorIndex);
    m_configuration.setRandomGenerator(rg);
    // Permute FitnessFunction's.
    // --------------------------
//    if (true || bitSet(m_componentIndex, 3)) {
    m_fitnessFunctionIndex++;
    if (m_fitnessFunctionIndex >= m_fitnessFunctionSlots.size()) {
      m_fitnessFunctionIndex = 0;
    }
//    }
    /**@todo BulkFitnessOffsetRemover vs. FitnessFunction*/

//    System.err.println(m_fitnessFunctionIndex+" / "+index++);
    FitnessFunction ff = (FitnessFunction) m_fitnessFunctionSlots.get(
        m_fitnessFunctionIndex);
    m_configuration.reset();
    m_configuration.setFitnessFunction(ff);
    m_componentIndex++;
    return m_configuration;
  }

  /**
   * Returns a subset of a given list acording to the index given.
   * If a bit in the binary number represented by the index is set then the
   * element at this index in the list will be included in the result list
   * @param index int
   * @param list List
   * @return List
   */
  private List nextList(int index, List list) {
    if (index <= 0) {
      index = 1;
    }
    else {
      index++;
    }
    List newList = new Vector();
    for (int i = 0; i < list.size(); i++) {
      if ( (index & (int) Math.pow(2, i)) > 0) {
        newList.add(list.get(i));
      }
    }
    return newList;
  }

//  private boolean bitSet(int number, int bitIndex) {
//    return ( (number & (int) Math.pow(2, (bitIndex))) > 0);
//  }

  public boolean hasNext() {
    double r = (m_randomGeneratorSlots.size())
        * (m_fitnessFunctionSlots.size())
        * (Math.pow(2, m_naturalSelectorSlots.size()) - 1)
        * (Math.pow(2, m_geneticOperatorSlots.size()) - 1);
    return m_componentIndex < r;
  }
}
