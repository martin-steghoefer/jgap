/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data.config;

import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 * This class is a Singleton that generates a properties file from
 * classes implementing IConfigInfo.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public final class ConfigWriter {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Singleton instance of ConfigWriter
   */
  private static ConfigWriter m_cWriter;

  // The configuration stored as Properties
  private Properties m_config;

  /**
   * Method to create and access the Singleton ConfigWriter instance.
   * @return a ConfigWriter Singleton instance.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public static ConfigWriter getInstance() {
    if (m_cWriter == null) {
      m_cWriter = new ConfigWriter();
    }
    return m_cWriter;
  }

  /**
   * Constructor for the ConfigWriter Singleton
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private ConfigWriter() {
    m_config = new Properties();
  }

  /**
   * Persist the configuration information as selected by the user.
   * @param a_cInfo configuration Information to persist
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void write(final IConfigInfo a_cInfo) {
    ConfigData cd = a_cInfo.getConfigData();
    String nsPrefix = cd.getNS() + ".";
    String name;
    List values;
    // construct name-value pairs from the information in the lists
    for (int i = 0; i < cd.getNumLists(); i++) {
      name = cd.getListNameAt(i);
      values = cd.getListValuesAt(i);
      int idx = 0;
      for (Iterator iter = values.iterator(); iter.hasNext(); idx++) {
        // append an index for same key elements
        String tmpName = name + "[" + idx + "]";
        tmpName = nsPrefix + tmpName;
        m_config.setProperty(tmpName, (String) iter.next());
      }
    }
    String value = "", tmpName = "";
    for (int i = 0; i < cd.getNumTexts(); i++) {
      name = cd.getTextNameAt(i);
      value = cd.getTextValueAt(i);
      tmpName = nsPrefix + name;
      m_config.setProperty(tmpName, value);
    }
    try {
      FileOutputStream out = new FileOutputStream(a_cInfo.getFileName());
      try {
        m_config.store(out, "---JGAP Configuration File---");
      } finally {
        out.close();
      }
    } catch (IOException ioEx) {
      JOptionPane.showMessageDialog(null,
                                    "Exception " + ioEx.getMessage(),
                                    "Configuration Exception",
                                    JOptionPane.INFORMATION_MESSAGE);
      ioEx.printStackTrace();
    }
  }
}
