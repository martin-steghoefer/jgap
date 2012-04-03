/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.distinctGenes;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Sample class: Descendent of Chromosome that creates Chromosomes with n
 * CompositeGenes. All but one CompositeGene's have 4 sub-genes, the last one
 * has only 3 sub-genes.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MyChromosome
    extends Chromosome {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public MyChromosome()
      throws InvalidConfigurationException {
    super();
  }

  public MyChromosome(final Configuration a_configuration)
      throws InvalidConfigurationException {
    super(a_configuration);
  }

  public MyChromosome(final Configuration a_configuration,
                      final int a_desiredSize)
      throws InvalidConfigurationException {
    super(a_configuration, a_desiredSize);
  }

  public MyChromosome(final Configuration a_configuration,
                      final Gene a_sampleGene, final int a_desiredSize)
      throws InvalidConfigurationException {
    super(a_configuration, a_sampleGene, a_desiredSize);
  }

  public MyChromosome(final Configuration a_configuration,
                      final Gene[] a_initialGenes)
      throws InvalidConfigurationException {
    super(a_configuration, a_initialGenes);
  }

  public MyChromosome(final Configuration a_configuration, Gene a_sampleGene,
                      int a_desiredSize,
                      IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    this(a_configuration, a_sampleGene, a_desiredSize);
  }

  public boolean isHandlerFor(Object a_obj, Class a_class) {
    if (a_class == MyChromosome.class) {
      return true;
    }
    else {
      return false;
    }
  }

  /**{@inheritDoc}*/
  public Object perform(Object a_obj, Class a_class, Object a_params)
      throws Exception {
    return randomInitialMyChromosome(getConfiguration());
  }

  public static IChromosome randomInitialMyChromosome(Configuration
      a_configuration)
      throws InvalidConfigurationException {
    // Sanity check: make sure the given configuration isn't null.
    // -----------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "Configuration instance must not be null");
    }
    // Lock the configuration settings so that they can't be changed
    // from now on.
    // -------------------------------------------------------------
    a_configuration.lockSettings();
    // First see if we can get a Chromosome instance from the pool.
    // If we can, we'll randomize its gene values (alleles) and then
    // return it.
    // -------------------------------------------------------------
    IChromosomePool pool = a_configuration.getChromosomePool();
    if (pool != null) {
      IChromosome randomChromosome = pool.acquireChromosome();
      if (randomChromosome != null) {
        Gene[] genes = randomChromosome.getGenes();
        RandomGenerator generator = a_configuration.getRandomGenerator();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setToRandomValue(generator);
        }
        randomChromosome.setFitnessValueDirectly(FitnessFunction.
            NO_FITNESS_VALUE);
        return randomChromosome;
      }
    }
    // We weren't able to get a Chromosome from the pool, so we have to
    // construct a new instance and build it from scratch.
    // ------------------------------------------------------------------
    IChromosome sampleChromosome =
        a_configuration.getSampleChromosome();
    sampleChromosome.setFitnessValue(FitnessFunction.NO_FITNESS_VALUE);
    Gene[] sampleGenes = sampleChromosome.getGenes();
    Gene[] newGenes = new Gene[sampleGenes.length];
    RandomGenerator generator = a_configuration.getRandomGenerator();
    // All genes except the last one should contain 4 fields.
    // ------------------------------------------------------
    for (int i = 0; i < newGenes.length - 1; i++) {
      CompositeGene newGene = new CompositeGene(a_configuration);
      for (int j = 0; j < 4; j++) {
        Gene field = new BooleanGene(a_configuration);
        newGene.addGene(field);
      }
      newGenes[i] = newGene; //sampleGenes[i].newGene();
      // Set the gene's value (allele) to a random value.
      // ------------------------------------------------
      newGenes[i].setToRandomValue(generator);
    }
    // The last gene should contain 3 fields only.
    // -------------------------------------------
    CompositeGene newGene = new CompositeGene(a_configuration);
    for (int j = 0; j < 3; j++) {
      Gene field = new BooleanGene(a_configuration);
      newGene.addGene(field);
      newGene.setToRandomValue(generator);
    }
    newGenes[newGenes.length - 1] = newGene;
    // Finally, construct the new chromosome with the new random
    // genes values and return it.
    // ---------------------------------------------------------
    return new MyChromosome(a_configuration, newGenes);
  }
}
