/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.lang.reflect.*;
import java.util.*;

import org.jgap.*;

/**
 * Creates a gene instance in which individual alleles have both a label (key)
 * and a value with a distinct meaning. For example, IntegerGene only allows
 * for values having a continuous range, and does not have a function where it
 * is possible to specify setValue...</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Organization: RSMAS, University of Miami</p>
 *
 * @author Johnathan Kool
 * @version 0.1
 */
public class SetGene
    extends BaseGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private HashSet geneSet = new HashSet();

  private Object m_value;

  /**
   * Represents the constant range of values supported by integers.
   */
  public SetGene() {}

  public Gene newGene() {
    return new SetGene();
  }

  /**
   * Adds a potential allele value to the collection.
   *
   * @param i_value Integer:  The Integer value to be added
   */
  public void addAllele(Object allele) {

    geneSet.add(allele);

  }

  /**
   * Add a set of potential allele values to the collection
   *
   * @param alleles Map:  The set of alleles to be added
   */
  public void addAlleles(Collection alleles) {

    geneSet.addAll(alleles);

  }

  /**
   * Removes a potential allele or set of alleles from the collection.
   *
   * @param key Object:  The unique value(s) of the object(s) to be removed
   */
  public void removeAlleles(Object allele) {

    geneSet.remove(allele);

  }

  /**
   * Sets the allele value to be a random value using a defined random number
   * generator.
   *
   * @author Johnathan Kool
   *
   * @param a_numberGenerator RandomGenerator
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {

    m_value = ( (List) geneSet).get(a_numberGenerator.nextInt(geneSet.size()));

  }

  /**
   * See interface Gene for description of applyMutation
   *
   * For this kind of gene, providing an index and a magnitude have no
   * significance because the individual allele forms are independent
   * of one another.  In mutating, they can only cange from one form to
   * another.  It may be possible to weight the likelihood of mutation
   * to different forms, but that will not be implemented here.
   *
   * @author Klaus Meffert
   * @author Johnathan Kool
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    RandomGenerator rn;
    if (Genotype.getConfiguration() != null) {
      rn = Genotype.getConfiguration().getRandomGenerator();
    }
    else {
      rn = new StockRandomGenerator();
    }
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
   * @param a_representation the string representation retrieved from a
   *                         prior call to the getPersistentRepresentation()
   *                         method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   * @throws UnsupportedRepresentationException if this Gene implementation
   *         does not support the given string representation.
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      StringTokenizer tokenizer =
          new StringTokenizer(a_representation,
                              PERSISTENT_FIELD_DELIMITER);
      // Make sure the representation contains the correct number of
      // fields. If not, throw an exception.
      // -----------------------------------------------------------
      if (tokenizer.countTokens() < 3) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: it must contain at least three tokens.");
      }
      String valueRepresentation = tokenizer.nextToken();

      // First parse and set the representation of the value.

      // ----------------------------------------------------
      if (valueRepresentation.equals("null")) {
        m_value = null;
      }
      else {
        try {
          m_value =
              new Integer(Integer.parseInt(valueRepresentation));
        }
        catch (NumberFormatException e) {
          throw new UnsupportedRepresentationException(
              "The format of the given persistent representation " +
              "is not recognized: field 1 does not appear to be " +
              "an integer value.");
        }
      }
      //Parse the potential categories.

      Integer allele;
      while (tokenizer.hasMoreTokens()) {
        try {
          allele = new Integer(Integer.parseInt(tokenizer.nextToken()));
          geneSet.add(allele);
        }
        catch (NumberFormatException e) {
          throw new UnsupportedRepresentationException(
              "The format of the given persistent representation " +
              "is not recognized: a member of the list of eligible values does " +
              "not appear to be an integer value.");
        }
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
   * @return A string representation of this Gene's current state.
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public String getPersistentRepresentation()
      throws
      UnsupportedOperationException {
    // The persistent representation includes the value, lower bound,
    // and upper bound. Each is separated by a colon.
    // --------------------------------------------------------------
    Iterator it = geneSet.iterator();
    StringBuffer strbf = new StringBuffer();
    while (it.hasNext()) {

      strbf.append(PERSISTENT_FIELD_DELIMITER);
      strbf.append(it.next().toString());

    }

    return m_value.toString() + strbf.toString();

  }

  /**
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be an instance of current type (e.g. Integer).
   *
   * @param a_newValue the new value of this Gene instance.
   *
   * @author Johnathan Kool
   */
  public void setAllele(Object a_newValue) {

    if (geneSet.contains(a_newValue)) {

      m_value = a_newValue;

    }

    else {

      throw new IllegalArgumentException("Allele value being set is not an " +
          "element of the set of permitted values.");
    }

  }

  /**
   * Compares this NumberGene with the specified object (which must also
   * be a NumberGene) for order, which is determined by the number
   * value of this Gene compared to the one provided for comparison.
   *
   * @param  other the NumberGene to be compared to this NumberGene.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this NumberGene.
   *
   * @author Klaus Meffert
   * @author Johnathan Kool
   * @since 1.1
   */
  public int compareTo(Object other) {

    SetGene otherGene = (SetGene) other;

    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the overridden compareToNative
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    else {
      try {
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
      return -67;
    }
    else {
      return super.hashCode();
    }
  }
}