/*
 * Copyright 2001-2003 Neil Rotstan
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
package examples;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.impl.BooleanGene;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;


public class TestXML
{
    public static void main( String[] args )
    {
        try
        {
            Configuration activeConfiguration = new DefaultConfiguration();

            activeConfiguration.setSampleChromosome(
                new Chromosome( new BooleanGene(), 8 ) );
            activeConfiguration.setPopulationSize( 10 );
            activeConfiguration.setFitnessFunction( new MaxFunction() );

            // Test Chromsome manipulation methods
            // -----------------------------------
            Chromosome chromosome =
                Chromosome.randomInitialChromosome( activeConfiguration );

            Document chromosomeDoc =
                XMLManager.representChromosomeAsDocument( chromosome );

            Chromosome chromosomeFromXML =
                XMLManager.getChromosomeFromDocument( activeConfiguration,
                                                      chromosomeDoc );

            if ( !( chromosomeFromXML.equals( chromosome ) ) )
            {
                System.out.println( "Chromosome test failed." );
                System.exit( -1 );
            }

            // Test Genotype manipulation methods
            // ----------------------------------
            Genotype genotype =
                Genotype.randomInitialGenotype( activeConfiguration );

            Document genotypeDoc =
                XMLManager.representGenotypeAsDocument( genotype );

            Genotype genotypeFromXML =
                XMLManager.getGenotypeFromDocument( activeConfiguration, genotypeDoc );

            if ( !( genotypeFromXML.equals( genotype ) ) )
            {
                System.out.println( "Genotype test failed." );
                System.exit( -1 );
            }
        }
        catch ( Exception e )
        {
            System.out.println( "Test failed due to error:" );
            e.printStackTrace();
        }

        System.out.println( "Tests successful." );
    }
}

