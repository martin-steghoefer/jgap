/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * Interface for handlers capable of doing somethign specific.<p>
 * Such a handler acknowledges via isHandlerFor(Class) that the method
 * perform(Object, Object) could be executed for the given class.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  boolean isHandlerFor(Object a_obj, Class a_class);

  Object perform(Object a_obj, Class a_class, Object a_params)
      throws Exception;
}
