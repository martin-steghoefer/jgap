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

import org.jgap.event.EventManager;
import java.util.*;

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
 *
 * Not all configuration options are required. See the documentation
 * for each of the respective mutrator methods to determine whether
 * it is required to provide a value for that setting, and what the
 * setting will default to if not.
 */
public class Configuration implements java.io.Serializable {
  private FitnessFunction objectiveFunction = null;
  private NaturalSelector populationSelector = null;
  private RandomGenerator random = null;
  private List geneticOperators = new ArrayList();
  private int chromosomeSize = 0;
  private int populationSize = 0;
  private boolean settingsLocked = false;

  private EventManager eventManager = new EventManager();

  /**
   * Set the fitness function to be used for this genetic algorithm.
   * The fitness function is responsible for evaluating a given
   * Chromosome and returning an integer that represents its
   * worth as a candidate solution. These values are typically
   * used by the natural selector to determine which Chromosome
   * instances will be allowed to move on to the next round
   * of evolution, and which will instead be eliminated.
   *
   * This setting is required.
   *
   * @param f:   The fitness function to be used.
   * @throws InvalidConfigurationException if the fitness function
   *         is not satisfactory or if this object is locked.
   */
  public synchronized void setFitnessFunction(FitnessFunction f)
                           throws InvalidConfigurationException {
    verifyChangesAllowed();

    if (f == null) {
      throw new InvalidConfigurationException(
        "FitnessFunction instance must not be null.");
    }

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
   * This setting is required.
   *
   * @param s   The natural selector to be used.
   * @throws InvalidConfigurationException if the natural selector
   *         is not satisfactory or this object is locked.
   */
  public synchronized void setNaturalSelector(NaturalSelector s)
                           throws InvalidConfigurationException {
    verifyChangesAllowed();

    if (s == null) {
      throw new InvalidConfigurationException(
        "Natural Selector instance must not be null.");
    }
    
    populationSelector = s;
  }


  /**
   * Retrieve the natural selector being used by this genetic
   * algorithm.
   *
   * @return The natural selector used by this genetic algorithm.
   */
  public NaturalSelector getNaturalSelector() {
    return populationSelector;
  }


  /**
   * Set the random generator to be used for this genetic algorithm.
   * The random generator is responsible for genrating random numbers,
   * which are used throughout the process of genetic evolution and
   * selection.
   *
   * This setting is required.
   *
   * @param g   The random generator to be used.
   * @throws InvalidConfigurationException if the random generator
   *         is not satisfactory or this object is locked.
   */
  public synchronized void setRandomGenerator(RandomGenerator g)
                           throws InvalidConfigurationException {
    verifyChangesAllowed();

    if (g == null) {
      throw new InvalidConfigurationException(
        "RandomGenerator instance must not be null.");
    }

    random = g;
  }


  /**
   * Retrieve the random generator being used by this genetic
   * algorithm.
   *
   * @return The random generator used by this genetic algorithm.
   */
  public RandomGenerator getRandomGenerator() {
    return random;
  }


  /**
   * Add a genetic operator for use in this algorithm. Genetic operators
   * represent evolutionary steps that, when combined, make up the
   * evolutionary process. Examples of genetic operators are reproduction,
   * crossover, and mutation. During the evolution process, all of the
   * genetic operators added via this method are invoked in the order
   * they were added.
   *
   * At least one genetic operator must be provided.
   *
   * @param o   The genetic operator to be added.
   * @throws InvalidConfigurationException if the genetic operator
   *         is null o this robject is locked.
   */
  public synchronized void addGeneticOperator(GeneticOperator o)
                           throws InvalidConfigurationException {
    verifyChangesAllowed();

    if (o == null) {
      throw new InvalidConfigurationException(
        "GeneticOperator instance must not be null.");
    }

    geneticOperators.add(o);
  }


  /**
   * Retrieve the genetic operators added for this genetic algorithm.
   * Note that once this Configuration instance is locked, a new,
   * immutable list of operators is used and any lists previously
   * retrieved with this method will no longer reflect the actual
   * list in use.
   *
   * @return The list of genetic operators added to this Configuration
   */
  public List getGeneticOperators() {
    return geneticOperators;
  }


  /**
   * Set the chromosome size to be used for this genetic algorithm.
   * The chromosome size is a fixed value that represntes the
   * number of genes represented by each Chromosome.
   *
   * This setting is required.
   *
   * @param s   The chromosome size to be used.
   * @throws InvalidConfigurationException if the chromosome size
   *         is not satisfactory or this object is locked.
   */
  public synchronized void setChromosomeSize(int s)
                           throws InvalidConfigurationException {
    verifyChangesAllowed();

    chromosomeSize = s;
  }

  /**
   * Retrieve the chromosome size being used by this genetic
   * algorithm.
   *
   * @return The chromosome size used by this genetic algorithm.
   */
  public int getChromosomeSize() {
    return chromosomeSize;
  }


  /**
   * Set the population size to be used for this genetic algorithm.
   * The population size is a fixed value that represntes the
   * number of Chromosomes represented in a Genotype.
   *
   * This setting is required.
   *
   * @param s   The population size to be used.
   * @throws InvalidConfigurationException if the population size
   *         is not satisfactory or this object is locked.
   */
  public synchronized void setPopulationSize(int s)
                           throws InvalidConfigurationException {
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
   * Retrieves the EventManager associated with this configuration.
   * The EventManager can be used to listen for events such as 
   * GenotypeEvolvedEvent.
   */
  public EventManager getEventManager() {
    return eventManager;
  }

  /**
   * Lock all of the settings in this configuration object. Once
   * this method is successfully invoked, none of the settings may
   * be changed. There is no way to unlock this object once it is locked.
   *
   * Prior to returning successfully, this method will first invoke the
   * hasValidState() method to make sure that any required configuration
   * options have been properly set. If it detects a problem, it will
   * throw an InvalidConfigurationException and leave the object unlocked. 
   *
   * It's possible to test whether is object is locked through the
   * isLocked() method.
   *
   * It is ok to lock an object more than once. In this case, this method
   * does nothing and simply returns.
   *
   * @throws InvalidConfigurationException if the object is in an invalid
   *         state at the time of invocation.
   */
  public synchronized void lockSettings() 
                           throws InvalidConfigurationException {
    if (!settingsLocked) {
      if (!hasValidState()) {
        throw new InvalidConfigurationException(
          "Attempt to lock a Configuration object in an invalid state. " +
          "Make sure that you have set all of the required settings.");
      }

      settingsLocked = true;

      // Make geneticOperators list immutable
      geneticOperators = Collections.unmodifiableList(geneticOperators);
    }
  }


  /**
   * Retrieve the lock status of this object.
   *
   * @return true if this object has been locked by a previous successful
   *         call to the lockSettings() method, false otherwise.
   */
  public boolean isLocked() {
    return settingsLocked;
  }


  /**
   * Test the state of this object to make sure it's valid. This generally
   * consists of verifying that required settings have, in fact, been set.
   * If this object is not in a valid state (meaning this method returns
   * false), then an attempt to lock the object via the lockSettings()
   * method will fail.
   *
   * @return true if this object is in a valid state, false otherwise.
   */
  public synchronized boolean hasValidState() {
    return objectiveFunction != null &&
           populationSelector != null &&
           random != null &&
           !geneticOperators.isEmpty() &&
           chromosomeSize > 0 &&
           populationSize > 0;
  }


  /**
   * Makes sure that this object isn't locked. If it is, then an
   * exception is thrown with an appropriate message indicating
   * that settings in this object may not be altered. This method
   * should be invoked by any mutator method in this object prior
   * to making any state alterations.
   *
   * @throws InvalidConfigurationException if this object is locked.
   */
  protected void verifyChangesAllowed()
                 throws InvalidConfigurationException {
    if (settingsLocked) {
      throw new InvalidConfigurationException(
        "This Configuration object is locked. Settings may not be altered.");
    }
  }
}

