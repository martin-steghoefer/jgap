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
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Executes the evolution and returns the result.
   *
   * @param work WorkRequest
   * @param workDir String
   * @return WorkResult
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public WorkResult doWork(WorkRequest work, String workDir)
      throws Exception {
    MyRequest req = ( (MyRequest) work);
    Configuration conf = req.getConfiguration();
    conf.setEventManager(new EventManager()); //because it is not serialized!
    conf.setJGAPFactory(new JGAPFactory(false)); //because it is not serialized!
    Genotype gen;
    Population initialPop = req.getPopulation();
    if (initialPop == null || initialPop.size() < 1) {
      gen = Genotype.randomInitialGenotype(conf);
    }
    else {
      // Initialize genotype with given population.
      // ------------------------------------------
      gen = new Genotype(conf, initialPop);
      // Fill up population to get the desired size.
      // -------------------------------------------
      int size = conf.getPopulationSize() - initialPop.size();
      gen.fillPopulation(conf, gen.getPopulation(), conf.getSampleChromosome(),
                         size);
    }
    // Execute evolution via registered strategy.
    // ------------------------------------------
    req.getEvolveStrategy().evolve(gen);/**@todo integrate this call into framework*/
    // Assemble result according to registered strategy.
    // --------..---------------------------------------
    MyResult res = (MyResult) req.getWorkerReturnStrategy().assembleResult(req,
        gen);
    return res;
  }

  /**
   * Convenience method to start the worker.
   *
   * @param args String[]
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
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
