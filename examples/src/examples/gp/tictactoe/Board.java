/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.tictactoe;

import java.util.*;

/**
 * A Tic Tac Toe board (3x3).
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class Board {
  private int[][] m_board;

  private int m_lastColor;

  public static int WIDTH = 3;

  public static int HEIGHT = WIDTH;

  private int movesInRound;
  private int movesInTurn;
  private Map m_readPositions;
  private int m_readPositionCount;

  public Board() {
    m_board = new int[WIDTH][HEIGHT];
    m_readPositions = new Hashtable();
    startNewRound();
  }

  public void resetBoard() {
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        m_board[x][y] = 0;
      }
    }
  }

  public void startNewRound() {
    m_lastColor = 0;
    movesInRound = 0;
    resetBoard();
  }

  public void beginTurn() {
    movesInTurn = 0;
    m_readPositionCount = 0;
    m_readPositions.clear();
  }

  public void endTurn() {
    if (movesInTurn != 1) {
      throw new IllegalStateException("One stone must be set by player!");
    }
  }

  public void endRound() {
    if (m_lastColor == 0) {
      throw new IllegalStateException("No stone set within the round!");
    }
    if (movesInRound != 2) {
      throw new IllegalStateException("Both players have to move."
                                      + " Moves registered: "
                                      + movesInRound);
    }
    System.out.println("*** Valid round!");
  }

  public boolean putStone(int x, int y, int a_color)
      throws IllegalArgumentException {
    if (x < 1 || y < 1
        || x > WIDTH || y > HEIGHT) {
      throw new IllegalArgumentException("x and y must be between 1..3");
    }
    if (m_board[x - 1][y - 1] != 0) {
      throw new IllegalArgumentException("Position already occupied");
    }
    if (a_color != 1 && a_color != 2) {
      throw new IllegalArgumentException("Color must be 1 or 2");
    }
    if (m_lastColor == a_color) {
      throw new IllegalArgumentException("Oponent must move first");
    }
    m_lastColor = a_color;
    m_board[x - 1][y - 1] = a_color;
    movesInTurn++;
    movesInRound++;
    if (isEndOfGame()) {
      return true;
    }
    return false;
  }

  public int readField(int x, int y)
      throws IllegalArgumentException {
    if (x < 1 || y < 1
        || x > WIDTH || y > HEIGHT) {
      throw new IllegalArgumentException("x and y must be between 1.." + WIDTH);
    }
    if (m_readPositions.get(x + "_" + y) == null) {
      m_readPositionCount++;
      m_readPositions.put(x + "_" + y, "jgap");
    }
    return m_board[x - 1][y - 1];
  }

  public int getReadPositionCount() {
    return m_readPositionCount;
  }

  public int readField(int a_index)
      throws IllegalArgumentException {
    if (a_index < 1 || a_index > (WIDTH * HEIGHT)) {
      throw new IllegalArgumentException("Index must be between 1.." +
          (WIDTH * HEIGHT));
    }
    int x = a_index - 1 / WIDTH - 1;
    int y = a_index - 1 % HEIGHT - 1;
    return m_board[x][y];
  }

  public boolean isEndOfGame() {
    for (int y = 0; y < HEIGHT; y++) {
      if (isWinner(0, WIDTH-1, y, y)) {
        return true;
      }
    }
    for (int x = 0; x < HEIGHT; x++) {
      if (isWinner(x, x, 0, HEIGHT-1)) {
        return true;
      }
    }
    if (isWinnerDiag(0, 0, 1)) {
      return true;
    }
    if (isWinnerDiag(WIDTH-1, 0, -1)) {
      return true;
    }
    return false;
  }

  private boolean isWinner(int a_firstX, int a_lastX, int a_firstY, int a_lastY) {
    int count = 0;
    int lastcolor = -1;
    for (int x = a_firstX; x <= a_lastX; x++) {
      for (int y = a_firstY; y <= a_lastY; y++) {
        int color = m_board[x][y];
        if (color == 0) {
          return false;
        }
        if (lastcolor == -1) {
          lastcolor = color;
          count++;
        }
        else {
          if (lastcolor != color) {
            return false;
          }
          else {
            count++;
          }
        }
      }
    }
    return true;
  }

  private boolean isWinnerDiag(int a_startX, int a_startY, int a_increment) {
    int count = 0;
    int lastcolor = -1;
    int x = a_startX;
    int y = a_startY;
    while (count < WIDTH) {
      int color = m_board[x][y];
      if (color == 0) {
        return false;
      }
      if (lastcolor == -1) {
        lastcolor = color;
        count++;
      }
      else {
        if (lastcolor != color) {
          return false;
        }
        else {
          count++;
        }
      }
      x = x + a_increment;
      y = y + 1;
    }
    return true;
  }

  public int getLastColor() {
    return m_lastColor;
  }
}
