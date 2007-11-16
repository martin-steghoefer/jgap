/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import java.io.*;
import org.jgap.data.config.*;
import java.util.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import junit.framework.*;

/**
 * Tests the ConfigWriter class.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class ConfigWriterTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ConfigWriterTest.class);
    return suite;
  }

  /**
   * Get mock config data, use the ConfigWriter to write a property file
   * retrieve the written file, load it and check if the contents got
   * written correctly.
   *
   * @throws Exception
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void testConfigData_0()
      throws Exception {
    IConfigInfo ici = new MockConfigInfoForTesting();
    ConfigWriter.getInstance().write(ici); /**@todo write to stream*/
    // read the file and check the values
    int totalProps = 0;
    Properties props = new Properties();
    try {
      props.load(new FileInputStream("jgapTmp.con"));
          /**@todo read from stream*/
      ConfigData cd = ici.getConfigData();
      String nsPrefix = cd.getNS() + ".";
      String name;
      List values;
      for (int i = 0; i < cd.getNumLists(); i++) {
        name = cd.getListNameAt(i);
        values = cd.getListValuesAt(i);
        int idx = 0;
        for (Iterator iter = values.iterator(); iter.hasNext(); idx++) {
          // append an index for same key elements
          String tmpName = nsPrefix + name + "[" + idx + "]";
          try {
            assertEquals(props.getProperty(tmpName), (String) iter.next());
          } catch (Exception ex) {
            ex.printStackTrace();
            fail();
          }
          totalProps++;
        }
      }
      // test the TextField properties
      String value = "", tmpName = "";
      for (int i = 0; i < cd.getNumTexts(); i++) {
        name = cd.getTextNameAt(i);
        value = cd.getTextValueAt(i);
        tmpName = nsPrefix + name;
        try {
          assertEquals(props.getProperty(tmpName), value);
        } catch (Exception ex) {
          ex.printStackTrace();
          fail();
        }
        totalProps++;
      }
      assertEquals(totalProps, props.size());
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
      fail();
    }
  }

  /**
   * Test the reading of a config file and loading of the m_populatioSize
   * variable of the Configuration Configurable.
   *
   * @throws Exception
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void testConfigReader_0()
      throws Exception {
    Configuration.reset();
    Configuration conf = new Configuration("jgapTest.con", false);
    assertEquals(conf.getPopulationSize(), 35);
    // Some setup so that the Configuration doesn't throw an Exception. We do
    // not set the population size of the Configuration since this is supposed
    // to be read directly from the config file.
    // set up a sample chromosome
    Gene[] sampleGenes = new Gene[3];
    sampleGenes[0] = new IntegerGene(conf, 60, 100);
    sampleGenes[1] = new IntegerGene(conf, 1, 50);
    sampleGenes[2] = new IntegerGene(conf, 100, 150);
    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    FitnessFunction fitFunc = new StaticFitnessFunction(100.0d);
    conf.setFitnessFunction(fitFunc);
    // The higher the value, the better
    conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
    conf.setSampleChromosome(sampleChromosome);
    BestChromosomesSelector bestChromsSelector = new
        BestChromosomesSelector(conf, 1.0d);
    bestChromsSelector.setDoubletteChromosomesAllowed(false);
    conf.addNaturalSelector(bestChromsSelector, true);
    conf.setRandomGenerator(new StockRandomGenerator());
    conf.setEventManager(new EventManager());
    conf.addGeneticOperator(new CrossoverOperator(conf));
    conf.addGeneticOperator(new MutationOperator(conf, 15));
    Genotype.randomInitialGenotype(conf);
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetInstance_0()
      throws Exception {
    ConfigWriter inst = ConfigWriter.getInstance();
    assertSame(inst, ConfigWriter.getInstance());
  }
}
