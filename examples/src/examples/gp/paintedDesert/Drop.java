/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Drops the sand at the current ants location
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class Drop
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Drops the sand at the current location
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *

   */
  public Drop(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.IntegerClass);
  }

  /**
   * returns the sand color that the ant dropped
   */
  public int execute_int(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.getAnt().drop(map);
  }

  /**
   * Program listing name
   */
  public String toString() {
    return "Drop";
  }
}
