package org.jgap;

import java.util.*;

/**
 * List of chromosomes held in the Genotype (or possibly later in the
 * Configuration object
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class Population {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.6 $";

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

  public Population(Chromosome[] chromosomes) {
    this();
    for (int i = 0; i < chromosomes.length; i++) {
      m_chromosomes.add(chromosomes[i]);
    }
    setChanged(true);
  }

  public Population(int size) {
    m_chromosomes = new ArrayList(size);
  }

  public Population() {
    m_chromosomes = new ArrayList(100);
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
   * @param p the Population to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosomes(Population toAdd) {
    if (toAdd != null) {
      m_chromosomes.addAll(toAdd.getChromosomes());
      setChanged(true);
    }
  }

  public List getChromosomes() {
    return m_chromosomes;
  }

  public Chromosome getChromosome(int index) {
    return (Chromosome) m_chromosomes.get(index);
  }

  public int size() {
    return m_chromosomes.size();
  }

  public Iterator iterator() {
    return m_chromosomes.iterator();
  }

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
    while (it.hasNext()) {
      Chromosome chrom = (Chromosome)it.next();
      double fitness = chrom.getFitnessValue();
      if (fitness > bestFitness || m_fittestChromosome == null) {
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
}
