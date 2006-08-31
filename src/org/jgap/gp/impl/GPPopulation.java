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
import java.util.*;

import org.jgap.*;

import org.jgap.gp.*;

/**
 * Population for GP programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPPopulation
    implements Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * The array of GPProgram's that makeup the Genotype's population.
   */
  private IGPProgram[] m_programs;

  private transient float[] m_fitnessRank;

  private int m_popSize;

  private transient IGPProgram m_fittestProgram;

  private /*transient*/ GPConfiguration m_config;

  /**
   * Indicates whether at least one of the programs has been changed
   * (deleted, added, modified).
   */
  private boolean m_changed;

  /**
   * Indicates that the list of GPPrograms has been sorted.
   */
  private boolean m_sorted;


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

  /**
   * Needed for cloning.
   */
  private int[] m_minDepths;

  /**
   * Needed for cloning.
   */
  private int[] m_maxDepths;

  /**
   * Needed for cloning.
   */
  private int m_maxNodes;

  /*
   * @param a_config the configuration to use.
   * @param a_size the maximum size of the population in GPProgram unit
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPPopulation(GPConfiguration a_config, int a_size)
      throws InvalidConfigurationException {
//    super(a_conf, a_size);
    if (a_config == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_config = a_config;

    m_programs = new GPProgram[a_size];
    m_popSize = a_size;
    m_fitnessRank = new float[a_size];
    for (int i = 0; i < a_size; i++) {
      m_fitnessRank[i] = 0.5f;
    }
  }

  /*
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPPopulation(GPPopulation a_pop)
      throws InvalidConfigurationException {
//    super(a_pop.getConfiguration(), a_pop.getPopSize());
    m_config = a_pop.getGPConfiguration();
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

    m_maxNodes = a_pop.m_maxNodes;

    m_programs = new GPProgram[m_popSize];

    m_fitnessRank = new float[m_popSize];
    for (int i = 0; i < m_popSize; i++) {
      m_fitnessRank[i] = 0.5f;
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
      m_fitnessRank[i] = f;
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
   * @param a_minDepths contains the minimum depth allowed for each chromosome
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @param a_fullModeAllowed array of boolean values. For each chromosome there
   * is one value indicating whether the full mode for creating chromosome
   * generations during evolution is allowed (true) or not (false)
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void create(Class[] a_types, Class[][] a_argTypes,
                     CommandGene[][] a_nodeSets, int[] a_minDepths,
                     int[] a_maxDepths, int a_maxNodes,
                     boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    m_avail_types = a_types;
    m_avail_argTypes = a_argTypes;
    m_avail_nodeSets = a_nodeSets;
    m_maxDepths = a_maxDepths;
    m_minDepths = a_minDepths;
    m_maxNodes = a_maxNodes;
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
      IGPProgram program = create(a_types, a_argTypes, a_nodeSets, a_minDepths,
                                 a_maxDepths, depth, (i % 2) == 0, a_maxNodes,
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
   * @param a_minDepths contains the minimum depth allowed for each chromosome
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_depth the maximum depth of the program to create
   * @param a_grow true: grow mode, false: full mode
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @param a_fullModeAllowed array of boolean values. For each chromosome there
   * is one value indicating whether the full mode for creating chromosome
   * generations during evolution is allowed (true) or not (false)
   * @return ProgramChromosome
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IGPProgram create(Class[] a_types, Class[][] a_argTypes,
                     CommandGene[][] a_nodeSets, int[] a_minDepths, int[] a_maxDepths,
                     int a_depth, boolean a_grow, int a_maxNodes,
                     boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    GPProgram program = new GPProgram(getGPConfiguration(), a_types.length);
    program.growOrFull(a_depth, a_types, a_argTypes, a_nodeSets, a_minDepths,
                       a_maxDepths, a_grow, a_maxNodes, a_fullModeAllowed);
    return program;
  }

  public IGPProgram create(int a_depth, boolean a_grow, boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    return create(m_avail_types, m_avail_argTypes, m_avail_nodeSets, m_minDepths,
                  m_maxDepths, a_depth, a_grow, m_maxNodes, a_fullModeAllowed);
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
    return m_config;
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
  public void setGPProgram(final int a_index, final IGPProgram a_program) {
    synchronized(m_programs) {
      m_programs[a_index] = a_program;
    }
    setChanged(true);
  }

  public IGPProgram getGPProgram(int a_index) {
    return m_programs[a_index];
  }

  public IGPProgram[] getGPPrograms() {
    return m_programs;
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
   * @since 3.0
   */
  public IGPProgram determineFittestProgram() {
    if (!m_changed && m_fittestProgram != null) {
      return m_fittestProgram;
    }
    double bestFitness = -1.0d;
    FitnessEvaluator evaluator = getGPConfiguration().getFitnessEvaluator();
    double fitness;
    for (int i=0;i<m_programs.length && m_programs[i] != null;i++) {
      IGPProgram program = m_programs[i];
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

  /**
   * Sorts the GPPrograms list and returns the fittest n GPPrograms in
   * the population.
   * @param a_numberOfPrograms number of top performer GPPrograms to be
   * returned
   * @return list of the fittest n GPPrograms of the population, or the fittest
   * x GPPrograms with x = number of GPPrograms in case n > x.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public List determineFittestChromosomes(final int a_numberOfPrograms) {
    int numberOfChromosomes = Math.min(a_numberOfPrograms, m_programs.length);
    if (numberOfChromosomes <= 0) {
      return null;
    }
    if (!m_changed && m_sorted) {
      return Arrays.asList(m_programs).subList(0, numberOfChromosomes);
    }
    // Sort the list of chromosomes using the fitness comparator
    sortByFitness();
    // Return the top n chromosomes
    return Arrays.asList(m_programs).subList(0, numberOfChromosomes);
  }

  /**
  * Sorts the programs within the population according to their fitness
  * value using GPProgramFitnessComparator.
  *
  * @author Klaus Meffert
  * @since 3.0
  */
 public void sortByFitness() {
   // The following construction could be cached but wrt that the
   // evaluator registered with the configuration could change
   // --> Don't cache it!
   sort(new GPProgramFitnessComparator(getGPConfiguration().
                                        getGPFitnessEvaluator()));
   setChanged(false);
   setSorted(true);
   m_fittestProgram = m_programs[0];
 }


  public float[] getFitnessRanks() {
    return m_fitnessRank;
  }

  public float getFitnessRank(int a_index) {
    return m_fitnessRank[a_index];
  }

  /**
   * Mark that for the population the fittest program may have changed.
   *
   * @param a_changed true: population's fittest program may have changed,
   * false: fittest program evaluated earlier is still valid
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void setChanged(final boolean a_changed) {
    m_changed = a_changed;
    setSorted(false);
  }

  /**
   * @return true: population's programs (maybe) were changed,
   * false: not changed for sure
   *
   * @since 3.0
   */
  public boolean isChanged() {
    return m_changed;
  }

  /**
   * Mark the population as sorted.
   * @param a_sorted true: mark population as sorted
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void setSorted(final boolean a_sorted) {
    m_sorted = a_sorted;
  }

  /**
   * This method is not producing symmetric results as -1 is more often returned
   * than 1 (see description of return value).
   *
   * @param a_pop the other population to compare
   * @return 1: a_pop is null or having fewer programs or equal number
   * of programs but at least one not contained. 0: both populations
   * containing exactly the same programs. -1: this population contains fewer
   * programs than a_pop
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(Object a_pop) {
    GPPopulation other = (GPPopulation) a_pop;
    if (a_pop == null) {
      return 1;
    }
    int size1 = size();
    int size2 = other.size();
    if (size1 != size2) {
      if (size1 < size2) {
        return -1;
      }
      else {
        return 1;
      }
    }
    IGPProgram[] progs2 = other.getGPPrograms();
    for (int i = 0; i < size1; i++) {
      if (!containedInArray(progs2, m_programs[i])) {
        return 1;
      }
    }
    return 0;
  }

  /**
   * Checks if a program is contained within an array of programs. Assumes that
   * in the array no element will follow after the first null element.
   * @param a_progs the array to search thru
   * @param a_prog the program to find
   * @return true: program found in array via equals-method
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected boolean containedInArray(IGPProgram[] a_progs, IGPProgram a_prog) {
    for(int i=0;i<a_progs.length;i++) {
      if (a_progs[i] == null) {
        return false;
      }
      if (a_progs[i].equals(a_prog)) {
        return true;
      }
    }
    return false;
  }

  /**
   * The equals-method.
   * @param a_pop the population instance to compare with
   * @return true: given object equal to comparing one
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_pop) {
    try {
      return compareTo(a_pop) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't an Population instance
      // then we're not equal.
      // ------------------------------------------------
      return false;
    }
  }

}
