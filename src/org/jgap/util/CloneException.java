package org.jgap.util;

/**
 * Exception that occurs during cloning an object.
 *
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class CloneException
    extends RuntimeException {
  public CloneException() {
    super();
  }

  public CloneException(Throwable a_throwable) {
    super(a_throwable);
  }

  public CloneException(String a_message) {
    super(a_message);
  }
}
