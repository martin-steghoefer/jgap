/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

import java.util.*;

/**
 * Implements the IDataElementList interface and represents a list of IDataElement
 * elements.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DataElementList
    implements IDataElementList {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private List list;

  public DataElementList() {
    list = new Vector();
  }

  public IDataElement item(int index) {
    return (IDataElement) list.get(index);
  }

  public int getLength() {
    return list.size();
  }

  public void add(IDataElement element) {
    list.add(element);
  }
}
