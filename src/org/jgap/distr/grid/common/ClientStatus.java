package org.jgap.distr.grid.common;

import java.util.*;

/**
 * Status information about the client.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class ClientStatus {
  /**
   * Last time a request listing was executed.
   */
  private long m_lastListingRequestsMillis;

  private Map<String, Object> requests;

  public ClientStatus() {
  }

  public long getLastListingRequestsMillis() {
    return m_lastListingRequestsMillis;
  }

  public void setLastListingRequestsMillis(long lastListingRequestsMillis) {
    m_lastListingRequestsMillis = lastListingRequestsMillis;
  }

  public Map getRequests() {
    return requests;
  }

  public void setRequests(Map requests) {
    this.requests = requests;
  }

}
