package org.jgap.util;

import java.util.*;

/**
 * Interface introducing the clone method that should already be within
 * java.util.Cloneable!
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public interface ICloneable
    extends Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  Object clone();
}
