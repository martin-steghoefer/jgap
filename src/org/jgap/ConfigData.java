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
import javax.swing.*;


/**
 * Class to encapsulate information given by the GUI to a ConfigWriter to persist.
 * @author Siddhartha Azad.
 * */
public class ConfigData {
	
	
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
		 * @throws IndexOutOfBoundsException when index < 0 || 
		 * index >= listData.size()
		 * */
		public String getListNameAt(int index) {
			ListData ld = (ListData)listData.get(index);
			return ld.getName();
		}
		
		/**
		 * Get the contents of the list at the specified index.
		 * @param index Index of the list
		 * @throws IndexOutOfBoundsException when index < 0 || 
		 * index >= listData.size()
		 * */
		public ArrayList getListValuesAt(int index) {
			ListData ld = (ListData)listData.get(index);
			return ld.getListData();
			
		}
		
		/**
		 * Get the name of the text at the specified index.
		 * @param index Index of the text
		 * @throws IndexOutOfBoundsException when index < 0 || 
		 * index >= textData.size()
		 * */
		public String getTextNameAt(int index) {
			TextData td = (TextData)textData.get(index);
			return td.getName();
		}

		/**
		 * Get the value of the text at the specified index.
		 * @param index Index of the text
		 * @throws IndexOutOfBoundsException when index < 0 || 
		 * index >= textData.size()
		 * */
		public String getTextValueAt(int index) {
			TextData ld = (TextData)textData.get(index);
			return ld.getValue();
		}
		
		ArrayList listData;
		ArrayList textData;
	
	/**
	 * Data associated with the lists on the GUI.
	 * @author Siddhartha Azad.
	 * */
	class ListData {
		
		/**
		 * Constructor.
		 * @param _name Name of the object being configured, to be used as the 
		 * key in the config properties file.
		 * @param _data Data associated with the List (Data selected by the user)
		 */
		ListData(String _name, ArrayList _data) {
			data = _data;
			name = _name;
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
	 * @author Siddhartha Azad.
	 * */
	class TextData {
		
		/**
		 * Constructor.
		 * @param _name Name of the object being configured. Will be used as the
		 * key in the config properties file.
		 * @param _value The value in the JTextField (value in the properties file)
		 * */
		public TextData(String _name, String _value) {
			name = _name;
			value = _value;
		}
		
		public void setName(String _name) {
			name = _name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setValue(String _value) {
			value= _value;
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