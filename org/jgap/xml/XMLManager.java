package org.jgap.xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.BitSet;
import org.jgap.*;


public class XMLManager 
{
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

  private XMLManager() {}

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


  public static Element getChromosomeAsElement(Chromosome subject, 
                                               Document xmlDocument)
  {
    int subjectSize = subject.size();

    Element chromosomeTag = xmlDocument.createElement("chromosome"); 
    chromosomeTag.setAttribute("size", Integer.toString(subjectSize));

    Element genesTag = xmlDocument.createElement("genes");
    genesTag.setAttribute("representation", "binary");

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


  public static Element getGenotypeAsElement(Genotype subject,
                                             Document xmlDocument)
  {
    Chromosome[] population = subject.getChromosomes();

    Element genotypeTag = xmlDocument.createElement("genotype");
    genotypeTag.setAttribute("size", Integer.toString(population.length));

    for(int i = 0; i < population.length; i++)
    {
      Element chromosomeElement =
        getChromosomeAsElement(population[i], xmlDocument);

      genotypeTag.appendChild(chromosomeElement);
    } 

    return genotypeTag;
  }


  public static Chromosome getChromosomeFromElement(Configuration gaConf,
                                                    Element xmlElement)
                           throws InvalidConfigurationException
  {
    // Find the text node, which is the representation of the genes.
    Node genes = xmlElement.getElementsByTagName("genes").item(0);
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
      throw new RuntimeException("<genes> element not found!");
    }

    int genesLength = geneValues.length();
    BitSet geneBits = new BitSet(genesLength);
    for(int i = 0; i < genesLength; i++)
    {
      if (geneValues.charAt(i) == '1')
      {
        geneBits.set(i);
      }
      else
      {
        geneBits.clear(i);
      }
    }

    return new Chromosome(gaConf, geneBits);
  }


  public static Genotype getGenotypeFromElement(Configuration gaConf,
                                                Element xmlElement)
                         throws InvalidConfigurationException
  {
    NodeList chromosomes = xmlElement.getElementsByTagName("chromosome");
    int numChromosomes = chromosomes.getLength();

    Chromosome[] population = new Chromosome[numChromosomes];

    for (int i = 0; i < numChromosomes; i++)
    {
      population[i] = getChromosomeFromElement(gaConf,
                                               (Element) chromosomes.item(i));
    }

    return new Genotype(gaConf, population);
  }


  public static Genotype getGenotypeFromDocument(Configuration gaConf,
                                                 Document xmlDocument)
                         throws InvalidConfigurationException
  {
    return getGenotypeFromElement(gaConf, xmlDocument.getDocumentElement());
  }


  public static Chromosome getChromosomeFromDocument(Configuration gaConf,
                                                     Document xmlDocument)
                           throws InvalidConfigurationException
  {
    return getChromosomeFromElement(gaConf, xmlDocument.getDocumentElement());
  }
}

