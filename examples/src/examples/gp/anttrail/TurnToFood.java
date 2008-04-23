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

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Look ahead, right and left (in this order) and turns to food in case such was
 * detected. Does nothing in the other case.<p>
 * This command is not part of the classic ant problem.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class TurnToFood
    extends AntCommand implements IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public TurnToFood(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass);
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    CommandGene mutant;
    if (a_percentage < 0.33d) {
      mutant = new Right(getGPConfiguration());
    }
    else if (a_percentage < 0.67d) {
      mutant = new Left(getGPConfiguration());
    }
    else {
      mutant = new Move(getGPConfiguration());
    }
    return mutant;
  }

  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    int x = map.getPosX();
    int y = map.getPosY();
    int orient = map.getOrientation();
    // Look ahead for food.
    // --------------------
    boolean found = false;
    switch (orient) {
      case AntMap.O_DOWN:
        if (y < map.getHeight() - 1) {
          if (map.getFromMap(x, y + 1) == AntMap.FOOD) {
            found = true;
            map.setPosY(y + 1);
          }
        }
        break;
      case AntMap.O_LEFT:
        if (x >= 1) {
          if (map.getFromMap(x-1, y ) == AntMap.FOOD) {
            found = true;
            map.setPosX(x - 1);
          }
        }
        break;
      case AntMap.O_RIGHT:
        if (x < map.getWidth() - 1) {
          if (map.getFromMap(x+1, y ) == AntMap.FOOD) {
            found = true;
            map.setPosX(x + 1);
          }
        }
        break;
      case AntMap.O_UP:
        if (y >= 1) {
          if (map.getFromMap(x, y - 1) == AntMap.FOOD) {
            found = true;
            map.setPosY(y - 1);
          }
        }
        break;
    }
    if (!found) {
      // Look right for food.
      // --------------------
      switch (orient) {
        case AntMap.O_RIGHT:
          if (y < map.getHeight() - 1) {
            if (map.getFromMap(x, y + 1) == AntMap.FOOD) {
              found = true;
              map.setPosY(y + 1);
              map.setOrientation(AntMap.O_DOWN);
            }
          }
          break;
        case AntMap.O_DOWN:
          if (x >= 1) {
            if (map.getFromMap(x-1, y ) == AntMap.FOOD) {
              found = true;
              map.setPosX(x - 1);
              map.setOrientation(AntMap.O_LEFT);
            }
          }
          break;
        case AntMap.O_UP:
          if (x < map.getWidth() - 1) {
            if (map.getFromMap(x+1, y ) == AntMap.FOOD) {
              found = true;
              map.setPosX(x + 1);
              map.setOrientation(AntMap.O_RIGHT);
            }
          }
          break;
        case AntMap.O_LEFT:
          if (y >= 1) {
            if (map.getFromMap(x, y - 1) == AntMap.FOOD) {
              found = true;
              map.setPosY(y - 1);
              map.setOrientation(AntMap.O_UP);
            }
          }
          break;
      }
      if (!found) {
        // Look left for food.
        // -------------------
        switch (orient) {
          case AntMap.O_LEFT:
            if (y < map.getHeight() - 1) {
              if (map.getFromMap(x, y + 1) == AntMap.FOOD) {
                found = true;
                map.setPosY(y + 1);
                map.setOrientation(AntMap.O_DOWN);
              }
            }
            break;
          case AntMap.O_UP:
            if (x >= 1) {
              if (map.getFromMap(x-1, y ) == AntMap.FOOD) {
                found = true;
                map.setPosX(x - 1);
                map.setOrientation(AntMap.O_LEFT);
              }
            }
            break;
          case AntMap.O_DOWN:
            if (x < map.getWidth() - 1) {
              if (map.getFromMap(x+1, y ) == AntMap.FOOD) {
                found = true;
                map.setPosX(x + 1);
                map.setOrientation(AntMap.O_RIGHT);
              }
            }
            break;
          case AntMap.O_RIGHT:
            if (y >= 1) {
              if (map.getFromMap(x, y - 1) == AntMap.FOOD) {
                found = true;
                map.setPosY(y - 1);
                map.setOrientation(AntMap.O_UP);
              }
            }
            break;
        }
      }
    }
    if (found) {
      map.IncrementMoveCounter();
    }
  }

  public String toString() {
    return "turn-to-food";
  }
}
