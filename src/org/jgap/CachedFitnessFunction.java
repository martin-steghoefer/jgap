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

import java.util.*;

/**
 * The cached fitness function extends the original FitnessFunction
 * functionality in order to reduce evaluating something twice.
 *
 * @author Dennis Fleurbaaij
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class CachedFitnessFunction
    extends FitnessFunction {
  /**@todo allow to restrict size of cache / age of entries*/
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  // Cache with the previous results
  /**@todo implement trove maps*/
  private Map<String, Double> cachedFitnessValues
      = new HashMap();

  /**
   * Cached fitness value function.
   *
   * @param a_subject the chromosome to evaluate
   * @return fitness value, from cache if available
   *
   * @author Dennis Fleurbaaij
   * @since 3.2
   */
  @Override
  public final double getFitnessValue(final IChromosome a_subject) {
    // Retrieve business key of chromosome.
    // ------------------------------------
    String businessKey = getBusinessKey(a_subject);
    if (businessKey == null) {
      // Caching not possible.
      // ---------------------
      return super.getFitnessValue(a_subject);
    }
    // Evaluate cache.
    // ---------------
    Double fitnessValue = cachedFitnessValues.get(businessKey);
    if (fitnessValue != null) {
      // Return cached result.
      // ---------------------
      return fitnessValue.doubleValue();
    }
    // Compute fitness value for the first time.
    // -----------------------------------------
    double returnValue = super.getFitnessValue(a_subject);
    // Put result into cache.
    // ----------------------
    cachedFitnessValues.put(businessKey, returnValue);
    // Return result.
    // --------------
    return returnValue;
  }

  /**
   * Retrieves the business key of a chromosome instance.
   *
   * @param a_subject the chromosome to retrieve the key for
   * @return the business key of the chromosome
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected String getBusinessKey(IChromosome a_subject) {
    String result;
    Class clazz = a_subject.getClass();
    if (IBusinessKey.class.isAssignableFrom(clazz)) {
      result = ( (IBusinessKey) a_subject).getBusinessKey();
    }
    else if (IPersistentRepresentation.class.isAssignableFrom(clazz)) {
      result = ( (IPersistentRepresentation) a_subject).
                                getPersistentRepresentation();
    }
    else {
      result = null;
    }
    return result;
  }
}
