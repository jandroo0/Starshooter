package com.garcia.starshooter.game.objects.ships.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.effects.Laser;
import com.garcia.starshooter.game.objects.StarshooterGame;
import com.garcia.starshooter.game.objects.ships.Ship;

import java.util.Locale;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class EnemyShip extends Ship {

    Vector2 directionVector;
    public float timeSinceLastDirectionChange = 0;
    public float directionChangeFrequency = 0.75f;

    public int points;

    // textures
    public TextureRegion[] shieldTextures = new TextureRegion[]{Assets.instance.hudAssets.ENEMY_SHIELD_1, Assets.instance.hudAssets.ENEMY_SHIELD_2, Assets.instance.hudAssets.ENEMY_SHIELD_3};

    static boolean flipped = false;

    public EnemyShip(int pts, int dmg, int health, int shield, int movementSpeed, int laserMovementSpeed, float timeBetweenShots,
                     float laserWidth, float laserHeight,
                     TextureRegion shipTextureRegion, TextureRegion laserTextureRegion) {

        super(StarshooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5);

        directionVector = new Vector2(0, -1); // initially ship moves downwards

        // STATS -------
        this.points = pts;

        this.dmg = dmg;
        this.health = health;
        this.shield = shield;
        this.movementSpeed = movementSpeed;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;

        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;

        this.shipTextureRegion = shipTextureRegion;
        this.laserTextureRegion = laserTextureRegion;

        if (!flipped) { // shield flipping bug
            Assets.instance.hudAssets.ENEMY_SHIELD_1.flip(false, true);
            Assets.instance.hudAssets.ENEMY_SHIELD_2.flip(false, true);
            Assets.instance.hudAssets.ENEMY_SHIELD_3.flip(false, true);
            flipped = true;
        }
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    private void randomizeDirectionVector() {
        double bearing = StarshooterGame.random.nextDouble() * 6.283185; // 0 to 2 * PI
        directionVector.x = (float) Math.sin(bearing);
        directionVector.x = (float) Math.cos(bearing);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        if (shield > 8) {
            batch.draw(shieldTextures[2], boundingBox.x, boundingBox.y - boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
        } else if(shield > 4) {
            batch.draw(shieldTextures[1], boundingBox.x, boundingBox.y - boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
        } else if(shield > 0) {
            batch.draw(shieldTextures[0], boundingBox.x, boundingBox.y - boundingBox.height * 0.1f, boundingBox.width, boundingBox.height);
        }

//        if(health <= 0) {
//            Assets.instance.fonts.miniFont.draw(batch, String.format(Locale.getDefault(), "+ %02d", points), boundingBox.x - 4, boundingBox.y + 6);
//        }
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[2]; // create lasers
        lasers[0] = new Laser(
                boundingBox.x + boundingBox.width * 0.40f,
                boundingBox.y,
                laserWidth, laserHeight,
                laserMovementSpeed, dmg/2f,
                laserTextureRegion);
        lasers[1] = new Laser(
                boundingBox.x + boundingBox.width * 0.60f,
                boundingBox.y,
                laserWidth, laserHeight,
                laserMovementSpeed, dmg/2f,
                laserTextureRegion);

        AudioManager.instance.play(Assets.instance.sounds.laser02);

        timeSinceLastShot = 0;
        return lasers;
    }
}
