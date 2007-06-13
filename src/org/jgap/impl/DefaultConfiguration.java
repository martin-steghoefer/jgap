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
import org.jgap.event.*;
import org.jgap.util.*;

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
    extends Configuration implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.21 $";

  public DefaultConfiguration() {
    this("","");
  }

  /**
   * Constructs a new DefaultConfiguration instance with a number of
   * configuration settings set to default values. It is still necessary
   * to set the sample Chromosome, population size, and desired fitness
   * function. Other settings may optionally be altered as desired.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public DefaultConfiguration(String a_id, String a_name) {
    super(a_id, a_name);
    try {
      setBreeder(new GABreeder(this));
      setRandomGenerator(new StockRandomGenerator());
      setEventManager(new EventManager());
      BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
          this, 0.95d);
      bestChromsSelector.setDoubletteChromosomesAllowed(false);
      addNaturalSelector(bestChromsSelector, true);
      setMinimumPopSizePercent(0);
      setKeepPopulationSizeConstant(true);
      setFitnessEvaluator(new DefaultFitnessEvaluator());
      setChromosomePool(new ChromosomePool());
      addGeneticOperator(new CrossoverOperator(this));
      addGeneticOperator(new MutationOperator(this, 15));
    }
    catch (InvalidConfigurationException e) {
      throw new RuntimeException(
          "Fatal error: DefaultConfiguration class could not use its "
          + "own stock configuration values. This should never happen. "
          + "Please report this as a bug to the JGAP team.");
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return super.clone();
  }
}
