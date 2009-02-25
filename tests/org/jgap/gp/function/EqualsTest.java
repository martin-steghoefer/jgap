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

import org.jgap.gp.*;
import org.jgap.util.*;
import junit.framework.*;

/**
 * Tests the Equals class.
 *
 * @author Frank Pape
 * @since 3.4.3
 */
public class EqualsTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(EqualsTest.class);
    return suite;
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Frank Pape
   * @since 3.4.3
   */
  public void testConstruct_0()
      throws Exception {
    new Equals(m_gpconf, Double.class);
    new Equals(m_gpconf, Float.class);
    new Equals(m_gpconf, Integer.class);
    new Equals(m_gpconf, Double.class, 0, null);
    new Equals(m_gpconf, Double.class, 1, new int[] {
      1, 2
    });
    new Equals(m_gpconf, Float.class, 1, new int[] {
      1, 1
    });
    new Equals(m_gpconf, Integer.class, 1, new int[] {
      0, 1
    });
  }

  /**
   * @throws Exception
   *
   * @author Frank Pape
   * @since 3.4.3
   */
  public void testEquals_0()
      throws Exception {
    CommandGene gene = new Equals(m_gpconf, Double.class);
    CommandGene gene2 = new Equals(m_gpconf, Double.class);
    assertEquals(gene, gene2);
    CommandGene gene3 = new Equals(m_gpconf, Float.class);
    assertFalse(gene.equals(gene3));
  }

  /**
   * @throws Exception
   *
   * @author Frank Pape
   * @since 3.4.3
   */
  public void testClone_0()
      throws Exception {
    CommandGene gene = new Equals(m_gpconf, Double.class);
    Object clone = ( (ICloneable) gene).clone();
    assertEquals(clone, gene);
    assertNotSame(clone, gene);
    CommandGene gene2 = new Equals(m_gpconf, Double.class, 0, null);
    Object clone2 = ( (ICloneable) gene2).clone();
    assertEquals(clone2, gene2);
    assertNotSame(clone2, gene2);
    // Both clones should be equal as the above constructors are equivalent.
    // ---------------------------------------------------------------------
    assertEquals(clone, clone2);
  }

  /**
   * @throws Exception
   *
   * @author Frank Pape
   * @since 3.4.3
   */
  public void testClone_1()
      throws Exception {
    CommandGene gene = new Equals(m_gpconf, Double.class);
    Equals clone = (Equals) ( (ICloneable) gene).clone();
    assertEquals(clone, gene);
    assertNotSame(clone, gene);
    assertEquals(2, clone.getArity(null));
    CommandGene gene2 = new Equals(m_gpconf, Double.class,
                                   1, new int[] {
      0
    });
    Object clone2 = ( (ICloneable) gene2).clone();
    assertEquals(clone2, gene2);
    assertNotSame(clone2, gene2);
    assertEquals(2, clone.getArity(null));
    assertFalse(clone.equals(clone2));
  }
}
