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

/**
 * The IDataCreators interface represents an entity comparable to
 * org.w3c.dom.Document
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IDataCreators {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.6 $";

  void setTree(IDataElementList a_tree);

  /**
   * @return the tree (of elements) held by the implementing class
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  IDataElementList getTree();

  /**
   * Constructs a new instance of the entity implementing IDataCreators
   * @throws Exception
   * @return new instance of the entity itself
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  IDataCreators newDocument()
      throws Exception;

  /**
   * Appends a child element to the tree
   * @param a_newChild the child to be added to the tree
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  void appendChild(IDataElement a_newChild)
      throws Exception;
}
