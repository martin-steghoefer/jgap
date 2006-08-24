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
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;

/**
 * Tests the GPProgram class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgramTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPConfiguration m_gpconf;

  private RandomGeneratorForTest rn;

  private Constant CMD_CONST0, CMD_CONST1, CMD_CONST2, CMD_CONST3, CMD_CONST4;

  private Add CMD_ADD;

  private ForLoop CMD_FOR;
  private ForXLoop CMD_FORX;

  private SubProgram CMD_SUB_V_I;
  private SubProgram CMD_SUB_V_V_V;

  private NOP CMD_NOP;

  public static Test suite() {
    TestSuite suite = new TestSuite(GPProgramTest.class);
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
      CMD_FOR = new ForLoop(m_gpconf, CommandGene.IntegerClass);
      CMD_FORX = new ForXLoop(m_gpconf, CommandGene.IntegerClass);
      CMD_SUB_V_I = new SubProgram(m_gpconf,
                                   new Class[] {CommandGene.VoidClass,
                                   CommandGene.IntegerClass});
      CMD_SUB_V_V_V = new SubProgram(m_gpconf,
                                   new Class[] {CommandGene.VoidClass,
                                   CommandGene.VoidClass,
                                   CommandGene.VoidClass});
      CMD_ADD = new Add(m_gpconf, CommandGene.IntegerClass);
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
  public void testExecute_0()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf,3);
    ProgramChromosome pc1 = new ProgramChromosome(m_gpconf, 50);
    pc1.getFunctions()[0] = CMD_SUB_V_V_V;
    pc1.getFunctions()[1] = new StoreTerminal(m_gpconf, "mem0",//a
                                              CommandGene.IntegerClass);
    pc1.getFunctions()[2] = CMD_CONST1;
    pc1.getFunctions()[3] = new StoreTerminal(m_gpconf, "mem1",//b
                                              CommandGene.IntegerClass);
    pc1.getFunctions()[4] = CMD_CONST1;
    pc1.getFunctions()[5] = new StoreTerminal(m_gpconf, "mem2",//x
                                              CommandGene.IntegerClass);
    pc1.getFunctions()[6] = CMD_CONST0;
    pc1.redepth();
    prog.setChromosome(0, pc1);

    ProgramChromosome pc2 = new ProgramChromosome(m_gpconf, 50);
    Variable vx;
    CommandGene[] funcSet2 = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FOR, //1
        CMD_NOP, //2
        vx = Variable.create(m_gpconf, "X", CommandGene.IntegerClass),//3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"),//5
        new TransferMemory(m_gpconf, "mem2", "mem1"),//6
        new TransferMemory(m_gpconf, "mem1", "mem0"),//7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem0"),//8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem1"),//9
    };
    rn.setNextIntSequence(new int[] {3, 0, 5, 8, 9, 7, 6});
    pc2.growOrFullNode(0, 5, CommandGene.IntegerClass, funcSet2, CMD_FOR, 0, true);
    pc2.redepth();
    prog.setChromosome(1, pc2);

    ProgramChromosome pc3 = new ProgramChromosome(m_gpconf, 50);
    pc3.getFunctions()[0] = new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem2");
    pc3.redepth();
    prog.setChromosome(2, pc3);
    Object[] noargs = new Object[0];

    prog.execute_void(0, noargs);
    vx.set(new Integer(2));
    prog.execute_void(1, noargs);
    int result = prog.execute_int(2, noargs);
    assertEquals(2, result);
    // 3
    prog.execute_void(0, noargs);
    vx.set(new Integer(3));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(3, result);
    // 4
    prog.execute_void(0, noargs);
    vx.set(new Integer(4));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(5, result);
    // 7
    prog.execute_void(0, noargs);
    vx.set(new Integer(7));
    prog.execute_void(1, noargs);
    result = prog.execute_int(2, noargs);
    assertEquals(21, result);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf,1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    prog.setChromosome(0, pc);
    GPProgram prog2 = (GPProgram) doSerialize(prog);
    assertSame(prog, prog2);
    /**@todo implement equals and compareTo to make this test pass*/
  }
}
