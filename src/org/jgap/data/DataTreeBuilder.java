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

package org.jgap.data;

import org.jgap.*;

/**
 * Builds a tree structure from Genes, Chrosomes or from a Genotype.
 * <p>
 * Generated result is generic data type usable for concrete persistence
 * strategies, including XML documents, writing an object to a file or
 * stream etc.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DataTreeBuilder {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Constant representing the name of the genotype element tag.
   */
  private static final String GENOTYPE_TAG = "genotype";

  /**
   * Constant representing the name of the chromosome element tag.
   */
  private static final String CHROMOSOME_TAG = "chromosome";

  /**
   * Constant representing the name of the gene element tag.
   */
  private static final String GENES_TAG = "genes";

  /**
   * Constant representing the name of the gene element tag.
   */
  private static final String GENE_TAG = "gene";

  private static final String ALLELE_TAG = "allele";

  /**
   * Constant representing the name of the size attribute that is
   * added to genotype and chromosome elements to describe their size.
   */
  private static final String SIZE_ATTRIBUTE = "size";

  /**
   * Constant representing the fully-qualified name of the concrete
   * Gene class that was marshalled.
   */
  private static final String CLASS_ATTRIBUTE = "class";

  /**
   * Shared lock object used for synchronization purposes.
   */
  private Object m_lock;

  private static DataTreeBuilder instance;

  /**
   * @return the singleton instance of this class
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public static DataTreeBuilder getInstance() {
    if (instance == null) {
      instance = new DataTreeBuilder();
      instance.m_lock = new Object();
    }
    return instance;
  }

  /**
   * Private constructor for Singleton
   */
  private DataTreeBuilder() {
  }

  public static void main(String[] args)
      throws Exception {
  }

  /**
   * Represent a Genotype as a generic data document, including its
   * population of Chromosome instances.
   *
   * @param a_subject The genotype to represent
   * @throws Exception
   * @return a generic document object representing the given Genotype.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataCreators representGenotypeAsDocument(Genotype a_subject)
      throws Exception {
    // DocumentBuilders do not have to be thread safe, so we have to
    // protect creation of the Document with a synchronized block.
    // -------------------------------------------------------------
    IDataCreators genotypeDocument;
    synchronized (m_lock) {
      genotypeDocument = new DataElementsDocument();
      genotypeDocument.setTree(createTree());
    }
    IDataElement genotypeElement = representGenotypeAsElement(a_subject);
    genotypeDocument.appendChild(genotypeElement);
    return genotypeDocument;
  }

  /**
   * Represent a Genotype as a generic data element, including its
   * population of Chromosome instances.
   *
   * This may be useful in scenarios where representation as an
   * entire document is undesirable, such as when the representation
   * of this Genotype is to be combined with other elements in a
   * single document.
   *
   * @param a_subject The genotype to represent
   * @throws Exception
   * @return an element object representing the given Genotype.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataElement representGenotypeAsElement(Genotype a_subject)
      throws Exception {
    Population population = a_subject.getPopulation();
    // Start by creating the genotype element and its size attribute,
    // which represents the number of chromosomes present in the
    // genotype.
    // --------------------------------------------------------------
    IDataElement genotypeTag = new DataElement(GENOTYPE_TAG);
    genotypeTag.setAttribute(SIZE_ATTRIBUTE,
                             Integer.toString(population.size()));
    // Next, add nested elements for each of the chromosomes in the
    // genotype.
    // ------------------------------------------------------------
    for (int i = 0; i < population.size(); i++) {
      IDataElement chromosomeElement =
          representChromosomeAsElement(population.getChromosome(i));
      genotypeTag.appendChild(chromosomeElement);
    }
    return genotypeTag;
  }

  /**
   * Represent a Chromosome as a generic data type document, including its
   * contained Gene instances.
   *
   * @param a_subject The chromosome to represent
   * @throws Exception
   * @return a document object representing the given Chromosome.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataCreators representChromosomeAsDocument(Chromosome a_subject)
      throws Exception {
    // DocumentBuilders do not have to be thread safe, so we have to
    // protect creation of the Document with a synchronized block.
    // -------------------------------------------------------------
    IDataCreators chromosomeDocument;
    synchronized (m_lock) {
      //hier die Datenstruktur für den Tree aufbauen
      chromosomeDocument = new DataElementsDocument();
      chromosomeDocument.setTree(createTree());
    }
    IDataElement chromosomeElement =
        representChromosomeAsElement(a_subject);
    chromosomeDocument.appendChild(chromosomeElement);
    return chromosomeDocument;
  }

  protected IDataElementList createTree() {
    return new DataElementList();
  }

  /**
   * Represent a Chromosome as a generic data element, including its
   * contained Gene instances.
   * This may be useful in scenarios where representation as an entire document
   * is undesirable, such as when the representation of this Chromosome is to
   * be combined with other elements in a single document.
   *
   * @param a_subject The chromosome to represent
   * @throws Exception
   * @return an element object representing the given Chromosome.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataElement representChromosomeAsElement(Chromosome a_subject)
      throws Exception {
    // Start by creating an element for the chromosome and its size
    // attribute, which represents the number of genes in the chromosome.
    // ------------------------------------------------------------------
    IDataElement chromosomeElement = new DataElement(CHROMOSOME_TAG);
    chromosomeElement.setAttribute(SIZE_ATTRIBUTE,
                                   Integer.toString(a_subject.size()));
    // Next create the genes element with its nested gene elements,
    // which will contain string representations of the alleles.
    // --------------------------------------------------------------
    IDataElement genesElement = representGenesAsElement(a_subject.getGenes());
    // Add the new genes element to the chromosome element and then
    // Add the new genes element to the chromosome element and then
    // return the chromosome element.
    // -------------------------------------------------------------
    chromosomeElement.appendChild(genesElement);
    return chromosomeElement;
  }

  /**
   * Represent Genes as a generic data type element.
   *
   * @param a_geneValues The genes to represent
   * @throws Exception
   * @return an element object representing the given genes.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataElement representGenesAsElement(Gene[] a_geneValues)
      throws Exception {
    // Create the parent genes element.
    // --------------------------------
    IDataElement genesElement = new DataElement(GENES_TAG);
    // Now add gene sub-elements for each gene in the given array.
    // ---------------------------------------------------------------
    IDataElement geneElement;
    for (int i = 0; i < a_geneValues.length; i++) {
      geneElement = representGeneAsElement(a_geneValues[i]);
      genesElement.appendChild(geneElement);
    }
    return genesElement;
  }

  /**
   * Represent a Gene as a generic data element.
   *
   * @param a_gene The Gene to represent
   * @throws Exception
   * @return an element object representing the given gene.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IDataElement representGeneAsElement(Gene a_gene)
      throws Exception {
    // Create the allele element for this gene.
    // ----------------------------------------
    IDataElement geneElement = new DataElement(GENE_TAG);
    // Add the class attribute and set its value to the class
    // name of the concrete class representing the current Gene.
    // ---------------------------------------------------------
    geneElement.setAttribute(CLASS_ATTRIBUTE,
                             a_gene.getClass().getName());
    // Create a text node to contain the string representation of
    // the gene's value (allele).
    // ----------------------------------------------------------
    geneElement.appendChild(representAlleleAsElement(a_gene));
    return geneElement;
  }

  /**
   * Represent an Allele as a generic data element
   *
   * @param gene the gene holding the allele
   * @throws Exception
   * @return IDataElement created data element
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private IDataElement representAlleleAsElement(Gene gene)
      throws Exception {
    IDataElement alleleElement = new DataElement(ALLELE_TAG);
    alleleElement.setAttribute("value", gene.getPersistentRepresentation());
    return alleleElement;
  }
}
