/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.monalisa.core;

import java.awt.*;

/**
 * Application data for the Mona Lisa example. Holds the graphics object
 * as well as statistics information needed for fitness evaluation.
 *
 * @author Klaus Meffert
 * @since 3.4.1
 */
public class ApplicationData {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Graphics graphics;

  public int numPoints;

  public int numPolygons;

  /**
   * For dynamic instantiation.
   *
   * @author Klaus Meffert
   * @since 3.4.1
   *
   */
  public ApplicationData() {
  }
}
