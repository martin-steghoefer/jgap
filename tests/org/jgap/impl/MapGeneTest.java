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
 * Tests the MapGene class
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class MapGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(MapGeneTest.class);
    return suite;
  }

  public void testConstruct_0()
      throws Exception {
    Gene gene = new MapGene();
    assertNotNull(privateAccessor.getField(gene, "m_geneMap"));
  }

  public void testConstruct_1()
      throws Exception {
    Map map = new HashMap();
    map.put(new Integer(2), new Integer(3));
    MapGene gene = new MapGene(map);
    Map geneMap = (Map) privateAccessor.getField(gene, "m_geneMap");
    assertNotNull(geneMap);
    assertEquals(new Integer(3), geneMap.get(new Integer(2)));
    assertEquals(1, geneMap.size());
  }

  public void testConstruct_2()
      throws Exception {
    try {
      new MapGene(null);
      fail();
    } catch (IllegalArgumentException iex) {
      ;//this is OK
    }
  }

  public void testToString_0() {
    MapGene gene = new MapGene();
    assertEquals("[null]", gene.toString());
  }

  public void testToString_1() {
    MapGene gene = new MapGene();
    gene.addAllele(new Integer(102));
    assertEquals("[(102,102)]", gene.toString());
  }

  public void testToString_2() {
    MapGene gene = new MapGene();
    gene.addAllele(new Integer(3), new Integer(102));
    gene.addAllele(new Integer(7), new Integer( -55));
    assertEquals("[(3,102),(7,-55)]", gene.toString());
  }

  public void testToString_3() {
    MapGene gene = new MapGene();
    gene.addAllele(new Double(3), new Integer(102));
    gene.addAllele(new Double(7), new Integer( -55));
    assertEquals("[(3.0,102),(7.0,-55)]", gene.toString());
  }

  /**
   * Using different types as keys. Order of adding alleles not important!
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_4() {
    MapGene gene = new MapGene();
    gene.addAllele(new Integer(3), new Double(102));
    gene.addAllele(new Double(7), new Integer( -55));
    assertEquals("[(7.0,-55),(3,102.0)]", gene.toString());
  }

  /**
   * Using different types as keys. Order of adding alleles not important!
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_5() {
    MapGene gene = new MapGene();
    gene.addAllele(new Double(7), new Integer( -55));
    gene.addAllele(new Integer(3), new Double(102));
    assertEquals("[(7.0,-55),(3,102.0)]", gene.toString());
  }

  public void testGetAllele_0() {
    MapGene gene = new MapGene();
    gene.addAllele(1);
    gene.addAllele(7);
    gene.addAllele( -5);
    gene.addAllele(3);
    gene.setAllele(new Integer(1));
    assertEquals(new Integer(1), gene.getAllele());
    gene.setAllele(new Integer(3));
    assertEquals(new Integer(3), gene.getAllele());
    gene.setAllele(new Integer( -5));
    assertEquals(new Integer( -5), gene.getAllele());
    gene.setAllele(new Integer(7));
    assertEquals(new Integer(7), gene.getAllele());
    try {
      gene.setAllele(new Integer(2));
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testEquals_0() {
    Gene gene1 = new MapGene();
    Gene gene2 = new MapGene();
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new MapGene();
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Map alleles = new HashMap();
    alleles.put(new Integer(1),new Integer(1));
    Gene gene1 = new MapGene(alleles);
    assertFalse(gene1.equals(new BooleanGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new MapGene();
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    Map alleles = new HashMap();
    alleles.put(new Integer(1),new Integer(1));
    Gene gene1 = new MapGene(alleles);
    Gene gene2 = new MapGene(alleles);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_4_2() {
    Map alleles1 = new HashMap();
    alleles1.put(new Integer(1),new Integer(1));
    Gene gene1 = new MapGene(alleles1);
    Map alleles2 = new HashMap();
    alleles1.put(new Integer(2),new Integer(3));
    Gene gene2 = new MapGene(alleles2);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_5() {
    /**@todo adapt test cases (copied from IntegerGene)*/
    Gene gene1 = new IntegerGene(1, 100);
    Gene gene2 = new DoubleGene(1, 99);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * Set Allele to null, no exception should occur
   */
  public void testSetAllele_0() {
    MapGene gene1 = new MapGene();
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    MapGene gene1 = new MapGene();
    gene1.setAllele("22");
  }

  public void testSetAllele_2() {
    Gene gene = new MapGene();
    gene.setAllele(new Integer(101));
  }

  /**
   * @throws Exception
   * @since 2.5
   */
  public void testNewGene_0()
      throws Exception {
    MapGene gene1 = new MapGene();
    gene1.setAllele(new Integer(4711));
    Object value1 = privateAccessor.getField(gene1,"m_value");
    MapGene gene2 = (MapGene)gene1.newGene();
    Object value2 = privateAccessor.getField(gene2,"m_value");
    Map geneMap = (Map)privateAccessor.getField(gene2,"m_geneMap");
    assertEquals(value1, value2);
    assertEquals(0, geneMap.size());
    assertEquals(gene1, gene2);
  }

  /**
   * @throws Exception
   * @since 2.5
   */
  public void testNewGene_1()
      throws Exception {
    MapGene gene1 = new MapGene();
    MapGene gene2 = (MapGene)gene1.newGene();
    assertEquals(gene1, gene2);
  }

  /**
   * @throws Exception
   * @since 2.5
   */
  public void testNewGene_2()
      throws Exception {
    Map alleles = new Hashtable();
    for (int i=0;i<40;i++) {
      alleles.put(new Integer(i), new Integer(i));
    }
    MapGene gene1 = new MapGene(alleles);

    MapGene gene2 = (MapGene)gene1.newGene();
    assertTrue(gene1.equals(gene2));
  }

  public void testCleanup() {
    //cleanup should do nothing!
    Gene gene = new MapGene();
    gene.setAllele("Hello");
    Gene copy = gene.newGene();
    gene.cleanup();
    assertEquals(copy, gene);
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testPersistentRepresentation_0()
      throws Exception {
    Map alleles = new HashMap();
    for (int i = -3; i < 45; i = i + 2) {
      alleles.put(new Integer(i), new Integer(i));
    }
    Gene gene1 = new MapGene(alleles);
    gene1.setAllele(new Integer(17));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new MapGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    /**@todo compare two maps independent of the order of their elements*/
    assertEquals(pres1, pres2);
  }

  /**
   * Should be possible without exception
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testPersistentRepresentation_1()
      throws Exception {
    Gene gene1 = new MapGene();
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
    MapGene gene1 = new MapGene();
    gene1.setAllele(new Integer(45));
    gene1.setValueFromPersistentRepresentation("6"
                                               +
                                               MapGene.
                                               PERSISTENT_FIELD_DELIMITER
                                               + "(0,1.0d),(2,3.0d),(4,5.0d)");
    assertEquals(6, ( (Integer) gene1.getAllele()).intValue());
    assertEquals(3, ( (Map) privateAccessor.getField(gene1,
        "m_geneMap")).size());
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
                                               IntegerGene.
                                               PERSISTENT_FIELD_DELIMITER +
                                               "3" +
                                               IntegerGene.
                                               PERSISTENT_FIELD_DELIMITER +
                                               "4");
    assertNull(gene1.getAllele());
    assertEquals(3, ( (Integer) privateAccessor.getField(gene1,
        "m_lowerBounds")).intValue());
    assertEquals(4, ( (Integer) privateAccessor.getField(gene1,
        "m_upperBounds")).intValue());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_4() {
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
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentRepresentation_5() {
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
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testPersistentRepresentation_6()
      throws Exception {
    Map alleles = new HashMap();
    for (int i = -49; i < -3; i++) {
      alleles.put(new Integer(i), new Integer(i+1));
    }
    Gene gene1 = new MapGene(alleles);
    gene1.setAllele(new Integer(-23));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new MapGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testCompareToNative_0() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(59));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  public void testCompareToNative_1() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(58));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  public void testCompareToNative_2() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(59));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer(58));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  public void testCompareToNative_3() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(59));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer( -59));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  public void testCompareToNative_4() {
    Gene gene1 = new IntegerGene(13, 65);
    gene1.setAllele(new Integer(0));
    Gene gene2 = new IntegerGene(53, 67);
    gene2.setAllele(new Integer( -0));
    assertEquals( ( (Integer) gene1.getAllele()).compareTo(gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  public void testApplyMutation_0() {
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(50));
    gene.applyMutation(0, 0.0d);
    assertEquals(50, gene.intValue());
  }

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

  public void testApplyMutation_6() {
    IntegerGene gene = new IntegerGene(0, 100);
    gene.setAllele(new Integer(60));
    gene.applyMutation(0, -0.4d);
    assertEquals(Math.round(60 + (100 * ( -0.4d))), gene.intValue());
  }

  public void testSetToRandomValue_0() {
    Gene gene = new MapGene();
    gene.setToRandomValue(new RandomGeneratorForTest(3));
    assertEquals(new Integer(3), gene.getAllele());
  }

  public void testSetToRandomValue_1()
      throws Exception {
    Gene gene = new IntegerGene( -1, 7);
    gene.setAllele(new Integer(4));

    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    gene.setToRandomValue(new RandomGeneratorForTest(0.3d));
    assertEquals(new Integer( (int) (0.3d * (7 + 1) - 1)), gene.getAllele());
  }

  public void testSetToRandomValue_2()
      throws Exception {
    Gene gene = new IntegerGene( -2, -1);
    gene.setAllele(new Integer(4));

    Configuration conf = new DefaultConfiguration();
    Genotype.setConfiguration(conf);

    gene.setToRandomValue(new RandomGeneratorForTest(0.8d));
    assertEquals(new Integer( (int) (0.8d * ( -1 + 2) - 2)), gene.getAllele());
  }

  public void testSetToRandomValue_3() {
    IntegerGene gene = new IntegerGene(0, 8);
    gene.setAllele(new Integer(5));
    gene.setToRandomValue(new RandomGeneratorForTest(4));

    if (gene.intValue() < 0 ||
        gene.intValue() > 8) {
      fail();
    }
  }

  public void testSetToRandomValue_4() {
    IntegerGene gene = new IntegerGene(1, 6);
    gene.setAllele(new Integer(2));
    gene.setToRandomValue(new RandomGeneratorForTest(3));

    if (gene.intValue() < 1 ||
        gene.intValue() > 6) {
      fail();
    }
  }
}
