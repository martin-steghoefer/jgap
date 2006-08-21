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
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public transient float[] fitnessRank;

  private int m_popSize;

  private Class[] m_avail_types;

  private Class[][] m_avail_argTypes;

  private CommandGene[][] m_avail_nodeSets;

  /*
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPPopulation(GPConfiguration a_conf, int a_size)
      throws InvalidConfigurationException {
    super(a_conf, a_size);
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

    m_avail_argTypes = a_pop.m_avail_argTypes;
    m_avail_types = a_pop.m_avail_types;
    m_avail_nodeSets = a_pop.m_avail_nodeSets;

    m_popSize = a_pop.getPopSize();
    fitnessRank = new float[m_popSize];
    for (int i = 0; i < m_popSize; i++) {
      fitnessRank[i] = 0.5f;
    }
  }

  /**
   * Sorts the population into "ascending" order using some criterion for
   * "ascending". A Comparator is given which will compare two individuals,
   * and if one individual compares as lower than another individual, the first
   * individual will appear in the population before the second individual.
   *
   * @param c the Comparator to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void sort(Comparator c) {
    IChromosome[] chroms = super.toChromosomes();
    Arrays.sort(chroms, c);
    float f = 0;
    for (int i = 0; i < chroms.length; i++) {
      fitnessRank[i] = f;
      f += chroms[i].getFitnessValue();
    }
  }

  /**
   * Creates a population using the ramped half-and-half method. Adapted from
   * JGProg.
   *
   * @param a_conf the configuration to use
   * @param a_types the type of each chromosome, the length
   * is the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes.
   * Note that it is not necessary to include the arguments of a chromosome as
   * terminals in the chromosome's node set. This is done automatically
   * @throws InvalidConfigurationException
   */
  public void create(final GPConfiguration a_conf, Class[] a_types,
                     Class[][] a_argTypes,
                     CommandGene[][] a_nodeSets)
      throws InvalidConfigurationException {
    m_avail_types = a_types;
    m_avail_argTypes = a_argTypes;
    m_avail_nodeSets = a_nodeSets;
    for (int i = 0; i < m_popSize - 1; i++) {
      int depth = 2 + (a_conf.getMaxInitDepth() - 1) * i /
          (m_popSize - 1);
      ProgramChromosome chrom = create(a_conf, depth, (i%2)==0);
//          new ProgramChromosome(getConfiguration());
//      if ( (i % 2) == 0) {
//        chrom.grow(depth, a_types, a_argTypes, a_nodeSets);
//      }
//      else {
//        chrom.full(depth, a_types, a_argTypes, a_nodeSets);
//      }
      addChromosome(chrom);
    }
    setChanged(true);
  }

  public ProgramChromosome create(final GPConfiguration a_conf, int a_depth,
                                  boolean a_grow)
      throws InvalidConfigurationException {
    ProgramChromosome chrom = new ProgramChromosome(getConfiguration());
    if (a_grow) {
      chrom.grow(a_depth, m_avail_types, m_avail_argTypes, m_avail_nodeSets);
    }
    else {
      chrom.full(a_depth, m_avail_types, m_avail_argTypes, m_avail_nodeSets);
    }
    return chrom;
  }

  public int getPopSize() {
    return m_popSize;
  }
}
