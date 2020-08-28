package com.garcia.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    int lives = 3; // player lives

    public PlayerShip(float xCentre, float yCentre, float width,
                      float height, float movementSpeed,
                      int shield, float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots,
                      TextureRegion shipTextureRegion,
                      TextureRegion shieldTextureRegion,
                      TextureRegion laserTextureRegion) {

        super(xCentre, yCentre, width, height, movementSpeed, shield, laserWidth, laserHeight,
                laserMovementSpeed, timeBetweenShots, shipTextureRegion, shieldTextureRegion,
                laserTextureRegion);
        lives = 3;
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(boundingBox.x + boundingBox.width * 0.45f, boundingBox.y + boundingBox.height * 0.98f, // determines location of first laser -- left side of ship 7%
                laserWidth,laserHeight,laserMovementSpeed,
                laserTextureRegion);

        lasers[1] = new Laser(boundingBox.x + boundingBox.width * 0.55f, boundingBox.y + boundingBox.height * 0.98f, // determines location of second laser -- right side of ship 93%
                laserWidth,laserHeight,laserMovementSpeed,
                laserTextureRegion);

        timeSinceLastShot = 0; // reset time since last shot -- inherited from ship class

        return lasers; // returns lasers array (2 simultaneous laser)
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        if(shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y + boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
        }
    }
}
