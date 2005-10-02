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
 * Tests the CompositeGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CompositeGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.30 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CompositeGeneTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    //following should be possible without exception
    CompositeGene gene = new CompositeGene();
    assertEquals(null, gene.getGeneTypeAllowed());
  }

  /**
   * Ensures that a CompositeGene may be added to a CompositeGene
   * @author Audrius Meskauskas
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testAddGene_0() {
    CompositeGene gene = new CompositeGene();
    gene.addGene(new CompositeGene(), false);
    assertEquals(1, gene.size());
  }

  public void testAddGene_1() {
    CompositeGene gene = new CompositeGene();
    Gene newGene = new DoubleGene();
    gene.addGene(newGene, false);
    assertEquals(1, gene.size());
    gene.addGene(new DoubleGene(1.2d, 3.4d), false);
    assertEquals(2, gene.size());
    //try to remove a non existent gene
    gene.removeGeneByIdentity(new DoubleGene());
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
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddGene_2() {
    CompositeGene gene = new CompositeGene(new DoubleGene(2, 3));
    assertEquals(new DoubleGene(), gene.getGeneTypeAllowed());
    gene.addGene(new DoubleGene(), false);
    assertEquals(1, gene.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddGene_3() {
    CompositeGene gene = new CompositeGene(new DoubleGene(2, 3));
    try {
      gene.addGene(new CompositeGene(), false);
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
  public void testAddGene_4() {
    CompositeGene gene = new CompositeGene();
    gene.addGene(new CompositeGene(), true);
    assertEquals(1, gene.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddGene_5() {
    CompositeGene gene = new CompositeGene();
    CompositeGene gene2 = new CompositeGene();
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
   * Adding two different initial genes with strict parameter set is not
   * allowed
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddGene_6() {
    CompositeGene gene = new CompositeGene();
    gene.addGene(new CompositeGene(), true);
    try {
      gene.addGene(new CompositeGene(), true);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Adding an initial CompositeGene twice should be possible, if they are two
   * newly created instances
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddGene_7() {
    CompositeGene gene = new CompositeGene();
    gene.addGene(new CompositeGene(), true);
    gene.addGene(new CompositeGene(), false);
  }

  /**
   * @author Klaus Meffert
   */
  public void testToString_0() {
    CompositeGene gene = new CompositeGene();
    Gene newGene1 = new DoubleGene();
    newGene1.setAllele(new Double(47.123d));
    gene.addGene(newGene1, false);
    assertEquals("CompositeGene=(" + newGene1.toString() + ")", gene.toString());
    Gene newGene2 = new IntegerGene();
    newGene2.setAllele(new Integer(23456));
    gene.addGene(newGene2, false);
    assertEquals("CompositeGene=(" + newGene1.toString() + gene.GENE_DELIMITER +
                 newGene2.toString() + ")", gene.toString());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testToString_1() {
    CompositeGene gene = new CompositeGene();
    assertEquals("CompositeGene=null", gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new CompositeGene();
    //this should be possible without exception
    gene.setAllele(new Vector());
  }

  public void testGetAllele_1() {
    CompositeGene gene = new CompositeGene();
    Gene newGene1 = new IntegerGene(1, 5);
    gene.addGene(newGene1, false);
    Vector v = new Vector();
    v.add(new Integer(4));
    gene.setAllele(v);
    v = (Vector) gene.getAllele();
    assertEquals(newGene1.getAllele(), v.elementAt(0));
    assertEquals(1, v.size());
  }

  public void testGetAllele_2() {
    CompositeGene gene = new CompositeGene();
    Gene newGene1 = new IntegerGene(1, 5);
    gene.addGene(newGene1, false);
    Gene newGene2 = new DoubleGene(77.2d, 999.0d);
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

  public void testEquals_0() {
    Gene gene1 = new CompositeGene();
    Gene gene2 = new CompositeGene();
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new CompositeGene();
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new CompositeGene();
    assertFalse(gene1.equals(new BooleanGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new CompositeGene();
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    CompositeGene gene1 = new CompositeGene();
    gene1.addGene(null, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(null, false);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_5() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene1, false);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_5_2() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    Gene newGene2 = new IntegerGene(3, 7);
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene2, false);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testEquals_6() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    Gene newGene2 = new IntegerGene(7, 9);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene2, false);
    assertEquals(newGene1.equals(newGene2), gene1.equals(gene2));
    assertEquals(newGene2.equals(newGene1), gene2.equals(gene1));
  }

  public void testEquals_7() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    Gene newGene2 = new DoubleGene(7.2d, 59.4d);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene2, false);
    assertEquals(newGene1.equals(newGene2), gene1.equals(gene2));
    assertEquals(newGene2.equals(newGene1), gene2.equals(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_8() {
    Gene newGene1 = new IntegerGene(3, 5);
    newGene1.setAllele(new Integer(3));
    Gene newGene2 = new IntegerGene(3, 15);
    newGene2.setAllele(new Integer(9));
    CompositeGene gene1 = new CompositeGene();
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene2, false);
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  public void testSetAllele_0() {
    Gene gene = new CompositeGene();
    try {
      gene.setAllele(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testSetAllele_1() {
    Gene gene = new CompositeGene();
    try {
      gene.setAllele(new Double(2.3d));
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to empty Vector, no exception should occur
   */
  public void testSetAllele_2() {
    Gene gene1 = new CompositeGene();
    gene1.setAllele(new Vector());
  }

  public void testSetAllele_3() {
    Gene gene1 = new CompositeGene();
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (IllegalArgumentException classex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_4() {
    CompositeGene gene = new CompositeGene();
    DoubleGene gene2 = new DoubleGene(1.0d, 3.0d);
    gene2.setAllele(new Double(1.0d));
    gene.addGene(gene2);
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
        return false;
      }
    });
    List l = new Vector();
    l.add(new Double(2.0d));
    gene.setAllele(l);
    assertEquals(1.0d, ( (Double) gene2.getAllele()).doubleValue(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetAllele_5() {
    CompositeGene gene = new CompositeGene();
    DoubleGene gene2 = new DoubleGene(1.0d, 3.0d);
    gene2.setAllele(new Double(1.0d));
    gene.addGene(gene2);
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
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
    CompositeGene gene1 = new CompositeGene();
    IGeneConstraintChecker checker = new GeneConstraintChecker();
    gene1.setConstraintChecker(checker);
    gene1.addGene(new DoubleGene(2.05d, 7.53d), false);
    gene1.addGene(new DoubleGene(128.35d, 155.90d), false);
    gene1.addGene(new IntegerGene(3, 8), false);
    gene1.addGene(new BooleanGene(), false);
    gene1.addGene(new StringGene(), false);
    gene1.addGene(new StringGene(2, 5), false);
    gene1.addGene(new StringGene(6, 11, "ABC"), false);
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
    CompositeGene gene1 = new CompositeGene();
    Gene gene0 = new DoubleGene(2.05d, 7.53d);
    gene0.setAllele(new Double(7.52d));
    gene1.addGene(gene0, false);
    gene1.addGene(new DoubleGene(128.35d, 155.90d), false);
    gene0 = new IntegerGene(3, 8);
    gene0.setAllele(new Integer(5));
    gene1.addGene(gene0, false);
    gene0 = new BooleanGene();
    gene0.setAllele(new Boolean(true));
    gene1.addGene(gene0, false);
    gene1.addGene(new StringGene(), false);
    gene1.addGene(new StringGene(2, 5), false);
    gene0 = new StringGene(6, 11, ":ABC"); // using ':'
    gene0.setAllele("B:BCBCCA");
    gene1.addGene(gene0, false);
    String pres1 = gene1.getPersistentRepresentation();
    CompositeGene gene2 = new CompositeGene();
    gene2.setValueFromPersistentRepresentation(pres1);
    String pres2 = gene2.getPersistentRepresentation();
    assertEquals(pres1, pres2);
  }

  /**
   * Test a nested persistent representation, including strings
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void testPersistentPresentation_1()
      throws Exception {
    CompositeGene composite1 = new CompositeGene();
    Gene strgene = new DoubleGene(2.05d, 7.53d);
    strgene.setAllele(new Double(7.52d));

    composite1.addGene(strgene, false);
    composite1.addGene(new DoubleGene(128.35d, 155.90d), false);
    strgene = new IntegerGene(3, 8);
    strgene.setAllele(new Integer(5));
    composite1.addGene(strgene, false);
    strgene = new BooleanGene();
    strgene.setAllele(new Boolean(true));

    composite1.addGene(strgene, false);
    composite1.addGene(new StringGene(), false);
    composite1.addGene(new StringGene(2, 5), false);

    String string = "<!-- many:various:chars &%$§/()=<>C:CA/ -->";
    strgene = new StringGene(6, 50, "CA! many:various:chars<>:&%$§/()-=");
    strgene.setAllele(string);

    // remember where, we will check the value later
    int stringPosition = composite1.size();
    composite1.addGene(strgene, false);

    CompositeGene compositeInside = new CompositeGene();

    Gene istrgene = new DoubleGene(2.05d, 17.53d);
    istrgene.setAllele(new Double(3.33));
    compositeInside.addGene(istrgene, false);

    compositeInside.addGene(new DoubleGene(128.35d, 155.90d), false);
    istrgene = new IntegerGene(3, 8);
    istrgene.setAllele(new Integer(5));
    compositeInside.addGene(istrgene, false);
    istrgene = new BooleanGene();
    istrgene.setAllele(new Boolean(true));
    compositeInside.addGene(istrgene, false);
    compositeInside.addGene(new StringGene(), false);
    compositeInside.addGene(new StringGene(2, 5), false);
    istrgene = new StringGene(6, 11, "&xyzab<:>");

    String string2 = "<:yzab:>";
    istrgene.setAllele(string2);
    int position2 = compositeInside.size();
    compositeInside.addGene(istrgene, false);

    int whereCompositeGene = composite1.size();
    composite1.addGene(compositeInside);

    String pres1 = composite1.getPersistentRepresentation();
    CompositeGene gene2 = new CompositeGene();

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
    CompositeGene gene1 = new CompositeGene();
    gene1.setValueFromPersistentRepresentation(null);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPersistentPresentation_3()
       {
    CompositeGene gene1 = new CompositeGene();
    try {
      gene1.setValueFromPersistentRepresentation("1" +
                                                 CompositeGene.GENE_DELIMITER +
                                                 "2");
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
  public void testPersistentPresentation_4() {
    CompositeGene gene1 = new CompositeGene();
    try {
      gene1.setValueFromPersistentRepresentation("<1" +
                                                 CompositeGene.GENE_DELIMITER +
                                                 "2>");
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
  public void testPersistentPresentation_5() {
    CompositeGene gene1 = new CompositeGene();
    try {
      gene1.setValueFromPersistentRepresentation("<1>");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Tests if removal from empty list returns false no matter what to remove
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRemoveGeneByIdentity_0() {
    CompositeGene gene1 = new CompositeGene();
    assertFalse(gene1.removeGeneByIdentity(gene1));
    assertFalse(gene1.removeGeneByIdentity(null));
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRemoveGeneByIdentity_1() {
    CompositeGene gene1 = new CompositeGene();
    DoubleGene gene2 = new DoubleGene(1.0d, 4.0d);
    gene1.addGene(gene2);
    DoubleGene gene3 = new DoubleGene(1.0d, 5.0d);
    assertFalse(gene1.removeGeneByIdentity(gene3));
    assertTrue(gene1.removeGeneByIdentity(gene2));
    assertEquals(0, gene1.size());
  }

  static int cleanedUp = 0;

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCleanup_0() {
    CompositeGene gene = new CompositeGene();
    CompositeGene gene2 = new CompositeGene() {
      public void cleanup() {
        cleanedUp++;
      }
    };
    CompositeGene gene3 = new CompositeGene() {
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
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCleanup_1() {
    CompositeGene gene = new CompositeGene();
    gene.cleanup();
    assertEquals(0, gene.size());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetConstraintChecker_0() {
    CompositeGene gene = new CompositeGene();
    assertNull(gene.getConstraintChecker());
    gene.setConstraintChecker(new IGeneConstraintChecker() {
      public boolean verify(Gene a_gene, Object a_alleleValue)
          throws RuntimeException {
        return false;
      }
    });
    assertNotNull(gene.getConstraintChecker());
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_0() {
    Gene gene1 = new CompositeGene();
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
    CompositeGene gene1 = new CompositeGene();
    DoubleGene d = new DoubleGene(0.0d, 1.0d);
    gene1.addGene(d);
    gene1.setToRandomValue(new RandomGeneratorForTest(0.23d));
    assertEquals(0.23d, d.doubleValue(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testSetToRandomValue_2() {
    CompositeGene gene1 = new CompositeGene();
    DoubleGene d = new DoubleGene(0.5d, 1.8d);
    gene1.addGene(d);
    gene1.setToRandomValue(new RandomGeneratorForTest(0.23d));
    assertEquals( (1.8d - 0.5d) * 0.23d + 0.5d, d.doubleValue(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0() {
    BaseGene gene = new CompositeGene();
    assertEquals(0.0, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1() {
    BaseGene gene = new CompositeGene();
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
    gene.setEnergy( -55.8);
    assertEquals( -55.8, gene.getEnergy(), DELTA);
    gene.setEnergy(0.5);
    gene.setEnergy(0.8);
    assertEquals(0.8, gene.getEnergy(), DELTA);
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_0() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    Gene newGene2 = new IntegerGene(3, 7);
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene2, false);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_1() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.addGene(newGene1, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_2() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    Gene newGene2 = new IntegerGene(3, 5);
    gene2.addGene(newGene2, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));

  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_3() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    Gene newGene2 = new IntegerGene(3, 5);
    newGene2.setAllele(new Integer(2));
    gene2.addGene(newGene2, false);
    assertEquals(-1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /**
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_4() {
    CompositeGene gene1 = new CompositeGene();
    Gene newGene1 = new IntegerGene(3, 5);
    newGene1.setAllele(new Integer(1));
    Gene newGene2 = new IntegerGene(3, 5);
    newGene2.setAllele(new Integer(2));
    gene1.addGene(newGene1, false);
    gene1.addGene(newGene2, false);
    CompositeGene gene2 = new CompositeGene();
    Gene newGene3 = new IntegerGene(3, 5);
    newGene3.setAllele(new Integer(1));
    Gene newGene4 = new IntegerGene(3, 5);
    newGene3.setAllele(new Integer(-2));
    gene2.addGene(newGene3, false);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));
  }

  /**
   * Using application data
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCompareTo_5() {
    CompositeGene gene1 = new CompositeGene();
    gene1.setCompareApplicationData(true);
    Gene newGene1 = new IntegerGene(3, 5);
    gene1.addGene(newGene1, false);
    CompositeGene gene2 = new CompositeGene();
    gene2.setCompareApplicationData(true);
    Gene newGene2 = new IntegerGene(3, 5);
    gene2.addGene(newGene2, false);
    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
    gene1.setApplicationData(new Integer(2));
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));
    gene2.setApplicationData(new Integer(3));
    assertEquals(-1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
    newGene1.setApplicationData(new Integer(5));
    newGene2.setApplicationData(new Integer(4));
    assertEquals(-1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
    newGene1.setCompareApplicationData(true);
    newGene2.setCompareApplicationData(true);
    assertEquals(1, gene1.compareTo(gene2));
    assertEquals(-1, gene2.compareTo(gene1));

  }

  class GeneConstraintChecker implements IGeneConstraintChecker {
    public boolean verify(Gene a_gene, Object a_alleleValue)
        throws RuntimeException {
      return true;
    }
  }
}
