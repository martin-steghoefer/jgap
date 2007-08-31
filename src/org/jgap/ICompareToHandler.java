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
 * Interface for handlers capable of  comparing instances of specific classes.
 * <p>A compareTo-handler answers via isHandlerFor(Class) whether he could
 * compare instances of the given class.<p>
 * The perform(Object, Object) method does the comparing acknowledged before
 * with isHandlerFor(Class).
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface ICompareToHandler
    extends IHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

}
