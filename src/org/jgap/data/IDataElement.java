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
 * The IDataElement interface represents an entity comparable to
 * org.w3c.dom.Element
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IDataElement {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  void setAttribute(String name, String value)
      throws Exception;

  void appendChild(IDataElement newChild)
      throws Exception;

  String getTagName();

  IDataElementList getElementsByTagName(String name);

  IDataElementList getChildNodes();

  String getAttribute(String name);

  Map getAttributes();
}
