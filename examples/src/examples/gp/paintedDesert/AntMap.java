/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

/**
 * Holds the map of the painted desert sand locations
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class AntMap {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";
  /** Holds the instance of the ant being processed.  Assumption that some external
   * single threaded process will loop through all the ants using nextAnt to sequence
   * through the ants.  It is expected that the clients of the AntMap can use getAnt()
   * to return the Ant instance at the currentAnt index.
   */
  private int m_currentAnt;

  // Carrying
  /**
   * No Sand at this location or carried by the ant
   */
  public static final int EMPTY = -1;

  /**
   * Black sand at this location or carried by the ant
   */
  public static final int BLACK = 0;

  /**
   * Gray sand at this location or carried by the ant
   */
  public static final int GRAY = 1;

  /**
   * Striped sand at this location or carried by the ant
   */
  public static final int STRIPED = 2;

  /**
   * Ant at this location
   */
  public static final int ANT_AT_POSITION = 9;

  /**
   *  Initial sand position as [x][y]
   */
  private int[][] m_initialPosition;

  /**
   * Width of map.
   */
  private int m_sizex;

  /**
   * Height of map.
   */
  private int m_sizey;

  /**
   * Population size.
   */
  private int m_popSize = 1;

  /**
   * Holder of the current map.
   *
   * Sand is indicated by AntMap.EMPTY|GRAY|STRIPED|BLACK
   */
  private int[][] m_currentMap;

  /**
   * Holds the list of ants
   */
  private Ant[] m_ants;

//	/**
//	 * Maximum number of moves allowed.
//	 */
  //private int m_maxMoves;


  /**
   * Creates the map.
   *
   * @param a_map the map itself
   * @param a_ants the list of ants
   */
  public AntMap(final int[][] a_map, Ant[] a_ants) {
    m_sizex = a_map.length;
    m_sizey = a_map[0].length;
    m_ants = a_ants;
    m_popSize = m_ants.length;
    m_currentAnt = -1;
    m_initialPosition = new int[m_sizex][m_sizey];
    this.m_currentMap = new int[m_sizex][m_sizey];
    for (int x = 0; x < m_sizex; x++) {
      for (int y = 0; y < m_sizey; y++) {
        if (a_map[x][y] == ANT_AT_POSITION) {
          m_initialPosition[x][y] = a_map[x][y]; //nextAntIndex + this.ANT_INDEX_OFFSET;
          m_currentMap[x][y] = a_map[x][y];
        }
        else {
          m_initialPosition[x][y] = a_map[x][y];
          m_currentMap[x][y] = a_map[x][y];
        }
      }
    }
  }

  /**
   * Places sand or identifies an empty spot at the current position
   * @param sandColor
   * @param x
   * @param y
   */
  public void placeSand(int sandColor, int x, int y) {
    m_currentMap[x][y] = sandColor;
  }

  /**
   * Returns the color of sand at the provided location
   * @param x
   * @param y
   * @return
   */
  public int atLocation(int x, int y) {
    return m_currentMap[x][y];
  }

  /**
   * Returns a representation of the current map
   * @return
   */
  public int[][] getMap() {
    return this.m_currentMap;
  }

  /**
   * Returns a representation of the initial map
   * @return
   */
  public int[][] getInitialMap() {
    return this.m_initialPosition;
  }

  /**
   * Identifies whether there is sand at the provided location
   * @param x
   * @param y
   * @return
   */
  public boolean sandAtLocation(int x, int y) {
    return this.m_currentMap[x][y] != AntMap.EMPTY;
  }

  /**
   * Identifies that sand is at that location
   * @param sandColor
   * @param x
   * @return
   */
  public boolean sandBelongsHere(int sandColor, int x) {
    return sandColor == x;
  }

  /**
   * Identifies that sand may be placed here
   * @param x
   * @param y
   * @return
   */
  public boolean mayDropSand(int x, int y) {
    return this.m_currentMap[x][y] == AntMap.EMPTY;
  }

  /**
   * Removes the sand from the current position
   * @param x
   * @param y
   * @return
   */
  public int removeSand(int x, int y) {
    int removed = this.m_currentMap[x][y];
    this.m_currentMap[x][y] = AntMap.EMPTY;
    return removed;
  }

  /**
   * Returns the current Ant.  The current ant is incremented by the NextAnt
   * method.
   * @return
   */
  public Ant getAnt() {
    if (getAnts()[m_currentAnt] == null) {
      throw new IllegalStateException(
          "currentAnt does not point at a valid ant instance");
    }
    return getAnts()[m_currentAnt];
  }

  /** Determine the final position of grains of sand.  If an ant is carrying sand and
   * there is no sand at the current position, drop the sand at this position.  If there
   * is sand, rubberband the sand back to where the ant picked up the sand.  If
   * both locations are full, then pick the nearest open spot
   * @see java.lang.Object#finalize()
   */
  public void finalize() {
    for (int antIndex = 0; antIndex < m_popSize; antIndex++) {
      if (getAnts()[antIndex].getCarrying() >= 0) {
        if (getMap()[getAnts()[antIndex].getXpos()][getAnts()[antIndex].getYpos()] !=
            AntMap.EMPTY) {
          getMap()[getAnts()[antIndex].getXpos()][getAnts()[antIndex].getYpos()] =
              getAnts()[antIndex].getCarrying();
        }
        else {
          /*System.out.println("carrying "+ getAnts()[antIndex].getCarrying());
                System.out.println("ant index = "+antIndex );
                System.out.println("picked up fromX`" + getAnts()[antIndex].getPickedUpFromXLoc());
                System.out.println("picked up fromY + getAnts()[antIndex].getPickedUpFromYLoc());
           */
          if (getMap()[getAnts()[antIndex].getPickedUpFromXLoc()][getAnts()[
              antIndex].getPickedUpFromYLoc()] != AntMap.EMPTY) {
            getMap()[getAnts()[antIndex].getPickedUpFromXLoc()][getAnts()[
                antIndex].getPickedUpFromYLoc()] = getAnts()[antIndex].
                getCarrying();
          }
          else {
            placeNear(getAnts()[antIndex].getCarrying(),
                      getAnts()[antIndex].getXpos(),
                      getAnts()[antIndex].getYpos());
          }
        }
      }
    }
  }

  /**
   * Calculates how well the sand is moved to the proper columns.
   * @return
   */
  public int fitness() {
    //Causes all ants to drop their sand at a location
    this.finalize();
    int fitness = 0;//(int) GPFitnessFunction.MAX_FITNESS_VALUE;
    int hit = 0;
    for (int x = 0; x < this.getWidth(); x++) {
      for (int y = 0; y < this.getHeight(); y++) {
        if (this.getMap()[x][y] != AntMap.EMPTY) {
          // The proper column for the color is the same as the x = color
          // Black = 0, Gray = 1, Striped = 2
          // so subtract the color of sand from the x value to find the delta
          // and take the absolute value
          // when all sand is in the proper location fitness = 0
          //fitness = fitness + Math.abs(x- antmap.getMap()[x][y]);
          fitness = fitness +
              fitnessValue(getMap(), getMap()[x][y], x, y);
          if (getMap()[x][y] == x) {
            // Sand is in the correct position - color = x value
            hit++;
          }
        }
      }
    }
    return fitness;
  }

  /**
   * Calculates the fitness value for a single grain of sand
   * @param antmap
   * @param sandColor
   * @param x
   * @param y
   * @return
   */
  private int fitnessValue(int[][] antmap, int sandColor, int x, int y) {
    if (x == antmap[x][y]) {
      return 0;
    }
    else if (x >= antmap[x][y]) {
      return (x - antmap[x][y]) * (x - antmap[x][y]);
    }
    else {
      //penalty for moving too far to left
      return (1 + antmap[x][y] - x) * (1 + antmap[x][y] - x);
    }
  }

  /**
   * Places the grain of sand near where the current location is at.
   * @param sandType
   * @param x
   * @param y
   */
  private void placeNear(int sandType, int x, int y) {
    boolean placed = false;
    int initX = x;
    int initY = y;
    while (!placed && x + 1 < this.m_sizex && y + 1 < this.m_sizey) {
      x++;
      if (getMap()[x][y] == AntMap.EMPTY) {
        this.placeSand(sandType, x, y);
        placed = true;
      }
      y++;
      if (!placed && y < this.m_sizey && getMap()[x][y] == AntMap.EMPTY) {
        this.placeSand(sandType, x, y);
        placed = true;
      }
    }
    x = initX;
    y = initY;
    if (!placed) {
      while (!placed && x - 1 > 0 && y - 1 > 0) {
        x--;
        if (y == this.m_sizey) {
          y--;
        }
        if (getMap()[x][y] == AntMap.EMPTY) {
          this.placeSand(sandType, x, y);
          x = this.m_sizex;
          y = this.m_sizey;
          placed = true;
        }
        y--;
        if (!placed && y > 0 && getMap()[x][y] == AntMap.EMPTY) {
          this.placeSand(sandType, x, y);
          x = this.m_sizex;
          y = this.m_sizey;
        }
      }
    }
  }

  /**
   * Increments the index to the next ant.
   * @return
   */
  public Ant nextAnt() {
    if (m_currentAnt + 1 < m_popSize) {
      m_currentAnt++;
    }
    else {
      System.out.println("nextAnt with m_currentAnt=" + m_currentAnt +
                         " and m_popSize=" + m_popSize);
      throw new IllegalStateException("Iterated beyond last ant");
    }
    return getAnts()[m_currentAnt];
  }

  /**
   * Asks each ant for the number of moves and sums up the result.
   * @return
   */
  public int getMoveCount() {
    int moves = 0;
    for (int antIndex = 0; antIndex < m_popSize; antIndex++) {
      if (getAnts()[antIndex] != null) {
        moves = getAnts()[antIndex].getMoves();
      }
    }
    return moves;
  }

  /**
   * Returns the width of the map
   * @return
   */
  public int getWidth() {
    return m_sizex;
  }

  /**
   * Returns the height of the map
   * @return
   */
  public int getHeight() {
    return m_sizey;
  }

  /**
   * Returns this list of ants
   * @return
   */
  public Ant[] getAnts() {
    return m_ants;
  }

  public void init() {
    resetMap();
  }

  /**
   * Resets the sand and ant back to their positions before the program was applied
   *
   */
  public void resetMap() {
    m_currentAnt = -1;
//    m_fitnessFailure = false;
    for (int antIndex = 0; antIndex < m_ants.length; antIndex++) {
      m_ants[antIndex].reset();
    }
    for (int x = 0; x < m_sizex; x++) {
      for (int y = 0; y < m_sizey; y++) {
        m_currentMap[x][y] = m_initialPosition[x][y];
      }
    }
  }

}
