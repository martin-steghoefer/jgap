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
import java.io.*;
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
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  protected static final double DELTA = 0.0000001;

  public final static PrivateAccessor privateAccessor = null;

  public JGAPTestCase(String a_name) {
    super(a_name);
  }

  public JGAPTestCase() {
    super();
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  /**
   *
   * @param a_list1 first list of chromosomes
   * @param a_list2 second list of chromosomes
   * @return true lists of chromosomes are equal
   *
   * @author Klaus Meffert
   */
  public static boolean isChromosomesEqual(Chromosome[] a_list1,
                                           Chromosome[] a_list2) {
    if (a_list1 == null) {
      return (a_list2 == null);
    }
    else if (a_list2 == null) {
      return false;
    }
    else {
      if (a_list1.length != a_list2.length) {
        return false;
      }
      else {
        for (int i = 0; i < a_list1.length; i++) {
          Chromosome c1 = (Chromosome) a_list1[i];
          Chromosome c2 = (Chromosome) a_list2[i];
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

  /**
   * @param a_obj object to verify
   * @return true: object implements serializable
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean isSerializable(Object a_obj) {
    return Serializable.class.isInstance(a_obj);
  }

  /**
   *
   * @param a_obj object to serialize, then deserialize
   * @return deserialized object that has previously been serialized
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public Object doSerialize(Object a_obj)
      throws Exception {
    // serialize object to a file
    File f = File.createTempFile("object", "ser");
    OutputStream os = new FileOutputStream(f);
    ObjectOutputStream oos = new ObjectOutputStream(os);
    oos.writeObject(a_obj);
    oos.flush();
    oos.close();
    InputStream oi = new FileInputStream(f);
    ObjectInputStream ois = new ObjectInputStream(oi);
    Object result = ois.readObject();
    ois.close();
    return result;
  }
}
