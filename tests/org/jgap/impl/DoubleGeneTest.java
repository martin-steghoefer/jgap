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
 * Tests the DoubleGene class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DoubleGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.42 $";

  public void setUp() {
    super.setUp();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DoubleGeneTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Gene gene = new DoubleGene(conf, 1.1d, 100.0d);
    //following should be possible without exception
    gene.setAllele(new Double(101.1d));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_1()
      throws Exception {
    Gene gene = new DoubleGene(conf);
    gene.setAllele(new Double(Double.MAX_VALUE));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_2()
      throws Exception {
    Gene gene = new DoubleGene(conf);
    gene.setAllele(new Double( - (Double.MAX_VALUE / 2)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_3()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    Gene gene = new DoubleGene();
    assertSame(conf, gene.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testToString_0()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.2d, 99.7d);
    gene.setAllele(new Double(47.3d));
    assertEquals("DoubleGene(1.2,99.7)=47.3", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testToString_1()
      throws Exception {
    Gene gene = new DoubleGene(conf, -100.0d, 100.0d);
    gene.setAllele(new Double( -88.75286d));
    assertEquals("DoubleGene(-100.0,100.0)=-88.75286", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testToString_2()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.2d, 99.7d);
    assertEquals("DoubleGene(1.2,99.7)=null", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetAllele_0()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.9d, 100.4d);
    gene.setAllele(new Double(33.0d));
    assertEquals(new Double(33.0d), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetAllele_1()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.8d, 100.1d);
    gene.setAllele(new Double(1.9d));
    assertEquals(new Double(1.9d), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetAllele_2()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.0d, 100.0d);
    gene.setAllele(new Double(100.0d));
    assertEquals(new Double(100.0d), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_0()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.1d, 100.2d);
    Gene gene2 = new DoubleGene(conf, 1.1d, 100.2d);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_1()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.9d, 100.4d);
    assertFalse(gene1.equals(null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_2()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 11.2d, 100.7d);
    assertFalse(gene1.equals(new BooleanGene(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_3()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.0d, 100.7d);
    assertFalse(gene1.equals(new Vector()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_4()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.2d, 100.3d);
    Gene gene2 = new DoubleGene(conf, 1.2d, 99.5d);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_5()
      throws Exception {
    Gene gene1 = new FixedBinaryGene(conf, 5);
    Gene gene2 = new DoubleGene(conf, 1, 99);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_6()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1, 99);
    Gene gene2 = new BooleanGene(conf);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_7()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1, 99);
    Gene gene2 = new IntegerGene(conf);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_8()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Gene gene1 = new DoubleGene(conf, 1.2d, 100.3d);
    gene1.setAllele(new Double(1));
    Gene gene2 = new DoubleGene(conf, 1.2d, 99.5d);
    gene2.setAllele(new Double( -1));
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testDoubleValue_0()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, 1.0d, 10000.0d);
    gene1.setAllele(new Double(4711.0d));
    assertEquals(4711.0d, gene1.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testDoubleValue_1()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, 1.765d, 10000.0d);
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
   * Set allele to null, no exception should occur.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetAllele_0()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.0d, 10000.0d);
    gene1.setAllele(null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetAllele_1()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.0d, 10000.0d);
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
   */
  public void testSetAllele_2()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.0d, 10000.0d);
    try {
      gene1.setAllele(new Integer(22));
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * Call setAllele with need of mapping to bounds.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetAllele_3()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.0d, 1000.0d);
    gene1.setAllele(new Double(2000.0d));
  }

  /**
   * Call setAllele with need of mapping to bounds.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testSetAllele_4()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, 1.0d, 1.0d);
    gene1.setAllele(new Double(5.7d));
    assertEquals(1.0d, gene1.doubleValue(), DELTA);
  }

  /**
   * Call setAllele with need of mapping to bounds.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testSetAllele_5()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, 0.0d, 0.0d);
    gene1.setAllele(new Double(5.7d));
    assertEquals(0.0d, gene1.doubleValue(), DELTA);
  }

  /**
   * Call setAllele with need of mapping to bounds.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testSetAllele_6()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, -12.5d, -12.5d);
    gene1.setAllele(new Double(5.7d));
    assertEquals(-12.5d, gene1.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testNewGene_0()
      throws Exception {
    DoubleGene gene1 = new DoubleGene(conf, 1.0d, 10000.0d);
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    gene1.setAllele(new Double(4711.0d));
    Double lower1 = (Double) privateAccessor.getField(gene1,
        "m_lowerBound");
    Double upper1 = (Double) privateAccessor.getField(gene1,
        "m_upperBound");
    DoubleGene gene2 = (DoubleGene) gene1.newGene();
    Double lower2 = (Double) privateAccessor.getField(gene2,
        "m_lowerBound");
    Double upper2 = (Double) privateAccessor.getField(gene2,
        "m_upperBound");
    assertEquals(lower1, lower2);
    assertEquals(upper1, upper2);
    assertEquals(checker, gene2.getConstraintChecker());
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_0()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    gene1.setAllele(new Double(4.5d));
    String pres1 = gene1.getPersistentRepresentation();
    Gene gene2 = new DoubleGene(conf);
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_1()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    gene1.setValueFromPersistentRepresentation(null);
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testPersistentRepresentation_2()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    try {
      gene1.setValueFromPersistentRepresentation("2.3");
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
   */
  public void testPersistentRepresentation_3()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    try {
      gene1.setValueFromPersistentRepresentation("2.3"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "4.6"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "6,5");
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
   */
  public void testPersistentRepresentation_4()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    try {
      gene1.setValueFromPersistentRepresentation("2.3"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "b"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "a");
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
   */
  public void testPersistentRepresentation_5()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 2.05d, 7.53d);
    try {
      gene1.setValueFromPersistentRepresentation("a"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "b"
                                                 + DoubleGene.
                                                 PERSISTENT_FIELD_DELIMITER
                                                 + "a");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCleanup_0()
      throws Exception {
    //cleanup should do nothing!
    Gene gene = new DoubleGene(conf, 1.3d, 6.5d);
    Gene copy = gene.newGene();
    gene.cleanup();
    assertEquals(copy, gene);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_0()
      throws Exception {
    Gene gene = new DoubleGene(conf, 1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    gene.setToRandomValue(new RandomGeneratorForTesting(0.789d));
    assertEquals(new Double(0.789d * (6.5d - 1.3d) + 1.3d), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_1()
      throws Exception {
    Gene gene = new DoubleGene(conf, -1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.258d));
    gene.setToRandomValue(new RandomGeneratorForTesting(0.014));
    assertEquals(new Double(0.014d * (6.5d + 1.3d) - 1.3d), gene.getAllele());
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_2()
      throws Exception {
    /**@todo test needed any longer?*/
    Configuration conf = new ConfigurationForTesting();
    Gene gene = new DoubleGene(conf, -1.3d, -0.5d);
    gene.setAllele(new Double(5.8d));
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.258d));
    gene.setToRandomValue(new RandomGeneratorForTesting(0.83d));
    assertEquals(new Double(0.83d * ( -0.5d + 1.3d) - 1.3d), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_3()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    gene.setToRandomValue(new RandomGeneratorForTesting(0.478d));
    if (gene.doubleValue() < 1.3d
        || gene.doubleValue() > 6.5d) {
      fail();
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetToRandomValue_4()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 1.3d, 6.5d);
    gene.setAllele(new Double(5.8d));
    gene.setToRandomValue(new RandomGeneratorForTesting(8.584d));
    if (gene.doubleValue() < 1.3d
        || gene.doubleValue() > 6.5d) {
      fail();
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareToNative_0()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.3d, 6.5d);
    gene1.setAllele(new Double(5.8d));
    Gene gene2 = new DoubleGene(conf, 5.3d, 6.7d);
    gene2.setAllele(new Double(5.9d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo( (Double) gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareToNative_1()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.3d, 6.5d);
    gene1.setAllele(new Double(5.8d));
    Gene gene2 = new DoubleGene(conf, 5.3d, 6.7d);
    gene2.setAllele(new Double(5.8d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo( (Double) gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareToNative_2()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.3d, 6.5d);
    gene1.setAllele(new Double(5.9d));
    Gene gene2 = new DoubleGene(conf, 5.3d, 6.7d);
    gene2.setAllele(new Double(5.8d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo( (Double) gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareToNative_3()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, 1.3d, 6.5d);
    gene1.setAllele(new Double(5.9d));
    Gene gene2 = new DoubleGene(conf, 5.3d, 6.7d);
    gene2.setAllele(new Double(5.4d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo( (Double) gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareToNative_4()
      throws Exception {
    Gene gene1 = new DoubleGene(conf, -1.3d, 6.5d);
    gene1.setAllele(new Double(0.0d));
    Gene gene2 = new DoubleGene(conf, -5.3d, 6.7d);
    gene2.setAllele(new Double( -0.0d));
    assertEquals( ( (Double) gene1.getAllele()).compareTo( (Double) gene2.
        getAllele()), gene1.compareTo(gene2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_0()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.0d);
    assertEquals(50.0d, gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_1()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(15.0d));
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.5d);
    assertEquals(50 + (100 - 0) * 0.5d, gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_2()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(15.0d));
    DoubleGene gene = new DoubleGene(conf, 44, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 0.3d);
    assertEquals(50 + (100 - 44) * 0.3d, gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_3()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.5d));
    DoubleGene gene = new DoubleGene(conf, 33, 100);
    gene.setAllele(new Double(50));
    gene.applyMutation(0, 1.9d);
    assertEquals(33 + 0.5d * (100 - 33), gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_4()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.4d));
    DoubleGene gene = new DoubleGene(conf, 2, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(0, 1.9d);
    assertEquals(2 + 0.4d * (100 - 2), gene.doubleValue(), DELTA);
  }

  /**
   * Exceed bounds for applyMutation to force randomized setting of allele.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_5()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.8d));
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(1, -1.0d);
    assertEquals(0 + 0.8d * (100 - 0), gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_6()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    gene.setAllele(new Double(60));
    gene.applyMutation(77, -0.4d);
    assertEquals(60 + (100 * ( -0.4d)), gene.doubleValue(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testApplyMutation_7()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    try {
      gene.applyMutation(0, -0.4d);
      fail();
    }
    catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetConstraintChecker_0()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 0, 100);
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testHashCode_0()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, 0, 100);
    assertEquals( -3, gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testHashCode_1()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    DoubleGene c1 = new DoubleGene(conf);
    DoubleGene c2 = new DoubleGene(conf);
    assertEquals(c1.hashCode(), c2.hashCode());
    c1.setAllele(new Double(2));
    assertFalse(c1.hashCode() == c2.hashCode());
    assertEquals(c1.hashCode(), c1.hashCode());
    c2.setAllele(new Double(2));
    assertTrue(c1.hashCode() == c2.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGene gene = new DoubleGene(conf);
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
    BaseGene gene = new DoubleGene(conf);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }


  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testBounds_0()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf, -3.4d, +5.7d);
    assertEquals( -3.4d, gene.getLowerBound(), DELTA);
    assertEquals( 5.7d, gene.getUpperBound(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testBounds_1()
      throws Exception {
    DoubleGene gene = new DoubleGene(conf);
    assertEquals( - (Double.MAX_VALUE / 2), gene.getLowerBound(), DELTA);
    assertEquals(Double.MAX_VALUE / 2, gene.getUpperBound(), DELTA);
  }

  class GeneConstraintChecker
      implements IGeneConstraintChecker {
    public boolean verify(Gene a_gene, Object a_alleleValue,
                          IChromosome a_chrom, int a_index) {
      return true;
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void testMapValueToWithinBoundsSupportsFullDoubleRange()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.2d));
    double lower = Double.MIN_VALUE + 1;
    double upper = Double.MAX_VALUE;
    DoubleGene gene = new DoubleGene(conf, lower, upper);
    gene.setAllele(Double.MIN_VALUE);
    double expectedValue = (lower + (0.2d * (upper - lower)));
    assertEquals(new Double(expectedValue), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void testMapValueToWithinBoundsSupportsFullDoubleRange2()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.9999d));
    double lower = Double.MIN_VALUE + 1.0d;
    double upper = Double.MAX_VALUE;
    DoubleGene gene = new DoubleGene(conf, lower, upper);
    gene.setAllele(Double.MIN_VALUE);
    double expectedValue = (lower + (0.9999d * (upper - lower)));
    assertEquals(new Double(expectedValue), gene.getAllele());
  }


  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void testMapValueToWithinBoundsSupportsFullDoubleRange3()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.9999d));
    double lower = -Double.MAX_VALUE/2 + 2;
    double upper = Double.MAX_VALUE;
    DoubleGene gene = new DoubleGene(conf, lower, upper);
    gene.setAllele(-Double.MAX_VALUE);
    double expectedValue = (lower + (0.9999d * (upper - lower)));
    assertEquals(new Double(expectedValue), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void testMapValueToWithinBoundsSupportsFullDoubleRange4()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.9999d));
    double lower = -Double.MAX_VALUE/1.1;
    double upper = Double.MAX_VALUE;
    DoubleGene gene = new DoubleGene(conf, lower, upper);
    gene.setAllele(-Double.MAX_VALUE);
    double expectedValue = (lower + (0.9999d * (upper - lower)));
    assertEquals(new Double(expectedValue), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void testMapValueToWithinBoundsSupportsFullDoubleRange5()
      throws Exception {
    conf.setRandomGenerator(new RandomGeneratorForTesting(0.0000001d));
    double lower = -Double.MAX_VALUE/1.1;
    double upper = Double.MAX_VALUE;
    DoubleGene gene = new DoubleGene(conf, lower, upper);
    gene.setAllele(-Double.MAX_VALUE);
    double expectedValue = (lower + (0.0000001d * (upper - lower)));
    assertEquals(new Double(expectedValue), gene.getAllele());
  }
}
