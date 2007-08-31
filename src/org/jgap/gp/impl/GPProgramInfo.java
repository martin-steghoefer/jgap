/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

/**
 * Holds information about a program. Used for caching GP programs during GP
 * evolution.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class GPProgramInfo {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private double m_fitnessValue;

  private String m_toStringNorm;

  private boolean m_found;

  public GPProgramInfo(GPProgram a_prog, boolean a_found) {
    m_fitnessValue = a_prog.getFitnessValueDirectly();
    m_toStringNorm = a_prog.toStringNorm(0);
    m_found = a_found;
  }

  public String getToStringNorm() {
    return m_toStringNorm;
  }

  public double getFitnessValue() {
    return m_fitnessValue;
  }

  public boolean isFound() {
    return m_found;
  }

  public void setFound(boolean a_found) {
    m_found = a_found;
  }

  public boolean equals(Object a_other) {
    GPProgramInfo other = (GPProgramInfo) a_other;
    if (m_toStringNorm == null) {
      if (other.m_toStringNorm == null) {
        return true;
      }
      return false;
    }
    return m_toStringNorm.equals(other.m_toStringNorm);
  }

  public int compareTo(Object a_other) {
    GPProgramInfo other = (GPProgramInfo) a_other;
    if (m_toStringNorm == null) {
      if (other.m_toStringNorm == null) {
        return 0;
      }
    }
    return m_toStringNorm.compareTo(other.m_toStringNorm);
  }
}
