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

import org.jgap.Configuration;
import org.jgap.GeneticOperator;
import org.jgap.event.EventManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for DefaultConfiguration class

 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultConfigurationTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
    assertEquals(3, conf.getGeneticOperators().size());
    //test if all 3 slots are occupied by the 3 default GeneticOperator's
    int code = 0;
    GeneticOperator op;
    for (int i = 0; i < 3; i++) {
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
    assertEquals(7, code);
  }
}
