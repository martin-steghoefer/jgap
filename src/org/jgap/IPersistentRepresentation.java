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

/**
 * Interface for objects that can represent themselves as a string and parse
 * this string later on to generate a new instance of themselves.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IPersistentRepresentation {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Returns a persistent representation of an entity (such as a chromosome or a
   * gene).
   *
   * @return string representation of current state
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getPersistentRepresentation();

  /**
   * Counterpart of getPersistentRepresentation.
   *
   * @param a_representation the string representation retrieved from a prior
   * call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException;
}
