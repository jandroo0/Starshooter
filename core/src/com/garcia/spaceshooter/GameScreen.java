package com.garcia.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen {

    // game controller
    SpaceShooterGame game;

    // screen
    private Camera camera;
    private Viewport viewport;

    // gfx
    private SpriteBatch batch; // list of sprites/gfx to display in order
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds;
    private Texture explosionTexture;

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion,
            enemyShipTextureRegion, enemyShieldTextureRegion, playerLaserTextureRegion,
            enemyLaserTextureRegion;


    // timing
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    private float backgroundHeight;
    private float timeBetweenEnemySpawns = 1f;
    private float enemySpawnTimer = 0;

    // world params
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;

    // game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;

    private LinkedList<Laser> playerLaserList; // easy to remove items from middle of list without
    private LinkedList<Laser> enemyLaserList;   // shuffling all other elements -- reduces cost

    private LinkedList<Explosion> explosionList;

    private int score = 0;
    private boolean gameOver = false;

    // hud
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;

    // -----

    GameScreen(SpaceShooterGame game) {
        this.game = game;

        camera = new OrthographicCamera(); // 2d camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // texture atlas
        textureAtlas = new TextureAtlas("images.atlas");
        backgrounds = new TextureRegion[4];

        // background
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        playerShipTextureRegion = textureAtlas.findRegion("playerShip"); // player ship
        enemyShipTextureRegion = textureAtlas.findRegion("enemyShip"); // enemy ship

        playerShieldTextureRegion = textureAtlas.findRegion("shield2"); // player shield
        enemyShieldTextureRegion = textureAtlas.findRegion("shield"); // enemy shield
        enemyShieldTextureRegion.flip(false, true);

        playerLaserTextureRegion = textureAtlas.findRegion("playerLaser"); // player laser
        enemyLaserTextureRegion = textureAtlas.findRegion("enemyLaser"); // enemy laser

        explosionTexture = new Texture("explosion.png"); // explosion

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (WORLD_HEIGHT) / 4;

        // game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                10, 10,
                36, 3,
                0.4f, 4,
                45, 0.5f, // hard coded values for now
                playerShipTextureRegion,
                playerShieldTextureRegion,
                playerLaserTextureRegion);

        enemyShipList = new LinkedList<>();

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();

        explosionList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD(); // heads up display

    }

    private void prepareHUD() {
        // create bitmap font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1,1,1,0.3f);
        fontParameter.borderColor = new Color(0,0,0,0.3f);

        font = fontGenerator.generateFont(fontParameter);

        // scale font to fit world
        font.getData().setScale(0.08f);

        // calculate hud margins
        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        batch.begin();
        if(gameOver) {
            // game over screen
            renderBackground(deltaTime); // scrolling background
            prepareGameOverMenu();

            StringBuilder sb = new StringBuilder("GAME OVER");
            font.setColor(new Color(1,0,0,0.85f));
            font.draw(batch, new String(sb), hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false);

            sb = new StringBuilder("PRESS SPACE\nTO RETRY");
            font.setColor(new Color(1,1,1,0.85f));
            font.draw(batch, new String(sb), hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false);

        } else {

            renderBackground(deltaTime); // scrolling background

            detectInput(deltaTime); // detects user input

            playerShip.update(deltaTime); // update player ships

            // enemy ships
            spawnEnemies(deltaTime); // spawn enemies

            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                moveEnemy(enemyShip, deltaTime); // move ship

                enemyShip.update(deltaTime); // update ship

                enemyShip.draw(batch); // draw ship
            }


            playerShip.draw(batch); // player ship

            renderLasers(deltaTime); // lasers

            detectCollisions(); // collisions

            renderExplosions(deltaTime); // explosions

            updateHUD(); // update hud
        }

        batch.end();
    }

    private void updateHUD() {
        // top row
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false); // score label
        font.draw(batch, "Shield", hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false); // shield label
        font.draw(batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false); // lives label

        // second row
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false); // score value
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.shield), hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false); // shield value
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false); // lives value
    }

    // set up game over menu
    private void prepareGameOverMenu() {
        // create bitmap font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1,1,1,0.3f);
        fontParameter.borderColor = new Color(0,0,0,0.3f);
        fontParameter.spaceX = 10;

        font = fontGenerator.generateFont(fontParameter);

        // scale font to fit world
        font.getData().setScale(0.12f);

        // calculate hud margins
        hudVerticalMargin = font.getCapHeight() / 2;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - WORLD_HEIGHT / 3;
        hudRow2Y = WORLD_HEIGHT / 2 - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    // background stars and parallaxing effect
    private void renderBackground(float deltaTime) {

        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed; // / 1

        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, backgroundHeight);

        }
    }

    // enemy control
    private void spawnEnemies(float deltaTime) {
        enemySpawnTimer += deltaTime;
        if(enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5,
                    10, 10,
                    36, 1,
                    0.3f, 5,
                    50, 0.8f, // hard coded values for now
                    enemyShipTextureRegion,
                    enemyShieldTextureRegion,
                    enemyLaserTextureRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    // detect user input
    private void detectInput(float deltaTime) {
        // keyboard
        // determine max distance ship can move
        // check each key that matters and move accordingly

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;

        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float)WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height; // cut in half so there is barrier

        // right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime, rightLimit), 0f);
        }
        // up
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, upLimit));
        }
        // left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime, leftLimit), 0f);
        }
        // down
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.movementSpeed * deltaTime, downLimit));
        }

        // mouse / touch
        if(Gdx.input.isTouched()) {
            // get screen pos of touch
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            // convert to world pos
            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            // calculate the x and y diff
            Vector2 playerShipCentre = new Vector2(
                    playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height/2);

            float touchDistance = touchPoint.dst(playerShipCentre); // distance between touch point and ship center pos

            if(touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

                // scale to max ship speed
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                // if there is space
                // x
                if(xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                // y
                if(yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }

        }
    }

    // enemy "ai"
    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = WORLD_HEIGHT / 2 - enemyShip.boundingBox.y; // cut in half so there is barrier

        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        // scale to max ship speed
        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        // if there is space
        // x
        if(xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        // y
        if(yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }

    // detect collisions between lasers and ships
    private void detectCollisions() {

        ListIterator<Laser> laserListIterator; // iterator for laser lists

        // for each player laser check whether it intersects with an enemy ship
        laserListIterator = playerLaserList.listIterator();

        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next(); // player laser

            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while(enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next(); // for every enemy in enemy list

                if (enemyShip.intersects(laser.boundingBox)) { // does the enemy ship intersect with bounding box of laser
                    if(enemyShip.hit(laser)) { // check contact with laser
                        enemyShipListIterator.remove(); // if enemy ship is destroyed
                        score += 100;
                        explosionList.add(new Explosion(explosionTexture, new Rectangle(enemyShip.boundingBox), 0.7f)); // play explosion
                    }
                    laserListIterator.remove(); // remove laser
                    break;
                }
            }
        }

        // for each enemy laser check whether it intersects with the player ship
        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next(); // enemy laser
            if (playerShip.intersects(laser.boundingBox)) { // does the player ship intersect with bounding box of laser
                if(playerShip.hit(laser)) {
                    explosionList.add(new Explosion(explosionTexture, new Rectangle(playerShip.boundingBox), 1.6f)); // play explosion
                    playerShip.lives--;

                    if(playerShip.lives < 0) {
                        gameOver = true;
                    }
                }
                laserListIterator.remove(); // remove laser
            }
        }

    }

    // render lasers : create -- draw -- dispose
    private void renderLasers(float deltaTime) {
        // create lasers
        // player lasers
        if (playerShip.canFireLaser()) { // if ship is ready to fire
            Laser[] lasers = playerShip.fireLasers(); // returns lasers that will be fired -- array of size 2
//            playerLaserList.addAll(Arrays.asList(lasers)); -- same thing as for loop below
            // adds lasers being fired to current laser list
            playerLaserList.addAll(Arrays.asList(lasers));
        }

        // enemy lasers
        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
        while(enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            if (enemyShip.canFireLaser()) { // if ship is ready to fire
                Laser[] lasers = enemyShip.fireLasers(); // returns lasers that will be fired -- array of size 2
                // adds lasers being fired to current laser list
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }

        // draw & dispose lasers
        ListIterator<Laser> iterator;

        // player lasers
        iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime; // change distance up screen

            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove(); // removes last item that was retrieved from iterator -- therefore the last laser shot
            }
        }

        // enemy lasers
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime; // change distance up screen

            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove(); // removes last item that was retrieved from iterator -- therefore the last laser shot
            }
        }
    }

    // update and render explosions
    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while(explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime); // update
            if(explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
