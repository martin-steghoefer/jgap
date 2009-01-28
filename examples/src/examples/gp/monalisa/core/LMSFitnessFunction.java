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

import java.util.logging.*;
import java.awt.image.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Computes the fitness of a program as the Least-Mean-Sqare distance between
 * the image it generates and the target image.
 *
 * @author Yann N. Dauphin
 * @author Klaus Meffert (enhancement)
 * @since 3.4
 */
public class LMSFitnessFunction
    extends GPFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

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
    int numPoints = programRunner.numPoints;
    int numPolygons = programRunner.numPolygons;
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
      int r, g, b;
      if (c2 == -1) {
//        r = 300;
//        g = 300;
//        b = 300;
//        sum += r * r + g * g + b * b;
        sum += 270000;
      }
      else {
        r = ( (c1 >> 16) & 0xff) - ( (c2 >> 16) & 0xff);
        g = ( (c1 >> 8) & 0xff) - ( (c2 >> 8) & 0xff);
        b = (c1 & 0xff) - (c2 & 0xff);
        sum += r * r + g * g + b * b;
      }
    }
    double error = Math.sqrt(sum);
    // Consider malus for number of used points and polygons.
    // ------------------------------------------------------
    GPProgram program = (GPProgram) a_subject;
    ApplicationData data = (ApplicationData)program.getApplicationData();
    data.numPoints = numPoints;
    data.numPolygons = numPolygons;
    double complexityMalus;
    if (error > 250000) {
      complexityMalus = 0;
    }
    else {
      /**
       * 5 Bytes per Polygon
       *   + Delimiter: 1 Byte (-1 for first Polygon)
       *   + Color (R, G, B, Alpha): 4 Bytes (simplified)
       * 2 Bytes per Point:
       *   + X: 1 Byte
       *   + Y: 1 Byte
       */
      complexityMalus = numPoints * 2 + numPolygons * 5 - 1;
    }
    // Consider number of polygons necessary.
    // --------------------------------------
    return error + complexityMalus;
  }
}
