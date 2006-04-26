package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;

public class SinCommand
    extends MathCommand {
  public SinCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 1, type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new SinCommand(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
  }

  public CommandGene mutateCommand() {
    return null; //new CosCommand();
  }

  public void evaluate(Configuration config, List parameters) {
    MathConfiguration mConfig = (MathConfiguration) config;
    double newResult = ( (Double) mConfig.popTerminal()).doubleValue();
    newResult = Math.sin(newResult);
    mConfig.pushTerminal(new Double(newResult));
  }

  public String toString() {
    return "sin";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float f = c.execute_float(n, 0, args);
    // clip to -10000 -> 10000
    return (float) Math.sin(Math.max( -10000.0f, Math.min(f, 10000.0f)));
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double f = c.execute_double(n, 0, args);
    // clip to -10000 -> 10000
    return Math.sin(Math.max( -10000.0, Math.min(f, 10000.0)));
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_sin();
  }

  public static interface Compatible {
    public Object execute_sin();
  }
}
