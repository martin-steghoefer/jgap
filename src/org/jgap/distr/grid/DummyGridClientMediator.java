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

import java.util.*;
import org.apache.log4j.*;
import org.homedns.dade.jcgrid.client.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.message.*;

/**
 * Mediates requests and responses either within a LAN or via internet (WAN).
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class DummyGridClientMediator
    implements IGridClientMediator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private transient Logger log = Logger.getLogger(getClass());

  protected GridNodeClientConfig m_gridconfig;

  /**
   * For LAN transmissions
   */
  private GridClient m_gc;

  /**
   *
   * @param a_gridconfig GridNodeClientConfig
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public DummyGridClientMediator(GridNodeClientConfig a_gridconfig)
      throws Exception {
    // Setup logging.
    // --------------
    MainCmd.setUpLog4J("DummyClientMediator", true);
    //
    m_gridconfig = a_gridconfig;
    m_gc = startClient();
    log.info("LAN mode selected");
  }

  /**
   * Starts the underlying grid client service.
   *
   * @return GridClient
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected GridClient startClient()
      throws Exception {
    GridClient gc = new GridClient();
    gc.setNodeConfig(m_gridconfig);
    /**@todo allow asynchronous wait for server (check for "java.net.ConnectException: Connection refused: connect")*/
    gc.start();
    return gc;
  }

  /**
   * Stops this service.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void stop()
      throws Exception {
    m_gc.stop();
  }

  /**
   *
   * @param a_msg GridMessage
   * @param a_context MessageContext
   * @param a_headerData Map
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void send(GridMessage a_msg, MessageContext a_context,
                   Map<String, String> a_headerData)
      throws Exception {
    m_gc.send(a_msg);
  }

  /**
   *
   * @param a_context MessageContext
   * @param a_datetime String
   * @param a_timeoutSeconds int
   * @param a_waitTimeSeconds int
   * @param a_removeRequest boolean
   * @return GridMessage
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public GridMessage getGridMessage(MessageContext a_context, String a_datetime,
                                    int a_timeoutSeconds, int a_waitTimeSeconds,
                                    boolean a_removeRequest)
      throws Exception {
    // This is asynchroneous
    /**@todo fallback solution*/
    m_gc.getGridMessageChannel();
    GridMessageWorkResult gmwr = (GridMessageWorkResult) m_gc.recv(
        a_timeoutSeconds * 1000);
    return gmwr;
  }

  /**
   *
   * @param a_context MessageContext
   * @param a_datetime String
   * @param a_pattern String
   * @return List
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public List listRequests(MessageContext a_context, String a_datetime,
                           String a_pattern)
      throws Exception {
    // Not implemented yet.
    // --------------------
    return null;
  }

  /**
   *
   * @param a_context MessageContext
   * @param a_datetime String
   * @param a_pattern String
   * @return List
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public List listResults(MessageContext a_context, String a_datetime,
                          String a_pattern)
      throws Exception {
    // Not implemented yet.
    // --------------------
    return null;
  }

  public void connect()
      throws Exception {
    // Not implemented yet.
    // --------------------
  }

  public void disconnect()
      throws Exception {
    // Not implemented yet.
    // --------------------
  }

  public void removeMessage(Object a_entry)
      throws Exception {
    // Not implemented yet.
    // --------------------
  }

  public void backupResult(Object a_result, String a_subDir, String a_title)
      throws Exception {
    // Not implemented yet.
    // --------------------
  }
}
