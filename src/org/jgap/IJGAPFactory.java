/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;

/**
 * Interface for central factory, see JGAPFactory.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IJGAPFactory {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  void setParameters(Collection a_parameters);

  RandomGenerator createRandomGenerator();
}
