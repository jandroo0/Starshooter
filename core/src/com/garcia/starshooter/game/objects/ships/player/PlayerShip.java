package com.garcia.starshooter.game.objects.ships.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.effects.Laser;
import com.garcia.starshooter.game.objects.ships.Ship;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public abstract class PlayerShip extends Ship {

    // player spawn
    static float xCenter = WORLD_WIDTH / 2;
    static float yCenter = WORLD_HEIGHT / 4;

    public TextureRegion lifeIcon;
    public TextureRegion shieldIcon;

    public TextureRegion[] damageTextures; // damage textures
    public TextureRegion[] shieldTextures = new TextureRegion[]{Assets.instance.hudAssets.PLAYER_SHIELD_1, Assets.instance.hudAssets.PLAYER_SHIELD_2, Assets.instance.hudAssets.PLAYER_SHIELD_3};

    // stat level converted to actual ships stats
    public int dmgStat, healthStat, shieldStat, speedStat, laserStat, fireRateStat;
    public int baseDmg, baseHealth, baseShield, baseSpeed, baseLaser;
    public float baseFireRate;

    protected int dmgUpgrade, healthUpgrade, shieldUpgrade, speedUpgrade, laserUpgrade;
    protected float fireRateUpgrade;


    public PlayerShip() {
        super(xCenter, yCenter);

        dmgStat = 1;
        healthStat = 1;
        shieldStat = 0;
        speedStat = 1;
        laserStat = 1;
        fireRateStat = 1;

        laserWidth = 0.4f;
        laserHeight = 4f;
    }

    public void resetShip() {
        health = baseHealth + healthUpgrade;
        shield = shield + shieldUpgrade;
    }


    public void upgradeDmg() {
        dmgStat++;

        if (dmgStat < 3) { //1,2
            dmgUpgrade = 1; // +1 dmg
        }
        if (dmgStat >= 3 && dmgStat < 5) { // 3,4
            dmgUpgrade = 2; // + 2 dmg
        }

        if (dmgStat >= 5 && dmgStat < 7) { // 5,6
            dmgUpgrade = 3; // +3 dmg
        }

        dmg += dmgUpgrade;
        printStats();
    }

    public void upgradeHealth() { // 6 upgrades -- 3 + 12 = 15 hp max
        healthStat++;

        if (healthStat < 3) { //1,2
            healthUpgrade = 1; // +1 hp
        }
        if (healthStat >= 3 && healthStat < 5) { // 3,4
            healthUpgrade = 2;
        }

        if (healthStat >= 5 && healthStat < 7) { // 5,6
            healthUpgrade = 3;
        }

        health += healthUpgrade;
        printStats();
    }

    public void upgradeShield() {
        shieldStat++;

        if (shieldStat < 3) { //1,2
            shieldUpgrade = 1; // +1 shield
        }
        if (shieldStat >= 3 && shieldStat < 5) { // 3,4
            shieldUpgrade = 2; // + 2 dmg
        }

        if (shieldStat >= 5 && shieldStat < 7) { // 5,6
            shieldUpgrade = 3; // +3 dmg
        }

        shield += shieldUpgrade;
        printStats();
    }

    public void upgradeSpeed() {
        speedStat++;

        if (speedStat < 3) { //1,2
            speedUpgrade = 4;
        }
        if (speedStat >= 3 && speedStat < 5) { // 3,4
            speedUpgrade = 6;
        }
        if (speedStat >= 5 && speedStat < 7) { // 5,6
            speedUpgrade = 8;
        }

        movementSpeed += speedUpgrade;
        printStats();
    }

    public void upgradeLaser() {
        laserStat++;

        if (laserStat < 3) { //1,2
            laserUpgrade = 5;
        }
        if (laserStat >= 3 && laserStat < 5) { // 3,4
            laserUpgrade = 7; // + 7 speed
        }

        if (laserStat >= 5 && laserStat < 7) { // 5,6
            laserUpgrade = 9; // +9 speed
        }

        laserMovementSpeed += laserUpgrade;
        printStats();
    }

    public void upgradeFireRate() {
        fireRateStat++;

        if (fireRateStat < 3) { //1,2
            fireRateUpgrade = 0.1f;
        }
        if (fireRateStat >= 3 && fireRateStat < 5) { // 3,4
            fireRateUpgrade = 0.1f;
        }

        if (fireRateStat == 5) { // 5,6
            fireRateUpgrade = 0.1f;
        }

        timeBetweenShots -= fireRateUpgrade;
        printStats();
    }

    public void printStats() {
        System.out.println(dmg + " DMG\n" + health + " HEALTH\n" + shield + " SHIELD\n" + movementSpeed + " SPEED\n" + laserMovementSpeed + " LASERSPEED\n" + timeBetweenShots + " FIRERATE\n");
    }


    @Override
    public abstract Laser[] fireLasers();

}
