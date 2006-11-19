/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;

/**
 * Stores the result of a computation.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyResult
    extends WorkResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private static final long serialVersionUID = 2L;

  private IChromosome fittest;

  private long unitDone;

  public MyResult(String name, int id, IChromosome a_fittestChrom,
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
