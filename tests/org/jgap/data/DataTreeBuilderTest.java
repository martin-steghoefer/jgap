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

import junit.framework.*;

/**
 * Tests for DataTreeBuilder class
 *
 * @author Klaus Meffert
 * @author Siddhartha Azad
 * @since 1.0
 */

import org.jgap.*;
import org.jgap.impl.*;

public class DataTreeBuilderTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";
  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;
  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public DataTreeBuilderTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DataTreeBuilderTest.class);
    return suite;
  }

    /**
       Create a Genotype and represent it as a document, and verify that the
       representation is as expected.
    */
  public void testRepresentGenotypeAsDocument_0() {
      try {
	  /*
	    Create a Genotype with a population of NUM_CHROMS Chromosomes, each
	    Chromosome with NUM_GENES Genes.
	  */
	  Chromosome[] chroms = new Chromosome[NUM_CHROMS];
	  for(int i = 0; i < NUM_CHROMS; i++) {
	      chroms[i] = new Chromosome(new Gene[] {
		  new IntegerGene(1, 5), new IntegerGene(1, 10)});
	      chroms[i].getGene(0).setAllele(new Integer(i+1));
	      chroms[i].getGene(1).setAllele(new Integer(i+1));
	  }
	  // configuration setup
	  Configuration conf = new DefaultConfiguration();
	  conf.setFitnessFunction(new StaticFitnessFunction(5));
	  conf.setPopulationSize(NUM_CHROMS);
	  conf.setSampleChromosome(new Chromosome(new Gene[] {
	      new IntegerGene(1, 5), new IntegerGene(1, 3)}));
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
	  for(int i = 0; i < NUM_CHROMS; i++) {
	      IDataElement chrom = chromList.item(i);
	      assertTrue(chrom.getTagName().equals("chromosome"));
	      IDataElementList genesList = chrom.getChildNodes();
	      assertTrue(genesList.getLength() == 1);
	      IDataElement genes = genesList.item(0);
	      assertTrue(genes.getTagName().equals("genes"));
	      IDataElementList geneList = genes.getChildNodes();
	      assertTrue(geneList.getLength() == NUM_GENES);
	      // for all genes in a chromosome
	      for(int j = 0; j < NUM_GENES; j++) {
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
