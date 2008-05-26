/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import java.util.*;

import org.homedns.dade.jcgrid.*;
import org.jgap.distr.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Holds the result of a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class JGAPResultGP
    extends WorkResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  private IGPProgram m_fittest;

  private GPPopulation m_pop;

  private MasterInfo m_workerInfo;

  /**
   * Arbitrary extra data
   */
  private Object m_genericData;

  private long m_unitDone;

  private int m_chunk;

  private String m_id;

  private String m_title;

  private Date m_responseDate;

  private String m_description;

  /**
   * Duration of computation
   */
  private long m_durationComputation;

  /**
   * Constructor: Takes the fittest program determined as result of a worker's
   * computation.
   *
   * @param a_sessionName arbitrary session name to distinct from other results
   * @param a_id ID of the result, should be unique
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_fittestProg the fittest program determined
   * @param a_unitdone number of units done
   * @deprecated use other constructor with GPPopulation parameter instead
   */
  public JGAPResultGP(String a_sessionName, String a_id, int a_chunk,
                      IGPProgram a_fittestProg, long a_unitdone) {
    super(a_sessionName, 0);
    m_fittest = a_fittestProg;
    m_unitDone = a_unitdone;
    m_chunk = a_chunk;
    m_id = a_id;
  }

  /**
   * Constructor: Takes a population as result of a worker's computation.
   *
   * @param a_sessionName arbitrary session name to distinct from other results
   * @param a_id ID of the result, should be unique
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_programs the result of a worker's computation
   * @param a_unitdone number of units done
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResultGP(String a_sessionName, String a_id, int a_chunk,
                      GPPopulation a_programs, long a_unitdone) {
    super(a_sessionName, 0);
    m_fittest = null;
    m_pop = a_programs;
    m_unitDone = a_unitdone;
    m_chunk = a_chunk;
    m_id = a_id;
  }

  /**
   * @return IGPProgram the fittest program known
   * @deprecated use getPopulation instead
   */
  public IGPProgram getFittest() {
    return m_fittest;
  }

  /**
   * @return the GPPopulation as a result from a worker's computation
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPPopulation getPopulation() {
    return m_pop;
  }

  public long getUnitDone() {
    return m_unitDone;
  }

  /**
   * Adds arbitrary generic data to the result.
   *
   * @param a_genericData arbitrary generic data
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setGenericData(Object a_genericData) {
    m_genericData = a_genericData;
  }

  /**
   * @return generic data contained in the result.
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public Object getGenericData() {
    return m_genericData;
  }

  public int getChunk() {
    return m_chunk;
  }

  /**
   * @return information about the worker
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public MasterInfo getWorkerInfo() {
    return m_workerInfo;
  }

  /**
   *
   * @param a_workerInfo set information about the worker
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setWorkerInfo(MasterInfo a_workerInfo) {
    m_workerInfo = a_workerInfo;
  }

  public String getID() {
    return m_id;
  }

  /**
   * @param a_date date the response was computed
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setResponseDate(Date a_date) {
    m_responseDate = a_date;
  }

  /**
   * @return the date the response was computed
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public Date getResponseDate() {
    return m_responseDate;
  }

  /**
   * @param a_descr arbitrary description of the result
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setDescription(String a_descr) {
    m_description = a_descr;
  }

  /**
   * @return arbitrary description of the result
   *
   * @author Klaus Meffert
   * @since 3.3.3
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

  /**
   * Clear the fittes program.
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void clearFittest() {
    m_fittest = null;
  }
}
