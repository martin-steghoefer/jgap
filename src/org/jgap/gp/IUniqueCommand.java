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

/**
 * Marker interface for GP commands that are allowed only once within a program.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public interface IUniqueCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";
}
