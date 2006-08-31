/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Base class for GPProgram's. See org.jgap.gp.impl.GPProgram for an
 * implementation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class GPProgramBase
    implements IGPProgram, Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private double m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;

  private GPConfiguration m_conf;

  public GPProgramBase(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    if (a_conf == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_conf = a_conf;
  }

  public GPConfiguration getGPConfiguration() {
    return m_conf;
  }

  /**
   * Compares this entity against the specified object.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * @return fitness value of this chromosome determined via the registered
   * fitness function
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double calcFitnessValue() {
    GPFitnessFunction normalFitnessFunction = getGPConfiguration().
        getGPFitnessFunction();
    if (normalFitnessFunction != null) {
      // Grab the "normal" fitness function and ask it to calculate our
      // fitness value.
      // --------------------------------------------------------------
      m_fitnessValue = normalFitnessFunction.getFitnessValue(this);
    }
    return m_fitnessValue;
  }

  public double getFitnessValue() {
    if (m_fitnessValue >= 0.000d) {
      return m_fitnessValue;
    }
    else {
      return calcFitnessValue();
    }
  }

  public void setFitnessValue(double a_fitness) {
    m_fitnessValue = a_fitness;
  }

}
