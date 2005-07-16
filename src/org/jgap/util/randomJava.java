package org.jgap.util;


import org.jgap.*;

import java.util.Random;

/**
    Implementation of a <b>randomX</b>-compliant class based upon the
    built-in <tt>Java.util.Random</tt> generator.  Note that since the higher
    level result composition methods are different from those in the
    (undocumented) standard library, <b>randomJava</b> results will differ
    from those of the standard library for a given seed.

    <p>
    Designed and implemented in July 1996 by
    <a href="http://www.fourmilab.ch/">John Walker</a>,
    <a href="mailto:kelvin@fourmilab.ch">kelvin@fourmilab.ch</a>.
*/
public class randomJava extends randomX {

    private Random r;
    private int ibytes = 0;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomJava() {
        /* Since Java's generator seeds itself from the current time
           when called with no arguments, we don't have to pass in a
           seed here. */
        r = new Random();
    }

    /** Creates a new pseudorandom number generator with a
        specified seed.

@param seed initial seed for the generator
    */

    public randomJava(long seed) {
        r = new Random(seed);
    }

    /** Set seed for generator.  Subsequent values will be based
        on the given seed.

@param seed seed for the generator
    */

    public void setSeed(long seed) {
        super.setSeed();              // Notify parent seed has changed
        r.setSeed(seed);
        ibytes = 0;                   // Clear bytes in nextByte buffer
    }

    private int idat;

    /** Get next byte from generator.  To minimise calls on the
        underlying Java generator, integers are generated and
        individual bytes returned on subsequent calls.  A call
        on <tt>setSeed()</tt> discards any bytes in the buffer.

@return the next byte from the generator.
    */

    public byte nextByte() {
        byte d;

        if (ibytes <= 0) {
            idat = r.nextInt();
            ibytes = 4;
        }
        d = (byte) idat;
        idat >>= 8;
        ibytes--;
        return d;
    }
};
