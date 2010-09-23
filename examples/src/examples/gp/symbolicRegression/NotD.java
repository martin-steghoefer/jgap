/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.symbolicRegression;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * The boolean not operation.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class NotD
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public NotD(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.DoubleClass);
  }

  public String toString() {
    return "!&1";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "NotD";
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double d = c.execute_double(n, 0, args);
    boolean b = true;
    if (d < 1.0d) {
      b = false;
    }
    if (b) {
      return 0.0d; // not true: false
    }
    else {
      return 1.0d; // not false: true
    }
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      NotD result = new NotD(getGPConfiguration());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
