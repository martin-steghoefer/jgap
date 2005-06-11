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

import java.io.*;
import java.util.*;
import org.jgap.impl.*;

/**
 * List of chromosomes held in the Genotype (or possibly later in the
 * Configuration object).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class Population
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.21 $";

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  private List m_chromosomes;

  /**
   * The fittest Chromosome of the population.
   */
  private Chromosome m_fittestChromosome;

  /**
   * Indicates whether at least one of the chromosomes has been changed
   * (deleted, added, modified).
   */
  private boolean m_changed;

  /*
   * Constructs the Population from a list of Chromosomes.
   * @param a_chromosomes the Chromosome's to be used for building the
   * Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(Chromosome[] a_chromosomes) {
    this(a_chromosomes.length);
    for (int i = 0; i < a_chromosomes.length; i++) {
      m_chromosomes.add(a_chromosomes[i]);
    }
    setChanged(true);
  }

  /*
   * Constructs an empty Population with the given initial size.
   * @param a_size the initial size of the empty Population. The initial size
   * is not fix, it is just for optimized list creation
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(int a_size) {
    m_chromosomes = new ArrayList(a_size);
  }

  /*
   * Constructs an empty Population with initial size 100.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population() {
    this(100);
  }

  /**
   * Adds a Chromosome to this Population. Does nothing when given null.
   *
   * @param a_toAdd the Chromosome to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosome(Chromosome a_toAdd) {
    if (a_toAdd != null) {
      m_chromosomes.add(a_toAdd);
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

  /**
   * Replaces all chromosomes in the population with the give list of
   * chromosomes
   * @param a_chromosomes the chromosomes to make the population up from
   *
   * @author Klaus Meffert
   */
  public void setChromosomes(List a_chromosomes) {
    m_chromosomes = a_chromosomes;
    setChanged(true);
  }

  /**
   * Sets in the given Chromosome on the given index in the list of chromosomes.
   * @param a_index the index to set the Chromosome in
   * @param a_chromosome the Chromosome to be set in
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setChromosome(int a_index, Chromosome a_chromosome) {
    m_chromosomes.set(a_index, a_chromosome);
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
   * Returns a Chromosome at given index in the Population.
   * @param a_index the index of the Chromosome to be returned
   * @return Chromosome at given index in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Chromosome getChromosome(int a_index) {
    return (Chromosome) m_chromosomes.get(a_index);
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
   * Determines the fittest Chromosome in the population (the one with the
   * highest fitness value) and memorizes it
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
    FitnessEvaluator evaluator = Genotype.getConfiguration().
        getFitnessEvaluator();
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

  /**
   *
   * @param a_changed true: mark population as changed; false: mark as not
   * changed (meaning: changes already acknowledged)
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected void setChanged(boolean a_changed) {
    m_changed = a_changed;
  }

  /**
   * Determines whether the given chromosome is contained within the population.
   * @param a_chromosome the chromosome to check
   * @return true: chromosome contained within population
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public boolean contains(Chromosome a_chromosome) {
    return m_chromosomes.contains(a_chromosome);
  }

  /**
   * Removes a chromosome in the list at the given index. Method has package
   * visibility to signal that this is a method not to be used outside the
   * JGAP kernal for normal circumstances.
   *
   * @param a_index index of chromosome to be removed in list
   * @return removed Chromosome
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Chromosome removeChromosome(int a_index) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Index must be within bounds!");
    }
    return (Chromosome) m_chromosomes.remove(a_index);
  }

  /**
   * Returns the genotype of the population, i.e. the list of genes in the
   * population.
   * @param a_resolveCompositeGenes true: split encountered CompositeGenes
   * into their single (atomic) genes
   * @return genotype of the population
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getGenome(boolean a_resolveCompositeGenes) {
    List result = new Vector();
    List chroms = getChromosomes();
    int len = chroms.size();
    for (int i = 0; i < len; i++) {
      Chromosome chrom = (Chromosome) chroms.get(i);
      Gene[] genes = chrom.getGenes();
      int len2 = genes.length;
      for (int j = 0; j < len2; j++) {
        Gene gene = genes[j];
        if (a_resolveCompositeGenes && gene instanceof CompositeGene) {
          addCompositeGene(result, (CompositeGene) gene);
        }
        else {
          addAtomicGene(result, gene);
        }
      }
    }
    return result;
  }

  /**
   * Adds all the genes of a CompositeGene to a result list.
   * Method calls itself recursively.
   *
   * @param a_result the list to add to
   * @param a_gene the gene to start with
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private void addCompositeGene(final List a_result, Gene a_gene) {
    if (a_gene instanceof CompositeGene) {
      int len = ( (CompositeGene) a_gene).size();
      for (int i = 0; i < len; i++) {
        addCompositeGene(a_result, ( (CompositeGene) a_gene).geneAt(i));
      }
    }
    else {
      addAtomicGene(a_result, a_gene);
    }
  }

  /**
   * Helper method for addCompositeGene
   * @param a_result List
   * @param a_gene Gene
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private void addAtomicGene(final List a_result, Gene a_gene) {
    a_result.add(a_gene);
  }
}
