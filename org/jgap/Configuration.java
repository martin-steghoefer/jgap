/*
 * Copyright 2001, Neil Rotstan
 *
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

package org.jgap;

/**
 * The Configuration class represents the current configuration of
 * plugins and flags necessary to execute the genetic algorithm (such
 * as fitness function, natural selector, mutation rate, and so on).
 *
 * Note that, while during setup the settings, flags, and other
 * values may be set multiple times, once the lockSettings() method
 * is invoked, they cannot be changed. The default behavior of the
 * Genotype constructor is to invoke this method, meaning that
 * once a Configuration object is passed to a Genotype, it cannot
 * be subsequently modified. There is no mechanism for unlocking
 * the settings once they are locked.
 */
public class Configuration {
  protected FitnessFunction objectiveFunction = null;
  protected NaturalSelector populationSelector = null;
  protected int populationSize = 0;
  protected int mutationRate = 0;
  protected boolean settingsLocked = false;


  /**
   * Set the fitness function to be used for this genetic algorithm.
   * The fitness function is responsible for evaluating a given
   * Chromosome and returning an integer that represents its
   * worth as a candidate solution. These values are typically
   * used by the natural selector to determine which Chromosome
   * instances will be allowed to move on to the next round
   * of evolution, and which will instead be eliminated.
   *
   * @param f:   The fitness function to be used.
   * @throws InvalidConfigurationChangeException if the fitness function
   *         is not satisfactory or if this object is locked.
   */
  public void setFitnessFunction(FitnessFunction f)
                                 throws InvalidConfigurationChangeException {
    verifyChangesAllowed();

    objectiveFunction = f;
  }


  /**
   * Retrieve the fitness function being used by this genetic
   * algorithm.
   *
   * @return The fitness function used by this genetic algorithm.
   */
  public FitnessFunction getFitnessFunction() {
    return objectiveFunction;
  }


  /**
   * Set the natural selector to be used for this genetic algorithm.
   * The natural selector is responsible for actually selecting
   * which Chromosome instances are allowed to move on to the next
   * round of evolution (usually based on the fitness values
   * provided by the fitness function).
   *
   * @param s   The natural selector to be used.
   * @throws InvalidConfigurationChangeException if the natural selector
   *         is not satisfactory or this object is locked.
   */
  public void setNaturalSelector(NaturalSelector s)
                                 throws InvalidConfigurationChangeException {
    verifyChangesAllowed();
    
    populationSelector = s;
  }


  /**
   * Retrieve the natural selector being used by this genetic
   * algorithm.
   *
   * @return The natural selectorused by this genetic algorithm.
   */
  public NaturalSelector getNaturalSelector() {
    return populationSelector;
  }


  /**
   * Set the population size to be used for this genetic algorithm.
   * The population size is a fixed value that represntes the
   * number of Chromosomes represented in a Genotype.
   *
   * @param s   The population size to be used.
   * @throws InvalidConfigurationChangeException if the population size
   *         is not satisfactory or this object is locked.
   */
  public void setPopulationSize(int s)
                                throws InvalidConfigurationChangeException {
    verifyChangesAllowed();

    populationSize = s;
  }

  /**
   * Retrieve the population size being used by this genetic
   * algorithm.
   *
   * @return The population size used by this genetic algorithm.
   */
  public int getPopulationSize() {
    return populationSize;
  }

  /**
   * Set the mutation rate to be used for this genetic algorithm.
   * The mutation rate should reflect the desired chances of a
   * Chromosome randomly mutating. For example, if 1000 were
   * provided, then there would be a statistical 1/1000 chance
   * of each Chromosome being mutated.
   *
   * @param r   The mutation rate to be used.
   * @throws InvalidConfigurationChangeException if the mutation rate
   *         is not satisfactory or this object is locked.
   */
  public void setMutationRate(int r) 
                              throws InvalidConfigurationChangeException {  
    verifyChangesAllowed();

    mutationRate = r;
  }

  /**
   * Retrieve the mutation rate being used by this genetic
   * algorithm.
   *
   * @return The mutation rate used by this genetic algorithm.
   */
  public int getMutationRate() {
    return mutationRate;
  }


  /**
   * Lock all of the settings in this configuration object. Once
   * this method is invoked, none of the settings may be changed.
   * There is no way to unlock this object once it is locked.
   */
  public void lockSettings() {
    settingsLocked = true;
  }


  /**
   * Makes sure that this object isn't locked. If it is, then an
   * exception is thrown with an appropriate message indicating
   * that settings in this object may not be altered. This method
   * should be invoked by any mutator method in this object prior
   * to making any state alterations.
   *
   * @throws InvalidConfigurationChangeException if this object is locked.
   */
  protected void verifyChangesAllowed()
                 throws InvalidConfigurationChangeException {
    if (settingsLocked) {
      throw new InvalidConfigurationChangeException(
        "This Configuration object is locked. Settings may not be altered.");
    }
  }
}

