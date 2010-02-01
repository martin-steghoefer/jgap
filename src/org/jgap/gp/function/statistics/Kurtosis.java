/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function.statistics;

import org.apache.commons.math.stat.descriptive.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Computes the Kurtosis of the available values.
 * <p>
 * We use the following (unbiased) formula to define kurtosis:</p>
 *  <p>
 *  kurtosis = { [n(n+1) / (n -1)(n - 2)(n-3)] sum[(x_i - mean)^4] / std^4 } - [3(n-1)^2 / (n-2)(n-3)]
 *  </p><p>
 *  where n is the number of values, and std is the
 * Standard Deviation</p>
 *
 * @author Luis Garcia
 * @since 3.5
 */
public class Kurtosis
    extends CommandDynamicArity implements ICloneable {
  public Kurtosis(GPConfiguration a_conf, int a_arityInitial, int a_arityMin,
                  int a_arityMax, Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, a_arityInitial, a_arityMin, a_arityMax, a_returnType);
  }

  @Override
  public String toString() {
    String s = "Kurtosis(";
    int size = size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        s += ";";
      }
      s += "&" + (i + 1);
    }
    return s + ")";
  }

  @Override
  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    int size = size();
    DescriptiveStatistics stats = new DescriptiveStatistics();
    for (int i = 0; i < size; i++) {
      stats.addValue(c.execute_double(n, i, args));
    }
    return stats.getKurtosis();
  }

  @Override
  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    int size = size();
    DescriptiveStatistics stats = new DescriptiveStatistics();
    for (int i = 0; i < size; i++) {
      stats.addValue(c.execute_float(n, i, args));
    }
    return (float) stats.getKurtosis();
  }

  @Override
  public String getName() {
    return "Kurtosis";
  }

  @Override
  public Object clone() {
    try {
      Kurtosis result = new Kurtosis(getGPConfiguration(), getArity(null),
                                     getArityMin(), getArityMax(),
                                     getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
