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
import java.util.jar.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

/**
 * Contains helper functions related to the file system.
 *
 * @author Klaus Meffert
 */
public class FileKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  public static String fileseparator = System.getProperty("file.separator");

  /**
   * Copies a complete file.
   *
   * @param source source file name
   * @param dest destination file name or destination path (filename then taken
   * from source)
   * @throws FileNotFoundException
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void copyFile(String source, String dest)
      throws FileNotFoundException, IOException {
    copyFile(source, dest, 0);
  }

  /**
   * Copies a file.
   *
   * @param source source file name
   * @param dest destination file name or destination path (filename then taken
   * from source)
   * @param a_offset start offset of file to copy (0 is begin of file)
   * @throws FileNotFoundException
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void copyFile(String source, String dest, int a_offset)
      throws FileNotFoundException, IOException {
    if (getFilename(dest).length() == 0) {
      dest = dest + getFilename(source);
    }
    File inputFile = new File(source);
    File outputFile = new File(dest);
    FileInputStream in;
    FileOutputStream out;
    in = new FileInputStream(inputFile);
    out = new FileOutputStream(outputFile);
    int c;
    int currentOffset = 0;
    while ( (c = in.read()) != -1) {
      if (currentOffset >= a_offset) {
        out.write(c);
      }
      currentOffset++;
    }
    in.close();
    out.close();
  }

  public static String getFilename(String name_and_path) {
    return getFilename(name_and_path, fileseparator);
  }

  /**
   * Extract file name from a given path+filename.
   *
   * @param name_and_path input
   * @param fileseparator Slash or Backslash
   * @return extracted file nmame
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getFilename(String name_and_path,
                                   String fileseparator) {
    /**@todo possibly use File.getName() instead? */
    if (name_and_path == null) {
      return "";
    }
    String s = name_and_path;
//    s = common.strings.deQuote(s);
    if (fileseparator.equals("/")) {
      s = s.replace('\\', '/');
    }
    else {
      s = s.replace('/', '\\');
    }
    int p = s.lastIndexOf(fileseparator);
    if (p < 0) {
      // No trailing file separator.
      // ---------------------------
      return s;
    }
    else {
      s = s.substring(p + 1);
      if (s == null) {
        s = "";
      }
      return s;
    }
  }

  /**
   * @return current directory of the application
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getCurrentDir()
      throws IOException {
    File file = new File(".");
    return file.getCanonicalPath();
  }

  /**
   * Adds a subdir to a given dir and returns the resulting dir.
   *
   * @param dir the original dir
   * @param subDir the subdir to add
   * @param makeNice true: call getNiceDir afterwards
   * @return dir with subdir added
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String addSubDir(String dir, String subDir, boolean makeNice) {
    File f = new File(getConformPath(dir, true), subDir);
    String s = getConformPath(f.getAbsolutePath(), makeNice);
    return s;
  }

  public static String addFilename(String dir, String a_filename) {
    File f = new File(getConformPath(dir, false), a_filename);
    return f.getAbsolutePath();
  }

  public static String getConformPath(String path, boolean makeNice) {
    if (makeNice) {
      return getNiceURL(getConformPath(path), fileseparator);
    }
    else {
      return getConformPath(path);
    }
  }

  public static String getConformPath(String path) {
    return getConformPath(path, fileseparator);
  }

  public static String getConformPath(String path, String a_fileseparator) {
    String result = path;
    if (a_fileseparator.equals("/")) {
      result = removeDoubleSeparators(path.replace('\\', '/'));
    }
    else {
      result = removeDoubleSeparators(path.replace('/', '\\'));
    }
    if (!result.endsWith(a_fileseparator)) {
      result += a_fileseparator;
    }
    return result;
  }

  /**
   * Makes an URL nice by bringing it into a normalized (i.e. convenient) form.
   *
   * @param url any URL
   * @param separator the separator to remove duplicates of
   * @return prettified URL
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getNiceURL(String url, String separator) {
    if (url == null) {
      return null;
    }
    if (url.length() == 0) {
      return separator;
    }
    int p = url.lastIndexOf(separator);
    if (p < url.length() - 1) {
      return removeDoubleSeparators(url + separator);
    }
    else {
      return removeDoubleSeparators(url);
    }
  }

  /**
   * Removes duplicate separators from an URL.
   *
   * @param dir name of directory including file name
   * @return prettified URL
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String removeDoubleSeparators(String dir) {
    int p;
    do {
      String sep;
      if (fileseparator.length() > 1) {
        sep = fileseparator;
      }
      else {
        sep = fileseparator + fileseparator;
      }
      p = dir.lastIndexOf(sep);
      if (p >= 0) {
        dir = dir.substring(0, p) + dir.substring(p + 1);
      }
      else {
        break;
      }
    } while (true);
    return dir;
  }

  public static boolean directoryExists(String a_dir) {
    File f = new File(a_dir);
    return f.exists();
  }

  public static boolean existsFile(String a_filename) {
    File file = new File(getConformPath(a_filename));
    return file.exists();
  }

  /**
   * Deletes a file from disk.
   *
   * @param a_filename name of file to delete
   *
   * @return true if deletion successful
   */
  public static boolean deleteFile(String a_filename) {
    File file = new File(getConformPath(a_filename));
    return deleteFile(file);
  }

  /**
   * Deletes a file from disk.
   *
   * @param a_file name of file to delete
   *
   * @return true if deletion successful
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
 public static boolean deleteFile(File a_file) {
    if (a_file.exists()) {
      return a_file.delete();
    }
    else {
      return false;
    }
  }

  /**
   * Deletes a directory from disk, also if it is non-empty.
   *
   * @param a_dir name of file to delete
   *
   * @return true if deletion successful
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public static boolean deleteDirectory(File a_dir) {
    if (a_dir.isDirectory()) {
      String[] children = a_dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDirectory(new File(a_dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return a_dir.delete();
  }
  /**
   * Loads a jar file and returns a class loader to access the jar's classes.
   *
   * @param a_filename the full jar file name, e.g.:
   *      C:/jgap/lib/ext/ashcroft.jar
   * @return ClassLoader the class loader with which to access the loaded
   * classes, e.g. by <classloader>.loadClass(<classname including package>),
   * e.g.: cl.loadClass("com.thoughtworks.ashcroft.runtime.JohnAshcroft");
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static ClassLoader loadJar(String a_filename)
      throws Exception {
//    URL url = new URL("jar:file:" + a_filename);
    JarClassLoader cl = new JarClassLoader(a_filename);
    return cl;
  }

  /**
   * Retrieve the manifest included in the given jar.
   *
   * @param a_filename jar file name
   * @return Manifest included in the given jar
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static Manifest getManifestOfJar(String a_filename)
      throws Exception {
    URL url = new URL("jar:file:" + a_filename + "!/");
    JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
    Manifest manifest = jarConnection.getManifest();
    // Very important: Close the jar file!
    // -----------------------------------
    jarConnection.getJarFile().close();
    return manifest;
  }

  /**
   * @param a_JGAPManifest manifest with JGAP-specific information
   * @return version the jar file is working with (or version of the JGAP
   * library in case the file _is_ the JGAP library)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getJGAPVersion(Manifest a_JGAPManifest) {
    Attributes attr = a_JGAPManifest.getMainAttributes();
    return attr.getValue("JGAP-Version");
  }

  /**
   * @param a_JGAPManifest Manifest with JGAP-specific information
   * @return version of the module represented by the jar
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getModuleVersion(Manifest a_JGAPManifest) {
    Attributes attr = a_JGAPManifest.getMainAttributes();
    return attr.getValue("Module-Version");
  }

  /**
   * See getModuleVersion.
   *
   * @param a_filename name of a JGAP jar
   * @return version of the module represented by the jar
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getVersionOfModule(String a_filename)
      throws Exception {
    Manifest mf = getManifestOfJar(a_filename);
    String version = getModuleVersion(mf);
    if (version == null) {
      version = "no version info found!";
    }
    return version;
  }

  /**
   * See getJGAPVersion.
   *
   * @param a_filename name of a JGAP jar
   * @return version the jar file is working wit (or version of the JGAP library
   * in case the file *is* the JGAP library)
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String getVersionOfJGAP(String a_filename)
      throws Exception {
    Manifest mf = getManifestOfJar(a_filename);
    String version = getJGAPVersion(mf);
    if (version == null) {
      version = "no version info found!";
    }
    return version;
  }

  /**
   * Converts an ordinary file name into a jar filename that can be used with
   * JarClassLoader.
   *
   * @param a_filename the name to convert
   * @return converted name
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String toJarFileName(String a_filename) {
    String result = a_filename.replace('\\', '/');
//    if (!result.endsWith("!/")) {
//      result += "!/";
//    }
    return result;
  }

  /**
   * Creates a directory, and if necessary, any of its parent directories.
   *
   * @param a_dirname name of the dir to create
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void createDirectory(String a_dirname)
      throws IOException {
    File file = new File(a_dirname);
    if (file.exists()) {
      return;
    }
    if (!file.mkdirs()) {
      throw new IOException("Directory " + a_dirname +
                            " could not be created!");
    }
  }

  /**
   * Reads a text file.
   *
   * @param a_filename name of text file
   * @return list of read text lines
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static Vector readFile(String a_filename)
      throws IOException {
    Vector v = new Vector();
    String thisLine;
    FileInputStream fin = new FileInputStream(a_filename);
    BufferedReader myInput = new BufferedReader
        (new InputStreamReader(fin));
    while ( (thisLine = myInput.readLine()) != null) {
      v.add(thisLine);
    }
    return (v);
  }

  /**
   * @return temporary directory
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String getTempDir() {
    return System.getProperty("java.io.tmpdir");
  }

  /**
   * Returns the files within a directory that match a given pattern. No
   * directories are returned.
   *
   * @param a_dir String
   * @param a_mask String
   * @return String[]
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public static String[] listFilesInDir(String a_dir, String a_mask)
      throws Exception {
    File f = new File(a_dir);
    return f.list(new MyFileNameFilter(a_dir, a_mask));
  }

  static class MyFileNameFilter
      implements FilenameFilter {
    private String m_dir;

    private String m_mask;

    public MyFileNameFilter(String a_dir, String a_mask) {
      m_dir = a_dir;
      if (a_mask == null || a_mask.length() < 1) {
        m_mask =null;
      }
      else {
        m_mask = a_mask;
      }
    }

    public boolean accept(File dir, String name) {
      if (m_dir.equals(FileKit.getConformPath(dir.getPath()))) {
        if (m_mask == null) {
          return true;
        }
        Pattern p = Pattern.compile(m_mask);
        Matcher m = p.matcher(name);
        return m.matches();
      }
      return false;
    }
  }
}
