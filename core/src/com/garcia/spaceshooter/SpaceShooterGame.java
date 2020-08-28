package com.garcia.spaceshooter;

import com.badlogic.gdx.Game;

import java.util.Random;

public class SpaceShooterGame extends Game {

//	TitleScreen titleScreen;
	GameScreen gameScreen;
	GameOverScreen gameOverScreen;

	public static Random random = new Random(); // SpaceShooterGame.random

	@Override
	public void create () {
		gameScreen = new GameScreen(this); // create gameScreen
		setScreen(gameScreen); // set screen
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height); // resize gameScreen on desktop if window is resized
	}
}
