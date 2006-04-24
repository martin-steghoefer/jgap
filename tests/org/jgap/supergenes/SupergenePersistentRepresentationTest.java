/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Test persistent representation of the AbstractSupergene.
 *
 * @author Meskauskas Audrius
 * @since 2.0
 */
public class SupergenePersistentRepresentationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public static Test suite() {
    TestSuite suite =
        new TestSuite(SupergenePersistentRepresentationTest.class);
    return suite;
  }

  public static class InstantiableSupergene
      extends AbstractSupergene {
    public InstantiableSupergene(final Configuration a_config)
        throws InvalidConfigurationException {
      super(a_config, null);
    }

    public InstantiableSupergene()
        throws InvalidConfigurationException {
      super();
    }

    public boolean isValid(Gene[] a_gene) {
      return true;
    };
  }
  public void testRepresentation()
      throws Exception {
    InstantiableSupergene gene = new InstantiableSupergene(conf);
    Gene i1 = new IntegerGene(conf, 1, 12);
    Gene i2 = new DoubleGene(conf, 3, 4);
    i1.setAllele(new Integer(7));
    i2.setAllele(new Double(3.2));
    gene.addGene(i1);
    gene.addGene(i2);
    InstantiableSupergene nested = new InstantiableSupergene(conf);
    Gene n1 = new IntegerGene(conf, 1, 12);
    Gene n2 = new DoubleGene(conf, 3, 4);
    n1.setAllele(new Integer(5));
    n2.setAllele(new Double(3.6));
    nested.addGene(n1);
    nested.addGene(n2);
    gene.addGene(nested);
    InstantiableSupergene nested2 = new InstantiableSupergene(conf);
    nested2.setValidator(new TestValidator(conf));
    Gene nn1 = new IntegerGene(conf, 1, 1000);
    Gene nn2 = new DoubleGene(conf, 0, 1000);
    nn1.setAllele(new Integer(22));
    nn2.setAllele(new Double(44));
    nested2.addGene(nn1);
    nested2.addGene(nn2);
    gene.addGene(nested2);
    InstantiableSupergene nested3 = new InstantiableSupergene(conf);
    nested3.setValidator(null);
    Gene nnn1 = new IntegerGene(conf, 1, 1000);
    Gene nnn2 = new DoubleGene(conf, 0, 1000);
    nnn1.setAllele(new Integer(555));
    nnn2.setAllele(new Double(777));
    nested3.addGene(nnn1);
    nested3.addGene(nnn2);
    nested2.addGene(nested3);
    String representation =
        gene.getPersistentRepresentation();
    InstantiableSupergene restored = new InstantiableSupergene(conf);
    restored.setValueFromPersistentRepresentation(representation);
    assertTrue(gene.equals(restored));
  }

  public class TestValidator
      extends Validator {
    public TestValidator(final Configuration a_conf) {
      super(a_conf);
    }

    public boolean isValid(Gene[] a_gene, Supergene a_supergene) {
      return true;
    }
  }
}
