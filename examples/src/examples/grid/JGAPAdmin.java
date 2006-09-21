/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.homedns.dade.jcgrid.admin.*;
import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.gui.*;
import java.util.*;

public class JGAPAdmin {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public JGAPAdmin()
      throws Exception {
    GridAdmin admin = new GridAdmin();
    admin.start();
    while (true) {
      List v = admin.getWorkerStats();
      System.out.println("Number of workers: " + v.size());
      Iterator it = v.iterator();
      while (it.hasNext()) {
        WorkerStats stat = (WorkerStats) it.next();
        System.out.println(" " + stat.getName() + " / " + stat.getWorkingFor() +
                           " / " + stat.getStatus() + " / " + stat.getUnitSec());
      }
      Thread.sleep(1000);
    }
  }

  public static void main(String[] args)
      throws Exception {
    //start admin
    new JGAPAdmin();
//    new guiJCGridAdminStatus(..);
  }
}
