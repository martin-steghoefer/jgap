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
 * Interface for objects that offer a business key. A business key helps in
 * identifying an object uniquely.
 *
 * @see http://www.hibernate.org/109.html for a discussion on business keys
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IBusinessKey {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * @return business key
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getBusinessKey();

}
