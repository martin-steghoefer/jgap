/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.math.*;
import java.util.*;

/**
 * Class used to help in the testing of the hashCode() method.
 *
 * @author John Serri
 */
public class TestHashcode {
  /** String containing the CVS revision. Read out via reflection! */
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Determines if object should output debug information.
   */
  private boolean m_verbose;

  /**
   * Minimum average value of hashCodes needed to pass the testDispersion()
   * test
   *
   * @see testDispersion(), m_AverageMax
   */
  private double m_AverageMin = 0.0d;

  /**
   * Maximum average value of hashCodes needed to pass the testDispersion()
   * test
   *
   * @see testDispersion(), m_AverageMin
   */
  private double m_AverageMax = 1.0d;

  /**
   * Minimum Standard Deviation value of hashCodes needed to pass the
   * testDispersion() test
   *
   * @see testDispersion(), m_StdDevMax
   */
  private double m_StdDevMin = 1.0d;

  /**
   * Maximum Standard Deviation value of hashCodes needed to pass the
   * testDispersion() test
   *
   * @see testDispersion(), m_StdDevMin
   */
  private double m_StdDevMax = 2.0d;

  /**
   * fraction of hashCodes that must be unique for the testHashCodeUniqueness
   * method to succed.
   *
   * @see testHashCodeUniqueness
   */
  private double m_fractionUnique = 0.9d;

  /**
   * Contains the real fraction of hashCodes that where found to be unique in
   * testHashCodeUniqueness.
   */
  private double m_actualFractionUnique = 0.0d;

  /**
   * Set whether the object will output debug information.
   *
   * @param a_verbose True if you want debug information, else set to false.
   */
  public void setVerbose(boolean a_verbose) {
    m_verbose = a_verbose;
  }

  /**
   * Set's the Maximum average value of hashCodes needed to pass the
   * testDispersion() test
   *
   * @param a_averageMax New Maximum average
   * @see testDispersion(), m_AverageMax
   */
  public void setAverageMax(double a_averageMax) {
    m_AverageMax = a_averageMax;
  }

  /**
   * Set's the Minimum average value of hashCodes needed to pass the
   * testDispersion() test
   *
   * @param a_averageMin New Minimum average
   * @see testDispersion(), m_AverageMin
   */
  public void setAverageMin(double a_averageMin) {
    m_AverageMin = a_averageMin;
  }

  /**
   * Set's the Maximum standard deviationvalue of hashCodes needed to pass
   * the testDispersion() test
   *
   * @param a_stdDevMax New Maximum standard deviation
   * @see testDispersion(), m_stdDevMax
   */
  public void setStdDevMax(double a_stdDevMax) {
    m_StdDevMax = a_stdDevMax;
  }

  /**
   * Set's the Minimum standard deviationvalue of hashCodes needed to pass
   * the testDispersion() test
   *
   * @param a_stdDevMin New Minimum standard deviation
   * @see testDispersion(), m_stdDevMin
   */
  public void setStdDevMin(double a_stdDevMin) {
    m_StdDevMin = a_stdDevMin;
  }

  /**
   * Set's the fraction of hashCodes that must be unique for the
   * testHashCodeUniqueness method to succed.
   *
   * @param a_fractionUnique New value. Must be between 0.0 and 1.0
   * @throws IllegalArgumentException
   * @see testHashCodeUniqueness
   */
  public void setFractionUnique(double a_fractionUnique) {
    if ( (a_fractionUnique < 0.0) || (a_fractionUnique > 1.0))
      throw new IllegalArgumentException(
          "fractionUnique must be between 0.0 and 1.0");
    m_fractionUnique = a_fractionUnique;
  }

  public double getActualFractionUnique() {
    return m_actualFractionUnique;
  }

  /**
   * Test that veridies that a the fraction of unique hashCodes is greater
   * than the one specified.
   *
   * @param a_ObjectList List of objects to test.
   * @return True if the fraction of unique hashCodes is greater than the one
   * specified. Else False
   * @see setFractionUnique
   * @author John Serri
   */
  public boolean testHashCodeUniqueness(List a_ObjectList) {
    boolean result = false;
    int index;
    int newvalue;
    int numObjects = a_ObjectList.size();
    Hashtable hashCodes = new Hashtable();
    Integer key;

    for (index = 0; index < numObjects; index++) {
      int hashcode = a_ObjectList.get(index).hashCode();
      key = new Integer(hashcode);
      if (hashCodes.containsKey(key) == true) {
        newvalue = ( (Integer) hashCodes.get(key)).intValue() + 1;
        hashCodes.put(key, new Integer(newvalue));
      }
      else {
        hashCodes.put(key, new Integer(1));
      }
    }
    m_actualFractionUnique = ( (double) hashCodes.size() / (double) numObjects);
    if (m_actualFractionUnique < m_fractionUnique)
      result = false;
    else
      result = true;
    return result;
  }

  /**
   * Test to make sure that equal objects all have the same hashCode
   *
   * @param a_ObjectList List of objects to test.
   * @return True if all hashCodes in List are the same. Else false.
   * @author John Serri
   */
  public boolean testHashCodeEquality(List a_ObjectList) {
    int index;
    int hashCode;
    long numObjects = a_ObjectList.size();

    if (numObjects < 2)
      return false;
    hashCode = a_ObjectList.get(0).hashCode();
    for (index = 1; index < numObjects; index++) {
      if (hashCode != a_ObjectList.get(index).hashCode())
        return false;
    }
    return true;
  }

  /**
   * Test that verifies if average and Standard deviation of hashCodes in
   * list match criteria.
   *
   * @param a_ObjectList List of objects to test.
   * @return True if average and Standard deviation of hashCodes in List
   * match criterias. Else false.
   * @see setAverageMax(),setAverageMin(),setStdDevMax(),setStdDevMin()
   *
   * @author John Serri
   * @since 2.1
   */
  public boolean testDispersion(List a_ObjectList) {
    int index;
    boolean result = false;
    int[] hashCodes = new int[a_ObjectList.size()];
    long numObjects = a_ObjectList.size();
    double average = 0;
    double stdDev;
    double sumOfSquare;
    double squareOfSum;

    for (index = 0; index < numObjects; index++) {
      hashCodes[index] = a_ObjectList.get(index).hashCode();
    }

    //Average
    for (index = 0; index < numObjects; index++) {
      average += hashCodes[index];
    }
    average /= numObjects;

    //STD Deviation
    sumOfSquare = 0;
    squareOfSum = 0;
    for (index = 0; index < numObjects; index++) {
      sumOfSquare += (double) hashCodes[index]
          * (double) hashCodes[index];
      squareOfSum += hashCodes[index];
    }
    squareOfSum *= squareOfSum;
    stdDev = (sumOfSquare * numObjects) - squareOfSum;
    stdDev /= numObjects * (numObjects - 1);
    stdDev = Math.sqrt(stdDev);

    if (m_verbose) {
      System.out.println("Average =" + average + " StdDev =" + stdDev);
      System.out.println("Average - StdDev =" + (average - stdDev));
      System.out.println("Average + StdDev =" + (average + stdDev));
    }

    if ( (m_AverageMin < average) && (average < m_AverageMax))
      result = true;
    else
      result = false;

    if ( (m_StdDevMin < stdDev) && (stdDev < m_StdDevMax))
      result &= true;
    else
      result &= false;
    return result;
  }

  /**
   * Simple main method to test the class
   *
   * @param args ignored
   * @author John Serri
   */
  public static void main(String[] args) {
    int com;
    TestHashcode th = new TestHashcode();
    List tl = new ArrayList();

    for (com = 600000; com < 600100; com++) {
      tl.add(new BigDecimal(com));
    }
    th.testDispersion(tl);
    th.setFractionUnique(0.8);
    th.testHashCodeUniqueness(tl);
  }
}
