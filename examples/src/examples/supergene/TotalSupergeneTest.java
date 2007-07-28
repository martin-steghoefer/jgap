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

import java.io.*;

import junit.framework.*;
import org.jgap.supergenes.*;

/**
 * Total test of the supported Supergene classes. Due to slow run it is not
 * included into AllTests.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class TotalSupergeneTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Test supported Supegene features, including performance tests.
   *
   *  @throws Exception
   */
  public void testSupergeneTotal() throws Exception {
      AbstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      AbstractSupergeneTest.REPORT_ENABLED = false;
      Force.REPORT_ENABLED = false;
//      assertTrue("Persistent representation",
//          SupergenePersistentRepresentationTest.testRepresentation());
      AbstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      AbstractSupergeneTest.POPULATION_SIZE = 256;
      long abe = 0;
      int N = 12;
      for (int i = 1; i <= N; i++) {
        System.out.println("Iteration " + i + " of " + N);
        AbstractSupergene.reset();
        long s_started;
        // Test with Supergene
        s_started = System.currentTimeMillis();
        int E_s = new SupergeneSample().test();
        long d_supergene = System.currentTimeMillis() - s_started;
        // Test without Supergene
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
  }
}
