/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import org.jgap.*;

import junit.framework.*;

/**
 * Test class for FixedBinaryGene class
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class FixedBinaryGeneTest
    extends TestCase {

/*@todo implement testcases by modifying the below tests copied from BooleanGene*/

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public FixedBinaryGeneTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(FixedBinaryGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    //following should be possible without exception
    Gene gene = new FixedBinaryGene(1);
    gene = new FixedBinaryGene(10);
    gene = new FixedBinaryGene(1000);
    gene = new FixedBinaryGene(100000);
  }

  public void testConstruct_1() {
    try {
      Gene gene = new FixedBinaryGene(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() {
    try {
      Gene gene = new FixedBinaryGene( -5);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testToString_0() {
    Gene gene = new FixedBinaryGene(1);
    gene.setAllele(new int[] {1});
    assertEquals("[1]", gene.toString());
  }

  public void testToString_1() {
    Gene gene = new FixedBinaryGene(3);
    gene.setAllele(new int[] {1, 0, 1});
    assertEquals("[1,0,1]", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new FixedBinaryGene(1);
    int[] value = new int[] {
        0};
    gene.setAllele(value);
    assertEquals(value, gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new FixedBinaryGene(2);
    try {
      gene.setAllele(new Integer(100));
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testEquals_0() {
    Gene gene1 = new BooleanGene();
    Gene gene2 = new BooleanGene();
    assertTrue(gene1.equals(gene2));
  }

  public void testEquals_1() {
    Gene gene1 = new BooleanGene();
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(false));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_3() {
    Gene gene1 = new BooleanGene();
    assertFalse(gene1.equals(new IntegerGene()));
  }

  public void testIntValues_0() {
    /**@todo implement*/

    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    assertEquals(true, gene1.booleanValue());
  }

  public void testIntValues_1() {
    /**@todo implement*/

    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(false));
    assertEquals(false, gene1.booleanValue());
  }

  public void testIntValues_2() {
    /**@todo implement*/

    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(null);
    try {
      assertEquals(true, gene1.booleanValue());
      fail();
    }
    catch (NullPointerException nullex) {
      ; //this is OK
    }
  }

  public void testSetAllele_0() {
    Gene gene1 = new FixedBinaryGene(1);
    try {
      gene1.setAllele(null);
      fail();
    } catch (IllegalArgumentException iex) {
      ;//this is OK
    }
  }

  public void testSetAllele_1() {
    Gene gene1 = new FixedBinaryGene(1);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to int values, no exception should occur
   */
  public void testSetAllele_2() {
    Gene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 0, 1});
  }

  public void testCompareTo_0() {
    /**@todo implement*/
  }

  public void testCompareTo_1() {
    /**@todo implement*/
           Gene gene1 = new BooleanGene();
        Gene gene2 = new BooleanGene();
        assertEquals(0, gene1.compareTo(gene2));
        assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_2() {
    /**@todo implement*/
        Gene gene1 = new BooleanGene();
        gene1.setAllele(new Boolean(true));
        Gene gene2 = new BooleanGene();
        gene2.setAllele(new Boolean(true));
        assertEquals(0, gene1.compareTo(gene2));
        assertEquals(0, gene2.compareTo(gene1));
        gene1.setAllele(new Boolean(false));
        gene2.setAllele(new Boolean(false));
        assertEquals(0, gene1.compareTo(gene2));
        assertEquals(0, gene2.compareTo(gene1));
  }

  public void testCompareTo_3() {
    /**@todo implement*/
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(false));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  public void testApplyMutation_0() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, 0.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_1() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(1, 0.000001d); //index 1 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_2() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(333, -0.000001d); //index 333 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_3() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_4() {
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, -2.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_5() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_6() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(0, 2.0d);
    assertEquals(true, gene.booleanValue());
  }

  public void testApplyMutation_7() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(0, -1.0d);
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_8() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(22, -0.5d); //22 should be ignored
    assertEquals(false, gene.booleanValue());
  }

  public void testApplyMutation_9() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    gene.applyMutation(22, 0.5d); //22 should be ignored
    assertEquals(true, gene.booleanValue());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_0() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation(null);
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_1()
      throws Exception {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("null");
    assertEquals(null, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_2()
      throws Exception {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("true");
    assertEquals(Boolean.TRUE, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_3()
      throws Exception {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setValueFromPersistentRepresentation("false");
    assertEquals(Boolean.FALSE, gene.getAllele());
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_4() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("True");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_5() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("False");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_6() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  public void testGetPersistentRepresentation_0() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    String s = gene.getPersistentRepresentation();
    assertEquals("true", s);
  }

  public void testGetPersistentRepresentation_1() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    String s = gene.getPersistentRepresentation();
    assertEquals("false", s);
  }

  public void testGetPersistentRepresentation_2() {
    /**@todo implement*/
    BooleanGene gene = new BooleanGene();
    String s = gene.getPersistentRepresentation();
    assertEquals("null", s);
  }
}
