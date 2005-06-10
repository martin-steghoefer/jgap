/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.functionFinder;

import java.util.*;

import org.jgap.*;

import com.eteks.parser.*;

/**
 * Fitness Function validating against a value table
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class MatchAgainstTruthTable
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private List m_truthTable;

  private int m_currentFitness;

  private static Syntax m_syntax;

  private static ExpressionParser m_parser;

  // Constants for calculating the fitness value
  // -------------------------------------------
  public static final float DELTA = 0.000001f;

  public static final int MAX_FITNESS = 9999999;

  private static final int RELATION_FITNESS = 100;

  public static final int WORST = MAX_FITNESS / RELATION_FITNESS;

  public static final int LEAST_FITNESS_VALUE = 0;

  /**
   * Constructor
   * @param a_truthTable table of input/output pairs for feeding the formula
   * and determining the fitness value thru delta computation
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public MatchAgainstTruthTable(List a_truthTable) {
    m_truthTable = a_truthTable;
    m_syntax = new JavaSyntax();
    m_parser = new ExpressionParser(m_syntax, null);
    Repository.init();
  }

  public int getCurrentFitness() {
    return m_currentFitness;
  }

  /**
   * Implementation of the evaluate method from class FitnessFunction.
   * Calculates the fitness of a given Chromosome in a determined way.
   * @param a_chromosome the Chromosome to be evaluated
   * @return positive integer value representing the fitness of the Chromosome
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public double evaluate(Chromosome a_chromosome) {
    String formula = null;
    try {
      //Calculcate result of formula
      formula = Utility.getFormulaFromChromosome(a_chromosome);
      //Calculate fitness
      int fitness = calcFitness(formula, m_truthTable);
      m_currentFitness = fitness;
      return fitness;
    }
    catch (Exception ex) {
      System.err.println("FORMULA: " + formula);
      ex.printStackTrace();
      return LEAST_FITNESS_VALUE;
    }
  }

  static class Tupel {
    private float inputValue;

    private float outputValue;

    public Tupel(float inputValue, float outputValue) {
      this.inputValue = inputValue;
      this.outputValue = outputValue;
    }

    public float getInputValue() {
      return inputValue;
    }

    public float getOutputValue() {
      return outputValue;
    }
  }

  /**
   * Helper function: scaled calculation of fitness value
   * @param input unscaled fitness value
   * @return scales fitness value
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static int getFitness(float input) {
    int result;
    input = Math.abs(input);
    if (input >= MAX_FITNESS / RELATION_FITNESS) {
      // Input too bad --> ignore
      result = 0;
    }
    else {
      // Normal case
      result = (int) Math.floor(MAX_FITNESS - input * RELATION_FITNESS);
    }
    if (result < 0) {
      result = 0;
    }
    return result;
  }

  /**
   * Fitness value calculation for a given formula. Based on given table of
   * input/output-pairs.
   * @param a_formula input formula
   * @param a_truthTable table of input/output-pairs for formula
   * @return fitness value of formula
   * @throws CompilationException e.g. in case of division by zero
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static int calcFitness(String a_formula, List a_truthTable)
      throws CompilationException {
    if (a_formula == null || a_formula.length() < 6) {
      // Minimal length of 6 because the minmimum prefix "F(x)=" has a length
      // of 5
      return 0;
    }

    // Compile expression with parser
    CompiledFunction ex1;
    try {
      ex1 = m_parser.compileFunction(a_formula);
    }
    catch (CompilationException ex) {
      System.err.println("Error in formula: " + a_formula);
      throw ex;
    }
    // Determine delta values of all function values and add up their squares
    double[] input;
    float inputValue;
    Tupel tupel;
    float diffAbs = 0.0f;
    float delta;
    float deltaAbs;
    for (int i = 0; i < a_truthTable.size(); i++) {
      tupel = (Tupel) a_truthTable.get(i);
      inputValue = tupel.getInputValue();
      input = new double[] {
          inputValue};
      // determine current value (evolved formula) minus reference value
      delta = (float) ex1.computeFunction(input) - tupel.getOutputValue();
      deltaAbs = (float) Math.abs(delta);
      diffAbs += deltaAbs;
    }
    /**@todo consider length of formula (i.e. number of terms, e.g.) for
     * fitness calculation*/

    // scale fitness value and return it
    return getFitness(diffAbs);
  }

}
