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
 * Look ahead, right and left (in this order) and turns to food in case such was
 * detected. Does nothing in the other case.<p>
 * This command is not part of the classic ant problem.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Y
    extends AntCommand implements IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Y(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.IntegerClass);
  }

  public Y(final GPConfiguration a_conf, final int a_subReturnType,
          final int[] a_childSubTypes)
  throws InvalidConfigurationException {
	  super(a_conf, 0, CommandGene.IntegerClass,a_subReturnType, a_childSubTypes );
}

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    CommandGene mutant;
    mutant = new X(getGPConfiguration());
    return mutant;
  }

  public int execute_int(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.getAnt().getYpos();
  }

  public String toString() {
    return "Y";
  }
}
