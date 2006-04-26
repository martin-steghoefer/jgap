package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.impl.*;

/**@todo not needed any more as different approach is used*/
public class MathLanguage
    extends AbstractLanguage {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  private List commands;

  public MathLanguage() {
    commands = new Vector();
    // Commands with zero parameters
    List commands0 = new Vector();
    commands0.add(Terminal.class);
    commands0.add(Variable.class);
    commands.add(commands0);
    // Commands with one parameter
    List commands1 = new Vector();
    commands1.add(SinCommand.class);
    commands1.add(CosCommand.class);
    commands.add(commands1);
    // Commands with two parameters
    List commands2 = new Vector();
    commands2.add(AddCommand.class);
    commands2.add(MultiplyCommand.class);
    commands.add(commands2);
  }

  public int defaultProgramSize() {
    return 14;
  }

  public String getLabel() {
    return "Mathematical Language";
  }

  /**@todo already done elsewhere! fullNode --> zusammenführen*/
  public void fillProgram(ProgramChromosome chrom)
      throws InvalidConfigurationException {
    chrom.clearProgram();
    Configuration conf = chrom.getConfiguration();
    RandomGenerator rn = new StockRandomGenerator();
    List commands2 = (List) commands.get(2);
    List commands0 = (List) commands.get(0);
    for (int i = 0; i < defaultProgramSize() / 7; i++) {
      int ran;
      if (true) {
        ran = rn.nextInt(commands0.size());
        switch (ran) {
          case 0:
            Terminal term = new Terminal(conf);
            term.setValue(rn.nextDouble() * (rn.nextInt(ran + 1) + 1));
            chrom.addCommand(term);
            break;
          case 1:
            Variable var = new Variable(conf, "X", CommandGene.doubleClass);
            chrom.addCommand(var);
            break;
        }
      }
      if (true) {
        ran = rn.nextInt(commands0.size());
        switch (ran) {
          case 0:
            Terminal term = new Terminal(conf);
            term.setValue(rn.nextDouble() * rn.nextInt(ran + 1));
            chrom.addCommand(term);
            break;
          case 1:
            Variable var = new Variable(conf, "X", CommandGene.doubleClass);
            chrom.addCommand(var);
            break;
        }
      }
      //now a 2-param command
      ran = rn.nextInt(commands2.size());
      switch (ran) {
        case 0:
          chrom.addCommand(new AddCommand(conf, CommandGene.doubleClass));
          break;
        case 1:
          chrom.addCommand(new MultiplyCommand(conf, CommandGene.doubleClass));
          break;
        case 2:
          chrom.addCommand(new SinCommand(conf, CommandGene.doubleClass));
          break;
        case 3:
          chrom.addCommand(new CosCommand(conf, CommandGene.doubleClass));
          break;
      }
    }
    //now a 2-param command
    int ran = rn.nextInt(commands2.size());
    switch (ran) {
      case 0:
        chrom.addCommand(new AddCommand(conf, CommandGene.doubleClass));
        break;
      case 1:
        chrom.addCommand(new MultiplyCommand(conf, CommandGene.doubleClass));
        break;
    }
  }

  public static void printProgram(ProgramChromosome chrom) {
    for (int i = 0; i < chrom.size(); i++) {
      Gene gene = chrom.getGene(i);
      System.out.print(gene + " ");
    }
  }
}
