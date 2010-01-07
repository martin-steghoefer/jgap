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
 * Interface for unique keys. A unique key is used to distinct an instance of
 * a chromosome, a gene etc. from other instances.
 * In the best case, a such a key is unique worldwide.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public interface IUniqueKey {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";


  /**
   *
   * @return String
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  String getUniqueID();

  /**
   *
   * @param a_templateID String
   * @param a_index int
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  void setUniqueIDTemplate(String a_templateID, int a_index);

  /**
   *
   * @param a_index int
   * @return String
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  String getUniqueIDTemplate(int a_index);

}
