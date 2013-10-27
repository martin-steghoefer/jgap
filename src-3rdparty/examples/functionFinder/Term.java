/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.functionFinder;

/**
 * Term as part of a formula.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Term {
  public String m_termName;

  public int m_depth; // Max. number of sub terms

  public int m_termType; // Constant(1), Form(2)

  public char m_operator; // Operator (+,-,*,/,%) left to the term!

  public Term(int a_termType, String a_termName, int a_depth) {
    this(a_termType, a_termName, a_depth, ' ');
  }

  public Term(int a_termType, String a_termName, int a_depth, char a_operator) {
    m_termType = a_termType;
    m_termName = a_termName;
    m_depth = a_depth;
    m_operator = a_operator;
  }

  public Term() {

  }
}
