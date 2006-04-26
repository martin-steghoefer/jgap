package org.jgap.gp;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;

public class GPConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private ILanguage m_language;

  private Object m_state;

  /**
   * The probability that a crossover operation is chosen during evolution. Must be
   * between 0.0f and 1.0f, inclusive. The default value is 0.9f.
   * JG
   */
  private float crossoverProb = 0.9f;

  /**
   * The probability that a reproduction operation is chosen during evolution. Must be
   * between 0.0f and 1.0f, inclusive. The default value is 0.1f. In the current version
   * of JGProg, crossoverProb + reproductionProb must equal 1.0f. Other operations may
   * be added in the future.
   * JG
   */
  private float reproductionProb = 0.1f;

  /**
   * The maximum depth of an individual resulting from crossover. The default value
   * is 17.
   * JG
   */
  private int maxCrossoverDepth = 17;

  /**
   * The maximum depth of an individual when the world is created.
   * JG
   */
  private int maxInitDepth = 6;

  //JG
  private int maxChromosomes = 4;

  //JG
  private int minChromosomes = 1;

  /**
   * The method of choosing an individual to perform an evolution operation on.
   */
  protected static SelectionMethod selectionMethod = new
      FitnessProportionateSelection();

  /**@todo use correct config!!!!!!!*/
  protected static CrossMethod crossMethod;

  /**
   * @param a_language default language for all commands. But for each command
   * a different language can be set!
   */
  public GPConfiguration(ILanguage a_language)
      throws InvalidConfigurationException {
    super();
    crossMethod = new BranchTypingCross(this);
    setEventManager(new EventManager());
    setRandomGenerator(new StockRandomGenerator());
//    setFitnessEvaluator(new DefaultFitnessEvaluator());
    setFitnessEvaluator(new DeltaFitnessEvaluator());
    m_language = a_language;
  }

  /**@todo needed?*/
  public ILanguage getLanguage() {
    return m_language;
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

  //Selection sollte kein Problem sein
//  public void addNaturalSelector(NaturalSelector a_selector,
//                                 boolean processBeforeGeneticOperators)
//      throws InvalidConfigurationException {
//    throw new UnsupportedOperationException();
//  }
  public synchronized void addGeneticOperator(GeneticOperator a_operatorToAdd)
      throws InvalidConfigurationException {
    throw new UnsupportedOperationException(
        "Use addGeneticOperator(GPGeneticOperator) instead!");
  }

  public synchronized void addGeneticOperator(GPGeneticOperator a_operatorToAdd)
      throws InvalidConfigurationException {
    /**@todo impl*/
  }

  public float getCrossoverProb() {
    return crossoverProb;
  }

  public void setCrossoverProb(float crossoverProb) {
    this.crossoverProb = crossoverProb;
  }

  public float getReproductionProb() {
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

  public SelectionMethod getSelectionMethod() {
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
