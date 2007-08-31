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
 * Interface for handlers capable of cloning specific classes.<p>
 * A clone handler answers via isHandlerFor(Class) whether he could clone the
 * given class.<p>
 * The perform(Object, Object) method does the cloning acknowledged before with
 * isHandlerFor(Class).
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface ICloneHandler
    extends IHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

}
