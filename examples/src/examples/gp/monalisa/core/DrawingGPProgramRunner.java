/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.monalisa.core;

import java.awt.*;
import java.awt.image.*;

import org.jgap.gp.*;

/**
 * Interprets the commands manifested within a GP Program. Finally, this leads
 * to painting an image from polygons.
 *
 * @author Yann N. Dauphin
 * @author Klaus Meffert;
 * @since 3.4
 */
public class DrawingGPProgramRunner {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private DrawingGPConfiguration m_conf;

  public int numPoints;
  public int numPolygons;

  public DrawingGPProgramRunner(DrawingGPConfiguration a_conf) {
    m_conf = a_conf;
  }

  public BufferedImage run(final IGPProgram a_subject) {
    BufferedImage target = m_conf.getTarget();
    BufferedImage generated = new BufferedImage(target.getWidth(),
        target.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics g2d = generated.getGraphics();
    g2d.setColor(Color.white);
    g2d.fillRect(0, 0, generated.getWidth(), generated.getHeight());
    ApplicationData appData = new ApplicationData();
    appData.graphics = g2d;
    a_subject.setApplicationData(appData);
    a_subject.execute_void(0, null);
    numPoints = appData.numPoints;
    numPolygons = appData.numPolygons;
    return generated;
  }
}
