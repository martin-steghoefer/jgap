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

package org.jgap.distr;

import org.jgap.*;
import java.io.*;
import java.util.*;
import org.jgap.event.*;
import org.jgap.impl.*;

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
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  public DistributedGenotype(Configuration a_activeConfiguration,
                             Population a_population,
                             FitnessEvaluator a_fitnessEvaluator)
      throws InvalidConfigurationException {
    super(a_activeConfiguration, a_population, a_fitnessEvaluator);
  }

  public DistributedGenotype(Configuration a_activeConfiguration,
                             Population a_population)
      throws InvalidConfigurationException {
    super(a_activeConfiguration, a_population);
  }
}
