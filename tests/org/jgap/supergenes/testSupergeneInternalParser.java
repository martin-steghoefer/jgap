package tests.org.jgap.supergenes;

import java.util.Iterator;
import org.jgap.supergenes.*;
import org.jgap.*;

/** Tests the Supergene internal parser. */
public class testSupergeneInternalParser extends abstractSupergene
{

    public static void main(String[] args) {
         System.out.println(testInternalParser());
     }

    public static boolean testInternalParser() {
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
    private static void splitRecursive(String a_t, StringBuffer a_buffer,
    String a_ident, boolean a_print)
     throws UnsupportedRepresentationException
     {
       if (a_t.indexOf(GSTART)<0)
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

    public boolean isValid() {
        throw new Error("Should never be called.");
    }

    public Gene newGene() {
        throw new Error("Should never be called.");
    }



}