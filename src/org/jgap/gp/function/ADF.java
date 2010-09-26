/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Automatically Defined Function (ADF). Works with output of other chromosomes.
 * An ADF is automatically created by ProgramChromosome.
 * For more information about ADFs see John Koza's book on Genetic Programming.
 * Or google for "Koza ADF".
 * For a German explanation, see
 * see http://www.tu-chemnitz.de/informatik/ThIS/seminare/ws01/gp/singer.pdf
 * or
 * http://www.tu-chemnitz.de/informatik/ThIS/seminare/ws01/gp/forbriger.pdf
 * or
 * http://www.klaus-meffert.de/download/genetische_programmierung_mit_java.pdf
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ADF
    extends CommandGene
//    implements ICloneable
{
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  private int m_chromosomeNum;

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_arity the arity of the ADF
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ADF(final GPConfiguration a_conf, int a_chromosomeNum, int a_arity)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, null);
    m_chromosomeNum = a_chromosomeNum;
  }

  /**
   * @return the index of the chromosome to execute
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getChromosomeNum() {
    return m_chromosomeNum;
  }

  public String toString() {
    String res = "ADF" + m_chromosomeNum + "(";
    int size = size();
    for (int i = 1; i <= size; i++) {
      res += "&" + i;
      if (i < size) {
        res += ",";
      }
    }
    res += ")";
    return res;
  }

  public int getArity(IGPProgram a_individual) {
    if (a_individual == null || a_individual.size() <= m_chromosomeNum) {
      return 0;
    }
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

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int numargs = c.getIndividual().getChromosome(m_chromosomeNum).getArity();
    Object[] vals = new Object[numargs];
    for (int i = 0; i < numargs; i++) {
      vals[i] = new Boolean(c.execute_boolean(n, i, args));
    }
    return c.getIndividual().execute_boolean(m_chromosomeNum, vals);
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

  public Class getChildType(IGPProgram a_ind, int i) {
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
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    ADF other = (ADF) a_other;
    return new CompareToBuilder()
        .append(m_chromosomeNum, other.m_chromosomeNum)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      ADF other = (ADF) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_chromosomeNum, other.m_chromosomeNum)
          .isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }
//  /**
//   * Clones the object.
//   *
//   * @return cloned instance of this object
//   *
//   * @author Klaus Meffert
//   * @since 3.4
//   */
//  public Object clone() {
//    try {
//      ADF result = new ADF(getGPConfiguration(), m_chromosomeNum, getArity(INDIVIDUAL));
//      return result;
//    } catch (Exception ex) {
//      throw new CloneException(ex);
//    }
//  }
}
