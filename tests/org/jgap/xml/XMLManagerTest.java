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

package org.jgap.xml;

import org.jgap.*;
import org.jgap.impl.*;
import junit.framework.*;
import org.w3c.dom.*;
import junitx.util.PrivateAccessor;


/**
 * Tests for XMLManager class
 *
 * @author Klaus Meffert
 * @since 1.0
 */
public class XMLManagerTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public XMLManagerTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(XMLManagerTest.class);
    return suite;
  }

  private Configuration conf;
  private Chromosome chrom;
  private Gene[] genes;
  private Genotype genotype;
  private String CHROMOSOME_TAG;

  public void setUp() throws Exception {
    conf = new DefaultConfiguration();
    genes = new IntegerGene[2];
    genes[0] = new IntegerGene(0,100);
    genes[0].setAllele(new Integer(54));
    genes[1] = new IntegerGene(22,44);
    genes[1].setAllele(new Integer(37));
    conf.setFitnessFunction(new RandomFitnessFunction());
    conf.setPopulationSize(8);
    chrom = new Chromosome(genes);
    conf.setSampleChromosome(chrom);
    genotype = new Genotype(conf, new Chromosome[]{chrom});

    CHROMOSOME_TAG = (String) PrivateAccessor.getField(XMLManager.class,
        "CHROMOSOME_TAG");
  }

  public void testGetChromosomeFromDocument_0() throws Exception {
    /***@todo implement*/
    try {
      XMLManager.getChromosomeFromDocument(conf, null);
      fail();
    }
    catch (NullPointerException nex) {
      ;//this is OK
    }
  }

  public void testGetChromosomeFromDocument_1() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Chromosome chrom2 = XMLManager.getChromosomeFromDocument(conf, doc);
    assertTrue(chrom.equals(chrom2));
  }

  public void testGetChromosomeFromElement_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = null;
    try {
      XMLManager.getChromosomeFromElement(conf, elem);
      fail();
    }
    catch (ImproperXMLException iex) {
      ;//this is OK
    }
  }

  public void testGetChromosomeFromElement_1() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = doc.getDocumentElement();
    Chromosome chrom2 = XMLManager.getChromosomeFromElement(conf, elem);
    assertTrue(chrom.equals(chrom2));
  }

  public void testGetDocumentElement_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = doc.getDocumentElement();
    XMLManager.getChromosomeFromElement(conf, elem);
    assertEquals(CHROMOSOME_TAG, elem.getTagName());
  }

  public void testGetGenesFromElement_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = doc.getDocumentElement();
    NodeList chromElems = elem.getElementsByTagName("genes");
    Gene[] genes2 = XMLManager.getGenesFromElement(conf, (Element)chromElems.item(0));
    assertEquals(genes.length, genes2.length);
    for(int i=0;i<genes.length;i++) {
      assertTrue(genes[i].equals(genes2[i]));
    }
  }

  public void testGetGenotypeFromDocument_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    try {
      Genotype genotype = XMLManager.getGenotypeFromDocument(conf, doc);
      fail();
    }
    catch (ImproperXMLException iex) {
      ;//this is OK
    }
  }

  public void testGetGenotypeFromDocument_1() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    Genotype genotype = XMLManager.getGenotypeFromDocument(conf, doc);
  }

  public void testGetGenotypeFromElement_0() throws Exception {
    /***@todo implement*/
  }

  public void testRepresentChromosomeAsDocument_0() throws Exception {
    /***@todo implement*/
  }

  public void testRepresentChromosomeAsElement_0() throws Exception {
    /***@todo implement*/
  }

  public void testRepresentGenesAsElement_0() throws Exception {
    /***@todo implement*/
  }

  public void testRepresentGenotypeAsDocument_0() throws Exception {
    /***@todo implement*/
  }

  public void testRepresentGenotypeAsElement_0() throws Exception {
    /***@todo implement*/
  }
}
