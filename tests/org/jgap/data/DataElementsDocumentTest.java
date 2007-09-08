/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests for DataElementsDocument class
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DataElementsDocumentTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DataElementsDocumentTest.class);
    return suite;
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testConstruct_0()
      throws Exception {
    DataElementsDocument el = new DataElementsDocument();
    assertNotNull(el.getTree());
    assertEquals(0, el.getTree().getLength());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testNewDocument_0()
      throws Exception {
    DataElementsDocument el = new DataElementsDocument();
    assertEquals(DataElementsDocument.class, el.newDocument().getClass());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testAppendChild_0()
      throws Exception {
    DataElementsDocument el = new DataElementsDocument();
    DataElement child = new DataElement("xyz");
    el.appendChild(child);
    assertEquals(1, el.getTree().getLength());
    assertEquals(child, el.getTree().item(0));
    el.appendChild(child);
    assertEquals(2, el.getTree().getLength());
    assertEquals(child, el.getTree().item(1));
  }

}
