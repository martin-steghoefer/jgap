/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Automatically Defined Function (ADF).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ADF
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int m_chromosomeNum;

  public ADF(final Configuration a_conf, int a_chromosomeNum)
      throws InvalidConfigurationException {
    super(a_conf, 0, null);
    m_chromosomeNum = a_chromosomeNum;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new ADF(getConfiguration(), m_chromosomeNum);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public int getChromosomeNum() {
    return m_chromosomeNum;
  }

  public String toString() {
    return "ADF(" + m_chromosomeNum + ")";
  }

  public int getArity() {
    return getIndividual().getChromosome(m_chromosomeNum).getArity();
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Integer(c.execute_int(n, i, args));
    }
    // Call the chromosome
    return getIndividual().execute_int(m_chromosomeNum, vals);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Float(c.execute_float(n, i, args));
    }
    return getIndividual().execute_float(m_chromosomeNum, vals);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Double(c.execute_double(n, i, args));
    }
    return getIndividual().execute_double(m_chromosomeNum, vals);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    int numargs = getIndividual().getChromosome(m_chromosomeNum).getArity();
    StackTraceElement[] stack = new Exception().getStackTrace();
    if (stack.length > 60) {
      throw new IllegalStateException("ADF recursion detected");
    }
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = c.execute(n, i, args);
    }
    return getIndividual().execute_object(m_chromosomeNum, vals);
  }

  public Class getChildType(int i) {
    return getIndividual().getChromosome(m_chromosomeNum).getArgTypes()[i];
  }

  public boolean isValid(ProgramChromosome a_chrom) {
    StackTraceElement[] stack = new Exception().getStackTrace();
    if (stack.length > 60) {/**@todo enhance*/
      return false;
    }
    return true;
  }
}
