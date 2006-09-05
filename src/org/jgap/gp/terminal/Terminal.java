/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.terminal;

import org.jgap.gp.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * A terminal is a static number that can be mutated.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Terminal
    extends CommandGene implements IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  private float m_value_float;

  private double m_value_double;

  private int m_value_int;

  private long m_value_long;

  private double m_lowerBounds;

  private double m_upperBounds;

  public Terminal()
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration(), CommandGene.IntegerClass);
  }

  public Terminal(final GPConfiguration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    this(a_conf, a_returnType, 0d, 99d);
  }

  public Terminal(final GPConfiguration a_conf, Class a_returnType,
                  double a_minValue, double a_maxValue)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType);
    m_lowerBounds = a_minValue;
    m_upperBounds = a_maxValue;
    setRandomValue();
  }

  protected void setRandomValue(int a_value) {
    RandomGenerator randomGen = getGPConfiguration().getRandomGenerator();
    m_value_int = (int) Math.round(randomGen.nextInt() *
                                   (m_upperBounds - m_lowerBounds) +
                                   m_lowerBounds);
  }

  protected void setRandomValue(long a_value) {
    RandomGenerator randomGen = getGPConfiguration().getRandomGenerator();
    m_value_long = Math.round(randomGen.nextLong() *
                              (m_upperBounds - m_lowerBounds) +
                              m_lowerBounds);
  }

  protected void setRandomValue(double a_value) {
    RandomGenerator randomGen = getGPConfiguration().getRandomGenerator();
    m_value_double = randomGen.nextDouble() * (m_upperBounds - m_lowerBounds) +
        m_lowerBounds;
  }

  protected void setRandomValue(float a_value) {
    RandomGenerator randomGen = getGPConfiguration().getRandomGenerator();
    m_value_float = (float) (randomGen.nextFloat() *
                             (m_upperBounds - m_lowerBounds) +
                             m_lowerBounds);
  }

  protected void setRandomValue() {
    Class retType = getReturnType();
    if (retType == CommandGene.FloatClass) {
      setRandomValue(m_value_float);
    }
    else if (retType == CommandGene.IntegerClass) {
      setRandomValue(m_value_int);
    }
    else if (retType == CommandGene.LongClass) {
      setRandomValue(m_value_long);
    }
    else if (retType == CommandGene.DoubleClass) {
      setRandomValue(m_value_double);
    }
    else {
      throw new RuntimeException("unknown terminal type: " + retType);
    }
  }

  public void setValue(double a_value) {
    m_value_double = a_value;
  }

  public void setValue(float a_value) {
    m_value_float = a_value;
  }

  public void setValue(int a_value) {
    m_value_int = a_value;
  }

  public void setValue(long a_value) {
    m_value_long = a_value;
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    // If very high then do mutation not relying on current value
    // random value.
    // ----------------------------------------------------------
    if (a_percentage > 0.85d) {
      setRandomValue();
    }
    else {
      Class retType = getReturnType();
      if (retType == CommandGene.FloatClass) {
        float newValuef;
        float rangef = ( (float) m_upperBounds - (float) m_lowerBounds) *
            (float) a_percentage;
        if (m_value_float >= (m_upperBounds - m_lowerBounds) / 2) {
          newValuef = m_value_float -
              getGPConfiguration().getRandomGenerator().nextFloat() * rangef;
        }
        else {
          newValuef = m_value_float +
              getGPConfiguration().getRandomGenerator().nextFloat() * rangef;
        }
        // Ensure value is within bounds.
        // ------------------------------
        if (Math.abs(newValuef - m_lowerBounds) < DELTA ||
            Math.abs(m_upperBounds - newValuef) < DELTA) {
          setRandomValue(m_value_float);
        }
        else {
          setValue(newValuef);
        }
      }
      else if (retType == CommandGene.DoubleClass) {
        double newValueD;
        double rangeD = (m_upperBounds - m_lowerBounds) * a_percentage;
        if (m_value_double >= (m_upperBounds - m_lowerBounds) / 2) {
          newValueD = m_value_double -
              getGPConfiguration().getRandomGenerator().nextFloat() * rangeD;
        }
        else {
          newValueD = m_value_double +
              getGPConfiguration().getRandomGenerator().nextFloat() * rangeD;
        }
        // Ensure value is within bounds.
        // ------------------------------
        if (Math.abs(newValueD - m_lowerBounds) < DELTA ||
            Math.abs(m_upperBounds - newValueD) < DELTA) {
          setRandomValue(m_value_double);
        }
        else {
          setValue(newValueD);
        }
      }
      else if (retType == CommandGene.IntegerClass) {
        int newValueI;
        double range = (m_upperBounds - m_lowerBounds) * a_percentage;
        if (m_value_int >= (m_upperBounds - m_lowerBounds) / 2) {
          newValueI = m_value_int -
              (int) Math.round(getGPConfiguration().getRandomGenerator().
                               nextInt() * range);
        }
        else {
          newValueI = m_value_int +
              (int) Math.round(getGPConfiguration().getRandomGenerator().
                               nextFloat() * range);
        }
        // Ensure value is within bounds.
        // ------------------------------
        if (newValueI < m_lowerBounds || newValueI > m_upperBounds) {
          setRandomValue(m_value_int);
        }
        else {
          setValue(newValueI);
        }
      }
      else if (retType == CommandGene.LongClass) {
        long newValueL;
        double range = (m_upperBounds - m_lowerBounds) * a_percentage;
        if (m_value_long >= (m_upperBounds - m_lowerBounds) / 2) {
          newValueL = m_value_long -
              Math.round(getGPConfiguration().getRandomGenerator().nextInt() *
                         range);
        }
        else {
          newValueL = m_value_long +
              Math.round(getGPConfiguration().getRandomGenerator().nextFloat() *
                         range);
        }
        // Ensure value is within bounds.
        // ------------------------------
        if (newValueL < m_lowerBounds || newValueL > m_upperBounds) {
          setRandomValue(m_value_long);
        }
        else {
          setValue(newValueL);
        }
      }
    }
    return this;
  }

  public String toString() {
    Class retType = getReturnType();
    if (retType == CommandGene.FloatClass) {
      return "" + m_value_float;
    }
    else if (retType == CommandGene.IntegerClass) {
      return "" + m_value_int;
    }
    else if (retType == CommandGene.LongClass) {
      return "" + m_value_long;
    }
    else if (retType == CommandGene.DoubleClass) {
      return "" + m_value_double;
    }
    else {
      return "unknown terminal type: " + retType;
    }
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return m_value_int;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return m_value_long;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return m_value_float;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return m_value_double;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return null;
  }
}
