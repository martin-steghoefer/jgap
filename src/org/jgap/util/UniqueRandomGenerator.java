package org.jgap.util;

import org.jgap.*;
import java.util.*;

public class UniqueRandomGenerator {
  private int m_upper;

  private List m_resultSet;

  private RandomGenerator m_generator;

  public UniqueRandomGenerator(int a_upperBoundary, RandomGenerator a_generator) {
    m_upper = a_upperBoundary;
    m_resultSet = new Vector();
    m_generator = a_generator;
    for (int i = 0; i < m_upper; i++) {
      m_resultSet.add(new Integer(i));
    }
  }

  public int nextInt()
      throws IllegalStateException {
    int size = m_resultSet.size();
    int index;
    if (size < 1) {
      throw new IllegalStateException("No more numbers left");
    }
    else if (size == 1) {
      index = 0;
    }
    else {
      index = m_generator.nextInt(size);
    }
    Integer result = (Integer) m_resultSet.get(index);
    m_resultSet.remove(index);
    return result.intValue();
  }
}
