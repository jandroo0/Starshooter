package com.garcia.starshooter.game.objects.ships.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShipStats {

    public int pts;

    public int movementSpeed;
    public int dmg;
    public int health;
    public int shield;
    public float laserWidth, laserHeight;
    public int laserMovementSpeed;
    public float timeBetweenShots;

    // textures
    public TextureRegion shipTextureRegion, laserTextureRegion;

}