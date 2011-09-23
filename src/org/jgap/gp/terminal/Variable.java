/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.terminal;

import java.util.*;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * A terminal represented by a variable (x, y, z...). A variable has one single
 * value with can be influenced from outside.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Variable
    extends CommandGene //implements ICloneable
{
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.12 $";

  public static Hashtable vars = new Hashtable();

  /**
   * Unique name of the variable.
   */
  private String m_name;

  /**
   * Current value of the variable.
   */
  private Object m_value;

  public Variable(final GPConfiguration a_conf, String a_varName, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_type);
    m_name = a_varName;
    vars.put(a_varName, this);
  }

  public String toString() {
    return m_name;
  }

  /**
   * Attention: It is important to return m_name here (see
   * GPGenotype.putVariable).
   *
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return m_name;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return null;
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    return ( (Boolean) m_value).booleanValue();
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return ( (Integer) m_value).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return ( (Long) m_value).longValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return ( (Float) m_value).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return ( (Double) m_value).doubleValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return m_value;
  }

  /**
   * Gets the one instance of a named variable.
   *
   * @param name the name of the variable to get
   * @return the named variable, or null if that name wasn't found.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static Variable getVariable(String name) {
    return (Variable) vars.get(name);
  }

  /**
   * Creates an instance of a Variable.
   * If a Variable of that name already exists, it is returned.
   * Otherwise a new instance is created, its value is initialized to null, and
   * it is placed into the static hashtable for later retrieval by name via
   * getVariable.
   *
   * @param a_conf the configuration to use
   * @param a_name the name of the Variable to create
   * @param a_type the type of the Variable to create
   * @return the variable object created
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static Variable create(GPConfiguration a_conf, String a_name,
                                Class a_type)
      throws InvalidConfigurationException {
    Variable var;
    var = a_conf.getVariable(a_name);
    if(var != null) {
      return var;
    }
    var = new Variable(a_conf, a_name, a_type);
    a_conf.putVariable(var);
    return var;
  }

  /**
   * Sets the value of this named variable.
   *
   * @param a_value the value to set this variable with
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void set(Object a_value) {
    m_value = a_value;
  }

  public Object getValue() {
    return m_value;
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.6
   */
//  public Object clone() {
//    try {
//      Variable result = create(getGPConfiguration(), m_name, getReturnType());
////      Variable result = new Variable(getGPConfiguration(), m_name, getReturnType());
////      getGPConfiguration().putVariable(result);
//      if(m_value != null) {
//        result.m_value = new Integer( ( (Integer) m_value)); /**@todo fix*/
//      }
//      return result;
//    } catch (Exception ex) {
//      throw new CloneException(ex);
//    }
//  }

}
