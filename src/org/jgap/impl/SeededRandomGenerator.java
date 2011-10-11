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
 * The seeded random generator uses the java.util.Random class to
 * provide a simple implementation of the RandomGenerator interface
 * with a custom seed number for repeatable results.
 * It shouldn't be used for a deserializing object because it could cauld
 * duplicate results.
 *
 * @author Machairas Vasileios
 * @author Klaus Meffert
 * @since 3.7
 */
public class SeededRandomGenerator
    extends Random
    implements RandomGenerator, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private final long seed;

  /**
   * Constructor with the seed number.
   *
   * @param seedNumber the seed to initialize randomization with
   *
   * @since 3.7
   */
  public SeededRandomGenerator(long seedNumber){
      super(seedNumber);
      seed = seedNumber;
  }


  /**
   * When deserializing, initialize the seed. This could get
   * duplicate evolution results when doing distributed computing!
   * Please use StockRandomGenerator for that purposes.
   *
   * @param a_inputStream the ObjectInputStream provided for deserialzation
   *
   * @throws IOException
   * @throws ClassNotFoundException
   *
   * @author Machairas Vasileios
   * @author Klaus Meffert
   * @since 3.7
   */
  private void readObject(ObjectInputStream a_inputStream)
      throws IOException, ClassNotFoundException {
    //always perform the default de-serialization first
    a_inputStream.defaultReadObject();
    setSeed(seed);
  }

  /**
   * @return deep clone of this instance
   *
   * @author Machairas Vasileios
   * @author Klaus Meffert
   * @since 3.6
   */
  public Object clone() {
    SeededRandomGenerator result = new SeededRandomGenerator(seed);
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
