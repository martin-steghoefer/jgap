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
 * Tests the StringGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class StringGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.31 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(StringGeneTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    StringGene gene = new StringGene(1, 100);
    //following should be possible without exception
    gene.setAllele("ABC");
    assertEquals(null, gene.getAlphabet());
    assertEquals(1, gene.getMinLength());
    assertEquals(100, gene.getMaxLength());
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
    try {
      new StringGene(2, 1);
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
      new StringGene( -1, 3);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_3() {
    try {
      Gene gene = new StringGene(1, 3);
      gene.setAllele("ABCD");
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
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
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetAlphabet_1()
      throws Exception {
    StringGene gene = new StringGene(3, 5);
    final String alphabet_const = "1234";
    gene.setAlphabet(alphabet_const);
    String alphabet = (String) privateAccessor.getField(gene, "m_alphabet");
    assertEquals(alphabet, alphabet_const);
    assertEquals(alphabet, gene.getAlphabet());
  }

  /**
   * @author Klaus Meffert
   */
  public void testToString_0() {
    Gene gene = new StringGene(3, 7);
    gene.setAllele("ABC");
    assertEquals("StringGene=ABC", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_1() {
    StringGene gene = new StringGene(3, 7);
    assertEquals("StringGene=null", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_2() {
    StringGene gene = new StringGene(0, 7);
    gene.setAllele("");
    assertEquals("StringGene=\"\"", gene.toString());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetAllele_0() {
    Gene gene = new StringGene(3, 5);
    gene.setAllele("BCD");
    assertEquals("BCD", gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_0() {
    Gene gene1 = new StringGene(1, 100);
    Gene gene2 = new StringGene(1, 100);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_1() {
    Gene gene1 = new StringGene(3, 100);
    assertFalse(gene1.equals(null));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_2() {
    Gene gene1 = new StringGene(11, 77);
    assertTrue(gene1.equals(new StringGene()));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_3() {
    Gene gene1 = new StringGene(11, 17);
    assertFalse(gene1.equals(new Vector()));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_4() {
    Gene gene1 = new StringGene(12, 100);
    Gene gene2 = new StringGene(12, 99);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_5() {
    Gene gene1 = new StringGene(2, 5);
    Gene gene2 = new StringGene(1, 5);
    gene1.setAllele("ABC");
    gene2.setAllele("AB");
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_6() {
    Gene gene1 = new StringGene(2, 5);
    Gene gene2 = new DoubleGene(1, 5);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   */
  public void testEquals_7() {
    Gene gene1 = new StringGene(2, 5);
    Gene gene2 = new BooleanGene();
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_8() {
    Gene gene1 = new StringGene(2, 6);
    gene1.setAllele("hallo");
    Gene gene2 = new StringGene(2, 6);
    gene2.setAllele("hello");
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
    gene1.setAllele("hello1");
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
    gene2.setAllele("HELLO1");
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Using application data
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_9() {
    Genotype.setConfiguration(new DefaultConfiguration());
    Gene gene1 = new StringGene(2, 6);
    gene1.setAllele("hallo");
    gene1.setApplicationData(new Double(2.3d));
    Gene gene2 = new StringGene(2, 6);
    gene2.setAllele("hallo");
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
    gene1.setCompareApplicationData(true);
    assertFalse(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
    gene2.setCompareApplicationData(true);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
    gene2.setApplicationData(new Double(2.3d));
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * Comparation using application data
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_0() {
    Genotype.setConfiguration(new DefaultConfiguration());
    Gene gene1 = new StringGene(2, 6);
    gene1.setAllele("hallo");
    gene1.setApplicationData(new Double(2.3d));
    Gene gene2 = new StringGene(2, 6);
    gene2.setAllele("hallo");
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene2.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
    gene2.setApplicationData(new Double(2.3d));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene2.setAllele(null);
    assertEquals(1, gene1.compareTo(gene2));
    gene2.setApplicationData(null);
    assertEquals(1, gene1.compareTo(gene2));
  }

  /**
   * class cast exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_1() {
    Gene gene1 = new StringGene(2, 6);
    try {
      gene1.compareTo(new Chromosome());
      fail();
    } catch (ClassCastException cex) {
      ; // this is OK
    }
  }

  /**
   * Comparation using application data and null allele
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_2() {
    Genotype.setConfiguration(new DefaultConfiguration());
    Gene gene1 = new StringGene(2, 6);
    gene1.setApplicationData(new Double(2.3d));
    Gene gene2 = new StringGene(2, 6);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene2.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
    gene2.setApplicationData(new Double(2.3d));
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene2.setApplicationData(null);
    assertEquals(1, gene1.compareTo(gene2));
  }

  /**
   * Set Allele to null, no exception should occur.
   *
   * @author Klaus Meffert
   */
  public void testSetAllele_0() {
    Gene gene1 = new StringGene(0, 10000);
    gene1.setAllele(null);
  }

  /**
   * Allele too short.
   *
   * @author Klaus Meffert
   */
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

  /**
   * Allele consists of illegal characters.
   *
   * @author Klaus Meffert
   */
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

  /**
   * @author Klaus Meffert
   */
  public void testSetAllele_3() {
    Gene gene1 = new StringGene(3, 4, "EGAL");
    try {
      // Length of allele to short.
      // --------------------------
      gene1.setAllele("");
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetAllele_4() {
    Gene gene1 = new StringGene(0, 4, "");
    //following should be possible
    gene1.setAllele("");
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_5() {
    StringGene gene1 = new StringGene(0, 4, "ABC");
    //following should be possible
    gene1.setAllele("A");
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    });
    gene1.setAllele("B");
    assertEquals("A", gene1.stringValue());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_6() {
    StringGene gene1 = new StringGene(0, 4, "ABC");
    //following should be possible
    gene1.setAllele("A");
    gene1.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return true;
      }
    });
    gene1.setAllele("B");
    assertEquals("B", gene1.stringValue());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testNewGene_0()
      throws Exception {
    StringGene gene1 = new StringGene(1, 4);
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    gene1.setAllele("XYZ");
    int minLength1 = gene1.getMinLength();
    int maxLength1 = gene1.getMaxLength();
    StringGene gene2 = (StringGene) gene1.newGene();
    int minLength2 = gene2.getMinLength();
    int maxLength2 = gene2.getMaxLength();
    assertEquals(minLength1, minLength2);
    assertEquals(maxLength1, maxLength2);
    assertEquals(checker, gene2.getConstraintChecker());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_0()
      throws Exception {
    Gene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele("BABE");
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  /**
   * @author Klaus Meffert
   */
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

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
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

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
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

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
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

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_5()
      throws Exception {
    StringGene gene1 = new StringGene(2, 10,
                                      "ABCDE" + CompositeGene.GENE_DELIMITER);
    gene1.setAllele("BABE");
    String pres1 = gene1.getPersistentRepresentation();
    StringGene gene2 = new StringGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
    assertEquals(gene1, gene2);
    assertEquals(gene1.getAlphabet(), gene2.getAlphabet());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_6()
      throws Exception {
    Gene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele("BABE");
    gene1.setValueFromPersistentRepresentation(null);
    assertEquals("BABE", gene1.getAllele());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_7()
      throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    assertEquals("null:2:10:ABCDE", gene1.getPersistentRepresentation());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_8() {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("null:2:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_9() {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    assertEquals("null:2:10:ABCDE", gene1.getPersistentRepresentation());
  }


  /**
   * Invalid number in second argument
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentRepresentation_10() throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("null:a:10:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Invalid number in third argument
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentRepresentation_11() throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("null:2:3b:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Minlen to great
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentRepresentation_12() throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("nada:7:6:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Maxlen to small
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentRepresentation_14() throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("nada:1:3:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Illegal character
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentRepresentation_15() throws Exception {
    StringGene gene1 = new StringGene(2, 10, "ABCDE");
    gene1.setAllele(null);
    try {
      gene1.setValueFromPersistentRepresentation("ABHJ:4:7:ABCDE");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_0()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    Gene gene1 = new StringGene(5, 5);
    gene1.setAllele("12345");
    gene1.applyMutation(0, 0.99d);
    gene1.applyMutation(4, -0.99d);
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_1()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    Gene gene1 = new StringGene(1, 1);
    gene1.setAllele("1");
    gene1.applyMutation(0, 0.99d);
  }

  /**
   * Invalid index specified.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_2()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
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
   * No allele set.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_3()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    Gene gene1 = new StringGene(1, 1);
    gene1.applyMutation(0, 0.99d);
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testApplyMutation_4()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    Gene gene1 = new StringGene(6, 6, StringGene.ALPHABET_CHARACTERS_LOWER);
    gene1.setAllele("ijklmn");
    gene1.applyMutation(0, 0.3d);
    assertFalse(gene1.getAllele().equals("ijklmn"));
    gene1.setAllele("ijklmn");
    gene1.applyMutation(4, -0.3d);
    assertFalse(gene1.getAllele().equals("ijklmn"));
  }

  /**
   * Mutation 0.0 should not change anything.
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testApplyMutation_5()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    Gene gene1 = new StringGene(6, 6, StringGene.ALPHABET_CHARACTERS_LOWER);
    gene1.setAllele("ijklmn");
    gene1.applyMutation(0, 0.0d);
    assertEquals(gene1.getAllele(), "ijklmn");
  }

  /**
   * applyMutation with empty alphabet.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_6()
      throws Exception {
    Genotype.setConfiguration(new ConfigurationForTest());
    StringGene gene1 = new StringGene(6, 6);
    gene1.setAllele("ijklmn");
    gene1.setAlphabet("");
    try {
      gene1.applyMutation(0, 0.0d);
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * null random generator should lead to NullPointerException.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_7()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    conf.setRandomGenerator(null);
    Genotype.setConfiguration(conf);
    Gene gene1 = new StringGene(5, 5);
    gene1.setAllele("12345");
    try {
      gene1.applyMutation(0, 0.99d);
      fail();
    }catch (NullPointerException nex) {
      ;//this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetMinMaxLength_0()
      throws Exception {
    StringGene gene = new StringGene();
    gene.setMinLength(4);
    gene.setMaxLength(3);
    assertEquals(4, gene.getMinLength());
    assertEquals(3, gene.getMaxLength());
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_0() {
    StringGene gene = new StringGene(1, 6, StringGene.ALPHABET_CHARACTERS_UPPER);
    gene.setToRandomValue(new RandomGeneratorForTest(2));
    assertEquals("CCC", gene.getAllele());
    gene.setToRandomValue(new RandomGeneratorForTest(1));
    assertEquals("BB", gene.getAllele());
    gene.setToRandomValue(new RandomGeneratorForTest(0));
    assertEquals("A", gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_1() {
    Gene gene = new StringGene(1, 6, StringGene.ALPHABET_CHARACTERS_UPPER);
    gene.setAllele("XYZA"); // should not matter here
    gene.setToRandomValue(new RandomGeneratorForTest(3));
    assertEquals("DDDD", gene.getAllele());
  }

  public void testSetToRandomValue_2() {
    Gene gene = new StringGene(1, 6, "ABC");
    gene.setToRandomValue(new RandomGeneratorForTest(3));
    assertEquals("AAAA", gene.getAllele());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_3()
      throws Exception {
    StringGene gene = new StringGene(1, 7, "DEF");
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    gene.setToRandomValue(new RandomGeneratorForTest(2));
    assertEquals("FFF", gene.getAllele());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_4()
      throws Exception {
    StringGene gene = new StringGene(1, 7, "DEF");
    gene.setAllele("EEFD");
    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rn = new RandomGeneratorForTest();
    // set random generator to produce
    // 1) length of new allele (-1)
    // 2) first character out of alphabet ("DEF"), starting from 0
    // 3) second character out of alphabet
    // 4) third character out of alphabet
    // 5) fourth character out of alphabet
    rn.setNextIntSequence(new int[] {3, 2, 1, 0, 2});
    gene.setToRandomValue(rn);
    assertEquals("FEDF", gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_5() {
    StringGene gene = new StringGene(1, 8, StringGene.ALPHABET_CHARACTERS_LOWER);
    gene.setToRandomValue(new StockRandomGenerator());
    for (int i = 0; i < gene.size(); i++) {
      if ( ( (String) gene.getAllele()).charAt(i) < 'a'
          || ( (String) gene.getAllele()).charAt(i) > 'z') {
        fail();
      }
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_6() {
    Gene gene = new StringGene(1, 6, "");
    try {
      gene.setToRandomValue(new StockRandomGenerator());
      fail();
    }
    catch (IllegalStateException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_7() {
    Gene gene = new StringGene(1, 6, null);
    try {
      gene.setToRandomValue(new StockRandomGenerator());
      fail();
    }
    catch (IllegalStateException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_8() {
    StringGene gene = new StringGene(2, 6, "ABC");
    try {
      gene.setMaxLength(1);
      gene.setToRandomValue(new StockRandomGenerator());
      fail();
    }
    catch (IllegalStateException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testSetConstraintChecker_0() {
    StringGene gene = new StringGene(1, 6, "ABC");
    assertNull(gene.getConstraintChecker());
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    });
    assertNotNull(gene.getConstraintChecker());
  }

  /**
   * @author Klaus Meffert
   */
  public void testIsValidAlphabet_0() {
    StringGene gene = new StringGene(1, 6, "");
    try {
      gene.setAllele("HALLO");
      fail();
    }
    catch (IllegalArgumentException ilex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0() {
    BaseGene gene = new IntegerGene();
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1() {
    BaseGene gene = new IntegerGene();
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
    public boolean verify(Gene a_gene, Object a_alleleValue, IChromosome a_chrom,
                          int a_index) {
      return true;
    }
  }
}
