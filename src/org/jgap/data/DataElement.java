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

import java.util.*;

/**
 * Element owning a tag name and holding attributes.
 * Used by the DataTreeBuilder.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DataElement
    implements IDataElement {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private IDataElementList m_elements;

  private Map m_attributes;

  private String m_tagName;

  public DataElement(final String a_tagName) {
    m_elements = new DataElementList();
    m_attributes = new HashMap();
    m_tagName = a_tagName;
  }

  public void setAttribute(final String a_name, final String a_value)
      throws Exception {
    m_attributes.put(a_name, a_value);
  }

  public void appendChild(final IDataElement a_newChild)
      throws Exception {
    m_elements.add(a_newChild);
  }

  public String getTagName() {
    return m_tagName;
  }

  public IDataElementList getElementsByTagName(final String a_name) {
    IDataElementList ret = new DataElementList();
    for (int i = 0; i < m_elements.getLength(); i++) {
      if (m_elements.item(i).getTagName().equals(a_name)) {
        ret.add(m_elements.item(i));
      }
    }
    return ret;
  }

  public short getNodeType() {
    return 1;
  }

  public String getNodeValue()
      throws Exception {
    return null;
  }

  public IDataElementList getChildNodes() {
    return m_elements;
  }

  public String getAttribute(final String a_name) {
    return (String) m_attributes.get(a_name);
  }

  public Map getAttributes() {
    return m_attributes;
  }
}
