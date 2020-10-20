package com.garcia.starshooter.game.objects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.garcia.starshooter.game.objects.Assets;
import com.garcia.starshooter.game.objects.AudioManager;
import com.garcia.starshooter.game.objects.StarshooterGame;
import com.garcia.starshooter.game.objects.effects.Coin;
import com.garcia.starshooter.game.objects.ships.player.PlayerShip;

import java.util.Locale;

import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_HEIGHT;
import static com.garcia.starshooter.game.objects.StarshooterGame.WORLD_WIDTH;

public class UpgradeScreen extends ScreenTemplate { // checking if it can even be upgraded based on other factors -- once stats are max, upgrade ship for more stats and new skin

    Stage stage;

    TextButtonStyle titleStyle, textStyle;
    TextButtonStyle addDmgStyle, addHealthStyle, addShieldStyle, addSpeedStyle, addLaserSpeedStyle, addFireRateStyle;
    TextButtonStyle upgradeShipButtonStyle;
    TextButtonStyle menuButtonStyle;
    TextButtonStyle playButtonStyle;

    TextButton titleLabel;
    TextButton ptsLabel;
    TextButton addDmg, addHealth, addShield, addMovementSpeed, addLaserSpeed, addFireRate;
    TextButton upgradeShipButton;
    TextButton menuButton;
    TextButton playButton;

    Coin coinAnimation; // beta branch

    private int shipLevel;  // current ship level ... changes--texture, max stats,
    private int shipLevelMultiplier; //

    private int dmgCost, healthCost, shieldCost, speedCost, laserSpeedCost, fireRateCost; // current cost of stat
    private Color dmgColor = Color.WHITE, healthColor = Color.WHITE, shieldColor = Color.WHITE, speedColor = Color.WHITE, laserColor = Color.WHITE, fireRateColor = Color.WHITE; // color of stat values for when stat is max
    private String deductedPtsColor = "#FF0000";

    private int dmgMultiplier = 125, healthMultiplier = 125, shieldMultiplier = 100, speedMultiplier = 110,
            laserSpeedMultiplier = 130, fireRateMultiplier = 150; // cost * stat level = cost of stat

    private int dmgMax = 7, healthMax = 7, shieldMax = 7, speedMax = 7, laserSpeedMax = 7, fireRateMax = 7;
    // 6 upgrabale levels for each stat except shield which starts at 0

    PlayerShip playerShip;
    int deductedPts;

    BitmapFont titleFont, buttonFont, hudFont;

    boolean dmgHover, shieldHover, healthHover, speedHover, laserHover, fireRateHover;
    boolean shipMaxSound = false;

    public UpgradeScreen(StarshooterGame game) {
        super(game);

        stage = new Stage(viewport, game.batch);

        titleFont = game.titleFont;
        buttonFont = game.buttonFont;
        hudFont = game.hudFont;

        playerShip = game.getPlayerShip();
    }

    @Override
    public void show() {
        super.show();

        AudioManager.instance.play(Assets.instance.music.menuMusic);

        Gdx.input.setInputProcessor(stage);

        styles();
        buttons();
        listeners();

        coinAnimation = new Coin(ptsLabel.getX() - 2, ptsLabel.getY());

        stage.addActor(menuButton);
        stage.addActor(playButton);
        stage.addActor(titleLabel);
        stage.addActor(ptsLabel);
        stage.addActor(addHealth);
        stage.addActor(addDmg);
        stage.addActor(addShield);
        stage.addActor(addMovementSpeed);
        stage.addActor(addLaserSpeed);
        stage.addActor(addFireRate);
        stage.addActor(upgradeShipButton);
    }

    private void styles() {
        textStyle = new TextButtonStyle();
        textStyle.font = hudFont;
        textStyle.fontColor = Color.WHITE;

        titleStyle = new TextButtonStyle();
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.WHITE;

        addDmgStyle = new TextButtonStyle();
        addDmgStyle.font = buttonFont;
        addDmgStyle.fontColor = Color.WHITE;

        addHealthStyle = new TextButtonStyle();
        addHealthStyle.font = buttonFont;
        addHealthStyle.fontColor = Color.WHITE;

        addShieldStyle = new TextButtonStyle();
        addShieldStyle.font = buttonFont;
        addShieldStyle.fontColor = Color.WHITE;

        addSpeedStyle = new TextButtonStyle();
        addSpeedStyle.font = buttonFont;
        addSpeedStyle.fontColor = Color.WHITE;

        addLaserSpeedStyle = new TextButtonStyle();
        addLaserSpeedStyle.font = buttonFont;
        addLaserSpeedStyle.fontColor = Color.WHITE;

        addFireRateStyle = new TextButtonStyle();
        addFireRateStyle.font = buttonFont;
        addFireRateStyle.fontColor = Color.WHITE;

        upgradeShipButtonStyle = new TextButtonStyle();
        upgradeShipButtonStyle.font = titleFont;
        upgradeShipButtonStyle.fontColor = Color.GOLD;

        playButtonStyle = new TextButtonStyle();
        playButtonStyle.font = buttonFont;
        playButtonStyle.fontColor = Color.WHITE;

        menuButtonStyle = new TextButtonStyle();
        menuButtonStyle.font = buttonFont;
        menuButtonStyle.fontColor = Color.WHITE;
    }

    private void buttons() {
        // title label --------
        titleLabel = new TextButton("UPGRADES", titleStyle);
        titleLabel.setPosition((WORLD_WIDTH - titleLabel.getWidth()) / 2, WORLD_HEIGHT - titleFont.getCapHeight() - 10);
        // --------------------

        // pts label --------
        ptsLabel = new TextButton("", textStyle);
        ptsLabel.setPosition((WORLD_WIDTH - ptsLabel.getWidth()) / 2 - 7, titleLabel.getY() - 10);
        // --------------------

        // dmg label ----------
        addDmg = new TextButton("ATTACK", addDmgStyle); // left ship
        addDmg.setPosition(((WORLD_WIDTH - addDmg.getWidth()) / 2), ptsLabel.getY() - hudFont.getCapHeight() - 15);

        // health label ----------
        addHealth = new TextButton("HEALTH", addHealthStyle); //above ship
        addHealth.setPosition(((WORLD_WIDTH - addHealth.getWidth()) / 2) - 22, addDmg.getY() - 18);

        // shield label ----------
        addShield = new TextButton("SHIELD", addShieldStyle); //right ship
        addShield.setPosition(((WORLD_WIDTH - addShield.getWidth()) / 2) + 22, addHealth.getY());

        // speed label ----------
        addMovementSpeed = new TextButton("SPEED", addSpeedStyle); //above ship
        addMovementSpeed.setPosition(((WORLD_WIDTH - addMovementSpeed.getWidth()) / 2) - 22, addHealth.getY() - 22);

        // laser speed label ----------
        addLaserSpeed = new TextButton("LASER", addLaserSpeedStyle); //above ship
        addLaserSpeed.setPosition(((WORLD_WIDTH - addLaserSpeed.getWidth()) / 2) + 22, addMovementSpeed.getY());

        // fire rate label ----------
        addFireRate = new TextButton("FIRERATE", addFireRateStyle); //above ship
        addFireRate.setPosition(((WORLD_WIDTH - addFireRate.getWidth()) / 2), addLaserSpeed.getY() - 20);

        // menu button
        menuButton = new TextButton("MENU", menuButtonStyle);
        menuButton.setPosition(WORLD_WIDTH * 0.05f, WORLD_HEIGHT - WORLD_HEIGHT / 4);

        // play button
        playButton = new TextButton("PLAY", playButtonStyle);
        playButton.setPosition(WORLD_WIDTH - menuButton.getX() - playButton.getWidth(), WORLD_HEIGHT - WORLD_HEIGHT / 4);

        // upgrade ship button
        upgradeShipButton = new TextButton("UPGRADE\nSHIP", upgradeShipButtonStyle);
        upgradeShipButton.setPosition((WORLD_WIDTH - upgradeShipButton.getWidth()) / 2, (WORLD_HEIGHT / 2) - 20);

    }

    private void updateStatLabels() {
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "%04d", game.pts), ptsLabel.getX() + 3, ptsLabel.getY() + 6);

        // deducted pts value
        if (dmgHover || healthHover || shieldHover || speedHover || laserHover || fireRateHover) {
            hudFont.draw(game.batch, String.format(Locale.getDefault(), "["+ deductedPtsColor +"]- %04d", deductedPts), ptsLabel.getX(), ptsLabel.getY());
        }

        // update costs
        dmgCost = playerShip.dmgStat * dmgMultiplier;
        healthCost = playerShip.healthStat * healthMultiplier;
        shieldCost = (playerShip.shieldStat + 1 )* shieldMultiplier;
        speedCost = playerShip.speedStat * speedMultiplier;
        laserSpeedCost = playerShip.laserStat * laserSpeedMultiplier;
        fireRateCost = playerShip.fireRateStat * fireRateMultiplier;

        // current dmg stat
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + dmgColor + "]%01d", playerShip.dmgStat), (addDmg.getWidth() / 2) + addDmg.getX() - 1, addDmg.getY() - 1);
        if (dmgHover && game.pts >= dmgCost) {
            hudFont.draw(game.batch, "[#008800]+", addDmg.getX() + 5, addDmg.getY() - 1);
        }
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + healthColor + "]%01d", playerShip.healthStat), (addHealth.getWidth() / 2) + addHealth.getX() - 1, addHealth.getY() - 1);
        if (healthHover && game.pts >= healthCost) {
            hudFont.draw(game.batch, "[#008800]+", addHealth.getX() + 4, addHealth.getY() - 1);
        }
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + shieldColor + "]%01d", playerShip.shieldStat), (addShield.getWidth() / 2) + addShield.getX() - 1, addShield.getY() - 1);
        if (shieldHover && game.pts >= shieldCost) {
            hudFont.draw(game.batch, "[#008800]+", addShield.getX() + 3, addShield.getY() - 1);
        }
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + speedColor + "]%01d", playerShip.speedStat), (addMovementSpeed.getWidth() / 2) + addMovementSpeed.getX() - 1, addMovementSpeed.getY() - 1);
        if (speedHover && game.pts >= speedCost) {
            hudFont.draw(game.batch, "[#008800]+", addMovementSpeed.getX() + 3, addMovementSpeed.getY() - 1);
        }
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + laserColor + "]%01d", playerShip.laserStat), (addLaserSpeed.getWidth() / 2) + addLaserSpeed.getX() - 1, addLaserSpeed.getY() - 1);
        if (laserHover && game.pts >= laserSpeedCost) {
            hudFont.draw(game.batch, "[#008800]+", addLaserSpeed.getX() + 3, addLaserSpeed.getY() - 1);
        }
        hudFont.draw(game.batch, String.format(Locale.getDefault(), "[#" + fireRateColor + "]%01d", playerShip.fireRateStat), (addFireRate.getWidth() / 2) + addFireRate.getX() - 1, addFireRate.getY() - 1);
        if (fireRateHover && game.pts >= fireRateCost) {
            hudFont.draw(game.batch, "[#008800]+", addFireRate.getX() + 7, addFireRate.getY() - 1);
        }

        isShipMaxxed(); // check if ship is maxxed
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        stage.act();
        stage.draw();

        game.batch.begin();

        // animation
        coinAnimation.update(deltaTime);
        coinAnimation.draw(game.batch);

        // data
        updateStatLabels();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void listeners() {

        // menu
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.uiClick);
                game.setScreen(new TitleScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuButtonStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuButtonStyle.fontColor = Color.WHITE;
            }
        });

        // play
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.instance.play(Assets.instance.sounds.gameStart);
                game.setScreen(new GameScreen(game));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                playButtonStyle.fontColor = Color.RED;
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                playButtonStyle.fontColor = Color.WHITE;
            }
        });

        // upgrade ship button
        upgradeShipButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                upgradeShipButtonStyle.fontColor = Color.WHITE;

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                upgradeShipButtonStyle.fontColor = Color.GOLD;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                game.upgradeShip();
            }
        });


        // dmg
        addDmg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playerShip.dmgStat < dmgMax) { // if max stat has not been reached yet
                    if (game.pts >= dmgCost) {  // if there are enough pts
                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeDmg();
                        game.pts -= dmgCost;

                        if (playerShip.dmgStat == dmgMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // max stat reached
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play max stat error sound
                    }// no points
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play no money error sound
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.dmgStat < dmgMax) {
                    if (game.pts >= dmgCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addDmgStyle.fontColor = Color.GREEN;
                        dmgColor = Color.GREEN;
                    } else {
                        addDmgStyle.fontColor = Color.GRAY;
                        dmgColor = Color.GRAY;
                    }
                    deductedPts = dmgCost; // set deducted

                    dmgHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addDmgStyle.fontColor = Color.WHITE;
                    dmgColor = Color.WHITE;
                    dmgHover = false;
                }
            }
        });

        // health
        addHealth.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerShip.healthStat < healthMax) { // if max stat has not been reached yet
                    if (game.pts >= healthCost) {  // if there are enough pts
                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeHealth();
                        game.pts -= healthCost;

                        if (playerShip.healthStat == healthMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // max stat reached
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play max stat error sound
                    }
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play no money error sound
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.healthStat < healthMax) {
                    if (game.pts >= healthCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addHealthStyle.fontColor = Color.GREEN;
                        healthColor = Color.GREEN;
                    } else {
                        addHealthStyle.fontColor = Color.GRAY;
                        healthColor = Color.GRAY;
                    }
                    deductedPts = healthCost; // set deducted

                    healthHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addHealthStyle.fontColor = Color.WHITE;
                    healthColor = Color.WHITE;
                    healthHover = false;
                }

            }
        });

        // shield
        addShield.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerShip.shieldStat < shieldMax) { // if max stat has not been reached yet
                    if (game.pts >= shieldCost) {  // if there are enough pts
                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeShield();
                        game.pts -= shieldCost;

                        if (playerShip.shieldStat == shieldMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // max stat reached
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play max stat error sound
                    }
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play no money error sound
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.shieldStat < shieldMax) {
                    if (game.pts >= shieldCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addShieldStyle.fontColor = Color.GREEN;
                        shieldColor = Color.GREEN;
                    } else {
                        addShieldStyle.fontColor = Color.GRAY;
                        shieldColor = Color.GRAY;
                    }
                    deductedPts = shieldCost; // set deducted

                    shieldHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addShieldStyle.fontColor = Color.WHITE;
                    shieldColor = Color.WHITE;
                    shieldHover = false;
                }

            }
        });

        // speed
        addMovementSpeed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerShip.speedStat < speedMax) { // if max stat has not been reached yet
                    if (game.pts >= speedCost) {  // if there are enough pts
                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeSpeed();
                        game.pts -= speedCost;

                        if (playerShip.speedStat == speedMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // max stat reached
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play max stat error sound
                    }
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play no money error sound
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.speedStat < speedMax) {
                    if (game.pts >= speedCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addSpeedStyle.fontColor = Color.GREEN;
                        speedColor = Color.GREEN;
                    } else {
                        addSpeedStyle.fontColor = Color.GRAY;
                        speedColor = Color.GRAY;
                    }
                    deductedPts = speedCost; // set deducted

                    speedHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addSpeedStyle.fontColor = Color.WHITE;
                    speedColor = Color.WHITE;
                    speedHover = false;
                }

            }
        });

        // laser speed
        addLaserSpeed.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerShip.laserStat < laserSpeedMax) { // if max stat has not been reached yet
                    if (game.pts >= laserSpeedCost) {  // if there are enough pts

                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeLaser();
                        game.pts -= laserSpeedCost;

                        if (playerShip.laserStat == laserSpeedMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // no pts
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play no money error sounds
                    }
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play max stat error sounds
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.laserStat < laserSpeedMax) {
                    if (game.pts >= laserSpeedCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addLaserSpeedStyle.fontColor = Color.GREEN;
                        laserColor = Color.GREEN;
                    } else {
                        addLaserSpeedStyle.fontColor = Color.GRAY;
                        laserColor = Color.GRAY;
                    }
                    deductedPts = laserSpeedCost; // set deducted

                    laserHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addLaserSpeedStyle.fontColor = Color.WHITE;
                    laserColor = Color.WHITE;
                    laserHover = false;
                }

            }
        });

        // firerate
        addFireRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (playerShip.fireRateStat < fireRateMax) { // if max stat has not been reached yet
                    if (game.pts >= fireRateCost) {  // if there are enough pts
                        AudioManager.instance.play(Assets.instance.sounds.statUpgrade);

                        playerShip.upgradeFireRate();
                        game.pts -= fireRateCost;

                        if (playerShip.fireRateStat == fireRateMax) AudioManager.instance.play(Assets.instance.sounds.statReachedMax);
                    } else { // max stat reached
                        AudioManager.instance.play(Assets.instance.sounds.uiCantSelect); // play max stat error sound
                    }
                } else { // max stat reached
                    AudioManager.instance.play(Assets.instance.sounds.statIsMax); // play no money error sound
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (playerShip.fireRateStat < fireRateMax) {
                    if (game.pts >= fireRateCost) {
                        AudioManager.instance.play(Assets.instance.sounds.uiRollover);
                        addFireRateStyle.fontColor = Color.GREEN;
                        fireRateColor = Color.GREEN;
                    } else {
                        addFireRateStyle.fontColor = Color.GRAY;
                        fireRateColor = Color.GRAY;
                    }
                    deductedPts = fireRateCost; // set deducted

                    fireRateHover = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                if (pointer == -1) {
                    addFireRateStyle.fontColor = Color.WHITE;
                    fireRateColor = Color.WHITE;
                    fireRateHover = false;
                }

            }
        });
    }

    private void isShipMaxxed() {

        if (playerShip.dmgStat == dmgMax) {
            addDmgStyle.fontColor = Color.GOLD;
            dmgColor = Color.GOLD;
        }
        if (playerShip.healthStat == healthMax) {
            addHealthStyle.fontColor = Color.GOLD;
            healthColor = Color.GOLD;
        }
        if (playerShip.shieldStat == shieldMax) {
            addShieldStyle.fontColor = Color.GOLD;
            shieldColor = Color.GOLD;
        }
        if (playerShip.speedStat == speedMax) {
            addSpeedStyle.fontColor = Color.GOLD;
            speedColor = Color.GOLD;
        }
        if (playerShip.laserStat == laserSpeedMax) {
            addLaserSpeedStyle.fontColor = Color.GOLD;
            laserColor = Color.GOLD;
        }
        if (playerShip.fireRateStat == fireRateMax) {
            addFireRateStyle.fontColor = Color.GOLD;
            fireRateColor = Color.GOLD;
        }


        if(playerShip.dmgStat == dmgMax && playerShip.healthStat == healthMax &&
                playerShip.shieldStat == shieldMax && playerShip.speedStat == speedMax &&
                playerShip.laserStat == laserSpeedMax && playerShip.fireRateStat == fireRateMax) {
            upgradeShipButton.setVisible(true);
            if(shipMaxSound == false) {
                AudioManager.instance.play(Assets.instance.sounds.allStatsMax);
                shipMaxSound = true;
            }
        } else upgradeShipButton.setVisible(false);

    }
}
