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

import java.io.*;
import java.util.*;

/**
 * Interface for central factory, see JGAPFactory.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IJGAPFactory extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.8 $";

  void setParameters(Collection a_parameters);

  /**
   * @return newly created instance of a RandomGenerator
   */
  RandomGenerator createRandomGenerator();

  /**
   * Retrieves a clone handler capable of clone the given class.
   * @param a_obj the object to clone (maybe null)
   * @param a_classToClone the class to clone an object of
   * @return the clone handler found capable of clone the given class, or null
   * if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  ICloneHandler getCloneHandlerFor(Object a_obj, Class a_classToClone);

  /**
   * Registers a clone handler that could be retrieved by
   * getCloneHandlerFor(Class).
   * @param a_cloneHandler the ICloneHandler to register
   * @return index of the added clone handler, needed when removeCloneHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  int registerCloneHandler(ICloneHandler a_cloneHandler);

  /**
   * Retrieves an initializer capable of initializing the Object of the given
   * class.
   * @param a_obj the object to init (maybe null)
   * @param a_objToInit the object class to init
   * @return the initializer found capable of initializing an object of the
   * given class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  IInitializer getInitializerFor(Object a_obj, Class a_objToInit);

  /**
   * Registers an initializer that could be retrieved by
   * getInitializerFor(Class).
   * @param a_chromIniter the IChromosomeInitializer to register
   * @return index of the added initializer, needed when
   * removeChromosomeInitializer will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  int registerInitializer(IInitializer a_chromIniter);

  void setGeneticOperatorConstraint(IGeneticOperatorConstraint
                                    a_constraint);

  IGeneticOperatorConstraint getGeneticOperatorConstraint();

  /**
   * Retrieves a handler capable of comparing two instances of the given class.
   * @param a_obj the object to compare (maybe null)
   * @param a_classToCompareTo the class instances to compare (maybe null)
   * @return the handler found capable of comparing instances
   * of the given class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  ICompareToHandler getCompareToHandlerFor(Object a_obj,
                                           Class a_classToCompareTo);

  /**
   * Registers a compareTo-handler that could be retrieved by
   * getCompareToHandlerFor(Class).
   * @param a_compareToHandler the ICompareToHandler to register
   * @return index of the added handler, needed when removeCompareToHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */

  int registerCompareToHandler(ICompareToHandler a_compareToHandler);
}
