package com.garcia.starshooter.game.objects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.ships.enemies.EnemyShip;
import com.garcia.starshooter.game.objects.effects.Explosion;
import com.garcia.starshooter.game.objects.effects.Laser;
import com.garcia.starshooter.game.objects.ships.enemies.EnemyShipFactory;
import com.garcia.starshooter.game.objects.ships.player.PlayerShip;
import com.garcia.starshooter.game.objects.StarshooterGame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class GameScreen extends ScreenTemplate {

    // game objects
    Stage stage;

    public PlayerShip playerShip;
    private EnemyShipFactory factory = new EnemyShipFactory();

    private LinkedList<EnemyShip> enemyShipList;

    // in-time laser lists
    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;

    // waves
    private float waveTimer = 0;
    private float enemySpawnTimer = 0;
    private float timeBetweenWaves = 5f;
    private float timeBetweenEnemySpawns = 3f;
    private int enemiesSpawned = 0;
    private int enemiesDestroyed = 0;
    private int waveCounter = 1;
    private int maxEnemies = 2;
    private int totalEnemiesDestroyed = 0;

    private float ptsTimer = 0;
    private float timePtsDrawn = 2f;

    // explosions
    private Texture explosionTexture;
    private LinkedList<Explosion> explosionList;

    // HUD
    BitmapFont hudFont, statFont, miniFont;
    String waveColor, healthColor;
    TextButton.TextButtonStyle scoreLabelStyle, waveLabelStyle, healthLabelStyle, shieldLabelStyle;
    TextButton scoreLabel, waveLabel, healthLabel, shieldLabel;
//    Coin coin;
    TextureRegion playerLifeTexture, playerShieldTexture, numeralXTexture;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudRow3Y, hudRow4Y, hudSectionWidth;

    public int score = game.pts;
    public int prevPts = game.pts;

    private boolean gameOver;

    public GameScreen(StarshooterGame game) {
        super(game);

        // game objects

        this.playerShip = game.getPlayerShip();

        // enemies
        enemyShipList = new LinkedList<>();

        // lasers
        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();

        // explosions
        explosionTexture = new Texture("sprites/effects/explosion.png");
        explosionList = new LinkedList<>();

    }

    @Override
    public void show() {
        AudioManager.instance.play(Assets.instance.music.fightMusic);

        // DRAW HUD
        prepareHUD();

        playerShip.resetShip(); // reset playership

    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);


        if (!gameOver) {
            game.batch.begin();

            // input
            movement(deltaTime);

            // updates
            playerShip.update(deltaTime);

            // enemy spawning
            spawnEnemies(deltaTime);

            // create ship
            for (EnemyShip enemyShip : enemyShipList) {
                moveEnemy(enemyShip, deltaTime); // move ship

                enemyShip.update(deltaTime); // update ship

                enemyShip.draw(game.batch); // draw ship
            }

            playerShip.draw(game.batch); // draw player ship

            renderLasers(deltaTime); // draw lasers

            detectCollisions(deltaTime); // collisions

            renderExplosions(deltaTime); // explosions

            updateHUD(deltaTime); // update HUD

            game.batch.end();
            stage.draw();
        }
    }

    // HUD
    private void prepareHUD() { // change position and add ships
        stage = new Stage(viewport, game.batch);

        hudFont = game.hudFont;
        statFont = game.statFont;
        miniFont = game.miniFont;

        playerLifeTexture = playerShip.lifeIcon;
        playerShieldTexture = playerShip.shieldIcon;
        numeralXTexture = Assets.instance.hudAssets.NUMERAL_X;

        healthLabelStyle = new TextButton.TextButtonStyle();
        healthLabelStyle.font = statFont;

        waveLabelStyle = new TextButton.TextButtonStyle();
        waveLabelStyle.font = hudFont;

        shieldLabelStyle = new TextButton.TextButtonStyle();
        shieldLabelStyle.font = statFont;

        hudLeftX = WORLD_WIDTH * 0.1f;
        hudRightX = (WORLD_WIDTH / 2) + 18;

        hudRow1Y = WORLD_HEIGHT - hudFont.getCapHeight();

        waveLabel = new TextButton("WAVE", waveLabelStyle);
        waveLabel.setPosition((WORLD_WIDTH - waveLabel.getWidth()) / 2, hudRow1Y - 6);

        healthLabel = new TextButton("", healthLabelStyle);
        healthLabel.setPosition(hudRightX, hudRow1Y - 5);

        shieldLabel = new TextButton("", shieldLabelStyle);
        shieldLabel.setPosition(hudRightX, healthLabel.getY() - 10);

        stage.addActor(waveLabel);
        stage.addActor(healthLabel);
        stage.addActor(shieldLabel);

//        coin = new Coin(hudLeftX - 5, hudRow1Y - 6);
        Assets.instance.coin.setPos(hudLeftX - 5, hudRow1Y - 6);
        waveColor = "#008000";
    }

    // update HUD
    private void updateHUD(float deltaTime) { // change position and add small ships
//        coin.update(deltaTime);
        Assets.instance.coin.update(deltaTime);
        if(playerShip.health >= 3) healthColor = "#008800"; else healthColor = "#FF0000";

//        coin.draw(game.batch);
        Assets.instance.coin.draw(game.batch);

        hudFont.draw(game.batch, String.format(Locale.getDefault(), "%04d", score), hudLeftX, hudRow1Y);// pts

        hudFont.draw(game.batch, String.format(Locale.getDefault(), "%02d", waveCounter), waveLabel.getX() + 4, waveLabel.getY() - 2); // wave counter

        // health
        game.batch.draw(playerLifeTexture, healthLabel.getX(), healthLabel.getY(), 5, 5); // player lives
        game.batch.draw(numeralXTexture, healthLabel.getX() + 5, healthLabel.getY() + 1, 3, 3); // numeral x
        statFont.draw(game.batch, String.format(Locale.getDefault(), "[" + healthColor + "]%02d", playerShip.health), healthLabel.getX() + 8 , healthLabel.getY() + 5);

        // shield
        game.batch.draw(playerShieldTexture, shieldLabel.getX(), shieldLabel.getY(), 5, 5); // player lives
        game.batch.draw(numeralXTexture, shieldLabel.getX() + 5, shieldLabel.getY() + 1, 3, 3); // numeral x
        statFont.draw(game.batch, String.format(Locale.getDefault(), "%02d", playerShip.shield), shieldLabel.getX() + 8 , shieldLabel.getY() + 5);


    }

    // detect user input
    private void movement(float deltaTime) { // add direction?
        // keyboard input

        float leftLimit, rightLimit, upLimit, downLimit;

        leftLimit = -playerShip.boundingBox.x; // if x = 31, limit is -31 since 31 + (-31) = 0
        downLimit = -playerShip.boundingBox.y;

        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height; // cut in half so there is barrier

        // up
        if (Gdx.input.isKeyPressed(Input.Keys.W) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, upLimit));
        }
        // left
        if (Gdx.input.isKeyPressed(Input.Keys.A) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime, leftLimit), 0f);
        }
        // down
        if (Gdx.input.isKeyPressed(Input.Keys.S) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.movementSpeed * deltaTime, downLimit));
        }
        // right
        if (Gdx.input.isKeyPressed(Input.Keys.D) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime, rightLimit), 0f);
        }
        // fire
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers(); // returns lasers array to be fired
            playerLaserList.addAll(Arrays.asList(lasers)); // add lasers to lasers array
        }
    }

    // player fire lasers
    private void renderLasers(float deltaTime) {

        // draw player lasers from array
        ListIterator<Laser> playerLaserListIterator = playerLaserList.listIterator(); // player laser list
        while (playerLaserListIterator.hasNext()) {
            Laser laser = playerLaserListIterator.next();
            laser.draw(game.batch); // draw laser

            laser.boundingBox.y += laser.movementSpeed * deltaTime; // move laser based on speed

            if (laser.boundingBox.y > WORLD_HEIGHT) {
                playerLaserListIterator.remove(); // remove if off screen
            }
        }

        // add enemy lasers to array
        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator(); // enemy ship list
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }

        ListIterator<Laser> enemyLaserListIterator = enemyLaserList.listIterator();
        while (enemyLaserListIterator.hasNext()) {
            Laser laser = enemyLaserListIterator.next();
            laser.draw(game.batch); // draw laser
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;

            if (laser.boundingBox.y + laser.boundingBox.height < 0) { // if out of bounds
                enemyLaserListIterator.remove(); // removes last item retrieved
            }
        }
    }

    // collision detection
    private void detectCollisions(float deltaTime) {
        ListIterator<Laser> laserListIterator; // laser list iterator

        laserListIterator = playerLaserList.listIterator(); // player laser list
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next(); // player laser

            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    if (enemyShip.shield <= 0) {
                        explosionList.add(new Explosion(explosionTexture, new Rectangle(enemyShip.boundingBox), .4f));
                    }
                    if (enemyShip.hit(laser.dmg)) { // check hit and if destroyed
                        score += enemyShip.points;
                        enemyShipListIterator.remove();
                        drawPtsEarned(enemyShip, deltaTime);
                        enemiesDestroyed++;
                        totalEnemiesDestroyed++;
                        explosionList.add(new Explosion(explosionTexture, new Rectangle(enemyShip.boundingBox), 1f));
                    }
                    laserListIterator.remove();
                    break;
                }
            }
        }

        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();

            if (playerShip.intersects(laser.boundingBox)) { // if ship is hit
                if (playerShip.shield <= 0) {
                    explosionList.add(new Explosion(explosionTexture, new Rectangle(playerShip.boundingBox), .7f));
                }
                if (playerShip.hit(laser.dmg)) {
                    gameOver();
                }
                laserListIterator.remove(); // remove laser
            }
        }
    }

    private void gameOver() {
        if (waveCounter > game.getHighestWave()) { // check if highscore is greater
            game.setHighestWave(waveCounter);
        }

        game.pts = score;

        gameOver = true; // stop rendering
        game.setScreen(new GameOverScreen(game, waveCounter, totalEnemiesDestroyed, prevPts)); // set new screen
    }

    // spawn enemies
    public void spawnEnemies(float deltaTime) { // better enemy spawning
        waveTimer += deltaTime;

        if (waveTimer > timeBetweenWaves) { // after time between waves
            enemySpawnTimer += deltaTime;
            if (enemySpawnTimer > timeBetweenEnemySpawns && enemiesSpawned < maxEnemies) { // after enemy spawn timer and only if its less than max enemies
                enemyShipList.add(enemyType());
                enemiesSpawned++;
                enemySpawnTimer -= timeBetweenEnemySpawns;
            }
        }

        if (enemiesDestroyed == maxEnemies) {
            nextWave();
        }

    }

    private void nextWave() {
        waveCounter++;
        // next wave sfx
        enemiesDestroyed += maxEnemies;
//        AudioManager.instance.play(Assets.instance.sounds.nextWave);

        changeWaveColor();

        maxEnemies += 1;

        enemiesSpawned = 0;
        enemiesDestroyed = 0;

        enemySpawnTimer = 0;
        waveTimer = 0;
    }

    // move enemies "ai"
    public void moveEnemy(EnemyShip enemyShip, float deltaTime) { // better enemy movement
        float leftLimit, rightLimit, upLimit, downLimit;

        leftLimit = -enemyShip.boundingBox.x;
        downLimit = WORLD_HEIGHT / 2 - enemyShip.boundingBox.y;

        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height; // cut in half so there is barrier

        // scale to max ship speed
        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        // if there is space

        // x
        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        // y
        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }

    // render all explosions
    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();

            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(game.batch);
            }
        }
    }

    private EnemyShip enemyType() { // temp -- returns enemy type based on wave -- make better
//        return factory.createEnemyShip(waveCounter);


        if(waveCounter >= 1 && waveCounter <= 3)
            return new EnemyShip(25, 1,2,1, 40, 46, 0.7f, 0.4f, 4f, Assets.instance.enemyShips.ENEMY_BLACK_02, Assets.instance.lasers.LASER_GREEN_06);

        return new EnemyShip(25, 1,2,1, 40, 46, 0.7f, 0.4f, 4f, Assets.instance.enemyShips.ENEMY_BLACK_02, Assets.instance.lasers.LASER_GREEN_06);
    }

    private void changeWaveColor() { // change wave number eventually make one that changes high score color, and stats color based on level
        if  (waveCounter >= 1 && waveCounter <= 5) { // change at round 1
            waveColor = "#008000";
        } else if (waveCounter >= 6 && waveCounter <= 10) {
            waveColor = "#FFFF00";
        } else if (waveCounter >= 11) {
            waveColor = "#ff0000";
        }
    }

    private void drawPtsEarned(EnemyShip enemyShip, float deltaTime) {
        ptsTimer += deltaTime;


        if(ptsTimer < timePtsDrawn) {
            hudFont.draw(game.batch, "[#008800]+ " + enemyShip.points, hudLeftX + 10, hudRow1Y - 5);
        }
    }

    public void setPlayerShip(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}

