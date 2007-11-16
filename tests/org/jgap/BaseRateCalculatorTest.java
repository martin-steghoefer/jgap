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

/**
 * Tests the BaseRateCalculator class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class BaseRateCalculatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(BaseRateCalculatorTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0()
      throws Exception {
    assertNotNull(new BaseRateCalculatorImpl(conf));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1()
      throws Exception {
    try {
      new BaseRateCalculatorImpl(null);
      fail();
    }
    catch (InvalidConfigurationException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetConfiguration_0()
      throws Exception {
    BaseRateCalculator calc = new BaseRateCalculatorImpl(conf);
    assertSame(conf, calc.getConfiguration());
  }

  /**
   * Test implementation of Gene interface extending abstract BaseRateCalculator
   * class.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  class BaseRateCalculatorImpl
      extends BaseRateCalculator {
    public BaseRateCalculatorImpl(final Configuration a_config)
        throws InvalidConfigurationException {
      super(a_config);
    }

    public int calculateCurrentRate() {
      return 0;
    }

    public boolean toBePermutated(IChromosome a_chrom, int a_geneIndex) {
      return true;
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
