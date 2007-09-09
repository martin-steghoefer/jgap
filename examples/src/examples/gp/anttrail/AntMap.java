/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

/**
 * Holds the map of the ant trail. Important: Clone intentionally not supported
 * here!
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class AntMap {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  // map point descriptions
  public static final int ERROR = 0;

  public static final int FOOD = -1;

  public static final int EMPTY = 1;

  public static final int TRAIL = 2;

  public static final int ATE = 3;

  // orientations
  public static final int O_UP = 0;

  public static final int O_LEFT = 1;

  public static final int O_DOWN = 2;

  public static final int O_RIGHT = 3;

  private int m_orientation;

  private int m_posx;

  private int m_posy;

  private int m_foodTaken;

  /**
   * Number of moves proceeded.
   */
  private int m_moves;

  /**
   * Width of map.
   */
  private int m_sizex;

  /**
   * Height of map.
   */
  private int m_sizey;

  /**
   * Holder of the trail's map.
   */
  private int[][] m_map;

  /**
   * For displaying moves as a..za..z etc. (idea from ECJ).
   * Also see m_moveModUpper
   */
  private int m_moveMod;

  /**
   * False: use lower case chars with m_moveMod. true: Use Upper case chars.
   */
  private boolean m_moveModUpper;

  /**
   * Stores the moves done to display them later.
   */
  private int[][] m_movementMap;

  /**
   * Maximum number of solutions allowed.
   */
  private int m_maxMoves;

  public AntMap(final int[][] a_map, int a_maxMoves) {
    m_sizex = a_map.length;
    m_sizey = a_map[0].length;
    m_map = new int[m_sizex][m_sizey];
    for (int x = 0; x < m_sizex; x++) {
      m_map[x] = (int[]) (a_map[x].clone());
    }
    m_movementMap = new int[m_sizex][m_sizey];/**@todo speedup possible by using string?*/
    m_orientation = O_RIGHT;
    m_posx = 0;
    m_posy = 0;
    m_moveMod = 0;
    m_moveModUpper = false;
    m_maxMoves = a_maxMoves;
    storeMove();
    m_moves = 0;
  }

  public int[][] getMap() {
    return m_map;
  }

  public int getFromMap(int a_x, int a_y) {
    return m_map[a_x][a_y];
  }

//  public void setInMap(int a_x, int a_y, int a_value) {
//    m_map[a_x][a_y] = a_value;
//  }

  public int getPosX() {
    return m_posx;
  }

  public int getPosY() {
    return m_posy;
  }

  public void setPosX(int a_value) {
    m_posx = a_value;
    storeMove();
    checkFoodTaken();
  }

  public void setPosY(int a_value) {
    m_posy = a_value;
    storeMove();
    checkFoodTaken();
  }

  private void storeMove() {
    char c;
    if (!m_moveModUpper) {
      c = (char) (m_moveMod + 'a');
      if (m_moveMod++ >= 25) {
        m_moveMod = 0;
        m_moveModUpper = true;
      }
    }
    else {
      c = (char) (m_moveMod + 'A');
      if (m_moveMod++ >= 25) {
        m_moveMod = 0;
        m_moveModUpper = false;
      }
    }
    m_movementMap[m_posx][m_posy] = c;
  }

  public int[][] getMovements() {
    return m_movementMap;
  }

  private void checkFoodTaken() {
    if (m_map[m_posx][m_posy] == AntMap.FOOD) {
      m_foodTaken++;
      m_map[m_posx][m_posy] = AntMap.ATE;
    }
    else {
      // Do nothing.
      // -----------
    }
  }

  public int getOrientation() {
    return m_orientation;
  }

  public void setOrientation(int a_orientation) {
    m_orientation = a_orientation;
  }

  public int getFoodTaken() {
    return m_foodTaken;
  }

  public int getMoveCount() {
    return m_moves;
  }

  public void IncrementMoveCounter() {
    m_moves++;
    if (m_moves > m_maxMoves) {
      throw new IllegalStateException("Maximum number of moves exceeded");
    }
  }

  public int getWidth() {
    return m_sizex;
  }

  public int getHeight() {
    return m_sizey;
  }

}
