package org.jgap.impl.salesman;

import junit.framework.*;

/** Test the Travelling salesman package (about 0.8 second) */
public class AllTests
    extends TestCase {

    public AllTests (String s) {
        super (s);
    }

    public static Test suite () {
        TestSuite suite = new TestSuite ();
        suite.addTestSuite (org.jgap.impl.salesman.testTravellingSalesman.class);
        return suite;
    }
}
