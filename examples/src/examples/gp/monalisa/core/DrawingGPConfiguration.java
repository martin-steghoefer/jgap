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

import java.awt.image.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Encapsulates the settings of the genetic algorithm.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class DrawingGPConfiguration
    extends GPConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  protected BufferedImage m_target = null;

  public DrawingGPConfiguration(BufferedImage a_target)
      throws InvalidConfigurationException {
    this.m_target = a_target;
    this.setSelectionMethod(new TournamentSelector(2));
    this.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    this.setFitnessFunction(new LMSFitnessFunction(this));
    setNodeValidator(new NodeValidator());
    this.setPopulationSize(5);
    this.setStrictProgramCreation(false);
    setProgramCreationMaxTries( -1);
    setMinInitDepth(3);
    setMaxInitDepth(15);
    setInitStrategy(new InitStrategy());
    this.setCrossoverProb(0.3f);
    this.setReproductionProb(0.7f);
    this.setNewChromsPercent(0.1f);
    this.setMutationProb(0.8f);
    this.setMaxCrossoverDepth(100);
  }

  /**
   * Get the target image of the drawing problem.
   *
   * @return the value of target
   */
  public BufferedImage getTarget() {
    return m_target;
  }
}
