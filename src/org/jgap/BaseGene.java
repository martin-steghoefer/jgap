/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;

import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * Abstract base class for all genes. Provides default implementations.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public abstract class BaseGene
    implements Gene, IBusinessKey {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.28 $";

  /**
   * Constants for toString()
   */
  public final static String S_APPLICATION_DATA = "Application data";

  /**
   * Delta, useful for comparing doubles and floats.
   */
  public static final double DELTA = 0.0000001;

  /** Energy of a gene, see RFE 1102206*/
  private double m_energy;

  /**
   * Application-specific data that is attached to the Gene. This data may
   * assist the application in labelling this Gene.
   * JGAP ignores the data, aside from allowing it to be set and
   * retrieved and considering it in clone() and compareTo().
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

  private Configuration m_configuration;

  /**
   * Unique ID of the gene that allows to distinct it from other genes. In the
   * best case, this ID is unique worldwide.
   */
  private String m_uniqueID;

  /**
   * In case mutation, crossing over etc. happened, this sequence gives evidence
   * about the parent(s) of the current gene.
   */
  private Map<Integer,String> m_uniqueIDTemplates;

  /**
   *
   * @param a_configuration the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BaseGene(Configuration a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_configuration = a_configuration;
    if (m_configuration.isUniqueKeysActive()) {
      m_uniqueIDTemplates = new HashMap();
      IJGAPFactory factory = m_configuration.getJGAPFactory();
      if (JGAPFactory.class.isAssignableFrom(factory.getClass())) {
        m_uniqueID = ( (JGAPFactory) (m_configuration.getJGAPFactory())).
            getUniqueKey(getClass().getName());
      }
    }
  }
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
   * @return a string representation of this Gene's value
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
   * @return the size of the gene, i.e the number of atomic elements. Always 1
   * for non-composed Gene types. Override for composed Gene types
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
   * @param a_other the object to compare to this Gene for equality
   * @return true if this Gene is equal to the given object, false otherwise
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(final Object a_other) {
    try {
      int result = compareTo(a_other);
      if (result == 0) {
        if (isCompareApplicationData()) {
          Gene otherGene = (Gene) a_other;
          int resultAppData = compareApplicationData(getApplicationData(),
              otherGene.getApplicationData());
          return resultAppData == 0;
        }
        else {
          return true;
        }
      }
      else {
        return false;
      }
    } catch (ClassCastException e) {
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
  public void setEnergy(final double a_energy) {
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
  public void setApplicationData(final Object a_newData) {
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
  public void setCompareApplicationData(final boolean a_doCompare) {
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

  protected int compareApplicationData(final Object a_appdata1,
                                       final Object a_appdata2) {
    // Compare application data.
    // -------------------------
    if (a_appdata1 == null) {
      if (a_appdata2 != null) {
        return -1;
      }
      else {
        return 0;
      }
    }
    else if (a_appdata2 == null) {
      return 1;
    }
    else {
      // The above code is contained in the following, but for performance
      // issues we keep it here redundantly.
      // -----------------------------------------------------------------
      ICompareToHandler handler = getConfiguration().getJGAPFactory().
          getCompareToHandlerFor(a_appdata1, a_appdata2.getClass());
      if (handler != null) {
        try {
          return ( (Integer) handler.perform(a_appdata1, null, a_appdata2)).
              intValue();
        } catch (Exception ex) {
          throw new Error(ex);
        }
      }
      else {
        return 0;
      }
    }
  }

  /**
   * Optional helper class for checking if a given allele value to be set
   * for a given gene is valid. If not, the allele value may not be set for the
   * gene or the gene type (e.g. IntegerGene) is not allowed in general!
   *
   * @since 2.5 (moved from CompositeGene, where it was since 2.0)
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object) is called.
   * @param a_constraintChecker the constraint checker to be set
   *
   * @author Klaus Meffert
   * @since 2.5 (moved from CompositeGene, where it was since 2.0)
   */
  public void setConstraintChecker(
      final IGeneConstraintChecker a_constraintChecker) {
    m_geneAlleleChecker = a_constraintChecker;
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setAllele(Object) is called.
   *
   * @author Klaus Meffert
   * @since 2.5 (moved from CompositeGene, where it was since 2.0)
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

  /**
   * Provides implementation-independent means for creating new Gene
   * instances. The new instance that is created and returned should be
   * setup with any implementation-dependent configuration that this Gene
   * instance is setup with (aside from the actual value, of course). For
   * example, if this Gene were setup with bounds on its value, then the
   * Gene instance returned from this method should also be setup with
   * those same bounds. This is important, as the JGAP core will invoke this
   * method on each Gene in the sample Chromosome in order to create each
   * new Gene in the same respective gene position for a new Chromosome.
   *
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 2.6 (since 1.0 in IntegerGene)
   */
  public Gene newGene() {
    Gene result = newGeneInternal();
    result.setConstraintChecker(getConstraintChecker());
    result.setEnergy(getEnergy());
    /**@todo clone app.data*/
    result.setApplicationData(getApplicationData());
    return result;
  }

  protected abstract Gene newGeneInternal();

  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_configuration;
  }

  public String getBusinessKey() {
    Object allele = getAllele();
    String result = getClass().getName() + PERSISTENT_FIELD_DELIMITER;
    if (allele == null) {
      return result;
    }
    return result + allele.toString();
  }

  protected String encode(String a_string) {
    return StringKit.encode(a_string);
  }

  protected String decode(String a_string) {
    return StringKit.decode(a_string);
  }

  /**
   * @return unique ID of the gene, which allows to distinct this instance
   * from others, in the best case worldwide
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public String getUniqueID() {
    return m_uniqueID;
  }

  /**
   * A template is a gene that is the logical predecessor of the current
   * gene. A template can occur in mutation or crossing over. In the
   * latter case can be at least two template genes. This is why in this
   * setter method the parameter a_index exists.
   *
   * @param a_templateID the unique ID of the template
   * @param a_index the index of the template, e.g. in crossing over for the
   * second candidate gene this is 2
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void setUniqueIDTemplate(String a_templateID, int a_index) {
    m_uniqueIDTemplates.put(a_index, a_templateID);
  }

  /**
   * @param a_index the index of the template to retrieve the key for
   * @return String
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public String getUniqueIDTemplate(int a_index) {
    return m_uniqueIDTemplates.get(a_index);
  }

}
