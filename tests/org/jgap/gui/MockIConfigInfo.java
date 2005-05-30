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
import java.util.*;

/**
 * A Mock implementation of IConfigInfo to test the proper generation of a
 * Config File.
 * @author Siddhartha Azad
 * @since 2.3
 * */
public class MockIConfigInfo
    implements IConfigInfo {
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * @author Siddhartha Azad
   * @since 2.3
   */
  public MockIConfigInfo() {
    conData = new ConfigData();
    // populate the ConfigData instance
    conData.setNS("Configurable");
    // add the TextField properties
    conData.addTextData("text1", "text1_value");
    conData.addTextData("text2", "text2_value");
    conData.addTextData("text3", "text3_value");
    // add the List properties
    ArrayList listData;
    String listName = "";
    for (int i = 0; i < 3; i++) {
      listName = "List" + (i + 1);
      listData = new ArrayList();
      listData.add(listName + "_value_1");
      listData.add(listName + "_value_2");
      listData.add(listName + "_value_3");
      conData.addListData(listName, listData);
    }
  }

  public ConfigData getConfigData() {
    return conData;
  }

  /**
   * Get the config file to write to.
   * @return The config file name to write to.
   * */
  public String getFileName() {
    // A temporary config file
    return "jgapTmp.con";
  }

  // The ConfigData instance that will be populated and returned
  private ConfigData conData;
}
