/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.xml;

/**
 * The GeneCreationException is a bit of a catch-all exception for
 * representing problems encountered during the creation of a Gene
 * object with a value (allele) representation found in an XML document.
 * Typically, this exception will be thrown if the concrete class
 * indicated in the XML file cannot be found or instantiated for
 * some reason, or if the class does not support the methods related to
 * XML persistence. Consult the error message for details on the specific
 * reason for failure.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class GeneCreationException
    extends Exception {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Constructs a new GeneCreationException instance with the given error
   * message.
   *
   * @param a_message an error message describing the reason this exception
   * is being thrown
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public GeneCreationException(final String a_message) {
    super(a_message);
  }

  /**
   * Constructs a new GeneCreationException instance with the given error
   * message.
   *
   * @param a_message an error message describing the reason this exception
   * is being thrown
   * @param a_geneClass the class of the gene that should be instantiated
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GeneCreationException(final Class a_geneClass, Throwable cause) {
    super(a_geneClass != null ?
          "Gene class " + a_geneClass.getName() : "", cause);
  }
}
