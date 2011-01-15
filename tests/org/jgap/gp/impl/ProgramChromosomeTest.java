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
import org.jgap.gp.terminal.*;
import junit.framework.*;

/**
 * Tests the ProgramChromosome class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosomeTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ProgramChromosomeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public void testConstruct_0()
      throws Exception {
    try {
      ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
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
    IGPProgram ind = new GPProgram(m_gpconf, 5);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_I, //0
        CMD_FOR, //1
        CMD_NOP, //2
        CMD_ADD, //3
        CMD_CONST2, //4
        CMD_CONST3, //5
        CMD_CONST4, //6
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {0, 1, 4, 2, 5});
    pc.growOrFullNode(0, 3, CommandGene.IntegerClass, 0, funcSet, null,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_V_I.toString(), pc.getNode(0).toString());
    assertEquals(CMD_FOR.toString(), pc.getNode(1).toString());
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
    IGPProgram ind = new GPProgram(m_gpconf, 5);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_I, //0
        CMD_FOR, //1
        CMD_NOP, //2
        CMD_ADD, //3
        CMD_CONST2, //4
        CMD_CONST3, //5
        CMD_CONST4, //6
    };
    // The next sequences contain two numbers with "-1".
    // "-1" is because we use a UniqueRandomGenerator that
    // removes each invalid try to avoid duplicate tries.
    // ---------------------------------------------------
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {1, 2, 6 - 1, 2, 2, 5 - 1});
    rn.setNextFloatSequence(new float[] {0.1f});
    rn.setNextDouble(0.2d);
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, CMD_SUB_V_I,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_V_I.toString(), pc.getNode(0).toString());
    assertEquals(CMD_FOR.toString(), pc.getNode(1).toString());
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
    IGPProgram ind = new GPProgram(m_gpconf, 5);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FORX, //1
        CMD_NOP, //2
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass), //3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"), //5
        new TransferMemory(m_gpconf, "mem2", "mem1"), //6
        new TransferMemory(m_gpconf, "mem1", "mem0"), //7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem0"), //8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem1"), //9
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {0, 5, 8, 9, 6, 7});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, CMD_FORX, 0, true,
                      -1, false);
    pc.redepth();
    assertEquals(CMD_FORX.toString(), pc.getNode(0).toString());
    assertEquals(CMD_SUB_V_V_V, pc.getNode(1));
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
    IGPProgram ind = new GPProgram(m_gpconf, 5);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_V_V_V, //0
        CMD_FOR, //1
        CMD_NOP, //2
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass), //3
        new Increment(m_gpconf, CommandGene.IntegerClass), //4
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"), //5
        new TransferMemory(m_gpconf, "mem2", "mem1"), //6
        new TransferMemory(m_gpconf, "mem1", "mem0"), //7
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem0"), //8
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem1"), //9
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {3, 0, 5, 8, 9, 6, 7});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, CMD_FOR, 0, true,
                      -1, false);
    pc.redepth();
    assertEquals(CMD_FOR, pc.getNode(0));
    assertEquals(3, pc.getDepth(0));
    assertEquals(Variable.class, pc.getNode(1).getClass());
    assertEquals(CMD_SUB_V_V_V, pc.getNode(2));
    assertEquals(AddAndStore.class, pc.getNode(3).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(4).getClass());
    assertEquals(ReadTerminal.class, pc.getNode(5).getClass());
    assertEquals(TransferMemory.class, pc.getNode(6).getClass());
    assertEquals(TransferMemory.class, pc.getNode(7).getClass());
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
  public void testGrowNode_4()
      throws Exception {
    IGPProgram ind = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_I_I, //0
        CMD_TERM0, //1
        CMD_TERM1, //2
        CMD_TERM2, //3
    };
    rn.setNextIntSequence(new int[] {1, 2, 3});
    rn.setNextFloatSequence(new float[] {0.2f});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, CMD_SUB_I_I,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_I_I, pc.getNode(0));
    assertEquals(CMD_TERM0, pc.getNode(1));
    assertEquals(CMD_TERM1, pc.getNode(2));
  }

  /**
   * Mutate arity of sub program: Increase arity from 2 to 3.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_4_2()
      throws Exception {
    IGPProgram ind = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_I_IM, //0
        CMD_TERM0, //1
        CMD_TERM1, //2
        CMD_TERM2, //3
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {0, //CMD_SUB_I_IM
                          2, //CMD_SUB_I_IM.applyMutation (2 + 1 = arity 3)
                          1, //CMD_TERM0
                          2, //CMD_TERM1
                          3 //CMD_TERM2
    });
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextFloatSequence(new float[] {0.1f});
    pc.setFunctions(funcSet);
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, null,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_I_I_I, pc.getNode(0));
    assertEquals(3, pc.getNode(0).getArity(null));
    assertEquals(CMD_TERM0, pc.getNode(1));
    assertEquals(CMD_TERM1, pc.getNode(2));
    assertEquals(CMD_TERM2, pc.getNode(3));
  }

  /**
   * Mutate arity of sub program: Reduce arity from 3 to 2.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGrowNode_4_3()
      throws Exception {
    IGPProgram ind = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_I_IM, //0
        CMD_TERM0, //1
        CMD_TERM1, //2
        CMD_TERM2, //3
    };
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {0, //CMD_SUB_I_IM
                          0, //CMD_SUB_I_IM.applyMutation (2 + 0 = arity 2)
                          1, //CMD_TERM0
                          3 //CMD_TERM2
    });
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextFloatSequence(new float[] {0.0f});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, null,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_I_I, pc.getNode(0));
    assertEquals(2, pc.getNode(0).getArity(null));
    assertEquals(CMD_TERM0, pc.getNode(1));
    assertEquals(CMD_TERM2, pc.getNode(2));
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
  public void testGrowNode_5()
      throws Exception {
    IGPProgram ind = new GPProgram(m_gpconf, 1);
    ProgramChromosome pc = new ProgramChromosome(m_gpconf, 50, ind);
    CommandGene[] funcSet = new CommandGene[] {
        CMD_SUB_I_I, //0
        CMD_SUB_I_I2, //1
        CMD_TERM0, //2
        CMD_TERM1, //3
        CMD_TERM2, //4
    };
    rn.setNextIntSequence(new int[] {1, 2, 3, 4});
    rn.setNextFloatSequence(new float[] {0.7f});
    pc.growOrFullNode(0, 5, CommandGene.IntegerClass, 0, funcSet, CMD_SUB_I_I,
                      0, true, -1, false);
    pc.redepth();
    assertEquals(CMD_SUB_I_I, pc.getNode(0));
    assertEquals(CMD_SUB_I_I2, pc.getNode(1));
    assertEquals(CMD_TERM0, pc.getNode(2));
    assertEquals(CMD_TERM1, pc.getNode(3));
    assertEquals(CMD_TERM2, pc.getNode(4));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_0()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals("X", pc.toStringNorm(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_1()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Increment(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("INC(X)", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_2()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("X + Y", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_3()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("X % Y", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_4()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(1);
    assertEquals("X", s);
    s = pc.toStringNorm(2);
    assertEquals("Y", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_5()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Modulo(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Subtract(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Variable(m_gpconf, "Z", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("(X - Y) % Z", s);
    s = pc.toStringNorm(1);
    assertEquals("(X - Y)", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_6()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Multiply(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Push(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(1);
    assertEquals("(push X)", s);
    s = pc.toStringNorm(0);
    assertEquals("(push X) * X", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_7()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0,
               new SubProgram(m_gpconf,
                              new Class[] {CommandGene.IntegerClass,
                              CommandGene.IntegerClass,
                              CommandGene.IntegerClass}));
    pc.setGene(1, new Multiply(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Push(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(5, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(6,
               new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(7)));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("sub[(X * Y) --> (push X) --> 7]", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToStringNorm_8()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0,
               new SubProgram(m_gpconf,
                              new Class[] {CommandGene.IntegerClass,
                              CommandGene.IntegerClass}));
    pc.setGene(1, new Multiply(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new Push(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(5,
               new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(9)));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("sub[(X * Y) --> (push 9)]", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void testToStringNorm_9()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Or(m_gpconf));
    pc.setGene(1, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(4, new GreaterThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(5, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(6, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(9)));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("(Equals(X, 3)) || (X > 9)", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void testToStringNorm_A()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Or(m_gpconf));
    pc.setGene(1, new Or(m_gpconf));
    pc.setGene(2, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(4, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(5, new GreaterThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(6, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(7, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(9)));
    pc.setGene(8, new LesserThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(9, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(10, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(1)));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("((Equals(X, 3)) || (X > 9)) || (X < 1)", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void testToStringNorm_B()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Or(m_gpconf));
    pc.setGene(1, new Or(m_gpconf));
    pc.setGene(2, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(3, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(4, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(5, new GreaterThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(6, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(7, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(9)));
    pc.setGene(8, new LesserThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(9, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(1)));
    pc.setGene(10, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("((Equals(3, X)) || (X > 9)) || (1 < X)", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void testToStringNorm_C()
      throws Exception {
    ProgramChromosome pc = new ProgramChromosome(m_gpconf);
    pc.setGene(0, new Or(m_gpconf));
    pc.setGene(1, new Or(m_gpconf));
    pc.setGene(2, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(3, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(4, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(5, new GreaterThan(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(6, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(7, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(9)));
    pc.setGene(8, new Or(m_gpconf));
    pc.setGene(9, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(10, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(1)));
    pc.setGene(11, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(12, new Equals(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(13, new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(4)));
    pc.setGene(14, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toStringNorm(0);
    assertEquals("((Equals(3, X)) || (X > 9)) || ((Equals(1, X)) || (Equals(4, X)))", s);
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
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
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
    pc.setGene(0, new Subtract(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
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
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    try {
      pc.redepth();
      fail();
    } catch (IllegalStateException ise) {
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
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(m_gpconf, "Y", CommandGene.IntegerClass)); //Node 1
    pc.setGene(2, new Variable(m_gpconf, "Z", CommandGene.IntegerClass)); // Node 2
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
    pc.setGene(0, new IfElse(m_gpconf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Z", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
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
    pc.setGene(0, new IfElse(m_gpconf, CommandGene.IntegerClass)); //Node 0
    pc.setGene(1, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new Add(m_gpconf, CommandGene.IntegerClass)); // Node 2
    pc.setGene(3, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(4,
               new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(5, new Variable(m_gpconf, "Z", CommandGene.IntegerClass));
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
    pc.setGene(0, new Add(m_gpconf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(m_gpconf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(m_gpconf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    ProgramChromosome pc2 = (ProgramChromosome) doSerialize(pc);
    assertEquals(pc, pc2);
  }
}
