/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.*;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Abstract base class for all GP commands.
 *
 * A CommandGene represents a node within a GP program. A node either is a
 * terminal (like a constant), or a function (having input parameters).
 *
 * A CommandGene can hold additional CommandGene's, it acts sort of like a
 * * Composite (also see CompositeGene for a comparable concept, although for a
 * GA).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class CommandGene
    implements Comparable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.40 $";

  /**
   * Represents the delimiter that is used to separate fields in the
   * persistent representation.
   */
  final static String PERSISTENT_FIELD_DELIMITER = ":";

  final static String EXTENDED_INFO_DELIMITER = "~";

  /**
   * Delta, useful for comparing doubles and floats.
   */
  public static final double DELTA = 0.0000001;

  public final static Class BooleanClass = Boolean.class;

  public final static Class IntegerClass = Integer.class;

  public final static Class LongClass = Long.class;

  public final static Class FloatClass = Float.class;

  public final static Class DoubleClass = Double.class;

  public final static Class VoidClass = Void.class;

  public final static Class CharacterClass = Character.class;

  public enum COMMAND_TYPE {
    COMMAND_TYPE_UNDEFINED(0),
    COMMAND_TYPE_LOOP(8),
    COMMAND_TYPE_CONDITIONAL(16),
    COMMAND_TYPE_OPERATION(32),
    COMMAND_TYPE_MATH_OPERATION(33),
    COMMAND_TYPE_DECLARATION(64),
    COMMAND_TYPE_ASSIGNMENT(128),
    COMMAND_TYPE_ANALYSIS(256),
    COMMAND_TYPE_EXECUTION(512);

    private int m_value;

    public int intValue() {
      return m_value;
    }

    COMMAND_TYPE(int a_value) {
      m_value = a_value;
    }
  }
  private GPConfiguration m_configuration;

  /**
   * Should isValid() be called? True = no!
   */
  private boolean m_noValidation;

  /**
   * The return type of this node.
   */
  private Class m_returnType;

  /**
   * The arity of this node. Arity is the number of children of the node.
   * An arity if zero means: there are no children.
   * A terminal has an arity of zero.
   */
  private int m_arity;

  private int m_arityMin;

  private int m_arityMax;

  private boolean m_integerType;

  private boolean m_floatType;

  /** Energy of a gene, see RFE 1102206*/
  private double m_energy;

  /**
   * Application-specific data that is attached to the Gene. This data may
   * assist the application in labelling this Gene.
   * JGAP ignores the data, aside from allowing it to be set and
   * retrieved and considering it in clone and compareTo.
   *
   * @since 3.0
   */
  private Object m_applicationData;

  /**
   * Method compareTo and equals: Should we also consider the application data
   * when comparing? Default is "false" as "true" means a Gene's losing its
   * identity when application data is set differently!
   *
   * @since 3.0
   */
  private boolean m_compareAppData;

  private int m_subReturnType;

  private int[] m_subChildTypes;

  public int nodeIndex;

  /**
   * Initializations, called from each Constructor.
   */
  protected void init() {
  }

  /**
   * Default constructor, only for dynamic instantiation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public CommandGene()
      throws Exception {
  }

  /**
   *
   * @param a_conf the configuration to use
   * @param a_arity the number of children of the node
   * @param a_returnType type of the return value of the node
   * @throws InvalidConfigurationException
   */
  public CommandGene(final GPConfiguration a_conf, final int a_arity,
                     final Class a_returnType)
      throws InvalidConfigurationException {
    if (a_conf == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_configuration = a_conf;
    init();
    m_arity = a_arity;
    m_returnType = a_returnType;
    if (a_returnType == Integer.class
        || a_returnType == Long.class
        || a_returnType == CommandGene.IntegerClass
        || a_returnType == CommandGene.LongClass) {
      m_integerType = true;
    }
    else if (a_returnType == Double.class
             || a_returnType == Float.class
        || a_returnType == CommandGene.DoubleClass
   || a_returnType == CommandGene.FloatClass) {
      m_floatType = true;
    }
  }

  /**
   * Allows specifying a sub return type and sub child types.
   *
   * @param a_conf the configuration to use
   * @param a_arity the number of children of the node
   * @param a_returnType type of the return value of the node
   * @param a_subReturnType sub type of the return type, optional usage
   * @param a_childSubTypes sub types of the childs, optional usage
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CommandGene(final GPConfiguration a_conf, final int a_arity,
                     final Class a_returnType, final int a_subReturnType,
                     final int[] a_childSubTypes)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_returnType);
    if (a_childSubTypes != null) {
      boolean specialCase = false;
      // Special case from convenient construction.
      // ------------------------------------------
      if (a_childSubTypes.length == 1) {
        if (a_childSubTypes[0] == 0) {
          m_subChildTypes = null;
          specialCase = true;
        }
      }
      if (!specialCase) {
        if (a_childSubTypes.length != a_arity) {
          throw new IllegalArgumentException(
              "Length of child sub types must equal"
              + " the given arity (or set the former to null)");
        }
        else {
          m_subChildTypes = a_childSubTypes;
        }
      }
    }
    else {
      m_subChildTypes = null;
    }
    m_subReturnType = a_subReturnType;
  }

  /**
   * Allows specifying a sub return type.
   *
   * @param a_conf the configuration to use
   * @param a_arity the number of children of the node
   * @param a_returnType type of the return value of the node
   * @param a_subReturnType sub type of the return type, optional usage
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CommandGene(final GPConfiguration a_conf, final int a_arity,
                     final Class a_returnType, final int a_subReturnType)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_returnType, a_subReturnType, null);
  }

  /**
   * Command with one child: Allows specifying a sub return type and a sub child
   * type. Convenience version of the called constructor.
   *
   * @param a_conf the configuration to use
   * @param a_arity the number of children of the node
   * @param a_returnType type of the return value of the node
   * @param a_subReturnType sub type of the return type, optional usage
   * @param a_childSubType sub type of a child, optional usage
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public CommandGene(final GPConfiguration a_conf, final int a_arity,
                     final Class a_returnType, final int a_subReturnType,
                     final int a_childSubType)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_returnType, a_subReturnType,
         new int[] {a_childSubType});
  }

  public void setAllele(Object a_newValue) {
    throw new java.lang.UnsupportedOperationException(
        "Method setAllele() not used.");
  }

  public Object getAllele() {
    return null;
  }

  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    // Do nothing here by default.
    // ---------------------------
  }

  public void cleanup() {
    // Do nothing here by default.
    // ---------------------------
  }

  public int size() {
    return m_arity;
  }

  /**
   * Arity of the command. Override if necessary.
   * The arity is the number of children a node has.
   *
   * @param a_indvividual the invididual the command's arity may depend on (in
   * most cases the arity will not depend on the individual)
   * @return arity of the command
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getArity(IGPProgram a_indvividual) {
    return m_arity;
  }

  /**
   * Should only be used by class CommandDynamicArity or a similar class.
   *
   * @param a_arity the arity to set, overrides the current arity.
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  protected void setArity(int a_arity) {
    m_arity = a_arity;
  }

  /**
   * Adaptation of the arity so that it represents a value within the interval
   * [m_arityMin, m_arityMax].
   *
   * Override if necessary. See CommandDynamicArity for an implementation.
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void dynamizeArity() {
    // Nothing done here by default.
    // -----------------------------
  }

  /**
   * Should only be used by class CommandDynamicArity or a similar class.
   * @param a_arityMin the minimal arity possible
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  protected void setArityMin(int a_arityMin) {
    m_arityMin = a_arityMin;
  }

  /**
   * Should only be used by class CommandDynamicArity or a similar class.
   * @param a_arityMax the maximal arity possible
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  protected void setArityMax(int a_arityMax) {
    m_arityMax = a_arityMax;
  }

  protected int getArityMin() {
    return m_arityMin;
  }

  protected int getArityMax() {
    return m_arityMax;
  }

  /**
   * The compareTo-method. Considers application data when the configuration
   * asks for this.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    CommandGene other = (CommandGene) a_other;
    if (getClass() != other.getClass()) {
      /**@todo do it more precisely*/
      return -1;
    }
    else {
      CompareToBuilder comparator = new CompareToBuilder();
      comparator.append(size(), other.size())
          .append(m_subChildTypes, other.m_subChildTypes)
          .append(m_subReturnType, other.m_subReturnType);
      if (m_compareAppData) {
        comparator.append(m_applicationData, other.m_applicationData);
      }
      return comparator.toComparison();
    }
  }

  /**
   * The equals-method. Considers application data when the configuration
   * asks for this.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    if (a_other == null) {
      return false;
    }
    try {
      CommandGene other = (CommandGene) a_other;
      EqualsBuilder equals = new EqualsBuilder();
      equals.append(size(), other.size())
          .append(m_subChildTypes, other.m_subChildTypes)
          .append(m_subReturnType, other.m_subReturnType)
          .append(m_returnType, other.m_returnType);
      if (m_compareAppData) {
        equals.append(m_applicationData, other.m_applicationData);
      }
      return equals.isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }
  /**
   * @return the string representation of the command. Especially usefull to
   * output a resulting formula in human-readable form.
   */
  public abstract String toString();

  /**
   * Executes this node without knowing its return type.
   *
   * @param c the current Chromosome which is executing
   * @param n the index of the Function in the Chromosome's Function array which
   * is executing
   * @param args the arguments to the current Chromosome which is executing
   * @return the object which wraps the return value of this node, or null
   * if the return type is null or unknown
   * @throws UnsupportedOperationException if the type of this node is not
   * boolean
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object execute(ProgramChromosome c, int n, Object[] args) {
    if (m_returnType == BooleanClass) {
      return new Boolean(execute_boolean(c, n, args));
    }
    if (m_returnType == IntegerClass) {
      return new Integer(execute_int(c, n, args));
    }
    if (m_returnType == LongClass) {
      return new Long(execute_long(c, n, args));
    }
    if (m_returnType == FloatClass) {
      return new Float(execute_float(c, n, args));
    }
    if (m_returnType == DoubleClass) {
      return new Double(execute_double(c, n, args));
    }
    if (m_returnType == VoidClass) {
      execute_void(c, n, args);
    }
    else {
      return execute_object(c, n, args);
    }
    return null;
  }

  /**
   * @return the return type of this node
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Class getReturnType() {
    return m_returnType;
  }

  /**
   * Sets the return type of this node.
   *
   * @param a_type the type to set the return type to
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setReturnType(Class a_type) {
    m_returnType = a_type;
  }

  /**
   * Executes this node as a boolean. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return boolean");
  }

  /**
   * Executes this node, returning nothing. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return void");
  }

  /**
   * Executes this node as an integer. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return int");
  }

  /**
   * Executes this node as a long. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return long");
  }

  /**
   * Executes this node as a float. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return float");
  }

  /**
   * Executes this node as a double. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return double");
  }

  /**
   * Executes this node as an object. Override to implement.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    throw new UnsupportedOperationException(getName() +
        " cannot return Object");
  }

  public String getName() {
    return toString() + " (class " + getClass().getName() + ")";
  }

  /**
   * Gets the type of node allowed from the given child number. Should be
   * overridden in subclasses.
   *
   * @param a_ind the individual the child belongs to
   * @param a_chromNum the chromosome number
   * @return the type of node allowed for that child, or null of no child
   * exists
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (m_arity == 0) {
      return null;
    }
    else {
      return getReturnType();
    }
  }

  protected Object getInternalValue() {
    return null;
  }

  /**
   * Retrieves the hash code value for a CommandGene.
   * Override if another hashCode() implementation is necessary or more
   * appropriate than this default implementation.
   *
   * @return this Gene's hash code
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int hashCode() {
    // If our internal value is null, then return zero. Otherwise,
    // just return the hash code of the allele Object.
    // -----------------------------------------------------------
    if (getInternalValue() == null) {
      return getClass().getName().hashCode();
    }
    else {
      return getInternalValue().hashCode();
    }
  }

  public boolean isIntegerType() {
    return m_integerType;
  }

  public boolean isFloatType() {
    return m_floatType;
  }

  /**
   * @return true: command affects global state (i.e. stack or memory)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isAffectGlobalState() {
    return false;
  }

  /**
   * Subclasses capable of validating programs should overwrite this method.
   * See class Push as a sample.
   *
   * @param a_program the ProgramChromosome to validate
   * @return true: a_program is (superficially) valid with the current Command
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public boolean isValid(ProgramChromosome a_program, int a_index) {
    return true;
  }

  protected void check(ProgramChromosome a_program) {
    if (m_noValidation) {
      return;
    }
    if (!isValid(a_program)) {
      throw new IllegalStateException("State for GP-command not valid");
    }
  }

  protected void check(ProgramChromosome a_program, int a_index) {
    if (m_noValidation) {
      return;
    }
    if (!isValid(a_program, a_index)) {
      throw new IllegalStateException("State for GP-command not valid");
    }
  }

  public void setNoValidation(boolean a_noValidation) {
    m_noValidation = a_noValidation;
  }

  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPConfiguration getGPConfiguration() {
    return m_configuration;
  }

  /**
   * This sets the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for some applications
   * when it comes time to distinguish a Gene from another. JGAP ignores this
   * data functionally.
   *
   * @param a_newData the new application-specific data to attach to this
   * Gene
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setApplicationData(final Object a_newData) {
    m_applicationData = a_newData;
  }

  /**
   * Retrieves the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for
   * some applications when it comes time to distinguish a Gene from another.
   * JGAP ignores this data functionally.
   *
   * @return the application-specific data previously attached to this Gene,
   * or null if there is no data attached
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Gene is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true: consider application data in method compareTo
   * and equals
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setCompareApplicationData(final boolean a_doCompare) {
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
   * @return energy of the gene
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double getEnergy() {
    return m_energy;
  }

  /**
   * Sets the energy of the gene.
   *
   * @param a_energy the energy to set
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setEnergy(final double a_energy) {
    m_energy = a_energy;
  }

  /**
   * @return sub return type
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int getSubReturnType() {
    return m_subReturnType;
  }

  /**
   * @param a_childNum the index of the child
   * @return the sub type of the given child
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int getSubChildType(int a_childNum) {
    if (m_subChildTypes == null) {
      return 0;
    }
    else {
      return m_subChildTypes[a_childNum];
    }
  }

  /**
   * @return sub type for all children
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int[] getSubChildTypes() {
    return m_subChildTypes;
  }

  /**
   * Ensures that the calling command is unique within the program.
   * Call it on first place from the execute method.
   *
   * @param a_program the program to validate
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public void ensureUniqueness(ProgramChromosome a_program) {
    if (a_program.getCommandOfClass(1, getClass()) >= 0) {
      throw new IllegalStateException("Command "
                                      + getClass()
                                      + " must not occur more than once!");
    }
  }

  /**
   * Ensures that the calling command is unique within the program. Throws an
   * exception if uniqueness is violated.
   * Call it on first place from the execute method.
   *
   * @param a_program the program to validate
   * @param a_maxCount maximum number of allowed occurences
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void ensureUniqueness(ProgramChromosome a_program, int a_maxCount) {
    if (a_program.getCommandOfClass(1, getClass()) > a_maxCount) {
      throw new IllegalStateException("Command "
                                      + getClass()
                                      + " must not occur more than "
                                      + a_maxCount
                                      + " times!");
    }
  }

  /**
   * Ensures that the calling command is unique within the program. Returns
   * false if uniqueness is violated.
   * Call it on first place from the execute method.
   *
   * @param a_program the program to validate
   * @param a_maxCount maximum number of allowed occurences
   *
   * @return false: uniqueness constraint violated
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public boolean ensureUniqueness2(ProgramChromosome a_program, int a_maxCount) {
    if (a_program.getCommandOfClass(1, getClass()) > a_maxCount) {
      return false;
    }
    return true;
  }

  /**
   * The type of the command this gene represents. Overwrite in sub classes.
   * This is optional and allows fine-tuning of GP program creating. For
   * example, you could have a function as a part of a desired GP program that
   * only does some analysis stuff and not any execution. E.g. take a game like
   * Noughts and Crosses (Tic Tac Toe). There, a first part could be analysing
   * the current state of the board and a second part could cope with exploiting
   * the analysis results to execute a command (like put stone onto board at
   * specific position).
   *
   * @return type of the command this gene represents
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  protected CommandGene.COMMAND_TYPE getCommandType() {
    return COMMAND_TYPE.COMMAND_TYPE_UNDEFINED;
  }

  /**
   * @return the persistent representation of the chromosome, including all
   * genes
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public String getPersistentRepresentation() {
    // Return Type
    String s;
    if (m_returnType == null) {
      s = "null";
    }
    else {
      s = m_returnType.getClass().getName();
    }
    String result = PERSISTENT_FIELD_DELIMITER + m_arity
        + PERSISTENT_FIELD_DELIMITER + s
        + PERSISTENT_FIELD_DELIMITER + m_subReturnType
        + PERSISTENT_FIELD_DELIMITER + m_subChildTypes
        + EXTENDED_INFO_DELIMITER + getPersistentRepresentationExt()
        + EXTENDED_INFO_DELIMITER;
    return result;
  }

  /**
   * Override in your sub classes of CommandGene if you have to add additional
   * information to be persisted.
   *
   * @return additional infos
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  protected String getPersistentRepresentationExt() {
    return null;
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
    /**@todo finish*/
    /*
        if (a_representation != null) {
          StringTokenizer tokenizer =
              new StringTokenizer(a_representation,
                                  PERSISTENT_FIELD_DELIMITER);
          // Make sure the representation contains the correct number of
          // fields. If not, throw an exception.
          // -----------------------------------------------------------
          if (tokenizer.countTokens() < 4) {
            throw new UnsupportedRepresentationException(
                "The format of the given persistent representation "
     + " is not recognized: it does not contain at least four tokens: "
                + a_representation);
          }
          String arityRepresentation = tokenizer.nextToken();
          String subRetBoundRepresentation = tokenizer.nextToken();
          String subChildBoundRepresentation = tokenizer.nextToken();
          // First parse and set the representation of the value.
          // ----------------------------------------------------
          if (a_representation.equals("null")) {
            setAllele(null);
          }
          else {
            try {
              setAllele(new Integer(Integer.parseInt(a_representation)));
            } catch (NumberFormatException e) {
              throw new UnsupportedRepresentationException(
                  "The format of the given persistent representation " +
                  "is not recognized: field 1 does not appear to be " +
                  "an integer value.");
            }
          }
          // Now parse and set the lower bound.
          // ----------------------------------
          try {
            m_lowerBounds =
                Integer.parseInt(lowerBoundRepresentation);
          } catch (NumberFormatException e) {
            throw new UnsupportedRepresentationException(
                "The format of the given persistent representation " +
                "is not recognized: field 2 does not appear to be " +
                "an integer value.");
          }
          // Now parse and set the upper bound.
          // ----------------------------------
          try {
            m_upperBounds =
                Integer.parseInt(upperBoundRepresentation);
          } catch (NumberFormatException e) {
            throw new UnsupportedRepresentationException(
                "The format of the given persistent representation " +
                "is not recognized: field 3 does not appear to be " +
                "an integer value.");
          }
        }
     */
  }

  /**
   * Override in your sub classes of CommandGene if you have to add additional
   * information to be persisted.
   *
   * @param a_index index of the parameter in the range 0..n-1 (n=number of
   * parameters)
   * @param a_value string value of the parameter
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  protected void setValueFromString(int a_index, String a_value) {
  }
}
