package org.jgap.gp;

public abstract class CrossMethod {
  private transient GPConfiguration m_configuration;

  public CrossMethod(GPConfiguration a_configuration) {
    m_configuration = a_configuration;
  }

  public GPConfiguration getConfiguration() {
    return m_configuration;
  }

  public abstract ProgramChromosome[] cross(ProgramChromosome i1,
                                            ProgramChromosome i2);
}
