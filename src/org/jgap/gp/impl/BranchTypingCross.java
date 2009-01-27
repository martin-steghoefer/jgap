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

import java.io.*;
import org.jgap.*;
import org.jgap.gp.*;

/**
 * Crossing over for GP ProgramChromosomes.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class BranchTypingCross
    extends CrossMethod implements Serializable, Comparable, Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  private boolean m_simpleChromosomeSelection;

  /**
   * Standard constructor.
   *
   * @param a_config the configuration to use
   */
  public BranchTypingCross(GPConfiguration a_config) {
    this(a_config, false);
  }

  /**
   *
   * @param a_config the configuration to use
   * @param a_simpleChromosomeSelection true: plainly select chromosomes,
   * false: select chromosomes proportionally to their size (=number of nodes
   * within a chromosome)
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public BranchTypingCross(GPConfiguration a_config,
          boolean a_simpleChromosomeSelection) {
    super(a_config);
    m_simpleChromosomeSelection = a_simpleChromosomeSelection;
  }

  /**
   * Crosses two individuals. A random chromosome is chosen for crossing based
   * on the proportion of nodes in each chromosome in the first individual.
   *
   * @param a_i1 the first individual to cross
   * @param a_i2 the second individual to cross
   * @return an array of the two resulting individuals
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IGPProgram[] operate(final IGPProgram a_i1,
                              final IGPProgram a_i2) {
    try {
      int chromosomeNum;
      if (!m_simpleChromosomeSelection) {
        // Determine which chromosome we'll cross, probabilistically determined
        // by the sizes of the chromosomes of the first individual.
        // This is equivalent to Koza's branch typing.
        // Advantage over plain selection: proportion of the chromosomes' sizes
        // is cared about.
        // --------------------------------------------------------------------
        int[] sizes = new int[a_i1.size()];
        int totalSize = 0;
        for (int i = 0; i < a_i1.size(); i++) {
          // Size of a chromosome = number of nodes.
          // ---------------------------------------
          sizes[i] = a_i1.getChromosome(i).getSize(0);
          totalSize += sizes[i];
        }
        int nodeNum = getConfiguration().getRandomGenerator().nextInt(
                totalSize);
        // Select the chromosome in which node "nodeNum" resides.
        // ------------------------------------------------------
        for (chromosomeNum = 0; chromosomeNum < a_i1.size(); chromosomeNum++) {
          nodeNum -= sizes[chromosomeNum];
          if (nodeNum < 0) {
            break;
          }
        }
      } else {
        // Select a chromosome directly.
        // -----------------------------
        chromosomeNum = getConfiguration().getRandomGenerator().
                nextInt(a_i1.size());
      }
      // Cross the selected chromosomes.
      // -------------------------------
      /**@todo try to ensure uniqueness for unique commands:
       * after selecting first node, check if there is a unique node in the sub
       * tree. If so, check if it appears in the sub tree of the second node.
       */
      ProgramChromosome[] newChromosomes = doCross(
          a_i1.getChromosome(chromosomeNum),
          a_i2.getChromosome(chromosomeNum));
      // Create the new individuals by copying the uncrossed chromosomes
      // and setting the crossed chromosome. There's no need to deep-copy
      // the uncrossed chromosomes because they don't change. That is,
      // even if two individuals' chromosomes point to the same chromosome,
      // the only change in a chromosome is crossing, which generates
      // deep-copied chromosomes anyway.
      // ------------------------------------------------------------------
      IGPProgram[] newIndividuals = {
          new GPProgram(a_i1), new GPProgram(a_i1)};
      for (int i = 0; i < a_i1.size(); i++)
        if (i != chromosomeNum) {
          // Unchanged, not crossed, chromosomes.
          // ------------------------------------
          newIndividuals[0].setChromosome(i, a_i1.getChromosome(i));
          newIndividuals[1].setChromosome(i, a_i2.getChromosome(i));
        }
        else {
          // The crossed chromosomes.
          // ------------------------
          newIndividuals[0].setChromosome(i, newChromosomes[0]);
          newIndividuals[1].setChromosome(i, newChromosomes[1]);
        }
      return newIndividuals;
    } catch (InvalidConfigurationException iex) {
      return null;
    }
  }

  /**
   * Crosses two chromsomes using branch-typing.
   * A random point in the first chromosome is chosen, with a certain probability
   * it will be a function and with a rest probability it will be a terminal. A
   * random point in the second chromosome is chosen using the same probability
   * distribution, but the node chosen must be of the same type as the chosen
   * node in the first chromosome.
   * If a suitable point in the second chromosome couldn't be found then the
   * chromosomes are not crossed.
   * If a resulting chromosome's depth is larger than the maximum crossover
   * depth then that chromosome is simply copied from the original
   * rather than crossed.
   *
   * @param a_c0 the first chromosome to cross
   * @param a_c1 the second chromosome to cross
   * @return an array of the two resulting chromosomes
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected ProgramChromosome[] doCross(ProgramChromosome a_c0,
                                        ProgramChromosome a_c1)
      throws InvalidConfigurationException {
    ProgramChromosome[] c = {
        a_c0, a_c1};
    // Choose a point in c1.
    // ---------------------
    int p0;
    RandomGenerator random = getConfiguration().getRandomGenerator();
    if (random.nextFloat() < getConfiguration().getFunctionProb()) {
      // Choose a function.
      // ------------------
      int nf = a_c0.numFunctions();
      if (nf == 0) {
        // No functions there.
        // -------------------
        return c;
      }
      int fctIndex = random.nextInt(nf);
      p0 = a_c0.getFunction(fctIndex);
    }
    else {
      // Choose a terminal.
      // ------------------
      p0 = a_c0.getTerminal(random.nextInt(a_c0.numTerminals()));
      // Mutate the command's value.
      // ----------------------------
      CommandGene command = a_c0.getNode(p0);
      if (random.nextDouble() <= getConfiguration().getMutationProb()) {
        if (IMutateable.class.isInstance(command)) {
          IMutateable term = (IMutateable) command;
          command = term.applyMutation(0, 0.3d);
          if (command != null) {
            // Check if mutant's function is allowed.
            // --------------------------------------
            if (a_c0.getCommandOfClass(0, command.getClass()) >= 0) {
              a_c0.setGene(p0, command);
            }
          }
        }
      }
    }
    // Choose a point in c2 matching the type and subtype of p0.
    // ---------------------------------------------------------
    int p1;
    CommandGene nodeP0 = a_c0.getNode(p0);
    Class type_ = nodeP0.getReturnType();
    int subType = nodeP0.getSubReturnType();
    if (random.nextFloat() < getConfiguration().getFunctionProb()) {
      // Choose a function.
      // ------------------
      int nf = a_c1.numFunctions(type_, subType);
      if (nf == 0) {
        // No functions of that type.
        // --------------------------
        return c;
      }
      p1 = a_c1.getFunction(random.nextInt(nf), type_, subType);
    }
    else {
      // Choose a terminal.
      // ------------------
      int nt = a_c1.numTerminals(type_, subType);
      if (nt == 0) {
        // No terminals of that type.
        // --------------------------
        return c;
      }
      p1 = a_c1.getTerminal(random.nextInt(a_c1.numTerminals(type_, subType)),
                          type_, subType);
      // Mutate the command's value.
      // ----------------------------
      CommandGene command = a_c1.getNode(p1);
      if (random.nextDouble() <= getConfiguration().getMutationProb()) {
        if (IMutateable.class.isInstance(command)) {
          IMutateable term = (IMutateable) command;
          command = term.applyMutation(0, 0.3d);
          if (command != null) {
            // Check if mutant's function is allowed.
            // --------------------------------------
            if (a_c0.getCommandOfClass(0, command.getClass()) >= 0) {
              a_c1.setGene(p1, command);
            }
          }
        }
      }
    }
    /**@todo solve in general*/
    if (org.jgap.gp.function.SubProgram.class.isAssignableFrom(a_c1.getFunctions()[p1].getClass())) {
      ((IMutateable)a_c1.getFunctions()[p1]).applyMutation(0, 0.5d);
    }
    int s0 = a_c0.getSize(p0); //Number of nodes in c0 from index p0
    int s1 = a_c1.getSize(p1); //Number of nodes in c1 from index p1
    int d0 = a_c0.getDepth(p0); //Depth of c0 from index p0
    int d1 = a_c1.getDepth(p1); //Depth of c1 from index p1
    int c0s = a_c0.getSize(0); //Number of nodes in c0
    int c1s = a_c1.getSize(0); //Number of nodes in c1
    // Check for depth constraint for p1 inserted into c0.
    // ---------------------------------------------------
    if (d0 - 1 + d1/*s1*/ > getConfiguration().getMaxCrossoverDepth()
        || c0s - p0 - s0 < 0
        || p0 + s1 + c0s - p0 - s0 >= a_c0.getFunctions().length) {
      // Choose the other parent.
      // ------------------------
      c[0] = a_c1;
    }
    else {
      c[0] = new ProgramChromosome(getConfiguration(),
                                   a_c0.getFunctions().length,
                                   c[0].getFunctionSet(),
                                   c[0].getArgTypes(),
                                   a_c0.getIndividual());
      System.arraycopy(a_c0.getFunctions(), 0, c[0].getFunctions(), 0, p0);
      System.arraycopy(a_c1.getFunctions(), p1, c[0].getFunctions(), p0, s1);
      System.arraycopy(a_c0.getFunctions(), p0 + s0, c[0].getFunctions(),
                       p0 + s1, c0s - p0 - s0);
      c[0].redepth();
    }
    // Check for depth constraint for p0 inserted into c1.
    // ---------------------------------------------------
    if (d1 - 1 + d0/*s0*/ > getConfiguration().getMaxCrossoverDepth()
        || c1s - p1 - s1 < 0
        || p1 + s0 + c1s - p1 - s1 >= a_c1.getFunctions().length) {
      // Choose the other parent.
      // ------------------------
      c[1] = a_c0;
    }
    else {
      c[1] = new ProgramChromosome(getConfiguration(),
                                   a_c1.getFunctions().length,
                                   c[1].getFunctionSet(),
                                   c[1].getArgTypes(),
                                   a_c1.getIndividual());
      System.arraycopy(a_c1.getFunctions(), 0, c[1].getFunctions(), 0, p1);
      System.arraycopy(a_c0.getFunctions(), p0, c[1].getFunctions(), p1, s0);
      System.arraycopy(a_c1.getFunctions(), p1 + s1, c[1].getFunctions(),
                       p1 + s0, c1s - p1 - s1);
      c[1].redepth();
    }
    return c;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return 0 or 1 in this case, as BranchTypingCross objects keep no state
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    BranchTypingCross other = (BranchTypingCross) a_other;
    if (other == null) {
      return 1;
    }
    return 0;
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return always true for non-null BranchTypingCross objects because they
   * keep no state
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      BranchTypingCross other = (BranchTypingCross) a_other;
      if (other == null) {
        return false;
      }
      else {
        return true;
      }
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    BranchTypingCross result = new BranchTypingCross(getConfiguration());
    return result;
  }
}
