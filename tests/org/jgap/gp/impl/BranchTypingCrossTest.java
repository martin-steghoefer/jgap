/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.gp.*;
import org.jgap.gp.function.*;

import junit.framework.*;

/**
 * Tests the BranchTypingCross class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class BranchTypingCrossTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BranchTypingCrossTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * See if crossover chooses a correct function for a function selected
   * priorly. Simple case: De facto replace subtrees (from "FOR" on, see above
   * code). Cross over at functions in this test case.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testOperate_0()
      throws Exception {
    BranchTypingCross btc = new BranchTypingCross(m_gpconf);
    Class[] types = new Class[]{Add.class};//needed for init. only
    // First program.
    // --------------
    GPProgram prog1 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50, prog1);
    pc1.setGene(0, CMD_SUB_V_I);
    pc1.setGene(1, CMD_FOR);
    pc1.setGene(2, CMD_CONST2);
    pc1.setGene(3, CMD_NOP);
    pc1.setGene(4, CMD_CONST3);
    pc1.redepth();
    prog1.setChromosome(0, pc1);
    prog1.setTypes(types);
    // Second program.
    // ---------------
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50, prog2);
    prog2.setTypes(types);
    pc2.setGene(0, CMD_SUB_V_I);
    pc2.setGene(1, CMD_FOR);
    pc2.setGene(2, CMD_ADD);
    pc2.setGene(3, CMD_CONST0);
    pc2.setGene(4, CMD_CONST1);
    pc2.setGene(5, CMD_NOP);
    pc2.setGene(6, CMD_CONST3);
    pc2.redepth();
    prog2.setChromosome(0, pc2);
    // Do crossing over.
    // -----------------
    rn.setNextIntSequence(new int[] {
                          1, // a node in pc1
                          1, // index of function to choose (p0 = CMD_FOR)
                          1 // index of function to choose (p1 = CMD_ADD)
    });
    rn.setNextFloatSequence(new float[] {
                            0.5f, // Choose a function when crossing over
                            0.5f // Choose a function when crossing over
    });
    IGPProgram[] result = btc.operate(prog1, prog2);
    assertEquals(2, result.length);
    IGPProgram p1 = result[0];
    ProgramChromosome chrom1 = p1.getChromosome(0);
    IGPProgram p2 = result[1];
    ProgramChromosome chrom2 = p2.getChromosome(0);
    assertSame(CMD_SUB_V_I, chrom1.getGene(0));
    // Next, FOR is assumed because it's the only function with return type void
    // in the above compilation for pc2
    assertSame(CMD_FOR, chrom1.getGene(1));
    assertSame(CMD_ADD, chrom1.getGene(2));
    assertSame(CMD_CONST0, chrom1.getGene(3));
    assertSame(CMD_CONST1, chrom1.getGene(4));
    assertSame(CMD_NOP, chrom1.getGene(5));
    assertSame(CMD_CONST3, chrom1.getGene(6));

    assertSame(CMD_SUB_V_I, chrom2.getGene(0));
    assertSame(CMD_FOR, chrom2.getGene(1));
    assertSame(CMD_CONST2, chrom2.getGene(2));
    assertSame(CMD_NOP, chrom2.getGene(3));
    assertSame(CMD_CONST3, chrom2.getGene(4));
  }

  /**
   * Cross over a function with a terminal.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testOperate_1()
      throws Exception {
    BranchTypingCross btc = new BranchTypingCross(m_gpconf);
    Class[] types = new Class[]{Add.class};//needed for init. only
    // First program.
    // --------------
    GPProgram prog1 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50, prog1);
    pc1.setGene(0, CMD_SUB_V_I);
    pc1.setGene(1, CMD_FOR);
    pc1.setGene(2, CMD_CONST2);
    pc1.setGene(3, CMD_NOP);
    pc1.setGene(4, CMD_CONST3);
    pc1.redepth();
    prog1.setChromosome(0, pc1);
    prog1.setTypes(types);
    // Second program.
    // ---------------
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50, prog2);
    pc2.setGene(0, CMD_SUB_V_I);
    pc2.setGene(1, CMD_FOR);
    pc2.setGene(2, CMD_ADD);
    pc2.setGene(3, CMD_CONST0);
    pc2.setGene(4, CMD_CONST1);
    pc2.setGene(5, CMD_NOP);
    pc2.setGene(6, CMD_CONST3);
    pc2.redepth();
    prog2.setChromosome(0, pc2);
    prog2.setTypes(types);
    // Do crossing over.
    // -----------------
    rn.setNextIntSequence(new int[] {
                          1, // a node in pc1
                          1, // index of function to choose (p0 = CMD_FOR)
                          2 // index of terminal to choose (p1 = NOP)
    });
    rn.setNextFloatSequence(new float[] {
                            0.50f, // Choose a function when crossing over
                            0.95f // Choose a terminal when crossing over
    });
    IGPProgram[] result = btc.operate(prog1, prog2);
    assertEquals(2, result.length);
    IGPProgram p1 = result[0];
    ProgramChromosome chrom1 = p1.getChromosome(0);
    IGPProgram p2 = result[1];
    ProgramChromosome chrom2 = p2.getChromosome(0);
    assertSame(CMD_SUB_V_I, chrom1.getGene(0));
    // NOP from other chromosome instead of FOR, CONST2, NOP (the for-loop).
    assertSame(CMD_NOP, chrom1.getGene(1));
    assertSame(CMD_CONST3, chrom1.getGene(2));

    assertSame(CMD_SUB_V_I, chrom2.getGene(0));
    assertSame(CMD_FOR, chrom2.getGene(1));
    assertSame(CMD_ADD, chrom2.getGene(2));
    assertSame(CMD_CONST0, chrom2.getGene(3));
    assertSame(CMD_CONST1, chrom2.getGene(4));
    // FOR, CONST2, NOP (the for-loop) from other chromosome.
    assertSame(CMD_FOR, chrom2.getGene(5));
    assertSame(CMD_CONST2, chrom2.getGene(6));
    assertSame(CMD_NOP, chrom2.getGene(7));
    assertSame(CMD_CONST3, chrom2.getGene(8));
  }

  /**
   * Cross over a terminal with a terminal.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testOperate_2()
      throws Exception {
    BranchTypingCross btc = new BranchTypingCross(m_gpconf);
    Class[] types = new Class[]{Add.class};//needed for init. only
    // First program.
    // --------------
    GPProgram prog1 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50, prog1);
    pc1.setGene(0, CMD_FOR);
    pc1.setGene(1, CMD_CONST2);
    pc1.setGene(2, CMD_NOP);
    pc1.redepth();
    prog1.setChromosome(0, pc1);
    prog1.setTypes(types);
    // Second program.
    // ---------------
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50, prog2);
    pc2.setGene(0, CMD_SUB_V_I);
    pc2.setGene(1, CMD_FOR);
    pc2.setGene(2, CMD_ADD);
    pc2.setGene(3, CMD_CONST0);
    pc2.setGene(4, CMD_CONST1);
    pc2.setGene(5, CMD_NOP);
    pc2.setGene(6, CMD_CONST3);
    pc2.redepth();
    prog2.setChromosome(0, pc2);
    prog2.setTypes(types);
    // Do crossing over.
    // -----------------
    rn.setNextIntSequence(new int[] {
                          1, // a node in pc1
                          0, // index of terminal to choose (p0 => CMD_CONST2)
                          2 // index of terminal to choose (p1 => CMD_CONST3)
    });
    rn.setNextFloatSequence(new float[] {
                            0.95f, // Choose a terminal when crossing over
                            0.95f // Choose a terminal when crossing over
    });
    IGPProgram[] result = btc.operate(prog1, prog2);
    assertEquals(2, result.length);
    IGPProgram p1 = result[0];
    ProgramChromosome chrom1 = p1.getChromosome(0);
    IGPProgram p2 = result[1];
    ProgramChromosome chrom2 = p2.getChromosome(0);
    assertSame(CMD_FOR, chrom1.getGene(0));
    assertSame(CMD_CONST3, chrom1.getGene(1));
    assertSame(CMD_NOP, chrom1.getGene(2));

    assertSame(CMD_SUB_V_I, chrom2.getGene(0));
    assertSame(CMD_FOR, chrom2.getGene(1));
    assertSame(CMD_ADD, chrom2.getGene(2));
    assertSame(CMD_CONST0, chrom2.getGene(3));
    assertSame(CMD_CONST1, chrom2.getGene(4));
    assertSame(CMD_NOP, chrom2.getGene(5));
    assertSame(CMD_CONST2, chrom2.getGene(6));
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
    BranchTypingCross btc2 = (BranchTypingCross) doSerialize(btc);
    assertEquals(btc, btc2);
  }
}
