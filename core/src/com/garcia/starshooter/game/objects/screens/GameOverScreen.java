package com.garcia.starshooter.game.objects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.StarshooterGame;

import java.util.Locale;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class GameOverScreen extends ScreenTemplate { // re write with buttons and same options as title screen upgrade screen

    Stage stage;
    Table table;

    TextButtonStyle waveCountStyle, enemyCountStyle, ptsStyle;
    TextButton waveCountLabel, enemyCountLabel, prevPtsLabel, ptsEarnedLabel;

    TextButtonStyle titleStyle, retryStyle, upgradeStyle, menuStyle, quitStyle;
    TextButton titleLabel, retryButton, upgradeButton, menuButton, quitButton;

    int waveCount, enemiesDestroyed, ptsEarned, prevPts;

    BitmapFont titleFont, hudFont, buttonFont;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudRow3Y, hudRow4Y, hudSectionWidth;

    public GameOverScreen(StarshooterGame game, int wave, int enemies, int prevPts) {
        super(game);

        titleFont = game.titleFont;
        buttonFont = game.buttonFont;
        hudFont = game.hudFont;

        this.waveCount = wave;
        this.enemiesDestroyed = enemies;
        this.ptsEarned = game.pts - prevPts; // current pts - pts before last game = ptsEarned
        this.prevPts = prevPts;

        stage = new Stage(viewport, game.batch);

    }

    @Override
    public void show() {
        // lost snd
        Gdx.input.setInputProcessor(stage);

        AudioManager.instance.play(Assets.instance.music.gameOverMusic);

        table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.padBottom(20);

        titleStyle = new TextButtonStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.WHITE;

        waveCountStyle = new TextButtonStyle();
        waveCountStyle.font = hudFont;
        waveCountStyle.fontColor = Color.WHITE;

        ptsStyle = new TextButtonStyle();
        ptsStyle.font = hudFont;
        ptsStyle.fontColor = Color.WHITE;

        enemyCountStyle = new TextButtonStyle();
        enemyCountStyle.font = hudFont;
        enemyCountStyle.fontColor = Color.WHITE;

        retryStyle = new TextButtonStyle();
        retryStyle.font = buttonFont;
        retryStyle.fontColor = Color.WHITE;

        upgradeStyle = new TextButtonStyle();
        upgradeStyle.font = buttonFont;
        upgradeStyle.fontColor = Color.WHITE;

        menuStyle = new TextButtonStyle();
        menuStyle.font = buttonFont;
        menuStyle.fontColor = Color.WHITE;

        quitStyle = new TextButtonStyle();
        quitStyle.font = buttonFont;
        quitStyle.fontColor = Color.WHITE;

        titleLabel = new TextButton("[#FF0000]GAMEOVER", titleStyle);
        titleLabel.setPosition((WORLD_WIDTH - titleLabel.getWidth()) / 2, WORLD_HEIGHT - WORLD_HEIGHT / 5);

        waveCountLabel = new TextButton(String.format(Locale.getDefault(), "WAVE | [#FF0000]%02d", waveCount), waveCountStyle);
        waveCountLabel.setPosition((WORLD_WIDTH - waveCountLabel.getWidth()) / 2, titleLabel.getY() - 15);

        enemyCountLabel = new TextButton(String.format(Locale.getDefault(), "ENEMIES | [#FF0000]%02d", enemiesDestroyed), waveCountStyle);
        enemyCountLabel.setPosition((WORLD_WIDTH - enemyCountLabel.getWidth()) / 2, waveCountLabel.getY() - 10);

        prevPtsLabel = new TextButton(String.format(Locale.getDefault(), "%04d", prevPts), waveCountStyle);
        prevPtsLabel.setPosition((WORLD_WIDTH - prevPtsLabel.getWidth()) / 2, enemyCountLabel.getY() - 10);
        Assets.instance.coin.setPos(prevPtsLabel.getX() - 5, prevPtsLabel.getY());

        ptsEarnedLabel = new TextButton(String.format(Locale.getDefault(), "[#008800]+ %04d", ptsEarned), waveCountStyle);
        ptsEarnedLabel.setPosition(prevPtsLabel.getX() - 3, prevPtsLabel.getY() - 6);

        retryButton = new TextButton("REPLAY", retryStyle);
        upgradeButton = new TextButton("UPGRADES", upgradeStyle);
        menuButton = new TextButton("MENU", menuStyle);
        quitButton = new TextButton("QUIT", quitStyle);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.uiClick);
                game.setScreen(new TitleScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuStyle.fontColor = Color.WHITE;
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.gameStart);
                game.setScreen(new GameScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                retryStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                retryStyle.fontColor = Color.WHITE;
            }
        });

        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.uiClick);
                game.setScreen(new UpgradeScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                upgradeStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                upgradeStyle.fontColor = Color.WHITE;
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.uiClick);
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                quitStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                quitStyle.fontColor = Color.WHITE;
            }
        });

        table.add(menuButton);
        table.row();
        table.add(retryButton);
        table.row();
        table.add(upgradeButton);
        table.row();
        table.add(quitButton);

        stage.addActor(titleLabel);

        stage.addActor(waveCountLabel);
        stage.addActor(enemyCountLabel);
        stage.addActor(ptsEarnedLabel);
        stage.addActor(prevPtsLabel);
        stage.addActor(table);
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);
        stage.act();
        stage.draw();

        game.batch.begin();
        Assets.instance.coin.update(deltaTime);
        Assets.instance.coin.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
