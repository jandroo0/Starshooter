package com.garcia.starshooter.game.objects.ships;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.effects.Laser;

public abstract class Ship {

    // pos
    public Rectangle boundingBox;

    // vars
    public float width = 10;
    public float height = 10;

    // base stats
    public int movementSpeed;
    public int dmg;
    public int health;
    public int shield;
    public float laserWidth, laserHeight;
    public int laserMovementSpeed;
    public float timeBetweenShots;
    public float timeSinceLastShot;

    // textures
    public TextureRegion shipTextureRegion, laserTextureRegion;

    public Ship(float xCenter, float yCenter) {
        this.boundingBox = new Rectangle(xCenter - width / 2, yCenter - height / 2, width, height); // pos
    }

    // draw ship and shield
    public abstract void draw(Batch batch);

    // change position of ship
    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange); // change position
    }

    public boolean intersects(Rectangle laserBoundingBox) {
        return (boundingBox.overlaps(laserBoundingBox));
    }

    public boolean hit(float dmg) { // true == dead
        if (shield > 0) { // if there is shield
            if (shield >= dmg) { // if there is more shield than incoming dmg
                shield -= dmg; // decrease shield
                return false; // false == alive
            } else {
                health -= (dmg - shield);
                AudioManager.instance.play(Assets.instance.sounds.explosion, 0.1f);
            }
        } else {
            health -= dmg;
            AudioManager.instance.play(Assets.instance.sounds.explosion, 0.1f);
        }

        System.out.println("SHIP HIT " + "DMG: " + dmg + " HEALTH: " + health + " SHIELD: " + shield);

        return (health <= 0); // return true if destroyed
    }

    // determine if ship can fire
    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    // return lasers being fired
    public abstract Laser[] fireLasers();

    // update ship
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }


}
