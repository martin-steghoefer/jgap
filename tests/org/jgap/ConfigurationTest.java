/*
 * Copyright 2003 Klaus Meffert
 *
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

import org.jgap.event.EventManager;
import org.jgap.impl.BooleanGene;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.GaussianRandomGenerator;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.ReproductionOperator;
import org.jgap.impl.StaticFitnessFunction;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.WeightedRouletteSelector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for Configuration class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ConfigurationTest
    extends TestCase
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.5 $";

    public ConfigurationTest ()
    {

    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (ConfigurationTest.class);
        return suite;
    }

    public void testAddGeneticOperator_0 ()
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.addGeneticOperator (null);
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testAddGeneticOperator_1 ()
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.lockSettings ();
            conf.addGeneticOperator (new MutationOperator ());
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testAddGeneticOperator_2 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        conf.addGeneticOperator (new MutationOperator ());
        assertEquals (1, conf.getGeneticOperators ().size ());
        conf.addGeneticOperator (new MutationOperator ());
        assertEquals (2, conf.getGeneticOperators ().size ());
    }

    public void testVerifyStateIsValid_0 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        try
        {
            conf.verifyStateIsValid ();
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_1 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        try
        {
            conf.verifyStateIsValid ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_2 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), false);
        try
        {
            conf.verifyStateIsValid ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_3 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), true);
        conf.setRandomGenerator (new StockRandomGenerator ());
        try
        {
            conf.verifyStateIsValid ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_4 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), false);
        conf.setRandomGenerator (new StockRandomGenerator ());
        conf.setEventManager (new EventManager ());
        try
        {
            conf.verifyStateIsValid ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_5 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), true);
        conf.setRandomGenerator (new StockRandomGenerator ());
        conf.setEventManager (new EventManager ());
        conf.addGeneticOperator (new MutationOperator ());
        try
        {
            conf.verifyStateIsValid ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testVerifyStateIsValid_6 ()
        throws
        InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), true);
        conf.setRandomGenerator (new StockRandomGenerator ());
        conf.setEventManager (new EventManager ());
        conf.addGeneticOperator (new MutationOperator ());
        conf.setPopulationSize (1);
        conf.verifyStateIsValid ();
    }

    public void testIsLocked_0 ()
        throws InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        conf.setFitnessFunction (new StaticFitnessFunction (2));
        Gene gene = new BooleanGene ();
        conf.setSampleChromosome (new Chromosome (gene, 5));
        conf.addNaturalSelector (new WeightedRouletteSelector (), false);
        conf.setRandomGenerator (new StockRandomGenerator ());
        conf.setEventManager (new EventManager ());
        conf.addGeneticOperator (new MutationOperator ());
        conf.setPopulationSize (1);
        assertEquals (false, conf.isLocked ());
        conf.lockSettings ();
        assertEquals (true, conf.isLocked ());
    }

    public void testGetter_0 ()
        throws InvalidConfigurationException
    {
        Configuration conf = new Configuration ();
        assertEquals (false, conf.isLocked ());
        FitnessFunction fitFunc = new StaticFitnessFunction (2);
        conf.setFitnessFunction (fitFunc);
        Gene gene = new BooleanGene ();
        Chromosome sample = new Chromosome (gene, 55);
        conf.setSampleChromosome (sample);
        NaturalSelector natSel = new WeightedRouletteSelector ();
        conf.addNaturalSelector (natSel, false);
        RandomGenerator randGen = new StockRandomGenerator ();
        conf.setRandomGenerator (randGen);
        EventManager evMan = new EventManager ();
        conf.setEventManager (evMan);
        GeneticOperator mutOp = new MutationOperator ();
        conf.addGeneticOperator (mutOp);
        GeneticOperator repOp = new ReproductionOperator ();
        conf.addGeneticOperator (repOp);
        GeneticOperator croOp = new CrossoverOperator ();
        conf.addGeneticOperator (croOp);
        conf.setPopulationSize (7);
        assertEquals (fitFunc, conf.getFitnessFunction ());
        assertEquals (natSel, conf.getNaturalSelectors (false).get (0));
        assertEquals (randGen, conf.getRandomGenerator ());
        assertEquals (sample, conf.getSampleChromosome ());
        assertEquals (evMan, conf.getEventManager ());
        assertEquals (7, conf.getPopulationSize ());
        assertEquals (3, conf.getGeneticOperators ().size ());
        assertEquals (mutOp, conf.getGeneticOperators ().get (0));
        assertEquals (repOp, conf.getGeneticOperators ().get (1));
        assertEquals (croOp, conf.getGeneticOperators ().get (2));
    }

    /**
     * Tests a deprecated function!
     * @throws Exception
     */
    public void testSetNaturalSelector_0 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        NaturalSelector selector = new WeightedRouletteSelector ();
        conf.setNaturalSelector (selector);
        assertEquals (selector, conf.getNaturalSelectors (false).get (0));
    }

    /**
     * Tests a deprecated function!
     */
    public void testGetNaturalSelector_0 ()
    {
        Configuration conf = new Configuration ();
        NaturalSelector selector = conf.getNaturalSelector ();
        assertEquals (null, selector);
    }

    public void testAddNaturalSelector_0 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        NaturalSelector selector = new WeightedRouletteSelector ();
        conf.addNaturalSelector (selector, true);
        assertEquals (selector, conf.getNaturalSelectors (true).get (0));
    }

    public void testAddNaturalSelector_1 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        NaturalSelector selector = new WeightedRouletteSelector ();
        conf.addNaturalSelector (selector, false);
        assertEquals (selector, conf.getNaturalSelectors (false).get (0));
    }

    public void testAddNaturalSelector_2 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        NaturalSelector selector1 = new WeightedRouletteSelector ();
        NaturalSelector selector2 = new WeightedRouletteSelector ();
        conf.addNaturalSelector (selector1, false);
        assertEquals (selector1, conf.getNaturalSelectors (false).get (0));
        conf.addNaturalSelector (selector2, true);
        assertEquals (selector2, conf.getNaturalSelectors (true).get (0));
        assertEquals (selector1, conf.getNaturalSelectors (false).get (0));
        try
        {
            assertEquals (null, conf.getNaturalSelectors (false).get (1));
            fail ();
        }
        catch (Exception ex)
        {
            ; //this is OK
        }
    }

    public void setFitnessFunction_0 ()
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.setFitnessFunction (null);
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void setBulkFitnessFunction_0 ()
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.setBulkFitnessFunction (null);
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void setBulkFitnessFunction_1 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        conf.setFitnessFunction (new TestFitnessFunction ());
        try
        {
            conf.setBulkFitnessFunction (new TestBulkFitnessFunction ());
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testGetPopulationSize_0 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        assertEquals (0, conf.getPopulationSize ());
        final int SIZE = 22;
        conf.setPopulationSize (SIZE);
        assertEquals (SIZE, conf.getPopulationSize ());
    }

    public void testSetPopulationSize_1 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.setPopulationSize (0);
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testLock_0 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        try
        {
            conf.lockSettings ();
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testLock_1 ()
        throws Exception
    {
        Configuration conf = new Configuration ();
        conf.setFitnessFunction (new TestFitnessFunction ());
        Gene gene = new BooleanGene ();
        Chromosome sample = new Chromosome (gene, 55);
        conf.setSampleChromosome (sample);
        conf.addNaturalSelector (new WeightedRouletteSelector (), false);
        conf.setRandomGenerator (new GaussianRandomGenerator ());
        conf.setEventManager (new EventManager ());
        conf.addGeneticOperator (new MutationOperator ());
        conf.setPopulationSize (1);
        conf.lockSettings ();
        assertEquals (true, conf.isLocked ());
        try
        {
            conf.verifyChangesAllowed ();
            fail ();
        }
        catch (InvalidConfigurationException invex)
        {
            ; //this is OK
        }
    }

    public void testSetSampleChromosome_0() throws Exception {
        Configuration conf = new Configuration ();
        Gene gene = new BooleanGene ();
        Chromosome sample = new Chromosome (gene, 55);
        try {
            conf.setSampleChromosome (null);
            fail();
        } catch (InvalidConfigurationException invex) {
            ; //this is OK
        }
    }
    public void testGetChromosomeSize_0() throws Exception {
        Configuration conf = new Configuration ();
        Gene gene = new BooleanGene ();
        final int SIZE = 55;
        Chromosome sample = new Chromosome (gene, SIZE);
        conf.setSampleChromosome (sample);
        assertEquals(SIZE, conf.getChromosomeSize());
    }
}

class TestFitnessFunction
    extends FitnessFunction
{
    protected int evaluate (Chromosome a_subject)
    {
        //result does not matter here
        return 1;
    }

}

class TestBulkFitnessFunction
    extends BulkFitnessFunction
{
    public void evaluate (Chromosome[] a_subjects)
    {
    }

 }