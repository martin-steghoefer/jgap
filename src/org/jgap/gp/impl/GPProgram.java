/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.gp.function.*;
import org.jgap.gp.*;

/**
 * A GP program contains 1..n ProgramChromosome's.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgram
    extends GPProgramBase
    implements Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Holds the chromosomes contained in this program.
   */
  private ProgramChromosome[] m_chromosomes;

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   * @param a_numChromosomes the number of chromosomes to use with this program.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgram(GPConfiguration a_conf, int a_numChromosomes)
      throws InvalidConfigurationException {
    super(a_conf);
    m_chromosomes = new ProgramChromosome[a_numChromosomes];
  }

  /**
   * @param a_index the chromosome to get
   * @return the ProgramChromosome with the given index
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ProgramChromosome getChromosome(int a_index) {
    return m_chromosomes[a_index];
  }

  /**
   * Sets the given chromosome at the given index.
   *
   * @param a_index sic
   * @param a_chrom sic
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setChromosome(int a_index, ProgramChromosome a_chrom) {
    m_chromosomes[a_index] = a_chrom;
  }

  public void growOrFull(int a_depth, Class[] a_types, Class[][] a_argTypes,
                         CommandGene[][] a_nodeSets, int[] a_minDepths,
                         int[] a_maxDepths, boolean a_grow, int a_maxNodes,
                         boolean[] a_fullModeAllowed) {
    GPConfiguration conf = getGPConfiguration();
    int size = m_chromosomes.length;
    for (int i = 0; i < size; i++) {
      try {
        // Construct a chromosome with place for a_maxNodes nodes.
        // -------------------------------------------------------
        m_chromosomes[i] = new ProgramChromosome(conf, a_maxNodes, this);
      } catch (InvalidConfigurationException iex) {
        throw new RuntimeException(iex);
      }
      m_chromosomes[i].setArgTypes(a_argTypes[i]);
      // If there are ADF's in the nodeSet, then set their type according to
      // the chromosome it references.
      // -------------------------------------------------------------------
      for (int j = 0; j < a_nodeSets[i].length; j++)
        if (a_nodeSets[i][j] instanceof ADF)
          ( (ADF) a_nodeSets[i][j]).setReturnType(
              a_types[ ( (ADF) a_nodeSets[i][j]).getChromosomeNum()]);
    }
    int depth;
    for (int i = 0; i < size; i++) {
      // Restrict depth to input params.
      // -------------------------------
      if (a_maxDepths != null && a_depth > a_maxDepths[i]) {
        depth = a_maxDepths[i];
      }
      else {
        if (a_minDepths != null && a_depth < a_minDepths[i]) {
          depth = a_minDepths[i];
        }
        else {
          depth = a_depth;
        }
      }
      // Decide whether to use grow mode or full mode.
      // ---------------------------------------------
      if (a_grow || !a_fullModeAllowed[i]) {
        m_chromosomes[i].growOrFull(i, depth, a_types[i], a_argTypes[i],
                                    a_nodeSets[i], true);
      }
      else {
        m_chromosomes[i].growOrFull(i, depth, a_types[i], a_argTypes[i],
                                    a_nodeSets[i], false);
      }
    }
  }

  /**
   * @return the number of chromosomes in the program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int size() {
    return m_chromosomes.length;
  }

  /**
   * Builds a String that represents the output of the GPProgram in
   * left-hand-notion.
   * @param a_startNode the node to start with
   * @return output in left-hand notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toString(int a_startNode) {
    if (a_startNode < 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < m_chromosomes.length; i++) {
      if (i > 0) {
        sb.append(" ==> ");
      }
      sb.append(m_chromosomes[i].toString(a_startNode));
    }
    return sb.toString();
  }

  /**
   * Builds a String that represents the normalized output of the GPProgram.
   * @param a_startNode the node to start with
   * @return output in normalized notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toStringNorm(int a_startNode) {
    if (a_startNode < 0) {
      return "";
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < m_chromosomes.length; i++) {
      if (i > 0) {
        sb.append(" ==> ");
      }
      sb.append(m_chromosomes[i].toStringNorm(a_startNode));
    }
    return sb.toString();
  }

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
  public int execute_int(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    return m_chromosomes[a_chromosomeNum].execute_int(a_args);
  }

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
  public float execute_float(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    return m_chromosomes[a_chromosomeNum].execute_float(a_args);
  }

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
  public double execute_double(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    return m_chromosomes[a_chromosomeNum].execute_double(a_args);
  }

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
  public Object execute_object(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    return m_chromosomes[a_chromosomeNum].execute_object(a_args);
  }

  /**
   * Executes the given chromosome as an object function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void execute_void(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    m_chromosomes[a_chromosomeNum].execute_void(a_args);
  }

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
  public int getCommandOfClass(int a_chromosomeNum, Class a_class) {
    for (int i = a_chromosomeNum; i < m_chromosomes.length; i++) {
      int j = m_chromosomes[i].getCommandOfClass(0, a_class);
      if (j >= 0) {
        return j;
      }
    }
    return -1;
  }

  /**
   * Compares the given program to this program.
   *
   * @param a_other the program against which to compare this program
   * @return a negative number if this program is "less than" the given
   * program, zero if they are equal to each other, and a positive number if
   * this program is "greater than" the given program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    // First, if the other Chromosome is null, then this chromosome is
    // automatically the "greater" Chromosome.
    // ---------------------------------------------------------------
    if (a_other == null) {
      return 1;
    }
    int size = size();
    GPProgram other = (GPProgram) a_other;
    ProgramChromosome[] otherChroms = other.m_chromosomes;
    // If the other Chromosome doesn't have the same number of genes,
    // then whichever has more is the "greater" Chromosome.
    // --------------------------------------------------------------
    if (other.size() != size) {
      return size() - other.size();
    }
    // Next, compare the gene values (alleles) for differences. If
    // one of the genes is not equal, then we return the result of its
    // comparison.
    // ---------------------------------------------------------------
    Arrays.sort(m_chromosomes);
    Arrays.sort(otherChroms);
    for (int i = 0; i < size; i++) {
      int comparison = m_chromosomes[i].compareTo(otherChroms[i]);
      if (comparison != 0) {
        return comparison;
      }
    }
    // Everything is equal. Return zero.
    // ---------------------------------
    return 0;
  }
}
