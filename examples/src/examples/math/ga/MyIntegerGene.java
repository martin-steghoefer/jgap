/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.ga;

import org.jgap.*;

/**
 *
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MyIntegerGene
    extends BaseGene implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Integer mValue;

  private int mMin = Integer.MIN_VALUE;

  private int mMax = Integer.MAX_VALUE;

  public MyIntegerGene(Configuration theConfiguration)
      throws InvalidConfigurationException {
    this(theConfiguration, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  public MyIntegerGene(Configuration theConfiguration, int theMin, int theMax)
      throws InvalidConfigurationException {
    super(theConfiguration);
    mValue = 0;
    mMin = theMin;
    mMax = theMax;
  }

  protected Object getInternalValue() {
    return mValue;
  }

  protected Gene newGeneInternal() {
    try {
      return new MyIntegerGene(getConfiguration(), mMin, mMax);
    } catch (InvalidConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  public void setAllele(Object o) {
    if (o instanceof Integer) {
      Integer aInt = (Integer) o;
      // keep it within bounds
      aInt = Math.min(aInt, mMax);
      aInt = Math.max(aInt, mMin);
      mValue = aInt;
    }
  }

  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    throw new RuntimeException("NYI");
  }

  public void setValueFromPersistentRepresentation(String s)
      throws UnsupportedOperationException, UnsupportedRepresentationException {
    throw new RuntimeException("NYI");
  }

  public void setToRandomValue(RandomGenerator theRandomGenerator) {
    double randomValue = (mMax - mMin) * theRandomGenerator.nextDouble() + mMin;
    setAllele(new Integer( (int) Math.round(randomValue)));
  }

  public void applyMutation(int i, double v) {
    double range = (mMax - mMin) * v;
    if (getAllele() == null) {
      setAllele(new Integer( (int) range + mMin));
    }
    else {
      int newValue = (int) Math.round(mValue + range);
      setAllele(new Integer(newValue));
    }
  }

  public int compareTo(Object o) {
    return mValue.compareTo( (Integer) ( (MyIntegerGene) o).getAllele());
  }
}
