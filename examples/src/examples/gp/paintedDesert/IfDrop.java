/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * A two argument if-then function.  If the ant is carrying sand and the current
 * location does not contain sand, the sand is dropped and the first argument is
 * returned. Otherwise the second argument is returned.  Mimics the Lisp function
 * from Koza.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class IfDrop
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * The class of the arguments to this function
   */
  private Class m_type;

  /**
   * Constructor for the If Drop function
   * @param a_conf configuration to use
   * @param a_type
   * @throws InvalidConfigurationException
   */
  public IfDrop(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_type = a_type;
  }

  /**
   * Returns the program listing name
   */
  public String toString() {
    return "if Carrying and No Sand at Location then (&1) else(&2)";
  }

  /**
   * @return textual name of this command
   *
   */
  public String getName() {
    return "IfDrop)";
  }

  /**
   * Executes the IfDrop function for integer arguments
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    AntMap map = getMap(c);
    if (map.getAnt().isCarrying() && !map.getAnt().sandAtLocation(map)) {
      c.execute_void(n, 1, args);
    }
    else {
      c.execute_void(n, 1, args);
    }
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (a_chromNum == 0) {
      return m_type;
    }
    return CommandGene.VoidClass;
  }
}
