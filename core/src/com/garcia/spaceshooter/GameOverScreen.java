package com.garcia.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

    // screen
    private Camera camera;
    private Viewport viewport;

    // gfx
    private SpriteBatch batch; // list of sprites/gfx to display in order
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds;

    // timing
    private float[] backgroundOffsets = {0, 0, 0, 0};
    private float backgroundMaxScrollingSpeed;
    private float backgroundHeight;

    // world params
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;

    // hud
    BitmapFont font;
    float hudVerticalMargin, hudCentreX, hudRow1Y, hudSectionWidth;

    public GameOverScreen() {
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

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (WORLD_HEIGHT) / 4;

        batch = new SpriteBatch();

        prepareGameOverMenu(); // prepare game over menu
    }

    private void prepareGameOverMenu() {
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
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT / 2;
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        renderBackground(deltaTime); // scrolling background

        StringBuilder sb = new StringBuilder("GAME OVER\nPRESS SPACE\nTO CONTINUE...");
        font.draw(batch, new String(sb), hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false);

        batch.end();
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

    @Override
    public void resize(int width, int height) {

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
