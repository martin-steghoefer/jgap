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

package org.jgap.impl;

import org.jgap.*;
import java.util.*;


/**
 * A basic implementation of NaturalSelector that models a
 * roulette wheel. When a Chromosome is added, it gets a number
 * of "slots" on the wheel equal to its fitness value. When the
 * select method is invoked, the wheel is "spun" and the Chromosome
 * occupying the spot on which it lands is selected. Then the wheel
 * is spun again and again until the requested number of
 * Chromosomes have been selected. Since Chromosomes with higher
 * fitness values get more slots on the wheel, there's a higher
 * statistical probability that they'll be chosen, but it's not
 * guaranteed.
 *
 * @author Neil Rotstan (neil at bluesock.org)
 */
public class WeightedRouletteSelector implements NaturalSelector 
{
  private HashMap wheel = new HashMap();
  private long totalInstances = 0;


  public synchronized void add(Configuration gaConf, Chromosome chromosome,
                               long fitness) 
  {
    if (wheel.containsKey(chromosome)) 
    {
      ((Counter) wheel.get(chromosome)).increment(fitness);
    }
    else 
    {
      wheel.put(chromosome, new Counter(fitness));
    }

    totalInstances += fitness;
  }

 
  public synchronized Chromosome[] select(Configuration gaConf, int howMany) 
  {
    RandomGenerator generator = gaConf.getRandomGenerator();
    Chromosome[] selections = new Chromosome[howMany];
    
    for ( int i = 0; i < howMany; i++ )
    {
      selections[i] = spinWheel(generator);
    }

    return selections;
  }

  /**
   * This method spins the wheel and finds the appropriate chromsome to return.
   * Each time a chromosome is selected from the wheel it is removed from 
   * the wheel so that it can not be selected again. Chromosomes are grouped
   * by their uniqueness for optimization.
   */
   
  private Chromosome spinWheel(RandomGenerator generator) 
  {
    // Randomly choose slot to select
    long selectedSlot = Math.abs(generator.nextLong() % totalInstances);
 
    Iterator iterator = wheel.entrySet().iterator();
    int currentSlot = 0;
    
    // Iterate through the wheel until we find our selected slot
    while( iterator.hasNext() )
    {
      Map.Entry chromosomeEntry = (Map.Entry) iterator.next();
      Chromosome chromo = (Chromosome) chromosomeEntry.getKey();
      Counter chromoCounter = (Counter) chromosomeEntry.getValue(); 
       
      currentSlot += chromoCounter.getCount();
      long fitness = chromoCounter.getStartCount();

      // We found our selected Slot on the wheel
      if ( currentSlot > selectedSlot)
      {
        // Remove 1 chromosome (it's fitness) from the wheel
        chromoCounter.decrement(fitness);  
        totalInstances -= fitness;
       
        // If the unique chromosome is no longer on the wheel we remove it
        // completely. 
        if (chromoCounter.getCount() <= 0 )
          iterator.remove();
      
        // Now return our selected Chromosome 
        return chromo;
      }
    } 

    // If we have reached here, it means we have not found any chromosomes to
    // select and something is wrong with our logic. For some reason the 
    // selected slot has exceeded the slots on our wheel.
    throw new RuntimeException( "Logic Error. This code should  never " +
                                "be reached. Please report this to the " +
                                "jgap team: SelectedSlot exceeded max value." );
  }

  public synchronized void empty() 
  {
    wheel.clear();
    totalInstances = 0;
  }
}  


class Counter {
  private long count;
  private long startCount;

  public Counter(long initialCount) {
    startCount = initialCount;
    count = initialCount;
  }


  public Counter() {
    this(0);
  }


  public long getStartCount() {
    return startCount;
  }

  public void increment() {
    count++;
  }


  public void increment(long howMany) {
    count += howMany;
  }

  public void decrement(long howMany) {
    count -= howMany;
  }

  public long getCount() {
    return count;
  }


  public void reset() {
    count = 0;
  }
}

