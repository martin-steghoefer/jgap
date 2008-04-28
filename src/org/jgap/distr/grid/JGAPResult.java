/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;

/**
 * Holds the result of a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class JGAPResult
    extends WorkResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private IChromosome m_fittest;
  private Population m_pop;

  private long m_unitDone;

  /**
   *
   * @param name String
   * @param id int
   * @param a_fittestChrom IChromosome
   * @param a_unitdone long
   * @deprecated use other constructor with Population parameter instead
   */
  public JGAPResult(String name, int id, IChromosome a_fittestChrom,
                    long a_unitdone) {
    super(name, id);
    m_fittest = a_fittestChrom;
    m_unitDone = a_unitdone;
  }

  /**
   * Constructor: Takes a Population as result of a worker's computation.
   *
   * @param name arbritrary session name to distinct from other results
   * @param id ID of the result, should be unique must need not
   * @param a_chromosomes the result of a worker's computation
   * @param a_unitdone number of units done
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResult(String name, int id, Population a_chromosomes,
                    long a_unitdone) {
    super(name, id);
    m_fittest = null;
    m_pop = a_chromosomes;
    m_unitDone = a_unitdone;
  }

  /**
   * @return IChromosome
   * @deprecated use getPopulation instead
   */
  public IChromosome getFittest() {
    return m_fittest;
  }

  /**
   * @return the Population as a result from a worker's computation
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Population getPopulation() {
    return m_pop;
  }

  public long getUnitDone() {
    return m_unitDone;
  }
}
