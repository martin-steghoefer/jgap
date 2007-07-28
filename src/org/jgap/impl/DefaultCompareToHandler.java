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

import org.jgap.*;
import org.jgap.util.*;
import java.io.*;

/**
 * Default implementation for comparing Comparables. Boolean values are also
 * covered with this handler as the Boolean class has no compareTo method.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DefaultCompareToHandler
    implements ICompareToHandler, ICloneable, Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.7 $";

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
    if (Comparable.class.isAssignableFrom(clazz)) {
      return true;
    }
    else {
      if (clazz != null && Boolean.class == clazz) {
        return true;
      }
      else {
        return false;
      }
    }
  }

  public Object perform(final Object a_obj, final Class a_class,
                        final Object a_params)
      throws Exception {
    int i;
    if (a_obj == null) {
      if (a_params != null) {
        i = -1;
      }
      else {
        i = 0;
      }
    }
    else if (a_params == null) {
      i = 1;
    }
    else {
      if (a_obj.getClass() == Boolean.class) {
        boolean b1 = ( (Boolean) a_obj).booleanValue();
        boolean b2 = ( (Boolean) a_params).booleanValue();
        if (b1 == b2) {
          i = 0;
        }
        else if (b1) {
          i = 1;
        }
        else
          i = -1;
      }
      else {
        i = ( (Comparable) a_obj).compareTo(a_params);
      }
    }
    return new Integer(i);
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new DefaultCompareToHandler();
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
