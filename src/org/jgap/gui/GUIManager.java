/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import java.util.*;

import javax.swing.*;

import org.jgap.*;
import org.jgap.data.config.*;

/**
 * Singleton GUIManager for the JGAP Configurator.
 * Creates a ConfigFrame on request, for a Configurable. Currently it only
 * creates a ConfigFrame for the root object, that is a Configuration itself.
 * In the future it will do the same for objects on the Root ConfigFrame that
 * are in turn Configurable.
 *
 * @author Siddhartha Azad
 * @author Klaus Meffert
 * @since 2.3
 */
public class GUIManager {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  // The root frame
  private ConfigFrame m_frame;

  // the children frames
  private List m_childFrames;

  // children configurables, one to one mapping with childFrames
  private List m_childCons;

  // The entity to configure
  private Configurable m_con;

  /**
   * Singleton instance of GUIManager
   */
  private static GUIManager m_gm;

  /**
   * @return a singleton GUIManager instance
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public static GUIManager getInstance() {
    if (m_gm == null) {
      m_gm = new GUIManager();
    }
    return m_gm;
  }

  /**
   * Constructor for the frame.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private GUIManager() {
    m_frame = null;
    m_childFrames = Collections.synchronizedList(new ArrayList());
    m_childCons = Collections.synchronizedList(new ArrayList());
  }

  /**
   * Create and show a new frame for a Configurable.
   * @param a_con configurable to use
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void showFrame(final ConfigFrame a_parent, final Configurable a_con)
      throws Exception {
    try {
      // TODO add edit-components for configurable properties

      // create the frame
      if (a_con.getClass() == Configuration.class) {
        m_frame = new ConfigFrame(null, "JGAP Configurator: "
                                  + "Configuration",
                                  true);
        m_con = a_con;
        m_frame.createAndShowGUI(a_con);
      }
      else {
        ConfigFrame tmpFrame =
            new ConfigFrame(a_parent, "JGAP Configurator: "
                            + "Unknown Title",
                            false);
        m_childCons.add(a_con);
        m_childFrames.add(tmpFrame);
        tmpFrame.createAndShowGUI(a_con);
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
                                    "Could not show configuration frame. This"
                                    + " attribute may not be configurable.",
                                    "Configuration Error",
                                    JOptionPane.INFORMATION_MESSAGE);
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
  public static void main(String[] args) {
    try {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          Configuration con = new Configuration();
          // parent is null for the root frame
          try {
            GUIManager.getInstance().showFrame(null, con);
          }
          catch (Exception ex) {
          }
        }
      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
