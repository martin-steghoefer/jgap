package org.jgap.util;


/**
    Implementation of a <b>randomX</b>-compliant class using
    L'Ecuyer's two-sequence generator with a Bays-Durham shuffle, as
    described on page 282 of Press et al., <cite>Numerical Recipes in
    C</cite>, 2nd edition.  Their implementation was constrained by
    the absence of a 64-bit integer data type.  Since Java guarantees
    a <tt>long</tt> to be 64 bit, we can use L'Ecuyer's multiplier and modulus
    directly, rather than flailing around with Schrage's algorithm.
    Further, 64-bit <tt>long</tt> arithmetic allows us to directly combine
    the results from the two generators by adding and taking the modulus of
    one of them, bypassing the subtract and test for negative gimmick used
    in <cite>Numerical Recipes</cite>.

    <p>
    For additional details, see L'Ecuyer's original 1968 paper
    at page 742 of <cite>Communications of the ACM</cite>,
    Vol. 31.

    <p>
    Designed and implemented in July 1996 by
    <a href="http://www.fourmilab.ch/">John Walker</a>,
    <a href="mailto:kelvin@fourmilab.ch">kelvin@fourmilab.ch</a>.
*/
public class randomLEcuyer extends randomX {

    /* L'Ecuyer's recommended multiplier and modulus for the two
       multiplicative congruential generators.  Even though the
       values fit in 32 bits, we declare them as long so that the
       arithmetic in calculating the next value will be automatically
       done in long without need for casting. */

    static final long mul1 = 40014,
                      mod1 = 2147483563,
                      mul2 = 40692,
                      mod2 = 2147483399;
    static final int shuffleSize = 32,       // Shuffle table size
                     warmup = 19;            /* Number of initial warmup
                                                results to "burn" */

    int gen1, gen2, state;
    int [] shuffle;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomLEcuyer() {
        shuffle = new int[shuffleSize];
        this.setSeed(System.currentTimeMillis());
    }

    /** Creates a new pseudorandom number generator with a
        specified nonzero seed.

@param seed initial seed for the generator
    */

    public randomLEcuyer(long seed) throws IllegalArgumentException {
        shuffle = new int[shuffleSize];
        this.setSeed(seed);
    }

    //  Seed access

    /** Set seed for generator.  Subsequent values will be based
        on the given nonzero seed.

@param seed seed for the generator
    */

    public void setSeed(long seed) throws IllegalArgumentException {
        int i;

        if (seed == 0) {
            throw new IllegalArgumentException("seed must be nonzero");
        }
        gen1 = gen2 = (int) (seed & 0x7FFFFFFFFL);

        /* "Warm up" the generator for a number of rounds to eliminate
           any residual inflence of the seed. */

        for (i = 0; i < warmup; i++) {
            gen1 = (int) ((gen1 * mul1) % mod1);
        }

        // Fill the shuffle table with values

        for (i = 0; i < shuffleSize; i++) {
            gen1 = (int) ((gen1 * mul1) % mod1);
            shuffle[(shuffleSize - 1) - i] = gen1;
        }
        state = shuffle[0];
    }

    /** Get next byte from generator.

@return the next byte from the generator.
    */

    public byte nextByte() {
        int i;

        gen1 = (int) ((gen1 * mul1) % mod1);   // Cycle generator 1
        gen2 = (int) ((gen2 * mul2) % mod2);   // Cycle generator 2

        /* Extract shuffle table index from most significant part
           of the previous result. */

        i = state / (1 + (((int) mod1) - 1) / shuffleSize);

        // New state is sum of generators modulo one of their moduli

        state = (int) ((((long) shuffle[i]) + gen2) % mod1);

        // Replace value in shuffle table with generator 1 result

        shuffle[i] = gen1;

        return (byte) (state / (1 + (((int) mod1) - 1) / 256));
    }
};
