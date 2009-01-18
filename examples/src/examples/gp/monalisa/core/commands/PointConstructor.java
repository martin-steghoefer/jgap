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

import java.awt.*;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

import examples.gp.monalisa.core.DrawingProblem.*;

/**
 * A point, consisting of X and Y coordinates.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class PointConstructor
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public PointConstructor(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 2, Point.class, 0,
          new int[] {
      TerminalType.WIDTH.intValue(),
      TerminalType.HEIGHT.intValue()
    });
  }

  @Override
  public Object execute_object(ProgramChromosome a_chrom, int a_n,
                               Object[] a_args) {
    int x = a_chrom.execute_int(a_n, 0, a_args);
    int y = a_chrom.execute_int(a_n, 1, a_args);
    return new Point(x, y);
  }

  @Override
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.IntegerClass;
  }

  @Override
  public String toString() {
    return "new Point(&1, &2)";
  }
}
