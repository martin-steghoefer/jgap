/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.supergene;

import junit.framework.*;
import org.jgap.supergenes.*;

/**
 * Total test of the supported Supergene classes. Due to slow run it is not
 * included into the test suite.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class TotalSupergeneTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Test supported Supergene features, including performance tests.
   * previously: return true if the Supergene tests succeded
   * @throws Exception
   */
  public void testSupergeneTotal() throws Exception {
      System.out.println("Testing Supergene...");
      AbstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      AbstractSupergeneTest.REPORT_ENABLED = false;
      Force.REPORT_ENABLED = false;
      System.out.println("Testing Persistent representation");
//      assertTrue("Persistent representation",
//          SupergenePersistentRepresentationTest.testRepresentation());
      System.out.println("Testing Supergene 150 % performance benefit ");
      AbstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      AbstractSupergeneTest.POPULATION_SIZE = 256;
      long abe = 0;
      int N = 12;
      for (int i = 1; i <= N; i++) {
        System.out.println("Iteration " + i + " of " + N);
        AbstractSupergene.reset();
        long s_started;
        // Test with Supergene
        System.out.print("            evaluating Supergene... ");
        s_started = System.currentTimeMillis();
        int E_s = new SupergeneSample().test();
        long d_supergene = System.currentTimeMillis() - s_started;
        // Test without Supergene
        System.out.println("control...");
        s_started = System.currentTimeMillis();
        int E_w = new WithoutSupergeneSample().test();
        long d_without = System.currentTimeMillis() - s_started;
        assertTrue("Correctness of solution: supergene " + E_s
                   + " control " + E_w, E_s == 0 && E_w == 0);
        long benefit = (100 * d_without) / d_supergene;
        assertTrue("Computation speed: supergene " + d_supergene
                   + " control " + d_without + ", benefit " + benefit, true);
        abe += benefit;
      }
      abe = abe / N;
      assertTrue("Averaged benefit " + abe, abe >= 150);
      System.out.println("Supergene test complete.");
//      return true;
  }
}
