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

/**
 * Data for WAN results.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class WANData {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private String m_uri;

  public WANData() {
  }

  public String getUri() {
    return m_uri;
  }

  public void setUri(String a_uri) {
    m_uri = a_uri;
  }

  public String toString() {
    return "URI: " + m_uri;
  }
}
