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
import org.jgap.*;

/**
 * Default clone handler supporting IApplicationData as well as implementations
 * of Cloneable (for the latter: in case the clone-method is accessible via
 * reflection).
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DefaultCloneHandler
    implements ICloneHandler {
  /**
   * Handles all implementations of IApplicationData as well as all of
   * java.lang.Cloneable (for which the clone-method is accessible via
   * reflection. This is not the case for package protected classes, e.g.).
   *
   * @param a_obj the object to check for (maybe null)
   * @param a_clazz the class to check for whether it is handled (maybe null)
   * @return true in case class is clonable via this handler
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean isHandlerFor(final Object a_obj, final Class a_clazz) {
    if (IApplicationData.class.isAssignableFrom(a_clazz)) {
      return true;
    }
    if (Cloneable.class.isAssignableFrom(a_clazz)) {
      // Ensure access via reflection is possible
      // Thank you Java for providing only a marker interface and not
      // something convenient :-(
      try {
        Method m = a_clazz.getMethod("clone", new Class[] {});
        if (!m.isAccessible()) {
          return false;
        }
      }
      catch (Exception ex) {
        return false;
      }
      return true;
    }
    else {
      return false;
    }
  }

  /**
   *
   * @param a_objToClone the object to clone
   * @param a_class not considered here
   * @param a_params not considered here
   * @return Object
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public Object perform(final Object a_objToClone, final Class a_class,
                        final Object a_params) {
    if (IApplicationData.class.isAssignableFrom(a_objToClone.getClass())) {
      try {
        return ( (IApplicationData) a_objToClone).clone();
      }
      catch (CloneNotSupportedException cex) {
        throw new IllegalStateException(cex.getMessage());
      }
    }
    // Support Cloneable interface by looking for clone() method
    // via introspection.
    // ---------------------------------------------------------
    try {
      Method cloneMethod = a_objToClone.getClass().getMethod(
          "clone", new Class[] {});
      cloneMethod.setAccessible(true);
      return cloneMethod.invoke(a_objToClone, new Object[] {});
    }
    catch (InvocationTargetException iex) {
      throw new IllegalStateException(iex.getTargetException().getMessage());
    }
    catch (Throwable ex) {
      throw new IllegalStateException(ex.getMessage());
    }
  }
}
