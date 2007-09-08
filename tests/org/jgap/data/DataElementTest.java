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
 * Tests for DataElement class
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DataElementTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DataElementTest.class);
    return suite;
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testAppendChild_0()
      throws Exception {
    DataElement el = new DataElement("xyz");
    DataElement child = new DataElement("tag1");
    el.appendChild(child);
    assertEquals(1, el.getChildNodes().getLength());
    assertEquals(child, el.getChildNodes().item(0));
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetElementsByTagName_0()
      throws Exception {
    DataElement el = new DataElement("xyz");
    assertEquals("xyz", el.getTagName());
    IDataElementList list = el.getElementsByTagName("myTag");
    assertEquals(0, list.getLength());
    DataElement child = new DataElement("tag1");
    el.appendChild(child);
    list = el.getElementsByTagName("myTag");
    assertEquals(0, list.getLength());
    DataElement child2 = new DataElement("myTag");
    el.appendChild(child2);
    DataElement child3 = new DataElement("tag3");
    el.appendChild(child3);
    list = el.getElementsByTagName("myTag");
    assertEquals(1, list.getLength());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetNodeType_0()
      throws Exception {
    DataElement el = new DataElement("xyz");
    assertEquals(1, el.getNodeType());
    assertNull(el.getNodeValue());
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testAttributes_0()
      throws Exception {
    DataElement el = new DataElement("xyz");
    assertNull(el.getNodeValue());
    assertEquals(0, el.getAttributes().size());
    el.setAttribute("att1", "val1");
    assertEquals(1, el.getAttributes().size());
    assertEquals("val1", el.getAttributes().get("att1"));
    el.setAttribute("att2", "val2");
    assertEquals(2, el.getAttributes().size());
    assertEquals("val2", el.getAttributes().get("att2"));
  }
}
