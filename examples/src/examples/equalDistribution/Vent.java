/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.equalDistribution;

/**
 * Represents a vent with a specific weight.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class Vent {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private double m_weight;

  public Vent(double a_weight) {
    m_weight = a_weight;
  }

  public double getWeight() {
    return m_weight;
  }
}
