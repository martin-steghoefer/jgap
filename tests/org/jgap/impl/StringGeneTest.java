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
import junitx.util.*;

/**
 * Tests for StringGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class StringGeneTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  public StringGeneTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(StringGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    StringGene gene = new StringGene(1, 100);
    //following should be possible without exception
    gene.setAllele("ABC");
    assertEquals(null, gene.getAlphabet());
    assertEquals(1,gene.getMinLength());
    assertEquals(100,gene.getMaxLength());
  }

  public void testConstruct_1() {
    try {
      Gene gene = new StringGene(2, 1);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() {
    try {
      Gene gene = new StringGene( -1, 3);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_3() {
    try {
      Gene gene = new StringGene(1, 3);
      gene.setAllele("ABCD");
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_4() {
    try {
      Gene gene = new StringGene(1, 3);
      gene.setAllele(new Double(2.3d));
    }
    catch (ClassCastException castex) {
      ; //this is OK
    }
  }

  /**
   * This feature is now implemented,
   * the persistend delimiters are allowed in the alphabet.
   * The block in StringGene is removed.
   * @todo Consider removing this test
   * @author Audrius Meskauskas (of commenting "fail") out
   */
      public void testSetAlphabet_0() {
    StringGene gene = new StringGene(3, 5);
    try {
      gene.setAlphabet("1" + Gene.PERSISTENT_FIELD_DELIMITER);
      // fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAlphabet_1()
      throws Exception {
    StringGene gene = new StringGene(3, 5);
    final String ALPHABET = "1234";
    gene.setAlphabet(ALPHABET);
    String alphabet = (String) PrivateAccessor.getField(gene, "m_alphabet");
    assertEquals(ALPHABET, alphabet);
    assertEquals(alphabet, gene.getAlphabet());
  }

  public void testToString_0() {
    Gene gene = new StringGene(3, 7);
    gene.setAllele("ABC");
    assertEquals("ABC", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new StringGene(3, 5);
    gene.setAllele("BCD");
    assertEquals("BCD", gene.getAllele());
  }

  public void testEquals_0() {
    Gene gene1 = new StringGene(1, 100);
    Gene gene2 = new StringGene(1, 100);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new StringGene(3, 100);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new StringGene(11, 77);
    assertTrue(gene1.equals(new StringGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new StringGene(11, 17);
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    Gene gene1 = new StringGene(12, 100);
    Gene gene2 = new StringGene(12, 99);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_5() {
    Gene gene1 = new StringGene(2, 5);
    Gene gene2 = new StringGene(1, 5);
    gene1.setAllele("ABC");
    gene2.setAllele("AB");
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Set Allele to null, no exception should occur

   */
  public void testSetAllele_0() {
    Gene gene1 = new StringGene(0, 10000);
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new StringGene(3, 4);
    try {
      gene1.setAllele("AB");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAllele_2() {
    Gene gene1 = new StringGene(3, 4, "ABCDEFHI");
    try {
      gene1.setAllele("ABDG");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAllele_3() {
    Gene gene1 = new StringGene(3, 4, "EGAL");
    try {
      //length of allele to short
      gene1.setAllele("");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAllele_4() {
    Gene gene1 = new StringGene(0, 4, "");
    //following should be possible
    gene1.setAllele("");
  }

  public void testNewGene_0()
      throws Exception {
    StringGene gene1 = new StringGene(1, 4);
    gene1.setAllele("XYZ");
    int minLength1 = gene1.getMinLength();
    int maxLength1 = gene1.getMaxLength();
    StringGene gene2 = (StringGene) gene1.newGene();
    int minLength2 = gene2.getMinLength();
    int maxLength2 = gene2.getMaxLength();
    assertEquals(minLength1, minLength2);
    assertEquals(maxLength1, maxLength2);
  }

  public void testPersistentRepresentation_0()
      throws Exception {
    Gene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(new String("BABE"));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testPersistentRepresentation_1() {
    Gene gene1 = new StringGene(2, 10);
    try {
      gene1.setAllele("");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testPersistentRepresentation_2()
      throws Exception {
    Gene gene1 = new StringGene(0, 10);
    gene1.setAllele("");
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testPersistentRepresentation_3()
      throws Exception {
    Gene gene1 = new StringGene();
    gene1.setAllele("");
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testPersistentRepresentation_4()
      throws Exception {
    Gene gene1 = new StringGene();
    gene1.setAllele(null);
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testPersistentRepresentation_5()
      throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE"+CompositeGene.GENE_DELIMITER);
    gene1.setAllele(new String("BABE"));
    String pres1 = gene1.getPersistentRepresentation();
    StringGene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
    assertEquals(gene1, gene2);
    assertEquals(gene1.getAlphabet(), gene2.getAlphabet());
  }

  public void testApplyMutation_0() {
    Gene gene1 = new StringGene(5, 5);
    gene1.setAllele("12345");
    gene1.applyMutation(0, 0.99d);
    gene1.applyMutation(4, -0.99d);
  }

  public void testApplyMutation_1() {
    Gene gene1 = new StringGene(1, 1);
    gene1.setAllele("1");
    gene1.applyMutation(0, 0.99d);
  }

  /**
   * Invalid index specified
   */
  public void testApplyMutation_2() {
    Gene gene1 = new StringGene(1, 1);
    gene1.setAllele("1");
    try {
      gene1.applyMutation(1, 0.99d);
      fail();
    }
    catch (StringIndexOutOfBoundsException sex) {
      ; //this is OK
    }
  }

  /**
   * No allele set
   */
  public void testApplyMutation_3() {
    Gene gene1 = new StringGene(1, 1);
    gene1.applyMutation(0, 0.99d);
  }

  public void testApplyMutation_4() {
    Gene gene1 = new StringGene(6, 6, StringGene.ALPHABET_CHARACTERS_LOWER);
    gene1.setAllele("ijklmn");
    gene1.applyMutation(0, 0.3d);
    assertFalse(gene1.getAllele().equals("ijklmn"));
    gene1.setAllele("ijklmn");
    gene1.applyMutation(4, -0.3d);
    assertFalse(gene1.getAllele().equals("ijklmn"));
  }

  /**
   * Mutation 0.0 should not change anything
   */
  public void testApplyMutation_5() {
    Gene gene1 = new StringGene(6, 6, StringGene.ALPHABET_CHARACTERS_LOWER);
    gene1.setAllele("ijklmn");
    gene1.applyMutation(0, 0.0d);
    assertEquals(gene1.getAllele(),"ijklmn");
  }

  public void testSetMinMaxLength_0()
      throws Exception {
    StringGene gene = new StringGene();
    gene.setMinLength(4);
    gene.setMaxLength(3);
    assertEquals(4,gene.getMinLength());
    assertEquals(3,gene.getMaxLength());
  }

  public void testSetToRandomValue_0() {
    /**@todo implement*/
  }

}
