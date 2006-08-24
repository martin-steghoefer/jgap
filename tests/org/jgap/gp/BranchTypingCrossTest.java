/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import junit.framework.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Tests the BranchTypingCross class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class BranchTypingCrossTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPConfiguration m_gpconf;

  private RandomGeneratorForTest rn;

  private Constant CMD_CONST0, CMD_CONST1, CMD_CONST2, CMD_CONST3, CMD_CONST4;

  private NOP CMD_NOP;

  public static Test suite() {
    TestSuite suite = new TestSuite(BranchTypingCrossTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    try {
      GPConfiguration.reset();
      m_gpconf = new GPConfiguration();
      m_gpconf.setPopulationSize(10);
      rn = new RandomGeneratorForTest(1);
      m_gpconf.setRandomGenerator(rn);
      CMD_CONST0 = new Constant(m_gpconf, CommandGene.IntegerClass,
                                new Integer(0));
      CMD_CONST1 = new Constant(m_gpconf, CommandGene.IntegerClass,
                                new Integer(1));
      CMD_CONST2 = new Constant(m_gpconf, CommandGene.IntegerClass,
                                new Integer(2));
      CMD_CONST3 = new Constant(m_gpconf, CommandGene.IntegerClass,
                                new Integer(3));
      CMD_CONST4 = new Constant(m_gpconf, CommandGene.IntegerClass,
                                new Integer(4));
      CMD_NOP = new NOP(m_gpconf);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    BranchTypingCross btc = new BranchTypingCross(m_gpconf);
    // First program.
    // --------------
    GPProgram prog1 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50);
    prog1.setChromosome(0, pc1);
    // Second program.
    // ---------------
    GPProgram prog2 = new GPProgram(m_gpconf, 2);
    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50);
    prog2.setChromosome(0, pc2);
    // Do crossing over.
    // -----------------
    btc.operate(prog1, prog2);
  }
}
