package com.garcia.starshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.garcia.starshooter.game.objects.StarshooterGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 1280;
		config.width = 720;


		new LwjglApplication(new StarshooterGame(), config);
	}
}
