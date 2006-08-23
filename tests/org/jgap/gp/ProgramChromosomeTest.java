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

/**
 * Tests the ProgramChromosome class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosomeTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private GPConfiguration m_gpconf;

  public static Test suite() {
    TestSuite suite = new TestSuite(ProgramChromosomeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    try {
      GPConfiguration.reset();
      m_gpconf = new GPConfiguration();
      m_gpconf.setPopulationSize(10);
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
    pc.setGene(0, new IncrementCommand(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("INC(1, X )", s);
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new ModCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new ModCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new ModCommand(conf, CommandGene.IntegerClass));
    pc.setGene(1, new SubtractCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new MultiplyCommand(conf, CommandGene.IntegerClass));
    pc.setGene(1, new PushCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new SubProgramCommand(conf, new Class[]{CommandGene.IntegerClass}, 3));
    pc.setGene(1, new MultiplyCommand(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new PushCommand(conf, CommandGene.IntegerClass));
    pc.setGene(5, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(6, new Constant(conf, CommandGene.IntegerClass, new Integer(7)));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("sub[(X  * Y ) --> (push X ) --> const(7) ]", s);
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
    pc.setGene(0, new SubProgramCommand(conf, new Class[]{CommandGene.IntegerClass}, 2));
    pc.setGene(1, new MultiplyCommand(conf, CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(4, new PushCommand(conf, CommandGene.IntegerClass));
    pc.setGene(5, new Constant(conf, CommandGene.IntegerClass, new Integer(7)));
    pc.redepth();
    String s = pc.toString2(0);
    assertEquals("sub[(X  * Y ) --> (push const(7) )]", s);
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));//Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass));//Node 1
    pc.setGene(2, new Variable(conf, "Z", CommandGene.IntegerClass));// Node 2
    pc.redepth();
    assertEquals(1, pc.getDepth(0));//1 = one level below node 0
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
    pc.setGene(0, new IfElseCommand(conf, CommandGene.IntegerClass));//Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Z", CommandGene.IntegerClass));
    pc.setGene(3, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals(1, pc.getDepth(0));//1 = one level below node 0
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
    pc.setGene(0, new IfElseCommand(conf, CommandGene.IntegerClass));//Node 0
    pc.setGene(1, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.setGene(2, new AddCommand(conf, CommandGene.IntegerClass));// Node 2
    pc.setGene(3, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(4, new Constant(conf, CommandGene.IntegerClass, new Integer(3)));
    pc.setGene(5, new Variable(conf, "Z", CommandGene.IntegerClass));
    pc.redepth();
    assertEquals(2, pc.getDepth(0));//2 = one level below node 0
    assertEquals(0, pc.getDepth(1));
    assertEquals(1, pc.getDepth(2));//1 = one level below node 2
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
    pc.setGene(0, new AddCommand(conf, CommandGene.IntegerClass));
    pc.setGene(1, new Variable(conf, "X", CommandGene.IntegerClass));
    pc.setGene(2, new Variable(conf, "Y", CommandGene.IntegerClass));
    pc.redepth();
    ProgramChromosome pc2 = (ProgramChromosome)doSerialize(pc);
    assertSame(pc, pc2);
    /**@todo implement equals and compareTo to make this test pass*/
  }
}
