package org.jgap.supergenes;

/** Solve the change problem using force method. This class was used to
 * verify if the solution exists in general.
 * @author Audrius Meskauskas
 */

public class Force {

    /** Check the existence of soulution.
     * @return true if the change can be expressed in coins,
     * satisfying pennies mod 2 = nickels mod 2 .
     */

    public static boolean solve(int a_sum)
     {
         for (int q = 0; q<4; q++)
         for (int d = 0; d<10; d++)
         for (int n = 0; n<20; n++)
         for (int p = 0; p<99; p++)
          {
              if (abstractSupergeneTest.amountOfChange(q,d,n,p)==a_sum)
                  if ( p % 2 == n % 2)
                      {  if (REPORT_ENABLED)
                          System.out.println("Force "+a_sum+": "+q+
                          " quarters "+d+" dimes "+
                              n+" nickels "+p+" pennies");
                          return true;
                      }
          }

          if (REPORT_ENABLED)
           System.out.println("Force "+a_sum+": no solution");
          return false;
     }

     /** Test the Force method itself. */
     public static void main(String[] args) {
         for (int i = 1; i <=100;  i++) {
             solve(i);
         }
     }

     public static boolean REPORT_ENABLED = true;

}
