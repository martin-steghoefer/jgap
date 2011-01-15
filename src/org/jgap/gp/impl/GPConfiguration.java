/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.distr.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.terminal.*;
import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * Configuration for a GP.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.52 $";

  /**@todo introduce lock for configuration*/
  /**
   * References the current fitness function that will be used to evaluate
   * chromosomes during the natural selection process.
   */
  private GPFitnessFunction m_objectiveFunction;

  /**
   * Internal stack, see PushCommand for example.
   */
  private /*transient*/ Stack m_stack = new Stack();

  /**
   * Internal memory, see StoreTerminalCommand for example.
   */
  private transient Culture m_memory = new Culture(50);

  private transient Hashtable<String, char[][]> m_matrices;

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
   * The probability that a node is mutated during growing a program.
   */
  private double m_mutationProb = 0.1d;

  /**
   * The probability that the arity of a node is changed during growing a
   * program.
   */
  private double m_dynArityProb = 0.08d;

  /**
   * Percentage of the population that will be filled with new individuals
   * during evolution. Must be between 0.0d and 1.0d.
   */
  private double m_newChromsPercent = 0.3d;

  /**
   * In crossover: If random number (0..1) < this value, then choose a function
   * otherwise a terminal.
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
  private int m_minInitDepth = 2;

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
   * is, for instance, a terminal with return type CommandGene.IntegerClass.
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
  private ISingleNodeValidator m_singleNodeValidator;

  /**
   * Internal flag to display a warning only once, in case a program could not
   * be evolved with the allowed maximum number of nodes.
   *
   * @since 3.2
   */
  private transient boolean m_warningPrinted;

  /**
   * Prototype of a valid program. May be cloned if needed (do not reference
   * it!).
   *
   * @since 3.2
   */
  private IGPProgram m_prototypeProgram;

  private boolean m_useProgramCache = false;

  private Map m_variables;

  private transient Map m_programCache;

  /**
   * Holds the central configurable factory for creating default objects.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  private transient IJGAPFactory m_factory;

  /**
   * For initializing GP programs before random creation.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  private IGPInitStrategy m_initStrategy;

  /**
   * TRUE: Activate methods checkErroneousPop and checkErroneousProg in class
   * GPGenotype.
   */
  private boolean m_verify;

  /**
   * TRUE: Do not clone command genes when creating a new GP program in
   * ProgramChromosome.
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  private boolean m_noCommandGeneCloning;

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
    this("", null);
  }

  public GPConfiguration(String a_id, String a_name)
      throws InvalidConfigurationException {
    super(a_id, a_name);
    init(true);
    m_selectionMethod = new TournamentSelector(3);
  }

  /**
   * Constructs a configuration with an informative name but without a unique
   * ID. This practically prevents more than one configurations to be
   * instantiated within the same thread.
   *
   * @param a_name informative name of the configuration, may be null
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   */
  public GPConfiguration(final String a_name)
      throws InvalidConfigurationException {
    this();
    setName(a_name);
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
   * @param a_fullInit true set event manager, random generator and fitness
   * evaluator to defauklt
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  protected void init(boolean a_fullInit)
      throws InvalidConfigurationException {
    /**@todo make reusable in class Configuration and reuse here from Configuration*/
    // Create factory for being able to configure the used default objects,
    // like random generators or fitness evaluators.
    // --------------------------------------------------------------------
    String clazz = System.getProperty(PROPERTY_JGAPFACTORY_CLASS);
    if (clazz != null && clazz.length() > 0) {
      try {
        m_factory = (IJGAPFactory) Class.forName(clazz).newInstance();
      } catch (Throwable ex) {
        throw new RuntimeException("Class " + clazz +
                                   " could not be instantiated" +
                                   " as type IJGAPFactory");
      }
    }
    else {
      m_factory = new JGAPFactory(false);
    }
    if (m_factory == null) {
      throw new IllegalStateException("JGAPFactory not registered!");
    }
    m_programCache = new HashMap(50);
    m_matrices = new Hashtable();
    if (a_fullInit) {
      m_variables = new Hashtable();
      m_crossMethod = new BranchTypingCross(this);
      setEventManager(new EventManager());
      setRandomGenerator(new StockRandomGenerator());
      setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
    }
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
    init(true);
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
   * @return probability for mutation of a node during growing a program
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
  public double getMutationProb() {
    return m_mutationProb;
  }

  /**
   * @param a_mutationProb probability for mutation of a node during growing a
   * program
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
  public void setMutationProb(float a_mutationProb) {
    m_mutationProb = a_mutationProb;
  }

  /**
   * @return probability for dynamizing the arity of a node during growing a
   * program
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public double getDynamizeArityProb() {
    return m_dynArityProb;
  }

  /**
   * @param a_dynArityProb probability for dynamizing the arity of a node during
   * growing a program
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void setDynamizeArityProb(float a_dynArityProb) {
    m_dynArityProb = a_dynArityProb;
  }

  /**
   * @param a_functionProb probability that a function instead of a terminal
   * is chosen in crossing over (between 0 and 1)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setFunctionProb(double a_functionProb) {
    m_functionProb = a_functionProb;
  }

  /**
   * @return probability that a function instead of a terminal is chosen in
   * crossing over
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public double getFunctionProb() {
    return m_functionProb;
  }

  public void setNewChromsPercent(double a_newChromsPercent) {
    if (m_newChromsPercent >= 1.0d) {
      throw new IllegalArgumentException(
          "Parameter value must be smaller than 1!");
    }
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
   * Creates an instance of a matrix with a unique name.
   *
   * @param a_name the name of the matrix
   * @param a_cols number of columns the matrix should have
   * @param a_rows number of rows the matrix should have
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public void createMatrix(String a_name, int a_cols, int a_rows) {
    if (a_name == null || a_name.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    if (a_cols < 1 || a_rows < 1) {
      throw new IllegalArgumentException(
          "Number of colums and rows must be greater than zero!");
    }
    char[][] m_matrix = new char[a_cols][a_rows];
    m_matrices.put(a_name, m_matrix);
  }

  /**
   * Sets a matrix field with a value.
   *
   * @param a_name the name of the matrix
   * @param a_col column in the matrix
   * @param a_row row in the matrix
   * @param a_value the value to set in the matrix at given column and row
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public void setMatrix(String a_name, int a_col, int a_row, char a_value) {
    char[][] m_matrix = m_matrices.get(a_name);
    if (m_matrix == null) {
      throw new IllegalArgumentException("Matrix with name " + a_name +
          " not found!");
    }
    m_matrix[a_col][a_row] = a_value;
  }

  /**
   * Sets a matrix field with a value.
   *
   * @param a_name the name of the matrix
   * @param a_col column in the matrix
   * @param a_row row in the matrix
   * @param a_value the value to set in the matrix at given column and row
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void setMatrix(String a_name, int a_col, int a_row, int a_value) {
    char[][] m_matrix = m_matrices.get(a_name);
    if (m_matrix == null) {
      throw new IllegalArgumentException("Matrix with name " + a_name +
          " not found!");
    }
    m_matrix[a_col][a_row] = (char)a_value;
  }

  /**
   * Resets the matrix by filling it with a given character.
   *
   * @param a_name the name of the matrix
   * @param a_filler the character to fill the whole matrix with
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public void resetMatrix(String a_name, char a_filler) {
    char[][] m_matrix = m_matrices.get(a_name);
    if (m_matrix == null) {
      throw new IllegalArgumentException("Matrix with name " + a_name +
          " not found!");
    }
    for (int col = 0; col < m_matrix.length; col++) {
      for (int row = 0; row < m_matrix[col].length; row++) {
        m_matrix[col][row] = a_filler;
      }
    }
  }

  /**
   * Reads a matrix cell and returns the value.
   *
   * @param a_name the name of the matrix
   * @param a_col the column to read
   * @param a_row the row to read
   * @return the value in the matrix
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public char readMatrix(String a_name, int a_col, int a_row) {
    char[][] m_matrix = m_matrices.get(a_name);
    if (m_matrix == null) {
      throw new IllegalArgumentException("Matrix with name " + a_name +
          " not found!");
    }
    return m_matrix[a_col][a_row];
  }

  /**
   * Retrieves a named matrix.
   *
   * @param a_name the name of the matrix
   * @return the matrix itself
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public char[][] getMatrix(String a_name) {
    char[][] m_matrix = m_matrices.get(a_name);
    return m_matrix;
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
   * @param a_name the name of the cell to evaluate
   * @return the value of a memory cell, if it exsists. Otherwise returns null.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object readFromMemoryIfExists(String a_name) {
    CultureMemoryCell cell = null;
    try {
      cell = m_memory.get(a_name);
    } catch (IllegalArgumentException iex) {
      // Memory name not found: OK.
      // --------------------------
      ;
    }
    if (cell == null) {
      return null;
    }
    return cell.getCurrentValue();
  }

  /**
   * Stores a value in the internal indexed memory.
   *
   * @param a_index index of the cell
   * @param a_value the value to store
   * @return created or used memory cell
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CultureMemoryCell storeIndexedMemory(int a_index, Object a_value) {
    return m_memory.set(a_index, a_value, -1, "noname");
  }

  /**
   * Reads a value from the internal indexed memory.
   *
   * @param a_index index of the cell
   * @return read value (maybe null )
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object readIndexedMemory(int a_index) {
    CultureMemoryCell cell = m_memory.get(a_index);
    if (cell == null) {
      return null;
    }
    else {
      return cell.getCurrentValue();
    }
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
   * Set the fitness evaluator (deciding if a given fitness value is better
   * when it's higher or better when it's lower).
   * @param a_fitnessEvaluator the FitnessEvaluator to be used
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setFitnessEvaluator(IGPFitnessEvaluator a_fitnessEvaluator) {
    setGPFitnessEvaluator(a_fitnessEvaluator);
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
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized void setFitnessFunction(GPFitnessFunction a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given fitness function isn't null.
    // -------------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The FitnessFunction instance must not be null.");
    }
    // Ensure that no other fitness function has been set in a different
    // configuration object within the same thread!
    // -----------------------------------------------------------------
    checkProperty(PROPERTY_FITFUNC_INST, a_functionToSet, m_objectiveFunction,
                  "Fitness function has already been set differently.");
    m_objectiveFunction = a_functionToSet;
  }

  /**
   * @return true: throw an error during evolution in case a situation is
   * detected where no function or terminal of a required type is declared
   * in the GPConfiguration; false: don't throw an error but try a completely
   * different combination of functions and terminals
   *
   * @author Klaus Meffert
   */
  public boolean isStrictProgramCreation() {
    return m_strictProgramCreation;
  }

  /**
   * @param a_strict true: throw an error during evolution in case a situation
   * is detected where no function or terminal of a required type is declared
   * in the GPConfiguration; false: don't throw an error but try a completely
   * different combination of functions and terminals
   *
   * @author Klaus Meffert
   */
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
   * @param a_rootNode root node of the node to be validated (may be null)
   * @param a_tries number of times the validator has been called, useful for
   * stopping by returning true if the number exceeds a limit
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_recurseLevel level of recursion
   * @param a_type the return type of the node needed
   * @param a_functionSet the array of available functions
   * @param a_depth the needed depth of the program chromosome
   * @param a_grow true: use grow mode, false: use full mode
   * @param a_childIndex index of the child in the parent node to which it
   * belongs (-1 if node is root node)
   * @param a_fullProgram true: whole program is available in a_chrom
   *
   * @return true: node is valid; false: node is invalid
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean validateNode(ProgramChromosome a_chrom, CommandGene a_node,
                              CommandGene a_rootNode, int a_tries, int a_num,
                              int a_recurseLevel, Class a_type,
                              CommandGene[] a_functionSet, int a_depth,
                              boolean a_grow, int a_childIndex,
                              boolean a_fullProgram) {
    INodeValidator nodeValidator = getNodeValidator();
    if (nodeValidator == null) {
      return true;
    }
    return nodeValidator.validate(a_chrom, a_node, a_rootNode, a_tries, a_num,
                                  a_recurseLevel, a_type, a_functionSet,
                                  a_depth, a_grow, a_childIndex,
                                  a_fullProgram);
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
   * Sets the validator for checking single nodes.
   *
   * @param a_singleNodeValidator sic
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void setSingleNodeValidator(ISingleNodeValidator a_singleNodeValidator) {
    m_singleNodeValidator = a_singleNodeValidator;
  }

  /**
   * @return the validator set for single node checking
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public ISingleNodeValidator getSingleNodeValidator() {
    return m_singleNodeValidator;
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
      return new CompareToBuilder().
          append(m_objectiveFunction, other.m_objectiveFunction).
          append(m_crossoverProb, other.m_crossoverProb).
          append(m_reproductionProb, other.m_reproductionProb).
          append(m_newChromsPercent, other.m_newChromsPercent).
          append(m_maxCrossoverDepth, other.m_maxCrossoverDepth).
          append(m_maxInitDepth, other.m_maxInitDepth).
          append(m_selectionMethod.getClass(), other.m_selectionMethod.getClass()).
          append(m_crossMethod.getClass(), other.m_crossMethod.getClass()).
          append(m_programCreationMaxTries, other.m_programCreationMaxTries).
          append(m_strictProgramCreation, other.m_strictProgramCreation).
          append(m_fitnessEvaluator.getClass(),
                 other.m_fitnessEvaluator.getClass()).toComparison();
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

  /**
   * @return capacity of the memory in cells
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int getMemorySize() {
    return m_memory.size();
  }

  public GPProgramInfo readProgramCache(GPProgram a_prog) {
    GPProgramInfo pci = new GPProgramInfo(a_prog, true);
    pci.setFound(false);
    return (GPProgramInfo) m_programCache.get(pci.getToStringNorm());
  }

  public GPProgramInfo putToProgramCache(GPProgram a_prog) {
    GPProgramInfo pci = new GPProgramInfo(a_prog, true);
    return (GPProgramInfo) m_programCache.put(pci.getToStringNorm(), pci);
  }

  public boolean isUseProgramCache() {
    return m_useProgramCache;
  }

  public void setUseProgramCache(boolean a_useCache) {
    m_useProgramCache = a_useCache;
  }

  /**
   * Stores a Variable.
   *
   * @param a_var the Variable to store
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void putVariable(Variable a_var) {
    m_variables.put(a_var.getName(), a_var);
  }

  /**
   * @param a_varName name of variable to retrieve
   * @return Variable instance or null, if not found
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Variable getVariable(String a_varName) {
    return (Variable) m_variables.get(a_varName);
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return newInstanceGP(getId(), getName());
  }

  /**
   * Creates a new GPConfiguration instance by cloning. Allows to preset the
   * ID and the name.
   *
   * @param a_id new ID for clone
   * @param a_name new name for clone
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPConfiguration newInstanceGP(String a_id, String a_name) {
    try {
      GPConfiguration result = new GPConfiguration(getName());
      // Clone JGAPFactory first because it helps in cloning other objects.
      // ------------------------------------------------------------------
      if (m_factory instanceof ICloneable) {
        result.m_factory = (IJGAPFactory) ( (ICloneable) m_factory).clone();
      }
      else {
        // We must fallback to a standardized solution.
        // --------------------------------------------
        m_factory = new JGAPFactory(false);
        result.m_factory = (IJGAPFactory) ( (JGAPFactory) m_factory).clone();
      }
      if (result.m_factory == null) {
        throw new IllegalStateException("JGAPFactory must not be null!");
      }
      if (m_objectiveFunction != null) {
        result.m_objectiveFunction = m_objectiveFunction;
      }
      int popSize = getPopulationSize();
      if (popSize > 0) {
        result.setPopulationSize(popSize);
      }
      result.m_crossoverProb = m_crossoverProb;
      result.m_reproductionProb = m_reproductionProb;
      result.m_newChromsPercent = m_newChromsPercent;
      result.m_functionProb = m_functionProb;
      result.m_maxCrossoverDepth = m_maxCrossoverDepth;
      result.m_maxInitDepth = m_maxInitDepth;
      result.m_minInitDepth = m_minInitDepth;
      result.m_strictProgramCreation = m_strictProgramCreation;
      result.m_programCreationMaxTries = m_programCreationMaxTries;
      result.m_selectionMethod = (INaturalGPSelector) doClone(m_selectionMethod);
      result.m_crossMethod = (CrossMethod) doClone(m_crossMethod);
      result.m_fitnessEvaluator = (IGPFitnessEvaluator) doClone(
          m_fitnessEvaluator);
      result.m_nodeValidator = (INodeValidator) doClone(m_nodeValidator);
      result.m_useProgramCache = m_useProgramCache;
      result.m_verify = m_verify;
      result.m_variables = m_variables;
      // Configurable data.
      // ------------------
//      result.m_config = new ConfigurationConfigurable();
      // Identificative data.
      // --------------------
      result.setName(a_name);
      result.setId(a_id);
      result.makeThreadKey(); // Must be called after m_id is set
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }

  /**
   * @return the JGAP factory registered
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IJGAPFactory getJGAPFactory() {
    return m_factory;
  }

  /**
   * When deserializing, do specific initializations.
   *
   * @param a_inputStream the ObjectInputStream provided for deserialzation
   *
   * @throws IOException
   * @throws ClassNotFoundException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  private void readObject(ObjectInputStream a_inputStream)
      throws IOException, ClassNotFoundException {
    //always perform the default de-serialization first
    a_inputStream.defaultReadObject();
    try {
      init(false);
    } catch (InvalidConfigurationException iex) {
      iex.printStackTrace();
      throw new IOException(iex.toString());
    }
  }

  /**
   *
   * @param a_strategy IGPInitStrategy
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void setInitStrategy(IGPInitStrategy a_strategy) {
    m_initStrategy = a_strategy;
  }

  /**
   *
   * @return IGPInitStrategy
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public IGPInitStrategy getInitStrategy() {
    return m_initStrategy;
  }

  /**
   * @param a_verify true: verify GP programs for correctness (i.e. is fitness
   * computation possible without exception?)
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void setVerifyPrograms(boolean a_verify) {
    m_verify = a_verify;
  }

  /**
   * @return true: verify GP programs for correctness (i.e. is fitness
   * computation possible without exception?)
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public boolean isVerifyPrograms() {
    return m_verify;
  }

  /**
   * Decide whether to clone command genes when creating a new GP program in
   * ProgramChromosome.
   *
   * @param a_noCommandGeneCloning boolean
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public void setNoCommandGeneCloning(boolean a_noCommandGeneCloning) {
    m_noCommandGeneCloning = a_noCommandGeneCloning;
  }

  /**
   * @return true: do not clone command genes when creating a new GP program in
   * ProgramChromosome
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public boolean isNoCommandGeneCloning() {
    return m_noCommandGeneCloning;
  }

}
