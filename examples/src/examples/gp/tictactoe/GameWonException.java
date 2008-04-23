/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.tictactoe;

public class GameWonException
    extends RuntimeException {

  private int m_color;

  public GameWonException(int a_color, String a_message) {
    super(a_message);
    m_color = a_color;
  }

  public int getColor() {
    return m_color;
  }
}
