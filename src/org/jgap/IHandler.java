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
 * Interface for handlers capable of doing somethign specific.<p>
 * Such a handler acknowledges via isHandlerFor(Class) that the method
 * perform(Object, Object) could be executed for the given class.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Determines whether the handler is suitable for the given object instance
   * or class.
   *
   * @param a_obj the object instance to check
   * @param a_class the class to check, alternatively if no object instance is
   * available (some handlers could need to get an object and would always
   * return false if only a class is provided)
   * @return true: handler suitable for given object or class
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  boolean isHandlerFor(Object a_obj, Class a_class);

  /**
   * Performs a task for the given object or class. For some handlers,
   * additional parameters are necessary, which could be provided.
   *
   * @param a_obj the object instance to perform the handler task for
   * @param a_class the class to perform the handler task for, in case no object
   * instance is available (some handlers could need to get an object and would
   * always throw an exception if only a class is provided)
   * @param a_params optional parameters needed for the handler to perform its
   * task
   * @throws Exception in case of any error
   * @return Object
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  Object perform(Object a_obj, Class a_class, Object a_params)
      throws Exception;
}
