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
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  public BranchTypingCross(GPConfiguration a_config) {
    super(a_config);
  }

  /**
   * Crosses two individuals. A random chromosome is chosen for crossing based
   * probabilistically on the proportion of nodes in each chromosome in the
   * first individual.
   *
   * @param i1 the first individual to cross
   * @param i2 the second individual to cross
   * @return an array of the two resulting individuals
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IGPProgram[] operate(final IGPProgram i1,
                              final IGPProgram i2) {
    try {
      // Determine which chromosome we'll cross, probabilistically determined
      // by the sizes of the chromosomes of the first individual --
      // equivalent to Koza's branch typing.
      int[] sizes = new int[i1.size()];
      int totalSize = 0;
      for (int i = 0; i < i1.size(); i++) {
        // Size of a chromosome = number of nodes.
        // ---------------------------------------
        sizes[i] = i1.getChromosome(i).getSize(0);
        totalSize += sizes[i];
      }
      /**@todo we could also select a chromosome directly!*/
      int nodeNum = getConfiguration().getRandomGenerator().nextInt(
          totalSize);
      // Select the chromosome in which node "nodeNum" resides.
      // ------------------------------------------------------
      int chromosomeNum;
      for (chromosomeNum = 0; chromosomeNum < i1.size(); chromosomeNum++) {
        nodeNum -= sizes[chromosomeNum];
        if (nodeNum < 0)
          break;
      }
      // Cross the selected chromosomes.
      // -------------------------------
      ProgramChromosome[] newChromosomes = doCross(
          i1.getChromosome(chromosomeNum),
          i2.getChromosome(chromosomeNum));
      // Create the new individuals by copying the uncrossed chromosomes
      // and setting the crossed chromosome. There's no need to deep-copy
      // the uncrossed chromosomes because they don't change. That is,
      // even if two individuals' chromosomes point to the same chromosome,
      // the only change in a chromosome is crossing, which generates
      // deep-copied chromosomes anyway.
      // ------------------------------------------------------------------
      IGPProgram[] newIndividuals = {
          new GPProgram(i1), //getConfiguration(), i1.size()),
          new GPProgram(i1)}; //getConfiguration(), i1.size())};
      for (int i = 0; i < i1.size(); i++)
        if (i != chromosomeNum) {
          newIndividuals[0].setChromosome(i, i1.getChromosome(i));
          newIndividuals[1].setChromosome(i, i2.getChromosome(i));
        }
        else {
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
   * A random point in the first chromosome is chosen, with 90% probability it
   * will be a function and 10% probability it will be a terminal. A random
   * point in the second chromosome is chosen using the same probability
   * distribution, but the node chosen must be of the same type as the chosen
   * node in the first chromosome.<p>
   * If a suitable point in the second chromosome couldn't be found then the
   * chromosomes are not crossed.<p>
   * If a resulting chromosome's depth is larger than the World's maximum
   * crossover depth then that chromosome is simply copied from the original
   * rather than crossed.
   *
   * @param c0 the first chromosome to cross
   * @param c1 the second chromosome to cross
   * @return an array of the two resulting chromosomes
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected ProgramChromosome[] doCross(ProgramChromosome c0,
                                        ProgramChromosome c1)
      throws InvalidConfigurationException {
    ProgramChromosome[] c = {
        c0, c1};
    // Choose a point in c1.
    // ---------------------
    int p0;
    RandomGenerator random = getConfiguration().getRandomGenerator();
    if (random.nextFloat() < getConfiguration().getFunctionProb()) {
      // choose a function
      int nf = c0.numFunctions();
      if (nf == 0) {
        // no functions
        return c;
      }
      p0 = c0.getFunction(random.nextInt(nf));
    }
    else {
      // Choose a terminal.
      // ------------------
      p0 = c0.getTerminal(random.nextInt(c0.numTerminals()));
      // Mutate the terminal's value.
      // ----------------------------
      /**@todo make this random and configurable*/
      CommandGene command = c0.getNode(p0);
      if (IMutateable.class.isInstance(command)) {
        IMutateable term = (IMutateable) command;
        command = term.applyMutation(0, 0.5d);
        if (command != null) {
          // Check if mutant's function is allowed.
          // --------------------------------------
          if (c0.getCommandOfClass(0, command.getClass()) >=0) {
            c0.setGene(p0, command);
          }
        }
      }
    }
    // Choose a point in c2 matching the type and subtype of p0.
    // ---------------------------------------------------------
    int p1;
    CommandGene nodeP0 = c0.getNode(p0);
    Class type_ = nodeP0.getReturnType();
    int subType = nodeP0.getSubReturnType();
    if (random.nextFloat() < getConfiguration().getFunctionProb()) {
      // choose a function
      int nf = c1.numFunctions(type_, subType);
      if (nf == 0) {
        // No functions of that type.
        // --------------------------
        return c;
      }
      p1 = c1.getFunction(random.nextInt(nf), type_, subType);
    }
    else {
      // Choose a terminal.
      // ------------------
      int nt = c1.numTerminals(type_, subType);
      if (nt == 0) {
        // No terminals of that type.
        // --------------------------
        return c;
      }
      p1 = c1.getTerminal(random.nextInt(c1.numTerminals(type_, subType)),
                          type_, subType);
      // Mutate the terminal's value.
      // ----------------------------
      /**@todo make this random and configurable*/
      CommandGene command = c1.getNode(p1);
      if (IMutateable.class.isInstance(command)) {
        IMutateable term = (IMutateable) command;
        command = term.applyMutation(0, 0.5d);
        if (command != null) {
          // Check if mutant's function is allowed.
          // --------------------------------------
          if (c0.getCommandOfClass(0, command.getClass()) >=0) {
            c1.setGene(p1, command);
          }
        }
      }
    }
    int s0 = c0.getSize(p0); //Number of nodes in c0 from index p0
    int s1 = c1.getSize(p1); //Number of nodes in c1 from index p1
    int d0 = c0.getDepth(p0); //Depth of c0 from index p0
    int d1 = c1.getDepth(p1); //Depth of c1 from index p1
    int c0s = c0.getSize(0); //Number of nodes in c0
    int c1s = c1.getSize(0); //Number of nodes in c1

    // Check for depth constraint for p1 inserted into c0.
    // ---------------------------------------------------
    if (d0 - 1 + s1 > getConfiguration().getMaxCrossoverDepth()
        || c0s - p0 - s0 < 0
        || p0 + s1 + c0s - p0 - s0>= c0.getFunctions().length) {
      // Choose the other parent.
      // ------------------------
      c[0] = c1;
    }
    else {
      c[0] = new ProgramChromosome(getConfiguration(), c0.getFunctions().length, // c0s - s0 + s1,
                                   c[0].getFunctionSet(),
                                   c[0].getArgTypes(),
                                   c0.getIndividual());
      System.arraycopy(c0.getFunctions(), 0, c[0].getFunctions(), 0, p0);
      System.arraycopy(c1.getFunctions(), p1, c[0].getFunctions(), p0, s1);
      System.arraycopy(c0.getFunctions(), p0 + s0, c[0].getFunctions(),
                       p0 + s1, c0s - p0 - s0);
      c[0].redepth();
    }
    // Check for depth constraint for p0 inserted into c1.
    // ---------------------------------------------------
    if (d1 - 1 + s0 > getConfiguration().getMaxCrossoverDepth()
        || c1s - p1 - s1 < 0
        || p1 + s0 + c1s - p1 - s1 >= c1.getFunctions().length) {
      // Choose the other parent.
      // ------------------------
      c[1] = c0;
    }
    else {
      c[1] = new ProgramChromosome(getConfiguration(), c1.getFunctions().length, // c1s - s1 + s0,
                                   c[1].getFunctionSet(), c[1].getArgTypes(),
                                   c1.getIndividual());
      System.arraycopy(c1.getFunctions(), 0, c[1].getFunctions(), 0, p1);
      System.arraycopy(c0.getFunctions(), p0, c[1].getFunctions(), p1, s0);
      System.arraycopy(c1.getFunctions(), p1 + s1, c[1].getFunctions(),
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
