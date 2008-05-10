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
 * Abstract base class for GP-commands related to the ant painted desert problem.
 * Derived from class AntCommand from the ant trail example.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public abstract class AntCommand
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public AntCommand(final GPConfiguration a_conf, int a_arity, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_type);
  }

  public AntCommand(final GPConfiguration a_conf, final int a_arity,
                    final Class a_returnType, final int a_subReturnType,
                    final int[] a_childSubTypes)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_returnType, a_subReturnType, a_childSubTypes);
  }

  public AntMap getMap(ProgramChromosome a_chrom) {
    if ( (AntMap) a_chrom.getIndividual().getApplicationData() == null) {
      System.out.println("Null map returned");
    }
    return (AntMap) a_chrom.getIndividual().getApplicationData();
  }
}
