/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

import java.util.*;

/**
 * Element owning of a tag name and holding attributes.
 * <p>
 * Used by the DataTreeBuilder.
 *
 * @author Klaus Meffert
 * @since 2.0
 */

public class DataElement
    implements IDataElement {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private IDataElementList elements;

  private Map attributes;

  private String tagName;

  public DataElement(String a_tagName) {
    elements = new DataElementList();
    attributes = new HashMap();
    tagName = a_tagName;
  }

  public void setAttribute(String name, String value)
      throws Exception {
    attributes.put(name, value);
  }

  public void appendChild(IDataElement newChild)
      throws Exception {
    elements.add(newChild);
  }

  public String getTagName() {
    return tagName;
  }

  public IDataElementList getElementsByTagName(String name) {
    IDataElementList ret = new DataElementList();
    for (int i = 0; i < elements.getLength(); i++) {
      if (elements.item(i).getTagName().equals(name)) {
        ret.add(elements.item(i));
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
    return elements;
  }

  public String getAttribute(String name) {
    return (String) attributes.get(name);
  }

  public Map getAttributes() {
    return attributes;
  }
}
