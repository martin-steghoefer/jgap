/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;
import java.util.List;

/**
* Test persistent representation of the CompositeGene.
*
* @author Audrius Meskauskas
* @since 2.1
*/
public class TestCompositeGenePersistentRepresentation {

    public static boolean testRepresentation()
     {
        System.out.println("TEST PERSISTENT REPRESENTATION");
        try {

            CompositeGene gene = createSampleNestedGene(5);

            String representation =
                gene.getPersistentRepresentation ();

            System.out.println ("Old representation: " + representation);

            CompositeGene restored = new CompositeGene ();
            restored.setValueFromPersistentRepresentation (representation);

            System.out.println ("New representation: " +
                                restored.getPersistentRepresentation ());

            System.out.println("Old gene "+gene);
            System.out.println("New gene "+restored);

            System.out.println("TEST GET/SET ALLELE");

            Object allele = gene.getAllele();
            System.out.println("Allele size "+ ( (List) allele).size());

            Gene other = createSampleNestedGene(55);
            System.out.println("Other gene "+other);
            Gene nGene = other.newGene();
            nGene.setAllele(allele);

            System.out.println("After transferring allele: "+nGene);

            return gene.equals (restored);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
     };

    private static CompositeGene createSampleNestedGene (int seed) {
        CompositeGene gene = new CompositeGene ();

        Gene i1 = new IntegerGene ();
        Gene i2 = new DoubleGene  ();

        i1.setAllele(new Integer(seed));
        i2.setAllele(new Double(seed+0.1));

        gene.addGene (i1);
        gene.addGene (i2);

        CompositeGene nested = new CompositeGene();

        Gene n1 = new IntegerGene ();
        Gene n2 = new DoubleGene  ();

        n1.setAllele(new Integer(10+seed));
        n2.setAllele(new Double(1));

        nested.addGene(n1);
        nested.addGene(n2);

        gene.addGene(nested);

        CompositeGene nested2 = new CompositeGene();

        Gene nn1 = new IntegerGene (1, 1000);
        Gene nn2 = new DoubleGene  (0, 1000);
        Gene nn3 = new StringGene(1, 10,StringGene.ALPHABET_CHARACTERS_UPPER+CompositeGene.GENE_DELIMITER);

        nn1.setAllele(new Integer(22+seed));
        nn2.setAllele(new Double(44+seed));
        nn3.setAllele("ABCCBA"+CompositeGene.GENE_DELIMITER);

        nested2.addGene(nn1);
        nested2.addGene(nn2);
        nested2.addGene(nn3);

        gene.addGene(nested2);

        CompositeGene nested3 = new CompositeGene();

        Gene nnn1 = new IntegerGene (1, 1000);
        Gene nnn2 = new DoubleGene  (0, 1000);

        nnn1.setAllele(new Integer(555+seed));
        nnn2.setAllele(new Double(777+seed));

        nested3.addGene(nnn1);
        nested3.addGene(nnn2);

        nested2.addGene(nested3);
        return gene;
    }

     public static void main(String[] args) {
         System.out.println(
          testRepresentation()
          );
     }

}
