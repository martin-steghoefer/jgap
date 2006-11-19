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

import org.apache.commons.cli.*;
import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.*;
import org.jgap.distr.grid.JGAPWorker;
import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * Receives work, computes a solution and returns the solution to the requester.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyGAWorker
    implements Worker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

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

  public static void main(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("worker", true);
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    Options options = new Options();
    CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
    // Start worker.
    // -------------
    new JGAPWorker(config, MyGAWorker.class, MyWorkerFeedback.class);
  }
}
