package org.jgap.impl;

import junit.framework.*;

/**
 * <p>Title: Tests for MutationOperator class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class MutationOperatorTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public MutationOperatorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(MutationOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    MutationOperator mutOp = new MutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertEquals(false, mutOp.m_dynamicMutationRate);
  }

  public void testConstruct_1() {
    MutationOperator mutOp = new MutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(true, mutOp.m_dynamicMutationRate);
  }

  public void testOperate_0() {
    /**@todo implement*/
  }
}