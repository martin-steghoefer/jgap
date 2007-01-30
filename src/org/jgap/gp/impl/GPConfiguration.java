/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.util.*;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.distr.*;
import org.jgap.event.*;
import org.jgap.gp.*;

/**
 * Configuration for a GP.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.17 $";

  /**
   * References the current fitness function that will be used to evaluate
   * chromosomes during the natural selection process.
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
   * Percentage of the population that will be filled with new individuals
   * during evolution. Must be between 0.0d and 1.0d.
   */
  private double m_newChromsPercent = 0.3d;

  /**
   * In crossover: If random number (0..1) < this value, then choose a function
   * otherwise a terminal
   */
  private double m_functionProb = 0.9d;

  /**
   * The maximum depth of an individual resulting from crossover.
   */
  private int m_maxCrossoverDepth = 17;

  /**
   * The maximum depth of an individual when the world is created.
   */
  private int m_maxInitDepth = 7;

  /**
   * The minimum depth of an individual when the world is created.
   */
  private int m_minInitDepth = 3;

  /**
   * The method of choosing an individual to perform an evolution operation on.
   */
  private INaturalGPSelector m_selectionMethod;

  /**
   * The method of crossing over two individuals during evolution.
   */
  private CrossMethod m_crossMethod;

  /**
   * True: Set of available functions must contain any "type of function" that
   * may be needed during construction of a new program. A "type of function"
   * is, for instance, a terminal with return type
   * <code>CommandGene.IntegerClass</code>.
   */
  private boolean m_strictProgramCreation;

  /**
   * If m_strictProgramCreation is false: Maximum number of tries to construct
   * a valid program.
   */
  private int m_programCreationMaxTries = 5;

  /**
   * The fitness evaluator. See interface IGPFitnessEvaluator for details.
   */
  private IGPFitnessEvaluator m_fitnessEvaluator;

  private INodeValidator m_nodeValidator;

  /**
   * Internal flag to display a warning only once, in case a program could not
   * be evolved with the allowed maximum number of nodes.
   *
   * @since 3.2
   */
  private boolean m_warningPrinted;

  /**
   * Prototype of a valid program. May be cloned if needed (do not reference
   * it!)
   *
   * @since 3.2
   */
  private IGPProgram m_prototypeProgram;

  /**
   * Constructor utilizing the FitnessProportionateSelection.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPConfiguration()
      throws InvalidConfigurationException {
    super();
    init();
    m_selectionMethod = new FitnessProportionateSelection();
  }

  public GPConfiguration(String a_id, String a_name)
      throws InvalidConfigurationException {
    super(a_id, a_name);
    init();
    m_selectionMethod = new FitnessProportionateSelection();
  }

  /**
   * Sets a GP fitness evaluator, such as
   * org.jgap.gp.impl.DefaultGPFitnessEvaluator.
   *
   * @param a_evaluator the fitness evaluator to set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void setGPFitnessEvaluator(IGPFitnessEvaluator a_evaluator) {
    m_fitnessEvaluator = a_evaluator;
  }

  /**
   * Helper for construction.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  protected void init()
      throws InvalidConfigurationException {
    m_crossMethod = new BranchTypingCross(this);
    setEventManager(new EventManager());
    setRandomGenerator(new StockRandomGenerator());
    setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
  }

  /**
   * Constructor utilizing the FitnessProportionateSelection.
   *
   * @param a_selectionMethod the selection method to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public GPConfiguration(INaturalGPSelector a_selectionMethod)
      throws InvalidConfigurationException {
    super();
    init();
    m_selectionMethod = a_selectionMethod;
  }

  /**
   * Sets the selection method to use.
   * @param a_method the selection method to use
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void setSelectionMethod(INaturalGPSelector a_method) {
    if (a_method == null) {
      throw new IllegalArgumentException("Selection method must not be null");
    }
    m_selectionMethod = a_method;
  }

  /**
   * Sets the crossover method to use.
   * @param a_method the crossover method to use
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void setCrossoverMethod(CrossMethod a_method) {
    if (a_method == null) {
      throw new IllegalArgumentException("Crossover method must not be null");
    }
    m_crossMethod = a_method;
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

//  /**@todo implement something like that*/
//  public synchronized void addGeneticOperator(IGPGeneticOperator a_operatorToAdd)
//      throws InvalidConfigurationException {
//  }

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

  /**
   * @param a_functionProb probability that a function is choosen in crossing
   * over (between 0 and 1)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setFunctionProb(double a_functionProb) {
    m_functionProb = a_functionProb;
  }

  /**
   * @return probability that a function is choosen in crossing over
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public double getFunctionProb() {
    return m_functionProb;
  }

  public void setNewChromsPercent(double a_newChromsPercent) {
    m_newChromsPercent = a_newChromsPercent;
  }

  public double getNewChromsPercent() {
    return m_newChromsPercent;
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

  public int getMinInitDepth() {
    return m_minInitDepth;
  }

  public void setMinInitDepth(int a_minDepth) {
    m_minInitDepth = a_minDepth;
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
   * Stores a value in the internal memory.
   *
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
   * Stores a value in the internal matrix memory.
   *
   * @param a_x the first coordinate of the matrix (width)
   * @param a_y the second coordinate of the matrix (height)
   * @param a_value the value to store
   * @return created or used memory cell
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CultureMemoryCell storeMatrixMemory(int a_x, int a_y, Object a_value) {
    return m_memory.setMatrix(a_x, a_y, a_value);
  }

  /**
   * Reads a value from the internal matrix memory.
   *
   * @param a_x the first coordinate of the matrix (width)
   * @param a_y the second coordinate of the matrix (height)
   * @return read value
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object readMatrixMemory(int a_x, int a_y) {
    return m_memory.getMatrix(a_x, a_y).getCurrentValue();
  }

  /**
   * Reads a value from the internal memory.
   *
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
   *
   * @param a_functionToSet fitness function to be used
   *
   * @throws InvalidConfigurationException if the fitness function is null, or
   * if this Configuration object is locked.
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
    // Ensure that no other fitness function has been set in a different
    // configuration object within the same thread!
    // -----------------------------------------------------------------
    checkProperty(PROPERTY_FITFUNC_INST, a_functionToSet,
                  "Fitness function has already been set differently.");
    m_objectiveFunction = a_functionToSet;
  }

  public boolean isStrictProgramCreation() {
    return m_strictProgramCreation;
  }

  public void setStrictProgramCreation(boolean a_strict) {
    m_strictProgramCreation = a_strict;
  }

  public int getProgramCreationMaxtries() {
    return m_programCreationMaxTries;
  }

  public void setProgramCreationMaxTries(int a_maxtries) {
    m_programCreationMaxTries = a_maxtries;
  }

  /**
   * @return the fitness evaluator set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IGPFitnessEvaluator getGPFitnessEvaluator() {
    return m_fitnessEvaluator;
  }

  /**
   * Validates a_node in the context of a_chrom. Considers the recursion level
   * (a_recursLevel), the type needed (a_type) for the node, the functions
   * available (a_functionSet) and the depth of the whole chromosome needed
   * (a_depth), and whether grow mode is used (a_grow is true) or not.
   *
   * @param a_chrom the chromosome that will contain the node, if valid
   * @param a_node the node selected and to be validated
   * @param a_tries number of times the validator has been called, useful for
   * stopping by returning true if the number exceeds a limit
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_recurseLevel level of recursion
   * @param a_type the return type of the node needed
   * @param a_functionSet the array of available functions
   * @param a_depth the needed depth of the program chromosome
   * @param a_grow true: use grow mode, false: use full mode
   * @return true: node is valid; false: node is invalid
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean validateNode(ProgramChromosome a_chrom, CommandGene a_node,
                              int a_tries, int a_num, int a_recurseLevel,
                              Class a_type, CommandGene[] a_functionSet,
                              int a_depth, boolean a_grow) {
    if (m_nodeValidator == null) {
      return true;
    }
    return m_nodeValidator.validate(a_chrom, a_node, a_tries, a_num,
                                    a_recurseLevel, a_type, a_functionSet,
                                    a_depth, a_grow);
  }

  /**
   * Sets the node validator. Also see method validateNode.
   *
   * @param a_nodeValidator sic
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setNodeValidator(INodeValidator a_nodeValidator) {
    m_nodeValidator = a_nodeValidator;
  }

  /**
   * @return the node validator set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public INodeValidator getNodeValidator() {
    return m_nodeValidator;
  }

  /**
   * Compares this entity against the specified object.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      return false;
    }
  }

  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      GPConfiguration other = (GPConfiguration) a_other;
      return new CompareToBuilder()
          .append(m_objectiveFunction, other.m_objectiveFunction)
          .append(m_crossoverProb, other.m_crossoverProb)
          .append(m_reproductionProb, other.m_reproductionProb)
          .append(m_newChromsPercent, other.m_newChromsPercent)
          .append(m_maxCrossoverDepth, other.m_maxCrossoverDepth)
          .append(m_maxInitDepth, other.m_maxInitDepth)
          .append(m_selectionMethod.getClass(), other.m_selectionMethod.getClass())
          .append(m_crossMethod.getClass(), other.m_crossMethod.getClass())
          .append(m_programCreationMaxTries, other.m_programCreationMaxTries)
          .append(m_strictProgramCreation, other.m_strictProgramCreation)
          .append(m_fitnessEvaluator.getClass(), other.m_fitnessEvaluator.getClass())
          .toComparison();
    }
  }

  /**
   *
   * @return see ProgramChromosome.growOrFull(...) and GPGenotype.evolve()
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean isMaxNodeWarningPrinted() {
    return m_warningPrinted;
  }
  /**
   * See ProgramChromosome.growOrFull(...) and GPGenotype.evolve().
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void flagMaxNodeWarningPrinted() {
    m_warningPrinted = true;
  }

  /**
   *
   * @param a_program IGPProgram
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setPrototypeProgram(IGPProgram a_program) {
    m_prototypeProgram = a_program;
  }

  /**
   * @return prototype program set (maybe null if not setted previously)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IGPProgram getPrototypeProgram() {
    return m_prototypeProgram;
  }
}
/**@todo introduce lock*/
