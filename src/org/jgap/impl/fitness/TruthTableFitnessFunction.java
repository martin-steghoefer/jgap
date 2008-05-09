/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.fitness;

import java.util.*;

import org.jgap.*;

/**
 * Fitness Function relying on a truth table.
 * To use this class, just implement a subclass of it, register the current
 * truth table and implement the evaluate method in a way that you can call
 * calcFitness with the required (parameter (given values: encoded with the
 * chromosome). If the truth table is dynamic just register it in evaluate,
 * otherwise do it after construction of this fitness function.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class TruthTableFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private Map m_truthTable;

  // Constants for calculating the fitness value
  // -------------------------------------------
  public static final int MAX_FITNESS = 9999999;

  private static final int RELATION_FITNESS = 100;

  public static final int WORST = MAX_FITNESS / RELATION_FITNESS;

  private Configuration m_conf;

  /**
   * Only use for dynamic instantiation as configuration retrieved from static
   * setting.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public TruthTableFitnessFunction() {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Constructor without registering a truth table. Use this constructor if the
   * truth table is not fix over generations and needs to be set in method
   * evaluate.
   *
   * @param a_conf the configuration to use
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public TruthTableFitnessFunction(Configuration a_conf) {
    m_conf = a_conf;
  }


  /**
   * Constructor for registering a truth table. Use this constructor if the
   * truth table is fix over generations.
   *
   * @param a_conf the configuration to use
   * @param a_truthTable table of input/output pairs for feeding the formula
   * and determining the fitness value thru delta computation
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public TruthTableFitnessFunction(Configuration a_conf, final Map a_truthTable) {
    this(a_conf);
    setTruthTable(a_truthTable);
  }

  public void setTruthTable(final Map a_truthTable) {
    m_truthTable = a_truthTable;
  }

  public Map getTruthTable() {
    return m_truthTable;
  }

  /**
   * Implementation of the evaluate method from class FitnessFunction.
   * Calculates the fitness of a given Chromosome in a determined way.
   * @param a_chromosome the Chromosome to be evaluated
   * @return positive integer value representing the fitness of the Chromosome
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  protected abstract double evaluate(IChromosome a_chromosome);

  /**
   * Fitness value calculation for a given table of input/output tupels
   * and a truth-table (also given as list of input/output tupels)
   *
   * @param a_actualInputOutput table of actual input/output pairs
   * @return delta between current values and given truth table
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public double calcFitness(final Map a_actualInputOutput) {
    // Determine delta values of all function values and add up their squares
    double outputValueGiven, outputValueWanted;
    Double inputValueWanted;
    double diffAbs = 0.0f;
    double delta;
    double deltaAbs;
    Set keySet = getTruthTable().keySet();
    Iterator keys = keySet.iterator();
    while (keys.hasNext()) {
      inputValueWanted = (Double) keys.next();
      outputValueWanted = ( (Double) getTruthTable().get(inputValueWanted)).
          doubleValue();
      Double output = ( (Double) a_actualInputOutput.get(inputValueWanted));
      if (output != null) {
        outputValueGiven = output.doubleValue();
        // determine current value (evolved formula) minus reference value
        if (Double.isNaN(outputValueWanted)) {
          return Double.NaN;
        }
        delta = outputValueGiven - outputValueWanted;
        deltaAbs = (float) Math.abs(delta);
      }
      else {
        deltaAbs = Math.abs(outputValueWanted);
      }
      diffAbs += deltaAbs;
    }
    /**@todo consider length of formula (i.e. number of terms, e.g.) for
     * fitness calculation*/

    return diffAbs;
  }

  /**
   * @return the Configuration object set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public Configuration getConfiguration() {
    return m_conf;
  }
}
