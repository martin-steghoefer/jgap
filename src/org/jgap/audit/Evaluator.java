/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import java.io.*;
import java.util.*;
import org.jgap.*;

/**
 * Gathers statistical data and returns them on request.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Evaluator
    implements Serializable {
  /**@todo implement: overall score calculation (out of best/avg. fitness value
   * etc.)
   */

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * Each data has its own data container
   */
  private Map<Object, KeyedValues2D> m_permutationData;

  /**
   * Stores the run-numbers (indexes) for all permutations submitted
   */
  private Map<Integer, Map> m_permutationRuns;

  /**
   * For processinf without permutation
   */
  private KeyedValues2D m_data;

  private PermutingConfiguration m_permConf;

  /**
   * Genotype data per permutation per run
   */
  private Map<String, GenotypeData> m_genotypeData;

  /**
   * Genotype data per permutation (averaged over all runs)
   */
  private List<GenotypeDataAvg> m_genotypeDataAvg;

  public Evaluator(final PermutingConfiguration a_conf) {
    if (a_conf == null) {
      throw new IllegalArgumentException(
          "Permuting Configuration must not be null!");
    }
    m_permConf = a_conf;
    m_data = new KeyedValues2D();
    m_permutationData = new Hashtable();
    m_permutationRuns = new Hashtable();
    m_genotypeData = new Hashtable();
    m_genotypeDataAvg = new Vector();
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

  /**
   * Sets a specific value.
   *
   * @param a_permutation int
   * @param a_run int
   * @param a_value double
   * @param a_rowKey Comparable
   * @param a_columnKey Comparable
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void setValue(int a_permutation, int a_run, double a_value,
                       Comparable a_rowKey, Comparable a_columnKey) {
    Object key = createKey(a_permutation, a_run);
    KeyedValues2D a_data = m_permutationData.get(key);
    if (a_data == null) {
      a_data = new KeyedValues2D();
      m_permutationData.put(key, a_data);
    }
    // Add run-number (index).
    // -----------------------
    addRunNumber(a_permutation, a_run);
    a_data.setValue(new Double(a_value), a_rowKey, a_columnKey);
  }

  protected void addRunNumber(int a_permutation, int a_run) {
    Map v = m_permutationRuns.get(new Integer(a_permutation));
    if (v == null) {
      v = new Hashtable();
    }
    v.put(new Integer(a_run), new Integer(a_run));
    m_permutationRuns.put(new Integer(a_permutation), v);
  }

  public Number getValue(int a_permutation, int a_run, Comparable rowKey,
                         Comparable columnKey) {
    KeyedValues2D a_data = m_permutationData.get(createKey(a_permutation, a_run));
    if (a_data == null) {
      return null;
    }
    return a_data.getValue(rowKey, columnKey);
  }

  public KeyedValues2D getData() {
    return m_data;
  }

  protected Object createKey(int a_permutation, int a_run) {
    return a_permutation + "_" + a_run;
  }

  /**
   * Calculates the average fitness value curve for a given permutation.
   * If permutation -1 is given, a composition of all permutations available
   * is created.
   * @param a_permutation -1 to use all permutations
   * @return DefaultKeyedValues2D list of fitness values, one for each
   * individual in the generation
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public KeyedValues2D calcAvgFitness(int a_permutation) {
    if (a_permutation == -1) {
      Iterator it = m_permutationRuns.keySet().iterator();
      Integer permNumberI;
      int permNumber;
      KeyedValues2D result = new KeyedValues2D();
      while (it.hasNext()) {
        permNumberI = (Integer) it.next();
        permNumber = permNumberI.intValue();
        calcAvgFitnessHelper(permNumber, result);
      }
      return result;
    }
    else {
      KeyedValues2D a_data = new KeyedValues2D();
      calcAvgFitnessHelper(a_permutation, a_data);
      return a_data;
    }
  }

  protected void calcAvgFitnessHelper(int a_permutation,
                                      final KeyedValues2D result) {
    // Determine run-numbers of given permutation.
    // -------------------------------------------
    Map runNumbers = m_permutationRuns.get(new Integer(a_permutation));
    if (runNumbers == null) {
      return;
    }
    // Loop over all run-numbers.
    // --------------------------
    Iterator it = runNumbers.keySet().iterator();
    int numRuns = runNumbers.keySet().size();
    Integer runI;
    while (it.hasNext()) {
      runI = (Integer) it.next();
      // Determine dataset of given permutation.
      // ---------------------------------------
      KeyedValues2D a_data = m_permutationData.get(createKey(a_permutation,
          runI.intValue()));
      // Determine values for current run-number and "add" them to gathered
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
   * Returns a list of lists (i.e. a matrix) to use for output as a table
   * @param a_data DefaultKeyedValues2D
   * @return List
   */
  /**@todo implement*/
//  public List getTable(KeyedValues2D a_data) {
//    return null;
//  }

  /**
   * Calculates average fitness value improvement per generation.
   *
   * @param a_permutation int
   * @return DefaultKeyedValues2D
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public KeyedValues2D calcAvgFitnessImpr(int a_permutation) {
    /**@todo implement*/
    /**@todo is this method used resp. contained in calcPerformance?*/
    Map runNumbers = m_permutationRuns.get(new Integer(a_permutation));
    if (runNumbers == null) {
      return null;
    }
//    Map fitnessImpr = new Hashtable();
    // Loop over all run-numbers.
    // --------------------------
    Iterator it = runNumbers.keySet().iterator();
//    int numRuns = runNumbers.keySet().size();
    Integer runI;
    while (it.hasNext()) {
      runI = (Integer) it.next();
      // Determine dataset of given permutation.
      // ---------------------------------------
      KeyedValues2D a_data = m_permutationData.get(createKey(a_permutation,
          runI.intValue()));
      for (int col = 0; col < a_data.getColumnCount(); col++) {
        for (int row = 0; row < a_data.getRowCount(); row++) {
        }
      }
    }
    return null;
  }

  /**
   *
   * @param a_permutation the permutation to determine the number of runs for
   * @return the number of runs for the given permutation
   */
  public int getNumberOfRuns(int a_permutation) {
    Map runNumbers = m_permutationRuns.get(new Integer(a_permutation));
    if (runNumbers == null) {
      return 0;
    }
    else {
      return runNumbers.keySet().size();
    }
  }

  /**
   * Stores information contained in the given genotype.
   * @param a_permutation int
   * @param a_run index of the run proceeded for the given genotype
   * @param a_genotype the genotype holding the population of chromosomes
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void storeGenotype(int a_permutation, int a_run, Genotype a_genotype) {
    storePopulation(a_permutation, a_run, a_genotype.getPopulation());
  }

  /**
   * Stores information contained in the given genotype.
   *
   * @param a_permutation int
   * @param a_run index of the run proceeded for the given genotype
   * @param a_pop the population holding the relevant chromosomes
   *
   * @author Klaus Meffert
   * @since 3.5 (originally named storeGenotype)
   */
  public void storePopulation(int a_permutation, int a_run, Population a_pop) {
    /**@todo implement*/
    // average and maximum fitness value
    //
    GenotypeData data = new GenotypeData();
    int generation = a_pop.getConfiguration().getGenerationNr();
    data.generation = generation;
    Population pop = a_pop;
    data.hashCode = a_pop.hashCode();
    int popSize = pop.size();
    data.chromosomeData = new ChromosomeData[popSize];
    data.size = popSize;
    // Gather data of Chromosomes.
    // ---------------------------
    IChromosome chrom;
    ChromosomeData chromData;
    for (int i = 0; i < popSize; i++) {
      chrom = pop.getChromosome(i);
      chromData = new ChromosomeData();
      chromData.fitnessValue = chrom.getFitnessValue();
      chromData.size = chrom.size();
      chromData.index = i;
      data.chromosomeData[i] = chromData;
    }
    String key = a_permutation + "_" + a_run;
    m_genotypeData.put(key, data);
    addRunNumber(a_permutation, a_run);
  }

  public GenotypeData retrieveGenotype(int a_permutation, int a_run) {
    return m_genotypeData.get(a_permutation + "_" + a_run);
  }

  /**
   * Calculates performance metrics for a given permutation and run stored
   * before with storeGenotype, like:
   * average fitness, maximum fitness...
   * @param a_permutation the permutation to compute the performance metrics
   * for
   * @return computed statistical data
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public GenotypeDataAvg calcPerformance(int a_permutation) {
    int numRuns = getNumberOfRuns(a_permutation);
    GenotypeData data;
    GenotypeDataAvg dataAvg = new GenotypeDataAvg();
    dataAvg.permutation = a_permutation;
    double sizeAvg = 0.0d;
    double fitnessAvg = 0.0d;
    double fitnessBest = -1.0d;
    double fitnessBestOld = -1.0d;
    double fitness;
    int fitnessBestGen = -1;
    double fitnessAvgChroms;
    double fitnessDiversityChromsOld = -1.0d;
    double fitnessBestDeltaAvg = 0.0d;
    double fitnessDiversity;
    double fitnessDiversityAvg = 0.0d;
    int size;
    ChromosomeData chrom;
    for (int i = 0; i < numRuns; i++) {
      data = retrieveGenotype(a_permutation, i);
      if (i == 0) {
        dataAvg.generation = data.generation;
      }
      // Average number of chromosomes.
      // ------------------------------
      sizeAvg += (double) data.size / numRuns;
      size = data.size;
      fitnessAvgChroms = 0.0d;
      fitnessDiversity = 0.0d;
      double fitnessBestLocal = -1.0d;
      for (int j = 0; j < size; j++) {
        chrom = data.chromosomeData[j];
        fitness = chrom.fitnessValue;
        // diversity of fitness values over all chromosomes
        if (j > 0) {
          fitnessDiversity += Math.abs(fitness - fitnessDiversityChromsOld) /
              (size - 1);
        }
        fitnessDiversityChromsOld = fitness;
        // average fitness value for generation over all Chromosomes
        fitnessAvgChroms += fitness / size;
        // fittest chromosome in generation over all runs
        if (fitnessBest < fitness) {
          fitnessBest = fitness;
          // memorize generation number in which fittest chromosome appeared
          fitnessBestGen = data.generation;
        }
        // fittest chromosome in generation over current runs
        if (fitnessBestLocal < fitness) {
          fitnessBestLocal = fitness;
        }
      }
      // average fitness value for generation over all runs
      fitnessAvg += fitnessAvgChroms / numRuns;
      // average fitness delta value for generation over all runs
      fitnessDiversityAvg += fitnessDiversity / numRuns;
      // absolute delta between two adjacent best fitness values
      if (i > 0) {
        fitnessBestDeltaAvg += Math.abs(fitnessBestLocal - fitnessBestOld) /
            (numRuns - 1);
      }
      fitnessBestOld = fitnessBestLocal;
    }
    dataAvg.sizeAvg = sizeAvg;
    dataAvg.avgFitnessValue = fitnessAvg;
    dataAvg.bestFitnessValue = fitnessBest;
    dataAvg.bestFitnessValueGeneration = fitnessBestGen;
    dataAvg.avgDiversityFitnessValue = fitnessDiversityAvg;
    dataAvg.avgBestDeltaFitnessValue = fitnessBestDeltaAvg;
    // Store computed (averaged) data.
    // -------------------------------
    m_genotypeDataAvg.add(dataAvg);
    return dataAvg;
  }

  /**
   * Averaged genotype data (computed over all runs of a permutation)
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public class GenotypeDataAvg {
    public int permutation;

    public int generation;

    public double sizeAvg;

    public double bestFitnessValue;

    public double avgFitnessValue;

    public int bestFitnessValueGeneration;

    public double avgDiversityFitnessValue;

    public double avgBestDeltaFitnessValue;
  }
  /**
   * Genotype data for one single run
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public class GenotypeData {
    public int generation;

    public int hashCode;

    public int size;

    public ChromosomeData[] chromosomeData;
  }
  public class ChromosomeData {
    public int index;

    public int size;

    public double fitnessValue;
  }
  /**
   * @return Averaged genotype data
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public List<GenotypeDataAvg> getGenotypeAverageData() {
    return m_genotypeDataAvg;
  }
}
