package org.jgap.impl;

import org.jgap.*;
import java.util.Random;

/**
 * <p>Title: Fitness function always returning the same value</p>
 * <p>Description: Only for testing purpose</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Klaus Meffert
 */

public class StaticFitnessFunction extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int staticFitnessValue;
  public StaticFitnessFunction(int staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }

  public int evaluate(Chromosome chrom) {
    int result = staticFitnessValue;
    return result;
  }
  public int getStaticFitnessValue() {
    return staticFitnessValue;
  }
  public void setStaticFitnessValue(int staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }
}
