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
import java.util.*;
import java.util.jar.*;

/**
 * This class will (slightly inefficiently) look for all classes that implement
 * a particular interface.  It is useful for plugins.  This is done by looking
 * through the contents of all jar files in the classpath, as well as
 * performing a recursive search for *.class files in the classpath directories
 *
 * This particular class may not work in restrictive ClassLoader environments
 * such as Applets or WebStart.  (It may...but unlikely and untested.)
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class PluginDiscoverer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private static final boolean DEBUG = false;

  //list of folders in the classpath
  private List classpathFolders;

  //list of jars in the classpath
  private List classpathJars;

  /**
   * Reads the list of jars and classpath folders into instance variables
   * for later (cached) access.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public PluginDiscoverer() {
    String classpath = System.getProperty("java.class.path");
    StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
    classpathFolders = new Vector();
    classpathJars = new Vector();
    while (st.hasMoreTokens()) {
      String item = st.nextToken();
      File f = new File(item);
      if (item.toLowerCase().endsWith(".jar") && f.isFile()) {
        classpathJars.add(item);
      }
      else if (f.isDirectory()) {
        classpathFolders.add(item);
      }
    }
  }

  /**
   * Checks if a given class matches a given interface
   * @param interfaceClass The interface we are looking for
   * @param testClass class under test against the interface
   * @return corrected name of matched class, or null if not matching
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private String checkIfClassMatches(Class interfaceClass, String testClass) {
    // remove trailing dots
    if (testClass.toLowerCase().endsWith(".class")) {
      testClass = testClass.substring(0, testClass.length() - 6);
    }
    // replace slashes with dots
    testClass = testClass.replace('\\', '.').replace('/', '.');
    // remove leading dots
    while (testClass.startsWith(".")) {
      testClass = testClass.substring(1);
    }
    if (testClass.indexOf('$') != -1) {
      // don't handle inner/internal classes
      return null;
    }

    try {
      Class testClassObj = Class.forName(testClass, false,
                                         this.getClass().getClassLoader());
      if (interfaceClass.isAssignableFrom(testClassObj)) {
        return testClass;
      }
    }
    catch (UnsatisfiedLinkError ule) {
      if (DEBUG) {
        System.out.println("Unsatisfied link error for class: " + testClass);
      }
    }
    catch (IllegalAccessError e) {
      if (DEBUG) {
        System.out.println("Unable to load class: " + testClass);
      }
    }
    catch (ClassNotFoundException cnfe) {
      if (DEBUG) {
        System.out.println("Class not found" + testClass);
      }
    }
    catch (NoClassDefFoundError nex) {
      if (DEBUG) {
        System.out.println("No class definition found: " + testClass);
      }
    }
    return null;
  }

  /**
   * Finds all classes implementing the given interface
   * @param intrface the interface to check against
   * @return list of class names that implement the given interface
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List findImplementingClasses(Class intrface) {
    List result = new Vector();
    // Check the jar files
    for (Iterator i = classpathJars.iterator(); i.hasNext(); ) {
      String filename = (String) i.next();
      try {
        JarFile jar = new JarFile(filename);
        for (Enumeration item = jar.entries(); item.hasMoreElements(); ) {
          JarEntry entry = (JarEntry) item.nextElement();
          String name = entry.getName();
          if (name.toLowerCase().endsWith(".class")) {
            String classname = checkIfClassMatches(intrface, name);
            if (classname != null)
              result.add(classname);
          }
        }
      }
      catch (IOException e) {
        System.out.println("Unable to open jar " + filename);
      }
    }
    // Iterate over the classpath folders
    for (Iterator i = classpathFolders.iterator(); i.hasNext(); ) {
      String folder = (String) i.next();
      findImplementingClasses0(intrface, result, folder, "");
    }
    return result;
  }

  /**
   * Recursive helper method, searching a path recursively for class files
   * conforming to a given interface
   * @param intrface the interface we are looking for
   * @param result container for storing the results
   * @param base base directory
   * @param path current location in the traversal
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private void findImplementingClasses0(Class intrface, List result,
                                        String base, String path) {
    File f = new File(base + File.separator + path);
    if (!f.isDirectory()) {
      return;
    }
    File[] matches = f.listFiles(new classFilter());
    for (int i = 0; i < matches.length; i++) {
      String classname = path + File.separator + matches[i].getName();
      classname = checkIfClassMatches(intrface, classname);
      if (classname != null) {
        result.add(classname);
      }
    }
    matches = f.listFiles(new directoryFilter());
    for (int i = 0; i < matches.length; i++) {
      String folder = path + File.separator + matches[i].getName();
      findImplementingClasses0(intrface, result, base, folder);
    }
  }

  /**
   * Filter that only matches class files
   */
  public class classFilter
      implements FilenameFilter {
    public boolean accept(File dir, String name) {
      return (name != null && name.toLowerCase().endsWith(".class"));
    }
  }

  /**
   * Filter that only matches subdirectories
   */
  public class directoryFilter
      implements FilenameFilter {
    public boolean accept(File dir, String name) {
      return (dir != null &&
              new File(dir.getPath() + File.separator + name).isDirectory());
    }
  }

  /**
   * For testing purpose
   * @param argv not used
   * @throws Exception in case of any problem
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public static void main(String[] argv)
      throws Exception {
    PluginDiscoverer discoverer = new PluginDiscoverer();
    Class interfaceToLookFor = Class.forName("org.jgap.INaturalSelector");
    List plugins = discoverer.findImplementingClasses(interfaceToLookFor);
    System.out.println();
    int size = plugins.size();
    System.out.println("" + size + " plugin" +
                       (size == 1 ? "" : "s") + " discovered" +
                       (size == 0 ? "" : ":"));
    for (int i = 0; i < size; i++) {
      System.out.println(plugins.get(i));
    }

    System.exit(0);
  }
}