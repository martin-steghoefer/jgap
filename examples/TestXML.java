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
    
      Genotype genotype = Genotype.randomInitialGenotype(gaConf);
    
      Document genotypeDoc = XMLManager.getGenotypeAsDocument(genotype);
      Genotype genotypeFromXML = 
        XMLManager.getGenotypeFromDocument(gaConf, genotypeDoc);

      if (genotypeFromXML.equals(genotype))
      {
        System.out.println("Test successful.");
      }
      else
      {
        System.out.println("Test failed.");
      }  
    }
    catch (Exception e)
    { 
      System.out.println("Test failed due to error:");
      e.printStackTrace();
    }
  }
}

