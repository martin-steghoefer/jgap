package org.jgap.supergenes;

import org.jgap.supergenes.abstractSupergene;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.*;

/**
 * Tests the performance, comparing computing time and the sum of the
 * computed change amount deviations from the required amount.
 * @author Audrius Meskauskas
 */

public class supergenePerformanceTest {

    public static void main(String[] args) {

        try {
            abstractSupergeneTest.REPORT_ENABLED = false;

            SupergeneTest st = new SupergeneTest ();
            withoutSupergeneTest wt = new withoutSupergeneTest ();

            FileOutputStream fo = new FileOutputStream ("Test_result.prn");
            PrintStream out = new PrintStream (fo);

            out.println ("Pop size\t Max iter\t t,supergene"+
                         "\t t, control" +
                         "\t Err, supergene \t Err, control");

            int maxiter, popsize, i;

            for (maxiter = 1; maxiter <= 256; maxiter = maxiter * 4) {
                abstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = maxiter;
                for (popsize = 16; popsize < 5000; popsize = popsize * 2) {
                    abstractSupergeneTest.POPULATION_SIZE = popsize;
                    int e_s = 0;
                    int e_w = 0;

                    long t_s = 0;
                    long t_w = 0;

                    for (i = 0; i < 10; i++) {
                        abstractSupergene.reset ();

                        long s_started;

                        // Test with Supergene
                        s_started = System.currentTimeMillis ();
                        int E_s = st.test ();
                        long d_supergene = System.currentTimeMillis () -
                            s_started;

                        // Test without Supergene
                        s_started = System.currentTimeMillis ();
                        int E_w = wt.test ();
                        long d_without = System.currentTimeMillis () -
                            s_started;

                        t_s += d_supergene;
                        t_w += d_without;
                        e_s += E_s;
                        e_w += E_w;
                    }

                    String r = (popsize + "\t " + maxiter + "\t " + t_s +
                                 "\t " + t_w +
                                 "\t " + e_s + "\t " + e_w);

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
