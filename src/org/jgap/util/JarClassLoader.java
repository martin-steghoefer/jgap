/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.jar.*;

/**
 * A class loader for loading jar files, both local and remote.
 *
 * @author Klaus Meffert (adapted from
 * http://java.sun.com/docs/books/tutorial/deployment/jar/apiindex.html)
 * @since 3.2
 */
public class JarClassLoader
    extends URLClassLoader {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private URL m_url;

  /**
   * Construction with an ordinary filename.
   *
   * @param a_filename ordinary filename
   * @throws MalformedURLException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JarClassLoader(String a_filename)
      throws MalformedURLException {
    this(new URL("jar:file:" + a_filename + "!/"));
  }

  /**
   * Creates a new JarClassLoader for the specified url.
   *
   * @param a_url the url of the jar file
   */
  public JarClassLoader(URL a_url) {
    super(new URL[] {a_url});
    m_url = a_url;
  }

  /**
   * @returns the name of the jar file main class, or null if no "Main-Class"
   * manifest attributes was defined
   *
   * @throws IOException
   *
   * @since 3.2
   */
  public String getMainClassName()
      throws IOException {
    URL u = new URL("jar", "", m_url + "!/");
    JarURLConnection uc = (JarURLConnection) u.openConnection();
    Attributes attr = uc.getMainAttributes();
    if (attr != null) {
      return attr.getValue(Attributes.Name.MAIN_CLASS);
    }
    else {
      return null;
    }
  }

  /**
   * Invokes the application in this jar file given the name of the
   * main class and an array of arguments. The class must define a
   * static method "main" which takes an array of String arguments
   * and is of return type "void".
   *
   * @param a_className the name of the main class
   * @param a_args the arguments for the application
   *
   * @throws ClassNotFoundException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   *
   * @since 3.2
   */
  public void invokeClass(String a_className, String[] a_args)
      throws ClassNotFoundException, NoSuchMethodException,
      InvocationTargetException {
    Class c = loadClass(a_className);
    Method m = c.getMethod("main", new Class[] {a_args.getClass()});
    m.setAccessible(true);
    int mods = m.getModifiers();
    if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
        !Modifier.isPublic(mods)) {
      throw new NoSuchMethodException("main");
    }
    try {
      m.invoke(null, new Object[] {a_args});
    } catch (IllegalAccessException e) {
      // This should not happen, as we have disabled access checks
    }
  }
}
