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
import org.jgap.util.*;
import examples.gp.monalisa.core.*;

/**
 * Draws a polygon with a certain fill color.
 *
 * @author Yann N. Dauphin
 * @author Klaus Meffert (statistics data)
 * @since 3.4
 */
public class DrawPolygon
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public DrawPolygon(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 2, Void.class);
  }

  @Override
  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    ApplicationData appData = getAppData(a_chrom);
    Graphics2D g2d = (Graphics2D) appData.graphics;
    Color color = (Color) a_chrom.execute_object(a_n, 0, a_args);
    Polygon polygon = (Polygon) a_chrom.execute_object(a_n, 1, a_args);
    g2d.setColor(color);
    g2d.fillPolygon(polygon);
    //Update statistics data.
    // ----------------------
    appData.numPoints += polygon.npoints;
    appData.numPolygons += 1;
  }

  @Override
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (a_chromNum == 0) {
      return Color.class;
    }
    return Polygon.class;
  }

  public ApplicationData getAppData(ProgramChromosome a_chrom) {
    ApplicationData appData = (ApplicationData) a_chrom.getIndividual().
        getApplicationData();
    return appData;
  }

  @Override
  public String toString() {
    return "DrawPolygon(&1, &2)";
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4.1
   */
  public Object clone() {
    try {
      DrawPolygon result = new DrawPolygon(getGPConfiguration());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
