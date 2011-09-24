/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.monalisa.core;

import java.awt.image.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * Encapsulates the settings of the genetic algorithm.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class GAConfiguration
    extends Configuration implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  final private GAPhenotypeExpresser m_phenotypeExpresser;

  final private BufferedImage m_target;

  final private int m_maxPolygons;

  public GAConfiguration(BufferedImage a_target, int a_maxPolygons)
      throws InvalidConfigurationException {
    super();
    m_maxPolygons = a_maxPolygons;
    m_target = a_target;
    m_phenotypeExpresser = new GAPhenotypeExpresser(this);
    setBreeder(new GABreeder());
    setRandomGenerator(new StockRandomGenerator());
    setEventManager(new EventManager());
    BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
        this, 0.50d);
    bestChromsSelector.setDoubletteChromosomesAllowed(true);
    addNaturalSelector(bestChromsSelector, !false);
//    WeightedRouletteSelector wrs = new WeightedRouletteSelector(this);
//    addNaturalSelector(wrs, true);
    setMinimumPopSizePercent(0);
    setPreservFittestIndividual(true);
    setPopulationSize(5);
    setKeepPopulationSizeConstant(false);
    setFitnessEvaluator(new DeltaFitnessEvaluator());
    setFitnessFunction(new LMSFitnessFunction(this));
    setChromosomePool(new ChromosomePool());
    /*      addGeneticOperator(new CrossoverOperator(this, 0.35d));*/
    addGeneticOperator(new SingleMutationOperator(this, 2));
    addGeneticOperator(new PointMutationOperator(this, 8));
    addGeneticOperator(new ColorMutationOperator(this, 10));
    //addGeneticOperator(new AlphaMutationOperator(this, 200));
    //addGeneticOperator(new AlphaOffMutationOperator(this, 150));
    addGeneticOperator(new PolygonMutationOperator(this, 5));

    setSampleChromosome(GAInitialChromosomeFactory.create(this));
  }

  public BufferedImage getTarget() {
    return m_target;
  }

  public int getMaxPolygons() {
    return m_maxPolygons;
  }

  public GAPhenotypeExpresser getPhenotypeExpresser() {
    return m_phenotypeExpresser;
  }

  @Override
  public Object clone() {
    try {
      return new GAConfiguration(m_target, m_maxPolygons);
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -1);
      return null;
    }
  }
}
