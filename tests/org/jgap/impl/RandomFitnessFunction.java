package org.jgap.impl;

import org.jgap.*;
import java.util.Random;

/**
 * <p>Title: Fitness function returning random values</p>
 * <p>Description: Only for testing purpose</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Klaus Meffert
 */

public class RandomFitnessFunction extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Random rand;

  public RandomFitnessFunction() {
    rand = new Random();
  }

  public int evaluate(Chromosome chrom) {
    int result;
    result = rand.nextInt();
    return result;
  }
}