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
 *
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class SandAtLocation
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Standard constructor for classic ant problem.
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public SandAtLocation(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.BooleanClass);
  }

  public boolean execute_boolean(ProgramChromosome a_chrom, int a_n,
                                 Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.getAnt().sandAtLocation(map);
  }

  public String toString() {
    return "SandAtLocation";
  }
}
