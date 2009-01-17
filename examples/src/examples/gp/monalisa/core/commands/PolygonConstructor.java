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

import java.awt.Point;
import java.awt.Polygon;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

/**
 * A polygon cosists of a series of points.
 *
 * @author Yann N. Dauphin
 */
public class PolygonConstructor
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public PolygonConstructor(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 5, Polygon.class);
  }

  @Override
  public Object execute_object(ProgramChromosome a_chrom, int a_n,
                               Object[] a_args) {
    Polygon polygon = new Polygon();
    for (int i = 0; i < 5; i++) {
      Point p = (Point) a_chrom.execute_object(a_n, i, a_args);
      polygon.addPoint(p.x, p.y);
    }
    return polygon;
  }

  @Override
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return Point.class;
  }

  @Override
  public String toString() {
    return "new Polygon(&1, &2, &3, &4, &5)";
  }
}
