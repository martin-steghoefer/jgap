/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.audit;

import org.jgap.*;
import java.util.*;

/**
 * Gathers statistical data and returns them on request.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Evaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Each data has its own data container
   */
  private Map m_permutationData;

  /**
   * Stores the run-numbers (indexes) for all permutations submitted
   */
  private Map m_permutationRuns;

  /**
   * For processinf without permutation
   */
  private DefaultKeyedValues2D m_data;

  private PermutingConfiguration m_permConf;

  public Evaluator(PermutingConfiguration a_conf) {
    if (a_conf == null) {
      throw new IllegalArgumentException("Configuration must not be null!");
    }
    m_permConf = a_conf;
    m_data = new DefaultKeyedValues2D();
    m_permutationData = new Hashtable();
    m_permutationRuns = new Hashtable();
  }

  public boolean hasNext() {
    return m_permConf.hasNext();
  }

  public Configuration next()
      throws InvalidConfigurationException {
    return m_permConf.next();
  }

  public void setValue(double a_value, Comparable a_rowKey,
                       Comparable a_columnKey) {
    m_data.setValue(new Double(a_value), a_rowKey, a_columnKey);
//    fireDatasetChanged();
  }

  public Number getValue(Comparable rowKey, Comparable columnKey) {
    return m_data.getValue(rowKey, columnKey);
  }

  public void setValue(int a_permutation, int a_run, double a_value,
                       Comparable a_rowKey, Comparable a_columnKey) {
    DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.get(
      createKey(a_permutation, a_run));
    if (a_data == null) {
      a_data = new DefaultKeyedValues2D();
      m_permutationData.put(createKey(a_permutation, a_run), a_data);
    }
    // Add run-number (index).
    // -----------------------
    Map v = (Map)m_permutationRuns.get(new Integer(a_permutation));
    if (v == null) {
      v = new Hashtable();
    }
    v.put(new Integer(a_run), new Integer(a_run));
    m_permutationRuns.put(new Integer(a_permutation),v);

    a_data.setValue(new Double(a_value), a_rowKey, a_columnKey);
  }

  public Number getValue(int a_permutation, int a_run, Comparable rowKey,
                         Comparable columnKey) {
    DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.get(
        createKey(a_permutation, a_run));
    return a_data.getValue(rowKey + String.valueOf(a_run), columnKey);
  }

  public Number getAvgValue(int a_permutation, Comparable rowKey,
                            Comparable columnKey) {
    return null;/**@todo*/
  }

  public DefaultKeyedValues2D getData() {
    return m_data;
  }

  protected Object createKey(int a_permutation, int a_run) {
    return new String(a_permutation + "_" + a_run);
  }

  /**
   * Calculates the average fitness value curve for a given permutation.
   * If permutation -1 is given, a composition of all permutations available
   * is created.
   * @param a_permutation -1 to use all permutations
   * @return DefaultKeyedValues2D
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public DefaultKeyedValues2D calcAvgFitness(int a_permutation) {
    if (a_permutation == -1) {
      Iterator it = m_permutationRuns.keySet().iterator();
      Integer permNumberI;
      int permNumber;
      DefaultKeyedValues2D result = new DefaultKeyedValues2D();
      while (it.hasNext()) {
        permNumberI = (Integer)it.next();
        permNumber = permNumberI.intValue();
        calcAvgFitnessHelper(permNumber, result);
      }
      return result;
    }
    else {
      DefaultKeyedValues2D a_data = new DefaultKeyedValues2D();
      calcAvgFitnessHelper(a_permutation, a_data);
      return a_data;
    }
  }

  protected void calcAvgFitnessHelper(int a_permutation, final DefaultKeyedValues2D result) {
    // determine run-numbers of given permutation
    Map runNumbers = (Map)m_permutationRuns.get(new Integer(a_permutation));
    if (runNumbers == null) {
      return;
    }

    // Loop over all run-numbers.
    // --------------------------
    Iterator it = runNumbers.keySet().iterator();
    int numRuns = runNumbers.keySet().size();
    Integer runI;
    while (it.hasNext()) {
      runI = (Integer)it.next();
      // determine dataset of given permutation
      DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.get(
        createKey(a_permutation, runI.intValue()));

      // determine values for current run-number and "add" them to gathered
      // data.
      // ------------------------------------------------------------------
      for (int col = 0; col < a_data.getColumnCount(); col++) {
        for (int row = 0; row < a_data.getRowCount(); row++) {
          // Previous value (summation).
          // --------------------------.
          Double d = (Double) result.getValue(a_data.getRowKey(row),
                                              a_data.getColumnKey(col));
          double newValue;
          if (d == null) {
            newValue = 0.0d;
          }
          else {
            newValue = d.doubleValue();
          }

          // Add current value (divided by total number of runs to get an
          // averaged value).
          // ------------------------------------------------------------
          newValue +=
              a_data.getValue(a_data.getRowKey(row), a_data.getColumnKey(col)).
              doubleValue() / numRuns;

          // Set averaged value back to result dataset.
          // ------------------------------------------
          result.setValue(new Double(newValue), a_data.getRowKey(row),
                          a_data.getColumnKey(col));
        }
      }
    }
  }

  /**
   * Calculates average fitness value improvement per generation.
   * @param a_generation int
   * @return DefaultKeyedValues2D
   */
  public DefaultKeyedValues2D calcAvgFitnessImpr(int a_generation) {
    return null;
  }
}
