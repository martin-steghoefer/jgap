package examples.grid.mathProblemDistributed;

import org.jgap.distr.grid.gp.IGPPopulationInitializer;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.CommandGene;
import org.jgap.gp.terminal.Variable;

/**
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class GPPopulationInitializer
    implements IGPPopulationInitializer {
  GPConfiguration m_conf;
  Class[] m_types;
  Class[][] m_argTypes;
  CommandGene[][] m_nodeSets;
  int m_maxNodes;
  boolean m_verboseOutput;
  Variable m_vx;

  public void setUp(GPConfiguration a_conf, Class[] a_types, Class[][] a_argTypes,
                    CommandGene[][] a_nodeSets, int a_maxNodes,
                    boolean a_verboseOutput)
      throws Exception {
    m_conf = a_conf;
    m_types = a_types;
    m_argTypes =a_argTypes;
    m_nodeSets = a_nodeSets;
    m_maxNodes = a_maxNodes;
    m_verboseOutput = a_verboseOutput;
  }

  public GPGenotype execute()
      throws InvalidConfigurationException {
    GPGenotype result = GPGenotype.randomInitialGenotype(m_conf, m_types, m_argTypes,
        m_nodeSets, m_maxNodes, m_verboseOutput);
    result.putVariable(m_vx);
    m_conf.putVariable(m_vx);
    return result;
  }

  public void setVariable(Variable vx) {
    m_vx = vx;
  }
}
