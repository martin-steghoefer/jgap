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
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private List m_parameters;

  private List m_cloneHandlers;

  private ICloneHandler m_defaultCloneHandler;

  public JGAPFactory() {
    m_cloneHandlers = new Vector();
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
   * @return ICloneHandler the clone handler found capable of clone the given
   * class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICloneHandler getCloneHandlerFor(Class a_classToClone) {
    Iterator it = m_cloneHandlers.iterator();
    while (it.hasNext()) {
      ICloneHandler handler = (ICloneHandler) it.next();
      if (handler.isHandlerFor(a_classToClone)) {
        return handler;
      }
    }
    // No registered handler found. Try the DefaultCloneHandler that supports
    // cloning classes implementing the Cloneable interface
    if (m_defaultCloneHandler == null) {
      m_defaultCloneHandler = new DefaultCloneHandler();
    }
    if (m_defaultCloneHandler.isHandlerFor(a_classToClone)) {
      return m_defaultCloneHandler;
    }
    return null;
  }
}
