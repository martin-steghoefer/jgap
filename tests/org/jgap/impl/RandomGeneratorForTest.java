package org.jgap.impl;

import org.jgap.*;

/**
 * <p>Title: A random generator only determined for testing purposes</p>
 * <p>Description: With this, you can specify the next value returned</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class RandomGeneratorForTest implements RandomGenerator {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  private int _nextInt;
  private long _nextLong;
  private double _nextDouble;
  private float _nextFloat;
  private boolean _nextBoolean;

  public RandomGeneratorForTest() {
  }

  public int nextInt() {
    return _nextInt;
  }


  public int nextInt( int ceiling ) {
    return _nextInt % ceiling;
  }

  public long nextLong() {
   return _nextLong;
  }

  public double nextDouble() {
    return _nextDouble;
  }

  public float nextFloat() {
    return _nextFloat;
  }

  public boolean nextBoolean() {
    return _nextBoolean;
  }

  public void set_nextBoolean(boolean _nextBoolean) {
    this._nextBoolean = _nextBoolean;
  }
  public void set_nextDouble(double _nextDouble) {
    this._nextDouble = _nextDouble;
  }
  public void set_nextFloat(float _nextFloat) {
    this._nextFloat = _nextFloat;
  }
  public void set_nextInt(int _nextInt) {
    this._nextInt = _nextInt;
  }
  public void set_nextLong(long _nextLong) {
    this._nextLong = _nextLong;
  }
}