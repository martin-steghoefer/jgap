/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.distr;

import org.jgap.*;

/**
 * Breeds populations using a GA that will be breeded either on a single
 * server or on multiple servers being whose results will be merged/synchronized
 * later on.
 * <p>
 * A breeder is part of a fractal structure (fractal because each Breeder can
 * be parent and/or child of other Breeder's).
*
 * @author Klaus Meffert
 * @since 2.0
 * @see wodka project for similar implementation
 */
public abstract class Breeder
    implements Runnable {

  /**
   * The parent Breeder to report to
   */
  private Breeder master;

  /**
   * The child Breeder's doing work for us and reporting to this Breeder.
   */
  private Breeder[] workers;

  /**
   * The Genotype this Breeder is responsible for
   */
  private Genotype genotype;

  private transient boolean running;

  private transient boolean stopped = true;

  private transient MeanBuffer meanBuffer = new MeanBuffer(40);

  public Breeder() {
    super();
  }

  /**
   * Runs the evolution.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void run() {
    try {
      stopped = false;
      while (running) {
        evalOneGeneration();
        int sleepTime = meanBuffer.mean() / 100;
        if (sleepTime <= 0) {
          pause(1);
        }
        else {
          pause(sleepTime);
        }
      }
      stopped = true;
    }
    catch (Throwable t) {
      stopped = true;
      running = false;
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
    genotype.evolve(1);
    informParent();
    meanBuffer.add( (int) (System.currentTimeMillis() - begin));
  }

  protected void informParent() {
    /**@todo implement*/
  }

  /**
   * Pauses the Breeder. This is important if a user providing a system for
   * running the GA does not want to make available 100% of his CPU resources.
   * @param milliSec int
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private synchronized void pause(int milliSec) {
    try {
      this.wait(milliSec);
    }
    catch (InterruptedException e) {
    }
  }

  public void start() {
    if (!running) {
      running = true;
      Thread thread = new Thread(this);
      thread.start();
    }
  }

  public void stop() {
    if (running) {
      running = false;
      if (genotype != null) {
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
    return running;
  }

  public boolean canBeStarted() {
    return !running;
  }

  public boolean canBeStopped() {
    return running;
  }
}

/**
 * A buffer that calculate the mean of a certain amount
 * of values. Uses a fifo inside.
 */

class MeanBuffer {

  private int[] buf = null;
  private int size;
  private int index = 0;

  public MeanBuffer(int size) {
    this.size = size;
    buf = new int[size];
    for (int i=0; i<size; i++){
      buf[i] = 0;
    }
  }
  public void add(int val){
    buf[index] = val;
    index = (index + 1) % size;
  }
  public int mean(){
    int sum = 0;
    for (int i=0; i<size; i++){
      sum += buf[i];
    }
    return sum / size;
  }
  public void reset(){
    for (int i=0; i<size; i++){
      buf[i] = 0;
    }
  }
}

/**@todo move to new class?*/
interface ExceptionHandler {

        void handleThrowable(Throwable throwable);

}
