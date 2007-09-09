/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Abstract base class for GP-commands related to the ant trail problem.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public abstract class AntCommand
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public AntCommand(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
  }

  public AntCommand(final GPConfiguration a_conf, int a_arity, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_type);
  }

  public AntMap getMap(ProgramChromosome a_chrom) {
    return (AntMap)a_chrom.getIndividual().getApplicationData();
  }
}
