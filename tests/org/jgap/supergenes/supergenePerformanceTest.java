package tests.org.jgap.supergenes;

import org.jgap.supergenes.abstractSupergene;
import java.util.Set;

/**
 * Tests the performance, comparing computing time and the sum of the
 * computed change amount deviations from the required amount.
 * @author Audrius Meskauskas
 */

public class supergenePerformanceTest {

    public static void main(String[] args) {

        abstractSupergeneTest.REPORT_ENABLED = false;

        long t_s = 0;
        long t_w = 0;
        int e_s = 0;
        int e_w = 0;

        SupergeneTest st = new SupergeneTest();
        withoutSupergeneTest wt = new withoutSupergeneTest();

        for (int i = 0; i < 3; i++) {

        abstractSupergene.reset();

        long s_started;

        // Test with Supergene
        s_started = System.currentTimeMillis();
        int E_s = st.test();
        long d_supergene = System.currentTimeMillis()-s_started;

        // Test without Supergene
        s_started = System.currentTimeMillis();
        int E_w = wt.test();
        long d_without = System.currentTimeMillis()-s_started;


        System.out.println("With "+d_supergene+" ("+E_s+"), "+
         "without "+d_without+" ("+E_w+")");

        t_s+=d_supergene;
        t_w+=d_without;
        e_s+=E_s;
        e_w+=E_w;
        }

        System.out.println("FINAL: With "+t_s+" ("+e_s+"), "+
         "without "+t_w+" ("+e_w+")");

    }

}