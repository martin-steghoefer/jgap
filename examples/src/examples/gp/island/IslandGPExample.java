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

/**
 * Simple example on how to build and run an island model with JGAP.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class IslandGPExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int nextNumber;

  public IslandGPExample() {
  }

  public void start()
      throws Exception {
    // Create islands and start evolution on each island.
    // --------------------------------------------------
    int numThreads = 5;
    Runnable[] pop = new Runnable[numThreads];
    for (int i = 0; i < numThreads; i++) {
      System.out.println("Creating Island " + i);
      pop[i] = getIsland();
      new Thread(pop[i]).start();
    }
    int finished = 0;
    while (finished < numThreads) {
      Thread.currentThread().sleep(50);
      for (int i = 0; i < numThreads; i++) {
        IslandGPThread current = (IslandGPThread)pop[i];
        if(!current.isFinished()) {
          finished = 0;
          break;
        }
        finished++;
      }
    }
    // Merge best solutions of all islands.
    // ------------------------------------
    /**@todo*/
    /*@todo rerun n times*/
  }

  private Runnable getIsland()
      throws Exception {
    return new IslandGPThread(nextNumber++);
  }

  public static void main(String[] args)
      throws Exception {
    IslandGPExample instance = new IslandGPExample();
    instance.start();
  }
}
