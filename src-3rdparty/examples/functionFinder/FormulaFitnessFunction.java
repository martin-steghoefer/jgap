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
import org.jgap.impl.*;
import org.jgap.impl.fitness.*;
import com.eteks.parser.*;
import java.io.*;

/**
 * Fitness Function validating against a value table.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class FormulaFitnessFunction
    extends TruthTableFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";
/**@todo swap out general parts into JGAP core*/
  private static Syntax m_syntax;

  private static ExpressionParser m_parser;

  // Constants for calculating the fitness value
  // -------------------------------------------
  public static final float DELTA = 0.000001f;

  public static final int MAX_FITNESS = 9999999;

  private static final int RELATION_FITNESS = 10;

  public static final int WORST = MAX_FITNESS / RELATION_FITNESS;

  public static final int LEAST_FITNESS_VALUE = 0;

  /**
   * Constructor.
   *
   * @param a_config the configuration to use
   * @param a_truthTable table of input/output pairs for feeding the formula
   * and determining the fitness value thru delta computation
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public FormulaFitnessFunction(Configuration a_config, Map a_truthTable) {
    super(a_config, a_truthTable);
    m_syntax = new JavaSyntax();
    m_parser = new ExpressionParser(m_syntax, null);
    Repository.init();
  }

  /**
   * Implementation of the evaluate method from class FitnessFunction.
   * Calculates the fitness of a given Chromosome in a determined way.
   *
   * @param a_chromosome the Chromosome to be evaluated
   * @return positive integer value representing the fitness of the Chromosome
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public double evaluate(IChromosome a_chromosome) {
    String formula = null;
    try {
      // Calculcate result of formula.
      formula = Utility.getFormulaFromChromosome(a_chromosome);
      return calcFitness(formula);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return worstFitness();
    }
  }

  public double calcFitness(String formula)
      throws CompilationException {
    if (formula == null || formula.length() < 6) {
      // Minimal length of 6 because the minmimum prefix "F(x)=" has a length
      // of 5
      return worstFitness();
    }
    // Compile expression with parser
    CompiledFunction ex1;
    ex1 = m_parser.compileFunction(formula);
    Map givenTruthTable = new HashMap();
    Set keySet = getTruthTable().keySet();
    Iterator keys = keySet.iterator();
    while (keys.hasNext()) {
      Double key = (Double) keys.next();
      double[] input = new double[] {
          key.doubleValue()};
      double value = ex1.computeFunction(input);
      if (Double.isNaN(value)) {
        return worstFitness();
      }
      givenTruthTable.put(key, new Double(value));
    }
    //Calculate fitness
    return getFitness(calcFitness(givenTruthTable));
  }

  /**
   * Helper function: scaled calculation of fitness value
   * @param a_input unscaled fitness value
   * @return scaled fitness value
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected double getFitness(double a_input) {
    if (Double.isNaN(a_input)) {
      return worstFitness();
    }
    double result;
    // Normal case
    if (bestFitness() < worstFitness()) {
      result = bestFitness() + a_input * RELATION_FITNESS;
    }
    else {
      result = bestFitness() - a_input * RELATION_FITNESS;
    }
    if (getConfiguration().getFitnessEvaluator().isFitter(result,
        bestFitness())) {
      result = bestFitness();
    }
    if (getConfiguration().getFitnessEvaluator().isFitter(
      worstFitness(), result)) {
      // Input too bad --> ignore.
      // -------------------------
      result = worstFitness();
    }
    return result;
  }

  public static void main(String[] args)
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    File f;
    if (args.length == 0) {
      f = new File("simpleFormula.properties");
    }
    else {
      f = new File(args[0]);
    }
    FileInputStream fin = new FileInputStream(f);
    Properties props = new Properties();
    props.load(fin);
    Enumeration anEnum = props.keys();
    // Read in problem configuration
    // -----------------------------
    props.remove("minFitness");
    props.remove("loopsMin");
    props.remove("loopsMax");
    props.remove("maxTerms");
    props.remove("populationSize");
    //Read in input-/output-value pairs
    Map truthTable = new HashMap();
    while (anEnum.hasMoreElements()) {
      String s = (String) anEnum.nextElement();
      float inputValue = Float.parseFloat(s);
      String outS = (String) props.get(s);
      float outputValue = Float.parseFloat(outS);
      truthTable.put(new Double(inputValue), new Double(outputValue));
    }
    FormulaFitnessFunction ff = new FormulaFitnessFunction(conf, truthTable);
    System.err.println(ff.calcFitness("F(X)=X*Math.PI*2"));
  }

  public double worstFitness() {
    if (getConfiguration().getFitnessEvaluator().isFitter(1, 2)) {
      return MAX_FITNESS;
    }
    else {
      return LEAST_FITNESS_VALUE;
    }
  }

  public double bestFitness() {
    if (getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
      return MAX_FITNESS;
    }
    else {
      return LEAST_FITNESS_VALUE;
    }
  }
}
