/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.homedns.dade.jcgrid.message.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.impl.*;

import com.thoughtworks.xstream.*;

/**
 * Specialized version of XStream for JGAP.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class JGAPGPXStream
    extends XStream {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";
  public JGAPGPXStream() {
    super();
    init(this);
  }

  protected void init(XStream a_xstream) {
    // Use aliases to reduce storage capacity.
    // ---------------------------------------
    a_xstream.alias("conf", Configuration.class);
    a_xstream.alias("gpconf", GPConfiguration.class);
    a_xstream.alias("gppop", GPPopulation.class);
    a_xstream.alias("gpprgbase", GPProgramBase.class);
    a_xstream.alias("gpprg", GPProgram.class);
    a_xstream.alias("basegpchrom", BaseGPChromosome.class);
    a_xstream.alias("prgchrom", ProgramChromosome.class);
    a_xstream.alias("cgene", CommandGene.class);
    a_xstream.alias("requestgp", JGAPRequestGP.class);
    a_xstream.alias("resultgp", JGAPResultGP.class);
    a_xstream.alias("factory", JGAPFactory.class);
    a_xstream.alias("stockrandomgen", StockRandomGenerator.class);
    a_xstream.alias("branchxover", BranchTypingCross.class);
    a_xstream.alias("defclonehandler", DefaultCloneHandler.class);
    a_xstream.alias("defcomphandler", DefaultCompareToHandler.class);
    a_xstream.alias("definit", DefaultInitializer.class);
    a_xstream.alias("gridmessworkreq", GridMessageWorkRequest.class);
    a_xstream.alias("gridmessworkres", GridMessageWorkResult.class);

    a_xstream.alias("eventman", EventManager.class);
    a_xstream.alias("defgpfiteval", DefaultGPFitnessEvaluator.class);
    a_xstream.alias("gpfitfunc", GPFitnessFunction.class);
    a_xstream.alias("tournsel", org.jgap.gp.impl.TournamentSelector.class);
    a_xstream.alias("void", java.lang.Void.class);
    a_xstream.alias("Jbool", java.lang.Boolean.class);
    a_xstream.alias("Jint", java.lang.Integer.class);
    a_xstream.alias("Jdouble", java.lang.Double.class);
    a_xstream.alias("Jfloat", java.lang.Float.class);
    a_xstream.alias("random", java.util.Random.class);
    a_xstream.alias("nop", NOP.class);
  }
}
