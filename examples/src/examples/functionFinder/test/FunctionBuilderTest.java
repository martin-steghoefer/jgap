package examples.functionFinder.test;

import java.util.*;

import org.jgap.*;

import junit.framework.*;
import examples.functionFinder.*;

/**
 * Tests for FunctionBuilder class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class FunctionBuilderTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public FunctionBuilderTest() {
    Repository.init();
  }

  public void testEmpty_0() {
    String formula = getFormula(null);
    assertEquals("", formula);
  }

  public void testEmpty_1() {
    String formula = getFormula(null);
    assertEquals("", formula);
  }

  public void testEmpty_2() {
    Vector elements = new Vector();
    Term element = new Term(0, "X", 1);
    elements.add(element);
    try {
      String formula = getFormula(elements);
      fail();
    }
    catch (RuntimeException rte) {
      ; //this is OK
    }
  }

  public void testEmpty_3() {
    Vector elements = new Vector();
    try {
      Term element = new Term(1, "", 1);
      fail();
    }
    catch (AssertionFailedError aex) {
      ; //this is OK
    }
  }

  public void testConstant_0() {
    Vector elements = new Vector();
    Term element = new Term(1, "2", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=2", formula);
  }

  /**
   * Two Constants, one operator
   */
  public void testConstant_1() {
    Vector elements = new Vector();
    Term element = new Term(1, "X", 1);
    elements.add(element);
    element = new Term(1, "3", 1, '+');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=X+3", formula);
  }

  /**
   * The operator is expected to be ignored
   */
  public void testConstant_2() {
    Vector elements = new Vector();
    Term element = new Term(1, "2", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=2", formula);
  }

  /**
   * The placeholder is expected to be replaced by a positive integer
   */
  public void testConstant_3() {
    Vector elements = new Vector();
    Term element = new Term(1, "+I", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    int i = Integer.parseInt(formula.substring(5));
    assertTrue(i > 0);
  }

  /**
   * The placeholder is expected to be replaced by a negative integer
   */
  public void testConstant_4() {
    Vector elements = new Vector();
    Term element = new Term(1, "-I", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    int i = Integer.parseInt(formula.substring(5));
    assertTrue(i<0);
  }

  /**
   * The placeholder is expected to be replaced by a positive double
   */
  public void testConstant_5() {
    Vector elements = new Vector();
    Term element = new Term(1, "+D", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    double d = Double.parseDouble(formula.substring(5));
    assertTrue(d>0);
  }

  /**
   * The placeholder is expected to be replaced by a negative double
   */
  public void testConstant_6() {
    Vector elements = new Vector();
    Term element = new Term(1, "-D", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    double d = Double.parseDouble(formula.substring(5));
    assertTrue(d<0);
  }

  /**
   * One formula with depth 1
   */
  public void testFormula_0() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 1);
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertEquals("F(X)=sin(X)", formula);
  }

  /**
   * Two Formulas, each depth 1
   */
  public void testFormula_1() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 1);
    elements.add(element);
    element = new Term(2, "cos", 1);
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertEquals("F(X)=sin(cos(X))", formula);
  }

  /**
   * Two formulas, depth 1, one operator
   * Two illegal operators are expected to be ignored
   */
  public void testFormula_2() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 1, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "27.5", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X)*cos(27.5)", formula);
  }

  /**
   * Three formulas, first depth 2, one operator
   */
  public void testFormula_3() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 2);
    elements.add(element);
    element = new Term(2, "exp", 1);
    elements.add(element);
    element = new Term(1, "16.4", 1);
    elements.add(element);
    element = new Term(2, "cos", 1, '-');
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(exp(16.4)-cos(X))", formula);
  }

  /**
   * Four terms, first depth 3, two operators
   */
  public void testFormula_4() {
    Vector elements = new Vector();
    Term element = new Term(2, "sqrt", 3);
    elements.add(element);
    element = new Term(2, "exp", 1);
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "33.7", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '+');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sqrt(exp(X)*cos(33.7)+X)", formula);
  }

  /**
   * Five terms, first depth 3, three operators (two block)
   */
  public void testFormula_5() {
    Vector elements = new Vector();
    Term element = new Term(2, "sqrt", 3);
    elements.add(element);
    element = new Term(2, "exp", 1);
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "33.7", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '+');
    elements.add(element);
    element = new Term(2, "abs", 1, '-');
    elements.add(element);
    element = new Term(1, "Y", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sqrt(exp(X)*cos(33.7)+X)-abs(Y)", formula);
  }

  /**
   * three simple terms, two operators
   */
  public void testFormula_6() {
    Vector elements = new Vector();
    Term element = new Term(1, "X", 1);
    elements.add(element);
    element = new Term(1, "Y", 1, '+');
    elements.add(element);
    element = new Term(1, "Z", 1, '+');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=X+Y+Z", formula);
  }

  /**
   * three terms, two operators
   */
  public void testFormula_7() {
    Vector elements = new Vector();
    Term element = new Term(1, "7.4", 1);
    elements.add(element);
    element = new Term(2, "abs", 1, '+');
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    element = new Term(1, "23.5", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=7.4+abs(X)*23.5", formula);
  }

  /**
   * Three complex terms, first and second depth 2, two operators (one block)
   */
  public void testFormula_8() {
    Vector elements = new Vector();
    Term element = new Term(2, "exp", 2);
    elements.add(element);
    element = new Term(2, "abs", 2);
    elements.add(element);
    element = new Term(1, "16.4", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '+');
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "347.2", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=exp(abs(16.4+X)*cos(347.2))", formula);
  }

  /**
   * Three complex terms and one simple term, first and second depth 2, three operators (two block)
   */
  public void testFormula_9() {
    Vector elements = new Vector();
    Term element = new Term(2, "exp", 2);
    elements.add(element);
    element = new Term(2, "abs", 2);
    elements.add(element);
    element = new Term(1, "16.4", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '+');
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "347.2", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '%');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=exp(abs(16.4+X)*cos(347.2))%X", formula);
  }

  /**
   * Four complex terms, first and second depth 2, three operators (two block)
   */
  public void testFormula_10() {
    Vector elements = new Vector();
    Term element = new Term(2, "exp", 2);
    elements.add(element);
    element = new Term(2, "abs", 2);
    elements.add(element);
    element = new Term(1, "16.4", 1);
    elements.add(element);
    element = new Term(1, "X", 1, '+');
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    element = new Term(1, "347.2", 1);
    elements.add(element);
    element = new Term(2, "sqrt", 1, '%');
    elements.add(element);
    element = new Term(1, "X", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=exp(abs(16.4+X)*cos(347.2))%sqrt(X)", formula);
  }

  /**
   * Two formulas, depth 1, one operator, one term too less
   * Two illegal operators and two depth point to much are expected to be ignored
   */
  public void testFormula_11() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 1, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 2, '*');
    elements.add(element);
    element = new Term(1, "27.5", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X)*cos(27.5)", formula);
  }

  /**
   * Two formulas, depth 1, one operator, two terms too less
   * Two illegal operators and two depth points too much are expected to be ignored
   */
  public void testFormula_12() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 2, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 2, '*');
    elements.add(element);
    element = new Term(1, "27.5", 1);
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X*cos(27.5))", formula);
  }

  /**
   * Two formulas, depth 1, one operator, several terms too less
   * Two illegal operators and several depth points too much are expected to be ignored
   */
  public void testFormula_13() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 3, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 3, '*');
    elements.add(element);
    element = new Term(1, "27.5", 2,'!');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X*cos(27.5))", formula);
  }

  /**
   * One formula, one operator, one constant too less
   */
  public void testFormula_14() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 1, '+');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X)", formula);
  }

  /**
   * Two formulas, depth 1, one operator, one constant too less
   */
  public void testFormula_15() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 3, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 1, '*');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X*cos(X))", formula);
  }

  /**
   * Two formulas, depth 1, one operator, one constant too less
   */
  public void testFormula_16() {
    Vector elements = new Vector();
    Term element = new Term(2, "sin", 3, '+');
    elements.add(element);
    element = new Term(1, "X", 1, '~');
    elements.add(element);
    element = new Term(2, "cos", 2, '*');
    elements.add(element);
    String formula = getFormula(elements);
    assertFormula("F(X)=sin(X*cos(X))", formula);
  }

  /**
   * Hilfsroutine, gibt Soll- und Istwert aus. assertEquals tut dies auch, aber
   * die Ausgabe ist bei längeren Zeichenfolgen abgeschnitten
   * @param soll
   * @param ist
   */
  protected void assertFormula(String soll, String ist) {
    try {
      assertEquals(soll, ist);
    }
    catch (ComparisonFailure cof) {
      System.err.println("Ist : " + ist);
      System.err.println("Soll: " + soll);
      //rethrow the exception
      assertEquals(soll, ist);
    }
  }

  public static String getFormulaFromChromosome(Chromosome chromosome) {
    /**@todo constructTerms allgemeiner machen (Object zurückliefern),
     * in Methode selbst das mit CompositeGene durch Call auf Plugin ersetzen
     */
    Vector terms = GeneExtractorTest.constructTerms(chromosome.getGenes());
    return getFormula(terms);
  }

  /**
   * Constructs a formula string out of terms (each holding operators, if relevant)
   * @param elements vector with ordered list of terms
   * @return constructed formula
   */
  public static String getFormula(Vector elements) {
    if (elements == null || elements.isEmpty()) {
      return "";
    }
    else {
      String result = "";
      Term element = null;
      boolean addKlammer = false;
      do {
        element = (Term) elements.elementAt(0);
        result += getFormula_int(elements, result);
      }
      while (elements.size() > 0);
      if (result == "") {
        return "";
      }
      else {
        return "F(X)=" + result;
      }
    }
  }

  /**
   * Recursive part of getFormula
   * @param elements vector with ordered list of terms
   * @return constructed part of formula
   */
  protected static String getFormula_int(Vector elements, String previous) {
    if (elements.size() < 1) {
      return "";
    }
    Term element = (Term) elements.elementAt(0);
    element = replaceSubstitute(element);
    String result = "";
    //ignore illegal operator specification
    if (element.operator != ' ' && previous.length() > 0 && !previous.endsWith("(")) {
      result += element.operator;
    }
    int type = element.termType;
    result += element.termName;
    elements.remove(0);
    if (type == 2) {
      result += "(";
      boolean compensationPossible = true;
      do {
        element.depth--;
        String tempRes = getFormula_int(elements, result);
        if (tempRes.length() > 0) {
          compensationPossible = false;
        }
        result += tempRes;
      }
      while (element.depth > 0);
      if (compensationPossible) {
        result += "X";
      }
      result += ")";
    }
    else if (type == 1) {
      /**@todo nicht benötigt?*/
      element.depth--;
    }
    else {
      throw new RuntimeException("Ungültiger Operatortyp: " + type);
    }
    return result;
  }

  private static Term replaceSubstitute(Term element) {
    if (element.termName.equals("+I")) {
      /**@todo: optional Wertebereich angeben können*/
      element.termName = String.valueOf(new Random().nextInt(3)+1);
    }
    else if (element.termName.equals("-I")) {
      element.termName = String.valueOf(-(new Random().nextInt(10)+1));
    }
    else if (element.termName.equals("+D")) {
      element.termName = String.valueOf(new Random().nextDouble()*10);
    }
    else if (element.termName.equals("-D")) {
      element.termName = String.valueOf(-(new Random().nextDouble()*10));
    }
    return element;
  }
}
