/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.functionFinder;

/**
 * Term as part of a formula
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Term {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public String termName;

  public int depth; //Max. number of sub terms

  public int termType; //Constant(1), Form(2)

  public char operator; //Operator (+,-,*,/,%) left to the term!

  public Term(int termType, String termName, int depth) {
    this(termType, termName, depth, ' ');
  }

  public Term(int termType, String termName, int depth, char operator) {
    this.termType = termType;
    this.termName = termName;
    this.depth = depth;
    this.operator = operator;
  }

  public Term() {

  }
}
