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
import org.jgap.*;
import org.jgap.gp.function.*;

/**
 * A GP program contains 1..n ProgramChromosome's.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgram
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private ProgramChromosome[] m_chromosomes;

  private double m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;

  private GPConfiguration m_conf;

  public GPProgram(GPConfiguration a_conf, int a_numChromosomes) {
    m_conf = a_conf;
    m_chromosomes = new ProgramChromosome[a_numChromosomes];
  }

  public ProgramChromosome getChromosome(int a_index) {
    return m_chromosomes[a_index];
  }

  public void setChromosome(int a_index, ProgramChromosome a_chrom) {
    m_chromosomes[a_index] = a_chrom;
  }

  public void growOrFull(int a_depth, Class[] a_types,
                         Class[][] a_argTypes, CommandGene[][] a_nodeSets,
                         int[] a_minDepths, int[] a_maxDepths,
                         boolean a_grow, boolean[] a_fullModeAllowed) {
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
    for (int i = 0; i < m_chromosomes.length; i++) {
      try {
        // Construct a chromosome with place for "size" nodes
        int size = a_maxDepths[i];
        if (size < 200) {
          size *= a_nodeSets.length;
        }
        if (size < 200) {
          size = 200;
        }
        m_chromosomes[i] = new ProgramChromosome(m_conf, size);
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
    for (int i = 0; i < m_chromosomes.length; i++) {
      // Restrict depth to input params.
      // -------------------------------
      if (a_depth > a_maxDepths[i]) {
        depth = a_maxDepths[i];
      }
      else {
        if (a_depth < a_minDepths[i]) {
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

  public double getFitnessValue() {
    if (m_fitnessValue >= 0.000d) {
      return m_fitnessValue;
    }
    else {
      return calcFitnessValue();
    }
  }

  /**
   * @return fitness value of this chromosome determined via the registered
   * fitness function
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected double calcFitnessValue() {
    if (m_conf != null) {
      GPFitnessFunction normalFitnessFunction = m_conf.getGPFitnessFunction();
      if (normalFitnessFunction != null) {
        // Grab the "normal" fitness function and ask it to calculate our
        // fitness value.
        // --------------------------------------------------------------
//        System.err.println(" calling normalFitnessFunction.getFitnessValue begin");
        m_fitnessValue = normalFitnessFunction.getFitnessValue(this);
//        System.err.println(" calling normalFitnessFunction.getFitnessValue end");
      }
    }
    return m_fitnessValue;
  }

  public void setFitnessValue(double a_fitness) {
    m_fitnessValue = a_fitness;
  }

  public int size() {
    return m_chromosomes.length;
  }

  public String toString2(int a_n) {
    String s = ""; /**@todo user StringBuffer*/
    for (int i = 0; i < m_chromosomes.length; i++) {
      if (i > 0) {
        s += " ==> ";
      }
      s += m_chromosomes[i].toString2(a_n);
    }
    return s;
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
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
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
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
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
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
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
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
    return m_chromosomes[a_chromosomeNum].execute_object(a_args);
  }

  /**
   * Executes the given chromosome as an object function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   *
   * @since 1.0
   */
  public void execute_void(int a_chromosomeNum, Object[] a_args) {
    CommandGene.setIndividual(this); /**@todo uaaaaaaaaaa*/
    m_chromosomes[a_chromosomeNum].execute_void(a_args);
  }

  public int getCommandOfClass(int a_n, Class a_class) {
    for (int i = a_n; i < m_chromosomes.length; i++) {
      int j = m_chromosomes[i].getCommandOfClass(0, a_class);
      if (j >= 0) {
        return j;
      }
    }
    return -1;
  }
}
