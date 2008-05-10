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

import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.*;
import org.jgap.*;

/**
 * Terminal function to identify the sand, if any, the ant is carrying.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class Carrying
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Constructor for the Carrying function.
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *

   */
  public Carrying(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.IntegerClass);
  }

  /**
   * Returns the sand the ant is carrying
   */
  public int execute_int(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.getAnt().getCarrying();
  }

  /**
   * Program listing name
   */
  public String toString() {
    return "Carrying";
  }
}
