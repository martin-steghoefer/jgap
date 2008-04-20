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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private transient Logger log = Logger.getLogger(getClass());

  protected GridNodeClientConfig m_gridconfig;

  /**
   * For LAN transmissions
   */
  private GridClient m_gc;

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

  protected GridClient startClient()
      throws Exception {
    GridClient gc = new GridClient();
    gc.setNodeConfig(m_gridconfig);
    /**@todo allow asynchronous wait for server (check for "java.net.ConnectException: Connection refused: connect")*/
    gc.start();
    return gc;
  }

  public void stop()
      throws Exception {
    m_gc.stop();
  }

  public void send(GridMessage a_msg, MessageContext a_context,
                   Map<String, String> a_headerData)
      throws Exception {
    m_gc.send(a_msg);
  }

  public GridMessage getGridMessage(MessageContext a_context,
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
}
