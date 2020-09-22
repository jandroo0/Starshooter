package com.garcia.starshooter.game.objects.ships.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.effects.Laser;

public class StarterShip extends PlayerShip {

    public StarterShip() {
        super();

        // STATS -------
        baseDmg = 1;
        baseHealth = 3;
        baseShield = 0;
        baseSpeed = 30;
        baseLaser = 45;
        baseFireRate = 1f;


        dmg = baseDmg + dmgUpgrade;
        health = baseHealth + healthUpgrade;
        shield = shieldUpgrade;
        movementSpeed = baseSpeed + speedUpgrade;
        laserMovementSpeed = baseLaser + laserUpgrade;
        timeBetweenShots = baseFireRate - fireRateUpgrade;

        // textures
        lifeIcon = Assets.instance.hudAssets.PLAYER_LIFE_1_BLUE;
        shieldIcon = Assets.instance.hudAssets.BRONZE_SHIELD;

        shipTextureRegion = Assets.instance.playerShips.PLAYER_SHIP_1_BLUE;
        laserTextureRegion = Assets.instance.lasers.LASER_RED_01;
        damageTextures = new TextureRegion[]{Assets.instance.hudAssets.PLAYERSHIP_01_DMG_01, Assets.instance.hudAssets.PLAYERSHIP_01_DMG_02, Assets.instance.hudAssets.PLAYERSHIP_01_DMG_03};
    }


        // draw ----------------------
        @Override
        public void draw (Batch batch){
            batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

            if (shield > 8) {
                batch.draw(shieldTextures[2], boundingBox.x, boundingBox.y + boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
            } else if (shield > 4) {
                batch.draw(shieldTextures[1], boundingBox.x, boundingBox.y + boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
            } else if (shield > 0 && shield < 4) {
                batch.draw(shieldTextures[0], boundingBox.x, boundingBox.y + boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
            } else if (health == 3) {
                batch.draw(damageTextures[0], boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
            } else if (health == 2) {
                batch.draw(damageTextures[1], boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
            } else if (health == 1) {
                batch.draw(damageTextures[2], boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
            }
        }
        // -------------------------

        @Override
        public Laser[] fireLasers () {
            Laser[] lasers = new Laser[2];
            lasers[0] = new Laser(
                    boundingBox.x + boundingBox.width * 0.45f,
                    boundingBox.y + boundingBox.height * 0.98f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, (float)dmg / 2,
                    laserTextureRegion);
            lasers[1] = new Laser(
                    boundingBox.x + boundingBox.width * 0.55f,
                    boundingBox.y + boundingBox.height * 0.98f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, (float)dmg / 2,
                    laserTextureRegion);

            AudioManager.instance.play(Assets.instance.sounds.laser01);

            timeSinceLastShot = 0; // reset time since last shot -- inherited from ship class

            return lasers; // returns lasers array
        }
    }
