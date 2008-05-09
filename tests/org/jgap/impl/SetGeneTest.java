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

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the SetGene class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class SetGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(SetGeneTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    SetGene gene = new SetGene(conf);
    assertSame(conf, gene.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1()
      throws Exception {
    SetGene gene = new SetGene(conf);
    assertEquals(null, gene.getInternalValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString_0()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.addAllele("testValue1");
    gene.addAllele("testValue2");
    gene.setAllele("testValue1");
    assertEquals("testValue1, Application data:null", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString_1()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.setApplicationData("hallo");
    gene.addAllele("testValue1");
    gene.addAllele("testValue2");
    gene.setAllele("testValue1");
    assertEquals("testValue1, Application data:hallo", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetAllele_0()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.addAllele("testValue1");
    gene.addAllele("testValue2");
    gene.setAllele("testValue2");
    assertEquals("testValue2", gene.getAllele());
  }

  public void testEquals_0()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    Gene gene2 = new SetGene(conf);
    assertTrue(gene1.equals(gene2));
  }

  public void testEquals_1()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2()
      throws Exception {
    SetGene gene1 = new SetGene(conf);
    gene1.addAllele(new Double(2.3d));
    gene1.setAllele(new Double(2.3d));
    SetGene gene2 = new SetGene(conf);
    gene2.addAllele(new Double(2.4d));
    gene2.setAllele(new Double(2.4d));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_3()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    assertFalse(gene1.equals(new IntegerGene(conf)));
  }

  public void testEquals_4()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    Gene gene2 = new IntegerGene(conf);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_5()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    Gene gene2 = new FixedBinaryGene(conf, 1);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetAllele_0()
      throws Exception {
    SetGene gene1 = new SetGene(conf);
    try {
      gene1.setAllele(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetAllele_1()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.addAllele("testValue1");
    gene.addAllele("testValue2");
    try {
      gene.setAllele("testValue3");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetAllele_2()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.addAllele("testValue1");
    gene.addAllele("testValue2");
    gene.addAllele("testValue3");
    gene.setAllele("testValue2");
  }

  public void testCompareTo_0()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    assertEquals(1, gene1.compareTo(null));
  }

  public void testCompareTo_1()
      throws Exception {
    Gene gene1 = new SetGene(conf);
    Gene gene2 = new SetGene(conf);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_2()
      throws Exception {
    SetGene gene1 = new SetGene(conf);
    gene1.addAllele(Boolean.valueOf(true));
    gene1.addAllele(Boolean.valueOf(false));
    gene1.setAllele(Boolean.valueOf(true));
    SetGene gene2 = new SetGene(conf);
    gene2.addAllele(Boolean.valueOf(true));
    gene2.setAllele(Boolean.valueOf(true));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setAllele(Boolean.valueOf(false));
    gene2.addAllele(Boolean.valueOf(false));
    gene2.setAllele(Boolean.valueOf(false));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_3()
      throws Exception {
    SetGene gene1 = new SetGene(conf);
    gene1.addAllele(Boolean.valueOf(true));
    gene1.setAllele(Boolean.valueOf(true));
    SetGene gene2 = new SetGene(conf);
    gene2.addAllele(Boolean.valueOf(false));
    gene2.setAllele(Boolean.valueOf(false));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testApplyMutation_0()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0));
    SetGene gene = new SetGene(conf);
    gene.addAllele(Boolean.valueOf(true));
    gene.addAllele(Boolean.valueOf(false));
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, 0.0d);
    assertEquals(false, ((Boolean)gene.getAllele()).booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testApplyMutation_1()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(1));
    SetGene gene = new SetGene(conf);
    gene.addAllele(Boolean.valueOf(true));
    gene.addAllele(Boolean.valueOf(false));
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(1, 0.000001d); //index 1 should be ignored
    assertEquals(true, ((Boolean)gene.getAllele()).booleanValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testApplyMutation_2()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(2));
    SetGene gene = new SetGene(conf);
    gene.addAllele(Boolean.valueOf(true));
    gene.addAllele(Boolean.valueOf(false));
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(333, -0.000001d); //index 333 should be ignored
    assertEquals(false, ((Boolean)gene.getAllele()).booleanValue());
  }

  /**@todo from here on: adapt tests to SetGene*/

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_3()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_4()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, -2.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_5()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_6()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_7()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_8()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(22, -0.5d); //22 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testApplyMutation_9()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    gene.applyMutation(22, 0.5d); //22 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testApplyMutation_10()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.applyMutation(0, 0.0d);
    assertEquals(false, gene.booleanValue());
  }

  /**
   * Should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetValueFromPersistentRepresentation_0()
      throws Exception {
    SetGene gene = new SetGene(conf);
    gene.setValueFromPersistentRepresentation(null);
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
   *
   * @author Klaus Meffert
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_4()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("True");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_5()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("False");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_6()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testGetPersistentRepresentation_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(true));
    String s = gene.getPersistentRepresentation();
    assertEquals("true", s);
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testGetPersistentRepresentation_1()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    gene.setAllele(Boolean.valueOf(false));
    String s = gene.getPersistentRepresentation();
    assertEquals("false", s);
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testGetPersistentRepresentation_2()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    String s = gene.getPersistentRepresentation();
    assertEquals("null", s);
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testHashCode_0()
      throws Exception {
    BooleanGene gene = new BooleanGene(conf);
    assertEquals( -2, gene.hashCode());
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.4
   * @throws Exception
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGene gene = new BooleanGene(conf);
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.4
   * @throws Exception
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
