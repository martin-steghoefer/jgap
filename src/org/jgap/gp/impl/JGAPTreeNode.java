/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.util.*;
import javax.swing.tree.TreeNode;
import org.jgap.gp.*;

/**
 * A CommandGene represented as a tree node.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class JGAPTreeNode
    implements TreeNode {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private ProgramChromosome m_chrom;

  private int m_index;

  public JGAPTreeNode(ProgramChromosome a_chrom, int a_index) {
    m_chrom = a_chrom;
    m_chrom.redepth();
    m_index = a_index;
  }

  public String getName() {
    return m_chrom.getFunctions()[m_index].getClass().getName();
  }

  /**
   * @param a_childIndex index of the child to get
   * @return the child TreeNode at index a_childIndex
   */
  public TreeNode getChildAt(int a_childIndex) {
    int child = m_chrom.getChild(m_index, a_childIndex);
    return new JGAPTreeNode(m_chrom, child);
  }

  /**
   * @return the number of children <code>TreeNode</code>s the receiver
   * contains
   */
  public int getChildCount() {
    int count = 0;
    try {
      do {
        if (m_chrom.getChild(m_index, count) < 0) {
          return count;
        }
        count++;
      } while (true);
    } catch (RuntimeException rex) {
      return count;
    }
  }

  /**
   * @return the parent <code>TreeNode</code> of the receiver
   */
  public TreeNode getParent() {
    return new JGAPTreeNode(m_chrom, m_chrom.getParentNode(m_index));
  }

  /**
   * @param a_node the node to retrieve the index for
   * @return the index of <code>node</code> in the receivers children.
   * If the receiver does not contain <code>node</code>, -1 will be
   * returned
   */
  public int getIndex(TreeNode a_node) {
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i).equals(a_node)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @return true if the receiver allows children
   */
  public boolean getAllowsChildren() {
    return getChildCount() > 0;
  }

  /**
   * @return true if the receiver is a leaf
   */
  public boolean isLeaf() {
    return getChildCount() == 0;
  }

  /**
   * @return the children of the receiver as an <code>Enumeration</code>
   */
  public Enumeration children() {
    Vector l = new Vector();
    for (int i = 0; i < getChildCount(); i++) {
      l.add(getChildAt(i));
    }
    return l.elements();
  }
}
