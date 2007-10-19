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
import org.jgap.gp.*;

/**
 * Tests the GPPopulation class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPPopulationTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GPPopulationTest.class);
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
  public void testConstruct_0()
      throws Exception {
    GPPopulation gppop = new GPPopulation(m_gpconf, 10);
    assertEquals(10, gppop.getPopSize());
    assertEquals(10, gppop.size());
  }

  /**
   * Create population with no nodes available.
   *
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
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
    }
    };
    try {
      gppop.create(types, argTypes, nodeSets, new int[] {1}, new int[] {1}, 1,
                   true, 10, new boolean[] {true}, 0);
      fail();
    } catch (IllegalStateException iex) {
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
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
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
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
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
   *
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
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        new Add(m_gpconf, CommandGene.IntegerClass),
        new Subtract(m_gpconf, CommandGene.IntegerClass),
        new Push(m_gpconf, CommandGene.IntegerClass),
    }
    };
    m_gpconf.setRandomGenerator(new StockRandomGenerator());
    try {
      gppop.create(types, argTypes, nodeSets, new int[] {1}, new int[] {1}, 3,
                   true, 10, new boolean[] {true}, 0);
      fail();
    } catch (IllegalStateException iex) {
      ; //this is OK
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
    GPPopulation pop = new GPPopulation(m_gpconf, 2);
    GPProgram prog = new GPProgram(m_gpconf, 2);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    // Serialize population to a file.
    // -------------------------------
    GPPopulation pop2 = (GPPopulation) doSerialize(pop);
    assertEquals(pop, pop2);
  }

  /**
   * Sorting with population having null programs, which is allowed.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testSort_0()
      throws Exception {
    GPPopulation pop = new GPPopulation(m_gpconf, 4);
    GPProgram prog = new GPProgram(m_gpconf, 2);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    pop.sort(new GPProgramFitnessComparator());
  }

  public void testSetPrototype_0()
      throws Exception {
    /**@todo assert that prototype is only set automatically if none is preset*/
  }
}
