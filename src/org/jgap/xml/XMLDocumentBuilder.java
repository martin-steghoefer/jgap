/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
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
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  protected void setAttribute(Object xmlElement, String key, String value) {
    ( (Element) xmlElement).setAttribute(key, value);
  }

  protected Object documentAppendChild(Object xmlDocument, Object element) {
    return ( (Document) xmlDocument).appendChild( (Element) element);
  }

  protected Object elementAppendChild(Object xmlElement, Object element) {
    return ( (Element) xmlElement).appendChild( (Element) element);
  }

  protected Object createElement(Object doc, Object xmlElement, String tagName) {
    return ( (Document) doc).createElement(tagName);
  }

  /**
   * Convenience method to build an XML document from a generic input structure
   * (of type IDataCreators)
   * @param a_document IDataCreators the input structure holding the data to
   *   be represented as XML document
   * @throws Exception
   * @return Object the XML document
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Object buildDocument(IDataCreators a_document)
      throws Exception {
    DocumentBuilder m_documentCreator = DocumentBuilderFactory.newInstance().
        newDocumentBuilder();
    Document xmlDoc = m_documentCreator.newDocument();

    return super.buildDocument(a_document, xmlDoc);
  }
}
