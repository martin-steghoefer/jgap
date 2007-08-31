/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.*;
import org.jgap.gp.function.*;

import org.jgap.gp.*;

/**
 * Easily creates single and batched consistent command objects.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class CommandFactory {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static CommandGene[] createStoreCommands(CommandGene[] a_target,
                                                  GPConfiguration a_conf,
                                                  Class a_type, String a_prefix,
                                                  int a_count)
      throws InvalidConfigurationException {
    CommandGene[] result = new CommandGene[a_count * 2 + a_target.length];
    for (int i = 0; i < a_target.length; i++) {
      result[i] = a_target[i];
    }
    for (int i = 0; i < a_count; i++) {
      result[i * 2 + a_target.length] = new StoreTerminal(a_conf,
          a_prefix + i, a_type);
      result[i * 2 + 1 +
          a_target.length] = new ReadTerminal(a_conf, a_type,
          a_prefix + i);
    }
    return result;
  }

  public static CommandGene[] createWriteOnlyCommands(CommandGene[] a_target,
                                                  GPConfiguration a_conf,
                                                  Class a_type, String a_prefix,
                                                  int a_count,
                                                  boolean a_noValidation)
      throws InvalidConfigurationException {
    CommandGene[] result = new CommandGene[a_count + a_target.length];
    for (int i = 0; i < a_target.length; i++) {
      result[i] = a_target[i];
    }
    for (int i = 0; i < a_count; i++) {
      CommandGene writeCommand = new StoreTerminal(a_conf,
          a_prefix + i, a_type);
      writeCommand.setNoValidation(a_noValidation);
      result[i + a_target.length] = writeCommand;
    }
    return result;
  }

  public static CommandGene[] createReadOnlyCommands(CommandGene[] a_target,
                                                  GPConfiguration a_conf,
                                                  Class a_type, String a_prefix,
                                                  int a_count,
                                                  int a_startIndex,
                                                  boolean a_noValidation)
      throws InvalidConfigurationException {
    CommandGene[] result = new CommandGene[a_count + a_target.length];
    for (int i = 0; i < a_target.length; i++) {
      result[i] = a_target[i];
    }
    for (int i = 0; i < a_count; i++) {
      CommandGene readCommand = new ReadTerminal(a_conf, a_type,
                                                 a_prefix + (i + a_startIndex));
      readCommand.setNoValidation(a_noValidation);
      result[i + a_target.length] = readCommand;
    }
    return result;
  }

  public static CommandGene[] createStackCommands(CommandGene[] a_target,
                                                  GPConfiguration a_conf,
                                                  Class a_type)
      throws InvalidConfigurationException {
    CommandGene[] result = new CommandGene[a_target.length + 2];
    for (int i = 0; i < a_target.length; i++) {
      result[i] = a_target[i];
    }
    result[a_target.length] = new Push(a_conf, a_type);
    result[a_target.length + 1] = new Pop(a_conf, a_type);
    return result;
  }
}
