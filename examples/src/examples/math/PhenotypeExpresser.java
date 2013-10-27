/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math;

import org.jgap.*;

/**
 *
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public interface PhenotypeExpresser<T> {
  T express(IChromosome theChromosome);
}
