/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.audit;

import org.jgap.*;

/**
 * Gathers statistical data and returns them on request.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class Evaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private PermutingConfiguration m_conf;

  private DefaultKeyedValues2D m_data;

  public Evaluator(PermutingConfiguration a_conf) {
    if (a_conf == null) {
      throw new IllegalArgumentException("Configuration must not be null!");
    }
    m_conf = a_conf;
    m_data = new DefaultKeyedValues2D();
  }

  public boolean hasNext() {
    return m_conf.hasNext();
  }

  public Configuration next() throws InvalidConfigurationException {
    return m_conf.next();
  }

  public void setValue(double a_value, Comparable a_rowKey, Comparable a_columnKey) {
    setValue(new Double(a_value), a_rowKey, a_columnKey);
  }

  public void setValue(Number value, Comparable rowKey, Comparable columnKey) {
      m_data.setValue(value, rowKey, columnKey);
//      fireDatasetChanged();
  }

  public Number getValue(Comparable rowKey, Comparable columnKey) {
      return m_data.getValue(rowKey, columnKey);
  }

 public DefaultKeyedValues2D getData() {
   return m_data;
 }
}
