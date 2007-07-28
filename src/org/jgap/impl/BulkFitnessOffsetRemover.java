/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**
 * Takes away the fitness offset of the population to evolve.
 * The fitness function values of the population of {@link org.jgap.IChromosome}
 * instances will start from a minimum of 1 afterwards.
 * </p>
 * <p>
 * The removal of an offset in the fitness values of a population strengthens
 * the "survival of the fittest" effect of a selector that performs selection
 * upon fitness values. A high offset in the fitness values of a population
 * lowers the relative difference between the fitness values of the Chromosomes
 * in a population.
 * </p>
 * <h3>Example of applicability</h3>
 * <p>
 * You are optimizing a black box with <i>n</i> parameters that are mapped
 * to {@link org.jgap.IChromosome} instances each having <i>n</i>
 * {@link org.jgap.Gene} instances.<br>
 * You want to minimize the answer time of the black box and provide
 * a {@link org.jgap.FitnessFunction#evaluate(org.jgap.IChromosome)}
 * that takes the genes out of the chromosome, put's it's
 * {@link org.jgap.Gene#getAllele()}
 * values to the parameters and measures the answer time of the black box
 * (by invoking it's service to optimize). <br>
 * The longer the time takes, the worse it's fitness is, so you have to
 * invert the measured times to fitness values:
 * <a name="bboptimizer"/>
 * <pre>
 * <font color="#0011EE">
 * class BlackBoxOptimizer extends org.jgap.FitnessFunction{
 *   private BlackBox bbox;
 *   <font color="#999999">//Additional code: constructors</font>
 *   <font color="#999999">...</font>
 *   public double evaluate(org.jgap.IChromosome chromosome){
 *     double fitness = 0;
 *     <font color="#999999">// get the Gene[] & put the parameters into the box.
 *     ...
 *     </font>
 *     long duration = System.currentTimeMillis();
 * <font color="#999999">
 * // You certainly will use an advanced StopWatch...</font>
 *     this.bbox.service();
 * <font color="#999999">// The black boxes service to optimize.</font>
 *     duration = System.currentTimeMillis()-duration;
 *     <font color="#999999">// transform the time into fitness value:</font>
 *     fitness = double.MAX_VALUE - (double)duration;
 *     return fitness;
 *   }
 * }
 * </font>
 * </pre>
 * </p>
 * <p>
 * <h4>We might get the following results (each row stands for a Chromosome,
 * the table is a population):</h4>
 * <table border="1">
 * <tr align="left" valign="top">
 * <th>
 * duration
 * </th>
 * <th>
 * fitness
 * </th>
 * <th>
 * piece of fitness cake
 * </th>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 2000
 * </td>
 * <td>
 * 9218868437227403311
 * </td>
 * <td>
 * 33.333333333333336949106088992532 %
 * </td>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 3000
 * </td>
 * <td>
 * 9218868437227402311
 * </td>
 * <td>
 * 33.333333333333333333333333333333 %
 * </td>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 4000
 * </td>
 * <td>
 * 9218868437227401311
 * </td>
 * <td>
 * 33.333333333333329717560577674135 %
 * </td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * If any {@link org.jgap.NaturalSelector} performs selection based upon the
 * fitness values, it will have to put those values in relation to each other.
 * As a fact, the probability to select the Chromosome that contained the black
 * box parameters that caused an answer time of 4000 ms is "equal" to the
 * probability to select the Chromosome that caused a black box answer time to
 * be 2000 ms!
 * </p>
 * <p>
 * Of course one could work around that problem by replacing the
 * <tt>Integer.MAX_VALUE</tt> transformation by a fixed maximum value the black
 * box would need for the service.
 * But what, if you have no guaranteed maximum answer time for the service of
 * the black box ?
 * Even if you have got one, it will be chosen sufficently high above the
 * average answer time thus letting your fitness function return values with a
 * high offset in the fitness.
 * </p>
 * <p>
 * <h4>This is, what happens, if you use this instance for fitness
 * evaluation:</h4>
 * <table border="1">
 * <tr align="left" valign="top">
 * <th>
 * duration
 * </th>
 * <th>
 * fitness
 * </th>
 * <th>
 * piece of fitness cake
 * </th>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 2000
 * </td>
 * <td>
 * 2001
 * </td>
 * <td>
 * 66.63 %
 * </td>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 3000
 * </td>
 * <td>
 * 1001
 * </td>
 * <td>
 * 33.33 %
 * </td>
 * </tr>
 * <tr align="left" valign="top">
 * <td>
 * 4000
 * </td>
 * <td>
 * 1
 * </td>
 * <td>
 * 0.03 %
 * </td>
 * </tr>
 * </table>
 * </p>
 * <h3>Example of usage</h3>
 *
 * <p>
 * This example shows how to use this instance for cutting fitness offsets.
 * It is the same example as used <a href="#bboptimizer">above</a>.
 * <pre>
 * <font color="#0011EE">
 * class BlackBoxOptimizer extends org.jgap.FitnessFunction{
 *   <font color="#999999">// Additional code: constructors
 *   ...</font>
 *   public double evaluate(org.jgap.IChromosome chromosome){
 *     <font color="#999999">.... // As shown above.</font>
 *   }
 *
 *   public void startOptimization(org.jgap.Configuration gaConf)
 *       throws InvalidConfigurationException{
 *     <font color="#999999">
 *     // The given Configuration may be preconfigured with
 *     // NaturalSelector & GeneticOperator instances,.
 *     // But should not contain a FitnessFunction or BulkFitnessFunction!
 * </font>
 *     <b>gaConf.setBulkFitnessFunction(new BulkFitnessOffsetRemover(this));</b>
 *     <font color="#999999">// Why does it work? We implement FitnessFunction!
 *     // Still to do here:
 *     // - Create a sample chromosome according to your blackbox & set it to
 *      //   the configuration.
 *     // - Create a random inital Genotype.
 *     // - loop over a desired amount of generations invoking
 *      //   aGenotype.evolve()..</font>
 *   }
 * }
 * </font>
 * </pre>
 * </p>
 *
 * @author Achim Westermann
 * @since 2.2
 *
 */
public class BulkFitnessOffsetRemover
    extends BulkFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /*
   * Replace this member by the Configuration as soon as Configuration allows
   * bulk fitness function and fitness function to be stored both in it.
   */
  private FitnessFunction m_ff;

  /**@todo This constructor is planned but not possible yet,
   * as the Configuration permits bulk fitness function and
   * simple fitness function both existing in it at the same time.
   */
  /*
    public BulkFitnessOffsetRemover(Configuration conf){
   if(m_activeConfiguration)
    }
   */
  /**
   * <p>
   * The last generations offset.
   * This has to be stored because Chromosomes that were put into the new
   * generation's candidate list already have the fitness value without offset
   * from their previous evaluation.
   * </p>
   * <P>
   * We try to avoid evaluations of the fitness function
   * as it might be expensive, so we reuse fitness values.
   * If a Chromosome already has a fitness value >0 this
   * previousOffset is added to it's fitness to allow
   * comparing this Chromosome's fitness with newly
   * evaluated Chromosomes (which still have the offset from the evaluation).
   * </p>
   */
  private double m_previousOffset;

  public BulkFitnessOffsetRemover(final FitnessFunction a_ff) {
    if (a_ff == null) {
      throw new IllegalArgumentException("Fitness function must not be null!");
    }
    m_ff = a_ff;
  }

  /* (non-Javadoc)
   * @see org.jgap.BulkFitnessFunction#evaluate(org.jgap.Chromosome[])
   */
  public void evaluate(final Population a_chromosomes) {
    double offset = Double.MAX_VALUE;
    double curFitness;
    Iterator itChromosomes = a_chromosomes.iterator();
    IChromosome chromosome;
    while (itChromosomes.hasNext()) {
      chromosome = (IChromosome) itChromosomes.next();
      /*
       * This is a workaround:
       * We have to check, wethter a Chromosome has
       * already been evaluated, because it may be too
       * expensive to unconditionally evaluate each Chromosome.
       * We use the "caching of fitness values" of the Chromosome.
       * But in the current Configuration,
       * there is no fittnessFunction: We have a bulk fitness function
       * assigned.
       * Look at the code of Chromosome.getFitnessValue():
       * In that case, a negative value will be returned.
       * If a redesign of that method is made, this has to be changed
       * here too.. .
       */
      curFitness = chromosome.getFitnessValueDirectly();
      if (curFitness < 0) {
        // OK, get it from our fitness function.
        curFitness = m_ff.getFitnessValue(chromosome);
        // And store it to avoid evaluation of the same Chromosome again:
        chromosome.setFitnessValue(curFitness);
      }
      else {
        /*
         * Those have fitness values already without offset
         * of the previous evaluation.
         * Reattach it to allow comparison.
         * Else these Chromosomes would be the unfittest and
         * additionally disallow cutting a huge offset from the others.
         */
        curFitness += m_previousOffset;
        chromosome.setFitnessValue(curFitness);
      }
      // search for the offset that is to be cut:
      offset = (offset < curFitness) ? offset : curFitness;
    }
    /*
     * Now we have the classical evaluated Chromosomes
     * and the minimum fitness.
     * It would be easy to simply subtract that value from
     * each Chromosomes fitness. But in that case the
     * unfittest Chromosome would possible have no chance to survive.
     * In a WeightedRouletteSelector it would not get
     * a single slot to be selected as it's fitness value
     * would be zero.
     * So we have to leave at least a fitness value of 1.
     * Ups, if forgot: Neil throws exceptions, whenever a
     * fitness value is below 1 and also does not accept
     * assignment of fitness values <1. Ok, so he ensures
     * that every Chromosome may survive...
     */
    offset--;
    m_previousOffset = offset;
    // offset cannot be <0... thx to fitness value policy of jgap.
    // finally remove the offset from every fitness value:
    itChromosomes = a_chromosomes.iterator();
    while (itChromosomes.hasNext()) {
      chromosome = (IChromosome) itChromosomes.next();
      chromosome.setFitnessValue(chromosome.getFitnessValue() - offset);
    }
  }

  /**
   * <p>
   * Using this instance to remove the fitness offset in the
   * populations brings the advantage of getting a selection
   * more sensitive to the differences of fitness of the chromosomes.
   * </p>
   * <p>
   * The disadvantage is, that the fitness values are modified.
   * The modification is good for jgap's selection method but
   * bad for the guys that want to see the success of your
   * work, or need a proof that a GA improves over time:
   * <br>
   * The value of {@link org.jgap.Genotype#getFittestChromosome()}
   * does not seem to increase over the generations. Most often
   * it becomes worse. This is caused by the fact, that all
   * Chromosomes are getting better over time
   * (the fitness interval of all Chromosomes gets narrower) and
   * the offset that may be cut becomes bigger.
   * </p>
   * <p>
   * If you want to get an absolute value independant from the
   * offset that is cut off from the chromosome's fitness value,
   * this method has to be used.
   * </p>
   * <p>
   * Stop reading here because a
   * </p>
   * <h4>Mathematical Proof</h4>
   * <p>
   * is following.
   * How can it work to get the absolute value for all
   * Chromosomes fitness values? Some Chromosomes may have lived
   * for many generations and everytime their fitness was
   * evaluated here, the old offset was added and a new one was
   * calculated and subtracted from the fitness value.
   * </p>
   * <p>
   * Each bulk fitness evaluation a Chromosome experiences,
   * it's fitness value <i>F</i> get's an addition of the old offset
   * <i>O<sub>(n-1)</sub></i>
   * and a substraction by the new offset <i>O<sub>n</sub></i>.<br>
   * <i><sub>n</sub></i> is the generation index.
   *
   * <pre>
   * F<sub>1</sub> = F<sub>0</sub> + O<sub>0</sub> - O<sub>1</sub>
   * F<sub>2</sub> = F<sub>1</sub> + O<sub>1</sub> - O<sub>2</sub>
   * F<sub>3</sub> = F<sub>2</sub> + O<sub>2</sub> - O<sub>3</sub>
   *
   * =>
   *
   * 1) F<sub>n</sub> = <b>F<sub>(n-1)</sub></b>
   * + O<sub>(n-1)</sub> - O<sub>n</sub>
   *
   * 2) <b>F<sub>(n-1)</sub></b> = F<sub>(n-2)</sub>
   * + O<sub>(n-2)</sub> - O<sub>(n-1)</sub>
   *
   * 2 in 1)
   *    F<sub>n</sub> = (F<sub>(n-2)</sub> + O<sub>(n-2)</sub>
   * - O<sub>(n-1)</sub>) + O<sub>(n-1)</sub> - O<sub>n</sub>
   *    F<sub>n</sub> = F<sub>(n-2)</sub> + O<sub>(n-2)</sub> - O<sub>n</sub>
   *
   * We made a step over 2 generations: With the current offset and the
   * fitness & offset of the
   * "preprevious" generation we can calculate the current fitness.
   * We can assume that this generation stepping works for farer steps
   * <sub>m</sub> (just continue step 2) until you have a generation step value
   * high enough ;-))
   *
   * => F<sub>n</sub> = F<sub>(n-m)</sub> + O<sub>(n-m)</sub> - O<sub>n</sub>
   *
   * We want to get the original absolute value of fitness:
   *
   * 3) m := n
   *
   * => F<sub>n</sub> = F<sub>0</sub> + O<sub>0</sub> - O<sub>n</sub>
   *
   * solved to F<sub>0</sub> our original value:
   *
   * F<sub>0</sub> = F<sub>n</sub> + O<sub>n</sub> - O<sub>0</sub>
   *
   * And our initial offset {@link #m_previousOffset O<sub>0</sub>} is zero!
   * </pre>
   * </p>
   * <p>
   * This shows, that it is possible to compute the original fitness value of a
   * Chromosome from it's current fitness value and the
   * {@link #m_previousOffset previous offset}
   * regardless of the amounts of generations between original evaluation and
   * the current generation.
   * </p>
   * @param a_individuum any Chromosome that is normally being evaluated by
   * this <tt>BulkFitnessFunction</tt>
   * @return the original fitness value as returned by the registered
   * {@link #m_ff fitnessFunction} instance.
   */
  public double getAbsoluteFitness(final IChromosome a_individuum) {
    double fitness = a_individuum.getFitnessValue();
    if (fitness < 0.0) {
      // OK, get it from our fitness function.
      fitness = m_ff.getFitnessValue(a_individuum);
      // And store it to avoid evaluation of the same Chromosome again:
      a_individuum.setFitnessValue(fitness);
    }
    else {
      /*
       * Those have fitness values already without offset
       * of the previous evaluation.
       * Reattach it to allow comparison.
       * Else these Chromosomes would be the unfittest and
       * additionally disallow cutting a huge offset from the others.
       */
      fitness += m_previousOffset;
    }
    return fitness;
  }

  /**
   * @return deep clone of current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    FitnessFunction ff = (FitnessFunction)m_ff.clone();
    BulkFitnessOffsetRemover result = new BulkFitnessOffsetRemover(ff);
    return result;
  }
}
