/*
 * Copyright 2003 Klaus Meffert
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
package org.jgap.impl;

import java.util.StringTokenizer;
import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

/**
 * Ordered container for multiple genes
 * Has the same interface as a single gene and could be used accordingly.
 * Use the addGene(Gene) method to add single genes (not CompositeGenes!) after
 * construction, an empty CompositeGene without genes makes no sense.

 * Beware that there are two equalities defined for a CompsoiteGene in respect
 * to its contained genes:
 * a) Two genes are (only) equal if they are identical
 * b) Two genes are (seen as) equal if their equals method returns true
 *
 * This influences several methods such as addGene. Notice that it is "better"
 * to use addGene(a_gene, false) than addGene(a_gene, true) because the second
 * variant only allows to add genes not seen as equal to already added genes in
 * respect to their equals function. But: the equals function returns true for
 * two different DoubleGenes (e.g.) just after their creation. If no specific
 * (and hopefully different)  allele is set for these DoubleGenes they are seen
 * as equal!
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class CompositeGene
    implements Gene
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.3 $";

    /**
     * Represents the delimiter that is used to separate genes in the
     * persistent representation of CompositeGene instances.
     */
    public final static String GENE_DELIMITER = "*";

    private Vector genes;

    public CompositeGene ()
    {
        genes = new Vector ();
    }

    public void addGene (Gene a_gene)
    {
        addGene(a_gene, false);
    }
    /**
     * Adds a gene to the CompositeGene's container. See comments in class
     * header for additional details about equality (concerning "strict" param.)
     * @param a_gene the gene to be added
     * @param strict false: add the given gene except the gene itself already is
     *       contained within the CompositeGene's container.
     *       true: add the gene if there is no other gene being equal to the
     *       given gene in request to the Gene's equals method
     * @author Klaus Meffert
     * @since 1.1
     */
    public void addGene (Gene a_gene, boolean strict)
    {
        if (a_gene instanceof CompositeGene)
        {
            throw new IllegalArgumentException ("It is not allowed to add a"
                + " CompositeGene to a CompositeGene!");
        }
        //check if gene already exists
        //----------------------------
        boolean containsGene;
        if (!strict)
        {
            containsGene = containsGeneByIdentity (a_gene);
        }
        else
        {
            containsGene = genes.contains (a_gene);
        }
        if (containsGene)
        {
            throw new IllegalArgumentException ("The gene is already contained"
                + " in the CompositeGene!");
        }
        genes.add (a_gene);
    }

    /**
     * Removes the given gene from the collection of genes. The gene is only
     * removed if an object of the same identity is contained. The equals
     * method will not be used here intentionally
     * @param gene the gene to be removed
     * @return true: given gene found and removed
     * @author Klaus Meffert
     * @since 1.1
     */
    public boolean removeGeneByIdentity (Gene gene)
    {
        boolean result;
        int size = size();
        if (size < 1) {
            result = false;
        }
        else {
            result = false;
            for (int i=0;i<size;i++) {
                if (geneAt(i) == gene) {
                    genes.remove(i);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Removes the given gene from the collection of genes. The gene is
     * removed if another gene exists that is equal to the given gene in respect
     * to the equals method of the gene
     * @param gene the gene to be removed
     * @return true: given gene found and removed
     * @author Klaus Meffert
     * @since 1.1
     */
    public boolean removeGene (Gene gene)
    {
        return genes.remove(gene);
    }

    /**
     * @author Klaus Meffert
     * @since 1.1
     */
    public void cleanup ()
    {
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.cleanup ();
        }
    }

    /**
     *
     * @param randomGenerator
     * @author Klaus Meffert
     * @since 1.1
     */
    public void setToRandomValue (RandomGenerator randomGenerator)
    {
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.setToRandomValue (randomGenerator);
        }
    }

    /**
     *
     * @param a_representation
     * @throws UnsupportedRepresentationException
     * @author Klaus Meffert
     * @since 1.1
     */
    public void setValueFromPersistentRepresentation (String a_representation) throws
        UnsupportedRepresentationException
    {
        if (a_representation != null)
        {
            StringTokenizer tokenizer =
                new StringTokenizer (a_representation,GENE_DELIMITER);

            int numberGenes = tokenizer.countTokens();
            String singleGene;
            String geneTypeClass;
            try
            {
                for (int i = 0; i < numberGenes; i++)
                {
                    singleGene = tokenizer.nextToken ();
                    StringTokenizer geneTypeTokenizer =
                        new StringTokenizer (singleGene,Gene.PERSISTENT_FIELD_DELIMITER);

                    //read type for every gene and then newly construct it
                    //----------------------------------------------------
                    geneTypeClass = geneTypeTokenizer.nextToken ();
                    Class clazz = Class.forName (geneTypeClass);
                    Gene gene = (Gene)clazz.newInstance();

                    //now work with the freshly constructed genes
                    // ------------------------------------------
                    String rep = "";
                    while (geneTypeTokenizer.hasMoreTokens()) {
                        if (rep.length() > 0) {
                            rep += Gene.PERSISTENT_FIELD_DELIMITER;
                        }
                        rep += geneTypeTokenizer.nextToken();
                    }
                    gene.setValueFromPersistentRepresentation(rep);
                    addGene(gene);
                }

            }
            catch (Exception ex)
            {
                throw new UnsupportedRepresentationException (ex.getMessage());
            }

        }
    }

    /**
     *
     * @return
     * @throws UnsupportedOperationException
     * @author Klaus Meffert
     * @since 1.1
     */
    public String getPersistentRepresentation () throws
        UnsupportedOperationException
    {
        String result = "";
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);

            //save type with every gene to make the process reversible
            //--------------------------------------------------------
            result += gene.getClass().getName();
            result += gene.PERSISTENT_FIELD_DELIMITER;

            //get persistent representation from each gene itself
            result += gene.getPersistentRepresentation ();

            if (i < genes.size () - 1)
            {
                result += GENE_DELIMITER;
                /**@todo if GENE_DELIMITER occurs in a gene (e.g. StringGene)
                 * undertake actions to maintain consistency
                 */
            }
        }
        return result;
    }

    /**
     * Retrieves the value represented by this Gene. All values returned
     * by this class will be Vector instances. Each element of the Vector
     * represents the allele of the corresponding gene in the CompositeGene's
     * container
     *
     * @return the Boolean value of this Gene.
     * @author Klaus Meffert
     * @since 1.1
     */
    public Object getAllele ()
    {
        Vector alleles = new Vector ();
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            alleles.add (gene.getAllele ());
        }
        return alleles;
    }

    /**
     * Sets the value of the contained Genes to the new given value. This class
     * expects the value to be of a Vector type. Each element of the Vector
     * must conform with the type of the gene in the CompositeGene's container
     * at the corresponding position.
     *
     * @param a_newValue the new value of this Gene instance.
     *
     * @author Klaus Meffert
     * @since 1.1
     */
    public void setAllele (Object a_newValue)
    {
        if (! (a_newValue instanceof Vector)) {
            throw new IllegalArgumentException("The expected type of the allele"
                +" is a Vector.");
        }
        Vector alleles = (Vector) a_newValue;
        Gene gene;
        for (int i = 0; i < alleles.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.setAllele (alleles.elementAt (i));
        }
    }

    /**
     * Provides an implementation-independent means for creating new Gene
     * instances. The new instance that is created and returned should be
     * setup with any implementation-dependent configuration that this Gene
     * instance is setup with (aside from the actual value, of course). For
     * example, if this Gene were setup with bounds on its value, then the
     * Gene instance returned from this method should also be setup with
     * those same bounds. This is important, as the JGAP core will invoke this
     * method on each Gene in the sample Chromosome in order to create each
     * new Gene in the same respective gene position for a new Chromosome.
     * <p>
     * It should be noted that nothing is guaranteed about the actual value
     * of the returned Gene and it should therefore be considered to be
     * undefined.
     *
     * @param a_activeConfiguration The current active configuration.
     * @return A new Gene instance of the same type and with the same
     *         setup as this concrete Gene.
     *
     * @author Klaus Meffert
     * @since 1.1
     */
    public Gene newGene (Configuration a_activeConfiguration)
    {
        CompositeGene compositeGene = new CompositeGene ();
        Gene gene;
        int geneSize = genes.size ();
        for (int i = 0; i < geneSize; i++)
        {
            gene = (Gene) genes.get (i);
            compositeGene.addGene (gene.newGene (a_activeConfiguration), false);
        }
        return compositeGene;
    }

    /**
     * Compares this CompositeGene with the specified object for order. A
     * false value is considered to be less than a true value. A null value
     * is considered to be less than any non-null value.
     *
     * @param  other the CompositeGene to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this CompositeGene.
     * @author Klaus Meffert
     * @since 1.1
     */
    public int compareTo (Object other)
    {
        CompositeGene otherCompositeGene = (CompositeGene) other;
        // First, if the other gene (or its value) is null, then this is
        // the greater allele. Otherwise, just use the contained genes' compareTo
        // method to perform the comparison.
        // ---------------------------------------------------------------
        if (otherCompositeGene == null)
        {
            return 1;
        }
        else if (otherCompositeGene.isEmpty ())
        {
            // If our value is also null, then we're the same. Otherwise,
            // this is the greater gene.
            // ----------------------------------------------------------
            return isEmpty () ? 0 : 1;
        }
        else
        {
            //compare each gene against each other
            // -----------------------------------
            int numberGenes = Math.min (size (), otherCompositeGene.size ());
            Gene gene1;
            Gene gene2;
            for (int i = 0; i < numberGenes; i++)
            {
                gene1 = geneAt (i);
                gene2 = otherCompositeGene.geneAt (i);
                if (gene1 == null)
                {
                    if (gene2 == null)
                    {
                        continue;
                    }
                    else
                    {
                        return -1;
                    }
                }
                else
                {
                    int result = gene1.compareTo (gene2);
                    if (result != 0)
                    {
                        return result;
                    }
                }
            }
            //if everything is equal until now the CompositeGene with more
            //contained genes wins
            // -----------------------------------------------------------
            if (size () == otherCompositeGene.size ())
            {
                return 0;
            }
            else
            {
                return size () > otherCompositeGene.size () ? 1 : -1;
            }
        }
    }

    /**
     * Compares this CompositeGene with the given object and returns true if
     * the other object is a IntegerGene and has the same value (allele) as
     * this IntegerGene. Otherwise it returns false.
     *
     * @param other the object to compare to this IntegerGene for equality.
     * @return true if this IntegerGene is equal to the given object,
     *         false otherwise.
     * @since 1.1
     */
    public boolean equals (Object other)
    {
        try
        {
            return compareTo (other) == 0;
        }
        catch (ClassCastException e)
        {
            // If the other object isn't an IntegerGene, then we're not
            // equal.
            // ----------------------------------------------------------
            return false;
        }
    }

    /**
     * Retrieves a string representation of this CompositeGene's value that
     * may be useful for display purposes.
     * @return a string representation of this CompositeGene's value. Every
     * contained gene's string representation is delimited by the given
     * delimiter
     * @since 1.1
     */
    public String toString ()
    {
        if (genes.isEmpty ())
        {
            return "null";
        }
        else
        {
            String result = "";
            Gene gene;
            for (int i = 0; i < genes.size (); i++)
            {
                gene = (Gene) genes.get (i);
                result += gene;
                if (i < genes.size () - 1)
                {
                    result += GENE_DELIMITER;
                }
            }
            return result;
        }
    }

    /**
     * @return true: no genes contained, false otherwise
     * @since 1.1
     */
    public boolean isEmpty ()
    {
        return genes.isEmpty () ? true : false;
    }

    /**
     * Returns the gene at the given index
     * @param index sic
     * @return the gene at the given index
     * @author Klaus Meffert
     * @since 1.1
     */
    public Gene geneAt (int index)
    {
        return (Gene) genes.get (index);
    }

    /**
     * @return the number of genes contained
     * @author Klaus Meffert
     * @since 1.1
     */
    public int size ()
    {
        return genes.size ();
    }

    /**
     * Checks whether a specific gene is already contained. The determination
     * will be done by checking for identity and not using the equal method!
     * @param gene the gene under test
     * @return true: the given gene object is contained
     * @author Klaus Meffert
     * @since 1.1
     */
    public boolean containsGeneByIdentity(Gene gene) {
        boolean result;
        int size = size();
        if (size < 1) {
            result = false;
        }
        else {
            result = false;
            for (int i=0;i<size;i++) {

                //check for identity
                //------------------
                if (geneAt(i) == gene) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}