/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.multiobjective;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Fitness evaluator for multi objectives examples.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class MOFitnessEvaluator
    implements FitnessEvaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Not to be called in multi-objectives context!
   * @param a_fitness_value1 ignored
   * @param a_fitness_value2 ignored
   * @return always a RuntimeException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean isFitter(final double a_fitness_value1,
                          final double a_fitness_value2) {
    throw new RuntimeException("Not supported for multi-objectives!");
  }

  public boolean isFitter(IChromosome a_chrom1, IChromosome a_chrom2) {
    // evaluate values to fill vector of multiobjectives with
    DoubleGene g1 = (DoubleGene)a_chrom1.getGene(0);
    double d = g1.doubleValue();
    double y1 = formula(1, d);
    List l = new Vector();
    l.add(new Double(y1));
    double y2 = formula(2, d);
    l.add(new Double(y2));
    ((Chromosome)a_chrom1).setMultiObjectives(l);

    l.clear();
    g1 = (DoubleGene)a_chrom2.getGene(0);
    d = g1.doubleValue();
    y1 = formula(1, d);
    l.add(new Double(y1));
    y2 = formula(2, d);
    l.add(new Double(y2));
    ((Chromosome)a_chrom2).setMultiObjectives(l);

    List v1 = ( (Chromosome) a_chrom1).getMultiObjectives();
    List v2 = ( (Chromosome) a_chrom2).getMultiObjectives();
    int size = v1.size();
    if (size != v2.size()) {
      throw new RuntimeException("Size of objectives inconsistent!");
    }
    boolean better = false;
    for (int i = 0; i < size; i++) {
      double d1 = ( (Double) v1.get(i)).doubleValue();
      double d2 = ( (Double) v2.get(i)).doubleValue();
      if (d1 > d2) {
        better = true;
      }
      else if (d1 < d2) {
        return false;
      }
    }
    return better;
  }

  private double formula(int a_index, double a_x) {
    if (a_index == 1) {
      return a_x * a_x;
    }
    else {
      return (a_x - 2) * (a_x - 2);
    }
  }
}
