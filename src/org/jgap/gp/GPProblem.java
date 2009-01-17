/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.*;

import java.awt.*;
import javax.swing.tree.*;

import org.jgap.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.util.tree.*;

/**
 * Abstract base class for all GP problems. See packages examples.gp and
 * examples.grid for sample implementations.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private GPConfiguration m_conf;

  public GPProblem(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    if (a_conf == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_conf = a_conf;
  }

  /**
   * Default constructor for dynamic instantiation.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPProblem() {

  }

  /**
   * @return newly created GPGenotype
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public abstract GPGenotype create()
      throws InvalidConfigurationException;

  /**
   * Creates a graphical tree out of a given GP program and saves it to a file.
   *
   * @param a_prog the GP program to visualize a tree for
   * @param a_filename the name of the file to save the tree in
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void showTree(IGPProgram a_prog, String a_filename)
      throws InvalidConfigurationException {
    if (a_prog == null) {
      return;
    }
    TreeNode myTree = createTree(a_prog);
    if (myTree == null) {
      return;
    }
    TreeVisualizer tv = new TreeVisualizer();
    tv.setTreeBranchRenderer(new JGAPTreeBranchRenderer());
    tv.setTreeNodeRenderer(new JGAPTreeNodeRenderer());
    tv.setBranchStartWidth(18.0);
    tv.setArenaColor(Color.black);
    tv.setBkgndColor(Color.black);
    tv.setRenderNodes(true);
    tv.setSide(1024);
    tv.setCircleDiminishFactor(0.5);
    tv.writeImageFile(tv.renderTree(myTree), new File(a_filename));
  }

  /**
   * Creates a tree out of a given GP program and saves it to a file. Allows to
   * preset the tree renderers.
   *
   * @param a_prog the GP program to visualize a tree for
   * @param a_filename the name of the file to save the tree in
   * @param a_treeBranchRenderer renderer for the tree's branches
   * @param a_treeNodeRenderer renderer for the tree's nodes
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void showTree(IGPProgram a_prog, String a_filename,
                       TreeBranchRenderer a_treeBranchRenderer,
                       TreeNodeRenderer a_treeNodeRenderer)
      throws InvalidConfigurationException {
    TreeNode myTree = createTree(a_prog);
    if (myTree == null) {
      return;
    }
    TreeVisualizer tv = new TreeVisualizer();
    tv.setTreeBranchRenderer(a_treeBranchRenderer);
    tv.setTreeNodeRenderer(a_treeNodeRenderer);
    tv.setBranchStartWidth(18.0);
    tv.setArenaColor(Color.black);
    tv.setBkgndColor(Color.black);
    tv.setRenderNodes(true);
    tv.setSide(1024);
    tv.setCircleDiminishFactor(0.5);
    tv.writeImageFile(tv.renderTree(myTree), new File(a_filename));
  }

  /**
   * Creates a tree out of a given GP program.
   *
   * @param a_prog the GPGenotype to visualize a tree for
   * @return the TreeNode object corresponding to the GP program
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public TreeNode createTree(IGPProgram a_prog)
      throws InvalidConfigurationException {
    if (a_prog == null) {
      return null;
    }
    ProgramChromosome master = new ProgramChromosome(m_conf);
    master.setIndividual(a_prog);
    TreeNode tree;
    if (a_prog.size() > 1) {
      Class[] types = new Class[a_prog.size()];
      for (int i = 0; i < a_prog.size(); i++) {
        types[i] = CommandGene.VoidClass; //this is arbitrary
      }
      master.setGene(0, new SubProgram(m_conf, types));
      int index = 1;
      for (int i = 0; i < a_prog.size(); i++) {
        ProgramChromosome child = a_prog.getChromosome(i);
        for (int j = 0; j < child.size(); j++) {
          master.setGene(index++, child.getGene(j));
        }
      }
      master.redepth();
      tree = new JGAPTreeNode(master, 0);
    }
    else {
      tree = new JGAPTreeNode(a_prog.getChromosome(0), 0);
    }
    return tree;
  }

  /**
   * @return the GPConfiguration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPConfiguration getGPConfiguration() {
    return m_conf;
  }

  /**
   * Sets the configuration. Only use in case of dynamic instantiation (in case
   * constructor with parameter GPConfiguration is not used).
   *
   * @param a_conf the configuration to set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected void setGPConfiguration(GPConfiguration a_conf) {
    m_conf = a_conf;
  }
}
