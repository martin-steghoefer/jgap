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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Vector truthTable;

  private int currentFitness;

  private static Syntax syntax;

  private static ExpressionParser parser;

  // Constants for calculating thr fitness value
  // -------------------------------------------
  public static final float DELTA = 0.000001f;

  public static final int MAX_FITNESS = 9999999;

  private static final int RELATION_FITNESS = 100;

  public static final int WORST = MAX_FITNESS / RELATION_FITNESS;

  public static final int LEAST_FITNESS_VALUE = 0;

  /**
   * Consteuctor
   * @param truthTable table of input/output pairs for feeding the formula and
   * determining the fitness value thru delta computation
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public MatchAgainstTruthTable(Vector truthTable) {
    this.truthTable = truthTable;
    syntax = new JavaSyntax();
    parser = new ExpressionParser(syntax, null);
    Repository.init();
  }

  public int getCurrentFitness() {
    return currentFitness;
  }

  /**
   * Implementation of the evaluate method from class FitnessFunction.
   * Calculates the fitness of a given Chromosom in a determined way.
   * @param chromosome the Chromosom to be evaluated
   * @return positive integer value representing the fitness of the Chromosom
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public double evaluate(Chromosome chromosome) {
    String formula = null;
    try {
      //Calculcate result of formula
      formula = Utility.getFormulaFromChromosome(chromosome);
      //Calculate fitness
      int fitness = calcFitness(formula, truthTable);
      currentFitness = fitness;
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
   * @param formula input formula
   * @param truthTable table of input/output-pairs for formula
   * @return fitness value of formula
   * @throws CompilationException e.g. in case of division by zero
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static int calcFitness(String formula, Vector truthTable)
      throws CompilationException {
    if (formula == null || formula.length() < 6) {
      // Minimal length of 6 because the minmimum prefix "F(x)=" has a length
      // of 5
      return 0;
    }

    // Compile expression with parser
    CompiledFunction ex1;
    try {
      ex1 = parser.compileFunction(formula);
    }
    catch (CompilationException ex) {
      System.err.println("FORMELFEHLER: " + formula);
      throw ex;
    }
    // Determine delta values of all function values and add up their squares
    double[] input;
    float inputValue;
    Tupel tupel;
    float diffAbs = 0.0f;
    float delta;
    float deltaAbs;
    for (int i = 0; i < truthTable.size(); i++) {
      tupel = (Tupel) truthTable.elementAt(i);
      inputValue = tupel.getInputValue();
      input = new double[] {
          inputValue};
      delta = (float) ex1.computeFunction(input) - tupel.getOutputValue();
      deltaAbs = (float) Math.abs(delta);
      diffAbs += deltaAbs;
    }
    // scale fitness value and return it
    return getFitness(diffAbs);
  }

  /**
   *
   * @param i int
   * @return String
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static String toBinaryString(int i) {
    return Integer.toBinaryString(i);
  }

  /**
   *
   * @param i Integer
   * @return String
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static String toBinaryString(Integer i) {
    return Integer.toBinaryString(i.intValue());
  }
}
