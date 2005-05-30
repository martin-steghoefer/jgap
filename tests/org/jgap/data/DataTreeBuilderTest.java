/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

import org.jgap.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests for DataTreeBuilder class
 *
 * @author Klaus Meffert
 * @author Siddhartha Azad
 * @since 1.0
 */
public class DataTreeBuilderTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;

  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public static Test suite() {
    TestSuite suite = new TestSuite(DataTreeBuilderTest.class);
    return suite;
  }

  /**
   * Create a Genotype and represent it as a document, and verify that the
   * representation is as expected.
   */
  public void testRepresentGenotypeAsDocument_0() {
    try {
      //Create a Genotype with a population of NUM_CHROMS Chromosomes, each
      //Chromosome with NUM_GENES Genes.
      Chromosome[] chroms = new Chromosome[NUM_CHROMS];
      for (int i = 0; i < NUM_CHROMS; i++) {
        chroms[i] = new Chromosome(new Gene[] {
                                   new IntegerGene(1, 5), new IntegerGene(1, 10)});
        chroms[i].getGene(0).setAllele(new Integer(i + 1));
        chroms[i].getGene(1).setAllele(new Integer(i + 1));
      }
      // configuration setup
      Configuration conf = new DefaultConfiguration();
      conf.setFitnessFunction(new StaticFitnessFunction(5));
      conf.setPopulationSize(NUM_CHROMS);
      conf.setSampleChromosome(new Chromosome(new Gene[] {
                                              new IntegerGene(1, 5),
                                              new IntegerGene(1, 3)}));
      Population popul = new Population(chroms);
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
      assertTrue(element.getTagName().equals("genotype"));
      IDataElementList chromList = element.getChildNodes();
      assertTrue(chromList.getLength() == NUM_CHROMS);
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
    catch (InvalidConfigurationException invex) {
      invex.printStackTrace();
      fail("Invalid Config");
    }
    catch (IllegalArgumentException invex) {
      fail("Illegal Arg");
    }
    catch (Exception ex) {
      fail("Exception Thrown");
    }
  }
}
