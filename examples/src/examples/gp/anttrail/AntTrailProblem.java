/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import java.io.*;
import java.util.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.impl.*;
import org.jgap.event.*;
import org.jgap.*;
import org.jgap.util.tree.*;

/**
 * The ant trail problem.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class AntTrailProblem
    extends GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private static int[][] map;

  private static int foodAvail;

  private static int m_maxx;

  private static int m_maxy;

  public AntTrailProblem(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  /**
   * Sets up the functions to use and other parameters. Then creates the
   * initial genotype.
   *
   * @return the genotype created
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype create()
      throws InvalidConfigurationException {
    Class[] types = {CommandGene.VoidClass};
    Class[][] argTypes = { {}
    };
    int[] minDepths = new int[] {6};
    int[] maxDepths = new int[] {12};
    GPConfiguration conf = getGPConfiguration();
    CommandGene[][] nodeSets = { {
        new SubProgram(conf, new Class[] {CommandGene.VoidClass}),
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass}),
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass}),
        new Left(conf),
        new Right(conf),
        new Move(conf),
        new IfFoodAheadElse(conf),
    }
    };
    // Create genotype with initial population.
    // ----------------------------------------
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        minDepths, maxDepths, 400, new boolean[] {true}, true);
  }

  private static int[][] readTrail(String a_filename)
      throws Exception {
    LineNumberReader lnr;
    try {
      lnr = new LineNumberReader(new FileReader(a_filename));
    } catch (FileNotFoundException fex) {
      throw new FileNotFoundException("File not found: " + new File(".").getAbsolutePath() +
                                      a_filename);
    }
    // Read dimensions of trail.
    // -------------------------
    try {
      StringTokenizer st = new StringTokenizer(lnr.readLine());
      m_maxx = Integer.parseInt(st.nextToken());
      m_maxy = Integer.parseInt(st.nextToken());
      int[][] result = new int[m_maxx][m_maxy];
      int y;
      foodAvail = 0;
      for (y = 0; y < m_maxy; y++) {
        String s = lnr.readLine();
        if (s == null) {
          throw new RuntimeException("Ant trail file ended prematurely");
        }
        int x;
        for (x = 0; x < s.length(); x++) {
          if (s.charAt(x) == ' ')
            result[x][y] = AntMap.EMPTY;
          else if (s.charAt(x) == '#') {
            result[x][y] = AntMap.FOOD;
            foodAvail++;
          }
          else if (s.charAt(x) == '.')
            result[x][y] = AntMap.TRAIL;
          else
            throw new RuntimeException("Bad character '" + s.charAt(x) +
                                       "' on line number " + lnr.getLineNumber() +
                                       " of the Ant trail file.");
        }
        // fill out rest of X's
        for (int z = x; z < m_maxx; z++)
          result[z][y] = AntMap.EMPTY;
      }
      // fill out rest of Y's
      for (int z = y; z < m_maxy; z++) {
        for (int x = 0; x < m_maxx; x++) {
          result[x][z] = AntMap.EMPTY;
        }
      }
      return result;
    } catch (NumberFormatException e) {
      throw new RuntimeException(
          "The Ant trail file does not begin with x and y integer values.");
    } catch (IOException e) {
      throw new RuntimeException(
          "The Ant trail file could not be read due to an IOException:\n" + e);
    }
  }

  /**
   * Starts the example.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void main(String[] args) {
    try {
      System.out.println("Ant trail problem");
      GPConfiguration config = new GPConfiguration();
      int popSize;
      if (args.length == 1) {
        popSize = Integer.parseInt(args[0]);
      }
      else {
        popSize = 1200;
      }
      System.out.println("Using population size of " + popSize);
      config.setMaxInitDepth(6);
      config.setPopulationSize(popSize);
      config.setFitnessFunction(new AntTrailProblem.AntFitnessFunction());
      config.setStrictProgramCreation(false);
      config.setProgramCreationMaxTries(5);
      final GPProblem problem = new AntTrailProblem(config);
      GPGenotype gp = problem.create();
      gp.setVerboseOutput(true);
      // Read the trail from file.
      // -------------------------
      map = readTrail("santafe.trail");
      AntMap antmap = new AntMap(map);
      System.out.println("Food to consume by ant: " + countFood(antmap));
      // Simple implementation of running evolution in a thread.
      // -------------------------------------------------------
      final Thread t = new Thread(gp);
      config.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_EVOLVED_EVENT, new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          double freeMem = GPGenotype.getFreeMemoryMB();
          if (evno % 50 == 0) {
            double bestFitness = genotype.getFittestProgram().
                getFitnessValue();
            System.out.println("Evolving generation " + evno
                               + ", best fitness: " + bestFitness
                               + ", memory free: " + freeMem + " MB");
          }
          if (evno > 3000) {
            t.stop();
          }
          else {
            try {
              // Collect garbage if memory low.
              // ------------------------------
              if (freeMem < 50) {
                System.gc();
                t.sleep(500);
              }
              else {
                // Avoid 100% CPU load.
                // --------------------
                t.sleep(30);
              }
            } catch (InterruptedException iex) {
              iex.printStackTrace();
              System.exit(1);
            }
          }
        }
      });
      config.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_NEW_BEST_SOLUTION, new GeneticEventListener() {
        /**
         * New best solution found.
         *
         * @param a_firedEvent GeneticEvent
         */
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          String indexString = "" + evno;
          while (indexString.length() < 5) {
            indexString = "0" + indexString;
          }
          String filename = "anttrail_best" + indexString + ".png";
          IGPProgram best = genotype.getAllTimeBest();
          try {
            TreeBranchRenderer antBranchRenderer = new AntTreeBranchRenderer();
            TreeNodeRenderer antNodeRenderer = new AntTreeNodeRenderer();
            problem.showTree(best, filename, antBranchRenderer, antNodeRenderer);
          } catch (InvalidConfigurationException iex) {
            iex.printStackTrace();
          }
          double bestFitness = genotype.getFittestProgram().
              getFitnessValue();
          if (bestFitness < 0.001) {
            genotype.outputSolution(best);
            t.stop();
            System.exit(0);
          }
        }
      });
      t.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public static class AntFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(final IGPProgram a_program) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      // Initialize local stores.
      // ------------------------
      GPGenotype.getGPConfiguration().clearStack();
      GPGenotype.getGPConfiguration().clearMemory();
      AntMap antmap = new AntMap(map);
      a_program.getGPConfiguration().storeInMemory("map", antmap);
      // number of moves proceeded
      int maxMoves;
      // how much food is consumed?
      int food;
      // Compute fitness for each program.
      // ---------------------------------
      for (int j = 0; j < a_program.size(); j++) {
        try {
          try {
            // Execute memory manipulating subprograms.
            // ----------------------------------------
            a_program.execute_void(j, noargs);
          } catch (IllegalStateException iex) {
            error = GPFitnessFunction.MAX_FITNESS_VALUE;
            break;
          }
        } catch (ArithmeticException ex) {
          System.out.println(a_program.getChromosome(j));
          throw ex;
        }
      }
      // Determine success of individual.
      // --------------------------------
      antmap = (AntMap) a_program.getGPConfiguration().readFromMemory("map");
      food = countFood(antmap);
      // The remaining food is the defect rate here.
      // -------------------------------------------
      error = food;
      if (GPGenotype.getGPConfiguration().stackSize() > 0) {
        error = GPFitnessFunction.MAX_FITNESS_VALUE;
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      else if (error < GPFitnessFunction.MAX_FITNESS_VALUE) {
        /**@todo add penalty for longer trails*/
      }
      return error;
    }
  }
  private static int countFood(AntMap a_map) {
    int result = 0;
    for (int x = 0; x < m_maxx; x++) {
      for (int y = 0; y < m_maxy; y++) {
        if (a_map.getFromMap(x, y) == AntMap.FOOD) {
          result++;
        }
      }
    }
    return result;
  }
}
