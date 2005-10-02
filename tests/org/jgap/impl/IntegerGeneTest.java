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
 * Tests the IntegerGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class IntegerGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.26 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(IntegerGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Gene gene = new IntegerGene(1, 100);
    //following should be possible without exception
    gene.setAllele(new Integer(101));
  }

  public void testToString_0() {
    Gene gene = new IntegerGene(1, 100);
    gene.setAllele(new Integer(47));
    assertEquals("IntegerGene(1,100)=47", gene.toString());
  }

  public void testToString_1() {
    Gene gene = new IntegerGene( -2, 100);
    gene.setAllele(new Integer(99));
    assertEquals("IntegerGene(-2,100)=99", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new IntegerGene(1, 100);
    gene.setAllele(new Integer(33));
    assertEquals(new Integer(33), gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new IntegerGene(1, 100);
    gene.setAllele(new Integer(1));
    assertEquals(new Integer(1), gene.getAllele());
  }

  public void testGetAllele_2() {
    Gene gene = new IntegerGene(1, 100);
    gene.setAllele(new Integer(100));
    assertEquals(new Integer(100), gene.getAllele());
  }

  public void testEquals_0() {
    Gene gene1 = new IntegerGene(1, 100);
    Gene gene2 = new IntegerGene(1, 100);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new IntegerGene(1, 100);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new IntegerGene(1, 100);
    assertFalse(gene1.equals(new BooleanGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new IntegerGene(1, 100);
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    Gene gene1 = new IntegerGene(1, 100);
    Gene gene2 = new IntegerGene(1, 99);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_5() {
    Gene gene1 = new IntegerGene(1, 100);
    Gene gene2 = new DoubleGene(1, 99);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Uses subclass of IntegerGene
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_6() {
    Gene gene1 = new IntegerGene(1, 100);
    Gene gene2 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(45));
    gene2.setAllele(new Integer(46));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Uses subclass of IntegerGene
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_7() {
    Gene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(7));
    Gene gene2 = new IntegerGene2(1, 100);
    gene2.setAllele(new Integer(7));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Compare with application data set and option for comparation activated
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_8() {
    BaseGene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(7));
    gene1.setApplicationData(new Integer(7));
    gene1.setCompareApplicationData(true);
    Gene gene2 = new IntegerGene(1, 100);
    gene2.setApplicationData(new Integer(7));
    gene2.setAllele(new Integer(7));
    gene2.setCompareApplicationData(true);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * Compare with application data set and option for comparation activated
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_9() {
    BaseGene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(7));
    gene1.setApplicationData(new Integer(7));
    gene1.setCompareApplicationData(true);
    Gene gene2 = new IntegerGene(1, 100);
    gene2.setCompareApplicationData(true);
    gene2.setApplicationData(new Integer(7));
    gene2.setAllele(new Integer(7));
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * Compare with application data set and option for comparation activated
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_9_2() {
    BaseGene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(8));
    gene1.setApplicationData(new Integer(5));
    gene1.setCompareApplicationData(true);
    Gene gene2 = new IntegerGene(1, 100);
    gene2.setCompareApplicationData(true);
    gene2.setApplicationData(new Integer(7));
    gene2.setAllele(new Integer(8));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /*
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_10() {
    Gene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(8));
    Gene gene2 = new IntegerGene(1, 99);
    gene2.setAllele(new Integer( -8));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Compare with application data set but option for comparation deactivated
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_9_3() {
    BaseGene gene1 = new IntegerGene(1, 100);
    gene1.setAllele(new Integer(8));
    gene1.setApplicationData(new Integer(5));
    gene1.setCompareApplicationData(false);
    Gene gene2 = new IntegerGene(1, 100);
    gene2.setCompareApplicationData(false);
    gene2.setApplicationData(new Integer(7));
    gene2.setAllele(new Integer(8));
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testIntValue_0() {
    IntegerGene gene1 = new IntegerGene(1, 10000);
    gene1.setAllele(new Integer(4711));
    assertEquals(4711, gene1.intValue());
  }

  public void testIntValue_1() {
    IntegerGene gene1 = new IntegerGene(1, 10000);
    gene1.setAllele(null);
    try {
      assertEquals(0, gene1.intValue());
      fail();
    }
    catch (NullPointerException nullex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to null, no exception should occur
   */
  public void testSetAllele_0() {
    Gene gene1 = new IntegerGene(1, 10000);
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new IntegerGene(1, 10000);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testNewGene_0()
      throws Exception {
    IntegerGene gene1 = new IntegerGene(1, 10000);
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    gene1.setAllele(new Integer(4711));
    Integer lower1 = (Integer) privateAccessor.getField(gene1,
        "m_lowerBounds");
    Integer upper1 = (Integer) privateAccessor.getField(gene1,
        "m_upperBounds");
    IntegerGene gene2 = (IntegerGene)gene1.newGene();
    Integer lower2 = (Integer) privateAccessor.getField(gene2,
        "m_lowerBounds");
    Integer upper2 = (Integer) privateAccessor.getField(gene2,
        "m_upperBounds");
    assertEquals(lower1, lower2);
    assertEquals(upper1, upper2);
    assertEquals(checker, gene2.getConstraintChecker());
  }

  public void testCleanup() {
    //cleanup should do nothing!
    Gene gene = new IntegerGene(1, 6);
    Gene copy = gene.newGene();
    gene.cleanup();
    assertEquals(copy, gene);
  }

  public void testPersistentRepresentation_0()
      throws Exception {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new IntegerGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  /**
   * Should be possible without exception
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_1()
      throws Exception {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    gene1.setValueFromPersistentRepresentation(null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_2()
      throws Exception {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    gene1.setValueFromPersistentRepresentation("2" +
                                               IntegerGene.PERSISTENT_FIELD_DELIMITER +
                                               "3" +
                                               IntegerGene.PERSISTENT_FIELD_DELIMITER +
                                               "4");
    assertEquals(2,((Integer)gene1.getAllele()).intValue());
    assertEquals(3,((Integer) privateAccessor.getField(gene1,
        "m_lowerBounds")).intValue());
    assertEquals(4,((Integer) privateAccessor.getField(gene1,
        "m_upperBounds")).intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_3()
      throws Exception {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    gene1.setValueFromPersistentRepresentation("null" +
                                               IntegerGene.PERSISTENT_FIELD_DELIMITER +
                                               "3" +
                                               IntegerGene.PERSISTENT_FIELD_DELIMITER +
                                               "4");
    assertNull(gene1.getAllele());
    assertEquals(3,((Integer) privateAccessor.getField(gene1,
        "m_lowerBounds")).intValue());
    assertEquals(4,((Integer) privateAccessor.getField(gene1,
        "m_upperBounds")).intValue());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_4()
       {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    try {
      gene1.setValueFromPersistentRepresentation("null" +
                                                 IntegerGene.
                                                 PERSISTENT_FIELD_DELIMITER +
                                                 "3.5" +
                                                 IntegerGene.
                                                 PERSISTENT_FIELD_DELIMITER +
                                                 "4");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ;//this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_5()
      {
    Gene gene1 = new IntegerGene(2, 753);
    gene1.setAllele(new Integer(45));
    try {
      gene1.setValueFromPersistentRepresentation("null" +
                                                 IntegerGene.
                                                 PERSISTENT_FIELD_DELIMITER +
                                                 "3" +
                                                 IntegerGene.
                                                 PERSISTENT_FIELD_DELIMITER +
                                                 "a");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ;//this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareToNative_0() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(59));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareToNative_1() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareToNative_2() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(59));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareToNative_3() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(59));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer( -59));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareToNative_4() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(0));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer( -0));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_0() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(0));
    Gene gene2 = new IntegerGene2(53, 67);
    gene2.setAllele(new Integer( -0));
    try {
      gene1.compareTo(gene2);
      fail();
    }
    catch (ClassCastException cex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_1() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(59));
    assertEquals( -1, gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_2() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals(0, gene1.compareTo(gene2));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_3() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(59));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCompareTo_4() {
    Gene gene1 = new IntegerGene(13, 65);
    Gene gene2 = new IntegerGene(53, 67);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_0() {
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(50));
    gene.applyMutation(0, 0.0d);
    assertEquals(50, gene.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_1()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15));
    Genotype.setConfiguration(config);
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(50));
    gene.applyMutation(0, 0.5d);
    assertEquals(Math.round(50 + (100 - 0) * 0.5d), gene.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_2()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15));
    Genotype.setConfiguration(config);
    IntegerGene gene = new IntegerGene(44, 100);
    gene.setAllele(new Integer(50));
    gene.applyMutation(0, 0.3d);
    assertEquals(Math.round(50 + (100 - 44) * 0.3d), gene.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_3()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15));
    Genotype.setConfiguration(config);
    IntegerGene gene = new IntegerGene(33, 100);
    gene.setAllele(new Integer(50));
    gene.applyMutation(0, 1.9d);
    assertEquals(Math.round(33 + 15), gene.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_4()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15));
    Genotype.setConfiguration(config);
    IntegerGene gene = new IntegerGene(2, 100);
    gene.setAllele(new Integer(60));
    gene.applyMutation(0, 1.9d);
    assertEquals(Math.round(2 + 15), gene.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_5()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15));
    Genotype.setConfiguration(config);
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(60));
    gene.applyMutation(0, -1.0d);
    assertEquals(Math.round(0 + 15), gene.intValue());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testApplyMutation_6() {
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(60));
    gene.applyMutation(0, -0.4d);
    assertEquals(Math.round(60 + (100 * ( -0.4d))), gene.intValue());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_0() {
    Gene gene = new IntegerGene(1, 6);
    gene.setAllele(new Integer(5));

    gene.setToRandomValue(new RandomGeneratorForTest(0.2d));
    assertEquals(new Integer((int)(0.2d * (6 - 1) + 1)), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_1()
      throws Exception {
    Gene gene = new IntegerGene( -1, 7);
    gene.setAllele(new Integer(4));

    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    gene.setToRandomValue(new RandomGeneratorForTest(0.3d));
    assertEquals(new Integer((int)(0.3d * (7 + 1) - 1)), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_2()
      throws Exception {
     Gene gene = new IntegerGene( -2, -1);
     gene.setAllele(new Integer(4));

     Configuration conf = new DefaultConfiguration();
     Genotype.setConfiguration(conf);

     gene.setToRandomValue(new RandomGeneratorForTest(0.8d));
     assertEquals(new Integer((int)(0.8d * ( -1 + 2) - 2)), gene.getAllele());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_3(){
    IntegerGene gene = new IntegerGene(0, 8);
    gene.setAllele(new Integer(5));
    gene.setToRandomValue(new RandomGeneratorForTest(4));

    if (gene.intValue() < 0 ||
        gene.intValue() > 8) {
      fail();
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_4(){
    IntegerGene gene = new IntegerGene(1, 6);
    gene.setAllele(new Integer(2));
    gene.setToRandomValue(new RandomGeneratorForTest(3));

    if (gene.intValue() < 1 ||
        gene.intValue() > 6) {
      fail();
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

  /**
   * Descendent of IntegerGene being virtually the same but of a different
   * class so that equals and compareTo should signal a difference.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  class IntegerGene2
      extends IntegerGene {
    public IntegerGene2() {
      super();
    }

    public IntegerGene2(int a_lowerBounds, int a_upperBounds) {
      super(a_lowerBounds, a_upperBounds);
    }
  }

  class GeneConstraintChecker implements IGeneConstraintChecker {
    public boolean verify(Gene a_gene, Object a_alleleValue)
        throws RuntimeException {
      return true;
    }
  }
}
