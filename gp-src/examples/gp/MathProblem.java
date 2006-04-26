package examples.gp;

import org.jgap.gp.*;
import org.jgap.impl.*;
import org.jgap.*;
import java.util.*;

public class MathProblem
    extends GPGenotype {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  static Variable vx;

  static Float[] x = new Float[20];

  static float[] y = new float[20];

  public MathProblem(Population a_pop)
      throws InvalidConfigurationException {
    super(a_pop);
  }

  /**@todo this should be randomInitializeGenotype*/
  public static Population create(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    Class[] types = {
        CommandGene.floatClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        vx = Variable.create(a_conf, "X", CommandGene.floatClass),
        new AddCommand(a_conf, CommandGene.floatClass),
        new SubtractCommand(a_conf, CommandGene.floatClass),
        new MultiplyCommand(a_conf, CommandGene.floatClass),
//            new DivideCommand(a_conf, CommandGene.floatClass),
//        new SinCommand(a_conf, CommandGene.floatClass),
//        new CosCommand(a_conf, CommandGene.floatClass),
        new ExpCommand(a_conf, CommandGene.floatClass),
//        new NaturalLogarithmCommand(a_conf, CommandGene.floatClass),
    }
    };
    Random random = new Random();
    // randomly initialize function data (X-Y table) for x^4+x^3+x^2+x
    for (int i = 0; i < 20; i++) {
      float f = 2.0f * (random.nextFloat() - 0.5f);
      x[i] = new Float(f);
      y[i] = f * f * f * f + f * f * f + f * f - f;
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create initial population
    return create(a_conf, types, argTypes, nodeSets);
  }

  // Method for testing purpose only during development phase
  public static void main(String[] args)
      throws Exception {
    System.out.println("Formula to discover: x^4+x^3+x^2+x");
    GPConfiguration config = new GPConfiguration(null);
    config.setMaxInitDepth(8);
    config.setPopulationSize(800);
    config.setFitnessFunction(new MathProblem.FormulaFitnessFunction());
    GPGenotype gp = new GPGenotype(config, create(config));
    gp.computeAll(800);
    gp.outputSolution(gp.getAllTimeBest());
    /**@todo remove?*/
    /*
        Configuration conf = new MathConfiguration(null);
        conf.setEventManager(new EventManager());
        // Set the fitness function we want to use, which is our
        // MinimizingMakeChangeFitnessFunction. We construct it with
        // the target amount of change passed in to this method.
        // ---------------------------------------------------------
//    FitnessFunction myFunc = new FormulaFitnessFunction();
//    conf.setFitnessFunction(myFunc);
        conf.setPopulationSize(100);
        Gene[] sampleGenes = new Gene[3];
        sampleGenes[0] = new AddCommand();
        sampleGenes[1] = new Terminal();
        sampleGenes[2] = new Terminal();
        Chromosome sampleChromosome = new Chromosome(sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        conf.addNaturalSelector(new BestChromosomesSelector(), true);
        conf.addGeneticOperator(new MutationOperator());
        conf.setEventManager(new EventManager());
        conf.setChromosomePool(new ChromosomePool());
        conf.addGeneticOperator(new AveragingCrossoverOperator());
        conf.setRandomGenerator(new StockRandomGenerator());
        GPGenotype population;
        population = (GPGenotype) GPGenotype.randomInitialGenotype(conf);
        conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
        population.evolve(50);
        Chromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println(bestSolutionSoFar.getFitnessValue());
        MathLanguage.printProgram((ProgramChromosome)bestSolutionSoFar);
     */
  }

  public static class FormulaFitnessFunction
      extends FitnessFunction {
    protected double evaluate(IChromosome a_subject) {
//      return 1.0f / (1.0f + computeRawFitness( (ProgramChromosome) a_subject));
      return computeRawFitness( (ProgramChromosome) a_subject);
    }

    public double computeRawFitness(ProgramChromosome ind) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      for (int i = 0; i < 20; i++) {
        vx.set(x[i]);
        try {
          double result = ind.execute_float(noargs);
          error += Math.abs(result - y[i]);
        }
        catch (ArithmeticException ex) {
          System.out.println("x=" + x[i].floatValue());
          System.out.println(ind);
          throw ex;
        }
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      return error;
    }
  }
}
