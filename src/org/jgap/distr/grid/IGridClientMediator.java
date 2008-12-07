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
  final static String CVS_REVISION = "$Revision: 1.8 $";

  void connect()
      throws Exception;

  void disconnect()
      throws Exception;

  void send(GridMessage a_msg, MessageContext a_context,
            Map<String, String> a_headerData)
      throws Exception;

  List listRequests(MessageContext a_context, String a_datetime,
                    String a_pattern)
      throws Exception;

  List listResults(MessageContext a_context, String a_datetime,
                   String a_pattern)
      throws Exception;

  GridMessage getGridMessage(MessageContext a_context, String a_datetime,
                             int a_timeoutSeconds, int a_waitTimeSeconds,
                             boolean a_removeRequest)
      throws Exception;

  void removeMessage(Object a_entry)
      throws Exception;

  void stop()
      throws Exception;

  /**
   * Backs up a result in the given sub directory.
   *
   * @param a_result the result to backup
   * @param a_subDir sub directory to store the copy of the result in
   * @param a_title the title of the entry to create
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  void backupResult(Object a_result, String a_subDir, String a_title)
      throws Exception;
}
