/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import java.lang.reflect.*;
import org.jgap.*;

/**
 * Ordered container for multiple genes
 * Has the same interface as a single gene and could be used accordingly.
 * Use the addGene(Gene) method to add single genes (possibly CompositeGenes)
 * after construction, an empty CompositeGene without genes makes no sense.
 * Beware that there are two equalities defined for a CompositeGene in respect
 * to its contained genes:
 * a) Two genes are (only) equal if they are identical
 * b) Two genes are (seen as) equal if their equals method returns true
 *
 * This influences several methods such as addGene. Notice that it is safer
 * to use addGene(a_gene, false) than addGene(a_gene, true) because the second
 * variant only allows to add genes not seen as equal to already added genes in
 * respect to their equals function. But: the equals function returns true for
 * two different DoubleGenes (e.g.) just after their creation. If no specific
 * (and hopefully different) allele is set for these DoubleGenes they are seen
 * as equal!
 *
 * @author Klaus Meffert
 * @author Audrius Meskauskas
 * @since 1.1
 */
public class CompositeGene
    extends BaseGene
    implements ICompositeGene, IPersistentRepresentation  {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.60 $";

  /**
   * This field separates gene class name from the gene persistent representation
   * string. '*' does not work properly with URLEncoder!
   */
  public final static String GENE_DELIMITER = "#";

  /**
   * Represents the heading delimiter that is used to separate genes in the
   * persistent representation of CompositeGene instances.
   */
  public final static String GENE_DELIMITER_HEADING = "<";

  /**
   * Represents the closing delimiter that is used to separate genes in the
   * persistent representation of CompositeGene instances.
   */
  public final static String GENE_DELIMITER_CLOSING = ">";

  private Gene m_geneTypeAllowed;

  /**
   * The genes contained in this CompositeGene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private List<Gene> m_genes;

  /**
   * Default constructor.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public CompositeGene()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CompositeGene(Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, null);
  }

  /**
   * Allows to specify which Gene implementation is allowed to be added to the
   * CompositeGene.
   *
   * @param a_config the configuration to use
   * @param a_geneTypeAllowed the class of Genes to be allowed to be added to
   * the CompositeGene
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public CompositeGene(final Configuration a_config,
                       final Gene a_geneTypeAllowed)
      throws InvalidConfigurationException {
    super(a_config);
    m_genes = new Vector();
    if (a_geneTypeAllowed != null) {
      m_geneTypeAllowed = a_geneTypeAllowed;
    }
  }

  /**
   * Adds a gene to the CompositeGene.
   *
   * @param a_gene the gene to add
   */
  public void addGene(final Gene a_gene) {
    addGene(a_gene, false);
  }

  /**
   * @return the gene type allowed, or null if any type allowed
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Gene getGeneTypeAllowed() {
    return m_geneTypeAllowed;
  }

  /**
   * Adds a gene to the CompositeGene's container. See comments in class
   * header for additional details about equality (concerning "strict" param.)
   *
   * @param a_gene the gene to be added
   * @param a_strict false: add the given gene except the gene itself already is
   * contained within the CompositeGene's container. true: add the gene if
   * there is no other gene being equal to the given gene in request to the
   * Gene's equals method
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addGene(final Gene a_gene, final boolean a_strict) {
    if (a_gene == null) {
      throw new IllegalArgumentException("Gene instance must not be null!");
    }
    if (m_geneTypeAllowed != null) {
      if (!a_gene.getClass().getName().equals(m_geneTypeAllowed.getClass().
                                              getName())) {
        throw new IllegalArgumentException("Adding a "
                                           + a_gene.getClass().getName()
                                           + " has been forbidden!");
      }
    }
    // Check if gene already exists.
    // -----------------------------
    boolean containsGene;
    if (!a_strict) {
      containsGene = containsGeneByIdentity(a_gene);
    }
    else {
      containsGene = m_genes.contains(a_gene);
    }
    if (containsGene) {
      throw new IllegalArgumentException("The gene is already contained"
                                         + " in the CompositeGene!");
    }
    m_genes.add(a_gene);
  }

  /**
   * Removes the given gene from the collection of genes. The gene is only
   * removed if an object of the same identity is contained. The equals
   * method is not used here intentionally.
   *
   * @param a_gene the gene to be removed
   * @return true: given gene found and removed
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean removeGeneByIdentity(final Gene a_gene) {
    int size = size();
    if (size < 1) {
      return false;
    }
    else {
      for (int i = 0; i < size; i++) {
        if (geneAt(i) == a_gene) {
          m_genes.remove(i);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Removes the given gene from the collection of genes. The gene is
   * removed if another gene exists that is equal to the given gene in respect
   * to the equals method of the gene.
   *
   * @param a_gene the gene to be removed
   * @return true: given gene found and removed
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean removeGene(final Gene a_gene) {
    return m_genes.remove(a_gene);
  }

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void cleanup() {
    Gene gene;
    int size = m_genes.size();
    for (int i = 0; i < size; i++) {
      gene = (Gene) m_genes.get(i);
      gene.cleanup();
    }
  }

  /**
   * See interface Gene for description.
   *
   * @param a_numberGenerator the random number generator that should be used
   * to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setToRandomValue(final RandomGenerator a_numberGenerator) {
    if (a_numberGenerator == null) {
      throw new IllegalArgumentException("Random generatoe must not be null!");
    }
    Gene gene;
    int size = m_genes.size();
    for (int i = 0; i < size; i++) {
      gene = (Gene) m_genes.get(i);
      gene.setToRandomValue(a_numberGenerator);
    }
  }

  /**
   * See interface Gene for description.
   *
   * @param a_representation the string representation retrieved from a prior
   * call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @author Audrius Meskauskas
   * @since 1.1
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      try {
        // Remove the old content.
        // -----------------------
        m_genes.clear();
        List r = split(a_representation);
        Iterator iter = r.iterator();
        StringTokenizer st;
        String clas;
        String representation;
        String g;
        Gene gene;
        while (iter.hasNext()) {
          g = decode( (String) iter.next());
          st = new StringTokenizer(g, GENE_DELIMITER);
          if (st.countTokens() != 2)
            throw new UnsupportedRepresentationException("In " + g + ", " +
                "expecting two tokens, separated by " + GENE_DELIMITER);
          clas = st.nextToken();
          representation = st.nextToken();
          gene = createGene(clas, representation);
          addGene(gene);
        }
      }
      catch (Exception ex) {
        throw new UnsupportedRepresentationException(ex.toString());
      }
    }
  }

  /**
   * Creates a new instance of gene.
   *
   * @param a_geneClassName name of the gene class
   * @param a_persistentRepresentation persistent representation of the gene to
   * create (could be obtained via getPersistentRepresentation)
   *
   * @return newly created gene
   * @throws Exception
   *
   * @author Klaus Meffert
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
   * See interface Gene for description.
   *
   * @return string representation of this Gene's current state
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @author Audrius Meskauskas
   * @since 1.1
   */
  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    StringBuffer b = new StringBuffer();
    Iterator iter = m_genes.iterator();
    Gene gene;
    while (iter.hasNext()) {
      gene = (Gene) iter.next();
      b.append(GENE_DELIMITER_HEADING);
        b.append(encode(
            gene.getClass().getName() +
            GENE_DELIMITER +
            gene.getPersistentRepresentation()));
      b.append(GENE_DELIMITER_CLOSING);
    }
    return b.toString();
  }

  /**
   * Retrieves the value represented by this Gene. All values returned
   * by this class will be Vector instances. Each element of the Vector
   * represents the allele of the corresponding gene in the CompositeGene's
   * container
   *
   * @return the value of this Gene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Object getAllele() {
    List alleles = new Vector();
    Gene gene;
    int size = m_genes.size();
    for (int i = 0; i < size; i++) {
      gene = (Gene) m_genes.get(i);
      alleles.add(gene.getAllele());
    }
    return alleles;
  }

  /**
   * Sets the value of the contained Genes to the new given value. This class
   * expects the value to be of a Vector type. Each element of the Vector
   * must conform with the type of the gene in the CompositeGene's container
   * at the corresponding position.
   *
   * @param a_newValue the new value of this Gene instance
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(Object a_newValue) {
    if (! (a_newValue instanceof List)) {
      throw new IllegalArgumentException(
          "The expected type of the allele"
          + " is a List descendent.");
    }
    if (getConstraintChecker() != null) {
      if (!getConstraintChecker().verify(this, a_newValue, null, -1)) {
        return;
      }
    }
    List alleles = (List) a_newValue;
    Gene gene;
    for (int i = 0; i < alleles.size(); i++) {
      gene = (Gene) m_genes.get(i);
      gene.setAllele(alleles.get(i));
    }
  }

  /**
   * Provides an implementation-independent means for creating new Gene
   * instances.
   *
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected Gene newGeneInternal() {
    try {
      CompositeGene compositeGene = new CompositeGene(getConfiguration());
      compositeGene.setConstraintChecker(getConstraintChecker());
      Gene gene;
      int geneSize = m_genes.size();
      for (int i = 0; i < geneSize; i++) {
        gene = (Gene) m_genes.get(i);
        compositeGene.addGene(gene.newGene(), false);
      }
      return compositeGene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Compares this CompositeGene with the specified object for order. A
   * false value is considered to be less than a true value. A null value
   * is considered to be less than any non-null value.
   *
   * @param a_other the CompositeGene to be compared
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object
   *
   * @throws ClassCastException if the specified object's type prevents it
   * from being compared to this CompositeGene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(Object a_other) {
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the contained genes' compareTo
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (a_other == null) {
      return 1;
    }
    if (! (a_other instanceof CompositeGene)) {
      return this.getClass().getName().compareTo(a_other.getClass().getName());
    }
    CompositeGene otherCompositeGene = (CompositeGene) a_other;
    if (otherCompositeGene.isEmpty()) {
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      if (isEmpty()) {
        return 0;
      }
      else {
        return 1;
      }
    }
    else {
      // Compare each gene against each other.
      // -------------------------------------
      int numberGenes = Math.min(size(), otherCompositeGene.size());
      Gene gene1;
      Gene gene2;
      for (int i = 0; i < numberGenes; i++) {
        gene1 = geneAt(i);
        gene2 = otherCompositeGene.geneAt(i);
        int result = gene1.compareTo(gene2);
        if (result != 0) {
          return result;
        }
      }
      // If everything is equal until now the CompositeGene with more
      // contained genes wins.
      // ------------------------------------------------------------
      if (size() == otherCompositeGene.size()) {
        if (isCompareApplicationData()) {
          return compareApplicationData(getApplicationData(),
                                        otherCompositeGene.getApplicationData());
        }
        else {
          return 0;
        }
      }
      else {
        if (size() > otherCompositeGene.size()) {
          return 1;
        }
        else {
          return -1;
        }
      }
    }
  }

  /**
   * Retrieves a string representation of this CompositeGene's value that
   * may be useful for display purposes.
   * @return string representation of this CompositeGene's value. Every
   * contained gene's string representation is delimited by the given
   * delimiter
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @author Audrius Meskauskas
   * @since 1.1
   */
  public String toString() {
    if (m_genes.isEmpty()) {
      return "CompositeGene=null";
    }
    else {
      String result = "CompositeGene=(";
      Gene gene;
      for (int i = 0; i < m_genes.size(); i++) {
        gene = (Gene) m_genes.get(i);
        result += gene;
        if (i < m_genes.size() - 1) {
          result += GENE_DELIMITER;
        }
      }
      return result + ")";
    }
  }

  /**
   * @return true: no genes contained, false otherwise
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean isEmpty() {
    return m_genes.isEmpty() ? true : false;
  }

  /**
   * @param a_index index to return the gene at
   * @return the gene at the given index
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Gene geneAt(int a_index) {
    return (Gene) m_genes.get(a_index);
  }

  /**
   *
   * @return list of genes contained in the CompositeGene
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public List<Gene> getGenes() {
    return m_genes;
  }

  /**
   * @return the number of genes contained
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int size() {
    return m_genes.size();
  }

  /**
   * Checks whether a specific gene is already contained. The determination
   * will be done by checking for identity and not using the equal method!
   *
   * @param gene the gene under test
   * @return true: the given gene object is contained
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean containsGeneByIdentity(Gene gene) {
    boolean result;
    int size = size();
    if (size < 1) {
      result = false;
    }
    else {
      result = false;
      for (int i = 0; i < size; i++) {
        //check for identity
        //------------------
        if (geneAt(i) == gene) {
          result = true;
          break;
        }
      }
    }
    return result;
  }

  /**
   * Don't use this method, is makes no sense here. It is just there to
   * satisfy the Gene interface. Instead, loop over all contained genes and
   * call their applyMutation method.
   * @param a_index does not matter here
   * @param a_percentage does not matter here
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int a_index, double a_percentage) {
    // problem here: size() of CompositeGene not equal to (different)
    // sizes of contained genes.
    // Solution: Don't use CompositeGene.applyMutation, instead loop
    //           over all contained genes and call their method
    // -------------------------------------------------------------
    throw new RuntimeException("applyMutation may not be called for "
                               + "a CompositeGene. Call this method for each"
                               + " gene contained in the CompositeGene.");
  }

  /**
   * Splits a_string into individual gene representations.
   *
   * @param a_string the string to split
   * @return the elements of the returned array are the persistent
   * representation strings of the gene's components
   * @throws UnsupportedRepresentationException
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  protected static final List split(String a_string)
      throws UnsupportedRepresentationException {
    List a = Collections.synchronizedList(new ArrayList());
    StringTokenizer st = new StringTokenizer
        (a_string, GENE_DELIMITER_HEADING + GENE_DELIMITER_CLOSING, true);
    while (st.hasMoreTokens()) {
      if (!st.nextToken().equals(GENE_DELIMITER_HEADING)) {
        throw new UnsupportedRepresentationException(a_string + " no opening tag");
      }
      String n = st.nextToken();
      if (n.equals(GENE_DELIMITER_CLOSING)) {
        // Empty token.
        a.add("");
      }
      else {
        a.add(n);
        if (!st.nextToken().equals(GENE_DELIMITER_CLOSING)) {
          throw new UnsupportedRepresentationException
              (a_string + " no closing tag");
        }
      }
    }
    return a;
  }

  /**
   * Retrieves the hash code value for this Gene.
   *
   * @return this Gene's hash code
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    int hashCode = 1;
    int geneHashcode;
    for (int i = 0; i < size(); i++) {
      geneHashcode = geneAt(i).hashCode();
      hashCode = 31 * hashCode + geneHashcode;
    }
    return hashCode;
  }

  /**
   * This method is not called internally because BaseGene.getAllele() is
   * overridden here!
   * @return always null
   */
  protected Object getInternalValue() {
    return null;
  }

  @Override
  public String getBusinessKey() {
    Iterator iter = m_genes.iterator();
    Gene gene;
    StringBuffer b = new StringBuffer();
    while (iter.hasNext()) {
      gene = (Gene) iter.next();
      b.append(GENE_DELIMITER_HEADING);
      if (IBusinessKey.class.isAssignableFrom(gene.getClass())) {
        b.append(((IBusinessKey)gene).getBusinessKey());
      }
      else {
        // Fall back to suboptimal solution.
        // ---------------------------------
        b.append(gene.getPersistentRepresentation());
      }
      b.append(GENE_DELIMITER_CLOSING);
    }
    return b.toString();
  }
}
