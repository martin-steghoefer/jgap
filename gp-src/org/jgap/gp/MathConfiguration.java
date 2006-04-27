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
 * Configuration for GP-programs related to mathematical problems.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MathConfiguration
    extends GPConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Stack m_stack;

  private Stack m_terminalStack;

  public MathConfiguration()
      throws InvalidConfigurationException {
    super();
    m_stack = new Stack();
    m_terminalStack = new Stack();
  }

  /**
   * Push object onto stack
   * @param a_object Object
   */
  public void push(Object a_object) {
    m_stack.push(a_object);
  }

  /**@todo used anymore?*/
  public Object pop() {
    return m_stack.pop();
  }

  public Object getStack(int i) {
    return m_stack.get(i);
  }

  public int stackSize() {
    return m_stack.size();
  }

  public void pushTerminal(Object a_object) {
    m_terminalStack.push(a_object);
  }

  public Object popTerminal() {
    return m_terminalStack.pop();
  }

  public int terminalSize() {
    return m_terminalStack.size();
  }
}
