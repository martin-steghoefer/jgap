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

package org.jgap.xml;

import org.jgap.data.*;
import org.w3c.dom.*;

/**
 * Class building an XML file. Can be used to persiste objects like Genotype,
 * Chromosome or Gene (or a list of them) to an XML file.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class XMLDocumentBuilder
    extends DocumentBuilderBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

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
}
