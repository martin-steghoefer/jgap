/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.util.*;
import java.io.*;
import com.thoughtworks.xstream.*;

/**
 * A wrapper that allows an object to be written to and read from a file.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class PersistableObject {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Object m_object;

  private File m_file;

  public PersistableObject(File a_file) {
    m_file = a_file;
  }

  public void setObject(Object a_object) {
    m_object = a_object;
  }

  public void save()
      throws Exception {
    XStream xstream = new XStream();
    FileOutputStream fos = new FileOutputStream(m_file);
    xstream.toXML(m_object, fos);
    fos.close();
  }

  public Object load()
      throws Exception {
    return load(m_file);
  }

  public Object load(File a_file) {
    XStream xstream = new XStream();
    try {
      FileInputStream fis = new FileInputStream(a_file);
      m_object = (Object) xstream.fromXML(fis);
      fis.close();
      return m_object;
    } catch (Exception ex) {
      return null;
    }
  }

  public Object getObject() {
    return m_object;
  }
}
