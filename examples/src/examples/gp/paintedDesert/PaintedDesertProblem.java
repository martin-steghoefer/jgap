/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * The Painted Desert problem from Koza's "Evolution of Emergent Cooperative
 * Behavior using Genetic Programming".  The problem is to create the same
 * genetic program for a group of ants that will move three colors of sand into
 * columns of like sand.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class PaintedDesertProblem
    extends GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";
  /**
   * Local copy of the map read into from the file.
   */
  private int[][] m_map;

  /**
   * Set by readMap and passed to the AntMap constructor
   */
  private Ant[] m_ants;

  /**
   * Holds the AntMap object used to hold the initial and current map of sand
   * and Ants.
   */
  private static AntMap m_antMap;

  /**
   * The maximum number of locations in the East or X direction.
   */
  private static int m_maxx;

  /**
   * The maximum number of locaitons in the North or Y direction.
   */
  private static int m_maxy;

  /**
   * The number of ants in the map.
   */
  private static int m_popSize;

  /**
   * Maximum number of moves allowed.
   */
  private static int m_maxMoves = 300;

  /**
   * Creates the Painted Desert Problem using the GPConfiguration.
   * @param a_conf the GP configuration
   * @throws InvalidConfigurationException
   */
  public PaintedDesertProblem(GPConfiguration a_conf)
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
   * @author Scott Mueller
   */
  public GPGenotype create()
      throws InvalidConfigurationException {
    Class[] types = {CommandGene.VoidClass};
    Class[][] argTypes = { {}
    };
    int[] minDepths = new int[] {2};
    int[] maxDepths = new int[] {8};
    GPConfiguration conf = getGPConfiguration();
    CommandGene[][] nodeSets = { {
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass}),
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass}),
        new X(conf),
        new Y(conf),
        new Carrying(conf),
        new SandColor(conf),
        new GO_N(conf),
        new GO_E(conf),
        new GO_S(conf),
        new GO_W(conf),
        new MoveRandom(conf),
        new Pickup(conf),
        new IfDrop(conf, CommandGene.IntegerClass),
        new IfLessThanOrEqual(conf, CommandGene.IntegerClass),
        new IfLessThanZero(conf, CommandGene.IntegerClass),
        new IfDrop(conf, CommandGene.IntegerClass),
    }
    };
    // Create genotype with initial population.
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        minDepths, maxDepths, 5000, new boolean[] {!true}, true);
  }

  /**
   * Reads the map from a file.  The first line contains the number of X and Y locations and
   * the number of Ants on the map. A 'b' at a location represents a Black grain of sand.
   * A 's' represents a striped grain of sands.  A 'g' represents a grey grain of sand.  An
   * 'A' represents an Ant.  A 'B' represents an Ant and a black grain of sand at the
   * same location.  A 'S' represents an Ant and a striped grain of sand at the same location.
   * A 'G' represents an Ant and a grey grain of sand at the same location.
   *
   * A side effect of this function is setting the m_ants member variable.
   *
   * @param a_filename The location of the file containing the map information.
   * @return An array of the sand locations read in from the map.
   * @throws Exception
   */
  private int[][] readMap(String a_filename)
      throws Exception {
    LineNumberReader lnr;
    try {
      lnr = new LineNumberReader(new FileReader(a_filename));
    } catch (FileNotFoundException fex) {
      throw new FileNotFoundException("File not found: " +
                                      new File(".").getAbsolutePath() +
                                      a_filename);
    }
    // Read dimensions of trail and the number of ants.
    try {
      StringTokenizer st = new StringTokenizer(lnr.readLine());
      m_maxx = Integer.parseInt(st.nextToken());
      m_maxy = Integer.parseInt(st.nextToken());
      m_popSize = Integer.parseInt(st.nextToken());
      int[][] result = new int[m_maxx][m_maxy];
      m_ants = new Ant[m_popSize];
      int y;
      int antIndex = 0;
      for (y = 0; y < m_maxy; y++) {
        String s = lnr.readLine();
        System.out.println(s);
        if (s == null) {
          throw new RuntimeException("Ant map file ended prematurely");
        }
        int x;
        for (x = 0; x < s.length() & x < m_maxx; x++) {
          if (s.charAt(x) == ' ') {
            result[x][y] = AntMap.EMPTY;
          }
          else if (s.charAt(x) == 'b') {
            result[x][y] = AntMap.BLACK;
          }
          else if (s.charAt(x) == 'g') {
            result[x][y] = AntMap.GRAY;
          }
          else if (s.charAt(x) == 's') {
            result[x][y] = AntMap.STRIPED;
          }
          else if (s.charAt(x) == 'A') {
            result[x][y] = AntMap.EMPTY;
            m_ants[antIndex] = new Ant(x, y);
            antIndex++;
          }
          else if (s.charAt(x) == 'S') {
            result[x][y] = AntMap.STRIPED;
            m_ants[antIndex] = new Ant(x, y);
            antIndex++;
          }
          else if (s.charAt(x) == 'G') {
            result[x][y] = AntMap.GRAY;
            m_ants[antIndex] = new Ant(x, y);
            antIndex++;
          }
          else if (s.charAt(x) == 'B') {
            result[x][y] = AntMap.BLACK;
            m_ants[antIndex] = new Ant(x, y);
            antIndex++;
          }
          else {
            throw new RuntimeException("Bad character '" + s.charAt(x) +
                                       "' on line number " + lnr.getLineNumber() +
                                       " of the Ant map file.");
          }
        }
        // fill out rest of X's
        for (int z = x; z < m_maxx; z++) {
          result[z][y] = AntMap.EMPTY;
        }
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
          "The Ant map file does not begin with x and y and nbrOfAnts integer values.");
    } catch (IOException e) {
      throw new RuntimeException(
          "The Ant map file could not be read due to an IOException:\n" + e);
    }
  }

  /**
   * Runs the Painted Desert Problem
   *
   * @param args The location of the ant map file is optional
   * @throws Exception
   *
   * @author Scott Mueller
   */
  public static void main(String[] args) {
    try {
      System.out.println("Painted Desert Problem");
      GPConfiguration config = new GPConfiguration();
      config.setSelectionMethod(new TournamentSelector(3));
      config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
      int popSize = 300;
      String filename;
      if (args.length == 1) {
        filename = args[0];
      }
      else {
        filename = "standard.desert";
      }
      System.out.println("Using population size of " + popSize);
      System.out.println("Using map " + filename);
      config.setMaxInitDepth(10);
      config.setPopulationSize(popSize);
      final PaintedDesertProblem problem = new PaintedDesertProblem(config);
      GPFitnessFunction func = problem.createFitFunc();
      config.setFitnessFunction(func);
      config.setCrossoverProb(0.4f);
      config.setReproductionProb(0.6f);
      config.setFunctionProb(0.6f);
      config.setNewChromsPercent(0.3f);
      config.setStrictProgramCreation(true);
      config.setUseProgramCache(false);
      GPGenotype gp = problem.create();
      gp.setVerboseOutput(true);
      // Read the map from file.
      problem.m_map = problem.readMap(filename);
      problem.displaySolution(problem.m_map, problem.m_map);
      m_antMap = new AntMap(problem.m_map, problem.m_ants);
      // Simple implementation of running evolution in a thread.

      final Thread t = new Thread(gp);
      IEventManager eventManager = config.getEventManager();
      eventManager.addEventListener(GeneticEvent.
                                    GPGENOTYPE_EVOLVED_EVENT,
                                    new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          double freeMem = SystemKit.getFreeMemoryMB();
          if (evno % 10 == 0) {
            double bestFitness = genotype.getFittestProgram().
                getFitnessValue();
            System.out.println("Evolving generation " + evno
                               + ", best fitness: " + bestFitness
                               + ", memory free: " + freeMem + " MB");
          }
          if (evno > 500000) {
            t.stop();
          }
          else {
            try {
              // Collect garbage if memory low.
              if (freeMem < 50) {
                System.gc();
                t.sleep(500);
              }
              else {
                // Avoid 100% CPU load.
                t.sleep(30);
              }
            } catch (InterruptedException iex) {
              iex.printStackTrace();
              System.exit(1);
            }
          }
        }
      });
      eventManager.addEventListener(GeneticEvent.
                                    GPGENOTYPE_NEW_BEST_SOLUTION,
                                    new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          String indexString = "" + evno;
          while (indexString.length() < 5) {
            indexString = "0" + indexString;
          }
//          String filename = "painteddesert_best" + indexString + ".png";
          IGPProgram best = genotype.getAllTimeBest();
          // Display solution's final map.
          // -----------------------------
          AntMap antmap = (AntMap) best.getApplicationData();
          problem.displaySolution(antmap.getMap(), antmap.getInitialMap());
          java.text.DateFormat df = java.text.DateFormat.getTimeInstance(java.
              text.DateFormat.SHORT);
          String time = df.format(new java.util.Date());
          System.out.println(time + " Number of moves: " + antmap.getMoveCount());
          double bestFitness = genotype.getFittestProgram().
              getFitnessValue();
          if (bestFitness < 0.001) {
            genotype.outputSolution(best);
            t.stop();
            System.exit(0);
          }
        }
      });
      //
//      eventManager.addEventListener(GeneticEvent.
//                                    GPGENOTYPE_NEW_BEST_SOLUTION,
//                                    new MyGeneticEventListener(problem, t));
      t.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Display ant map as found by GP.
   *
   * @param a_antmap the map containing the ants and grains of sand
   * @param a_origMap the original map
   */
  private static void displaySolution(int[][] a_antmap, int[][] a_origMap) {
    for (int y = 0; y < m_maxy; y++) {
      for (int x = 0; x < m_maxx; x++) {
        char toPrint = '?';
        int c = a_antmap[x][y];
        switch (c) {
          case AntMap.ANT_AT_POSITION:
            toPrint = 'A';
            break;
          case AntMap.BLACK:
            toPrint = 'b';
            break;
          case AntMap.GRAY:
            toPrint = 'g';
            break;
          case AntMap.EMPTY:
            toPrint = ' ';
            break;
          case AntMap.STRIPED:
            toPrint = 's';
            break;
        }
        System.out.print(toPrint);
      }
      System.out.print("  ");
      for (int x = 0; x < m_maxx; x++) {
        char toPrint = '?';
        int c = a_origMap[x][y];
        switch (c) {
          case AntMap.ANT_AT_POSITION:
            toPrint = 'A';
            break;
          case AntMap.BLACK:
            toPrint = 'b';
            break;
          case AntMap.GRAY:
            toPrint = 'g';
            break;
          case AntMap.EMPTY:
            toPrint = ' ';
            break;
          case AntMap.STRIPED:
            toPrint = 's';
            break;
        }
        System.out.print(toPrint);
      }
      System.out.println();
    }
    System.out.println();
  }

  private GPFitnessFunction createFitFunc() {
    return new AntFitnessFunction();
  }

  /**
   * Represents the fitness funtion.  Counts the grains of sand to that are not in the
   * correct column.  This varies from the measurement used by Koza and tries to penalize
   * the ants that are further from the proper location.
   *
   * @author Scott Mueller
   *
   */
  class AntFitnessFunction
      extends GPFitnessFunction {
    private static final int VALUE1 = 100;

    protected double evaluate(final IGPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(final IGPProgram a_program) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      // Initialize local stores.
      a_program.getGPConfiguration().clearStack();
      a_program.getGPConfiguration().clearMemory();
      a_program.setApplicationData(m_antMap);
      try {
        m_antMap.init();
        // Execute the program for each ant in turn.
        for (int antIndex = 0; antIndex < m_popSize; antIndex++) {
          m_antMap.nextAnt();
          a_program.execute_void(0, noargs);
        }
        // Determine success of individual.
        // --------------------------------
        error = (double) m_antMap.fitness();
      } catch (IllegalStateException iex) {
        error = GPFitnessFunction.MAX_FITNESS_VALUE;
      }
      return error;
    }
  }
  /**
   * Resets the ants to initial positions. So they can be run with the next version
   * of the program.
   */
  public void resetAnts() {
    for (int antIndex = 0; antIndex < m_popSize; antIndex++) {
      m_antMap.getAnts()[antIndex].reset();
    }
  }

}
