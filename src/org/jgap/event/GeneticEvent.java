/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
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
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Public constant representing the name of the event that is fired each
   * time a Genotype is finished with a single evolution cycle.
   */
  public static final String GENOTYPE_EVOLVED_EVENT =
      "genotype_evolved_event";

  /**
   * References the name of this event instance.
   */
  private final String m_eventName;

  /**
   * Constructs a new GeneticEvent of the given name.
   *
   * @param a_eventName The name of the event.
   * @param a_source The genetic object that acted as the source of the
   *                 event. The type of this object will be dependent on
   *                 the kind of event (which can be identified by the
   *                 event name). It may not be null.
   *
   * @throws IllegalArgumentException if the given source object is null.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public GeneticEvent(String a_eventName, Object a_source) {
    super(a_source);
    m_eventName = a_eventName;
  }

  /**
   * Retrieves the name of this event, which can be used to identify the
   * type of event.
   *
   * @return the name of this GeneticEvent instance.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public String getEventName() {
    return m_eventName;
  }
}
