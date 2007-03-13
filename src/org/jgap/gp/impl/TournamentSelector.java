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

import java.io.*;
import org.jgap.*;
import org.jgap.gp.*;

/**
 * A GP tournament selector.
 *
 * @author Javier Meseguer
 * @author Enrique D. Martí
 * @since 3.2
 */
public class TournamentSelector
    implements INaturalGPSelector, Serializable, Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

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
   * @since 3.2
   */
  public IGPProgram select(GPGenotype a_genotype) {
    GPPopulation pop = a_genotype.getGPPopulation();
    IGPProgram bestProgram = null;
    int index = 0;
    RandomGenerator random = a_genotype.getGPConfiguration().getRandomGenerator();
    IGPFitnessEvaluator evaluator = a_genotype.getGPConfiguration().
        getGPFitnessEvaluator();
    for (int i = 0; i < m_tournament_size; i++) {
      index = (int) (random.nextDouble() * pop.getPopSize());
      if (bestProgram == null) {
        bestProgram = pop.getGPProgram(index);
      }
      else {
        if (evaluator.isFitter(pop.getGPProgram(index), bestProgram)) {
          bestProgram = pop.getGPProgram(index);
        }
      }
    }
    return bestProgram;
  }

  /**
   * @return deep clone of this instande
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    TournamentSelector sel = new TournamentSelector(m_tournament_size);
    return sel;
  }
}
