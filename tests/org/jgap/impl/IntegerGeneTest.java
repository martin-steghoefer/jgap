package org.jgap.impl;

import java.util.*;

import junit.framework.*;
import junitx.util.*;

import org.jgap.*;

/**
 * <p>Title: Tests for IntegerGene class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class IntegerGeneTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public IntegerGeneTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(IntegerGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Gene gene = new IntegerGene(1,100);
    //following should be possible without exception
    gene.setAllele(new Integer(101));
  }

  public void testToString_0() {
    Gene gene = new IntegerGene(1,100);
    gene.setAllele(new Integer(47));
    assertEquals("47",gene.toString());
  }

  public void testToString_1() {
    Gene gene = new IntegerGene(1,100);
    gene.setAllele(new Integer(102));
    int toString = Integer.parseInt(gene.toString());
    assertTrue(toString>=1 && toString <=100);
  }

  public void testGetAllele_0() {
    Gene gene = new IntegerGene(1,100);
    gene.setAllele(new Integer(33));
    assertEquals(new Integer(33), gene.getAllele());
  }

  public void testGetAllele_1() {
    Gene gene = new IntegerGene(1,100);
    gene.setAllele(new Integer(1));
    assertEquals(new Integer(1), gene.getAllele());
  }

  public void testGetAllele_2() {
    Gene gene = new IntegerGene(1,100);
    gene.setAllele(new Integer(100));
    assertEquals(new Integer(100), gene.getAllele());
  }

  public void testEquals_0() {
    Gene gene1 = new IntegerGene(1,100);
    Gene gene2 = new IntegerGene(1,100);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testEquals_1() {
    Gene gene1 = new IntegerGene(1,100);
    assertFalse(gene1.equals(null));
  }

  public void testEquals_2() {
    Gene gene1 = new IntegerGene(1,100);
    assertFalse(gene1.equals(new BooleanGene()));
  }

  public void testEquals_3() {
    Gene gene1 = new IntegerGene(1,100);
    assertFalse(gene1.equals(new Vector()));
  }

  public void testEquals_4() {
    Gene gene1 = new IntegerGene(1,100);
    Gene gene2 = new IntegerGene(1,99);
    assertTrue(gene1.equals(gene2));
    assertTrue(gene2.equals(gene1));
  }

  public void testIntValue_0() {
    IntegerGene gene1 = new IntegerGene(1,10000);
    gene1.setAllele(new Integer(4711));
    assertEquals(4711, gene1.intValue());
  }

  public void testIntValue_1() {
    IntegerGene gene1 = new IntegerGene(1,10000);
    gene1.setAllele(null);
    try {
      assertEquals(0, gene1.intValue());
      fail();
    } catch (NullPointerException nullex) {
      ;//this is OK
    }
  }

  /**
   * Set Allele to null, no exception should occur
   */
  public void testSetAllele_0() {
    Gene gene1 = new IntegerGene(1,10000);
    gene1.setAllele(null);
  }

  public void testSetAllele_1() {
    Gene gene1 = new IntegerGene(1,10000);
    try {
      gene1.setAllele("22");
      fail();
    } catch (ClassCastException classex) {
      ;//this is OK
    }
  }

  public void testNewGene_0() throws Exception {
    Gene gene1 = new IntegerGene(1,10000);
    gene1.setAllele(new Integer(4711));
    Integer lower1 = (Integer)PrivateAccessor.getField(gene1, "m_lowerBounds");
    Integer upper1 = (Integer)PrivateAccessor.getField(gene1, "m_upperBounds");
    Gene gene2 = gene1.newGene(new DefaultConfiguration());
    Integer lower2 = (Integer)PrivateAccessor.getField(gene2, "m_lowerBounds");
    Integer upper2 = (Integer)PrivateAccessor.getField(gene2, "m_upperBounds");
    assertEquals(lower1, lower2);
    assertEquals(upper1, upper2);
  }


}