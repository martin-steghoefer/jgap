/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import junit.framework.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;
import org.jgap.gp.*;

/**
 * Tests the GPGenotype class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPGenotypeTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

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
      new GPGenotype(m_gpconf, pop, null, null, null, null, null, 1);
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
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    GPGenotype gen = new GPGenotype(m_gpconf, pop, null, null, null, null, null,
                                    1);
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
    /*@todo fix sequence of random numbers to correspond to adapted logic*/
    rn.setNextIntSequence(new int[] {0, 2, 1, 3, 1,
                          2, 8, 0, 7, 1, 5, 6, 4, 3});
    // Execute the functionality to test.
    // ----------------------------------
    m_gpconf.setPopulationSize(1);
    GPGenotype gen = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes,
        nodeSets, minDepths, maxDepths, 200, new boolean[] {true, true, false}, false);
    GPPopulation pop = gen.getGPPopulation();
    assertEquals(m_gpconf.getPopulationSize(), pop.size());
    // Evaluate program 1.
    // -------------------
    IGPProgram p = pop.getGPProgram(0);
    assertEquals(8, p.getChromosome(0).size());
    assertEquals(CMD_SUB_V_V, p.getChromosome(0).getNode(0));
    assertEquals(StoreTerminal.class, p.getChromosome(0).getNode(1).getClass());
    assertEquals(CMD_CONST1, p.getChromosome(0).getNode(2));
    assertEquals(SubProgram.class, p.getChromosome(0).getNode(3).getClass());
    assertEquals(StoreTerminal.class, p.getChromosome(0).getNode(4).getClass());
    // Evaluate program 2.
    // -------------------
    int node = 0;
    assertEquals(9, p.getChromosome(1).size());
    assertEquals(CMD_FOR, p.getChromosome(1).getNode(node++));
    assertEquals(Increment.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(Variable.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(CMD_SUB_V_V_V, p.getChromosome(1).getNode(node++));
    assertEquals(AddAndStore.class, p.getChromosome(1).getNode(node++).getClass());
    assertEquals(ReadTerminal.class,
                 p.getChromosome(1).getNode(node++).getClass());
    assertEquals(ReadTerminal.class,
                 p.getChromosome(1).getNode(node++).getClass());
    assertEquals(TransferMemory.class,
                 p.getChromosome(1).getNode(node++).getClass());
    assertEquals(TransferMemory.class,
                 p.getChromosome(1).getNode(node++).getClass());
    // Evaluate program 3.
    // -------------------
    assertEquals(1, p.getChromosome(2).size());
    assertEquals(ReadTerminal.class, p.getChromosome(2).getNode(0).getClass());
    assertEquals(0.0, computeFitness(p, vx), DELTA);
  }

  private double computeFitness(IGPProgram a_program, Variable vx) {
    double error = 0.0f;
    Object[] noargs = new Object[0];
    // Initialize local stores.
    // ------------------------
    a_program.getGPConfiguration().clearStack();
    a_program.getGPConfiguration().clearMemory();
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
          System.out.println("Arithmetic Exception with x = " + i);
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
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    GPGenotype gen = new GPGenotype(m_gpconf, pop, null, null, null, null, null,
                                    1);
    // Serialize genotype to a file.
    // -----------------------------
    assertEquals(gen, doSerialize(gen));
  }

  /**
   * Verifies that terminals are passed as clones during evolution and not
   * as references. Thanx a lot Javier for pointing this out nicely!
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testReferenceProblem_0()
      throws Exception {
    m_gpconf.setPopulationSize(30);
    m_gpconf.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    m_gpconf.setFitnessFunction(new TerminalsOnly());
    m_gpconf.setRandomGenerator(new StockRandomGenerator());
    //
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        new Increment(m_gpconf, CommandGene.IntegerClass, 1),
        new Terminal(m_gpconf, CommandGene.IntegerClass, 1.0d, 10000.0d),
    }
    };
    GPGenotype gen = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes,
        nodeSets, 10, false);
    gen.getGPPopulation().sort(new TerminalsFirstComparator());
    IGPProgram prog1 = gen.getGPPopulation().getGPProgram(0);
    ProgramChromosome chrom1 = prog1.getChromosome(0);
    Terminal gene1 = (Terminal) chrom1.getGene(0);
    IGPProgram prog2 = gen.getGPPopulation().getGPProgram(1);
    ProgramChromosome chrom2 = prog2.getChromosome(0);
    Terminal gene2 = (Terminal) chrom2.getGene(0);
    assertNotSame(gene1, gene2);
  }

  /**
   * Verifies that for different genotypes different configurations are
   * possible.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testConfigurationInstance_0() throws Exception {
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        new Increment(m_gpconf, CommandGene.IntegerClass, 1),
        new Terminal(m_gpconf, CommandGene.IntegerClass, 1.0d, 2.0d),
    }
    };
    m_gpconf.setPopulationSize(30);
    m_gpconf.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    m_gpconf.setFitnessFunction(new TerminalsOnly());
    m_gpconf.setRandomGenerator(new StockRandomGenerator());
    GPGenotype gen1 = GPGenotype.randomInitialGenotype(m_gpconf, types, argTypes,
        nodeSets, 10, false);
    GPConfiguration conf2 = new GPConfiguration(m_gpconf.getId()+"_2","noname");
    conf2.setPopulationSize(1);
    conf2.setGPFitnessEvaluator(new DefaultGPFitnessEvaluator());
    conf2.setFitnessFunction(new TerminalsOnly());
    conf2.setRandomGenerator(new StockRandomGenerator());
    GPGenotype gen2 = GPGenotype.randomInitialGenotype(conf2, types, argTypes,
        nodeSets, 4, false);
    assertNotSame(gen1, gen2);
    assertNotSame(gen1.getGPConfiguration(), gen2.getGPConfiguration());
    assertEquals(30, gen1.getGPConfiguration().getPopulationSize());
    assertEquals(1, gen2.getGPConfiguration().getPopulationSize());
  }

  /**
   * Output null solution should not produce an Exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public void testOutputSolution_0()
      throws Exception {
    GPPopulation pop = new GPPopulation(m_gpconf, 2);
    GPProgram prog = new GPProgram(m_gpconf, 2);
    ProgramChromosome chrom = new ProgramChromosome(m_gpconf, 2, prog);
    prog.setChromosome(0, chrom);
    prog.setChromosome(1, chrom);
    pop.setGPProgram(0, prog);
    pop.setGPProgram(1, prog);
    GPGenotype gen = new GPGenotype(m_gpconf, pop, null, null, null, null, null,
                                    1);
    gen.outputSolution(null);
  }

  class TerminalsOnly
      extends GPFitnessFunction {
    protected double evaluate(IGPProgram a_subject) {
      ProgramChromosome chrom1 = a_subject.getChromosome(0);
      CommandGene gene1 = chrom1.getGene(0);
      if (gene1 instanceof Terminal) {
        return gene1.execute_double(null, 0, new Object[] {});
      }
      else {
        return 999999.0d;
      }
    }
  }
  class TerminalsFirstComparator
      implements java.util.Comparator {
    public int compare(Object o1, Object o2) {
      IGPProgram prog1 = (IGPProgram)o1;
      IGPProgram prog2 = (IGPProgram)o2;
      CommandGene gene1 = prog1.getChromosome(0).getGene(0);
      CommandGene gene2 = prog2.getChromosome(0).getGene(0);
      boolean o1is, o2is;
      if (gene1 instanceof Terminal) {
        o1is = true;
      }
      else {
        o1is = false;
      }
      if (gene2 instanceof Terminal) {
        o2is = true;
      }
      else {
        o2is = false;
      }
      if (o1is) {
        if (o2is) {
          return 0;
        }
        else {
          return -1;
        }
      }
      else {
        if (o2is) {
          return 1;
        }
        else {
          return 0;
        }
      }
    }

    public boolean equals(Object obj) {
      return compare(this, obj) == 0;
    }
  }
}
