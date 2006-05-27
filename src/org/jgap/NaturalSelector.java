/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;
import org.jgap.data.config.*;

/**
 * Abstract base implementation of interface INaturalSelector.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class NaturalSelector
    implements INaturalSelector, Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.23 $";

  private transient Configuration m_config;
  /**
   *
   * @param a_config the configuration to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public NaturalSelector(Configuration a_config) {
    m_config = a_config;
  }

  /**
   * @return the (immutable) configuration to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   *
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  protected abstract void add(IChromosome a_chromosomeToAdd);

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public class FitnessValueComparator
      implements Comparator, java.io.Serializable  {
    public int compare(Object first, Object second) {
      IChromosome chrom1 = (IChromosome) first;
      IChromosome chrom2 = (IChromosome) second;
      if (getConfiguration().getFitnessEvaluator().isFitter(chrom2,
          chrom1)) {
        return 1;
      }
      else if (getConfiguration().getFitnessEvaluator().isFitter(
          chrom1, chrom2)) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
}
