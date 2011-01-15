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
import org.jgap.impl.*;
import org.jgap.gp.function.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.impl.*;

/**
 * Abstract test case class for GP-related tests.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class GPTestCase
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  protected GPConfiguration m_gpconf;

  protected RandomGeneratorForTesting rn;

  protected Constant CMD_CONST0, CMD_CONST1, CMD_CONST2, CMD_CONST3, CMD_CONST4;

  protected Terminal CMD_TERM0, CMD_TERM1, CMD_TERM2;

  protected Add CMD_ADD;

  protected ForLoop CMD_FOR;

  protected ForXLoop CMD_FORX;

  protected SubProgram CMD_SUB_I_I, CMD_SUB_I_I2, CMD_SUB_I_IM, CMD_SUB_I_I2M,
  CMD_SUB_I_I_I, CMD_SUB_V_I, CMD_SUB_V_V_V, CMD_SUB_V_V;

  protected NOP CMD_NOP;

  public RandomGeneratorForTesting getRandomGenerator() {
    return (RandomGeneratorForTesting) m_gpconf.getRandomGenerator();
  }

  public void setUp() {
    super.setUp();
    try {
      GPConfiguration.reset();
      m_gpconf = new GPConfiguration();
      rn = new RandomGeneratorForTesting(3);
      m_gpconf.setRandomGenerator(rn);
      m_gpconf.setPopulationSize(20);
      //
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
      CMD_TERM0 = new Terminal(m_gpconf, CommandGene.IntegerClass);
      CMD_TERM1 = new Terminal(m_gpconf, CommandGene.IntegerClass);
      CMD_TERM2 = new Terminal(m_gpconf, CommandGene.IntegerClass);
      CMD_NOP = new NOP(m_gpconf);
      CMD_FOR = new ForLoop(m_gpconf, CommandGene.IntegerClass, 15);
      CMD_FORX = new ForXLoop(m_gpconf, CommandGene.IntegerClass);
      CMD_SUB_I_I = new SubProgram(m_gpconf,
                                   new Class[] {CommandGene.IntegerClass,
                                   CommandGene.IntegerClass});
      CMD_SUB_I_IM = new SubProgram(m_gpconf,
                                    new Class[] {CommandGene.IntegerClass,
                                    CommandGene.IntegerClass}, true);
      CMD_SUB_I_I2 = new SubProgram(m_gpconf,
                                    new Class[] {CommandGene.IntegerClass,
                                    CommandGene.IntegerClass});
      CMD_SUB_I_I2M = new SubProgram(m_gpconf,
                                     new Class[] {CommandGene.IntegerClass,
                                     CommandGene.IntegerClass}, true);
      CMD_SUB_I_I_I = new SubProgram(m_gpconf,
                                     new Class[] {CommandGene.IntegerClass,
                                     CommandGene.IntegerClass,
                                     CommandGene.IntegerClass});
      CMD_SUB_V_I = new SubProgram(m_gpconf,
                                   new Class[] {CommandGene.VoidClass,
                                   CommandGene.IntegerClass});
      CMD_SUB_V_V = new SubProgram(m_gpconf,
                                   new Class[] {CommandGene.VoidClass,
                                   CommandGene.VoidClass});
      CMD_SUB_V_V_V = new SubProgram(m_gpconf,
                                     new Class[] {CommandGene.VoidClass,
                                     CommandGene.VoidClass,
                                     CommandGene.VoidClass});
      CMD_ADD = new Add(m_gpconf, CommandGene.IntegerClass);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
