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

/**
 * Supergene to hold pennies and nickels. Valid if the number of
 * nickels and pennies is either both odd or both even.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class NickelsPenniesSupergene
    extends AbstractSupergene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public NickelsPenniesSupergene()
      throws InvalidConfigurationException {
    this(Genotype.getConfiguration());
  }

  public NickelsPenniesSupergene(final Configuration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, null);
  }

  public NickelsPenniesSupergene(final Configuration a_conf, Gene[] a_genes)
      throws InvalidConfigurationException {
    super(a_conf, a_genes);
  }

  public boolean isValid(Gene[] a_genes, Supergene a_supergene) {
    IntegerGene nickels = (IntegerGene) a_genes[0];
    IntegerGene pennies = (IntegerGene) a_genes[1];
    boolean valid = nickels.intValue() % 2 == pennies.intValue() % 2;
    return valid;
  }
}
