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

import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * Ready-to-go Implementation of org.jgap.Configuration with all important
 * parameters already set.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class ConfigurationForTesting
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public final static double STATIC_FITNESS_VALUE = 2.3d;

  /**
   * Default constructor.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public ConfigurationForTesting()
      throws InvalidConfigurationException {
    super();
    setPopulationSize(5);
    reset();
    setFitnessFunction(new StaticFitnessFunction(STATIC_FITNESS_VALUE));
    setEventManager(new EventManager());
    setFitnessEvaluator(new DefaultFitnessEvaluator());
    addNaturalSelector(new BestChromosomesSelector(this), true);
    addGeneticOperator(new MutationOperator(this, new DefaultMutationRateCalculator(this)));
    setRandomGenerator(new StockRandomGenerator());
    Gene[] genes = new Gene[3];
    Gene gene = new BooleanGene(this);
    genes[0] = gene;
    gene = new StringGene(this, 1,10,StringGene.ALPHABET_CHARACTERS_LOWER);
    genes[1] = gene;
    Object appData = new MyAppData("TEST123");
    gene.setApplicationData(appData);
    gene = new IntegerGene(this, 100,300);
    genes[2] = gene;
    Chromosome chrom = new Chromosome(this, genes);
    setSampleChromosome(chrom);
  }

  /**
   * Allows to set the random generator freely, also to null (normally
   * forbidden).
   * @param a_generatorToSet the random generator to set
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public synchronized void setRandomGenerator(RandomGenerator a_generatorToSet)
      throws InvalidConfigurationException {
    try {
      junitx.util.PrivateAccessor.setField(this, "m_randomGenerator",
                                           a_generatorToSet);
    }
    catch (NoSuchFieldException nex) {
      throw new InvalidConfigurationException(nex.getMessage());
    }
  }

  public class MyAppData
      implements Cloneable, java.io.Serializable {
    private String m_value;

    public MyAppData(String a_value) {
      m_value = a_value;
    }

    public String getValue() {
      return m_value;
    }

    public int compareTo(Object a_o) {
      MyAppData other = (MyAppData)a_o;
      return m_value.compareTo(other.m_value);
    }

    public Object clone() {
      return new String(m_value);
    }
  }
}
