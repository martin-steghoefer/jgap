/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;

public class MyGAWorker
    implements Worker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public WorkResult doWork(WorkRequest work, String workDir)
      throws Exception {
    MyRequest req = ( (MyRequest) work);
    Configuration conf = req.getConfiguration();
    conf.setEventManager(new EventManager()); //because it is not serialized!
    conf.setJGAPFactory(new JGAPFactory(false)); //because it is not serialized!
    Genotype gen = Genotype.randomInitialGenotype(conf);
    gen.evolve(40);
    IChromosome fittest = gen.getFittestChromosome();
    MyResult res = new MyResult(req.getSessionName(), req.getRID(), fittest, 1);
    return res;
  }
}
