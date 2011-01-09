package examples.gp.tictactoe;

import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.CommandGene;
import org.jgap.gp.ISingleNodeValidator;

/**
 * Valides single nodes during GP program creation to make this process more
 * efficient.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class SingleNodeValidator implements ISingleNodeValidator{

  /**
   * Checks if the given a_function is allowed in the current context.
   *
   * @param a_chromIndex index of the chromosome within the GP program
   * @param a_pc the instance of the ProgramChromosome
   * @param a_functionSet the current function set within the program chromosome
   * (given as input parameter for convenience, could also be determined via
   * a_pc.m_genes)
   * @param a_function the function that should be added, if allowed
   * @param a_returnType the class the return type is required to have
   * @param a_subReturnTyp the sub return type the return type is required to
   * have
   * @param m_index the index of the up-to-date last command gene in the
   * program chromosome
   * @return true if a_function's class exists within a_functionSet
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public boolean isAllowed(int a_chromIndex, ProgramChromosome a_pc,
                           CommandGene[] a_functionSet, CommandGene a_function,
                           Class a_returnType, int a_subReturnTyp, int m_index) {
    CommandGene[] m_genes = a_pc.getFunctions();
    Class clazz = a_function.getClass();
    if(clazz == org.jgap.gp.function.Loop.class) {
      // Max. two loops per chromosome allowed.
      // --------------------------------------
      if(a_pc.contains(a_functionSet, a_function,2) >= 2) {
        return false;
      }
    }
    if(clazz == org.jgap.gp.function.SubProgram.class) {
      // Max. four sub programs per chromosome allowed.
      // --------------------------------------
      if(a_pc.contains(a_functionSet, a_function,4) >= 4) {
        return false;
      }
    }
    if(clazz == org.jgap.gp.function.ForLoop.class) {
      // Max. two loops per chromosome allowed.
      // --------------------------------------
      if(a_pc.contains(a_functionSet, a_function,2) >= 2) {
        return false;
      }
    }
    if(clazz == org.jgap.gp.function.IncrementMemory.class
        || clazz == org.jgap.gp.function.Increment.class) {
      /**@todo provide convenient method setNonConsecutive(boolean) in CommandGene*/
      if(m_index > 0 && m_genes[m_index-1].getClass() ==  clazz) {
        return false;
      }
    }
    // Increment of constant or terminal is more or less useless
    if(clazz == org.jgap.gp.terminal.Constant.class
        || clazz == org.jgap.gp.terminal.Terminal.class) {
      if (m_index > 0
          && m_genes[m_index - 1].getClass()
          == org.jgap.gp.function.IncrementMemory.class) {
        return false;
      }
    }
    switch(a_chromIndex) {
      case 0:
        break;
      case 2:
        if(m_index == 0 && (
            clazz != org.jgap.gp.function.SubProgram.class
           || ((org.jgap.gp.function.SubProgram)a_function).getArity(null) < 3)) {
          return false;
        }
        if(m_index == 1 && (
            clazz != org.jgap.gp.function.SubProgram.class
           && clazz != examples.gp.tictactoe.CountStones.class)) {
          return false;
        }
        if (m_index > 3 && clazz == examples.gp.tictactoe.CountStones.class) {
          return false;
        }
        if (m_index > 3 &&
            !a_pc.contains(a_functionSet, examples.gp.tictactoe.CountStones.class)) {
          return false;
        }
        break;
    }
    return true;
  }

}
