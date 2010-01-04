/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.event;

import java.util.EventObject;

/**
 * Represents events that are fired via the EventManager when various
 * genetic events occur. The specific kind of event is conveyed through the
 * event name. Standard event names are provided as constants in this class.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class GeneticEvent
    extends EventObject {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  /**
   * Multi-purpose value object
   */
  private Object m_value;

  /**
   * Public constant representing the name of the event that is fired each
   * time a Genotype is finished with a single evolution cycle.
   */
  public static final String GENOTYPE_EVOLVED_EVENT =
      "genotype_evolved_event";

  public static final String GPGENOTYPE_EVOLVED_EVENT =
      "gpgenotype_evolved_event";

  public static final String GPGENOTYPE_NEW_BEST_SOLUTION =
      "gpgenotype_best_solution";

  /**
   * Fired before a genetic operator, liek mutation or crossing over, is
   * executed.
   */
  public static final String BEFORE_GENETIC_OPERATOR =
      "before_genetic_operator";

  /**
   * Fired after a genetic operator, liek mutation or crossing over, is
   * executed.
   */
  public static final String AFTER_GENETIC_OPERATOR =
      "after_genetic_operator";

  /**
   * References the name of this event instance.
   */
  private final String m_eventName;

  /**
   * Constructs a new GeneticEvent of the given name.
   *
   * @param a_eventName the name of the event
   * @param a_source the genetic object that acted as the source of the event.
   * The type of this object will be dependent on the kind of event (which can
   * be identified by the event name). It may not be null
   *
   * @throws IllegalArgumentException if the given source object is null
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public GeneticEvent(final String a_eventName, final Object a_source) {
    super(a_source);
    m_eventName = a_eventName;
  }

  /**
   * Constructs a new GeneticEvent of the given name.
   *
   * @param a_eventName the name of the event
   * @param a_source the genetic object that acted as the source of the event.
   * The type of this object will be dependent on the kind of event (which can
   * be identified by the event name). It may not be null
   * @param a_value informative value of the event
   *
   * @throws IllegalArgumentException if the given source object is null
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public GeneticEvent(final String a_eventName, final Object a_source,
                      final Object a_value) {
    this(a_eventName, a_source);
    m_value = a_value;
  }

  /**
   * Retrieves the name of this event, which can be used to identify the
   * type of event.
   *
   * @return the name of this GeneticEvent instance
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public String getEventName() {
    return m_eventName;
  }

  /**
   * @return multi-purpose value of the event
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object getValue() {
    return m_value;
  }
}
