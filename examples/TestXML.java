import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.xml.*;
import org.w3c.dom.*;

public class TestXML
{
  public static void main(String[] args)
  {
    try
    {
      Configuration gaConf = new DefaultConfiguration();
    
      gaConf.setChromosomeSize(8);
      gaConf.setPopulationSize(10);
      gaConf.setFitnessFunction(new MaxFunction());

      // Test Chromsome manipulation methods
      Chromosome chromosome = Chromosome.randomInitialChromosome(gaConf);
      Document chromosomeDoc = XMLManager.getChromosomeAsDocument(chromosome);
      Chromosome chromosomeFromXML =
        XMLManager.getChromosomeFromDocument(gaConf, chromosomeDoc);

      if (!(chromosomeFromXML.equals(chromosome)))
      {
        System.out.println("Chromosome test failed.");
        System.exit(-1);
      }

      // Test Genotype manipulation methods
      Genotype genotype = Genotype.randomInitialGenotype(gaConf);
    
      Document genotypeDoc = XMLManager.getGenotypeAsDocument(genotype);
      Genotype genotypeFromXML = 
        XMLManager.getGenotypeFromDocument(gaConf, genotypeDoc);

      if (!(genotypeFromXML.equals(genotype)))
      {
        System.out.println("Genotype test failed.");
        System.exit(-1);
      }
    }
    catch (Exception e)
    { 
      System.out.println("Test failed due to error:");
      e.printStackTrace();
    }

    System.out.println("Tests successful.");
  }
}

