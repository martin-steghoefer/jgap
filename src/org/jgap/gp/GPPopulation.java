/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import java.util.*;

/**
 * Population for GP programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPPopulation
    extends Population {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  private GPProgram[] m_programs;

  public transient float[] fitnessRank;/**@todo make private*/

  private int m_popSize;

  private GPProgram m_fittestProgram;

  /**
   * Needed for cloning.
   */
  private Class[] m_avail_types;

  /**
   * Needed for cloning.
   */
  private Class[][] m_avail_argTypes;

  /**
   * Needed for cloning.
   */
  private CommandGene[][] m_avail_nodeSets;

  private int[] m_minDepths;
  private int[] m_maxDepths;
  /*
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPPopulation(GPConfiguration a_conf, int a_size)
      throws InvalidConfigurationException {
    super(a_conf, a_size);
    m_programs = new GPProgram[a_size];
    m_popSize = a_size;
    fitnessRank = new float[a_size];
    for (int i = 0; i < a_size; i++) {
      fitnessRank[i] = 0.5f;
    }
  }

  /*
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPPopulation(GPPopulation a_pop)
      throws InvalidConfigurationException {
    super(a_pop.getConfiguration(), a_pop.getPopSize());

    // Clone important state variables.
    // --------------------------------
    m_avail_argTypes = (Class[][])a_pop.m_avail_argTypes.clone();
    m_avail_types = (Class[])a_pop.m_avail_types.clone();
    m_avail_nodeSets = (CommandGene[][])a_pop.m_avail_nodeSets.clone();

    if (a_pop.m_maxDepths != null) {
      m_maxDepths = (int[]) a_pop.m_maxDepths.clone();
    }
    if (a_pop.m_minDepths != null) {
      m_minDepths = (int[]) a_pop.m_minDepths.clone();
    }
    m_popSize = a_pop.getPopSize();

    m_programs = new GPProgram[m_popSize];/**@todo is m_popSize correct?*/

    fitnessRank = new float[m_popSize];
    for (int i = 0; i < m_popSize; i++) {
      fitnessRank[i] = 0.5f;
    }
  }

  /**
   * Sorts the population into "ascending" order using some criterion for
   * "ascending". A Comparator is given which will compare two individuals,
   * and if one individual compares lower than another individual, the first
   * individual will appear in the population before the second individual.
   *
   * @param c the Comparator to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void sort(Comparator c) {
    Arrays.sort(m_programs, c);
    float f = 0;
    for (int i = 0; i < m_programs.length; i++) {
      fitnessRank[i] = f;
      f += m_programs[i].getFitnessValue();
    }
  }

  /**
   * Creates a population using the ramped half-and-half method. Adapted from
   * JGProg.
   *
   * @param a_types the type of each chromosome, the length
   * is the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void create(Class[] a_types, Class[][] a_argTypes,
                     CommandGene[][] a_nodeSets, int[] a_minDepths, int[] a_maxDepths,
                     boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    m_avail_types = a_types;
    m_avail_argTypes = a_argTypes;
    m_avail_nodeSets = a_nodeSets;
    m_maxDepths = a_maxDepths;
    m_minDepths = a_minDepths;
    int divisor;
    if (m_popSize < 2) {
      divisor = 1;
    }
    else {
      divisor = m_popSize - 1;
    }
    for (int i = 0; i < m_popSize; i++) {
      // Vary depth dependent on run index.
      // ----------------------------------
      int depth = 2 +
          (getGPConfiguration().getMaxInitDepth() - 1) * i / divisor;
      GPProgram program = create(a_types, a_argTypes, a_nodeSets, a_minDepths,
                                 a_maxDepths, depth, (i % 2) == 0,
                                 a_fullModeAllowed);
      setGPProgram(i, program);
    }
    setChanged(true);
  }

  /**
   * Creates a complete, valid ProgramChromosome.
   *
   * @param a_types the type of each chromosome, the length
   * is the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_depth the maximum depth of the program to create
   * @param a_grow true: grow mode, false: full mode
   * @return ProgramChromosome
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgram create(Class[] a_types, Class[][] a_argTypes,
                     CommandGene[][] a_nodeSets, int[] a_minDepths, int[] a_maxDepths,
                     int a_depth, boolean a_grow, boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    GPProgram program = new GPProgram(getGPConfiguration(), a_types.length);
    program.growOrFull(a_depth, a_types, a_argTypes, a_nodeSets, a_minDepths,
                       a_maxDepths, a_grow, a_fullModeAllowed);
    return program;
  }

  protected GPProgram create(int a_depth, boolean a_grow, boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    return create(m_avail_types, m_avail_argTypes, m_avail_nodeSets, m_minDepths,
                  m_maxDepths, a_depth, a_grow, a_fullModeAllowed);
  }

  /**
   * @return fixed size of the population
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getPopSize() {
    return m_popSize;
  }

  /**
   * @return the GPConfiguration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPConfiguration getGPConfiguration() {
    return (GPConfiguration)getConfiguration();
  }

  /**
   * Sets the given GPProgram at the given index in the list of GPProgram's.
   * If the given index is exceeding the list by one, the chromosome is
   * appended.
   *
   * @param a_index the index to set the GPProgram in
   * @param a_program the GPProgram to be set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setGPProgram(final int a_index, final GPProgram a_program) {
    synchronized(m_programs) {
      m_programs[a_index] = a_program;
    }
    setChanged(true);
  }

  public GPProgram getGPProgram(int a_index) {
    return m_programs[a_index];
  }

  public int size() {
    return m_programs.length;
  }

  /**
   * Determines the fittest GPProgram in the population (the one with the
   * highest fitness value) and memorizes it. This is an optimized version
   * compared to calling determineFittesPrograms(1).
   * @return the fittest GPProgram of the population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public GPProgram determineFittestProgram() {
//    if (!m_changed && m_fittestChromosome != null) {
//      return m_fittestChromosome;
//    }
    double bestFitness = -1.0d;
    FitnessEvaluator evaluator = getConfiguration().getFitnessEvaluator();
    double fitness;
    for (int i=0;i<m_programs.length && m_programs[i] != null;i++) {
      GPProgram program = m_programs[i];
      fitness = program.getFitnessValue();
      if (evaluator.isFitter(fitness, bestFitness)
          || m_fittestProgram == null) {
        m_fittestProgram = program;
        bestFitness = fitness;
      }
    }
    setChanged(false);
    return m_fittestProgram;
  }
}
