/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;

/**
 * Non-abstract Supergene ued for testing purposes only.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class InstantiableSupergeneForTesting
    extends AbstractSupergene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public InstantiableSupergeneForTesting(final Configuration a_config,
                                      Gene[] a_genes)
      throws InvalidConfigurationException {
    super(a_config, a_genes);
  }

  public InstantiableSupergeneForTesting(final Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config, new Gene[] {});
  }

  public InstantiableSupergeneForTesting()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  public boolean isValid(Gene[] a_gene) {
    return true;
  };
}
/**
 * Test implementation of a validator. Always returns true (i.e. valid).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
class TestValidator
    extends Validator {
  public TestValidator(final Configuration a_conf) {
    super(a_conf);
  }

  public boolean isValid(Gene[] a_gene, Supergene a_supergene) {
    return true;
  }
}
