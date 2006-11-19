/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.homedns.dade.jcgrid.*;
import org.jgap.IChromosome;

/**
 * Holds the result of a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public abstract class JGAPResult
    extends WorkResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private IChromosome fittest;

  private long unitDone;

  public JGAPResult(String name, int id, IChromosome a_fittestChrom,
                    long a_unitdone) {
    super(name, id);
    fittest = a_fittestChrom;
    unitDone = a_unitdone;
  }

  public IChromosome getFittest() {
    return fittest;
  }

  public long getUnitDone() {
    return unitDone;
  }
}
