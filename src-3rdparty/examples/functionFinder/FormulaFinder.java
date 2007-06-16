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

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Function Finder example - Main class.
 * <p>
 * The function finder tries to find a formula that matches best a given table
 * of input/output-value pairs (X,Y values). Meaning: Minimize the error for all
 * given inputs X over the corresponding output Y.
 * <P>
 * Refactored solution of the example provided with JGAP 2.2. Now uses the
 * newly introduced TruthTableFitnessFunction.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class FormulaFinder {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private static int MIN_WANTED_EVOLUTIONS = 300;

  private static int MAX_ALLOWED_EVOLUTIONS_HARDLIMIT = 500;

  private static int MIN_FITNESS_WANTED;

  private static int POPULATION_SIZE;

  /**
   * Maximum number of different terms allowed in formula to be evolved.
   * Can be specified in configuration file.
   */
  private static int MAX_ALLOWED_TERMS = 3;

  /**
   * Maximum number of allowed terms in formula to be evolved.
   *
   * Example:
   * MAX_TERMS_IN_FUNCTION = 1 --> F(X) = sin(X)
   * MAX_TERMS_IN_FUNCTION = 2 --> F(X) = sin(X*cos(X))
   *                       = 2 --> F(X) = sin(X*cos(abs(X)+3))
   * ...
   */
  public static final int MAX_TERMS_IN_FUNCTION = 1;

  /**
   * Main method
   * @param args first parameter: if provided = configuration file, if not
   * the default "simpleFormula.properties" is taken
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static void main(String[] args)
      throws Exception {
    // Initialize repository of available funktions
    Repository.init();
    String filename;
    if (args.length > 0) {
      // Use specified configuration file.
      // ---------------------------------
      filename = args[0];
    }
    else {
      // Use default.
      // ------------
      filename = "simpleFormula.properties";
    }
    String dir = new File(".").getAbsolutePath() + "/";
    evolveFunction(dir + filename);
  }

  /**
   * Does the evolution.
   *
   * @param a_filename name of file that contains configuration for the problem
   * to solve via evolution
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  private static void evolveFunction(String a_filename)
      throws Exception {
    // Create JGAP configuration.
    // --------------------------
    Configuration conf = new DefaultConfiguration();
    conf.setPreservFittestIndividual(true);
    // Fitness Evaluator (lower is better).
    // ------------------------------------
    conf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    // Selector.
    // ---------
    conf.removeNaturalSelectors(false);
    conf.addNaturalSelector(new WeightedRouletteSelector(conf), true);
    // Read in related data.
    // ----------------------
    File f = new File(a_filename);
    FileInputStream fin = new FileInputStream(f);
    Properties props = new Properties();
    props.load(fin);
    Enumeration anEnum = props.keys();
    // Read in problem configuration.
    // ------------------------------
    float factor = Float.parseFloat( (String) props.remove("minFitness"));
    if (conf.getFitnessEvaluator().isFitter(1, 2)) {
      MIN_FITNESS_WANTED = (int) (FormulaFitnessFunction.MAX_FITNESS *
                                  (1 - factor));
    }
    else {
      MIN_FITNESS_WANTED = (int) (FormulaFitnessFunction.MAX_FITNESS * factor);
    }
    MIN_WANTED_EVOLUTIONS = Integer.parseInt( (String) props.remove("loopsMin"));
    MAX_ALLOWED_EVOLUTIONS_HARDLIMIT = Integer.parseInt( (String) props.remove(
        "loopsMax"));
    MAX_ALLOWED_TERMS = Integer.parseInt( (String) props.remove("maxTerms"));
    POPULATION_SIZE = Integer.parseInt( (String) props.remove("populationSize"));
    // Read in input-/output-value pairs.
    // ---------------------------------
    Map truthTable = new HashMap();
    while (anEnum.hasMoreElements()) {
      String s = (String) anEnum.nextElement();
      float inputValue = Float.parseFloat(s);
      String outS = (String) props.get(s);
      float outputValue = Float.parseFloat(outS);
      truthTable.put(new Double(inputValue), new Double(outputValue));
    }
    // Validate formula against repository of valid functions.
    // -------------------------------------------------------
    Repository.apply(truthTable);
    // Fitness function.
    // -----------------
    FormulaFitnessFunction myFunc = new FormulaFitnessFunction(conf, truthTable);
    conf.setFitnessFunction(myFunc);
    int maxTerms = MAX_ALLOWED_TERMS;
    // Maximum number of alleles needed for a "term gene" is the maximum
    // number of possible different terms.
    // -----------------------------------------------------------------
    Gene[] termGenes = new Gene[maxTerms];
    int numberOfFunctions = Repository.getFunctions().size();
    int numberOfConstants = Repository.getConstants().size();
    int max = (numberOfFunctions + numberOfConstants) * MAX_TERMS_IN_FUNCTION -
        1;
    CompositeGene comp;
    IntegerGene gene;
    for (int i = 0; i < maxTerms; i++) {
      comp = new CompositeGene(conf);
      // Funtions, constants.
      // -------------------
      gene = new IntegerGene(conf, 0, max);
      comp.addGene(gene);
      // Operators.
      // ----------
      gene = new IntegerGene(conf, 0, Repository.getOperators().size() - 1);
      comp.addGene(gene);
      termGenes[i] = comp;
    }
    IChromosome termChromosome = new Chromosome(conf, termGenes);
    conf.setSampleChromosome(termChromosome);
    conf.setPopulationSize(POPULATION_SIZE);
    Genotype population = Genotype.randomInitialGenotype(conf);
    int i = 0;
    int step = 100;
    double localBestFitness = myFunc.worstFitness();
    double localCurrentFitness;
    String bestSolutionSoFar = null;
    IChromosome chrom;
    // Do the evolution.
    // -----------------
    while (i < MAX_ALLOWED_EVOLUTIONS_HARDLIMIT) {
      population.evolve();
      chrom = population.getFittestChromosome();
      localCurrentFitness = chrom.getFitnessValue();
      if (conf.getFitnessEvaluator().isFitter(
          localCurrentFitness, localBestFitness)) {
        // Found a new best solution
        localBestFitness = localCurrentFitness;
        String formula = Utility.getFormulaFromChromosome(chrom);

        bestSolutionSoFar = formula;
        if (Math.abs(localBestFitness - myFunc.LEAST_FITNESS_VALUE) < 0.0001d) {
          break;
        }
      }
      if (conf.getFitnessEvaluator().isFitter(
          localBestFitness, MIN_FITNESS_WANTED)) {
        if (i > MIN_WANTED_EVOLUTIONS) {
          break;
        }
      }
      i++;
      // Simple progress display.
      // ------------------------
      if (--step == 0) {
        step = 100;
        System.out.println("Generation: " + i);
        System.out.println("Best result so far: " + bestSolutionSoFar);
        System.out.println("Fitness value: " + localBestFitness);
      }
    }
    if (bestSolutionSoFar == null) {
      bestSolutionSoFar = Utility.getFormulaFromChromosome(population.
          getFittestChromosome());
    }
    // Output of result.
    // -----------------
    System.out.println("Number of generations: " + i);
    System.out.println("Best solution so far: ");
    String formula = bestSolutionSoFar;
    System.out.println("Formula: " + formula);
    System.out.println("Fitness value: " + localBestFitness);
  }
}
