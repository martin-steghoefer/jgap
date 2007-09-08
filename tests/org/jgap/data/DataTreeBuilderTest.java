/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

import org.jgap.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the DataTreeBuilder class
 *
 * @author Klaus Meffert
 * @author Siddhartha Azad
 * @since 1.0
 */
public class DataTreeBuilderTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;

  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public static Test suite() {
    TestSuite suite = new TestSuite(DataTreeBuilderTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * Create a Genotype and represent it as a document, and verify that the
   * representation is as expected.
   * @throws Exception
   */
  public void testRepresentGenotypeAsDocument_0()
      throws Exception {
    // configuration setup
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new StaticFitnessFunction(5));
    conf.setPopulationSize(NUM_CHROMS);
    conf.setSampleChromosome(new Chromosome(conf, new Gene[] {
                                            new IntegerGene(conf, 1, 5),
                                            new IntegerGene(conf, 1, 3)}));
    //Create a Genotype with a population of NUM_CHROMS Chromosomes, each
    //Chromosome with NUM_GENES Genes.
    Chromosome[] chroms = new Chromosome[NUM_CHROMS];
    for (int i = 0; i < NUM_CHROMS; i++) {
      chroms[i] = new Chromosome(conf, new Gene[] {
                                 new IntegerGene(conf, 1, 5),
                                 new IntegerGene(conf, 1, 10)});
      chroms[i].getGene(0).setAllele(new Integer(i + 1));
      chroms[i].getGene(1).setAllele(new Integer(i + 1));
    }
    Population popul = new Population(conf, chroms);
    Genotype genotype = new Genotype(conf, popul);
    // write the genotype as a document
    IDataCreators doc = DataTreeBuilder.getInstance().
        representGenotypeAsDocument(genotype);
    // test if it got written as expected
    IDataElementList tree = doc.getTree();
    // a single top level element
    assertTrue(tree.getLength() == 1);
    IDataElement element = tree.item(0);
    // a Genotype should be the top level element
    assertEquals("genotype", element.getTagName());
    IDataElementList chromList = element.getChildNodes();
    assertEquals(NUM_CHROMS, chromList.getLength());
    // for all chromosomes
    for (int i = 0; i < NUM_CHROMS; i++) {
      IDataElement chrom = chromList.item(i);
      assertTrue(chrom.getTagName().equals("chromosome"));
      IDataElementList genesList = chrom.getChildNodes();
      assertTrue(genesList.getLength() == 1);
      IDataElement genes = genesList.item(0);
      assertTrue(genes.getTagName().equals("genes"));
      IDataElementList geneList = genes.getChildNodes();
      assertTrue(geneList.getLength() == NUM_GENES);
      // for all genes in a chromosome
      for (int j = 0; j < NUM_GENES; j++) {
        IDataElement gene = geneList.item(j);
        assertTrue(gene.getTagName().equals("gene"));
        IDataElementList alleleList = gene.getChildNodes();
        assertTrue(alleleList.getLength() == 1);
        IDataElement allele = alleleList.item(0);
        assertTrue(allele.getTagName().equals("allele"));
        assertTrue(allele.getAttribute("value").
                   equals(chroms[i].getGene(j).
                          getPersistentRepresentation()));
      }
    }
  }

  /**
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testRepresentChromosomeAsDocument_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    Chromosome chrom = new Chromosome(conf, new Gene[] {
                                      new IntegerGene(conf, 1, 5),
                                      new IntegerGene(conf, 1, 10)});
    chrom.getGene(0).setAllele(new Integer(1));
    chrom.getGene(1).setAllele(new Integer( -3));
    // write the chromosome as a document
    IDataCreators doc = DataTreeBuilder.getInstance().
        representChromosomeAsDocument(chrom);
    // test if it got written as expected
    IDataElementList tree = doc.getTree();
    // a single top level element
    assertTrue(tree.getLength() == 1);
    IDataElement element = tree.item(0);
    // a Chromosome should be the top level element
    assertTrue(element.getTagName().equals("chromosome"));
    IDataElementList chromsList = element.getChildNodes();
    assertEquals(1, chromsList.getLength());
    IDataElement genes = chromsList.item(0);
    assertTrue(genes.getTagName().equals("genes"));
    IDataElementList geneList = genes.getChildNodes();
    assertTrue(geneList.getLength() == NUM_GENES);
    // for all genes in a chromosome
    for (int j = 0; j < NUM_GENES; j++) {
      IDataElement gene = geneList.item(j);
      assertTrue(gene.getTagName().equals("gene"));
      IDataElementList alleleList = gene.getChildNodes();
      assertTrue(alleleList.getLength() == 1);
      IDataElement allele = alleleList.item(0);
      assertTrue(allele.getTagName().equals("allele"));
      assertTrue(allele.getAttribute("value").
                 equals(chrom.getGene(j).
                        getPersistentRepresentation()));
    }
  }
}
