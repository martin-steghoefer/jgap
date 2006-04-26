package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.*;

/**@todo currently not used*/
public class FormulaFitnessFunction extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  protected double evaluate(IChromosome a_subject) {
    ProgramChromosome chrom = (ProgramChromosome) a_subject;
    /**@todo for some problems: compare against I/O-Table*/
    GPGenotype.setVariable("X", new Double(1));
    Double res = (Double)chrom.evaluate();
    final double desiredValue = 47.11d;
    return Math.abs(res.doubleValue()-desiredValue);
    //sum up second value
//    chrom.setVariable("X",new Double(2));
//    Double res = (Double)chrom.evaluate();
//    final double desiredValue = 55.8d;
//    return Math.abs(res.doubleValue()-desiredValue);
  }

}
