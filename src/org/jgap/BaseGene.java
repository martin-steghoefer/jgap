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
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /** Energy of a gene, see RFE 1102206*/
  private double m_energy;

  /**
   * Retrieves the value represented by this Gene. All values returned
   * by this class will be Boolean instances.
   *
   * @return the Boolean value of this Gene.
   * @since 1.0
   */
  public Object getAllele() {
    return getInternalValue();
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
    // If our internal value is null, then return zero. Otherwise,
    // just return the hash code of the Object.
    // -------------------------------------------------------------
    if (getInternalValue() == null) {
      return 0;
    }
    else {
      return getInternalValue().hashCode();
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
    if (getInternalValue() == null) {
      return "null";
    }
    else {
      return getInternalValue().toString();
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
   * Compares this Gene with the given object and returns true if the other
   * object is a Gene of the same type and has the same value (allele) as
   * this Gene. Otherwise it returns false.
   *
   * @param other the object to compare to this Gene for equality.
   * @return true if this Gene is equal to the given object, false otherwise.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(Object other) {
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't an Gene of current type
      // (like IntegerGene for IntegerGene's), then we're not equal.
      // -------------------------------------------------
      return false;
    }
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
  protected abstract Object getInternalValue();

  /**
   * @return energy of the gene
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double getEnergy() {
    return m_energy;
  }

  /**
   * Sets the energy of the gene
   * @param a_energy the energy to set
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setEnergy(double a_energy) {
    m_energy = a_energy;
  }
}