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

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;

public class GPConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Object m_state;

  /**
   * The probability that a crossover operation is chosen during evolution. Must
   * be between 0.0d and 1.0d, inclusive.
   */
  private double crossoverProb = 0.9d;

  /**
   * The probability that a reproduction operation is chosen during evolution.
   * Must be between 0.0d and 1.0d. crossoverProb + reproductionProb must equal
   * 1.0d.
   */
  private double reproductionProb = 0.1d;

  /**
   * The maximum depth of an individual resulting from crossover.
   */
  private int maxCrossoverDepth = 17;

  /**
   * The maximum depth of an individual when the world is created.
   */
  private int maxInitDepth = 6;

  /**
   * The method of choosing an individual to perform an evolution operation on.
   */
  private INaturalGPSelector selectionMethod;

  private CrossMethod crossMethod;

  /**
   * @throws InvalidConfigurationException
   */
  public GPConfiguration()
      throws InvalidConfigurationException {
    super();
    crossMethod = new BranchTypingCross(this);
    selectionMethod = new FitnessProportionateSelection();
    setEventManager(new EventManager());
    setRandomGenerator(new StockRandomGenerator());
//    setFitnessEvaluator(new DefaultFitnessEvaluator());
    setFitnessEvaluator(new DeltaFitnessEvaluator());
  }

  public Object getState() {
    return m_state;
  }

  public void setState(Object a_state) {
    m_state = a_state;
  }

  public synchronized void verifyStateIsValid()
      throws InvalidConfigurationException {
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
    return crossoverProb;
  }

  public void setCrossoverProb(float crossoverProb) {
    this.crossoverProb = crossoverProb;
  }

  public double getReproductionProb() {
    return reproductionProb;
  }

  public void setReproductionProb(float reproductionProb) {
    this.reproductionProb = reproductionProb;
  }

  public int getMaxCrossoverDepth() {
    return maxCrossoverDepth;
  }

  public void setMaxCrossoverDepth(int maxCrossoverDepth) {
    this.maxCrossoverDepth = maxCrossoverDepth;
  }

  public INaturalGPSelector getSelectionMethod() {
    return selectionMethod;
  }

  public CrossMethod getCrossMethod() {
    return crossMethod;
  }

  public int getMaxInitDepth() {
    return maxInitDepth;
  }

  public void setMaxInitDepth(int a_maxDepth) {
    maxInitDepth = a_maxDepth;
  }
}
