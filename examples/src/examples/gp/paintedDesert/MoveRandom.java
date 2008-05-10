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
 * Randomly moves the ant one square.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class MoveRandom
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Constructor for the move random function.
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Scott Mueller
   * @since 3.2
   */
  public MoveRandom(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
  }

  /**
   * Executes the move random function for an integer argument
   */
  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    map.getAnt().moveRandom(map);
  }

  /**
   * Returns the program listing name for the function
   */
  public String toString() {
    return "MoveRandom";
  }
}
