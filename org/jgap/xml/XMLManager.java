/*
 * Copyright 2001, 2002 Neil Rotstan
 *
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

import org.jgap.Allele;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.UnsupportedRepresentationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;


/**
 * The XMLManager performs conversions from instances of genetic entities
 * (such as Chromosome and Genotype) to XML representations of those entities,
 * and vice versa. All of the methods in this class are static, so no
 * construction is required (or allowed).
 */
public class XMLManager
{
    /**
     * Constant representing the name of genotype XML element tags.
     */
    private static final String GENOTYPE_TAG = "genotype";

    /**
     * Constant representing the name of chromosome XML element tags.
     */
    private static final String CHROMOSOME_TAG = "chromosome";

    /**
     * Constant representing the name of gene XML element tags.
     */
    private static final String GENES_TAG = "genes";

    /**
     * Constant representing the name of the allele XML element tags.
     */
    private static final String ALLELE_TAG = "allele";

    /**
     * Constant representing the name of the size XML attribute that is
     * added to genotype and chromosome elements to describe their size.
     */
    private static final String SIZE_ATTRIBUTE = "size";

    /**
     * Constant representing the fully-qualified name of the concrete
     * Allele class that was used to manage the allele in question.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * Shared DocumentBuilder, which is used to create new DOM Document
     * instances.
     */
    private static final DocumentBuilder m_documentCreator;

    /**
     * Shared m_lock object used for synchronization purposes.
     */
    private static final Object m_lock = new Object();


    static
    {
        try
        {
            m_documentCreator =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch ( ParserConfigurationException parserError )
        {
            throw new RuntimeException(
                "XMLManager: Unable to setup DocumentBuilder: " +
                parserError.getMessage() );
        }
    }


    /**
     * Private constructor. All methods in this class are static, so no
     * construction is allowed.
     */
    private XMLManager()
    {
    }


    /**
     * Create an XML Document representation of a Chromosome instance.
     *
     * @param a_subject The chromosome to represent as an XML document.
     *
     * @return a document representing the given Chromosome.
     */
    public static Document representChromosomeAsDocument( Chromosome a_subject )
    {
        // DocumentBuilders do not have to be thread safe, so we have to
        // protect creation of the Document with a synchronized block.
        // -------------------------------------------------------------
        Document chromosomeDocument;
        synchronized( m_lock )
        {
            chromosomeDocument = m_documentCreator.newDocument();
        }

        Element chromosomeElement =
                representChromosomeAsElement( a_subject, chromosomeDocument );

        chromosomeDocument.appendChild( chromosomeElement );
        return chromosomeDocument;
    }


    /**
     * Create an XML Document representation of a Genotype instance,
     * including its population of Chromosome instances.
     *
     * @param a_subject The genotype to represent as an XML document.
     *
     * @return a Document representing the given Genotype.
     */
    public static Document representGenotypeAsDocument( Genotype a_subject )
    {
        // DocumentBuilders do not have to be thread safe, so we have to
        // protect creation of the Document with a synchronized block.
        // -------------------------------------------------------------
        Document genotypeDocument;
        synchronized( m_lock )
        {
            genotypeDocument = m_documentCreator.newDocument();
        }

        Element genotypeElement =
                representGenotypeAsElement( a_subject, genotypeDocument );

        genotypeDocument.appendChild( genotypeElement );
        return genotypeDocument;
    }


    /**
     * Create an XML Element representation of a set of gene values.
     *
     * @param a_geneValues The alleles to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element representing the given gene values.
     */
    public static Element representGenesAsElement( Allele[] a_geneValues,
                                                   Document a_xmlDocument )
    {
        // Create the parent genes element.
        // --------------------------------
        Element genesElement = a_xmlDocument.createElement( GENES_TAG );

        // Now add allele sub-elements for each allele in the given array.
        // ---------------------------------------------------------------
        Element alleleElement;

        for( int i = 0; i < a_geneValues.length; i++ )
        {
            // Create the allele element for this gene.
            // ----------------------------------------
            alleleElement = a_xmlDocument.createElement( ALLELE_TAG );

            // Add the class attribute and set its value to the class
            // name of the concrete class representing the current Allele.
            // -----------------------------------------------------------
            alleleElement.setAttribute( CLASS_ATTRIBUTE,
                                        a_geneValues[i].getClass().getName() );

            // Create a text node to contain its string representation.
            // --------------------------------------------------------
            Text alleleRepresentation = a_xmlDocument.createTextNode(
                a_geneValues[i].getPersistentRepresentation() );

            // And now add the text node to the allele element, and then
            // add the allele element to the genes element.
            // ---------------------------------------------------------
            alleleElement.appendChild( alleleRepresentation );
            genesElement.appendChild( alleleElement );
        }

        return genesElement;
    }


    /**
     * Create an XML Element representation of a Chromosome instance.
     * This may be useful in scenarios where representation as an
     * entire Document is undesirable, such as when the representation
     * of this Chromosome is to be combined with other elements in a
     * single Document.
     *
     * @param a_subject   The chromosome to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element representing the given Chromosome.
     */
    public static Element representChromosomeAsElement( Chromosome a_subject,
                                                        Document a_xmlDocument )
    {
        // Start by creating an element for the chromosome and its size
        // attribute, which represents the number of genes in the chromosome.
        // ------------------------------------------------------------------
        Element chromosomeElement =
            a_xmlDocument.createElement( CHROMOSOME_TAG );

        chromosomeElement.setAttribute( SIZE_ATTRIBUTE,
                                        Integer.toString( a_subject.size() ) );

        // Next create the genes element with its nested allele elements,
        // which will contain string representations of the alleles.
        // --------------------------------------------------------------
        Element genesElement = representGenesAsElement( a_subject.getGenes(),
                                                        a_xmlDocument );

        // Add the new genes element to the chromosome element and then
        // return the chromosome element.
        // -------------------------------------------------------------
        chromosomeElement.appendChild( genesElement );

        return chromosomeElement;
    }


    /**
     * Create an XML Element representation of a Genotype instance,
     * including its population of Chromosome instances as sub-elements.
     * This may be useful in scenarios where representation as an
     * entire Document is undesirable, such as when the representation
     * of this Genotype is to be combined with other elements in a
     * single Document.
     *
     * @param a_subject The genotype to represent as an XML element.
     * @param a_xmlDocument A Document instance that will be used to create
     *                      the Element instance. Note that the element will
     *                      NOT be added to the document by this method.
     *
     * @return an Element representing the given Genotype.
     */
    public static Element representGenotypeAsElement( Genotype a_subject,
                                                      Document a_xmlDocument )
    {
        Chromosome[] population = a_subject.getChromosomes();

        // Start by creating the genotype element and its size attribute,
        // which represents the number of chromosomes present in the
        // genotype.
        // --------------------------------------------------------------
        Element genotypeTag = a_xmlDocument.createElement( GENOTYPE_TAG );
        genotypeTag.setAttribute( SIZE_ATTRIBUTE,
                Integer.toString( population.length ) );

        // Next, add nested elements for each of the chromosomes in the
        // genotype.
        // ------------------------------------------------------------
        for ( int i = 0; i < population.length; i++ )
        {
            Element chromosomeElement =
                representChromosomeAsElement( population[ i ], a_xmlDocument );

            genotypeTag.appendChild( chromosomeElement );
        }

        return genotypeTag;
    }


    /**
     * Retrieve a Chromosome instance constructed from a given XML Element
     * representation.
     *
     * @param a_xmlElement The XML Element representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *                              structured or missing data.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Allele implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws AlleleCreationException if there is a problem creating or
     *                                 populating an Allele instance.
     */
    public static Allele[] getGenesFromElement(
                               Configuration a_activeConfiguration,
                               Element a_xmlElement )
                           throws ImproperXMLException,
                                  UnsupportedRepresentationException,
                                  AlleleCreationException
    {
        // Do some sanity checking. Make sure the XML Element isn't null and
        // that in fact represents an allele.
        // -----------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( GENES_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "given Element is not a 'chromosome' element." );
        }

        List genes = new ArrayList();

        // Extract the nested allele elements.
        // ---------------------------------------------------------
        NodeList alleles = a_xmlElement.getElementsByTagName( ALLELE_TAG );

        if ( alleles == null )
        {
            throw new ImproperXMLException(
                "Unable to build Allele instances from XML Element: " +
                "'allele' sub-elements not found." );
        }

        // For each allele, get the class attribute so we know what class
        // to instantiate to represent the allele instance, and then find
        // the child text node, which is where the string representation
        // of the allele is located, and extract the representation.
        // --------------------------------------------------------------
        int numberOfAlleleNodes = alleles.getLength();
        for( int i = 0; i < numberOfAlleleNodes; i++ )
        {
            Element thisAlleleElement = (Element) alleles.item( i );
            thisAlleleElement.normalize();

            // Fetch the class attribute and create an instance of that
            // class to manage the current allele.
            // --------------------------------------------------------
            String alleleClassName =
                thisAlleleElement.getAttribute( CLASS_ATTRIBUTE );

            Allele thisAlleleObject;
            try
            {
                // Note that we don't bother trying to get an allele from
                // the AllelePool. We're assuming here that if you're loading
                // stuff from an XML file, then you're just getting started
                // and there won't be anything in the pool to get. This keeps
                // the code here a little bit simpler.
                // ----------------------------------------------------------
                thisAlleleObject =
                    (Allele) Class.forName( alleleClassName ).newInstance();
            }
            catch( Exception e )
            {
                throw new AlleleCreationException( e.getMessage() );
            }

            // Find the text node and fetch the string representation of the
            // current allele.
            // -------------------------------------------------------------
            NodeList children = thisAlleleElement.getChildNodes();
            int childrenSize = children.getLength();
            String alleleRepresentation = null;

            for ( int j = 0; j < childrenSize; j++ )
            {
                if ( children.item( j ).getNodeType() == Node.TEXT_NODE )
                {
                    // We found the text node. Extract the representation.
                    // ------------------------------------------------------
                    alleleRepresentation = children.item( j ).getNodeValue();
                    break;
                }
            }

            // Sanity check: Make sure the representation isn't null.
            // ------------------------------------------------------
            if( alleleRepresentation == null )
            {
                throw new ImproperXMLException(
                    "Unable to build Chromosome instance from XML Element: " +
                    "allele is missing representation." );
            }

            // Now set the value of the allele to that reflect the
            // string representation.
            // ---------------------------------------------------
            try
            {
                thisAlleleObject.setValueFromPersistentRepresentation(
                    alleleRepresentation );
            }
            catch( UnsupportedOperationException e )
            {
                throw new AlleleCreationException(
                    "Unable to build Allele because it does not support the " +
                    "setValueFromPersistentRepresentation() method." );
            }

            // Finally, add the current allele object to the list of genes.
            // ------------------------------------------------------------
            genes.add( thisAlleleObject );
        }

        return (Allele[]) genes.toArray( new Allele[ genes.size() ] );
    }


    /**
     * Retrieve a Chromosome instance constructed from a given XML Element
     * representation.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Chromosome.
     *
     * @param a_xmlElement The XML Element representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *                              structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *                                       an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Allele implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws AlleleCreationException if there is a problem creating or
     *                                 populating an Allele instance.
     */
    public static Chromosome getChromosomeFromElement(
                                 Configuration a_activeConfiguration,
                                 Element a_xmlElement )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   AlleleCreationException
    {
        // Do some sanity checking. Make sure the XML Element isn't null and
        // that in fact represents a chromosome.
        // -----------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( CHROMOSOME_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "given Element is not a 'chromosome' element." );
        }

        // Extract the nested genes element and make sure it exists.
        // ---------------------------------------------------------
        Element genesElement = (Element)
            a_xmlElement.getElementsByTagName( GENES_TAG ).item( 0 );

        if ( genesElement == null )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Element: " +
                "'genes' sub-element not found." );
        }

        // Construct the genes from their representations.
        // -----------------------------------------------
        Allele[] geneAlleles = getGenesFromElement( a_activeConfiguration,
                                                    genesElement );

        // Construct the new Chromosome with the genes and return it.
        // ----------------------------------------------------------
        return new Chromosome( a_activeConfiguration, geneAlleles );
    }


    /**
     * Retrieve a Genotype instance constructed from a given
     * XML Element representation. Its population of Chromosomes
     * will be constructed from the Chromosome sub-elements.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Genotype and Chromosome instances.
     *
     * @param a_xmlElement The XML Element representation of the Genotype
     *
     * @return A new Genotype instance, complete with a population
     *         of Chromosomes, setup with the data from the XML
     *         Element representation.
     *
     * @throws ImproperXMLException if the given Element is improperly
     *                              structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *                                       an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Allele implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws AlleleCreationException if there is a problem creating or
     *                                 populating an Allele instance.
     */
    public static Genotype getGenotypeFromElement( Configuration a_activeConfiguration,
                                                   Element a_xmlElement )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   AlleleCreationException
    {
        // Sanity check. Make sure the XML element isn't null and that it
        // actually represents a genotype.
        // --------------------------------------------------------------
        if ( a_xmlElement == null ||
             !( a_xmlElement.getTagName().equals( GENOTYPE_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Genotype instance from XML Element: " +
                "given Element is not a 'genotype' element." );
        }

        // Fetch all of the nested chromosome elements and convert them
        // into Chromosome instances.
        // ------------------------------------------------------------
        NodeList chromosomes =
            a_xmlElement.getElementsByTagName( CHROMOSOME_TAG );
        int numChromosomes = chromosomes.getLength();

        Chromosome[] population = new Chromosome[ numChromosomes ];

        for ( int i = 0; i < numChromosomes; i++ )
        {
            population[ i ] = getChromosomeFromElement( a_activeConfiguration,
                    (Element) chromosomes.item( i ) );
        }

        // Construct a new Genotype with the chromosomes and return it.
        // ------------------------------------------------------------
        return new Genotype( a_activeConfiguration, population );
    }


    /**
     * Retrieve a Genotype instance constructed from a given
     * XML Document representation. Its population of Chromosomes
     * will be constructed from the Chromosome sub-elements.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Genotype and Chromosome instances.
     *
     * @param a_xmlDocument The XML Document representation of the Genotype.
     *
     * @return A new Genotype instance, complete with a population of
     *         Chromosomes, setup with the data from the XML Document
     *         representation.
     *
     * @throws ImproperXMLException if the given Document is improperly
     *                              structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *                                       an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Allele implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws AlleleCreationException if there is a problem creating or
     *                                 populating an Allele instance.
     */
    public static Genotype getGenotypeFromDocument(
                               Configuration a_activeConfiguration,
                               Document a_xmlDocument )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   AlleleCreationException
    {
        // Extract the root element, which should be a genotype element.
        // After verifying that the root element is not null and that it
        // in fact is a genotype element, then convert it into a Genotype
        // instance.
        // --------------------------------------------------------------
        Element rootElement = a_xmlDocument.getDocumentElement();

        if ( rootElement == null ||
             !( rootElement.getTagName().equals( GENOTYPE_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Genotype from XML Document: " +
                "'genotype' element must be at root of document." );
        }

        return getGenotypeFromElement( a_activeConfiguration, rootElement );
    }


    /**
     * Retrieve a Chromosome instance constructed from a given
     * XML Document representation.
     *
     * @param a_activeConfiguration The current active Configuration object
     *                              that is to be used during construction of
     *                              the Chromosome instances.
     *
     * @param a_xmlDocument The XML Document representation of the Chromosome.
     *
     * @return A new Chromosome instance setup with the data from the XML
     *         Document representation.
     *
     * @throws ImproperXMLException if the given Document is improperly
     *                              structured or missing data.
     * @throws InvalidConfigurationException if the given Configuration is in
     *                                       an inconsistent state.
     * @throws UnsupportedRepresentationException if the actively configured
     *         Allele implementation does not support the string representation
     *         of the alleles used in the given XML document.
     * @throws AlleleCreationException if there is a problem creating or
     *                                 populating an Allele instance.
     */
    public static Chromosome getChromosomeFromDocument( Configuration a_activeConfiguration,
                                                        Document a_xmlDocument )
            throws ImproperXMLException,
                   InvalidConfigurationException,
                   UnsupportedRepresentationException,
                   AlleleCreationException
    {
        // Extract the root element, which should be a chromosome element.
        // After verifying that the root element is not null and that it
        // in fact is a chromosome element, then convert it into a Chromosome
        // instance.
        // ------------------------------------------------------------------
        Element rootElement = a_xmlDocument.getDocumentElement();
        if ( rootElement == null ||
             !( rootElement.getTagName().equals( CHROMOSOME_TAG ) ) )
        {
            throw new ImproperXMLException(
                "Unable to build Chromosome instance from XML Document: " +
                "'chromosome' element must be at root of Document." );
        }

        return getChromosomeFromElement( a_activeConfiguration, rootElement );
    }
}

