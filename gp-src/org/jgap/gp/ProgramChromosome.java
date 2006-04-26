package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Chromosome representing a single GP Program.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ProgramChromosome
    extends Chromosome {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private ILanguage m_language;

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
   * Array to hold the nodes in this Chromosome.
   * jg
   */
//  private CommandGene[] functions; //--> SET/GET-GENES //2005

  /**
   * The allowable function/terminal list.
   * jg
   */
  transient CommandGene[] functionSet = null;

  /**
   * Array to hold the depths of each node.
   * jg
   */
  private int[] depth;

  /**
   * Array to hold the types of the arguments to this Chromosome.
   * jg
   */
  private Class[] argTypes;

  transient int index;

  transient int maxDepth;

  public ProgramChromosome(GPConfiguration a_configuration, int a_size)
      throws InvalidConfigurationException {
    super(a_configuration, a_size);
    init();
  }

  public ProgramChromosome(GPConfiguration a_configuration, int a_size,
                           CommandGene[] a_functionSet,
                           CommandGene[] a_functions, Class[] a_argTypes)
      throws InvalidConfigurationException {
    super(a_configuration, a_size);
    functionSet = a_functionSet;
    argTypes = a_argTypes;
    /**@todo leere functions setzen, geht aber wegen null-Prüfung in setGenes
     * nicht!
     */
//    setFunctions(a_functions);
    init();
  }

  public ProgramChromosome(Configuration a_configuration, Gene[] a_initialGenes)
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

  public ProgramChromosome()
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration());
  }

  public ProgramChromosome(final Configuration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    init();
  }

  protected void init()
      throws InvalidConfigurationException {
    init(getConfiguration().getPopulationSize());
  }

  protected void init(int a_size)
      throws InvalidConfigurationException {
    depth = new int[a_size]; //jg
    setFunctions(new CommandGene[a_size]); //jg
  }

//  public abstract Interpreter interpreter();
  public ILanguage getLanguage() {
    return m_language;
  }

  public void setLanguage(ILanguage a_language) {
    m_language = a_language;
  }

  public Object evaluate() {
    /**@todo set the variable terminals with the values currently set with the
     * ProgramChromosome
     */

    // set the program to the configuration by pushing its commands (=Gene's)
    /**@todo solve more elegantly --> e.g., use list and iterate thru it*/
    MathConfiguration config = (MathConfiguration) getConfiguration();
    for (int i = 0; i < getGenes().length; i++) {
      config.push(getGene(i));
    }
//    for (int i = config.stackSize() - 1; i >= 0; i--) {
    for (int i = 0; i < config.stackSize(); i++) {
      CommandGene gene = (CommandGene) config.getStack(i);
      gene.evaluate(config, null);
    }
    Double res = (Double) config.popTerminal();
    return res;
  }

  public synchronized Object clone() {
    try {
      ProgramChromosome chrom = new ProgramChromosome(getConfiguration(),
          (Gene[]) getGenes().clone());
      chrom.argTypes = (Class[]) argTypes.clone();
      chrom.setFunctionSet( (CommandGene[]) getFunctionSet().clone());
      chrom.setFunctions( (CommandGene[]) getFunctions().clone());
      chrom.depth = (int[]) depth.clone();
      return chrom;
    }
    catch (Exception cex) {
      // rethrow to have a more convenient handling
      cex.printStackTrace();
      throw new IllegalStateException(cex.getMessage());
    }
  }

  public void cleanup() {
    cleanup(0);
  }

  protected void cleanup(int n) {
    if (n < 0) { //KM
      return; //KM
    }
    for (int i = 0; i < getFunctions()[n].getArity(); i++) {
      cleanup(getChild(n, i));
    }
    getFunctions()[n].cleanup();
  }

  public void addCommand(CommandGene a_command)
      throws InvalidConfigurationException {
    if (a_command == null) {
      throw new IllegalArgumentException("Command may not be null!");
    }
    int len = getGenes().length;
    Gene[] genes = new Gene[len + 1];
    System.arraycopy(getGenes(), 0, genes, 0, len);
    genes[len] = a_command;
    setGenes(genes);
  }

  public String[] printProgram() {
    /**@todo output all commands/terminals in the correct order*/
    //Result of this will be printed out
    return null;
  }

  public void clearProgram()
      throws InvalidConfigurationException {
    super.setGenes(new Gene[0]);
    System.err.println("clearProgram");
  }

  //BEGIN JG
  /**
   * Initialize this chromosome using the full method.
   *
   * @param num the number of this chromosome
   * @param depth the depth of the chromosome to create
   * @param type the type of the chromosome to create
   * @param argTypes the array of types of arguments for this chromosome
   * @param functionSetSet the set of nodes valid to pick from
   *
   * @since 1.0
   */
  public void full(int num, int depth, Class type, Class[] a_argTypes,
                   CommandGene[] a_functionSet) {
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, getFunctionSet(), 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        getFunctionSet()[a_functionSet.length +
            i] = new Variable(getConfiguration(), "X", a_argTypes[i]);
      }
      index = 0;
      maxDepth = depth;
      fullNode(depth, type, getFunctionSet());
      redepth();
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Initialize this chromosome using the grow method.
   *
   * @param num the chromosome number in the individual of this chromosome
   * @param depth the maximum depth of the chromosome to create
   * @param type the type of the chromosome to create
   * @param argTypes the array of types of arguments for this chromosome
   * @param functionSet the set of nodes valid to pick from
   *
   * @since 1.0
   */
  public void grow(int num, int depth, Class type, Class[] a_argTypes,
                   CommandGene[] a_functionSet) {
    try {
      argTypes = a_argTypes;
      setFunctionSet(new CommandGene[a_functionSet.length + a_argTypes.length]);
      System.arraycopy(a_functionSet, 0, getFunctionSet(), 0,
                       a_functionSet.length);
      for (int i = 0; i < a_argTypes.length; i++) {
        getFunctionSet()[a_functionSet.length +
            i] = new Variable(getConfiguration(), "X", a_argTypes[i]);
      }
      index = 0;
      maxDepth = depth;
      growNode(depth, type, getFunctionSet());
      redepth();
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString(int n) {
    if (n < 0) { //KM
      return ""; //KM
    }
    if (getFunctions()[n].getArity() == 0)
      return getFunctions()[n].getName() + " ";
    String str = new String();
    str += getFunctions()[n].getName() + " ( ";
    for (int i = 0; i < getFunctions()[n].getArity(); i++)
      str += toString(getChild(n, i));
    str += ") ";
    return str;
  }

  public String toString2(int n) {
    if (n < 0) { //KM
      return ""; //KM
    }
    if (getFunctions()[n].getArity() == 0) {
      return getFunctions()[n].getName() + " ";
    }
    String str = new String();
    if (getFunctions()[n].getArity() == 1) {
      str += getFunctions()[n].getName();
    }
    if (n > 0) {
      str += "(";
    }
    for (int i = 0; i < getFunctions()[n].getArity(); i++) {
      str += toString2(getChild(n, i));
      if (i == 0) {
        if (getFunctions()[n].getArity() != 1) {
          str += " " + getFunctions()[n].getName() + " ";
        }
      }
    }
    if (n > 0) {
      str += ")";
    }
    return str;
  }

  /**
   * Determines whether there exists a function or terminal in the given node
   * set with the given type.
   *
   * @param type the type to look for
   * @param nodeSet the array of nodes to look through
   * @param function true to look for a function, false to look for a terminal
   *
   * @return true if such a node exists, false otherwise
   *
   * @since 1.0
   */
  public static boolean isPossible(Class type, CommandGene[] nodeSet,
                                   boolean function) {
    for (int i = 0; i < nodeSet.length; i++) {
      if (nodeSet[i].getReturnType() == type
          && (nodeSet[i].getArity() != 0) == function) {
        return true;
      }
    }
    return false;
  }

  public boolean isPossible(CommandGene f) {
    for (int i = 0; i < getFunctionSet().length; i++) {
      if (getFunctionSet()[i] == f) {
        return true;
      }
    }
    return false;
  }

  /**
   * Randomly chooses a node from the node set.
   *
   * @param type the type of node to choose
   * @param nodeSet the array of nodes to choose from
   * @param function true to choose a function, false to choose a terminal
   * @param growing true to ignore the function parameter, false otherwise
   * @return the node chosen
   *
   * @since 1.0
   */
  public static CommandGene selectNode(GPConfiguration a_config, Class type,
                                       CommandGene[] functionSet,
                                       boolean function, boolean growing) {
    if (!isPossible(type, functionSet, function))
      throw new IllegalArgumentException("Chromosome requires a " +
                                         (function ?
                                          ("function" +
                                           (growing ? " or terminal" : ""))
                                          : "terminal") + " of type " +
                                         type +
                                         " but there is no such node available");
    CommandGene n = null;
    int lindex;
    while (n == null) {
      lindex = a_config.getRandomGenerator().nextInt(
          functionSet.length);
      if (functionSet[lindex].getReturnType() == type) {
        if (functionSet[lindex].getArity() == 0 && (!function || growing))
          n = functionSet[lindex];
        if (functionSet[lindex].getArity() != 0 && function)
          n = functionSet[lindex];
      }
    }
    return n;
  }

  /**
   * Create a tree of nodes using the full method.
   *
   * @param depth the depth of the tree to create
   * @param type the type of node to start with
   * @param nodeSet the set of nodes valid to pick from
   * @return a node which is the root of the generated tree
   *
   * @since 1.0
   */
  void fullNode(int a_depth, Class type, CommandGene[] a_functionSet) {
    // Generate the node.
    CommandGene n = selectNode( (GPConfiguration) getConfiguration(), type,
                               a_functionSet, a_depth > 1, false);
    depth[index] = maxDepth - a_depth;
    getFunctions()[index++] = n;
    if (a_depth > 1)
      for (int i = 0; i < n.getArity(); i++)
        fullNode(a_depth - 1, n.getChildType(i), a_functionSet);
  }

  /**
   * Create a tree of nodes using the grow method.
   *
   * @param depth the maximum depth of the tree to create
   * @param type the type of node to start with
   * @param nodeSet the set of nodes valid to pick from
   * @return a node which is the root of the generated tree
   *
   * @since 1.0
   */
  void growNode(int a_depth, Class type, CommandGene[] a_functionSet) {
    // Generate the node.
    CommandGene n = selectNode( (GPConfiguration) getConfiguration(), type,
                               a_functionSet, a_depth > 1, true);
    depth[index] = maxDepth - a_depth;
    getFunctions()[index++] = n;
    if (a_depth > 1)
      for (int i = 0; i < n.getArity(); i++)
        growNode(a_depth - 1, n.getChildType(i), a_functionSet);
  }

  /**
   * Recalculate the depths of each node.
   */
  public void redepth() {
    depth[0] = 0;
    redepth(0);
  }

  /**
   * Calculate the depth of the next node and the indices of the children
   * of the current node.
   *
   * The depth of the next node is
   * just one plus the depth of the current node. The index of the first
   * child is always the next node. The index of the second child is found
   * by recursively calling this method on the tree starting with the first
   * child.
   *
   * @returns the index of the next node of the same depth as the
   * current node (i.e. the next sibling node)
   */
  protected int redepth(int n) {
    int num = n + 1;
//    if (num >= depth.length) {//KM
//      return -1; //KM: inserted
//    }
    CommandGene command = getNode(n);
    if (command == null) { //KM
      /**@todo Error must not occur. Seems an init. problem*/
//      return -1;//KM
    }
    int arity = command.getArity();
    for (int i = 0; i < arity; i++) {
      depth[num] = depth[n] + 1;
      // children[i][n] = num;
      num = redepth(num);
      if (num < 0) { //KM
        break; //KM
      }
    }
    return num;
  }

  /**
   * Counts the number of terminals in this chromosome.
   *
   * @return the number of terminals in this chromosome.
   *
   * @since 1.0
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
   * @since 1.0
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
   * @param type the type of terminal to count
   * @return the number of terminals in this chromosome
   *
   * @since 1.0
   */
  public int numTerminals(Class type) {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() == 0
          && getFunctions()[i].getReturnType() == type) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of functions of the given type in this chromosome.
   *
   * @param type the type of function to count
   * @return the number of functions in this chromosome.
   *
   * @since 1.0
   */
  public int numFunctions(Class type) {
    int count = 0;
    for (int i = 0; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (getFunctions()[i].getArity() != 0
          && getFunctions()[i].getReturnType() == type) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the i'th node in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the root of this chromosome.
   *
   * @param i the node number to get
   * @return the node
   *
   * @since 1.0
   */
  public CommandGene getNode(int i) {
    if (i >= getFunctions().length || getFunctions()[i] == null) {
      return null;
    }
    return getFunctions()[i];
  }

  /**
   * Gets the child'th child of the n'th node in this chromosome. This is the same
   * as the child'th node whose depth is one more than the depth of the n'th node.
   *
   * @param n the node number of the parent
   * @param child the child number (starting from 0) of the parent
   * @returns the node number of the child, or -1 if not found
   *
   * @since 1.2.0
   */
  public int getChild(int n, int child) {
    for (int i = n + 1; i < getFunctions().length; i++) {
//      if (i>=depth.length) {//KM
//        return -1;//KM
//      }
      if (depth[i] <= depth[n]) {
        return -1;
      }
      if (depth[i] == depth[n] + 1) {
        if (--child < 0) {
          return i;
        }
      }
    }
    throw new RuntimeException("Bad child " + child + " of n " + n);
  }

  /**
   * Gets the i'th node of the given type in this chromosome. The nodes are counted in a depth-first
   * manner, with node 0 being the first node of the given type in this chromosome.
   *
   * @param i the node number to get
   * @param type the type of node to get
   * @return the node
   *
   * @since 1.0
   */
  public int getNode(int i, Class type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getReturnType() == type) {
        if (--i < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th terminal in this chromosome. The nodes are counted in a depth-first
   * manner, with node 0 being the first terminal in this chromosome.
   *
   * @param i the terminal number to get
   * @return the terminal
   *
   * @since 1.0
   */
  public int getTerminal(int i) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getArity() == 0) {
        if (--i < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th function in this chromosome. The nodes are counted in a depth-first
   * manner, with node 0 being the first function in this chromosome.
   *
   * @param i the function number to get
   * @return the function
   *
   * @since 1.0
   */
  public int getFunction(int i) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++) {
      if (getFunctions()[j].getArity() != 0) {
        if (--i < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th terminal of the given type in this chromosome. The nodes are counted in a depth-first
   * manner, with node 0 being the first terminal of the given type in this chromosome.
   *
   * @param i the terminal number to get
   * @param type the type of terminal to get
   * @return the terminal
   *
   * @since 1.0
   */
  public int getTerminal(int i, Class type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++)
      if (getFunctions()[j].getReturnType() == type
          && getFunctions()[j].getArity() == 0)
        if (--i < 0)
          return j;
    return -1;
  }

  /**
   * Gets the i'th function of the given type in this chromosome. The nodes are counted in a depth-first
   * manner, with node 0 being the first function of the given type in this chromosome.
   *
   * @param i the function number to get
   * @param type the type of function to get
   * @return the function
   *
   * @since 1.0
   */
  public int getFunction(int i, Class type) {
    for (int j = 0; j < getFunctions().length && getFunctions()[j] != null; j++)
      if (getFunctions()[j].getReturnType() == type
          && getFunctions()[j].getArity() != 0)
        if (--i < 0)
          return j;
    return -1;
  }

  public CommandGene[] getFunctionSet() {
    return functionSet;
  }

  public void setFunctionSet(CommandGene[] a_functionSet) {
    functionSet = a_functionSet;
  }

  public CommandGene[] getFunctions() {
    return (CommandGene[])super.getGenes();
  }

  public void setFunctions(CommandGene[] a_functions)
      throws InvalidConfigurationException {
    setGenes(a_functions);
  }

  /**
   * Gets the number of nodes in the branch starting at the n'th node.
   *
   * @param n the index of the node at which to start counting.
   * @returns the number of nodes in the branch starting at the n'th node.
   */
  public int getSize(int n) {
    int i;
    // Get the node at which the depth is <= depth[n].
    for (i = n + 1; i < getFunctions().length && getFunctions()[i] != null; i++) {
//      if (i >= depth.length) {//KM
//        return 0;//KM
//      }
      if (depth[i] <= depth[n]) {
        break;
      }
    }
    return i - n;
  }

  /**
   * Gets the depth of the branch starting at the n'th node.
   *
   * @param n the index of the node at which to check the depth.
   * @returns the depth of the branch starting at the n'th node.
   */
  public int getDepth(int n) {
    int i, maxdepth = depth[n];
    for (i = n + 1; i < getFunctions().length && getFunctions()[i] != null; i++) {
      if (depth[i] <= depth[n]) {
        break;
      }
      if (depth[i] > maxdepth) {
        maxdepth = depth[i];
      }
    }
    return maxdepth - depth[n];
  }

  /**
   * Gets the node which is the parent of the given node in this chromosome. If the child is at
   * depth d then the parent is the first function at depth d-1 when iterating backwards through
   * the function list starting from the child.
   *
   * @param child the child node
   * @return the parent node, or null if the child is the root node
   *
   * @since 1.0
   */
  public int getParentNode(int child) {
    if (child >= getFunctions().length || getFunctions()[child] == null)
      return -1;
    for (int i = child - 1; i >= 0; i--) {
      if (depth[i] == depth[child] - 1) {
        return i;
      }
    }
    return -1;
  }

//BEGIN JG
  /**
   * Executes this node as a boolean.
   *
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not
   * boolean
   *
   * @since 1.0
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
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not boolean
   */
  public boolean execute_boolean(int n, int child, Object[] args) {
    if (child == 0)
      return getFunctions()[n + 1].execute_boolean(this, n + 1, args);
    int other = getChild(n, child);
    return getFunctions()[other].execute_boolean(this, other, args);
  }

  /**
   * Executes this node, returning nothing.
   *
   * @throws UnsupportedOperationException if the type of this node is not void
   *
   * @since 1.0
   */
  public void execute_void(Object[] args) {
    getFunctions()[0].execute_void(this, 0, args);
    cleanup();
  }

  public void execute_void(int n, int child, Object[] args) {
    if (child == 0)
      getFunctions()[n + 1].execute_void(this, n + 1, args);
    int other = getChild(n, child);
    getFunctions()[other].execute_void(this, other, args);
  }

  /**
   * Executes this node as an integer.
   *
   * @return the integer return value of this node
   * @throws UnsupportedOperationException if the type of this node is not integer
   *
   * @since 1.0
   */
  public int execute_int(Object[] args) {
    int rtn = getFunctions()[0].execute_int(this, 0, args);
    cleanup();
    return rtn;
  }

  public int execute_int(int n, int child, Object[] args) {
    if (child == 0)
      return getFunctions()[n + 1].execute_int(this, n + 1, args);
    int other = getChild(n, child);
    return getFunctions()[other].execute_int(this, other, args);
  }

  /**
   * Executes this node as a long.
   *
   * @return the long return value of this node
   * @throws UnsupportedOperationException if the type of this node is not long
   *
   * @since 1.0
   */
  public long execute_long(Object[] args) {
    long rtn = getFunctions()[0].execute_long(this, 0, args);
    cleanup();
    return rtn;
  }

  public long execute_long(int n, int child, Object[] args) {
    if (child == 0)
      return getFunctions()[n + 1].execute_long(this, n + 1, args);
    int other = getChild(n, child);
    return getFunctions()[other].execute_long(this, other, args);
  }

  /**
   * Executes this node as a float.
   *
   * @return the float return value of this node
   * @throws UnsupportedOperationException if the type of this node is not float
   *
   * @since 1.0
   */
  public float execute_float(Object[] args) {
    float rtn = getFunctions()[0].execute_float(this, 0, args);
    cleanup();
    return rtn;
  }

  public float execute_float(int n, int child, Object[] args) {
    if (child == 0) {
      if (getFunctions()[n + 1] == null) { //KM
        return 0; //KM
      }
      return getFunctions()[n + 1].execute_float(this, n + 1, args);
    }
    int other = getChild(n, child);
    if (other < 0) { //KM
      return 0; //KM
    }
    return getFunctions()[other].execute_float(this, other, args);
  }

  /**
   * Executes this node as a double.
   *
   * @return the double return value of this node
   * @throws UnsupportedOperationException if this node's type is not double
   *
   * @since 1.0
   */
  public double execute_double(Object[] args) {
    double rtn = getFunctions()[0].execute_double(this, 0, args);
    cleanup();
    return rtn;
  }

  public double execute_double(int n, int child, Object[] args) {
    if (child == 0)
      return getFunctions()[n + 1].execute_double(this, n + 1, args);
    int other = getChild(n, child);
    return getFunctions()[other].execute_double(this, other, args);
  }

  /**
   * Executes this node as an object.
   *
   * @return the object return value of this node
   * @throws UnsupportedOperationException if the type of this node is not object
   *
   * @since 1.0
   */
  public Object execute_object(Object[] args) {
    Object rtn = getFunctions()[0].execute_object(this, 0, args);
    cleanup();
    return rtn;
  }

  public Object execute_object(int n, int child, Object[] args) {
    if (child == 0)
      return getFunctions()[n + 1].execute_object(this, n + 1, args);
    int other = getChild(n, child);
    return getFunctions()[other].execute_object(this, other, args);
  }

  /**
   * Executes this node without knowing its return type.
   *
   * @return the Object which wraps the return value of this node, or null
   * if the return type is null or unknown.
   *
   * @since 1.0
   */
  public Object execute(Object[] args) {
    return execute_object(args);
  }

  public Object execute(int n, int child, Object[] args) {
    return execute_object(n, child, args);
  }

  // AB HIER AUS JG.INDIVIDUAL (angepaßt an einzelnes Chromosome)
  /**
   * Initializes all chromosomes in this individual using the grow method.
   * <p>
   * @param types the type of each chromosome, must be an array of the same
   * length as the number of chromosomes
   * @param argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome.
   * @param nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes. Note
   * that it is not necessary to include the arguments of a chromosome as
   * terminals in the chromosome's node set. This is done automatically for you.
   *
   * @since 1.0
   */
  public void grow(int depth, Class[] types,
                   Class[][] a_argTypes, CommandGene[][] nodeSets) {
//    CommandGene.setIndividual(this);
    argTypes = a_argTypes[0];
    // If there are any ADF's in the nodeSet, then set its type
    // according to the chromosome it references
    for (int j = 0; j < nodeSets[0].length; j++) {
      /**@todo ADF impl und folgendes reaktivieren*/
//        if (nodeSets[0][j] instanceof ADF)
//          ( (ADF) nodeSets[i][j]).setReturnType(
//              types[ ( (ADF) nodeSets[0][j]).getChromosomeNum()]);
    }
    grow(0, depth, types[0], a_argTypes[0], nodeSets[0]);
  }

  /**
   * Initializes all chromosomes in this individual using the full method.
   * <p>
   * @param types the type of each chromosome, must be an array of the same length
   * as the number of chromosomes
   * @param argTypes the types of the arguments to each chromosome, must be an array
   * of arrays, the first dimension of which is the number of chromosomes and the
   * second dimension of which is the number of arguments to the chromosome.
   * @param nodeSets the nodes which are allowed to be used by each chromosome, must
   * be an array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of nodes. Note that it is not necessary
   * to include the arguments of a chromosome as terminals in the chromosome's node set.
   * This is done automatically for you.
   *
   * @since 1.0
   */
  public void full(int depth, Class[] types,
                   Class[][] a_argTypes, CommandGene[][] nodeSets) {
//    CommandGene.setIndividual(this);
    argTypes = a_argTypes[0];
    // If there are any ADF's in the nodeSet, then set its type
    // according to the chromosome it references
    for (int j = 0; j < nodeSets[0].length; j++) {
      /**@todo ADF impl und folgendes reaktivieren*/
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

  public int size() {
    int i = 0;
    while (i < getFunctions().length && getFunctions()[i] != null) {
      i++;
    }
    return i;
  }
}
