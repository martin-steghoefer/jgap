/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.monalisa.gui;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class GeneticDrawingApp
    extends SingleFrameApplication {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    show(new GeneticDrawingView(this));
  }

  /**
   * This method is to initialize the specified window by injecting resources.
   * Windows shown in our application come fully initialized from the GUI
   * builder, so this additional configuration is not needed.
   */
  @Override
  protected void configureWindow(java.awt.Window root) {
  }

  /**
   * A convenient static getter for the application instance.
   * @return the instance of GeneticDrawingApp
   */
  public static GeneticDrawingApp getApplication() {
    return Application.getInstance(GeneticDrawingApp.class);
  }

  /**
   * Main method launching the application.
   */
  public static void main(String[] args) {
    launch(GeneticDrawingApp.class, args);
  }
}
