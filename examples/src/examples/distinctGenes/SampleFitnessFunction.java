/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.distinctGenes;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Fitness function for our example. It's without sense, it's just to
 * demonstrate how to evaluate a chromosome with 40 4-field-genes and one
 * 3-field gene. Each toplevel-gene is a CompositeGene, each "field" within
 * a CompositeGene is a BooleanGene here (arbitrarily chosen).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class SampleFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Calculate the fitness value of a Chromosome.
   * @param a_subject the Chromosome to be evaluated
   * @return defect rate of our problem (the smaller the better)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double evaluate(IChromosome a_subject) {
    int total = 0;
    // Process the first 40 genes with 4 fields per gene
    for (int i = 0; i < a_subject.size() - 1; i++) {
      CompositeGene gene = (CompositeGene) a_subject.getGene(i);
      for (int j = 0; j < 4; j++) {
        // Now evaluate the fields within the gene somehow (here: just an
        // example without sense).
        // --------------------------------------------------------------
        BooleanGene field = (BooleanGene) gene.geneAt(j);
        if (field.booleanValue() == true) {
          // A field with value true is seen as defect
          total++;
        }
      }
    }
    // Process the last gene with 3 fields. Here, a field with value false
    // is seen as defect (in opposition to the other genes)
    CompositeGene gene = (CompositeGene) a_subject.getGene(a_subject.size() - 1);
    for (int j = 0; j < 3; j++) {
      BooleanGene field = (BooleanGene) gene.geneAt(j);
      if (field.booleanValue() == false) {
        // A field with value false is seen as defect
        total++;
      }
    }
    return total;
  }
}
