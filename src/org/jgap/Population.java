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
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  private List m_chromosomes;

  public Population(Chromosome[] chromosomes) {
    this();
    for (int i = 0; i < chromosomes.length; i++) {
      m_chromosomes.add(chromosomes[i]);
    }
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
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosome(Chromosome toAdd) {
    if (toAdd != null) {
      m_chromosomes.add(toAdd);
    }
  }

  /**
   * Adds all the <code>Cell</code>s in the given <code>Population</code>.
   * Does nothing on <code>null</code> or an empty <code>Population</code>.
   *
   * @param p	the <code>Population</code> to add
   */
  public void addChromosomes(Population toAdd) {
    if (toAdd != null) {
      m_chromosomes.addAll(toAdd.getChromosomes());
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
}
