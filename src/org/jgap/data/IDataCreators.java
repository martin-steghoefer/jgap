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

/**
 * The IDataCreators interface represents an entity comparable to
 * org.w3c.dom.Document
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IDataCreators {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  void setTree(IDataElementList tree);

  /**
   * @return the tree (of elements) held by the implementing class
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  IDataElementList getTree();

  IDataCreators newDocument()
      throws Exception;

  /**
   * Appends a child element to the tree
   * @param newChild the child to be added to the tree
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  void appendChild(IDataElement newChild)
      throws Exception;
}
