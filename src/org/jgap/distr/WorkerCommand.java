/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
     * or have a look at the top of class org.jgap.Chromosome which representatively
     * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.util.*;

/**
 * Command sent by an IMaster instance to an IWorker instance.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class WorkerCommand
    implements ICommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public WorkerCommand() {

  }

  /**@inheritedDoc*/
  public CommandResult execute(Object a_parameters)
      throws Exception {
    /**@todo implement*/
    return null;
  }
}