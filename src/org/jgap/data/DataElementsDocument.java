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

/**
 * The IDataCreators interface represents an entity comparable to
 * org.w3c.dom.Document
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DataElementsDocument
    implements IDataCreators {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private IDataElementList tree;

  public void setTree(IDataElementList a_tree) {
    tree = a_tree;
  }

  /**
   * @return the tree (of elements) held by this class
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataElementList getTree() {
    return tree;
  }

  public DataElementsDocument()
      throws Exception {
    tree = new DataElementList();
  }

  public IDataCreators newDocument()
      throws Exception {
    return new DataElementsDocument();
  }

  /**
   * Appends a child element to the tree
   * @param newChild the child to be added to the tree
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void appendChild(IDataElement newChild)
      throws Exception {
    tree.add(newChild);
  }
}
