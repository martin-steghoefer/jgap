/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**
 * The Greedy Crossover is a specific type of crossover. It can only be is
 * applied if
 * <ul>
 * <li>
 * 1. All genes in the chromosome are different and
 * </li>
 * <li>
 * 2. The set of genes for both chromosomes is identical and only their order
 * in the chromosome can vary.
 * </li>
 * </ul>
 *
 * After the GreedyCrossover, these two conditions always remain true, so
 * it can be applied again and again.
 *
 * The algorithm throws an assertion error if the two initial chromosomes
 * does not satisfy these conditions.
 *
 * Greedy crossover can be best explained in the terms of the
 * Traveling Salesman Problem:
 *
 * The algorithm selects the first city of one parent, compares the cities
 * leaving that city in both parents, and chooses the closer one to extend
 * the tour. If one city has already appeared in the tour, we choose the
 * other city. If both cities have already appeared, we randomly select a
 * non-selected city.
 *
 * See J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
 *  <i>Genetic algorithms for the traveling salesman problem</i>.
 * In Proceedings of the Second International Conference on Genetic Algorithms.
 * Lawrence Eribaum Associates, Mahwah, NJ, 1985.
 * and also <a href="http://ecsl.cs.unr.edu/docs/techreports/gong/node3.html">
 * Sushil J. Louis & Gong Li</a>}
 *
 * @author Audrius Meskauskas
 * @author <font size=-1>Neil Rotstan, Klaus Meffert (reused code
 * from {@link org.jgap.impl.CrossoverOperator CrossoverOperator})</font>
 * @since 2.0
 */
public class GreedyCrossover
    extends BaseGeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.30 $";

  /** Switches assertions on/off. Must be true during tests and debugging. */
  boolean ASSERTIONS = true;

  private int m_startOffset = 1;

  /**
   * Default constructor for dynamic instantiation.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public GreedyCrossover()
      throws InvalidConfigurationException {
    super(Genotype.getStaticConfiguration());
  }

  /**
   * Using the given configuration.
   *
   * @param a_configuration the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0 (since 2.6 without a_configuration)
   */
  public GreedyCrossover(Configuration a_configuration)
      throws InvalidConfigurationException {
    super(a_configuration);
  }

  /**
   * Compute the distance between "cities", indicated by these two
   * given genes. The default method expects the genes to be
   * IntegerGene's and returns their absolute difference, that
   * makes sense only for tests.
   *
   * @param a_from Object
   * @param a_to Object
   * @return distance between the two given cities
   */
  public double distance(final Object a_from, final Object a_to) {
    IntegerGene from = (IntegerGene) a_from;
    IntegerGene to = (IntegerGene) a_to;
    return Math.abs(to.intValue() - from.intValue());
  }

  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    int size = Math.min(getConfiguration().getPopulationSize(),
                        a_population.size());
    int numCrossovers = size / 2;
    RandomGenerator generator = getConfiguration().getRandomGenerator();
    // For each crossover, grab two random chromosomes and do what
    // Grefenstette et al say.
    // --------------------------------------------------------------
    for (int i = 0; i < numCrossovers; i++) {
      IChromosome origChrom1 = a_population.getChromosome(generator.
                                     nextInt(size));
      IChromosome firstMate = (IChromosome)origChrom1.clone();
      IChromosome origChrom2 = a_population.getChromosome(generator.
                                     nextInt(size));
      IChromosome secondMate = (IChromosome)origChrom2.clone();
      // In case monitoring is active, support it.
      // -----------------------------------------
      if (m_monitorActive) {
        firstMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
        firstMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
        secondMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
        secondMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
      }
      operate(firstMate, secondMate);
      // Add the modified chromosomes to the candidate pool so that
      // they'll be considered for natural selection during the next
      // phase of evolution.
      // -----------------------------------------------------------
      a_candidateChromosomes.add(firstMate);
      a_candidateChromosomes.add(secondMate);
    }
  }

  /**
   * Performs a greedy crossover for the two given chromosoms.
   *
   * @param a_firstMate the first chromosome to crossover on
   * @param a_secondMate the second chromosome to crossover on
   * @throws Error if the gene set in the chromosomes is not identical
   *
   * @author Audrius Meskauskas
   * @since 2.1
   */
  public void operate(final IChromosome a_firstMate,
                      final IChromosome a_secondMate) {
    Gene[] g1 = a_firstMate.getGenes();
    Gene[] g2 = a_secondMate.getGenes();
    Gene[] c1, c2;
    try {
      c1 = operate(g1, g2);
      c2 = operate(g2, g1);
      a_firstMate.setGenes(c1);
      a_secondMate.setGenes(c2);
    }
    catch (InvalidConfigurationException cex) {
      throw new Error("Error occured while operating on:"
                      + a_firstMate + " and "
                      + a_secondMate
                      + ". First " + m_startOffset + " genes were excluded "
                      + "from crossover. Error message: "
                      + cex.getMessage());
    }
  }

  protected Gene[] operate(final Gene[] a_g1, final Gene[] a_g2) {
    int n = a_g1.length;
    LinkedList out = new LinkedList();
    TreeSet not_picked = new TreeSet();
    out.add(a_g1[m_startOffset]);
    for (int j = m_startOffset + 1; j < n; j++) { // g[m_startOffset] picked
      if (ASSERTIONS && not_picked.contains(a_g1[j])) {
        throw new Error("All genes must be different for "
                        + getClass().getName()
                        + ". The gene " + a_g1[j] + "[" + j
                        + "] occurs more "
                        + "than once in one of the chromosomes. ");
      }
      not_picked.add(a_g1[j]);
    }
    if (ASSERTIONS) {
      if (a_g1.length != a_g2.length) {
        throw new Error("Chromosome sizes must be equal");
      }
      for (int j = m_startOffset; j < n; j++) {
        if (!not_picked.contains(a_g2[j])) {
          if (!a_g1[m_startOffset].equals(a_g2[j])) {
            throw new Error("Chromosome gene sets must be identical."
                            + " First gene set: " + a_g1
                            + ", second gene set: " + a_g2);
          }
        }
      }
    }
    while (not_picked.size() > 1) {
      Gene last = (Gene) out.getLast();
      Gene n1 = findNext(a_g1, last);
      Gene n2 = findNext(a_g2, last);
      Gene picked, other;
      boolean pick1;
      if (n1 == null) {
        pick1 = false;
      }
      else if (n2 == null) {
        pick1 = true;
      }
      else {
        pick1 = distance(last, n1) < distance(last, n2);
      }
      if (pick1) {
        picked = n1;
        other = n2;
      }
      else {
        picked = n2;
        other = n1;
      }
      if (out.contains(picked)) {
        picked = other;
      }
      if (picked == null || out /* still */.contains(picked)) {
        // select a non-selected // it is not random
        picked = (Gene) not_picked.first();
      }
      out.add(picked);
      not_picked.remove(picked);
    }
    if (ASSERTIONS && not_picked.size() != 1) {
      throw new Error("Given Gene not correctly created (must have length > 1"
                      + ")");
    }
    out.add(not_picked.last());
    Gene[] g = new Gene[n];
    Iterator gi = out.iterator();
    for (int i = 0; i < m_startOffset; i++) {
      g[i] = a_g1[i];
    }
    if (ASSERTIONS) {
      if (out.size() != g.length - m_startOffset) {
        throw new Error("Unexpected internal error. "
                        + "These two must be equal: " + out.size()
                        + " and " + (g.length - m_startOffset) + ", g.length "
                        + g.length + ", start offset " + m_startOffset);
      }
    }
    for (int i = m_startOffset; i < g.length; i++) {
      g[i] = (Gene) gi.next();
    }
    return g;
  }

  protected Gene findNext(final Gene[] a_g, final Gene a_x) {
    for (int i = m_startOffset; i < a_g.length - 1; i++) {
      if (a_g[i].equals(a_x)) {
        return a_g[i + 1];
      }
    }
    return null;
  }

  /**
   * Sets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @param a_offset the start offset to use
   */
  public void setStartOffset(int a_offset) {
    m_startOffset = a_offset;
  }

  /**
   * Gets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @return the start offset used
   */
  public int getStartOffset() {
    return m_startOffset;
  }

  /**
   * Compares the given GeneticOperator to this GeneticOperator.
   *
   * @param a_other the instance against which to compare this instance
   * @return a negative number if this instance is "less than" the given
   * instance, zero if they are equal to each other, and a positive number if
   * this is "greater than" the given instance
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(final Object a_other) {
    if (a_other == null) {
      return 1;
    }
    GreedyCrossover op = (GreedyCrossover) a_other;
    if (getStartOffset() < op.getStartOffset()) {
      // start offset less, meaning more to do --> return 1 for "is greater than"
      return 1;
    }
    else if (getStartOffset() > op.getStartOffset()) {
      return -1;
    }
    else {
      // Everything is equal. Return zero.
      // ---------------------------------
      return 0;
    }
  }
}
