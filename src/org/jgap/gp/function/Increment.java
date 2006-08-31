/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.jgap.gp.impl.*;

/**
 * The increment operation.
 *
 * @author Konrad Odell
 * @author Klaus Meffert
 * @since 3.0
 */
public class Increment
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  private int m_increment;

  /**
   * Constructor for using an increment of 1.
   * @param a_conf the configuration to use
   * @param a_type the type of the terminal to increment (e.g. IntegerClass)
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Increment(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_type, 1);
  }

  /**
   * Constructor for freely choosable increment.
   * @param a_conf the configuration to use
   * @param a_type the type of the terminal to increment (e.g. IntegerClass)
   * @param a_increment the increment to use, may also be negative
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Increment(final GPConfiguration a_conf, Class a_type, int a_increment)
      throws InvalidConfigurationException {
    super(a_conf, 1, a_type);
    m_increment = a_increment;
  }

  protected CommandGene newGeneInternal() {
    try {
      CommandGene gene = new Increment(getGPConfiguration(), getReturnType(),
                                m_increment);
      return gene;
    } catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    if (m_increment == 1) {
      return "INC(&1)";
    }
    else {
      return "INC(" + m_increment + ", &1)";
    }
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) + m_increment;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) + m_increment;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) + m_increment;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) + m_increment;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_increment();
  }

  public static interface Compatible {
    public Object execute_increment();
  }

  /**
    * The compareTo-method.
    * @param a_other the other object to compare
    * @return -1, 0, 1
    *
    * @author Klaus Meffert
    * @since 3.0
    */
   public int compareTo(Object a_other) {
     if (a_other == null) {
       return 1;
     }
     else {
       Increment other = (Increment) a_other;
       return new CompareToBuilder()
           .append(m_increment, other.m_increment)
           .toComparison();
     }
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
     if (a_other == null) {
       return false;
     }
     else {
       try {
         Increment other = (Increment) a_other;
         return new EqualsBuilder()
             .append(m_increment, other.m_increment)
             .isEquals();
       } catch (ClassCastException cex) {
         return false;
       }
     }
  }}
