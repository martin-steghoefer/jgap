/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.jgap.audit.*;
import org.jgap.util.*;

/**
 * Makes up the population of a generation during evolution. Represented by a
 * list of chromosomes held in the Genotype.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class Population
    implements Serializable, ICloneable, IPersistentRepresentation {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.66 $";

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  private List<IChromosome> m_chromosomes;

  /**
   * The fittest Chromosome of the population.
   */
  private IChromosome m_fittestChromosome;

  /**
   * Indicates whether at least one of the chromosomes has been changed
   * (deleted, added, modified).
   */
  private boolean m_changed;

  /**
   * Indicates that the list of Chromosomes has been sorted.
   */
  private boolean m_sorted;

  private Configuration m_config;

  public final static String CHROM_DELIMITER = "~";

  /**
   * Represents the heading delimiter that is used to separate chromosomes in
   * the persistent representation.
   */
  public final static String CHROM_DELIMITER_HEADING = "[";

  /**
   * Represents the closing delimiter that is used to separate chromosomes in
   * the persistent representation.
   */
  public final static String CHROM_DELIMITER_CLOSING = "]";

  /*
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Population(final Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, 100);
  }

  /*
   * Constructs the Population from a list of Chromosomes. Does not use cloning!
   *
   * @param a_chromosomes the Chromosome's to be used for building the
   * Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(final Configuration a_config,
                    final IChromosome[] a_chromosomes)
      throws InvalidConfigurationException {
    this(a_config, a_chromosomes.length);
    synchronized (m_chromosomes) {
      for (int i = 0; i < a_chromosomes.length; i++) {
        // In here we could test for null chromosomes, but this is skipped
        // because of performance issues (although this may seem idiotic...)
        m_chromosomes.add(a_chromosomes[i]);
      }
    }
    setChanged(true);
  }

  /*
   * Constructs the Population from a single Chromosome. Does not use cloning!

   * @param a_chromosome the Chromosome to be used for building the Population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Population(final Configuration a_config,
                    final IChromosome a_chromosome)
      throws InvalidConfigurationException {
    this(a_config, 1);
    if (a_chromosome == null) {
      throw new IllegalArgumentException("Chromosome passed must not be null!");
    }
    synchronized (m_chromosomes) {
      m_chromosomes.add(a_chromosome);
    }
    setChanged(true);
  }

  /*
   * Constructs an empty Population with the given initial size.

   * @param a_size the initial size of the empty Population. The initial size
   * is not fix, it is just for optimized list creation.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population(final Configuration a_config, final int a_size)
      throws InvalidConfigurationException {
    if (a_config == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_config = a_config;
    // Use a synchronized list (important for distributed computing!)
    m_chromosomes = new Vector(a_size);
    setChanged(true);
  }

  /*
   * Constructs an empty Population with initial array size 100.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Adds a Chromosome to this Population. Does nothing when given null.
   *
   * @param a_toAdd the Chromosome to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosome(final IChromosome a_toAdd) {
    if (a_toAdd != null) {
      synchronized (m_chromosomes) {
        m_chromosomes.add(a_toAdd);
      }
      setChanged(true);
    }
  }

  /**
   * Adds all the Chromosomes in the given Population. Does nothing on null or
   * an empty Population.
   *
   * @param a_population the Population to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addChromosomes(final Population a_population) {
    if (a_population != null) {
      synchronized (m_chromosomes) {
        m_chromosomes.addAll(a_population.getChromosomes());
      }
      // The following would do the same:
//      if (a_population.getChromosomes() != null) {
//        int size = a_population.getChromosomes().size();
//        for (int i = 0; i < size; i++) {
//          IChromosome chrom = a_population.getChromosome(i);
//          m_chromosomes.add(chrom);
//        }
//      }
      setChanged(true);
    }
  }

  /**
   * Replaces all chromosomes in the population with the give list of
   * chromosomes.
   *
   * @param a_chromosomes the chromosomes to make the population up from
   *
   * @author Klaus Meffert
   */
  public void setChromosomes(final List a_chromosomes) {
    synchronized (m_chromosomes) {
      m_chromosomes = a_chromosomes;
    }
    setChanged(true);
  }

  /**
   * Sets in the given Chromosome on the given index in the list of chromosomes.
   * If the given index is exceeding the list by one, the chromosome is
   * appended.
   *
   * @param a_index the index to set the Chromosome in
   * @param a_chromosome the Chromosome to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setChromosome(final int a_index, final IChromosome a_chromosome) {
    if (m_chromosomes.size() == a_index) {
      addChromosome(a_chromosome);
    }
    else {
      synchronized (m_chromosomes) {
        m_chromosomes.set(a_index, a_chromosome);
      }
      setChanged(true);
    }
  }

  /**
   * @return the list of Chromosome's in the Population. Don't modify the
   * retrieved list by using clear(), remove(int) etc. If you do so, you need to
   * call setChanged(true)
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public List<IChromosome> getChromosomes() {
    return m_chromosomes;
  }

  /**
   * @param a_index the index of the Chromosome to be returned
   * @return Chromosome at given index in the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IChromosome getChromosome(final int a_index) {
    return (IChromosome) m_chromosomes.get(a_index);
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
   * @return Iterator for the Chromosome list in the Population. Please be aware
   * that using remove() forces you to call setChanged(true)
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
   * @author Klaus Meffert, Dan Clark
   * @since 2.0
   */
  public IChromosome[] toChromosomes() {
    return (IChromosome[]) m_chromosomes.toArray(
        new IChromosome[m_chromosomes.size()]);
  }

  /**
   * Determines the fittest Chromosome in the Population (the one with the
   * highest fitness value) and memorizes it. This is an optimized version
   * compared to calling determineFittesChromosomes(1).
   *
   * @return the fittest Chromosome of the Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IChromosome determineFittestChromosome() {
    if (!m_changed && m_fittestChromosome != null) {
      return m_fittestChromosome;
    }
    Iterator it = m_chromosomes.iterator();
    FitnessEvaluator evaluator = getConfiguration().getFitnessEvaluator();
    double bestFitness;
    if (evaluator.isFitter(2.0d, 1.0d)) {
      bestFitness = -1.0d;
    }
    else {
      bestFitness = Double.MAX_VALUE;
    }
    double fitness;
    while (it.hasNext()) {
      IChromosome chrom = (IChromosome) it.next();
      fitness = chrom.getFitnessValue();
      if (evaluator.isFitter(fitness, bestFitness)
          || m_fittestChromosome == null) {
        m_fittestChromosome = chrom;
        bestFitness = fitness;
      }
    }
    setChanged(false);
    return m_fittestChromosome;
  }

  /**
   * Determines the fittest Chromosome in the population (the one with the
   * highest fitness value) within the given indices and memorizes it. This is
   * an optimized version compared to calling determineFittesChromosomes(1).
   *
   * @param a_startIndex index to begin the evaluation with
   * @param a_endIndex index to end the evaluation with
   * @return the fittest Chromosome of the population within the given indices
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IChromosome determineFittestChromosome(int a_startIndex,
      int a_endIndex) {
    double bestFitness = -1.0d;
    FitnessEvaluator evaluator = getConfiguration().getFitnessEvaluator();
    double fitness;
    int startIndex = Math.max(0, a_startIndex);
    int endIndex = Math.min(m_chromosomes.size()-1, a_endIndex);
    m_fittestChromosome = null;
    for (int i = startIndex; i <= endIndex; i++) {
      IChromosome chrom = (IChromosome) m_chromosomes.get(i);
      fitness = chrom.getFitnessValue();
      if (evaluator.isFitter(fitness, bestFitness)
          || m_fittestChromosome == null) {
        m_fittestChromosome = chrom;
        bestFitness = fitness;
      }
    }
    return m_fittestChromosome;
  }

  /**
   * Mark that for the population the fittest chromosome may have changed.
   *
   * @param a_changed true: population's fittest chromosome may have changed,
   * false: fittest chromosome evaluated earlier is still valid
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected void setChanged(final boolean a_changed) {
    m_changed = a_changed;
    setSorted(false);
  }

  /**
   * @return true: population's chromosomes (maybe) were changed,
   * false: not changed for sure
   *
   * @since 2.6
   */
  public boolean isChanged() {
    return m_changed;
  }

  /**
   * Mark the population as sorted.
   *
   * @param a_sorted true: mark population as sorted
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  protected void setSorted(final boolean a_sorted) {
    m_sorted = a_sorted;
  }

  /**
   * Determines whether the given chromosome is contained within the population.
   * @param a_chromosome the chromosome to check
   * @return true: chromosome contained within population
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public boolean contains(final IChromosome a_chromosome) {
    return m_chromosomes.contains(a_chromosome);
  }

  /**
   * Removes a chromosome in the list at the given index. Method has package
   * visibility to signal that this is a method not to be used outside the
   * JGAP kernel under normal circumstances.
   *
   * @param a_index index of chromosome to be removed in list
   * @return removed Chromosome
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  IChromosome removeChromosome(final int a_index) {
    if (a_index < 0 || a_index >= size()) {
      throw new IllegalArgumentException("Index must be within bounds!");
    }
    setChanged(true);
    return (IChromosome) m_chromosomes.remove(a_index);
  }

  /**
   * Cares that the population size does not exceed the maximum size given in
   * the configuration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void keepPopSizeConstant() throws InvalidConfigurationException {
    int popSize = size();
    // See request  1213752.
    // ---------------------
    int maxSize = getConfiguration().getPopulationSize();
//    INaturalSelector selector = getConfiguration().getKeepPopConstantSelector();
//    if (popSize > maxSize) {
//      Population newPop = new Population(getConfiguration(), maxSize);
//      selector.select(maxSize, this, newPop);
//      m_chromosomes = newPop.getChromosomes();
//      setChanged(true);
//    }
    IEvolutionMonitor monitor = getConfiguration().getMonitor();
    boolean monitorActive = monitor != null;
    while (popSize > maxSize) {
      if (monitorActive) {
        // Fire monitor with population and index of chromosome to be removed.
        // -------------------------------------------------------------------
        monitor.event(IEvolutionMonitor.MONITOR_EVENT_REMOVE_CHROMOSOME,
            getConfiguration().getGenerationNr(),
            new Object[] {this, new Integer(0)});
      }
      // Remove a chromosome.
      // --------------------
      /**@todo use dedicated selector for that*/
      removeChromosome(0);
      popSize--;
    }
    }
  /**
   * Sorts the Chromosome list and returns the fittest n Chromosomes in
   * the population.
   *
   * @param a_numberOfChromosomes number of top performer chromosomes to be
   * returned
   * @return list of the fittest n Chromosomes of the population, or the fittest
   * x Chromosomes with x = number of chromosomes in case n > x.
   *
   * @author Charles Kevin Hill
   * @since 2.4
   */
  public List determineFittestChromosomes(final int a_numberOfChromosomes) {
    int numberOfChromosomes = Math.min(a_numberOfChromosomes,
                                       getChromosomes().size());
    if (numberOfChromosomes <= 0) {
      return null;
    }
    if (!m_changed && m_sorted) {
      return getChromosomes().subList(0, numberOfChromosomes);
    }
    // Sort the list of chromosomes using the fitness comparator
    sortByFitness();
    // Return the top n chromosomes
    return getChromosomes().subList(0, numberOfChromosomes);
  }

  /**
   * Sorts the chromosomes within the population according to their fitness
   * value using ChromosomFitnessComparator. The fittest chromosome is then
   * at index 0.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void sortByFitness() {
    // The following construction could be cached but wrt that the
    // evaluator registered with the configuration could change
    // --> Don't cache it!
    sort(new ChromosomeFitnessComparator(getConfiguration().
        getFitnessEvaluator()));
    setChanged(false);
    setSorted(true);
    m_fittestChromosome = (IChromosome) m_chromosomes.get(0);
  }

  /**
   * Sorts the chromosomes within the population utilzing the given comparator.
   *
   * @param a_comparator the comparator to utilize for sorting
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  protected void sort(Comparator a_comparator) {
    Collections.sort(getChromosomes(), a_comparator);
  }

  /**
   * Returns the genotype of the population, i.e. the list of genes in the
   * Population.
   *
   * @param a_resolveCompositeGenes true: split encountered CompositeGenes
   * into their single (atomic) genes
   * @return genotype of the population
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getGenome(final boolean a_resolveCompositeGenes) {
    List result = new Vector();
    List chroms = getChromosomes();
    int len = chroms.size();
    for (int i = 0; i < len; i++) {
      IChromosome chrom = (IChromosome) chroms.get(i);
      Gene[] genes = chrom.getGenes();
      int len2 = genes.length;
      for (int j = 0; j < len2; j++) {
        Gene gene = genes[j];
        if (a_resolveCompositeGenes && gene instanceof ICompositeGene) {
          addCompositeGene(result, (ICompositeGene) gene);
        }
        else {
          addAtomicGene(result, gene);
        }
      }
    }
    return result;
  }

  /**
   * Adds all the genes of a CompositeGene to a result list.<p>
   * Note: Method calls itself recursively.
   *
   * @param a_result the list to add to
   * @param a_gene the gene to start with
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private void addCompositeGene(final List a_result, final Gene a_gene) {
    if (a_gene instanceof ICompositeGene) {
      int len = a_gene.size();
      for (int i = 0; i < len; i++) {
        addCompositeGene(a_result, ( (ICompositeGene) a_gene).geneAt(i));
      }
    }
    else {
      addAtomicGene(a_result, a_gene);
    }
  }

  /**
   * Helper method for addCompositeGene.
   *
   * @param a_result List
   * @param a_gene Gene
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private void addAtomicGene(final List a_result, final Gene a_gene) {
    a_result.add(a_gene);
  }

  public boolean isSorted() {
    return m_sorted;
  }

  /**
   * The equals-method.
   *
   * @param a_pop the population instance to compare with
   * @return true: given object equal to comparing one
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean equals(Object a_pop) {
    try {
      return compareTo(a_pop) == 0;
    } catch (ClassCastException e) {
      // If the other object isn't an Population instance
      // then we're not equal.
      // ------------------------------------------------
      return false;
    }
  }

  /**
   * This method is not producing symmetric results as -1 is more often returned
   * than 1 (see description of return value).
   *
   * @param a_pop the other population to compare
   * @return 1: a_pop is null or having fewer chromosomes or equal number
   * of chromosomes but at least one not contained. 0: both populations
   * containing exactly the same chromosomes. -1: this population contains fewer
   * chromosomes than a_pop
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(Object a_pop) {
    Population other = (Population) a_pop;
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
    List chroms2 = other.getChromosomes();
    for (int i = 0; i < size1; i++) {
      if (!chroms2.contains(m_chromosomes.get(i))) {
        return 1;
      }
    }
    return 0;
  }

  /**
   * @return deeply cloned population instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
      Population result = new Population(m_config);
      // Precautiously set changed to true in case cloning is not 1:1
      // ------------------------------------------------------------
      result.m_changed = true;
      result.m_sorted = false;
      result.m_fittestChromosome = m_fittestChromosome;
      int size = m_chromosomes.size();
      for (int i = 0; i < size; i++) {
        IChromosome chrom = (IChromosome) m_chromosomes.get(i);
        result.addChromosome( (IChromosome) chrom.clone());
      }
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

  /**
   * Clears the list of chromosomes. Normally, this should not be necessary.
   * But especially in distributed computing, a fresh population has to be
   * provided sometimes.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void clear() {
    m_chromosomes.clear();
    m_changed = true;
    m_sorted = true;
    m_fittestChromosome = null;
  }

  /**
   * Returns a persistent representation of this chromosome, see interface Gene
   * for description. Similar to CompositeGene's routine. But does not include
   * all information of the chromosome (yet).
   *
   * @return string representation of this Chromosome's relevant parts of its
   * current state
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getPersistentRepresentation() {
    StringBuffer b = new StringBuffer();
    // Persist the chromosomes.
    // ------------------------
    IChromosome chrom;
    for (int i = 0; i < m_chromosomes.size(); i++) {
      chrom = (IChromosome) m_chromosomes.get(i);
      if (! (chrom instanceof IPersistentRepresentation)) {
        throw new RuntimeException("Population contains a chromosome of type "
                                   + chrom.getClass().getName()
                                   + " which does not implement"
                                   + " IPersistentRepresentation!");
      }
      b.append(CHROM_DELIMITER_HEADING);
      b.append(StringKit.encode(chrom.getClass().getName()
                                + CHROM_DELIMITER
                                + ( (IPersistentRepresentation) chrom).
                                getPersistentRepresentation()));
      b.append(CHROM_DELIMITER_CLOSING);
    }
    return b.toString();
  }

  /**
   * Counterpart of getPersistentRepresentation.
   *
   * @param a_representation the string representation retrieved from a prior
   * call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      try {
        List r = split(a_representation);
        String g;
        m_chromosomes = new Vector();
        // Obtain the chromosomes.
        // -----------------------
        Iterator iter = r.iterator();
        StringTokenizer st;
        String clas;
        String representation;
        IChromosome chrom;
        while (iter.hasNext()) {
          g = StringKit.decode( (String) iter.next());
          st = new StringTokenizer(g, CHROM_DELIMITER);
          if (st.countTokens() != 2)
            throw new UnsupportedRepresentationException("In " + g + ", " +
                "expecting two tokens, separated by " + CHROM_DELIMITER);
          clas = st.nextToken();
          representation = st.nextToken();
          chrom = createChromosome(clas, representation);
          m_chromosomes.add(chrom);
        }
        setChanged(true);
      } catch (Exception ex) {
        throw new UnsupportedRepresentationException(ex.toString());
      }
    }
  }

  /**
   * Creates a new Chromosome instance.<p>
   * Taken and adapted from CompositeGene.
   *
   * @param a_chromClassName name of the Chromosome class
   * @param a_persistentRepresentation persistent representation of the
   * Chromosome to create (could be obtained via getPersistentRepresentation)
   *
   * @return newly created Chromosome
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected IChromosome createChromosome(String a_chromClassName,
      String a_persistentRepresentation)
      throws Exception {
    Class chromClass = Class.forName(a_chromClassName);
    Constructor constr = chromClass.getConstructor(new Class[] {Configuration.class});
    IChromosome chrom = (IChromosome) constr.newInstance(new Object[] {
        getConfiguration()});
    ( (IPersistentRepresentation) chrom).setValueFromPersistentRepresentation(
        a_persistentRepresentation);
    return chrom;
  }

  /**
   * Splits the input a_string into individual chromosome representations.<p>
   * Taken and adapted from CompositeGene.
   *
   * @param a_string the string to split
   * @return the elements of the returned array are the persistent
   * representation strings of the population's components
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected static final List split(String a_string)
      throws UnsupportedRepresentationException {
    List a = Collections.synchronizedList(new ArrayList());
    // No Header data.
    // ---------------

    // Chromosome data.
    // ----------------
    StringTokenizer st = new StringTokenizer
        (a_string, CHROM_DELIMITER_HEADING + CHROM_DELIMITER_CLOSING, true);
    while (st.hasMoreTokens()) {
      if (!st.nextToken().equals(CHROM_DELIMITER_HEADING)) {
        throw new UnsupportedRepresentationException(a_string + " no open tag");
      }
      String n = st.nextToken();
      if (n.equals(CHROM_DELIMITER_CLOSING)) {
        a.add(""); /* Empty token */
      }
      else {
        a.add(n);
        if (!st.nextToken().equals(CHROM_DELIMITER_CLOSING)) {
          throw new UnsupportedRepresentationException
              (a_string + " no close tag");
        }
      }
    }
    return a;
  }

  /***
   * Hashcode function for the genotype, tries to create a unique hashcode for
   * the chromosomes within the population. The logic for the hashcode is
   *
   * Step  Result
   * ----  ------
   *    1  31*0      + hashcode_0 = y(1)
   *    2  31*y(1)   + hashcode_1 = y(2)
   *    3  31*y(2)   + hashcode_2 = y(3)
   *    n  31*y(n-1) + hashcode_n-1 = y(n)
   *
   * @return the computed hashcode
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public int hashCode() {
    int i, size = size();
    IChromosome s;
    int twopower = 1;
    // For empty population we want a special value different from other
    // hashcode implementations.
    // ------------------------------------------------------------------
    int localHashCode = -593;
    for (i = 0; i < size; i++, twopower = 2 * twopower) {
      s = getChromosome(i);
      localHashCode = 31 * localHashCode + s.hashCode();
    }
    return localHashCode;
  }

}
