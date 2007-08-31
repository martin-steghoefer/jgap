/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.jgap.*;
import org.jgap.gp.impl.*;
import java.io.*;

/**
 * Interface specifying how to initialize a GPGenotype on behalf of the worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IGenotypeInitializerGP
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  GPGenotype setupGenotype(JGAPRequestGP a_req, GPPopulation a_initialPop)
      throws Exception;
}
