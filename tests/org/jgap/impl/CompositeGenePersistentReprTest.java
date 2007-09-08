/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Test persistent representation of the CompositeGene.
 *
 * @author Audrius Meskauskas
 * @author Klaus Meffert
 * @since 2.1
 */
public class CompositeGenePersistentReprTest extends JGAPTestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CompositeGenePersistentReprTest.class);
    return suite;
  }

  public void testRepresentation() throws Exception {
      CompositeGene gene = createSampleNestedGene(5);
      String representation = gene.getPersistentRepresentation();
//      System.out.println("Old representation: " + representation);
      CompositeGene restored = new CompositeGene(conf);
      restored.setValueFromPersistentRepresentation(representation);
//      System.out.println("New representation: "
//                         + restored.getPersistentRepresentation());
      Object allele = gene.getAllele();
      Gene other = createSampleNestedGene(55);
      Gene nGene = other.newGene();
      nGene.setAllele(allele);
      assertEquals(restored, gene);
  }

  private CompositeGene createSampleNestedGene(int a_seed) throws Exception {
    CompositeGene gene = new CompositeGene(conf);
    Gene i1 = new IntegerGene(conf);
    Gene i2 = new DoubleGene(conf);
    i1.setAllele(new Integer(a_seed));
    i2.setAllele(new Double(a_seed + 0.1));
    gene.addGene(i1);
    gene.addGene(i2);
    CompositeGene nested = new CompositeGene(conf);
    Gene n1 = new IntegerGene(conf);
    Gene n2 = new DoubleGene(conf);
    n1.setAllele(new Integer(10 + a_seed));
    n2.setAllele(new Double(1));
    nested.addGene(n1);
    nested.addGene(n2);
    gene.addGene(nested);
    CompositeGene nested2 = new CompositeGene(conf);
    Gene nn1 = new IntegerGene(conf, 1, 1000);
    Gene nn2 = new DoubleGene(conf, 0, 1000);
    Gene nn3 = new StringGene(conf, 1, 10,
                              StringGene.ALPHABET_CHARACTERS_UPPER
                              + CompositeGene.GENE_DELIMITER);
    nn1.setAllele(new Integer(22 + a_seed));
    nn2.setAllele(new Double(44 + a_seed));
    nn3.setAllele("ABCCBA" + CompositeGene.GENE_DELIMITER);
    nested2.addGene(nn1);
    nested2.addGene(nn2);
    nested2.addGene(nn3);
    gene.addGene(nested2);
    CompositeGene nested3 = new CompositeGene(conf);
    Gene nnn1 = new IntegerGene(conf, 1, 1000);
    Gene nnn2 = new DoubleGene(conf, 0, 1000);
    nnn1.setAllele(new Integer(555 + a_seed));
    nnn2.setAllele(new Double(777 + a_seed));
    nested3.addGene(nnn1);
    nested3.addGene(nnn2);
    nested2.addGene(nested3);
    return gene;
  }

}
