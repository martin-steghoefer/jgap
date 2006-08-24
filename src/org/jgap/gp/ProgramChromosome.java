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

import org.jgap.*;

/**
 * Chromosome representing a single GP Program.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProgramChromosome
    extends Chromosome {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.16 $";

  /*wodka:
   void add(Command cmd);
   java.util.Iterator commands();
   Interpreter interpreter();
   Program createEmptyChildProgram();
   SodaGlobals getGlobals();
   void setGlobals(SodaGlobals globals);
   Language getLanguage();
   */

  /**
   * The allowable function/terminal list.
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

  public ProgramChromosome(GPConfiguration a_configuration, int a_size)
      throws InvalidConfigurationException {
    super(a_configuration, a_size);
    init(a_size);
  }

  public ProgramChromosome(GPConfiguration a_configuration, int a_size,
                           CommandGene[] a_functionSet,
                           Class[] a_argTypes)
      throws InvalidConfigurationException {
    super(a_configuration, a_size);
    m_functionSet = a_functionSet;
    argTypes = a_argTypes;
    init();
  }

  public ProgramChromosome(GPConfiguration a_configuration,
                           Gene[] a_initialGenes)
      throws InvalidConfigurationException {
    super(a_configuration);
    int i = 0;
    while (i < a_initialGenes.length && a_initialGenes[i] != null) {
      i++;
    }
    Gene[] genes = new Gene[i];
    for (int k = 0; k < i; k++) {
      genes[k] = a_initialGenes[k];
    }
    init(a_initialGenes.length);
  }

  public void setArgTypes(Class[] a_argTypes) {
    argTypes = a_argTypes;
  }

  /**
   * Default constructor.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ProgramChromosome()
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration());
  }

  public ProgramChromosome(final Configuration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    init();
  }

  private void init()
      throws InvalidConfigurationException {
    init(getConfiguration().getPopulationSize());
  }

  private void init(final int a_size)
      throws InvalidConfigurationException {
    m_depth = new int[a_size];
    setFunctions(new CommandGene[a_size]);
  }

  public synchronized Object clone() {
    try {
      ProgramChromosome chrom = new ProgramChromosome( (GPConfiguration)
          getConfiguration(),
          (Gene[]) getGenes().clone());
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

  public void cleanup() {
    cleanup(0);
  }

  protected void cleanup(final int n) {
    if (n < 0) {
      return;
    }
    for (int i = 0; i < getFunctions()[n].getArity(); i++) {
      cleanup(getChild(n, i));
    }
    getFunctions()[n].cleanup();
  }

  public void addCommand(final CommandGene a_command)
      throws InvalidConfigurationException {
    if (a_command == null) {
      throw new IllegalArgumentException("Command may not be null!");
    }
    final int len = getGenes().length;
    Gene[] genes = new Gene[len + 1];
    System.arraycopy(getGenes(), 0, genes, 0, len);
    genes[len] = a_command;
    setGenes(genes);
  }

  /**
   * Initialize this chromosome using the full method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_depth the depth of the chromosome to create
   * @param a_type the type of the chromosome to create
   * @param a_argTypes the array of types of arguments for this chromosome
   * @param a_functionSet the set of nodes valid to pick from
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void full(final int a_num, final int a_depth, final Class a_type,
                   final Class[] a_argTypes, final CommandGene[] a_functionSet) {
    /**@todo consolidate with grow*/
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, m_functionSet, 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        m_functionSet[a_functionSet.length + i]
            = new Argument(getConfiguration(), i, a_argTypes[i]);
      }
      m_index = 0;
      m_maxDepth = a_depth;
      /**@todo init is experimental, make dynamic*/
      // Initialization of genotype according to specific problem requirements.
      // ----------------------------------------------------------------------
      CommandGene n = null;
      if (!false) {
        if (a_num == 0) {
          for (int i = 0; i < m_functionSet.length; i++) {
            CommandGene m = m_functionSet[i];
            if (m.getClass() == SubProgramCommand.class) {
              n = m;
              break;
            }
          }
        }
        else if (a_num == 1) {
          for (int i = 0; i < m_functionSet.length; i++) {
            CommandGene m = m_functionSet[i];
            if (m.getClass() == SubProgramCommand.class) {
              n = m;
              break;
            }
          }
        }
      }
      fullNode(a_num, a_depth, a_type, m_functionSet, n, 0);
      redepth();
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Initialize this chromosome using the grow method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param depth the maximum depth of the chromosome to create
   * @param type the type of the chromosome to create
   * @param a_argTypes the array of types of arguments for this chromosome
   * @param a_functionSet the set of nodes valid to pick from
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void grow(final int a_num, final int depth, final Class type,
                   final Class[] a_argTypes, final CommandGene[] a_functionSet) {
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, getFunctionSet(), 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        m_functionSet[a_functionSet.length + i]
            = new Argument(getConfiguration(), i, a_argTypes[i]);
      }
      m_index = 0;
      m_maxDepth = depth;
      /**@todo init is experimental, make dynamic*/
      // Initialization of genotype according to specific problem requirements.
      // ----------------------------------------------------------------------
      CommandGene n = null;
      if (!false) {
        if (a_num == 0) { /**@todo experimental*/
          for (int i = 0; i < m_functionSet.length; i++) {
            CommandGene m = m_functionSet[i];
            if (m.getClass() == SubProgramCommand.class) {
              n = m;
              break;
            }
          }
        }
      }
      growNode(a_num, depth, type, getFunctionSet(), n, 0);
      redepth();
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Output program in left-hand notion (e.g.: "+ X Y" for "X + Y")
   * @param a_n node to start with
   * @return output in left-hand notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toString(final int a_n) {
    if (a_n < 0) {
      return "";
    }
    // Replace any occurance of placeholders (e.g. &1, &2...) in the function's
    // name.
    // ------------------------------------------------------------------------
    String funcName = getFunctions()[a_n].getName();
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
    if (getFunctions()[a_n].getArity() == 0) {
      return funcName + " ";
    }
    String str = "";
    str += funcName + " ( ";
    for (int i = 0; i < getFunctions()[a_n].getArity(); i++) {
      str += toString(getChild(a_n, i));
    }
    if (a_n == 0) {
      str += ")";
    }
    else {
      str += ") ";
    }
    return str;
  }

  /**
   * Output program in "natural" notion (e.g.: "X + Y" for "X + Y")
   * @param a_n node to start with
   * @return output in natural notion
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public String toString2(final int a_n) {
    if (a_n < 0) {
      return "";
    }
    if (getFunctions()[a_n].getArity() == 0) {
      return getFunctions()[a_n].getName() + " ";
    }
    String str = "";
    boolean paramOutput = false;
    if (getFunctions()[a_n].getArity() > 0) {
      if (getFunctions()[a_n].getName().indexOf("&1") >= 0) {
        paramOutput = true;
      }
    }
    if (getFunctions()[a_n].getArity() == 1 || paramOutput) {
      str += getFunctions()[a_n].getName();
    }
    if (a_n > 0) {
      str = "(" + str;
//      str += "(";
    }
    for (int i = 0; i < getFunctions()[a_n].getArity(); i++) {
      String childString = toString2(getChild(a_n, i));
      String placeHolder = "&" + (i + 1);
      int placeholderIndex = str.indexOf(placeHolder);
      if (placeholderIndex >= 0) {
        str = str.replaceFirst(placeHolder, childString);
      }
      else {
        str += childString;
      }
      if (i == 0 && getFunctions()[a_n].getArity() != 1 && !paramOutput) {
        str += " " + getFunctions()[a_n].getName() + " ";
      }
    }
    if (a_n > 0) {
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
  public static boolean isPossible(Class a_type, CommandGene[] a_nodeSet,
                            boolean a_function, boolean a_growing) {
    for (int i = 0; i < a_nodeSet.length; i++) {
      if (a_nodeSet[i].getReturnType() == a_type) {
        if (a_nodeSet[i].getArity() == 0 && (!a_function || a_growing)) {
          return true;
        }
        if (a_nodeSet[i].getArity() != 0 && a_function) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Randomly chooses a valid node from the functions set.
   *
   * @param a_config the configuration to use
   * @param a_type the type of node to choose
   * @param a_functionSet the functions to use
   * @param a_function true to choose a function, false to choose a terminal
   * @param a_growing true to ignore the function parameter, false otherwise
   * @return the node chosen
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected CommandGene selectNode(GPConfiguration a_config, Class a_type,
                                   CommandGene[] a_functionSet,
                                   boolean a_function, boolean a_growing) {
    if (!isPossible(a_type, a_functionSet, a_function, a_growing)) {
      throw new IllegalArgumentException("Chromosome requires a " +
                                         (a_function ?
                                          ("function" +
                                           (a_growing ? " or terminal" : ""))
                                          : "terminal") + " of type " +
                                         a_type +
                                         " but there is no such node available");
    }
    CommandGene n = null;
    int lindex;
    // Following is analog to isPossible except with the random generator.
    // -------------------------------------------------------------------
    while (n == null) {
      lindex = a_config.getRandomGenerator().nextInt(a_functionSet.length);
      if (a_functionSet[lindex].getReturnType() == a_type) {
        if (a_functionSet[lindex].getArity() == 0 && (!a_function || a_growing)) {
          n = a_functionSet[lindex];
        }
        if (a_functionSet[lindex].getArity() != 0 && a_function) {
          n = a_functionSet[lindex];
        }
      }
    }
    return n;
  }

  /**
   * Create a tree of nodes using the full method.
   *
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_depth the depth of the tree to create
   * @param a_type the type of node to start with
   * @param a_functionSet the set of function valid to pick from
   * @param a_rootNode null, or root node to use
   * @param a_recurseLevel 0 for first call
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void fullNode(int a_num, int a_depth, Class a_type,
                          CommandGene[] a_functionSet, CommandGene a_rootNode,
                          int a_recurseLevel) {
    if (a_rootNode == null) {
      do {
      a_rootNode = selectNode( (GPConfiguration) getConfiguration(), a_type,
                              a_functionSet, a_depth > 1, false);
      }
      /**@todo following is experimental*/
      while ((a_num == 0 || a_num == 1) && a_recurseLevel > 0 &&
             a_rootNode.getClass() == SubProgramCommand.class);
    }
    m_depth[m_index] = m_maxDepth - a_depth;
    getFunctions()[m_index++] = a_rootNode;
    if (a_depth > 1) {
      for (int i = 0; i < a_rootNode.getArity(); i++) {
        fullNode(a_num, a_depth - 1, a_rootNode.getChildType(i), a_functionSet,
                 null, a_recurseLevel + 1);
      }
    }
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
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void growNode(int a_num, int a_depth, Class a_type,
                          CommandGene[] a_functionSet, CommandGene a_rootNode,
                          int a_recurseLevel) {
    if (a_rootNode == null) {
      do {
        if (a_num == 1 && a_depth > 1) {
          int x = 2;
        }
      a_rootNode = selectNode( (GPConfiguration) getConfiguration(), a_type,
                              a_functionSet, a_depth > 1, true);
      }
      /**@todo following is experimental*/
      while ((a_num == 0 || a_num == 1) && a_recurseLevel > 0 &&
             a_rootNode.getClass() == SubProgramCommand.class);
    }
    // Generate the node.
    // ------------------
    m_depth[m_index] = m_maxDepth - a_depth;
    getFunctions()[m_index++] = a_rootNode;
    if (a_depth > 1) {
      for (int i = 0; i < a_rootNode.getArity(); i++) {
        growNode(a_num, a_depth - 1, a_rootNode.getChildType(i), a_functionSet,
                 null, a_recurseLevel + 1);
      }
    }
  }

  /**
   * Recalculate the depths of each node.
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
    int arity = command.getArity();
    for (int i = 0; i < arity; i++) {
      m_depth[num] = m_depth[a_index] + 1;
      // children[i][n] = num;
      num = redepth(num);
      if (num < 0) {
        break;
      }
    }
    return num;
  }

  /**
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numTerminals() {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() == 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * @return the number of functions in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numFunctions() {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() != 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of terminals of the given type in this chromosome.
   *
   * @param a_type the type of terminal to count
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numTerminals(Class a_type) {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() == 0
          && getFunctions()[i].getReturnType() == a_type) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of functions of the given type in this chromosome.
   *
   * @param a_type the type of function to count
   * @return the number of functions in this chromosome.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numFunctions(Class a_type) {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() != 0
          && getFunctions()[i].getReturnType() == a_type) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the i'th node in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the root of this chromosome.
   *
   * @param a_index the node number to get
   * @return the node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public CommandGene getNode(int a_index) {
    if (a_index >= getFunctions().length || getFunctions()[a_index] == null) {
      return null;
    }
    return getFunctions()[a_index];
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
   * @since 3.0
   */
  public int getChild(int a_index, int a_child) {
    for (int i = a_index + 1; i < getFunctions().length; i++) {
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

  /**
   * Gets the i'th node of the given type in this chromosome. The nodes are
   * counted in a depth-first manner, with node 0 being the first node of the
   * given type in this chromosome.
   *
   * @param a_index the i'th node to get
   * @param a_type the type of node to get
   * @return the node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getNode(int a_index, Class a_type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getReturnType() == a_type) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th terminal in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the first terminal in this
   * chromosome.
   *
   * @param a_index the i'th terminal to get
   * @return the terminal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getTerminal(int a_index) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getArity() == 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th function in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the first function in this
   * chromosome.
   *
   * @param a_index the i'th function to get
   * @return the function
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getFunction(int a_index) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getArity() != 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th terminal of the given type in this chromosome. The nodes are
   * counted in a depth-first manner, with node 0 being the first terminal of
   * the given type in this chromosome.
   *
   * @param a_index the i'th terminal to get
   * @param a_type the type of terminal to get
   * @return the index of the terminal found, or -1 if no appropriate terminal
   * was found
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getTerminal(int a_index, Class a_type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getReturnType() == a_type
          && getFunctions()[j].getArity() == 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th function of the given type in this chromosome. The nodes are
   * counted in a depth-first manner, with node 0 being the first function of
   * the given type in this chromosome.
   *
   * @param a_index the i'th function to get
   * @param a_type the type of function to get
   * @return the index of the function found, or -1 if no appropriate function
   * was found
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getFunction(int a_index, Class a_type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getReturnType() == a_type
          && getFunctions()[j].getArity() != 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Helper: Find GP command with given class and return index of it
   * @param a_n return the n'th found command
   * @param a_terminalClass the class to find a command for
   * @return index of first found matching GP command, or -1 if none found
   */
  public int getCommandOfClass(int a_n, Class a_terminalClass) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getClass() == a_terminalClass) {
        if (--a_n < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Helper: Find GP Variable with given return type and return index of it
   * @param a_n return the n'th found command
   * @param a_returnType the return type to find a Variable for
   * @return index of first found matching GP command, or -1 if none found
   */
  public int getVariableWithReturnType(int a_n, Class a_returnType) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getClass() == Variable.class) {
        Variable v = (Variable) getFunctions()[j];
        if (v.getReturnType() == a_returnType) {
          if (--a_n < 0) {
            return j;
          }
        }
      }
    }
    return -1;
  }

  public CommandGene[] getFunctionSet() {
    return m_functionSet;
  }

  public void setFunctionSet(CommandGene[] a_functionSet) {
    m_functionSet = a_functionSet;
  }

  public CommandGene[] getFunctions() {
    return (CommandGene[])super.getGenes();
  }

  public void setFunctions(CommandGene[] a_functions)
      throws InvalidConfigurationException {
    setGenes(a_functions);
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
    for (i = a_index + 1; i < getFunctions().length && getFunctions()[i] != null;
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
    for (i = a_index + 1; i < getFunctions().length && getFunctions()[i] != null;
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
    if (a_child >= getFunctions().length || getFunctions()[a_child] == null) {
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
    boolean rtn = getFunctions()[0].execute_boolean(this, 0, args);
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
      return getFunctions()[n + 1].execute_boolean(this, n + 1, args);
    }
    int other = getChild(n, child);
    return getFunctions()[other].execute_boolean(this, other, args);
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
    getFunctions()[0].execute_void(this, 0, args);
    cleanup();
  }

  public void execute_void(int n, int child, Object[] args) {
    if (child == 0) {
      getFunctions()[n + 1].execute_void(this, n + 1, args);
    }
    int other = getChild(n, child);
    getFunctions()[other].execute_void(this, other, args);
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
    int rtn = getFunctions()[0].execute_int(this, 0, args);
    /**@todo reactivate*/
//    cleanup();
    return rtn;
  }

  public int execute_int(int n, int child, Object[] args) {
    if (child == 0) {
      return getFunctions()[n + 1].execute_int(this, n + 1, args);
    }
    int other = getChild(n, child);
    return getFunctions()[other].execute_int(this, other, args);
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
    long rtn = getFunctions()[0].execute_long(this, 0, args);
    cleanup();
    return rtn;
  }

  public long execute_long(int n, int child, Object[] args) {
    if (child == 0) {
      return getFunctions()[n + 1].execute_long(this, n + 1, args);
    }
    int other = getChild(n, child);
    return getFunctions()[other].execute_long(this, other, args);
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
    float rtn = getFunctions()[0].execute_float(this, 0, args);
    cleanup();
    return rtn;
  }

  public float execute_float(int n, int child, Object[] args) {
    if (child == 0) {
      if (getFunctions()[n + 1] == null) {
        return 0;
      }
      return getFunctions()[n + 1].execute_float(this, n + 1, args);
    }
    int other = getChild(n, child);
    if (other < 0) {
      return 0;
    }
    return getFunctions()[other].execute_float(this, other, args);
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
    double rtn = getFunctions()[0].execute_double(this, 0, args);
    cleanup();
    return rtn;
  }

  public double execute_double(int n, int child, Object[] args) {
    if (child == 0) {
      return getFunctions()[n + 1].execute_double(this, n + 1, args);
    }
    int other = getChild(n, child);
    return getFunctions()[other].execute_double(this, other, args);
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
    Object rtn = getFunctions()[0].execute_object(this, 0, args);
    cleanup();
    return rtn;
  }

  public Object execute_object(int n, int child, Object[] args) {
    if (child == 0) {
      return getFunctions()[n + 1].execute_object(this, n + 1, args);
    }
    int other = getChild(n, child);
    return getFunctions()[other].execute_object(this, other, args);
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
    return getFunctions()[0].execute_object(this, 0, args);
//    return execute_object(args);
  }

  public Object execute(int n, int child, Object[] args) {
    return execute_object(n, child, args);
  }

  /**
   * Initializes all chromosomes in this individual using the grow method.
   *
   * @param depth the depth of the chromosome to create
   * @param types the type of each chromosome, must be an array of the same
   * length as the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes. Note
   * that it is not necessary to include the arguments of a chromosome as
   * terminals in the chromosome's node set. This is done automatically for you
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void grow(int depth, Class[] types,
                   Class[][] a_argTypes, CommandGene[][] nodeSets) {
    /**@todo obsolete*/
//    CommandGene.setIndividual(this);
    argTypes = a_argTypes[0];
    // If there are any ADF's in the nodeSet, then set its type
    // according to the chromosome it references
    for (int j = 0; j < nodeSets[0].length; j++) {
      /**@todo implement ADF and reactivate the following*/
//        if (nodeSets[0][j] instanceof ADF)
//          ( (ADF) nodeSets[i][j]).setReturnType(
//              types[ ( (ADF) nodeSets[0][j]).getChromosomeNum()]);
    }
    grow(0, depth, types[0], a_argTypes[0], nodeSets[0]);
  }

  /**
   * Initializes all chromosomes in this individual using the full method.
   *
   * @param depth the depth of the chromosome to create
   * @param types the type of each chromosome, must be an array of the same
   * length as the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes.
   * Note that it is not necessary to include the arguments of a chromosome as
   * terminals in the chromosome's node set. This is done automatically for you
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void full(int depth, Class[] types,
                   Class[][] a_argTypes, CommandGene[][] nodeSets) {
//    CommandGene.setIndividual(this);
    /**@todo obsolete*/
    argTypes = a_argTypes[0];
    // If there are any ADF's in the nodeSet, then set its type
    // according to the chromosome it references
    for (int j = 0; j < nodeSets[0].length; j++) {
      /**@todo implement ADF impl and reactivate the following*/
//      if (nodeSets[0][j] instanceof ADF)
//        ( (ADF) nodeSets[i][j]).setReturnType(
//            types[ ( (ADF) nodeSets[0][j]).getChromosomeNum()]);
    }
    full(0, depth, types[0], a_argTypes[0], nodeSets[0]);
  }

  public void setGene(int index, Gene a_gene) {
    if (a_gene == null) {
      throw new IllegalArgumentException("Gene may not be null!");
    }
    getGenes()[index] = a_gene;
  }

  public Class[] getArgTypes() {
    return argTypes;
  }

  public int getArity() {
    return argTypes.length;
  }

  public int size() {
    int i = 0;
    while (i < getFunctions().length && getFunctions()[i] != null) {
      i++;
    }
    return i;
  }
}
