/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * Abstract base class for all genes. Provides default implementations.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public abstract class BaseGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public BaseGene() {
  }

  /**
   * Retrieves the value represented by this Gene. All values returned
   * by this class will be Boolean instances.
   *
   * @return the Boolean value of this Gene.
   * @since 1.0
   */
  public Object getAllele() {
    return getValue();
  }

  /**
   * Retrieves the hash code value for a Gene.
   * Override if another hashCode() implementation is necessary or more
   * appropriate than this default implementation.
   *
   * @return this Gene's hash code.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int hashCode() {
    // If our internal Integer is null, then return zero. Otherwise,
    // just return the hash code of the Object.
    // -------------------------------------------------------------
    if (getValue() == null) {
      return 0;
    }
    else {
      return getValue().hashCode();
    }
  }

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   * If you need a special cleanup, override this method.
   *
   * @author Klaus Meffert
   * @since 1.0
   */
  public void cleanup() {
    // No specific cleanup is necessary by default.
    // ---------------------------------------------------------
  }

  /**
   * Retrieves a string representation of this Gene's value that
   * may be useful for display purposes.
   *
   * @return a string representation of this Gene's value.
   *
   * @author Klaus Meffert
   * @since 1.0
   */
  public String toString() {
    if (getValue() == null) {
      return "null";
    }
    else {
      return getValue().toString();
    }
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   * Always 1 for non-composed Gene types. Override for composed Gene types.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public int size() {
    return 1;
  }

  /**
   * Each Gene implementation holds its own m_value object keeping the allele
   * value. In your Gene implementation, just return it with this method
   * (see {@link org.jgap.impl.BooleanGene} for example)
   * @return the m_value object
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected abstract Object getValue();
}
