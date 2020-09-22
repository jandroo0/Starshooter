package com.garcia.starshooter.game.objects.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.garcia.starshooter.game.objects.StarshooterGame;

// parent statics
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class ScreenTemplate implements Screen {

    StarshooterGame game; // parent game controller

    // screen
    Camera camera;
    Viewport viewport;

    // background gfx
    TextureAtlas textureAtlas;
    TextureRegion[] backgrounds;

    // timing
    float[] backgroundOffsets = {0,0,0,0};
    float backgroundMaxScrollingSpeed;
    float backgroundHeight;


    ScreenTemplate(StarshooterGame game) {
        this.game = game;

        camera = new OrthographicCamera(); // 2D camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera); // viewport

        // gfx
        textureAtlas = new TextureAtlas("sprites/texture_atlas.atlas");

        // background ------
        backgrounds = new TextureRegion[4];

        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = WORLD_HEIGHT / 4;
        // -----------------
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        game.batch.begin();

        renderBackground(deltaTime);

        game.batch.end();

    }

    // render scrolling background
    public void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollingSpeed; // / 1

        for(int layer = 0; layer < backgroundOffsets.length; layer++) {
            if(backgroundOffsets[layer] > WORLD_HEIGHT) { backgroundOffsets[layer] = 0; }

            game.batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, backgroundHeight);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);

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
