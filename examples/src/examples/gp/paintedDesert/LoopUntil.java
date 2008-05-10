/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * A loop until the condition of the first argument is true.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class LoopUntil
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Type of the fist argument
   */
  private Class m_typeVar;

  /**
   * Maximum number of times the function is allowed to loop
   */
  private int m_count = 50;

  /**
   * Constructor for the function
   * @param a_conf
   * @throws InvalidConfigurationException
   */
  public LoopUntil(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, CommandGene.BooleanClass, 50);
  }

  public LoopUntil(final GPConfiguration a_conf, Class a_typeVar, int a_count)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.BooleanClass);
    m_count = a_count;
  }

  /**
   * Program listing name of function
   */
  public String toString() {
    return "loop until( &1 )";
  }

  /**
   * @return textual name of this command
   *
   */
  public String getName() {
    return "Loop Until";
  }

  /**
   * Executes the LoopUntil function for a void argument.  Essentially loops
   * m_count times.
   * @param c
   * @param test
   * @param args
   */
  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    // Repeatedly execute the child.
    // -----------------------------
//		loop{
//
//		}
    //for (int i = 0; i < m_count; i++) {
    //if(!test)
    //	//c.execute_void(n, 0, args);
    //}
    return false;
  }

  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.VoidClass;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      return 1;
//			Loop other = (Loop) a_other;
//			return new CompareToBuilder()
//			.append(m_typeVar, other.m_typeVar)
//			.append(m_count, other.m_count)
//			.toComparison();
    }
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public boolean equals(Object a_other) {
    if (a_other == null) {
      return false;
    }
    else {
      try {
//				Loop other = (Loop) a_other;
//				return new EqualsBuilder()
//				.append(m_typeVar, other.m_typeVar)
//				.append(m_count, other.m_count)
//				.isEquals();
        return false;
      } catch (ClassCastException cex) {
        return false;
      }
    }
  }
}
