package org.jgap.impl.fitness;

import java.util.*;
import org.jgap.*;

/**
 * Rosenbrock's fitness function.
 *
 * Rosenbrock's function is a good test function in optimization.
 * It has a unique minimum value at the point (1,1).
 * Finding the minimum is a challenge for some algorithms since it
 * has a shallow minimum inside a deeply curved valley.
 *
 * @author Vasilis
 * @since 3.6.2
 */
public class RosenbrocksBulkNegativeFunction
    extends BulkFitnessFunction {
  Configuration config1;

  List<IChromosome> allChromosomesSoFar;

  int numOfDublicates;

  int numOfUniques;

  RosenbrocksBulkNegativeFunction(Configuration config) {
    config1 = config;
    allChromosomesSoFar = new ArrayList<IChromosome> ();
    numOfDublicates = 0;
    numOfUniques = 0;
  }

  public double evaluate(IChromosome ic) {
    double X = (Double) ic.getGene(0).getAllele();
    double Y = (Double) ic.getGene(1).getAllele();
    double function = getFunctionValue(X, Y);
    return function;
  }

  public double getFunctionValue(double X, double Y) {
    return 100 * ( (Y - X * X) * (Y - X * X)) + (1 - X) * (1 - X);
  }

  @Override
  public void evaluate(Population a_subject) {
    Iterator it = a_subject.getChromosomes().iterator();
    while (it.hasNext()) {
      IChromosome a_chrom1 = (IChromosome) it.next();
      double res = evaluate(a_chrom1);
      ( (Chromosome) a_chrom1).setFitnessValueDirectly(res);
      if (allChromosomesSoFar.contains( (Chromosome) a_chrom1)) {
        numOfDublicates++;
      }
      else {
        allChromosomesSoFar.add(a_chrom1);
        numOfUniques++;
      }
    }
  }
}
