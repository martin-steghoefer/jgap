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
import java.util.*;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * A GP tournament selector. The winner is determined by letting fight a number
 * of opponents against each other. The best of all wins.
 *
 * @author Javier Meseguer
 * @author Enrique D. Martí
 * @author Klaus Meffert
 * @since 3.2
 */
public class TournamentSelector
    implements INaturalGPSelector, Serializable, Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private int m_tournament_size;

  /**
   * Constructor with default tournament size.
   *
   * @since 3.2
   */
  public TournamentSelector() {
    this(5);
  }

  /**
   * Preferred Constructor.
   *
   * @param a_tournament_size the size of the tournament
   *
   * @since 3.2
   */
  public TournamentSelector(int a_tournament_size) {
    setTournamentSize(a_tournament_size);
  }

  /**
   * @param a_tournament_size the size of the tournament
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void setTournamentSize(int a_tournament_size) {
    m_tournament_size = a_tournament_size;
  }

  /**
   * Does the tournament selection.
   *
   * @param a_genotype the genotype containing the competers
   * @return program that won the tournament
   *
   * @author Javier Meseguer
   * @author Enrique D. Martí
   * @author Klaus Meffert
   * @since 3.2
   */
  public IGPProgram select(GPGenotype a_genotype) {
    GPPopulation pop = a_genotype.getGPPopulation();
    IGPProgram bestProgram = null;
    int index = 0;
    RandomGenerator random = a_genotype.getGPConfiguration().getRandomGenerator();
    IGPFitnessEvaluator evaluator = a_genotype.getGPConfiguration().
        getGPFitnessEvaluator();
    int popSize = pop.getPopSize();
    // Care that in one tournament each individual is only considered once!
    // --------------------------------------------------------------------
    List<Integer> indexes = new Vector(popSize);
    for (int i = 0; i < popSize; i++) {
      indexes.add(i);
    }
    for (int i = 0; i < m_tournament_size; i++) {
      index = (int) (random.nextDouble() * indexes.size());
      int realIndex = indexes.get(index);
      if (bestProgram == null) {
        bestProgram = pop.getGPProgram(realIndex);
      }
      else {
        IGPProgram prog = pop.getGPProgram(realIndex);
        if (evaluator.isFitter(prog, bestProgram)) {
          bestProgram = prog;
        }
      }
      if (i < m_tournament_size - 1) {
        indexes.remove(index);
      }
    }
    return bestProgram;
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    TournamentSelector sel = new TournamentSelector(m_tournament_size);
    return sel;
  }
}
