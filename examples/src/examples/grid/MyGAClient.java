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

import org.homedns.dade.jcgrid.message.*;
import org.homedns.dade.jcgrid.client.*;

public class MyGAClient
    extends Thread {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GridNodeClientConfig clientCfg;

  private MyClientFeedback clientFeedback;

  private MyRequest workReq;

  public MyGAClient(GridNodeClientConfig cfg, MyClientFeedback feedback,
                    MyRequest req) {
    clientCfg = cfg;
    clientFeedback = feedback;
    workReq = req;
  }

  public void run() {
    try {
      // Start Client
      GridClient gc = new GridClient();
      gc.setNodeConfig(clientCfg);
      gc.start();
      try {
        // Splitting the work
        MyRequest[] workList;
        workList = workReq.split();
        clientFeedback.setProgressMaximum(0);
        clientFeedback.setProgressMaximum(workList.length - 1);
        clientFeedback.beginWork();
        // Sending work requests
        for (int i = 0; i < workList.length; i++) {
          MyRequest req = workList[i];
          clientFeedback.sendingFragmentRequest(req);
          gc.send(new GridMessageWorkRequest(req));
          if (this.isInterrupted())
            break;
        }
        // Receiving work results
        for (int i = 0; i < workList.length; i++) {
          clientFeedback.setProgressValue(i + workList.length);
          GridMessageWorkResult gmwr = (GridMessageWorkResult) gc.recv();
          MyResult workResult = (MyResult) gmwr.getWorkResult();
          int idx = workResult.getRID();
          clientFeedback.receivedFragmentResult(workList[idx], workResult,
              idx);
          clientFeedback.completeFrame(idx);
          if (this.isInterrupted()) {
            break;
          }
        }
      } finally {
        try {
          gc.stop();
        } catch (Exception ex) {}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      clientFeedback.error("Error while doing the work", ex);
    }
    clientFeedback.endWork();
  }
}
