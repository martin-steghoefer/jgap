package org.jgap.gp;

/**
 * Abstract class representing a method of selecting individuals for evolutionary operations.
 * Classes extending this class must implement the select method.
 */
public abstract class SelectionMethod implements java.io.Serializable {

  /**
   * Select an individual based on some method.
   *
   * @param random the random number generator to use
   * @param world the World for the run
   * @return the individual chosen from the world's population
   *
   * @since 1.0
   */
  public abstract ProgramChromosome select(GPGenotype world);
}
