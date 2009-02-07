/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.ga;

import org.jgap.*;
import examples.math.*;

/**
 * Computes the value expressed by integer genes.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MyIntegerGenePhenotypeExpresser
    implements PhenotypeExpresser<Double> {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Double express(IChromosome theChromosome) {
    double aValue = 0;
    for (Gene aGene : theChromosome.getGenes()) {
      MyIntegerGene aMathGene = (MyIntegerGene) aGene;
      aValue += (Integer) aMathGene.getAllele();
    }
    return aValue;
  }
}
