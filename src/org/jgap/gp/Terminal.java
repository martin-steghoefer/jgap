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

import org.jgap.gp.*;
import org.jgap.*;

/**
 * A terminal having no children. Practically, it may be a static number.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Terminal
    extends CommandGene
    implements Mutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  private String m_value;

  private double m_lowerBounds;

  private double m_upperBounds;

  public Terminal()
      throws InvalidConfigurationException {
    this(Genotype.getConfiguration(), CommandGene.IntegerClass);
  }

  public Terminal(final Configuration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    this(a_conf, 0d, 99d, a_returnType);
  }

  public Terminal(final Configuration a_conf, double a_minValue,
                  double a_maxValue, Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType);
    m_lowerBounds = a_minValue;
    m_upperBounds = a_maxValue;
    setRandomValue();
  }

  protected void setRandomValue() {
    RandomGenerator randomGen = getConfiguration().getRandomGenerator();
    m_value = new Long(Math.round(randomGen.nextDouble() *
                                  (m_upperBounds - m_lowerBounds) +
                                  m_lowerBounds)).toString();
  }

  protected Gene newGeneInternal() {
    try {
      return new Terminal(getConfiguration(), getReturnType());
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void setValue(double a_value) {
    if (isIntegerType()) {
      m_value = new Long(Math.round(a_value)).toString();
    }
    else if (isFloatType()){
      m_value = Double.toString(a_value);
    }
    else {
      throw new UnsupportedOperationException("Setting a value for type "
                                              + getReturnType()
                                              +
          " is not supported with Terminal!");
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // If very high then do mutation not relying on current value
    // random value.
    // ----------------------------------------------------------
    if (a_percentage > 0.85d) {
      setRandomValue();
    }
    else {
      double range = (m_upperBounds - m_lowerBounds) * a_percentage;
      double newValue;
      double value = Double.parseDouble(m_value);
      if (value >= (m_upperBounds - m_lowerBounds) / 2) {
          newValue = value -
              (getConfiguration().getRandomGenerator().nextDouble() * range);
      }
      else {
        newValue = value +
            (getConfiguration().getRandomGenerator().nextDouble() * range);
      }
      // Ensure value is within bounds
      if (newValue < m_lowerBounds || newValue > m_upperBounds) {
        setRandomValue();
      }
      else {
        setValue(newValue);
      }
    }
  }

  public String toString() {
    return "terminal(" + m_value + ")";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return Integer.parseInt((String)m_value);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return Long.parseLong((String)m_value);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return Float.parseFloat((String)m_value);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return Double.parseDouble(m_value);
  }

  public Class getChildType(int i) {
    return null;
  }
}
