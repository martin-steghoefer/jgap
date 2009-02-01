/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.monalisa.core;

import java.util.logging.*;

import java.awt.image.*;

import org.jgap.*;

/**
 * Computes the fitness of a program as the Least-Mean-Sqare distance between
 * the image it generates and the target image.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class LMSFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final GAConfiguration m_conf;

  private final int[] targetPixels;

  LMSFitnessFunction(GAConfiguration a_conf) {
    super();
    m_conf = a_conf;
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
  protected double evaluate(IChromosome a_chromosome) {
    BufferedImage generated = m_conf.getPhenotypeExpresser().express(
        a_chromosome);
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
