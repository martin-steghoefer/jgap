package org.jgap;

import junit.framework.*;
import junitx.util.*;

import org.jgap.impl.*;

/**
 * <p>Title: Tests for Chromosome class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class ChromosomeTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public ChromosomeTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(ChromosomeTest.class);
    return suite;
  }

  /**
   * Illegal constructions regarding first parameter
   */
  public void testConstruct_0() {
    try {
      new Chromosome(null, 1);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter
   */
  public void testConstruct_1() {
    try {
      new Chromosome(new IntegerGene(), 0);
      new Chromosome(new IntegerGene(), -1);
      new Chromosome(new IntegerGene(), -500);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding first and second parameter
   */
  public void testConstruct_2() {
    try {
      new Chromosome(null, 0);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Legal construction
   */
  public void testConstruct_3() {
    new Chromosome(new IntegerGene(), 1);
  }

  /**
   * Illegal constructions regarding the only parameter
   */
  public void testConstruct_4() {
    try {
      new Chromosome(null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding an element of the only parameter
   */
  public void testConstruct_51() {
    try {
      Gene[] genes = new IntegerGene[ 2 ];
      genes[ 0 ] = new IntegerGene();
      genes[ 1 ] = null;
      new Chromosome(genes);
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding an element of the only parameter
   */
  public void testConstruct_52() {
    try {
      Gene[] genes = new IntegerGene[ 1 ];
      genes[ 0 ] = null;
      new Chromosome(genes);
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding first and second parameter
   */
  public void testConstruct_6() {
    try {
      new Chromosome(null, null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding first parameter
   */
  public void testConstruct_7() throws InvalidConfigurationException {
    try {
      new Chromosome(new Configuration(), null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding first parameter
   */
  public void testConstruct_8() {
    try {
      Gene[] genes = new IntegerGene[ 2 ];
      genes[ 0 ] = new IntegerGene();
      genes[ 1 ] = null;
      new Chromosome(null, genes);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding first parameter
   */
  public void testConstruct_9() throws IllegalArgumentException {
    try {
      Gene[] genes = new IntegerGene[ 2 ];
      genes[ 0 ] = new IntegerGene();
      genes[ 1 ] = new IntegerGene();
      new Chromosome(null, genes);
      fail();
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding configuration
   */
  public void testConstruct_10() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    try {
      new Chromosome(conf, genes);
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK: fitness function missing
    }
  }

  /**
   * Illegal constructions regarding configuration
   */
  public void testConstruct_11() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    try {
      new Chromosome(conf, genes);
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK: sample Chromosome missing
    }
  }

  /**
   * Illegal constructions regarding configuration
   */
  public void testConstruct_12() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    Chromosome chrom2 = new Chromosome(genes);
    conf.setSampleChromosome(chrom2);
    try {
      new Chromosome(conf, genes);
    }
    catch (InvalidConfigurationException invex) {
      ; //this is OK: genotype size <= zero
    }
  }

  public void testConstruct_14() throws Exception {
    Gene gene = new BooleanGene();
    Chromosome chrom =  new Chromosome(gene, 7);
    Gene[] genes = (Gene[])PrivateAccessor.getField(chrom, "m_genes");
    assertEquals(7, genes.length);
    Gene sample;
    for (int i=0;i<genes.length;i++) {
      sample = (Gene)genes[i];
      assertEquals(gene, sample);
    }
  }


  /**
   * Test clone without setting a configuration
   */
  public void testClone_0() {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    try {
      Chromosome chrom2 = (Chromosome) chrom.clone();
      fail();
    }
    catch (IllegalStateException illex) {
      ; //this is OK
    }
  }

  /**
   * Test clone with setting a configuration
   */
  public void testClone_1() throws InvalidConfigurationException {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    chrom2 = (Chromosome) chrom.clone();
    assertEquals(chrom.hashCode(), chrom2.hashCode());
    assertEquals(chrom.getFitnessValue(), chrom2.getFitnessValue());
    assertEquals(chrom.isSelectedForNextGeneration(), chrom2.isSelectedForNextGeneration());
    assertEquals(chrom.size(), chrom2.size());
    assertEquals(chrom.toString(), chrom2.toString());
    assertTrue(chrom.equals(chrom2));
  }

  public void testEquals_0() {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertTrue(chrom.equals(chrom));
    Chromosome chrom2 = new Chromosome(genes);
    assertTrue(chrom.equals(chrom2));
  }

  public void testEquals_1() {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertTrue(chrom.equals(chrom));
    genes = new BooleanGene[ 2 ];
    genes[ 0 ] = new BooleanGene();
    genes[ 1 ] = new BooleanGene();
    Chromosome chrom2 = new Chromosome(genes);
    try {
      assertFalse(chrom.equals(chrom2));
    }
    catch (ClassCastException castex) {
      ; //this is OK
    }
  }

  public void testEquals_2() {
    Gene[] genes = new IntegerGene[ 2 ];
    Gene gen0 = new IntegerGene();
    gen0.setAllele(new Integer(1));
    Gene gen1 = new IntegerGene();
    gen1.setAllele(new Integer(2));
    genes[ 0 ] = gen0;
    genes[ 1 ] = gen1;
    Chromosome chrom = new Chromosome(genes);
    assertTrue(chrom.equals(chrom));
    genes = new IntegerGene[ 2 ];
    gen0 = new IntegerGene();
    gen0.setAllele(new Integer(1));
    gen1 = new IntegerGene();
    gen1.setAllele(new Integer(3));
    genes[ 0 ] = gen0;
    genes[ 1 ] = gen1;
    Chromosome chrom2 = new Chromosome(genes);
    assertFalse(chrom.equals(chrom2));
    gen1.setAllele(new Integer(2));
    assertTrue(chrom.equals(chrom2));
    gen0.setAllele(new Integer(2));
    assertFalse(chrom.equals(chrom2));
    gen1.setAllele(new Integer(1));
    assertFalse(chrom.equals(chrom2));
  }

  public void testEquals_3() {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertFalse(chrom.equals(null));
    try {
      assertFalse(chrom.equals(genes));
      fail();
    }
    catch (ClassCastException castex) {
      ; //this is OK
    }
  }

  public void testGetFitnessValue_0() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    StaticFitnessFunction ff = new StaticFitnessFunction(20);
    conf.setFitnessFunction(ff);
    Chromosome chrom = new Chromosome(genes);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    chrom = new Chromosome(conf, genes);
    assertEquals(ff.getStaticFitnessValue(), chrom.getFitnessValue());
    //intentionally assert it a second time
    assertEquals(ff.getStaticFitnessValue(), chrom.getFitnessValue());
  }

  public void testSize_0() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertEquals(2, chrom.size());
  }

  public void testSize_1() throws Exception {
    Gene[] genes = new IntegerGene[ 1 ];
    genes[ 0 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertEquals(1, chrom.size());
  }

  public void testSize_2() throws Exception {
    Gene[] genes = new IntegerGene[ 0 ];
    Chromosome chrom = new Chromosome(genes);
    assertEquals(0, chrom.size());
  }

  public void testCompareTo_0() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

  public void testCompareTo_1() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    Gene gen0 = new IntegerGene();
    gen0.setAllele(new Integer(1147));
    genes[ 0 ] = gen0;
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom0 = new Chromosome(genes);
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    conf.setSampleChromosome(chrom0);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Gene[] genes2 = new IntegerGene[ 2 ];
    Gene gene01 = new IntegerGene();
    gene01.setAllele(new Integer(4711));
    genes2[ 0 ] = gene01;
    genes2[ 1 ] = new IntegerGene();
    Chromosome chrom2 = new Chromosome(genes2);
    assertTrue(chrom.compareTo(chrom2) < 0);
    assertTrue(chrom2.compareTo(chrom) > 0);
  }

  public void testCompareTo_2() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    genes[ 0 ] = new IntegerGene();
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Gene[] genes2 = new IntegerGene[ 3 ];
    genes2[ 0 ] = new IntegerGene();
    genes2[ 1 ] = new IntegerGene();
    genes2[ 2 ] = new IntegerGene();
    Chromosome chrom2 = new Chromosome(genes2);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    assertTrue(chrom.compareTo(chrom2) < 0);
    assertTrue(chrom2.compareTo(chrom) > 0);
  }

  public void testCompareTo_3() throws Exception {
    Gene[] genes = new IntegerGene[ 1 ];
    genes[ 0 ] = new IntegerGene();
    Chromosome chrom = new Chromosome(genes);
    assertTrue(chrom.compareTo(null) > 0);
  }

  public void testCompareTo_4() throws Exception {
    Gene[] genes = new IntegerGene[ 2 ];
    Gene gen0 = new IntegerGene();
    gen0.setAllele(new Integer(4711));
    genes[ 0 ] = gen0;
    genes[ 1 ] = new IntegerGene();
    Chromosome chrom0 = new Chromosome(genes);
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    conf.setSampleChromosome(chrom0);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Gene[] genes2 = new IntegerGene[ 2 ];
    Gene gene01 = new IntegerGene();
    gene01.setAllele(new Integer(4711));
    genes2[ 0 ] = gene01;
    genes2[ 1 ] = new IntegerGene();
    Chromosome chrom2 = new Chromosome(genes2);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

}