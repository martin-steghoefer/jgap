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

/**
 * Class to encapsulate information given by the GUI to a ConfigWriter to
 * persist.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public class ConfigData {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private List m_listData;

  private List m_textData;

  // The namespace for the properties file.
  private String m_ns;

  public ConfigData() {
    m_listData = Collections.synchronizedList(new ArrayList());
    m_textData = Collections.synchronizedList(new ArrayList());
  }

  public void addListData(final String a_name, final List a_values) {
    ListData ld = new ListData(a_name, a_values);
    m_listData.add(ld);
  }

  public void addTextData(final String a_name, final String a_value) {
    TextData td = new TextData(a_name, a_value);
    m_textData.add(td);
  }

  public int getNumLists() {
    return m_listData.size();
  }

  public int getNumTexts() {
    return m_textData.size();
  }

  /**
   * Get the name of the list at the specified index.
   * @param a_index index of the list
   * @return name of the list at the specified index
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= listData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getListNameAt(final int a_index) {
    ListData ld = (ListData) m_listData.get(a_index);
    return ld.getName();
  }

  /**
   * Get the contents of the list at the specified index.
   * @param a_index index of the list
   * @return contents of the list at the specified index.
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= listData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public List getListValuesAt(final int a_index) {
    ListData ld = (ListData) m_listData.get(a_index);
    return ld.getListData();
  }

  /**
   * Get the name of the text at the specified index.
   * @param a_index index of the text
   * @return name of the text at the specified index.
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= textData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getTextNameAt(final int a_index) {
    TextData td = (TextData) m_textData.get(a_index);
    return td.getName();
  }

  /**
   * Get the value of the text at the specified index.
   * @param a_index index of the text
   * @return value of the text at the specified index
   * @throws IndexOutOfBoundsException when index < 0 ||
   * index >= textData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getTextValueAt(final int a_index) {
    TextData ld = (TextData) m_textData.get(a_index);
    return ld.getValue();
  }

  /**
   * Set the namespace of the Configurable for which this ConfigData is being
   * used.
   * @param a_ns namespace of the Configurable to be used while writing the
   * config file
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void setNS(final String a_ns) {
    m_ns = a_ns;
  }

  /**
   * Get the namespace of the Configurable for which this ConfigData is being
   * used.
   * @return The namespace of the Configurable to be used while writing the
   * config file.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getNS() {
    return m_ns;
  }

  /**
   * Data associated with the lists on the GUI.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */

  class ListData {
    // name of the object being configured
    private String m_name;

    // values selected for this object, to be written in the config file
    private List m_data;

    /**
     * Constructor.
     * @param a_name Name of the object being configured, to be used as the
     * key in the config properties file.
     * @param a_data Data associated with the List (Data selected by the user)
     */
    ListData(final String a_name, final List a_data) {
      m_data = a_data;
      m_name = a_name;
    }

    public List getListData() {
      return m_data;
    }

    public String getName() {
      return m_name;
    }
  }
  /**
   * Data associated with the TextFields, on the GUI.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  class TextData {
    // name of the object being configured
    private String m_textname;

    // value in the text field for this object
    private String m_textvalue;

    /**
     * Constructor.
     * @param a_name name of the object being configured. Will be used as the
     * key in the config properties file
     * @param a_value the value in the JTextField (value in the properties file)
     * */
    public TextData(final String a_name, final String a_value) {
      m_textname = a_name;
      m_textvalue = a_value;
    }

    public String getName() {
      return m_textname;
    }

    public String getValue() {
      return m_textvalue;
    }
  }
}
