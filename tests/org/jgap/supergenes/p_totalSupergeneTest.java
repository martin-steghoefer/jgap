/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
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
class p_totalSupergeneTest {

    /** Test supported Supegene features, including performance tests.
     * @return true if the Supergene tests succeded.
     */
    public static boolean testSupergeneTotal()
    {
        try {
        System.out.println("Testing Supergene...");

        p_abstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
        p_abstractSupergeneTest.REPORT_ENABLED = false;
        p_Force.REPORT_ENABLED = false;

        System.out.println("Testing Persistent representation");
        assertTrue(
         "Persistent representation",
          SupergenePersistentRepresentationTest.testRepresentation());

        System.out.println("Testing Supergene 150 % performance benefit ");

        p_abstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
        p_abstractSupergeneTest.POPULATION_SIZE = 256;

        long abe = 0;


        int N = 12;

        for (int i = 1; i <= N; i++) {
            System.out.println("Iteration "+i+" of "+N);

            abstractSupergene.reset ();

            long s_started;
            // Test with Supergene
            System.out.print("            evaluating Supergene... ");
            s_started = System.currentTimeMillis ();
            int E_s = new p_SupergeneTest().test ();
            long d_supergene = System.currentTimeMillis () -
                s_started;

            // Test without Supergene
            System.out.println("control...");
            s_started = System.currentTimeMillis ();
            int E_w = new p_withoutSupergeneTest().test ();
            long d_without = System.currentTimeMillis () -
                s_started;

            assertTrue("Correctness of solution: supergene "+E_s+
            " control "+E_w, E_s==0 && E_w==0);

            long benefit = (100*d_without)/d_supergene;

            assertTrue("Computation speed: supergene "+d_supergene+
            " control "+d_without+", benefit "+benefit, true);

            abe+= benefit;
        }

        abe = abe/N;
        assertTrue("Averaged benefit "+abe, abe>=150);


        System.out.println("Supergene test complete.");
        return true;
        }
        catch (Exception ex)
         {
             return false;
         }
    }

    static void assertTrue(String msg, boolean b) throws Exception
    {
        System.out.print("   testing "+msg);
        if (!b)
         {
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
            System.in.read ();
        }
        catch (IOException ex) {
        }
    }

}
