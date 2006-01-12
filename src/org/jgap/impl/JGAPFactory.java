/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**
 * Central factory for creating default objects to use, e.g. random generators.
 * Could be made configurable. An instance of JGAPFactory can be acces via<p>
 * <code><Configuration>.getJGAPFactory();</code><p>
 * To use your own factory class instead, use:<p>
 * <code>
 * System.setProperty(Configuration.PROPERTY_JGAPFACTORY_CLASS, "myFactory");<p>
 * </code>
 * with "myFactory" representing the name of your class to use.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class JGAPFactory
    implements IJGAPFactory {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private List m_parameters;

  private List m_cloneHandlers;

  private List m_initer;

  private ICloneHandler m_defaultCloneHandler;

  private IInitializer m_defaultIniter;

  public JGAPFactory() {
    m_cloneHandlers = new Vector();
    m_initer = new Vector();
  }

  /**
   * Allows setting (generic because unknown) parameters for creating objects
   * @param a_parameters Collection
   */
  public void setParameters(Collection a_parameters) {
    m_parameters = new Vector(a_parameters);
  }

  public RandomGenerator createRandomGenerator() {
    return new StockRandomGenerator();
  }

  /**
   * Registers a clone handler that could be retrieved by
   * getCloneHandlerFor(Class)
   * @param a_cloneHandler the ICloneHandler to register
   * @return index of the added clone handler, needed when removeCloneHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int registerCloneHandler(ICloneHandler a_cloneHandler) {
    m_cloneHandlers.add(a_cloneHandler);
    return m_cloneHandlers.size() - 1;
  }

  /**
   * Removes a clone handler at a given index (which is obtained from
   * registerCloneHandler
   * @param a_index the index of the clone handler to remove
   * @return the removed ICloneHandler, or null if not successfull
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICloneHandler removeCloneHandler(int a_index) {
    return (ICloneHandler) m_cloneHandlers.remove(a_index);
  }

  /**
   * Retrieves a clone handler capable of clone the given class
   * @param a_classToClone the class to clone an object of
   * @return the clone handler found capable of clone the given
   * class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICloneHandler getCloneHandlerFor(Object a_obj, Class a_classToClone) {
    Iterator it = m_cloneHandlers.iterator();
    while (it.hasNext()) {
      ICloneHandler handler = (ICloneHandler) it.next();
      if (handler.isHandlerFor(a_obj, a_classToClone)) {
        return handler;
      }
    }
    // No registered handler found. Try the DefaultCloneHandler that supports
    // cloning classes implementing the Cloneable interface
    if (m_defaultCloneHandler == null) {
      m_defaultCloneHandler = new DefaultCloneHandler();
    }
    if (m_defaultCloneHandler.isHandlerFor(a_obj, a_classToClone)) {
      return m_defaultCloneHandler;
    }
    return null;
  }


  /**
   * Registers an initializer that could be retrieved by getInitializerFor(Class)
   * @param a_chromIniter the IChromosomeInitializer to register
   * @return index of the added initializer, needed when
   * removeChromosomeInitializer will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int registerInitializer(IInitializer a_chromIniter) {
    m_initer.add(a_chromIniter);
    return m_initer.size() - 1;
  }

  /**
   * Removes an initializer at a given index (which is obtained from
   * registerInitializer
   * @param a_index the index of the initializer to remove
   * @return the removed IInitializer, or null if not successfull
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public IInitializer removeInitializer(int a_index) {
    return (IInitializer) m_initer.remove(a_index);
  }

  /**
   * Retrieves an initializer capable of initializing the Object of the given
   * class
   * @param a_objToInit the object class to init
   * @return  the initializer found capable of initializing an object of the
   * given class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public IInitializer getInitializerFor(Object a_obj, Class a_objToInit) {
    Iterator it = m_initer.iterator();
    while (it.hasNext()) {
      IInitializer initer = (IInitializer) it.next();
      if (initer.isHandlerFor(a_obj, a_objToInit)) {
        return initer;
      }
    }
    // No registered handler found. Try the DefaultInitializer
    // that supports init for Chromosomes
    // via Chromosome.randomInitialChromosome()
    if (m_defaultIniter == null) {
      m_defaultIniter = new DefaultInitializer();
    }
    if (m_defaultIniter.isHandlerFor(a_obj, a_objToInit)) {
      return m_defaultIniter;
    }
    return null;
  }
}
