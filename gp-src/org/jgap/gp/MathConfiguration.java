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

import java.util.*;
import org.jgap.*;

/**
 * Configuration for GP-programs related to mathematical problems.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MathConfiguration
    extends GPConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public MathConfiguration()
      throws InvalidConfigurationException {
    super();
  }

}
