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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.jgap.gp.IGPProgram;

/**
 * Interprets the commands manifested within a GP Program. Finally, this leads
 * to painting an image from polygons.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class DrawingGPProgramRunner {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private DrawingGPConfiguration m_conf = null;

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
    a_subject.setApplicationData(g2d);
    a_subject.execute_void(0, null);
    return generated;
  }
}
