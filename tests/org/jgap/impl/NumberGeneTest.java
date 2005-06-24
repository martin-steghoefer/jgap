/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the (abstract) NumberGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class NumberGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(NumberGeneTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testConstruct_0() {
    Gene gene = new NumberGeneImpl(1, 100);
    //following should be possible without exception
    gene.setAllele(new Integer(101));
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testToString_0() {
    Gene gene = new NumberGeneImpl(1, 100);
    gene.setAllele(new Integer(47));
    assertEquals("47, "+BaseGene.S_APPLICATION_DATA+":null", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testToString_1() {
    Gene gene = new NumberGeneImpl(1, 100);
    gene.setAllele(new Integer(102));
    int indexComma = gene.toString().indexOf(',');
    int toString = Integer.parseInt(gene.toString().substring(0,indexComma));
    assertTrue(toString >= 1 && toString <= 100);
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_0() {
    Gene gene = new NumberGeneImpl(1, 100);
    gene.setAllele(new Integer(33));
    assertEquals(new Integer(33), gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_1() {
    Gene gene = new NumberGeneImpl(1, 100);
    gene.setAllele(new Integer(1));
    assertEquals(new Integer(1), gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_2() {
    Gene gene = new NumberGeneImpl(1, 100);
    gene.setAllele(new Integer(100));
    assertEquals(new Integer(100), gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_0() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    Gene gene2 = new NumberGeneImpl(1, 100);
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_1() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    gene1.setAllele(new Integer(43));
    Gene gene2 = new NumberGeneImpl(1, 100);
    gene2.setAllele(new Integer(43));
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_2() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    gene1.setAllele(new Integer(55));
    Gene gene2 = new NumberGeneImpl(1, 100);
    gene1.setAllele(new Integer(43));
    assertFalse(gene1.hashCode() == gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_0() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    Gene gene2 = new NumberGeneImpl(1, 100);
    assertEquals(gene1, gene2);
    assertEquals(gene2, gene1);
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_1() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    assertFalse(gene1.equals(null));
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_2() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    assertFalse(gene1.equals(new BooleanGene()));
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_3() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    assertFalse(gene1.equals(new Vector()));
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_4() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    Gene gene2 = new NumberGeneImpl(1, 99);
    assertEquals(gene1, gene2);
    assertEquals(gene2, gene1);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_5() {
    Gene gene1 = new NumberGeneImpl(1, 100);
    gene1.setAllele(new Integer(2));
    Gene gene2 = new NumberGeneImpl(1, 99);
    gene1.setAllele(new Integer(3));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Set Allele to null, no exception should occur
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSetAllele_0() {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setAllele(null);
  }

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSetAllele_1() {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_2() {
    NumberGene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
        return false;
      }
    }
    );
    gene1.setAllele("22");
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_3() {
    NumberGene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
        return true;
      }
    }
    );
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testNewGene_0()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setAllele(new Integer(4711));
    Integer lower1 = (Integer) privateAccessor.getField(gene1,
        "m_lowerBounds");
    Integer upper1 = (Integer) privateAccessor.getField(gene1,
        "m_upperBounds");
    Gene gene2 = gene1.newGene();
    Integer lower2 = (Integer) privateAccessor.getField(gene2,
        "m_lowerBounds");
    Integer upper2 = (Integer) privateAccessor.getField(gene2,
        "m_upperBounds");
    assertEquals(lower1, lower2);
    assertEquals(upper1, upper2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_0()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = gene1.newGene();
    assertEquals(0, gene1.compareTo(gene1));
    assertEquals(1, gene1.compareTo(gene2));
    gene2.setAllele(new Integer(4711));
    assertEquals(0, gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_1()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = new DoubleGene();
    try {
      assertEquals(0, gene1.compareTo(gene2));
      fail();
    }
    catch (ClassCastException cex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_2()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = new DoubleGene();
    gene2.setAllele(new Double(4711.0d));
    try {
      assertEquals(0, gene1.compareTo(gene2));
      fail();
    }
    catch (ClassCastException cex) {
      ; //this is OK
    }
  }

  public void testSetConstraintChecker_0() {
    NumberGene gene1 = new NumberGeneImpl(1, 3);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
        return false;
      }
    }
    );
    assertNotNull(gene1.getConstraintChecker());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0() {
    BaseGene gene = new NumberGeneImpl(0, 1);
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1() {
    BaseGene gene = new NumberGeneImpl(2, 4);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  /**
   * Test implementation of NumberGene, based on IntegerGene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private class NumberGeneImpl
      extends NumberGene {
    protected final static long INTEGER_RANGE = (long) Integer.MAX_VALUE -
        (long) Integer.MIN_VALUE;

    protected int m_upperBounds;

    protected int m_lowerBounds;

    protected long m_boundsUnitsToIntegerUnits;

    public NumberGeneImpl(int a_lowerBounds, int a_upperBounds) {
      m_lowerBounds = a_lowerBounds;
      m_upperBounds = a_upperBounds;
      calculateBoundsUnitsToIntegerUnitsRatio();
    }

    public Gene newGene() {
      return new NumberGeneImpl(m_lowerBounds, m_upperBounds);
    }

    public String getPersistentRepresentation()
        throws
        UnsupportedOperationException {
      return toString() + PERSISTENT_FIELD_DELIMITER + m_lowerBounds +
          PERSISTENT_FIELD_DELIMITER + m_upperBounds;
    }

    public void setValueFromPersistentRepresentation(String
        a_representation)
        throws UnsupportedRepresentationException {
      // not implemented here!
      // ---------------------
    }

    public void setToRandomValue(RandomGenerator a_numberGenerator) {
      // not implemented here!
      // ---------------------
    }

    public void applyMutation(int index, double a_percentage) {
      // not implemented here!
      // ---------------------
    }

    protected void calculateBoundsUnitsToIntegerUnitsRatio() {
      int divisor = m_upperBounds - m_lowerBounds + 1;
      if (divisor == 0) {
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE;
      }
      else {
        m_boundsUnitsToIntegerUnits = INTEGER_RANGE / divisor;
      }
    }

    protected void mapValueToWithinBounds() {
      if (m_value != null) {
        Integer i_value = ( (Integer) m_value);
        // If the value exceeds either the upper or lower bounds, then
        // map the value to within the legal range. To do this, we basically
        // calculate the distance between the value and the integer min,
        // determine how many bounds units that represents, and then add
        // that number of units to the upper bound.
        // -----------------------------------------------------------------
        if (i_value.intValue() > m_upperBounds ||
            i_value.intValue() < m_lowerBounds) {
          long differenceFromIntMin = (long) Integer.MIN_VALUE +
              (long) i_value.intValue();
          int differenceFromBoundsMin =
              (int) (differenceFromIntMin /
                     m_boundsUnitsToIntegerUnits);
          m_value =
              new Integer(m_upperBounds + differenceFromBoundsMin);
        }
      }
    }

    protected int compareToNative(Object o1, Object o2) {
      return ( (Integer) o1).compareTo(o2);
    }

    public boolean equals(Object other) {
      try {
        return compareTo(other) == 0;
      }
      catch (ClassCastException e) {
        return false;
      }
    }
  }
}
