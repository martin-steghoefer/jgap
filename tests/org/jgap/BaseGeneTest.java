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

import junit.framework.*;

/**
 * Tests the BaseGene class
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class BaseGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BaseGeneTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testConstruct_0() {
    Gene gene = new BaseGeneImpl();
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_0() {
    Gene gene = new BaseGeneImpl();
    assertEquals("null", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_1() {
    Gene gene = new BaseGeneImpl();
    gene.setAllele(new Integer(98));
    assertEquals("98", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testGetAllele_0() {
    Gene gene = new BaseGeneImpl();
    gene.setAllele(new Double(75));
    assertEquals(new Double(75), gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSize_0() {
    Gene gene = new BaseGeneImpl();
    assertEquals(1, gene.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_0() {
    BaseGeneImpl gene = new BaseGeneImpl();
    gene.m_compareTo_result = 0;
    assertEquals(gene.m_compareTo_result == 0, gene.equals(null));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(gene));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(new Integer(2)));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_1() {
    BaseGeneImpl gene = new BaseGeneImpl();
    gene.m_compareTo_result = -1;
    assertEquals(gene.m_compareTo_result == 0, gene.equals(null));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(gene));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(new Integer(2)));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_2() {
    BaseGeneImpl gene = new BaseGeneImpl();
    gene.m_compareTo_result = 1;
    assertEquals(gene.m_compareTo_result == 0, gene.equals(null));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(gene));
    assertEquals(gene.m_compareTo_result == 0, gene.equals(new Integer(2)));
  }

  /**
   * Following should be possible without exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCleanup_0() {
    Gene gene = new BaseGeneImpl();
    gene.setAllele(new Double(75));
    gene.cleanup();
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testHashCode_0() {
    BaseGeneImpl gene = new BaseGeneImpl();
    assertEquals( -79, gene.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testHashCode_1() {
    BaseGeneImpl gene = new BaseGeneImpl();
    gene.setAllele(new Double(1.5d));
    assertEquals(new Double(1.5d).hashCode(), gene.hashCode());
  }


  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testSetEnergy_0() {
    BaseGeneImpl gene = new BaseGeneImpl();
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testSetEnergy_1() {
    BaseGeneImpl gene = new BaseGeneImpl();
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy(-55.8);
    assertEquals(-55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  /**
   * Test implementation of Gene interface extending BaseGene class
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  class BaseGeneImpl
      extends BaseGene {
    private Object m_allele;

    public int m_compareTo_result;

    public int compareTo(Object o) {
      return m_compareTo_result;
    }

    public Gene newGene() {
      return null;
    }

    public void setAllele(Object a_newValue) {
      m_allele = a_newValue;
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

    public void applyMutation(int index, double a_percentage) {
    }

    protected Object getInternalValue() {
      return m_allele;
    }
  }
}
