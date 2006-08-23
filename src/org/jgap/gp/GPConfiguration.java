/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.distr.*;
import org.jgap.event.*;

/**
 * Configuration for a GP.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * References the current fitness function that will be used to evaluate
   * chromosomes during the natural selection process. Note that only this
   * or the bulk fitness function may be set - the two are mutually exclusive.
   */
  private GPFitnessFunction m_objectiveFunction;


  /**
   * Internal stack, see PushCommand for example.
   */
  private Stack m_stack = new Stack();

  /**
   * Internal memory, see StoreTerminalCommand for example.
   */
  private Culture m_memory = new Culture(20);

  /**
   * The probability that a crossover operation is chosen during evolution. Must
   * be between 0.0d and 1.0d, inclusive.
   */
  private double m_crossoverProb = 0.9d;

  /**
   * The probability that a reproduction operation is chosen during evolution.
   * Must be between 0.0d and 1.0d. crossoverProb + reproductionProb must equal
   * 1.0d.
   */
  private double m_reproductionProb = 0.1d;

  /**
   * The maximum depth of an individual resulting from crossover.
   */
  private int m_maxCrossoverDepth = 17;

  /**
   * The maximum depth of an individual when the world is created.
   */
  private int m_maxInitDepth = 7;

  /**
   * The method of choosing an individual to perform an evolution operation on.
   */
  private INaturalGPSelector m_selectionMethod;

  /**
   * The method of crossing over two individuals during evolution.
   */
  private CrossMethod m_crossMethod;

  /**
   * @throws InvalidConfigurationException
   */
  public GPConfiguration()
      throws InvalidConfigurationException {
    super();
    m_crossMethod = new BranchTypingCross(this);
    m_selectionMethod = new FitnessProportionateSelection();
    setEventManager(new EventManager());
    setRandomGenerator(new StockRandomGenerator());
//    setFitnessEvaluator(new DefaultFitnessEvaluator());
    setFitnessEvaluator(new DeltaFitnessEvaluator());
  }

  public synchronized void verifyStateIsValid()
      throws InvalidConfigurationException {
    // Do nothing in here.
    // -------------------
  }

  public synchronized void addGeneticOperator(GeneticOperator a_operatorToAdd)
      throws InvalidConfigurationException {
    throw new UnsupportedOperationException(
        "Use addGeneticOperator(GPGeneticOperator) instead!");
  }

  public synchronized void addGeneticOperator(GPGeneticOperator a_operatorToAdd)
      throws InvalidConfigurationException {
    /**@todo impl*/
  }

  public double getCrossoverProb() {
    return m_crossoverProb;
  }

  public void setCrossoverProb(float a_crossoverProb) {
    m_crossoverProb = a_crossoverProb;
  }

  public double getReproductionProb() {
    return m_reproductionProb;
  }

  public void setReproductionProb(float a_reproductionProb) {
    m_reproductionProb = a_reproductionProb;
  }

  public int getMaxCrossoverDepth() {
    return m_maxCrossoverDepth;
  }

  public void setMaxCrossoverDepth(int a_maxCrossoverDepth) {
    m_maxCrossoverDepth = a_maxCrossoverDepth;
  }

  public INaturalGPSelector getSelectionMethod() {
    return m_selectionMethod;
  }

  public CrossMethod getCrossMethod() {
    return m_crossMethod;
  }

  public int getMaxInitDepth() {
    return m_maxInitDepth;
  }

  public void setMaxInitDepth(int a_maxDepth) {
    m_maxInitDepth = a_maxDepth;
  }

  public void pushToStack(Object a_value) {
    m_stack.push(a_value);
  }

  public Object popFromStack() {
    return m_stack.pop();
  }

  public Object peekStack() {
    return m_stack.peek();
  }

  public int stackSize() {
    return m_stack.size();
  }

  public void clearStack() {
    m_stack.clear();
  }

  /**
   * Stores a value in the internal memory
   * @param a_name named index of the memory cell
   * @param a_value the value to store
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void storeInMemory(String a_name, Object a_value) {
    m_memory.set(a_name, a_value, -1);
  }

  /**
   * Reads a value from the internal memory.
   * @param a_name named index of the memory cell to read out
   * @return read value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object readFromMemory(String a_name) {
    return m_memory.get(a_name).getCurrentValue();
  }

  /**
   * Clears the memory.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void clearMemory() {
    m_memory.clear();
  }

  public void storeThruput(int a_index, Object a_value) {
    m_memory.set("thruput" + a_index, a_value, -1); /**@todo do it cleaner*/
  }

  public Object readThruput(int a_index) {
    return m_memory.get("thruput" + a_index); /**@todo do it cleaner*/
  }

  public GPFitnessFunction getGPFitnessFunction() {
    return m_objectiveFunction;
  }

  /**
   * Sets the fitness function to be used for this genetic algorithm.
   * The fitness function is responsible for evaluating a given
   * Chromosome and returning a positive integer that represents its
   * worth as a candidate solution. These values are used as a guide by the
   * natural to determine which Chromosome instances will be allowed to move
   * on to the next round of evolution, and which will instead be eliminated.
   * <p>
   * Note that it is illegal to set both this fitness function and a bulk
   * fitness function. Although one or the other must be set, the two are
   * mutually exclusive.
   *
   * @param a_functionToSet fitness function to be used
   *
   * @throws InvalidConfigurationException if the fitness function is null, a
   * bulk fitness function has already been set, or if this Configuration
   * object is locked.
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public synchronized void setFitnessFunction(GPFitnessFunction a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given fitness function isn't null.
    // -------------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The FitnessFunction instance may not be null.");
    }
    // Make sure the bulk fitness function hasn't already been set.
    // ------------------------------------------------------------
    /**@todo re-add*/
//    if (m_bulkObjectiveFunction != null) {
//      throw new InvalidConfigurationException(
//          "The bulk fitness function and normal fitness function " +
//          "may not both be set.");
//    }
    // Ensure that no other fitness function has been set in a different
    // configuration object within the same thread!
    // -----------------------------------------------------------------
    checkProperty(PROPERTY_FITFUNC_INST, a_functionToSet,
                  "Fitness function has already been set differently.");
    m_objectiveFunction = a_functionToSet;
  }
}
