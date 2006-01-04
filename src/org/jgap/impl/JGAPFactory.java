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
 * Could be made configurable.<p>
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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private List m_parameters;

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
}
