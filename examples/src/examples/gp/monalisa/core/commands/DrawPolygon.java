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
 * Draws a polygon with a certain fill color.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class DrawPolygon
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public DrawPolygon(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 2, Void.class);
  }

  @Override
  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    Graphics2D g2d = getGraphics2D(a_chrom);
    Color color = (Color) a_chrom.execute_object(a_n, 0, a_args);
    Polygon polygon = (Polygon) a_chrom.execute_object(a_n, 1, a_args);
    g2d.setColor(color);
    g2d.fillPolygon(polygon);
  }

  @Override
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (a_chromNum == 0) {
      return Color.class;
    }
    return Polygon.class;
  }

  public Graphics2D getGraphics2D(ProgramChromosome a_chrom) {
    if ( (Graphics2D) a_chrom.getIndividual().getApplicationData() == null) {
      throw new UnsupportedOperationException(
          "Application data not provided to individual.");
    }
    return (Graphics2D) a_chrom.getIndividual().getApplicationData();
  }

  @Override
  public String toString() {
    return "DrawPolygon(&1, &2)";
  }
}
