/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import java.io.*;
import java.util.*;

import org.homedns.dade.jcgrid.message.*;

/**
 * Interface for grid client mediators.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public interface IGridClientMediator
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  void send(GridMessage a_msg, String a_module, String a_context,
            String a_contextid, Map<String, String> a_headerData)
      throws Exception;

  GridMessage getGridMessage(String a_module, String a_context,
                             String a_contextid, int a_timeoutSeconds,
                             int a_waitTimeSeconds, boolean a_removeRequest)
      throws Exception;

  void stop()
      throws Exception;
}
