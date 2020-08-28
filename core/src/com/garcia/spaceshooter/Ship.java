package com.garcia.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Ship {

    // pos
    Rectangle boundingBox; // x, y, width, height

    // vars
    float movementSpeed; // world units per second
    int shield;

    // laser
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;

    // gfx
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    public Ship(float xCentre, float yCentre, float width,
                float height, float movementSpeed,
                int shield, float laserWidth, float laserHeight,
                float laserMovementSpeed, float timeBetweenShots,
                TextureRegion shipTextureRegion,
                TextureRegion shieldTextureRegion,
                TextureRegion laserTextureRegion) {

        this.boundingBox = new Rectangle(xCentre - width / 2, yCentre - height / 2, width, height); // pos

        this.movementSpeed = movementSpeed;
        this.shield = shield;

        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;

        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
    }

    // ship hit
    public boolean hit(Laser laser) {
        if (shield > 0) { // if there is shield
            shield--;
            return false; // ship alive
        }
        return true; // ship destroyed
    }

    // updates ship
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    // move ship
    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange); // change position of ship
    }

    // returns if ship is ready to fire
    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    // for collision detection
    public boolean intersects(Rectangle laserBoundingBox) {
        return boundingBox.overlaps(laserBoundingBox); // checks if passed in laser rectangle overlaps with rectangle of ship -- therefore collision -- not very efficient
    }

    public abstract Laser[] fireLasers();

    // draw ship and shield
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        if (shield > 0) { // if there is shield draw shield
            batch.draw(shieldTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }
    }
}
