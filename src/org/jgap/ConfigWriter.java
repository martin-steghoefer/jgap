/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * This class is a Singleton that generates a properties file from
 * classes implementing IConfigInfo.
 * @author Siddhartha Azad.
 * */
public class ConfigWriter {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Method to create and access the Singleton ConfigWriter instance.
   * @return A ConfigWriter Singleton instance.
   * @author Siddhartha Azad.
   * */
  public static ConfigWriter instance() {
    if (cWriter == null)
      cWriter = new ConfigWriter();
    return cWriter;
  }

  /**
   * Constructor for the ConfigWriter Singleton
   * @author Siddhartha Azad.
   */
  private ConfigWriter() {
    config = new Properties();
  }

  /**
   * Persist the configuration information as selected by the user.
   * @author Siddhartha Azad.
   * @param cInfo Configuration Information to persist.
   * */
  public void write(IConfigInfo cInfo) {
    try {
      ConfigData cd = cInfo.getConfigData();
      String name;
      ArrayList values;
      // construct name-value pairs from the information in the lists
      for (int i = 0; i < cd.getNumLists(); i++) {
        name = cd.getListNameAt(i);
        values = cd.getListValuesAt(i);
        int idx = 0;
        for (Iterator iter = values.iterator(); iter.hasNext(); idx++) {
          // append an index for same key elements
          String tmpName = name + "[" + idx + "]";
          config.setProperty(tmpName, (String) iter.next());
        }
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
                                    "Exception " + ex.getMessage(),
                                    "Configuration Information",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    try {
      FileOutputStream out = new FileOutputStream(cInfo.getFileName());
      config.store(out, "---JGAP Configuration File---");
      out.close();
    }
    catch (FileNotFoundException fileEx) {
      JOptionPane.showMessageDialog(null,
                                    "Exception " + fileEx.getMessage(),
                                    "Configuration Exception",
                                    JOptionPane.INFORMATION_MESSAGE);
      fileEx.printStackTrace();
    }
    catch (IOException ioEx) {
      JOptionPane.showMessageDialog(null,
                                    "Exception " + ioEx.getMessage(),
                                    "Configuration Exception",
                                    JOptionPane.INFORMATION_MESSAGE);
      ioEx.printStackTrace();
    }
  }

  /**
   * Singleton Instance of ConfigWriter
   */
  private static ConfigWriter cWriter;

  // The configuration stored as Properties
  private Properties config;
}
