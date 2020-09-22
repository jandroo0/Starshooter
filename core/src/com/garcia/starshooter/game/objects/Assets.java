package com.garcia.starshooter.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    public AssetPlayerShips playerShips;
    public AssetEnemyShips enemyShips;
    public AssetLasers lasers;
    public AssetCoin coin;
//    public AssetExplosion explosion;
    public AssetHUD hudAssets;
    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);

        // load texture atlas
        assetManager.load("sprites/texture_atlas.atlas", TextureAtlas.class);

        // load sounds
        assetManager.load("sfx/sfx_laser1.ogg", Sound.class);
        assetManager.load("sfx/sfx_laser2.ogg", Sound.class);
        assetManager.load("sfx/sfx_lose.ogg", Sound.class);
        assetManager.load("sfx/sfx_shieldDown.ogg", Sound.class);
        assetManager.load("sfx/sfx_twoTone.ogg", Sound.class);
        assetManager.load("sfx/sfx_explosion.mp3", Sound.class);
        assetManager.load("sfx/sfx_select.wav", Sound.class);
        assetManager.load("sfx/sfx_gameStart.ogg", Sound.class);
        assetManager.load("sfx/sfx_click02.mp3", Sound.class);
        assetManager.load("sfx/sfx_error01.ogg", Sound.class);
        assetManager.load("sfx/sfx_error02.ogg", Sound.class);
        assetManager.load("sfx/sfx_shipUpgrade.mp3", Sound.class);
        assetManager.load("sfx/sfx_statMaxed.mp3", Sound.class);
        assetManager.load("sfx/sfx_allStatsMax.mp3", Sound.class);
        assetManager.load("sfx/sfx_nextWave.mp3", Sound.class);


        // load music
        assetManager.load("music/fight_music.wav", Music.class);
        assetManager.load("music/fight_music02.wav", Music.class);

        assetManager.load("music/menu_music.wav", Music.class);
        assetManager.load("music/menu_music02.wav", Music.class);

        // load coin
        assetManager.load("sprites/effects/coin_01.png", Texture.class);

        // explosion
        assetManager.load("sprites/effects/explosion.png", Texture.class);

        // start loading assets and wait until finished
        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "asset: " + a);
        }

        TextureAtlas atlas = assetManager.get("sprites/texture_atlas.atlas");
        // enable texture filtering for pixel smoothing
        for (Texture t : atlas.getTextures()) {
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // create game resource objects
        fonts = new AssetFonts();
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
        playerShips = new AssetPlayerShips(atlas);
        enemyShips = new AssetEnemyShips(atlas);
        lasers = new AssetLasers(atlas);
        coin = new AssetCoin(assetManager);
//        explosion = new AssetExplosion(assetManager);
        hudAssets = new AssetHUD(atlas);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.titleFont.dispose();
        fonts.hudFont.dispose();
        fonts.buttonFont.dispose();
        fonts.statFont.dispose();
        fonts.miniFont.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    // sounds
    public class AssetSounds {

        // game sfx
        public final Sound laser01;
        public final Sound laser02;
        public final Sound shieldDown;
        public final Sound explosion;
        public final Sound gameStart;
        public final Sound gameOver;
        public final Sound nextWave;

        // upgrade screen
        public final Sound statUpgrade; // individual stat upgrade -- coin snd
        public final Sound statReachedMax; // individual stat upgrade -- coin snd
        public final Sound statIsMax; // cant upgrade stat is max snd
        public final Sound allStatsMax; // when all stats are max chime


        // ui sfx
        public final Sound uiRollover; // button hover
        public final Sound uiClick; // succesfull select
        public final Sound uiCantSelect; // cant select / not enough pts


        public AssetSounds(AssetManager am) {
            laser01 = am.get("sfx/sfx_laser1.ogg", Sound.class);
            laser02 = am.get("sfx/sfx_laser2.ogg", Sound.class);

            gameOver = am.get("sfx/sfx_lose.ogg", Sound.class);
            shieldDown = am.get("sfx/sfx_shieldDown.ogg", Sound.class);
            gameStart = am.get("sfx/sfx_gameStart.ogg", Sound.class);
            explosion = am.get("sfx/sfx_explosion.mp3", Sound.class);
            uiRollover = am.get("sfx/sfx_select.wav", Sound.class);
            uiClick = am.get("sfx/sfx_click02.mp3", Sound.class);
            uiCantSelect = am.get("sfx/sfx_error01.ogg", Sound.class);
            statIsMax = am.get("sfx/sfx_error02.ogg", Sound.class);
            statUpgrade = am.get("sfx/sfx_shipUpgrade.mp3", Sound.class);
            statReachedMax = am.get("sfx/sfx_statMaxed.mp3", Sound.class);
            allStatsMax = am.get("sfx/sfx_allStatsMax.mp3", Sound.class);
            nextWave = am.get("sfx/sfx_nextWave.mp3", Sound.class);
        }
    }

    // music
    public class AssetMusic {
        public final Music menuMusic;
        public final Music fightMusic;
        public final Music gameOverMusic;

        public AssetMusic(AssetManager am) {
            menuMusic = am.get("music/menu_music.wav", Music.class);

            fightMusic = am.get("music/fight_music.wav", Music.class);

            gameOverMusic = am.get("music/menu_music02.wav", Music.class);
        }
    }

    // fonts
    public class AssetFonts {
        public final BitmapFont titleFont;
        public final BitmapFont hudFont;
        public final BitmapFont buttonFont;
        public final BitmapFont statFont;
        public final BitmapFont miniFont;

        public AssetFonts() {
            //  default font parameters ------
            FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/GalaxyFont.otf"));
            FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();

            fontParameters.size = 72;
            fontParameters.borderWidth = 3.6f;
            fontParameters.color = new Color(1, 1, 1, 0.3f);
            fontParameters.borderColor = new Color(0, 0, 0, 0.3f);
            // ------

            // font settings ------
            titleFont = fontGenerator.generateFont(fontParameters);// generate HUD font with paramater
            hudFont = fontGenerator.generateFont(fontParameters);
            buttonFont = fontGenerator.generateFont(fontParameters);
            statFont = fontGenerator.generateFont(fontParameters);
            miniFont = fontGenerator.generateFont(fontParameters);

            titleFont.getData().setScale(0.11f);
            hudFont.getData().setScale(0.08f);
            buttonFont.getData().setScale(0.09f);
            statFont.getData().setScale(0.07f);
            miniFont.getData().setScale(0.03f);

            titleFont.setUseIntegerPositions(false);
            titleFont.getData().markupEnabled = true; // html markup
            titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            hudFont.setUseIntegerPositions(false);
            hudFont.getData().markupEnabled = true; // html markup
            hudFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            buttonFont.setUseIntegerPositions(false);
            buttonFont.getData().markupEnabled = true; // html markup
            buttonFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            statFont.setUseIntegerPositions(false);
            statFont.getData().markupEnabled = true; // html markup
            statFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            miniFont.setUseIntegerPositions(false);
            miniFont.getData().markupEnabled = true; // html markup
            miniFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            // -------------------------------
        }
    }

    // player ships
    public class AssetPlayerShips {
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_1_BLUE;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_2_BLUE;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_3_BLUE;

        public final TextureAtlas.AtlasRegion PLAYER_SHIP_1_GREEN;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_2_GREEN;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_3_GREEN;

        public final TextureAtlas.AtlasRegion PLAYER_SHIP_1_ORANGE;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_2_ORANGE;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_3_ORANGE;

        public final TextureAtlas.AtlasRegion PLAYER_SHIP_1_RED;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_2_RED;
        public final TextureAtlas.AtlasRegion PLAYER_SHIP_3_RED;

        public AssetPlayerShips(TextureAtlas atlas) {
            PLAYER_SHIP_1_BLUE = atlas.findRegion("playerShip1_blue");
            PLAYER_SHIP_2_BLUE = atlas.findRegion("playerShip2_blue");
            PLAYER_SHIP_3_BLUE = atlas.findRegion("playerShip3_blue");

            PLAYER_SHIP_1_GREEN = atlas.findRegion("playerShip1_green");
            PLAYER_SHIP_2_GREEN = atlas.findRegion("playerShip2_green");
            PLAYER_SHIP_3_GREEN = atlas.findRegion("playerShip3_green");

            PLAYER_SHIP_1_ORANGE = atlas.findRegion("playerShip1_orange");
            PLAYER_SHIP_2_ORANGE = atlas.findRegion("playerShip2_orange");
            PLAYER_SHIP_3_ORANGE = atlas.findRegion("playerShip3_orange");

            PLAYER_SHIP_1_RED = atlas.findRegion("playerShip1_red");
            PLAYER_SHIP_2_RED = atlas.findRegion("playerShip2_red");
            PLAYER_SHIP_3_RED = atlas.findRegion("playerShip3_red");
        }
    }


    // enemy ships
    public class AssetEnemyShips {
        public final TextureAtlas.AtlasRegion ENEMY_BLACK_01;
        public final TextureAtlas.AtlasRegion ENEMY_BLACK_02;
        public final TextureAtlas.AtlasRegion ENEMY_BLACK_03;
        public final TextureAtlas.AtlasRegion ENEMY_BLACK_04;
        public final TextureAtlas.AtlasRegion ENEMY_BLACK_05;

        public final TextureAtlas.AtlasRegion ENEMY_GREEN_01;
        public final TextureAtlas.AtlasRegion ENEMY_GREEN_02;
        public final TextureAtlas.AtlasRegion ENEMY_GREEN_03;
        public final TextureAtlas.AtlasRegion ENEMY_GREEN_04;
        public final TextureAtlas.AtlasRegion ENEMY_GREEN_05;

        public final TextureAtlas.AtlasRegion ENEMY_RED_01;
        public final TextureAtlas.AtlasRegion ENEMY_RED_02;
        public final TextureAtlas.AtlasRegion ENEMY_RED_03;
        public final TextureAtlas.AtlasRegion ENEMY_RED_04;
        public final TextureAtlas.AtlasRegion ENEMY_RED_05;

        public final TextureAtlas.AtlasRegion ENEMY_BLUE_01;
        public final TextureAtlas.AtlasRegion ENEMY_BLUE_02;
        public final TextureAtlas.AtlasRegion ENEMY_BLUE_03;
        public final TextureAtlas.AtlasRegion ENEMY_BLUE_04;
        public final TextureAtlas.AtlasRegion ENEMY_BLUE_05;

        public AssetEnemyShips(TextureAtlas atlas) {

            // black ships
            ENEMY_BLACK_01 = atlas.findRegion("enemyBlack1");
            ENEMY_BLACK_02 = atlas.findRegion("enemyBlack2");
            ENEMY_BLACK_03 = atlas.findRegion("enemyBlack3");
            ENEMY_BLACK_04 = atlas.findRegion("enemyBlack4");
            ENEMY_BLACK_05 = atlas.findRegion("enemyBlack5");

            // green ships
            ENEMY_GREEN_01 = atlas.findRegion("enemyGreen1");
            ENEMY_GREEN_02 = atlas.findRegion("enemyGreen2");
            ENEMY_GREEN_03 = atlas.findRegion("enemyGreen3");
            ENEMY_GREEN_04 = atlas.findRegion("enemyGreen4");
            ENEMY_GREEN_05 = atlas.findRegion("enemyGreen5");

            // red ships
            ENEMY_RED_01 = atlas.findRegion("enemyRed1");
            ENEMY_RED_02 = atlas.findRegion("enemyRed2");
            ENEMY_RED_03 = atlas.findRegion("enemyRed3");
            ENEMY_RED_04 = atlas.findRegion("enemyRed4");
            ENEMY_RED_05 = atlas.findRegion("enemyRed5");

            // blue ship
            ENEMY_BLUE_01 = atlas.findRegion("enemyBlue1");
            ENEMY_BLUE_02 = atlas.findRegion("enemyBlue2");
            ENEMY_BLUE_03 = atlas.findRegion("enemyBlue3");
            ENEMY_BLUE_04 = atlas.findRegion("enemyBlue4");
            ENEMY_BLUE_05 = atlas.findRegion("enemyBlue5");
        }
    }

    // lasers
    public class AssetLasers {
        public final TextureAtlas.AtlasRegion LASER_RED_01;
        public final TextureAtlas.AtlasRegion LASER_RED_02;
        public final TextureAtlas.AtlasRegion LASER_RED_03;
        public final TextureAtlas.AtlasRegion LASER_RED_04;
        public final TextureAtlas.AtlasRegion LASER_RED_05;
        public final TextureAtlas.AtlasRegion LASER_RED_06;
        public final TextureAtlas.AtlasRegion LASER_RED_07;
        public final TextureAtlas.AtlasRegion LASER_RED_08;
        public final TextureAtlas.AtlasRegion LASER_RED_09;
        public final TextureAtlas.AtlasRegion LASER_RED_10;
        public final TextureAtlas.AtlasRegion LASER_RED_11;
        public final TextureAtlas.AtlasRegion LASER_RED_12;
        public final TextureAtlas.AtlasRegion LASER_RED_13;
        public final TextureAtlas.AtlasRegion LASER_RED_14;
        public final TextureAtlas.AtlasRegion LASER_RED_15;
        public final TextureAtlas.AtlasRegion LASER_RED_16;

        public final TextureAtlas.AtlasRegion LASER_BLUE_01;
        public final TextureAtlas.AtlasRegion LASER_BLUE_02;
        public final TextureAtlas.AtlasRegion LASER_BLUE_03;
        public final TextureAtlas.AtlasRegion LASER_BLUE_04;
        public final TextureAtlas.AtlasRegion LASER_BLUE_05;
        public final TextureAtlas.AtlasRegion LASER_BLUE_06;
        public final TextureAtlas.AtlasRegion LASER_BLUE_07;
        public final TextureAtlas.AtlasRegion LASER_BLUE_08;
        public final TextureAtlas.AtlasRegion LASER_BLUE_09;
        public final TextureAtlas.AtlasRegion LASER_BLUE_10;
        public final TextureAtlas.AtlasRegion LASER_BLUE_11;
        public final TextureAtlas.AtlasRegion LASER_BLUE_12;
        public final TextureAtlas.AtlasRegion LASER_BLUE_13;
        public final TextureAtlas.AtlasRegion LASER_BLUE_14;
        public final TextureAtlas.AtlasRegion LASER_BLUE_15;
        public final TextureAtlas.AtlasRegion LASER_BLUE_16;

        public final TextureAtlas.AtlasRegion LASER_GREEN_01;
        public final TextureAtlas.AtlasRegion LASER_GREEN_02;
        public final TextureAtlas.AtlasRegion LASER_GREEN_03;
        public final TextureAtlas.AtlasRegion LASER_GREEN_04;
        public final TextureAtlas.AtlasRegion LASER_GREEN_05;
        public final TextureAtlas.AtlasRegion LASER_GREEN_06;
        public final TextureAtlas.AtlasRegion LASER_GREEN_07;
        public final TextureAtlas.AtlasRegion LASER_GREEN_08;
        public final TextureAtlas.AtlasRegion LASER_GREEN_09;
        public final TextureAtlas.AtlasRegion LASER_GREEN_10;
        public final TextureAtlas.AtlasRegion LASER_GREEN_11;
        public final TextureAtlas.AtlasRegion LASER_GREEN_12;
        public final TextureAtlas.AtlasRegion LASER_GREEN_13;
        public final TextureAtlas.AtlasRegion LASER_GREEN_14;
        public final TextureAtlas.AtlasRegion LASER_GREEN_15;
        public final TextureAtlas.AtlasRegion LASER_GREEN_16;


        public AssetLasers(TextureAtlas atlas) {

            // red lasers
            LASER_RED_01 = atlas.findRegion("laserRed01");
            LASER_RED_02 = atlas.findRegion("laserRed02");
            LASER_RED_03 = atlas.findRegion("laserRed03");
            LASER_RED_04 = atlas.findRegion("laserRed04");
            LASER_RED_05 = atlas.findRegion("laserRed05");
            LASER_RED_06 = atlas.findRegion("laserRed06");
            LASER_RED_07 = atlas.findRegion("laserRed07");
            LASER_RED_08 = atlas.findRegion("laserRed08");
            LASER_RED_09 = atlas.findRegion("laserRed09");
            LASER_RED_10 = atlas.findRegion("laserRed10");
            LASER_RED_11 = atlas.findRegion("laserRed11");
            LASER_RED_12 = atlas.findRegion("laserRed12");
            LASER_RED_13 = atlas.findRegion("laserRed13");
            LASER_RED_14 = atlas.findRegion("laserRed14");
            LASER_RED_15 = atlas.findRegion("laserRed15");
            LASER_RED_16 = atlas.findRegion("laserRed16");

            // blue lasers
            LASER_BLUE_01 = atlas.findRegion("laserBlue01");
            LASER_BLUE_02 = atlas.findRegion("laserBlue02");
            LASER_BLUE_03 = atlas.findRegion("laserBlue03");
            LASER_BLUE_04 = atlas.findRegion("laserBlue04");
            LASER_BLUE_05 = atlas.findRegion("laserBlue05");
            LASER_BLUE_06 = atlas.findRegion("laserBlue06");
            LASER_BLUE_07 = atlas.findRegion("laserBlue07");
            LASER_BLUE_08 = atlas.findRegion("laserBlue08");
            LASER_BLUE_09 = atlas.findRegion("laserBlue09");
            LASER_BLUE_10 = atlas.findRegion("laserBlue10");
            LASER_BLUE_11 = atlas.findRegion("laserBlue11");
            LASER_BLUE_12 = atlas.findRegion("laserBlue12");
            LASER_BLUE_13 = atlas.findRegion("laserBlue13");
            LASER_BLUE_14 = atlas.findRegion("laserBlue14");
            LASER_BLUE_15 = atlas.findRegion("laserBlue15");
            LASER_BLUE_16 = atlas.findRegion("laserBlue16");

            // green lasers
            LASER_GREEN_01 = atlas.findRegion("laserGreen01");
            LASER_GREEN_02 = atlas.findRegion("laserGreen02");
            LASER_GREEN_03 = atlas.findRegion("laserGreen03");
            LASER_GREEN_04 = atlas.findRegion("laserGreen04");
            LASER_GREEN_05 = atlas.findRegion("laserGreen05");
            LASER_GREEN_06 = atlas.findRegion("laserGreen06");
            LASER_GREEN_07 = atlas.findRegion("laserGreen07");
            LASER_GREEN_08 = atlas.findRegion("laserGreen08");
            LASER_GREEN_09 = atlas.findRegion("laserGreen09");
            LASER_GREEN_10 = atlas.findRegion("laserGreen10");
            LASER_GREEN_11 = atlas.findRegion("laserGreen11");
            LASER_GREEN_12 = atlas.findRegion("laserGreen12");
            LASER_GREEN_13 = atlas.findRegion("laserGreen13");
            LASER_GREEN_14 = atlas.findRegion("laserGreen14");
            LASER_GREEN_15 = atlas.findRegion("laserGreen15");
            LASER_GREEN_16 = atlas.findRegion("laserGreen16");
        }
    }

    // hud assets
    public class AssetHUD {

        // player lives icons
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_1_BLUE;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_2_BLUE;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_3_BLUE;

        public final TextureAtlas.AtlasRegion PLAYER_LIFE_1_GREEN;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_2_GREEN;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_3_GREEN;

        public final TextureAtlas.AtlasRegion PLAYER_LIFE_1_ORANGE;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_2_ORANGE;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_3_ORANGE;

        public final TextureAtlas.AtlasRegion PLAYER_LIFE_1_RED;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_2_RED;
        public final TextureAtlas.AtlasRegion PLAYER_LIFE_3_RED;

        // player shield icons
        public final TextureAtlas.AtlasRegion BRONZE_SHIELD;
        public final TextureAtlas.AtlasRegion SILVER_SHIELD;
        public final TextureAtlas.AtlasRegion GOLD_SHIELD;

        public final TextureAtlas.AtlasRegion NUMERAL_X;

        // player shield
        public final TextureAtlas.AtlasRegion PLAYER_SHIELD_1;
        public final TextureAtlas.AtlasRegion PLAYER_SHIELD_2;
        public final TextureAtlas.AtlasRegion PLAYER_SHIELD_3;

        public final TextureAtlas.AtlasRegion ENEMY_SHIELD_1;
        public final TextureAtlas.AtlasRegion ENEMY_SHIELD_2;
        public final TextureAtlas.AtlasRegion ENEMY_SHIELD_3;

        // player dmg
        public final TextureAtlas.AtlasRegion PLAYERSHIP_01_DMG_01;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_01_DMG_02;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_01_DMG_03;

        public final TextureAtlas.AtlasRegion PLAYERSHIP_02_DMG_01;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_02_DMG_02;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_02_DMG_03;

        public final TextureAtlas.AtlasRegion PLAYERSHIP_03_DMG_01;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_03_DMG_02;
        public final TextureAtlas.AtlasRegion PLAYERSHIP_03_DMG_03;

        public AssetHUD(TextureAtlas atlas) {

            // player health icons
            PLAYER_LIFE_1_BLUE = atlas.findRegion("playerLife1_blue");
            PLAYER_LIFE_2_BLUE = atlas.findRegion("playerLife2_blue");
            PLAYER_LIFE_3_BLUE = atlas.findRegion("playerLife3_blue");

            PLAYER_LIFE_1_GREEN = atlas.findRegion("playerLife1_green");
            PLAYER_LIFE_2_GREEN = atlas.findRegion("playerLife2_green");
            PLAYER_LIFE_3_GREEN = atlas.findRegion("playerLife3_green");

            PLAYER_LIFE_1_ORANGE = atlas.findRegion("playerLife1_orange");
            PLAYER_LIFE_2_ORANGE = atlas.findRegion("playerLife2_orange");
            PLAYER_LIFE_3_ORANGE = atlas.findRegion("playerLife3_orange");

            PLAYER_LIFE_1_RED = atlas.findRegion("playerLife1_red");
            PLAYER_LIFE_2_RED = atlas.findRegion("playerLife1_red");
            PLAYER_LIFE_3_RED = atlas.findRegion("playerLife1_red");

            // numeralx
            NUMERAL_X = atlas.findRegion("numeralX");

            // shield icons
            BRONZE_SHIELD = atlas.findRegion("shield_bronze");
            SILVER_SHIELD = atlas.findRegion("shield_silver");
            GOLD_SHIELD = atlas.findRegion("shield_gold");

            // players shield
            PLAYER_SHIELD_1 = atlas.findRegion("shield1");
            PLAYER_SHIELD_2 = atlas.findRegion("shield2");
            PLAYER_SHIELD_3 = atlas.findRegion("shield3");

            // enemy shield
            ENEMY_SHIELD_1 = atlas.findRegion("enemyShield1");
            ENEMY_SHIELD_2 = atlas.findRegion("enemyShield2");
            ENEMY_SHIELD_3 = atlas.findRegion("enemyShield3");

            // player dmg
            PLAYERSHIP_01_DMG_01 = atlas.findRegion("playerShip1_damage1");
            PLAYERSHIP_01_DMG_02 = atlas.findRegion("playerShip1_damage2");
            PLAYERSHIP_01_DMG_03 = atlas.findRegion("playerShip1_damage3");

            PLAYERSHIP_02_DMG_01 = atlas.findRegion("playerShip2_damage1");
            PLAYERSHIP_02_DMG_02 = atlas.findRegion("playerShip2_damage2");
            PLAYERSHIP_02_DMG_03 = atlas.findRegion("playerShip2_damage3");

            PLAYERSHIP_03_DMG_01 = atlas.findRegion("playerShip3_damage1");
            PLAYERSHIP_03_DMG_02 = atlas.findRegion("playerShip3_damage2");
            PLAYERSHIP_03_DMG_03 = atlas.findRegion("playerShip3_damage3");
        }
    }

    public class AssetCoin {
        Texture texture;
        public final Animation<TextureRegion> coinAnimation;
        private float coinTimer;

        TextureRegion[][] textureRegion2D;
        TextureRegion[] textureRegion1D;

        public float x, y;
        float totalAnimationTime;

        public AssetCoin(AssetManager assetManager) {
            texture = assetManager.get("sprites/effects/coin_01.png", Texture.class);

            totalAnimationTime = 0.8f;

            // split texture
            textureRegion2D = TextureRegion.split(texture, 120, 120);

            // convert to 1D array
            textureRegion1D = new TextureRegion[8];

            int index = 0;

            for (int j = 0; j < 8; j++) { //column
                textureRegion1D[index] = textureRegion2D[0][j];
                index++;
            }

            coinAnimation = new Animation<TextureRegion>(totalAnimationTime / 8, textureRegion1D);
            coinAnimation.setPlayMode(Animation.PlayMode.LOOP);
            coinTimer = 0;
        }

        public void update(float deltaTime) {
            coinTimer += deltaTime;
        }

        public void setPos(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Batch batch) {
            batch.draw(coinAnimation.getKeyFrame(coinTimer),
                    x, y, 5, 6);
        }
    }

//    public class AssetExplosion {
//        Texture texture;
//        private Animation<TextureRegion> explosionAnimation;
//        private float explosionTimer;
//
//        public Rectangle boundingBox;
//
//        public AssetExplosion(AssetManager am) {
//            texture = am.get("sprites/effects/explosion.png", Texture.class);
//
//            // split texture
//            TextureRegion[][] textureRegion2D = TextureRegion.split(texture,64,64);
//
//            // convert to 1D array
//            TextureRegion[] textureRegion1D = new TextureRegion[16];
//            int index = 0;
//
//            for(int i = 0; i < 4; i++) {
//                for(int j = 0; j < 4; j++) {
//                    textureRegion1D[index] = textureRegion2D[i][j];
//                    index++;
//                }
//            }
//
//            float totalAnimationTime = 0.7f;
//            explosionAnimation = new Animation<TextureRegion>(totalAnimationTime /16,textureRegion1D);
//            explosionTimer = 0;
//        }
//
//        public void update(float deltaTime) {explosionTimer += deltaTime;}
//
//        public void draw(Batch batch) {
//            batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
//                    boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
//        }
//
//        public void setBoundingBox(Rectangle boundingBox) {
//            this.boundingBox = boundingBox;
//        }
//
//        public boolean isFinished() {return explosionAnimation.isAnimationFinished(explosionTimer);}
//    }
}




































