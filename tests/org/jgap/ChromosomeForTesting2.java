/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * Derived Chromosome class for testing purposes only.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class ChromosomeForTesting2
    extends Chromosome {
  private boolean m_isCloned;

  // Default constructor needed for construction via newInstance()
  public ChromosomeForTesting2()
      throws InvalidConfigurationException {
    super(Genotype.getStaticConfiguration());
  }

  public ChromosomeForTesting2(Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config);
  }

}
