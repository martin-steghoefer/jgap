/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.*;
import org.jgap.gp.impl.*;

/**
 * Interface for GP programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface IGPProgram
    extends Serializable, Comparable {
  /**
   * Executes the given chromosome as an integer function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   * @return the integer return value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  int execute_int(int a_chromosomeNum, Object[] a_args);

  /**
   * Executes the given chromosome as a float function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   * @return the floar return value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  float execute_float(int a_chromosomeNum, Object[] a_args);

  /**
   * Executes the given chromosome as a double function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   * @return the double return value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  double execute_double(int a_chromosomeNum, Object[] a_args);

  /**
   * Executes the given chromosome as an object function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   * @return the object return value
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  Object execute_object(int a_chromosomeNum, Object[] a_args);

  /**
   * Executes the given chromosome as an object function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  void execute_void(int a_chromosomeNum, Object[] a_args);

  /**
   * @return the number of chromosomes in the program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  int size();

  /**
   * @param a_index the chromosome to get
   * @return the ProgramChromosome with the given index
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  ProgramChromosome getChromosome(int a_index);

  /**
   * @return fitness value of this program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  double getFitnessValue();

  /**
   * Builds a String that represents the normalized output of the GPProgram.
   * @param a_startNode the node to start with
   * @return output in normalized notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  String toStringNorm(int a_startNode);

  /**
   * Sets the given chromosome at the given index.
   *
   * @param a_index sic
   * @param a_chrom sic
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  void setChromosome(int a_index, ProgramChromosome a_chrom);

  /**
   * Searches for a chromosome that has the given class and returns its index.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_class the class to find
   * @return the index of the first chromosome found that is of a_class, or -1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  int getCommandOfClass(int a_n, Class a_class);
}
