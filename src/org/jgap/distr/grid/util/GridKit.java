/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.util;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jgap.distr.grid.gp.*;
import org.jgap.distr.grid.request.*;
import org.jgap.gp.*;
import org.jgap.util.*;

/**
 * Utility functions related to distributed/grid computing.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class GridKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static String ensureDirectory(String a_currentDir, String a_subDir,
                                       String a_descr)
      throws IOException {
    if (a_currentDir == null || a_currentDir.length() < 1) {
      String currentDir = FileKit.getCurrentDir();
      // Set workdir, create it if it does not exist.
      // --------------------------------------------
      String workDir = FileKit.addSubDir(currentDir, a_subDir, true);
      File f = new File(workDir);
      if (!f.exists()) {
        if (!f.mkdirs()) {
          throw new RuntimeException("Creation of "
                                     + a_descr + " "
                                     + workDir
                                     + " failed!");
        }
      }
      return workDir;
    }
    else {
      return null;
    }
  }

  public static URLConnection getConnection(String a_url)
      throws Exception {
    URL url1 = new URL(a_url);
    URL url = new URL(url1.toExternalForm());
    URLConnection con = url.openConnection();
    con.setUseCaches(false);
    con.setDoInput(true);
    con.setDoOutput(true);
    return con;
  }

  public static VersionInfo isUpdateAvailable(String BASE_URL,
      String a_moduleName, String currentVersion)
      throws Exception {
    VersionInfo result;
    /**@todo download a versions.txt file and obtain version per project*/
    String url = BASE_URL + "getVersion=" + a_moduleName;
    addURLParameter(url, "version", currentVersion);
    URLConnection con = GridKit.getConnection(url);
    ObjectInputStream ois = new ObjectInputStream(con.getInputStream());
    result = (VersionInfo) ois.readObject();
    return result;
  }

  public static String addURLParameter(String a_requestURL, String a_key,
                                       long a_value) {
    String result = a_requestURL + "&" + a_key + "=" + a_value;
    return result;
  }

  public static String addURLParameter(String a_requestURL, String a_key,
                                       String a_value) {
    String result = a_requestURL + "&" + a_key + "=" + a_value;
    return result;
  }

  public static String retrieveModule(String BASE_URL,
                                      VersionInfo a_versionInfo,
                                      String a_destDir)
      throws Exception {
    String filename = a_versionInfo.filenameOfLib;
    if (!getFile(BASE_URL, filename, a_destDir)) {
      return null;
    }
    return filename;
  }

  public static void updateModule(String a_filename, String a_workDir,
                                  String a_libDir)
      throws Exception {
    String libDir = a_libDir;
    // Copy module to new location.
    // ----------------------------
    String sourceFileName = a_workDir + a_filename;
    FileKit.copyFile(sourceFileName, libDir);
    // Delete file in workdir.
    // -----------------------
    if (!FileKit.deleteFile(sourceFileName)) {
      /**@todo write log: delete manually*/
    }
  }

  public static boolean getFile(String BASE_URL, String a_sourceFilename,
                                String a_targetDir)
      throws Exception {
    String filename = FileKit.getFilename(a_sourceFilename);
    String destFilename = a_targetDir + filename;
    long offset;
    // If file already exists, determine start offset
    File f = new File(destFilename);
    if (f.exists()) {
      offset = f.length();
    }
    else {
      offset = 0;
    }
    String a_url = BASE_URL + "download=";
    String requestURL = a_url + a_sourceFilename;
    // Include start offset into request
    requestURL = GridKit.addURLParameter(requestURL, "offset", offset);
    URL url1 = new URL(requestURL);
    URL url = new URL(url1.toExternalForm());
    // Open connection
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    // Configure connection
    con.setUseCaches(false);
//    con.setDoInput(true);
    con.setDoOutput(true);
    // Receive result
    boolean append;
    if (offset == 0) {
      append = false;
    }
    else {
      append = true;
    }
    InputStream in = con.getInputStream();
    ObjectInputStream sis = new ObjectInputStream(new BufferedInputStream(in,
        1024 * 5));
    Status s;
//    BufferedInputStream ois = new BufferedInputStream(sis, 1024 * 5);
    FileOutputStream fos = new FileOutputStream(destFilename, append);
    long currentOffset = 0;
    try {
      int loopIndex = 0;
      do {
        s = (Status) sis.readObject();
        if (s.code == 0) {
          if (loopIndex == 0) {
            if (s.buffer == null || s.buffer.length < 1) {
              System.out.println("File already exists");
              return true;
            }
          }
        }
        else if (s.code < 0) {
          throw new IOException(s.description);
        }
        loopIndex++;
        fos.write(s.buffer);
        currentOffset += s.buffer.length;
        if (s.code == 0) {
          break;
        }
//        Thread.sleep(50);
      } while (true);
    } catch (SocketException sex) {
      System.err.println("Connection to server lost"
                         + " - file transfer interrupted (resum possible)");
      sex.printStackTrace();
      // Close file being able to resume it later
      fos.close();
      return false;
    }
    fos.close();
    System.out.println("File received: " + destFilename);
    return true;
  }

  public static void updateModuleLibrary(String BASE_URL, String a_moduleName,
      String a_libDir, String a_workDir)
      throws Exception {
    String currentVersion;
    String JGAPVersionNeeded;
    String filename;
    boolean isCoreModule;
    if (a_moduleName.equalsIgnoreCase("evolutionDistributed")) {
      /**@todo zuordnung irgendwo abspeichern*/
      filename = "evdistr.jar";
      isCoreModule = false;
    }
    else if (a_moduleName.equalsIgnoreCase("jgap")) {
      /**@todo zuordnung irgendwo abspeichern*/
      filename = "jgap.jar";
      isCoreModule = true;
    }
    else if (a_moduleName.equalsIgnoreCase("rjgrid")) {
      /**@todo zuordnung irgendwo abspeichern*/
      filename = "rjgrid.zip";
      isCoreModule = true;
    }
    else {
      throw new IllegalArgumentException("Unknown module " + a_moduleName);
    }
    filename = a_libDir + filename;
    // Determine if file available locally
    if (!FileKit.existsFile(filename)) {
      currentVersion = "none";
      System.out.println(" File not found: " + filename);
    }
    else {
      // If file exists: determine version of module
      if (isCoreModule) {
        currentVersion = FileKit.getVersionOfJGAP(filename);
      }
      else {
        currentVersion = FileKit.getVersionOfModule(filename);
        // Also determine minimal version of JGAP library that is
        // required for the module to work.
        // -------------------------------------------------------
        JGAPVersionNeeded = FileKit.getVersionOfJGAP(filename);
      }
    }
    // ask server if update required
    VersionInfo versionInfo = GridKit.isUpdateAvailable(BASE_URL, a_moduleName,
        currentVersion);
    if (versionInfo.currentVersion == null) {
      System.out.println("Module " + a_moduleName + " is unknown");
      return;
    }
    if (!currentVersion.equals(versionInfo.currentVersion)) {
      System.out.println("Newer module " + a_moduleName + " with version "
                         + versionInfo.currentVersion
                         + " available");
      String workDir = a_workDir;
      // Download the newest module version
      filename = GridKit.retrieveModule(BASE_URL, versionInfo, workDir);
      if (filename != null) {
        // Do the update
        GridKit.updateModule(filename, workDir, a_libDir);
      }
    }
    else {
      System.out.println("Module " + a_moduleName + " with version " +
                         currentVersion + " is up-to-date");
    }
  }

  public static void updateJGAPLibrary(String BASE_URL, String a_libDir,
                                       String a_workDir)
      throws Exception {
    updateModuleLibrary(BASE_URL, "jgap", a_libDir, a_workDir);
  }

  public static void main(String[] args)
      throws Exception {
    JGAPGPXStream xstream = new JGAPGPXStream();
    String dir = "D:\\JavaProjekte\\JGAP_CVS\\work\\storage\\ntb\\";
    String filename = "ntb_fitness_20080719180208812_12418.45";
    File f = new File(dir, filename);
    FileInputStream fis = new FileInputStream(f);
    IGPProgram result = (IGPProgram) xstream.fromXML(fis);
    String line = result.toStringNorm(0);
    Vector v = new Vector();
    v.add(line);
    writeTextFile(v, dir + filename + ".gp");
    fis.close();
  }

  public static void writeTextFile(Vector zeilen, String dateiname)
      throws Exception {
    FileWriter fi;
    fi = new FileWriter(dateiname, false);
    PrintWriter os = new PrintWriter(fi);
    String s;
    for (int i = 0; i < zeilen.size(); i++) {
      s = (String) zeilen.elementAt(i);
      os.println(s);
    }
    os.close();
    //Auf Fehler abfragen, da keine Exception über PrintWriter erzeugt wird!
    if (os.checkError()) {
      throw new IOException("Fehler beim Schreiben der Textdatei " + dateiname);
    }
  }
}
