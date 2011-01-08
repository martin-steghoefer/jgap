/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.util.*;
import org.jgap.gp.terminal.Argument;

/**
 * A GP program contains 1..n ProgramChromosome's.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgram
    extends GPProgramBase implements Serializable, Comparable, ICloneable,
    IBusinessKey {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.24 $";

  final static String PROGRAMCHROM_DELIMITER_HEADING = "<";

  final static String PROGRAMCHROM_DELIMITER_CLOSING = ">";

  final static String PROGRAMCHROM_DELIMITER = "#";

  /**
   * Holds the chromosomes contained in this program.
   */
  private ProgramChromosome[] m_chromosomes;

  /**
   * Default constructor, only for dynamic instantiation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public GPProgram()
      throws Exception {
  }

  /**
   * Master constructor.
   *
   * @param a_conf the configuration to use
   * @param a_types the type of each chromosome, the length is the number of
   * chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes
   * @param a_minDepths contains the minimum depth allowed for each chromosome
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgram(GPConfiguration a_conf, Class[] a_types,
                   Class[][] a_argTypes, CommandGene[][] a_nodeSets,
                   int[] a_minDepths, int[] a_maxDepths, int a_maxNodes)
      throws InvalidConfigurationException {
    super(a_conf);
    m_chromosomes = new ProgramChromosome[a_types.length];
    setTypes(a_types);
    setArgTypes(a_argTypes);
    setNodeSets(a_nodeSets);
    setMaxDepths(a_maxDepths);
    setMinDepths(a_minDepths);
    setMaxNodes(a_maxNodes);
  }

  /**
   * Constructor to initialize a GPProgram with values of another GPProgram.
   *
   * @param a_prog the GPProgram to read the initialization values from
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgram(IGPProgram a_prog)
      throws InvalidConfigurationException {
    super(a_prog);
    m_chromosomes = new ProgramChromosome[getTypes().length];
  }

  /**
   * Sort of minimalistic constructor. Use only if you are aware of what you do.
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
    IGPProgram ind = m_chromosomes[a_index].getIndividual();
    if (this != ind) {
      m_chromosomes[a_index].setIndividual(this);
    }
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

  /**
   * Initialize the chromosomes within this GP program using the grow or the
   * full method.
   *
   * @param a_depth the maximum depth of the chromosome to create
   * @param a_grow true: use grow method; false: use full method
   * @param a_maxNodes maximum number of nodes allowed
   * @param a_fullModeAllowed for each chromosome: true means full mode allowed,
   * otherwise use grow mode
   * @param a_tries maximum number of tries to create a valid program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void growOrFull(int a_depth, boolean a_grow, int a_maxNodes,
                         boolean[] a_fullModeAllowed, int a_tries) {
    GPConfiguration conf = getGPConfiguration();
    // The number of chromosomes to create.
    // ------------------------------------
    int size = m_chromosomes.length;
    for (int i = 0; i < size; i++) {
      try {
        // Construct a chromosome with place for a_maxNodes nodes.
        // -------------------------------------------------------
        m_chromosomes[i] = new ProgramChromosome(conf, a_maxNodes, this);
      } catch (InvalidConfigurationException iex) {
        throw new RuntimeException(iex);
      }
      m_chromosomes[i].setArgTypes(getArgTypes()[i]);
      // If there are ADF's in the nodeSet, then set their type according to
      // the chromosome it references.
      // -------------------------------------------------------------------
      int len = getNodeSets()[i].length;
      for (int j = 0; j < len; j++) {
        if (getNodeSets()[i][j] instanceof ADF) {
          ( (ADF) getNodeSets()[i][j]).setReturnType(
              getTypes()[ ( (ADF) getNodeSets()[i][j]).getChromosomeNum()]);
        }
      }
    }
    int depth;
    for (int i = 0; i < size; i++) {
      // Restrict depth to input params.
      // -------------------------------
      if (getMaxDepths() != null && a_depth > getMaxDepths()[i]) {
        depth = getMaxDepths()[i];
      }
      else {
        if (getMinDepths() != null && a_depth < getMinDepths()[i]) {
          depth = getMinDepths()[i];
        }
        else {
          depth = a_depth;
        }
      }
      // Decide whether to use grow mode or full mode.
      // Here, the program is finally created.
      // ---------------------------------------------
      if (a_grow || a_fullModeAllowed == null || !a_fullModeAllowed[i]) {
        m_chromosomes[i].growOrFull(i, depth, getType(i), getArgType(i),
                                    getNodeSet(i), true, a_tries);
      }
      else {
        m_chromosomes[i].growOrFull(i, depth, getType(i), getArgType(i),
                                    getNodeSet(i), false, a_tries);
      }
    }
    if (getGPConfiguration().isUseProgramCache()) {
      // Cache fitness value by checking if a program with same
      // representation was computed before.
      // ------------------------------------------------------
      GPProgramInfo pcInfo = getGPConfiguration().readProgramCache(this);
      if (pcInfo == null) {
        pcInfo = putToCache(this);
      }
      else {
        setFitnessValue(pcInfo.getFitnessValue());
      }
    }
  }

  /**
   * Put program to cache.
   *
   * @param a_program the program to put into the cache
   * @return GPProgramInfo info about the program
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  protected GPProgramInfo putToCache(GPProgram a_program) {
    return getGPConfiguration().putToProgramCache(a_program);
  }

  /**
   * Initialize this program by using given chromosomes.
   *
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void growOrFull(Class[][] a_argTypes, CommandGene[][] a_nodeSets) {
    int size = m_chromosomes.length;
    for (int i = 0; i < size; i++) {
      m_chromosomes[i].setArgTypes(a_argTypes[i]);
      // If there are ADF's in the nodeSet, then set their type according to
      // the chromosome it references.
      // -------------------------------------------------------------------
      int len = getNodeSets()[i].length;
      for (int j = 0; j < len; j++) {
        if (getNodeSets()[i][j] instanceof ADF) {
          ( (ADF) getNodeSets()[i][j]).setReturnType(
              getTypes()[ ( (ADF) getNodeSets()[i][j]).getChromosomeNum()]);
        }
      }
    }
    for (int i = 0; i < size; i++) {
      ProgramChromosome chrom = m_chromosomes[i];
      chrom.setFunctionSet(a_nodeSets[i]);
      CommandGene[] functionSet = chrom.getFunctionSet();
      CommandGene[] newFktSet = new CommandGene[functionSet.length +
          a_argTypes[i].length];
      System.arraycopy(functionSet, 0, newFktSet, 0,
                       functionSet.length);
      for (int ii = 0; ii < a_argTypes[i].length; ii++) {
        try {
          functionSet[a_nodeSets[i].length + ii]
              = new Argument(getGPConfiguration(), ii, a_argTypes[i][ii]);
        } catch (InvalidConfigurationException iex) {
          throw new RuntimeException(iex);
        }
      }
      chrom.redepth();
    }
    if (getGPConfiguration().isUseProgramCache()) {
      // Cache fitness value by checking if a program with same
      // representation was computed before.
      GPProgramInfo pcInfo = getGPConfiguration().readProgramCache(this);
      if (pcInfo == null) {
        pcInfo = putToCache(this);
      }
      else {
        setFitnessValue(pcInfo.getFitnessValue());
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
   * Builds a string that represents the output of the GPProgram in
   * left-hand-notion.
   *
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
   * Builds a string that represents the normalized output of the GPProgram.
   *
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
      m_chromosomes[i].setIndividual(this);
      sb.append(m_chromosomes[i].toStringNorm(a_startNode));
    }
    return sb.toString();
  }

  /**
   * Builds a string that represents the debug output of the GPProgram.
   *
   * @return class names of all program chromosomes
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toStringDebug() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < m_chromosomes.length; i++) {
      if (i > 0) {
        sb.append(" ==> ");
      }
      m_chromosomes[i].setIndividual(this);
      sb.append(m_chromosomes[i].toStringDebug());
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
   * Executes the given chromosome as a boolean function.
   *
   * @param a_chromosomeNum the index of the chromosome to execute
   * @param a_args the arguments to use
   * @return the boolean return value
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean execute_boolean(int a_chromosomeNum, Object[] a_args) {
    m_chromosomes[a_chromosomeNum].setIndividual(this);
    return m_chromosomes[a_chromosomeNum].execute_boolean(a_args);
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
   * @param a_chromosomeNum the index of the chromosome to start the search with
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

  /**
   * @return deep clone of the object instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
      // Min and max depths can be null.
      // -------------------------------
      int[] minDepthsClone;
      if (getMinDepths() != null) {
        minDepthsClone = (int[]) getMinDepths().clone();
      }
      else {
        minDepthsClone = null;
      }
      int[] maxDepthsClone;
      if (getMaxDepths() != null) {
        maxDepthsClone = (int[]) getMaxDepths().clone();
      }
      else {
        maxDepthsClone = null;
      }
      GPProgram result = new GPProgram(getGPConfiguration(),
                                       (Class[]) getTypes().clone(),
                                       (Class[][]) getArgTypes().clone(),
                                       (CommandGene[][]) getNodeSets().clone(),
                                       minDepthsClone,
                                       maxDepthsClone,
                                       getMaxNodes());
      result.setFitnessValue(getFitnessValueDirectly());
      // Try to clone application data.
      // ------------------------------
      Object appData = getApplicationData();
      if (appData != null) {
        ICloneHandler cloner = getGPConfiguration().getJGAPFactory().
            getCloneHandlerFor(appData, null);
        if (cloner != null) {
          result.setApplicationData(cloner.perform(appData, null, null));
        }
        else {
          result.setApplicationData(appData);
        }
      }
      for (int i = 0; i < m_chromosomes.length; i++) {
        if (m_chromosomes[i] == null) {
          break;
        }
        result.m_chromosomes[i] = (ProgramChromosome) m_chromosomes[i].clone();
      }
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

  /**
   * @return the persistent representation of the GP program, including all
   * chromosomes
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public String getPersistentRepresentation() {
    StringBuffer b = new StringBuffer();
    for (ProgramChromosome chrom : m_chromosomes) {
      b.append(PROGRAMCHROM_DELIMITER_HEADING);
      b.append(encode(
          chrom.getClass().getName() +
          PROGRAMCHROM_DELIMITER +
          chrom.getPersistentRepresentation()));
      b.append(PROGRAMCHROM_DELIMITER_CLOSING);
    }
    return b.toString();
  }

  protected String encode(String a_string) {
    return StringKit.encode(a_string);
  }

  protected String decode(String a_string) {
    return StringKit.decode(a_string);
  }

  /**
   * @return hopefully unique key representing the state of the GPProgram
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public String getBusinessKey() {
    return toStringNorm(0);
  }
}
