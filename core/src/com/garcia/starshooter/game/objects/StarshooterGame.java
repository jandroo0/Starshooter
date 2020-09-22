package com.garcia.starshooter.game.objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.garcia.starshooter.game.objects.screens.GameOverScreen;
import com.garcia.starshooter.game.objects.screens.TitleScreen;
import com.garcia.starshooter.game.objects.ships.player.PlayerShip;
import com.garcia.starshooter.game.objects.ships.player.StarterShip;

import java.util.Random;

public class StarshooterGame extends Game { // music looping into certain screens based on current screen in hide method **

    // gfx
    public SpriteBatch batch;

    // random
    public static final Random random = new Random();

    // fonts
    public BitmapFont titleFont, hudFont, buttonFont, statFont, miniFont; // font for in game HUD

    // world vars
    public static float WORLD_WIDTH = 72;
    public static float WORLD_HEIGHT = 128;

    // player
    private PlayerShip playerShip;
    public int pts = 999999;
    public boolean hasPlayed;

    private Preferences userPrefs;


    @Override
    public void create() {
        batch = new SpriteBatch(); // global gfx batch

        Assets.instance.init(new AssetManager()); // asset manager

        loadPrefs(); // prefs

//        loadShip();
        playerShip = new StarterShip();

        setScreen(new TitleScreen(this)); // set screen to new title screen

    }

    private void loadPrefs() {

        userPrefs = Gdx.app.getPreferences("gamePreferences");
        hasPlayed = userPrefs.getBoolean("hasPlayed", false);

        titleFont = Assets.instance.fonts.titleFont;
        hudFont = Assets.instance.fonts.hudFont;
        buttonFont = Assets.instance.fonts.buttonFont;
        statFont = Assets.instance.fonts.statFont;
        miniFont = Assets.instance.fonts.miniFont;
    }

    public void setHighestWave(int wave) {
        userPrefs.putInteger("highestWave", wave);
        userPrefs.flush();
    }

    public void setPlayedTrue() {
        hasPlayed = true;
        userPrefs.putBoolean("hasPlayed", true);
        userPrefs.flush();

    }

    public void savePts(int pts) {
        userPrefs.putInteger("points", pts); // save new pts value
        userPrefs.flush();
    }

    public int getPts() {
        return userPrefs.getInteger("points", 0);
    }

    public int getHighestWave() {
        return userPrefs.getInteger("highestWave", 0);
    }


    public PlayerShip getPlayerShip() {
        return playerShip;
    }

    public void setPlayerShip(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
