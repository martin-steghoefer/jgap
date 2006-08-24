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
 * Tests the GPPopulation class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPPopulationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private GPConfiguration m_gpconf;

  private RandomGeneratorForTest rn;

  public static Test suite() {
    TestSuite suite = new TestSuite(GPPopulationTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    try {
      GPConfiguration.reset();
      m_gpconf = new GPConfiguration();
      rn = new RandomGeneratorForTest(3);
      m_gpconf.setRandomGenerator(rn);
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
  public void testConstruct_0()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    assertEquals(10, gppop.getPopSize());
    assertEquals(0, gppop.size());
  }

  /**
   * Create with no nodes available.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testCreate_0()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
    }
    };
    try {
      gppop.create(types, argTypes, nodeSets, new int[] {1}, 1, true, new boolean[] {true});
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testCreate_1()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        new Add(m_gpconf, CommandGene.IntegerClass),
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass),
        new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(1)),
    }
    };
    rn.setNextInt(2); //Constant
    /**@todo adapt*/
//    ProgramChromosome pc = gppop.create(types, argTypes, nodeSets, 1, true);
//    assertEquals(0, pc.getDepth(0));
//    assertEquals(1, pc.size());
//    assertEquals(Constant.class, pc.getGene(0).getClass());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testCreate_2()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        new Add(m_gpconf, CommandGene.IntegerClass),
        Variable.create(m_gpconf, "X", CommandGene.IntegerClass),
        new Constant(m_gpconf, CommandGene.IntegerClass, new Integer(1)),
    }
    };
    rn.setNextIntSequence(new int[] {0, 2, 1}); //AddCommand, Constant, Variable
    /**@todo adapt*/

//    ProgramChromosome pc = gppop.create(types, argTypes, nodeSets, 3, true);
//    assertEquals(1, pc.getDepth(0)); //AddCommand
//    assertEquals(0, pc.getDepth(1)); //Constant
//    assertEquals(0, pc.getDepth(2)); //Variable
//    assertEquals(3, pc.size());
//    assertEquals(AddCommand.class, pc.getGene(0).getClass());
//    assertEquals(Constant.class, pc.getGene(1).getClass());
//    assertEquals(Variable.class, pc.getGene(2).getClass());
  }

  /**
   * Given nodeset doesn't allow to build a valid program.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testCreate_3()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        new Add(m_gpconf, CommandGene.IntegerClass),
        new Subtract(m_gpconf, CommandGene.IntegerClass),
        new Push(m_gpconf, CommandGene.IntegerClass),
    }
    };
    m_gpconf.setRandomGenerator(new StockRandomGenerator());
    try {
      gppop.create(types, argTypes, nodeSets, new int[] {1}, 3, true, new boolean[] {true});
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }
}
