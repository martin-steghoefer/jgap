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
 * Natural selectors are responsible for actually selecting a
 * specified number of Chromosome specimens from a population,
 * using the fitness values as a guide. Usually fitness is
 * treated as a statistic probability of survival, not as the
 * sole determining factor. Therefore, Chromosomes with higher
 * fitness values are more likely to survive than those with
 * lesser fitness values, but it's not guaranteed.
 */
public interface NaturalSelector
{
  /**
   * Add a Chromosome instance and corresponding fitness
   * value to this selector's working pool of Chromosomes.
   *
   * @param chromosome: The specimen to add to the pool.
   * @param fitness: The specimen's fitness value.
   */
  public void add( Chromosome chromosome, int fitness );


  /**
   * Select a given number of Chromosomes from the pool
   * that will continue to survive. This selection should
   * be guided by the fitness values, but fitness should
   * be treated as a statistical probability of survival,
   * not as the sole determining factor. In other words,
   * Chromosomes with higher fitness values are more likely
   * to be selected than those with lower fitness values,
   * but it's not guaranteed.
   *
   * @param howMany: The number of Chromosomes to select.
   * 
   * @return An array of the selected Chromosomes.
   */
  public Chromosome[] select( int howMany );


  /**
   * Empty out the working pool of Chromosomes.
   */
  public void empty();
}  
