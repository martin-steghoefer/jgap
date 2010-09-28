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
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private int nextNumber;

  private IslandGPThread m_firstIsland = null;

  private int runs = 0;

  /**@todo Abbruch aller Threads, wenn beste Lösung gefunden*/
  /**@todo Island config. einbauen, so dass Island model automatisch ausgeführt
   * wird
   */
  /**@todo impl. MergEvent, e.g.: merge islands when one island stucks*/

  public IslandGPExample() {
  }

  public void start()
      throws Exception {
    do {
      nextNumber = 0;
      // Create islands and start evolution on each island.
      // --------------------------------------------------
      int numThreads = 5;
      Runnable[] islands = new Runnable[numThreads];
      Thread[] islandThreads = new Thread[numThreads];
      for (int i = 0; i < numThreads; i++) {
        System.out.println("Creating Island " + i);
        islands[i] = getIsland(nextNumber++);
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
        Thread.currentThread().sleep(50);
        for (int i = 0; i < numThreads; i++) {
          IslandGPThread current = (IslandGPThread) islands[i];
          if (!current.isFinished()) {
            finished = 0;
//            break;
          }
          else {
            IGPProgram best = current.getBestSolution();
            if (best.getFitnessValue() < 0.05) {
              bestIsland = current;
              System.out.println("Satisfying solution found - stop all islands...");
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
      if (runs <= 3) {
        // Merge best solutions of all islands.
        // ------------------------------------
        int programsToMergeIn = ( (IslandGPThread) islands[0]).getGPConfiguration().
            getPopulationSize() / numThreads;
        int offset = programsToMergeIn;
        GPPopulation first = null;
        for (int i = 0; i < numThreads; i++) {
          IslandGPThread current = (IslandGPThread) islands[i];
          GPPopulation pop = current.getPopulation();
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
        // Rerun evolution.
        // ----------------
        m_firstIsland = (IslandGPThread) islands[0];
      }
      else {
        break;
      }
    } while (true);
  }

  /**
   * mergePopulation
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
    for(int i=0;i<islandThreads.length;i++) {
      Thread thread = (Thread)islandThreads[i];
      thread.interrupt();
    }
  }

  /**
   * @param a_index index of island to create
   *
   * @return new island instance
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  private Runnable getIsland(int a_index)
      throws Exception {
    if (a_index == 0 && m_firstIsland != null) {
      return m_firstIsland;
    }
    return IslandGPThread.newInstance(a_index);
  }

  public static void main(String[] args)
      throws Exception {
    IslandGPExample instance = new IslandGPExample();
    instance.start();
  }
}
