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
 * Tests for DoubleGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DoubleGeneTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.14 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.0001d;

  public DoubleGeneTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DoubleGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Gene gene = new DoubleGene(1.1d, 100.0d);
    //following should be possible without exception
    gene.setAllele(new Double(101.1d));
  }

  public void testConstruct_1() {
    Gene gene = new DoubleGene();
    gene.setAllele(new Double(Double.MAX_VALUE));
  }

  public void testConstruct_2() {
    Gene gene = new DoubleGene();
    gene.setAllele(new Double( - (Double.MAX_VALUE / 2)));
  }

  public void testToString_0() {
    Gene gene = new DoubleGene(1.2d, 99.7d);
    gene.setAllele(new Double(47.3d));
    assertEquals("47.3", gene.toString());
  }

  public void testToString_1() {
    Gene gene = new DoubleGene( -100.0d, 100.0d);
    gene.setAllele(new Double( -88.75286d));
    assertEquals("-88.75286", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new DoubleGene(1.9d, 100.4d);
    gene.setAllele(new Double(33.0d));
    assertEquals(new Double(33.0d), gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new DoubleGene(1.8d, 100.1d);
    gene.setAllele(new Double(1.9d));
    assertEquals(new Double(1.9d), gene.getAllele());
  }

  public void testGetAllele_2() {
    Gene gene = new DoubleGene(1.0d, 100.0d);
    gene.setAllele(new Double(100.0d));
    assertEquals(new Double(100.0d), gene.getAllele());
  }

  public void testEquals_0() {
    Gene gene1 = new DoubleGene(1.1d, 100.2d);
    Gene gene2 = new DoubleGene(1.1d, 100.2d);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new DoubleGene(1.9d, 100.4d);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new DoubleGene(11.2d, 100.7d);
    assertFalse(gene1.equals(new BooleanGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new DoubleGene(1.0d, 100.7d);
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    Gene gene1 = new DoubleGene(1.2d, 100.3d);
    Gene gene2 = new DoubleGene(1.2d, 99.5d);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testDoubleValue_0() {
    DoubleGene gene1 = new DoubleGene(1.0d, 10000.0d);
    gene1.setAllele(new Double(4711.0d));
    assertEquals(4711.0d, gene1.doubleValue(), DELTA);
  }

  public void testDoubleValue_1() {
    DoubleGene gene1 = new DoubleGene(1.765d, 10000.0d);
    gene1.setAllele(null);
    try {
      assertEquals(0.0d, gene1.doubleValue(), DELTA);
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
    Gene gene1 = new DoubleGene(1.0d, 10000.0d);
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new DoubleGene(1.0d, 10000.0d);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testSetAllele_2() {
    Gene gene1 = new DoubleGene(1.0d, 10000.0d);
    try {
      gene1.setAllele(new Integer(22));
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  public void testNewGene_0()
      throws Exception {
    Gene gene1 = new DoubleGene(1.0d, 10000.0d);
    gene1.setAllele(new Double(4711.0d));
    Double lower1 = (Double) PrivateAccessor.getField(gene1,
        "m_lowerBounds");
    Double upper1 = (Double) PrivateAccessor.getField(gene1,
        "m_upperBounds");
    Gene gene2 = gene1.newGene();
    Double lower2 = (Double) PrivateAccessor.getField(gene2,
        "m_lowerBounds");
    Double upper2 = (Double) PrivateAccessor.getField(gene2,
        "m_upperBounds");
    assertEquals(lower1, lower2);
    assertEquals(upper1, upper2);
  }

  public void testPersistentRepresentation_0()
      throws Exception {
    Gene gene1 = new DoubleGene(2.05d, 7.53d);
    gene1.setAllele(new Double(4.5d));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new DoubleGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  public void testCleanup_0() {
    //cleanup should do nothing!
    Gene gene = new DoubleGene(1.3d, 6.5d);
    Gene copy = gene.newGene();
    gene.cleanup();
    assertEquals(copy, gene);
  }

  public void testSetToRandomValue_0() {
    Gene gene = new DoubleGene(1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));

    gene.setToRandomValue(new RandomGeneratorForTest(0.789d));
    assertEquals(new Double(0.789d * (6.5d - 1.3d) + 1.3d), gene.getAllele());
  }

  public void testSetToRandomValue_1() throws Exception {
    Gene gene = new DoubleGene( -1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));

    Configuration conf = new DefaultConfiguration();
    conf.setRandomGenerator(new RandomGeneratorForTest(0.258d));
    Genotype.setConfiguration(conf);

    gene.setToRandomValue(new RandomGeneratorForTest(0.014));
    assertEquals(new Double(0.014d * (6.5d + 1.3d) - 1.3d), gene.getAllele());
  }

   public void testSetToRandomValue_2() throws Exception {
     Gene gene = new DoubleGene( -1.3d, -0.5d);
     gene.setAllele(new Double(5.8d));

     Configuration conf = new DefaultConfiguration();
     conf.setRandomGenerator(new RandomGeneratorForTest(0.258d));
     Genotype.setConfiguration(conf);

     gene.setToRandomValue(new RandomGeneratorForTest(0.83d));
     assertEquals(new Double(0.83d * ( -0.5d + 1.3d) - 1.3d), gene.getAllele());
  }

  public void testSetToRandomValue_3(){
    DoubleGene gene = new DoubleGene(1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    gene.setToRandomValue(new RandomGeneratorForTest(0.478d));

    if (gene.doubleValue() < 1.3d ||
        gene.doubleValue() > 6.5d) {
      fail();
    }
  }

  public void testSetToRandomValue_4(){
    DoubleGene gene = new DoubleGene(1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    gene.setToRandomValue(new RandomGeneratorForTest(8.584d));

    if (gene.doubleValue() < 1.3d ||
        gene.doubleValue() > 6.5d) {
      fail();
    }
  }

  public void testCompareToNative_0() {
    Gene gene1 = new DoubleGene(1.3d, 6.5d);
    gene1.setAllele(new Double(5.8d));
    Gene gene2 = new DoubleGene(5.3d, 6.7d);
    gene2.setAllele(new Double(5.9d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo(gene2.getAllele()),
                 gene1.compareTo(gene2));
  }

  public void testCompareToNative_1() {
    Gene gene1 = new DoubleGene(1.3d, 6.5d);
    gene1.setAllele(new Double(5.8d));
    Gene gene2 = new DoubleGene(5.3d, 6.7d);
    gene2.setAllele(new Double(5.8d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo(gene2.getAllele()),
                 gene1.compareTo(gene2));
  }

  public void testCompareToNative_2() {
    Gene gene1 = new DoubleGene(1.3d, 6.5d);
    gene1.setAllele(new Double(5.9d));
    Gene gene2 = new DoubleGene(5.3d, 6.7d);
    gene2.setAllele(new Double(5.8d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo(gene2.getAllele()),
                 gene1.compareTo(gene2));
  }

  public void testCompareToNative_3() {
    Gene gene1 = new DoubleGene(1.3d, 6.5d);
    gene1.setAllele(new Double(5.9d));
    Gene gene2 = new DoubleGene(5.3d, 6.7d);
    gene2.setAllele(new Double(5.4d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo(gene2.getAllele()),
                 gene1.compareTo(gene2));
  }

  public void testCompareToNative_4() {
    Gene gene1 = new DoubleGene( -1.3d, 6.5d);
    gene1.setAllele(new Double(0.0d));
    Gene gene2 = new DoubleGene( -5.3d, 6.7d);
    gene2.setAllele(new Double( -0.0d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo(gene2.getAllele()),
                 gene1.compareTo(gene2));
  }

  public void testApplyMutation_0() {
    DoubleGene gene = new DoubleGene(0, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.0d);
    assertEquals(50.0d, gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_1()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15.0d));
    Genotype.setConfiguration(config);
    DoubleGene gene = new DoubleGene(0, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.5d);
    assertEquals(50 + (100 - 0) * 0.5d, gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_2()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(15.0d));
    Genotype.setConfiguration(config);
    DoubleGene gene = new DoubleGene(44, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.3d);
    assertEquals(50 + (100 - 44) * 0.3d, gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_3()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(0.5d));
    Genotype.setConfiguration(config);
    DoubleGene gene = new DoubleGene(33, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 1.9d);
    assertEquals(33 + 0.5d * (100 - 33), gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_4()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(0.4d));
    Genotype.setConfiguration(config);
    DoubleGene gene = new DoubleGene(2, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(0, 1.9d);
    assertEquals(2 + 0.4d * (100 - 2), gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_5()
      throws Exception {
    DefaultConfiguration config = new DefaultConfiguration();
    config.setRandomGenerator(new RandomGeneratorForTest(0.8d));
    Genotype.setConfiguration(config);
    DoubleGene gene = new DoubleGene(0, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(0, -1.0d);
    assertEquals(0 + 0.8d * (100 - 0), gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_6() {
    DoubleGene gene = new DoubleGene(0, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(0, -0.4d);
    assertEquals(60 + (100 * ( -0.4d)), gene.doubleValue(), DELTA);
  }

  public void testApplyMutation_7() {
    DoubleGene gene = new DoubleGene(0, 100);
    try {
      gene.applyMutation(0, -0.4d);
      fail();
    } catch (NullPointerException nex) {
      ;//this is OK
    }
  }
}
