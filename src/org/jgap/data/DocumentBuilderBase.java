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
 * @since 1.0
 */
public abstract class DocumentBuilderBase {
  /**@todo add new class DocumentCreatorBase that reads in data written by
   * DocumentBuilderBase */

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Object buildDocument(IDataCreators a_document, Object document)
      throws Exception {
    // traverse over input structure
    IDataElementList tree = a_document.getTree();
    int len = tree.getLength();
    IDataElement elem;
    for (int i = 0; i < len; i++) {
      elem = tree.item(i);
      doTraverse(elem, document, null);
    }
    return document;
  }

  /**
   * Recursive traversing over data tree containing elements to be transformed
   * into XML tags
   * @param elem IDataElement
   * @param doc Document
   * @param xmlElement Element
   * @throws Exception
   */
  private void doTraverse(IDataElement elem, Object doc,
                          Object xmlElement)
      throws Exception {
    String tagName = elem.getTagName();
    xmlElement = createElementGeneric(doc, xmlElement, tagName);
    Map attributes = elem.getAttributes();
    Set keys = attributes.keySet();
    Iterator it = keys.iterator();
    String key, value;
    while (it.hasNext()) {
      key = (String) it.next();
      value = (String) attributes.get(key);
      setAttribute(xmlElement, key, value);
    }
    IDataElementList list = elem.getChildNodes();
    if (list != null) {
      for (int j = 0; j < list.getLength(); j++) {
        IDataElement elem2 = list.item(j);
        doTraverse(elem2, doc, xmlElement);
      }
    }
  }

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
