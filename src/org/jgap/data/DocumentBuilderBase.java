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

/*
 * Abstract base class for generating a document holding its elements in a
 * tree. Inherit from this class and create your own DocumentBuilder.
 * For example, have a look at XMLDocumentBuilder
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class DocumentBuilderBase {
  /**@todo add new class DocumentCreatorBase that reads in data written by
   * DocumentBuilderBase */

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   *
   * @param a_dataholder IDataCreators the input structure holding the data to
   *   be represented as a generic document
   * @param a_document Object the document to put the elements in
   * @throws Exception
   * @return Object the document built up by adding elements
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Object buildDocument(IDataCreators a_dataholder, Object a_document)
      throws Exception {
    // traverse over input structure
    IDataElementList tree = a_dataholder.getTree();
    int len = tree.getLength();
    IDataElement elem;
    for (int i = 0; i < len; i++) {
      elem = tree.item(i);
      doTraverse(elem, a_document, null);
    }
    return a_document;
  }

  /**
   * Recursive traversing over data tree containing elements to be transformed
   * into tags
   * @param a_elem IDataElement
   * @param a_document Document
   * @param a_Element Element
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void doTraverse(IDataElement a_elem, Object a_document,
                          Object a_Element)
      throws Exception {
    String tagName = a_elem.getTagName();
    a_Element = createElementGeneric(a_document, a_Element, tagName);
    Map attributes = a_elem.getAttributes();
    Set keys = attributes.keySet();
    Iterator it = keys.iterator();
    String key, value;
    while (it.hasNext()) {
      key = (String) it.next();
      value = (String) attributes.get(key);
      setAttribute(a_Element, key, value);
    }
    IDataElementList list = a_elem.getChildNodes();
    if (list != null) {
      for (int j = 0; j < list.getLength(); j++) {
        IDataElement elem2 = list.item(j);
        doTraverse(elem2, a_document, a_Element);
      }
    }
  }

  /**
   *
   * @param document Object
   * @param element Object
   * @param tagName String
   * @return Object
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private Object createElementGeneric(Object document, Object element,
                                      String tagName) {
    if (element == null) {
      element = createElement(document, element, tagName);
      documentAppendChild(document, element);
    }
    else {
      Object xmlElement2 = createElement(document, element, tagName);
      elementAppendChild(element, xmlElement2);
      element = xmlElement2;
    }
    return element;
  }

  protected abstract Object documentAppendChild(Object document,
                                                Object element);

  protected abstract Object elementAppendChild(Object rootElement,
                                               Object childElement);

  protected abstract Object createElement(Object document, Object element,
                                          String tagName);

  protected abstract void setAttribute(Object element, String key,
                                       String value);
}
