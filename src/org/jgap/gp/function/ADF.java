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
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Automatically Defined Function (ADF). Works with output of other chromosomes.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ADF
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private int m_chromosomeNum;

  public ADF(final GPConfiguration a_conf, int a_chromosomeNum)
      throws InvalidConfigurationException {
    super(a_conf, 0, null);
    m_chromosomeNum = a_chromosomeNum;
  }

  protected CommandGene newGeneInternal() {
    try {
      CommandGene gene = new ADF(getGPConfiguration(), m_chromosomeNum);
      return gene;
    } catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public int getChromosomeNum() {
    return m_chromosomeNum;
  }

  public String toString() {
    return "ADF(" + m_chromosomeNum + ")";
  }

  public int getArity(GPProgram a_individual) {
    return a_individual.getChromosome(m_chromosomeNum).getArity();
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = c.getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Integer(c.execute_int(n, i, args));
    }
    // Call the chromosome.
    // --------------------
    return c.getIndividual().execute_int(m_chromosomeNum, vals);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = c.getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Float(c.execute_float(n, i, args));
    }
    return c.getIndividual().execute_float(m_chromosomeNum, vals);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = c.getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Double(c.execute_double(n, i, args));
    }
    return c.getIndividual().execute_double(m_chromosomeNum, vals);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = c.getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = c.execute(n, i, args);
    }
    return c.getIndividual().execute_object(m_chromosomeNum, vals);
  }

  public Class getChildType(GPProgram a_ind, int i) {
    return a_ind.getChromosome(m_chromosomeNum).getArgTypes()[i];
  }

  public boolean isValid(ProgramChromosome a_chrom) {
    // Avoid endless recursion.
    // ------------------------
    StackTraceElement[] stack = new Exception().getStackTrace();
    if (stack.length > 60) { /**@todo enhance*/
      return false;
    }
    return true;
  }

  /**
   * The compareTo-method.
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      ADF other = (ADF) a_other;
      return new CompareToBuilder()
          .append(m_chromosomeNum, other.m_chromosomeNum)
          .toComparison();
    }
  }

  /**
   * The equals-method.
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    if (a_other == null) {
      return false;
    }
    else {
      try {
        ADF other = (ADF) a_other;
        return new EqualsBuilder()
            .append(m_chromosomeNum, other.m_chromosomeNum)
            .isEquals();
      } catch (ClassCastException cex) {
        return false;
      }
    }
  }
}
