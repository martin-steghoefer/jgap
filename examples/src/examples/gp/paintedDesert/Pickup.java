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
 * Picks up any sand at the current location and returns the sand color at
 * the current location.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class Pickup
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Constructor for the pickup sand function
   */
  public Pickup(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.IntegerClass);
  }

  /**
   * Executes the function for an integer argument
   */
  public int execute_int(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.getAnt().pickup(map);
  }

  /**
   * Program listing anme for the function
   */
  public String toString() {
    return "Pickup";
  }
}
