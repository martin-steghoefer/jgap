/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.wan;

import org.homedns.dade.jcgrid.message.*;
import org.jgap.distr.*;
import org.jgap.distr.grid.gp.*;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.util.*;

/**
 * WAN Utilities.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class WANUtils {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public WANUtils() {
  }

  public static String outputRequestInformation(ReceivedPacket a_packet) {
    GridMessageWorkRequest greq = (GridMessageWorkRequest) a_packet.getData();
    JGAPRequestGP req = (JGAPRequestGP) greq.getWorkRequest();
    MasterInfo worker = req.getRequesterInfo();
    String postfix = req.getID();
    String descr = req.getDescription();
    if (descr != null && descr.length() > 0) {
      postfix += "(" + descr + ")";
    }
    int popSize = req.getConfiguration().getPopulationSize();
    String popSizeS = "pop:" + popSize;
    postfix += ", " + popSizeS;
    return outputInformation(worker, postfix);
  }

  public static String outputRequestInformation(RequestInformation a_info) {
    MasterInfo worker = a_info.requesterInfo;
    String postfix = a_info.getID();
    String descr = a_info.getDescription();
    if (descr != null && descr.length() > 0) {
      postfix += "(" + descr + ")";
    }
    String reqDate;
    if (a_info.getRequestDate() == null) {
      reqDate = "unkn. date";
    }
    else {
      reqDate = DateKit.dateToString(a_info.getRequestDate(),
                                     DateKit.DATEFORMAT_FULL_1);
    }
    String title = a_info.getTitle();
    if (title == null) {
      title = "no title";
    }
    int popSize = a_info.getPopSize();
    String popSizeS = "pop:" + popSize;
    String s = worker.m_IPAddress + ":" + worker.m_name + " / " + postfix +
        " / " + title + " / " + reqDate + " / " + popSizeS;
    return s;
  }

  public static String outputResultInformation(ReceivedPacket a_packet) {
    JGAPResultGP res = (JGAPResultGP) a_packet.getData();
    MasterInfo worker = res.getWorkerInfo();
    String fitness = getFitnessString(res);
    String postfix = "";
    String descr = res.getDescription();
    if (descr != null && descr.length() > 0) {
      postfix += " (" + descr + ")";
    }
    String dur;
    long duration = res.getDurationComputation();
    if (duration > 1) {
      dur = " / " + (long) (duration / 1000) + "sec.";
    }
    else {
      dur = "";
    }
    return outputInformation(worker, res.getID()) + fitness + dur + postfix;
  }

  public static String outputResultInformation(ResultInformation a_info) {
    MasterInfo worker = a_info.workerInfo;
    String fitness = NumberKit.niceDecimalNumber(a_info.m_fittest, 2);
    String postfix = "";
    String respDate;
    if (a_info.getResponseDate() == null) {
      respDate = "unkn. date";
    }
    else {
      respDate = DateKit.dateToString(a_info.getResponseDate(),
                                      DateKit.DATEFORMAT_FULL_1);
    }
    String descr = a_info.getDescription();
    if (descr != null && descr.length() > 0) {
      postfix += " (" + descr + ")";
    }
    String dur;
    long duration = a_info.getDurationComputation();
    if (duration > 1) {
      dur = (long) (duration / 1000) + "sec.";
    }
    else {
      dur = "unkn. duration";
    }
    String title = a_info.getTitle();
    if (title == null) {
      title = "no title";
    }
    String ID = a_info.getID();
    int popSize = a_info.getPopSize();
    String popSizeS = "pop:" + popSize;
    postfix += ", " + popSizeS;
    String s = worker.m_IPAddress + ":" + worker.m_name + " / " + ID + "/ "
        + fitness + " / " + title + " / " + respDate + " / " + popSizeS + " / "
        + dur + postfix;
    return s;
  }

  public static String getFitnessString(JGAPResultGP res) {
    String fitness = " / fitness: ";
    if (res.getFittest() != null) {
      fitness = fitness + res.getFittest().getFitnessValue();
    }
    else {
      if (res.getPopulation() != null) {
        if (res.getPopulation().getGPProgram(0) != null) {
          fitness = fitness + NumberKit.niceDecimalNumber(
              res.getPopulation().getGPProgram(0).getFitnessValue(), 2);
        }
        else {
          fitness += "N/A";
        }
      }
      else {
        fitness += "N/A";
      }
    }
    return fitness;
  }

  public static String outputInformation(MasterInfo worker, String postFix) {
    String s = worker.m_IPAddress + ":" + worker.m_name + " / " + postFix;
    return s;
  }

  public static IGPProgram getFittest(JGAPResultGP a_res) {
    if (a_res == null) {
      return null;
    }
    if (a_res.getFittest() != null) {
      return a_res.getFittest();
    }
    if (a_res.getPopulation() == null) {
      return null;
    }
    return a_res.getPopulation().determineFittestProgram();
  }

  public static double getFitnessValue(IGPProgram a_prog) {
    if (a_prog == null) {
      return GPFitnessFunction.NO_FITNESS_VALUE;
    }
    return a_prog.getFitnessValue();
  }
}
