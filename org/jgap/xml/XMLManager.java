package org.jgap.xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.BitSet;
import org.jgap.*;

/**
 * The XMLManager performs conversions from instances of genetic
 * entities (such as Chromosome and Genotype) to XML representations
 * of those entities, and vice versa. All of the methods in this
 * class are static, so no construction is required (or allowed).
 */
public class XMLManager 
{
  private static final String CHROMOSOME_TAG = "chromosome";
  private static final String GENOTYPE_TAG = "genotype";
  private static final String GENES_TAG = "genes";
  private static final String SIZE_ATTRIBUTE = "size"; 
  private static final String REPRESENTATION_ATTRIBUTE = "representation";
  private static final String BINARY_VALUE = "binary";

  private static final DocumentBuilder documentCreator;

  static 
  {
    try 
    {
      documentCreator =
         DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException parserError) 
    {
      throw new RuntimeException(
        "XMLManager: Unable to setup DocumentBuilder: " +
        parserError.getMessage());
    }
  }

  private static final Object lock = new Object();

  /**
   * Constructor. All methods in this class are static, so no
   * construction is allowed.
   */
  private XMLManager() {}


  /**
   * Retrieve an XML Document representation of a Chromosome instance.
   *
   * @param subject   The chromosome to represent as an XML document.
   *
   * @return a document representing the given Chromosome.
   */
  public static Document getChromosomeAsDocument(Chromosome subject) 
  {
    // DocumentBuilders do not have to be thread safe.
    Document chromosomeDocument;
    synchronized(lock) 
    {
      chromosomeDocument = documentCreator.newDocument();
    }

    Element chromosomeElement =
      getChromosomeAsElement(subject, chromosomeDocument);

    chromosomeDocument.appendChild(chromosomeElement);
    return chromosomeDocument;
  }


  /**
   * Retrieve an XML Document representation of a Genotype instance,
   * including its population of Chromosome instances.
   *
   * @param subject   The genotype to represent as an XML document.
   *
   * @return a Document representing the given Genotype.
   */
  public static Document getGenotypeAsDocument(Genotype subject)
  {
    // DocumentBuilders do not have to be thread safe.
    Document genotypeDocument;
    synchronized(lock)
    {
      genotypeDocument = documentCreator.newDocument();
    }

    Element genotypeElement =
      getGenotypeAsElement(subject, genotypeDocument);

    genotypeDocument.appendChild(genotypeElement);
    return genotypeDocument;
  }


  /**
   * Retrieve an XML Element representation of a Chromosome instance.
   * This may be useful in scenarios where representation as an
   * entire Document is undesirable, such as when the representation
   * of this Chromosome is to be combined with other elements in a
   * single Document.
   *
   * @param subject   The chromosome to represent as an XML element.
   * @param xmlDocument A Document instance that will be used to
   *                    create the Element instance. Note that the
   *                    element will NOT be added to the document.
   *
   * @return an Element representing the given Chromosome.
   */
  public static Element getChromosomeAsElement(Chromosome subject, 
                                               Document xmlDocument)
  {
    int subjectSize = subject.size();

    Element chromosomeTag = xmlDocument.createElement(CHROMOSOME_TAG); 
    chromosomeTag.setAttribute(SIZE_ATTRIBUTE, 
                               Integer.toString(subjectSize));

    Element genesTag = xmlDocument.createElement(GENES_TAG);
    genesTag.setAttribute(REPRESENTATION_ATTRIBUTE, BINARY_VALUE);

    StringBuffer geneValues = new StringBuffer(subjectSize);
    
    for(int i = 0; i < subjectSize; i++)
    {
      if (subject.getAllele(i))
      {
        geneValues.append('1');
      }
      else
      {
        geneValues.append('0');
      }
    }

    Text geneValuesText = 
      xmlDocument.createTextNode(geneValues.toString());

    genesTag.appendChild(geneValuesText);
    chromosomeTag.appendChild(genesTag);

    return chromosomeTag;    
  }


  /**
   * Retrieve an XML Element representation of a Genotype instance,
   * including its population of Chromosome instances as sub-elements.
   * This may be useful in scenarios where representation as an
   * entire Document is undesirable, such as when the representation
   * of this Genotype is to be combined with other elements in a
   * single Document.
   *
   * @param subject     The chromosome to represent as an XML element.
   * @param xmlDocument A Document instance that will be used to
   *                    create the Element instance. Note that the
   *                    element will NOT be added to the document.
   *
   * @return an Element representing the given Genotype.
   */
  public static Element getGenotypeAsElement(Genotype subject,
                                             Document xmlDocument)
  {
    Chromosome[] population = subject.getChromosomes();

    Element genotypeTag = xmlDocument.createElement(GENOTYPE_TAG);
    genotypeTag.setAttribute(SIZE_ATTRIBUTE, 
                             Integer.toString(population.length));

    for(int i = 0; i < population.length; i++)
    {
      Element chromosomeElement =
        getChromosomeAsElement(population[i], xmlDocument);

      genotypeTag.appendChild(chromosomeElement);
    } 

    return genotypeTag;
  }


  /**
   * Retrieve a Chromosome instance constructed from a given
   * XML Element representation.
   *
   * @param gaConf   The Configuration object that will be passed
   *                 to the Chromosome during construction.
   *
   * @param xmlElement The XML Element representation of the
   *                   Chromosome.
   *
   * @return A new Chromosome instance setup with the data
   *         from the XML Element representation.
   *
   * @throws ImproperXMLException if the given Element is improperly
   *                              structured or missing data.
   * @throws InvalidConfigurationException if the given Configuration is
   *                                       in an inconsistent state.
   */
  public static Chromosome getChromosomeFromElement(Configuration gaConf,
                                                    Element xmlElement)
                           throws ImproperXMLException,
                                  InvalidConfigurationException
  {
    if (xmlElement == null ||
        !(xmlElement.getTagName().equals(CHROMOSOME_TAG)))
    {
      throw new ImproperXMLException(
        "Unable to build Chromosome instance from XML Element: " +
        "given Element is not a 'chromosome' element.");
    }

    Node genes = xmlElement.getElementsByTagName(GENES_TAG).item(0);

    if (genes == null)
    {
      throw new ImproperXMLException(
        "Unable to build Chromosome instance from XML Element: " +
        "'genes' sub-element not found.");
    }

    genes.normalize();
    NodeList children = genes.getChildNodes();
    int childrenSize = children.getLength();
    String geneValues = null;

    for (int i = 0; i < childrenSize; i++)
    {
      if (children.item(i).getNodeType() == Node.TEXT_NODE)
      {
        geneValues = children.item(i).getNodeValue();
        break;
      }
    }

    if (geneValues == null)
    {
      throw new ImproperXMLException(
        "Unable to build Chromosome instance from XML Element: " +
        "no gene values found.");
    }

    int genesLength = geneValues.length();
    BitSet geneBits = new BitSet(genesLength);

    for(int i = 0; i < genesLength; i++)
    {
      if (geneValues.charAt(i) == '1')
      {
        geneBits.set(i);
      }
      else if (geneValues.charAt(i) == '0')
      {
        geneBits.clear(i);
      }
      else
      {
        throw new ImproperXMLException(
          "Unable to build Chromosome instance from XML Element: " +
          "gene value '" + geneValues.charAt(i) + "' is invalid.");
      }
    }

    return new Chromosome(gaConf, geneBits);
  }


  /**
   * Retrieve a Genotype instance constructed from a given
   * XML Element representation. Its population of Chromosomes
   * will be constructed from the Chromosome sub-elements.
   *
   * @param gaConf   The Configuration object that will be passed
   *                 to the Genotype and Chromosomes during construction.
   *
   * @param xmlElement The XML Element representation of the
   *                   Genotype.
   *
   * @return A new Genotype instance, complete with a population
   *         of Chromosomes, setup with the data from the XML
   *         Element representation.
   *
   * @throws ImproperXMLException if the given Element is improperly
   *                              structured or missing data.
   * @throws InvalidConfigurationException if the given Configuration is
   *                                       in an inconsistent state.
   */
  public static Genotype getGenotypeFromElement(Configuration gaConf,
                                                Element xmlElement)
                         throws ImproperXMLException,
                                InvalidConfigurationException
  {
    if (xmlElement == null ||
        !(xmlElement.getTagName().equals(GENOTYPE_TAG)))
    {
      throw new ImproperXMLException(
        "Unable to build Genotype instance from XML Element: " +
        "given Element is not a 'genotype' element.");
    }

    NodeList chromosomes = xmlElement.getElementsByTagName(CHROMOSOME_TAG);
    int numChromosomes = chromosomes.getLength();

    Chromosome[] population = new Chromosome[numChromosomes];

    for (int i = 0; i < numChromosomes; i++)
    {
      population[i] = getChromosomeFromElement(gaConf,
                                               (Element) chromosomes.item(i));
    }

    return new Genotype(gaConf, population);
  }


  /**
   * Retrieve a Genotype instance constructed from a given
   * XML Document representation. Its population of Chromosomes
   * will be constructed from the Chromosome sub-elements.
   *
   * @param gaConf   The Configuration object that will be passed
   *                 to the Genotype and Chromosomes during construction.
   *
   * @param xmlDocument The XML Document representation of the
   *                    Genotype.
   *
   * @return A new Genotype instance, complete with a population
   *         of Chromosomes, setup with the data from the XML
   *         Document representation.
   *
   * @throws ImproperXMLException if the given Document is improperly
   *                              structured or missing data.
   * @throws InvalidConfigurationException if the given Configuration is
   *                                       in an inconsistent state.
   */
  public static Genotype getGenotypeFromDocument(Configuration gaConf,
                                                 Document xmlDocument)
                         throws ImproperXMLException,
                                 InvalidConfigurationException
  {
    Element rootElement = xmlDocument.getDocumentElement();

    if (rootElement == null || 
        !(rootElement.getTagName().equals(GENOTYPE_TAG)))
    {
      throw new ImproperXMLException(
        "Unable to build Genotype from XML Document: " +
        "'genotype' element must be at root of document.");
    }

    return getGenotypeFromElement(gaConf, rootElement);
  }


  /**
   * Retrieve a Chromosome instance constructed from a given
   * XML Document representation.
   *
   * @param gaConf   The Configuration object that will be passed
   *                 to the Chromosome during construction.
   *
   * @param xmlDocument The XML Document representation of the
   *                    Chromosome.
   *
   * @return A new Chromosome instance setup with the data
   *         from the XML Document representation.
   *
   * @throws ImproperXMLException if the given Document is improperly
   *                              structured or missing data.
   * @throws InvalidConfigurationException if the given Configuration is
   *                                       in an inconsistent state.
   */
  public static Chromosome getChromosomeFromDocument(Configuration gaConf,
                                                     Document xmlDocument)
                           throws ImproperXMLException,
                                  InvalidConfigurationException
  {
    Element rootElement = xmlDocument.getDocumentElement();
    if (rootElement == null || 
        !(rootElement.getTagName().equals(CHROMOSOME_TAG)))
    {
      throw new ImproperXMLException(
        "Unable to build Chromosome instance from XML Document: " +
        "'chromosome' element must be at root of Document.");
    }

    return getChromosomeFromElement(gaConf, rootElement);
  }
}

