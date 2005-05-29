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

import org.jgap.*;
import junit.framework.*;

/**
 * Test class for BaseGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BaseGeneTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(BaseGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    //construction should be possible without exception
    Gene gene = new BaseGeneImpl();
  }

  public void testToString_0() {
  }

  public void testGetAllele_0() {
  }

  public void testEquals_0() {
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testHashCode_0() {
    BaseGeneImpl gene = new BaseGeneImpl();
    assertEquals( -2, gene.hashCode());
  }

  class BaseGeneImpl
      extends BaseGene {
    public int compareTo(Object o) {
      return 0;
    }

    public Gene newGene() {
      return null;
    }

    public void setAllele(Object a_newValue) {
    }

    public Object getAllele() {
      return null;
    }

    public String getPersistentRepresentation()
        throws UnsupportedOperationException {
      return null;
    }

    public void setValueFromPersistentRepresentation(String a_representation)
        throws UnsupportedOperationException,
        UnsupportedRepresentationException {
    }

    public void setToRandomValue(RandomGenerator a_numberGenerator) {

    }

    public   void applyMutation(int index, double a_percentage) {

    }

    protected Object getInternalValue() {
      return null;
    }
  }
}
