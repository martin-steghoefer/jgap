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

import org.jgap.data.config.*;

/**
 * A Mock implementation of IConfigInfo to test the proper generation of a
 * Config File.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public class MockConfigInfoForTesting
    implements IConfigInfo {
  final static String CVS_REVISION = "$Revision: 1.2 $";

  // The ConfigData instance that will be populated and returned
  private ConfigData m_conData;

  /**
   * @author Siddhartha Azad
   * @since 2.3
   */
  public MockConfigInfoForTesting() {
    m_conData = new ConfigData();
    // populate the ConfigData instance
    m_conData.setNS("Configurable");
    // add the TextField properties
    m_conData.addTextData("text1", "text1_value");
    m_conData.addTextData("text2", "text2_value");
    m_conData.addTextData("text3", "text3_value");
    // add the List properties
    List listData;
    String listName = "";
    for (int i = 0; i < 3; i++) {
      listName = "List" + (i + 1);
      listData = new ArrayList();
      listData.add(listName + "_value_1");
      listData.add(listName + "_value_2");
      listData.add(listName + "_value_3");
      m_conData.addListData(listName, listData);
    }
  }

  public ConfigData getConfigData() {
    return m_conData;
  }

  /**
   * Get the config file to write to.
   * @return the config file name to write to
   * */
  public String getFileName() {
    // A temporary config file
    return "jgapTmp.con";
  }

}
