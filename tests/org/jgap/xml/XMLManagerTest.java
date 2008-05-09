/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.xml;

import java.io.*;
import javax.xml.parsers.*;
import org.jgap.*;
import org.jgap.supergenes.*;
import org.jgap.impl.*;
import org.w3c.dom.*;
import junit.framework.*;

/**
 * Tests the XMLManager class.
 *
 * @author Klaus Meffert
 * @since 1.0
 */
public class XMLManagerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.20 $";

  private final static String FILENAME_WRITE = "GAtestWrite.xml";

  public static Test suite() {
    TestSuite suite = new TestSuite(XMLManagerTest.class);
    return suite;
  }

  private Configuration m_conf;

  private Chromosome m_chrom;
  private Chromosome m_supergenechrom;

  private Gene[] m_genes;
  private Gene[] m_supergenes;

  private Genotype m_genotype;

  private String m_chromosome_tag;

  private String m_genes_tag;

  private String m_genotype_tag;

  public void setUp() {
    super.setUp();
    try {
      Configuration.reset();
      m_conf = new DefaultConfiguration();
      m_genes = new IntegerGene[2];
      m_genes[0] = new IntegerGene(conf, 0, 100);
      m_genes[0].setAllele(new Integer(54));
      m_genes[1] = new IntegerGene(conf, 22, 44);
      m_genes[1].setAllele(new Integer(37));
      m_conf.setFitnessFunction(new RandomFitnessFunction());
      m_conf.setPopulationSize(8);
      m_chrom = new Chromosome(conf, m_genes);
      InstantiableSupergeneForTesting gene = new InstantiableSupergeneForTesting(conf, new Gene[]{});
      m_supergenes = new Supergene[]{gene};
      m_supergenechrom = new Chromosome(conf, m_supergenes);

      m_conf.setSampleChromosome(m_chrom);
      m_genotype = new Genotype(m_conf, new Chromosome[] {m_chrom});
      m_chromosome_tag = (String) privateAccessor.getField(XMLManager.class,
          "CHROMOSOME_TAG");
      m_genes_tag = (String) privateAccessor.getField(XMLManager.class,
          "GENES_TAG");
      m_genotype_tag = (String) privateAccessor.getField(XMLManager.class,
          "GENOTYPE_TAG");
    }
    catch (Exception ex) {
      throw new RuntimeException("Error in setUp: " + ex.getMessage());
    }
  }

  public void testGetChromosomeFromDocument_0()
      throws Exception {
    try {
      XMLManager.getChromosomeFromDocument(m_conf, null);
      fail();
    }
    catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  public void testGetChromosomeFromDocument_1()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    Chromosome chrom2 = XMLManager.getChromosomeFromDocument(m_conf, doc);
    assertTrue(m_chrom.equals(chrom2));
  }

  public void testGetChromosomeFromElement_0()
      throws Exception {
    XMLManager.representChromosomeAsDocument(m_chrom);
    Element elem = null;
    try {
      XMLManager.getChromosomeFromElement(m_conf, elem);
      fail();
    }
    catch (ImproperXMLException iex) {
      ; //this is OK
    }
  }

  public void testGetChromosomeFromElement_1()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    Element elem = doc.getDocumentElement();
    Chromosome chrom2 = XMLManager.getChromosomeFromElement(m_conf, elem);
    assertEquals(m_chrom, chrom2);
  }

  public void testGetGenesFromElement_0()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    Element elem = doc.getDocumentElement();
    NodeList chromElems = elem.getElementsByTagName(m_genes_tag);
    Gene[] genes2 = XMLManager.getGenesFromElement(m_conf,
        (Element) chromElems.item(0));
    assertEquals(m_genes.length, genes2.length);
    for (int i = 0; i < m_genes.length; i++) {
      assertEquals(m_genes[i], genes2[i]);
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetGenesFromElement_2()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_supergenechrom);
    Element elem = doc.getDocumentElement();
    NodeList chromElems = elem.getElementsByTagName(m_genes_tag);
    Gene[] genes2 = XMLManager.getGenesFromElement(m_conf,
        (Element) chromElems.item(0));
    assertEquals(m_supergenes.length, genes2.length);
    for (int i = 0; i < m_supergenes.length; i++) {
      assertEquals(m_supergenes[i], genes2[i]);
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetGenesFromElement_1()
      throws Exception {
    try {
      XMLManager.getGenesFromElement(m_conf, null);
      fail();
    }
    catch (ImproperXMLException iex) {
      ; //this is OK
    }
  }

  public void testGetGenotypeFromDocument_0()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    try {
      XMLManager.getGenotypeFromDocument(m_conf, doc);
      fail();
    }
    catch (ImproperXMLException iex) {
      ; //this is OK
    }
  }

  public void testGetGenotypeFromDocument_1()
      throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(m_genotype);
    Genotype genotype2 = XMLManager.getGenotypeFromDocument(m_conf, doc);
    assertTrue(m_genotype.equals(genotype2));
    assertEquals(m_genotype, genotype2);
  }

  public void testGetGenotypeFromElement_0()
      throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(m_genotype);
    Element elem = doc.getDocumentElement();
    Genotype genotype2 = XMLManager.getGenotypeFromElement(m_conf, elem);
    assertEquals(m_genotype, genotype2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testGetGenotypeFromElement_1()
      throws Exception {
    try {
      XMLManager.getGenotypeFromElement(m_conf, null);
      fail();
    }
    catch (ImproperXMLException iex) {
      ; //this is OK
    }
  }

  public void testRepresentChromosomeAsDocument_0()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    Element elem = doc.getDocumentElement();
    assertEquals(m_chromosome_tag, elem.getTagName());
  }

  public void testRepresentChromosomeAsElement_0()
      throws Exception {
    DocumentBuilder docCreator =
        DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = docCreator.newDocument();
    Element elem = XMLManager.representChromosomeAsElement(m_chrom, doc);
    assertEquals(m_chromosome_tag, elem.getTagName());
  }

  public void testRepresentGenesAsElement_0()
      throws Exception {
    Document doc = XMLManager.representChromosomeAsDocument(m_chrom);
    Element elem = XMLManager.representGenesAsElement(m_genes, doc);
    assertEquals(m_genes_tag, elem.getTagName());
  }

  public void testRepresentGenotypeAsDocument_0()
      throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(m_genotype);
    Element elem = doc.getDocumentElement();
    assertEquals(m_genotype_tag, elem.getTagName());
  }

  public void testRepresentGenotypeAsElement_0()
      throws Exception {
    DocumentBuilder docCreator =
        DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = docCreator.newDocument();
    Element elem = XMLManager.representGenotypeAsElement(m_genotype, doc);
    assertEquals(m_genotype_tag, elem.getTagName());
  }

  /**
   * Attention: This test generates a console output with Java 5:
   * "[Fatal Error] : -1:-1: Premature end of file."
   * This is OK, but kinda weird and unwanted.
   *
   * @throws Exception
   */
  public void testReadFile_0()
      throws Exception {
    try {
      XMLManager.readFile(File.createTempFile(FILENAME_WRITE, "tmp"));
      fail();
    }
    catch (Exception ex) {
      ; //this is OK
    }
  }

  public void testReadFile_1()
      throws Exception {
    Document doc = XMLManager.representGenotypeAsDocument(m_genotype);
    File f = File.createTempFile(FILENAME_WRITE, "tmp");
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(m_genotype),
                         f);
    XMLManager.readFile(f);
    Genotype population = XMLManager.getGenotypeFromDocument(m_conf, doc);
    assertEquals(m_genotype, population);
  }

  public void testWriteFile_0()
      throws Exception {
    XMLManager.representGenotypeAsDocument(m_genotype);
    File f = File.createTempFile(FILENAME_WRITE, "tmp");
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(m_genotype),
                         f);
  }

  /**
   * Do the same as above test to verify that overriding existing file works.
   *
   * @throws Exception
   */
  public void testWriteFile_1()
      throws Exception {
    XMLManager.representGenotypeAsDocument(m_genotype);
    File f = File.createTempFile(FILENAME_WRITE, "tmp");
    XMLManager.writeFile(XMLManager.representGenotypeAsDocument(m_genotype),
                         f);
  }

  /**
   * Tests XML capabilities of JGAP. Moved from examples.simpleBoolean.TestXML.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testChromosome_0()
      throws Exception {
    Configuration.resetProperty(Configuration.PROPERTY_SAMPLE_CHROM_INST);
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 8));
    conf.setPopulationSize(10);
    conf.reset();
    conf.setFitnessFunction(new TestFitnessFunction());
    // Test Chromsome manipulation methods.
    // ------------------------------------
    IChromosome chromosome = Chromosome.randomInitialChromosome(conf);
    Document chromosomeDoc =
        XMLManager.representChromosomeAsDocument(chromosome);
    IChromosome chromosomeFromXML =
        XMLManager.getChromosomeFromDocument(conf, chromosomeDoc);
    assertEquals(chromosomeFromXML, chromosome);
  }

  /**
   * Tests XML capabilities of JGAP. Moved from examples.simpleBoolean.TestXML.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGenotype_0()
      throws Exception {
    Configuration.resetProperty(Configuration.PROPERTY_SAMPLE_CHROM_INST);
    conf.setSampleChromosome(new Chromosome(conf, new BooleanGene(conf), 8));
    conf.setPopulationSize(10);
    conf.reset();
    conf.setFitnessFunction(new TestFitnessFunction());
    // Test Genotype manipulation methods.
    // -----------------------------------
    Genotype genotype = Genotype.randomInitialGenotype(conf);
    Document genotypeDoc = XMLManager.representGenotypeAsDocument(genotype);
    Genotype genotypeFromXML =
        XMLManager.getGenotypeFromDocument(conf, genotypeDoc);
    assertEquals(genotypeFromXML, genotype);
  }

}
