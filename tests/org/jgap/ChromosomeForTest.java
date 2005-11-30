/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import org.jgap.impl.*;

/**
 * Derived Chromosome class for testing purposes only.
 *
 * @author Klaus Meffert
 * @since 2.5
 */
public class ChromosomeForTest
    extends Chromosome {
  public boolean isCloned;

  // Default constructor needed for construction via newInstance()
  public ChromosomeForTest() {
  }

  public ChromosomeForTest(Gene[] a_initialGenes) {
    super(a_initialGenes);
  }

  public int getComputedTimes() {
    return TestResultHolder.computedTimes;
  }

  public void resetComputedTimes() {
    TestResultHolder.computedTimes = 0;
  }

  public double getFitnessValue() {
    if (isCloned && m_fitnessValue < 0) {
      // Record test result!
      TestResultHolder.computedTimes++;
    }
    return super.getFitnessValue();
  }

  public Chromosome randomInitialChromosome2()
      throws InvalidConfigurationException {
    // Sanity check: make sure the given configuration isn't null.
    // -----------------------------------------------------------
    if (Genotype.getConfiguration() == null) {
      throw new IllegalArgumentException(
          "Configuration instance must not be null");
    }
    // Lock the configuration settings so that they can't be changed
    // from now on.
    // -------------------------------------------------------------
    Genotype.getConfiguration().lockSettings();
    // First see if we can get a Chromosome instance from the pool.
    // If we can, we'll randomize its gene values (alleles) and then
    // return it.
    // ------------------------------------------------------------
    ChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      Chromosome randomChromosome = pool.acquireChromosome();
      if (randomChromosome != null) {
        Gene[] genes = randomChromosome.getGenes();
        RandomGenerator generator = Genotype.getConfiguration().
            getRandomGenerator();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setToRandomValue(generator);
        }
        randomChromosome.m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;
        return randomChromosome;
      }
    }
    // If we got this far, then we weren't able to get a Chromosome from
    // the pool, so we have to construct a new instance and build it from
    // scratch.
    // ------------------------------------------------------------------
    Chromosome sampleChromosome =
        Genotype.getConfiguration().getSampleChromosome();
    Gene[] sampleGenes = sampleChromosome.getGenes();
    Gene[] newGenes = new Gene[sampleGenes.length];
    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    for (int i = 0; i < newGenes.length; i++) {
      // We use the newGene() method on each of the genes in the
      // sample Chromosome to generate our new Gene instances for
      // the Chromosome we're returning. This guarantees that the
      // new Genes are setup with all of the correct internal state
      // for the respective gene position they're going to inhabit.
      // -----------------------------------------------------------
      newGenes[i] = sampleGenes[i].newGene();
      // Set the gene's value (allele) to a random value.
      // ------------------------------------------------
      newGenes[i].setToRandomValue(generator);
    }
    // Finally, construct the new chromosome with the new random
    // genes values and return it.
    // ---------------------------------------------------------
    return new ChromosomeForTest(newGenes);
  }

  public synchronized Object clone() {
    ChromosomeForTest chrom = new ChromosomeForTest(
        ( (Chromosome)super.clone()).getGenes());
    chrom.isCloned = true;
    return chrom;
  }

  public static class TestResultHolder {
    public static int computedTimes;
  }
}
