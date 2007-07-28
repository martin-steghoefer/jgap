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

import java.util.*;

/**
 * A simple, generic pool class that can be used to pool any kind of object.
 * Objects can be released to this pool, either individually or as a
 * Collection, and then later acquired again. It is not necessary for an
 * object to have been originally acquired from the pool in order for it to
 * be released to the pool. If there are no objects present in the pool,
 * an attempt to acquire one will return null. The number of objects
 * available in the pool can be determined with the size() method. Finally,
 * it should be noted that the pool does not attempt to perform any kind
 * of cleanup or re-initialization on the objects to restore them to some
 * clean state when they are released to the pool; it's up to the user to
 * reset any necessary state in the object prior to the release call (or
 * just after the acquire call).
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class Pool {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * The objects currently in the pool.
   */
  private List m_pooledObjects;

  /**
   * Constructor.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Pool() {
    m_pooledObjects = Collections.synchronizedList(new ArrayList());
  }

  /**
   * Attempts to acquire an Object instance from the pool. It should
   * be noted that no cleanup or re-initialization occurs on these
   * objects, so it's up to the caller to reset the state of the
   * returned Object if that's desirable.
   *
   * @return an Object instance from the pool or null if no Object instances
   * are available in the pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Object acquirePooledObject() {
    if (m_pooledObjects.isEmpty()) {
      return null;
    }
    else {
      // Remove the last Object in the pool and return it.
      // Note that removing the last Object (as opposed to the first
      // one) is an optimization because it prevents the ArrayList
      // from resizing itself.
      // -----------------------------------------------------------
      return m_pooledObjects.remove(m_pooledObjects.size() - 1);
    }
  }

  /**
   * Releases an object to the pool. It's not required that the Object
   * originated from the pool - any Object can be released to it.
   *
   * @param a_objectToPool the Object instance to be released into the pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void releaseObject(final Object a_objectToPool) {
    m_pooledObjects.add(a_objectToPool);
  }

  /**
   * Releases a Collection of objects to the pool. It's not required that
   * the objects in the Collection originated from the pool - any objects
   * can be released to it.
   *
   * @param a_objectsToPool the Collection of objects to release into the pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void releaseAllObjects(final Collection a_objectsToPool) {
    if (a_objectsToPool != null) {
      m_pooledObjects.addAll(a_objectsToPool);
    }
  }

  /**
   * Retrieves the number of objects currently available in this pool.
   *
   * @return the number of objects in this pool
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized int size() {
    return m_pooledObjects.size();
  }

  /**
   * Empties out this pool of all objects.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void clear() {
    m_pooledObjects.clear();
  }
}
