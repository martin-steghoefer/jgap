/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.multiobjective;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Sample fitness function for the multiobjective problem.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class MultiObjectiveFitnessFunction
    extends BulkFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static final int MAX_BOUND = 4000;

  public static final double MIN_X = -10;

  public static final double MAX_X = 10;

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * returned value, the fitter the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject the population of chromosomes to evaluate
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void evaluate(Population a_subject) {
    Iterator it = a_subject.getChromosomes().iterator();
    while (it.hasNext()) {
      IChromosome a_chrom1 = (IChromosome) it.next();
      // Evaluate values to fill vector of multiobjectives with.
      // -------------------------------------------------------
      List l = new Vector();
      DoubleGene g1 = (DoubleGene) a_chrom1.getGene(0);
      double d = g1.doubleValue();
      l.add(new Double(d));
      double y1 = formula(1, d);
      l.add(new Double(y1));
      double y2 = formula(2, d);
      l.add(new Double(y2));
      ( (Chromosome) a_chrom1).setMultiObjectives(l);
    }
  }

  /**
   * @param a_chrom the chromosome for which to obtain the result it represents
   *
   * @return vector of data for output
   */
  public static Vector<Double> getVector(IChromosome a_chrom) {
    // Fill MO Vector with X (input), output F1(X), output F2(X),
    // difference of |F1(X)| + |F2(X)| from zero
    // ----------------------------------------------------------
    Vector result = new Vector();
    List mo = ( (Chromosome) a_chrom).getMultiObjectives();
    // X as input for F1 and F2
    Double d = (Double) mo.get(0);
    result.add(d);
    // Result F1(X)
    d = (Double) mo.get(1);
    result.add(d);
    // Result F2(X)
    Double d2 = (Double) mo.get(2);
    result.add(d2);
    // Difference from optimum
    result.add(Math.abs(d) + Math.abs(d2));
    return result;
  }

  public static double formula(int a_index, double a_x) {
    if (a_index == 1) {
      // First objective.
      // ----------------
      return a_x * a_x;
    }
    else {
      // Second objective.
      // -----------------
      return (a_x - 2) * (a_x - 2);
    }
  }

  /**
   * @return deep clone of the current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new MultiObjectiveFitnessFunction();
  }
}
