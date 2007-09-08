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
import org.jgap.gp.impl.*;
import org.jgap.gp.*;

/**
 * Tests the DeltaGPFitnessEvaluator class.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class DeltaGPFitnessEvaluatorTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DeltaGPFitnessEvaluatorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testisFitter_0()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setFitnessValue(1.0d);
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom2 = new ProgramChromosome(m_gpconf, 2, prog2);
    prog2.setChromosome(0, chrom2);
    prog2.setFitnessValue(2.0d);
    DeltaGPFitnessEvaluator eval = new DeltaGPFitnessEvaluator();
    assertTrue(eval.isFitter(prog, prog2));
    assertFalse(eval.isFitter(prog2, prog));
    assertFalse(eval.isFitter(prog2, prog2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testisFitter_1()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setFitnessValue(2.0d);
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom2 = new ProgramChromosome(m_gpconf, 2, prog2);
    prog2.setChromosome(0, chrom2);
    prog2.setFitnessValue(Double.NaN);
    DeltaGPFitnessEvaluator eval = new DeltaGPFitnessEvaluator();
    assertTrue(eval.isFitter(prog, prog2));
    assertFalse(eval.isFitter(prog2, prog));
    assertFalse(eval.isFitter(prog2, prog2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void testisFitter_2()
      throws Exception {
    GPProgram prog = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setFitnessValue(Double.NaN);
    GPProgram prog2 = new GPProgram(m_gpconf, 1);
    ProgramChromosome chrom2 = new ProgramChromosome(m_gpconf, 2, prog2);
    prog2.setChromosome(0, chrom2);
    prog2.setFitnessValue(2.0d);
    DeltaGPFitnessEvaluator eval = new DeltaGPFitnessEvaluator();
    assertTrue(eval.isFitter(prog2, prog));
    assertFalse(eval.isFitter(prog, prog2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testSerialize_0()
      throws Exception {
    DeltaGPFitnessEvaluator eval = new DeltaGPFitnessEvaluator();
    DeltaGPFitnessEvaluator eval2 = (DeltaGPFitnessEvaluator) doSerialize(
        eval);
    assertEquals(eval, eval2);
  }
}
