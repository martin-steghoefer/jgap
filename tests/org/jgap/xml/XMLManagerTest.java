/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.xml;

import java.io.*;
import javax.xml.parsers.*;

import org.jgap.*;
import org.jgap.impl.*;
import org.w3c.dom.*;

import junit.framework.*;
import junitx.util.*;

/**
 * Tests for XMLManager class
 *
 * @author Klaus Meffert
 * @since 1.0
 */
public class XMLManagerTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private final static String FILENAME_WRITE = "GAtestWrite.xml";

  public static Test suite() {
    TestSuite suite = new TestSuite(XMLManagerTest.class);
    return suite;
  }

  private Configuration conf;
  private Chromosome chrom;
  private Gene[] genes;
  private Genotype genotype;
  private String CHROMOSOME_TAG;
  private String GENES_TAG;
  private String GENOTYPE_TAG;

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

    // delete file perhaps created during test
    new File(FILENAME_WRITE).delete();

    CHROMOSOME_TAG = (String) PrivateAccessor.getField(XMLManager.class,
        "CHROMOSOME_TAG");
    GENES_TAG = (String) PrivateAccessor.getField(XMLManager.class,
        "GENES_TAG");
    GENOTYPE_TAG = (String) PrivateAccessor.getField(XMLManager.class,
        "GENOTYPE_TAG");
  }

  public void testGetChromosomeFromDocument_0() throws Exception {
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
    XMLManager.representChromosomeAsDocument(chrom);
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
    assertEquals(chrom, chrom2);
  }

  public void testGetGenesFromElement_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = doc.getDocumentElement();
    NodeList chromElems = elem.getElementsByTagName(GENES_TAG);
    Gene[] genes2 = XMLManager.getGenesFromElement(conf, (Element)chromElems.item(0));
    assertEquals(genes.length, genes2.length);
    for(int i=0;i<genes.length;i++) {
      assertEquals(genes[i], genes2[i]);
    }
  }

  public void testGetGenotypeFromDocument_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    try {
      XMLManager.getGenotypeFromDocument(conf, doc);
      fail();
    }
    catch (ImproperXMLException iex) {
      ;//this is OK
    }
  }

  public void testGetGenotypeFromDocument_1() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    Genotype genotype2 = XMLManager.getGenotypeFromDocument(conf, doc);
    assertTrue(genotype.equals(genotype2));
    assertEquals(genotype, genotype2);
  }

  public void testGetGenotypeFromElement_0() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    Element elem = doc.getDocumentElement();
    Genotype genotype2 = XMLManager.getGenotypeFromElement(conf, elem);
    assertEquals(genotype, genotype2);
  }

  public void testRepresentChromosomeAsDocument_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem = doc.getDocumentElement();
    assertEquals(CHROMOSOME_TAG, elem.getTagName());
  }

  public void testRepresentChromosomeAsElement_0() throws Exception {
    DocumentBuilder docCreator =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = docCreator.newDocument();
    Element elem = XMLManager.representChromosomeAsElement(chrom, doc);
    assertEquals(CHROMOSOME_TAG, elem.getTagName());
  }

  public void testRepresentGenesAsElement_0() throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(chrom);
    Element elem  = XMLManager.representGenesAsElement(genes, doc);
    assertEquals(GENES_TAG, elem.getTagName());
  }

  public void testRepresentGenotypeAsDocument_0() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    Element elem = doc.getDocumentElement();
    assertEquals(GENOTYPE_TAG, elem.getTagName());
  }

  public void testRepresentGenotypeAsElement_0() throws Exception {
    DocumentBuilder docCreator =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = docCreator.newDocument();
    Element elem  = XMLManager.representGenotypeAsElement(genotype, doc);
    assertEquals(GENOTYPE_TAG, elem.getTagName());
  }

  public void testReadFile_0() throws Exception {
    new File(FILENAME_WRITE).delete();
    try {
      XMLManager.readFile(new File(FILENAME_WRITE));
      fail();
    } catch (Exception ex) {
      ;//this is OK
    }
  }

  public void testReadFile_1() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(genotype),
                         new File(FILENAME_WRITE));
    XMLManager.readFile(new File(FILENAME_WRITE));
    Genotype population = XMLManager.getGenotypeFromDocument(conf, doc);
    assertEquals(genotype, population);
  }

  public void testWriteFile_0() throws Exception {
    XMLManager.representGenotypeAsDocument(genotype);
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(genotype),
                         new File(FILENAME_WRITE));
  }

  /**
   * Do the same as above test to verify that overriding existin file works
   * @throws Exception
   */
  public void testWriteFile_1() throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(genotype);
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(genotype),
                         new File(FILENAME_WRITE));
  }

}
