package org.jgap.impl;

import java.util.*;
import junit.framework.*;
import junitx.util.PrivateAccessor;
import org.jgap.impl.*;
import org.jgap.*;

/*
 * <p>Title: Test class for BooleanGene class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class BooleanGeneTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public BooleanGeneTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(BooleanGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Gene gene = new BooleanGene();
    //following should be possible without exception
    gene.setAllele(new Boolean(true));
    gene.setAllele(new Boolean(false));
  }

  public void testToString_0() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    assertEquals("true",gene.toString());
  }

  public void testToString_1() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    assertEquals("false",gene.toString());
  }

  public void testGetAllele_0() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(true));
    assertEquals(new Boolean(true), gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new BooleanGene();
    gene.setAllele(new Boolean(false));
    assertEquals(new Boolean(false), gene.getAllele());
  }

  public void testGetAllele_2() {
    Gene gene = new BooleanGene();
    try {
      gene.setAllele(new Integer(100));
      fail();
    }catch (ClassCastException classex) {
      ;//this is OK
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


  public void testBooleanValue_0() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    assertEquals(true, gene1.booleanValue());
  }

  public void testBooleanValue_1() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(false));
    assertEquals(false, gene1.booleanValue());
  }

  public void testBooleanValue_2() {
    BooleanGene gene1 = new BooleanGene();
    gene1.setAllele(null);
    try {
      assertEquals(true, gene1.booleanValue());
      fail();
    } catch (NullPointerException nullex) {
      ;//this is OK
    }
  }

  /**
   * Set Allele to null, no exception should occur
   */
  public void testSetAllele_0() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new BooleanGene();
    try {
      gene1.setAllele("22");
      fail();
    } catch (ClassCastException classex) {
      ;//this is OK
    }
  }

  public void testCompareTo_0() {
    Gene gene1 = new BooleanGene();
    assertEquals(1,gene1.compareTo(null));
  }

  public void testCompareTo_1() {
    Gene gene1 = new BooleanGene();
    Gene gene2 = new BooleanGene();
    assertEquals(0,gene1.compareTo(gene2));
    assertEquals(0,gene2.compareTo(gene1));
  }

  public void testCompareTo_2() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(true));
    assertEquals(0,gene1.compareTo(gene2));
    assertEquals(0,gene2.compareTo(gene1));
    gene1.setAllele(new Boolean(false));
    gene2.setAllele(new Boolean(false));
    assertEquals(0,gene1.compareTo(gene2));
    assertEquals(0,gene2.compareTo(gene1));
  }

  public void testCompareTo_3() {
    Gene gene1 = new BooleanGene();
    gene1.setAllele(new Boolean(true));
    Gene gene2 = new BooleanGene();
    gene2.setAllele(new Boolean(false));
    assertEquals(1,gene1.compareTo(gene2));
    assertEquals(-1,gene2.compareTo(gene1));
  }


}