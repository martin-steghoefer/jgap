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
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  private static final boolean DEBUG = false;

  //list of folders in the classpath
  private List m_classpathFolders;

  //list of jars in the classpath
  private List m_classpathJars;

  private String m_jarFile;

  /**
   * Reads the list of jars and classpath folders into instance variables
   * for later (cached) access.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public PluginDiscoverer() {
    init();
    String classpath = System.getProperty("java.class.path");
    StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
    while (st.hasMoreTokens()) {
      String item = st.nextToken();
      File f = new File(item);
      if (item.toLowerCase().endsWith(".jar") && f.isFile()) {
        m_classpathJars.add(item);
      }
      else if (f.isDirectory()) {
        m_classpathFolders.add(item);
      }
    }
  }

  /**
   * Prepares the discoverer for a single jar file
   * @param a_jarFile String
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public PluginDiscoverer(String a_jarFile) {
    init();
    m_jarFile = a_jarFile;
    m_classpathJars.add(m_jarFile);
  }

  private void init() {
    m_classpathFolders = new Vector();
    m_classpathJars = new Vector();
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
  private String checkIfClassMatches(String a_jarFilename, final Class a_interfaceClass,
                                     String a_testClass) {
    // remove trailing dots
    if (a_testClass.toLowerCase().endsWith(".class")) {
      a_testClass = a_testClass.substring(0, a_testClass.length() - 6);
    }
    // replace slashes with dots
    a_testClass = a_testClass.replace('\\', '.').replace('/', '.');
    // remove leading dots
    while (a_testClass.startsWith(".")) {
      a_testClass = a_testClass.substring(1);
    }
    if (a_testClass.indexOf('$') != -1) {
      // don't handle inner/internal classes
      return null;
    }
    try {
      ClassLoader cl;
      if (a_jarFilename == null) {
        cl = getClass().getClassLoader();
      }
      else {
        cl = new JarClassLoader(a_jarFilename);
      }
      Class testClassObj = Class.forName(a_testClass, false,
          cl);
      if (a_interfaceClass.isAssignableFrom(testClassObj)) {
        if (testClassObj.isInterface()) {
          // no interfaces wanted as result
          return null;
        }
        if ( (testClassObj.getModifiers() & java.lang.reflect.Modifier.ABSTRACT)
            > 0) {
          // no abstract classes wanted as result
          return null;
        }
        return a_testClass;
      }
    } catch (UnsatisfiedLinkError ule) {
      if (DEBUG) {
        System.out.println("Unsatisfied link error for class: " + a_testClass);
      }
    } catch (IllegalAccessError e) {
      if (DEBUG) {
        System.out.println("Unable to load class: " + a_testClass);
      }
    } catch (ClassNotFoundException cnfe) {
      if (DEBUG) {
        System.out.println("Class not found: " + a_testClass);
      }
    } catch (NoClassDefFoundError nex) {
      if (DEBUG) {
        System.out.println("No class definition found: " + a_testClass);
      }
    }
    return null;
  }

  /**
   * Finds all classes implementing the given interface
   * @param a_fullInterfaceName name of the interface (inclusive package name)
   * to find implementing classes (not abstract) for
   * @return list of class names that implement the given interface
   *
   * @throws ClassNotFoundException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public List findImplementingClasses(final String a_fullInterfaceName)
      throws ClassNotFoundException {
    Class interfaceToLookFor = Class.forName(a_fullInterfaceName);
    return findImplementingClasses(interfaceToLookFor);
  }

  /**
   * Finds all classes implementing the given interface
   * @param a_intrface the interface to check against
   * @return list of class names that implement the given interface
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List findImplementingClasses(final Class a_intrface) {
    List result = new Vector();
    // Check the jar files
    String s = null;
    try {
      // determine current directory
      File f = new File(".");
      s = f.getCanonicalPath();
      s = FileKit.getConformPath(s, true);
    } catch (IOException iex) {
      throw new RuntimeException("Unable to determine current directory",iex);
    }
    Iterator i = m_classpathJars.iterator();
    while (i.hasNext()) {
      String filename = (String) i.next();
      filename = FileKit.getConformPath(filename, true);
      // only search for jars in current dir or subdir (otherwise we would scan
      // the whole bunch of system and external library jars, too, and that
      // would be really inperformant)
      if (filename.startsWith(s)) {
        try {
          JarFile jar = new JarFile(filename);
          Enumeration item = jar.entries();
          while (item.hasMoreElements()) {
            JarEntry entry = (JarEntry) item.nextElement();
            String name = entry.getName();
            if (name.toLowerCase().endsWith(".class")) {
              String classname = checkIfClassMatches(filename, a_intrface, name);
              if (classname != null) {
                result.add(classname);
              }
            }
          }
        } catch (IOException e) {
          System.out.println("Unable to open jar " + filename);
        }
      }
    }
    // Iterate over the classpath folders
    i = m_classpathFolders.iterator();
    while (i.hasNext()) {
      String folder = (String) i.next();
      System.err.println(folder);
      findImplementingClasses0(a_intrface, result, folder, "");
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
  private void findImplementingClasses0(final Class a_intrface,
                                        final List a_result,
                                        final String a_base,
                                        final String a_path) {
    a_result.addAll(findImplementingClasses(a_intrface, a_base, a_path));
    File f = new File(a_base + File.separator + a_path);
    if (!f.isDirectory()) {
      return;
    }
//    File[] matches = f.listFiles(new ClassFilter());
//    for (int i = 0; i < matches.length; i++) {
//      String classname = a_path + File.separator + matches[i].getName();
//      classname = checkIfClassMatches(a_intrface, classname);
//      if (classname != null) {
//        a_result.add(classname);
//      }
//    }
    File[] matches = f.listFiles(new DirectoryFilter());
    for (int i = 0; i < matches.length; i++) {
      String folder = a_path + File.separator + matches[i].getName();
      findImplementingClasses0(a_intrface, a_result, a_base, folder);
    }
  }

  /**
   * Finds all classes implementing the given interface within a given
   * directory.
   *
   * @param a_intrface Class
   * @param a_base String
   * @param a_path String
   * @return List
   */
  public List findImplementingClasses(final Class a_intrface,
                                      final String a_base,
                                      final String a_path) {
    List result = new Vector();
    File f = new File(a_base + File.separator + a_path);
    if (!f.isDirectory()) {
      return result;
    }
    File[] matches = f.listFiles(new ClassFilter());
    for (int i = 0; i < matches.length; i++) {
      String classname = a_path + File.separator + matches[i].getName();
      classname = checkIfClassMatches(null, a_intrface, classname);
      if (classname != null) {
        result.add(classname);
      }
    }
    return result;
  }

  /**
   * Filter that only matches class files
   */
  public class ClassFilter
      implements FilenameFilter {
    public boolean accept(final File a_dir, final String a_name) {
      return (a_name != null && a_name.toLowerCase().endsWith(".class"));
    }
  }
  /**
   * Filter that only matches subdirectories
   */
  public class DirectoryFilter
      implements FilenameFilter {
    public boolean accept(final File a_dir, final String a_name) {
      return (a_dir != null
              && new File(a_dir.getPath()
                          + File.separator + a_name).isDirectory());
    }
  }
  /**
   * For testing purpose
   * @param args not used
   * @throws Exception in case of any problem
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public static void main(String[] args)
      throws Exception {
    PluginDiscoverer discoverer = new PluginDiscoverer();
//    Manifest mf = discoverer.getManifestOfJar("C:/temp/jgap/jgap.jar!/");
//    String version = discoverer.getJGAPVersion(mf);
    //
    List plugins = discoverer.findImplementingClasses(
        "org.jgap.INaturalSelector");
    System.out.println();
    int size = plugins.size();
    System.out.println("" + size + " plugin"
                       + (size == 1 ? "" : "s") + " discovered"
                       + (size == 0 ? "" : ":"));
    for (int i = 0; i < size; i++) {
      System.out.println(plugins.get(i));
    }
//    System.out.println("\n\n");
//    plugins = discoverer.findImplementingClasses(IGridConfiguration.class,
//        "c:/JavaProjekte/JGAP_CVS/classes", "examples/grid/fitnessDistributed");
//    System.out.println();
//    size = plugins.size();
//    System.out.println("" + size + " plugin"
//                       + (size == 1 ? "" : "s") + " discovered"
//                       + (size == 0 ? "" : ":"));
//    for (int i = 0; i < size; i++) {
//      System.out.println(plugins.get(i));
//    }
    System.exit(0);
  }
}
