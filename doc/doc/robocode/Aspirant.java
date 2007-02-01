package dunapep;

import robocode.*;
import java.awt.Color;

public class Aspirant
    extends Robot {
  public void run() {
    setColors(Color.red, Color.blue, Color.green);
    while (true) {
      turnRight( (2.156702756881714d));
      ahead( ( -1) * ( (2.156702756881714d) - getHeading()));
      ahead( ( (2.156702756881714d) + (2.156702756881714d)));
    }
  }

  public void onScannedRobot(ScannedRobotEvent e) {
    ahead(getHeading());
    turnRight( ( (e.getBearing() / getHeading()) - ( -71.53790283203125d)));
    turnRight( ( (e.getDistance() - e.getBearing()) - (2.0456292629241943d)));
  }

  public void onHitByBullet(HitByBulletEvent e) {
    ahead(e.getBearing());
    turnGunRight( (2.3312511444091797d));
    fire( ( (getHeading() * (145.2213134765625d)) /
           (e.getBearing() * (145.2213134765625d))));
  }

  public void onHitWall(HitWallEvent e) {
    turnRight( (31.579986572265625d));
    fire( (e.getBearing() / getHeading()));
    turnLeft( (e.getBearing() + (getHeading() / (1.3382158279418945d))));
  }

  public void onHitRobot(HitRobotEvent e) {
    fire( (95.45698547363281d));
    ahead( (e.getBearing() + getHeading()));
    turnGunRight( ( (95.45698547363281d) * e.getBearing()));
  }
}
