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

/**
 * A class loader for loading jar files, both local and remote.
 *
 * @author Klaus Meffert (adapted from
 * http://java.sun.com/docs/books/tutorial/deployment/jar/apiindex.html)
 * @since 3.2
 */
public class JarClassLoader
    extends MultiClassLoader {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private JarResources m_jarResources;

  public JarClassLoader(final String a_jarName) {
    // Create the JarResource and suck in the .jar file.
    m_jarResources = new JarResources(a_jarName);
  }

  protected byte[] loadClassBytes(final String a_className) {
    // Support the MultiClassLoader's class name munging facility.
    String className = formatClassName(a_className);
    // Attempt to get the class data from the JarResource.
    return (m_jarResources.getResource(className));
  }
}
