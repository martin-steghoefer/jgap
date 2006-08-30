/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import java.io.File;
import javax.swing.tree.TreeNode;
import org.jgap.util.tree.TreeVisualizer;
import java.awt.Color;

/**
 * Abstract base class for all GP problems. See package examples.gp for sample
 * implementations.
 *
 * @author Klaus Meffert
 * @version 1.0
 */
public abstract class GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPConfiguration m_conf;

  public GPProblem(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    if (a_conf == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_conf = a_conf;
  }

  public abstract GPGenotype create()
      throws InvalidConfigurationException;

  public void showTree(GPGenotype gp, String a_filename)
      throws InvalidConfigurationException {
    TreeNode myTree = createTree(gp.getAllTimeBest());
    if (myTree == null) {
      return;
    }
    TreeVisualizer tv = new TreeVisualizer();
    tv.setTreeBranchRenderer(new JGAPTreeBranchRenderer());
    tv.setTreeNodeRenderer(new JGAPTreeNodeRenderer());
    tv.setBranchStartWidth(18.0);
    tv.setArenaColor(Color.black);
    tv.setBkgndColor(Color.black); //new Color(10, 10, 10));
    tv.setRenderNodes(true);
    tv.setSide(1024);
    tv.setCircleDiminishFactor(0.5);
    tv.writeImageFile(tv.renderTree(myTree),
                      new File(a_filename));
  }

  public TreeNode createTree(GPProgram a_prog)
      throws InvalidConfigurationException {
    if (a_prog == null) {
      return null;
    }
    ProgramChromosome master = new ProgramChromosome(m_conf);
    TreeNode tree;
    if (a_prog.size() > 1) {
      Class[] types = new Class[a_prog.size()];
      for (int i = 0; i < a_prog.size(); i++) {
        types[i] = CommandGene.VoidClass; //arbitrary
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

  public GPConfiguration getGPConfiguration() {
    return m_conf;
  }
}
