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
 * @author Siddhartha Azad.
 */
public class GUIManager {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Return a singleton GUIManager instance.
   * */
  public static GUIManager instance() {
    if (gm == null)
      gm = new GUIManager();
    return gm;
  }

  /**
   * Constructor for the frame
   */
  private GUIManager() {
    frame = null;
  }

  /**
   * Create and show a new frame for a Configurable.
   * */
  private void showFrame(Configurable _con) {
    try {
      // create the frame
      frame = new ConfigFrame("JGAP Configurator:" +
                              _con.getConfigurationHandler().getName());
      con = _con;
      frame.createAndShowGUI(_con.getConfigurationHandler());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Main method for the GUI
   */
  public static void main(String args[]) {
    try {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          Configuration con = new Configuration();
          GUIManager.instance().showFrame(con);
        }
      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
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
