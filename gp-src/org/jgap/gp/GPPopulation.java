package org.jgap.gp;

import org.jgap.*;
import java.util.*;

public class GPPopulation
    extends Population {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public transient float[] fitnessRank; /**@todo fill, see jg.Population*/

  private int m_popSize;

  /*
   * @author Klaus Meffert
   * @since 2.2
   */
  public GPPopulation(GPConfiguration a_conf, int a_size)
      throws InvalidConfigurationException {
    super(a_conf, a_size);
    m_popSize = a_size;
    fitnessRank = new float[a_size];
    for (int i = 0; i < a_size; i++) {
      fitnessRank[i] = 0.5f;
    }
  }

  /**
   * Sorts the population into "ascending" order using some criterion for "ascending".
   * A Comparator is given which will compare two individuals, and if one individual
   * compares as lower than another individual, the first individual will appear
   * in the population before the second individual.
   * <p>
   * @param c the Comparator to use
   */
  public void sort(Comparator c) {
    IChromosome[] chroms = super.toChromosomes();
    Arrays.sort(chroms, c);
    float f = 0;
    for (int i = 0; i < chroms.length; i++) {
      fitnessRank[i] = f;
      f += chroms[i].getFitnessValue();
    }
  }

  /**
   * Creates a population using the ramped half-and-half method.
   *
   * @param a_conf the configuration to use
   * @param types the type of each chromosome, the length
   * is the number of chromosomes
   * @param argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes.
   * Note that it is not necessary to include the arguments of a chromosome as
   * terminals in the chromosome's node set. This is done automatically for you
   * @throws InvalidConfigurationException
   */
  public void create(final GPConfiguration a_conf, Class[] types,
                     Class[][] argTypes,
                     CommandGene[][] nodeSets)
      throws InvalidConfigurationException {
    for (int i = 0; i < m_popSize - 1; i++) {
      int depth = 2 + (a_conf.getMaxInitDepth() - 1) * i /
          (m_popSize - 1);
//      ProgramChromosome chrom = new ProgramChromosome(types.length);//KM:auskommentiert 040206
      ProgramChromosome chrom = new ProgramChromosome(getConfiguration());
      if ( (i % 2) == 0) {
        chrom.grow(depth, types, argTypes, nodeSets);
      }
      else {
        chrom.full(depth, types, argTypes, nodeSets);
      }
      // Debug-Ausgaben
//      for (int k=0;k<chrom.getGenes().length;k++) {
//        Gene g = chrom.getGene(k);
//        if (g==null) { break;}
//        if (g.getClass() == examples.gp.function.AddCommand.class) {
//          System.err.println("AddCommand at Chromosome "+i);
//          break;
//        }
//      }
      addChromosome(chrom);
    }
    setChanged(true);
  }
}
