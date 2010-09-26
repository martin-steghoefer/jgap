/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import junit.framework.*;

/**
 * Tests the CommandDynamicArity class and the dynamic arity concept.
 *
 * @author Klaus Meffert
 * @since 3.4
 */
public class CommandDynamicArityTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CommandDynamicArityTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testConstruct_0()
      throws Exception {
    assertNotNull(new CommandDynamicArityTest());
    assertNotNull(new CommandDynamicArityImpl(m_gpconf, 2, 1, 3));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testConstruct_1()
      throws Exception {
    try {
      new CommandDynamicArityImpl(m_gpconf, 2, 4, 5);
    } catch (RuntimeException rex) {
      ; //this is expected
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testConstruct_2()
      throws Exception {
    try {
      new CommandDynamicArityImpl(m_gpconf, 2, 0, 5);
    } catch (RuntimeException rex) {
      ; //this is expected
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testConstruct_3()
      throws Exception {
    try {
      new CommandDynamicArityImpl(m_gpconf, 3, 2, 1);
    } catch (RuntimeException rex) {
      ; //this is expected
    }
  }

  public void testExecution_0()
      throws Exception {
    getRandomGenerator().setNextFloatSequence(new float[] {
        0.01f, //compared against crossover prob.
        0.20f, //function probability 1
        0.20f, //function probability 2
    });
    getRandomGenerator().setNextIntSequence(new int[] {
        2, 1, 3, 0, 4, 6, 2, 5, 0, 5, 1, 1, 3, 0, 4, 9, 11, 45, 7, 3, 13, 2, 0,
        0, 0, 1
    });
    m_gpconf.setDynamizeArityProb(1);
    GPGenotype gp;
    Class[] types = {
        Void.class};
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        new SubProgram(m_gpconf, new Class[] {Void.class, Void.class}),
        new SubProgram(m_gpconf, new Class[] {Void.class, Void.class, Void.class}),
        new IfDyn(m_gpconf, Void.class, 1, 1, 4),
        new NOP(m_gpconf, Void.class, 0),
        new Terminal(m_gpconf, int.class),
        new True(m_gpconf, Boolean.class),
    }
    };
    gp = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes, nodeSets,
        20, true);
    gp.setVerboseOutput(false);
    gp.evolve(1);
    // Verify arity of IfDyn.
    // ----------------------
    GPPopulation pop = gp.getGPPopulation();
    int index = 0;
    for (int i = 0; i < pop.size(); i++) {
      CommandGene gene = pop.getGPProgram(i).getChromosome(0).getGene(0);
      if (IfDyn.class.isAssignableFrom(gene.getClass())) {
        IfDyn ifdyn = (IfDyn) gene;
        int arity = ifdyn.getArity(null);
        switch (index) {
          case 0:
            assertEquals(4, arity);
            break;
          case 1:
            assertEquals(4, arity);
            break;
          case 2:
            assertEquals(4, arity);
            break;
          case 3:
            assertEquals(4, arity);
            break;
          case 4:
            assertEquals(4, arity);
            break;
          case 5:
            assertEquals(4, arity);
            break;
          case 7:
            assertEquals(3, arity);
            break;
          case 9:
            assertEquals(3, arity);
            break;
        }
        index++;
      }
    }
  }

  /**
   * Test implementation extending abstract CommandDynamicArity class.
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  class CommandDynamicArityImpl
      extends CommandDynamicArity {
    public CommandDynamicArityImpl(final GPConfiguration a_config,
                                   int a_arityInitial, int a_arityMin,
                                   int a_arityMax)
        throws InvalidConfigurationException {
      super(a_config, a_arityInitial, a_arityMin, a_arityMax,
            Void.class);
    }

    protected Gene newGeneInternal() {
      return null;
    }

    public Class getChildType(IGPProgram a_ind, int a_chromNum) {
      return null;
    }

    public String toString() {
      return "test";
    }
  }
}
