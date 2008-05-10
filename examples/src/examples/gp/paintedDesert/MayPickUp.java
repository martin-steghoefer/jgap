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
 * Identifies that the ant may pick up sand at the current position
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class MayPickUp
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Constructor for the may pick up sand function
   * @param a_conf
   * @throws InvalidConfigurationException
   */
  public MayPickUp(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.BooleanClass);
  }

  /**
   * Executes the may pick up sand for a boolean argument.
   */
  public boolean execute_boolean(ProgramChromosome a_chrom, int a_n,
                                 Object[] a_args) {
    AntMap map = getMap(a_chrom);
    // How do you return a number
    return map.getAnt().mayPickup(map);
  }

  /**
   * Returns the program listing name
   */
  public String toString() {
    return "MayPickUp";
  }
}
