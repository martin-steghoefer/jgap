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
 * An instance splitting a single request into multiple requests that will be
 * sent to multiple workers for computation.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyRequest
    extends JGAPRequest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public MyRequest(String name, int id, Configuration a_config) {
    super(name, id, a_config);
  }

  public MyRequest(String name, int id, Configuration a_config, Population a_pop) {
    super(name, id, a_config, a_pop);
  }

  /**
   * @return deep clone of current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return super.clone();
  }
}
