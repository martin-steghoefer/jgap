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
 * Represents the Ant.  Contains the rules about how an Ant behaves.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class Ant {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * The x position of the Ant
   */
  private int m_xpos;

  /**
   * The y position of the Ant
   */
  private int m_ypos;

  /**
   * The number of moves the ant has made
   */
  private int m_moves;

  /**
   * The maximum number of moves an Ant is allowed to move in a particular program.
   */
  private int m_maxMoves = 500;

  /**
   * The grain of sand, if any, that the Ant is carrying.
   */
  private int m_carrying = AntMap.EMPTY;

  /**
   * The x location the ant picked up the sand from.
   */
  private int m_pickedUpFromXLoc = -1;

  /**
   * The y location the ant picked up the sand from.
   */
  private int m_pickedUpFromYLoc = -1;

  /**
   * The initial x location of the ant.  This is used when the ant is reset back
   * to it's original location.
   */
  private int m_initX;

  /**
   * The initial y locaiton of the Ant.  This is used when the ant is reset back
   * to it's original location.
   */
  private int m_initY;

  /**
   * Constructs the Ant at an initial location.
   * @param x Initial x location
   * @param y Initial y locaiton
   */
  public Ant(int x, int y) {
    m_initX = x;
    m_initY = y;
    reset();
  }

  /**
   * Sets the x location of the ant.
   * @param m_xpos
   */
  public void setXpos(int m_xpos) {
    this.m_xpos = m_xpos;
  }

  /**
   * Returns the x location of the ant.
   * @return The x location.
   */
  public int getXpos() {
    return m_xpos;
  }

  /**
   * Sets the y location of the ant.
   * @param m_ypos The y location
   */
  public void setYpos(int m_ypos) {
    this.m_ypos = m_ypos;
  }

  /**
   * Returns the y location of the ant.
   * @return The y location
   */
  public int getYpos() {
    return m_ypos;
  }

  /**
   * Returns the grain of sand the ant is carrying.  Returns EMPTY if no
   * sand is being carried.
   * @return  The color of sand
   */
  public int getCarrying() {
    return m_carrying;
  }

  /**
   * Identifies if the ant is carrying any sand.
   * @return The color of sand
   */
  public boolean isCarrying() {
    return (m_carrying != AntMap.EMPTY);
  }

  /**
   *
   * @param m_lastMove
   */

  /**
   * Stores the x location where the sand was picked up.
   */
  public void setPickedUpFromXLoc(int pickedUpFromXLoc) {
    this.m_pickedUpFromXLoc = pickedUpFromXLoc;
  }

  /**
   * Returns the x location where the sand was picked up.
   * @return
   */
  public int getPickedUpFromXLoc() {
    return m_pickedUpFromXLoc;
  }

  /**
   * Sets the y location where the sand was picked up.
   * @param pickedUpFromYLoc The y location
   */
  public void setPickedUpFromYLoc(int pickedUpFromYLoc) {
    this.m_pickedUpFromYLoc = pickedUpFromYLoc;
  }

  /**
   * Returns the Y locaiton where the sand was picked up.
   * @return  The Y location
   */
  public int getPickedUpFromYLoc() {
    return m_pickedUpFromYLoc;
  }

  /**
   * Drops the sand at the current location if possible.
   * @return the color of sand the ant is carrying whether or not it could be dropped at
   * the current location.
   */
  public int drop(AntMap antmap) {
    int result = m_carrying;
    if (antmap.atLocation(this.getXpos(), this.getYpos()) == AntMap.EMPTY) {
      m_carrying = AntMap.EMPTY;
    }
    return result;
  }

  /**
   * Picks up the sand at the current location, if the ant is not carrying any sand
   * and there is sand at the location
   * @param antmap The map of locations of sand
   * @return The sand color at the location whether or not the Ant could pick up
   * the sand.
   */
  public int pickup(AntMap antmap) {
    if (m_carrying == AntMap.EMPTY &&
        antmap.atLocation(this.getXpos(), this.getYpos()) != AntMap.EMPTY) {
      m_carrying = antmap.removeSand(this.getXpos(), this.getYpos());
      m_pickedUpFromXLoc = this.getXpos();
      m_pickedUpFromYLoc = this.getYpos();
    }
    return antmap.atLocation(this.getXpos(), this.getYpos());
  }

  /**
   * Randomly moves the ant.
   * @param map  The map of locations of sand
   * @return The sand at the new location of the Ant.
   */
  public int moveRandom(AntMap map) {
    int x = this.getXpos();
    int y = this.getYpos();
    double rand = Math.random() * 4.0;
    int dir = (int) Math.round(rand);
    if (dir == 0) {
      if (x + 1 < map.getHeight() &&
          map.getMap()[x + 1][y] != AntMap.ANT_AT_POSITION) {
        this.goN(map);
      }
      else {
        dir++;
      }
    }
    if (dir == 1) {
      if (y - 1 > 0 && map.getMap()[x][y - 1] != AntMap.ANT_AT_POSITION) {
        this.goS(map);
      }
      else {
        dir++;
      }
    }
    if (dir == 2) {
      if (x - 1 > 0 && map.getMap()[x - 1][y] != AntMap.ANT_AT_POSITION) {
        this.goW(map);
      }
      else {
        dir++;
      }
    }
    if (dir == 3) {
      if (y + 1 < map.getHeight() &&
          map.atLocation(x, y + 1) != AntMap.ANT_AT_POSITION) {
        this.goN(map);
      }
      else {
        dir++;
      }
    }
    incrementMoveCounter();
    return map.getAnt().sandColor(map);
  }

  /**
   * Instructs the ant to go east, if possible.
   */
  public int goE(AntMap map) {
    if (this.getXpos() + 1 < map.getWidth()) {
      this.setXpos(this.getXpos() + 1);
    }
    incrementMoveCounter();
    return map.getAnt().sandColor(map);
  }

  /**
   * Instructs the ant to go west, if possible
   * @param map
   * @return The color of the sand at the new location
   */
  public int goW(AntMap map) {
    if (this.getXpos() - 1 > 0) {
      this.setXpos(this.getXpos() - 1);
    }
    incrementMoveCounter();
    return map.getAnt().sandColor(map);
  }

  /**
   * Instructs the ant to go north, if possible
   * @param map
   * @return The color of the sand at the new location
   */
  public int goN(AntMap map) {
    if (this.getYpos() + 1 < map.getHeight()) {
      this.setYpos(this.getYpos() + 1);
    }
    incrementMoveCounter();
    return map.getAnt().sandColor(map);
  }

  /**
   * Instructs the ant to go south, if possible
   * @param map
   * @return The color of the sand at the new location
   */
  public int goS(AntMap map) {
    if (this.getYpos() - 1 > 0) {
      this.setYpos(this.getYpos() - 1);
    }
    incrementMoveCounter();
    return map.getAnt().sandColor(map);
  }

  /**
   * Identifies whether the ant could pickup sand
   * @param map
   * @return
   */
  public boolean mayPickup(AntMap map) {
    return!this.isCarrying();
  }

  /**
   * Identifies whether there is sand at the location
   * @param map
   * @return true if sand at location
   */
  public boolean sandAtLocation(AntMap map) {
    return map.sandAtLocation(this.getXpos(), this.getYpos());
  }

  /**
   * Identifies that sand the ant is carrying, belongs in this column
   * @param map
   * @return
   */
  public boolean sandBelongsHere(AntMap map) {
    return map.sandBelongsHere(this.getCarrying(), this.getXpos());
  }

  /**
   * Identifies the color of the sand at the ant's location
   * @param map
   * @return
   */
  public int sandColor(AntMap map) {
    return map.getMap()[this.getXpos()][this.getYpos()];
  }

  /**
   * Increments the move counter for the ant
   *
   */
  public void incrementMoveCounter() {
    m_moves++;
    if (m_moves > m_maxMoves) {
      throw new IllegalStateException("Maximum number of moves exceeded");
    }
  }

  /**
   * Identifies the number of moves the ant has performed
   * @return
   */
  public int getMoves() {
    return m_moves;
  }

  /**
   * Resets the ant to its original location on the map.
   *
   */
  public void reset() {
    m_xpos = m_initX;
    m_ypos = m_initY;
    m_moves = 0;
    m_carrying = AntMap.EMPTY;
    m_pickedUpFromXLoc = -1;
    m_pickedUpFromYLoc = -1;
  }
}
