package com.garcia.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship {

    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;

    public EnemyShip(float xCentre, float yCentre, float width,
                     float height, float movementSpeed,
                     int shield, float laserWidth, float laserHeight,
                     float laserMovementSpeed, float timeBetweenShots,
                     TextureRegion shipTextureRegion,
                     TextureRegion shieldTextureRegion,
                     TextureRegion laserTextureRegion) {

        super(xCentre, yCentre, width, height, movementSpeed, shield, laserWidth, laserHeight,
                laserMovementSpeed, timeBetweenShots, shipTextureRegion, shieldTextureRegion,
                laserTextureRegion);

        directionVector = new Vector2(0,-1); // initally enemy ship moves downwards towards player
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    private void randomizeDirectionVector() {
        double bearing = SpaceShooterGame.random.nextDouble() * 6.283185; // 0 to 2 * PI
        directionVector.x = (float)Math.sin(bearing);
        directionVector.x = (float)Math.cos(bearing);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeSinceLastDirectionChange += deltaTime;
        if(timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2];
        lasers[0] = new Laser(boundingBox.x + boundingBox.width * 0.34f, boundingBox.y - laserHeight, // determines location of first laser -- 34% from left side
                laserWidth, laserHeight, laserMovementSpeed,
                laserTextureRegion);

        lasers[1] = new Laser(boundingBox.x + boundingBox.width * 0.67f, boundingBox.y - laserHeight, // determines location of second laser -- 67% from left side
                laserWidth, laserHeight, laserMovementSpeed,
                laserTextureRegion);

        timeSinceLastShot = 0; // reset time since last shot -- inherited from ship class

        return lasers; // returns lasers array (2 simultaneous laser)
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y - boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
        }
    }
}
