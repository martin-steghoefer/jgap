/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.wan;

import java.util.*;

import org.jgap.distr.*;

/**
 * Meta-Information about a result.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class ResultInformation {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public MasterInfo workerInfo;

  private String m_id;

  private String m_title;

  private Date m_responseDate;

  private String m_description;

  public double m_fittest;

  private int m_popSize;

  /**
   * Duration of computation
   */
  private long m_durationComputation;

  public ResultInformation() {
  }

  public String getID() {
    return m_id;
  }

  public void setID(String a_id) {
    m_id = a_id;
  }

  /**
   * @param a_date date the result was computed
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void setResponseDate(Date a_date) {
    m_responseDate = a_date;
  }

  /**
   * @param the result was computed
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public Date getResponseDate() {
    return m_responseDate;
  }

  /**
   * @param a_descr arbitrary description of the request
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void setDescription(String a_descr) {
    m_description = a_descr;
  }

  /**
   * @return arbitrary description of the request
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getDescription() {
    return m_description;
  }

  public long getDurationComputation() {
    return m_durationComputation;
  }

  public void setDurationComputation(long a_durationComputation) {
    m_durationComputation = a_durationComputation;
  }

  /**
   * @param a_title the title to set
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void setTitle(String a_title) {
    m_title = a_title;
  }

  /**
   * @return the title set
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getTitle() {
    return m_title;
  }

  public int getPopSize() {
    return m_popSize;
  }

  public void setPopSize(int popSize) {
    m_popSize = popSize;
  }
}
