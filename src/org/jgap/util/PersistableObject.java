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

import java.io.*;

import org.jgap.distr.grid.gp.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.io.xml.*;
import org.jgap.distr.grid.wan.JGAPClientGPWAN;
import org.apache.log4j.Logger;

/**
 * A wrapper that allows an object to be written to and read from a file.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class PersistableObject {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private transient Logger log = Logger.getLogger(getClass());

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
    save(false);
  }
    public void save(boolean a_omitConfig)
        throws Exception {
    XStream xstream = new XStream();
    FileOutputStream fos = new FileOutputStream(m_file);
    if (a_omitConfig) {
      xstream.omitField(GPPopulation.class, "m_config");
      xstream.omitField(GPProgram.class, "m_conf");
      xstream.omitField(GPProgramBase.class, "m_conf");
      xstream.omitField(JGAPRequestGP.class, "m_config");
      xstream.omitField(CommandGene.class,"m_configuration");
    }
    FileWriter fw = new FileWriter(m_file);
    CompactWriter compact = new CompactWriter(fw);
    xstream.marshal(m_object, compact);
//    xstream.toXML(m_object);
    fos.close();
  }

  public Object load()
      throws Exception {
    return load(m_file);
  }

  public Object load(File a_file) {
    log.info("Loading database");
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