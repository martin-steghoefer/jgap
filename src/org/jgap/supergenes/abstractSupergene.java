/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.supergenes;

import org.jgap.Gene;
import org.jgap.Configuration;
import org.jgap.UnsupportedRepresentationException;
import org.jgap.RandomGenerator;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;

import java.net.URLEncoder;
import java.net.URLDecoder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;


/** Supergene implementation, supporting the most of the methods, except
 * isValid. To make any sense of using supergenes, you must always override
 * isValid methods.
 */

public abstract class abstractSupergene implements Supergene, Serializable {

    /** String containing the CVS revision. Read out via reflection!*/
    final static String CVS_REVISION = "0.0.1 alpha-explosive";

    /** Holds the genes of this supergene. */
    private Gene[] m_genes;

    /**
     * Get the array of genes - components of this supergene.
     * The supergene components may be supergenes itself.
     */
    public Gene[] getGenes()
     {
         return m_genes;
     }

     /**
      * Returns the Gene at the given index (locus) within the Chromosome. The
      * first gene is at index zero and the last gene is at the index equal to
      * the size of this Chromosome - 1.
      *
      * @param a_desiredLocus: The index of the gene value to be returned.
      * @return The Gene at the given index.
      */
     public Gene getGene(int a_index)
      {
          return m_genes[a_index];
      };


    /** Constructs abstract supergene with the given gene list.
     * @param a_genes array of genes for this Supergene
     */
    public abstractSupergene(Gene [] a_genes) {
        m_genes = a_genes;
    }

    /**
     * <b>Always provide the parameterless
     * constructor</b> for the derived class. This is required to
     * create a new instance of supergene and should be used inside
     * <code>newGene</code> only. The parameterless
     * constructor need not (and cannot) assign the private
     * <code>genes</code> array.
     */
    public abstractSupergene() {
    }


    /**
     * Test the allele combination of this supergene for validity.
     *<p>
     * At <i>least about 5 % of the randomly
     * generated Supergene suparallele values should be valid.</i> If the valid
     * combinations represents too small part of all possible combinations,
     * it can take too long to find the suitable mutation that does not brake
     * a supergene. If you face this problem, try to split the supergene into
     * several sub-supergenes.
     * </p>
     * @return true only if the supergene allele combination is valid.
     */
    public abstract boolean isValid();

    /** Creates a new instance of this Supergene class with the same number of
     * genes, calling newGene for each subgene. The class, derived from this
     * abstract supergene will be instantiated
     * (not the instance of abstractSupergene itself. */
    public Gene newGene(Configuration a_activeConfiguration) {
        Gene [] g = new Gene[m_genes.length];
        for (int i = 0; i < m_genes.length; i++) {
            g[i] = m_genes[i].newGene(a_activeConfiguration);
        }

        try {
            abstractSupergene age =
                (abstractSupergene) getClass ().newInstance ();

            age.m_genes = g;
            return age;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new Error("This should not happen. Is the parameterless "+
            "constructor provided fo "+getClass().getName()+"?");
        }

    }

    /** Maximal number of retries for applyMutation and setToRandomValue.
     * If the valid supergen cannot be created after this number of iterations,
     * the error message is printed and the unchanged instance is returned. */
    public static int MAX_RETRIES = 1000;

    /**
     * Applies a mutation of a given intensity (percentage) onto the gene
     * at the given index. Retries while isValid() returns true for the
     * supergene. The method is delegated to the first element [0] of the
     * gene, indexed by <code>index</code>.
     * @see isValid()
     */
    public void applyMutation(int index, double a_percentage) {

        // Return immediately the current value is found in
        // the list of immutable alleles for this position.
        // ---------------------------------------------------
        if ( index<m_immutable.length )
         if ( m_immutable [index] !=null )
          {
           synchronized (m_immutable)
            {
              if ( m_immutable [index] .contains(this) ) return;
            }
          };

        if ( !isValid() ) throw new Error("Should be valid on entry");
        try {
            /** Get the old value for backup using serialization */
            ByteArrayOutputStream bout = new ByteArrayOutputStream ();
            ObjectOutputStream oout = new ObjectOutputStream (bout);
            oout.writeObject (m_genes[index]);
            oout.close ();
            byte [] boa = bout.toByteArray();

            for (int i = 0; i < MAX_RETRIES; i++) {
                m_genes [index] .applyMutation (0, a_percentage);
                if (isValid ())  return;
            }

            // restore the gene as it was
            // --------------------------
             ObjectInputStream backup = new ObjectInputStream (new
               ByteArrayInputStream (boa));
              m_genes [index] = (Gene) backup.readObject();

             markImmutable(index);
        }
        catch (ClassNotFoundException ex) {
            throw new Error("This should never happen");
        }
        catch (IOException ex) {
            throw new Error("Unable to mutate", ex);
        }
    }

    /** Maximal number of notes about immutable genes per
     * single gene position */
    public static int MAX_IMMUTABLE_GENES = 100000;

    /** @todo: Implement protection against overgrowing of this
     * data block.
     */
    private void markImmutable(int a_index)
     {
         synchronized (m_immutable)
         {
             if (m_immutable.length<=a_index)
              {
                  /** Extend the array (double length). */
                  Set [] r = new Set [2*m_immutable.length];
                  System.arraycopy(m_immutable, 0, r, 0, m_immutable.length);
                  m_immutable = r;
              }

              if (m_immutable [a_index] == null)
               m_immutable [a_index] = new TreeSet();

              if (m_immutable [a_index] .size()<MAX_IMMUTABLE_GENES)
               m_immutable [a_index].add(this);
         };
     }

    /** Set of supergene allele values that cannot mutate. */
    static Set [] m_immutable = new Set[1];

    /**
     * Sets the value of this Gene to a random legal value for the
     * implementation. It calls setToRandomValue for all subgenes and
     * then validates. With a large number of subgenes and low percent of
     * valid combinations this may take too long to complete. We think,
     * at lease several % of the all possible combintations must be valid.
     *
     * @throws an error if unable to get a valid random instance in
     * the number of loops, defined by MAX_RETRIES.
     */
    public void setToRandomValue(RandomGenerator a_numberGenerator) {
        /** set all to random value first */
        for (int i = 0; i < m_genes.length; i++) {
            m_genes[i].setToRandomValue(a_numberGenerator);
        }
        if (isValid()) return;

        for (int i = 0; i < MAX_RETRIES; i++) {
            for (int j = 0; j < m_genes.length; j++) {
                /* mutate only one gene at time. */
                m_genes[j].setToRandomValue(a_numberGenerator);
                if (isValid()) return;
            }
        }
    }

    /**
     * Sets the allele.
     * @param that must be an array of objects, size matching the
     * number of genes.
     */
    public void setAllele(Object a_superAllele) {
        Object[] a = (Object[]) a_superAllele;
        if (a.length!=m_genes.length) throw new
         ClassCastException("Record length, "+a.length+" != "+m_genes.length);
        for (int i = 0; i < m_genes.length; i++) {
            m_genes[i].setAllele(a[i]);
        }
     }

    /**
     * Retrieves the allele value represented by this Supergene.
     * @return array of objects, each matching the subgene in this Supergene
     */
    public Object getAllele() {
        Object [] o = new Object [m_genes.length];
        for (int i = 0; i < m_genes.length; i++) {
            o[i] = m_genes[i].getAllele();
        }
        return o;
    }

    /**
     * Retrieves a string representation of the value of this Supergene
     * instance, using calls to the Supergene components.
     */
    public String getPersistentRepresentation()
      throws UnsupportedOperationException {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < m_genes.length; i++) {
          b.append(GSTART);
          b.append(encode(m_genes[i].getPersistentRepresentation()));
          b.append(GEND);
        }
        return b.toString();
    }

    /**
     * Sets the value and internal state of this Gene from the string
     * representation returned by a previous invocation of the
     * getPersistentRepresentation() method.
     * Calls getPersistentRepresentation() for all Supergene components.
     */
    public void setValueFromPersistentRepresentation (String a_representation)
    throws UnsupportedOperationException, UnsupportedRepresentationException {

        ArrayList r = split(a_representation);
        if (r.size()!=m_genes.length) throw new
         UnsupportedRepresentationException("Supergene size, "+m_genes.length+
          " mismatch the record number, "+r.size());

        for (int i = 0; i < m_genes.length; i++) {
            m_genes[i].setValueFromPersistentRepresentation(
             (String) r.get(i));
        }

    }

    /** Calls cleanup() for each subgene. */
    public void cleanup() {
        for (int i = 0; i < m_genes.length; i++) {
            m_genes[i].cleanup();
        }
    }

    /**
     * @return a string representation of the supergene, providing
     * class name and calling toString() for all subgenes.
     */
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Supergene "+getClass().getName()+ " {");
        for (int i = 0; i < m_genes.length; i++) {
            b.append(" ");
            b.append(m_genes[i].toString());
        }
        b.append("}");
        return b.toString();
    }

    /** Returns the number of the genes-components of this supergene. */
    public int size() {
        return m_genes.length;
    }

    /** Calls compareTo of subgenes. The passed parameter must be
     * an instance of abstractSupergene. */
    public int compareTo(Object o) {
        abstractSupergene q = (abstractSupergene) o;

        int c = m_genes.length-q.m_genes.length;
        if (c!=0) return c;

        for (int i = 0; i < m_genes.length; i++) {
            c = m_genes[i].compareTo(q.m_genes[i]);
            if (c!=0) return c;
        }
        if (getClass().equals(o.getClass())) return 0;

        return getClass().getName().compareTo(o.getClass().getName());
    }

    /** Calls equals() for each pair of genes. If the supplied object is
     * an instance of the different class, returns false. */
    public boolean equals(Object a_gene) {
        if (a_gene==null || ! (a_gene.getClass().equals(getClass())))
         return false;

        abstractSupergene age = (abstractSupergene) a_gene;
        return Arrays.equals(m_genes, age.m_genes);
    }

    /** Returns sum of hashCode() of the genes-components. */
    public int hashCode() {
        long s = 0;
        for (int i = 0; i < m_genes.length; i++) {
            s+=m_genes[i].hashCode();
        }
        return (int) ( s % Integer.MAX_VALUE);
    }

    /** Char used to start gene data in string representations. */
    private static final String GSTART = "<";
    /** Char used to end gene data in string representations. */
    private static final String GEND   = ">";

    /* Encode string, doubling the separators. */
    private static final String encode(String a_x)
     {
        try {
            return URLEncoder.encode (a_x, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new Error("This should never happen!");
        }
     }

     /** Decode string, undoubling the separators. */
     private static final String decode(String a_x)
     {
        try {
            return URLDecoder.decode (a_x, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new Error("This should never happen!");
        }
     }

     /** Splits the string x into individual gene representations */
     private static final ArrayList split(String a_x)
      throws UnsupportedRepresentationException
       {
         ArrayList a = new ArrayList();

         StringTokenizer st = new StringTokenizer(a_x,GSTART+GEND, true);
         while (st.hasMoreTokens())
          {
             if (!st.nextToken().equals(GSTART))
              throw new UnsupportedRepresentationException
                (a_x+" no open tag");
             String n = st.nextToken();
             if (n.equals(GEND)) a.add(""); /* Empty token */
             else
              {
                a.add(n);
                if (!st.nextToken().equals(GEND))
                throw new UnsupportedRepresentationException
                 (a_x+" no close tag");
              }
          }
         return a;
       }

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
           "><"+encode("<k1><k2><"+encode("<hn1><"+encode("<><a>")+
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


}