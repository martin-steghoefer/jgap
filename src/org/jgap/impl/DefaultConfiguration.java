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
import org.jgap.event.*;

/**
 * The DefaultConfiguration class simplifies the JGAP configuration
 * process by providing default configuration values for many of the
 * configuration settings. The following values must still be provided:
 * the sample Chromosome, population size, and desired fitness function.
 * All other settings may also be changed in the normal fashion for
 * those who wish to specify other custom values.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class DefaultConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * Constructs a new DefaultConfiguration instance with a number of
   * Configuration settings set to default values. It is still necessary
   * to set the sample Chromosome, population size, and desired fitness
   * function. Other settings may optionally be altered as desired.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public DefaultConfiguration() {
    super();
    try {
      BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector();
      bestChromsSelector.setDoubletteChromosomesAllowed(false);
      addNaturalSelector(bestChromsSelector, true);
      setRandomGenerator(new StockRandomGenerator());
      setMinimumPopSizePercent(0);
      setEventManager(new EventManager());
      setChromosomePool(new ChromosomePool());
      addGeneticOperator(new ReproductionOperator());
      addGeneticOperator(new AveragingCrossoverOperator());
      addGeneticOperator(new MutationOperator());
    }
    catch (InvalidConfigurationException e) {
      throw new RuntimeException(
          "Fatal error: DefaultConfiguration class could not use its " +
          "own stock configuration values. This should never happen. " +
          "Please report this as a bug to the JGAP team.");
    }
  }
}
