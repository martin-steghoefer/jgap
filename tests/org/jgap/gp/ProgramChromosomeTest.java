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
  private final static String CVS_REVISION = "$Revision: 1.2 $";

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
}
