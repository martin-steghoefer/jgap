package org.jgap.distr;

import java.util.*;
import org.jgap.*;

//import net.openai.ai.ga.*;

/**
 * Breeds populations using a GA that will be breeded either on a single
 * server or on multiple servers being whose results will be merged/synchronized
 * later on.
 * <p>
 * A breeder is part of a fractal structure (fractal because each Breeder can
 * be parent and/or child of other Breeder's).
 * @author Klaus Meffert
 * @since 2.0
 * @see wodka project for similar implementation
 */
public abstract class Breeder
    implements Runnable {

//  private GeneticAlgorithm genAlgo;

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

  private transient Collection listeners = new Vector();

  private transient ExceptionHandler exHandler;

  public int categoryCount() {
    return 7;
  }

  public String getCategoryName(int index) {
    switch (index) {
      case 0:
        return "General";
      case 1:
        return "Genotype";
      case 2:
        return "Selection Policy";
      case 3:
        return "Racer";
      case 4:
        return "Fitness Function";
      case 5:
        return "Terrain Manager";
      case 6:
        return "All";
      default:
        throw new Error("getCategoryName>> Invalid index: " + index);
    }
  }

  public Breeder() {
    super();
  }

  public void setExceptionHandler(ExceptionHandler handler) {
    this.exHandler = handler;
  }

/*
  public void addListener(BreederListener listener) {
    listeners.add(listener);
  }
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
      if (exHandler != null) {
        exHandler.handleThrowable(t);
      }
    }
  }

  private void evalOneGeneration()
      throws Exception {
    long begin = System.currentTimeMillis();
    genotype.evolve(1);
//    informListeners();
    meanBuffer.add( (int) (System.currentTimeMillis() - begin));
  }

/*
  private void informListeners() {
    Iterator iter = listeners.iterator();
    while (iter.hasNext()) {
      BreederListener listener = (BreederListener) iter.next();
      listener.performEvaluationOfStepFinished(this.genAlgo);
    }
  }
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
