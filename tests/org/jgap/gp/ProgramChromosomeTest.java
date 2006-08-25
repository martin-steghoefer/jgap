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
 * Tests the ProgramChromosome class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosomeTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ProgramChromosomeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * Produce a valid program. Random numbers preset to optimum (= hit at first
   * number returned by generator).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_I, //0
        CMD_FOR, //1
        CMD_NOP, //2
        CMD_ADD, //3
        CMD_CONST2, //4
        CMD_CONST3, //5
        CMD_CONST4, //6
    };
    rn.setNextIntSequence(new int[] {1, 4, 2, 5});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, funcSet, CMD_SUB_V_I, 0, true);
    pc.redepth();
    assertSame(CMD_SUB_V_I, pc.getNode(0));
    assertSame(CMD_FOR, pc.getNode(1));
    assertSame(CMD_CONST2, pc.getNode(2));
    assertSame(CMD_NOP, pc.getNode(3));
    assertSame(CMD_CONST3, pc.getNode(4));
  }

  /**
   * Produce a valid program. Random numbers preset to sub-optimum (multiple
   * requests to generator necessary).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_1()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_I, //0
        CMD_FOR, //1
        CMD_NOP, //2
        CMD_ADD, //3
        CMD_CONST2, //4
        CMD_CONST3, //5
        CMD_CONST4, //6
    };
    rn.setNextIntSequence(new int[] {1, 2, 6, 2, 2, 5});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, funcSet, CMD_SUB_V_I, 0, true);
    pc.redepth();
    assertSame(CMD_SUB_V_I, pc.getNode(0));
    assertSame(CMD_FOR, pc.getNode(1));
    assertSame(CMD_CONST4, pc.getNode(2));
    assertSame(CMD_NOP, pc.getNode(3));
    assertSame(CMD_CONST3, pc.getNode(4));
  }

  /**
   * Produce a valid program that is similar to computing Fibonacci.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_2()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FORX, //1
        CMD_NOP, //2
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass),//3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"),//5
        new TransferMemory(m_gpconf, "mem2", "mem1"),//6
        new TransferMemory(m_gpconf, "mem1", "mem0"),//7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem0"),//8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem1"),//9
    };
    rn.setNextIntSequence(new int[] {0, 5, 8, 9, 6, 7});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, funcSet, CMD_FORX, 0, true);
    pc.redepth();
    assertSame(CMD_FORX, pc.getNode(0));
    assertSame(CMD_SUB_V_V_V, pc.getNode(1));
    assertEquals(AddAndStore.class, pc.getNode(2).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(3).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(4).getClass());
    assertEquals(TransferMemory.class, pc.getNode(5).getClass());
    assertEquals(TransferMemory.class, pc.getNode(6).getClass());
  }

  /**
   * Produce a valid program that is similar to computing Fibonacci.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_3()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FOR, //1
        CMD_NOP, //2
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass),//3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"),//5
        new TransferMemory(m_gpconf, "mem2", "mem1"),//6
        new TransferMemory(m_gpconf, "mem1", "mem0"),//7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem0"),//8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass,"mem1"),//9
    };
    rn.setNextIntSequence(new int[] {3, 0, 5, 8, 9, 6, 7});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, funcSet, CMD_FOR, 0, true);
    pc.redepth();
    assertEquals(3, pc.getDepth(0));
    assertSame(CMD_FOR, pc.getNode(0));
    assertEquals(Variable.class, pc.getNode(1).getClass());
    assertSame(CMD_SUB_V_V_V, pc.getNode(2));
    assertEquals(AddAndStore.class, pc.getNode(3).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(4).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(5).getClass());
    assertEquals(TransferMemory.class, pc.getNode(6).getClass());
    assertEquals(TransferMemory.class, pc.getNode(7).getClass());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals("X ", pc.toString2(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_1()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Increment(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("INC(X )", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_2()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("X  + Y ", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_3()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("X  % Y ", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_4()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(1);
    assertEquals("X ", s);
    s = pc.toString2(2);
    assertEquals("Y ", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_5()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Subtract(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Variable(conf, "Z", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("(X  - Y ) % Z ", s);
    s = pc.toString2(1);
    assertEquals("(X  - Y )", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_6()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Multiply(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Push(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(1);
    assertEquals("(push X )", s);
    s = pc.toString2(0);
    assertEquals("(push X ) * X ", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_7()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0,
               new SubProgram(conf,
                                     new Class[] {CommandGene.IntegerClass,
                                     CommandGene.IntegerClass,
                                     CommandGene.IntegerClass}));
    pc.setGene(1, new Multiply(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Push(conf, CommandGene.IntegerClass));
    pc.setGene(5, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(6, new Constant(conf, CommandGene.IntegerClass, new Integer(7)));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("sub[(X  * Y ) --> (push X ) --> 7 ]", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString2_8()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0,
               new SubProgram(conf,
                                     new Class[] {CommandGene.IntegerClass,
                                     CommandGene.IntegerClass}));
    pc.setGene(1, new Multiply(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Push(conf, CommandGene.IntegerClass));
    pc.setGene(5, new Constant(conf, CommandGene.IntegerClass, new Integer(9)));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("sub[(X  * Y ) --> (push 9 )]", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString(0);
    assertEquals("+ ( X Y )", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString_1()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Subtract(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString(1);
    assertEquals("X ", s);
    s = pc.toString(2);
    assertEquals("Y ", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testRedepth_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    try {
      pc.redepth();
      fail();
    }
    catch (IllegalStateException ise) {
      ; //this i expected
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetDepth_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass)); //Node 1
    pc.setGene(2, new Variable(conf, "Z", CommandGene.IntegerClass)); // Node 2
    pc.redepth();
    assertEquals(1, pc.getDepth(0)); //1 = one level below node 0
    assertEquals(0, pc.getDepth(1));
    assertEquals(0, pc.getDepth(2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetDepth_1()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new IfElse(conf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Z", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals(1, pc.getDepth(0)); //1 = one level below node 0
    assertEquals(0, pc.getDepth(1));
    assertEquals(0, pc.getDepth(2));
    assertEquals(0, pc.getDepth(3));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetDepth_2()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new IfElse(conf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new Add(conf, CommandGene.IntegerClass)); // Node 2
    pc.setGene(3, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(4, new Constant(conf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(5, new Variable(conf, "Z", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals(2, pc.getDepth(0)); //2 = one level below node 0
    assertEquals(0, pc.getDepth(1));
    assertEquals(1, pc.getDepth(2)); //1 = one level below node 2
    assertEquals(0, pc.getDepth(3));
    assertEquals(0, pc.getDepth(4));
    assertEquals(0, pc.getDepth(5));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    ProgramChromosome pc2 = (ProgramChromosome) doSerialize(pc);
    assertSame(pc, pc2);
    /**@todo implement equals and compareTo to make this test pass*/
  }
}
