/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data;

/**
 * IDataElementList is an interface describing a list of IDataElement elements
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IDataElementList {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  IDataElement item(int a_index);

  int getLength();

  void add(IDataElement a_element);
}
