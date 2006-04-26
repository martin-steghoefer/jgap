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
 * Abstract base class for all GP commands.
 * A CommandGene can hold additional CommandGene's, it acts sort of like a
 * Composite (also see CompositeGene for a smiliar characteristics, although
 * for a GA).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class CommandGene
    extends BaseGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  //jg
  public final static Class booleanClass = Boolean.class;

  public final static Class integerClass = Integer.class;

  public final static Class longClass = Long.class;

  public final static Class floatClass = Float.class;

  public final static Class doubleClass = Double.class;

  public final static Class voidClass = Void.class;

  /**
   * The individual currently being evaluated. This is here for nodes
   * (such as ADF) whose execution depends on the individual. It is
   * automatically set by the {@link World World} when it evaluates an
   * individual.
   */
  transient private static ProgramChromosome individual = null;

  /**
   * The return type of this node.
   *
   * @JG
   */
  private Class returnType;

  //JG
  private int m_arity;

  private ILanguage m_language;

  /**@todo how to represent a Command parameter?
   * --> Allel = parameter
   * --> Command = SingleCommand || Tree of Command's (CompositeCommand)
   * */
  /**@todo what is the allele in the CommandGene ?*/
  /**
   * Initializations, called from each Constructor
   */
  protected void init() {
  }

  public CommandGene(final Configuration a_conf, int a_arity,
                     Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf);
    init();
    m_arity = a_arity;
    returnType = a_returnType;
  }

  public void setAllele(Object a_newValue) {
    throw new java.lang.UnsupportedOperationException(
        "Method setAllele() not used.");
  }

  public Object getAllele() {
    /**@todo Implement this org.jgap.Gene method*/
    throw new java.lang.UnsupportedOperationException(
        "Method getAllele() not used.");
  }

  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    /**@todo Implement this org.jgap.Gene method*/
    throw new java.lang.UnsupportedOperationException(
        "Method getPersistentRepresentation() not yet implemented.");
  }

  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedOperationException, UnsupportedRepresentationException {
    /**@todo Implement this org.jgap.Gene method*/
    throw new java.lang.UnsupportedOperationException(
        "Method setValueFromPersistentRepresentation() not yet implemented.");
  }

  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    // do nothing for GP here!
  }

  public void cleanup() {
    /**@todo Implement this org.jgap.Gene method*/
  }

  public int size() {
    return m_arity;
  }

  public int getArity() {
    /**@todo entweder das oder size()*/
    return m_arity;
  }

  public void applyMutation(int index, double a_percentage) {
    /**@todo Implement this org.jgap.Gene method*/
    throw new java.lang.UnsupportedOperationException(
        "Method applyMutation() not yet implemented.");
  }

  public int compareTo(Object o) {
    CommandGene o2 = (CommandGene) o;
    if (size() != o2.size()) {
      if (size() > o2.size()) {
        return 1;
      }
      else {
        return -1;
      }
    }
    if (getLanguage() != o2.getLanguage()) {
      /**@todo do it more precisely*/
      return -1;
    }
    if (getClass() != o2.getClass()) {
      /**@todo do it more precisely*/
      return -1;
    }
    else {
      return 0;
    }
  }

  /**
   * Every command can have a different language.
   * @param a_language ther language to use
   */
  public void setLanguage(ILanguage a_language) {
    m_language = a_language;
  }

  public ILanguage getLanguage() {
    return m_language;
  }

  public abstract void evaluate(Configuration config, java.util.List parameters);

  public boolean equals(Object other) {
    return hashCode() == other.hashCode();
  }

  /**
   * @return the string representation of the command. Especially usefull to
   * output a resulting formula in human-readable form
   */
  public abstract String toString();

  //BEGIN JG
  /**
   * Executes this node without knowing its return type.
   *
   * @return the Object which wraps the return value of this node, or null
   * if the return type is null or unknown.
   *
   * @since 1.0
   */
  public Object execute(ProgramChromosome c, int n, Object[] args) {
    if (returnType == booleanClass)
      return new Boolean(execute_boolean(c, n, args));
    if (returnType == integerClass)
      return new Integer(execute_int(c, n, args));
    if (returnType == longClass)
      return new Long(execute_long(c, n, args));
    if (returnType == floatClass)
      return new Float(execute_float(c, n, args));
    if (returnType == doubleClass)
      return new Double(execute_double(c, n, args));
    if (returnType == voidClass)
      execute_void(c, n, args);
    else
      return execute_object(c, n, args);
    return null;
  }

  /**
   * Gets the return type of this node
   *
   * @return the return type of this node
   *
   * @since 1.0
   */
  public Class getReturnType() {
    return returnType;
  }

  /**
   * Sets the return type of this node
   *
   * @param type the type to set the return type to
   *
   * @since 1.0
   */
  public void setReturnType(Class type) {
    returnType = type;
  }

  /**
   * Executes this node as a boolean.
   *
   * @param c the current Chromosome which is executing
   * @param n the index of the Function in the Chromosome's Function array which is executing
   * @param args the arguments to the current Chromosome which is executing
   * @return the boolean return value of this node
   * @throws UnsupportedOperationException if the type of this node is not boolean
   *
   * @since 1.0
   */
  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return boolean");
  }

  /**
   * Executes this node, returning nothing.
   *
   * @throws UnsupportedOperationException if the type of this node is not void
   *
   * @since 1.0
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return void");
  }

  /**
   * Executes this node as an integer.
   *
   * @return the integer return value of this node
   * @throws UnsupportedOperationException if the type of this node is not integer
   *
   * @since 1.0
   */
  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return int");
  }

  /**
   * Executes this node as a long.
   *
   * @return the long return value of this node
   * @throws UnsupportedOperationException if the type of this node is not long
   *
   * @since 1.0
   */
  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return long");
  }

  /**
   * Executes this node as a float.
   *
   * @return the float return value of this node
   * @throws UnsupportedOperationException if the type of this node is not float
   *
   * @since 1.0
   */
  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return float");
  }

  /**
   * Executes this node as a double.
   *
   * @return the double return value of this node
   * @throws UnsupportedOperationException if the type of this node is not double
   *
   * @since 1.0
   */
  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return double");
  }

  /**
   * Executes this node as an object.
   *
   * @return the object return value of this node
   * @throws UnsupportedOperationException if the type of this node is not object
   *
   * @since 1.0
   */
  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
                                            " cannot return Object");
  }

  public String getName() {
    return toString();
  }

  /**
   * Gets the type of node allowed form the given child number. Must be
   * overridden in subclasses.
   *
   * @param i the child number
   * @return the type of node allowed for that child
   */
  public abstract Class getChildType(int i);

  /**
   * Sets the individual currently being evaluated.
   *
   * @param individual the individual currently being evaluated
   *
   * @since 1.0
   */
  public static void setIndividual(ProgramChromosome individual) {
    CommandGene.individual = individual;
  }

  public Object getInternalValue() {
    /**@todo is this correct?*/
    return null;
  }
}
