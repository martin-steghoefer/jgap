/*
 * Copyright 2001-2003 Neil Rotstan
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
package org.jgap.impl;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.NaturalSelector;
import org.jgap.RandomGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A basic implementation of NaturalSelector that models a roulette wheel.
 * When a Chromosome is added, it gets a number of "slots" on the wheel equal
 * to its fitness value. When the select method is invoked, the wheel is
 * "spun" and the Chromosome occupying the spot on which it lands is selected.
 * Then the wheel is spun again and again until the requested number of
 * Chromosomes have been selected. Since Chromosomes with higher fitness
 * values get more slots on the wheel, there's a higher statistical probability
 * that they'll be chosen, but it's not guaranteed.
 */
public class WeightedRouletteSelector implements NaturalSelector
{
    /**
     * Represents the "roulette wheel." Each key in the Map is a Chromosome
     * and each value is an instance of the SlotCounter inner class, which
     * keeps track of how many slots on the wheel that Chromosome is using.
     */
    private Map m_wheel = new HashMap();

    /**
     * Keeps track of the total number of slots that are in use on the
     * roulette wheel. This is equal to the combined fitness values of
     * all Chromosome instances that have been added to this wheel.
     */
    private long m_totalNumberOfUsedSlots = 0;

    /**
     * An internal pool in which old SlotCounter instances can be stored
     * so that they can be reused over and over again, thus saving memory
     * and the overhead of constructing new ones each time.
     */
    private Pool m_counterPool = new Pool();


    /**
     * Add a Chromosome instance and corresponding fitness value to this
     * selector's working pool of Chromosomes.
     *
     * @param a_activeConfigurator: The current active Configuration to be used
     *                              during the add process.
     * @param a_chromosomeToAdd: The specimen to add to the pool.
     * @param a_chromosomeFitnessValue: The specimen's fitness value.
     */
    public synchronized void add( Configuration a_activeConfigurator,
                                  Chromosome a_chromosomeToAdd,
                                  int a_chromosomeFitnessValue )
    {
        // The "roulette wheel" is represented by a Map. Each key is a
        // Chromosome and each value is an instance of the SlotCounter inner
        // class. The counter keeps track of the total number of slots that
        // each chromosome is using on the wheel (which is equal to the
        // combined total of their fitness values). If the Chromosome is
        // already in the Map, then we just increment its number of slots
        // by its fitness value. Otherwise we add it to the Map.
        // -----------------------------------------------------------------
        SlotCounter counter = (SlotCounter) m_wheel.get( a_chromosomeToAdd );

        if( counter != null )
        {
            counter.incrementByFitness();
        }
        else
        {
            // First, reset the Chromosome's isSelected flag to false.
            // -------------------------------------------------------
            a_chromosomeToAdd.setIsSelected( false );

            // We're going to need a SlotCounter. See if we can get one
            // from the pool. If not, construct a new one.
            // --------------------------------------------------------
            counter = (SlotCounter) m_counterPool.acquirePooledObject();
            if( counter == null )
            {
                counter = new SlotCounter();
            }

            counter.reset( a_chromosomeFitnessValue );
            m_wheel.put( a_chromosomeToAdd, counter );
        }

        m_totalNumberOfUsedSlots += a_chromosomeFitnessValue;
    }


    /**
     * Select a given number of Chromosomes from the pool that will continue
     * to survive. This selection should be guided by the fitness values, but
     * fitness should be treated as a statistical probability of survival,
     * not as the sole determining factor. In other words, Chromosomes with
     * higher fitness values are more likely to be selected than those with
     * lower fitness values, but it's not guaranteed.
     *
     * @param a_activeConfiguration: The current active Configuration that is
     *                               to be used during the selection process.
     * @param a_howManyToSelect: The number of Chromosomes to select.
     *
     * @return An array of the selected Chromosomes.
     */
    public synchronized Chromosome[] select( Configuration a_activeConfiguration,
                                             int a_howManyToSelect )
    {
        RandomGenerator generator = a_activeConfiguration.getRandomGenerator();
        Chromosome[] selections = new Chromosome[ a_howManyToSelect ];

        // Build three arrays from the sorted set: one that contains the
        // fitness values for each chromosome, one that contains the total
        // number of "slots on the wheel" for each chromosome, and one that
        // contains the chromosomes themselves. The array indices can be
        // used to associate the fitness values, counter values, and
        // chromosomes with each other (eg, if a chromosome is at index 5,
        // then its fitness value and counter values are also at index 5
        // of their respective arrays).
        // -----------------------------------------------------------------
        Set entries = m_wheel.entrySet();
        int[] fitnessValues = new int[ entries.size() ];
        long[] counterValues = new long[ entries.size() ];
        Chromosome[] chromosomes = new Chromosome[ entries.size() ];

        int currentIndex = 0;
        Iterator entryIterator = entries.iterator();
        while( entryIterator.hasNext() )
        {
            Map.Entry chromosomeEntry = (Map.Entry) entryIterator.next();

            Chromosome currentChromosome =
                (Chromosome) chromosomeEntry.getKey();

            SlotCounter currentCounter =
                (SlotCounter) chromosomeEntry.getValue();

            fitnessValues[ currentIndex ] = currentCounter.getFitnessValue();
            counterValues[ currentIndex ] = currentCounter.getCounterValue();
            chromosomes[ currentIndex ] = currentChromosome;

            currentIndex++;
        }

        // To select each chromosome, we just "spin" the wheel and grab
        // whichever chromosome it lands on.
        // ------------------------------------------------------------
        Chromosome selectedChromosome;

        for ( int i = 0; i < a_howManyToSelect; i++ )
        {
            selectedChromosome = spinWheel( generator,
                                            fitnessValues,
                                            counterValues,
                                            chromosomes );

            selectedChromosome.setIsSelected( true );
            selections[ i ] = selectedChromosome;
        }

        return selections;
    }


    /**
     * This method "spins" the wheel and returns the Chromosome that is
     * "landed upon." Each time a chromosome is selected from the wheel it
     * is removed from the wheel so that it can not be selected again.
     *
     * @param a_generator The random number generator to be used during the
     *                    spinning process.
     */
    private Chromosome spinWheel( RandomGenerator a_generator,
                                  int[] a_fitnessValues,
                                  long[] a_counterValues,
                                  Chromosome[] a_chromosomes )
    {
        // Randomly choose a slot on the wheel.
        // ------------------------------------
        long selectedSlot =
            Math.abs( a_generator.nextLong() % m_totalNumberOfUsedSlots );

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
        // array indices and, for each one, we add the number of slots it's
        // consuming (its counter value) to an ongoing total until that total
        // reaches or exceeds the chosen slot number. When that happenes,
        // we've found the chromosome sitting in that slot and we return it.
        // ------------------------------------------------------------------
        long currentSlot = 0;

        for( int i = 0; i < a_counterValues.length; i++ )
        {
            // Increment our ongoing total and see if we've landed on the
            // selected slot.
            // ----------------------------------------------------------
            currentSlot += a_counterValues[ i ];

            if ( currentSlot > selectedSlot )
            {
                // Remove one instance of the chromosome from the wheel by
                // decrementing the slot counter by the fitness value.
                // --------------------------------------------------------
                a_counterValues[ i ] -= a_fitnessValues[ i ];
                m_totalNumberOfUsedSlots -= a_fitnessValues[ i ];

                // Now return our selected Chromosome
                // ----------------------------------
                return a_chromosomes[ i ];
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
        for( int i = 0; i < a_counterValues.length; i++ )
        {
            totalSlotsLeft += a_counterValues[ i ];
        }

        throw new RuntimeException( "Logic Error. This code should never " +
                "be reached. Please report this to the " +
                "jgap team: selected slot " + selectedSlot + " " +
                "exceeded " + totalSlotsLeft + " number of slots left." );
    }


    /**
     * Empty out the working pool of Chromosomes.
     */
    public synchronized void empty()
    {
        // Put all of the old SlotCounters into the pool so that we can
        // reuse them later instead of constructing new ones.
        // ------------------------------------------------------------
        m_counterPool.releaseAllObjects( m_wheel.values() );

        // Now clear the wheel and reset the internal state.
        // -------------------------------------------------
        m_wheel.clear();
        m_totalNumberOfUsedSlots = 0;
    }
}


/**
 * Implements a counter that is used to keep track of the total number of
 * slots that a single Chromosome is occupying in the roulette wheel. Since
 * all equal copies of a chromosome have the same fitness value, the increment
 * and decrement methods always add/subtract the fitness value of the
 * chromosome, which is provided during construction of this class.
 */
class SlotCounter
{
    private int m_fitnessValue = 0;
    private long m_count = 0;


    /**
     * Resets the internal state of this SlotCounter instance.
     *
     * @param a_initialFitness The fitness value of the Chromosome for which
     *                         this instance is acting as a counter.
     */ 
    public void reset( int a_initialFitness )
    {
        m_fitnessValue = a_initialFitness;
        m_count = a_initialFitness;
    }


    /**
     * Retrieves the fitness value of the chromosome for which this instance
     * is acting as a counter.
     *
     * @return The fitness value that was passed in at construction time.
     */
    public int getFitnessValue()
    {
        return m_fitnessValue;
    }


    /**
     * Increments the value of this counter by the fitness value that was
     * passed in at construction time.
     */
    public void incrementByFitness()
    {
        m_count += m_fitnessValue;
    }


    /**
     * Decrements the value of this counter by the fitness value that was
     * passed in at construction time.
     */
    public void decrementByFitness()
    {
        m_count -= m_fitnessValue;
    }


    /**
     * Retrieves the current value of this counter.
     *
     * @return the current value of this counter.
     */
    public long getCounterValue()
    {
        return m_count;
    }
}

