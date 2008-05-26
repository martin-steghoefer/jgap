/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Base class for GPProgram's. See org.jgap.gp.impl.GPProgram for an
 * implementation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class GPProgramBase
    implements IGPProgram {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  private double m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;

  private GPConfiguration m_conf;

  /**
   * Return type per chromosome.
   */
  private Class[] m_types;

  /**
   * Argument types for ADF's
   */
  private Class[][] m_argTypes;

  /**
   * Available GP-functions.
   */
  private CommandGene[][] m_nodeSets;

  /**
   * Minimum depth per each chromosome
   */
  private int[] m_minDepths;

  /**
   * Maximum depth per each chromosome
   */
  private int[] m_maxDepths;

  /**
   * Maximum number of nodes allowed per chromosome (when exceeded program
   * aborts)
   */
  private int m_maxNodes;

  /**
   * Free to use data object.
   */
  private Object m_applicationData;

  /**
   * Default constructor, only for dynamic instantiation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public GPProgramBase()
      throws Exception {
  }

  public GPProgramBase(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    if (a_conf == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_conf = a_conf;
  }

  public GPProgramBase(IGPProgram a_prog)
      throws InvalidConfigurationException {
    this(a_prog.getGPConfiguration());
    m_types = a_prog.getTypes();
    m_argTypes = a_prog.getArgTypes();
    m_nodeSets = a_prog.getNodeSets();
    m_maxDepths = a_prog.getMaxDepths();
    m_minDepths = a_prog.getMinDepths();
    m_maxNodes = a_prog.getMaxNodes();
  }

  public GPConfiguration getGPConfiguration() {
    return m_conf;
  }

  /**
   * Compares this entity against the specified object.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * @return fitness value of this program determined via the registered
   * fitness function
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double calcFitnessValue() {
    GPFitnessFunction normalFitnessFunction = getGPConfiguration().
        getGPFitnessFunction();
    if (normalFitnessFunction != null) {
      // Grab the "normal" fitness function and ask it to calculate our
      // fitness value.
      // --------------------------------------------------------------
      m_fitnessValue = normalFitnessFunction.getFitnessValue(this);
    }
    if (Double.isInfinite(m_fitnessValue)) {
      return GPFitnessFunction.NO_FITNESS_VALUE;
    }
    else {
      return m_fitnessValue;
    }
  }

  /**
   * @return fitness value of this program, cached access
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double getFitnessValue() {
    if (m_fitnessValue >= 0.000d) {
      return m_fitnessValue;
    }
    else {
      return calcFitnessValue();
    }
  }

  /**
   * @return computed fitness value of this program, may be unitialized
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public double getFitnessValueDirectly() {
    return m_fitnessValue;
  }

  public void setFitnessValue(double a_fitness) {
    m_fitnessValue = a_fitness;
  }

  public void setTypes(Class[] a_types) {
    m_types = a_types;
  }

  public Class[] getTypes() {
    return m_types;
  }

  public Class getType(int a_index) {
    return m_types[a_index];
  }

  public void setArgTypes(Class[][] a_argTypes) {
    m_argTypes = a_argTypes;
  }

  public Class[][] getArgTypes() {
    return m_argTypes;
  }

  public Class[] getArgType(int a_index) {
    return m_argTypes[a_index];
  }

  public void setNodeSets(CommandGene[][] a_nodeSets) {
    m_nodeSets = a_nodeSets;
  }

  public CommandGene[][] getNodeSets() {
    return m_nodeSets;
  }

  public CommandGene[] getNodeSet(int a_index) {
    return m_nodeSets[a_index];
  }

  public void setMaxDepths(int[] a_maxDepths) {
    m_maxDepths = a_maxDepths;
  }

  public int[] getMaxDepths() {
    return m_maxDepths;
  }

  public void setMinDepths(int[] a_minDepths) {
    m_minDepths = a_minDepths;
  }

  public int[] getMinDepths() {
    return m_minDepths;
  }

  public void setMaxNodes(int a_maxNodes) {
    m_maxNodes = a_maxNodes;
  }

  public int getMaxNodes() {
    return m_maxNodes;
  }

  /**
   * Sets the application data object.
   *
   * @param a_data the object to set
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void setApplicationData(Object a_data) {
    m_applicationData = a_data;
  }

  /**
   * @return the application data object set
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public abstract Object clone();
}
