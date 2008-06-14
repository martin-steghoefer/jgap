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
 * Holds information about how a result was verified.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class ResultVerification {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private List<ResultData> m_resultData;

  public ResultVerification() {
    m_resultData = new Vector();
  }

  public void addResultData(ResultData a_data) {
    m_resultData.add(a_data);
  }

  public List getResultData() {
    return m_resultData;
  }

  public boolean isVerifiedByWorker(String a_workerGUID) {
    for(ResultData result:m_resultData) {
      if (result.getWorker().m_GUID.equals(a_workerGUID)) {
        return true;
      }
    }
    return false;
  }
}
