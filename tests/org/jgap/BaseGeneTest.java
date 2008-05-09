/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import junit.framework.*;
import java.util.*;

/**
 * Tests the BaseGene class.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class BaseGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.28 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BaseGeneTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testConstruct_0()
      throws Exception {
    assertNotNull(new BaseGeneImpl(conf));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_0()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    assertEquals("null, " + BaseGene.S_APPLICATION_DATA + ":null",
                 gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testToString_1()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    gene.setAllele(new Integer(98));
    assertEquals("98, " + BaseGene.S_APPLICATION_DATA + ":null",
                 gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testToString_2()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    gene.setAllele(new Integer(98));
    gene.setApplicationData("myAppData");
    assertEquals("98, " + BaseGene.S_APPLICATION_DATA + ":myAppData",
                 gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testGetAllele_0()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    gene.setAllele(new Double(75));
    assertEquals(new Double(75), gene.getAllele());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSize_0()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    assertEquals(1, gene.size());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_0()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    gene.m_compareTo_result = 0;
    assertTrue(gene.equals(null));
    assertTrue(gene.equals(gene));
    assertTrue(gene.equals(new Integer(2)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_1()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    gene.m_compareTo_result = -1;
    assertFalse(gene.equals(null));
    assertFalse(gene.equals(gene));
    assertFalse(gene.equals(new Integer(2)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_2()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    gene.m_compareTo_result = 1;
    assertFalse(gene.equals(null));
    assertFalse(gene.equals(gene));
    assertFalse(gene.equals(new Integer(2)));
  }

  /**
   * Compare application data.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_3()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
//    Genotype.setConfiguration(new ConfigurationForTest());
//    Genotype.getConfiguration().getJGAPFactory();
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    gene.m_compareTo_result = 0;
    gene.setApplicationData(new AppDataForTesting());
    BaseGeneImpl gene2 = new BaseGeneImpl(conf);
    gene2.m_compareTo_result = 0;
    gene2.setApplicationData(new AppDataForTesting());
    gene.setCompareApplicationData(true);
    assertTrue(gene.equals(gene2));
    /**@todo use other than JGAPFactory to be able to receive a null
     * CompareToHandler for the application data object
     */
  }

  /**
   * Simple cleanup should be possible without exception.
   * @throws Exception
   *
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testCleanup_0()
      throws Exception {
    Gene gene = new BaseGeneImpl(conf);
    gene.setAllele(new Double(75));
    gene.cleanup();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testHashCode_0()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    assertEquals( -79, gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testHashCode_1()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    gene.setAllele(new Double(1.5d));
    assertEquals(new Double(1.5d).hashCode(), gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_0()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    assertEquals(0.0, gene.getEnergy(), DELTA);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSetEnergy_1()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
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
   * @since 2.4
   */
  public void testSetApplicationData_0()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    assertNull(gene.getApplicationData());
    Integer i = new Integer(23);
    gene.setApplicationData(i);
    assertSame(i, gene.getApplicationData());
    String s = "Hallo";
    gene.setApplicationData(s);
    assertSame(s, gene.getApplicationData());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testSetApplicationData_1()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    String appData = "Hallo";
    IChromosome c = new Chromosome(conf, gene, 2);
    conf.reset();
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setSampleChromosome(c);
    conf.setPopulationSize(5);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    Population pop = genotype.getPopulation();
    c = pop.getChromosome(0);
    c.setApplicationData(appData);
    pop.setChromosome(0, c);
    genotype.evolve();
    Population pop2 = genotype.getPopulation();
    /**@todo find the one chromosome*/
//    c = pop2.getChromosome(2);// 2 = 0 + 2 X-Overs
//    assertSame(appData, c.getApplicationData());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public void testSetApplicationData_2()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    String appData = "Hallo";
    IChromosome c = new Chromosome(conf, gene, 2);
    conf.reset();
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setSampleChromosome(c);
    conf.setPopulationSize(5);
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    Population pop = genotype.getPopulation();
    c = pop.getChromosome(0);
    c.setApplicationData(appData);
    List geneAppData = new Vector();
    geneAppData.add("x");
    geneAppData.add(new Integer(3));
    Gene g = c.getGene(0);
    g.setApplicationData(geneAppData);
    pop.setChromosome(0, c);
    genotype.evolve();
    Population pop2 = genotype.getPopulation();
    /**@todo find the one chromosome*/
//    c = pop2.getChromosome(2);// 2 = 0 + 2 X-Overs
//    assertSame(appData, c.getApplicationData());
//    g = c.getGene(0);
//    assertSame(geneAppData, g.getApplicationData());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testIsCompareApplicationData_0()
      throws Exception {
    BaseGeneImpl gene = new BaseGeneImpl(conf);
    assertFalse(gene.isCompareApplicationData());
    gene.setCompareApplicationData(false);
    assertFalse(gene.isCompareApplicationData());
    gene.setCompareApplicationData(true);
    assertTrue(gene.isCompareApplicationData());
  }

  /**
   * Test implementation of Gene interface extending abstract BaseGene class.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  class BaseGeneImpl
      extends BaseGene {
    private Object m_allele;

    protected int m_compareTo_result;

    public int compareTo(Object a_o) {
      return m_compareTo_result;
    }

    public BaseGeneImpl(final Configuration a_config)
        throws InvalidConfigurationException {
      super(a_config);
    }

    protected Gene newGeneInternal() {
      try {
        return new BaseGeneImpl(getConfiguration());
      } catch (InvalidConfigurationException iex) {
        throw new RuntimeException(iex);
      }
    }

    public void setAllele(Object a_newValue) {
      m_allele = a_newValue;
    }

    public String getPersistentRepresentation() {
      return null;
    }

    public void setValueFromPersistentRepresentation(String a_representation) {
    }

    public void setToRandomValue(RandomGenerator a_numberGenerator) {
    }

    public void applyMutation(int a_index, double a_percentage) {
    }

    protected Object getInternalValue() {
      return m_allele;
    }
  }
  class AppDataForTesting
      implements IApplicationData {
    public int compareTo(Object o2) {
      return 0;
    }

    public Object clone()
        throws CloneNotSupportedException {
      return null;
    }
  }

}
