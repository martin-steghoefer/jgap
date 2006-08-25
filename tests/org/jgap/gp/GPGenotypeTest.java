/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import junit.framework.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;

/**
 * Tests the GPGenotype class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPGenotypeTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GPGenotypeTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0()
      throws Exception {
    GPPopulation pop = new GPPopulation(m_gpconf, 5);
    try {
      new GPGenotype(m_gpconf, pop);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1()
      throws Exception {
    GPPopulation pop = new GPPopulation(m_gpconf, 2);
    GPProgram prog = new GPProgram(m_gpconf, 2);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    GPGenotype gen = new GPGenotype(m_gpconf, pop);
    assertSame(pop, gen.getGPPopulation());
  }

  public void testRandomInitialize_0()
      throws Exception {
    Variable vx;
    Class[] types = {
        CommandGene.VoidClass, CommandGene.VoidClass, CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}, {}, {}
    };
    int[] minDepths = new int[] {3, 4, 1};
    int[] maxDepths = new int[] {3, 4, 1};
    CommandGene[][] nodeSets = {
        {
          CMD_SUB_V_V,
              CMD_CONST1,
              CMD_CONST0,
        new StoreTerminal(m_gpconf, "mem0", CommandGene.IntegerClass),//3
        new StoreTerminal(m_gpconf, "mem1", CommandGene.IntegerClass),//4
    }, {
        vx = Variable.create(m_gpconf, "X", CommandGene.IntegerClass),//0
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"),//1
        CMD_FOR,//2
        new TransferMemory(m_gpconf, "mem2", "mem1"),//3
        new TransferMemory(m_gpconf, "mem1", "mem0"),//4
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem0"),//5
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem1"),//6
        CMD_SUB_V_V_V,//7
    }, {
    }
    };
    // Add commands working with internal memory.
    // ------------------------------------------
    nodeSets[2] = CommandFactory.createReadOnlyCommands(nodeSets[2], m_gpconf,
        CommandGene.IntegerClass, "mem", 1, 2, !true);
    // Execute the functionality to test.
    // ----------------------------------
    rn.setNextIntSequence(new int[] {3, 1, 4, 2,
                          0, 7, 1, 5, 6, 4, 3});
    m_gpconf.setPopulationSize(1);
    GPGenotype gen = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes,
        nodeSets, minDepths, maxDepths, new boolean[] {true, true, false});
    GPPopulation pop = gen.getGPPopulation();
    assertEquals(m_gpconf.getPopulationSize(), pop.size());
    // Evaluate program 1
    // ------------------
    GPProgram p = pop.getGPProgram(0);
    assertEquals(5, p.getChromosome(0).size());
    assertSame(CMD_SUB_V_V, p.getChromosome(0).getNode(0));
    assertEquals(StoreTerminal.class, p.getChromosome(0).getNode(1).getClass());
    assertSame(CMD_CONST1, p.getChromosome(0).getNode(2));
    assertEquals(StoreTerminal.class, p.getChromosome(0).getNode(3).getClass());
    assertSame(CMD_CONST0, p.getChromosome(0).getNode(4));
    // Evaluate program 2
    // ------------------
    assertEquals(8, p.getChromosome(1).size());
    assertSame(CMD_FOR, p.getChromosome(1).getNode(0));
    assertEquals(Variable.class, p.getChromosome(1).getNode(1).getClass());
    assertSame(CMD_SUB_V_V_V, p.getChromosome(1).getNode(2));
    assertEquals(AddAndStore.class, p.getChromosome(1).getNode(3).getClass());
    assertEquals(ReadTerminal.class, p.getChromosome(1).getNode(4).getClass());
    assertEquals(ReadTerminal.class, p.getChromosome(1).getNode(5).getClass());
    assertEquals(TransferMemory.class, p.getChromosome(1).getNode(6).getClass());
    assertEquals(TransferMemory.class, p.getChromosome(1).getNode(7).getClass());
    // Evaluate program 3
    // ------------------
    assertEquals(1, p.getChromosome(2).size());
    assertEquals(ReadTerminal.class, p.getChromosome(2).getNode(0).getClass());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    GPPopulation pop = new GPPopulation(m_gpconf, 2);
    GPProgram prog = new GPProgram(m_gpconf, 2);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    GPGenotype gen = new GPGenotype(m_gpconf, pop);
    GPGenotype gen2 = (GPGenotype) doSerialize(gen);
    assertSame(gen, gen2);
    /**@todo implement equals and compareTo and divide GPGenotype from Genotype
     * to make this test pass*/
  }
}
