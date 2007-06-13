package org.jgap.util;

/**
 * Interface introducing the clone method that should already be within
 * java.lang.Cloneable!
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public interface ICloneable
    extends Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  Object clone();
}
