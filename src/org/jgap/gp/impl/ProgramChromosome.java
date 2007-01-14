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
import org.jgap.*;
import org.jgap.util.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;
import org.jgap.gp.*;

/**
 * Chromosome representing a single GP Program.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosome
    extends BaseGPChromosome
    implements IGPChromosome, Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  /**
   * The list of allowed functions/terminals.
   */
  private transient CommandGene[] m_functionSet;

  /**
   * Array to hold the depths of each node.
   */
  private int[] m_depth;

  /**
   * Array to hold the types of the arguments to this Chromosome.
   */
  private Class[] argTypes;

  private transient int m_index;

  private transient int m_maxDepth;

  /**
   * The array of genes contained in this chromosome.
   */
  private CommandGene[] m_genes;

  /**
   * Application-specific data that is attached to this Chromosome.
   * This data may assist the application in evaluating this Chromosome
   * in the fitness function. JGAP does not operate on the data, aside
   * from allowing it to be set and retrieved, and considering it with
   * comparations (if user opted in to do so).
   */
  private Object m_applicationData;

  /**
   * Method compareTo(): Should we also consider the application data when
   * comparing? Default is "false" as "true" means a Chromosome's losing its
   * identity when application data is set differently!
   *
   * @since 3.0
   */
  private boolean m_compareAppData;

  public ProgramChromosome(GPConfiguration a_configuration, int a_size,
                           IGPProgram a_ind)
      throws InvalidConfigurationException {
    super(a_configuration, a_ind);
    if (a_size <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_configuration, int a_size,
                           CommandGene[] a_functionSet,
                           Class[] a_argTypes,
                           IGPProgram a_ind)
      throws InvalidConfigurationException {
    super(a_configuration, a_ind);
    if (a_size <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    m_functionSet = a_functionSet;
    argTypes = a_argTypes;
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_configuration,
                           CommandGene[] a_initialGenes)
      throws InvalidConfigurationException {
    super(a_configuration);
    int i = 0;
    while (i < a_initialGenes.length && a_initialGenes[i] != null) {
      i++;
    }
    CommandGene[] genes = new CommandGene[i];
    for (int k = 0; k < i; k++) {
      genes[k] = a_initialGenes[k];
    }
    init(a_initialGenes.length);
  }

  public ProgramChromosome(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    init();
  }

  /**
   * Default constructor. Only use with dynamic instantiation.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ProgramChromosome()
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration());
  }

  private void init()
      throws InvalidConfigurationException {
    init(getGPConfiguration().getPopulationSize());
  }

  private void init(final int a_size)
      throws InvalidConfigurationException {
    m_depth = new int[a_size];
    m_genes = new CommandGene[a_size];/**@todo speedup possible by using dynamic list?*/
  }

  public void setArgTypes(Class[] a_argTypes) {
    argTypes = a_argTypes;
  }

  public synchronized Object clone() {
    try {
      ProgramChromosome chrom = new ProgramChromosome( (GPConfiguration)
          getGPConfiguration(), (CommandGene[]) m_genes.clone());
      chrom.argTypes = (Class[]) argTypes.clone();
      chrom.setFunctionSet( (CommandGene[]) getFunctionSet().clone());
      chrom.setFunctions( (CommandGene[]) getFunctions().clone());
      chrom.m_depth = (int[]) m_depth.clone();
      return chrom;
    }
    catch (Exception cex) {
      // rethrow to have a more convenient handling
      throw new IllegalStateException(cex.getMessage());
    }
  }

  /**
   * Clean up the chromosome.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void cleanup() {
    int len = m_genes.length;
    for (int i = 0; i < len; i++) {
      if (m_genes[i] == null) {
        break;
      }
      m_genes[i].cleanup();
    }
  }

  /**
   * Initialize this chromosome using the grow or the full method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param depth the maximum depth of the chromosome to create
   * @param type the type of the chromosome to create
   * @param a_argTypes the array of types of arguments for this chromosome
   * @param a_functionSet the set of nodes valid to pick from
   * @param a_grow true: use grow method; false: use full method
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void growOrFull(final int a_num, final int depth, final Class type,
                   final Class[] a_argTypes, final CommandGene[] a_functionSet,
                   boolean a_grow) {
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, getFunctionSet(), 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        m_functionSet[a_functionSet.length + i]
            = new Argument(getGPConfiguration(), i, a_argTypes[i]);
      }
      /**@todo init is experimental, make dynamic*/
      // Initialization of genotype according to specific problem requirements.
      // ----------------------------------------------------------------------
      CommandGene n = null;
      if (a_num == 0 && false) {
        for (int i = 0; i < m_functionSet.length; i++) {
          CommandGene m = m_functionSet[i];
          if (m.getClass() == SubProgram.class) {
            n = m;
            break;
          }
        }
      }
      else if (a_num == 1 && false) {
        for (int i = 0; i < m_functionSet.length; i++) {
          CommandGene m = m_functionSet[i];
          if (m.getClass() == ForLoop.class) {
            n = m;
            break;
          }
        }
      }
      int tries = 0;
      int localDepth = depth;
      do {
        m_index = 0;
        m_maxDepth = localDepth;
        try {
          growOrFullNode(a_num, localDepth, type, m_functionSet, n, 0, a_grow);
          redepth();
          break;
        } catch (IllegalStateException iex) {
          tries++;
          if (tries >= getGPConfiguration().getProgramCreationMaxtries()) {
            throw new IllegalArgumentException(iex.getMessage());
          }
          // Clean up genes for next try.
          // ----------------------------
          for (int j = 0; j < size(); j++) {
            if (m_genes[j] == null) {
              break;
            }
            m_genes[j] = null;
          }
          localDepth++;
        }
      } while (true);
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Output program in left-hand notion (e.g.: "+ X Y" for "X + Y")
   * @param a_startNode node to start with
   * @return output in left-hand notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toString(final int a_startNode) {
    if (a_startNode < 0) {
      return "";
    }
    // Replace any occurance of placeholders (e.g. &1, &2...) in the function's
    // name.
    // ------------------------------------------------------------------------
    String funcName = m_genes[a_startNode].getName();
    int j = 1;
    do {
      String placeHolder = "&" + j;
      int foundIndex = funcName.indexOf(placeHolder);
      if (foundIndex < 0) {
        break;
      }
      funcName = funcName.replaceFirst(placeHolder, "");
      j++;
    }
    while (true);
    // Now remove any leading and trailing spaces.
    // -------------------------------------------
    if (j > 0) {
      funcName = funcName.trim();
    }
    IGPProgram ind = getIndividual();
    if (getFunctions()[a_startNode].getArity(ind) == 0) {
      return funcName + " ";
    }
    String str = "";
    str += funcName + " ( ";
    int arity = m_genes[a_startNode].getArity(ind);
    for (int i = 0; i < arity; i++) {
      str += toString(getChild(a_startNode, i));
    }
    if (a_startNode == 0) {
      str += ")";
    }
    else {
      str += ") ";
    }
    return str;
  }

  /**
   * Output program in "natural" notion (e.g.: "X + Y" for "X + Y")
   * @param a_startNode the node to start with
   * @return output in normalized notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toStringNorm(final int a_startNode) {
    if (a_startNode < 0) {
      return "";
    }
    IGPProgram ind = getIndividual();
    if (m_genes[a_startNode].getArity(ind) == 0) {
      return getFunctions()[a_startNode].getName();
    }
    String str = "";
    boolean paramOutput = false;
    if (m_genes[a_startNode].getArity(ind) > 0) {
      if (m_genes[a_startNode].getName().indexOf("&1") >= 0) {
        paramOutput = true;
      }
    }
    if (m_genes[a_startNode].getArity(ind) == 1 || paramOutput) {
      str += getFunctions()[a_startNode].getName();
    }
    if (a_startNode > 0) {
      str = "(" + str;
    }
    for (int i = 0; i < m_genes[a_startNode].getArity(ind); i++) {
      String childString = toStringNorm(getChild(a_startNode, i));
      String placeHolder = "&" + (i + 1);
      int placeholderIndex = str.indexOf(placeHolder);
      if (placeholderIndex >= 0) {
        str = str.replaceFirst(placeHolder, childString);
      }
      else {
        str += childString;
      }
      if (i == 0 && m_genes[a_startNode].getArity(ind) != 1
          && !paramOutput) {
        str += " " + m_genes[a_startNode].getName() + " ";
      }
    }
    if (a_startNode > 0) {
      str += ")";
    }
    return str;
  }

  /**
   * Determines whether there exists a function or terminal in the given node
   * set with the given type.
   *
   * @param a_type the type to look for
   * @param a_nodeSet the array of nodes to look through
   * @param a_function true to look for a function, false to look for a terminal
   * @param a_growing true: grow mode, false: full mode
   *
   * @return true if such a node exists, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isPossible(Class a_type, CommandGene[] a_nodeSet,
                            boolean a_function, boolean a_growing) {
    IGPProgram ind = getIndividual();
    for (int i = 0; i < a_nodeSet.length; i++) {
      if (a_nodeSet[i].getReturnType() == a_type) {
        if (a_nodeSet[i].getArity(ind) == 0 && (!a_function || a_growing)) {
          return true;
        }
        if (a_nodeSet[i].getArity(ind) != 0 && a_function) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Randomly chooses a valid node from the functions set.
   *
   * @param a_type the type of node to choose
   * @param a_functionSet the functions to use
   * @param a_function true to choose a function, false to choose a terminal
   * @param a_growing true to ignore the function parameter, false otherwise
   * @return the node chosen
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected CommandGene selectNode(Class a_type, CommandGene[] a_functionSet,
                                   boolean a_function, boolean a_growing) {
    if (!isPossible(a_type, a_functionSet, a_function, a_growing)) {
      final String errormsg = "Chromosome requires a " +
          (a_function ?
           ("function" + (a_growing ? " or terminal" : ""))
           : "terminal") + " of type " +
          a_type + " but there is no such node available";
      if (!getGPConfiguration().isStrictProgramCreation()) {
        throw new IllegalStateException(errormsg);
      }
      else {
        throw new IllegalArgumentException(errormsg);
      }
    }
    CommandGene n = null;
    int lindex;
    // Following is analog to isPossible except with the random generator.
    // -------------------------------------------------------------------
    IGPProgram ind = getIndividual();
    RandomGenerator randGen = getGPConfiguration().getRandomGenerator();
    while (n == null) {
      /**@todo speedup, relying on getting a fitting random number some time
       * is not satisfying
       */
      lindex = randGen.nextInt(a_functionSet.length);
      if (a_functionSet[lindex].getReturnType() == a_type) {
        if (a_functionSet[lindex].getArity(ind) == 0 &&
            (!a_function || a_growing)) {
          n = a_functionSet[lindex];
        }
        if (a_functionSet[lindex].getArity(ind) != 0 && a_function) {
          n = a_functionSet[lindex];
        }
      }
    }
    return n;
  }

  /**
   * Create a tree of nodes using the grow method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_depth the maximum depth of the tree to create
   * @param a_type the type of node to start with
   * @param a_functionSet the set of function valid to pick from
   * @param a_rootNode null, or root node to use
   * @param a_recurseLevel 0 for first call
   * @param a_grow true: use grow method; false: use full method
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void growOrFullNode(int a_num, int a_depth, Class a_type,
                          CommandGene[] a_functionSet, CommandGene a_rootNode,
                          int a_recurseLevel, boolean a_grow) {
    if (a_rootNode == null) {
      int a_tries = 0;
      do {
        a_rootNode = selectNode(a_type, a_functionSet, a_depth > 1, a_grow);
        if (!getGPConfiguration().validateNode(this, a_rootNode, a_tries++,
            a_num, a_recurseLevel, a_type, a_functionSet, a_depth, a_grow)) {
          continue;
        }
        break;
      }
      while (true);
    }
    // Generate the node.
    // ------------------
    m_depth[m_index] = m_maxDepth - a_depth;
    if (a_rootNode instanceof ICloneable) {
        m_genes[m_index++] = (CommandGene) ( (ICloneable) a_rootNode).clone();
    }
    else {
      m_genes[m_index++] = a_rootNode;
    }
    if (a_depth > 1) {
      IGPProgram ind = getIndividual();
      for (int i = 0; i < a_rootNode.getArity(ind); i++) {
        if (m_index < m_depth.length) { //xx
          growOrFullNode(a_num, a_depth - 1,
                         a_rootNode.getChildType(getIndividual(), i),
                         a_functionSet, null, a_recurseLevel + 1, a_grow);
        }
      }
    }
  }

  /**
   * Recalculate the depth of each node.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void redepth() {
    m_depth[0] = 0;
    redepth(0);
  }

  /**
   * Calculate the depth of the next node and the indices of the children
   * of the current node.
   * The depth of the next node is just one plus the depth of the current node.
   * The index of the first child is always the next node. The index of the
   * second child is found by recursively calling this method on the tree
   * starting with the first child.
   *
   * @param a_index the index of the reference depth
   * @return the index of the next node of the same depth as the
   * current node (i.e. the next sibling node)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected int redepth(int a_index) {
    int num = a_index + 1;
    CommandGene command = getNode(a_index);
    if (command == null) {
      throw new IllegalStateException("ProgramChromosome invalid");
    }
    IGPProgram ind = getIndividual();
    int arity = command.getArity(ind);
    for (int i = 0; i < arity; i++) {
      if (num < m_depth.length) {//xx
        m_depth[num] = m_depth[a_index] + 1;
        // children[i][n] = num;
        num = redepth(num);
        if (num < 0) {
          break;
        }
      }
      else {
        return -1;//xx
      }
    }
    return num;
  }

  /**
   * Gets the a_child'th child of the a_index'th node in this chromosome. This
   * is the same as the a_child'th node whose depth is one more than the depth
   * of the a_index'th node.
   *
   * @param a_index the node number of the parent
   * @param a_child the child number (starting from 0) of the parent
   * @return the node number of the child, or -1 if not found
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getChild(int a_index, int a_child) {
    /**@todo speedup*/
    int len = getFunctions().length;
    for (int i = a_index + 1; i < len; i++) {
      if (m_depth[i] <= m_depth[a_index]) {
        return -1;
      }
      if (m_depth[i] == m_depth[a_index] + 1) {
        if (--a_child < 0) {
          return i;
        }
      }
    }
    throw new RuntimeException("Bad child " + a_child +
                               " of node with index = "
                               + a_index);
  }

  public CommandGene[] getFunctionSet() {
    return m_functionSet;
  }

  public void setFunctionSet(CommandGene[] a_functionSet) {
    m_functionSet = a_functionSet;
  }

  public CommandGene[] getFunctions() {
    return m_genes;
  }

  public void setFunctions(CommandGene[] a_functions)
      throws InvalidConfigurationException {
    m_genes = a_functions;
  }

  /**
   * Gets the number of nodes in the branch starting at the a_index'th node.
   *
   * @param a_index the index of the node at which to start counting
   * @return the number of nodes in the branch starting at the a_index'th node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getSize(int a_index) {
    int i;
    // Get the node at which the depth is <= depth[n].
    for (i = a_index + 1; i < m_genes.length && m_genes[i] != null;
         i++) {
      if (m_depth[i] <= m_depth[a_index]) {
        break;
      }
    }
    return i - a_index;
  }

  /**
   * Gets the depth of the branch starting at the a_index'th node.
   *
   * @param a_index the index of the node at which to check the depth
   * @return the depth of the branch starting at the a_index'th node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getDepth(int a_index) {
    int i, maxdepth = m_depth[a_index];
    for (i = a_index + 1; i < m_genes.length && m_genes[i] != null;
         i++) {
      if (m_depth[i] <= m_depth[a_index]) {
        break;
      }
      if (m_depth[i] > maxdepth) {
        maxdepth = m_depth[i];
      }
    }
    return maxdepth - m_depth[a_index];
  }

  /**
   * Gets the node which is the parent of the given node in this chromosome. If
   * the child is at depth d then the parent is the first function at depth d-1
   * when iterating backwards through the function list starting from the child.
   *
   * @param a_child the child node
   * @return the parent node, or null if the child is the root node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getParentNode(int a_child) {
    if (a_child >= m_genes.length || m_genes[a_child] == null) {
      return -1;
    }
    for (int i = a_child - 1; i >= 0; i--) {
      if (m_depth[i] == m_depth[a_child] - 1) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Executes this node as a boolean.
   *
   * @param args the arguments for execution
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not
   * boolean
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean execute_boolean(Object[] args) {
    boolean rtn = m_genes[0].execute_boolean(this, 0, args);
    cleanup();
    return rtn;
  }

  /**
   * Executes this node as a boolean.
   *
   * @param n the index of the parent node
   * @param child the child number of the node to execute
   * @param args the arguments for execution
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not
   * boolean
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean execute_boolean(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_boolean(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_boolean(this, other, args);
  }

  /**
   * Executes this node, returning nothing.
   *
   * @param args the arguments for execution
   * @throws UnsupportedOperationException if the type of this node is not void
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void execute_void(Object[] args) {
    m_genes[0].execute_void(this, 0, args);
    cleanup();
  }

  public void execute_void(int n, int child, Object[] args) {
    if (child == 0) {
      m_genes[n + 1].execute_void(this, n + 1, args);
    }
    else {
      int other = getChild(n, child);
      m_genes[other].execute_void(this, other, args);
    }
  }

  /**
   * Executes this node as an integer.
   *
   * @param args the arguments for execution
   * @return the integer return value of this node
   * @throws UnsupportedOperationException if the type of this node is not
   * integer
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int execute_int(Object[] args) {
    int rtn = m_genes[0].execute_int(this, 0, args);
    cleanup();
    return rtn;
  }

  public int execute_int(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_int(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_int(this, other, args);
  }

  /**
   * Executes this node as a long.
   *
   * @param args the arguments for execution
   * @return the long return value of this node
   * @throws UnsupportedOperationException if the type of this node is not long
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public long execute_long(Object[] args) {
    long rtn = m_genes[0].execute_long(this, 0, args);
    cleanup();
    return rtn;
  }

  public long execute_long(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_long(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_long(this, other, args);
  }

  /**
   * Executes this node as a float.
   *
   * @param args the arguments for execution
   * @return the float return value of this node
   * @throws UnsupportedOperationException if the type of this node is not float
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public float execute_float(Object[] args) {
    float rtn = m_genes[0].execute_float(this, 0, args);
    cleanup();
    return rtn;
  }

  public float execute_float(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_float(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_float(this, other, args);
  }

  /**
   * Executes this node as a double.
   *
   * @param args the arguments for execution
   * @return the double return value of this node
   * @throws UnsupportedOperationException if this node's type is not double
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double execute_double(Object[] args) {
    double rtn = m_genes[0].execute_double(this, 0, args);
    cleanup();
    return rtn;
  }

  public double execute_double(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_double(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_double(this, other, args);
  }

  /**
   * Executes this node as an object.
   *
   * @param args the arguments for execution
   * @return the object return value of this node
   * @throws UnsupportedOperationException if the type of this node is not
   * of type Object
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object execute_object(Object[] args) {
    Object rtn = m_genes[0].execute_object(this, 0, args);
    cleanup();
    return rtn;
  }

  public Object execute_object(int n, int child, Object[] args) {
    if (child == 0) {
      return m_genes[n + 1].execute_object(this, n + 1, args);
    }
    int other = getChild(n, child);
    return m_genes[other].execute_object(this, other, args);
  }

  /**
   * Executes this node without knowing its return type.
   *
   * @param args the arguments for execution
   * @return the Object which wraps the return value of this node, or null
   * if the return type is null or unknown
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object execute(Object[] args) {
    return m_genes[0].execute_object(this, 0, args);
  }

  public Object execute(int n, int child, Object[] args) {
    return execute_object(n, child, args);
  }

  public void setGene(int index, CommandGene a_gene) {
    if (a_gene == null) {
      throw new IllegalArgumentException("Gene may not be null!");
    }
    m_genes[index] = a_gene;
  }

  public Class[] getArgTypes() {
    return argTypes;
  }

  public int getArity() {
    return argTypes.length;
  }

  /**
   * @return number of functions and terminals present
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int size() {
    int i = 0;
    while (i < m_genes.length && m_genes[i] != null) {
      i++;
    }
    return i;
  }

  /**
   * Compares the given chromosome to this chromosome. This chromosome is
   * considered to be "less than" the given chromosome if it has a fewer
   * number of genes or if any of its gene values (alleles) are less than
   * their corresponding gene values in the other chromosome.
   *
   * @param a_other the chromosome against which to compare this chromosome
   * @return a negative number if this chromosome is "less than" the given
   * chromosome, zero if they are equal to each other, and a positive number if
   * this chromosome is "greater than" the given chromosome
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
    ProgramChromosome otherChromosome = (ProgramChromosome) a_other;
    CommandGene[] otherGenes = otherChromosome.m_genes;
    // If the other Chromosome doesn't have the same number of genes,
    // then whichever has more is the "greater" Chromosome.
    // --------------------------------------------------------------
    if (otherChromosome.size() != size) {
      return size() - otherChromosome.size();
    }
    // Next, compare the gene values (alleles) for differences. If
    // one of the genes is not equal, then we return the result of its
    // comparison.
    // ---------------------------------------------------------------
    for (int i = 0; i < size; i++) {
      int comparison = m_genes[i].compareTo(otherGenes[i]);
      if (comparison != 0) {
        return comparison;
      }
    }
    /**@todo compare m_functionSet*/
    if (isCompareApplicationData()) {
      // Compare application data.
      // -------------------------
      if (getApplicationData() == null) {
        if (otherChromosome.getApplicationData() != null) {
          return -1;
        }
      }
      else if (otherChromosome.getApplicationData() == null) {
        return 1;
      }
      else {
        if (getApplicationData() instanceof Comparable) {
          try {
            return ( (Comparable) getApplicationData()).compareTo(
                otherChromosome.getApplicationData());
          }
          catch (ClassCastException cex) {
            return -1;
          }
        }
        else {
          return getApplicationData().getClass().getName().compareTo(
              otherChromosome.getApplicationData().getClass().getName());
        }
      }
    }
    // Everything is equal. Return zero.
    // ---------------------------------
    return 0;
  }

  /**
   * Compares this chromosome against the specified object.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    }
    catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Chromosome is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true: consider application data in method compareTo
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setCompareApplicationData(boolean a_doCompare) {
    m_compareAppData = a_doCompare;
  }

  /*
   * @return should we also consider the application data when comparing?
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isCompareApplicationData() {
    return m_compareAppData;
  }

  /**
   * Retrieves the application-specific data that is attached to this
   * Chromosome. Attaching application-specific data may be useful for
   * some applications when it comes time to evaluate this Chromosome
   * in the fitness function. JGAP ignores this data functionally.
   *
   * @return the application-specific data previously attached to this
   * Chromosome, or null if there is no data attached
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * @param a_locus index of the gene value to be returned
   * @return Gene at the given index
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public synchronized CommandGene getGene(int a_locus) {
    return m_genes[a_locus];
  }

}
