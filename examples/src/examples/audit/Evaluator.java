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
  private final static String CVS_REVISION = "$Revision: 1.8 $";

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

  /**
   * Genotype data per permutation per run
   */
  private Map m_genotypeData;

  /**
   * Genotype data per permutation (averaged over all runs)
   */
  private List m_genotypeDataAvg;

  public Evaluator(PermutingConfiguration a_conf) {
    if (a_conf == null) {
      throw new IllegalArgumentException("Configuration must not be null!");
    }
    m_permConf = a_conf;
    m_data = new DefaultKeyedValues2D();
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
    addRunNumber(a_permutation, a_run);

    a_data.setValue(new Double(a_value), a_rowKey, a_columnKey);
  }

  protected void addRunNumber(int a_permutation, int a_run) {
    Map v = (Map) m_permutationRuns.get(new Integer(a_permutation));
    if (v == null) {
      v = new Hashtable();
    }
    v.put(new Integer(a_run), new Integer(a_run));
    m_permutationRuns.put(new Integer(a_permutation), v);
  }

  public Number getValue(int a_permutation, int a_run, Comparable rowKey,
                         Comparable columnKey) {
    DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.get(
        createKey(a_permutation, a_run));
    return a_data.getValue(rowKey + String.valueOf(a_run), columnKey);
  }

  public Number getAvgValue(int a_permutation, Comparable rowKey,
                            Comparable columnKey) {
    return null; /**@todo*/
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
        permNumberI = (Integer) it.next();
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

  protected void calcAvgFitnessHelper(int a_permutation,
                                      final DefaultKeyedValues2D result) {
    // determine run-numbers of given permutation
    Map runNumbers = (Map) m_permutationRuns.get(new Integer(a_permutation));
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
      DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.
          get(createKey(a_permutation, runI.intValue()));

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
   * Returns a list of lists (i.e. a matrix) to use for output as a table
   * @param a_data DefaultKeyedValues2D
   * @return List
   */
  public List getTable(DefaultKeyedValues2D a_data) {
    return null;
  }

  /**
   * Calculates average fitness value improvement per generation.
   * @param a_permutation int
   * @return DefaultKeyedValues2D
   */
  public DefaultKeyedValues2D calcAvgFitnessImpr(int a_permutation) {
    Map runNumbers = (Map) m_permutationRuns.get(new Integer(a_permutation));
    if (runNumbers == null) {
      return null;
    }

    Map fitnessImpr = new Hashtable();

    // Loop over all run-numbers.
    // --------------------------
    Iterator it = runNumbers.keySet().iterator();
    int numRuns = runNumbers.keySet().size();
    Integer runI;
    while (it.hasNext()) {

      runI = (Integer) it.next();
      // Determine dataset of given permutation.
      // ---------------------------------------
      DefaultKeyedValues2D a_data = (DefaultKeyedValues2D) m_permutationData.
          get(createKey(a_permutation, runI.intValue()));
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
  protected int getNumberOfRuns(int a_permutation) {
    Map runNumbers = (Map) m_permutationRuns.get(new Integer(a_permutation));
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
   * @param a_run int
   * @param a_genotype the genotype holding the population of chromosomes
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void storeGenotype(int a_permutation, int a_run, Genotype a_genotype) {
    /**@todo implement*/
    // average and maximum fitness value
    //
    GenotypeData data = new GenotypeData();
    int generation = a_genotype.getConfiguration().getGenerationNr();
    data.generation = generation;
    Population pop = a_genotype.getPopulation();
    data.hashCode = a_genotype.hashCode();
    int popSize = pop.size();
    data.chromosomeData = new ChromosomeData[popSize];
    data.size = popSize;
    // gather data of Chromosomes
    Chromosome chrom;
    ChromosomeData chromData;
    for (int i=0;i<popSize;i++) {
      chrom = pop.getChromosome(i);
      chromData = new ChromosomeData();
      chromData.fitnessValue = chrom.getFitnessValue();
      chromData.size= chrom.size();
      chromData.index = i;
      data.chromosomeData[i] = chromData;
    }

    String key = a_permutation + "_" + a_run;
    m_genotypeData.put(key, data);

    addRunNumber(a_permutation, a_run);
  }

  public GenotypeData retrieveGenotype(int a_permutation, int a_run) {
    return (GenotypeData) m_genotypeData.get(a_permutation + "_" + a_run);
  }

  /**
   * Calculates performance metrics for a given permutation and run stored
   * before with storeGenotype, like:
   * average fitness, maximum fitness...
   * @param a_permutation the permutation to compute the performance metrics
   * for
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void calcPerformance(int a_permutation) {
    int numRuns = getNumberOfRuns(a_permutation);
    GenotypeData data;

    GenotypeDataAvg dataAvg = new GenotypeDataAvg();
    dataAvg.permutation = a_permutation;

    double sizeAvg = 0.0d;
    double fitnessAvg = 0.0d;
    double fitnessBest = -1.0d;
    double fitness;
    int fitnessBestGen = -1;
    double fitnessAvgChroms;

    int size;
    ChromosomeData chrom;

    for (int i=0;i<numRuns;i++) {
      data = retrieveGenotype(a_permutation, i);

      // generation the genotype data represents
      if (i==0) {
        dataAvg.generation = data.generation;
      }

      // average number of chromosomes
      sizeAvg += data.size/numRuns;

      size = data.size;
      fitnessAvgChroms = 0.0d;
      for (int j=0;i<size;j++) {
        chrom = data.chromosomeData[j];
        fitness = chrom.fitnessValue;
        // average fitness value for generation over all Chromosomes
        fitnessAvgChroms += fitness/size;

        // fittest chromosome in generation
        if (fitnessBest < fitness) {
          fitnessBest = fitness;
          // memorize generation number in which fittest chromosome appeared
          fitnessBestGen = data.generation;
        }

        // absolute delta between two adjacent best fitness values

        // absolute delta between two adjacent average fitness values
      }
      // average fitness value for generation over all runs
      fitnessAvg += fitnessAvgChroms/numRuns;
    }

    dataAvg.sizeAvg = sizeAvg;
    dataAvg.avgFitnessValue = fitnessAvg;
    dataAvg.bestFitnessValue = fitnessBest;
    dataAvg.bestFitnessValueGeneration = fitnessBestGen;

    //store computed (averaged) data
    m_genotypeDataAvg.add(dataAvg);
  }

  /**
   * Averaged genotype data (computed over all runs of a permutation)
   */
  public class GenotypeDataAvg {
    public int permutation;
    public int generation;
    public double sizeAvg;
    public double bestFitnessValue;
    public double avgFitnessValue;
    public int bestFitnessValueGeneration;
  }

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
}
