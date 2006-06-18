package org.jgap.audit;

import java.util.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the PermutingConfiguration class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class PermutingConfigurationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(PermutingConfigurationTest.class);
    return suite;
  }

  /**
   * Test construction with empty fitness function, population size 0, random
   * generator initial, sample chromosome initial of reference configuration.
   * No error should occur.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0()
      throws Exception {
    Configuration conf1 = new DefaultConfiguration();
    new PermutingConfiguration(conf1);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_1()
      throws Exception {
    Configuration conf1 = new DefaultConfiguration();
    conf1.setFitnessFunction(new StaticFitnessFunction(0.5d));
    PermutingConfiguration conf = new PermutingConfiguration(conf1);
    assertEquals(StaticFitnessFunction.class,
                 conf.getFitnessFunction().getClass());
    assertEquals(EventManager.class, conf.getEventManager().getClass());
    assertEquals(DefaultFitnessEvaluator.class,
                 conf.getFitnessEvaluator().getClass());
    assertEquals(0, conf.getNaturalSelectorsSize(true));
    assertEquals(StockRandomGenerator.class,
                 conf.getRandomGenerator().getClass());
    assertEquals(ChromosomePool.class, conf.getChromosomePool().getClass());
    assertEquals(0, conf.getGeneticOperators().size());
    assertNull(conf.getBulkFitnessFunction());
  }

  /**
   * Tests if the correct number of permutations appears and that they are
   * unique.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testPermute_0()
      throws Exception {
    Map cache = new Hashtable();
    Configuration conf1 = new DefaultConfiguration();
    PermutingConfiguration conf = new PermutingConfiguration(conf1);
    // 2 possible combinations (because only 1 RandomGenerator a time possible)
    conf.addRandomGeneratorSlot(new StockRandomGenerator());
    conf.addRandomGeneratorSlot(new GaussianRandomGenerator());
    // 3 possible combinations (multiple NaturalSelector's possible)
    conf.addNaturalSelectorSlot(new BestChromosomesSelector(conf));
    conf.addNaturalSelectorSlot(new WeightedRouletteSelector(conf));
    // 3 possible combinations (multiple GeneticOperator's possible)
    conf.addGeneticOperatorSlot(new MutationOperator(conf));
    conf.addGeneticOperatorSlot(new CrossoverOperator(conf));
    // 1 possible combinations
    conf.addFitnessFunctionSlot(new StaticFitnessFunction(0.5d));
    assertTrue(conf.hasNext());
    Configuration c;
    for (int i = 0; i < 18; i++) { // 18 = 2 * 3 * 3 * 1
      c = conf.next();
      assertNotNull(c);
      // add to cache and check that object is unique among cached ones
      int hash = c.getRandomGenerator().hashCode()
          + c.getFitnessFunction().hashCode();
      for (int j = 0; j < c.getNaturalSelectorsSize(true); j++) {
        hash += c.getNaturalSelector(true, j).hashCode();
      }
      for (int j = 0; j < c.getGeneticOperators().size(); j++) {
        hash += c.getGeneticOperators().get(j).hashCode();
      }
      Integer hashI = new Integer(hash);
      assertFalse(cache.containsKey(hashI));
      cache.put(hashI, hashI);
    }
    assertFalse(conf.hasNext());
  }

  /**
   * Permutation without chromosome pool
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testPermute_1()
      throws Exception {
    Configuration conf1 = new DefaultConfiguration();
    conf1.setChromosomePool(null);
    conf1.setFitnessFunction(new StaticFitnessFunction(0.5d));
    PermutingConfiguration conf = new PermutingConfiguration(conf1);
    assertFalse(conf.hasNext());
    conf.addFitnessFunctionSlot(new StaticFitnessFunction(0.5d));
    conf.addRandomGeneratorSlot(new StockRandomGenerator());
    conf.addNaturalSelectorSlot(new BestChromosomesSelector(conf));
    conf.addGeneticOperatorSlot(new MutationOperator(conf));
    assertTrue(conf.hasNext());
    Configuration c = conf.next();
    assertNull(c.getChromosomePool());
  }

  /**
   * Ensures that not supported methods throw an exception
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNotSupported_1()
      throws Exception {
    Configuration conf1 = new DefaultConfiguration();
    conf1.setFitnessFunction(new StaticFitnessFunction(0.5d));
    PermutingConfiguration conf = new PermutingConfiguration(conf1);
    try {
      conf.addNaturalSelector(null, true);
      fail();
    } catch (UnsupportedOperationException uex) {
      ;//this is OK
    }
    try {
      conf.addGeneticOperator(null);
      fail();
    } catch (UnsupportedOperationException uex) {
      ;//this is OK
    }
  }
}
