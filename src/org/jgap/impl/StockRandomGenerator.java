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
import java.util.*;

import org.jgap.*;
import org.jgap.util.*;

/**
 * The stock random generator uses the java.util.Random class to
 * provide a simple implementation of the RandomGenerator interface.
 * No actual code is provided here.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class StockRandomGenerator
    extends Random
    implements RandomGenerator, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * When deserializing, initialize the seed because otherwise we could get
   * duplicate evolution results when doing distributed computing!
   *
   * @param a_inputStream the ObjectInputStream provided for deserialzation
   *
   * @throws IOException
   * @throws ClassNotFoundException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  private void readObject(ObjectInputStream a_inputStream)
      throws IOException, ClassNotFoundException {
    //always perform the default de-serialization first
    a_inputStream.defaultReadObject();
    setSeed(System.currentTimeMillis());
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    StockRandomGenerator result = new StockRandomGenerator();
    result.setSeed(System.currentTimeMillis());
    return result;
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
