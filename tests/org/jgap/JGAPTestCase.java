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

import java.util.*;
import junit.framework.*;
import junitx.util.*;

/**
 * Abstract test case for all JGAP test cases providing a common infrastructure
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class JGAPTestCase
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  protected static final double DELTA = 0.0000001;

  public final static PrivateAccessor privateAccessor = null;

  public JGAPTestCase(String name) {
    super(name);
  }

  public JGAPTestCase() {
    super();
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  /**
   *
   * @param list1 first list of chromosomes
   * @param list2 second list of chromosomes
   * @return true lists of chromosomes are equal
   * @author Klaus Meffert
   */
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


  public static void assertEqualsMap(Map a_map1, Map a_map2) {
    /**@todo implement*/
  }

  public class TestFitnessFunction
      extends FitnessFunction {
    /**
     * @param a_subject Chromosome
     * @return double
     * @since 2.0 (until 1.1: return type int)
     */
    protected double evaluate(Chromosome a_subject) {
      //result does not matter here
      return 1.0000000d;
    }
  }

  public static void assertInList(final Map a_list, Object a_object) {
    if (a_list.containsKey(a_object)) {
      a_list.remove(a_object);
    }
    else {
      // Because only source code is browsed (also non-compilable code!),
      // there is no disctinction between class java.lang.X and class X
      if (a_list.containsKey("java.lang." + a_object)) {
        a_list.remove("java.lang." + a_object);
      }
      else {
        fail("Object " + a_object + " not in list!");
      }
    }
  }

}
