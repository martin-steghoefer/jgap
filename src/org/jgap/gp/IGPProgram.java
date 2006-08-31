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
import java.util.*;
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
  abstract int execute_int(int a_chromosomeNum, Object[] a_args);

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
  abstract float execute_float(int a_chromosomeNum, Object[] a_args);

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
  abstract double execute_double(int a_chromosomeNum, Object[] a_args);

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
  abstract Object execute_object(int a_chromosomeNum, Object[] a_args);

  /**
   * Executes the given chromosome as an object function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  abstract void execute_void(int a_chromosomeNum, Object[] a_args);

  /**
   * @return the number of chromosomes in the program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  abstract int size();

  abstract ProgramChromosome getChromosome(int a_index);
}
