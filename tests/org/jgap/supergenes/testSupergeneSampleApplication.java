package org.jgap.supergenes;

import junit.framework.*;

/**
* Test Supergene sample application, verifying the "make change"
* supergene and non-supergene versions. Both must return a zero error.
* Performance is not verified.
* @return true if the Supergene tests succeded.
*/

public class testSupergeneSampleApplication extends TestCase {

  public void testSupergeneTotal()
  {
    int E_s = Integer.MAX_VALUE;
    Test:
    for (int i = 0; i < 2; i++) {
      abstractSupergeneTest.EXISTING_SOLUTIONS_ONLY = true;
      abstractSupergeneTest.REPORT_ENABLED = false;
      Force.REPORT_ENABLED = false;

      abstractSupergeneTest.MAX_ALLOWED_EVOLUTIONS = 512;
      abstractSupergeneTest.POPULATION_SIZE = 256;

      abstractSupergene.reset ();
      E_s = new SupergeneTest().test ();

      if (E_s == 0) break Test;

      assertTrue("Correctness of solution: supergene "+E_s, E_s <3);
      System.out.println(" next iteration");
    }
    assertTrue("Correctness of solution: supergene "+E_s, E_s == 0);
  }

  protected void setUp() throws Exception {
      super.setUp();
  }

  protected void tearDown() throws Exception {
      super.tearDown();
  }

  public static Test suite() {
    TestSuite suite =
     new TestSuite(testSupergeneSampleApplication.class);
    return suite;
  }

}