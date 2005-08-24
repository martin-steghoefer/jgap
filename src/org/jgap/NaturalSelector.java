/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;
import org.jgap.data.config.*;


/**
 * Abstract implementation of interface INaturalSelector.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class NaturalSelector
    implements INaturalSelector, Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.16 $";

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   *
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  protected abstract void add(Chromosome a_chromosomeToAdd);

  // Default implementation for the Configurable Interface
  /**
   * Default implementation of this method. To be called only for derived class objects.
   * @return a concrete ConfigurationHandler
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public ConfigurationHandler getConfigurationHandler() throws ConfigException {
  	throw new ConfigException("No ConfigurationHandler present for this class");
  }

  /**
   * Default implementation of this method. To be called only for derived class objects.
   * @param name The name of the property.
   * @param value The value of the property.
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public void setConfigProperty(String name, String value)
      throws ConfigException, InvalidConfigurationException {
  	throw new ConfigException("No properties present to be configured.");
  }

  /**
   * Default implementation of this method. To be called only for derived class objects.
   * @param name The name of the property.
   * @param values The different values of the property.
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public void setConfigMultiProperty(String name, ArrayList values)
      throws ConfigException, InvalidConfigurationException {
  	throw new ConfigException("No properties present to be configured.");
  }
  
  
  
  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public class FitnessValueComparator
      implements Comparator {
    public int compare(Object first, Object second) {
      Chromosome chrom1 = (Chromosome) first;
      Chromosome chrom2 = (Chromosome) second;

      if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(chrom2.
          getFitnessValue(), chrom1.getFitnessValue())) {
        return 1;
      }
      else if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(
          chrom1.getFitnessValue(), chrom2.getFitnessValue())) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }

}
