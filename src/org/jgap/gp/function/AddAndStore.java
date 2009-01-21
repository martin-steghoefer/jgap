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
 * The add operation that stores the result in internal memory afterwards.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class AddAndStore
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  /**
   * Symbolic name of the storage. Must correspond with a chosen name for
   * ReadTerminalCommand.
   */
  private String m_storageName;

  private Class m_type;

  public AddAndStore(final GPConfiguration a_conf, Class a_type,
                     String a_storageName)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_type = a_type;
    m_storageName = a_storageName;
  }

  public String toString() {
    return "Store(" + m_storageName + ", &1 + &2)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "AddAndStore";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    Object value = null;
    if (m_type == CommandGene.IntegerClass) {
      value = new Integer(c.execute_int(n, 0, args) + c.execute_int(n, 1, args));
    }
    else if (m_type == CommandGene.LongClass) {
      value = new Long(c.execute_long(n, 0, args) + c.execute_long(n, 1, args));
    }
    else if (m_type == CommandGene.DoubleClass) {
      value = new Double(c.execute_double(n, 0, args) +
                         c.execute_double(n, 1, args));
    }
    else if (m_type == CommandGene.FloatClass) {
      value = new Float(c.execute_float(n, 0, args) +
                        c.execute_float(n, 1, args));
    }
    else {
      throw new RuntimeException("Type " + m_type +
                                 " not supported by AddAndStore");
    }
    getGPConfiguration().storeInMemory(m_storageName, value);
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return m_type;
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
    AddAndStore other = (AddAndStore) a_other;
    return new CompareToBuilder()
        .append(m_type, other.m_type)
        .append(m_storageName, other.m_storageName)
        .toComparison();
  }

  /**
   * The equals-method.
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      AddAndStore other = (AddAndStore) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_type, other.m_type)
          .append(m_storageName, other.m_storageName)
          .isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      AddAndStore result = new AddAndStore(getGPConfiguration(), m_type,
          m_storageName);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
