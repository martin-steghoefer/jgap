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
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /** Energy of a gene, see RFE 1102206*/
  private double m_energy;

  /**
   * Application-specific data that is attached to the Gene.
   * This data may assist the application in labelling this Gene.
   * in JGAP completely ignores the data, aside from allowing it to be set and
   * retrieved.
   *
   * @since 2.4
   */
  private Object m_applicationData;

  /**
   * Method compareTo(): Should we also consider the application data when
   * comparing? Default is "false" as "true" means a Gene's losing its
   * identity when application data is set differently!
   *
   * @since 2.4
   */
  private boolean m_compareAppData;

  /**
   * Constants for toString()
   */
  public final static String S_APPLICATION_DATA = "Application data";

  /**
   * Retrieves the allele value represented by this Gene.
   *
   * @return the allele value of this Gene
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
   * @return this Gene's hash code
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int hashCode() {
    // If our internal value is null, then return zero. Otherwise,
    // just return the hash code of the allele Object.
    // -----------------------------------------------------------
    if (getInternalValue() == null) {
      return -79;
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
    // --------------------------------------------
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
    String representation;
    if (getInternalValue() == null) {
      representation = "null";
    }
    else {
      representation = getInternalValue().toString();
    }
    String appData;
    if (getApplicationData() != null) {
      appData = getApplicationData().toString();
    }
    else {
      appData = "null";
    }
    representation += ", " + S_APPLICATION_DATA + ":" + appData;
    return representation;
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
   * @param a_other the object to compare to this Gene for equality.
   * @return true if this Gene is equal to the given object, false otherwise.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(Object a_other) {
    try {
      int result =compareTo(a_other);
      if (result == 0) {
        if (m_compareAppData) {
          Gene otherGene = (Gene)a_other;
          // Compare application data.
          // -------------------------
          if (getApplicationData() == null) {
            if (otherGene.getApplicationData() != null) {
              return false;
            }
            else {
              return true;
            }
          }
          else if (otherGene.getApplicationData() == null) {
            return false;
          }
          else {
            if (getApplicationData() instanceof Comparable) {
              try {
                return ( (Comparable) getApplicationData()).compareTo(
                    otherGene.getApplicationData()) == 0;
              }
              catch (ClassCastException cex) {
                return true;
              }
            }
            else {
              return getApplicationData().getClass().getName().compareTo(
                  otherGene.getApplicationData().getClass().getName()) == 0;
            }
          }
        }
        else return true;
      }
      else return false;
    }
    catch (ClassCastException e) {
      // If the other object isn't an Gene of current type
      // (like IntegerGene for IntegerGene's), then we're not equal.
      // -----------------------------------------------------------
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

  /**
   * This sets the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for
   * some applications when it comes time to distinguish a Gene from another.
   * JGAP ignores this data functionally.
   *
   * @param a_newData the new application-specific data to attach to this
   * Gene
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setApplicationData(Object a_newData) {
    m_applicationData = a_newData;
  }

  /**
   * Retrieves the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for
   * some applications when it comes time to distinguish a Gene from another.
   * JGAP ignores this data functionally.
   *
   * @return the application-specific data previously attached to this Gene,
   * or null if there is no data attached
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Gene is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true: consider application data in method compareTo
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setCompareApplicationData(boolean a_doCompare) {
    m_compareAppData = a_doCompare;
  }

  /*
   * @return should we also consider the application data when comparing?
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public boolean isCompareApplicationData() {
    return m_compareAppData;
  }

}
