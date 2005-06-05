/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.supergenes.*;
import java.io.*;

/**
 * Total test of the supported Supergene classes. Due slow run it is not
 * included into AllTests
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
class TotalSupergeneTest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Test supported Supegene features, including performance tests.
   * @return true if the Supergene tests succeded
   */
  public static boolean testSupergeneTotal() {
    try {
      System.out.println("Testing Supergene...");
      AbstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      AbstractSupergeneTest.REPORT_ENABLED = false;
      Force.REPORT_ENABLED = false;
      System.out.println("Testing Persistent representation");
      assertTrue(
          "Persistent representation",
          SupergenePersistentRepresentationTest.testRepresentation());
      System.out.println("Testing Supergene 150 % performance benefit ");
      AbstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      AbstractSupergeneTest.POPULATION_SIZE = 256;
      long abe = 0;
      int N = 12;
      for (int i = 1; i <= N; i++) {
        System.out.println("Iteration " + i + " of " + N);
        abstractSupergene.reset();
        long s_started;
        // Test with Supergene
        System.out.print("            evaluating Supergene... ");
        s_started = System.currentTimeMillis();
        int E_s = new SupergeneTest().test();
        long d_supergene = System.currentTimeMillis() -
            s_started;
        // Test without Supergene
        System.out.println("control...");
        s_started = System.currentTimeMillis();
        int E_w = new WithoutSupergeneTest().test();
        long d_without = System.currentTimeMillis() -
            s_started;
        assertTrue("Correctness of solution: supergene " + E_s +
                   " control " + E_w, E_s == 0 && E_w == 0);
        long benefit = (100 * d_without) / d_supergene;
        assertTrue("Computation speed: supergene " + d_supergene +
                   " control " + d_without + ", benefit " + benefit, true);
        abe += benefit;
      }
      abe = abe / N;
      assertTrue("Averaged benefit " + abe, abe >= 150);
      System.out.println("Supergene test complete.");
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  static void assertTrue(String msg, boolean b)
      throws Exception {
    System.out.print("   testing " + msg);
    if (!b) {
      System.out.println(" This test failed.");
      throw new Exception();
    }
    else
      System.out.println(" ok");
  }

  public static void main(String[] args) {
    boolean t = testSupergeneTotal();
    if (t)
      System.out.println("TEST SUCCEEDED");
    else
      System.out.println("TEST FAILED");
    try {
      System.in.read();
    }
    catch (IOException ex) {
      ;
    }
  }
}
