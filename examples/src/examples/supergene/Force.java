/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.supergene;

/**
 * Solve the change problem using force method. This class was used to
 * verify if the solution exists in general.
 *
 * @author Audrius Meskauskas
 */
public final class Force {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static boolean REPORT_ENABLED = true;

  private Force() {

  }

  /**
   * Check the existence of a soulution.
   *
   * @param a_sum the sum needed
   * @return true if the change can be expressed in coins, satisfying
   * pennies mod 2 = nickels mod 2
   *
   * @author Audrius Meskauskas
   */
  public static boolean solve(int a_sum) {
    for (int q = 0; q < 4; q++) {
      for (int d = 0; d < 10; d++) {
        for (int n = 0; n < 20; n++) {
          for (int p = 0; p < 99; p++) {
            if (AbstractSupergeneTest.amountOfChange(q, d, n, p) == a_sum) {
              if (p % 2 == n % 2) {
                if (REPORT_ENABLED) {
                  System.out.println("Force " + a_sum + ": " + q
                                     + " quarters " + d + " dimes "
                                     + n + " nickels " + p + " pennies");
                }
                return true;
              }
            }
          }
        }
      }
    }
    if (REPORT_ENABLED) {
      System.out.println("Force " + a_sum + ": no solution");
    }
    return false;
  }

  /**
   * Test the Force method itself.
   *
   * @param args ignored
   */
  public static void main(String[] args) {
    for (int i = 1; i <= 100; i++) {
      solve(i);
    }
  }

}
