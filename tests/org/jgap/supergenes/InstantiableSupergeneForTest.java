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
  public class InstantiableSupergeneForTest
      extends AbstractSupergene {
    public InstantiableSupergeneForTest(final Configuration a_config, Gene[] a_genes)
        throws InvalidConfigurationException {
      super(a_config, a_genes);
    }

    public InstantiableSupergeneForTest(final Configuration a_config)
        throws InvalidConfigurationException {
      super(a_config, new Gene[]{});
    }

    public InstantiableSupergeneForTest()
        throws InvalidConfigurationException {
      this(Genotype.getConfiguration());
    }

    public boolean isValid(Gene[] a_gene) {
      return true;
    };
  }
  class TestValidator
    extends Validator {
  public TestValidator(final Configuration a_conf) {
    super(a_conf);
  }

  public boolean isValid(Gene[] a_gene, Supergene a_supergene) {
    return true;
  }
}

