package org.jgap.xml;

/**
 * An ImproperXMLException will be thrown if an XML document
 * or element is parsed for genetic data, but is found to
 * be structured improperly or missing required data.
 */
public class ImproperXMLException extends Exception
{
  /**
   * Constructor.
   */
  public ImproperXMLException()
  {
    super();
  }


  /**
   * Constructor.
   *
   * @param message An error message describing the reason
   *                this exception was thrown.
   */
  public ImproperXMLException(String message)
  {
    super(message);
  }
}

