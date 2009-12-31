/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import java.lang.reflect.*;
import java.util.*;

import org.jgap.*;

/**
 * Combined implementation of both Supergene and SupergeneValidator.
 * A working supergene can be easily created from this class just by
 * adding genes and overriding
 * {@link org.jgap.supergenes.AbstractSupergene#isValid(Gene [] a_case,
 *  Supergene a_forSupergene) isValid (Gene [], Supergene)}
 *  method. For more complex cases, you may need to set your own
 * {@link org.jgap.supergenes.Validator Validator}.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public abstract class AbstractSupergene
    extends BaseGene
    implements Supergene, SupergeneValidator, IPersistentRepresentation  {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.24 $";

  /**
   * This field separates gene class name from
   * the gene persistent representation string.
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

  /**
   * Maximal number of retries for applyMutation and setToRandomValue.
   * If the valid supergen cannot be created after this number of iterations,
   * the error message is printed and the unchanged instance is returned.
   * */
  public final static int MAX_RETRIES = 1;

  /**
   * Maximal number of notes about immutable genes per
   * single gene position
   * */
  public final static int MAX_IMMUTABLE_GENES = 100000;

  /** Holds the genes of this supergene. */
  private Gene[] m_genes;

  /** Set of supergene allele values that cannot mutate. */
  private static Set[] m_immutable = new Set[1];

  /**
   * @return the array of genes - components of this supergene. The supergene
   * components may be supergenes itself
   */
  public Gene[] getGenes() {
    return m_genes;
  }

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * This seems to be one of the bottlenecks, so it is declared final.
   * I cannot imagine the reason for overriding this trivial single line
   * method.
   *
   * @param a_index the index of the gene value to be returned
   * @return the Gene at the given index
   */
  public final Gene geneAt(final int a_index) {
    return m_genes[a_index];
  };

  /**
   * Default constructor for dynamic instantiation.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public AbstractSupergene()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration(), new Gene[]{});
  }

  /**
   * Constructor for dynamic instantiation.
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public AbstractSupergene(final Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, new Gene[]{});
  }

  /**
   * Constructs abstract supergene with the given gene list.
   *
   * @param a_conf the configuration to use
   * @param a_genes array of genes for this Supergene
   * @throws InvalidConfigurationException
   */
  public AbstractSupergene(final Configuration a_conf, final Gene[] a_genes)
      throws InvalidConfigurationException {
    super(a_conf);
    if (a_genes == null) {
      throw new RuntimeException("null value for genes not allowed!");
    }
    m_genes = a_genes;
  }

  /**
   * Test the allele combination of this supergene for validity. This method
   * calls isValid for the current gene list.
   * @return true only if the supergene allele combination is valid
   * or the setValidator (<i>null</i>) has been previously called
   */
  public boolean isValid() {
    if (m_validator == null) {
      return true;
    }
    else {
      return m_validator.isValid(m_genes, this);
    }
  }

  /**
   * Test the given gene list for validity. The genes must exactly the same
   * as inside this supergene.
   * At <i>least about 5 % of the randomly
   * generated Supergene suparallele values should be valid.</i> If the valid
   * combinations represents too small part of all possible combinations,
   * it can take too long to find the suitable mutation that does not brake
   * a supergene. If you face this problem, try to split the supergene into
   * several sub-supergenes.
   *
   * This method is only called if you have not set any alternative
   * validator (including <i>null</i>).
   *
   * @param a_case ignored here
   * @param a_forSupergene ignored here
   *
   * @return true only if the supergene allele combination is valid
   * @throws Error by default. If you do not set external validator,
   * you should always override this method
   */
  public boolean isValid(final Gene[] a_case, final Supergene a_forSupergene) {
    throw new Error("For " + getClass().getName() + ", override "
                    + " isValid (Gene[], Supergene) or set an"
                    + " external validator.");
  }

  /**
   * Creates a new instance of this Supergene class with the same number of
   * genes, calling newGene() for each subgene. The class, derived from this
   * abstract supergene will be instantiated
   * (not the instance of abstractSupergene itself). If the external
   * validator is set, the same validator will be set for the new gene.
   *
   * @return the new Gene
   * @throws Error if the instance of <i>this</i> cannot be instantiated
   * (for example, if it is not public or  the parameterless constructor is
   * not provided).
   * */
  protected Gene newGeneInternal() {
    Gene[] g = new Gene[m_genes.length];
    for (int i = 0; i < m_genes.length; i++) {
      g[i] = m_genes[i].newGene();
    }
    try {
      Constructor constr = getClass().getConstructor(new Class[] {Configuration.class, Gene[].class});
      AbstractSupergene asg =
          (AbstractSupergene) constr.newInstance(new Object[] {getConfiguration(), g});
      if (m_validator != this) {
        asg.setValidator(m_validator);
      }
      return asg;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new Error(
          "This should not happen. Is the constructor with parameters "
          + "{org.jgap.Configuration, org,jgap,Gene[]} provided for "
          + getClass().getName() + "?");
    }
  }

  /**
   * Applies a mutation of a given intensity (percentage) onto the gene
   * at the given index. Retries while isValid() returns true for the
   * supergene. The method is delegated to the first element of the
   * gene, indexed by a_index.
   * See org.jgap.supergenes.AbstractSupergene.isValid()
   */
  public void applyMutation(final int a_index, final double a_percentage) {
    // Immediately return the current value is found in the list of immutable
    // alleles for this position.
    // ----------------------------------------------------------------------
    if (a_index < m_immutable.length) {
      if (m_immutable[a_index] != null) {
        synchronized (m_immutable) {
          if (m_immutable[a_index].contains(this)) {
            return;
          }
        }
      }
    }
    // Following commented out because if only very few valid states exist, it
    // may be that they are not reached within a given number of tries.
    // ----------------------------------------------------------------------
//    if (!isValid()) {
//      throw new Error("Should be valid on entry");
//    }
    Object backup = m_genes[a_index].getAllele();
    // Care that in case of a composite supergene, each sub-gene is mutated
    // sometimes.
    // --------------------------------------------------------------------
    int size = m_genes[a_index].size();
    int mutIndex;
    if (size > 0) {
      mutIndex = getConfiguration().getRandomGenerator().nextInt(size + 1);
    }
    else {
      mutIndex = 0;
    }
    for (int i = 0; i < MAX_RETRIES; i++) {
      m_genes[a_index].applyMutation(mutIndex, a_percentage);
      if (isValid()) {
        return;
      }
    }
    // restore the gene as it was
    m_genes[a_index].setAllele(backup);
    markImmutable(a_index);
  }

  /** @todo: Implement protection against overgrowing of this
   * data block.
   */
  private void markImmutable(final int a_index) {
    synchronized (m_immutable) {
      if (m_immutable.length <= a_index) {
        // Extend the array (double length).
        // ---------------------------------
        Set[] r = new Set[2 * m_immutable.length];
        System.arraycopy(m_immutable, 0, r, 0, m_immutable.length);
        m_immutable = r;
      }
      if (m_immutable[a_index] == null) {
        m_immutable[a_index] = new TreeSet();
      }
      if (m_immutable[a_index].size() < MAX_IMMUTABLE_GENES) {
        m_immutable[a_index].add(this);
      }
    }
    ;
  }

  /**
   * Discards all internal caches, ensuring correct repetetive tests
   * of performance. Differently from cleanup(), discards also static
   * references, that are assumed to be useful for the multiple instances
   * of the Supergene.
   * Clears the set of the alleles that are known to be immutable.
   */
  public static void reset() {
    m_immutable = new Set[1];
  }

  /**
   * Sets the value of this Gene to a random legal value for the
   * implementation. It calls setToRandomValue for all subgenes and
   * then validates. With a large number of subgenes and low percent of
   * valid combinations this may take too long to complete. We think,
   * at lease several % of the all possible combintations must be valid.
   */
  public void setToRandomValue(final RandomGenerator a_numberGenerator) {
    // set all to random value first
    for (int i = 0; i < m_genes.length; i++) {
      m_genes[i].setToRandomValue(a_numberGenerator);
    }
    if (isValid()) {
      return;
    }
    for (int i = 0; i < MAX_RETRIES; i++) {
      for (int j = 0; j < m_genes.length; j++) {
        // Mutate only one gene at time.
        // -----------------------------
        m_genes[j].setToRandomValue(a_numberGenerator);
        if (isValid()) {
          return;
        }
      }
    }
  }

  /**
   * Sets the allele.
   * @param a_superAllele must be an array of objects, size matching the
   * number of genes
   */
  public void setAllele(final Object a_superAllele) {
    if (m_genes.length < 1) {
      // Nothing to do
      return;
    }
    Object[] a = (Object[]) a_superAllele;
    if (a.length != m_genes.length) {
      throw new IllegalArgumentException("Record length, " + a.length
                                   + " not equal to "
                                   + m_genes.length);
    }
    for (int i = 0; i < m_genes.length; i++) {
      m_genes[i].setAllele(a[i]);
    }
  }

  /**
   * Retrieves the allele value represented by this Supergene.
   * @return array of objects, each matching the subgene in this Supergene
   */
  public Object getAllele() {
    Object[] o = new Object[m_genes.length];
    for (int i = 0; i < m_genes.length; i++) {
      o[i] = m_genes[i].getAllele();
    }
    return o;
  }

  /**
   * @return a string representation of the value of this Supergene
   * instance, using calls to the Supergene components. Supports other
   * (nested) supergenes in this supergene
   * @throws UnsupportedOperationException
   */
  public String getPersistentRepresentation() throws UnsupportedOperationException {
    StringBuffer b = new StringBuffer();
    // Write validator:
    String validator = null;
    String v_representation = "";
    SupergeneValidator v = getValidator();
    if (v == null) {
      validator = "null";
    }
    else
    if (v == this) {
      validator = "this";
    }
    else {
      validator = v.getClass().getName();
      v_representation = v.getPersistent();
    }
    b.append(GENE_DELIMITER_HEADING);
    b.append(encode(validator + GENE_DELIMITER + v_representation));
    b.append(GENE_DELIMITER_CLOSING);
    // Write genes:
    Gene gene;
    for (int i = 0; i < m_genes.length; i++) {
      gene = m_genes[i];
      b.append(GENE_DELIMITER_HEADING);
      b.append(encode(gene.getClass().getName() + GENE_DELIMITER
                      + gene.getPersistentRepresentation()));
      b.append(GENE_DELIMITER_CLOSING);
    }
    return b.toString();
  }

  /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method.
   *
   * If the validator is not THIS and not null, a new validator is
   * created using Class.forName(..).newInstance.
   *
   * @param a_representation the string representation retrieved from a
   * prior call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedRepresentationException
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      try {
        /// Remove the old content.
        // ------------------------
        List r = split(a_representation);
        Iterator iter = r.iterator();
        m_genes = new Gene[r.size() - 1];
        // The first member in array is a validator representation.
        // --------------------------------------------------------
        StringTokenizer st;
        String clas;
        String representation;
        String g;
        Gene gene;
        String validator = (String) iter.next();
        setValidator(createValidator(decode(validator)));
        for (int i = 0; i < m_genes.length; i++) {
          g = decode( (String) iter.next());
          st = new StringTokenizer(g, GENE_DELIMITER);
          if (st.countTokens() != 2)
            throw new UnsupportedRepresentationException("In " + g + ", " +
                "expecting two tokens, separated by " + GENE_DELIMITER);
          clas = st.nextToken();
          representation = st.nextToken();
          gene = createGene(clas, representation);
          m_genes[i] = gene;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        throw new UnsupportedRepresentationException(ex.getCause().
            getMessage());
      }
    }
    else {
      throw new UnsupportedRepresentationException("null value not allowed");
    }
  }

  /** Create validator from the string representation. */
  protected SupergeneValidator createValidator(String a_rep) {
    try {
      StringTokenizer vo = new StringTokenizer
          (a_rep, GENE_DELIMITER, true);
      if (vo.countTokens() != 2)throw new Error
          ("In " + a_rep + ", expecting two tokens, separated by " +
           GENE_DELIMITER);
      String clas = vo.nextToken();
      SupergeneValidator sv;
      if (clas.equals("this")) {
        sv = this;
      }
      else if (clas.equals("null")) {
        sv = null;
      }
      else {
//        sv = (SupergeneValidator) Class.forName(clas).newInstance();
        Class svClass = Class.forName(clas);
        Constructor constr = svClass.getConstructor(new Class[] {Configuration.class});
        sv = (SupergeneValidator) constr.newInstance(new Object[] {
            getConfiguration()});
      }
      if (sv != null) {
        sv.setFromPersistent(decode(vo.nextToken()));
      }
      return sv;
    }
    catch (Exception ex) {
      throw new Error
          ("Unable to create validator from '" + a_rep + "' for " +
           getClass().getName(), ex);
    }
  }

  /** Creates a new instance of gene. */
  protected Gene createGene(String a_geneClassName,
                            String a_persistentRepresentation)
      throws Exception {
    Class geneClass = Class.forName(a_geneClassName);
    Constructor constr = geneClass.getConstructor(new Class[] {Configuration.class});
    Gene gene = (Gene) constr.newInstance(new Object[] {getConfiguration()});
    gene.setValueFromPersistentRepresentation(a_persistentRepresentation);
    return gene;
  }

  /** Calls cleanup() for each subgene. */
  public void cleanup() {
    for (int i = 0; i < m_genes.length; i++) {
      m_genes[i].cleanup();
    }
  }

  /**
   * @return a string representation of the supergene, providing
   * class name and calling toString() for all subgenes.
   */
  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("Supergene " + getClass().getName() + " {");
    for (int i = 0; i < m_genes.length; i++) {
      b.append("|");
      b.append(m_genes[i].toString());
      b.append("|");
    }
    if (m_validator == null) {
      b.append(" non validating");
    }
    else {
      b.append(" validator: "+m_validator.getClass().getName());
    }
    b.append("}");
    return b.toString();
  }

  /** Returns the number of the genes-components of this supergene. */
  public int size() {
    return m_genes.length;
  }

  /** Calls compareTo() for all subgenes. The passed parameter must be
   * an instance of AbstractSupergene. */
  public int compareTo(Object o) {
    AbstractSupergene q = (AbstractSupergene) o;
    int c = m_genes.length - q.m_genes.length;
    if (c != 0) {
      return c;
    }
    for (int i = 0; i < m_genes.length; i++) {
      c = m_genes[i].compareTo(q.m_genes[i]);
      if (c != 0) {
        return c;
      }
    }
    if (getClass().equals(o.getClass())) {
      return 0;
    }
    return getClass().getName().compareTo(o.getClass().getName());
  }

  /**
   * Calls equals() for each pair of genes. If the supplied object is
   * an instance of the different class, returns false. Also, the
   * genes are assumed to be different if they have different validator
   * classes (or only one of the validators is set to null).
   */
  public boolean equals(Object a_gene) {
    if (a_gene == null || ! (a_gene.getClass().equals(getClass()))) {
      return false;
    }
    AbstractSupergene age = (AbstractSupergene) a_gene;
    if (m_validator != age.m_validator)
      if (m_validator != null && age.m_immutable != null)
        if (!m_validator.getClass().equals(age.m_validator.getClass()))
          return false;
    return Arrays.equals(m_genes, age.m_genes);
  }

  /** Returns sum of hashCode() of the genes-components. */
  public int hashCode() {
    int s = 0;
    for (int i = m_genes.length - 1; i >= 0; i--) {
      s += m_genes[i].hashCode();
    }
    return s;
  }

  /**
   * Splits the string a_x into individual gene representations
   * @param a_string the string to split
   * @return the elements of the returned array are the
   * persistent representation strings of the genes - components
   *
   * @author Audrius Meskauskas
   */
  protected static final List split(String a_string)
      throws UnsupportedRepresentationException {
    List a = Collections.synchronizedList(new ArrayList());
    StringTokenizer st = new StringTokenizer
        (a_string, GENE_DELIMITER_HEADING + GENE_DELIMITER_CLOSING, true);
    while (st.hasMoreTokens()) {
      if (!st.nextToken().equals(GENE_DELIMITER_HEADING)) {
        throw new UnsupportedRepresentationException
            (a_string + " no open tag");
      }
      String n = st.nextToken();
      if (n.equals(GENE_DELIMITER_CLOSING)) a.add(""); // Empty token
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

  /** Append a new gene to the gene array. */
  public void addGene(Gene a_gene) {
    Gene[] genes = new Gene[m_genes.length + 1];
    System.arraycopy(m_genes, 0, genes, 0, m_genes.length);
    genes[m_genes.length] = a_gene;
    m_genes = genes;
  }

  /**
   * Sets an object, responsible for deciding if the Supergene allele
   * combination is valid. If it is set to null, no validation is performed
   * (all combinations are assumed to be valid). If no validator is
   * set, the method <code>isValid (Gene [] ) </code>is called.
   */
  public void setValidator(SupergeneValidator a_validator) {
    m_validator = a_validator;
  }

  /**
   * Gets an object, responsible for deciding if the Supergene allele
   * combination is valid. If no external validator was set and the
   * class uses its own internal validation method, it returns <i>this</i>
   */
  public SupergeneValidator getValidator() {
    return m_validator;
  }

  /** A validator (initially set to <i>this</i> */
  protected SupergeneValidator m_validator = this;

  /** {@inheritDoc}
   * The default implementation returns an empty string. */
  public String getPersistent() {
    return "";
  }

  /** {@inheritDoc}
   * The default implementation does nothing. */
  public void setFromPersistent(String a_from) {
  }

  /**
   * @return not needed for abstract supergene
   */
  public Object getInternalValue() {
    if (true) {
      throw new RuntimeException("getInternalValue() called unexpectedly!");
    }
    return null;
  }
}
