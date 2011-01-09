/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.tictactoe;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.function.*;
import org.jgap.gp.terminal.*;
import org.jgap.util.*;
import org.jgap.impl.*;
import org.apache.log4j.Logger;

/**
 * Example demonstrating Genetic Programming (GP) capabilities of JGAP.<p>
 * Here, a strategy for playing Noughts and Crosses (Tic Tac Toe) is evolved.<p>
 * THIS PROGRAM IS STILL UNDER DEVELOPMENT AND IS NOT FINISHED YET! ANY COMMENTS
 * AND EXTENSIONS ARE VERY WELCOME!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class TicTacToeMain
    extends GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public transient static Logger LOGGER = Logger.getLogger(TicTacToeMain.class);

  private static Variable vb;

  private Board m_board;

  public static CommandGene LOOP;
  public static CommandGene SUBPROGRAM1;
  public static CommandGene SUBPROGRAM2;
  public static CommandGene PUTSTONE1;

  public TicTacToeMain(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    m_board = new Board();
  }

  public Board getBoard() {
    return m_board;
  }

  /**
   * Sets up the functions to use and other parameters. Then creates the
   * initial genotype.
   *
   * @param a_conf the configuration to use
   * @param a_color the color to create a program for
   * @param a_other an optional opponent, may be null
   * @param a_otherColor color of the opponent
   *
   * @return the genotype created
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPGenotype create(GPConfiguration a_conf, int a_color,
                           GPGenotype a_other, int a_otherColor)
      throws InvalidConfigurationException {
    Class[] types = {CommandGene.VoidClass, CommandGene.VoidClass,
        CommandGene.VoidClass, CommandGene.VoidClass};
    Class[][] argTypes = { {}, {}, {}, {}
    };
    int[] minDepths = new int[] {0, 2, 2, 1};
    int[] maxDepths = new int[] {0, 2, 15, 4};
    int color = a_color;
    int color2;
    if (a_color == 2) {
      color2 = 1;
    }
    else {
      color2 = 2;
    }
    ForLoop forLoop1 = new ForLoop(a_conf, SubProgram.VoidClass, 1, Board.WIDTH,
                                   1, "x", 0, 0);
    ForLoop forLoop2 = new ForLoop(a_conf, SubProgram.VoidClass, 1,
                                   Board.HEIGHT,
                                   1, "y", 0, 0);
    Variable vx = new Variable(a_conf, "move", CommandGene.IntegerClass);
    Variable vb = new Variable(a_conf, "firstmove", CommandGene.BooleanClass);
    //
    final String MATRIX1 = "MATRIX1";
    a_conf.createMatrix(MATRIX1, 3, 3);
    //
    LOOP = new Loop(a_conf, CommandGene.IntegerClass,
                 Board.WIDTH * Board.HEIGHT);
    SUBPROGRAM1 = new SubProgram(a_conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass});
    SUBPROGRAM2 = new SubProgram(a_conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass});
    PUTSTONE1 = new PutStone1(a_conf, m_board, color, 0, 0);
    //
    CommandGene[][] nodeSets = { {
        // Transfer board to evolution memory.
        // -----------------------------------
        new TransferBoardToMemory(a_conf, m_board, 0, 0),
    }, {
        // Create strategy data.
        // ---------------------
//        LOOP,
        new EvaluateBoard(a_conf, m_board, CommandGene.IntegerClass),
        new Terminal(a_conf, CommandGene.IntegerClass, 9.0d, 9, true),
//        new IncrementMemory(a_conf, CommandGene.IntegerClass, "counter", 10),
        /**@todo evaluate board to matrix*/
    }, {
        // Evaluate.
        // ---------
        vx,
        vb,
        SUBPROGRAM1,
        SUBPROGRAM2,
        new SubProgram(a_conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass,
                       CommandGene.VoidClass}),
        forLoop1,
//        forLoop2,
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 0),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 1),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 2),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 3),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 4),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 5),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 6),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 10, 22),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 11, 22),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 12, 22),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 14, 23),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 13, 22),
//        new EvaluateBoard(a_conf, m_board, 0),
        new Loop(a_conf, SubProgram.class, Board.WIDTH),
        new Loop(a_conf, SubProgram.class, Board.HEIGHT),
        new Loop(a_conf, SubProgram.class, Board.WIDTH * Board.HEIGHT),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(0), 55),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(1), 55),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(2), 55),
//        new Constant(a_conf, CommandGene.IntegerClass, new Integer(3), 55),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(0), 66),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(1), 66),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(2), 66),
        new Terminal(a_conf, CommandGene.IntegerClass, 1.0d, Board.WIDTH, true,
                     4),
        new Terminal(a_conf, CommandGene.IntegerClass, 1.0d, Board.HEIGHT, true,
                     4),
        new Modulo(a_conf, CommandGene.IntegerClass),
        new Add(a_conf, CommandGene.IntegerClass),
        new Subtract(a_conf, CommandGene.IntegerClass),
        new Divide(a_conf, CommandGene.IntegerClass),
        new Multiply(a_conf, CommandGene.IntegerClass),
        new Equals(a_conf, CommandGene.IntegerClass, 0, new int[] {22, 23}),
        new Equals(a_conf, CommandGene.IntegerClass, 0, new int[] {0, 8}),
        new Equals(a_conf, CommandGene.IntegerClass),
        new GreaterThan(a_conf, CommandGene.IntegerClass),
        new IfElse(a_conf, CommandGene.BooleanClass),
        new ReadBoard(a_conf, m_board, 0, new int[] {4, 4}),
        new ReadBoard(a_conf, m_board),
        new Not(a_conf),
        new Push(a_conf, CommandGene.IntegerClass),
        new Pop(a_conf, CommandGene.IntegerClass),
        new IfIsOccupied(a_conf, m_board, CommandGene.IntegerClass, 0,
                         new int[] {4, 4, 0}),
        new IfIsFree(a_conf, m_board, CommandGene.IntegerClass, 0, new int[] {4,
                     4, 0}),
        new CountStones(a_conf, m_board, color, "count"),
        new CountStones(a_conf, m_board, color2, "count"),
        new IfColor(a_conf, CommandGene.IntegerClass, color, 0, 0),
        new IsOwnColor(a_conf, color),
        new Increment(a_conf, CommandGene.IntegerClass, 1),
        new Increment(a_conf, CommandGene.IntegerClass, -1),
        new StoreTerminalIndexed(a_conf, 0, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 1, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 2, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 3, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 4, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 5, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 6, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 10, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 11, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 12, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 14, CommandGene.IntegerClass),
        new StoreTerminalIndexed(a_conf, 13, CommandGene.IntegerClass),
        new StoreTerminal(a_conf, "mem0", CommandGene.IntegerClass),
//        new StoreTerminal(conf, "mem1", CommandGene.IntegerClass),
        new AddAndStoreTerminal(a_conf, "memA", CommandGene.IntegerClass),
//        new AddAndStoreTerminal(conf, "memB", CommandGene.IntegerClass),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "counter", 0),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "mem0"),
//        new ReadTerminal(conf, CommandGene.IntegerClass, "mem1"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "memA"),
//        new ReadTerminal(conf, CommandGene.IntegerClass, "memB"),
//        new ReadTerminal(conf, CommandGene.IntegerClass, "countr0", 1),
//        new ReadTerminal(conf, CommandGene.IntegerClass, "countr1", 1),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "count"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "count2"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "countc0", 8),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "countc1", 8),
//        new ReadTerminal(a_conf, CommandGene.IntegerClass, "countd0"),
//        new ReadTerminal(a_conf, CommandGene.IntegerClass, "countd1"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass,
                         forLoop1.getCounterMemoryName(), 5),
//        new ReadTerminal(a_conf, CommandGene.IntegerClass,
//                         forLoop2.getCounterMemoryName(), 6),
        new ReadFromMatrix(a_conf, MATRIX1),
        new WriteToMatrix(a_conf, MATRIX1, 55, 55, 66),
        new ResetMatrix(a_conf, MATRIX1, ' '),
        new CountMatrix(a_conf, MATRIX1, CountMatrix.CountType.COLUMN,
                        CountMatrix.CountMode.EMPTY, ' ', ' '),
        new ReplaceInMatrix(a_conf, MATRIX1,
                            ReplaceInMatrix.ReplacementMode.COLUMN, "ABC",
                            'R'),
    }, {
        // Make a move.
        // ------------
        vb,
//        vx,
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "count"),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(1)),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(2)),
        new Equals(a_conf, CommandGene.IntegerClass),
        new PutStone(a_conf, m_board, color),
        PUTSTONE1,
        new IfIsFree(a_conf, m_board, CommandGene.IntegerClass),
        new IfElse(a_conf, CommandGene.BooleanClass),
        new Increment(a_conf, CommandGene.IntegerClass, 1),
        new Increment(a_conf, CommandGene.IntegerClass, -1),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 1, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 2, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 3, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 4, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 5, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 6, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 7, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 8, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 9, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 10, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 11, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 12, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 13, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 14, 33),
        new ReadTerminalIndexed(a_conf, CommandGene.IntegerClass, 15, 33),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "mem0"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "mem1"),
        new ReadTerminal(a_conf, CommandGene.IntegerClass, "memA"),
        new SubProgram(a_conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass}),
        new Terminal(a_conf, CommandGene.IntegerClass, 1.0d, Board.WIDTH, true),
        new Terminal(a_conf, CommandGene.IntegerClass, 1.0d, Board.HEIGHT, true),
        new IfIsOccupied(a_conf, m_board, CommandGene.IntegerClass),
    }
    };
    a_conf.setFitnessFunction(new TicTacToeMain.
                              GameFitnessFunction(getBoard(), a_color, a_other,
        a_otherColor));
//    }
    // Create genotype with initial population.
    // ----------------------------------------
    GPGenotype result = GPGenotype.randomInitialGenotype(a_conf, types,
        argTypes, nodeSets, minDepths, maxDepths, 600,
        new boolean[] {!true, !true, !true, !true}, true);
    // Register variables to later have access to them.
    // ------------------------------------------------
    result.putVariable(vb);
    result.putVariable(vx);
    return result;
  }

  public GPGenotype create()
      throws InvalidConfigurationException {
    throw new InvalidConfigurationException(
        "Please use other create-method!");
  }

  /**
   * Starts the example.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void main(String[] args) {
    try {
      System.out.println("Task: Find a strategy for playing Tic Tac Toe");
      GPConfiguration config = new GPConfiguration();
      config.setInitStrategy(new InitStrategy());
      config.setRandomGenerator(new GaussianRandomGenerator());
      config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
      int popSize;
      popSize = 50;
      System.out.println("Using population size of " + popSize);
      // Setup for player 1.
      // -------------------
      config.setMaxInitDepth(6);
      config.setMinInitDepth(2);
      config.setNewChromsPercent(0.3d);
      config.setPopulationSize(popSize);
      config.setStrictProgramCreation(false);
      config.setProgramCreationMaxTries(30);
      config.setMaxCrossoverDepth(10);
      INodeValidator validator = new GameNodeValidator();
      config.setNodeValidator(validator);
      ISingleNodeValidator singleNodeValidator = new SingleNodeValidator();
      config.setSingleNodeValidator(singleNodeValidator);
      final TicTacToeMain problem = new TicTacToeMain(config);
      config.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_EVOLVED_EVENT, new MyGeneticEventListener(LOGGER));
      // Setup for player 2.
      // -------------------
      GPConfiguration config2 = new GPConfiguration(config.getId() + "_2",
          config.getName() + "_2");
      config2.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
      config2.setMaxInitDepth(7);
      config.setMinInitDepth(2);
      config.setNewChromsPercent(0.3d);
      config2.setPopulationSize(popSize);
      config2.setStrictProgramCreation(false);
      config2.setProgramCreationMaxTries(30);
      config2.setMaxCrossoverDepth(10);
      config2.setNodeValidator(validator);
      final TicTacToeMain problem2 = new TicTacToeMain(config);
      GPGenotype gp2 = problem2.create(config2, 2, null, 1);
      gp2.setVerboseOutput(true);
      config.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_NEW_BEST_SOLUTION, new BestGeneticEventListener(gp2));
//      config2.getEventManager().addEventListener(GeneticEvent.
//          GPGENOTYPE_EVOLVED_EVENT, new MyGeneticEventListener());
      //
      GPGenotype gp1 = problem.create(config, 1, gp2, 2);
      ( (GameFitnessFunction) gp1.getGPConfiguration().getGPFitnessFunction()).
          setPlayer(gp1);
      gp1.setVerboseOutput(true);
      //
      config2.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_NEW_BEST_SOLUTION, new BestGeneticEventListener(gp1));
      //
      ( (GameFitnessFunction) gp2.getGPConfiguration().getGPFitnessFunction()).
          setOpponent(gp1);
      ( (GameFitnessFunction) gp2.getGPConfiguration().getGPFitnessFunction()).
          setPlayer(gp2);
      //
      Coevolution executer = new Coevolution(config, gp1, gp2);
      executer.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  static double bestFitnessYet = -1;
  static IGPProgram bestProgramYet = null;

  public static class GameFitnessFunction
      extends GPFitnessFunction {
    private Board m_board;

    private int m_color;

    private GPGenotype m_other;

    private boolean firstTime;

    private static int maxMoves;

    private static int maxreads;

    private GPGenotype m_player;

    private int nullfound;

    static final double GAME_LOST = 5000;

    static final double MY_WORST_FITNESS_VALUE = 9999999;

    static final double READ_VALUE = 10000;

    static final double UNKNOWN_BUT_SOMETHING = MY_WORST_FITNESS_VALUE -
        5000;

    static final double ONE_MOVE = MY_WORST_FITNESS_VALUE /
        (Board.HEIGHT * Board.WIDTH);

    static final double ONE_MOVE2 = ONE_MOVE * 0.9;

    public GameFitnessFunction(Board a_board, int a_color, GPGenotype a_other,
                               int a_otherColor) {
      m_board = a_board;
      m_color = a_color;
      m_other = a_other;
      firstTime = true;
    }

    public void setPlayer(GPGenotype a_player) {
      m_player = a_player;
    }

    public void setOpponent(GPGenotype a_other) {
      m_other = a_other;
    }

    protected double evaluate(final IGPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(final IGPProgram a_program) {
      /**@todo reduce fitness for NOP */
      double error = MY_WORST_FITNESS_VALUE;
      double errorOpponent = MY_WORST_FITNESS_VALUE;
      Object[] noargs = new Object[0];
      // Determine opponent's program.
      // -----------------------------
      IGPProgram opponent;
      if (firstTime) {
        // First call.
        // -----------
        firstTime = false;
        opponent = m_other.getGPPopulation().getGPProgram(0);
        if (opponent == null) {
          System.err.println("First time: opponent is null!");
        }
      }
      else {
        opponent = m_other.getFittestProgramComputed();
        if (opponent == null) {
          nullfound++;
          if (nullfound == 10 && bestProgramYet != null) {
            opponent = bestProgramYet;
            nullfound = 0;
          }
          else {
            if (nullfound == 100) {
              LOGGER.error("---------- Consecutive calls: opponent is null!");
            }
            opponent = m_other.getGPPopulation().getGPProgram(0);
          }
        }
        else {
          nullfound = 0;
        }
      }
      // Compute fitness for each program.
      // ---------------------------------
      int moves = 0;
      // Set opponent's fitness value to a value to have it set at least here.
      // ---------------------------------------------------------------------
      opponent.setFitnessValue(UNKNOWN_BUT_SOMETHING);
      try {
        while (moves < Board.WIDTH * Board.HEIGHT) {
          m_board.startNewRound();
          Boolean var;
          if (moves == 0) {
            var = new Boolean(true);
          }
          else {
            var = new Boolean(false);
          }
          Integer var2 = new Integer(moves);
          Variable vb1 = m_player.getVariable("firstmove");
          vb1.set(var);
          Variable vb2 = m_other.getVariable("firstmove");
          vb2.set(var);
          Variable vx1 = m_player.getVariable("move");
          vx1.set(var2);
          Variable vx2 = m_other.getVariable("move");
          vx2.set(var2);
          // Initialize local stores.
          // ------------------------
          a_program.getGPConfiguration().clearStack();
          a_program.getGPConfiguration().clearMemory();
          try {
            // First player.
            // -------------
            m_board.beginTurn();
            for (int j = 0; j < a_program.size(); j++) {
              try {
                if (j == a_program.size() - 1) {
                  a_program.execute_void(j, noargs);
                }
                else {
                  a_program.execute_void(j, noargs);
                }
              } catch (IllegalStateException ise) {
                // Ignore, but break the loop.
                break;
              }
            }
            // Value the number of distinct read outs of the board by the
            // player.
            // -----------------------------------------------------------
            int readCount = m_board.getReadPositionCount();
            if (readCount > maxreads) {
              maxreads = readCount;
              if (maxreads > 1) {
                LOGGER.info("**** Number of board reads reached: " +
                                   maxreads);
              }
            }
            error -= readCount * READ_VALUE;
            m_board.endTurn();
            LOGGER.debug("**** First player made a correct move!");
            moves++;
            error -= ONE_MOVE2;
            // Initialize local stores.
            // ------------------------
            a_program.getGPConfiguration().clearStack();
            a_program.getGPConfiguration().clearMemory();
            // Second player.
            // --------------
            m_board.beginTurn();
            for (int j = 0; j < opponent.size(); j++) {
              try {
                if (j == opponent.size() - 1) {
                  opponent.execute_void(j, noargs);
                }
                else {
                  opponent.execute_void(j, noargs);
                }
              } catch (IllegalStateException ise) {
                // Ignore, but break the loop.
                break;
              }
            }
            // Value the number of distinct read outs of the board by the
            // player.
            // -----------------------------------------------------------
            readCount = m_board.getReadPositionCount();
            if (readCount > maxreads) {
              maxreads = readCount;
              if (maxreads > 1) {
                LOGGER.info("**** Number of board reads reached: " +
                                   maxreads);
              }
            }
            errorOpponent -= readCount * READ_VALUE;
            m_board.endTurn();
            moves++;
            errorOpponent -= ONE_MOVE2;
          } catch (GameWonException gex) {
            if (gex.getColor() == m_color) {
              // Best case.
              // ----------
              error -= 0;
              errorOpponent = GAME_LOST;
              break;
            }
            else {
              // Worse case: Player lost, but finished game correctly!
              // -----------------------------------------------------
              error -= GAME_LOST;
              errorOpponent = 0.0d;
              break;
            }
          }
          m_board.endRound();
        }
        LOGGER.fatal("******************* SUPERB: WE MADE IT");
      } catch (IllegalArgumentException iax) {
        // Already cared about by not reducing error rate.
        // -----------------------------------------------
        ;
      } catch (IllegalStateException iex) {
        // Already cared about by not reducing error rate.
        // -----------------------------------------------
      }
      if (maxMoves < moves && moves > 0) {
        maxMoves = moves;
        LOGGER.info("**** Number of valid moves reached: " + maxMoves);
      }
      int depth13 = a_program.getChromosome(3).getSize(0);
      int depth1 = a_program.getChromosome(2).getSize(0) + depth13;
      error -= depth1 * 100;
      if(depth13 < 8) {
        error += (8 - depth13) * 150;
      }
      int depth23 = opponent.getChromosome(3).getSize(0);
      int depth2 = opponent.getChromosome(2).getSize(0) + depth23;
      errorOpponent -= depth2 * 100;
      if(depth23 < 8) {
        errorOpponent += (8 - depth23) * 150;
      }
      /**@todo value memory reads and writes*/
      if (error < 0.000001) {
        error = 0.0d;
      }
      else if (error < (MY_WORST_FITNESS_VALUE * 0.999d)) {
        // Add penalty for very long and very short solutions
        if (depth1 < 12) {
          // Very short solution.
          // --------------------
          error += (12 - depth1) * 200;
        }
        else if (depth1 > 110) {
          // Very long solution.
          // --------------------
          error += (depth1 - 110) * 30;
        }
      }
      if (errorOpponent < 0.000001) {
        errorOpponent = 0.0d;
      }
      else if (errorOpponent < MY_WORST_FITNESS_VALUE * 0.999d) {
        // Add penalty for very long and very short solutions
        if (depth2 < 12) {
            // Very short solution.
            // --------------------
            errorOpponent += (12 - depth2) * 200;
          }
          else if (depth2 > 110) {
            // Very long solution.
            // --------------------
            errorOpponent += (depth2 - 110) * 30;
          }
        }
        opponent.setFitnessValue(errorOpponent);
        if (errorOpponent < bestFitnessYet || bestFitnessYet < 0) {
          bestFitnessYet = errorOpponent;
          bestProgramYet = opponent;
        }
        if (error < bestFitnessYet || bestFitnessYet < 0) {
          bestFitnessYet = error;
        bestProgramYet = a_program;
      }
      return error;
    }
  }
}
class BestGeneticEventListener
    implements GeneticEventListener {
  private GPGenotype m_other;

  public BestGeneticEventListener(GPGenotype a_other) {
    m_other = a_other;
  }

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
    IGPProgram best = genotype.getAllTimeBest();
    IGPProgram fittest = genotype.getFittestProgram();
    if (fittest != null) {
      // Inject fittest program into opponent's population.
      // --------------------------------------------------
      m_other.addFittestProgram(fittest);
      double bestFitness = fittest.getFitnessValue();
      if (bestFitness < 0.5) {
        genotype.outputSolution(best);
        System.exit(0);
      }
    }
  }
};
class MyGeneticEventListener
    implements GeneticEventListener {
  private Logger LOGGER;
  public MyGeneticEventListener(Logger LOGGER) {
    this.LOGGER = LOGGER;
  }

  public void geneticEventFired(GeneticEvent a_firedEvent) {
    GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
    int evno = genotype.getGPConfiguration().getGenerationNr();
    double freeMem = SystemKit.getFreeMemoryMB();
    if (evno % 100 == 0) {
      IGPProgram best = genotype.getAllTimeBest();
      double allBestFitness = FitnessFunction.NO_FITNESS_VALUE;
      if (best != null) {
        allBestFitness = best.
            getFitnessValue();
      }
      LOGGER.info("Evolving generation " + evno
                         + ", all-time-best fitness: " +
                         allBestFitness
                         + ", memory free: "
                         + NumberKit.niceDecimalNumber(freeMem, 2) + " MB");
      IGPProgram best1 = genotype.getFittestProgram();
      LOGGER.info("  Best in current generation: " +
                         best1.getFitnessValue());
      LOGGER.info("    " + best1.toStringNorm(0));
    }
    if (evno > 50000) {
      System.exit(0);
    }
    else {
      // Collect garbage if memory low.
      // ------------------------------
      if (freeMem < 50) {
        System.gc();
      }
    }
  }
}
class Coevolution {
  private GPConfiguration m_conf;

  private GPGenotype m_gp1;

  private GPGenotype m_gp2;

  public Coevolution(GPConfiguration a_conf, GPGenotype a_gp1, GPGenotype a_gp2) {
    m_conf = a_conf;
    m_gp1 = a_gp1;
    m_gp2 = a_gp2;
  }

  public void start() {
    try {
      do {
        m_gp1.evolve();
        Thread.currentThread().sleep(5);
        m_gp2.evolve();
        Thread.currentThread().sleep(5);
        m_gp1.calcFitness();
        Thread.currentThread().sleep(5);
        m_gp2.calcFitness();
        Thread.currentThread().sleep(20);
      } while (true);
    } catch (InterruptedException iex) {
      ; //this is OK and means: end of evolution
      iex.printStackTrace();
    }
  }
}
/**@todo avoid some commands to appear consecutively, e.g. loop, the negator or inc/dec*/
/**@todo avoid loop to appear more than twice in one prog.chrom. */

/**todo firstmove setzen*/
/**@todo punish sequence of inc's*/
/**@todo crossover: care that no sequence of inc's results*/
/**@todo island model, mit einer Haupt-Insel (Konsolidierung)*/
