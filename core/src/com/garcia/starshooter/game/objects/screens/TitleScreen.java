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
import com.garcia.starshooter.game.objects.effects.Coin;

import java.util.Locale;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class TitleScreen extends ScreenTemplate {

    BitmapFont titleFont, buttonFont, hudFont;

    Stage stage;
    Table table;

    TextButtonStyle titleStyle;
    TextButtonStyle hudStyle;
    TextButtonStyle playButtonStyle;
    TextButtonStyle upgradeButtonStyle;
    TextButtonStyle statsButtonStyle;
    TextButtonStyle exitButtonStyle;
    TextButtonStyle scoreButtonStyle;

    TextButton titleLabel;

    TextButton ptsLabel;

    TextButton playButton;
    TextButton upgradeButton;
    TextButton statsButton; // player statistics
    TextButton exitButton;

//    Coin coin;

    public TitleScreen(StarshooterGame game) {
        super(game);

        stage = new Stage(viewport, game.batch);

        titleFont = game.titleFont;
        buttonFont = game.buttonFont;
        hudFont = game.hudFont;
    }

    @Override
    public void show() {
        AudioManager.instance.play(Assets.instance.music.menuMusic);

        Gdx.input.setInputProcessor(stage);

        buttons();

//        coin = new Coin(ptsLabel.getX() - 5, ptsLabel.getY());

        Assets.instance.coin.setPos(ptsLabel.getX() - 5, ptsLabel.getY());

        table.add(playButton);
        table.row();
        table.add(upgradeButton);
        table.row();
        table.add(exitButton);

        stage.addActor(titleLabel);
        stage.addActor(ptsLabel);
        stage.addActor(table);
    }

    private void buttons() { // add settings menu

        titleStyle = new TextButtonStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.WHITE;

        hudStyle = new TextButtonStyle();
        hudStyle.font = hudFont;
        hudStyle.fontColor = Color.WHITE;

        playButtonStyle = new TextButtonStyle();
        playButtonStyle.font = buttonFont;
        playButtonStyle.fontColor = Color.WHITE;

        upgradeButtonStyle = new TextButtonStyle();
        upgradeButtonStyle.font = buttonFont;
        upgradeButtonStyle.fontColor = Color.WHITE;

        exitButtonStyle = new TextButtonStyle();
        exitButtonStyle.font = buttonFont;
        exitButtonStyle.fontColor = Color.WHITE;

        scoreButtonStyle = new TextButtonStyle();
        scoreButtonStyle.font = hudFont;
        scoreButtonStyle.fontColor = Color.RED;

        titleLabel = new TextButton("STAR\nSHOOTER", titleStyle); // title label
        titleLabel.setPosition((WORLD_WIDTH - titleLabel.getWidth()) / 2, WORLD_HEIGHT - WORLD_HEIGHT / 3);

        ptsLabel = new TextButton(String.format(Locale.getDefault(), "%04d", game.pts), hudStyle); // score value label
        ptsLabel.setPosition(WORLD_WIDTH * 0.12f, WORLD_HEIGHT - hudFont.getCapHeight() - 5);

        playButton = new TextButton("PLAY", playButtonStyle); // play button
        upgradeButton = new TextButton("UPGRADES", upgradeButtonStyle); // play button
        exitButton = new TextButton("QUIT", exitButtonStyle); // exit button

        // input listeners
        // play button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.gameStart);
                if(!game.hasPlayed) game.setPlayedTrue();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                playButtonStyle.fontColor = Color.RED;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                playButtonStyle.fontColor = Color.WHITE;
            }
        });

        // upgrade button
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(game.hasPlayed) {
                    AudioManager.instance.play(Assets.instance.sounds.uiClick);
                    game.setScreen(new UpgradeScreen(game));
                } else {
                    AudioManager.instance.play(Assets.instance.sounds.uiCantSelect);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if(game.hasPlayed) {
                    upgradeButtonStyle.fontColor = Color.RED;
                    AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                } else {
                    upgradeButtonStyle.fontColor = Color.GRAY;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                upgradeButtonStyle.fontColor = Color.WHITE;
            }
        });

        // exit button
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                exitButtonStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                exitButtonStyle.fontColor = Color.WHITE;
            }
        });


        table = new Table();
        table.setFillParent(true); // fills stage
        table.center();
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);
        stage.act();
        stage.draw();

        game.batch.begin();
//        coin.update(deltaTime);
//        coin.draw(game.batch);

        Assets.instance.coin.update(deltaTime);
        Assets.instance.coin.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

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
        stage.dispose();
    }
}
