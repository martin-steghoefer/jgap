/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.xml;

import org.jgap.data.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * Class building an XML file. Can be used to persist objects like Genotype,
 * Chromosome or Gene (or a list of them) to an XML file.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class XMLDocumentBuilder
    extends DocumentBuilderBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  protected void setAttribute(final Object a_xmlElement, final String a_key,
                              final String a_value) {
    ( (Element) a_xmlElement).setAttribute(a_key, a_value);
  }

  protected Object documentAppendChild(final Object a_xmlDocument,
                                       final Object a_element) {
    return ( (Document) a_xmlDocument).appendChild( (Element) a_element);
  }

  protected Object elementAppendChild(final Object a_xmlElement,
                                      final Object a_element) {
    return ( (Element) a_xmlElement).appendChild( (Element) a_element);
  }

  protected Object createElement(final Object a_doc, final Object a_xmlElement,
                                 final String a_tagName) {
    return ( (Document) a_doc).createElement(a_tagName);
  }

  /**
   * Convenience method to build an XML document from a generic input structure
   * (of type IDataCreators)
   *
   * @param a_document the input structure holding the data to be represented
   * as XML document
   * @throws Exception
   * @return Object the XML document
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Object buildDocument(final IDataCreators a_document)
      throws Exception {
    DocumentBuilder m_documentCreator = DocumentBuilderFactory.newInstance().
        newDocumentBuilder();
    Document xmlDoc = m_documentCreator.newDocument();
    return super.buildDocument(a_document, xmlDoc);
  }
}
