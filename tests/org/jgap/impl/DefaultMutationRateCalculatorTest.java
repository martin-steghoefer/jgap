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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for DefaultMutationRateCalculator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultMutationRateCalculatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";
  public DefaultMutationRateCalculatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultMutationRateCalculatorTest.class);
    return suite;
  }

  public void testCalculateCurrentRate_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    MutationRateCalculator calc = new DefaultMutationRateCalculator();
    Gene gene = new IntegerGene(1, 5);
    Chromosome chrom = new Chromosome(gene, 50);
    conf.setSampleChromosome(chrom);
    int rate = calc.calculateCurrentRate(conf);
    assertEquals(conf.getChromosomeSize(), rate);
    chrom = new Chromosome(gene, 30);
    conf.setSampleChromosome(chrom);
    rate = calc.calculateCurrentRate(conf);
    assertEquals(conf.getChromosomeSize(), rate);
  }

  /**
   * If there are zero chromosomes in the config., the mutation rate
   * nevertheless should be 1, because Random needs positive integers as input
   * (see MutationOperator.operate for calling Random class)
   * @throws Exception
   */
  public void testCalculateCurrentRate_1() throws Exception {
    Configuration conf = new DefaultConfiguration();
    MutationRateCalculator calc = new DefaultMutationRateCalculator();
    int rate = calc.calculateCurrentRate(conf);
    assertEquals(1, rate);
  }
}
