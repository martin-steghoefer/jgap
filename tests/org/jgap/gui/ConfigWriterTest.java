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

import junit.framework.*;
import junitx.util.*;

import java.util.*;
import java.io.*;
/**
 * Tests for ConfigWriter class
 * @author Siddhartha Azad
 */
public class ConfigWriterTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";


  public static Test suite() {
    TestSuite suite = new TestSuite(ConfigWriterTest.class);
    return suite;
  }

  /**
   * Get mock config data, use the ConfigWriter to write a property file
   * retrieve the written file, load it and check if the contents got
   * written correctly.
   * @author Siddhartha Azad.
   * */
  public void testConfigData_0() throws Exception {
      IConfigInfo ici = new MockIConfigInfo();
  	  ConfigWriter.instance().write(ici);
  	  // read the file and check the values
  	  int totalProps = 0;
  	  Properties props = new Properties();
  	  try {
  	  	  props.load(new FileInputStream("jgapTmp.con"));
  	  	  ConfigData cd = ici.getConfigData();
  	  	  String name;
  	  	  ArrayList values;
  	  	  for (int i = 0; i < cd.getNumLists(); i++) {
  	  	  	  name = cd.getListNameAt(i);
	  		  values = cd.getListValuesAt(i);
	  		  int idx = 0;
	  		  for (Iterator iter = values.iterator(); iter.hasNext(); idx++) {
	  		  	  // append an index for same key elements
	  		  	  String tmpName = name + "[" + idx + "]";
	  		  	  try {
	  		  	  	  assertEquals(props.getProperty(tmpName), (String)iter.next());	  
	  		  	  }	
	  		  	  catch(Exception ex) {
	  		  	  	  ex.printStackTrace();
	  		  	  	  fail();
	  		  	  }
	  		  	  totalProps++;
	  		  }
  		}
  		// test the TextField properties
  		String value = "";
  		for (int i = 0; i < cd.getNumTexts(); i++) {
  	      	name = cd.getTextNameAt(i);
  	      	value = cd.getTextValueAt(i);
  	      	try {
		  	    assertEquals(props.getProperty(name), value);
	        }
	        catch(Exception ex) {
	            ex.printStackTrace();
	            fail();
	        }
	        totalProps++;
  	    }
  		assertEquals(totalProps, props.size());
  	}
  	catch(IOException ioEx) {
  		ioEx.printStackTrace();
  		fail();
  	}
}

  

}
