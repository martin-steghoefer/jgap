package org.jgap.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.jgap.Chromosome;
import org.jgap.Genotype;

public class XMLManager {

  private static final DocumentBuilder documentCreator;

  static {
    try {
      documentCreator =
         DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException parserError) {
      throw new RuntimeException(
        "XMLManager: Unable to setup DocumentBuilder: " +
        parserError.getMessage());
    }
  }

  private static final Object lock = new Object();

  private XMLManager() {}

  public static Document getChromosomeAsDocument(Chromosome subject) {
    // DocumentBuilders do not have to be thread safe.
    Document chromosomeDocument;
    synchronized(lock) {
      chromosomeDocument = documentCreator.newDocument();
    }

    Element chromosomeElement =
      getChromosomeAsElement(subject, chromosomeDocument);

    chromosomeDocument.appendChild(chromosomeElement);
    return chromosomeDocument;
  }


  public static Document getGenotypeAsDocument(Genotype subject) {
    // DocumentBuilders do not have to be thread safe.
    Document genotypeDocument;
    synchronized(lock) {
      genotypeDocument = documentCreator.newDocument();
    }

    Element genotypeElement =
      getGenotypeAsElement(subject, genotypeDocument);

    genotypeDocument.appendChild(genotypeElement);
    return genotypeDocument;
  }


  public static Element getChromosomeAsElement(Chromosome subject, 
                                               Document xmlDocument) {
    int subjectSize = subject.size();

    Element chromosomeTag = xmlDocument.createElement("chromosome"); 
    chromosomeTag.setAttribute("size", Integer.toString(subjectSize));

    Element genesTag = xmlDocument.createElement("genes");
    genesTag.setAttribute("representation", "binary");

    StringBuffer geneValues = new StringBuffer(subjectSize);
    
    for(int i = 0; i < subjectSize; i++) {
      if (subject.getAllele(i)) {
        geneValues.append('1');
      }
      else {
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
                                             Document xmlDocument) {
    Chromosome[] population = subject.getChromosomes();

    Element genotypeTag = xmlDocument.createElement("genotype");
    genotypeTag.setAttribute("size", Integer.toString(population.length));

    for(int i = 0; i < population.length; i++) {
      Element chromosomeElement =
        getChromosomeAsElement(population[i], xmlDocument);

      genotypeTag.appendChild(chromosomeElement);
    } 

    return genotypeTag;
  }
}

