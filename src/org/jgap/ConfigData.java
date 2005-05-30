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

/**
 * Class to encapsulate information given by the GUI to a ConfigWriter to
 * persist.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public class ConfigData {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public ConfigData() {
    listData = new ArrayList();
    textData = new ArrayList();
  }

  public void addListData(String name, ArrayList values) {
    ListData ld = new ListData(name, values);
    listData.add(ld);
  }

  public void addTextData(String name, String value) {
    TextData td = new TextData(name, value);
    textData.add(td);
  }

  public int getNumLists() {
    return listData.size();
  }

  public int getNumTexts() {
    return textData.size();
  }

  /**
   * Get the name of the list at the specified index.
   * @param index Index of the list
   * @return name of the list at the specified index
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= listData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getListNameAt(int index) {
    ListData ld = (ListData) listData.get(index);
    return ld.getName();
  }

  /**
   * Get the contents of the list at the specified index.
   * @param index Index of the list
   * @return contents of the list at the specified index.
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= listData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public ArrayList getListValuesAt(int index) {
    ListData ld = (ListData) listData.get(index);
    return ld.getListData();
  }

  /**
   * Get the name of the text at the specified index.
   * @param index Index of the text
   * @return name of the text at the specified index.
   * @throws IndexOutOfBoundsException when index < 0 or
   * index >= textData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getTextNameAt(int index) {
    TextData td = (TextData) textData.get(index);
    return td.getName();
  }

  /**
   * Get the value of the text at the specified index.
   * @param index index of the text
   * @return value of the text at the specified index
   * @throws IndexOutOfBoundsException when index < 0 ||
   * index >= textData.size()
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getTextValueAt(int index) {
    TextData ld = (TextData) textData.get(index);
    return ld.getValue();
  }

  /**
   * Set the namespace of the Configurable for which this ConfigData is being
   * used.
   * @param _ns The namespace of the Configurable to be used while writing the
   * config file.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void setNS(String _ns) {
    ns = _ns;
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
    return ns;
  }

  ArrayList listData;

  ArrayList textData;
  // The namespace for the properties file

  String ns;
  /**
   * Data associated with the lists on the GUI.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */

  class ListData {
    /**
     * Constructor.
     * @param a_name Name of the object being configured, to be used as the
     * key in the config properties file.
     * @param a_data Data associated with the List (Data selected by the user)
     */
    ListData(String a_name, ArrayList a_data) {
      data = a_data;
      name = a_name;
    }

    public Iterator getDataIter() {
      return data.iterator();
    }

    public ArrayList getListData() {
      return data;
    }

    public void setName(String _name) {
      name = _name;
    }

    public String getName() {
      return name;
    }

    // name of the object being configured
    private String name;

    // values selected for this object, to be written in the config file
    private ArrayList data;
  }
  /**
   * Data associated with the TextFields, on the GUI.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  class TextData {
    /**
     * Constructor.
     * @param _name Name of the object being configured. Will be used as the
     * key in the config properties file.
     * @param a_value The value in the JTextField (value in the properties file)
     * */
    public TextData(String a_name, String a_value) {
      name = a_name;
      value = a_value;
    }

    public void setName(String _name) {
      name = _name;
    }

    public String getName() {
      return name;
    }

    public void setValue(String _value) {
      value = _value;
    }

    public String getValue() {
      return value;
    }

    // name of the object being configured
    private String name;

    // value in the text field for this object
    private String value;
  }
}
