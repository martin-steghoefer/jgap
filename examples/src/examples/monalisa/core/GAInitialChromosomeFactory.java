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

import java.awt.image.*;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Creates a suitable initial chromosome.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class GAInitialChromosomeFactory {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Number of points in a polygon.
   */
  public final static int POINTS = 5;

  public static IChromosome create(GAConfiguration a_conf)
      throws InvalidConfigurationException {
    BufferedImage target = a_conf.getTarget();
    IChromosome sample_chromosome = new Chromosome(a_conf);
    Gene[] sample_genes = new Gene[getGenomeSize(a_conf)];
    for (int i = 0; i < getGenomeSize(a_conf); ) {
      // Genes that encode the HSB Color
      for (int c = 0; c < getNumberOfColorGenesPerPolygon() - 1; c++) {
        sample_genes[i] = new IntegerGene(a_conf, 0, 255);
        sample_genes[i++].setToRandomValue(a_conf.getRandomGenerator());
      }
      // Gene that encodes the alpha value of the color
      sample_genes[i] = new IntegerGene(a_conf, 0, 255);
      sample_genes[i++].setAllele(new Integer(0));
      // Genes that encode the position of the points
      for (int j = 0; j < POINTS; j++) {
        sample_genes[i] = new IntegerGene(a_conf, 0, target.getWidth());
        sample_genes[i++].setToRandomValue(a_conf.getRandomGenerator());
        sample_genes[i] = new IntegerGene(a_conf, 0, target.getHeight());
        sample_genes[i++].setToRandomValue(a_conf.getRandomGenerator());
      }
    }
    sample_chromosome.setGenes(sample_genes);
    return sample_chromosome;
  }

  public static int getGenomeSize(GAConfiguration a_conf) {
    return getNumberOfPointGenes(a_conf) + getNumberOfColorGenes(a_conf);
  }

  public static int getNumberOfPointGenes(GAConfiguration a_conf) {
    return a_conf.getMaxPolygons() * (getNumberOfGenesPerPoint() * POINTS);
  }

  public static int getNumberOfColorGenes(GAConfiguration a_conf) {
    return a_conf.getMaxPolygons() * getNumberOfColorGenesPerPolygon();
  }

  public static int getNumberOfGenesPerPolygon() {
    return getNumberOfColorGenesPerPolygon() +
        getNumberOfGenesPerPoint() * POINTS;
  }

  public static int getNumberOfColorGenesPerPolygon() {
    return 4;
  }

  public static int getNumberOfGenesPerPoint() {
    return 2;
  }
}
