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

import java.util.*;
import java.io.*;
import junit.framework.*;
import junitx.util.*;
import org.jgap.impl.*;

/**
 * Abstract test case for all JGAP test cases providing a common infrastructure.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class JGAPTestCase
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.21 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  protected static final double DELTA = 0.0000001;

  public final static PrivateAccessor privateAccessor = null;
  public Configuration conf;

  public JGAPTestCase(String a_name) {
    super(a_name);
  }

  public JGAPTestCase() {
    super();
  }

  public void setUp() {
    Genotype.setStaticConfiguration(null);
    // reset property --> use JGAPFactory
    System.setProperty(Configuration.PROPERTY_JGAPFACTORY_CLASS, "");
    conf.resetProperty(conf.PROPERTY_FITEVAL_INST);
    conf.resetProperty(conf.PROPERTY_EVENT_MGR_INST);
    conf = new DefaultConfiguration();
  }

  /**
   *
   * @param a_list1 first list of chromosomes
   * @param a_list2 second list of chromosomes
   * @return true lists of chromosomes are equal
   *
   * @author Klaus Meffert
   */
  public static boolean isChromosomesEqual(IChromosome[] a_list1,
                                           IChromosome[] a_list2) {
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
          IChromosome c1 = (IChromosome) a_list1[i];
          IChromosome c2 = (IChromosome) a_list2[i];
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
     * @since 2.0
     */
    protected double evaluate(IChromosome a_subject) {
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

  public static void assertInList(final List a_list, Object a_object) {
    if (a_list.contains(a_object)) {
      a_list.remove(a_object);
    }
    else {
      // Because only source code is browsed (also non-compilable code!),
      // there is no disctinction between class java.lang.X and class X
      if (a_list.contains("java.lang." + a_object)) {
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
    // Serialize object to a file.
    // ---------------------------
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

  /**
   * Retrieves a nested (private) field, that is field2 from "field1.field2".
   *
   * @param a_instance the instance the parent field is located in
   * @param a_parentFieldName the name of the parent field (case sensitive!)
   * @param a_childFieldName the name of the child field (case sensitive!)
   * @throws NoSuchFieldException
   * @return the value of the child field
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public Object getNestedField(Object a_instance, String a_parentFieldName,
                               String a_childFieldName)
      throws NoSuchFieldException {
    Object parentField = privateAccessor.getField(a_instance, a_parentFieldName);
    Object childField = privateAccessor.getField(parentField, a_childFieldName);
    return childField;
  }

  /**
   * Sets a nested (private) field, that is field2 from "field1.field2".
   *
   * @param a_instance the instance the parent field is located in
   * @param a_parentFieldName the name of the parent field (case sensitive!)
   * @param a_childFieldName the name of the child field (case sensitive!)
   * @param a_value the value to set the child field to
   * @throws NoSuchFieldException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void setNestedField(Object a_instance, String a_parentFieldName,
                               String a_childFieldName, Object a_value)
      throws NoSuchFieldException {
    Object parentField = privateAccessor.getField(a_instance, a_parentFieldName);
    privateAccessor.setField(parentField, a_childFieldName, a_value);
  }

  /**
   * Are all chromosomes in the given population unique?
   *
   * @param a_pop the population to verify
   * @return true if all chromosomes in the population are unique
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
  public boolean uniqueChromosomes(Population a_pop) {
    // Check that all chromosomes are unique
    for(int i=0;i<a_pop.size()-1;i++) {
      IChromosome c = a_pop.getChromosome(i);
      for(int j=i+1;j<a_pop.size();j++) {
        IChromosome c2 =a_pop.getChromosome(j);
        if (c == c2) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Helper for assertion comparing two doubles with a JGAP-wide delta.
   *
   * @param a_one first double to compare
   * @param a_two second double to compare
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void assertEquals(double a_one, double a_two) {
    assertEquals(a_one, a_two, DELTA);
  }

}
