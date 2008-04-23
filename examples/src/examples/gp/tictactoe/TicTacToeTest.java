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

import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import junit.framework.*;

public class TicTacToeTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(TicTacToeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testConstruct_0()
      throws Exception {
    // Player 1
    GPConfiguration config = new GPConfiguration();
    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config.setMaxInitDepth(8);
    config.setPopulationSize(40);
    config.setStrictProgramCreation(false);
    config.setProgramCreationMaxTries(5);
    config.setMaxCrossoverDepth(12);
//    INodeValidator validator = new GameNodeValidator();
    TicTacToeMain game1 = new TicTacToeMain(config);
    GPGenotype player1 = game1.create(config,1,null,2);
    // Player 2
    GPConfiguration config2 = new GPConfiguration();
    config2.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config2.setMaxInitDepth(8);
    config2.setPopulationSize(40);
    config2.setStrictProgramCreation(false);
    config2.setProgramCreationMaxTries(5);
    config2.setMaxCrossoverDepth(12);
    TicTacToeMain game2 = new TicTacToeMain(config2);
    GPGenotype player2 = game2.create(config2,2,null,1);
    // Preset program of player 1
  }
}
/**
 * for(int x=0;x<2;x++) {
 *   mem0 = 0;
 *   for(int y=0;y<2;y++) {
 *     IfColor(2) {
 *       mem0+1;
 *     }
 *     mem0 = ReadBoard(x,y);
 *
 *   }
 * }
 */
