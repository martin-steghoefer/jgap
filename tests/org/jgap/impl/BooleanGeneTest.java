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
 * Tests the BooleanGene class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BooleanGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.22 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BooleanGeneTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    //following should be possible without exception
    gene.setAllele(Boolean.valueOf(true));
    gene.setAllele(Boolean.valueOf(false));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_1()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf, true);
    assertEquals(true, gene.booleanValue());
    gene = new BooleanGene(conf, false);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_2()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf, Boolean.valueOf(true));
    assertEquals(true, gene.booleanValue());
    gene = new BooleanGene(conf, Boolean.valueOf(false));
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_3()
      throws Exception {
    try {
      new BooleanGene(conf, null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_4()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    Gene gene = new BooleanGene();
    assertSame(conf, gene.getConfiguration());
  }

  public void testToString_0()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    assertEquals("BooleanGene=true", gene.toString());
  }

  public void testToString_1()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    assertEquals("BooleanGene=false", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_2()
      throws Exception {
    Gene gene = new BooleanGene(conf, true);
    assertEquals("BooleanGene=true", gene.toString());
  }

  public void testGetAllele_0()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    assertEquals(Boolean.valueOf(true), gene.getAllele());
  }

  public void testGetAllele_1()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    assertEquals(Boolean.valueOf(false), gene.getAllele());
  }

  public void testGetAllele_2()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    try {
      gene.setAllele(new Integer(100));
      fail();
    } catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testEquals_0()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new BooleanGene(conf);
    assertTrue(gene1.equals(gene2));
  }

  public void testEquals_1()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.valueOf(false));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_3()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    assertFalse(gene1.equals(new IntegerGene(conf)));
  }

  public void testEquals_4()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new IntegerGene(conf);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_5()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new FixedBinaryGene(conf, 1);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testBooleanValue_0()
      throws Exception {
    BooleanGene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
    assertEquals(true, gene1.booleanValue());
  }

  public void testBooleanValue_1()
      throws Exception {
    BooleanGene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(false));
    assertEquals(false, gene1.booleanValue());
  }

  public void testBooleanValue_2()
      throws Exception {
    BooleanGene gene1 = new BooleanGene(conf);
    gene1.setAllele(null);
    try {
      assertEquals(true, gene1.booleanValue());
      fail();
    } catch (NullPointerException nullex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to null, no exception should occur.
   *
   * @throws Exception
   */
  public void testSetAllele_0()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(null);
  }

  public void testSetAllele_1()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    try {
      gene1.setAllele("22");
      fail();
    } catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to boolean value, no exception should occur.
   *
   * @throws Exception
   */
  public void testSetAllele_2()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
  }

  /**
   * Set Allele to boolean value, no exception should occur.
   *
   * @throws Exception
   */
  public void testSetAllele_3()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(false));
  }

  public void testCompareTo_0()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    assertEquals(1, gene1.compareTo(null));
  }

  public void testCompareTo_1()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new BooleanGene(conf);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_2()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.valueOf(true));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setAllele(Boolean.valueOf(false));
    gene2.setAllele(Boolean.valueOf(false));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_3()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.valueOf(false));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_4()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.valueOf(true));
    Gene gene2 = new BooleanGene(conf);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_5()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new BooleanGene(conf);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_6()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new BooleanGene(conf);
    gene1.setCompareApplicationData(true);
    gene2.setCompareApplicationData(false);
    List app1 = new Vector();
    gene1.setApplicationData(app1);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_6_2()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.TRUE);
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.FALSE);
    gene1.setCompareApplicationData(true);
    gene2.setCompareApplicationData(true);
    List app1 = new Vector();
    gene1.setApplicationData(app1);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_6_3()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    gene1.setAllele(Boolean.FALSE);
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.FALSE);
    gene1.setCompareApplicationData(true);
    gene2.setCompareApplicationData(true);
    List app1 = new Vector();
    gene1.setApplicationData(app1);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_7()
      throws Exception {
    Gene gene1 = new BooleanGene(conf);
    Gene gene2 = new BooleanGene(conf);
    gene2.setAllele(Boolean.TRUE);
    assertEquals(-1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, 0.0d);
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_1()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(1, 0.000001d); //index 1 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_2()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(333, -0.000001d); //index 333 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_3()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_4()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, -2.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_5()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_6()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_7()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_8()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(22, -0.5d); //22 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_9()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(22, 0.5d); //22 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_10()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.applyMutation(0, 0.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation(null);
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_1()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setValueFromPersistentRepresentation("null");
    assertEquals(null, gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_2()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setValueFromPersistentRepresentation("true");
    assertEquals(Boolean.TRUE, gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_3()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setValueFromPersistentRepresentation("false");
    assertEquals(Boolean.FALSE, gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_4()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("True");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_5()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("False");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_6()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetPersistentRepresentation_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    String s = gene.getPersistentRepresentation();
    assertEquals("true", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetPersistentRepresentation_1()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    String s = gene.getPersistentRepresentation();
    assertEquals("false", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetPersistentRepresentation_2()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    String s = gene.getPersistentRepresentation();
    assertEquals("null", s);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testHashCode_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    assertEquals( -2, gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGene gene = new BooleanGene(conf);
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
    BaseGene gene = new BooleanGene(conf);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }
}
