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
 * Container for multiple genes
 * Has the same interface as a single gene and could be used accordingly.
 * Use the addGene(Gene) method to add single genes (not CompositeGenes!) after
 * construction, an empty CompositeGene without genes makes no sense</p>
 */

public class CompositeGene
    implements Gene
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.1 $";

    /**
     * Represents the delimiter that is used to separate fields in the
     * persistent representation of IntegerGene instances.
     */
    protected final static String PERSISTENT_FIELD_DELIMITER = "*";

    public final static String GENE_DELIMITER = ":";

    private Vector genes;

    public CompositeGene ()
    {
        genes = new Vector ();
    }

    public void addGene (Gene gene)
    {
        if (gene instanceof CompositeGene)
        {
            throw new IllegalArgumentException (
                "It is not allowed to add a CompositeGene"
                + " to a CompositeGene!");
        }
        /**@todo check if gene already exists*/

        genes.add (gene);
    }

    public boolean removeGene (Gene gene)
    {
        return genes.remove (gene);
    }

    public void cleanup ()
    {
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.cleanup ();
        }
    }

    public void setToRandomValue (RandomGenerator randomGenerator)
    {
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.setToRandomValue (randomGenerator);
        }
    }

    public void setValueFromPersistentRepresentation (String a_representation) throws
        UnsupportedRepresentationException
    {
        if (a_representation != null)
        {
            StringTokenizer tokenizer =
                new StringTokenizer (a_representation,
                                     PERSISTENT_FIELD_DELIMITER);
            Gene gene;
            String single_representation;
            /**@todo read type for every gene and then newly construct it*/

            //now work with the freshly constructed genes
            for (int i = 0; i < genes.size (); i++)
            {
                gene = (Gene) genes.get (i);
                single_representation = tokenizer.nextToken ();
                gene.setValueFromPersistentRepresentation (
                    single_representation);
            }
        }
    }

    public String getPersistentRepresentation () throws
        UnsupportedOperationException
    {
        String result = "";
        Gene gene;
        for (int i = 0; i < genes.size (); i++)
        {
            gene = (Gene) genes.get (i);
            result += gene.getPersistentRepresentation ();
            if (i < genes.size () - 1)
            {
                result += PERSISTENT_FIELD_DELIMITER;
                    /**@todo save type with every gene to make the process reversible*/
            }
        }
        return result;
    }

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

    public void setAllele (Object object)
    {
        Vector alleles = (Vector) object;
        Gene gene;
        for (int i = 0; i < alleles.size (); i++)
        {
            gene = (Gene) genes.get (i);
            gene.setAllele (alleles.elementAt (i));
        }
    }

    public Gene newGene (Configuration configuration)
    {
        CompositeGene compositeGene = new CompositeGene ();
        Gene gene;
        int geneSize = genes.size ();
        for (int i = 0; i < geneSize; i++)
        {
            gene = (Gene) genes.get (i);
            compositeGene.addGene (gene.newGene (configuration));
        }
        return compositeGene;
    }

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
     *
         * @return a string representation of this CompositeGene's value. Every contained
     * gene's string representation is delimited by the given delimiter
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
     */
    public Gene geneAt (int index)
    {
        return (Gene) genes.get (index);
    }

    /**
     * @return the number of genes contained
     * @author Klaus Meffert
     */
    public int size ()
    {
        return genes.size ();
    }
}