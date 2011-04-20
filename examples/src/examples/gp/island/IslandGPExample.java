/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.island;

import org.jgap.gp.impl.*;
import org.jgap.gp.IGPProgram;

/**
 * Simple example on how to build and run an GP island model with JGAP.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class IslandGPExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private int nextNumber;

  private IslandGPThread m_firstIsland = null;

  private int runs = 0;

  /**@todo Island config. einbauen, so dass Island model automatisch ausgeführt
   * wird
   */
  /**@todo impl. MergeEvent, e.g.: merge islands when one island stucks*/
  /**@todo jeweils beste lösung geht öfters verloren! Beeinflusst ein Island das
   * andere oder ist die Lösung in einem Island nicht beständig?*/

  public IslandGPExample() {
  }

  public void start()
      throws Exception {
    int numThreads = 5;
    int maxRuns = 4;
    int popSize = 80;
    Runnable[] islands = new Runnable[numThreads];
    Thread[] islandThreads = new Thread[numThreads];
    IGPProgram allBest = null;
    IGPProgram allBest2 = null;
    do {
      for (int i = 0; i < numThreads; i++) {
        Thread t = islandThreads[i];
        if (t != null) {
          t.interrupt();
          islandThreads[i] = null;
        }
      }
      nextNumber = 0;
      // Create islands and start evolution on each island.
      // --------------------------------------------------
      for (int i = 0; i < numThreads; i++) {
//        System.out.println("Creating Island " + i);
        islands[i] = getIsland(nextNumber++, popSize);
        // Start evolution on the island.
        // ------------------------------
        islandThreads[i] = new Thread(islands[i]);
        islandThreads[i].start();
      }
      int finished = 0;
      // Wait for all islands to finish.
      // -------------------------------
      IslandGPThread bestIsland = null;
      while (finished < numThreads) {
        Thread.currentThread().sleep(5);
        for (int i = 0; i < numThreads; i++) {
          IslandGPThread current = (IslandGPThread) islands[i];
          if (!current.isFinished()) {
            finished = 0;
            break;
          }
          else {
            IGPProgram best = current.getBestSolution();
            if (best != null && best.getFitnessValue() < 0.05) {
              bestIsland = current;
              System.out.println(
                  "Satisfying solution found - stop all islands...");
              allBest2 = best;
              stopAllIslands(islandThreads);
              finished = -1;
              break;
            }
          }
          finished++;
        }
        if (finished == -1) {
          bestIsland.outputBestSolution();
          // Unconditional stop because best solution found.
          // -----------------------------------------------
          break;
        }
      }
      runs++;
      // Merge best solutions of all islands.
      // ------------------------------------
      int programsToMergeIn = ( (IslandGPThread) islands[0]).
          getGPConfiguration().getPopulationSize() / numThreads;
      int offset = programsToMergeIn;
      GPPopulation first = null;
      GPPopulation pop = null;
      // Lock all threads.
      // -----------------
      IslandGPThread currentFirst = (IslandGPThread) islands[0];
      do {
        Thread.sleep(10);
      } while (currentFirst.isLocked());
      try {
        currentFirst.setLocked();
        //
        for (int i = 0; i < numThreads; i++) {
          IslandGPThread current = (IslandGPThread) islands[i];
          synchronized (current) {
            IGPProgram allbestTemp2 = current.getBestSolution();
            if (allBest2 == null ||
                (allbestTemp2 != null &&
                 allBest2.getFitnessValue() > allbestTemp2.getFitnessValue())) {
              allBest2 = allbestTemp2;
            }
            pop = current.getPopulation();
            pop.sortByFitness();
            if (i == 0) {
              first = pop;
            }
            else {
              // Merge to first island.
              // ----------------------
              mergePopulation(first, pop, offset, programsToMergeIn);
              offset += programsToMergeIn;
            }
          }
        }
        IGPProgram allbestTemp = first.determineFittestProgram();
        if (allbestTemp != null) {
          if (allBest == null ||
              allbestTemp.getFitnessValue() < allBest.getFitnessValue()) {
            allBest = allbestTemp;
          }
        }
      } finally {
        if (currentFirst.isLocked()) {
          currentFirst.releaseLocked();
        }
      }
      // Rerun evolution.
      // ----------------
      m_firstIsland = (IslandGPThread) islands[0];
      if (! (runs <= maxRuns && finished != -1)) {
        break;
      }
    } while (true);
    if (allBest != null) {
      System.out.println("-------------------------------");
      System.out.println("Best overall solution currently (merged):");
      System.out.println("  Fitness value: " +
                         allBest.getFitnessValue()
                         + ",  Solution: " + allBest.toStringNorm(0));
    }
    if (allBest2 != null) {
      System.out.println("-------------------------------");
      System.out.println("Best overall solution currently (directly):");
      System.out.println("  Fitness value: " +
                         allBest2.getFitnessValue()
                         + ",  Solution: " + allBest2.toStringNorm(0));
    }
  }

  /**
   * Merge two populations.
   *
   * @param original GPPopulation
   * @param toMerge GPPopulation
   * @param offset offset in "original" for programs to be merged in to
   * @param count number of programs to merge from "toMerge" to "original"
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  private void mergePopulation(GPPopulation original, GPPopulation toMerge,
                               int offset, int count) {
    for (int i = 0; i < count; i++) {
      IGPProgram prog = toMerge.getGPProgram(i);
      original.setGPProgram(offset + i, prog);
    }
  }

  private void stopAllIslands(Thread[] islandThreads) {
    for (int i = 0; i < islandThreads.length; i++) {
      Thread thread = (Thread) islandThreads[i];
      thread.interrupt();
    }
  }

  /**
   * @param a_index index of island to create
   * @param a_popSize the population size the island should have
   *
   * @return new island instance
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  private Runnable getIsland(int a_index, int a_popSize)
      throws Exception {
    if (a_index == 0 && m_firstIsland != null) {
      return m_firstIsland;
    }
    return IslandGPThread.newInstance(a_index, a_popSize);
  }

  public static void main(String[] args)
      throws Exception {
    IslandGPExample instance = new IslandGPExample();
    instance.start();
  }
}
