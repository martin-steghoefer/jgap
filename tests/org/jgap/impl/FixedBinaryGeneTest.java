/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import org.jgap.*;
import junit.framework.*;

/**
 * Test class for FixedBinaryGene class
 *
 * @author Klaus Meffert
 * @author vamsi
 * @since 2.0
 */
public class FixedBinaryGeneTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public FixedBinaryGeneTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(FixedBinaryGeneTest.class);
    return suite;
  }

  public void testConstruct_0() {
    //following should be possible without exception
    FixedBinaryGene gene = new FixedBinaryGene(1);
    gene = new FixedBinaryGene(10);
    gene = new FixedBinaryGene(1000);
    gene = new FixedBinaryGene(100000);
  }

  public void testConstruct_1() {
    try {
      Gene gene = new FixedBinaryGene(0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  public void testConstruct_2() {
    try {
      Gene gene = new FixedBinaryGene( -5);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testConstruct_3() {
    //following should be possible without exception
    int i = 0;
    FixedBinaryGene gene = new FixedBinaryGene(5);
    for (i = 0; i < 4; i++) {
      //assert that we have
      assertFalse(gene.getBit(i));
    }
    assertEquals("[0,0,0,0,0]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testConstruct_4() {
    FixedBinaryGene gene = new FixedBinaryGene(6);
    assertEquals(1, gene.size());
    assertEquals(1, (gene.getValue()).length);
    assertEquals("[0,0,0,0,0,0]", gene.toString());
  }

  /**
   * Buffer allocation test case.
   * @author vamsi
   */
  public void testConstruct_5() {
    FixedBinaryGene gene;
    gene = new FixedBinaryGene(32);
    assertEquals(1, gene.size());
    gene = new FixedBinaryGene(81);
    assertEquals(3, gene.size());
  }

  /**
   * @author vamsi
   */
  public void testToString_0() {
    Gene gene = new FixedBinaryGene(1);
    gene.setAllele(new int[] {1});
    assertEquals("[1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testToString_1() {
    Gene gene = new FixedBinaryGene(3);
    gene.setAllele(new int[] {1, 0, 1});
    assertEquals("[1,0,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testGetAllele_0() {
    Gene gene = new FixedBinaryGene(1);
    int[] value = new int[] {
        0};
    gene.setAllele(value);
    assertEquals(value.length,((int[])gene.getAllele()).length);
    for (int i=0;i<value.length;i++) {
      assertEquals(value[i], ((int[])gene.getAllele())[i]);
    }
  }

  /**
   * @author vamsi
   */
  public void testGetAllele_1() {
    Gene gene = new FixedBinaryGene(2);
    try {
      gene.setAllele(new Integer(100));
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testEquals_0() {
    Gene gene1 = new FixedBinaryGene(1);
    Gene gene2 = new FixedBinaryGene(1);
    assertTrue(gene1.equals(gene2));
  }

  /**
   * @author vamsi
   */
  public void testEquals_1() {
    Gene gene1 = new FixedBinaryGene(1);
    assertFalse(gene1.equals(null));
  }

  /**
   * @author vamsi
   */
  public void testEquals_2() {
    Gene gene1 = new FixedBinaryGene(2);
    gene1.setAllele(new int[] {1, 0});
    Gene gene2 = new FixedBinaryGene(2);
    gene2.setAllele(new int[] {0, 1});
    assertFalse(gene1.equals(gene2));
    assertFalse(gene2.equals(gene1));
  }

  /**
   * @author vamsi
   */
  public void testEquals_3() {
    Gene gene1 = new FixedBinaryGene(5);
    assertFalse(gene1.equals(new IntegerGene()));
  }

  /**
   * @author vamsi
   */
  public void testIntValues_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    assertFalse(gene1.getIntValues() == null);
  }

  /**
   * @author vamsi
   */
  public void testIntValues_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(2);
    int[] values = gene1.getIntValues();
    int i;
    for (i = 0; i < values.length; i++) {
      assertEquals(0, values[i]);
    }
  }

  /**
   * @author vamsi
   */
  public void testIntValues_2() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 1, 0});
    assertEquals(false, gene1.getBit(0));
    assertEquals(true, gene1.getBit(1));
    assertEquals(false, gene1.getBit(2));
    assertEquals(3, gene1.getLength());
  }

  /**
   * @author vamsi
   */
  public void testSetAllele_0() {
    Gene gene1 = new FixedBinaryGene(1);
    try {
      gene1.setAllele(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testSetAllele_1() {
    Gene gene1 = new FixedBinaryGene(1);
    try {
      gene1.setAllele("22");
      fail();
    }
    catch (ClassCastException classex) {
      ; //this is OK
    }
  }

  /**
   * Set Allele to int values, no exception should occur
   * @author vamsi
   */
  public void testSetAllele_2() {
    Gene gene1 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {0, 0, 1});
  }

  /***
   * The implementation should throw an exception if the alle size is more
   * than the size of the created gene.
   * @author vamsi
   */
  public void testSetAllele_3() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    try {
      gene1.setAllele(new int[] {0, 1, 1, 1, 1, 1});
      fail();
    }
    catch (Exception e) {
      /*Some Exception should be thrown*/

    }
  }

  /**
   * @author vamsi
   */
  public void testSetAllele_4() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    try {
      gene1.setAllele(new int[] {0, 3, 1, 4});
      fail();
    }
    catch (Exception e) {
      /**
       *This is ok
       */
    }
  }

  /**
   * @author vamsi
   */
  public void testSetAllele_5() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    try {
      gene1.setAllele(new int[] {0, 0});
      fail();
    }
    catch (Exception e) {
      /*This is ok.*/
    }

  }

  /**
   *Comparision should return 0 if same, -1 if less 1 if more
   * @author vamsi
   */
  public void testCompareTo_0() {
    FixedBinaryGene gene1 = new FixedBinaryGene(4);
    FixedBinaryGene gene2 = new FixedBinaryGene(4);

    gene1.setAllele(new int[] {1, 0, 1, 0});
    gene2.setAllele(new int[] {1, 1, 0, 1});

    assertEquals(1, gene1.compareTo(null));
    assertEquals( -1, gene1.compareTo(gene2));
    assertEquals(1, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_1() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = new FixedBinaryGene(3);

    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_2() {

    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    FixedBinaryGene gene2 = new FixedBinaryGene(3);
    gene1.setAllele(new int[] {1, 1, 1});
    gene2.setAllele(new int[] {1, 1, 1});

    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));

    gene1.setAllele(new int[] {0, 0, 0});
    gene2.setAllele(new int[] {0, 0, 0});

    assertEquals(0, gene1.compareTo(gene2));
    assertEquals(0, gene2.compareTo(gene1));
  }

  /**
   * @author vamsi
   */
  public void testCompareTo_3() {
    FixedBinaryGene gene1 = new FixedBinaryGene(3);
    BooleanGene gene2 = new BooleanGene();
    try {
      gene1.compareTo(gene2);
      fail();
    }
    catch (Exception e) {
      /*Should compare only Fixed Binary Genes*/
    }

    try {
      gene1.compareTo(new Integer(3));

    }
    catch (Exception e) {
      /*Should compare only FixedBinary Gene's*/
    }
  }

  /***
   * Apply Mutation (index,percentage). if >0 make 1(0) if <0 make 0(1)
   * @author vamsi
   */
  public void testApplyMutation_0() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 0, 1, 1});
    gene.applyMutation(0, 0.0d);
    assertEquals("[0,0,1,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_1() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 0, 1, 0});
    gene.applyMutation(1, 0.000001d);
    assertEquals("[0,1,1,0]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_2() {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    gene.setAllele(new int[] {1, 0, 1, 0, 1});
    try {
      //index size is greater
      gene.applyMutation(333, -0.000001d);
      fail();
    }
    catch (Exception E) {
      /*Fine*/
    }
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_3() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {1, 1, 0, 1});
    gene.applyMutation(0, -1.0d);
    assertEquals("[0,1,0,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_4() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {0, 1, 0, 1});
    gene.applyMutation(0, -2.0d);
    gene.applyMutation(3, 2.0d);
    gene.applyMutation(1, -4.0d);
    assertEquals("[0,0,0,1]", gene.toString());
  }

  /**
   * @author vamsi
   */
  public void testApplyMutation_5() {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setAllele(new int[] {1, 1, 1, 1});
    gene.applyMutation(0, 2.0d);
    gene.applyMutation(1, 2.0d);
    gene.applyMutation(2, 2.0d);
    gene.applyMutation(3, 2.0d);
    assertEquals("[1,1,1,1]", gene.toString());
  }

  /**
   * @author vamsi
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_0() {

    FixedBinaryGene gene = new FixedBinaryGene(4);
    try {
      gene.setValueFromPersistentRepresentation(null);
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_1()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    try {
      gene.setValueFromPersistentRepresentation("null");
      fail();
    }catch (UnsupportedRepresentationException uex) {
      //this is OK
    }
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_2()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setValueFromPersistentRepresentation("[1,1,1,1]");
    assertTrue(gene.getBit(0));
    assertTrue(gene.getBit(1));
    assertTrue(gene.getBit(2));
    assertTrue(gene.getBit(3));
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_3()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(4);
    gene.setValueFromPersistentRepresentation("[0,0,0,0]");
    assertFalse(gene.getBit(0));
    assertFalse(gene.getBit(1));
    assertFalse(gene.getBit(2));
    assertFalse(gene.getBit(3));
  }

  /**
   * @author vamsi
   * @since 2.0
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_4()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    gene.setValueFromPersistentRepresentation("[0,1,1,0,0]");
    assertFalse(gene.getBit(0));
    assertTrue(gene.getBit(1));
    assertTrue(gene.getBit(2));
    assertFalse(gene.getBit(3));
    assertFalse(gene.getBit(4));
  }

  /**
   * @since 2.0
   * @author Klaus Meffert
   * @throws Exception
   */
  public void testSetValueFromPersistentRepresentation_5()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(5);
    try {
      gene.setValueFromPersistentRepresentation("[0,1,1,0]");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ;//this is OK
    }
  }

  /**
   * @author vamsi
   * @since 2.0
   */
  public void testSetValueFromPersistentRepresentation_6() {
    FixedBinaryGene gene = new FixedBinaryGene(1);
    try {
      gene.setValueFromPersistentRepresentation("X");
      fail();
    }
    catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @author vamsi
   */
  public void testGetPersistentRepresentation_0() {
    FixedBinaryGene gene = new FixedBinaryGene(3);
    gene.setAllele(new int[] {1, 0, 1});
    String s = gene.getPersistentRepresentation();
    assertEquals("[1,0,1]", s);
  }

  /**
   * @throws Exception
   * @author vamsi
   * @since 2.0
   */
  public void testGetPersistentRepresentation_1()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(3);
    try {
      gene.setValueFromPersistentRepresentation(null);
      fail();
    }catch (UnsupportedRepresentationException uex) {
      ;//this is OK
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testGetPersistentRepresentation_2()
      throws Exception {
    FixedBinaryGene gene = new FixedBinaryGene(3);
    String s = gene.getPersistentRepresentation();
    assertEquals("[0,0,0]", s);
  }

}
