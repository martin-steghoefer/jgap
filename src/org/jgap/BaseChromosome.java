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

import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * Base class for any implementation of interface IChromosome.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class BaseChromosome
    implements IChromosome, IInitializer, IPersistentRepresentation, IBusinessKey {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.15 $";

  /**
   * This field separates gene class name from the gene persistent representation
   * string. '*' does not work properly with URLEncoder!
   */
  public final static String GENE_DELIMITER = "#";

  /**
   * Represents the heading delimiter that is used to separate genes in the
   * persistent representation of Chromosome instances.
   */
  public final static String GENE_DELIMITER_HEADING = "<";

  /**
   * Represents the closing delimiter that is used to separate genes in the
   * persistent representation of Chromosome instances.
   */
  public final static String GENE_DELIMITER_CLOSING = ">";

  /**
   * Separates chromosome-related information.
   */
  public final static String CHROM_DELIMITER = "#";

  /**
   * The configuration object to use
   */
  private Configuration m_configuration;

  /**
   * The array of Genes contained in this Chromosome.
   */
  private Gene[] m_genes;

  private int m_age;

  private int m_operatedOn;

  /**
   * Unique ID of the chromosome that allows to distinct it from other
   * chromosomes. In the best case, this ID is unique worldwide.
   */
  private String m_uniqueID;

  /**
   * In case mutation, crossing over etc. happened, this sequence gives evidence
   * about the parent(s) of the current chromosome.
   */
  private Map<Integer,String> m_uniqueIDTemplates;

  /**
   * The only constructor in this class. Sets the immutable configuration.
   *
   * @param a_configuration the configuration to set
   * @throws InvalidConfigurationException if configuration is null
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BaseChromosome(Configuration a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new InvalidConfigurationException(
          "Configuration to be set must not"
          + " be null!");
    }
    m_configuration = a_configuration;
    if (m_configuration.isUniqueKeysActive()) {
      m_uniqueIDTemplates = new HashMap();
      IJGAPFactory factory = m_configuration.getJGAPFactory();
      if (JGAPFactory.class.isAssignableFrom(factory.getClass())) {
        m_uniqueID = ( (JGAPFactory) (m_configuration.getJGAPFactory())).
            getUniqueKey(getClass().getName());
      }
    }
  }
  /**
   * @return unique ID of the chromosome, which allows to distinct this instance
   * from others, in the best case worldwide
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public String getUniqueID() {
    return m_uniqueID;
  }

  /**
   * A template is a chromosome that is the logical predecessor of the current
   * chromosome. A template can occur in mutation or crossing over. In the
   * latter case can be at least two template chromosomes. This is why in this
   * setter method the parameter a_index exists.
   *
   * @param a_templateID the unique ID of the template
   * @param a_index the index of the template, e.g. in crossing over for the
   * second candidate chromosome this is 2
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void setUniqueIDTemplate(String a_templateID, int a_index) {
    m_uniqueIDTemplates.put(a_index, a_templateID);
  }

  /**
   * @param a_index the index of the template to retrieve the key for
   * @return String
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public String getUniqueIDTemplate(int a_index) {
    return m_uniqueIDTemplates.get(a_index);
  }

  /**
   * @return the configuration used
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_configuration;
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance
   * @throws IllegalStateException instead of CloneNotSupportedException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public abstract Object clone();

  /**
   * Increases the number of evolutionary rounds of chromosome in which it has
   * not been changed by one.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void increaseAge() {
    m_age++;
  }

  /**
   * Reset age of chromosome because it has been changed.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void resetAge() {
    m_age = 0;
  }

  /**
   * @return 0: Chromosome newly created in this generation. This means it
   * does not need being crossed over with another newly created one
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int getAge() {
    return m_age;
  }

  /**
   * @param a_age set the age of the chromosome, see BestChromosomesSelector
   * for a use-case
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setAge(int a_age) {
    m_age = a_age;
  }

  /**
   * Increase information of number of genetic operations performed on
   * chromosome in current evolution round.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void increaseOperatedOn() {
    m_operatedOn++;
  }

  /**
   * Resets the information of how many genetic operators have been performed
   * on the chromosome in the current round of evolution.
   *
   * @author Klaus Meffert
   * @since 3.2
   *
   */
  public void resetOperatedOn() {
    m_operatedOn = 0;
  }

  /**
   * @return number of genetic operations performed on chromosome in current
   * evolution round
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int operatedOn() {
    return m_operatedOn;
  }

  /**
   * Retrieves the set of genes that make up this Chromosome. This method
   * exists primarily for the benefit of GeneticOperators that require the
   * ability to manipulate Chromosomes at a low level.
   *
   * @return an array of the Genes contained within this Chromosome
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Gene[] getGenes() {
    return m_genes;
  }

  /**
   * Sets the genes for the chromosome.
   *
   * @param a_genes the genes to set for the chromosome
   *
   * @throws InvalidConfigurationException in case constraint checker is
   * provided
   *
   * @author Klaus Meffert
   * @since 3.2 (previously in class Chromosome)
   */
  public void setGenes(Gene[] a_genes)
      throws InvalidConfigurationException {
    m_genes = a_genes;
  }

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * @param a_desiredLocus index of the gene value to be returned
   * @return Gene at the given index
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Gene getGene(int a_desiredLocus) {
    return m_genes[a_desiredLocus];
  }

  public void setGene(int a_index, Gene a_gene) {
    m_genes[a_index] = a_gene;
  }

  /**
   * Returns the size of this Chromosome (the number of genes it contains).
   * A Chromosome's size is constant and will not change, until setGenes(...)
   * is used.
   *
   * @return number of genes contained within this Chromosome instance
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int size() {
    if (m_genes == null) {
      // only possible when using default constructor
      return 0;
    }
    else {
      return m_genes.length;
    }
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
    // Persist the chromosome's fitness value.
    // ---------------------------------------
    b.append(getFitnessValueDirectly());
    b.append(CHROM_DELIMITER);
    // Persist the genes.
    // ------------------
    b.append(size());
    b.append(CHROM_DELIMITER);
    getGenesPersistentRepresentation(b);
    return b.toString();
  }

  /**
   * @return the persistent representation of the chromosome by considering
   * its genes.
   *
   * @author Klaus Meffert
   */
  public StringBuffer getGenesPersistentRepresentation() {
    StringBuffer b = new StringBuffer();
    getGenesPersistentRepresentation(b);
    return b;
  }

  /**
   * @return business key of the chromosome
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getBusinessKey() {
    return getGenesPersistentRepresentation().toString();
  }

  /**
   * Retrieves the persistent representation of the chromosome by considering
   * its genes.
   *
   * @param a_buffer the variable to store the persistent representation in
   *
   * @author Klaus Meffert
   */
  public void getGenesPersistentRepresentation(StringBuffer a_buffer) {
    Gene gene;
    int size = size();
    for (int i = 0; i < size; i++) {
      gene = getGene(i);
      a_buffer.append(GENE_DELIMITER_HEADING);
          a_buffer.append(encode(gene.getClass().getName()
              + GENE_DELIMITER
              + gene.getPersistentRepresentation()));
      a_buffer.append(GENE_DELIMITER_CLOSING);
    }
  }

  protected String encode(String a_string) {
    return StringKit.encode(a_string);
  }

  protected String decode(String a_string) throws UnsupportedEncodingException {
    return StringKit.decode(a_string);
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
        // Obtain fitness value.
        // ---------------------
        g = decode( (String) r.get(0));
        setFitnessValue(Double.parseDouble(g));
        r.remove(0);/**@todo we can do this faster!*/
        // Obtain number of genes.
        // -----------------------
        g = decode( (String) r.get(0));
        int count = Integer.parseInt(g);
        setGenes(new Gene[count]);
        r.remove(0);/**@todo we can do this faster!*/
        // Obtain the genes.
        // -----------------
        Iterator iter = r.iterator();
        StringTokenizer st;
        String clas;
        String representation;
        Gene gene;
        int index = 0;
        while (iter.hasNext()) {
          g = decode( (String) iter.next());
          st = new StringTokenizer(g, GENE_DELIMITER);
          if (st.countTokens() != 2)
            throw new UnsupportedRepresentationException("In " + g + ", " +
                "expecting two tokens, separated by " + GENE_DELIMITER);
          clas = st.nextToken();
          representation = st.nextToken();
          gene = createGene(clas, representation);
          setGene(index++, gene);
        }
      }
      catch (Exception ex) {
        throw new UnsupportedRepresentationException(ex.toString());
      }
    }
  }

  /**
   * Creates a new Gene instance.<p>
   * Taken from CompositeGene.
   *
   * @param a_geneClassName name of the gene class
   * @param a_persistentRepresentation persistent representation of the gene to
   * create (could be obtained via getPersistentRepresentation)
   *
   * @return newly created gene
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected Gene createGene(String a_geneClassName,
                            String a_persistentRepresentation)
      throws Exception {
    Class geneClass = Class.forName(a_geneClassName);
    Constructor constr = geneClass.getConstructor(new Class[] {Configuration.class});
    Gene gene = (Gene) constr.newInstance(new Object[] {getConfiguration()});
    gene.setValueFromPersistentRepresentation(a_persistentRepresentation);
    return gene;
  }

  /**
   * Splits the input a_string into individual gene representations.<p>
   * Taken and adapted from CompositeGene.
   *
   * @param a_string the string to split
   * @return the elements of the returned array are the persistent
   * representation strings of the chromosome's components
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected static final List split(String a_string)
      throws UnsupportedRepresentationException {
    List a = Collections.synchronizedList(new ArrayList());
    // Header data.
    // ------------
    int index = 0;
    StringTokenizer st0 = new StringTokenizer
        (a_string, CHROM_DELIMITER, false);
    if (!st0.hasMoreTokens()) {
      throw new UnsupportedRepresentationException("Fitness value expected!");
    }
    String fitnessS = st0.nextToken();
    a.add(fitnessS);
    index += fitnessS.length();
    if (!st0.hasMoreTokens()) {
      throw new UnsupportedRepresentationException("Number of genes expected!");
    }
    String numGenes = st0.nextToken();
    a.add(numGenes);
    index += numGenes.length();

    index += 2; //2 one-character delimiters

    if (!st0.hasMoreTokens()) {
      throw new UnsupportedRepresentationException("Gene data missing!");
    }

    // Remove previously parsed content.
    // ---------------------------------
    a_string = a_string.substring(index);

    // Gene data.
    // ----------
    StringTokenizer st = new StringTokenizer
        (a_string, GENE_DELIMITER_HEADING + GENE_DELIMITER_CLOSING, true);
    while (st.hasMoreTokens()) {
      if (!st.nextToken().equals(GENE_DELIMITER_HEADING)) {
        throw new UnsupportedRepresentationException(a_string + " no open tag");
      }
      String n = st.nextToken();
      if (n.equals(GENE_DELIMITER_CLOSING)) {
        a.add(""); /* Empty token */
      }
      else {
        a.add(n);
        if (!st.nextToken().equals(GENE_DELIMITER_CLOSING)) {
          throw new UnsupportedRepresentationException
              (a_string + " no close tag");
        }
      }
    }
    return a;
  }

}
