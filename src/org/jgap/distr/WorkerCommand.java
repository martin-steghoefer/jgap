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

import java.util.*;
import org.jgap.util.*;

/**
 * Command sent by an IMaster instance to an IWorker instance.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class WorkerCommand
    implements ICommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Informative name
   */
  private String m_name;

  /**
   * Time the command was created (i.e. the constructor was called) in
   * milliseconds. For statistical and purposes and for allowing to set a
   * timeout
   */
  private long m_timeCreated;

  public WorkerCommand(final String a_name) {
    m_name = a_name;
    m_timeCreated = getCurrentMilliseconds();
  }

  /**@inheritedDoc*/
  public CommandResult execute(final Object a_parameters)
      throws Exception {
    /**@todo implement*/
    return null;
  }

  private static long getCurrentMilliseconds() {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    return cal.getTimeInMillis();
  }

  public String getName() {
    return m_name;
  }

  public long getAgeMillis() {
    return getCurrentMilliseconds() - m_timeCreated;
  }
}
