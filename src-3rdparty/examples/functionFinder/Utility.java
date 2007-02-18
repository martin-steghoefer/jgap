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

import java.util.*;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Utility functions.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Utility {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private static int numberOfFunctions;

  private static int numberOfConstants;

  private static int numberOfOperators;

  /**
   * Static initializers
   */
  static {
    numberOfFunctions = Repository.getFunctions().size();
    numberOfConstants = Repository.getConstants().size();
    numberOfOperators = Repository.getOperators().size();
  }

  /**
   * Constructs terms that are represented by a list of genes.
   *
   * @param genes Gene[] list of genes representing terms
   * @return Vector list list of terms
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static List constructTerms(Gene[] genes) {
    List result = new Vector();
    Term term;
    Gene[] geneTupel;
    CompositeGene comp;
    Gene gene1;
    Gene gene2;
    for (int i = 0; i < genes.length; i++) {
      comp = (CompositeGene) genes[i];
      gene1 = comp.geneAt(0);
      gene2 = comp.geneAt(1);
      geneTupel = new Gene[] {
          gene1, gene2};
      term = constructTerm(geneTupel);

      result.add(term);
    }
    return result;
  }

  /**
   * Constructs a term that is represented by a gene tuple (containing a tuple).
   *
   * @param a_genes Gene[]
   * @return Term
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static Term constructTerm(Gene[] a_genes) {
    Gene op = a_genes[1];
    Integer allele = (Integer) a_genes[0].getAllele();
    int fktNr = allele.intValue();
    fktNr = fktNr % (numberOfFunctions + numberOfConstants);
    String fktName;
    int type;
    if (fktNr >= numberOfFunctions) {
      // Read out constant
      /**@todo store constant in chromosome's application data in order
       * to make it immutable and immune regarind replaceSubstitute(..)
       */
      fktName = (String) Repository.getConstants().get(fktNr -
          numberOfFunctions);
      type = 1;
    }
    else {
      // Read out function
      fktName = (String) Repository.getFunctions().get(fktNr);
      type = 2;
    }

    allele = (Integer) op.getAllele();
    int opNr = allele.intValue();
    opNr = opNr % numberOfOperators;
    char opCh = ( (String) Repository.getOperators().get(opNr)).charAt(0);
    Term elem = new Term(type, fktName, 1, opCh);
    return elem;
  }

  public static String getFormulaFromChromosome(IChromosome chromosome) {
    List terms = constructTerms(chromosome.getGenes());
    return getFormula(terms);
  }

  /**
   * Constructs a formula string out of terms (each containing operators,
   * if applicable).
   *
   * @param a_elements ordered list of terms
   * @return constructed formula
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static String getFormula(List a_elements) {
    if (a_elements == null || a_elements.isEmpty()) {
      return "";
    }
    else {
      String result = "";
      do {
        result += getFormula_int(a_elements, result);
      }
      while (a_elements.size() > 0);
      if (result.length() < 1) {
        return "";
      }
      else {
        return "F(X)=" + result;
      }
    }
  }

  /**
   * Recursive part of getFormula.
   *
   * @param a_elements ordered list of terms
   * @param a_previous needed for recursion
   * @return constructed part of formula
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected static String getFormula_int(List a_elements, String a_previous) {
    if (a_elements.size() < 1) {
      return "";
    }
    Term element = (Term) a_elements.get(0);
    element = replaceSubstitute(element);
    String result = "";
    // Ignore illegal operator specification
    if (element.m_operator != ' ' && a_previous.length() > 0 &&
        !a_previous.endsWith("(")) {
      result += element.m_operator;
    }
    int type = element.m_termType;
    result += element.m_termName;
    a_elements.remove(0);
    if (type == 2) {
      result += "(";
      boolean compensationPossible = true;
      do {
        element.m_depth--;
        String tempRes = getFormula_int(a_elements, result);
        if (tempRes.length() > 0) {
          compensationPossible = false;
        }
        result += tempRes;
      }
      while (element.m_depth > 0);
      if (compensationPossible) {
        result += "X";
      }
      result += ")";
    }
    else if (type == 1) {
      element.m_depth--;
    }
    else {
      throw new RuntimeException("Invalid operator type: " + type);
    }
    return result;
  }

  private static Term replaceSubstitute(Term element) {
    if (element.m_termName.equals("+I")) {
      element.m_termName = String.valueOf(new Random().nextInt(3) + 1);
    }
    else if (element.m_termName.equals("-I")) {
      element.m_termName = String.valueOf( - (new Random().nextInt(10) + 1));
    }
    else if (element.m_termName.equals("+D")) {
      element.m_termName = String.valueOf(new Random().nextDouble() * 10);
      /**@todo recalculate fitnessValue of all Chromosomes using "+D"*/
    }
    else if (element.m_termName.equals("-D")) {
      element.m_termName = String.valueOf( - (new Random().nextDouble() * 10));
      /**@todo recalculate fitnessValue of all Chromosomes using "-D"*/
    }
    return element;
  }
}
