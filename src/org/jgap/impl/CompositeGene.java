/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.*;

import org.jgap.*;

/**
 * Ordered container for multiple genes
 * Has the same interface as a single gene and could be used accordingly.
 * Use the addGene(Gene) method to add single genes (not CompositeGenes!) after
 * construction, an empty CompositeGene without genes makes no sense.
 * Beware that there are two equalities defined for a CompsoiteGene in respect
 * to its contained genes:
 * a) Two genes are (only) equal if they are identical
 * b) Two genes are (seen as) equal if their equals method returns true
 *
 * This influences several methods such as addGene. Notice that it is "better"
 * to use addGene(a_gene, false) than addGene(a_gene, true) because the second
 * variant only allows to add genes not seen as equal to already added genes in
 * respect to their equals function. But: the equals function returns true for
 * two different DoubleGenes (e.g.) just after their creation. If no specific
 * (and hopefully different) allele is set for these DoubleGenes they are seen
 * as equal!
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CompositeGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  /**
   * Represents the delimiter that is used to separate genes in the
   * persistent representation of CompositeGene instances.
   */
  public final static String GENE_DELIMITER = "*";

  private Gene m_geneTypeAllowed;

  /**
   * The genes contained in this CompositeGene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private List genes;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Holds the configuration object associated with the Gene. The configuration
   * object is important to obtain referenced objects from it, like the
   * RandomGenerator.
   */
  private Configuration m_configuration;

  /**
   * @author Klaus Meffert
   * @since 1.1
   */
  public CompositeGene() {
    this(new DefaultConfiguration());
  }

  /**
   * @param a_configuration
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public CompositeGene(Configuration a_configuration) {
    this(null, a_configuration);
  }

  /**
   * Allows to specify which Gene implementation is allowed to be added to the
   * CompositeGene. Uses the DefaultConfiguration.
   *
   * @param a_geneTypeAllowed the class of Genes to be allowed to be added to
   * the CompositeGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public CompositeGene(Gene a_geneTypeAllowed) {
    this(a_geneTypeAllowed, new DefaultConfiguration());
  }

  /**
   * Allows to specify which Gene implementation is allowed to be added to the
   * CompositeGene. Uses the specified Configuration.
   *
   * @param a_geneTypeAllowed the class of Genes to be allowed to be added to
   * the CompositeGene.
   * @param a_configuration sic.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public CompositeGene(Gene a_geneTypeAllowed, Configuration a_configuration) {
    m_configuration = a_configuration;
    genes = new Vector();
    if (a_geneTypeAllowed != null) {
      m_geneTypeAllowed = a_geneTypeAllowed;
    }
  }

  public void addGene(Gene a_gene) {
    addGene(a_gene, false);
  }

  /**
   * Adds a gene to the CompositeGene's container. See comments in class
   * header for additional details about equality (concerning "strict" param.)
   * @param a_gene the gene to be added
   * @param strict false: add the given gene except the gene itself already is
   *        contained within the CompositeGene's container.
   *        true: add the gene if there is no other gene being equal to the
   *        given gene in request to the Gene's equals method
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addGene(Gene a_gene, boolean strict) {
    if (m_geneTypeAllowed != null) {
      if (! a_gene.getClass().getName().equals(m_geneTypeAllowed.getClass().getName())) {
        throw new IllegalArgumentException("Adding a "+a_gene.getClass().getName()
                                           + " has been forbidden!");
      }
    }
    if (a_gene instanceof CompositeGene) {
      throw new IllegalArgumentException("It is not allowed to add a"
                                         + " CompositeGene to a CompositeGene!");
    }
    //check if gene already exists
    //----------------------------
    boolean containsGene;
    if (!strict) {
      containsGene = containsGeneByIdentity(a_gene);
    }
    else {
      containsGene = genes.contains(a_gene);
    }
    if (containsGene) {
      throw new IllegalArgumentException("The gene is already contained"
                                         + " in the CompositeGene!");
    }
    genes.add(a_gene);
  }

  /**
   * Removes the given gene from the collection of genes. The gene is only
   * removed if an object of the same identity is contained. The equals
   * method will not be used here intentionally
   * @param gene the gene to be removed
   * @return true: given gene found and removed
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean removeGeneByIdentity(Gene gene) {
    boolean result;
    int size = size();
    if (size < 1) {
      result = false;
    }
    else {
      result = false;
      for (int i = 0; i < size; i++) {
        if (geneAt(i) == gene) {
          genes.remove(i);
          result = true;
          break;
        }
      }
    }
    return result;
  }

  /**
   * Removes the given gene from the collection of genes. The gene is
   * removed if another gene exists that is equal to the given gene in respect
   * to the equals method of the gene
   * @param gene the gene to be removed
   * @return true: given gene found and removed
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean removeGene(Gene gene) {
    return genes.remove(gene);
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
    for (int i = 0; i < genes.size(); i++) {
      gene = (Gene) genes.get(i);
      gene.cleanup();
    }
  }

  /**
   * See interface Gene for description
   * @param a_numberGenerator The random number generator that should be
   *        used to create any random values. It's important to use this
   * generator to maintain the user's flexibility to configure the genetic
   * engine to use the random number generator of their choice.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    Gene gene;
    for (int i = 0; i < genes.size(); i++) {
      gene = (Gene) genes.get(i);
      gene.setToRandomValue(a_numberGenerator);
    }
  }

  /**
   * See interface Gene for description
   * @param a_representation the string representation retrieved from a
   *        prior call to the getPersistentRepresentation() method.
   *
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setValueFromPersistentRepresentation(String a_representation) throws
      UnsupportedRepresentationException {
    if (a_representation != null) {
      StringTokenizer tokenizer =
          new StringTokenizer(a_representation, GENE_DELIMITER);
      int numberGenes = tokenizer.countTokens();
      String singleGene;
      String geneTypeClass;
      try {
        for (int i = 0; i < numberGenes; i++) {
          singleGene = tokenizer.nextToken();
          StringTokenizer geneTypeTokenizer =
              new StringTokenizer(singleGene,
                                  Gene.PERSISTENT_FIELD_DELIMITER);
          //read type for every gene and then newly construct it
          //----------------------------------------------------
          geneTypeClass = geneTypeTokenizer.nextToken();
          Class clazz = Class.forName(geneTypeClass);
          Gene gene = (Gene) clazz.newInstance();
          //now work with the freshly constructed genes
          // ------------------------------------------
          String rep = "";
          while (geneTypeTokenizer.hasMoreTokens()) {
            if (rep.length() > 0) {
              rep += Gene.PERSISTENT_FIELD_DELIMITER;
            }
            rep += geneTypeTokenizer.nextToken();
          }
          gene.setValueFromPersistentRepresentation(rep);
          addGene(gene);
        }
      }
      catch (Exception ex) {
        throw new UnsupportedRepresentationException(ex.getCause().
            getMessage());
      }
    }
  }

  /**
   * See interface Gene for description
   * @return A string representation of this Gene's current state.
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public String getPersistentRepresentation() throws
      UnsupportedOperationException {
    String result = "";
    Gene gene;
    for (int i = 0; i < genes.size(); i++) {
      gene = (Gene) genes.get(i);
      //save type with every gene to make the process reversible
      //--------------------------------------------------------
      result += gene.getClass().getName();
      result += gene.PERSISTENT_FIELD_DELIMITER;
      //get persistent representation from each gene itself
      result += gene.getPersistentRepresentation();
      if (i < genes.size() - 1) {
        result += GENE_DELIMITER;
        /**@todo if GENE_DELIMITER occurs in a gene (e.g. StringGene)
         * undertake actions to maintain consistency
         */
      }
    }
    return result;
  }

  /**
   * Retrieves the value represented by this Gene. All values returned
   * by this class will be Vector instances. Each element of the Vector
   * represents the allele of the corresponding gene in the CompositeGene's
   * container
   *
   * @return the Boolean value of this Gene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Object getAllele() {
    List alleles = new Vector();
    Gene gene;
    for (int i = 0; i < genes.size(); i++) {
      gene = (Gene) genes.get(i);
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
   * @param a_newValue the new value of this Gene instance.
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
    if (m_geneAlleleChecker != null) {
      if (!m_geneAlleleChecker.verify(this, a_newValue)) {
        return;
      }
    }
    List alleles = (List) a_newValue;
    Gene gene;
    for (int i = 0; i < alleles.size(); i++) {
      gene = (Gene) genes.get(i);
      gene.setAllele(alleles.get(i));
    }
  }

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object a_newValue) is called
   * @param a_constraintChecker the constraint checker to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setConstraintChecker(IGeneConstraintChecker a_constraintChecker) {
    m_geneAlleleChecker = a_constraintChecker;
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setAllele(Object a_newValue) is called
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

  /**
   * Provides an implementation-independent means for creating new Gene
   * instances. The new instance that is created and returned should be
   * setup with any implementation-dependent configuration that this Gene
   * instance is setup with (aside from the actual value, of course). For
   * example, if this Gene were setup with bounds on its value, then the
   * Gene instance returned from this method should also be setup with
   * those same bounds. This is important, as the JGAP core will invoke this
   * method on each Gene in the sample Chromosome in order to create each
   * new Gene in the same respective gene position for a new Chromosome.
   * <p>
   * It should be noted that nothing is guaranteed about the actual value
   * of the returned Gene and it should therefore be considered to be
   * undefined.
   *
   * @param a_activeConfiguration The current active configuration.
   * @return A new Gene instance of the same type and with the same
   *         setup as this concrete Gene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Gene newGene(Configuration a_activeConfiguration) {
    CompositeGene compositeGene = new CompositeGene(a_activeConfiguration);
    Gene gene;
    int geneSize = genes.size();
    for (int i = 0; i < geneSize; i++) {
      gene = (Gene) genes.get(i);
      compositeGene.addGene(gene.newGene(a_activeConfiguration), false);
    }
    return compositeGene;
  }

  /**
   * Compares this CompositeGene with the specified object for order. A
   * false value is considered to be less than a true value. A null value
   * is considered to be less than any non-null value.
   *
   * @param  other the CompositeGene to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this CompositeGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(Object other) {
    CompositeGene otherCompositeGene = (CompositeGene) other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the contained genes' compareTo
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherCompositeGene == null) {
      return 1;
    }
    else if (otherCompositeGene.isEmpty()) {
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      return isEmpty() ? 0 : 1;
    }
    else {
      //compare each gene against each other
      // -----------------------------------
      int numberGenes = Math.min(size(), otherCompositeGene.size());
      Gene gene1;
      Gene gene2;
      for (int i = 0; i < numberGenes; i++) {
        gene1 = geneAt(i);
        gene2 = otherCompositeGene.geneAt(i);
        if (gene1 == null) {
          if (gene2 == null) {
            continue;
          }
          else {
            return -1;
          }
        }
        else {
          int result = gene1.compareTo(gene2);
          if (result != 0) {
            return result;
          }
        }
      }
      //if everything is equal until now the CompositeGene with more
      //contained genes wins
      // -----------------------------------------------------------
      if (size() == otherCompositeGene.size()) {
        return 0;
      }
      else {
        return size() > otherCompositeGene.size() ? 1 : -1;
      }
    }
  }

  /**
   * Compares this CompositeGene with the given object and returns true if
   * the other object is a IntegerGene and has the same value (allele) as
   * this IntegerGene. Otherwise it returns false.
   *
   * @param other the object to compare to this IntegerGene for equality.
   * @return true if this IntegerGene is equal to the given object,
   *         false otherwise.
   *
   * @since 1.1
   */
  public boolean equals(Object other) {
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't an IntegerGene, then we're not
      // equal.
      // ----------------------------------------------------------
      return false;
    }
  }

  /**
   * Retrieves a string representation of this CompositeGene's value that
   * may be useful for display purposes.
   * @return a string representation of this CompositeGene's value. Every
   * contained gene's string representation is delimited by the given
   * delimiter
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.1
   */
  public String toString() {
    if (genes.isEmpty()) {
      return "null";
    }
    else {
      String result = "";
      Gene gene;
      for (int i = 0; i < genes.size(); i++) {
        gene = (Gene) genes.get(i);
        result += gene;
        if (i < genes.size() - 1) {
          result += GENE_DELIMITER;
        }
      }
      return result;
    }
  }

  /**
   * @return true: no genes contained, false otherwise
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean isEmpty() {
    return genes.isEmpty() ? true : false;
  }

  /**
   * Returns the gene at the given index
   * @param index sic
   * @return the gene at the given index
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Gene geneAt(int index) {
    return (Gene) genes.get(index);
  }

  /**
   * @return the number of genes contained
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int size() {
    return genes.size();
  }

  /**
   * Checks whether a specific gene is already contained. The determination
   * will be done by checking for identity and not using the equal method!
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
   * satisfy the Gene interface.
   * Instead, loop over all cotnained genes and call their applyMutation
   * method.
   * @param index does not matter here
   * @param a_percentage does not matter here
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    for (int i = 0; i < size(); i++) {
      // problem here: size() of CompositeGene not equal to (different)
      // sizes of contained genes.
      // Solution: Don't use CompositeGene.applyMutation, instead loop
      //           over all contained genes and call their method
      // -------------------------------------------------------------
      throw new RuntimeException("applyMutation may not be called for"
          + " a CompositeGene. Call this method for each gene contained"
                                 + " in the CompositeGene.");
    }
  }
}
