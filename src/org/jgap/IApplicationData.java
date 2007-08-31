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
 * The Chromosome class allows to attach a custom object that is ignored by
 * the genetic operations. Because the Chromosome holding it can be cloned
 * and compared, the attached object should also. With this interface it is
 * forced that the application object attached to the Chromosome with the
 * setApplicationData-method is cloneable and comparable.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IApplicationData extends Comparable, Cloneable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  Object clone() throws CloneNotSupportedException;
}
