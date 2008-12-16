package org.jgap.util;

/**
 * Interface introducing the clone method that should already be there with
 * java.lang.Cloneable!
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public interface ICloneable
    extends Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * @return clone of the current object instance
   */
  Object clone();
}
