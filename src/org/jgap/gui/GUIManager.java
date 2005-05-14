/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import javax.swing.*;
import org.jgap.*;

/**
 * Singleton GUIManager for the JGAP Configurator.
 * Creates a ConfigFrame on request, for a Configurable. Currently it only
 * creates a ConfigFrame for the root object, that is a Configuration itself.
 * In the future it will do the same for objects on the Root ConfigFrame that
 * are in turn Configurable.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class GUIManager {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * @return a singleton GUIManager instance
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public static GUIManager instance() {
    if (gm == null)
      gm = new GUIManager();
    return gm;
  }

  /**
   * Constructor for the frame
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private GUIManager() {
    frame = null;
  }

  /**
   * Create and show a new frame for a Configurable.
   * @param a_con configurable to use
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private void showFrame(Configurable a_con) {
    try {
      // create the frame
      frame = new ConfigFrame("JGAP Configurator:" +
                              a_con.getConfigurationHandler().getName());
      con = a_con;
      frame.createAndShowGUI(a_con.getConfigurationHandler());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Main method for the GUI
   * @param args not used
   * @throws Exception
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public static void main(String args[])
      throws Exception {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Configuration con = new Configuration();
        GUIManager.instance().showFrame(con);
      }
    });
  }

  // The root frame
  private ConfigFrame frame;

  // The entity to configure
  protected Configurable con;

  /**
   * Singleton Instance of GUIManager;
   */
  private static GUIManager gm;
}
