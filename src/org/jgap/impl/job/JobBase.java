package org.jgap.impl.job;

public abstract class JobBase
    implements IJob {
  private Object m_data;

  public JobBase(Object a_data) {
    m_data = a_data;
  }

  public void run() {
    try {
      execute(m_data);
    } catch (Exception ex) {
      /**@todo what to do here?*/
      ex.printStackTrace();
    }
  }

  public Object getData() {
    /**@todo maybe we should make the returned m_data immutable (via cloning, e.g.)*/
    return m_data;
  }
}
