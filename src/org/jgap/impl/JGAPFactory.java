/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.util.*;
import org.apache.commons.lang.builder.*;

/**
 * Central factory for creating default objects to use, e.g. random generators.
 * Could be made configurable. An instance of JGAPFactory can be accessed via<p>
 * <code>Genotype.getConfiguration().getJGAPFactory();</code><p>
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
    implements IJGAPFactory, Serializable, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  private List m_parameters;

  private List m_cloneHandlers;

  private List m_initer;

  private List m_compareHandlers;

  private ICloneHandler m_defaultCloneHandler;

  private IInitializer m_defaultIniter;

  private ICompareToHandler m_defaultComparer;

  private IGeneticOperatorConstraint m_geneticOpConstraint;

  private transient LRUCache m_cache;

  private boolean m_useCaching;

  private Map<String,Long> m_lastKeys;

  public JGAPFactory(boolean a_useCaching) {
    m_initer = new Vector();
    m_cache = new LRUCache(50);
    m_useCaching = a_useCaching;
    m_cloneHandlers = new Vector();
    m_compareHandlers = new Vector();
    // Construct default handlers at the beginning to avoid multi-threading
    // conflicts in getXXXFor methods.
    m_defaultCloneHandler = new DefaultCloneHandler();
    m_defaultIniter = new DefaultInitializer();
    m_defaultComparer = new DefaultCompareToHandler();
    m_lastKeys = new HashMap();
  }

  /**
   * Allows setting (generic because unknown) parameters for creating objects.
   *
   * @param a_parameters Collection of generic parameters
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void setParameters(final Collection a_parameters) {
    m_parameters = new Vector(a_parameters);
  }

  /**
   * @return Collection of generic parameters
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Collection getParameters() {
    return m_parameters;
  }

  public RandomGenerator createRandomGenerator() {
    return new StockRandomGenerator();
  }

  /**
   * Registers a clone handler that could be retrieved by
   * getCloneHandlerFor(Class).
   *
   * @param a_cloneHandler the ICloneHandler to register
   * @return index of the added clone handler, needed when removeCloneHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int registerCloneHandler(final ICloneHandler a_cloneHandler) {
    m_cloneHandlers.add(a_cloneHandler);
    return m_cloneHandlers.size() - 1;
  }

  /**
   * Removes a clone handler at a given index (which is obtained from
   * registerCloneHandler).
   *
   * @param a_index the index of the clone handler to remove
   * @return the removed ICloneHandler, or Exception if not successfull
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICloneHandler removeCloneHandler(final int a_index) {
    return (ICloneHandler) m_cloneHandlers.remove(a_index);
  }

  /**
   * Retrieves a clone handler capable of clone the given class.
   *
   * @param a_obj the object to clone (maybe null)
   * @param a_classToClone the class to clone an object of (maybe null)
   * @return the clone handler found capable of clone the given
   * class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICloneHandler getCloneHandlerFor(final Object a_obj,
      final Class a_classToClone) {
    return (ICloneHandler) findHandlerFor(a_obj, a_classToClone,
        m_cloneHandlers,
        m_defaultCloneHandler,
        "clone");
  }

  /**
   * Registers an initializer that could be retrieved by
   * getInitializerFor(Class).
   *
   * @param a_chromIniter the IChromosomeInitializer to register
   * @return index of the added initializer, needed when
   * removeChromosomeInitializer will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int registerInitializer(final IInitializer a_chromIniter) {
    m_initer.add(a_chromIniter);
    return m_initer.size() - 1;
  }

  /**
   * Removes an initializer at a given index (which is obtained from
   * registerInitializer).
   *
   * @param a_index the index of the initializer to remove
   * @return the removed IInitializer, or null if not successfull
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public IInitializer removeInitializer(final int a_index) {
    return (IInitializer) m_initer.remove(a_index);
  }

  /**
   * Retrieves an initializer capable of initializing the Object of the given
   * class.
   *
   * @param a_obj the object to init (maybe null)
   * @param a_class the object class to init (maybe null)
   * @return new instance (should be!) of initialized object
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public IInitializer getInitializerFor(final Object a_obj,
                                        final Class a_class) {
    return (IInitializer) findHandlerFor(a_obj, a_class,
        m_initer,
        m_defaultIniter,
        "init");
  }

  public void setGeneticOperatorConstraint(final IGeneticOperatorConstraint
      a_constraint) {
    m_geneticOpConstraint = a_constraint;
  }

  public IGeneticOperatorConstraint getGeneticOperatorConstraint() {
    return m_geneticOpConstraint;
  }

  /**
   * Retrieves a handler capable of comparing two instances of the given class.
   *
   * @param a_obj the object to compare (maybe null)
   * @param a_classToCompareTo the class instances to compare (maybe null)
   * @return the handler found capable of comparing instances
   * of the given class, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICompareToHandler getCompareToHandlerFor(Object a_obj,
      Class a_classToCompareTo) {
    return (ICompareToHandler) findHandlerFor(a_obj, a_classToCompareTo,
        m_compareHandlers,
        m_defaultComparer,
        "compare");
  }

  /**
   * Registers a compareTo-handler that could be retrieved by
   * getCompareToHandlerFor(Class).
   *
   * @param a_compareToHandler the ICompareToHandler to register
   * @return index of the added handler, needed when removeCompareToHandler
   * will be called
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int registerCompareToHandler(ICompareToHandler a_compareToHandler) {
    m_compareHandlers.add(a_compareToHandler);
    return m_compareHandlers.size() - 1;
  }

  /**
   * Removes a compareTo-handler at a given index (which is obtained from
   * registerCompareToHandler).
   *
   * @param a_index the index of the handler to remove
   * @return the removed handler, or Exception if not successfull
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ICompareToHandler removeCompareToHandler(final int a_index) {
    return (ICompareToHandler) m_compareHandlers.remove(a_index);
  }

  /**
   * Helper: Finds a handler for a given Object or Class, returns the default
   * handler, if one is provided. Uses an LRU cache to speedup things!
   *
   * @param a_obj the object to find a handler for (maybe null)
   * @param a_class the class to find a handler for (maybe null)
   * @param a_list list of available handlers
   * @param a_default a default handler to return in none other is found
   * @param a_listID arbitrary unique string for accessing the cache
   * @return the handler found, or null if none registered
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  protected IHandler findHandlerFor(final Object a_obj,
                                    final Class a_class,
                                    final List a_list,
                                    final IHandler a_default,
                                    final String a_listID) {
    String key = null;
    String key1, key2;
    if (m_useCaching) {
      // Construct key for cache lookup:
      // Class name of list + a_class-Name + a_obj.hashCode()
      // ----------------------------------------------------
      if (a_class == null) {
        key1 = "null";
      }
      else {
        key1 = a_class.getName();
      }
      if (a_obj == null) {
        key2 = "null";
      }
      else {
        key2 = a_obj.getClass().getName();
      }
      key = a_listID + "/" + key1 + "/" + key2;
      // Lookup cache.
      // -------------
      Object handler = m_cache.get(key);
      if (handler != null) {
        return (IHandler) handler;
      }
      // Not found in cache. Search initially.
      // -------------------------------------
    }
    IHandler result = null;
    Iterator it = a_list.iterator();
    while (it.hasNext()) {
      IHandler initer = (IHandler) it.next();
      if (initer.isHandlerFor(a_obj, a_class)) {
        result = initer;
        break;
      }
    }
    if (result == null) {
      // No registered handler found. Try the default handler.
      // -----------------------------------------------------
      if (a_default != null) {
        if (a_default.isHandlerFor(a_obj, a_class)) {
          result = a_default;
        }
      }
    }
    if (m_useCaching) {
      // Add to cache.
      // -------------
      if (result != null) {
        m_cache.put(key, result);
      }
    }
    return result;
  }

  /**
   * @return true: caching used, false: no caching used
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isUseCaching() {
    return m_useCaching;
  }

  public Object clone() {
    try {
      /**@todo check if it works this way*/
      return super.clone();
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }

  /**
   * The equals-method
   * @param a_other sic
   * @return sic
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * @param a_other other object to compare
   * @return as always
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      // Do not consider m_parameters, m_cache and m_useCaching.
      // -------------------------------------------------------
      JGAPFactory other = (JGAPFactory) a_other;
      return new CompareToBuilder()
          .append(m_cloneHandlers.toArray(), other.m_cloneHandlers.toArray())
          .append(m_initer.toArray(), other.m_initer.toArray())
          .append(m_compareHandlers.toArray(), other.m_compareHandlers.toArray())
          .append(m_defaultCloneHandler, other.m_defaultCloneHandler)
          .append(m_defaultComparer, other.m_defaultComparer)
          .append(m_geneticOpConstraint, other.m_geneticOpConstraint)
          .toComparison();
    }
  }

  /**
   * Returns a unique key for the given context. The key uses the current date
   * and time and a GUID. Thus it is quite probable, that the key is unique
   * worldwide.
   *
   * @param a_context the context to get the next key for, like "Chromosome".
   * @return the unique key for the given context
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public String getUniqueKey(String a_context) {
    // For each context, keep track of keys used.
    // ------------------------------------------
    Long lastKey = m_lastKeys.get(a_context);
    if(lastKey == null) {
      lastKey = new Long(1);
    }
    else {
      // The next key is always the increment of the previous key.
      // ---------------------------------------------------------
      lastKey = lastKey.longValue() + 1;
    }
    m_lastKeys.put(a_context, lastKey);
    String GUID = UUID.randomUUID().toString();
    String key = GUID + "_"+ DateKit.getNowAsString() + "_" + lastKey;
    return key;
  }
}
