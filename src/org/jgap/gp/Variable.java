/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.util.*;
import org.jgap.*;

/**
 * A terminal represented by a variable (x,y,z...).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Variable
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  public static Hashtable vars = new Hashtable();

  /**
   * Unique name of the variable.
   */
  private String m_name;

  public Object value;

  /**@todo map varName to argNum and skip argNum in constructor*/
  private int m_argnum;

  public Variable(final Configuration a_conf, String a_varName, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 0, type);
//    m_argnum = a_argnum;
    m_name = a_varName;
    vars.put(a_varName, this);
  }

  public String getVarName() {
    return m_name + "/" + m_argnum;
  }

  public void applyMutation(int index, double a_percentage) {
    // do nothing here!
  }

  protected Gene newGeneInternal() {
    try {
      return new Variable(getConfiguration(), m_name, getReturnType());
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    return m_name;
  }

  public Class getChildType(int num) {
    return null;
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    return ( (Boolean) args[m_argnum]).booleanValue();
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return ( (Integer) args[m_argnum]).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return ( (Long) args[m_argnum]).longValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return ( (Float) value).floatValue();
//    return ( (Float) args[m_argnum]).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return ( (Double) args[m_argnum]).doubleValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return args[m_argnum];
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
   * If a Variable of that name already exists, that is returned.
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
  public static Variable create(Configuration a_conf, String a_name, Class a_type)
      throws InvalidConfigurationException {
    Variable var;
    if ( (var = getVariable(a_name)) != null) {
      return var;
    }
    return new Variable(a_conf, a_name, a_type);
  }

  /**
   * Sets the value of this named variable.
   *
   * @param a_value the value to set this variable with
   *
   * @since 1.0
   */
  public void set(Object a_value) {
    value = a_value;
  }

}
