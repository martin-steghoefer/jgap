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

import junit.framework.*;
import junitx.util.*;

/**
 * Tests for Population class
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class PopulationTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public PopulationTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(PopulationTest.class);
    return suite;
  }
  
  public void testConstruct_0() {
      try
      {
        Population pop = new Population(null);
        fail();
      }
      catch (NullPointerException e)
      {
          //this is OK        
          return;
      }
      //any other exception we fail
      fail();
  }

  public void testConstruct_1() {
      try
      {
        Population pop = new Population(-1);    
      }
      catch (IllegalArgumentException iae)
      {
          //this is ok
          return;
      }
      //any other exception we fail
      fail();
  }
  
  public void testConstruct_2() {
      Population pop = new Population();    
      this.assertNotNull(pop);
  }
  
  public void testConstruct_3() {
      int nTot = 100;
      Chromosome[] chromosomes = new Chromosome[nTot];
      Population pop = new Population(chromosomes);
      assertNotNull(pop);

      //check size is correct
      assertEquals(pop.size(), nTot);
  }
}
