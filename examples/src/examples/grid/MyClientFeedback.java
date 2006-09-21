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

public interface MyClientFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  public void setProgressMinimum(int min);

  public void setProgressMaximum(int max);

  public void setProgressValue(int val);

  public void beginWork();

  public void sendingFragmentRequest(MyRequest req);

  public void receivedFragmentResult(MyRequest req, MyResult res,
                                     int idx);

  public void endWork();

  public void completeFrame(int idx);

  public void error(String msg, Exception ex);
}
