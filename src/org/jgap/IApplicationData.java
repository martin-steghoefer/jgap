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

package org.jgap;

/**
 * The Chromosome class allows to attach a custom object that is ignored by
 * the genetic operations. Because the Chromosome holding it can be cloned
 * and compared, the attached object should also. With this interface it is
 * forced that the application object attached to the Chromosome with the
 * setApplicationData-method is cloneable and comparable.
 *
 * @author Klaus Meffert
 * @since 2.0
 */

public interface IApplicationData extends Comparable, Cloneable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  public Object clone() throws CloneNotSupportedException;
}
