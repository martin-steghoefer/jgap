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

import java.io.*;
import java.util.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Abstract test case for all JGAP test cases providing a common infrastructure
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class JGAPTestCase
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  protected static final double DELTA = 0.0000001;

  public JGAPTestCase(String name) {
    super(name);
  }

  public JGAPTestCase() {
    super();
  }


  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static boolean isChromosomesEqual(Chromosome[] list1,
                                           Chromosome[] list2) {
    if (list1 == null) {
      return (list2 == null);
    }
    else if (list2 == null) {
      return false;
    }
    else {
      if (list1.length != list2.length) {
        return false;
      }
      else {
        for (int i = 0; i < list1.length; i++) {
          Chromosome c1 = (Chromosome) list1[i];
          Chromosome c2 = (Chromosome) list2[i];
          if (!c1.equals(c2)) {
            return false;
          }
        }
        return true;
      }
    }
  }
}
