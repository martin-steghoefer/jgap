package org.jgap.util;

/**
    Implementation of a <b>randomX</b>-compliant class using the
    simple (and not very good) <tt>rand()</tt> linear congruential
    generator given as an example in the ANSI C specification.  This
    is intended not for serious use, merely as an illustration of a
    simple software-based <b>randomX</b> generator.

    <p>
    The generation algorithm is:

    <p>
    <center>
        <em>I<sub>j+1</sub></em> = (<em>I<sub>j</sub></em> × 1103515245 + 12345) & 0x7FFFFFFF
    </center>

    <p>
    Designed and implemented in July 1996 by
    <a href="http://www.fourmilab.ch/">John Walker</a>,
    <a href="mailto:kelvin@fourmilab.ch">kelvin@fourmilab.ch</a>.
*/
public class randomLCG extends randomX {
    long state;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomLCG() {
        this.setSeed(System.currentTimeMillis());
    }

    /** Creates a new pseudorandom number generator with a
        specified seed.

@param seed initial seed for the generator
    */

    public randomLCG(long seed) {
        this.setSeed(seed);
    }

    //  Seed access

    /** Set seed for generator.  Subsequent values will be based
        on the given seed.

@param seed seed for the generator
    */

    public void setSeed(long seed) {
        super.setSeed();              // Notify parent seed has changed
        state = seed & 0xFFFFFFFFL;
    }

    /** Get next byte from generator.  Given how poor this generator
        is, it's wise to make a separate call for each byte rather than
        extract fields from a single call, which may be correlated.
        Also, the higher-order bits of this generator are more
        random than the low, so we extract the byte after discarding
        the low-order 11 bits.

@return the next byte from the generator.
    */

    public byte nextByte() {
        state = (state * 1103515245L + 12345L) & 0x7FFFFFFFL;
        return (byte) ((state >> 11) & 0xFF);
    }
};
