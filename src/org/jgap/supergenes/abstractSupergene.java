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
 * {@link org.jgap.supergenes.Supergene#isValid(Gene [] genes)} To make any sense of
 * using supergenes, you must always override isValid (Gene []).
 */

public abstract class abstractSupergene implements Supergene, Serializable
 {

    /** String containing the CVS revision. Read out via reflection!*/
    final static String CVS_REVISION = "0.0.1 alpha-explosive";

    /**
     * This field separates gene class name from
     * the gene persistent representation string.
     */
    public final static String GENE_DELIMITER = "#";
    /**
     * Represents the heading delimiter that is used to separate genes in the
     * persistent representation of CompositeGene instances.
     */
    public final static String GENE_DELIMITER_HEADING = "<";

    /**
     * Represents the closing delimiter that is used to separate genes in the
     * persistent representation of CompositeGene instances.
     */
    public final static String GENE_DELIMITER_CLOSING = ">";


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
      * This seems to be one of the bottlenecks, so it is declared final.
      * I cannot imagine the reason for overriding this trivial single line
      * method.
      *
      * @param a_desiredLocus: The index of the gene value to be returned.
      * @return The Gene at the given index.
      */
     public final Gene getGene(int a_index)
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
     * This method calls isValid for the current gene list.
     * @return true only if the supergene allele combination is valid or
     * the validation is switched off by calling
     * {@link org.jgap.supergenes.abstractSupergene#setValidateWhenMutating
     * setValidateWhenMutating (<i>false</i>) }
     */
    public boolean isValid()
    {
        if (!validation_on) return true;
        return isValid (m_genes);
    }

    /**
     * Test the given gene list for validity. The genes must exactly the same
     * as inside this supergene.
     * At <i>least about 5 % of the randomly
     * generated Supergene suparallele values should be valid.</i> If the valid
     * combinations represents too small part of all possible combinations,
     * it can take too long to find the suitable mutation that does not brake
     * a supergene. If you face this problem, try to split the supergene into
     * several sub-supergenes. setValidateWhenMutating has no effect on this
     * method.
     * </p>
     * @return true only if the supergene allele combination is valid.
     */
    public abstract boolean isValid(Gene [] a_case);


    /** Creates a new instance of this Supergene class with the same number of
     * genes, calling newGene() for each subgene. The class, derived from this
     * abstract supergene will be instantiated
     * (not the instance of abstractSupergene itself).
     * @throws Error if the instance of <i>this</i> cannot be instantiated
     * (for example, if it is not public or  the parameterless constructor is
     * not provided).
     * */
    public Gene newGene() {
        Gene [] g = new Gene[m_genes.length];
        for (int i = 0; i < m_genes.length; i++) {
            g[i] = m_genes[i].newGene();
        }

        try {
            abstractSupergene age =
                (abstractSupergene) getClass ().newInstance ();

            age.validation_on = validation_on;

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
     * @see org.jgap.supergenes.abstractSupergene.isValid()
     */
    /**
     * Applies a mutation of a given intensity (percentage) onto the gene
     * at the given index. Retries while isValid() returns true for the
     * supergene. The method is delegated to the first element [0] of the
     * gene, indexed by <code>index</code>.
     * @see org.jgap.supergenes.abstractSupergene.isValid()
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

        Object backup = m_genes [index].getAllele();

        for (int i = 0; i < MAX_RETRIES; i++) {
            m_genes [index] .applyMutation (0, a_percentage);
            if (isValid ())  return;
        }

        // restore the gene as it was
        m_genes [index]. setAllele(backup);
        markImmutable(index);

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
    private static Set [] m_immutable = new Set[1];

    /**
     * Discards all internal caches, ensuring correct repetetive tests
     * of performance. Differently from cleanup(), discards also static
     * references, that are assumed to be useful for the multiple instances
     * of the Supergene.
     * Clears the set of the alleles that are known to be immutable.
     */
    public static void reset()
     {
         m_immutable = new Set[1];
     }

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
     * instance, using calls to the Supergene components. Supports other
     * (nested) supergenes in this supergene.
     */
    public String getPersistentRepresentation()
     throws  UnsupportedOperationException {
        StringBuffer b = new StringBuffer();

        Gene gene;
        for (int i = 0; i < m_genes.length; i++) {
          gene = m_genes[i];
          b.append(GENE_DELIMITER_HEADING);
          b.append(
           encode
           (
            gene.getClass().getName()+
            GENE_DELIMITER+
            gene.getPersistentRepresentation())
           );
          b.append(GENE_DELIMITER_CLOSING);
        }
        return b.toString();
    }

    /**
     * See interface Gene for description
     * @param a_representation the string representation retrieved from a
     *        prior call to the getPersistentRepresentation() method.
     *
     * @throws UnsupportedRepresentationException
     *
     * @author Audrius Meskauskas
     * @since 1.1
     */
    public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
      if (a_representation != null) {
        try {
            /** Remove the old content */
            ArrayList r = split(a_representation);
            Iterator iter = r.iterator();
            m_genes = new Gene [r.size()];

            StringTokenizer st;
            String clas;
            String representation;
            String g;
            Gene gene;

           for (int i = 0; i < m_genes.length; i++)
            {
                g = decode ((String) iter.next());
                st = new StringTokenizer(g, GENE_DELIMITER);
                if (st.countTokens()!=2)
                 throw new UnsupportedRepresentationException("In "+g+", "+
                  "expecting two tokens, separated by "+GENE_DELIMITER);
                clas = st.nextToken();
                representation = st.nextToken();
                gene = createGene(clas, representation);
                m_genes [i] = gene;
            }
        }
        catch (Exception ex) {
          ex.printStackTrace();
          throw new UnsupportedRepresentationException(ex.getCause().
              getMessage());
        }
      }
    }

    /** Creates a new instance of gene. */
    protected Gene createGene(String a_geneClassName,
     String a_persistentRepresentation) throws Exception
     {
          Class geneClass = Class.forName (a_geneClassName);
          Gene gene = (Gene) geneClass.newInstance ();
          gene.setValueFromPersistentRepresentation (a_persistentRepresentation);
          return gene;
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

    /** Calls compareTo() for all subgenes. The passed parameter must be
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
        int s = 0;
        for (int i = m_genes.length-1; i>=0; i--) {
            s+=m_genes[i].hashCode();
        }
        return s;
    }

    /* Encode string, doubling the separators. */
    protected static final String encode(String a_x)
     {
        try {
            return URLEncoder.encode (a_x, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new Error("This should never happen!");
        }
     }

     /** Decode string, undoubling the separators. */
     protected static final String decode(String a_x)
     {
        try {
            return URLDecoder.decode (a_x, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            throw new Error("This should never happen!");
        }
     }

     /**
      * Splits the string a_x into individual gene representations
      * @author Audrius Meskauskas
      * @param a_x The string to split.
      * @return The elements of the returned array are the
      * persistent representation strings of the genes - components.
      */
     protected static final ArrayList split(String a_x)
      throws UnsupportedRepresentationException
       {
         ArrayList a = new ArrayList();

         StringTokenizer st = new StringTokenizer
         (a_x,GENE_DELIMITER_HEADING+ GENE_DELIMITER_CLOSING, true);

         while (st.hasMoreTokens())
          {
             if (!st.nextToken().equals(GENE_DELIMITER_HEADING))
              throw new UnsupportedRepresentationException
                (a_x+" no open tag");
             String n = st.nextToken();
             if (n.equals(GENE_DELIMITER_CLOSING)) a.add(""); /* Empty token */
             else
              {
                a.add(n);
                if (!st.nextToken().equals(GENE_DELIMITER_CLOSING))
                throw new UnsupportedRepresentationException
                 (a_x+" no close tag");
              }
          }
         return a;
       }

    /** Append a new gene to the gene array. */
    public void addGene(Gene g)
     {
         if (m_genes == null)
                 m_genes = new Gene [] { g };
          else
              {
                 Gene [] genes = new Gene [m_genes.length+1];
                 System.arraycopy(m_genes, 0, genes, 0, m_genes.length);
                 genes [m_genes.length] = g;
                 m_genes = genes;
              }

     }

     /**
      * {@inheritDoc}
      */
     public void setValidateWhenMutating(boolean a_validate)
     {
         validation_on = a_validate;
     }

     protected boolean validation_on = true;

}