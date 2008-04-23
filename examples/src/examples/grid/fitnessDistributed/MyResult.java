/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.fitnessDistributed;

import org.jgap.distr.grid.*;
import org.jgap.*;

/**
 * Stores the result of a computation. Actually adds no functionality to
 * superclass JGAPResult. Extend this class for your application if necessary.
 * It is not necessary to use a class like this, you could simply use
 * JGAPResult!
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyResult
    extends JGAPResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Control the class' serializability via this attribute
   */
  private static final long serialVersionUID = 2L;

  public MyResult(String a_name, int a_id, IChromosome a_fittestChrom,
                  long a_unitdone) {
    super(a_name, a_id, a_fittestChrom, a_unitdone);
  }

  public MyResult(String a_name, int a_id, Population a_pop,
                  long a_unitdone) {
    super(a_name, a_id, a_pop, a_unitdone);
  }
}
