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

import junit.framework.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.*;

/**
 * Tests the GPProgram class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgramTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GPProgramTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testExecute_0()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf, 3);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50, prog);
    pc1.getFunctions()[0] = CMD_SUB_V_V;
    pc1.getFunctions()[1] = new StoreTerminal(m_gpconf, "mem0", //a
        CommandGene.IntegerClass);
    pc1.getFunctions()[2] = CMD_CONST1;
    pc1.getFunctions()[3] = new StoreTerminal(m_gpconf, "mem1", //b
        CommandGene.IntegerClass);
    pc1.getFunctions()[4] = CMD_CONST1;
//    pc1.getFunctions()[5] = new StoreTerminal(m_gpconf, "mem2",//x
//                                              CommandGene.IntegerClass);
//    pc1.getFunctions()[6] = CMD_CONST0;
    pc1.redepth();
    assertEquals(2, pc1.getDepth(0));
    prog.setChromosome(0, pc1);
    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50, prog);
    Variable vx;
    CommandGene[] funcSet2 = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FOR, //1
        CMD_NOP, //2
        vx = Variable.create(m_gpconf, "X", CommandGene.IntegerClass), //3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"), //5
        new TransferMemory(m_gpconf, "mem2", "mem1"), //6
        new TransferMemory(m_gpconf, "mem1", "mem0"), //7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem0"), //8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem1"), //9
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {3, 0, 5, 8, 9, 7, 6});
    pc2.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet2, CMD_FOR, 0, true,
                       -1, false);
    pc2.redepth();
    prog.setChromosome(1, pc2);
    ProgramChromosome pc3 = new ProgramChromosome(m_gpconf, 50, prog);
    pc3.getFunctions()[0] = new ReadTerminal(m_gpconf, CommandGene.IntegerClass,
        "mem2");
    pc3.redepth();
    prog.setChromosome(2, pc3);
    Object[] noargs = new Object[0];
    prog.execute_void(0, noargs);
    vx.set(new Integer(2));
    prog.execute_void(1, noargs);
    int result = prog.execute_int(2, noargs);
    assertEquals(3, result);
    // 3
    prog.execute_void(0, noargs);
    vx.set(new Integer(3));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(5, result);
    // 4
    prog.execute_void(0, noargs);
    vx.set(new Integer(4));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(8, result);
    // 7
    prog.execute_void(0, noargs);
    vx.set(new Integer(7));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(34, result);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    prog.setChromosome(0, pc);
    GPProgram prog2 = (GPProgram) doSerialize(prog);
    assertEquals(prog, prog2);
  }
}
