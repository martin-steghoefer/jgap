package org.jgap.supergenes.test;

public class supergenePerformanceTest {

    public static void main(String[] args) {
        long s_started = System.currentTimeMillis();
        SupergeneTest.main(null);
        long d_supergene = System.currentTimeMillis()-s_started;

        s_started = System.currentTimeMillis();
        withoutSupergeneTest.main(null);
        long d_without = System.currentTimeMillis()-s_started;

        System.out.println("With "+d_supergene+", without "+d_without);
    }

}