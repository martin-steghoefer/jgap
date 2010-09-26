/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.island;

/**
 * Simple example on how to build and run an island model with JGAP.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class IslandExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private int nextNumber;

  public IslandExample() {
  }

  public void start()
      throws Exception {
    // Create islands and start evolution on each island.
    // --------------------------------------------------
    int numThreads = 5;
    Thread[] pop = new Thread[numThreads];
    for (int i = 0; i < numThreads; i++) {
      System.out.println("Creating Island " + i);
      pop[i] = getIsland();
      pop[i].start();
    }
    int finished = 0;
    while (finished < numThreads) {
      Thread.currentThread().sleep(50);
      for (int i = 0; i < numThreads; i++) {
        IslandThread current = (IslandThread)pop[i];
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

  private Thread getIsland()
      throws Exception {
    return new IslandThread(nextNumber++);
  }

  public static void main(String[] args)
      throws Exception {
    IslandExample instance = new IslandExample();
    instance.start();
  }
}
