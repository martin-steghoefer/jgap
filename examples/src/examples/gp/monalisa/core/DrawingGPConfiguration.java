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
 * @author Klaus Meffert
 * @since 3.4
 */
public class DrawingGPConfiguration
    extends GPConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  protected BufferedImage m_target = null;

  public DrawingGPConfiguration(BufferedImage a_target)
      throws InvalidConfigurationException {
    m_target = a_target;
    setSelectionMethod(new TournamentSelector(2));
    setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    setFitnessFunction(new LMSFitnessFunction(this));
    setNodeValidator(new NodeValidator());
    setPopulationSize(5);
    setStrictProgramCreation(false);
    setProgramCreationMaxTries( -1);
    setMinInitDepth(3);
    setMaxInitDepth(14);
    setInitStrategy(new InitStrategy());
    setCrossoverProb(0.3f); //0.9
    setReproductionProb(0.7f); //0.1
    setNewChromsPercent(0.25f); //0.4
    setMutationProb(0.8f);
    setMaxCrossoverDepth(45);
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
