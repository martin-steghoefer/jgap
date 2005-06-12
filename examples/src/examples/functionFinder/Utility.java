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
 * Utility functions
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Utility {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

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
   * Constructs terms that are represented by a list of genes
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
   * Constructs a term that is represented by a gene tupel (containing a tupel)
   * @param genes Gene[]
   * @return Term
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static Term constructTerm(Gene[] genes) {
    Gene op = genes[1];
    Integer allele = (Integer) genes[0].getAllele();
    int fktNr = allele.intValue();
    fktNr = fktNr % (numberOfFunctions + numberOfConstants);
    String fktName;
    int type;
    if (fktNr >= numberOfFunctions) {
      // Read out constant
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

  public static String getFormulaFromChromosome(Chromosome chromosome) {
    List terms = constructTerms(chromosome.getGenes());
    return getFormula(terms);
  }

  /**
   * Constructs a formula string out of terms (each containing operators,
   * if applicable)
   * @param elements ordered list of terms
   * @return constructed formula
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public static String getFormula(List elements) {
    if (elements == null || elements.isEmpty()) {
      return "";
    }
    else {
      String result = "";
      do {
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
   * @param elements ordered list of terms
   * @param previous needed for recursion
   * @return constructed part of formula
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  protected static String getFormula_int(List elements, String previous) {
    if (elements.size() < 1) {
      return "";
    }
    Term element = (Term) elements.get(0);
    element = replaceSubstitute(element);
    String result = "";
    // Ignore illegal operator specification
    if (element.operator != ' ' && previous.length() > 0 &&
        !previous.endsWith("(")) {
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
      element.depth--;
    }
    else {
      throw new RuntimeException("Invalid operator type: " + type);
    }
    return result;
  }

  private static Term replaceSubstitute(Term element) {
    if (element.termName.equals("+I")) {
      element.termName = String.valueOf(new Random().nextInt(3) + 1);
    }
    else if (element.termName.equals("-I")) {
      element.termName = String.valueOf( - (new Random().nextInt(10) + 1));
    }
    else if (element.termName.equals("+D")) {
      element.termName = String.valueOf(new Random().nextDouble() * 10);
      /**@todo recalculate fitnessValue of all Chromosomes using "+D"*/
    }
    else if (element.termName.equals("-D")) {
      element.termName = String.valueOf( - (new Random().nextDouble() * 10));
      /**@todo recalculate fitnessValue of all Chromosomes using "-D"*/
    }
    return element;
  }
}
