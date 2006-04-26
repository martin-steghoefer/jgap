package org.jgap.gp;

import java.util.*;
import org.jgap.gp.*;
import org.jgap.*;
import org.jgap.impl.*;

public class Terminal
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  private double m_value;

  private double m_lowerBounds;

  private double m_upperBounds;

  private boolean m_minmax;

  public Terminal()
      throws InvalidConfigurationException {
    this(Genotype.getConfiguration());
  }

  public Terminal(final Configuration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, 0d, 99d);
//    m_minmax = false;
//    m_lowerBounds = 0;
//    m_upperBounds = 99;
  }

  public Terminal(final Configuration a_conf, double a_minValue,
                  double a_maxValue)
      throws InvalidConfigurationException {
    super(a_conf, 0, null);
    /**@todo min, max considern*/
    m_lowerBounds = a_minValue;
    m_upperBounds = a_maxValue;
    m_minmax = true;
  }

  protected Gene newGeneInternal() {
    try {
      return new Terminal(getConfiguration());
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void setValue(double a_value) {
    m_value = a_value;
  }

  public void evaluate(Configuration config, List parameters) {
    //todo wird res überhaupt benötigt?*/
//    Terminal res = new Terminal();
//    res.setValue(m_value);
   ( (MathConfiguration) config).pushTerminal(new Double(m_value));
  }

  public void applyMutation(int index, double a_percentage) {
    /**@todo implement correctly*/
    /**@todo consider min/max boundaries*/

    /**@todo decide whether adding or subtracting a delta*/

    /**@todo if a_percentage > 0.95 then do mutation not basing on current value
     * --> random value*/

    /**@todo add delta to current value to receive new value*/
    double range = (m_upperBounds - m_lowerBounds) * a_percentage;
    double newValue = m_value +
        (Genotype.getConfiguration().getRandomGenerator().nextDouble() * range);
//    setAllele(new Double(newValue));
    setValue(newValue);
  }

  public String toString() {
    return m_value + "";
  }

  public int execute_int(Chromosome c, int n, Object[] args) {
    return 1;
  }

  public long execute_long(Chromosome c, int n, Object[] args) {
    return 1;
  }

  public float execute_float(Chromosome c, int n, Object[] args) {
    return 1.0f;
  }

  public double execute_double(Chromosome c, int n, Object[] args) {
    return 1.0;
  }

  public Class getChildType(int i) {
    return null;
  }
}
