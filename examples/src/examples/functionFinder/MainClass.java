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
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class MainClass {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

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
   * @param args Eingabeparameter
   * @throws Exception whatever
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
      // Use specified configuration file
      filename = args[0];
    }
    else {
      // Use default
      filename = "simpleFormula.properties";
    }
    String dir = new File(".").getAbsolutePath()+"/";
    evolveFunction(dir+filename);
  }

  /**
   *
   * @param werteTabelleFile String
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  private static void evolveFunction(String werteTabelleFile)
      throws Exception {

    //Read in related data
    File f = new File(werteTabelleFile);
    FileInputStream fin = new FileInputStream(f);
    Properties props = new Properties();
    props.load(fin);
    Enumeration enum = props.keys();

    //Read in configuration
    float factor = Float.parseFloat( (String) props.remove("minFitness"));
    MIN_FITNESS_WANTED = (int) (factor * MatchAgainstTruthTable.MAX_FITNESS);
    MIN_WANTED_EVOLUTIONS = Integer.parseInt( (String) props.remove("loopsMin"));
    MAX_ALLOWED_EVOLUTIONS_HARDLIMIT = Integer.parseInt( (String) props.remove(
        "loopsMax"));
    MAX_ALLOWED_TERMS = Integer.parseInt( (String) props.remove("maxTerms"));
    POPULATION_SIZE = Integer.parseInt( (String) props.remove("populationSize"));

    //Read in input-/output-value pairs
    Vector truthTable = new Vector();
    while (enum.hasMoreElements()) {
      String s = (String) enum.nextElement();
      float inputValue = Float.parseFloat(s);
      String outS = (String) props.get(s);
      float outputValue = Float.parseFloat(outS);
      truthTable.add(new MatchAgainstTruthTable.Tupel(inputValue, outputValue));
    }

    Repository.apply(truthTable);

    Configuration conf = new DefaultConfiguration();

    // Selectors
    conf.getNaturalSelectors(true).clear();
    conf.addNaturalSelector(new WeightedRouletteSelector(), true);

    // Fitnessfunction
    MatchAgainstTruthTable myFunc =
        new MatchAgainstTruthTable(truthTable);
    conf.setFitnessFunction(myFunc);

    int maxTerms = MAX_ALLOWED_TERMS;
    // Maximum number of alleles needed for a "term gene" is the maximum
    // number of possible different terms.
    Gene[] termGenes = new Gene[maxTerms];
    int numberOfFunctions = Repository.getFunctions().size();
    int numberOfConstants = Repository.getConstants().size();
    int max = (numberOfFunctions + numberOfConstants) * MAX_TERMS_IN_FUNCTION -
        1;
    CompositeGene comp;
    IntegerGene gene;
    for (int i = 0; i < maxTerms; i++) {
      comp = new CompositeGene();
      // Funtions, constants
      gene = new IntegerGene(0, max);
      comp.addGene(gene);
      // Operators
      gene = new IntegerGene(0, Repository.getOperators().size() - 1);
      comp.addGene(gene);
      termGenes[i] = comp;
    }

    Chromosome termChromosome = new Chromosome(termGenes);
    conf.setSampleChromosome(termChromosome);

    conf.setPopulationSize(POPULATION_SIZE);

    Genotype population = Genotype.randomInitialGenotype(conf);

    int i = 0;
    int step = 100;
    int localBestFitness = 0;
    int localCurrentFitness;
    String bestSolutionSoFar = null;

    Chromosome chrom;
    while (i < MAX_ALLOWED_EVOLUTIONS_HARDLIMIT) {

      population.evolve();
      chrom = population.getFittestChromosome();
      String formula = Utility.getFormulaFromChromosome(chrom);
      localCurrentFitness = MatchAgainstTruthTable.calcFitness(formula,
          truthTable);

      if (localCurrentFitness > localBestFitness) {
        // Found a new best solution
        localBestFitness = localCurrentFitness;
        bestSolutionSoFar = formula;
        if (localBestFitness == MatchAgainstTruthTable.MAX_FITNESS) {
          break;
        }
      }

      if (localBestFitness > MIN_FITNESS_WANTED) {
        if (i > MIN_WANTED_EVOLUTIONS) {
          break;
        }
      }

      i++;
      // Simple progress display
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

    // Output of result
    System.out.println("Number of generations: " + i);

    System.out.println("Best solution so far: ");
    String formula = bestSolutionSoFar;
    System.out.println("Formula: " + formula);
    System.out.println("Fitness value: " + localBestFitness);
  }
}
