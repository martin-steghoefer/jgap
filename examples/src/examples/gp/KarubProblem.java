/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.GPProblem;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.function.GreaterThan;
import org.jgap.gp.function.If;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.terminal.Constant;
import org.jgap.gp.terminal.False;
import org.jgap.gp.terminal.True;
import org.jgap.gp.terminal.Variable;

/**
 * Sort of minimal GP problem definition. Does not stop when best solution
 * found. Discovers a very simple formula (see comments in code!).
 *
 * Only there to show the general setup.
 *
 * Inspired by user Karub (who wrote most of the code in this class),
 * therefor "KarubProblem"
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class KarubProblem
    extends GPProblem {
  public static Variable vx;

  public KarubProblem(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  public GPGenotype create()
      throws InvalidConfigurationException {
    GPConfiguration conf = getGPConfiguration();
    Class[] types = {CommandGene.BooleanClass};
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        vx = Variable.create(conf, "X", CommandGene.IntegerClass),
        new Constant(conf, CommandGene.IntegerClass, 0),
        new GreaterThan(conf, CommandGene.IntegerClass),
        new If(conf, CommandGene.BooleanClass),
        new True(conf),
        new False(conf)
    }
    };
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        100, true);
  }

  public static void main(String[] args)
      throws Exception {
    System.out.println("Formula to discover: if x>0 ret 1 else ret 0");
    // Setup the algorithm's parameters.
    GPConfiguration config = new GPConfiguration();
    // We use a delta fitness evaluator because we compute a defect rate, not a point score!
    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config.setMaxInitDepth(4);
    config.setPopulationSize(100);
    config.setMaxCrossoverDepth(8);
    config.setFitnessFunction(new KarubProblem.FormulaFitnessFunction());
    config.setStrictProgramCreation(!true);
    GPProblem problem = new KarubProblem(config);
    // Create the genotype of the problem, i.e., define the GP commands and
    // terminals that can be used, and constrain the structure of the GP program.
    GPGenotype gp = problem.create();
    gp.setVerboseOutput(true);
    // Start the computation with maximum 200 evolutions. If a satisfying result is found
    // (fitness value almost 0), JGAP stops earlier automatically.
    System.out.println("Start evolution ...");
    gp.evolve(200);
    // Print the best solution so far to the console.
    gp.outputSolution(gp.getAllTimeBest());
    // Create a graphical tree of the best solution's program and write it to a PNG file.
    problem.showTree(gp.getAllTimeBest(), "mathproblem_best.png");
    System.out.println("Graphical tree of the solution built and saved. BYE.");
  }

  public static class FormulaFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram ind) {
      int error = 0;
      Object[] noargs = new Object[0];
      // Evaluate function for input numbers 0 to 10.
      for (int i = -10; i < 10; i++) {
        vx.set(new Integer(i));
        boolean y = false;
        if (i > 0) {
          y = true;
        }
        try {
          boolean result = ind.execute_boolean(0, noargs);
          if (result != y)
            error++;
        } catch (ArithmeticException ex) { // some illegal operation was executed.
          System.out.println("x = " + i);
          System.out.println(ind);
          throw ex;
        }
      }
      return error;
    }
  }
}
