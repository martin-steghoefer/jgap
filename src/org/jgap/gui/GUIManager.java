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

import org.jgap.*;

import javax.swing.*;
import java.util.ArrayList;

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
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
    childFrames = new ArrayList();
    childCons = new ArrayList();
  }

  /**
   * Create and show a new frame for a Configurable.
   * @param a_con configurable to use
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void showFrame(ConfigFrame parent, Configurable _con) throws Exception {
  	try {
      // create the frame
  	  if(_con.getConfigurationHandler().getName().equals("Configuration")) {
  	  	frame = new ConfigFrame(null, "JGAP Configurator:" +
  	  			_con.getConfigurationHandler().getName(), true);
  	  	con = _con;
  	  	frame.createAndShowGUI(_con.getConfigurationHandler());
  	  }
  	  else {
  	  	ConfigFrame tmpFrame = new ConfigFrame(parent, "JGAP Configurator:" +
  	  			_con.getConfigurationHandler().getName(), false);
  	  	childCons.add(_con);
  	  	childFrames.add(tmpFrame);
  	  	tmpFrame.createAndShowGUI(_con.getConfigurationHandler());
  	  }
    }
    catch (Exception ex) {
    	JOptionPane.showMessageDialog( null ,
				"Could not show configuration frame. This attribute may not be configurable.",
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
  public static void main(String args[]) {
    try {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          Configuration con = new Configuration();
          // parent is null for the root frame
          try{
          	GUIManager.instance().showFrame(null, con);
          }
          catch(Exception ex) {
          	
          }
        }
      });
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // The root frame
  private ConfigFrame frame;
  // the children frames
  private ArrayList childFrames;
  // children configurables, one to one mapping with childFrames
  private ArrayList childCons;
  // The entity to configure
  protected Configurable con;

  /**
   * Singleton Instance of GUIManager;
   */
  private static GUIManager gm;
}
