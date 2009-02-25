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
 * Returns a single character out of a set of given characters. The decision
 * which character to return is controlled by a child of this command which in
 * turn returns an integer index.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class CharacterProvider
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Alphabet of valid characters.
   */
  private String m_alphabet;

  public CharacterProvider(final GPConfiguration a_conf, Class a_returnType,
                      String a_alphabet)
      throws InvalidConfigurationException {
    this(a_conf, a_returnType, a_alphabet, 0);
  }

  public CharacterProvider(final GPConfiguration a_conf, Class a_returnType,
                      String a_alphabet, int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 1, a_returnType, a_subReturnType, null);
    if (a_alphabet == null || a_alphabet.length() < 1) {
      throw new IllegalArgumentException("Alphabet must not be empty!");
    }
    m_alphabet = a_alphabet;
  }

  public String toString() {
    return "character(" + m_alphabet + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Character";
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    int index = c.execute_int(n, 0, args);
    try {
      return m_alphabet.charAt(index);
    } catch (ArrayIndexOutOfBoundsException iex) {
      throw new IllegalStateException(
          "CharacterProvider: child returned invalid index");
    }
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    CharacterProvider other = (CharacterProvider) a_other;
    return new CompareToBuilder()
        .append(m_alphabet, other.m_alphabet)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public boolean equals(Object a_other) {
    try {
      CharacterProvider other = (CharacterProvider) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_alphabet, other.m_alphabet)
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
   * @since 3.4.3
   */
  public Object clone() {
    try {
      CharacterProvider result = new CharacterProvider(getGPConfiguration(),
          getReturnType(), m_alphabet, getSubReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
