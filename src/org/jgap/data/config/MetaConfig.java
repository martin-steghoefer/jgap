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

/**
 * This class provides an interface to the configuration information to create
 * a JGAP Configuration GUI.
 *
 * @author Siddhartha Azad
 * @since 2.4
 *
 */
public class MetaConfig {
  // file to read the GUI Configuration information from
  private static final String METACON_FILENAME = "jgap-meta.con";

  private static final String CN = "MetaConfig";

  // singleton instance
  private static MetaConfig instance;

  // ClassName-ConfigProperty mapping
  private Hashtable m_metaMap = new Hashtable();

  // state for the parser
  private int m_state;

  private static final int INIT = 0;

  private static final int CLASS = 1;

  private static final int PROPERTY = 2;

  private static final int VALUES = 3;

  // class name currently being handled
  private String m_currName;

  private ConfigProperty m_currProperty;

  public static MetaConfig getInstance()
      throws MetaConfigException, IOException {
    if (null == instance) {
      instance = new MetaConfig();
    }
    return instance;
  }

  private MetaConfig()
      throws MetaConfigException, IOException {
    m_state = MetaConfig.INIT;
    init();
  }

  // public interface

  /**
   * Read the meta-config file and load it in memory.
   * @param className the name of the class of which the properties are
   * required
   * @return the list of properties for this class, if class is registered,
   * otherwise null
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public List getConfigProperty(String className) {
    return (List)m_metaMap.get(className);
  }

  /**
   * Read the meta-config file and load it in memory.
   * Having to read my own property file without using the Java Property
   * class since I need to preserve the order of these properties, plus
   * I have duplicate labels.
   * @throws MetaConfigException
   * @throws IOException
   *
   * @author Siddhartha Azad
   * @since 2.4
   */
  protected void init()
      throws MetaConfigException, IOException {
    Reader fr = getReader(METACON_FILENAME);
    LineNumberReader lr = new LineNumberReader(fr);
    String line = lr.readLine();
    while (line != null) {
      if (!this.isComment(line)) {
        parseLine(line);
      }
      line = lr.readLine();
    }
    endState();
    lr.close();
  }

  /**
   * Returns a reader to a file
   * @param a_filename the file to retrieve a reader for
   * @throws IOException
   * @return the Reader
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Reader getReader(String a_filename) throws IOException {
    File metaFile = new File(a_filename);
    FileReader fr = new FileReader(metaFile);
    return fr;
  }

  /**
   * Check whether a line is a comment. Any line starting with a '#' is a
   * comment.
   * @return true if a line is a comment, false if it is not
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private boolean isComment(String line) {
    String tmpLine = line.trim();
    StringBuffer sb = new StringBuffer(tmpLine);
    if (sb.charAt(0) == '#') {
      return true;
    }
    return false;
  }

  /**
   * Parse a line. This method is dispatches lines to other methods, hence
   * acting like a state machine.
   * @throws MetaConfigException
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private void parseLine(String a_line)
      throws MetaConfigException {
    String[] tokens = a_line.split("=");
    if (tokens == null || tokens.length != 2)
      throw new MetaConfigException(CN + ".parseLine():Exception while " +
                                    "parsing " + METACON_FILENAME + " line " +
                                    a_line + " is invalid");
    if (m_state == MetaConfig.INIT && tokens[0].equals("class")) {
      handleClass(tokens[1]);
    }
    else if (m_state == MetaConfig.CLASS && tokens[0].equals("property")) {
      handleProperty(tokens[1]);
    }
    else if (m_state == MetaConfig.PROPERTY && tokens[0].equals("values")) {
      handleValues(tokens[1]);
    }
    else if (m_state == MetaConfig.PROPERTY && tokens[0].equals("class")) {
      handleClass(tokens[1]);
    }
    else if (m_state == MetaConfig.VALUES && tokens[0].equals("class")) {
      handleClass(tokens[1]);
    }
    else if (m_state == MetaConfig.VALUES && tokens[0].equals("property")) {
      handleProperty(tokens[1]);
    }
    else {
      throw new MetaConfigException(CN + ".parseLine():Exception while "
                                    + "parsing " + METACON_FILENAME + " state "
                                    + m_state
                                    + " incompatible with line " + a_line);
    }
  }

  /**
   * Handle the state when a 'class' tag is found.
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private void handleClass(final String a_token) {
    m_state = MetaConfig.CLASS;
    if (m_currProperty != null) {
      add(m_currName, m_currProperty);
    }
    m_currProperty = new ConfigProperty();
    m_currName = a_token;
  }

  /**
   * Handle the state when a 'property' tag is found.
   * @throws MetaConfigException
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private void handleProperty(final String a_token)
      throws MetaConfigException {
    int prevState = m_state;
    if (prevState == MetaConfig.VALUES) {
      if (m_currProperty != null) {
        add(m_currName, m_currProperty);
      }
    }
    m_currProperty = new ConfigProperty();
    m_state = MetaConfig.PROPERTY;
    String[] tokens = a_token.split(",");
    if (tokens.length < 2 || tokens.length > 3) {
      throw new MetaConfigException("Invalid format of property line: " +
                                    a_token);
    }
    m_currProperty.setName(tokens[0].trim());
    m_currProperty.setWidget(tokens[1].trim());
    if (tokens.length == 3) {
      m_currProperty.setLabel(tokens[2]);
    }
  }

  /**
   * Handle the state when a 'values' tag is found.
   * @param a_token the rhs of the values property
   * @throws MetaConfigException
   *
   * @author Siddhartha Azad
   * @since 2.4
   *
   * */
  private void handleValues(final String a_token)
      throws MetaConfigException {
    m_state = MetaConfig.VALUES;
    String[] tokens = a_token.split(",");
    if (tokens.length == 0) {
      throw new MetaConfigException("Invalid format of property line: " +
                                    a_token);
    }
    for (int i = 0; i < tokens.length; i++) {
      m_currProperty.addValue(tokens[i].trim());
    }
  }

  /**
   * Called once the EOF is encountered while parsing the file.
   *
   * @throws MetaConfigException if parsing ends in an invalid state
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private void endState()
      throws MetaConfigException {
    if (m_state != MetaConfig.PROPERTY && m_state != MetaConfig.VALUES) {
      throw new MetaConfigException("Invalid format of JGAP MetaConfig "
                                    + "file: " + METACON_FILENAME
                                    + "Ending in Invalid state : "
                                    + m_state);
    }
    if (m_currProperty != null) {
      add(m_currName, m_currProperty);
    }
  }

  /**
   * Add a new ConfigProperty for a certain class to the hashtable of
   * properties.
   * @param currName name of the class to which the property belongs
   * @param a_cp the ConfigProperty to be added to the class
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  private void add(final String currName, ConfigProperty a_cp) {
    List props = (List) m_metaMap.get(currName);
    if (null == props) {
      props = Collections.synchronizedList(new ArrayList());
      m_metaMap.put(currName, props);
    }
    props.add(a_cp);
  }
}
