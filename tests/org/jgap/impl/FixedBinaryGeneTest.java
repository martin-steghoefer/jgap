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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the FixedBinaryGene class
 *
 * @author Klaus Meffert
 * @author vamsi
 * @since 2.0
 */
public class FixedBinaryGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.27 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(FixedBinaryGeneTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testConstruct_0() {
    // following should be possible without exception
    new FixedBinaryGene(1);
    new FixedBinaryGene(10);
    new FixedBinaryGene(1000);
    new FixedBinaryGene(100000);
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
    try {
      new FixedBinaryGene(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_2() {
    try {
      new FixedBinaryGene( -5);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testConstruct_3() {
    int i = 0;
    FixedBinaryGene gene = new FixedBinaryGene(5);
    for (i = 0; i < 4; i++) {
      //assert that we have
      assertFalse(gene.getBit(i));
    }
    assertEquals("FixedBinaryGene[0,0,0,0,0]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testConstruct_4() {
    FixedBinaryGene gene = new FixedBinaryGene(6);
    assertEquals(1, gene.size());
    assertEquals(1, (gene.getValue()).length);
    assertEquals("FixedBinaryGene[0,0,0,0,0,0]", gene.toString());
  }

  /**
   * Buffer allocation test case.
   * @author vamsi
   */
  public void testConstruct_5() {
    FixedBinaryGene gene;
    gene = new FixedBinaryGene(32);
    assertEquals(1, gene.size());
    gene = new FixedBinaryGene(81);
    assertEquals(3, gene.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_6() {
    FixedBinaryGene gene1 = new FixedBinaryGene(1);
    new FixedBinaryGene(gene1);
  }

  /**
   * @author vamsi
   */
  public void testToString_0() {
    Gene gene = new FixedBinaryGene(1);
    gene.setAllele(new int[] {1});
    assertEquals("FixedBinaryGene[1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testToString_1() {
    Gene gene = new FixedBinaryGene(3);
    gene.setAllele(new int[] {1, 0, 1});
    assertEquals("FixedBinaryGene[1,0,1]", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testToString_2() {
    Gene gene = new FixedBinaryGene(3);
    assertEquals("FixedBinaryGene[0,0,0]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testGetAllele_0() {
    Gene gene = new FixedBinaryGene(1);
    int[] value = new int[] {
        0};
    gene.setAllele(value);
    assertEquals(value.length, ( (int[]) gene.getAllele()).length);
    for (int i = 0; i < value.length; i++) {
      assertEquals(value[i], ( (int[]) gene.getAllele())[i]);
    }
  }

  /**
   * @author vamsi
   */
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

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testGetAllele_2() {
    Gene gene = new FixedBinaryGene(1);
    int[] value = new int[] {
        1};
    gene.setAllele(value);
    assertEquals(value.length, ( (int[]) gene.getAllele()).length);
    for (int i = 0; i < value.length; i++) {
      assertEquals(value[i], ( (int[]) gene.getAllele())[i]);
    }
  }

  /**
   * @author vamsi
   */
  public void testEquals_0() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new FixedBinaryGene(1);
    assertTrue(gene1.equals(gene2));
  }

  /**
   * @author vamsi
   */
  public void testEquals_1() {
    Gene gene1 = new FixedBinaryGene(1);
    assertFalse(gene1.equals(null));
  }

  /**
   * @author vamsi
   */
  public void testEquals_2() {
    Gene gene1 = new FixedBinaryGene(2);
    gene1.setAllele(new int[] {1, 0});
    Gene gene2 = new FixedBinaryGene(2);
    gene2.setAllele(new int[] {0, 1});
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author vamsi
   */
  public void testEquals_3() {
    Gene gene1 = new FixedBinaryGene(5);
    assertFalse(gene1.equals(new IntegerGene()));
  }

  public void testEquals_4() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new IntegerGene();
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_5() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new DoubleGene();
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_6() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new BooleanGene();
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_7() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new StringGene();
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_8() {
    Gene gene1 = new FixedBinaryGene(2);
    gene1.setAllele(new int[] {0, 1});
    Gene gene2 = new FixedBinaryGene(2);
    gene2.setAllele(new int[] {0, 0});
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author vamsi
   */
  public void testIntValues_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    assertFalse(gene1.getIntValues() == null);
  }

  /**
   * @author vamsi
   */
  public void testIntValues_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(2);
    int[] values = gene1.getIntValues();
    int i;
    for (i = 0; i < values.length; i++) {
      assertEquals(0, values[i]);
    }
  }

  /**
   * @author vamsi
   */
  public void testIntValues_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 1, 0});
    assertEquals(false, gene1.getBit(0));
    assertEquals(true, gene1.getBit(1));
    assertEquals(false, gene1.getBit(2));
    assertEquals(3, gene1.getLength());
  }

  /**
   * Allele is null.
   *
   * @author vamsi
   */
  public void testSetAllele_0() {
    Gene gene1 = new FixedBinaryGene(1);
    try {
      gene1.setAllele(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Allele is of wrong type.
   *
   * @author vamsi
   */
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
   * Set Allele to int values, no exception should occur.
   *
   * @author vamsi
   */
  public void testSetAllele_2() {
    Gene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 0, 1});
  }

  /***
   * The implementation should throw an exception if the alle size is more
   * than the size of the created gene.
   * @author vamsi
   */
  public void testSetAllele_3() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    try {
      gene1.setAllele(new int[] {0, 1, 1, 1, 1, 1});
      fail();
    }
    catch (Exception e) {
      ; //this is OK
    }
  }

  /**
   * Allele contains illegal characters.
   *
   * @author vamsi
   */
  public void testSetAllele_4() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    try {
      gene1.setAllele(new int[] {0, 3, 1, 4});
      fail();
    }
    catch (Exception e) {
      ; //this is OK
    }
  }

  /**
   * Allele is of wrong length.
   *
   * @author vamsi
   */
  public void testSetAllele_5() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    try {
      gene1.setAllele(new int[] {0, 0});
      fail();
    }
    catch (Exception e) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_6() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    });
    gene1.setAllele(new int[] {0, 0, 1});
    assertFalse(gene1.getBit(0));
    assertFalse(gene1.getBit(1));
    assertFalse(gene1.getBit(2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_7() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return true;
      }
    });
    gene1.setAllele(new int[] {0, 0, 1});
    assertFalse(gene1.getBit(0));
    assertFalse(gene1.getBit(1));
    assertTrue(gene1.getBit(2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetConstraintChecker_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    assertNull(gene1.getConstraintChecker());
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    });
    assertNotNull(gene1.getConstraintChecker());
  }

  /**
   * Comparision should return 0 if same, -1 if less 1 if more
   * @author vamsi
   */
  public void testCompareTo_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    FixedBinaryGene gene2 = new FixedBinaryGene(4);
    gene1.setAllele(new int[] {1, 0, 1, 0});
    gene2.setAllele(new int[] {1, 1, 0, 1});
    assertEquals(1, gene1.compareTo(null));
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = new FixedBinaryGene(3);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {1, 1, 1});
    gene2.setAllele(new int[] {1, 1, 1});
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setAllele(new int[] {0, 0, 0});
    gene2.setAllele(new int[] {0, 0, 0});
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_3_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    BooleanGene gene2 = new BooleanGene();
    try {
      gene1.compareTo(gene2);
      fail();
    }
    catch (Exception e) {
      ; //this is OK (should compare only FixedBinaryGene's)
    }
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_3_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    try {
      gene1.compareTo(new Integer(3));
      fail();
    }
    catch (Exception e) {
      ; //this is OK (should compare only FixedBinaryGene's)
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_4() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = new FixedBinaryGene(4);
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /***
   * Apply Mutation (index,percentage). if >0 make 1(0) if <0 make 0(1)
   * @author vamsi
   */
  public void testApplyMutation_0() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 0, 1, 1});
    gene.applyMutation(0, 0.0d);
    assertEquals("FixedBinaryGene[0,0,1,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_1() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 0, 1, 0});
    gene.applyMutation(1, 0.000001d);
    assertEquals("FixedBinaryGene[0,1,1,0]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_2() {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    gene.setAllele(new int[] {1, 0, 1, 0, 1});
    try {
      //index size is greater
      gene.applyMutation(333, -0.000001d);
      fail();
    }
    catch (Exception e) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_3() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {1, 1, 0, 1});
    gene.applyMutation(0, -1.0d);
    assertEquals("FixedBinaryGene[0,1,0,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_4() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 1, 0, 1});
    gene.applyMutation(0, -2.0d);
    gene.applyMutation(3, 2.0d);
    gene.applyMutation(1, -4.0d);
    assertEquals("FixedBinaryGene[0,0,0,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_5() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {1, 1, 1, 1});
    gene.applyMutation(0, 2.0d);
    gene.applyMutation(1, 2.0d);
    gene.applyMutation(2, 2.0d);
    gene.applyMutation(3, 2.0d);
    assertEquals("FixedBinaryGene[1,1,1,1]", gene.toString());
  }

  /**
   * @author vamsi
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_0() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
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
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_1()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    try {
      gene.setValueFromPersistentRepresentation("null");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_2()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setValueFromPersistentRepresentation("[1,1,1,1]");
    assertTrue(gene.getBit(0));
    assertTrue(gene.getBit(1));
    assertTrue(gene.getBit(2));
    assertTrue(gene.getBit(3));
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_3()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setValueFromPersistentRepresentation("[0,0,0,0]");
    assertFalse(gene.getBit(0));
    assertFalse(gene.getBit(1));
    assertFalse(gene.getBit(2));
    assertFalse(gene.getBit(3));
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_4()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    gene.setValueFromPersistentRepresentation("[0,1,1,0,0]");
    assertFalse(gene.getBit(0));
    assertTrue(gene.getBit(1));
    assertTrue(gene.getBit(2));
    assertFalse(gene.getBit(3));
    assertFalse(gene.getBit(4));
  }

  /**
   * @since 2.0
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_5()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    try {
      gene.setValueFromPersistentRepresentation("[0,1,1,0]");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_6() {
    FixedBinaryGene gene = new FixedBinaryGene(1);
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testGetPersistentRepresentation_0() {
    FixedBinaryGene gene = new FixedBinaryGene(3);
    gene.setAllele(new int[] {1, 0, 1});
    String s = gene.getPersistentRepresentation();
    assertEquals("FixedBinaryGene[1,0,1]", s);
  }

  /**
   * @throws Exception
   * @author vamsi


     /**
    * @throws Exception
    * @author vamsi
    * @since 2.0
    */
   public void testGetPersistentRepresentation_1()
       throws Exception {
     FixedBinaryGene gene = new FixedBinaryGene(3);
     try {
       gene.setValueFromPersistentRepresentation(null);
       fail();
     }
     catch (UnsupportedRepresentationException uex) {
       ; //this is OK
     }
   }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testGetPersistentRepresentation_2()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(3);
    String s = gene.getPersistentRepresentation();
    assertEquals("FixedBinaryGene[0,0,0]", s);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClone_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(1);
    FixedBinaryGene gene2 = (FixedBinaryGene) gene1.clone();
    assertEquals(gene1, gene2);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testClone_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = (FixedBinaryGene) gene1.clone();
    assertEquals(gene1, gene2);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetBit_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    assertTrue(gene1.getBit(0));
    gene1.setBit(0, false);
    assertFalse(gene1.getBit(0));
    gene1.setBit(1, true);
    assertTrue(gene1.getBit(1));
    gene1.setBit(4, false);
    assertFalse(gene1.getBit(0));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetBit_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    gene1.setBit(2, 4, true);
    assertTrue(gene1.getBit(0));
    assertTrue(gene1.getBit(1));
    assertTrue(gene1.getBit(2));
    assertTrue(gene1.getBit(3));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetBit_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    try {
      gene1.setBit(2, 2);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetBit_3() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    try {
      gene1.setBit(2, -1);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetBit_4() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    try {
      gene1.setBit(2, 1, false);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetBit_5() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    FixedBinaryGene gene2 = new FixedBinaryGene(9);
    gene2.setBit(2, 6, gene1);
    assertTrue(gene2.getBit(2));
    assertTrue(gene2.getBit(3));
    assertTrue(gene2.getBit(6));
    assertFalse(gene2.getBit(0));
    assertFalse(gene2.getBit(1));
    assertFalse(gene2.getBit(4));
    assertFalse(gene2.getBit(5));
    assertFalse(gene2.getBit(7));
    assertFalse(gene2.getBit(8));
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetBit_6() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    FixedBinaryGene gene2 = new FixedBinaryGene(4);
    gene2.setBit(2, 7, gene1);
    assertTrue(gene2.getBit(2));
    assertTrue(gene2.getBit(3));
    assertTrue(gene2.getBit(6));
    assertTrue(gene2.getBit(7));
    assertFalse(gene2.getBit(0));
    assertFalse(gene2.getBit(1));
    assertFalse(gene2.getBit(4));
    assertFalse(gene2.getBit(5));
    assertFalse(gene2.getBit(8));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSubbString_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    FixedBinaryGene gene2 = gene1.substring(0, 4);
    assertEquals(5, gene2.getLength());
    assertEquals(1, gene2.size());
    assertTrue(gene2.getBit(0));
    assertTrue(gene2.getBit(1));
    assertFalse(gene2.getBit(2));
    assertFalse(gene2.getBit(3));
    assertTrue(gene2.getBit(4));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSubString_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    try {
      gene1.substring(0, 7);
      fail();
    }
    catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testFlip_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    gene1.flip(0);
    assertFalse(gene1.getBit(0));
    gene1.flip(6);
    assertFalse(gene1.getBit(6));
    gene1.flip(2);
    assertTrue(gene1.getBit(2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testFlip_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setAllele(new int[] {1, 1, 0, 0, 1, 0, 1});
    try {
      gene1.flip(7);
      fail();
    }
    catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    try {
      gene1.setToRandomValue(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setToRandomValue(new StockRandomGenerator());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setToRandomValue(new RandomGeneratorForTest(false));
    for (int i = 0; i < 7; i++) {
      assertFalse(gene1.getBit(i));
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_3() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    gene1.setToRandomValue(new RandomGeneratorForTest(true));
    for (int i = 0; i < 7; i++) {
      assertTrue(gene1.getBit(i));
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testNewGene_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(7);
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    FixedBinaryGene gene2 = (FixedBinaryGene) gene1.newGene();
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
    assertEquals(checker, gene2.getConstraintChecker());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testHashCode_0() {
    FixedBinaryGene gene = new FixedBinaryGene(6);
    gene.hashCode();
    gene.setBit(0, 5, false);
    gene.hashCode();
    gene.setBit(0, 5, true);
    gene.hashCode();
    /**@todo implement checks for uniqueness*/
  }

  /**
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testHashCode_1() {
    Gene gene1 = new FixedBinaryGene(2);
    Gene gene2 = new FixedBinaryGene(2);
    int[] v1 = (int[]) gene1.getAllele();
    int[] v2 = (int[]) gene1.getAllele();
    int h1 = v1.hashCode();
    int h2 = v2.hashCode();
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testHashCode_2() {
    Gene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 1, 0});
    Gene gene2 = new FixedBinaryGene(3);
    gene2.setAllele(new int[] {0, 1, 0});
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testHashCode_3() {
    Gene gene1 = new FixedBinaryGene(2);
    gene1.setAllele(new int[] {1, 1});
    Gene gene2 = new FixedBinaryGene(2);
    gene1.setAllele(new int[] {1, 0});
    assertFalse(gene1.hashCode() == gene2.hashCode());
    assertTrue(gene1.hashCode() == gene1.hashCode());
    assertTrue(gene2.hashCode() == gene2.hashCode());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0() {
    BaseGene gene = new FixedBinaryGene(2);
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1() {
    BaseGene gene = new FixedBinaryGene(3);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  class GeneConstraintChecker
      implements IGeneConstraintChecker {
    public boolean verify(Gene a_gene, Object a_alleleValue,
                          IChromosome a_chrom, int a_index) {
      return true;
    }
  }
}
