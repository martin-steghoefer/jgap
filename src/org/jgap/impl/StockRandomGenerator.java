/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;

import org.jgap.*;

/**
 * The stock random generator uses the java.util.Random class to
 * provide a simple implementation of the RandomGenerator interface.
 * No actual code is provided here.
 *
 * @author Neil Rotstan
 * @since 1.0
 */
public class StockRandomGenerator
    extends Random
    implements RandomGenerator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";
}
