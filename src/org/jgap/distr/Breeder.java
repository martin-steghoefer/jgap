/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.*;

/**
 * Breeds populations using a GA that will be executed either on a single
 * server or on multiple servers, whose results will be merged/synchronized
 * later on.
 * <p>
 * A breeder is part of a fractal structure (fractal because each Breeder can
 * be parent and/or child of other Breeder's).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class Breeder
    implements Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  /**
   * The parent Breeder to report to
   */
  private Breeder m_master; /**@todo use*/

  /**
   * The child Breeder's doing work for us and reporting to this Breeder.
   */
  private Breeder[] m_workers; /**@todo use*/

  /**
   * The Genotype this Breeder is responsible for
   */
  private Genotype m_genotype; /**@todo construct somewhere*/

  /**
   * Helper class for merging together two Populations into one.
   */
  private IPopulationMerger m_populationMerger; /**@todo use*/

  private transient boolean m_running;

  private transient boolean m_stopped = true;

  private transient MeanBuffer m_meanBuffer = new MeanBuffer(40);

  public Breeder(final IPopulationMerger a_populationMerger) {
    super();
    m_populationMerger = a_populationMerger;
  }

  /**
   * Runs the evolution.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void run() {
    try {
      m_stopped = false;
      while (m_running) {
        evalOneGeneration();
        int sleepTime = m_meanBuffer.mean() / 100;
        if (sleepTime <= 0) {
          pause(1);
        }
        else {
          pause(sleepTime);
        }
      }
      m_stopped = true;
    }
    catch (Throwable t) {
      m_stopped = true;
      m_running = false;
//      if (exHandler != null) {
//        exHandler.handleThrowable(t);
//      }
    }
  }

  /**
   * Evaluate one generation and memorize the time needed. This is important
   * for load balancing and calculating the performance of a system
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void evalOneGeneration()
      throws Exception {
    long begin = System.currentTimeMillis();
    m_genotype.evolve(1);
    informParent();
    m_meanBuffer.add( (int) (System.currentTimeMillis() - begin));
  }

  protected void informParent() {
    /**@todo implement*/
  }

  /**
   * Pauses the Breeder. This is important if a user providing a system for
   * running the GA does not want to make available 100% of his CPU resources.
   * @param a_milliSec number of milliseconds to wait
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private synchronized void pause(final int a_milliSec) {
    try {
      wait(a_milliSec);
    }
    catch (InterruptedException e) {
      ;
    }
  }

  public void start() {
    if (!m_running) {
      m_running = true;
      Thread thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    if (m_running) {
      m_running = false;
      if (m_genotype != null) {
        /**@todo implement*/
//:        genAlgo.stop();
        /*
                if (genAlgo.getRacer() != null) {
                  genAlgo.getRacer().reset();
                }
         */
      }
    }
  }

  public boolean isRunning() {
    return m_running;
  }

  public boolean canBeStarted() {
    return !m_running;
  }

  public boolean canBeStopped() {
    return m_running;
  }
}
/**
 * A buffer that calculate the mean of a certain amount
 * of values. Uses a fifo inside.
 */
class MeanBuffer {
  private int[] m_buf = null;

  private int m_size;

  private int m_index;

  public MeanBuffer(final int a_size) {
    m_size = a_size;
    m_buf = new int[m_size];
    for (int i = 0; i < m_size; i++) {
      m_buf[i] = 0;
    }
  }

  public void add(final int a_val) {
    m_buf[m_index] = a_val;
    m_index = (m_index + 1) % m_size;
  }

  public int mean() {
    int sum = 0;
    for (int i = 0; i < m_size; i++) {
      sum += m_buf[i];
    }
    return sum / m_size;
  }

  public void reset() {
    for (int i = 0; i < m_size; i++) {
      m_buf[i] = 0;
    }
  }
}
