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

import org.jgap.*;
import org.jgap.event.*;

import junit.framework.*;

/**
 * Tests for DefaultConfiguration class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultConfigurationTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public DefaultConfigurationTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultConfigurationTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Configuration conf = new DefaultConfiguration();
    assertEquals(EventManager.class, conf.getEventManager().getClass());
    assertEquals(BestChromosomesSelector.class,
                 conf.getNaturalSelectors(true).get(0).getClass());
    assertEquals(StockRandomGenerator.class,
                 conf.getRandomGenerator().getClass());
    assertEquals(ChromosomePool.class, conf.getChromosomePool().getClass());
    assertEquals(2, conf.getGeneticOperators().size());
    //test if all 2 slots are occupied by the 2 default GeneticOperator's
    int code = 0;
    GeneticOperator op;
    for (int i = 0; i < 2; i++) {
      op = (GeneticOperator) conf.getGeneticOperators().get(i);
      if (op instanceof MutationOperator) {
        code = code ^ 1;
      }
      else if (op instanceof ReproductionOperator) {
        code = code ^ 2;
      }
      else if (op instanceof CrossoverOperator) {
        code = code ^ 4;
      }
    }
    assertEquals(5, code);
  }
}
