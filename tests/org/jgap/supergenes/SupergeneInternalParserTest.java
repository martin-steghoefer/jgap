/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import java.util.*;
import org.jgap.*;
import junit.framework.*;
import org.jgap.util.StringKit;

/** Tests the Supergene internal parser. */
public class SupergeneInternalParserTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.15 $";

  public static Test suite() {
    TestSuite suite =
        new TestSuite(SupergeneInternalParserTest.class);
    return suite;
  }

  public void testSupergeneInternalParser()
      throws Exception {
    TestClass m_test = new TestClass(conf);
    /* Undocumented test statements. */
    String expectedResponse =
        "----'0'"
        + "----'1'"
        + "----'2'"
        + "----''"
        + "--------'i1'"
        + "--------'ib'"
        + "--------'ic'"
        + "--------'k1'"
        + "--------'k2'"
        + "------------'hn1'"
        + "----------------''"
        + "----------------'a'"
        + "------------'hn2'"
        + "------------'hn3'";
    String s = "<0><1><2><><" + m_test.encode("<i1><ib><ic>")
        + "><" + m_test.encode("<k1><k2><"
                               + m_test.encode("<hn1><" + m_test.encode("<><a>")
        + "><hn2><hn3>") + ">") + ">";
    StringBuffer b = new StringBuffer();
    m_test.splitRecursive(s, b, "", false);
    assertEquals(b.toString(), expectedResponse);
  }

  class TestClass
      extends AbstractSupergene {
    public TestClass(final Configuration a_conf)
        throws InvalidConfigurationException {
      super(a_conf, new Gene[] {});
    }

    //Used in test only
    private void splitRecursive(String a_t, StringBuffer a_buffer,
                                String a_ident, boolean a_print)
        throws UnsupportedRepresentationException {
      if (a_t.indexOf(GENE_DELIMITER_HEADING) < 0) {
        String p = a_ident + "'" + a_t + "'";
        if (a_print) {
          System.out.println(p);
        }
        a_buffer.append(p);
      }
      else {
        Iterator iter = split(a_t).iterator();
        while (iter.hasNext()) {
          String item = (String) iter.next();
          item = decode(item);
          splitRecursive(item, a_buffer, a_ident + "----", a_print);
        }
      }
    }

    public boolean isValid(Gene[] a) {
      throw new Error("Should never be called.");
    }

    protected Gene newGeneInternal() {
      throw new Error("Should never be called.");
    }

    protected String encode(String a_string) {
      return super.encode(a_string);
    }

    public String decode(String a_string) {
      return super.decode(a_string);
    }
  }
}
