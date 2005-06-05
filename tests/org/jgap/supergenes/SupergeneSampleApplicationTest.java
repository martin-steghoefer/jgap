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

import junit.framework.*;
import org.jgap.*;

/**
 * Test Supergene sample application, verifying the "make change"
 * supergene and non-supergene versions. Both must return a zero error.
 * Performance is not verified.
 * @return true if the Supergene tests succeded.
 *
 * @author Meskauskas Audrius
 * @since 2.0
 */
public class SupergeneSampleApplicationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public void testSupergeneTotal() {
    int E_s = Integer.MAX_VALUE;
    Test:
        for (int i = 0; i < 2; i++) {
      AbstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      AbstractSupergeneTest.REPORT_ENABLED = false;
      Force.REPORT_ENABLED = false;
      AbstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      AbstractSupergeneTest.POPULATION_SIZE = 256;
      abstractSupergene.reset();
      E_s = new SupergeneTest().test();
      if (E_s == 0) {
        break Test;
      }
      assertTrue("Correctness of solution: supergene " + E_s, E_s < 3);
    }
    assertTrue("Correctness of solution: supergene " + E_s, E_s == 0);
  }

  public static Test suite() {
    TestSuite suite =
        new TestSuite(SupergeneSampleApplicationTest.class);
    return suite;
  }
}
