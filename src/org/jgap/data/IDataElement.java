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
  final static String CVS_REVISION = "$Revision: 1.1 $";

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
