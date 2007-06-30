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
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import org.apache.log4j.*;

public class ClassKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  private transient static Logger LOGGER = Logger.getLogger(ClassKit.class);

  public static void main(String[] args)
      throws Exception {
    File f = new File(".");
//    getPlugins("c:\\java\\jgap\\lib");
    getPlugins(f.getCanonicalPath() + "\\lib");
    if (true) {
      return;
    }
    List result = new Vector();
    result = find("org.jgap.INaturalSelector");
    for (int i = 0; i < result.size(); i++) {
      LOGGER.debug(result.get(i));
    }
//    URL url = ClassKit.class.getResource("/info/clearthought/layout/");
    URL url = ClassKit.class.getResource("/org/jgap/impl/");
    findInJar(result, url, Class.forName("java.io.Serializable"));
  }

  /**
   * Retrieves all the classes inheriting or implementing a given class in the
   * currently loaded packages.
   * @param a_tosubclassname the name of the class to inherit from
   * @return classes inheriting or implementing a given class in the
   * currently loaded packages
   * @see http//www.javaworld.com/javaworld/javatips/jw-javatip113.html
   */
  public static List find(final String a_tosubclassname) {
    try {
      List result = new Vector();
      Class tosubclass = Class.forName(a_tosubclassname);
      Package[] pcks = Package.getPackages();
      /**@todo take care of abstract classes --> introduce parameter for that*/
      for (int i = 0; i < pcks.length; i++) {
        List subresult = find(pcks[i].getName(), tosubclass);
        result.addAll(subresult);
      }
//        List subresult = find("org.jgap.impl", tosubclass);
//        result.addAll(subresult);
      return result;
    }
    catch (ClassNotFoundException ex) {
      LOGGER.warn("Class " + a_tosubclassname + " not found!");
      return null;
    }
  }

  /**
   * Display all the classes inheriting or implementing a given
   * class in a given package.
   *
   * @param a_pckname the fully qualified name of the package
   * @param a_tosubclassname the name of the class to inherit from
   * @return classes inheriting or implementing a given class in a given package
   * @throws ClassNotFoundException
   *
   * @see http//www.javaworld.com/javaworld/javatips/jw-javatip113.html
   */
  public static List find(final String a_pckname, final String a_tosubclassname)
      throws ClassNotFoundException {
    Class tosubclass = Class.forName(a_tosubclassname);
    return find(a_pckname, tosubclass);
  }

  /**
   * Display all the classes inheriting or implementing a given
   * class in a given package.
   * @param a_pckgname the fully qualified name of the package
   * @param a_tosubclass the Class object to inherit from
   * See http//www.javaworld.com/javaworld/javatips/jw-javatip113.html
   */
  public static List find(final String a_pckgname, final Class a_tosubclass) {
    List result = new Vector();
    // Translate the package name into an absolute path
    String name = a_pckgname;
    if (!name.startsWith("/")) {
      name = "/" + name;
    }
    name = name.replace('.', '/');
    // Get a File object for the package
    URL url = ClassKit.class.getResource(name);
//    URL url = tosubclass.getResource(name);
    // URL url = ClassLoader.getSystemClassLoader().getResource(name);

    // Happens only if the jar file is not well constructed, i.e.
    // if the directories do not appear alone in the jar file like here:
    //
    //          meta-inf/
    //          meta-inf/manifest.mf
    //          commands/                  <== IMPORTANT
    //          commands/Command.class
    //          commands/DoorClose.class
    //          commands/DoorOpen.class
    //          ClassKit.class
    //
    if (url == null) {
      return result;
    }
    return find(url, a_pckgname, a_tosubclass);
  }

  public static List find(final URL a_url, final String a_pckgname,
                          final Class a_tosubclass) {
    List result = new Vector();
    File directory = new File(a_url.getFile());
    if (directory.exists()) {
      // Get the list of the files contained in the package
      String[] files = directory.list();
      for (int i = 0; i < files.length; i++) {
        // we are only interested in .class files
        if (files[i].endsWith(".class")) {
          // removes the .class extension
          String classname = files[i].substring(0, files[i].length() - 6);
          try {
            // Try to create an instance of the object
            Class c = Class.forName(a_pckgname + "." + classname);
            if (implementsInterface(c, a_tosubclass)
                || extendsClass(c, a_tosubclass)) {
              result.add(a_pckgname + "." + classname);
            }
          }
          catch (ClassNotFoundException cnfex) {
            LOGGER.error(cnfex);
          }
//          catch (InstantiationException iex) {
//            // We try to instanciate an interface or an object that does not
//            // have a default constructor
//          }
//          catch (IllegalAccessException iaex) {
//            // The class is not public
//          }
        }
      }
    }
    else {
      findInJar(result, a_url, a_tosubclass);
    }
    return result;
  }

  public static void findInJar(final List a_result, final URL a_url,
                               Class a_tosubclass) {
    try {
      // It does not work with the filesystem: we must
      // be in the case of a package contained in a jar file.
      JarURLConnection conn = (JarURLConnection) a_url.openConnection();
      String starts = conn.getEntryName();
      JarFile jfile = conn.getJarFile();
      Enumeration e = jfile.entries();
      while (e.hasMoreElements()) {
        ZipEntry entry = (ZipEntry) e.nextElement();
        String entryname = entry.getName();
        if (entryname.startsWith(starts)
            && (entryname.lastIndexOf('/') <= starts.length())
            && entryname.endsWith(".class")) {
          String classname = entryname.substring(0, entryname.length() - 6);
          if (classname.startsWith("/")) {
            classname = classname.substring(1);
          }
          classname = classname.replace('/', '.');
          try {
            // Try to create an instance of the object
            Class c = Class.forName(classname);
            if (implementsInterface(c, a_tosubclass)
                || extendsClass(c, a_tosubclass)) {
//              Object o = Class.forName(classname).newInstance();
//              if (tosubclass.isInstance(o)) {
              a_result.add(classname);
            }
          }
          catch (ClassNotFoundException cnfex) {
            LOGGER.error(cnfex);
          }
        }
      }
    }
    catch (IOException ioex) {
      LOGGER.error(ioex);
    }
  }

  public static boolean implementsInterface(final Class a_o,
                                            final Class a_clazz) {
    Class[] interfaces = a_o.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      Class c = interfaces[i];
      if (c.equals(a_clazz)) {
        return true;
      }
    }
    return false;
  }

  public static boolean extendsClass(final Class a_o, final Class a_clazz) {
    if (a_clazz.getName().equals(a_o.getName())) {
      return false;
    }
    if (a_clazz.isAssignableFrom(a_o)) {
      return true;
    }
    else {
      return false;
    }
  }

  /**@todo add input param: type (or list of types) to look for*/
  public static void getPlugins(final String a_directory) {
    File modulePath = new File(a_directory);
//        if(modulePath == null || !modulePath.exists())modulePath.mkdirs();
    File[] jarFiles = modulePath.listFiles(new ExtensionsFilter("jar", false));
    URL[] urls = new URL[jarFiles.length + 1];
    int i = 0;
    for (; i < jarFiles.length; i++) {
      try {
        urls[i] = jarFiles[i].toURL();
      }
      catch (Exception ex) {
      }
    }
    try {
      urls[i] = modulePath.toURL();
    }
    catch (Exception ex) {
      ; //do nothing?
    }
    ClassLoader ucl = new URLClassLoader(urls);
    // -------------------------------
    Vector classes = new Vector();
    long startTime = System.currentTimeMillis();
    addClasses(classes, modulePath, "");
    LOGGER.info("Found plugin classes in: "
                       + (System.currentTimeMillis() - startTime)
                       + " milliseconds");
    // -------------------------------
//    Vector implementingClasses = new Vector();
    Enumeration e = classes.elements();
    while (e.hasMoreElements()) {
      try {
        String name = e.nextElement().toString();
        /**@todo check if class assignable from given type*/
        LOGGER.info("Found plugin class: " + name);
      }
      catch (Throwable ex) {
        ; // do nothing?
      }
    }
  }

  public static void addClasses(final Vector a_v, final File a_path,
                                final String a_name) {
    addClassesFile(a_v, a_path, a_name);
    addClassesJar(a_v, a_path);
  }

  public static void addClassesJar(final Vector a_v, final File a_path) {
    File[] files = a_path.listFiles(new ExtensionsFilter("jar", false));
    for (int i = 0; i < files.length; i++) {
      try {
        java.util.jar.JarFile jar = new java.util.jar.JarFile(files[i]);
        String wa;
        java.util.Enumeration e = jar.entries();
        while (e.hasMoreElements()) {
          wa = e.nextElement().toString();
          if (wa.endsWith(".class") && (wa.indexOf("$") == -1)) {
            a_v.add(wa.substring(0, wa.length() - 6).replace('/', '.'));
          }
        }
      }
      catch (Exception ex) {
        ; // do nothing?
      }
    }
  }

  //this method is recursive to go down sub-dirs
  public static void addClassesFile(Vector a_v, final File a_path,
                                    final String a_name) {
    File[] files = a_path.listFiles(new ExtensionsFilter("class", true));
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        addClassesFile(a_v, files[i], a_name + files[i].getName() + ".");
      }
      else if (files[i].getName().indexOf("$") == -1) {
        a_v.add(a_name
                + files[i].getName().
                substring(0, files[i].getName().length() - 6));
      }
    }
  }

  static class ExtensionsFilter
      implements FilenameFilter {
    private String m_ext;

    public ExtensionsFilter(final String a_extension, final boolean a_dummy) {
      m_ext = a_extension;
    }

    public boolean accept(final File a_dir, final String a_name) {
      return a_name != null && a_name.endsWith("." + m_ext);
    }
  }
}
