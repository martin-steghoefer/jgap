package org.jgap.gp;

import java.util.*;
import org.jgap.*;

public class MathConfiguration
    extends GPConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Stack m_stack;

  private Stack m_terminalStack;

  public MathConfiguration(ILanguage a_language)
      throws InvalidConfigurationException {
    super(a_language);
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
