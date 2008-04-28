package org.jgap.distr.grid.common;

import org.jgap.distr.grid.gp.*;

/**
 * Exception when sending work requests. Stores the work requests, for which
 * sending failed.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class WorkRequestsSendException
    extends Exception {
  private JGAPRequestGP[] m_workRequests;

  public WorkRequestsSendException() {
    super();
  }

  public WorkRequestsSendException(Exception a_ex) {
    super(a_ex);
  }

  public WorkRequestsSendException(Exception a_ex,
                                   JGAPRequestGP[] a_workRequests) {
    super(a_ex);
    m_workRequests = a_workRequests;
  }

  public JGAPRequestGP[] getWorkRequests() {
    return m_workRequests;
  }
}
