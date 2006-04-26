package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;

public class SubtractCommand
    extends MathCommand {
  public SubtractCommand(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 2, a_type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new SubtractCommand(getConfiguration(), getReturnType());
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
    /**@todo ist überflüssig, sollte aber möglich sein, um manuell übersteuern
     * zu können
     */
    return null; //new AddCommand();
  }

  public void evaluate(Configuration config, List parameters) {
    MathConfiguration mConfig = (MathConfiguration) config;
    double newResult = ( (Double) mConfig.popTerminal()).doubleValue();
    Double d1 = (Double) mConfig.popTerminal();
    newResult = newResult - d1.doubleValue();
    mConfig.pushTerminal(new Double(newResult));
  }

  public String toString() {
    return "-";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) - c.execute_int(n, 1, args);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) - c.execute_long(n, 1, args);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) - c.execute_float(n, 1, args);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) - c.execute_double(n, 1, args);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_subtract(c.
        execute_object(n, 1, args));
  }

  public static interface Compatible {
    public Object execute_subtract(Object o);
  }
}
