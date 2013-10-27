package examples.functionFinder.test;

import examples.functionFinder.*;
import org.jgap.*;

/**
 * Tests the FitnessValue class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class FitnessValueTest extends JGAPTestCase {

  private MatchAgainstTruthTable matcher;

  public FitnessValueTest() {
    super();
    matcher = new MatchAgainstTruthTable(null);
  }

  public double getFitness(float input) throws Throwable {
    double fitness = ((Double)privateAccessor.invoke(matcher, "scaleFitness", new Class[] {float.class}
                                            , new Object[] {new Float(input)})).doubleValue();
    assertTrue(fitness >= 0);
    return fitness;
  }

  public void testFitness0_1() throws Throwable{
    //fitness for diff 0 must be maximum
    assertEquals(MatchAgainstTruthTable.MAX_FITNESS, getFitness(0),MatchAgainstTruthTable.DELTA);
    //fitness for diff 0 must be greater than for diff 1.1
    assertTrue(getFitness(0) > getFitness(1.1f));
    //fitness for diff 0 must be greater than for diff 1.0001
    assertTrue(getFitness(0) > getFitness(1.0001f));
  }

  public void testFitness0_2() throws Throwable{
    //fitness for diff 0 must be greater than for diff 1.0
    double diff1 = getFitness(0);
    double diff2 = getFitness(1.0f);
    assertTrue( diff1 > diff2);
  }

  public void testFitness0_05() throws Throwable{
    //fitness for diff 0 must be greater than for diff 0.5
    assertTrue(getFitness(0) > getFitness(0.5f));
  }

  public void testFitness01_02() throws Throwable{
    //fitness for diff 0.01 must be greater/equal than for diff 0.2
    double diff1 = getFitness(0.01f);
    double diff2 = getFitness(0.2f);
    assertTrue(diff1 >= diff2);
  }

  public void testFitness001_01() throws Throwable{
    //fitness for diff 0.01 must be greater/equal than for diff 0.1
    assertTrue(getFitness(0.01f) >= getFitness(0.1f));
  }

  public void testFitness0_01() throws Throwable{
    //fitness for diff 0 must be greater than for diff 0.1
    assertTrue(getFitness(0) > getFitness(0.1f));
  }

  public void testFitness0_DELTA() throws Throwable{
    //fitness for diff 0 must be greater than for diff 1.0
    double diff1 =getFitness(0);
    double diff2 =getFitness(MatchAgainstTruthTable.DELTA);
    assertTrue( diff1 >= diff2);
  }

  public void testFitness01_05() throws Throwable{
    //fitness for diff 0.1 must be greater/equal than for diff 0.5
    assertTrue(getFitness(0.1f) >= getFitness(0.5f));
  }

  /**
   * fitness for diff 2 must be greater than 90% of MAX
   */
  public void testFitness2() throws Throwable{
    double diff1 = getFitness(2);
    assertTrue(diff1 > MatchAgainstTruthTable.MAX_FITNESS*0.9);
  }

  /**
   * fitness for diff 10 must be less than MAX
   */
  public void testFitness10() throws Throwable{
    double diff1 = getFitness(10);
    assertTrue(diff1 < MatchAgainstTruthTable.MAX_FITNESS);
  }

  public void testFitness10_100() throws Throwable{
    //fitness for diff 10 must be greater than fitness for diff 100
    double diff1 = getFitness(10);
    double diff2 = getFitness(100);
    assertTrue(diff1 > diff2);
  }

  public void testFitness20_21() throws Throwable{
    //fitness for diff 20 must be greater/equal than fitness for diff 21
    double diff1 = getFitness(20);
    double diff2 = getFitness(21);
    assertTrue(diff1 >= diff2);
  }

  public void testFitness2048() throws Throwable{
    assertTrue(getFitness(20)>5);
  }

  public void testFitness20000_20001() throws Throwable{
    //fitness for diff 20000 must be greater/equal than fitness for diff 20001
    double diff1 = getFitness(20000);
    double diff2 = getFitness(20001);
    assertTrue(diff1 >= diff2);
  }

  public void testFitnessWORST() throws Throwable{
    //fitness for worst input must be 0
    double diff = getFitness(MatchAgainstTruthTable.MAX_FITNESS);
    assertTrue(diff == 0);
    diff = getFitness(MatchAgainstTruthTable.MAX_FITNESS + 100);
    assertTrue(diff == 0);
  }

  public void testFitnessNegative() throws Throwable{
    //fitness for negative input must be equal to positive input
    double diff1 = getFitness(-200);
    double diff2 = getFitness(200);
    assertEquals(diff1, diff2, DELTA);
  }

  public void testStetigkeit() throws Throwable{
    double oldFitness = -1;
    double newFitness;
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
