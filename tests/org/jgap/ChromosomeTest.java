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

import java.util.*;
import org.jgap.impl.*;
import org.jgap.util.*;
import junit.framework.*;

/**
 * Tests the Chromosome class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ChromosomeTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.69 $";

  public static Test suite() {
    return new TestSuite(ChromosomeTest.class);
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_0()
      throws Exception {
    try {
      new Chromosome(conf, null, 1);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_0_2()
      throws Exception {
    try {
      new Chromosome(conf, null, 1, new MyConstraintChecker());
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding third parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_1()
      throws Exception {
    try {
      new Chromosome(conf, new IntegerGene(conf), 0);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding third parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_1_2()
      throws Exception {
    try {
      new Chromosome(conf, new IntegerGene(conf), -1);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_1_3()
      throws Exception {
    try {
      new Chromosome(new ConfigurationForTesting(), new IntegerGene(conf), -500);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second and third parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_2()
      throws Exception {
    try {
      new Chromosome(conf, null, 0);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second and third parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_2_2()
      throws Exception {
    try {
      new Chromosome(conf, null, 0, null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testConstruct_2_3()
      throws Exception {
    try {
      new Chromosome(null);
      fail();
    } catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * Legal construction.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_3()
      throws Exception {
    Chromosome chrom = new Chromosome(conf, new IntegerGene(conf), 1);
    assertEquals(1, chrom.size());
    chrom = new Chromosome(conf, new IntegerGene(conf), 1, null);
    assertEquals(1, chrom.size());
    assertNull(chrom.getConstraintChecker());
    IGeneConstraintChecker cc = new MyConstraintChecker();
    chrom = new Chromosome(conf, new IntegerGene(conf), 1, cc);
    assertEquals(1, chrom.size());
    assertEquals(cc, chrom.getConstraintChecker());
  }

  /**
   * Illegal constructions regarding an element of the second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_5_2()
      throws Exception {
    try {
      Gene[] genes = new IntegerGene[1];
      genes[0] = null;
      new Chromosome(conf, genes);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding a gene type forbidden by the constraint
   * checker used.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testConstruct_5_3()
      throws Exception {
    try {
      Gene[] genes = new Gene[2];
      genes[0] = new IntegerGene();
      genes[1] = new DoubleGene();
      IGeneConstraintChecker cc = new MyConstraintChecker(DoubleGene.class);
      new Chromosome(conf, genes, cc);
      fail();
    } catch (IllegalArgumentException iex) {
      fail();
    } catch (InvalidConfigurationException cex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_6()
      throws Exception {
    try {
      new Chromosome(conf, (Gene[])null);
      fail();
    } catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_7_1()
      throws Exception {
    try {
      new Chromosome(conf, 0);
      fail();
    } catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * Illegal constructions regarding second parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_7_2()
      throws Exception {
    try {
      new Chromosome(conf, -5);
      fail();
    } catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testConstruct_7_3()
      throws Exception {
    Chromosome chrom = new Chromosome(conf, 5);
    assertEquals(5, chrom.getGenes().length);
    for (int i = 0; i < 5; i++) {
      assertEquals(null, chrom.getGene(i));
    }
  }

  /**
   * Illegal constructions regarding first parameter.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_8()
      throws Exception {
    try {
      Gene[] genes = new IntegerGene[2];
      genes[0] = new IntegerGene(conf);
      genes[1] = null;
      new Chromosome(conf, genes);
      fail();
    } catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testConstruct_9()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes1 = new Gene[2];
    genes1[0] = new IntegerGene(conf);
    genes1[1] = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, genes1);
    String repr = chrom.getPersistentRepresentation();
    Chromosome chrom2 = new Chromosome(conf, repr);
    assertEquals(chrom, chrom2);
    assertEquals(chrom.getPersistentRepresentation(),
                 chrom2.getPersistentRepresentation());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_10()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertEquals(2, chrom.size());
    assertEquals(genes[0], chrom.getGene(0));
    assertEquals(genes[1], chrom.getGene(1));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_11()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new BooleanGene[3];
    genes[0] = new BooleanGene(conf);
    genes[1] = new BooleanGene(conf);
    genes[2] = new BooleanGene(conf);
    genes[2].setAllele(Boolean.valueOf(true));
    conf.setFitnessFunction(new RandomFitnessFunction());
    Chromosome chrom = new Chromosome(conf, genes);
    assertEquals(3, chrom.size());
    assertEquals(genes[0], chrom.getGene(0));
    assertEquals(genes[1], chrom.getGene(1));
    assertEquals(genes[2], chrom.getGene(2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_12()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new RandomFitnessFunction());
    Chromosome chrom2 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom2);
    new Chromosome(conf, genes);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_14()
      throws Exception {
    Gene gene = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, gene, 7);
    Gene[] genes = chrom.getGenes();
    assertEquals(7, genes.length);
    Gene sample;
    for (int i = 0; i < genes.length; i++) {
      sample = (Gene) genes[i];
      assertEquals(gene, sample);
    }
  }

  /**
   * Tests cloning and cleanup of a chromosome.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testClone_5()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    Chromosome chrom = new Chromosome(conf);
    assertEquals(0, chrom.size());
    Chromosome chrom2 = (Chromosome) chrom.clone();
    assertEquals(chrom, chrom2);
    conf.getChromosomePool().releaseChromosome(chrom);
    assertEquals(Chromosome.class, chrom.clone().getClass());
    chrom.cleanup();
    conf.setChromosomePool(null);
  }

  /**
   * Tests cloning of a chromosome with genes using the chromosome pool.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testClone_6()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    chrom.cleanup();
    assertEquals(2, chrom.size());
    Chromosome chrom2 = (Chromosome) chrom.clone();
    assertEquals(chrom, chrom2);
    Chromosome chrom3 = (Chromosome) chrom.clone();
    assertEquals(chrom3, chrom2);
  }

  /**
   * Test clone with configuration set.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   */
  public void testClone_1()
      throws InvalidConfigurationException {
    Gene[] genes = new Gene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom3 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom3);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Chromosome chrom2 = (Chromosome) chrom.clone();
    assertEquals(chrom.hashCode(), chrom2.hashCode());
    assertEquals(chrom.getFitnessValue(), chrom2.getFitnessValue(), DELTA);
    assertEquals(chrom.isSelectedForNextGeneration(),
                 chrom2.isSelectedForNextGeneration());
    assertEquals(chrom.size(), chrom2.size());
    assertEquals(chrom.getGene(0), chrom2.getGene(0));
    assertEquals(chrom.getGene(1), chrom2.getGene(1));
    assertEquals(chrom.getGenes().getClass(), chrom2.getGenes().getClass());
    assertEquals(chrom.toString(), chrom2.toString());
    assertTrue(chrom.equals(chrom2));
    assertNotSame(chrom.getGenes(), chrom2.getGenes());
    assertNotSame(chrom.getGene(0), chrom2.getGene(0));
    assertNotSame(chrom.getGene(1), chrom2.getGene(1));
  }

  /**
   * Test clone with set application data implementing interface Cloneable,
   * but restricting access because MyAppData is a package protected class (and
   * the Chromosome class resides in different package) and also not
   * implementing IApplicationData. But by using setAccessible in framework
   * code, now it works. Some months ago it didn't *uuumm*.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   */
  public void testClone_2()
      throws InvalidConfigurationException {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Object appObj = new MyAppObject();
    chrom.setApplicationData(appObj);
    Chromosome cloned = (Chromosome) chrom.clone();
    assertSame(appObj, cloned.getApplicationData());
  }

  /**
   * Test clone with set application data, where cloning supported. Access is
   * not granted via Cloneable (because of inner class) but via explicit and
   * specially considered interface IApplicationData (see MyAppObject2)!
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   */
  public void testClone_3()
      throws InvalidConfigurationException {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Object appObj = new MyAppObject2();
    chrom.setApplicationData(appObj);
    chrom2 = (Chromosome) chrom.clone();
    assertTrue(chrom.equals(chrom2));
    assertEquals(appObj, chrom2.getApplicationData());
    assertFalse(appObj == chrom2.getApplicationData());
  }

  /**
   * Test clone with Gene's energy set (value should be considered, too).
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testClone_4()
      throws InvalidConfigurationException {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new Gene[2];
    genes[0] = new IntegerGene(conf);
    genes[0].setEnergy(47.11d);
    genes[1] = new IntegerGene(conf);
    genes[1].setEnergy(8.15d);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom3 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom3);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Chromosome chrom2 = (Chromosome) chrom.clone();
    Gene[] clonedGenes = chrom2.getGenes();
    assertEquals(genes[0].getEnergy(), clonedGenes[0].getEnergy(), DELTA);
    assertEquals(genes[1].getEnergy(), clonedGenes[1].getEnergy(), DELTA);
  }

  /**
   * Tests cloning of sub class of org.jgap.Chromosome.
   * This test uses a chromosome with own clone method.
   * Thanx to Peter Molettiere for pointing out the problem with
   * Chromosome.clone().
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void testClone_7()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    IChromosome chrom = new ChromosomeForTesting(conf);
    conf.setSampleChromosome(chrom);
    Gene[] genes = new Gene[1];
    genes[0] = new IntegerGene(conf);
    chrom.setGenes(genes);
    IChromosome copy = (IChromosome)chrom.clone();
    assertEquals(copy, chrom);
  }

  /**
   * Tests cloning of sub class of org.jgap.Chromosome.
   * This test uses a chromosome without own clone method.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void testClone_8()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new RandomFitnessFunction());
    IChromosome chrom = new ChromosomeForTesting2(conf);
    conf.setSampleChromosome(chrom);
    Gene[] genes = new Gene[1];
    genes[0] = new IntegerGene(conf);
    chrom.setGenes(genes);
    IChromosome copy = (IChromosome)chrom.clone();
    assertEquals(copy, chrom);
  }

  private final static int MAX_CHROMOSOME_TO_TEST = 1000;

  private final static int MAX_GENES_TO_TEST = 25;

  private final static int MAX_GENES_TYPES = 6;

  /**
   * Test hashcode for intensity of diversity.<p>
   * <b>Warning:</b> when a new Gene type is added the constant MAX_GENES_TYPES
   * must be adjusted as well as adding the new type in the switch case below.
   *
   * @throws InvalidConfigurationException
   *
   * @author John Serri
   * @since 2.1
   */
  public void testHashCode_0()
      throws InvalidConfigurationException {
    int count;
    int numGenes;
    int geneCount;
    int geneType;
    Gene[] genes;
    Chromosome chrom;
    TestHashcode thc = new TestHashcode();
    thc.setVerbose(!true);
    List uniqueChromosome = new ArrayList();
    List equalChromosome = new ArrayList();
    // Build Random Chromosomes
    for (count = 0; count < MAX_CHROMOSOME_TO_TEST; count++) {
      numGenes = (int) (Math.random() * (MAX_GENES_TO_TEST - 1)) + 1;
      genes = new Gene[numGenes];
      for (geneCount = 0; geneCount < numGenes; geneCount++) {
        geneType = (int) (Math.random() * MAX_GENES_TYPES);
        switch (geneType) {
          case 0:
            genes[geneCount] = new IntegerGene(conf);
            break;
          case 1:
            genes[geneCount] = new BooleanGene(conf);
            break;
          case 2:
            genes[geneCount] = new CompositeGene(conf);
            break;
          case 3:
            genes[geneCount] = new DoubleGene(conf);
            break;
          case 4:
            genes[geneCount] = new FixedBinaryGene(conf, 5);
            break;
          case 5:
            genes[geneCount] = new StringGene(conf);
            break;
        }
      }
      chrom = new Chromosome(new ConfigurationForTesting(), genes);
      // We only want to add unique object, since equal object will
      // return the same hashcode
      if (!uniqueChromosome.contains(chrom)) {
        uniqueChromosome.add(chrom);
      }
    }
    //Test to see if enough hashcodes are unique
    thc.setFractionUnique(.95);
    if (!thc.testHashCodeUniqueness(uniqueChromosome)) {
      System.out.println(
          "testHashCodeUniqueness failed\n Actual Percent unique = " +
          thc.getActualFractionUnique());
      fail();
    }
    //Test mathematical average and dispersion of hashcode
    //I am not sure of the value of this test since boundry values are
    // pretty much arbitrary
    thc.setAverageMax(2100000000);
    thc.setAverageMin( -140000000);
    thc.setStdDevMax(2100000000);
    thc.setStdDevMin(9000000);
    if (!thc.testDispersion(uniqueChromosome)) {
      fail();
    }
    // Build identical Chromosomes
    for (count = 0; count < 3; count++) {
      genes = new Gene[1];
      genes[0] = new IntegerGene(conf);
      chrom = new Chromosome(new ConfigurationForTesting(), genes);
      equalChromosome.add(chrom);
    }
    //If an object is equal it must have the same hashcode
    if (!thc.testHashCodeEquality(equalChromosome)) {
      fail();
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_0()
      throws Exception {
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.equals(chrom));
    Chromosome chrom2 = new Chromosome(conf, genes);
    assertTrue(chrom.equals(chrom2));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_1()
      throws Exception {
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.equals(chrom));
    genes = new BooleanGene[2];
    genes[0] = new BooleanGene(conf);
    genes[1] = new BooleanGene(conf);
    Chromosome chrom2 = new Chromosome(conf, genes);
    assertFalse(chrom.equals(chrom2));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_2()
      throws Exception {
    Gene[] genes = new IntegerGene[2];
    Gene gen0 = new IntegerGene(conf);
    gen0.setAllele(new Integer(1));
    Gene gen1 = new IntegerGene(conf);
    gen1.setAllele(new Integer(2));
    genes[0] = gen0;
    genes[1] = gen1;
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.equals(chrom));
    genes = new IntegerGene[2];
    gen0 = new IntegerGene(conf);
    gen0.setAllele(new Integer(1));
    gen1 = new IntegerGene(conf);
    gen1.setAllele(new Integer(3));
    genes[0] = gen0;
    genes[1] = gen1;
    Chromosome chrom2 = new Chromosome(conf, genes);
    assertFalse(chrom.equals(chrom2));
    gen1.setAllele(new Integer(2));
    assertTrue(chrom.equals(chrom2));
    gen0.setAllele(new Integer(2));
    assertFalse(chrom.equals(chrom2));
    gen1.setAllele(new Integer(1));
    assertFalse(chrom.equals(chrom2));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testEquals_3()
      throws Exception {
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertFalse(chrom.equals(null));
    // no ClassCastException is expected next!
    // ---------------------------------------
    assertFalse(chrom.equals(genes));
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetFitnessValue_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    StaticFitnessFunction ff = new StaticFitnessFunction(20);
    conf.setFitnessFunction(ff);
    Chromosome chrom = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    chrom = new Chromosome(conf, genes);
    assertEquals(ff.getStaticFitnessValue(), chrom.getFitnessValue(), DELTA);
    //intentionally assert it a second time
    assertEquals(ff.getStaticFitnessValue(), chrom.getFitnessValue(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetFitnessValue_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    StaticFitnessFunction ff = new StaticFitnessFunction(20);
    conf.setFitnessFunction(ff);
    Chromosome chrom = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    chrom = new Chromosome(conf, genes);
    assertEquals(ff.getStaticFitnessValue(), chrom.getFitnessValue(), DELTA);
    // set fitness value to a different one (should not affect first set value
    // as no computation is performed once the fitness is known in the
    // chromosome.
    ff.setStaticFitnessValue(44.235d);
    assertEquals(20, chrom.getFitnessValue(), DELTA);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSize_0()
      throws Exception {
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertEquals(2, chrom.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSize_1()
      throws Exception {
    Gene[] genes = new IntegerGene[1];
    genes[0] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertEquals(1, chrom.size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSize_2()
      throws Exception {
    Gene[] genes = new IntegerGene[0];
    try {
      new Chromosome(conf, genes);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_0_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    chrom2.setCompareApplicationData(true);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    chrom.setCompareApplicationData(true);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_0_3()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    chrom2.setCompareApplicationData(true);
    chrom2.setApplicationData(new Object());
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    chrom.setCompareApplicationData(true);
    chrom.setApplicationData(null);
    assertFalse(chrom.compareTo(chrom2) == 0);
    assertFalse(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_0_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    chrom2.setCompareApplicationData(true);
    chrom2.setApplicationData(new Object());
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    chrom.setCompareApplicationData(true);
    chrom.setApplicationData(new Date());
    assertFalse(chrom.compareTo(chrom2) == 0);
    assertFalse(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    Gene gen0 = new IntegerGene(conf);
    gen0.setAllele(new Integer(1147));
    genes[0] = gen0;
    genes[1] = new IntegerGene(conf);
    Chromosome chrom0 = new Chromosome(conf, genes);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    conf.setSampleChromosome(chrom0);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    Gene[] genes2 = new IntegerGene[2];
    Gene gene01 = new IntegerGene(conf);
    gene01.setAllele(new Integer(4711));
    genes2[0] = gene01;
    genes2[1] = new IntegerGene(conf);
    Chromosome chrom2 = new Chromosome(conf, genes2);
    assertTrue(chrom.compareTo(chrom2) < 0);
    assertTrue(chrom2.compareTo(chrom) > 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom = new Chromosome(conf, genes);
    Gene[] genes2 = new IntegerGene[3];
    genes2[0] = new IntegerGene(conf);
    genes2[1] = new IntegerGene(conf);
    genes2[2] = new IntegerGene(conf);
    Chromosome chrom2 = new Chromosome(conf, genes2);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    assertTrue(chrom.compareTo(chrom2) < 0);
    assertTrue(chrom2.compareTo(chrom) > 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_3()
      throws Exception {
    Gene[] genes = new IntegerGene[1];
    genes[0] = new IntegerGene(conf);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.compareTo(null) > 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new IntegerGene[2];
    Gene gen0 = new IntegerGene(conf);
    gen0.setAllele(new Integer(4711));
    genes[0] = gen0;
    genes[1] = new IntegerGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    conf.setPopulationSize(5);
    Chromosome chrom0 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom0);
    Chromosome chrom = new Chromosome(conf, genes);
    Gene[] genes2 = new IntegerGene[2];
    Gene gene01 = new IntegerGene(conf);
    gene01.setAllele(new Integer(4711));
    genes2[0] = gene01;
    genes2[1] = new IntegerGene(conf);
    Chromosome chrom2 = new Chromosome(conf, genes2);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_5()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes = new Gene[2];
    genes[0] = new IntegerGene(conf);
    genes[1] = new BooleanGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCompareTo_6()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes1 = new Gene[2];
    genes1[0] = new IntegerGene(conf);
    genes1[1] = new BooleanGene(conf);
    Gene[] genes2 = new Gene[2];
    genes2[0] = new IntegerGene(conf);
    genes2[1] = new BooleanGene(conf);
    conf.setFitnessFunction(new StaticFitnessFunction(20));
    Chromosome chrom2 = new Chromosome(conf, genes1);
    conf.setSampleChromosome(chrom2);
    conf.setPopulationSize(5);
    Chromosome chrom = new Chromosome(conf, genes2);
    assertTrue(chrom.compareTo(chrom2) == 0);
    assertTrue(chrom2.compareTo(chrom) == 0);
    genes2[1].setAllele(Boolean.valueOf(false));
    genes1[1].setAllele(Boolean.valueOf(true));
    assertFalse(chrom2.compareTo(chrom) == 0);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testRandomInitialChromosome_0()
      throws Exception {
    try {
      Chromosome.randomInitialChromosome(null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRandomInitialChromosome_1()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    conf.setChromosomePool(new ChromosomePool());
    conf.setRandomGenerator(new RandomGeneratorForTesting(true));
    IChromosome chrom = Chromosome.randomInitialChromosome(conf);
    //The BooleanGene comes from the sample chrom set in ConfigurationForTest
    assertTrue( ( (BooleanGene) chrom.getGene(0)).booleanValue());
    ConfigurationForTesting.MyAppData appData = (ConfigurationForTesting.MyAppData)(chrom.getGene(1).getApplicationData());
    assertEquals("TEST123", appData.getValue());
    assertNull(chrom.getGene(0).getApplicationData());
    assertNull(chrom.getGene(2).getApplicationData());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testRandomInitialChromosome_2()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    conf.setChromosomePool(new ChromosomePool());
    conf.setRandomGenerator(new RandomGeneratorForTesting(false));
    IChromosome chrom = Chromosome.randomInitialChromosome(conf);
    //The BooleanGene comes from the sample chrom set in ConfigurationForTest
    assertFalse( ( (BooleanGene) chrom.getGene(0)).booleanValue());
  }

  /**
   * Use Chromosome Pool.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRandomInitialChromosome_3()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    conf.setChromosomePool(new ChromosomePool());
    conf.setRandomGenerator(new RandomGeneratorForTesting(true));
    IChromosome chrom = Chromosome.randomInitialChromosome(conf);
    chrom.cleanup(); //fill the pool
    chrom = Chromosome.randomInitialChromosome(conf);
    assertEquals(FitnessFunction.NO_FITNESS_VALUE,
                 chrom.getFitnessValueDirectly(), DELTA);
    assertTrue( ( (BooleanGene) chrom.getGene(0)).booleanValue());
  }

  /**
   * Setting random generator not allowed after configuration has been locked.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testRandomInitialChromosome_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(2.5d));
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene(conf);
    genes[0] = gene;
    Chromosome chrom = new Chromosome(conf, genes);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(5);
    // following command locks configuration
    Chromosome.randomInitialChromosome(conf);
    try {
      conf.setRandomGenerator(new RandomGeneratorForTesting(true));
      fail();
    } catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testCleanup_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    IChromosome chrom = Chromosome.randomInitialChromosome(conf);
    chrom.cleanup();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testCleanup_2()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    ChromosomePool chromosomePool = new ChromosomePool();
    assertNull(chromosomePool.acquireChromosome());
    conf.setChromosomePool(chromosomePool);
    IChromosome chrom = Chromosome.randomInitialChromosome(conf);
    chrom.cleanup();
    assertSame(chrom, chromosomePool.acquireChromosome());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testSetCompareApplicationData_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Chromosome chrom = (Chromosome) Chromosome.randomInitialChromosome(conf);
    assertFalse(chrom.isCompareApplicationData());
    chrom.setCompareApplicationData(true);
    assertTrue(chrom.isCompareApplicationData());
  }

  /**
   * No genes.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_0()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Chromosome chrom = new Chromosome(conf, 3);
    assertEquals(IChromosome.S_SIZE + ":" + chrom.size()
                 + ", " + IChromosome.S_FITNESS_VALUE + ":" +
                 FitnessFunction.NO_FITNESS_VALUE
                 + ", " + IChromosome.S_ALLELES + ":[null, null, null]"
                 + ", " + IChromosome.S_APPLICATION_DATA + ":null",
                 chrom.toString());
  }

  /**
   * Two genes.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testToString_1()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Gene[] genes = new IntegerGene[2];
    genes[0] = new IntegerGene(conf, 0, 77);
    genes[0].setAllele(new Integer(47));
    genes[1] = new IntegerGene(conf, 2, 333);
    genes[1].setAllele(new Integer(55));
    Chromosome chrom = new Chromosome(conf, 2);
    chrom.setFitnessValue(47.11d);
    chrom.setGenes(genes);
    assertEquals(IChromosome.S_SIZE + ":" + chrom.size()
                 + ", " + IChromosome.S_FITNESS_VALUE + ":" +
                 47.11d
                 + ", " + IChromosome.S_ALLELES + ":[IntegerGene(0,77)=47,"
                 + " IntegerGene(2,333)=55]"
                 + ", " + IChromosome.S_APPLICATION_DATA + ":null",
                 chrom.toString());
  }

  /**
   * Considering application data.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testToString_2()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    Chromosome chrom = new Chromosome(conf, 3);
    chrom.setApplicationData("uIoP");
    assertEquals(IChromosome.S_SIZE + ":" + chrom.size()
                 + ", " + IChromosome.S_FITNESS_VALUE + ":" +
                 FitnessFunction.NO_FITNESS_VALUE
                 + ", " + IChromosome.S_ALLELES + ":[null, null, null]"
                 + ", " + IChromosome.S_APPLICATION_DATA + ":uIoP",
                 chrom.toString());
  }

  /**
   * Test setter/getter of constraint checker.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testSetConstraintChecker_0()
      throws Exception {
    Chromosome c = new Chromosome(new ConfigurationForTesting(), 2);
    assertNull(c.getConstraintChecker());
    IGeneConstraintChecker cc = new MyConstraintChecker();
    c.setConstraintChecker(cc);
    assertEquals(cc, c.getConstraintChecker());
    c.setConstraintChecker(null);
    assertNull(c.getConstraintChecker());
  }

  /**
   * Test setter/getter of constraint checker.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void testSetConstraintChecker_1()
      throws Exception {
    Gene gene = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, gene, 2);
    assertNull(c.getConstraintChecker());
    IGeneConstraintChecker cc = new MyConstraintChecker(IntegerGene.class);
    try {
      c.setConstraintChecker(cc);
      fail();
    } catch (InvalidConfigurationException cex) {
      ; //this is OK
    }
    assertNull(c.getConstraintChecker());
  }

  /**
   * Test setter/getter of constraint checker.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetConstraintChecker_2()
      throws Exception {
    Gene gene = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, gene, 2);
    Gene[] genes = new Gene[] {
        gene};
    c.setConstraintChecker(null);
    assertNull(c.getConstraintChecker());
    // the following should be possible without exception
    c.setGenes(genes);
  }

  /**
   * Test setter/getter of constraint checker which forbids the used gene type.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetConstraintChecker_3()
      throws Exception {
    Gene gene = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, gene, 2);
    IGeneConstraintChecker cc = new MyConstraintChecker(IntegerGene.class);
    try {
      c.setConstraintChecker(cc);
      fail();
    } catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * Test setter/getter of constraint checker which allows ther used gene type.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testSetConstraintChecker_4()
      throws Exception {
    Gene gene = new IntegerGene(conf);
    Chromosome c = new Chromosome(conf, gene, 2);
    IGeneConstraintChecker cc = new MyConstraintChecker(DoubleGene.class);
    c.setConstraintChecker(cc);
    Gene[] genes = new Gene[] {
        gene};
    // the following should be possible without exception
    c.setGenes(genes);
  }

  class MyAppObject
      extends TestFitnessFunction implements Cloneable {
    public int compareTo(Object o) {
      return 0;
    }

    public Object clone() {
      return this;
    }
  }
  class MyAppObject2
      extends TestFitnessFunction implements IApplicationData {
    public boolean equals(Object o2) {
      return compareTo(o2) == 0;
    }

    public int compareTo(Object o) {
      return 0;
    }

    public Object clone() {
      return new MyAppObject2();
    }
  }
  /**
   * Class needs to be static, otherwise the serialization of the Chromosome
   * does not work properly (in JBuilder it does but running the test with ant
   * fails).
   *
   * @author Klaus Meffert
   */
  static class MyConstraintChecker
      implements IGeneConstraintChecker {
    private Class m_forbidden;

    public MyConstraintChecker() {
      this(null);
    }

    public MyConstraintChecker(Class a_forbiddenClass) {
      m_forbidden = a_forbiddenClass;
    }

    public boolean verify(final Gene a_gene, final Object a_value,
                          final IChromosome a_chrom, final int a_geneIndex) {
      if (m_forbidden == null) {
        return true;
      }
      return! (a_gene.getClass().equals(m_forbidden));
    }
  }

  class MyChromosome extends Chromosome {
    public boolean isCalculated;

    public MyChromosome(Configuration a_conf)
        throws InvalidConfigurationException {
      super(a_conf);
    }

    protected double calcFitnessValue() {
      isCalculated = true;
      return super.calcFitnessValue();
    }
  }

  /**
   * Ensures Chromosome is implementing Serializable.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    Chromosome chrom = new Chromosome(new ConfigurationForTesting(), new Gene[] {
      new IntegerGene(new ConfigurationForTesting(), 1, 5)
    });
    assertTrue(isSerializable(chrom));
  }

  /**
   * Ensures that Chromosome and all objects contained implement Serializable
   * correctly.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct chromosome to be serialized
    Chromosome chrom = new Chromosome(new ConfigurationForTesting(), new Gene[] {
      new IntegerGene(new ConfigurationForTesting(), 1, 5)
    });
    IGeneConstraintChecker checker = new MyConstraintChecker();
    chrom.setConstraintChecker(checker);
    Object o = doSerialize(chrom);
    assertEquals(o, chrom);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsHandlerFor_0()
      throws Exception {
    Chromosome chrom = new Chromosome(new ConfigurationForTesting(), 3);
    assertTrue(chrom.isHandlerFor(chrom, Chromosome.class));
    assertFalse(chrom.isHandlerFor(chrom, ConfigurationForTesting.class));
    assertFalse(chrom.isHandlerFor(chrom, Object.class));
    assertTrue(chrom.perform(chrom, Chromosome.class, null) instanceof
               Chromosome);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetMultiObjectives_0()
      throws Exception {
    Chromosome chrom = new Chromosome(new ConfigurationForTesting(), 3);
    List l = new Vector();
    l.add("Entry_1");
    l.add("Entry_2");
    chrom.setMultiObjectives(l);
    assertEquals(l, chrom.getMultiObjectives());
    List l2 = new Vector();
    l2.add("Entry_3");
    l2.add("Entry_4");
    chrom.setMultiObjectives(l2);
    assertEquals(l2, chrom.getMultiObjectives());
    assertEquals(2, chrom.getMultiObjectives().size());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes1 = new Gene[2];
    genes1[0] = new IntegerGene(conf);
    genes1[1] = new BooleanGene(conf);
    Chromosome chrom = new Chromosome(conf, genes1);
    String repr = chrom.getPersistentRepresentation();
    Chromosome chrom2 = new Chromosome(conf);
    chrom2.setValueFromPersistentRepresentation(repr);
    assertEquals(chrom, chrom2);
    assertEquals(chrom.getPersistentRepresentation(),
                 chrom2.getPersistentRepresentation());
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes1 = new Gene[2];
    genes1[0] = new StringGene(conf);
    genes1[1] = new DoubleGene(conf);
    Chromosome chrom = new Chromosome(conf, genes1);
    String repr = chrom.getPersistentRepresentation();
    Chromosome chrom2 = new Chromosome(conf);
    chrom2.setValueFromPersistentRepresentation(repr);
    assertEquals(chrom, chrom2);
    assertEquals(chrom.getPersistentRepresentation(),
                 chrom2.getPersistentRepresentation());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_2()
      throws Exception {
    Chromosome chrom = new Chromosome(conf);
    chrom.setValueFromPersistentRepresentation(null);
    assertEquals(0, chrom.size());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_3()
      throws Exception {
    Chromosome chrom = new Chromosome(conf);
    try {
      chrom.setValueFromPersistentRepresentation("1"
          + Chromosome.CHROM_DELIMITER
          + "2");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_4()
      throws Exception {
    Chromosome chrom = new Chromosome(conf);
    try {
      chrom.setValueFromPersistentRepresentation("1"
          + Chromosome.CHROM_DELIMITER
          + "0"
          + Chromosome.CHROM_DELIMITER);
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   * Invalid closing tag.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_6()
      throws Exception {
    Chromosome chrom = new Chromosome(conf);
    try {
      chrom.setValueFromPersistentRepresentation("47.11"
          + Chromosome.CHROM_DELIMITER
          + "1"
          + Chromosome.CHROM_DELIMITER
          + "<" + IntegerGene.class.getName()
          + Chromosome.GENE_DELIMITER
          + "2<");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_7()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Gene[] genes1 = new Gene[2];
    genes1[0] = new BooleanGene(conf);
    genes1[1] = new DoubleGene(conf);
    Chromosome chrom = new Chromosome(conf, genes1);
    String repr = chrom.getPersistentRepresentation();
    Chromosome chrom2 = new Chromosome(conf);
    chrom2.setValueFromPersistentRepresentation(repr);
    assertEquals(chrom, chrom2);
    assertEquals(chrom.getPersistentRepresentation(),
                 chrom2.getPersistentRepresentation());
  }

  /**
   * Empty representation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPersistentRepresentation_5()
      throws Exception {
    Chromosome chrom = new Chromosome(conf);
    try {
      chrom.setValueFromPersistentRepresentation("47.11"
          + Chromosome.CHROM_DELIMITER
          + "1"
          + Chromosome.CHROM_DELIMITER
          + "<" + IntegerGene.class.getName()
          + Chromosome.GENE_DELIMITER
          + "2:4:4"
          + Chromosome.GENE_DELIMITER
          + "><>");
      fail();
    } catch (UnsupportedRepresentationException uex) {
      ; //this is OK
      assertEquals(1, chrom.size());
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testAlwaysCalculate_0() throws Exception {
    conf.setFitnessFunction(new TestFitnessFunction());
    MyChromosome chrom = new MyChromosome(conf);
    boolean value = (Boolean)privateAccessor.getField(chrom, "m_alwaysCalculate");
    assertFalse(value);
    //
    chrom.getFitnessValue();
    assertTrue(chrom.isCalculated);
    //
    chrom.isCalculated = false;
    chrom.getFitnessValue();
    assertFalse(chrom.isCalculated);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testAlwaysCalculate_1() throws Exception {
    conf.setAlwaysCaculateFitness(true);
    Chromosome chrom = new Chromosome(conf);
    boolean value = (Boolean)privateAccessor.getField(chrom, "m_alwaysCalculate");
    assertTrue(value);
    //
    conf.setAlwaysCaculateFitness(false);
    value = (Boolean)privateAccessor.getField(chrom, "m_alwaysCalculate");
    assertTrue(value);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void testAlwaysCalculate_2() throws Exception {
    conf.setFitnessFunction(new TestFitnessFunction());
    conf.setAlwaysCaculateFitness(true);
    MyChromosome chrom = new MyChromosome(conf);
    boolean value = (Boolean)privateAccessor.getField(chrom, "m_alwaysCalculate");
    assertTrue(value);
    //
    chrom.getFitnessValue();
    assertTrue(chrom.isCalculated);
    //
    chrom.isCalculated = false;
    chrom.getFitnessValue();
    assertTrue(chrom.isCalculated);
    //
    conf.setAlwaysCaculateFitness(false);
    value = (Boolean)privateAccessor.getField(chrom, "m_alwaysCalculate");
    assertTrue(value);
  }
}
