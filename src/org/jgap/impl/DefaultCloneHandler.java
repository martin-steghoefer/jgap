/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.io.*;
import java.lang.reflect.*;
import org.jgap.util.*;
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
    implements ICloneHandler, ICloneable, Serializable, Comparable {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

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
    Class clazz;
    if (a_clazz == null) {
      if (a_obj == null) {
        return false;
      }
      clazz = a_obj.getClass();
    }
    else {
      clazz = a_clazz;
    }
    if (IApplicationData.class.isAssignableFrom(clazz)) {
      return true;
    }
    if (ICloneable.class.isAssignableFrom(clazz)) {
      return true;
    }
    if (Cloneable.class.isAssignableFrom(clazz)) {
      // Ensure access via reflection is possible.
      // Thank you Java for providing only a marker interface and not
      // something convenient :-(
      // ------------------------------------------------------------
      boolean result;
      try {
        Method m = clazz.getMethod("clone", new Class[] {});
        boolean modified = false;
        if (!m.isAccessible()) {
          m.setAccessible(true);
          modified = true;
        }
        result = m.isAccessible();
        if (modified) {
          m.setAccessible(false);
        }
      }
      catch (Exception ex) {
        return false;
      }
      return result;
    }
    else {
      return false;
    }
  }

  /**
   * Performs the clone operation.
   *
   * @param a_objToClone the object to clone
   * @param a_class not considered here
   * @param a_params not considered here
   *
   * @return Object
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public Object perform(final Object a_objToClone, final Class a_class,
                        final Object a_params) {
    Class clazz;
    if (a_class == null) {
      if (a_objToClone == null) {
        return null;
      }
      clazz = a_objToClone.getClass();
    }
    else {
      clazz = a_class;
    }
    if (ICloneable.class.isAssignableFrom(clazz)) {
      try {
        return ( (ICloneable) a_objToClone).clone();
      }
      catch (CloneException cex) {
        throw new IllegalStateException(cex);
      }
    }
    if (IApplicationData.class.isAssignableFrom(a_objToClone.getClass())) {
      try {
        return ( (IApplicationData) a_objToClone).clone();
      }
      catch (CloneNotSupportedException cex) {
        throw new IllegalStateException(cex);
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
      throw new IllegalStateException(iex.getTargetException());
    }
    catch (Throwable ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new DefaultCloneHandler();
  }

  /**
   * @param a_other sic
   * @return as always
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other.getClass().equals(getClass())) {
      return 0;
    }
    else {
      return getClass().getName().compareTo(a_other.getClass().getName());
    }
  }
}
