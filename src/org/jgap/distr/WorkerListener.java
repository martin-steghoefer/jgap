/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import java.io.*;

/**
 * Represents a listener for requests from workers to the master.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class WorkerListener {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Listens to a request.
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public abstract void listen()
      throws IOException;

  /**
   * Stops the listener.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public abstract void stop();
}
