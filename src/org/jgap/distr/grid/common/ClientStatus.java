/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.common;

import java.util.*;

/**
 * Status information about the client.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class ClientStatus {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Last time a requests listing was executed.
   */
  private long m_lastListingRequestsMillis;

  /**
   * Last time a results listing was executed.
   */
  private long m_lastListingResultsMillis;

  private Map<String, Object> requests;

  private Map<String, String> results;

  private Map<String, List<Object>> topResults;

  public ClientStatus() {
  }

  public long getLastListingRequestsMillis() {
    return m_lastListingRequestsMillis;
  }

  public void setLastListingRequestsMillis(long lastListingRequestsMillis) {
    m_lastListingRequestsMillis = lastListingRequestsMillis;
  }

  public Map getRequests() {
    if (requests == null) {
      requests = new HashMap();
    }
    return requests;
  }

  public void setRequests(Map requests) {
    this.requests = requests;
  }

  public Map<String, String> getResults() {
    if (results == null) {
      results = new HashMap();
    }
    return results;
  }

  public void setResults(Map results) {
    this.results = results;
  }

  public Map getTopResults() {
    if (topResults == null) {
      topResults = new HashMap();
    }
    return topResults;
  }

  public long getLastListingResultsMillis() {
    return m_lastListingResultsMillis;
  }

  public void setTopResults(Map topResults) {
    this.topResults = topResults;
  }

  public void setLastListingResultsMillis(long lastListingResultsMillis) {
    m_lastListingResultsMillis = lastListingResultsMillis;
  }
}
