package org.jgap.impl;

import java.util.*;
import junit.framework.*;
import junitx.util.PrivateAccessor;
import org.jgap.impl.*;
import org.jgap.event.*;
import org.jgap.*;

/**
 * <p>Title: Tests for DefaultConfiguration class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class DefaultConfigurationTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public DefaultConfigurationTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultConfigurationTest.class);
    return suite;
  }

  public void testConstruct_0() {
    Configuration conf = new DefaultConfiguration();
    assertEquals(EventManager.class,conf.getEventManager().getClass());
    assertEquals(WeightedRouletteSelector.class,conf.getNaturalSelector().getClass());
    assertEquals(StockRandomGenerator.class,conf.getRandomGenerator().getClass());
    assertEquals(ChromosomePool.class,conf.getChromosomePool().getClass());
    assertEquals(3, conf.getGeneticOperators().size());
  }

}