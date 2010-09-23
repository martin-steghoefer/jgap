/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.monalisa.core;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import examples.gp.monalisa.core.commands.*;

/**
 * DrawingProblem encapsulates the GP configuration of the commands to use for
 * composing a picture from polygons.
 *
 * @author Yann N. Dauphin
 * @author Klaus Meffert (finalization, tuning)
 * @since 3.4
 */
public class DrawingProblem
    extends GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public DrawingProblem(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  public static CommandGene SUBPROGRAM;

  /**
   * Generates an inital genotype for the drawing problem.
   *
   * @return an initial Genotype
   * @throws InvalidConfigurationException in case of an error
   */
  @Override
  public GPGenotype create()
      throws InvalidConfigurationException {
    DrawingGPConfiguration conf = (DrawingGPConfiguration) getGPConfiguration();
    Class[] retTypes = {CommandGene.VoidClass};
    Class[][] argTypes = { {}
    };
    SUBPROGRAM = new SubProgram(conf, new Class[] {Void.class, Void.class, Void.class}, true);
    CommandGene[][] nodeSets = { {
        SUBPROGRAM,
        new SubProgram(conf, new Class[] {Void.class, Void.class}, true),
//        new SubProgram(conf, new Class[] {Void.class, Void.class, Void.class,
//                       Void.class}, true),
//        new SubProgram(conf, new Class[] {Void.class, Void.class, Void.class,
//                       Void.class, Void.class}, true),
//        new SubProgram(conf, new Class[] {Void.class, Void.class, Void.class,
//                       Void.class, Void.class, Void.class}, true),
        new SubProgram(conf, 4, Void.class, 2, 7, true),
        new SubProgram(conf, 6, Void.class, 5, 10, true),
        new SubProgram(conf, 8, Void.class, 7, 15, true),
        new PointConstructor(conf),
        new PolygonConstructor(conf, 5, true),
        new ColorConstructor(conf),
        new DrawPolygon(conf),
        new Terminal(conf, CommandGene.FloatClass, 0.0d, 1.0d, false),
        new Terminal(conf, CommandGene.IntegerClass, 0,
                     conf.getTarget().getWidth() - 1, true,
                     TerminalType.WIDTH.intValue()),
        new Terminal(conf, CommandGene.IntegerClass, 0,
                     conf.getTarget().getHeight() - 1, true,
                     TerminalType.HEIGHT.intValue()), }
    };
    int[] minDepth = new int[] {12};
    int[] maxDepth = new int[] {50};
    int maxNodes = 3000;
    boolean[] fullMode = new boolean[] {true};
    return GPGenotype.randomInitialGenotype(conf, retTypes, argTypes, nodeSets,
        minDepth, maxDepth, maxNodes, fullMode, true);
  }

  /**
   * TerminalType helps differentiate the terminals that have different ranges.
   */
  public enum TerminalType {
    WIDTH(1),
    HEIGHT(2), ;

    private int m_value;

    public int intValue() {
      return m_value;
    }

    TerminalType(int a_value) {
      m_value = a_value;
    }
  }
}
