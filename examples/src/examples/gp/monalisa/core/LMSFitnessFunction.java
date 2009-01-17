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

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;

/**
 * Computes the fitness of a program as the Least-Mean-Sqare distance between
 * the image it generates and the target image.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class LMSFitnessFunction
    extends GPFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private final DrawingGPConfiguration m_conf;

  private final DrawingGPProgramRunner programRunner;

  private final int[] targetPixels;

  LMSFitnessFunction(DrawingGPConfiguration a_conf) {
    super();
    m_conf = a_conf;
    programRunner = new DrawingGPProgramRunner(m_conf);
    BufferedImage target = m_conf.getTarget();
    targetPixels = new int[target.getWidth() * target.getHeight()];
    PixelGrabber pg = new PixelGrabber(target, 0, 0, target.getWidth(),
                                       target.getHeight(), targetPixels, 0,
                                       target.getWidth());
    try {
      pg.grabPixels();
    } catch (InterruptedException ex) {
      Logger.getLogger(LMSFitnessFunction.class.getName()).log(Level.SEVERE, null,
          ex);
    }
  }

  @Override
  protected double evaluate(final IGPProgram a_subject) {
    BufferedImage generated = programRunner.run(a_subject);
    final int[] generatedPixels = new int[generated.getWidth() *
        generated.getHeight()];
    PixelGrabber pg = new PixelGrabber(generated, 0, 0, generated.getWidth(),
                                       generated.getHeight(), generatedPixels,
                                       0, generated.getWidth());
    try {
      pg.grabPixels();
    } catch (InterruptedException ex) {
      Logger.getLogger(LMSFitnessFunction.class.getName()).log(Level.SEVERE, null,
          ex);
    }
    double sum = 0;
    for (int i = 0; i < generatedPixels.length && i < targetPixels.length; i++) {
      int c1 = targetPixels[i];
      int c2 = generatedPixels[i];
      int r = ( (c1 >> 16) & 0xff) - ( (c2 >> 16) & 0xff);
      int g = ( (c1 >> 8) & 0xff) - ( (c2 >> 8) & 0xff);
      int b = (c1 & 0xff) - (c2 & 0xff);
      sum += r * r + g * g + b * b;
    }
    return Math.sqrt(sum);
  }
}
