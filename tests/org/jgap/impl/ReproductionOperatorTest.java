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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.Configuration;
import java.util.*;
import org.jgap.*;

/**
 * Tests for ReproductionOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ReproductionOperatorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public ReproductionOperatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(ReproductionOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    //test only if class can be constructed without error
    ReproductionOperator repOp = new ReproductionOperator();
  }

  public void testOperate_0() {
    ReproductionOperator repOp = new ReproductionOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{};
    repOp.operate(new Configuration(), population, candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    repOp.operate(new Configuration(), population, candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  public void testOperate_1() {
  /**@todo implement.
   * E.g. we could check if something has changed. For that use a
   * RandomGeneratorForTest*/
  }
}
