/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import java.math.*;
import java.util.*;
import org.jgap.*;
import org.jgap.util.*;
import gnu.trove.*;

/**
 * An extended implementation of NaturalSelector that acts as a base class for
 * the built-in JGAP selectors BestChromosomesSelector and
 * WeightedRouletteSelector.
 *
 * @author Klaus Meffert
 * @since 3.3.2
 */
public abstract class NaturalSelectorExt
    extends NaturalSelector {
  private Population m_to_pop;

  /**
   * Allows or disallows doublette chromosomes to be added to the selector
   */
  private boolean m_doublettesAllowed;

  /**
   * Default constructor, only needed for dynamic instantiation!
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public NaturalSelectorExt() {
    super();
  }

  /**
   *
   * @param a_config the configuration to use
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public NaturalSelectorExt(Configuration a_config) {
    super(a_config);
  }

  /**
   * Determines whether doublette chromosomes may be added to the selector or
   * will be ignored.
   * @param a_doublettesAllowed true: doublette chromosomes allowed to be
   * added to the selector. false: doublettes will be ignored and not added
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void setDoubletteChromosomesAllowed(final boolean a_doublettesAllowed) {
    m_doublettesAllowed = a_doublettesAllowed;
  }

  /**
   * @return true: doublette chromosomes allowed to be added to the selector
   * false: this is sort of risky and might lead to unexpected population sizes
   * during evolution!
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public boolean getDoubletteChromosomesAllowed() {
    return m_doublettesAllowed;
  }

  /**
   * @param a_o the object to compare
   * @return true: compared object is seen as equal to current instance
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public boolean equals(Object a_o) {
    if (a_o == null) {
      return false;
    }
    NaturalSelectorExt other = (NaturalSelectorExt) a_o;
    if (!super.equals(a_o)) {
      return false;
    }
    if (m_doublettesAllowed != other.m_doublettesAllowed) {
      return false;
    }
    return true;
  }

  /* Add a Chromosome instance to this selector's working pool of Chromosomes.
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  protected abstract void add(final IChromosome a_chromosomeToAdd);

  /**
   * Selects a given number of Chromosomes that will move on to the next
   * generation population.
   *
   * @param a_from_pop the population the Chromosomes will be selected from
   * @param a_to_pop the population the Chromosomes will be added to
   * @param a_howManyToSelect the number of Chromosomes to select
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public final synchronized void select(final int a_howManyToSelect,
                                        final Population a_from_pop,
                                        final Population a_to_pop) {
    if (a_from_pop != null) {
      int popSize = a_from_pop.size();
      if (popSize < 1) {
        throw new IllegalStateException("Population size must be greater 0");
      }
      for (int i = 0; i < popSize; i++) {
        add(a_from_pop.getChromosome(i));
      }
    }
    m_to_pop = a_to_pop;
    selectChromosomes(a_howManyToSelect, a_from_pop);
  }

  /**
   * Selects a given number of chromosomes that will move on to the next
   * generation population. For selecting a chromosome for the next generation,
   * the method selectChromosome(IChromosome, boolean) has to be used!
   *
   * @param a_from_pop the population the Chromosomes will be selected from
   * @param a_howManyToSelect the number of Chromosomes to select
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  protected abstract void selectChromosomes(final int a_howManyToSelect,
      final Population a_from_pop);

  protected final void selectChromosome(IChromosome a_chrom,
                                        final boolean a_clone) {
    if (a_clone && !m_to_pop.contains(a_chrom)) {
      // Use cloning.
      // ------------
      ICloneHandler cloner = getConfiguration().getJGAPFactory().
          getCloneHandlerFor(a_chrom, null);
      if (cloner != null) {
        try {
          a_chrom = (IChromosome) cloner.perform(a_chrom, null, null);
          a_chrom.setIsSelectedForNextGeneration(true);
          m_to_pop.addChromosome(a_chrom);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    else {
      m_to_pop.addChromosome(a_chrom);
    }
  }
}
