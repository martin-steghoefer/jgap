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

package org.jgap.data;

import java.util.*;
import org.jgap.data.*;

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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
