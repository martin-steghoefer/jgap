/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * A connector for independent subprograms (subtrees). Each subtree except the
 * last one must have a memory- or stack-modifying command (such as push or
 * store), otherwise there is no connection between the subtrees (which would
 * be useless bloating).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class SubProgram
    extends CommandGene implements ICloneable, IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.18 $";

  /**
   * Number of subprograms. Redundant, because equal to m_types.length.
   */
  private int m_subtrees;

  /**
   * Minimum arity allowed during mutation of arity.
   */
  private int m_minArity;

  /**
   * Maximum arity allowed during mutation of arity.
   */
  private int m_maxArity;

  /**
   * Return types of the subprograms to excecute.
   */
  private Class[] m_types;

  private boolean m_mutateable;

  private int m_mode;

  public SubProgram(final GPConfiguration a_conf, Class[] a_types)
      throws InvalidConfigurationException {
    this(a_conf, a_types, 0, null);
  }

  /**
   * Collage constructor: Create a sub program that has a_arity elements
   * of the same type a_types.
   *
   * @param a_conf the configuration to use
   * @param a_arity number of children in the collage
   * @param a_types uniform type of all children
   * @throws org.jgap.InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public SubProgram(final GPConfiguration a_conf, int a_arity, Class a_types)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_types, false);
  }

  /**
   * Collage constructor: Create a sub program that has a_arity elements
   * of the same type a_types.
   *
   * @param a_conf the configuration to use
   * @param a_arity number of children in the collage
   * @param a_types uniform type of all children
   * @param a_mutateable true: allow mutation of the sub program, i.e., the
   * number of children (=arity) may be varied automatically during evolution
   * @throws org.jgap.InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public SubProgram(final GPConfiguration a_conf, int a_arity, Class a_types,
                    boolean a_mutateable)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_types, a_arity, a_arity + 5, a_mutateable);
  }

  public SubProgram(final GPConfiguration a_conf, int a_arity, Class a_types,
                    int a_minArity, int a_maxArity, boolean a_mutateable)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_types, 0, null);
    if (a_arity < 1) {
      throw new IllegalArgumentException("Arity must be >= 1");
    }
    if (a_minArity > a_arity) {
      throw new IllegalArgumentException("Arity must not be smaller than"
          + " min. arity");
    }
    if (a_maxArity < a_arity) {
      throw new IllegalArgumentException("Arity must not be bigger than"
          + " max. arity");
    }
    m_mode = 2;
    m_types = new Class[a_arity];
    for (int i = 0; i < a_arity; i++) {
      m_types[i] = a_types;
    }
    m_subtrees = a_arity;
    m_mutateable = a_mutateable;
    m_minArity = a_minArity;
    m_maxArity = a_maxArity;
  }

  public SubProgram(final GPConfiguration a_conf, Class[] a_types,
                    boolean a_mutateable)
      throws InvalidConfigurationException {
    this(a_conf, a_types, 0, null, a_mutateable);
  }

  public SubProgram(final GPConfiguration a_conf, Class[] a_types,
                    int a_subReturnType, int[] a_subChildTypes)
      throws InvalidConfigurationException {
    this(a_conf, a_types, a_subReturnType, a_subChildTypes, false);
  }

  public SubProgram(final GPConfiguration a_conf, Class[] a_types,
                    int a_subReturnType, int[] a_subChildTypes, boolean
                    a_mutateable)
      throws InvalidConfigurationException {
    super(a_conf, a_types.length, a_types[a_types.length - 1], a_subReturnType,
          a_subChildTypes);
    if (a_types.length < 1) {
      throw new IllegalArgumentException("Number of subtrees must be >= 1");
    }
    m_mode = 1;
    m_minArity = a_types.length;
    m_maxArity = m_minArity + 5;
    m_types = a_types;
    m_subtrees = a_types.length;
    m_mutateable = a_mutateable;
  }

  public String toString() {
    String ret = "sub[";
    for (int i = 1; i < m_subtrees; i++) {
      ret += "&" + i + " --> ";
    }
    ret += "&" + m_subtrees + "]";
    return ret;
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Sub program";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      if (i < m_subtrees - 1) {
        c.execute_void(n, i, args);
      }
      else {
        value = c.execute_int(n, i, args); /**@todo evaluate m_types*/
      }
//      if (i < m_subtrees - 1) {
//        ( (GPConfiguration) getConfiguration()).storeThruput(i,
//            new Integer(value));
//      }
    }
    return value;
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    for (int i = 0; i < m_subtrees; i++) {
      c.execute_void(n, i, args); /**@todo evaluate m_types*/
    }
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_long(n, i, args);
    }
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_float(n, i, args);
    }
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_double(n, i, args);
    }
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = null;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_object(n, i, args);
    }
    return value;
  }

  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    try {
      return m_types[a_chromNum];
    } catch (ArrayIndexOutOfBoundsException aex) {
      return null;
    }
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    SubProgram other = (SubProgram) a_other;
    return new CompareToBuilder()
        .append(m_types, other.m_types)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      SubProgram other = (SubProgram) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_types, other.m_types)
          .isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
//      int[] subChildTypes = getSubChildTypes();
//      if (subChildTypes != null) {
//        subChildTypes = (int[]) subChildTypes.clone();
//      }
//      SubProgram result = new SubProgram(getGPConfiguration(), m_types,
//          getSubReturnType(), subChildTypes, m_mutateable);
//      result.m_subtrees = m_subtrees;
//      result.m_types = (Class[]) m_types.clone();
//      return result;
      SubProgram result;
      if (m_mode == 1) {
        // First way of construction.
        // --------------------------
        Class[] types = new Class[m_subtrees];
        for (int i = 0; i < m_subtrees; i++) {
          types[i] = m_types[m_types.length - 1];
        }
        int[] subChildTypes = getSubChildTypes();
        if (subChildTypes != null) {
          subChildTypes = (int[]) subChildTypes.clone();
        }
        result = new SubProgram(getGPConfiguration(), types,
                                getSubReturnType(), subChildTypes, m_mutateable);
      }
      else {
        // Second way of construction.
        // ---------------------------
        result = new SubProgram(getGPConfiguration(), m_subtrees, m_types[0],
                                m_minArity, m_maxArity, m_mutateable);
      }
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    if (!m_mutateable) {
      return this;
    }
    org.jgap.RandomGenerator randomGen = getGPConfiguration().
        getRandomGenerator();
    double random = randomGen.nextDouble();
    if (random < a_percentage) {
      return applyMutation();
    }
    return this;
  }

  /**@todo use dynamizeArity instead!*/

  /**
   * @return mutated command gene
   * @throws InvalidConfigurationException
   */
  public CommandGene applyMutation()
      throws InvalidConfigurationException {
    int size = getGPConfiguration().getRandomGenerator().nextInt(m_maxArity + 1 -
        m_minArity) + m_minArity;
    if (m_types.length == size) {
      return this;
    }
    SubProgram result;
    if (m_mode == 1) {
      // First way of construction.
      // --------------------------
      Class[] types = new Class[size];
      for (int i = 0; i < size; i++) {
        types[i] = m_types[m_types.length - 1];
      }
      int[] subChildTypes = getSubChildTypes();
      if (subChildTypes != null) {
        subChildTypes = (int[]) subChildTypes.clone();
      }
      result = new SubProgram(getGPConfiguration(), types,
                              getSubReturnType(), subChildTypes, m_mutateable);
    }
    else {
      // Second way of construction.
      // ---------------------------
      result = new SubProgram(getGPConfiguration(), size, m_types[0],
                              m_minArity, m_maxArity, m_mutateable);
    }
    return result;
  }
  /**
   * Adaptation of the arity so that it represents a value within the interval
   * [m_arityMin, m_arityMax].
   *
   * @author Klaus Meffert
   * @since 3.4
   */
//  public void dynamizeArity() {
//    int size = getGPConfiguration().getRandomGenerator().nextInt(m_maxArity + 1 -
//        m_minArity) + m_minArity;
//    setArity(size);
//    Class atype = m_types[0];
//    m_types = new Class[size];
//    for (int i = 0; i < size; i++) {
//      m_types[i] = atype;
//    }
//    m_subtrees = size;
//  }
}
