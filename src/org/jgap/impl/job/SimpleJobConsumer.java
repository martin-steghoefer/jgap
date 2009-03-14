/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.distr.*;

/**
 * Sample implementation of a service that generates jobs, takes them, executes
 * them, receives results from finished jobs and merges the results in order to
 * start a new round of evolution.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SimpleJobConsumer {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private List m_jobs = new Vector();
  private List m_results = new Vector();

  public void pushJobToGrid(IJob a_job) {
    m_jobs.add(a_job);
    // Execute the job asynchronously.
    // -------------------------------
    new Thread(a_job).start();
  }

  public void waitForAllJobs() {
    while (true) {
      if (m_jobs.size() < 1) {
        break;
      }
      try {
        Thread.sleep(50);
      } catch (InterruptedException iex) {
        iex.printStackTrace();
        break;
      }
      Iterator it = m_jobs.iterator();
      while (it.hasNext()) {
        IJob job = (IJob)it.next();
        if (job.isFinished()) {
          System.out.println("Another job finished!");
          m_results.add(job.getResult());
          it.remove();
        }
      }
    }
  }

  public EvolveResult[] getResults() {
    int size = m_results.size();
    if (size < 1) {
      throw new IllegalStateException("No results found!");
    }
    EvolveResult[] results = new EvolveResult[size];
    for(int i=0;i<size;i++) {
      results[i] = (EvolveResult)m_results.get(i);
    }
    return results;
  }

  public void init()
      throws Exception {
    Configuration gaConf = new DefaultConfiguration();
    gaConf.setPreservFittestIndividual(true);
    gaConf.setKeepPopulationSizeConstant(false);

    SimpleJobConsumer gridClient = new SimpleJobConsumer();

    IChromosome sampleChromosome = new Chromosome(gaConf,
        new BooleanGene(gaConf), 16);
    gaConf.setSampleChromosome(sampleChromosome);
    gaConf.setPopulationSize(20);
    gaConf.setFitnessFunction(new MaxFunction());

    Genotype genotype = Genotype.randomInitialGenotype(gaConf);
    // Run evolution
    IPopulationSplitter popSplitter = new SimplePopulationSplitter(3);
    for (int i = 0; i < 50; i++) {
      // Get jobs, encompass them in a griddable task (only run() supported)
      List evolves = genotype.getEvolves(popSplitter);
      Iterator it = evolves.iterator();
      while (it.hasNext()) {
        IEvolveJob evolve = (IEvolveJob) it.next();
        gridClient.pushJobToGrid(evolve);
      }
      // Wait for the jobs.
      // ------------------
      gridClient.waitForAllJobs();
      // Update genotype with all entities, ready for new run
      IPopulationMerger popMerger = new FittestPopulationMerger();
      genotype.mergeResults(popMerger, gridClient.getResults());
    }
    // Get best and show info
    IChromosome fittest = genotype.getFittestChromosome();
    System.out.println("Best solution: " + fittest.toString());
  }

  public static void main(String[] args) throws Exception {
    new SimpleJobConsumer().init();
    System.exit(0);
  }
}
