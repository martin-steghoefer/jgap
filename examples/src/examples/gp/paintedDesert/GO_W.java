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
 * Moves the ant to the west or negative x direction.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class GO_W
    extends AntCommand implements IMutateable {
  /**
   * Constructor for the go west function
   * @param a_conf
   * @throws InvalidConfigurationException
   */
  public GO_W(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
  }

  /**
   * Mutates the gene
   */
  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    GO_E mutant = new GO_E(getGPConfiguration());
    return mutant;
  }

  /**
   * Goes west if possible
   */
  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    map.getAnt().goW(map);
  }

  /**
   * Returns the program listing name
   */
  public String toString() {
    return "GO-W";
  }
}
