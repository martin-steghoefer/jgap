/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.monalisa.core.commands;

import java.awt.Color;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

/**
 * Specifies a color, represented by R, G, B and Alpha.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class ColorConstructor
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public ColorConstructor(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 4, Color.class);
  }

  @Override
  public Object execute_object(ProgramChromosome a_chrom, int a_n,
                               Object[] a_args) {
    float r = a_chrom.execute_float(a_n, 0, a_args);
    float g = a_chrom.execute_float(a_n, 1, a_args);
    float b = a_chrom.execute_float(a_n, 2, a_args);
    float a = a_chrom.execute_float(a_n, 3, a_args);
    return new Color(r, g, b, a);
  }

  @Override
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.FloatClass;
  }

  @Override
  public String toString() {
    return "new Color(&1, &2, &3, &4)";
  }
}
