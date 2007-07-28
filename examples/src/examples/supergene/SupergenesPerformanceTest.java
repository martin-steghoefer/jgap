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
import org.jgap.supergenes.*;

/**
 * Tests the performance, comparing computing time and the sum of the
 * computed change amount deviations from the required amount.
 * <P>
 * Result of test is briefly outputted to the console and to a file named
 * "Test_result.prn"
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public final class SupergenesPerformanceTest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Starts the performance test.
   *
   * @param args ignored
   */
  public static void main(String[] args) {
    try {
      AbstractSupergeneTest.REPORT_ENABLED = false;
      SupergeneSample st = new SupergeneSample();
      WithoutSupergeneSample wt = new WithoutSupergeneSample();
      FileOutputStream fo = new FileOutputStream("Test_result.prn");
      PrintStream out = new PrintStream(fo);
      String s = "Popsize\t MaxIter\t t,supergene"
          + "\t t,control"
          + "\t Err,supergene \t Err,control";
      out.println(s);
      System.out.println(s);
      int maxiter, popsize, i;
      for (maxiter = 1; maxiter <= 256; maxiter = maxiter * 4) {
        AbstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = maxiter;
        for (popsize = 16; popsize < 2000; popsize = popsize * 2) {
          AbstractSupergeneTest.POPULATION_SIZE = popsize;
          int e_s = 0;
          int e_w = 0;
          long t_s = 0;
          long t_w = 0;
          for (i = 0; i < 10; i++) {
            AbstractSupergene.reset();
            long s_started;
            // Test with Supergene.
            // --------------------
            s_started = System.currentTimeMillis();
            int E_s = st.test();
            long d_supergene = System.currentTimeMillis() - s_started;
            // Test without Supergene.
            // -----------------------
            s_started = System.currentTimeMillis();
            /*int E_w = */wt.test();
            long d_without = System.currentTimeMillis() - s_started;
            t_s += d_supergene;
            t_w += d_without;
            e_s += E_s;
          }
          String r = (popsize + "\t " + maxiter + "\t " + t_s
                      + "\t " + t_w + "\t " + e_s + "\t " + e_w);
          out.println(r);
          System.out.println(r);
        }
      }
      out.flush();
      out.close();
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
  }
}
