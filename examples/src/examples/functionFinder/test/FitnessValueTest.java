package examples.functionFinder.test;

import junit.framework.*;

import examples.functionFinder.*;

/**
 * Tests for FitnessValue class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class FitnessValueTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public FitnessValueTest() {
    super();
  }

  public int getFitness(float input) {
    int fitness = MatchAgainstTruthTable.getFitness(input);
    assertTrue(fitness >= 0);
    return fitness;
  }

  public void testFitness0_1() {
    //fitness for diff 0 must be maximum
    assertEquals(MatchAgainstTruthTable.MAX_FITNESS, getFitness(0),MatchAgainstTruthTable.DELTA);
    //fitness for diff 0 must be greater than for diff 1.1
    assertTrue(getFitness(0) > getFitness(1.1f));
    //fitness for diff 0 must be greater than for diff 1.0001
    assertTrue(getFitness(0) > getFitness(1.0001f));
  }

  public void testFitness0_2() {
    //fitness for diff 0 must be greater than for diff 1.0
    int diff1 = getFitness(0);
    int diff2 = getFitness(1.0f);
    assertTrue( diff1 > diff2);
  }

  public void testFitness0_05() {
    //fitness for diff 0 must be greater than for diff 0.5
    assertTrue(getFitness(0) > getFitness(0.5f));
  }

  public void testFitness01_02() {
    //fitness for diff 0.01 must be greater/equal than for diff 0.2
    int diff1 = getFitness(0.01f);
    int diff2 = getFitness(0.2f);
    assertTrue(diff1 >= diff2);
  }

  public void testFitness001_01() {
    //fitness for diff 0.01 must be greater/equal than for diff 0.1
    assertTrue(getFitness(0.01f) >= getFitness(0.1f));
  }

  public void testFitness0_01() {
    //fitness for diff 0 must be greater than for diff 0.1
    assertTrue(getFitness(0) > getFitness(0.1f));
  }

  public void testFitness0_DELTA() {
    //fitness for diff 0 must be greater than for diff 1.0
    int diff1 =getFitness(0);
    int diff2 =getFitness(MatchAgainstTruthTable.DELTA);
    assertTrue( diff1 >= diff2);
  }

  public void testFitness01_05() {
    //fitness for diff 0.1 must be greater/equal than for diff 0.5
    assertTrue(getFitness(0.1f) >= getFitness(0.5f));
  }

  /**
   * fitness for diff 2 must be greater than 90% of MAX
   */
  public void testFitness2() {
    int diff1 = getFitness(2);
    assertTrue(diff1 > MatchAgainstTruthTable.MAX_FITNESS*0.9);
  }

  /**
   * fitness for diff 10 must be less than MAX
   */
  public void testFitness10() {
    int diff1 = getFitness(10);
    assertTrue(diff1 < MatchAgainstTruthTable.MAX_FITNESS);
  }

  public void testFitness10_100() {
    //fitness for diff 10 must be greater than fitness for diff 100
    int diff1 = getFitness(10);
    int diff2 = getFitness(100);
    assertTrue(diff1 > diff2);
  }

  public void testFitness20_21() {
    //fitness for diff 20 must be greater/equal than fitness for diff 21
    int diff1 = getFitness(20);
    int diff2 = getFitness(21);
    assertTrue(diff1 >= diff2);
  }

  public void testFitness2048() {
    assertTrue(getFitness(20)>5);
  }

  public void testFitness20000_20001() {
    //fitness for diff 20000 must be greater/equal than fitness for diff 20001
    int diff1 = getFitness(20000);
    int diff2 = getFitness(20001);
    assertTrue(diff1 >= diff2);
  }

  public void testFitnessWORST() {
    //fitness for worst input must be 0
    int diff = getFitness(MatchAgainstTruthTable.MAX_FITNESS);
    assertTrue(diff == 0);
    diff = getFitness(MatchAgainstTruthTable.MAX_FITNESS + 100);
    assertTrue(diff == 0);
  }

  public void testFitnessNegative() {
    //fitness for negative input must be equal to positive input
    int diff1 = getFitness(-200);
    int diff2 = getFitness(200);
    assertTrue(diff1 == diff2);
  }

  public void testStetigkeit() {
    int oldFitness = -1;
    int newFitness;
    int diff1 = getFitness(81.92f);
    for (float i=0.01f;i<20000;i=i*2) {
//      System.err.print("i: "+i);
      newFitness = getFitness(i);
//      System.err.print(" --> "+newFitness);
//      System.err.println(" EXP = "+Math.pow(1.05d, i));
      if (oldFitness != -1) {
        assertTrue(oldFitness >= newFitness);
      }
      oldFitness = newFitness;
    }
  }
}
