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

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the FileKit class.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class FileKitTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    return new TestSuite(FileKitTest.class);
  }

  public void testAddFilename_0() throws Exception {
    String s = FileKit.addFilename("c:\\temp", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_1() throws Exception {
    String s = FileKit.addFilename("c://temp", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_2() throws Exception {
    String s = FileKit.addFilename("c://temp//", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_3() throws Exception {
    String s = FileKit.addFilename("c:\\temp\\", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_4() throws Exception {
    String s = FileKit.addFilename("c:\\\\temp", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_5() throws Exception {
    String s = FileKit.addFilename("c:////temp", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_6() throws Exception {
    String s = FileKit.addFilename("c://temp////", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_7() throws Exception {
    String s = FileKit.addFilename("c:\\temp\\\\", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_8() throws Exception {
    String s = FileKit.addFilename("c:////temp//", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_9() throws Exception {
    String s = FileKit.addFilename("c:\\\\temp\\", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_10() throws Exception {
    String s = FileKit.addFilename("c:\\temp//", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  public void testAddFilename_11() throws Exception {
    String s = FileKit.addFilename("c://temp\\", "test.txt");
    String expected = "c:/temp/test.txt";
    expected = replaceSeparator(expected,"/");
    assertEquals(expected, s);
  }

  private String replaceSeparator(String a_s, String a_orig) {
    String repl;
    if (FileKit.fileseparator.equals("\\")) {
      repl = FileKit.fileseparator + FileKit.fileseparator;
    }
    else {
      repl = FileKit.fileseparator;
    }
    if (a_s == null || a_s.length() <1) {
      return "";
    }
    return a_s.replaceAll(a_orig, repl);
  }
  /**@todo add further tests*/
}
