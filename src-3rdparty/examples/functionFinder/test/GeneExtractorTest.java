package examples.functionFinder.test;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;
import examples.functionFinder.*;

/**
 * Tests for GeneExtractor class.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class GeneExtractorTest
    extends JGAPTestCase {
  private static int numberOfFunctions;

  private static int numberOfConstants;

  private static int numberOfOperators;

  public GeneExtractorTest(String name) {
    super(name);
  }

  /**
   * @author Klaus Meffert
   */
  public static void init() {
    Repository.init();
    numberOfFunctions = Repository.getFunctions().size();
    numberOfConstants = Repository.getConstants().size();
    numberOfOperators = Repository.getOperators().size();
  }

  public void setUp() {
    super.setUp();
    init();
  }

  /**
   * Use a function.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_0() throws Exception {
    Gene[] genes = new Gene[2];
    genes[0] = new TestGene(conf, 3);
    final int opNr = 2;
    genes[1] = new TestGene(conf, opNr);
    Term elem = constructTerm(genes);
    assertEquals(Repository.getFunctions().get(3), elem.m_termName);
    assertEquals( ( (String) Repository.getOperators().get(opNr)).charAt(0)
                 , elem.m_operator);
  }

  /**
   * Use a constant.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_1() throws Exception {
    Gene[] genes = new Gene[2];
    genes[0] = new TestGene(conf, numberOfFunctions + 0);
    genes[1] = new TestGene(conf, 2);
    Term elem = constructTerm(genes);
    assertEquals(Repository.getConstants().get(0), elem.m_termName);
    assertEquals( ( (String) Repository.getOperators().get(2)).charAt(0),
                 elem.m_operator);
  }

  /**
   * Use a constant (forcing modulo for functions).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_2() throws Exception {
    Gene[] genes = new Gene[2];
    genes[0] = new TestGene(conf,
                            (numberOfFunctions + 0) + 33 * (numberOfConstants
        + numberOfFunctions));
    genes[1] = new TestGene(conf, 2);
    Term elem = constructTerm(genes);
    assertEquals(Repository.getConstants().get(0), elem.m_termName);
    assertEquals( ( (String) Repository.getOperators().get(2)).charAt(0),
                 elem.m_operator);
  }

  /**
   * Use a function (forcing modulo for operator).
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_3() throws Exception {
    Gene[] genes = new Gene[2];
    genes[0] = new TestGene(conf, 3);
    final int opNr = 1;
    genes[1] = new TestGene(conf, numberOfOperators * 24 + opNr);
    Term elem = constructTerm(genes);
    assertEquals(Repository.getFunctions().get(3), elem.m_termName);
    assertEquals( ( (String) Repository.getOperators().get(opNr)).charAt(0)
                 , elem.m_operator);
  }

  /**
   * Use a function.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_4()
      throws Exception {
    CompositeGene comp = new CompositeGene(conf);
    Gene[] genes = new Gene[2];
    comp.addGene(new TestGene(conf, 3));
    genes[0] = comp;
    try {
      constructTerms(genes);
      fail();
    }
    catch (ArrayIndexOutOfBoundsException nex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGene_5()
      throws Exception {
    CompositeGene comp = new CompositeGene(conf);
    Gene[] genes = new Gene[1];
    final int fktNr = 4;
    comp.addGene(new TestGene(conf, fktNr));
    final int opNr = 2;
    comp.addGene(new TestGene(conf, opNr));
    genes[0] = comp;
    Vector elems = constructTerms(genes);
    Term elem = (Term) elems.elementAt(0);
    assertEquals(Repository.getFunctions().get(fktNr), elem.m_termName);
    assertEquals( ( (String) Repository.getOperators().get(opNr)).charAt(0),
                 elem.m_operator);
  }

  /**
   * Helper method
   * @param genes Gene[]
   * @return Vector
   *
   * @author Klaus Meffert
   */
  private Vector constructTerms(Gene[] genes) {
    Vector result = new Vector();
    Term term;
    Gene[] geneTupel;
    CompositeGene comp;
    Gene gene1;
    Gene gene2;
    int totalTiefe = 0;
    for (int i = 0; i < genes.length; i++) {
      comp = (CompositeGene) genes[i];
      gene1 = comp.geneAt(0);
      gene2 = comp.geneAt(1);
      geneTupel = new Gene[] {
          gene1, gene2};
      term = constructTerm(geneTupel);
      //Alle Tiefen von Funktionen addieren (nicht die von Konstanten)
      if (term.m_termType == 2) {
        totalTiefe += term.m_depth;
      }
      result.add(term);
    }
    //Die Tiefe pro Term anpassen: Reicht die Anzahl der Terme insgesamt nicht aus,
    //dann alles pauschal kürzen. Sind zu viele Terme da, dann alles pauschal
    //erhöhen. Zuerst alles relativ gleich stark kürzen/erhöhen.
    //Dann ab dem ersten Term so lange die Terme durchlaufen und bei jedem die Tiefe
    //um 1 kürzen/erhöhen, bis exakt alle Terme abgedeckt sind
    /**@todo folgendes wegkicken, oder?*/
    int termAnz = result.size();
    int pauschalDazu;
    boolean negativ;
    if (totalTiefe > 0) {
      if (termAnz < totalTiefe) {
        pauschalDazu = totalTiefe / termAnz - 1;
        negativ = true;
      }
      else {
        pauschalDazu = termAnz / totalTiefe - 1;
        negativ = false;
      }
    }
    else {
      //what to do here?
      pauschalDazu = 0;
      negativ = false;
    }
    int restTiefe;
    if (negativ) {
      restTiefe = totalTiefe - termAnz * pauschalDazu;
    }
    else {
      restTiefe = termAnz - totalTiefe * pauschalDazu;
    }
    /**@todo folgendes wegkicken, oder?*/
    if (false && (restTiefe > 0 || pauschalDazu > 0)) {
      //tiefenpunkt verteilen auf Formelterme
      for (int i = 0; i < termAnz; i++) {
        term = (Term) result.elementAt(i);
        if (term.m_termType == 2) {
          if (!negativ) {
            term.m_depth += 1 + pauschalDazu;
            restTiefe--;
          }
          else {
            if (term.m_depth > 1) {
              if (restTiefe > 0) {
                term.m_depth -= 1;
                restTiefe--;
              }
              if (term.m_depth > 1 + pauschalDazu) {
                term.m_depth -= pauschalDazu;
              }
              else {
                if (term.m_depth > 1) {
                  int reduzier = term.m_depth - 1;
                  term.m_depth = 1;
                  restTiefe -= reduzier;
                }
              }
            }
          }
        }
      }
    }
    return result;
  }

  /**
   *
   * @param genes Gene[]
   * @return Term
   * @author Klaus Meffert
   */
  private Term constructTerm(Gene[] genes) {
    Gene fkt = genes[0];
    Gene op = genes[1];
    Integer allele = (Integer) fkt.getAllele();
    int fktNr = allele.intValue();
    fktNr = fktNr % (numberOfFunctions + numberOfConstants);
    String fktName;
    int type;
    if (fktNr >= numberOfFunctions) {
      //Konstante auslesen
      fktName = (String) Repository.getConstants().get(fktNr -
          numberOfFunctions);
      type = 1;
    }
    else {
      //Funktion auslesen
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

  /**
   * Gene that always returns the same (Integer) allele value
   *
   * @author Klaus Meffert
   */
  private class TestGene
      extends BaseGene {
    private int value;

    public TestGene(final Configuration a_conf, int value)
        throws InvalidConfigurationException {
      super(a_conf);
      this.value = value;
    }

    public Object getAllele() {
      return new Integer(value);
    }

    public Object getInternalValue() {
      return getAllele();
    }

    public void setToRandomValue(RandomGenerator a_numberGenerator) {
    }

    public void setValueFromPersistentRepresentation(String a_representation)
        throws UnsupportedOperationException,
        UnsupportedRepresentationException {
    }

    public String getPersistentRepresentation()
        throws UnsupportedOperationException {
      return null;
    }

    public void setAllele(Object a_newValue) {
    }

    public int compareTo(Object o) {
      return 0;
    }

    public void applyMutation(int index, double a_percentage) {
    }

    public int size() {
      return 1;
    }

    protected Gene newGeneInternal() {
      return null;
    }
  }
}
