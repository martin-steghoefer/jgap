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
 * Tests the CompositeGene class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CompositeGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.52 $";

  private static int cleanedUp = 0;

  public static Test suite() {
    TestSuite suite = new TestSuite(CompositeGeneTest.class);
    return suite;
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_4()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    newGene1.setAllele(new Integer(4));
    Gene newGene2 = new IntegerGene(conf, 3, 5);
    newGene2.setAllele(new Integer(2));
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene(conf);
    Gene newGene3 = new IntegerGene(conf, 3, 5);
    newGene3.setAllele(new Integer(1));
    newGene3.setAllele(new Integer(3));
    gene2.addGene(newGene3, false);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testConstruct_0()
      throws Exception {
    //following should be possible without exception
    CompositeGene gene = new CompositeGene(conf);
    assertEquals(null, gene.getGeneTypeAllowed());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_1()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    CompositeGene gene = new CompositeGene();
    assertSame(conf, gene.getConfiguration());
  }

  /**
   * Ensures that a CompositeGene may be added to a CompositeGene
   *
   * @author Audrius Meskauskas
   * @author Klaus Meffert
   * @since 2.0
   * @throws Exception
   */
  public void testAddGene_0()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new CompositeGene(conf), false);
    assertEquals(1, gene.size());
  }

  public void testAddGene_1()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    Gene newGene = new DoubleGene(conf);
    gene.addGene(newGene, false);
    assertEquals(1, gene.size());
    gene.addGene(new DoubleGene(conf, 1.2d, 3.4d), false);
    assertEquals(2, gene.size());
    //try to remove a non existent gene
    gene.removeGeneByIdentity(new DoubleGene(conf));
    assertEquals(2, gene.size());
    //try to add an already existent gene
    try {
      gene.addGene(newGene, false);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_2()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf, new DoubleGene(conf, 2, 3));
    assertEquals(new DoubleGene(conf), gene.getGeneTypeAllowed());
    gene.addGene(new DoubleGene(conf), false);
    assertEquals(1, gene.size());
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_3()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf, new DoubleGene(conf, 2, 3));
    try {
      gene.addGene(new CompositeGene(conf), false);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_4()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new CompositeGene(conf), true);
    assertEquals(1, gene.size());
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_5()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    CompositeGene gene2 = new CompositeGene(conf);
    gene.addGene(gene2, true);
    try {
      gene.addGene(gene2, false);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Adding two different initial genes with strict parameter set is not allowed.
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_6()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new CompositeGene(conf), true);
    try {
      gene.addGene(new CompositeGene(conf), true);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Adding an initial CompositeGene twice should be possible, if they are two
   * newly created instances.
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testAddGene_7()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    gene.addGene(new CompositeGene(conf), true);
    gene.addGene(new CompositeGene(conf), false);
  }

  /**
   *
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testToString_0()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    Gene newGene1 = new DoubleGene(conf);
    newGene1.setAllele(new Double(47.123d));
    gene.addGene(newGene1, false);
    assertEquals("CompositeGene=(" + newGene1.toString() + ")", gene.toString());
    Gene newGene2 = new IntegerGene(conf);
    newGene2.setAllele(new Integer(23456));
    gene.addGene(newGene2, false);
    assertEquals("CompositeGene=(" + newGene1.toString() + gene.GENE_DELIMITER
                 + newGene2.toString() + ")", gene.toString());
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testToString_1()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    assertEquals("CompositeGene=null", gene.toString());
  }

  public void testGetAllele_0()
      throws Exception {
    Gene gene = new CompositeGene(conf);
    //this should be possible without exception
    gene.setAllele(new Vector());
  }

  public void testGetAllele_1()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 1, 5);
    gene.addGene(newGene1, false);
    Vector v = new Vector();
    v.add(new Integer(4));
    gene.setAllele(v);
    v = (Vector) gene.getAllele();
    assertEquals(newGene1.getAllele(), v.elementAt(0));
    assertEquals(1, v.size());
  }

  public void testGetAllele_2()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 1, 5);
    gene.addGene(newGene1, false);
    Gene newGene2 = new DoubleGene(conf, 77.2d, 999.0d);
    gene.addGene(newGene2, false);
    Vector v = new Vector();
    v.add(new Integer(4));
    v.add(new Double(333.5d));
    gene.setAllele(v);
    v = (Vector) gene.getAllele();
    assertEquals(newGene1.getAllele(), v.elementAt(0));
    assertEquals(newGene2.getAllele(), v.elementAt(1));
    assertEquals(2, v.size());
  }

  public void testEquals_0()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    Gene gene2 = new CompositeGene(conf);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    assertFalse(gene1.equals(new BooleanGene(conf)));
  }

  public void testEquals_3()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    assertFalse(gene1.equals(new Vector()));
  }

  /**
   * Tests that no NullPointerException arises.
   *
   * @author Klaus Meffert
   * @since 2.6
   * @throws Exception
   */
  public void testEquals_4_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf, new BooleanGene(conf));
    try {
      gene1.addGene(null, false);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; // this is expected
    }
  }

  public void testEquals_5()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene1, false);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.4
   * @throws Exception
   */
  public void testEquals_5_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    Gene newGene2 = new IntegerGene(conf, 3, 7);
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene2, false);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_6()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    Gene newGene2 = new IntegerGene(conf, 7, 9);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene2, false);
    assertEquals(newGene1.equals(newGene2), gene1.equals(gene2));
    assertEquals(newGene2.equals(newGene1), gene2.equals(gene1));
  }

  public void testEquals_7()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    Gene newGene2 = new DoubleGene(conf, 7.2d, 59.4d);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene2, false);
    assertEquals(newGene1.equals(newGene2), gene1.equals(gene2));
    assertEquals(newGene2.equals(newGene1), gene2.equals(gene1));
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.4
   * @throws Exception
   */
  public void testEquals_8()
      throws Exception {
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    newGene1.setAllele(new Integer(3));
    Gene newGene2 = new IntegerGene(conf, 3, 15);
    newGene2.setAllele(new Integer(9));
    CompositeGene gene1 = new CompositeGene(conf);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene2, false);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testSetAllele_0()
      throws Exception {
    Gene gene = new CompositeGene(conf);
    try {
      gene.setAllele(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAllele_1()
      throws Exception {
    Gene gene = new CompositeGene(conf);
    try {
      gene.setAllele(new Double(2.3d));
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to empty Vector, no exception should occur.
   *
   * @throws Exception
   */
  public void testSetAllele_2()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    gene1.setAllele(new Vector());
  }

  public void testSetAllele_3()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (IllegalArgumentException classex) {
      ; //this is OK
    }
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testSetAllele_4()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    DoubleGene gene2 = new DoubleGene(conf, 1.0d, 3.0d);
    gene2.setAllele(new Double(1.0d));
    gene.addGene(gene2);
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return false;
      }
    });
    List l = new Vector();
    l.add(new Double(2.0d));
    gene.setAllele(l);
    assertEquals(1.0d, ( (Double) gene2.getAllele()).doubleValue(), DELTA);
  }

  /**
   *
   * @author Klaus Meffert
   * @since 2.2
   * @throws Exception
   */
  public void testSetAllele_5()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    DoubleGene gene2 = new DoubleGene(conf, 1.0d, 3.0d);
    gene2.setAllele(new Double(1.0d));
    gene.addGene(gene2);
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue,
                            IChromosome a_chrom, int a_index) {
        return true;
      }
    });
    List l = new Vector();
    l.add(new Double(2.0d));
    gene.setAllele(l);
    assertEquals(2.0d, ( (Double) gene2.getAllele()).doubleValue(), DELTA);
  }

  public void testNewGene_0()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    gene1.addGene(new DoubleGene(conf, 2.05d, 7.53d), false);
    gene1.addGene(new DoubleGene(conf, 128.35d, 155.90d), false);
    gene1.addGene(new IntegerGene(conf, 3, 8), false);
    gene1.addGene(new BooleanGene(conf), false);
    gene1.addGene(new StringGene(conf), false);
    gene1.addGene(new StringGene(conf, 2, 5), false);
    gene1.addGene(new StringGene(conf, 6, 11, "ABC"), false);
    CompositeGene gene2 = (CompositeGene) gene1.newGene();
    assertTrue(gene1.equals(gene2));
    assertEquals(checker, gene2.getConstraintChecker());
    //Remove all genes from gene2 that are contained in gene1.
    //Because they should be equal, gene2 should then be empty.
    //---------------------------------------------------------
    for (int i = 0; i < gene1.size(); i++) {
      gene2.removeGene(gene1.geneAt(i));
    }
    assertEquals(0, gene2.size());
  }

  public void testPersistentPresentation_0()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene gene0 = new DoubleGene(conf, 2.05d, 7.53d);
    gene0.setAllele(new Double(7.52d));
    gene1.addGene(gene0, false);
    gene1.addGene(new DoubleGene(conf, 128.35d, 155.90d), false);
    gene0 = new IntegerGene(conf, 3, 8);
    gene0.setAllele(new Integer(5));
    gene1.addGene(gene0, false);
    gene0 = new BooleanGene(conf);
    gene0.setAllele(Boolean.valueOf(true));
    gene1.addGene(gene0, false);
    gene1.addGene(new StringGene(conf), false);
    gene1.addGene(new StringGene(conf, 2, 5), false);
    gene0 = new StringGene(conf, 6, 11, ":ABC"); // using ':'
    gene0.setAllele("B:BCBCCA");
    gene1.addGene(gene0, false);
    String pres1 = gene1.getPersistentRepresentation();
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  /**
   * Use special characters in alphabet.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentPresentation_0_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene gene0 = new StringGene(conf, 6, 31, "##*~?ß\\/%$§!\".;:_-,");
    gene0.setAllele("#*~?ß\\/%$§!\".;:_-,");
    gene1.addGene(gene0, false);
    Gene gene2 = new StringGene(conf, 8, 33, "w*~?ß\\/%$§!\".;:_-,lL");
    gene2.setAllele("*~?ß/%$§!\".;:_-,");
    gene1.addGene(gene2, false);
    String pres1 = gene1.getPersistentRepresentation();
    CompositeGene gene3 = new CompositeGene(conf);
    gene3.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene3.getPersistentRepresentation();
    assertEquals(pres1, pres2);
    Gene gene3_0 = gene3.geneAt(0);
    assertEquals(gene0, gene3_0);
    Gene gene3_1 = gene3.geneAt(1);
    assertEquals(gene2, gene3_1);
  }

  /**
   * Test a nested persistent representation, including strings.
   *
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void testPersistentPresentation_1()
      throws Exception {
    CompositeGene composite1 = new CompositeGene(conf);
    Gene strgene = new DoubleGene(conf, 2.05d, 7.53d);
    strgene.setAllele(new Double(7.52d));
    composite1.addGene(strgene, false);
    composite1.addGene(new DoubleGene(conf, 128.35d, 155.90d), false);
    strgene = new IntegerGene(conf, 3, 8);
    strgene.setAllele(new Integer(5));
    composite1.addGene(strgene, false);
    strgene = new BooleanGene(conf);
    strgene.setAllele(Boolean.valueOf(true));
    composite1.addGene(strgene, false);
    composite1.addGene(new StringGene(conf), false);
    composite1.addGene(new StringGene(conf, 2, 5), false);
    String string = "<!-- many:various:chars &%$§/()=<>C:CA/ -->";
    strgene = new StringGene(conf, 6, 50, "CA! many:various:chars<>:&%$§/()-=");
    strgene.setAllele(string);
    // remember where, we will check the value later
    int stringPosition = composite1.size();
    composite1.addGene(strgene, false);
    CompositeGene compositeInside = new CompositeGene(conf);
    Gene istrgene = new DoubleGene(conf, 2.05d, 17.53d);
    istrgene.setAllele(new Double(3.33));
    compositeInside.addGene(istrgene, false);
    compositeInside.addGene(new DoubleGene(conf, 128.35d, 155.90d), false);
    istrgene = new IntegerGene(conf, 3, 8);
    istrgene.setAllele(new Integer(5));
    compositeInside.addGene(istrgene, false);
    istrgene = new BooleanGene(conf);
    istrgene.setAllele(Boolean.valueOf(true));
    compositeInside.addGene(istrgene, false);
    compositeInside.addGene(new StringGene(conf), false);
    compositeInside.addGene(new StringGene(conf, 2, 5), false);
    istrgene = new StringGene(conf, 6, 11, "&xyzab<:>");
    String string2 = "<:yzab:>";
    istrgene.setAllele(string2);
    int position2 = compositeInside.size();
    compositeInside.addGene(istrgene, false);
    int whereCompositeGene = composite1.size();
    composite1.addGene(compositeInside);
    String pres1 = composite1.getPersistentRepresentation();
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
    // check the string
    StringGene s = (StringGene) gene2.geneAt(stringPosition);
    assertTrue(string.equals(s.getAllele()));
    // check also in the composite gene
    CompositeGene cg = (CompositeGene) gene2.geneAt(whereCompositeGene);
    s = (StringGene) cg.geneAt(position2);
    assertTrue(string2.equals(s.getAllele()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentPresentation_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    gene1.setValueFromPersistentRepresentation(null);
    assertEquals(0, gene1.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentPresentation_3()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    try {
      gene1.setValueFromPersistentRepresentation("1"
                                                 + CompositeGene.GENE_DELIMITER
                                                 + "2");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Invalid Gene class.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentPresentation_4()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    try {
      gene1.setValueFromPersistentRepresentation("<1"
                                                 + CompositeGene.GENE_DELIMITER
                                                 + "2>");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentPresentation_5()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    try {
      gene1.setValueFromPersistentRepresentation("<1>");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * No closing tag.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentPresentation_6()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    try {
      gene1.setValueFromPersistentRepresentation("<IntegerGene"
                                                 + CompositeGene.GENE_DELIMITER
                                                 + "2<");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Empty representation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPersistentPresentation_7()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    try {
      gene1.setValueFromPersistentRepresentation("<org.jgap.impl.IntegerGene"
                                                 + CompositeGene.GENE_DELIMITER
                                                 + "2:4:4"
                                                 + CompositeGene.GENE_DELIMITER
                                                 + "><>");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      assertEquals(1, gene1.size());
      ; //this is OK
    }
  }

  /**
   * Tests if removal from empty list returns false no matter what to remove.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRemoveGeneByIdentity_0()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    assertFalse(gene1.removeGeneByIdentity(gene1));
    assertFalse(gene1.removeGeneByIdentity(null));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRemoveGeneByIdentity_1()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    DoubleGene gene2 = new DoubleGene(conf, 1.0d, 4.0d);
    gene1.addGene(gene2);
    DoubleGene gene3 = new DoubleGene(conf, 1.0d, 5.0d);
    assertFalse(gene1.removeGeneByIdentity(gene3));
    assertTrue(gene1.removeGeneByIdentity(gene2));
    assertEquals(0, gene1.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCleanup_0()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    CompositeGene gene2 = new CompositeGene(conf) {
      public void cleanup() {
        cleanedUp++;
      }
    };
    CompositeGene gene3 = new CompositeGene(conf) {
      public void cleanup() {
        cleanedUp++;
      }
    };
    gene.addGene(gene2, true);
    gene.addGene(gene3, false);
    gene.cleanup();
    assertEquals(2, gene.size());
    assertEquals(2, cleanedUp);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCleanup_1()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    gene.cleanup();
    assertEquals(0, gene.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetConstraintChecker_0()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
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
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_0()
      throws Exception {
    Gene gene1 = new CompositeGene(conf);
    try {
      gene1.setToRandomValue(null);
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
   * @since 2.2
   */
  public void testSetToRandomValue_1()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    DoubleGene d = new DoubleGene(conf, 0.0d, 1.0d);
    gene1.addGene(d);
    gene1.setToRandomValue(new RandomGeneratorForTesting(0.23d));
    assertEquals(0.23d, d.doubleValue(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    DoubleGene d = new DoubleGene(conf, 0.5d, 1.8d);
    gene1.addGene(d);
    gene1.setToRandomValue(new RandomGeneratorForTesting(0.23d));
    assertEquals( (1.8d - 0.5d) * 0.23d + 0.5d, d.doubleValue(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGene gene = new CompositeGene(conf);
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1()
      throws Exception {
    BaseGene gene = new CompositeGene(conf);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_0()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    Gene newGene2 = new IntegerGene(conf, 3, 7);
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene2, false);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_1()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.addGene(newGene1, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_2()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    Gene newGene2 = new IntegerGene(conf, 3, 5);
    gene2.addGene(newGene2, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_3()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    Gene newGene2 = new IntegerGene(conf, 3, 5);
    newGene2.setAllele(new Integer(2));
    int i = ( (Integer) newGene2.getAllele()).intValue();
    assertTrue(i >= 3 && i <= 5);
  }

  public void testCompareTo_3_2()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    Gene newGene2 = new IntegerGene(conf, 3, 5);
    newGene2.setAllele(new Integer(2));
    gene2.addGene(newGene2, false);
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /**
   * Using application data.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_5()
      throws Exception {
    CompositeGene gene1 = new CompositeGene(conf);
    gene1.setCompareApplicationData(true);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    gene2.setCompareApplicationData(true);
    Gene newGene2 = new IntegerGene(conf, 3, 5);
    gene2.addGene(newGene2, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setApplicationData(new Integer(2));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
    gene2.setApplicationData(new Integer(3));
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
    newGene1.setApplicationData(new Integer(5));
    newGene2.setApplicationData(new Integer(4));
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
    newGene1.setCompareApplicationData(true);
    newGene2.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_6()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    CompositeGene gene1 = new CompositeGene(conf);
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene(conf);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals( -1, gene2.compareTo(gene1));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testApplyMutation_0()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    try {
      gene.applyMutation(0, 0.5d);
      fail();
    }
    catch (RuntimeException rex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetAllele_3()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    assertNull(gene.getInternalValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testHashCode_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    CompositeGene c1 = new CompositeGene(conf);
    CompositeGene c2 = new CompositeGene(conf);
    assertEquals(c1.hashCode(), c2.hashCode());
    Gene newGene1 = new IntegerGene(conf, 3, 5);
    c1.addGene(newGene1, false);
    assertFalse(c1.hashCode() == c2.hashCode());
    assertEquals(c1.hashCode(), c1.hashCode());
    Gene newGene2 = new IntegerGene(conf, 2, 5);
    c2.addGene(newGene2, false);
    assertTrue(c1.hashCode() == c2.hashCode());
    newGene1.setAllele(new Integer(2));
    assertFalse(c1.hashCode() == c2.hashCode());
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
   * @since 3.2.2
   */
  public void testGetGenes_1()
      throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    assertEquals(0, gene.getGenes().size());
    Gene newGene = new DoubleGene(conf);
    gene.addGene(newGene, false);
    assertEquals(1, gene.getGenes().size());
    assertSame(newGene, gene.geneAt(0));
    assertSame(newGene, gene.getGenes().get(0));
    assertEquals(1, gene.size());
    Gene newGene2 = new DoubleGene(conf, 1.2d, 3.4d);
    gene.addGene(newGene2, false);
    assertEquals(2, gene.getGenes().size());
    assertSame(newGene, gene.geneAt(0));
    assertSame(newGene2, gene.geneAt(1));
    assertSame(newGene2, gene.getGenes().get(1));
    Gene newGene3 = new DoubleGene(conf, 1.2d, 3.4d);
    gene.addGene(newGene3, false);
    assertEquals(3, gene.getGenes().size());
  }

}
