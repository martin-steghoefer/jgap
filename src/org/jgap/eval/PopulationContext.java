package org.jgap.eval;

import org.jgap.*;

public class PopulationContext {

  private Population m_pop;
  private NaturalSelector m_selector;
  private GeneticOperator m_operator;
  private IChromosome m_chromosome;
  private BulkFitnessFunction m_bulkFitnessFunction;

  public PopulationContext(Population a_pop) {
    m_pop = a_pop;
  }

  public Population getPopulation() {
    return m_pop;
  }

  public IChromosome getChromosome() {
    return m_chromosome;
  }

  public GeneticOperator getOperator() {
    return m_operator;
  }

  public NaturalSelector getSelector() {
    return m_selector;
  }

  public void setChromosome(IChromosome m_chromosome) {
    this.m_chromosome = m_chromosome;
  }

  public void setOperator(GeneticOperator m_operator) {
    this.m_operator = m_operator;
  }

  public void setSelector(NaturalSelector m_selector) {
    this.m_selector = m_selector;
  }

  public BulkFitnessFunction getBulkFitnessFunction() {
    return m_bulkFitnessFunction;
  }

  public void setBulkFitnessFunction(BulkFitnessFunction
                                       m_bulkFitnessFunction) {
    this.m_bulkFitnessFunction = m_bulkFitnessFunction;
  }
}
