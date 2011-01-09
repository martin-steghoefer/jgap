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

import java.lang.reflect.*;
import java.util.*;

import org.apache.log4j.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.terminal.*;
import org.jgap.util.*;

/**
 * Chromosome representing a single GP Program.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosome
    extends BaseGPChromosome implements Comparable, Cloneable, IBusinessKey {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.52 $";

  final static String PERSISTENT_FIELD_DELIMITER = ":";

  final static String GENE_DELIMITER_HEADING = "<";

  final static String GENE_DELIMITER_CLOSING = ">";

  final static String GENE_DELIMITER = "#";

  private transient static Logger LOGGER = Logger.getLogger(ProgramChromosome.class);

  /**
   * The list of allowed functions/terminals.
   */
  private CommandGene[] m_functionSet;

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

  public ProgramChromosome(GPConfiguration a_conf, int a_size)
      throws InvalidConfigurationException {
    super(a_conf);
    if (a_size <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_conf, int a_size,
                           IGPProgram a_ind)
      throws InvalidConfigurationException {
    super(a_conf, a_ind);
    if (a_size <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    if (a_ind == null) {
      throw new IllegalArgumentException("Individual must not be null");
    }
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_conf, int a_size,
                           CommandGene[] a_functionSet,
                           Class[] a_argTypes,
                           IGPProgram a_ind)
      throws InvalidConfigurationException {
    super(a_conf, a_ind);
    if (a_size <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    if (a_ind == null) {
      throw new IllegalArgumentException("Individual must not be null");
    }
    m_functionSet = a_functionSet;
    argTypes = a_argTypes;
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_conf, CommandGene[] a_initialGenes)
      throws InvalidConfigurationException {
    super(a_conf);
    int i = 0;
    while (i < a_initialGenes.length && a_initialGenes[i] != null) {
      i++;
    }
    init(a_initialGenes.length);
    for (int k = 0; k < i; k++) {
      m_genes[k] = a_initialGenes[k];
    }
  }

  public ProgramChromosome(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    init();
  }

  /**
   * Default constructor. Only use for dynamic instantiation.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ProgramChromosome()
      throws InvalidConfigurationException {
    this(GPGenotype.getStaticGPConfiguration());
  }

  private void init()
      throws InvalidConfigurationException {
    init(getGPConfiguration().getPopulationSize());
  }

  private void init(final int a_size)
      throws InvalidConfigurationException {
    m_depth = new int[a_size];
    m_genes = new CommandGene[a_size];
  }

  public void setArgTypes(Class[] a_argTypes) {
    argTypes = a_argTypes;
  }

  public synchronized Object clone() {
    try {
      int size = m_genes.length;
      CommandGene[] genes = new CommandGene[size];
      for (int i = 0; i < size; i++) {
        if (m_genes[i] == null) {
          break;
        }
        // Try deep clone of genes.
        // ------------------------
        if (ICloneable.class.isAssignableFrom(m_genes[i].getClass())) {
          genes[i] = (CommandGene) ( (ICloneable) m_genes[i]).clone();
        }
        else {
          // No deep clone possible.
          // -----------------------
          genes[i] = m_genes[i];
        }
      }
      ProgramChromosome chrom = new ProgramChromosome( (GPConfiguration)
          getGPConfiguration(), (CommandGene[]) genes);
      chrom.argTypes = (Class[]) argTypes.clone();
      if (getFunctionSet() != null) {
        chrom.setFunctionSet( (CommandGene[]) getFunctionSet().clone());
      }
      if (m_depth != null) {
        chrom.m_depth = (int[]) m_depth.clone();
      }
      chrom.setIndividual(getIndividual());
      return chrom;
    } catch (Exception cex) {
      // Rethrow to have a more convenient handling.
      // -------------------------------------------
      throw new IllegalStateException(cex);
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
   * @param a_depth the maximum depth of the chromosome to create
   * @param a_type the type of the chromosome to create
   * @param a_argTypes the array of argument types for this chromosome
   * @param a_functionSet the set of nodes valid to pick from
   * @param a_grow true: use grow method; false: use full method
   * @param a_tries maximum number of tries for creating a valid program
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void growOrFull(final int a_num, final int a_depth, final Class a_type,
                         final Class[] a_argTypes,
                         final CommandGene[] a_functionSet, boolean a_grow,
                         int a_tries) {
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, getFunctionSet(), 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        m_functionSet[a_functionSet.length + i]
            = new Argument(getGPConfiguration(), i, a_argTypes[i]);
      }
      // Initialization of genotype according to specific problem requirements.
      // ----------------------------------------------------------------------
      CommandGene n;
      IGPInitStrategy programIniter = getGPConfiguration().getInitStrategy();
      if (programIniter == null) {
        n = null;
      }
      else {
        try {
          n = programIniter.init(this, a_num);
        } catch (Exception ex) {
          throw new IllegalStateException(ex);
        }
      }
      // Build the (rest of the) GP program.
      // -----------------------------------
      int localDepth = a_depth;
      m_index = 0;
      m_maxDepth = localDepth;
      growOrFullNode(a_num, localDepth, a_type, 0, m_functionSet, n, 0, a_grow,
                     -1, false);
      // Give the chance of validating the whole program.
      // ------------------------------------------------
      if (!getGPConfiguration().validateNode(this, null, n, a_tries,
          a_num, 0, a_type, m_functionSet,
          a_depth, a_grow, -1, true)) {
        throw new IllegalStateException("Randomly created program violates"
                                        +
                                        " configuration constraints (symptom 3).");
      }
      redepth();
    } catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Output program in left-hand notion (e.g.: "+ X Y" for "X + Y").
   *
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
    String funcName = m_genes[a_startNode].toString();
    int j = 1;
    do {
      String placeHolder = "&" + j;
      int foundIndex = funcName.indexOf(placeHolder);
      if (foundIndex < 0) {
        break;
      }
      funcName = funcName.replaceFirst(placeHolder, "");
      j++;
    } while (true);
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
   * Output program in "natural" notion (e.g.: "X + Y" for "X + Y").
   *
   * @param a_startNode the node to start with, e.g. 0 for a complete dump of
   * the program
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
      return getFunctions()[a_startNode].toString();
    }
    String str = "";
    boolean paramOutput = false;
    if (m_genes[a_startNode].getArity(ind) > 0) {
      if (m_genes[a_startNode].toString().indexOf("&1") >= 0) {
        paramOutput = true;
      }
    }
    if (m_genes[a_startNode].getArity(ind) == 1 || paramOutput) {
      str += getFunctions()[a_startNode].toString();
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
        str += " " + m_genes[a_startNode].toString() + " ";
      }
    }
    if (a_startNode > 0) {
      str += ")";
    }
    return str;
  }

  /**
   * @return business key of the chromosome
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public String getBusinessKey() {
    return toStringNorm(0);
  }

  /**
   * @return debug representation of progrm chromosome, containing class names
   * of all children
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public String toStringDebug() {
    IGPProgram ind = getIndividual();
    if (m_genes[0].getArity(ind) == 0) {
      return getClass().getName();
    }
    String s = "";
    for (int i = 0; i < m_genes[0].getArity(ind); i++) {
      String childString = toStringNorm(getChild(0, i));
      s = s + "<" + childString + " >";
    }
    return s;
  }


  private Map<NodeInfo,Boolean> m_possibleNodes = new HashMap();

  /**
   * Determines whether there exists a function or terminal in the given node
   * set with the given return and sub return type.
   *
   * @param a_returnType the return type to look for
   * @param a_subReturnType the sub return type to look for
   * @param a_nodeSet the array of nodes to look through
   * @param a_function true to look for a function, false to look for a terminal
   * @param a_growing true: grow mode, false: full mode
   *
   * @return true if such a node exists, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isPossible(Class a_returnType, int a_subReturnType,
                            CommandGene[] a_nodeSet,
                            boolean a_function, boolean a_growing) {
    NodeInfo nodeInfo = new NodeInfo(a_returnType, a_subReturnType);
    Boolean result = m_possibleNodes.get(nodeInfo);
    if(result != null) {
      return result;
    }
    IGPProgram ind = getIndividual();
    for (int i = 0; i < a_nodeSet.length; i++) {
      if (a_nodeSet[i].getReturnType() == a_returnType
          && (a_subReturnType == 0
              || a_subReturnType == a_nodeSet[i].getSubReturnType())) {
        if (a_nodeSet[i].getArity(ind) == 0 && (!a_function || a_growing)) {
          m_possibleNodes.put(nodeInfo, true);
          return true;
        }
        if (a_nodeSet[i].getArity(ind) != 0 && a_function) {
          m_possibleNodes.put(nodeInfo, true);
          return true;
        }
      }
    }
    m_possibleNodes.put(nodeInfo, false);
    return false;
  }

  private class NodeInfo {
    public Class returnType;
    public int subReturnType;
    public NodeInfo(Class a_returnType, int a_subReturnType) {
      returnType = a_returnType;
      subReturnType = a_subReturnType;
    }
  }

  /**
   * Randomly chooses a valid node from the functions set.
   *
   * @param a_chromIndex index of the chromosome in the individual (0..n-1)
   * @param a_returnType the return type of node to choose
   * @param a_subReturnType the sub return type to look for
   * @param a_functionSet the functions to use
   * @param a_function true to choose a function, false to choose a terminal
   * @param a_growing true to ignore the function parameter, false otherwise
   * @return the node chosen
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected CommandGene selectNode(int a_chromIndex, Class a_returnType,
                                   int a_subReturnType,
                                   CommandGene[] a_functionSet,
                                   boolean a_function, boolean a_growing) {
    // Determine possible functions.
    // -----------------------------
    Vector<CommandGene> possibleFunctions=new Vector<CommandGene>(0);
    IGPProgram ind = getIndividual();
    ISingleNodeValidator singleNodeValidator = getGPConfiguration().
        getSingleNodeValidator();
    for (int i = 0; i < a_functionSet.length; i++) {
      if (a_functionSet[i].getReturnType() == a_returnType
          && (a_subReturnType == 0
              || a_subReturnType == a_functionSet[i].getSubReturnType())) {
        if (a_functionSet[i].getArity(ind) == 0 && (!a_function || a_growing)) {
          // Verify if function/terminal is allowed here.
          // --------------------------------------------
          if (singleNodeValidator == null ||
              singleNodeValidator.isAllowed(a_chromIndex, this, a_functionSet,
              a_functionSet[i], a_returnType, a_subReturnType, m_index)) {
            possibleFunctions.add(a_functionSet[i]);
          }
        }
        if (a_functionSet[i].getArity(ind) != 0 && a_function) {
          // Verify if function/terminal is allowed here.
          // --------------------------------------------
          if (singleNodeValidator == null ||
              singleNodeValidator.isAllowed(a_chromIndex, this, a_functionSet,
              a_functionSet[i],
              a_returnType, a_subReturnType, m_index)) {
            possibleFunctions.add(a_functionSet[i]);
          }
        }
      }
    }
    // Error handing in case no valid function found.
    // ----------------------------------------------
    if (possibleFunctions.isEmpty()) {
      if (a_growing && (a_returnType == CommandGene.VoidClass
                        || a_returnType == Void.class)) {
        // We simply return a NOP, it does nothing :-)
        // -------------------------------------------
        try {
          return new NOP(getGPConfiguration(), a_subReturnType);
        } catch (InvalidConfigurationException iex) {
          // Should never happen.
          // --------------------
          throw new RuntimeException(iex);
        }
      }
      final String errormsg = "Chromosome (depth "
          + getDepth(0)
          + ", index " + a_chromIndex
          + ") requires a " +
          (a_function ?
           ("function" + (a_growing ? " or terminal" : ""))
           : "terminal") + " of return type " +
          a_returnType
          + " (sub return type " + a_subReturnType + ")"
          + " but there is no such node available";
      if (!getGPConfiguration().isStrictProgramCreation()) {
        // Allow another try in case it is allowed.
        // ----------------------------------------
        throw new IllegalStateException(errormsg);
      }
      else {
        // Interrupt the whole evolution process.
        // --------------------------------------
        throw new RuntimeException(errormsg);
      }
    }
    // Select a function randomly.
    // ---------------------------
    int index = getGPConfiguration().getRandomGenerator().nextInt(
        possibleFunctions.size());
    CommandGene n = possibleFunctions.elementAt(index);
    return n;
  }

  /**
   * Create a tree of nodes using the grow or the full method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_depth the maximum depth of the tree to create
   * @param a_returnType the return type the lastly evaluated node must have
   * @param a_subReturnType the sub return type to look for
   * @param a_functionSet the set of function valid to pick from
   * @param a_rootNode null, or parent node of the node to develop
   * @param a_recurseLevel 0 for first call
   * @param a_grow true: use grow method; false: use full method
   * @param a_childNum index of the child in the parent node to which it belongs
   * (-1 if node is root node)
   * @param a_validateNode true: check if node selected is valid (when called
   * recursively a_validateNode is set to true)
   *
   * @return possible modified set of functions (e.g. to avoid having a unique
   * command more than once)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected CommandGene[] growOrFullNode(int a_num, int a_depth,
      Class a_returnType, int a_subReturnType, CommandGene[] a_functionSet,
      CommandGene a_rootNode, int a_recurseLevel,
      boolean a_grow, int a_childNum, boolean a_validateNode) {
    boolean mutated = false;
    boolean uncloned = true;
    GPConfiguration conf = getGPConfiguration();
    RandomGenerator random = conf.getRandomGenerator();
    if (a_rootNode == null || a_validateNode) {
      int tries = 0;
      int evolutionRound = getGPConfiguration().getGenerationNr();
      boolean aFunction = a_depth >= 1;
      // Clone the array, not the content of the array.
      // ----------------------------------------------
      CommandGene[] localFunctionSet = (CommandGene[]) a_functionSet.clone();
      int len = a_functionSet.length;
      do {
        CommandGene node = selectNode(a_num, a_returnType, a_subReturnType,
                                      localFunctionSet, aFunction, a_grow);
        if (!conf.validateNode(this, node, a_rootNode, tries++, a_num,
                               a_recurseLevel, a_returnType, localFunctionSet,
                               a_depth, a_grow, a_childNum, false)) {
          // In the first round of evolution ensure to always have one valid
          // individual as we need a prototype for cloning!
          // ---------------------------------------------------------------
          if (evolutionRound > 0 || tries <= len*2) {
            // Remove invalid node from local function set.
            // --------------------------------------------
            localFunctionSet = remove(localFunctionSet, node);
            if (localFunctionSet.length == 0) {
              throw new IllegalStateException("No appropriate function found"
                  + " during program creation!");
            }
            continue;
          }
        }
        // Optionally use a mutant/clone of the originally selected command
        // instead of reusing the same command instance.
        // ----------------------------------------------------------------
        if (random.nextDouble() <= conf.getMutationProb()) {
          if (IMutateable.class.isAssignableFrom(node.getClass())) {
            try {
              CommandGene node2 = ( (IMutateable) node).applyMutation(0,
                  random.nextDouble());
              // Check if mutant's function is allowed.
              // --------------------------------------
              if (getCommandOfClass(0, node2.getClass()) >= 0) {
                mutated = true;
                if (node2 != node) {
                  node = node2;
                  uncloned = false;
                }
              }
            } catch (InvalidConfigurationException iex) {
              // Ignore but log.
              // ---------------
              LOGGER.warn("Ignored problem", iex);
            }
          }
        }
        // Avoid using commands more than once if allowed only once.
        // ---------------------------------------------------------
        if (IUniqueCommand.class.isAssignableFrom(node.getClass())) {
          a_functionSet = remove(a_functionSet, node);
        }
        a_rootNode = node;
        break;
      } while (true);
    }
    // Generate the new node.
    // ----------------------
    m_depth[m_index] = m_maxDepth - a_depth;
    // Optional dynamize the arity for commands with a flexible number
    // of children. Normally, dynamizeArity does nothing, see declaration
    // of method in CommandGene, which can be overridden in sub classes.
    // -------------------------------------------------------------------
    boolean dynamize = random.nextDouble() <= conf.getDynamizeArityProb();
    if (dynamize) {
      a_rootNode.dynamizeArity();
    }
    // Clone node if possible and not already done via mutation.
    // ---------------------------------------------------------
    if (uncloned && !conf.isNoCommandGeneCloning()
        && a_rootNode instanceof ICloneable) {
        /**@todo we could optionally use the clone handler*/
      a_rootNode = (CommandGene) ( (ICloneable) a_rootNode).clone();
      m_genes[m_index++] = a_rootNode;
    }
    else {
      m_genes[m_index++] = a_rootNode;
    }
    if (a_depth >= 1) {
      IGPProgram ind = getIndividual();
      int arity = a_rootNode.getArity(ind);
      for (int i = 0; i < arity; i++) {
        // Ensure required depth is cared about.
        // -------------------------------------
        if (m_index < m_depth.length) {
          a_functionSet = growOrFullNode(a_num, a_depth - 1,
              a_rootNode.getChildType(getIndividual(), i),
              a_rootNode.getSubChildType(i),
              a_functionSet, a_rootNode, a_recurseLevel + 1, a_grow,
              i, true);
        }
        else {
          // No valid program could be generated. Abort.
          // -------------------------------------------
          throw new IllegalStateException("Randomly created program violates"
              + " configuration constraints (symptom 1). It may be that you"
              + " specified a too small number of maxNodes to use"
              + " (current arity: "
              + i
              + ", overall arity: "
              + arity
              + ")!");
        }
      }
    }
    else {
      if (a_rootNode.getArity(getIndividual()) > 0) {
        // No valid program could be generated. Abort.
        // -------------------------------------------
        throw new IllegalStateException("Randomly created program violates"
                                        + " configuration constraints"
                                        + " (symptom 2):"
                                        + " Root node: "
                                        + a_rootNode.getClass().toString());
      }
    }
    return a_functionSet;
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
   * @return the index of the next node of the same depth as the current node
   * (i.e. the next sibling node)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected int redepth(int a_index) {
    int num = a_index + 1;
    CommandGene command = getNode(a_index);
    if (command == null) {
      throw new IllegalStateException("ProgramChromosome invalid at index "
                                      + a_index
                                      + " (command gene is null)");
    }
    IGPProgram ind = getIndividual();
    int arity = command.getArity(ind);
    for (int i = 0; i < arity; i++) {
      if (num < m_depth.length) {
        m_depth[num] = m_depth[a_index] + 1;
        // children[i][n] = num;
        num = redepth(num);
        if (num < 0) {
          break;
        }
      }
      else {
        return -1;
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
   * @since 3.01
   */
  public int getChild(int a_index, int a_child) {
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
    throw new RuntimeException("Bad child "
                               + a_child
                               + " of node with index = "
                               + a_index);
  }

  public int getChild(CommandGene a_node, int a_child) {
    int len = getFunctions().length;
    int index = -1;
    for (int i = 0; i < len; i++) {
      if (m_genes[i] == a_node) {
        index = i;
        break;
      }
    }
    if (index == -1) {
      return -2;
    }
    for (int i = index + 1; i < len; i++) {
      if (m_depth[i] <= m_depth[index]) {
        return -1;
      }
      if (m_depth[i] == m_depth[index] + 1) {
        if (--a_child < 0) {
          return i;
        }
      }
    }
    throw new RuntimeException("Bad child " + a_child +
                               " of node with index = " + index);
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
    // -----------------------------------------------
    for (i = a_index + 1; i < m_genes.length && m_genes[i] != null; i++) {
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
    int maxdepth = m_depth[a_index];
    for (int i = a_index + 1; i < m_genes.length && m_genes[i] != null; i++) {
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
   * Checks whether a node with a given type is contained in the program.
   *
   * @param a_type the type to look for
   * @param a_exactMatch true: look for exactly the given type: false: also look
   * for sub types
   * @return true specific node found
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  public CommandGene getNode(Class a_type, boolean a_exactMatch) {
    return getNode(a_type, a_exactMatch, 0);
  }

  public CommandGene getNode(Class a_type, boolean a_exactMatch,
                             int a_startIndex) {
    int size = m_genes.length;
    for (int i = a_startIndex; i < size; i++) {
      if (m_genes[i] != null) {
        if (a_exactMatch) {
          if (m_genes[i].getClass() == a_type) {
            m_genes[i].nodeIndex = i;
            return m_genes[i];
          }
        }
        else {
          if (a_type.isAssignableFrom(m_genes[i].getClass())) {
            m_genes[i].nodeIndex = i;
            return m_genes[i];
          }
        }
      }
      else {
        break;
      }
    }
    return null;
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
    else {
      int other = getChild(n, child);
      return m_genes[other].execute_int(this, other, args);
    }
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
      throw new IllegalArgumentException("Gene must not be null!");
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
          } catch (ClassCastException cex) {
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
    } catch (ClassCastException cex) {
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

  private CommandGene[] remove(CommandGene[] a_functionSet, CommandGene node) {
    int size = a_functionSet.length;
    for (int i = 0; i < size; i++) {
      if (a_functionSet[i] == node) {
        // Remove found element
        CommandGene[] result = new CommandGene[size - 1];
        if (i > 0) {
          System.arraycopy(a_functionSet, 0, result, 0, i);
        }
        if (size - i > 1) {
          System.arraycopy(a_functionSet, i + 1, result, i, size - i - 1);
        }
        return result;
      }
    }
    return a_functionSet;
  }

  protected String encode(String a_string) {
    return StringKit.encode(a_string);
  }

  protected String decode(String a_string) {
    return StringKit.decode(a_string);
  }

  /**
   * @return the persistent representation of the chromosome, including all
   * genes
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public String getPersistentRepresentation() {
    StringBuffer b = new StringBuffer();
    // Store current state.
    // --------------------
//    String state = PERSISTENT_FIELD_DELIMITER+m_functionSet

    // Process the contained genes.
    // ----------------------------
    for (CommandGene gene : m_genes) {
      if (gene == null) {
        break;
      }
      b.append(GENE_DELIMITER_HEADING);
      b.append(encode(
          gene.getClass().getName() +
          GENE_DELIMITER +
          gene.getPersistentRepresentation()));
      b.append(GENE_DELIMITER_CLOSING);
    }
    return b.toString();
  }

  /**
   *
   * @param a_representation String
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public void setValueFromPersistentRepresentation(final String
      a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      try {
        List r = split(a_representation);
        Iterator iter = r.iterator();
        StringTokenizer st;
        String clas;
        String representation;
        String g;
        CommandGene gene;
        List genes = new Vector();
        while (iter.hasNext()) {
          g = decode( (String) iter.next());
          st = new StringTokenizer(g, GENE_DELIMITER);
          if (st.countTokens() != 2)
            throw new UnsupportedRepresentationException("In " + g + ", " +
                "expecting two tokens, separated by " + GENE_DELIMITER);
          clas = st.nextToken();
          representation = st.nextToken();
          gene = createGene(clas, representation);
          genes.add(gene);
        }
        m_genes = (CommandGene[]) genes.toArray(new CommandGene[0]);
      } catch (Exception ex) {
        throw new UnsupportedRepresentationException(ex.toString());
      }
    }
  }

  /**
   * Creates a new instance of gene.
   *
   * @param a_geneClassName name of the gene class
   * @param a_persistentRepresentation persistent representation of the gene to
   * create (could be obtained via getPersistentRepresentation)
   *
   * @return newly created gene
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  protected CommandGene createGene(String a_geneClassName,
                                   String a_persistentRepresentation)
      throws Exception {
    Class geneClass = Class.forName(a_geneClassName);
    Constructor constr = geneClass.getConstructor(new Class[] {GPConfiguration.class});
    CommandGene gene = (CommandGene) constr.newInstance(new Object[] {
        getGPConfiguration()});
    gene.setValueFromPersistentRepresentation(a_persistentRepresentation);
    return gene;
  }

  /**
   * Splits a_string into individual gene representations.
   *
   * @param a_string the string to split
   * @return the elements of the returned array are the persistent
   * representation strings of the gene's components
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  protected static final List split(String a_string)
      throws UnsupportedRepresentationException {
    List a = Collections.synchronizedList(new ArrayList());
    StringTokenizer st = new StringTokenizer
        (a_string, GENE_DELIMITER_HEADING + GENE_DELIMITER_CLOSING, true);
    while (st.hasMoreTokens()) {
      if (!st.nextToken().equals(GENE_DELIMITER_HEADING)) {
        throw new UnsupportedRepresentationException(a_string +
            " no opening tag");
      }
      String n = st.nextToken();
      if (n.equals(GENE_DELIMITER_CLOSING)) {
        // Empty token.
        a.add("");
      }
      else {
        a.add(n);
        if (!st.nextToken().equals(GENE_DELIMITER_CLOSING)) {
          throw new UnsupportedRepresentationException
              (a_string + " no closing tag");
        }
      }
    }
    return a;
  }

  /**
   * Checks if a function or terminal of the class of the given instance exists
   * within a_functionSet
   *
   * @param a_functionSet the set of functions to check
   * @param a_function the commandgene to check for via its class
   * @return true if a_function's class exists within a_functionSet
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public boolean contains(CommandGene[] a_functionSet, CommandGene a_function) {
    Class clazz = a_function.getClass();
    return contains(a_functionSet, clazz);
  }

  /**
   * Checks if a function or terminal of the class of the given instance exists
   * within a_functionSet
   * @param a_functionSet the set of functions to check
   * @param a_function the commandgene to check for via its class
   * @return true if a_function's class exists within a_functionSet
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public boolean contains(CommandGene[] a_functionSet, Class a_function) {
    Class clazz = a_function;
    for(int i=0;i<a_functionSet.length;i++) {
      if (a_functionSet[i].getClass() == clazz) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a function or terminal of the class of the given instance exists
   * within a_functionSet at least a_limit times
   *
   * @param a_functionSet the set of functions to check
   * @param a_function the commandgene to check for via its class
   * @param a_limit if a_limit occurrences of a_function were detected, exit
   * @return true if a_function's class exists at least a_limit times within
   * a_functionSet
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public int contains(CommandGene[] a_functionSet, CommandGene a_function, int a_limit) {
    Class clazz = a_function.getClass();
    if(a_limit <= 0) {
      a_limit = Integer.MAX_VALUE;
    }
    int found = 0;
    for(int i=0;i<a_functionSet.length;i++) {
      if (a_functionSet[i].getClass() == clazz) {
        found++;
        if(found >= a_limit) {
          return found;
        }
      }
    }
    return found;
  }
}
