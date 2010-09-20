/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.gp.impl.*;
import org.jgap.util.*;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.*;

/**
 * Converts an XStream to a GPPopulation object.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class GPPopulationConverter
    extends XStreamPassThruConverter implements Converter {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public GPPopulationConverter(XStream a_xstream, GPConfiguration a_conf) {
    super(a_xstream, GPPopulation.class);
    setConfiguration(a_conf);
  }

  public Object doUnmarshal(UnmarshallingContext context)
      throws Exception {
    GPPopulation pop = new GPPopulation();
    pop = (GPPopulation)unmarshalDefault(context, pop);
    setConfiguration(pop, "m_config");
    pop.sortByFitness();
    return pop;
  }
}
