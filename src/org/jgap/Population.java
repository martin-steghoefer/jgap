/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;

/**
 * List of chromosomes held in the Genotype (or possibly later in the
 * Configuration object).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class Population {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.16 $";

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  private List m_chromosomes;

  /**
   * The fittest Chromosome of the population
   */
  private Chromosome m_fittestChromosome;

  /**
   * Indicated whether at least one of the chromosomes has been changed (deleted,
   * added, modified)
   */
  private boolean m_changed;

  /*
   * Constructs the Population from a list of Chromosomes
   * @param chromosomes the Chromosome's to be used for building the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(Chromosome[] chromosomes) {
    this(chromosomes.length);
    for (int i = 0; i < chromosomes.length; i++) {
      m_chromosomes.add(chromosomes[i]);
    }
    setChanged(true);
  }

  /*
   * Constructs an empty Population with the given initial size
   * @param size the initial size of the empty Population. The initial size
   * is not fix, it is just for optimized list creation.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(int size) {
    m_chromosomes = new ArrayList(size);
  }

  /*
   * Constructs an empty Population with initial size 100
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population() {
    this(100);
  }

  /**
   * Adds a Chromosome to this Population. Does nothing
   * when given null.
   *
   * @param toAdd the Chromosome to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosome(Chromosome toAdd) {
    if (toAdd != null) {
      m_chromosomes.add(toAdd);
      setChanged(true);
    }
  }

  /**
   * Adds all the Chromosomes in the given Population.
   * Does nothing on null or an empty Population.
   *
   * @param a_population the Population to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosomes(Population a_population) {
    if (a_population != null) {
      m_chromosomes.addAll(a_population.getChromosomes());
      setChanged(true);
    }
  }

  public void setChromosomes(List a_chromosomes) {
    m_chromosomes = a_chromosomes;
    setChanged(true);
  }

  /**
   * Sets in the given Chromosome on the given index in the list of chromosomes
   * @param index the index to set the Chromosome in
   * @param a_chromosome the Chromosome to be set in
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setChromosome(int index, Chromosome a_chromosome) {
    m_chromosomes.set(index, a_chromosome);
    setChanged(true);
  }

  /**
   * @return the list of Chromosome's in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public List getChromosomes() {
    return m_chromosomes;
  }

  /**
   * Returns a Chromosome at given index in the Population
   * @param index the index of the Chromosome to be returned
   * @return Chromosome at given index in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Chromosome getChromosome(int index) {
    return (Chromosome) m_chromosomes.get(index);
  }

  /**
   * @return number of Chromosome's in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int size() {
    return m_chromosomes.size();
  }

  /**
   * @return Iterator for the Chromosome list in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Iterator iterator() {
    return m_chromosomes.iterator();
  }

  /**
   * @return the Population converted into a list of Chromosome's
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Chromosome[] toChromosomes() {
    Chromosome[] result = new Chromosome[m_chromosomes.size()];
    for (int i = 0; i < m_chromosomes.size(); i++) {
      result[i] = (Chromosome) m_chromosomes.get(i);
    }
    return result;
  }

  /**
   * Determines the fittest Chromosome in the population (the one with the highest
   * fitness value) and memorizes it
   * @return the fittest Chromosome of the population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Chromosome determineFittestChromosome() {
    if (!m_changed) {
      return m_fittestChromosome;
    }
    Iterator it = m_chromosomes.iterator();
    double bestFitness = -1.0d;
    FitnessEvaluator evaluator = Genotype.getConfiguration().getFitnessEvaluator();
    double fitness;
    while (it.hasNext()) {
      Chromosome chrom = (Chromosome) it.next();
      fitness = chrom.getFitnessValue();
      if (evaluator.isFitter(fitness, bestFitness) || m_fittestChromosome == null) {
        m_fittestChromosome = chrom;
        bestFitness = fitness;
      }
    }
    setChanged(false);
    return m_fittestChromosome;
  }

  private void setChanged(boolean a_changed) {
    m_changed = a_changed;
  }

  /**
   * Determines whether the given chromosome is contained within the population.
   * @param a_chromosome the chromosome to check
   * @return boolean true: chromosome contained within population
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public boolean contains(Chromosome a_chromosome) {
    return m_chromosomes.contains(a_chromosome);
  }
}
