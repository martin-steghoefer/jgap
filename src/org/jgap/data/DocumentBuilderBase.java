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

/*
 * Abstract base class for generating a document holding its elements in a
 * tree. Inherit from this class and create your own DocumentBuilder.
 * For example, have a look at XMLDocumentBuilder.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class DocumentBuilderBase {
  /**@todo add new class DocumentCreatorBase that reads in data written by
   * DocumentBuilderBase */

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Builds a document from the given input (input data + existing document).
   * @param a_dataholder the input structure holding the data to
   * be represented as a generic document
   * @param a_document the document to put the elements in
   * @throws Exception
   * @return the document built up by adding elements
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Object buildDocument(final IDataCreators a_dataholder,
                              final Object a_document)
      throws Exception {
    // Traverse over input structure.
    // ------------------------------
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
   * into tags.
   * @param a_elem IDataElement
   * @param a_document Document
   * @param a_Element Element
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void doTraverse(final IDataElement a_elem, final Object a_document,
                          final Object a_Element)
      throws Exception {
    String tagName = a_elem.getTagName();
    Object element = createElementGeneric(a_document, a_Element, tagName);
    Map attributes = a_elem.getAttributes();
    Set keys = attributes.keySet();
    Iterator it = keys.iterator();
    String key, value;
    while (it.hasNext()) {
      key = (String) it.next();
      value = (String) attributes.get(key);
      setAttribute(element, key, value);
    }
    IDataElementList list = a_elem.getChildNodes();
    if (list != null) {
      for (int j = 0; j < list.getLength(); j++) {
        IDataElement elem2 = list.item(j);
        doTraverse(elem2, a_document, element);
      }
    }
  }

  /**
   * Generically creates an element (Template Method).
   * @param a_document could be used as factory to create the element with
   * @param a_element null or existing element as template
   * @param a_tagName name of tag to create for the element
   * @return created element
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private Object createElementGeneric(final Object a_document,
                                      final Object a_element,
                                      final String a_tagName) {
    Object element;
    if (a_element == null) {
      element = createElement(a_document, null, a_tagName);
      documentAppendChild(a_document, element);
    }
    else {
      Object xmlElement2 = createElement(a_document, a_element, a_tagName);
      elementAppendChild(a_element, xmlElement2);
      element = xmlElement2;
    }
    return element;
  }

  /**
   * Append a child to a given document.
   * @param a_document to append element on (e.g. org.w3c.dom.Document)
   * @param a_element to append to document (e.g. org.w3c.com.Element)
   * @return a_document with appended element (e.g. org.w3c.dom.Document)
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected abstract Object documentAppendChild(Object a_document,
                                                Object a_element);

  /**
   * Append a child to a given element.
   * @param a_rootElement to append childElement on (e.g. org.w3c.com.Element)
   * @param a_childElement to append to rootElement (e.g. org.w3c.com.Element)
   * @return rootElement with appended childElement (e.g. org.w3c.com.Element)
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected abstract Object elementAppendChild(Object a_rootElement,
                                               Object a_childElement);

  /**
   * Creates an element with help for a given document.
   * @param a_document could be used as factory to create the element with
   * @param a_element null or existing element as template
   * @param a_tagName name of tag to create for the element
   * @return created element
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected abstract Object createElement(Object a_document, Object a_element,
                                          String a_tagName);

  /**
   * Sets an attribute for a given Element.
   * @param a_element the Element to set an attribute for
   * (e.g. org.w3c.com.Element)
   * @param a_key the key of the attribute
   * @param a_value the value of the attribute
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected abstract void setAttribute(Object a_element, String a_key,
                                       String a_value);
}
