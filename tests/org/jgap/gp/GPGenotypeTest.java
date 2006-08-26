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
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
    } catch (IllegalArgumentException iex) {
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
    Class[][] argTypes = { {}, {}, {}
    };
    int[] minDepths = new int[] {3, 4, 1};
    int[] maxDepths = new int[] {3, 4, 1};
    CommandGene[][] nodeSets = { {
        CMD_SUB_V_V, //0
        CMD_CONST1, //1
        new StoreTerminal(m_gpconf, "mem0", CommandGene.IntegerClass), //2
        new StoreTerminal(m_gpconf, "mem1", CommandGene.IntegerClass), //3
    }, {
        vx = Variable.create(m_gpconf, "X", CommandGene.IntegerClass), //0
        new AddAndStore(m_gpconf, CommandGene.IntegerClass, "mem2"), //1
        CMD_FOR, //2
        new TransferMemory(m_gpconf, "mem2", "mem1"), //3
        new TransferMemory(m_gpconf, "mem1", "mem0"), //4
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem0"), //5
        new ReadTerminal(m_gpconf, CommandGene.IntegerClass, "mem1"), //6
        CMD_SUB_V_V_V, //7
        new Increment(m_gpconf, CommandGene.IntegerClass, -1), //8
    }, {
    }
    };
    // Add commands working with internal memory.
    // ------------------------------------------
    nodeSets[2] = CommandFactory.createReadOnlyCommands(nodeSets[2], m_gpconf,
        CommandGene.IntegerClass, "mem", 1, 2, !true);
    // Execute the functionality to test.
    // ----------------------------------
    rn.setNextIntSequence(new int[] {0, 2, 1, 3, 1,
                          2, 8, 0, 7, 1, 5, 6, 4, 3});
    m_gpconf.setPopulationSize(1);
    GPGenotype gen = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes,
        nodeSets, minDepths, maxDepths, 200, new boolean[] {true, true, false});
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
    assertSame(CMD_CONST1, p.getChromosome(0).getNode(4));
    // Evaluate program 2
    // ------------------
    int node = 0;
    assertEquals(9, p.getChromosome(1).size());
    assertSame(CMD_FOR, p.getChromosome(1).getNode(node++));
    assertEquals(Increment.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(Variable.class, p.getChromosome(1).getNode(node++).getClass());
    assertSame(CMD_SUB_V_V_V, p.getChromosome(1).getNode(node++));
    assertEquals(AddAndStore.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(ReadTerminal.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(ReadTerminal.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(TransferMemory.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(TransferMemory.class, p.getChromosome(1).getNode(node++).getClass());
    // Evaluate program 3
    // ------------------
    assertEquals(1, p.getChromosome(2).size());
    assertEquals(ReadTerminal.class, p.getChromosome(2).getNode(0).getClass());
    assertEquals(0.0, computeFitness(p, vx), DELTA);
  }

  private double computeFitness(GPProgram a_program, Variable vx) {
    double error = 0.0f;
    Object[] noargs = new Object[0];
    // Initialize local stores.
    // ------------------------
    GPGenotype.getGPConfiguration().clearStack();
    GPGenotype.getGPConfiguration().clearMemory();
    // Compute fitness for each program.
    // ---------------------------------
    for (int i = 2; i < 15; i++) {
      for (int j = 0; j < a_program.size(); j++) {
        vx.set(new Integer(i));
        try {
          try {
            // Only evaluate after whole GP program was run.
            // ---------------------------------------------
            if (j == a_program.size() - 1) {
              double result = a_program.execute_int(j, noargs);
              error += Math.abs(result - fib_iter(i));
            }
            else {
              a_program.execute_void(j, noargs);
            }
          } catch (IllegalStateException iex) {
            error = Double.MAX_VALUE / 2;
            break;
          }
        } catch (ArithmeticException ex) {
          System.out.println("x = " + i);
          System.out.println(a_program.getChromosome(j));
          throw ex;
        }
      }
    }
    return error;
  }

  private int fib_iter(int a_index) {
    // 1
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    // 2
    int a = 1; //Store("mem0", Constant(1))
    int b = 1; //Store("mem1", Constant(1))
    int x = 0; //Store("mem2", Constant(0))
    // 3
    for (int i = 2; i <= a_index; i++) { //FORX (Subprogram(A;B;C))
      x = a + b; // A: AddAndStore(Read("mem0"),Read("mem1"),"mem2")
      a = b; //B: TransferMemory("mem1","mem0")
      b = x; //C: TransferMemory("mem2","mem1")
    }
    return x; //Read("mem2")
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
    // Serialize genotype to a file.
    // -----------------------------
    assertEquals(gen, doSerialize(gen));
  }
}
