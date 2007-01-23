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

public class FileKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static String fileseparator = System.getProperty("file.separator");

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
      if (currentOffset <= a_offset) {
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
      // No trailing file separator
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

  public static String getConformPath(String path, boolean makeNice) {
    if (makeNice) {
      return getNiceURL(getConformPath(path), fileseparator);
    }
    else {
      return getConformPath(path);
    }
  }

  public static String getConformPath(String path) {
    if (fileseparator.equals("/")) {
      return removeDoubleSeparators(path.replace('\\', '/'));
    }
    else {
      return removeDoubleSeparators(path.replace('/', '\\'));
    }
  }

  /**
   * Removes duplicate separators from an URL.
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
   * Entfernt doppelte Separatoren (Slash oder Backslash)
   * @param dir Verzeichnisname (evtl. inklusive Dateiname)
   * @param separator Separator (Slash oder Backslash)
   * @return um doppelte Separatoren bereinigter Verzeichnisname
   */
  public static String removeDoubleSeparators(String dir) {
    int p;
    do {
      String sep;
      if (fileseparator.length()>1) {
        sep = fileseparator;
      }
      else {
        sep = fileseparator+fileseparator;
      }
      p = dir.lastIndexOf(sep);
      if (p >= 0) {
          dir = dir.substring(0,p)+dir.substring(p+1);
      }
      else {
        break;
      }
    }
    while (true);
    return dir;
  }

  public static boolean directoryExists(String a_dir) {
    File f = new File(a_dir);
    return f.exists();
  }
}
