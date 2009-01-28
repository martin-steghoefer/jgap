/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.monalisa.gui;

import java.awt.*;
import java.awt.image.*;
import org.apache.log4j.*;
import org.jfree.chart.*;
import org.jfree.data.xy.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;
import examples.gp.monalisa.core.*;

/**
 * Class in charge of actually running the evolution process.
 *
 * @author Yann N. Dauphin
 * @since 3.4
 */
public class EvolutionRunnable
    implements Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private transient static Logger LOGGER = Logger.getLogger(EvolutionRunnable.class);

  protected final GeneticDrawingView m_view;

  protected static boolean initView;

  public EvolutionRunnable(GeneticDrawingView a_view) {
    super();
    m_view = a_view;
  }

  public void run() {
    Configuration.reset();
    try {
      final DrawingGPConfiguration conf =
          new DrawingGPConfiguration(m_view.getTargetImage());
      JFreeChart chart = m_view.getChart();
      XYSeriesCollection sc = (XYSeriesCollection) chart.getXYPlot().getDataset();
      XYSeries series = sc.getSeries(0);
      series.clear();
      IEventManager eventManager = conf.getEventManager();
      eventManager.addEventListener(GeneticEvent.GPGENOTYPE_EVOLVED_EVENT,
                                    new GeneticEventListener() {
        /**
         * Updates the chart in the main view.
         *
         * @param a_firedEvent the event
         */
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          if (evno % 25 == 0) {
            double bestFitness = genotype.getFittestProgram().getFitnessValue();
            JFreeChart chart = m_view.getChart();
            XYSeriesCollection sc = (XYSeriesCollection) chart.getXYPlot().
                getDataset();
            XYSeries series = sc.getSeries(0);
            series.add(evno, bestFitness);
          }
        }
      });
      eventManager.addEventListener(GeneticEvent.GPGENOTYPE_NEW_BEST_SOLUTION,
                                    new GeneticEventListener() {
        private transient Logger LOGGER2 = Logger.getLogger(EvolutionRunnable.class);
        private DrawingGPProgramRunner gpProgramRunner = new
            DrawingGPProgramRunner(conf);

        /**
         * Display best solution in fittestChromosomeView's mainPanel.
         *
         * @param a_firedEvent the event
         */
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          IGPProgram best = genotype.getAllTimeBest();
          ApplicationData data = (ApplicationData)best.getApplicationData();
          LOGGER2.info("Num Points / Polygons: " + data.numPoints + " / " +
                      data.numPolygons);

          BufferedImage image = gpProgramRunner.run(best);
          Graphics g = m_view.getFittestDrawingView().getMainPanel().
              getGraphics();
          if (!initView) {
            m_view.getFittestDrawingView().setSize(204, 200 + 30);
            m_view.getFittestDrawingView().getMainPanel().setSize(200, 200);
            initView = true;
          }
          g.drawImage(image, 0, 0, m_view.getFittestDrawingView());
          if (m_view.isSaveToFile()) {
            int fitness = (int) best.getFitnessValue();
            String filename = "monalisa_"
                + NumberKit.niceNumber(fitness, 5, '_') + ".png";
            java.io.File f = new java.io.File(filename);
            try {
              javax.imageio.ImageIO.write(image, "png", f);
            } catch (java.io.IOException iex) {
              iex.printStackTrace();
            }
          }
        }
      });
      GPProblem problem = new DrawingProblem(conf);
      GPGenotype gp = problem.create();
      gp.setVerboseOutput(true);
      while (m_view.isEvolutionActivated()) {
        gp.evolve();
        gp.calcFitness();
        if (gp.getGPConfiguration().getGenerationNr() % 25 == 0) {
          String freeMB = SystemKit.niceMemory(SystemKit.getFreeMemoryMB());
          LOGGER.info("Evolving gen. " +
                      (gp.getGPConfiguration().getGenerationNr()) +
                      ", mem free: " + freeMB + " MB");
        }
      }
      // Create graphical tree from currently fittest image.
      // ---------------------------------------------------
      IGPProgram best = gp.getAllTimeBest();
      int fitness = (int) best.getFitnessValue();
      String filename = "monalisa_"
          + NumberKit.niceNumber(fitness, 5, '_') + ".png";
      problem.showTree(best, filename);
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -1);
    }
  }
}
