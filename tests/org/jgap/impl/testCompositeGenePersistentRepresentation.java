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
package org.jgap.impl;

import org.jgap.*;
import java.util.List;

/** Test persistent representation of the CompositeGene. */
public class testCompositeGenePersistentRepresentation {

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

        nn1.setAllele(new Integer(22+seed));
        nn2.setAllele(new Double(44+seed));

        nested2.addGene(nn1);
        nested2.addGene(nn2);

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