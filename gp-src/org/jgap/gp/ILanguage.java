package org.jgap.gp;

import org.jgap.*;

public interface ILanguage {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  int defaultProgramSize();

  void fillProgram(ProgramChromosome chrom)
      throws InvalidConfigurationException;
}
