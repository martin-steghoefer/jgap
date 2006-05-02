/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import junit.framework.*;
import org.jgap.*;

/**
 * Tests the CommandGene class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class CommandGeneTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CommandGeneTest.class);
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
    assertNotNull(new CommandGeneImpl(conf));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testToString_0()
      throws Exception {
    Gene gene = new CommandGeneImpl(conf);
    assertEquals("test", gene.toString());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetAllele_0()
      throws Exception {
    Gene gene = new CommandGeneImpl(conf);
    try {
      gene.setAllele(new Double(75));
      fail();
    }catch (UnsupportedOperationException uex) {
      ;//this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testSize_0()
      throws Exception {
    Gene gene = new CommandGeneImpl(conf);
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
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertFalse(gene.equals(null));
    assertTrue(gene.equals(gene));
    assertFalse(gene.equals(new Integer(2)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testEquals_1()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertFalse(gene.equals(null));
    assertTrue(gene.equals(gene));
    assertFalse(gene.equals(new Integer(2)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_2()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertFalse(gene.equals(null));
    assertTrue(gene.equals(gene));
    assertFalse(gene.equals(new Integer(2)));
  }

  /**
   * Compare application data.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testEquals_3()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    gene.setApplicationData(new AppDataForTest());
    CommandGeneImpl gene2 = new CommandGeneImpl(conf);
    gene2.setApplicationData(new AppDataForTest());
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
   * @since 3.0
   */
  public void testCleanup_0()
      throws Exception {
    Gene gene = new CommandGeneImpl(conf);
    gene.cleanup();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testHashCode_0()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertEquals( -81, gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testHashCode_1()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    gene.setAllele(new Double(1.5d));
    assertEquals(new Double(1.5d).hashCode(), gene.hashCode());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetEnergy_0()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertEquals(0.0, gene.getEnergy(), DELTA);
    gene.setEnergy(2.3);
    assertEquals(2.3, gene.getEnergy(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetEnergy_1()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
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
   * @since 3.0
   */
  public void testSetApplicationData_0()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
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
   * @since 3.0
   */
  public void testIsCompareApplicationData_0()
      throws Exception {
    CommandGeneImpl gene = new CommandGeneImpl(conf);
    assertFalse(gene.isCompareApplicationData());
    gene.setCompareApplicationData(false);
    assertFalse(gene.isCompareApplicationData());
    gene.setCompareApplicationData(true);
    assertTrue(gene.isCompareApplicationData());
  }

  /**
   * Test implementation of Gene interface extending abstract CommandGene class.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  class CommandGeneImpl
      extends CommandGene {

    public CommandGeneImpl(final Configuration a_config)
        throws InvalidConfigurationException {
      super(a_config,1,CommandGene.FloatClass);
    }

    protected Gene newGeneInternal() {
      return null;
    }

    public Class getChildType(int i) {
      return null;
    }
    public String toString() {
      return "test";
    }

  }
  class AppDataForTest
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
