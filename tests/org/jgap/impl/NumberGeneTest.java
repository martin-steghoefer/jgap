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

import java.util.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the (abstract) NumberGene class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class NumberGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.27 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(NumberGeneTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testConstruct_0()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    //following should be possible without exception
    gene.setAllele(new Integer(101));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testToString_0()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    gene.setAllele(new Integer(47));
    assertEquals("47, " + BaseGene.S_APPLICATION_DATA + ":null",
                 gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testToString_1()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    gene.setAllele(new Integer(102));
    int indexComma = gene.toString().indexOf(',');
    int toString = Integer.parseInt(gene.toString().substring(0, indexComma));
    assertTrue(toString >= 1 && toString <= 100);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_0()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    gene.setAllele(new Integer(33));
    assertEquals(new Integer(33), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_1()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    gene.setAllele(new Integer(1));
    assertEquals(new Integer(1), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testGetAllele_2()
      throws Exception {
    Gene gene = new NumberGeneImpl(conf, 1, 100);
    gene.setAllele(new Integer(100));
    assertEquals(new Integer(100), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_0()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    Gene gene2 = new NumberGeneImpl(conf, 1, 100);
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_1()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    gene1.setAllele(new Integer(43));
    Gene gene2 = new NumberGeneImpl(conf, 1, 100);
    gene2.setAllele(new Integer(43));
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testHashCode_2()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    gene1.setAllele(new Integer(55));
    Gene gene2 = new NumberGeneImpl(conf, 1, 100);
    gene1.setAllele(new Integer(43));
    assertFalse(gene1.hashCode() == gene2.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_0()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    Gene gene2 = new NumberGeneImpl(conf, 1, 100);
    assertEquals(gene1, gene2);
    assertEquals(gene2, gene1);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_1()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    assertFalse(gene1.equals(null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_2()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    assertFalse(gene1.equals(new BooleanGene(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_3()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    assertFalse(gene1.equals(new Vector()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testEquals_4()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    Gene gene2 = new NumberGeneImpl(conf, 1, 99);
    assertEquals(gene1, gene2);
    assertEquals(gene2, gene1);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_5()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 100);
    gene1.setAllele(new Integer(2));
    Gene gene2 = new NumberGeneImpl(conf, 1, 99);
    gene1.setAllele(new Integer(3));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Set Allele to null, no exception should occur
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSetAllele_0()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setAllele(null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testSetAllele_1()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
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
   * @since 2.2
   */
  public void testSetAllele_2()
      throws Exception {
    NumberGene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    }
    );
    gene1.setAllele("22");
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_3()
      throws Exception {
    NumberGene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
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
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
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
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = gene1.newGene();
    assertEquals(0, gene1.compareTo(gene1));
    assertEquals(1, gene1.compareTo(gene2));
    gene2.setAllele(new Integer(4711));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_1()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = new DoubleGene(conf);
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
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setAllele(new Integer(4711));
    Gene gene2 = new DoubleGene(conf);
    gene2.setAllele(new Double(4711.0d));
    try {
      assertEquals(0, gene1.compareTo(gene2));
      fail();
    }
    catch (ClassCastException cex) {
      ; //this is OK
    }
  }

  /**
   * Using application data.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_3()
      throws Exception {
    Gene gene1 = new NumberGeneImpl(conf, 1, 10000);
    gene1.setAllele(new Integer(4711));
    gene1.setApplicationData(new Integer(2));
    gene1.setCompareApplicationData(true);
    Gene gene2 = gene1.newGene();
    gene2.setAllele(new Integer(4711));
    gene2.setApplicationData(null);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene2.setCompareApplicationData(true);
    assertEquals( -1, gene2.compareTo(gene1));
    gene2.setApplicationData(new Integer(3));
    assertEquals(1, gene2.compareTo(gene1));
    assertEquals( -1, gene1.compareTo(gene2));
  }

  public void testSetConstraintChecker_0()
      throws Exception {
    NumberGene gene1 = new NumberGeneImpl(conf, 1, 3);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    }
    );
    assertNotNull(gene1.getConstraintChecker());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGene gene = new NumberGeneImpl(conf, 0, 1);
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1()
      throws Exception {
    BaseGene gene = new NumberGeneImpl(conf, 2, 4);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  /**
   * Test implementation of NumberGene, based on IntegerGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private class NumberGeneImpl
      extends NumberGene {
    protected final static long INTEGER_RANGE = (long) Integer.MAX_VALUE
        - (long) Integer.MIN_VALUE;

    private int m_upperBounds;

    private int m_lowerBounds;

    private long m_boundsUnitsToIntegerUnits;

    public NumberGeneImpl(final Configuration a_conf, int a_lowerBounds,
                          int a_upperBounds)
        throws InvalidConfigurationException {
      super(a_conf);
      m_lowerBounds = a_lowerBounds;
      m_upperBounds = a_upperBounds;
      calculateBoundsUnitsToIntegerUnitsRatio();
    }

    protected Gene newGeneInternal() {
      try {
        return new NumberGeneImpl(getConfiguration(), m_lowerBounds,
                                  m_upperBounds);
      }
      catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }

    public String getPersistentRepresentation() {
      return toString() + PERSISTENT_FIELD_DELIMITER + m_lowerBounds
          + PERSISTENT_FIELD_DELIMITER + m_upperBounds;
    }

    public void setValueFromPersistentRepresentation(String a_representation) {
      // not implemented here!
      // ---------------------
    }

    public void setToRandomValue(RandomGenerator a_numberGenerator) {
      // not implemented here!
      // ---------------------
    }

    public void applyMutation(int a_index, double a_percentage) {
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
      if (getAllele() != null) {
        Integer i_value = ( (Integer) getAllele());
        // If the value exceeds either the upper or lower bounds, then
        // map the value to within the legal range. To do this, we basically
        // calculate the distance between the value and the integer min,
        // determine how many bounds units that represents, and then add
        // that number of units to the upper bound.
        // -----------------------------------------------------------------
        if (i_value.intValue() > m_upperBounds
            || i_value.intValue() < m_lowerBounds) {
          long differenceFromIntMin = (long) Integer.MIN_VALUE
              + (long) i_value.intValue();
          int differenceFromBoundsMin = (int) (differenceFromIntMin
                                               / m_boundsUnitsToIntegerUnits);
          setAllele(new Integer(m_upperBounds + differenceFromBoundsMin));
        }
      }
    }

    protected int compareToNative(Object a_o1, Object a_o2) {
      return ( (Integer) a_o1).compareTo( (Integer) a_o2);
    }

    public boolean equals(Object a_other) {
      try {
        return compareTo(a_other) == 0;
      }
      catch (ClassCastException e) {
        return false;
      }
    }
  }
}
