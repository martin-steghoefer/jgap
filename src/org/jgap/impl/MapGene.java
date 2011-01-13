/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.lang.reflect.*;
import java.util.*;
import org.jgap.*;
import gnu.trove.*;

/**
 * Creates a gene instance in which individual alleles have both a label (key)
 * and a value with a distinct meaning. This allows to realize a gene with a set
 * of valid values instead of a range of values.
 * For example, IntegerGene only allows for values having a continuous range,
 * and does not have a function where it is possible to specify setValue...
 * <p>This implementation does not support specifying a range of valid
 * integer values. Instead it is planned to provide a constraint checker plugin
 * later on. With this, the current implementation will stay unchanged and can
 * be as performant as possible without losing flexibility.</p>
 *
 * @see class examples.MapGeneExample
 *
 * @author Johnathan Kool, Organisation: RSMAS, University of Miami
 * @author Klaus Meffert
 * @since 2.4
 */
public class MapGene
    extends BaseGene implements IPersistentRepresentation{
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.26 $";

  /**
   * Container for valid alleles
   */
  private THashMap m_geneMap;

  /**
   * Represents the constant range of values supported by integers.
   */
  private Object m_value;

  /**
   * Represents the delimiter that is used to mark the allele map.
   */
  final static String ALLELEMAP_BEGIN_DELIMITER = "[";

  final static String ALLELEMAP_END_DELIMITER = "]";

  /**
   * Default constructor.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   * @throws InvalidConfigurationException
   *
   * @since 2.4
   */
  public MapGene()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public MapGene(final Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config);
    m_geneMap = new THashMap();
  }

  /**
   * Constructor setting up valid alleles directly.
   *
   * @param a_config the configuration to use
   * @param a_alleles the valid alleles of the gene
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public MapGene(final Configuration a_config, final Map a_alleles)
      throws InvalidConfigurationException {
    super(a_config);
    m_geneMap = new THashMap();
    addAlleles(a_alleles);
  }

  protected Gene newGeneInternal() {
    try {
      MapGene result = new MapGene(getConfiguration(), m_geneMap);
      // get m_value from original
      Object value = getAllele();
      result.setAllele(value);
      return result;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Adds a potential allele value to the collection.
   *
   * @param a_key the key to be added, e.g. a descriptive string value
   * @param a_value the Integer value to be added
   * @since 2.4
   */
  public void addAllele(final Object a_key, final Object a_value) {
    m_geneMap.put(a_key, a_value);
  }

  /**
   * Adds a potential allele value to the collection.
   *
   * @param a_value the value to be added, also used as key
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void addAllele(final Object a_value) {
    m_geneMap.put(a_value, a_value);
  }

  /**
   * Convenience method for addAllele (Object's that are Integer's)
   *
   * @param a_value the int value to be added, also used as key
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void addAllele(final int a_value) {
    m_geneMap.put(new Integer(a_value), new Integer(a_value));
  }

  /**
   * Add a set of potential allele values to the collection
   *
   * @param a_alleles the set of alleles to be added
   *
   * @since 2.4
   */
  public void addAlleles(final Map a_alleles) {
    if (a_alleles == null) {
      throw new IllegalArgumentException("List of alleles must not be null!");
    }
    else {
      m_geneMap.putAll(a_alleles);
    }
  }

  /**
   * Removes a potential allele or set of alleles from the collection.
   *
   * @param a_key the unique value(s) of the object(s) to be removed
   *
   * @since 2.4
   */
  public void removeAlleles(final Object a_key) {
    m_geneMap.remove(a_key);
  }

  /**
   * @return the map of alleles
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Map getAlleles() {
    return m_geneMap;
  }

  /**
   * Sets the allele value to be a random value using a defined random number
   * generator. If no valid alleles are defined, any allele is allowed. Then,
   * a new Integer with random value is set as random value. Override this
   * method if you want a different behaviour, such as a Double instead of the
   * Integer type.
   *
   * @param a_numberGenerator the random generator to use
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setToRandomValue(final RandomGenerator a_numberGenerator) {
    if (m_geneMap.isEmpty()) {
      m_value = new Integer(a_numberGenerator.nextInt());
    }
    else {
      m_value = m_geneMap.get(m_geneMap.keySet().toArray()[a_numberGenerator.
                              nextInt(m_geneMap.size())]);
    }
  }

  /**
   * See interface Gene for description of applyMutation.
   *
   * For this kind of gene, providing an index and a percentage of mutation
   * would have no significance because the individual allele forms are
   * independent of one another.  In mutating, they can only change from one
   * form to another. It may be possible to weight the likelihood of mutation
   * to different forms, but that is not implemented currently.
   *
   * @param a_index ignored here
   * @param a_percentage ignored here
   *
   * @author Klaus Meffert
   * @author Johnathan Kool
   * @since 2.4
   */
  public void applyMutation(final int a_index, final double a_percentage) {
    RandomGenerator rn;
    rn = getConfiguration().getRandomGenerator();
    setToRandomValue(rn);
  }

  /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method. This is an optional method but,
   * if not implemented, XML persistence and possibly other features will not
   * be available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @param a_representation the string representation retrieved from a prior
   * call to the getPersistentRepresentation() method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation.
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setValueFromPersistentRepresentation(final String
      a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      StringTokenizer tokenizer = new StringTokenizer(a_representation,
          PERSISTENT_FIELD_DELIMITER);
      // Make sure the representation contains the correct number of
      // fields. If not, throw an exception.
      // -----------------------------------------------------------
      if (tokenizer.countTokens() != 2) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: it must contain two tokens.");
      }
      String valueRepresentation = tokenizer.nextToken();
      // First parse and set the representation of the value.
      // ----------------------------------------------------
      if (valueRepresentation.equals("null")) {
        m_value = null;
      }
      else {
        try {
          m_value = new Integer(Integer.parseInt(valueRepresentation));
        }
        catch (NumberFormatException e) {
          throw new UnsupportedRepresentationException(
              "The format of the given persistent representation " +
              "is not recognized: field 1 does not appear to be " +
              "an integer value.");
        }
      }
      // Parse gene map.
      // ---------------
      String s = tokenizer.nextToken();
      tokenizer = new StringTokenizer(s, ",");
      int lastWasOpening = 0;
      String key = null;
      String keyClass = null;
      String valueClass = null;
      while (tokenizer.hasMoreTokens()) {
        String element = tokenizer.nextToken(",");
        if (lastWasOpening == 1) {
          key = element.substring(0);
          lastWasOpening = 2;
        }
        else if (lastWasOpening == 2) {
          valueClass = element.substring(0);
          lastWasOpening = 3;
        }
        else if (lastWasOpening == 3) {
          if (element.endsWith(")")) {
            element = element.substring(0, element.length() - 1);
            try {
              Class keyType = Class.forName(keyClass);
              Constructor keyC = keyType.getConstructor(new Class[]{String.class});
              Object keyObject = keyC.newInstance(new Object[]{key});

              Class valueType = Class.forName(valueClass);
              Constructor valueC = valueType.getConstructor(new Class[]{String.class});
              Object valueObject = valueC.newInstance(new Object[]{element});
              addAllele(keyObject, valueObject);
              lastWasOpening = 0;
            } catch (Exception cex) {
              throw new UnsupportedRepresentationException("Invalid class: "
                  + keyClass);
            }
          }
          else {
            throw new IllegalStateException("Closing bracket missing");
          }
        }
        else {
          if (element.startsWith("(")) {
            keyClass = element.substring(1);
            lastWasOpening = 1;
          }
          else {
            throw new IllegalStateException("Opening bracket missing");
          }
        }
      }
      if (lastWasOpening != 0) {
        throw new IllegalStateException("Elements missing");
      }
    }
  }

  /**
   * Retrieves a string representation of this Gene that includes any
   * information required to reconstruct it at a later time, such as its
   * value and internal state. This string will be used to represent this
   * Gene in XML persistence. This is an optional method but, if not
   * implemented, XML persistence and possibly other features will not be
   * available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @return string representation of this Gene's current state
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 2.4
   */
  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    // The persistent representation includes the value and the allele
    // assignment.
    // ---------------------------------------------------------------
    Iterator it = m_geneMap.keySet().iterator();
    StringBuffer strbf = new StringBuffer();
    boolean first = true;
    while (it.hasNext()) {
      if (!first) {
        strbf.append(",");
      }
      Object key = it.next();
      Object value = m_geneMap.get(key);
      strbf.append("(" + key.getClass().getName() + "," + key.toString() + "," +
                   value.getClass().getName() + "," + value.toString() + ")");
      first = false;
    }
    return m_value.toString() + MapGene.PERSISTENT_FIELD_DELIMITER +
        strbf.toString();
  }

  /**
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be an instance of current type (e.g. Integer).
   *
   * @param a_newValue the new value of this Gene instance
   *
   * @author Johnathan Kool, Klaus Meffert
   * @since 2.4
   */
  public void setAllele(Object a_newValue) {
    // Ignore null value as it should have no effect here (otherwise problematic
    // in conjunction with newGene).
    // -------------------------------------------------------------------------
    if (a_newValue == null) {
      return;
    }
    if (m_geneMap.values().isEmpty()) {
      m_value = a_newValue;
    }
    else if (m_geneMap.values().contains(a_newValue)) {
      m_value = a_newValue;//m_geneMap.get(a_newValue);
    }
    else {
      throw new IllegalArgumentException("Allele value being set ("
                                         + a_newValue
                                         + ") is not an element of the set of"
                                         + " permitted values.");
    }
  }

  /**
   * Compares this NumberGene with the specified object (which must also
   * be a NumberGene) for order, which is determined by the number
   * value of this Gene compared to the one provided for comparison.
   *
   * @param a_other the NumberGene to be compared to this NumberGene
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison
   *
   * @throws ClassCastException if the specified object's type prevents it from
   * being compared to this Gene
   *
   * @author Klaus Meffert
   * @author Johnathan Kool
   * @since 2.4
   */
  public int compareTo(Object a_other) {
    MapGene otherGene = (MapGene) a_other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the overridden compareToNative
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // If our value is not null, then we're the greater gene.
      // ------------------------------------------------------
      if (m_value != null) {
        return 1;
      }
    }
    try {
      int size1 = m_geneMap.size();
      int size2 = otherGene.m_geneMap.size();
      if (size1 != size2) {
        if (size1 < size2) {
          return -1;
        }
        else {
          return 1;
        }
      }
      else {
        // Compare geneMap keys and values.
        Iterator it1 = m_geneMap.keySet().iterator();
//        Iterator it2 = otherGene.m_geneMap.keySet().iterator();
        while (it1.hasNext()) {
          Object key1 = it1.next();
          if (!otherGene.m_geneMap.keySet().contains(key1)) {
            Object key2 = otherGene.m_geneMap.keySet().iterator().next();
            if (Comparable.class.isAssignableFrom(key1.getClass())
                && Comparable.class.isAssignableFrom(key2.getClass())) {
              return ( (Comparable) key1).compareTo(key2);
            }
            else {
              // Arbitrarily return -1
              return -1;
            }
          }
          Object value1 = m_geneMap.get(key1);
          Object value2 = otherGene.m_geneMap.get(key1);
          if (value1 == null && value2 != null) {
            return -1;
          }
          else if (value1 == null && value2 != null) {
            return -1;
          }
          else if (!value1.equals(value2)) {
            if (value2 == null) {
              return 1;
            }
            else {
              if (Comparable.class.isAssignableFrom(value1.getClass())
                  && Comparable.class.isAssignableFrom(value2.getClass())) {
                return ( (Comparable) value1).compareTo(value2);
              }
              else {
                // Arbitrarily return -1
                return -1;
              }
            }
          }
        }
      }
      if (m_value == null) {
        if (otherGene.m_value != null) {
          return 1;
        }
        else {
          return 0;
        }
      }
      Method method = m_value.getClass().getMethod("compareTo",
          new Class[] {otherGene.m_value.getClass()});
      Integer i = (Integer) method.invoke(m_value,
                                          new Object[] {otherGene.m_value});
      return i.intValue();
    }
    catch (InvocationTargetException ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("CompareTo method of the Gene value" +
                                         " object cannot be invoked.");
    }
    catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("The value object of the Gene does" +
                                         " not have a compareTo method.  It" +
                                         " cannot be compared.");
    }
    catch (IllegalAccessException ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("The compareTo method of the Gene" +
                                         " value object cannot be accessed ");
    }
    catch (SecurityException ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("The compareTo method of the Gene" +
                                         " value object cannot be accessed." +
                                         "  Insufficient permission levels.");
    }
    catch (NoSuchMethodException ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException("The value object of the Gene does" +
                                         " not have a compareTo method.  It" +
                                         " cannot be compared.");
    }
  }

  /**
   * @return the internal value of the gene
   * @since 2.4
   */
  protected Object getInternalValue() {
    return m_value;
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome
   * @return -1 if no allele set, otherwise value return by BaseGene.hashCode()
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public int hashCode() {
    if (getInternalValue() == null) {
      return -71;
    }
    else {
      return super.hashCode();
    }
  }

  /**
   * Retrieves a string representation of this Gene's value that may be useful
   * for display purposes.
   *
   * @return a string representation of this Gene's value
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public String toString() {
    String result = "[";
    if (m_geneMap.size() < 1) {
      result += "null";
    }
    else {
      Set keys = m_geneMap.keySet();
      Iterator keyIterator = keys.iterator();
      boolean firstTime = true;
      while (keyIterator.hasNext()) {
        if (!firstTime) {
          result += ",";
        }
        else {
          firstTime = false;
        }
        Object key = keyIterator.next();
        String keyString;
        if (key == null) {
          keyString = "null";
        }
        else {
          keyString = key.toString();
        }
        result += "(" + keyString + ",";
        Object value = m_geneMap.get(key);
        String valueString;
        if (value == null) {
          valueString = "null";
        }
        else {
          valueString = value.toString();
        }
        result += valueString + ")";
      }
    }
    result += "]";
    return result;
  }
}
