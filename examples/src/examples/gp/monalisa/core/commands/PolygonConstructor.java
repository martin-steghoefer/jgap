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

/**
 * A polygon cosists of a series of points.
 *
 * @author Yann N. Dauphin
 */
public class PolygonConstructor
    extends CommandGene implements IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private boolean m_mutateable;

  private int m_points = 5;

  public PolygonConstructor(GPConfiguration a_conf, int a_points)
      throws InvalidConfigurationException {
    super(a_conf, a_points, Polygon.class);
    m_points = a_points;
    m_mutateable = false;/**@todo set to true when applyMutation works*/
  }

  @Override
  public Object execute_object(ProgramChromosome a_chrom, int a_n,
                               Object[] a_args) {
    Polygon polygon = new Polygon();
    for (int i = 0; i < m_points; i++) {
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
    String s = "new Polygon(";
    for (int i = 0; i < m_points; i++) {
      if (i > 0) {
        s += ", ";
      }
      s += "&" + (i + 1);
    }
    s += ")";
    return s;
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    if (!m_mutateable) {
      return this;
    }
    RandomGenerator randomGen = getGPConfiguration().getRandomGenerator();
    double random = randomGen.nextDouble();
    if (random < a_percentage) {
      return applyMutation();
    }
    return this;
  }

  public CommandGene applyMutation()
      throws InvalidConfigurationException {
    int points = getGPConfiguration().getRandomGenerator().nextInt(7) + 3;
    PolygonConstructor result = new PolygonConstructor(getGPConfiguration(),
        points);
    return result;
  }
}
