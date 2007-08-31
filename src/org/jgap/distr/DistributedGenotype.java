/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.*;

/**
 * Genotype that is running on one of many servers computing a population.
 * This type of Genotype knows his companions (other servers to contact for
 * distributed calculating of populations).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DistributedGenotype
    extends Genotype {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.8 $";

  public DistributedGenotype(Configuration a_activeConfiguration,
                             Population a_population)
      throws InvalidConfigurationException {
    super(a_activeConfiguration, a_population);
  }

/**@todo implement island model*/
}
