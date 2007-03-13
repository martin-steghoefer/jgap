/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Holds the result of a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class JGAPResultGP
    extends WorkResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private IGPProgram m_fittest;
  private GPPopulation m_pop;

  private long m_unitDone;

  /**
   *
   * @param name String
   * @param id int
   * @param a_fittestProg
   * @param a_unitdone long
   * @deprecated use other constructor with GPPopulation parameter instead
   */
  public JGAPResultGP(String name, int id, IGPProgram a_fittestProg,
                    long a_unitdone) {
    super(name, id);
    m_fittest = a_fittestProg;
    m_unitDone = a_unitdone;
  }

  /**
   * Constructor: Takes a Population as result of a worker's computation.
   *
   * @param name arbritrary session name to distinct from other results
   * @param id ID of the result, should be unique must need not
   * @param a_programs the result of a worker's computation
   * @param a_unitdone number of units done
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResultGP(String name, int id, GPPopulation a_programs,
                    long a_unitdone) {
    super(name, id);
    m_fittest = null;
    m_pop = a_programs;
    m_unitDone = a_unitdone;
  }

  /**
   * @return IGPProgram
   * @deprecated use getPopulation instead
   */
  public IGPProgram getFittest() {
    return m_fittest;
  }

  /**
   * @return the GPPopulation as a result from a worker's computation
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPPopulation getPopulation() {
    return m_pop;
  }

  public long getUnitDone() {
    return m_unitDone;
  }
}
