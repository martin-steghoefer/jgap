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

import org.jgap.distr.grid.*;
import org.jgap.*;

/**
 * Stores the result of a computation.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyResult
    extends JGAPResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private static final long serialVersionUID = 2L;

  public MyResult(String name, int id, IChromosome a_fittestChrom,
                  long a_unitdone) {
    super(name, id, a_fittestChrom, a_unitdone);
  }

}
