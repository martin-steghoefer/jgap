package examples.audit;

import java.util.*;

public class Permutator {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  private List list;

  private List nextList;

  private int[] counter;

  public Permutator(List list) {
    this.list = list;
    counter = new int[list.size()];
    for (int i = 0; i < counter.length; i++) {
      counter[i] = 0;
    }
    if (list.size() != 1) {
      increment();
    }
    else {
      nextList = list;
    }
  }

  /**
   * Finds the next permutation. If there is no next permutation {@linkplain
   * #nextList} will be set to null.
   */
  private void increment() {
    boolean pairwiseUnequale;
    boolean incremented;

    do {

      //increment counter
      incremented = false;
      for (int i = counter.length - 1; i >= 0; i--) {
        counter[i]++;
        if (counter[i] < list.size()) {
          incremented = true;
          break;
        }
        else {
          counter[i] = 0;
        }
      }

      //digits must be pairwise unequal
      pairwiseUnequale = true;
      pairwiseTestLoop:
          for (int i = 0; i < counter.length - 1; i++) {
        for (int j = i + 1; j < counter.length; j++) {
          if (counter[i] == counter[j]) {
            pairwiseUnequale = false;
            break pairwiseTestLoop;
          }
        }
      }

    }
    while (!pairwiseUnequale && incremented);

    //set next list
    if (incremented) {
      //next permutation available
      List newList = new ArrayList(list.size());
      for (int i = 0; i < counter.length; i++) {
        newList.add(list.get(counter[i]));
      }
      nextList = newList;
    }
    else {
      //no next permutation
      nextList = null;
    }
  }

  /**
   * @return true if the iteration has more permutations
   */
  public boolean hasNext() {
    return (nextList != null);
  }

  /**
   * @return the next permutation in the iteration
   * @throws NoSuchElementException there are no more permutations
   */
  public List next()
      throws NoSuchElementException {
    if (nextList == null) {
      throw new NoSuchElementException();
    }
    List ret = nextList;
    increment();
    return ret;
  }

  /**
   * Returns a subset of a given list acording to the index given.
   * If a bit in the binary number represented by the index is set then the
   * element at this index in the list will be included in the result list
   * @param index int
   * @param list List
   * @return List
   */
  public static List nextList(int index, List list) {
    if (index <= 0) {
      index = 1;
    }
    else {
      index++;
    }
    List newList = new Vector();
    for(int i=0;i<list.size();i++) {
      if ((index & (int)Math.pow(2,(i+1-1)))> 0) {
        newList.add(list.get(i));
      }
    }

    return newList;
  }

  public static void main(String[] args) {
    List l = new Vector();
    l.add("a");
    l.add("b");
    l.add("c");
//    l.add("d");
    List menge;
    for (int i=0;i<7;i++) {
      menge = Permutator.nextList(i, l);
      System.err.println(menge);
//      Permutator p = new Permutator(menge);
//      while (p.hasNext()) {
//        String s = p.next().toString();
//        System.err.println(s);
//      }
    }
  }
}
