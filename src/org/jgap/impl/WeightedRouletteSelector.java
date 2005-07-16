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

import java.math.*;
import java.util.*;
import org.jgap.*;

/**
 * A basic implementation of NaturalSelector that models a roulette wheel.
 * When a Chromosome is added, it gets a number of "slots" on the wheel equal
 * to its fitness value. When the select method is invoked, the wheel is
 * "spun" and the Chromosome occupying the spot on which it lands is selected.
 * Then the wheel is spun again and again until the requested number of
 * Chromosomes have been selected. Since Chromosomes with higher fitness
 * values get more slots on the wheel, there's a higher statistical probability
 * that they'll be chosen, but it's not guaranteed.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class WeightedRouletteSelector
    extends NaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001d;

  private static final BigDecimal ZERO_BIG_DECIMAL = new BigDecimal(0.0d);

  /**
   * Represents the "roulette wheel." Each key in the Map is a Chromosome
   * and each value is an instance of the SlotCounter inner class, which
   * keeps track of how many slots on the wheel each Chromosome is occupying.
   */
  private Map m_wheel = new HashMap();

  /**
   * Keeps track of the total number of slots that are in use on the
   * roulette wheel. This is equal to the combined fitness values of
   * all Chromosome instances that have been added to this wheel.
   */
  private double m_totalNumberOfUsedSlots;

  /**
   * An internal pool in which discarded SlotCounter instances can be stored
   * so that they can be reused over and over again, thus saving memory
   * and the overhead of constructing new ones each time.
   */
  private Pool m_counterPool = new Pool();

  /**
   * Allows or disallows doublette chromosomes to be added to the selector
   */
  private boolean m_doublettesAllowed;

  /**
   * @author Klaus Meffert
   * @since 2.0 (prior: existent thru super class)
   */
  public WeightedRouletteSelector() {
    m_doublettesAllowed = true;
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   *
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  protected synchronized void add(Chromosome a_chromosomeToAdd) {
    // The "roulette wheel" is represented by a Map. Each key is a
    // Chromosome and each value is an instance of the SlotCounter inner
    // class. The counter keeps track of the total number of slots that
    // each chromosome is occupying on the wheel (which is equal to the
    // combined total of their fitness values). If the Chromosome is
    // already in the Map, then we just increment its number of slots
    // by its fitness value. Otherwise we add it to the Map.
    // -----------------------------------------------------------------
    SlotCounter counter = (SlotCounter) m_wheel.get(a_chromosomeToAdd);
    if (counter != null) {
      // The Chromosome is already in the map.
      // -------------------------------------
      counter.increment();
    }
    else {
      // We need to add this Chromosome and an associated SlotCounter
      // to the map. First, we reset the Chromosome's
      // isSelectedForNextGeneration flag to false. Later, if the
      // Chromosome is actually selected to move on to the next
      // generation population by the select() method, then it will
      // be set to true.
      // ------------------------------------------------------------
      a_chromosomeToAdd.setIsSelectedForNextGeneration(false);
      // We're going to need a SlotCounter. See if we can get one
      // from the pool. If not, construct a new one.
      // --------------------------------------------------------
      counter = (SlotCounter) m_counterPool.acquirePooledObject();
      if (counter == null) {
        counter = new SlotCounter();
      }
      counter.reset(a_chromosomeToAdd.getFitnessValue());
      m_wheel.put(a_chromosomeToAdd, counter);
    }
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection should be guided by
   * the fitness values, but fitness should be treated as a statistical
   * probability of survival, not as the sole determining factor. In other
   * words, Chromosomes with higher fitness values should be more likely to
   * be selected than those with lower fitness values, but it should not be
   * guaranteed.
   *
   * @param a_howManyToSelect the number of Chromosomes to select
   * @param a_from_pop the population the Chromosomes will be selected from
   * @param a_to_pop the population the Chromosomes will be added to
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized void select(int a_howManyToSelect, Population a_from_pop,
                                  Population a_to_pop) {
    if (a_from_pop != null) {
      int size = a_from_pop.size();
      for (int i = 0; i < size; i++) {
        add(a_from_pop.getChromosome(i));
      }
    }

    RandomGenerator generator = Genotype.getConfiguration().
        getRandomGenerator();
    scaleFitnessValues();
    // Build three arrays from the key/value pairs in the wheel map: one
    // that contains the fitness values for each chromosome, one that
    // contains the total number of occupied slots on the wheel for each
    // chromosome, and one that contains the chromosomes themselves. The
    // array indices are used to associate the values of the three arrays
    // together (eg, if a chromosome is at index 5, then its fitness value
    // and counter values are also at index 5 of their respective arrays).
    // -------------------------------------------------------------------
    Set entries = m_wheel.entrySet();
    int numberOfEntries = entries.size();
    double[] fitnessValues = new double[numberOfEntries];
    double[] counterValues = new double[numberOfEntries];
    Chromosome[] chromosomes = new Chromosome[numberOfEntries];
    m_totalNumberOfUsedSlots = 0.0;
    Iterator entryIterator = entries.iterator();
    for (int i = 0; i < numberOfEntries; i++) {
      Map.Entry chromosomeEntry = (Map.Entry) entryIterator.next();
      Chromosome currentChromosome =
          (Chromosome) chromosomeEntry.getKey();
      SlotCounter currentCounter =
          (SlotCounter) chromosomeEntry.getValue();
      fitnessValues[i] = currentCounter.getFitnessValue();
      counterValues[i] = currentCounter.getFitnessValue() *
          currentCounter.getCounterValue();
      chromosomes[i] = currentChromosome;
      // We're also keeping track of the total number of slots,
      // which is the sum of all the counter values.
      // ------------------------------------------------------
      m_totalNumberOfUsedSlots += counterValues[i];
    }
    if (a_howManyToSelect > numberOfEntries
        && !getDoubletteChromosomesAllowed()) {
      a_howManyToSelect = numberOfEntries;
    }
    // To select each chromosome, we just "spin" the wheel and grab
    // whichever chromosome it lands on.
    // ------------------------------------------------------------
    Chromosome selectedChromosome;
    for (int i = 0; i < a_howManyToSelect; i++) {
      selectedChromosome = spinWheel(generator, fitnessValues, counterValues,
                                     chromosomes);
      selectedChromosome.setIsSelectedForNextGeneration(true);
      a_to_pop.addChromosome(selectedChromosome);
    }
  }

  /**
   * This method "spins" the wheel and returns the Chromosome that is
   * "landed upon." Each time a chromosome is selected, one instance of it
   * is removed from the wheel so that it cannot be selected again.
   *
   * @param a_generator the random number generator to be used during the
   * spinning process
   * @param a_fitnessValues an array of fitness values of the respective
   * Chromosomes
   * @param a_counterValues an array of total counter values of the
   * respective Chromosomes
   * @param a_chromosomes the respective Chromosome instances from which
   * selection is to occur
   * @return selected Chromosome from the roulette wheel
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  private Chromosome spinWheel(RandomGenerator a_generator,
                               double[] a_fitnessValues,
                               double[] a_counterValues,
                               Chromosome[] a_chromosomes) {
    // Randomly choose a slot on the wheel.
    // ------------------------------------
    double selectedSlot =
        Math.abs(a_generator.nextDouble() * m_totalNumberOfUsedSlots);
    if (selectedSlot > m_totalNumberOfUsedSlots) {
      selectedSlot = m_totalNumberOfUsedSlots;
    }
    // Loop through the wheel until we find our selected slot. Here's
    // how this works: we have three arrays, one with the fitness values
    // of the chromosomes, one with the total number of slots on the
    // wheel that each chromosome occupies (its counter value), and
    // one with the chromosomes themselves. The array indices associate
    // each of the three together (eg, if a chromosome is at index 5,
    // then its fitness value and counter value are also at index 5 of
    // their respective arrays).
    //
    // We've already chosen a random slot number on the wheel from which
    // we want to select the Chromosome. We loop through each of the
    // array indices and, for each one, we add the number of occupied slots
    // (the counter value) to an ongoing total until that total
    // reaches or exceeds the chosen slot number. When that happenes,
    // we've found the chromosome sitting in that slot and we return it.
    // --------------------------------------------------------------------
    double currentSlot = 0.0;
    FitnessEvaluator evaluator = Genotype.getConfiguration().
        getFitnessEvaluator();
    for (int i = 0; i < a_counterValues.length; i++) {
      // Increment our ongoing total and see if we've landed on the
      // selected slot.
      // ----------------------------------------------------------
      currentSlot += a_counterValues[i];
      boolean found;
      if (evaluator.isFitter(2,1)) {
        found = currentSlot > selectedSlot - DELTA;
      }
      else {
        found = currentSlot <= selectedSlot - DELTA;
      }
      if (found) {
        // Remove one instance of the chromosome from the wheel by
        // decrementing the slot counter by the fitness value.
        // -------------------------------------------------------
        a_counterValues[i] -= a_fitnessValues[i];
        m_totalNumberOfUsedSlots -= a_fitnessValues[i];
        // Now return our selected Chromosome.
        // -----------------------------------
        return a_chromosomes[i];
      }
    }
    // If we have reached here, it means we have not found any chromosomes
    // to select and something is wrong with our logic. For some reason
    // the selected slot has exceeded the slots on our wheel. To help
    // with debugging, we tally up the total number of slots left on
    // the wheel and report it along with the chosen slot number that we
    // couldn't find.
    // -------------------------------------------------------------------
    long totalSlotsLeft = 0;
    for (int i = 0; i < a_counterValues.length; i++) {
      totalSlotsLeft += a_counterValues[i];
    }
    throw new RuntimeException("Logic Error. This code should never " +
                               "be reached. Please report this as a bug to the " +
                               "JGAP team: selected slot " + selectedSlot + " " +
                               "exceeded " + totalSlotsLeft +
                               " number of slots left. " +
                               "We thought there were " +
                               m_totalNumberOfUsedSlots +
                               " slots left.");
  }

  /**
   * Empty out the working pool of Chromosomes.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void empty() {
    // Put all of the old SlotCounters into the pool so that we can
    // reuse them later instead of constructing new ones.
    // ------------------------------------------------------------
    m_counterPool.releaseAllObjects(m_wheel.values());
    // Now clear the wheel and reset the internal state.
    // -------------------------------------------------
    m_wheel.clear();
    m_totalNumberOfUsedSlots = 0;
  }

  private void scaleFitnessValues() {
    // First, add up all the fitness values. While we're doing this,
    // keep track of the largest fitness value we encounter.
    // -------------------------------------------------------------
    double largestFitnessValue = 0.0;
    BigDecimal totalFitness = ZERO_BIG_DECIMAL;
    Iterator counterIterator = m_wheel.values().iterator();
    while (counterIterator.hasNext()) {
      SlotCounter counter = (SlotCounter) counterIterator.next();
      if (counter.getFitnessValue() > largestFitnessValue) {
        largestFitnessValue = counter.getFitnessValue();
      }
      BigDecimal counterFitness = new BigDecimal(counter.getFitnessValue());
      totalFitness = totalFitness.add(counterFitness.multiply(
          new BigDecimal(counter.getCounterValue())));
    }
    // Now divide the total fitness by the largest fitness value to
    // compute the scaling factor.
    // ------------------------------------------------------------
    if (largestFitnessValue > 0.000000d
        && totalFitness.floatValue() > 0.0000001d) {
      double scalingFactor =
          totalFitness.divide(new BigDecimal(largestFitnessValue),
                              BigDecimal.ROUND_HALF_UP).doubleValue();
      // Divide each of the fitness values by the scaling factor to
      // scale them down.
      // --------------------------------------------------------------
      counterIterator = m_wheel.values().iterator();
      while (counterIterator.hasNext()) {
        SlotCounter counter = (SlotCounter) counterIterator.next();
        counter.scaleFitnessValue(scalingFactor);
      }
    }
  }

  /**
   * @return always false as some Chromosome's could be returnd multiple times
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean returnsUniqueChromosomes() {
    return false;
  }

  /**
   * Determines whether doublette chromosomes may be added to the selector or
   * will be ignored.
   * @param a_doublettesAllowed true: doublette chromosomes allowed to be
   * added to the selector. FALSE: doublettes will be ignored and not added
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setDoubletteChromosomesAllowed(boolean a_doublettesAllowed) {
    m_doublettesAllowed = a_doublettesAllowed;
  }

  /**
   * @return TRUE: doublette chromosomes allowed to be added to the selector
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean getDoubletteChromosomesAllowed() {
    return m_doublettesAllowed;
  }
  // methods derived from the Configurable interface
  
  /**
   * Return a ConfigurationHandler for this class.
   * @author Siddhartha Azad.
   * @since 2.3
   * */
  public ConfigurationHandler getConfigurationHandler() {
  	// @todo write this
  	return null;
  }
  
  /**
   * Pass the name and value of a property to be set.
   * @author Siddhartha Azad.
   * @param name The name of the property.
   * @param value The value of the property.
   * @since 2.3
   * */
  public void setConfigProperty(String name, String value) throws ConfigException,
  	InvalidConfigurationException   {
  	/**@todo write this */
  }
  
 
  
  /**
   * Pass the name and values of a property to be set.
   * @author Siddhartha Azad.
   * @param name The name of the property.
   * @param values The different values of the property.
   * @since 2.3
   * */
  public void setConfigMultiProperty(String name, ArrayList values) throws 
  	ConfigException, InvalidConfigurationException {
  	/**@todo write this */
  }
}

/**
 * Implements a counter that is used to keep track of the total number of
 * slots that a single Chromosome is occupying in the roulette wheel. Since
 * all equal copies of a chromosome have the same fitness value, the increment
 * method always adds the fitness value of the chromosome. Following
 * construction of this class, the reset() method must be invoked to provide
 * the initial fitness value of the Chromosome for which this SlotCounter is
 * to be associated. The reset() method may be reinvoked to begin counting
 * slots for a new Chromosome.
 *
 * @author Neil Rotstan
 * @since 1.0
 */
class SlotCounter {
  /**
   * The fitness value of the Chromosome for which we are keeping count of
   * roulette wheel slots. Although this value is constant for a Chromosome,
   * it's not declared final here so that the slots can be reset and later
   * reused for other Chromosomes, thus saving some memory and the overhead
   * of constructing them from scratch.
   */
  private double m_fitnessValue;

  /**
   * The current number of Chromosomes represented by this counter.
   */
  private int m_count;

  /**
   * Resets the internal state of this SlotCounter instance so that it can
   * be used to count slots for a new Chromosome.
   *
   * @param a_initialFitness the fitness value of the Chromosome for which
   * this instance is acting as a counter
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void reset(double a_initialFitness) {
    m_fitnessValue = a_initialFitness;
    m_count = 1;
  }

  /**
   * Retrieves the fitness value of the chromosome for which this instance
   * is acting as a counter.
   *
   * @return the fitness value that was passed in at reset time
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public double getFitnessValue() {
    return m_fitnessValue;
  }

  /**
   * Increments the value of this counter by the fitness value that was
   * passed in at reset time.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void increment() {
    m_count++;
  }

  /**
   * Retrieves the current value of this counter: ie, the number of slots
   * on the roulette wheel that are currently occupied by the Chromosome
   * associated with this SlotCounter instance.
   *
   * @return the current value of this counter
   */
  public int getCounterValue() {
    return m_count;
  }

  /**
   * Scales this SlotCounter's fitness value by the given scaling factor.
   *
   * @param a_scalingFactor the factor by which the fitness value is to be
   * scaled
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void scaleFitnessValue(double a_scalingFactor) {
    m_fitnessValue /= a_scalingFactor;
  }
}
