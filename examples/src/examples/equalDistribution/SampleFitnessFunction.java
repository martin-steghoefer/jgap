/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.equalDistribution;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Fitness function for our example. See method evaluate(..).
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SampleFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Vent[] m_vents;

  public SampleFitnessFunction(Vent[] a_vents) {
    m_vents = a_vents;
  }

  /**
   * Calculates the difference in weight between the 8 groups of vents. The
   * lower the different the better the solution.
   *
   * @param a_subject the Chromosome to be evaluated
   * @return fitness of our problem (smaller is better here)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public double evaluate(IChromosome a_subject) {
    double[] groupWeights = new double[8];
    double squaredDiff = 0.0d;
    for (int i = 0; i < 8; i++) {
      double groupWeight = 0.0d;
      for (int j = 0; j < 8; j++) {
        IntegerGene ventIndex = (IntegerGene) a_subject.getGene( (i * 8 + j));
        Vent vent = (Vent) m_vents[ventIndex.intValue()];
        groupWeight += vent.getWeight();
      }
      if (i > 0) {
        for (int k = 0; k < i; k++) {
          double diff = Math.abs(groupWeight - groupWeights[k]);
          squaredDiff += diff * diff;
        }
      }
      groupWeights[i] = groupWeight;
    }
    return squaredDiff;
  }
}
