package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.supergenes.*;

import java.util.Iterator;

import junit.framework.*;

/** Tests the Supergene internal parser. */
public class testSupergeneInternalParser extends TestCase
{

  class test extends abstractSupergene
  {

    public boolean testInternalParser() {
        /* Undocumented test statements. */
        String expectedResponse =
            "----'0'"+
            "----'1'"+
            "----'2'"+
            "----''"+
            "--------'i1'"+
            "--------'ib'"+
            "--------'ic'"+
            "--------'k1'"+
            "--------'k2'"+
            "------------'hn1'"+
            "----------------''"+
            "----------------'a'"+
            "------------'hn2'"+
            "------------'hn3'";


        String s = "<0><1><2><><"+encode("<i1><ib><ic>")+
         "><"+encode("<k1><k2><"+
         encode("<hn1><"+encode("<><a>")+
          "><hn2><hn3>")+">")+">";

        StringBuffer b = new StringBuffer();
        try {
          splitRecursive (s, b, "", true);
        }
        catch (UnsupportedRepresentationException ex) {
            ex.printStackTrace();
            return false;
        }
        return b.toString().equals(expectedResponse);
    }

    /** Used in test only */
    private void splitRecursive(String a_t, StringBuffer a_buffer,
    String a_ident, boolean a_print)
     throws UnsupportedRepresentationException
     {
       if (a_t.indexOf(GENE_DELIMITER_HEADING)<0)
        {
          String p = a_ident+"'"+a_t+"'";
          if (a_print) System.out.println(p);
          a_buffer.append(p);
        }
       else
        {
          Iterator iter = split(a_t).iterator();
          while (iter.hasNext()) {
              String item = (String)iter.next();
              item = decode(item);
              splitRecursive(item, a_buffer, a_ident+"----", a_print);
          }
        }
     }

    public boolean isValid(Gene [] a) {
        throw new Error("Should never be called.");
    }

    public Gene newGene() {
        throw new Error("Should never be called.");
    }

  }

  public static Test suite() {
    TestSuite suite =
     new TestSuite(testSupergenePersistentRepresentation.class);
    return suite;
  }

  test m_test;

  protected void setUp() throws Exception {
      super.setUp();
      m_test = new test();
  }

  protected void tearDown() throws Exception {
      m_test = null;
      super.tearDown();
  }

  public void testSupergeneInternalParser() {
      boolean expectedReturn = true;
      boolean actualReturn = m_test.testInternalParser();
      assertEquals("return value", expectedReturn, actualReturn);
  }

}
