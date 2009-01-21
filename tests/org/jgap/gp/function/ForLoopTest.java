/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import junit.framework.*;
import org.jgap.gp.*;
import org.jgap.util.ICloneable;

/**
 * Tests the ForLoop class.
 *
 * @author Klaus Meffert
 * @since 3.4
 */
public class ForLoopTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ForLoopTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testConstruct_0()
      throws Exception {
    assertNotNull(new ForLoop(m_gpconf, Double.class, 5));
    assertNotNull(new ForLoop(m_gpconf, Double.class, 1, 5));
    assertNotNull(new ForLoop(m_gpconf, Double.class, 1, 5, "myVar"));
    assertNotNull(new ForLoop(m_gpconf, Double.class, 1, 5, 2, "myVar"));
    assertNotNull(new ForLoop(m_gpconf, Double.class, 1, 5, 2, "myVar", 1, 2));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testClone_0()
      throws Exception {
    CommandGene gene = new ForLoop(m_gpconf, Double.class, 5);
    Object clone = ( (ICloneable) gene).clone();
    assertEquals(clone, gene);
    CommandGene gene2 = new ForLoop(m_gpconf, Double.class, 0, 5);
    Object clone2 = ( (ICloneable) gene2).clone();
    assertEquals(clone2, gene2);
    // Both clones should be equal as the above constructors are equivalent.
    // ---------------------------------------------------------------------
    assertEquals(clone, clone2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testClone_1()
      throws Exception {
    CommandGene gene = new ForLoop(m_gpconf, Double.class, 3);
    ForLoop clone = (ForLoop) ( (ICloneable) gene).clone();
    assertEquals(clone, gene);
    assertEquals(2, clone.getArity(null));
    CommandGene gene2 = new ForLoop(m_gpconf, Double.class, 0, 5);
    ForLoop clone2 = (ForLoop) ( (ICloneable) gene2).clone();
    assertEquals(2, clone2.getArity(null));
    assertEquals(clone2, gene2);
    assertNotSame(clone, clone2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void testClone_2()
      throws Exception {
    CommandGene gene = new ForLoop(m_gpconf, Double.class, 1, 17, 2, "x");
    assertEquals(1, gene.getArity(null));
    ForLoop clone = (ForLoop) ( (ICloneable) gene).clone();
    assertEquals(clone, gene);
    assertEquals(1, clone.getArity(null));
    assertEquals("x", clone.getVarName());
  }
}
